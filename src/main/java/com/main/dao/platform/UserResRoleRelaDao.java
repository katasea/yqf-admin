package com.main.dao.platform;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserResRoleRelaDao {
	/**
	 * 维护角色和资源的对应关系
	 *
	 * @param list [{rolepkid:xx,respkid:xx},...]
	 */
	void manageRoleRes(@Param("list") List<Map<String, String>> list);

	/**
	 * 维护角色和用户的对应关系
	 *
	 * @param list [{rolepkid:xx,userpkid:xx},...]
	 */
	void manageRoleUsers(@Param("list") List<Map<String, String>> list);

	/**
	 * 维护用户和资源的对应关系
	 *
	 * @param list [{respkid:xx,userpkid:xx},...]
	 */
	void manageUserRes(@Param("list") List<Map<String, String>> list);

	/**
	 * 删除角色和资源的关联关系
	 *
	 * @param keys 角色ids
	 */
	void deleteRoleRes(@Param("rolepkids") String keys);

	/**
	 * 删除用户角色对应关系
	 *
	 * @param userpkid 用户id
	 */
	void deleteUserRole(@Param("userpkid") String userpkid);

	/**
	 * 删除用户资源对应关系
	 *
	 * @param userpkid 用户id
	 */
	void deleteUserRes(@Param("userpkid") String userpkid);

}
