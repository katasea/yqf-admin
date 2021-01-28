package com.common.auto.db;

import com.common.ClassTools;
import com.common.CommonUtil;
import com.common.Global;
import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.main.dao.platform.CommonDao;
import com.main.pojo.platform.StateInfo;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自动扫描pojo包下的实体类，进行注解解析生成表结构语句并维护表结构
 *
 * @author chenfuqiang
 */
@Service("autoFixTable")
public class AutoFixTableMySQLService implements AutoFixTable {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private CommonDao commonDao;
	@Resource
	private DataSource secondDatasource;
	@Value("${org.quartz.dataSource.qzDS.URL}")
	private String path;
	private String pack = "com.main.pojo";

	@Override
    public StateInfo run(int year) {
        long begin = System.currentTimeMillis();
        logger.info("---自动修复报表数据库表结构开始---");
        /*
	     * 要扫描的model所在的pack
	     */
	    Set<Class<?>> classes = ClassTools.getClasses(pack);
	    StateInfo stateInfo = dealClassSQLMySQL(classes, year);
        long end = System.currentTimeMillis();
        logger.info("---自动修复报表数据库表结构结束---");
        logger.info("修复结构花费时间： " + ((end - begin) * 0.001) + " 秒");
        return stateInfo;
    }

    private StateInfo dealClassSQLMySQL(Set<Class<?>> classes, int year) {

		String schema = path.substring(0,path.indexOf("?"));
		schema = schema.substring(schema.lastIndexOf("/")+1);

        //mysql创建存储过程
        String proc = "DROP PROCEDURE IF EXISTS `columnFixProc`;\n" +
		        "CREATE PROCEDURE `columnFixProc`(IN tableName VARCHAR(50), IN cloName VARCHAR(50), IN flag VARCHAR(50),\n" +
		        "                                 IN oth       VARCHAR(50), IN type VARCHAR(50))\n" +
		        "  BEGIN\n" +
		        "    SET @num = 0, @qqq = '''';\n" +
		        "    SELECT count(1)\n" +
		        "    INTO @num\n" +
		        "    FROM information_schema.columns\n" +
		        "    WHERE TABLE_NAME = tableName AND COLUMN_NAME = cloName;\n" +
		        "    IF @num = 1\n" +
		        "    THEN\n" +
		        "      SET @qqq = CONCAT(' ALTER TABLE `',tableName,'` modify COLUMN `',cloName,'` ', type, '', oth);\n" +
		        "    ELSE\n" +
		        "      SET @qqq = CONCAT(' ALTER TABLE `',tableName,'` ADD `',cloName,'` ', type, '' '', oth);\n" +
		        "    END IF;\n" +
		        "    PREPARE stmt FROM @qqq;\n" +
		        "    EXECUTE stmt;\n" +
		        "  END";

        commonDao.executeSQL(proc);

	    StateInfo businessStateDTO = new StateInfo();
        List<String> allSql = new ArrayList<>();
        StringBuilder mainSql = new StringBuilder();
        StringBuilder pkSql = new StringBuilder();
        List<String> pkLs = new ArrayList<>();

        //for循环class，判断表是否存在，存在拼接建表sql，不存在拼接调用存储过程的sql
        for (Class<?> clas : classes) {
            mainSql.setLength(0);
            pkSql.setLength(0);
            pkLs.clear();
            Table table = clas.getAnnotation(Table.class);
            if (table == null) {
                continue;
            }
            //取出实体类里的字段定义
            Field[] fields = clas.getDeclaredFields();
            //这里支持集成的父类，要支持只要把下面的fields 附加到子类的fields即可。
            if (table.includeSupperClass()) {
                if (clas.getSuperclass() != null) {
                    Class<?> clsSup = clas.getSuperclass();
                    fields = (Field[]) ArrayUtils.addAll(fields, clsSup.getDeclaredFields());
                }
            }
            String tablename = table.name();
            if (CommonUtil.isEmpty(tablename)) {
                tablename = clas.getSimpleName();
            }
            tablename = tablename.toUpperCase().replace("@YEAR", String.valueOf(year));
            String existSql = "SELECT COUNT(1) FROM information_schema.`TABLES` WHERE TABLE_NAME='" + tablename + "' AND TABLE_SCHEMA = '"+schema+"'";
            int exist = commonDao.executeForNum(existSql);
            if (exist == 0) {
                //表不存在创建表
                mainSql.append("CREATE TABLE `").append(tablename).append("` (");
            }
            int x = 0;
            List<Field> needField = new ArrayList<>();
            //先过滤掉不要的column
            for(Field field : fields) {
	            Column column = field.getAnnotation(Column.class);
	            if (column != null) {//serialVersionUID没有column属性，跳过
	            	needField.add(field);
	            }
            }
            for (Field field : needField) {
                Column column = field.getAnnotation(Column.class);
                String columnname = CommonUtil.isEmpty(column.name()) ? field.getName() : column.name();
                columnname = columnname.toUpperCase();//字段名
                String flag = column.flag().toUpperCase();//PRIMARY，标识是否主键
                String oth = column.oth().toUpperCase();//NOT NULL/NULL，是否为空
                String type = column.type().trim().toUpperCase();//identity(1,1)，字段类型
                type = type.replace("VARCHAR2", "VARCHAR").replace("CLOB", "TEXT");
                type = type.replace("NUMBER", "INT");
                if ("PRIMARY".equals(flag)) {
                    pkLs.add(columnname);
                }
                if (exist == 0) {//表存在
                    mainSql.append("`").append(columnname).append("` ").append(type).append(" ").append(oth).append(",");
	                if (x == needField.size() - 1) {
                        if (pkLs.size() > 0) {
                            mainSql.append("primary key(");
                            for (String pk : pkLs) {
                                mainSql.append("`").append(pk).append("`,");
                            }
                            mainSql.deleteCharAt(mainSql.length() - 1);//删掉主键语句里的最后一个","
                            mainSql.append(")");
                        } else {
                            mainSql.deleteCharAt(mainSql.length() - 1);//删掉建表语句里的最后一个","
                        }
                        mainSql.append(");");
                    }
                } else {//表不存在
                    //调用存储过程，逻辑在存储过程中
	                String tempStr = "CALL columnFixProc('" + tablename
			                + "','" + columnname
			                + "','" + flag
			                + "','" + oth
			                + "','" + type
			                + "');";
                    mainSql.append(tempStr);
                    if (x == fields.length - 1 && pkLs.size() > 0) {//主键语句
                        pkSql.append("ALTER TABLE `").append(tablename).append("` DROP PRIMARY KEY ,ADD PRIMARY KEY (");
                        for (String pk : pkLs) {
                            pkSql.append("`").append(pk).append("`,");
                        }
                        pkSql.deleteCharAt(pkSql.length() - 1);//删掉primary key语句里的最后一个","
                        pkSql.append(");");
                    }
                }
                x++;
            }
            allSql.add(mainSql.toString() + " " + pkSql.toString());
        }
        try {
            if (allSql.size() > 0) {
                logger.info("--维护表字段开始--");
                for (String sql : allSql) {
                	if(CommonUtil.isNotEmpty(sql))
                    commonDao.executeSQL(sql);
                }
            }
        } catch (Exception e) {
	        businessStateDTO.setFlag(false);
	        businessStateDTO.setMsg(this.getClass(),e.getMessage(),e);
        }
        return businessStateDTO;
    }

}
