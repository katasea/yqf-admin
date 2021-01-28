package com.main.service.platform;

import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;

/**
 * 用于服务用户，角色，资源菜单关联关系
 */
public interface UserResRoleRelaService {
	/**
	 * 管理维护角色和资源的关系[自动删除rolepkids并重新插入信息]
	 *
	 * @param bridge
	 * @param rolepkids 可以是单个编号也可以是一组编号即 1,2,3
	 * @param respkids  可以是单个编号也可以是一组编号即 1,2,3
	 * @return
	 */
	StateInfo manageRoleRes(Bridge bridge, String rolepkids, String respkids);

	/**
	 * 管理维护用户和角色的关系[不自动删除记录仅插入信息]
	 *
	 * @param bridge
	 * @param rolepkids 可以是单个编号也可以是一组编号即 1,2,3
	 * @param userpkids 可以是单个编号也可以是一组编号即 1,2,3
	 * @return
	 */
	StateInfo manageRoleUser(Bridge bridge, String rolepkids, String userpkids);

	/**
	 * 管理单用户和【角色或菜单】对应关系[自动删除原userpkid所有对应]
	 *
	 * @param bridge 桥梁对象
	 * @param userpkid 用户id 单用户
	 * @param pkids 角色ids 即 01,02,03 或资源ids 即 99,9901
	 * @param flag 1角色维护 2资源菜单维护
	 * @return
	 */
	StateInfo manageUserRela(Bridge bridge, String userpkid, String pkids, int flag);

}
