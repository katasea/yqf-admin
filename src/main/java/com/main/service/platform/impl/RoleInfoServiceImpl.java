package com.main.service.platform.impl;

import com.common.CommonUtil;
import com.common.Global;
import com.main.dao.platform.RoleInfoDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.RoleInfo;
import com.main.pojo.platform.StateInfo;
import com.main.service.platform.RoleInfoService;
import com.main.service.platform.UserResRoleRelaService;
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
public class RoleInfoServiceImpl implements RoleInfoService {
	@Resource
	private RoleInfoDao roleInfoDao;
	@Resource
	private UserResRoleRelaService u3rService;

	@Override
	public RoleInfo selectBypkid(int id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return this.roleInfoDao.selectBypkid(map);
	}

	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(roleInfoDao.validator(key, value, bridge))) {
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
	public List<Map<String, Object>> getUserRoleZTree(Bridge bridge, String userpkid) {
		List<Map<String, Object>> result = new ArrayList<>();
		//获取所有的树信息
		List<RoleInfo> roles = roleInfoDao.getAll();
		//获取用户关联的角色
		List<Map<String,Object>> userOwnRoles = roleInfoDao.getRoleByUserid(userpkid);
		//获取该用户所拥有的字典信息，拼装为map
		Map<String, String> ownInfo = CommonUtil.changetListToMap(userOwnRoles, "rolepkid", "userpkid");
		//循环拼凑所需的数据
		for (RoleInfo role : roles) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", role.getRoleid());
			map.put("pId", Global.NULLSTRING);
			map.put("name", role.getRoleid() + " " + role.getRoledesc());
			//打勾选项
			if (CommonUtil.isNotEmpty(ownInfo.get(role.getRoleid()))) {
				map.put("checked", true);
			}
			result.add(map);
		}
		return result;
	}

	@Override
	public int getCount(String keyword, String node, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.roleInfoDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@Override
	public List<RoleInfo> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		return this.roleInfoDao.getPage(map);
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
				this.roleInfoDao.deleteByPrimaryKey(map);
				if (!CommonUtil.isEmpty(parentId) && !"0".equals(parentId) && !"root".equals(parentId)) {
					map.put("node", parentId);
					map.put("companyid", bridge.getCompanyid());
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

	/**
	 * 获取bean 信息的map 用于传递给mybatis
	 */
	private Map<String, Object> getBeanInfoMap(RoleInfo roleInfo, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("pkid", roleInfo.getPkid());
		map.put("roleid", roleInfo.getRoleid());
		map.put("roledesc", roleInfo.getRoledesc());
		return map;
	}

	@Override
	public StateInfo edit(String pkid, RoleInfo roleInfo, Bridge bridge, String resRelas, String userRelas) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(pkid) && roleInfo != null) {
			Map<String, Object> map = this.getBeanInfoMap(roleInfo, bridge);
			map.put("whereId", pkid);
			try {
				this.roleInfoDao.update(map);
//				维护对应关系
				stateInfo = u3rService.manageRoleRes(bridge,pkid,resRelas);
				if(stateInfo.getFlag()) {
					stateInfo = u3rService.manageRoleUser(bridge,pkid,userRelas);
				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "传递的关键字段为空或者对象不存在，请刷新页面重试", null);
		}
		return stateInfo;
	}

	@Override
	public StateInfo add(RoleInfo roleInfo, Bridge bridge, String resRelas, String userRelas) {
		//tree init
		//自动生成主键按1 101 10101规则
		Map<String, Object> mapOfID = this.roleInfoDao.getAutoGeneralID(null, bridge);
		roleInfo.setPkid(CommonUtil.dealPKRule(mapOfID, null));
		Map<String, Object> map = this.getBeanInfoMap(roleInfo, bridge);
		StateInfo stateInfo = new StateInfo();
		try {
			this.roleInfoDao.insert(map);
//			维护对应关系
			stateInfo = u3rService.manageRoleRes(bridge,roleInfo.getPkid(),resRelas);
			if(stateInfo.getFlag()) {
				stateInfo = u3rService.manageRoleUser(bridge,roleInfo.getPkid(),userRelas);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}
}