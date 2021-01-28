package com.main.service.platform.impl;

import com.common.CommonUtil;
import com.common.Global;
import com.main.dao.platform.CommonDao;
import com.main.dao.platform.ResourcesInfoDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.ResourcesInfo;
import com.main.pojo.platform.StateInfo;
import com.main.service.platform.ResourcesInfoService;
import com.main.service.platform.SystemService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ATC[auto-code project create]
 */
@Service
@Transactional
public class ResourcesInfoServiceImpl implements ResourcesInfoService {
	@Resource
	private ResourcesInfoDao resourcesInfoDao;
	@Resource
	private CommonDao comDao;
	@Resource
	private SystemService sysService;

	@Override
	public ResourcesInfo selectBypkid(int id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.resourcesInfoDao.selectBypkid(map);
	}

	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(resourcesInfoDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的内容，请修改！", null);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public List<ResourcesInfo> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		List<ResourcesInfo> result = this.resourcesInfoDao.get(map);
		if (CommonUtil.isEmpty(keyword)) {
			for (ResourcesInfo m : result) {
				m.setId(String.valueOf(m.getPkid()));
				m.setLeaf(m.getIsleaf() == 1 ? true : false);
			}
		} else {
			for (ResourcesInfo m : result) {
				m.setId(String.valueOf(m.getPkid()));
				m.setLeaf(true);
			}
		}
		return result;
	}

	/**
	 * 获取bean 信息的map 用于传递给mybatis
	 */
	public Map<String, Object> getBeanInfoMap(ResourcesInfo resourcesInfo, Bridge bridge) {

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("pkid", resourcesInfo.getPkid());
		map.put("pkid", resourcesInfo.getPkid());
		map.put("resid", resourcesInfo.getResid());
		map.put("name", resourcesInfo.getName());
		map.put("resurl", resourcesInfo.getResurl());
		map.put("parentid", resourcesInfo.getParentid());
		map.put("isleaf", resourcesInfo.getIsleaf());
		map.put("type", resourcesInfo.getType());
		map.put("sort", resourcesInfo.getSort());
		map.put("fa", resourcesInfo.getFa());
		return map;
	}

	@Override
	public int getCount(String keyword, String node, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.resourcesInfoDao.getCount(map);
		int count = Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
		return count;
	}

	@Override
	public StateInfo delete(String code, String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(code)) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id", code);
				//顺序不能乱

				this.resourcesInfoDao.deleteByPrimaryKey(map);
				if (!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node", parentId);
					map.put("companyid", bridge.getCompanyid());

					List<ResourcesInfo> childrens = this.resourcesInfoDao.get(map);
					//父节点没有子节点要把父节点的leaf 设置为0
					if (childrens == null || childrens.size() == 0) {
						Map<String, Object> map2 = new HashMap<>();
						map2.put("year", bridge.getYear());
						map2.put("id", parentId);
						map2.put("value", "1");
						//父节点leaf 设置为0
						this.resourcesInfoDao.changeLeaf(map2);
					}
				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的id为空无法删除，请刷新后重试！", null);
		}
		return stateInfo;
	}

	@Override
	public StateInfo edit(String pkid, ResourcesInfo resourcesInfo, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(pkid) && resourcesInfo != null) {
			Map<String, Object> map = this.getBeanInfoMap(resourcesInfo, bridge);
			map.put("whereId", pkid);
			try {
				this.resourcesInfoDao.update(map);
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
				Logger.getLogger(this.getClass()).error(e.getMessage());
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的关键字段为空或者对象不存在，请刷新页面重试", null);
		}
		return stateInfo;
	}


	@Override
	public StateInfo add(String parentId, ResourcesInfo resourcesInfo, Bridge bridge) {
		//tree init
		resourcesInfo.setIsleaf(1);
		if (!CommonUtil.isEmpty(parentId)) {
			resourcesInfo.setParentid(parentId);
		} else {
			resourcesInfo.setParentid(Global.NULLSTRING);
		}
		//自动生成主键按1 101 10101规则
		Map<String, Object> mapOfID = this.resourcesInfoDao.getAutoGeneralID(parentId, bridge);
		String id = CommonUtil.dealPKRule(mapOfID, parentId);
		resourcesInfo.setPkid(id);
		resourcesInfo.setResid(id);
		resourcesInfo.setSort(resourcesInfo.getPkid());
		Map<String, Object> map = this.getBeanInfoMap(resourcesInfo, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(parentId)) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("year", bridge.getYear());
				map2.put("id", parentId);
				map2.put("value", "0");
				//父节点leaf 设置为0
				this.resourcesInfoDao.changeLeaf(map2);
			}
			this.resourcesInfoDao.insert(map);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			Logger.getLogger(this.getClass()).error(e.getMessage());
		}
		return stateInfo;
	}

	/**
	 * 初始化内置数据
	 */
	@Override
	public StateInfo autoInitFromAccess(Bridge bridge) {
		return sysService.initResource();
	}

	@Override
	public List<ResourcesInfo> queryAll() {
		return new ArrayList<>();
	}

	@Override
	public List<Map<String, Object>> getResZTree(Bridge bridge, String rolepkid, String userpkid) {
		List<Map<String, Object>> result = new ArrayList<>();
		//获取所有的树信息
		List<ResourcesInfo> resources = resourcesInfoDao.getAll();
		Map<String, String> ownInfo = null;
		if (CommonUtil.isNotEmpty(rolepkid)) {
			//获取角色拥有的菜单
			List<Map<String, Object>> ownResources = resourcesInfoDao.getResourcesByRole(rolepkid);
			ownInfo = CommonUtil.changetListToMap(ownResources, "respkid", "rolepkid");
		} else if (CommonUtil.isNotEmpty(userpkid)) {
			//获取用户拥有的菜单
			List<Map<String, Object>> ownResources = resourcesInfoDao.getResourcesByUser(userpkid);
			ownInfo = CommonUtil.changetListToMap(ownResources, "respkid", "userpkid");
		}
		//{ id:1, pId:0, name:"名称", open:true},
		for (ResourcesInfo info : resources) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", info.getPkid());
			map.put("pId", CommonUtil.nullToZero(info.getParentid()));
			map.put("name", info.getPkid() + " " + info.getName());
			if (!info.isLeaf()) {
				map.put("open", true);
			}
			//打勾选项
			if (ownInfo != null && info != null && CommonUtil.isNotEmpty(ownInfo.get(info.getPkid()))) {
				map.put("checked", true);
			}
			result.add(map);
		}
		return result;
	}

	@Override
	public List<ResourcesInfo> loadUserResources(String userpkid) {
		return new ArrayList<>();
	}
}