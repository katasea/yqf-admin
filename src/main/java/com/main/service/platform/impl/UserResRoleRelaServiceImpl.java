package com.main.service.platform.impl;

import com.common.CommonUtil;
import com.main.dao.platform.UserResRoleRelaDao;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.service.platform.UserResRoleRelaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于服务用户，角色，资源菜单关联关系
 */
@Service
@Transactional
public class UserResRoleRelaServiceImpl implements UserResRoleRelaService {
	@Resource
	private UserResRoleRelaDao u3rDao;

	@Override
	public StateInfo manageRoleRes(Bridge bridge, String rolepkids, String respkids) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (CommonUtil.isEmpty(rolepkids)) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "传递的参数为空无法维护角色资源对应关系", null);
			}
			String[] roleArray = rolepkids == null ? new String[0] : rolepkids.split(",");
			String[] resArray = respkids == null ? new String[0] : respkids.split(",");
			List<Map<String, String>> list = new ArrayList<>();
			StringBuffer keys = new StringBuffer();
			for (String rolepkid : roleArray) {
				for (String respkid : resArray) {
					Map<String, String> map = new HashMap<>();
					map.put("rolepkid", rolepkid);
					map.put("respkid", respkid);
					list.add(map);

					if (keys.length() == 0) {
						keys.append("'").append(rolepkid).append("'");
					} else {
						keys.append(",").append("'").append(rolepkid).append("'");
					}
				}
			}
			//删除角色对应的对应关系信息
			u3rDao.deleteRoleRes(keys.toString());
			//批量插入到数据库中进行维护
			if (list.size() != 0) u3rDao.manageRoleRes(list);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public StateInfo manageRoleUser(Bridge bridge, String rolepkids, String userpkids) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (CommonUtil.isEmpty(rolepkids)) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "传递的参数为空无法维护角色资源对应关系", null);
				return stateInfo;
			}
			if (CommonUtil.isEmpty(userpkids)) {
				return stateInfo;
			}
			String[] roleArray = rolepkids.split(",");
			String[] userArray = userpkids.split(",");
			List<Map<String, String>> list = new ArrayList<>();
			StringBuffer keys = new StringBuffer();
			for (String rolepkid : roleArray) {
				for (String userpkid : userArray) {
					Map<String, String> map = new HashMap<>();
					map.put("rolepkid", rolepkid);
					map.put("userpkid", userpkid);
					list.add(map);

					if (keys.length() == 0) {
						keys.append("'").append(rolepkid).append("'");
					} else {
						keys.append(",").append("'").append(rolepkid).append("'");
					}
				}
			}

			//批量插入到数据库中进行维护
			u3rDao.manageRoleUsers(list);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public StateInfo manageUserRela(Bridge bridge, String userpkid, String pkids, int flag) {
		StateInfo stateInfo = new StateInfo();
		if (!"NOCHANGE".equals(pkids)) {
			try {
				if (CommonUtil.isEmpty(userpkid)) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "传递的参数为空无法维护用户对应关系", null);
				}
				String[] pkidsArray = pkids.split(",");
				List<Map<String, String>> list = new ArrayList<>();
				for (String pkid : pkidsArray) {
					Map<String, String> map = new HashMap<>();
					if (flag == 1) {
						map.put("rolepkid", pkid);
					} else {
						map.put("respkid", pkid);
					}
					map.put("userpkid", userpkid);
					list.add(map);
				}
				if (flag == 1) {
					//删除角色对应的对应关系信息
					u3rDao.deleteUserRole(userpkid);
					//批量插入到数据库中进行维护
					if (list.size() != 0) u3rDao.manageRoleUsers(list);
				} else {
					//删除角色对应的对应关系信息
					u3rDao.deleteUserRes(userpkid);
					//批量插入到数据库中进行维护
					if (list.size() != 0) u3rDao.manageUserRes(list);
				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		}
		return stateInfo;
	}

}
