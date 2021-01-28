package com.main.service.prfm;

import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.AuthCenter;

import java.util.List;
import java.util.Map;

/**
 * 权限配置中心
 */
public interface AuthCenterService {

	/**
	 * 从Access获取权限列表
	 *
	 * @return 权限列表
	 */
	List<Map<String, Object>> getList4Type(String funcode);

	/**
	 * 获取待分配数据列表
	 *
	 * @param keyword 关键词
	 * @param bridge  桥梁信息
	 * @param start   起始
	 * @param limit   每页限制
	 * @return 结果信息
	 */
	Map<String, Object> getMorigList(String keyword, String funcode, Bridge bridge, String start, String limit);


	/**
	 * 获取待关联数据列表
	 *
	 * @param keyword 关键词
	 * @param bridge  桥梁信息
	 * @param start   起始
	 * @param limit   每页限制
	 * @return 结果信息
	 */
	Map<String, Object> getMtargList(String keyword, String funcode, Bridge bridge, String start, String limit, String morig);

	/**
	 * 获取权限关联信息列表
	 *
	 * @param keyword 关键词
	 * @param bridge  桥梁信息
	 * @param start   起始
	 * @param limit   每页限制
	 * @param morig   待分配
	 * @return 结果信息
	 */
	List<Map<String, Object>> getAuthList(String keyword, String funcode, Bridge bridge, String morig, String start, String limit);

	/**
	 * 获取权限关联总量
	 *
	 * @param keyword 关键词
	 * @param bridge  桥梁信息
	 * @param morig   待分配
	 * @return 结果信息
	 */
	int getAuthCount(String keyword, String funcode, Bridge bridge, String morig);

	/**
	 * 新增权限关联
	 *
	 * @param bridge     桥梁信息
	 * @param authCenter 关联信息
	 * @return 状态信息
	 */
	StateInfo addAuth(Bridge bridge, AuthCenter authCenter);

	/**
	 * 删除权限关联
	 *
	 * @param bridge 桥梁信息
	 * @param pkid   主键
	 * @return 状态信息
	 */
	StateInfo delAuth(Bridge bridge, String pkid);

	StateInfo delAuthByMorig(Bridge bridge, String morig);
}
