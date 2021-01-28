package com.main.service.prfm.impl;

import com.common.*;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.MidDataDicDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.MidDataDic;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.MidDataDicParent;
import com.main.service.prfm.MidDataDicParentService;
import com.main.service.prfm.MidDataDicService;
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
public class MidDataDicServiceImpl implements MidDataDicService {
	@Resource
	private MidDataDicDao midDataDicDao;
	@Resource
	private CommonDao comDao;
	@Resource
	private MidDataDicParentService midDataDicParentService;

    @LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "查询")
	@Override
	public MidDataDic selectByid(String id,String companyid) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("companyid", companyid);
		return this.midDataDicDao.selectByid(map);
	}

	@Override
	public Map<String, String> getDecInfo(String id, String companyid) {
    	Map<String,String> resultMap = new HashMap<>();
    	List<MidDataDic> decInfo = midDataDicDao.getAll(id,companyid);
    	for(MidDataDic dic : decInfo) {
    		resultMap.put(dic.getId(),String.valueOf(dic.getDec()));
	    }
		return resultMap;
	}

	@LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(midDataDicDao.validator(key,value,bridge))) {
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
        Map<String,Object> resultMap = this.midDataDicDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "查询列表数据")
	@Override
	public List<MidDataDic> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.midDataDicDao.getPage(map);
	}

	@Override
	public List<MidDataDic> getByType(String companyid,int type) {
		return this.midDataDicDao.getByType(companyid,type);
	}

	@Override
	public List<MidDataDic> getByParentid(Bridge bridge, String chooseParentId) {
		return this.midDataDicDao.getByParentid(bridge,chooseParentId);
	}

	@LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.midDataDicDao.deleteByPrimaryKey(map);
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
	private Map<String,Object> getBeanInfoMap(MidDataDic midDataDic, Bridge bridge){
		midDataDic.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid",midDataDic.getCompanyid());
		map.put("id",midDataDic.getId());
		map.put("name",midDataDic.getName());
		map.put("type",midDataDic.getType());
		map.put("isstop",midDataDic.getIsstop());
		map.put("parentid",midDataDic.getParentid());
		map.put("deptorper",midDataDic.getDeptorper());
		map.put("dec",midDataDic.getDec());
		return map;
	}

    @LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "修改")
	@Override
	public StateInfo edit(String id, MidDataDic midDataDic, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(id) && midDataDic != null) {
			Map<String,Object> map = this.getBeanInfoMap(midDataDic,bridge);
			map.put("whereId",id);
			try {
				this.midDataDicDao.update(map);
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

    @LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "新增")
	@Override
	public StateInfo add(String parentId,MidDataDic midDataDic, Bridge bridge) {
		//自动生成主键按1 101 10101规则
//		Map<String,Object> mapOfID = this.midDataDicDao.getAutoGeneralID(parentId,bridge);
//		midDataDic.setId(CommonUtil.dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(midDataDic,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.midDataDicDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	/**
	 *初始化内置数据
	 */
	@LogAnnotation(moduleName = "PD_MIDDATADIC服务", option = "初始化数据")
	@Override
	public StateInfo autoInitFromAccess(Bridge bridge){
		StateInfo stateInfo = new StateInfo();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			this.midDataDicDao.deleteAll(bridge.getYear());
			conn = AccessOptUtil.connectAccessFile();
			if(conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("Select * From PD_MIDDATADIC");
				List<String> sqlList = new ArrayList<>();
				StringBuilder sqlBuffer = new StringBuilder();
				while(rs.next()) {
					sqlBuffer.setLength(0);
					sqlBuffer.append("Insert Into PD_MIDDATADIC");
					sqlBuffer.append("(companyid,id,name,type,isstop,parentid,deptorper,dec) values (");
					sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("id")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("name")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("type")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("isstop")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("parentid")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("deptorper")).append("'");
					sqlBuffer.append(",");
					sqlBuffer.append("'").append(rs.getString("dec")).append("'");
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

	@Override
	public List<Map<String, Object>> getTreeJson(Bridge bridge,int type) {
		List<Map<String,Object>> result = new ArrayList<>();
		//获取自动计算类型的辅助数据字典。
		List<MidDataDic> list = this.getByType(bridge.getCompanyid(),2);
		List<MidDataDicParent> parentList = midDataDicParentService.get(null,null,bridge,"0",String.valueOf(Global.TABLE_MAX_RECORD));
		Map<String,List<MidDataDic>> mapInfo = new HashMap<>();
		for(MidDataDic bean : list) {
			List<MidDataDic> tempList = mapInfo.get(bean.getParentid());
			if(tempList == null) {
				tempList = new ArrayList<>();
			}
			tempList.add(bean);
			mapInfo.put(bean.getParentid(),tempList);
		}
		for(MidDataDicParent parent : parentList) {
			Map<String,Object> map = new HashMap<>();
			//防止id与子id重复
			map.put("id","P_"+parent.getId());
			map.put("text",parent.getName());
			map.put("leaf",false);
			List<MidDataDic> tempChildList = mapInfo.get(parent.getId());
			if(tempChildList!=null) {
				//==========拼凑子节点信息===============//
				List<Map<String,Object>> childrenList = new ArrayList<>();
				for(MidDataDic dic:tempChildList) {
					Map<String,Object> dicmap = new HashMap<>();
					dicmap.put("id",dic.getId());
					if(dic.getDeptorper()==1) {
						dicmap.put("text","<span style='font-weight:bold;color:green'>[部门] "+dic.getId()+" "+dic.getName()+"</span>");
					}else {
						dicmap.put("text","<span style='font-weight:bold;color:red'>[个人] "+dic.getId()+" "+dic.getName()+"</span>");
					}
					dicmap.put("leaf",true);
					dicmap.put("deptorper",dic.getDeptorper());
					childrenList.add(dicmap);
				}
				//===========拼凑完毕=================//
				//子节点信息
				map.put("children",childrenList);
				result.add(map);
			}
		}
		return result;
	}

}