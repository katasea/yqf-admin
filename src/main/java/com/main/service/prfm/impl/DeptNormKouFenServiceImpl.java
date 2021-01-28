package com.main.service.prfm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.CommonUtil;
import com.common.constants.GlobalConstants;
import com.main.aop.LogAnnotation;
import com.main.dao.prfm.DeptNormKouFenDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.DeptNormKouFen;
import com.main.pojo.result.DeptNormKouFenResult;
import com.main.service.prfm.AuthCenterService;
import com.main.service.prfm.DeptNormKouFenService;

/**
 * 对象服务层实现类
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class DeptNormKouFenServiceImpl implements DeptNormKouFenService {
	@Resource
	private DeptNormKouFenDao deptNormKouFenDao;
	@Resource
	private AuthCenterService acs;

    @LogAnnotation(moduleName = "PD_DNKF服务", option = "查询")
	@Override
	public DeptNormKouFen selectBykid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.deptNormKouFenDao.selectBykid(map);
	}

	@LogAnnotation(moduleName = "PD_DNKF服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(deptNormKouFenDao.validator(key,value,bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"已录入重复的kid编号，请修改！",null);
			}
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

	@Override
	@Deprecated
    public int getCount(String keyword,String node,Bridge bridge) {
        Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        Map<String,Object> resultMap = this.deptNormKouFenDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "PD_DNKF服务", option = "查询列表数据")
	@Override
	@Deprecated
	public List<DeptNormKouFen> get(String keyword, String node, Bridge bridge, String start, String limit,String deptOrPerson) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		map.put("deptOrPerson", deptOrPerson);
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		return this.deptNormKouFenDao.getPage(map);
	}

	
	@LogAnnotation(moduleName = "PD_DNKF服务", option = "查询列表数据")
	@Override
	public List<DeptNormKouFenResult> getJoin(String keyword, String node, Bridge bridge,
			String start, String limit,String deptId,String normId,String deptOrPerson,
			String zhiBiaoType,String isKouFen,String isMarkNull,String zhiBiaoInFo) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		zhiBiaoInFo = CommonUtil.decodeKeyWord(this.getClass(),zhiBiaoInFo);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		map.put("deptId", deptId);
		map.put("normId", normId);
		//map.put("zhiBiaoType", zhiBiaoType);
		map.put("isKouFen", isKouFen);
		map.put("isMarkNull", isMarkNull);
		map.put("zhiBiaoInFo", zhiBiaoInFo);
		if(deptOrPerson==null){
			deptOrPerson = GlobalConstants.DEPT_OR_PERSON_D+"";
		}
		if(GlobalConstants.DEPT_OR_PERSON_D.equals(Integer.parseInt(deptOrPerson))){
			if(normId!=null && !"".equals(normId)){//如果指标不为空 先判断指标关联部分是否配置
				int count = acs.getAuthCount("", GlobalConstants.AUTHCODE_ZB_DEPT_RELA, bridge, normId);
				if(count>0){
					map.put("isInAuthcenter", 1);
				}else{//否则查所有的 要把跟指标有关的查询去掉
					map.remove("normId");
					map.remove("zhiBiaoType");
				}
			}
			return this.deptNormKouFenDao.getPageJoinDept(map);
		}else{
			return this.deptNormKouFenDao.getPageJoinUser(map);
		}
	
	}
	
	
	@Override
    public int getCountJoin(String keyword,String node,Bridge bridge,
    		String deptId,String normId,String deptOrPerson,
    		String zhiBiaoType,String isKouFen,String isMarkNull,String zhiBiaoInFo) {
        Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
		map.put("deptId", deptId);
		map.put("normId", normId);
		//map.put("zhiBiaoType", zhiBiaoType);
		map.put("isKouFen", isKouFen);
		map.put("isMarkNull", isMarkNull);
		map.put("zhiBiaoInFo", zhiBiaoInFo);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(deptOrPerson==null){
			deptOrPerson = GlobalConstants.DEPT_OR_PERSON_D+"";
		}
		if(zhiBiaoType==null){
			zhiBiaoType = GlobalConstants.ZHIBIAOTYPE_YUE+"";
		}
		if(GlobalConstants.DEPT_OR_PERSON_D.equals(Integer.parseInt(deptOrPerson))){
			if(normId!=null && !"".equals(normId)){//如果指标不为空 先判断指标关联部分是否配置
				int count = acs.getAuthCount("", GlobalConstants.AUTHCODE_ZB_DEPT_RELA, bridge, normId);
				if(count>0){
					map.put("isInAuthcenter", 1);
				}else{//否则查所有的 要把跟指标有关的查询去掉
					map.remove("normId");
					map.remove("zhiBiaoType");
				}
			}
			resultMap = this.deptNormKouFenDao.getCountJoinDept(map);
		}else{
			resultMap = this.deptNormKouFenDao.getCountJoinUser(map);
		}
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }
	

	
    @LogAnnotation(moduleName = "PD_DNKF服务", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.deptNormKouFenDao.deleteByPrimaryKey(map);
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
	private Map<String,Object> getBeanInfoMap(DeptNormKouFen deptNormKouFen, Bridge bridge){
		deptNormKouFen.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("kid",deptNormKouFen.getKid());
		map.put("companyid",deptNormKouFen.getCompanyid());
		map.put("deptOrPersonId",deptNormKouFen.getDeptOrPersonId());
		map.put("zhiBiaoId",deptNormKouFen.getZhiBiaoId());
		map.put("kouFen",deptNormKouFen.getKouFen());
		map.put("realKouFen",deptNormKouFen.getRealKouFen());
		map.put("kouFenMark",deptNormKouFen.getKouFenMark());
		map.put("zkkKouFen", deptNormKouFen.getZkkKouFen());
		map.put("qiTaKouFen", deptNormKouFen.getQiTaKouFen());
		map.put("kouFenRenId", deptNormKouFen.getKouFenRenId());
		map.put("bigtype",deptNormKouFen.getBigtype());
		return map;
	}

    @LogAnnotation(moduleName = "PD_DNKF服务", option = "修改")
	@Override
	public StateInfo edit(String kid, DeptNormKouFen deptNormKouFen, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(kid) && deptNormKouFen != null) {
			Map<String,Object> map = this.getBeanInfoMap(deptNormKouFen,bridge);
			map.put("whereId",kid);
			try {
				this.deptNormKouFenDao.update(map);
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

    @LogAnnotation(moduleName = "PD_DNKF服务", option = "新增")
	@Override
	public StateInfo add(String parentId,DeptNormKouFen deptNormKouFen, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String,Object> mapOfID = this.deptNormKouFenDao.getAutoGeneralID(parentId,bridge);
		deptNormKouFen.setKid(CommonUtil.dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(deptNormKouFen,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.deptNormKouFenDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}

}