package com.main.service.prfm.impl;

import com.common.*;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.BaseDic4ListDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.BaseDic4List;
import com.main.pojo.platform.Bridge;
import com.main.service.prfm.BaseDic4ListService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象服务层实现类
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class BaseDic4ListServiceImpl implements BaseDic4ListService {
	@Resource
	private BaseDic4ListDao baseDic4ListDao;
	@Resource
	private CommonDao comDao;

    @LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "查询")
	@Override
	public BaseDic4List selectBytype(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.baseDic4ListDao.selectBytype(map);
	}

	@LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(baseDic4ListDao.validator(key,value,bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"已录入重复的type编号，请修改！",null);
			}
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
    public int getCount(String keyword,String node,Bridge bridge) {
        Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        Map<String,Object> resultMap = this.baseDic4ListDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "查询列表数据")
	@Override
	public List<BaseDic4List> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.baseDic4ListDao.getPage(map);
	}

    @LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "删除")
	@Override
	public StateInfo delete(String code,Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("companyid",bridge.getCompanyid());
				map.put("id",code);
				this.baseDic4ListDao.deleteByPrimaryKey(map);
			}catch(Exception e){
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),e.getMessage(),e);
			}
		}else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),"传递的id为空无法删除，请刷新后重试！",null);
		}
		return stateInfo;
	}
	/**
	 * 获取bean 信息的map 用于传递给mybatis
	 */
	private Map<String,Object> getBeanInfoMap(BaseDic4List baseDic4List, Bridge bridge){
		baseDic4List.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid",baseDic4List.getCompanyid());
		map.put("mkey",baseDic4List.getMkey());
		map.put("type",baseDic4List.getType());
		map.put("mvalue",baseDic4List.getMvalue());
		map.put("reverse",baseDic4List.getReverse());
		return map;
	}

    @LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "修改")
	@Override
	public StateInfo edit(String oldKey, BaseDic4List baseDic4List, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(oldKey) && baseDic4List != null) {
			Map<String,Object> map = this.getBeanInfoMap(baseDic4List,bridge);
			map.put("id",oldKey);
			try {
				this.baseDic4ListDao.update(map);
			}catch(Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),e.getMessage(),e);
			}
		}else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),"传递的关键字段为空或者对象不存在，请刷新页面重试",null);
		}
		return stateInfo;
	}

    @LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "新增")
	@Override
	public StateInfo add(String parentId,BaseDic4List baseDic4List, Bridge bridge) {
		//tree init
		Map<String,Object> map = this.getBeanInfoMap(baseDic4List,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.baseDic4ListDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	/**
	 *初始化内置数据
	 */
     @LogAnnotation(moduleName = "PD_DICTIONARY_LIST服务", option = "初始化数据")
	 @Override
	 public StateInfo autoInitFromAccess(Bridge bridge){
	 	StateInfo stateInfo = new StateInfo();
	 	Connection conn = null;
	 	Statement stmt = null;
	 	ResultSet rs = null;
	 	try{
			this.baseDic4ListDao.deleteAll(bridge.getYear());
			conn = AccessOptUtil.connectAccessFile();
            if(conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("Select * From PD_DICTIONARY_LIST");
                List<String> sqlList = new ArrayList<>();
                StringBuilder sqlBuffer = new StringBuilder();
                while(rs.next()) {
                    sqlBuffer.setLength(0);
                    sqlBuffer.append("Insert Into PD_DICTIONARY_LIST");
                    sqlBuffer.append("(companyid,mkey,type,mvalue,reverse) values (");
                    sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("mkey")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("type"));
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("mvalue")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("reverse")).append("'");
                
                    sqlBuffer.append(")");
                    sqlList.add(sqlBuffer.toString());
                }
                comDao.transactionUpdate(sqlList);
            }
		}catch(Exception e){
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}finally {
			Global.close(conn,stmt,rs);
		}
	 	return stateInfo;
	 }
}