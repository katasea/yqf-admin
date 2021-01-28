package com.main.service.prfm.impl;

import com.common.*;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.CommonDao;
import com.main.dao.prfm.BonusDicDao;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.BonusDic;
import com.main.pojo.platform.Bridge;
import com.main.service.prfm.BonusDicService;
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
public class BonusDicServiceImpl implements BonusDicService {
	@Resource
	private BonusDicDao bonusDicDao;
	@Resource
	private CommonDao comDao;

    @LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "查询")
	@Override
	public BonusDic selectBybh(String id,Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
	    map.put("year", bridge.getYear());
	    map.put("month", bridge.getMonth());
	    map.put("companyid",bridge.getCompanyid());
		return this.bonusDicDao.selectBybh(map);
	}

	@LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(bonusDicDao.validator(key,value,bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"已录入重复的month编号，请修改！",null);
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
		map.put("month", bridge.getMonth());
		map.put("companyid",bridge.getCompanyid());
        Map<String,Object> resultMap = this.bonusDicDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "查询列表数据")
	@Override
	public List<BonusDic> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("month", bridge.getMonth());
		map.put("companyid",bridge.getCompanyid());
		List<BonusDic> result = this.bonusDicDao.get(map);
		if(CommonUtil.isEmpty(keyword)) {
			for(BonusDic m : result) {
				m.setId(m.getBh());
				m.setLeaf(m.getIsleaf()==1);
			}
		}else {
			for(BonusDic m : result) {
				m.setId(m.getBh());
				m.setLeaf(true);
			}
		}
		return result;
	}

    @LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				map.put("month", bridge.getMonth());
				map.put("companyid",bridge.getCompanyid());
				//顺序不能乱
				this.bonusDicDao.deleteByPrimaryKey(map);
				if(!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node",parentId);
					map.put("companyid",bridge.getCompanyid());

					List<BonusDic> childrens = this.bonusDicDao.get(map);
					//父节点没有子节点要把父节点的leaf 设置为0
					if(childrens == null || childrens.size() == 0) {
						Map<String,Object> map2 = new HashMap<>();
						map2.put("id",parentId);
						map2.put("value","1");
						map2.put("year", bridge.getYear());
						map2.put("month", bridge.getMonth());
						map2.put("companyid",bridge.getCompanyid());
						//父节点leaf 设置为0
						this.bonusDicDao.changeLeaf(map2);
					}
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
	private Map<String,Object> getBeanInfoMap(BonusDic bonusDic, Bridge bridge){
		bonusDic.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("companyid",bonusDic.getCompanyid());
		map.put("bh",bonusDic.getBh());
		map.put("month",bridge.getMonth());
		map.put("name",bonusDic.getName());
		map.put("grade",bonusDic.getGrade());
		map.put("isleaf",bonusDic.getIsleaf());
		map.put("formula",bonusDic.getFormula());
		map.put("parentid",bonusDic.getParentid());
		map.put("isstop",bonusDic.getIsstop());
		return map;
	}

    @LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "修改")
	@Override
	public StateInfo edit(String month, BonusDic bonusDic, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(month) && bonusDic != null) {
			Map<String,Object> map = this.getBeanInfoMap(bonusDic,bridge);
			map.put("whereId",month);
			map.put("year", bridge.getYear());
			map.put("month", bridge.getMonth());
			map.put("companyid",bridge.getCompanyid());
			try {
				this.bonusDicDao.update(map);
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

    @LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "新增")
	@Override
	public StateInfo add(String parentId,BonusDic bonusDic, Bridge bridge) {
		//tree init
        bonusDic.setIsleaf(1);
		if(!CommonUtil.isEmpty(parentId)) {
                bonusDic.setParentid(parentId);
		}else {
                bonusDic.setParentid(Global.NULLSTRING);
		}
		//自动生成主键按1 101 10101规则
		Map<String,Object> mapOfID = this.bonusDicDao.getAutoGeneralID(parentId,bridge);
		bonusDic.setBh(CommonUtil.dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(bonusDic,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(parentId)) {
				Map<String,Object> map2 = new HashMap<>();
				map2.put("id",parentId);
				map2.put("value","0");
				map2.put("year", bridge.getYear());
				map2.put("month", bridge.getMonth());
				map2.put("companyid",bridge.getCompanyid());
				//父节点leaf 设置为0
				this.bonusDicDao.changeLeaf(map2);
			}
			this.bonusDicDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}
	/**
	 *拷贝上年数据
	 */
    @LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "拷贝上年数据")
	@Override
	public StateInfo copyLasyYearData(Bridge bridge){
		StateInfo stateInfo = new StateInfo();
		try{
			Map<String,String> map = new HashMap<>();
			map.put("year", bridge.getYear());
			map.put("month", bridge.getMonth());
			map.put("companyid",bridge.getCompanyid());
			this.bonusDicDao.deleteAll(map);

			if(Integer.parseInt(map.get("month")) == 1) {
				map.put("month","12");
				map.put("year",String.valueOf(Integer.parseInt(map.get("year"))-1));
			}else {
				map.put("month",String.valueOf(Integer.parseInt(map.get("month"))-1));
			}
			this.bonusDicDao.copyLastYear(map);
		}catch(Exception e){
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	/**
	 *初始化内置数据
	 */
     @LogAnnotation(moduleName = "PD_BONUSDIC服务", option = "初始化数据")
	 @Override
	 public StateInfo autoInitFromAccess(Bridge bridge){
	 	StateInfo stateInfo = new StateInfo();
	 	Connection conn = null;
	 	Statement stmt = null;
	 	ResultSet rs = null;
	 	try{
		    Map<String,String> map = new HashMap<>();
		    map.put("year", bridge.getYear());
		    map.put("month", bridge.getMonth());
		    map.put("companyid",bridge.getCompanyid());
			this.bonusDicDao.deleteAll(map);

			conn = AccessOptUtil.connectAccessFile();
            if(conn != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("Select * From PD_BONUSDIC");
                List<String> sqlList = new ArrayList<>();
                StringBuilder sqlBuffer = new StringBuilder();
                while(rs.next()) {
                    sqlBuffer.setLength(0);
                    sqlBuffer.append("Insert Into PD_BONUSDIC");
                    sqlBuffer.append("(companyid,bh,year,month,name,grade,isleaf,formula,parentid,isstop) values (");
                    sqlBuffer.append("'").append(bridge.getCompanyid()).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("bh")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("year")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("month")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("name")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("grade"));
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("isleaf"));
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("formula")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append("'").append(rs.getString("parentid")).append("'");
                sqlBuffer.append(",");
                    sqlBuffer.append(rs.getInt("isstop"));
                
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