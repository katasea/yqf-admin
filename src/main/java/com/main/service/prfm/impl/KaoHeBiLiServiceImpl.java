package com.main.service.prfm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.CommonUtil;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.DeptInfoDao;
import com.main.dao.prfm.KaoHeBiLiDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.KaoHeBiLi;
import com.main.pojo.result.KaoHeBiLiResult;
import com.main.service.prfm.KaoHeBiLiService;

/**
 * 对象服务层实现类
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class KaoHeBiLiServiceImpl implements KaoHeBiLiService {
	@Resource
	private KaoHeBiLiDao kaoHeBiLiDao;
	@Resource
	private DeptInfoDao deptInfoDao;

    @LogAnnotation(moduleName = "PD_KHBL服务", option = "查询")
	@Override
	public KaoHeBiLi selectBykid(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.kaoHeBiLiDao.selectBykid(map);
	}

	@LogAnnotation(moduleName = "PD_KHBL服务", option = "验证编号唯一")
	@Override
	public StateInfo validator(String key,String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if(!CommonUtil.isEmpty(kaoHeBiLiDao.validator(key,value,bridge))) {
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
    public int getCount(String keyword,String node,Bridge bridge) {
        Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        Map<String,Object> resultMap = this.kaoHeBiLiDao.getCount(map);
        return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
    }

	@LogAnnotation(moduleName = "PD_KHBL服务", option = "查询列表数据")
	@Override
	public List<KaoHeBiLiResult> get(String keyword, String node, Bridge bridge, String start, String limit,String deptId) {
		Map<String,Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(),keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid",bridge.getCompanyid());
        map.put("start", start);
		map.put("end", Integer.parseInt(start)+Integer.parseInt(limit));
		map.put("deptId", deptId);
		return this.kaoHeBiLiDao.getPage(map);
	}

    @LogAnnotation(moduleName = "PD_KHBL服务", option = "删除")
	@Override
	public StateInfo delete(String code,String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(code)) {
			try {
				Map<String,Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id",code);
				//顺序不能乱
				this.kaoHeBiLiDao.deleteByPrimaryKey(map);
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
	private Map<String,Object> getBeanInfoMap(KaoHeBiLi kaoHeBiLi, Bridge bridge){
		kaoHeBiLi.setCompanyid(bridge.getCompanyid());
		
		Map<String,Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("kid",kaoHeBiLi.getKid());
		map.put("companyid",kaoHeBiLi.getCompanyid());
		map.put("deptId",kaoHeBiLi.getDeptId());
		map.put("zbId",kaoHeBiLi.getZbId());
		map.put("zpbl",kaoHeBiLi.getZpbl());
		map.put("zkkbl",kaoHeBiLi.getZkkbl());
		return map;
	}

    @LogAnnotation(moduleName = "PD_KHBL服务", option = "修改")
	@Override
	public StateInfo edit(String kid, KaoHeBiLi kaoHeBiLi, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(kid) && kaoHeBiLi != null) {
			Map<String,Object> map = this.getBeanInfoMap(kaoHeBiLi,bridge);
			map.put("whereId",kid);
			try {
				this.kaoHeBiLiDao.update(map);
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

    @LogAnnotation(moduleName = "PD_KHBL服务", option = "新增")
	@Override
	public StateInfo add(String parentId,KaoHeBiLi kaoHeBiLi, Bridge bridge) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String,Object> mapOfID = this.kaoHeBiLiDao.getAutoGeneralID(parentId,bridge);
		kaoHeBiLi.setKid(CommonUtil.dealPKRule(mapOfID,parentId));
		Map<String,Object> map = this.getBeanInfoMap(kaoHeBiLi,bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.kaoHeBiLiDao.insert(map);
		}catch(Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),e.getMessage(),e);
		}
		return stateInfo;
	}
}