package com.main.service.prfm.impl;

import com.common.*;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.MidDataDicParentDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.MidDataDicParent;
import com.main.pojo.platform.Bridge;
import com.main.service.prfm.MidDataDicParentService;
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
public class MidDataDicParentServiceImpl implements MidDataDicParentService {
	@Resource
	private MidDataDicParentDao midDataDicParentDao;
	@Resource
	private CommonDao comDao;

    @LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "查询")
	@Override
	public MidDataDicParent selectByid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.midDataDicParentDao.selectByid(map);
	}

	@LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(midDataDicParentDao.validator(key,value,bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"已录入重复的id编号，请修改！",null);
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
        Map<String,Object> resultMap = this.midDataDicParentDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "查询列表数据")
	@Override
	public List<MidDataDicParent> get(String keyword, String deptOrper, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("deptOrper", CommonUtil.nullToStr(deptOrper));
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.midDataDicParentDao.getPage(map);
	}

    @LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.midDataDicParentDao.deleteByPrimaryKey(map);
				if(!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node",parentId);
					map.put("companyid",bridge.getCompanyid());

				}
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
	private Map<String,Object> getBeanInfoMap(MidDataDicParent midDataDicParent, Bridge bridge){
		midDataDicParent.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid",midDataDicParent.getCompanyid());
		map.put("id",midDataDicParent.getId());
		map.put("name",midDataDicParent.getName());
		map.put("orderid",midDataDicParent.getOrderid());
		return map;
	}

    @LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "修改")
	@Override
	public StateInfo edit(String id, MidDataDicParent midDataDicParent, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(id) && midDataDicParent != null) {
			Map<String,Object> map = this.getBeanInfoMap(midDataDicParent,bridge);
			map.put("whereId",id);
			try {
				this.midDataDicParentDao.update(map);
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

    @LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "新增")
	@Override
	public StateInfo add(String parentId,MidDataDicParent midDataDicParent, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String,Object> mapOfID = this.midDataDicParentDao.getAutoGeneralID(parentId,bridge);
		midDataDicParent.setId(CommonUtil.dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(midDataDicParent,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.midDataDicParentDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	/**
	 *初始化内置数据
	 */
     @LogAnnotation(moduleName = "PD_MIDDATADICPARENT服务", option = "初始化数据")
	 @Override
	 public StateInfo autoInitFromAccess(Bridge bridge){
	 	StateInfo stateInfo = new StateInfo();
	 	Connection conn = null;
	 	Statement stmt = null;
	 	ResultSet rs = null;
	 	try{
			this.midDataDicParentDao.deleteAll(bridge.getYear());
			conn = AccessOptUtil.connectAccessFile();
            if(conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("Select * From PD_MIDDATADICPARENT");
                List<String> sqlList = new ArrayList<>();
                StringBuilder sqlBuffer = new StringBuilder();
                while(rs.next()) {
                    sqlBuffer.setLength(0);
                    sqlBuffer.append("Insert Into PD_MIDDATADICPARENT");
                    sqlBuffer.append("(companyid,id,name,orderid) values (");
                    sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("id")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("name")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("orderid"));
                
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