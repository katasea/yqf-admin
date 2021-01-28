package com.main.service.platform;

import com.main.pojo.platform.RoleInfo;
import com.main.pojo.platform.StateInfo;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.Bridge;

/**
 * 角色信息管理服务类
 *
 * @author 陈富强
 */
public interface RoleInfoService {

	/**
	 * 通过主键获取角色信息
	 *
	 * @param id
	 * @return 角色信息
	 */
	public RoleInfo selectBypkid(int id);

	/**
	 * 获取所有角色列表
	 *
	 * @param keyword 关键字
	 * @param node    当前节点【列表展示忽略此参数】
	 * @param bridge  系统信息
	 * @param start   起始页
	 * @param limit   每页显示行数
	 * @return 角色集合
	 */
	public List<RoleInfo> get(String keyword, String node, Bridge bridge, String start, String limit);

	/**
	 * 获取角色列表总记录数
	 *
	 * @param keyword 关键字
	 * @param node    当前节点【列表展示忽略此参数】
	 * @param bridge  系统信息
	 * @return 记录数
	 */
	public int getCount(String keyword, String node, Bridge bridge);

	/**
	 * 删除角色
	 *
	 * @param code     主键
	 * @param parentId 父节点
	 * @param bridge   系统信息
	 * @return 结果状态
	 */
	public StateInfo delete(String code, String parentId, Bridge bridge);

	/**
	 * 修改角色
	 *
	 * @param code      主键
	 * @param roleInfo  角色描述
	 * @param bridge    系统信息
	 * @param resRelas  关联的资源信息
	 * @param userRelas 关联的用户信息
	 * @return 结果状态
	 */
	public StateInfo edit(String code, RoleInfo roleInfo, Bridge bridge, String resRelas, String userRelas);

	/**
	 * 新增角色
	 *
	 * @param roleInfo  角色描述
	 * @param bridge    系统信息
	 * @param resRelas  关联的资源信息
	 * @param userRelas 关联的用户信息
	 * @return 结果状态
	 */
	public StateInfo add(RoleInfo roleInfo, Bridge bridge, String resRelas, String userRelas);

	/**
	 * 验证角色编号
	 *
	 * @param key    编号
	 * @param value  值
	 * @param bridge 系统信息
	 * @return 结果状态
	 */
	public StateInfo validator(String key, String value, Bridge bridge);

	/**
	 * 获取用户关联角色的ztree数据
	 *
	 * @param bridge 桥梁对象
	 * @param userpkid 用户id
	 * @return ztree的对象集
	 */
	List<Map<String,Object>> getUserRoleZTree(Bridge bridge, String userpkid);
}