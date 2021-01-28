package com.common.auto.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import javax.sql.DataSource;
import com.common.Global;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.common.ArrayUtils;
import com.common.ClassTools;
import com.common.CommonUtil;
import com.main.dao.platform.CommonDao;
import com.main.pojo.platform.StateInfo;

/**
 * 自动扫描pojo包下的实体类，进行注解解析生成表结构语句并维护表结构
 * @author chenfuqiang
 *
 */
@Deprecated
//@Service("autoFixTable")
public class AutoFixTableSQLServerImpl implements AutoFixTable{
	/**
	 * 要扫描的model所在的pack
	 */
	private String pack = "com.main.pojo";
	@Resource
	private CommonDao commonDao;
	@Resource
	private DataSource secondDatasource;
	@Value("${org.quartz.dataSource.qzDS.URL}")
	private String path;

	@Override
	public StateInfo run(int year) {
		long begin = System.currentTimeMillis();
		Logger.getLogger(this.getClass()).info("---AUTO FIX DB TABLE START---");
		/**********分区函数/分区方案的创建************/
		createPartitionTable(year);

		Set<Class<?>> classes = ClassTools.getClasses(pack);
		StateInfo stateInfo = dealClassSQL(classes,year);
		long end = System.currentTimeMillis();
		Logger.getLogger(this.getClass()).info("---AUTO FIX DB TABLE END---");
		Logger.getLogger(this.getClass()).info("TAKE TIME "+((end-begin)*0.001)+" SECOND");
		return stateInfo;
	}
	
	public StateInfo dealClassSQL(Set<Class<?>> classes,int year) {
		StateInfo stateInfo = new StateInfo();
		List<String> sqlAdd = new ArrayList<>();
		List<String> sqlUpt = new ArrayList<>();
		List<String> sqlPK = new ArrayList<>();
 		List<String> sqlEtc = new ArrayList<>();
		Map<String,String> table_indexName = this.getKeyName();
		StringBuffer keyBuf = new StringBuffer();
		StringBuffer keyEditBuf = new StringBuffer();
		StringBuffer allBuf = new StringBuffer();
		StringBuffer addBuf = new StringBuffer();
		StringBuffer editBuf = new StringBuffer();
		StringBuffer pkBuf = new StringBuffer();
		StringBuffer dvBuf = new StringBuffer();

		if(year == 0 ) {
			year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		}
		
		for (Class<?> clas : classes){
			addBuf.setLength(0);
			keyBuf.setLength(0);
			keyEditBuf.setLength(0);
			pkBuf.setLength(0);
			editBuf.setLength(0);
			dvBuf.setLength(0);
			allBuf.setLength(0);
			
			Table table = clas.getAnnotation(Table.class);
			if(table == null ) {
				continue;
			}

			String tablename = table.name();
			if(CommonUtil.isEmpty(tablename)) {
				tablename = clas.getSimpleName();
			}
			tablename = tablename.toUpperCase().replace("@YEAR", String.valueOf(year));
			//==================================================================================//
			//去掉索引，然后再修改列
			String indexName = table_indexName.get(tablename.toUpperCase());
			if(!CommonUtil.isEmpty(indexName)){
				pkBuf.append("ALTER TABLE "+tablename+" drop CONSTRAINT "+indexName+" \r\n");
				sqlPK.add(pkBuf.toString());
			}
			addBuf.append("CREATE TABLE dbo.").append(tablename).append("( \r\n");
			Field[] fields = clas.getDeclaredFields();
// 			这里支持集成的父类，要支持只要把下面的fields 附加到子类的fields即可。
			if(table.includeSupperClass()) {
				if(clas.getSuperclass()!=null){
					Class<?> clsSup = clas.getSuperclass();
					fields = (Field[]) ArrayUtils.addAll(fields,clsSup.getDeclaredFields());
				}
			}
			boolean hasFieldAnnotation = false;
			for (Field field : fields){
				Column column = field.getAnnotation(Column.class);
				if(column == null) {continue;}
				hasFieldAnnotation = true;
				String columnname = CommonUtil.isEmpty(column.name())?field.getName():column.name();
				String flag = column.flag();
//				String dv = column.defaultValue();
				String oth = column.oth();//identity(1,1)
//				if(!CommonUtil.isEmpty(dv)) {
//					dv = dv.toUpperCase().replace("@YEAR", String.valueOf(year));
//				}
				String type = column.type();
				
				addBuf.append(columnname).append(" ").append(type).append(" ");
				if(!CommonUtil.isEmpty(oth)) {
					addBuf.append(" "+oth+" ");
				}
				if("PRIMARY".equals(CommonUtil.nullToStr(flag).toUpperCase())) {
					keyBuf.append("").append(columnname).append(",\r\n");
					keyEditBuf.append(columnname).append(",");
					
					addBuf.append(" NOT NULL ");
				}
//				if(!CommonUtil.isEmpty(dv)) {
//					addBuf.append(" DEFAULT (").append(dv).append(") ");
//					dvBuf.append("Update ").append(tablename).append(" Set ").append(columnname).append("=").append(dv).append(" ");
//					dvBuf.append("Where ").append(columnname).append(" is null").append(" \r\n");
//				}
				addBuf.append(",\r\n");
				//===================================UPDATE FIELDS=================================//
				editBuf.append("IF EXISTS(SELECT * FROM syscolumns WHERE ID=OBJECT_ID('"+tablename+"') AND NAME='"+columnname+"') \r\n");
				editBuf.append("BEGIN \r\n");
				editBuf.append("ALTER TABLE ").append(tablename).append(" ALTER column [").append(columnname).append("] ").append(type).append(" ");
				if("PRIMARY".equals(CommonUtil.nullToStr(flag).toUpperCase())) {
					editBuf.append(" NOT NULL ");
				}else {
					if(!CommonUtil.isEmpty(oth)) {
						editBuf.append(" NOT NULL ");
					}
				}
				editBuf.append(" \r\n");
				editBuf.append("END \r\n");
				editBuf.append("IF NOT EXISTS(SELECT * FROM syscolumns WHERE ID=OBJECT_ID('"+tablename+"') AND NAME='"+columnname+"') \r\n");
				editBuf.append("BEGIN \r\n");
				editBuf.append("ALTER TABLE ").append(tablename).append(" add [").append(columnname).append("] ").append(type).append(" ");
				if("PRIMARY".equals(CommonUtil.nullToStr(flag).toUpperCase())) {
					editBuf.append(" NOT NULL ");
				}else {
					if(!CommonUtil.isEmpty(oth)) {
						editBuf.append(" NOT NULL ");
					}
				}
				editBuf.append(" \r\n");
				editBuf.append("END \r\n");
				//=================================================================================//
			}
			if(hasFieldAnnotation == false) {
				continue;
			}
			if(keyBuf.length() != 0) {
				addBuf.append("CONSTRAINT PK_" + tablename+ " PRIMARY KEY ( \r\n");
				addBuf.append(keyBuf.substring(0,keyBuf.length()-3));
				addBuf.append(") \r\n");
			}else {
				addBuf.delete(addBuf.length()-3, addBuf.length()-1);
			}
			addBuf.append(") \r\n");
			
			allBuf.append("IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'dbo."+ tablename+ "') AND OBJECTPROPERTY(id, N'IsUserTable') = 1) \r\n");
			allBuf.append("BEGIN \r\n");
			allBuf.append(editBuf.toString());
			allBuf.append("END \r\n");
			sqlUpt.add(allBuf.toString());

			allBuf.setLength(0);
			allBuf.append("IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'dbo."+ tablename+ "') AND OBJECTPROPERTY(id, N'IsUserTable') = 1) \r\n");
			allBuf.append("BEGIN \r\n");
			allBuf.append(addBuf.toString());
			allBuf.append("END \r\n");
			sqlAdd.add(allBuf.toString());
			
			//修改主键需要在列都修改完执行完之后再修改主键，因为有些列是NULL,修改完列后就是NOT NULL
			//=====================================UPDATE TABLE ===============================//
			//修改默认值
			if(dvBuf.length() != 0) {
				sqlEtc.add(dvBuf.toString());
			}
			//修改主键
			if(keyBuf.length() != 0) {
				allBuf.setLength(0);
				allBuf.append("IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'dbo."+ tablename+ "') AND OBJECTPROPERTY(id, N'IsUserTable') = 1) \r\n");
				allBuf.append("BEGIN \r\n");
				allBuf.append("alter table "+tablename+" add constraint pk_"+tablename+" primary key ("+keyEditBuf.substring(0,keyEditBuf.length()-1)+") \r\n");
				allBuf.append("END \r\n");
				sqlEtc.add(allBuf.toString());
			}
		}
		//======================================JDBC===============================================//
		try {
			
			if(sqlAdd.size()>0) {
				commonDao.transactionUpdate(sqlAdd);
				Logger.getLogger(this.getClass()).info("--ADD DB TABLE DONE--");
			}
			if(sqlUpt.size()>0) {
				commonDao.transactionUpdate(sqlUpt);
				Logger.getLogger(this.getClass()).info("--UPDATE DB TABLE DONE--");
			}
			if(sqlPK.size()>0) {
				commonDao.transactionUpdate(sqlPK);
				Logger.getLogger(this.getClass()).info("--DROP PRIMARY KEY DONE--");
			}
			if(sqlEtc.size()>0) {
				commonDao.transactionUpdate(sqlEtc);
				Logger.getLogger(this.getClass()).info("--OTHER OPTION DONE--");
			}
			
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	
	public Map<String,String> getKeyName() {
//		select a.name,b.name from sysobjects a left join sysobjects b on a.parent_obj = b.id where 1=1 
//		and a.xtype='pk'
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select b.name AS [表名], ");
		sqlBuffer.append("a.name  AS [主键名称] ");
		sqlBuffer.append("from  dbo.sysobjects a left join  dbo.sysobjects b on a.parent_obj = b.id where 1=1 ");
		sqlBuffer.append("and a.xtype='pk' ");
		List<Map<String,Object>> list = commonDao.getListForMap(sqlBuffer.toString());
		Map<String,String> result = new HashMap<>();
		for(Map<String,Object> m : list) {
			result.put(String.valueOf(m.get("表名")).toUpperCase(), String.valueOf(m.get("主键名称")));
		}
		return result;
	}

	public void executeSQL(String sql) {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = secondDatasource.getConnection();
			statement = conn.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Global.close(conn,statement,null);
		}
	}

	public boolean createPartitionTable(int year){

		//创建分区文件，分区函数，分区方案
		if(createPartitionPlan()==false){
			return false;
		}

		//创建pm_mx_original

		//创建pm_mx


		return true;
	}

	/**
	 * @Author: sjl
	 * @Date: 2018/4/29 12:46
	 * @param
	 * @Description:
	 *	创建分区函数，分区方案，分区文件
	 */
	public boolean createPartitionPlan(){

		//获取数据库路径
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" select top 1 filename from sysfiles");
		List<Map<String,Object>> checkList1 = commonDao.getListForMap(sqlBuffer.toString());

		if(CommonUtil.isEmpty(checkList1)){
			return false;
		}

		//获取是否已经创建过分区文件
		sqlBuffer.setLength(0);
		sqlBuffer.append(" select filename from sysfiles where name like 'PM_data_month%'");
		List<Map<String,Object>> checkList2 = commonDao.getListForMap(sqlBuffer.toString());

		//创建分区文件 分区方案 分区函数
		if(CommonUtil.isEmpty(checkList2) || checkList2.size()<12 ) {
			String filePath = String.valueOf(checkList1.get(0).get("filename"));
			filePath = filePath.substring(0, filePath.lastIndexOf("\\"));

			String dbName = path.substring(path.indexOf("DatabaseName=")+13,path.indexOf(";SelectMethod"));
			Date d = new Date();
			String time = DateFormat.getDateInstance().format(d).replace("-", "")
					+"-"+DateFormat.getTimeInstance().format(d).replace(":", "");
			time.replace(" ", "");

			//1.创建分区文件
			sqlBuffer.setLength(0);
			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month1]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month1',");
			sqlBuffer.append("FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_1.ndf',SIZE = 51200KB,");
			sqlBuffer.append("FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month1] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month2]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month2',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_2.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month2] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month3]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month3',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_3.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month3] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month4]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month4',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_4.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month4] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month5]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month5',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_5.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month5] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month6]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month6',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_6.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month6] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month7]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month7',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_7.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month7] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month8]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month8',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_8.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month8] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month9]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month9',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_9.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month9] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month10]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month10',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_10.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month10] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month11]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month11',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_11.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month11] ");

			sqlBuffer.append(" ALTER DATABASE [").append(dbName).append("] ADD FILEGROUP [pm_data_month12]");
			sqlBuffer.append(" ALTER DATABASE [").append(dbName);
			sqlBuffer.append("] ADD FILE ( NAME = 'pm_data_month12',");
			sqlBuffer.append(" FILENAME = '").append(filePath).append("\\").append(dbName + time).append("_12.ndf',SIZE = 51200KB,");
			sqlBuffer.append(" FILEGROWTH = 102400KB ) ");
			sqlBuffer.append(" TO FILEGROUP [pm_data_month12] ");
			executeSQL(sqlBuffer.toString());

			//2.创建分区函数
			sqlBuffer.setLength(0);
			sqlBuffer.append(" create partition function splitByThemonth(int) as range left for values (1,2,3,4,5,6,7,8,9,10,11)");
			executeSQL(sqlBuffer.toString());

			//3.创建分区方案
			sqlBuffer.setLength(0);
			sqlBuffer.append(" create  partition  scheme  splitByThemonthPlan as partition  splitByThemonth ");
			sqlBuffer.append(" to(pm_data_month1,pm_data_month2,pm_data_month3,pm_data_month4,pm_data_month5,");
			sqlBuffer.append(" pm_data_month6,pm_data_month7,pm_data_month8,pm_data_month9,pm_data_month10,pm_data_month11,pm_data_month12)");
			executeSQL(sqlBuffer.toString());
		}
		return true;
	}
}
