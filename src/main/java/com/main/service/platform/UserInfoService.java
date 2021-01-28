package com.main.service.platform;

import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.LoginInfo;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.UserInfo;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface UserInfoService {

	/**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
	List<UserInfo> get(String keyword, String node, Bridge bridge, String start, String limit);

	/**
	 * 获取当前条件下的对象数量
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 数量
	 */
	int getCount(String keyword, String node, Bridge bridge);

	/**
	 * 获取用户列表附带权限
	 *
	 * @param keyword  关键检索词
	 * @param bridge   桥梁对象
	 * @param start    开始
	 * @param limit    结束
	 * @param authcode 权限代码
	 * @return 用户列表
	 */
	List<UserInfo> getWithAuth(String keyword, Bridge bridge, String start, String limit, String authcode);

	/**
	 * 获取用户列表数量
	 *
	 * @param keyword  关键检索词
	 * @param bridge   桥梁对象
	 * @param authcode 权限代码
	 * @return 数量
	 */
	int getCountWithAuth(String keyword, Bridge bridge, String authcode);

	/**
	 * 删除对象
	 *
	 * @param code     主键信息
	 * @param parentId 父节点信息
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo delete(String code, String parentId, Bridge bridge);

	/**
	 * 修改对象
	 *
	 * @param code     主键信息
	 * @param userInfo 修改对象
	 * @param bridge   桥梁对象
	 * @param roles    关联角色 01,02,03
	 * @param res      关联资源 99,9901,9902
	 * @return 状态对象
	 */
	StateInfo edit(String code, UserInfo userInfo, Bridge bridge, String roles, String res);

	/**
	 * 新增对象
	 *
	 * @param userInfo 新增对象
	 * @param bridge   桥梁对象
	 * @param roles    关联角色 01,02,03
	 * @param res      关联资源 99,9901,9902
	 * @return 状态对象
	 */
	StateInfo add(UserInfo userInfo, Bridge bridge, String roles, String res);

	/**
	 * 验证当前key是否存在重复记录value
	 *
	 * @param key    数据库字段
	 * @param value  数据库值
	 * @param bridge 桥梁对象
	 * @return 状态对象
	 */
	StateInfo validator(String key, String value, Bridge bridge);

	/**
	 * 获取所属用户查看对应菜单资源的信息
	 *
	 * @param keyword      关键字
	 * @param resourcePkid 资源id
	 * @param rolePkid     角色id
	 * @param bridge       桥梁对象
	 * @param start        开始页数
	 * @param limit        一行多少条记录
	 * @return 用户集
	 */
	List<UserInfo> getUserMgrInfo(String keyword, String resourcePkid, String rolePkid, Bridge bridge, String start, String limit);

	/**
	 * 获取所属用户查看对应菜单资源的信息总记录
	 *
	 * @param keyword      关键字
	 * @param resourcePkid 资源id
	 * @param rolePkid     角色id
	 * @param bridge       桥梁对象
	 * @return 记录数
	 */
	int getCountOfUserMgrInfo(String keyword, String resourcePkid, String rolePkid, Bridge bridge);

	/**
	 * 通过账号获取用户信息
	 *
	 * @param bridge 桥梁对象
	 * @param userid 用户账号
	 * @return 用户对象
	 */
	UserInfo selectByUserid(Bridge bridge, String userid);

	/**
	 * 登录的用户获取所有角色和菜单放置于loginInfo中。
	 *
	 * @param user      用户对象
	 * @param loginInfo 登录信息
	 */
	void getLoginInfo(UserInfo user, LoginInfo loginInfo);

	/**
	 * 删除所属用户查看对应菜单资源的权限
	 *
	 * @param bridge       桥梁对象
	 * @param resourcepkid 资源id
	 * @param rolepkid     角色id
	 * @param userpkid     用户id
	 * @param resfrom      权限出处
	 * @return 状态信息
	 */
	StateInfo delUserResRoleAuth(Bridge bridge, String resourcepkid, String rolepkid, String userpkid, String resfrom);

	/**
	 * 增加用户对应资源的权限
	 *
	 * @param bridge       桥梁对象
	 * @param resourcepkid 资源id
	 * @param userpkid     用户id
	 * @return 状态信息
	 */
	StateInfo addUserResAuth(Bridge bridge, String resourcepkid, String userpkid);

	/**
	 * 导入上传的EXCEL数据
	 *
	 * @param targetFile 上传的临时EXCEL文件
	 * @param bridge     桥梁对象
	 * @return 状态信息
	 */
	StateInfo importData(File targetFile, Bridge bridge);

	/**
	 * 生成上传EXCEL模板
	 *
	 * @param os     输出源
	 * @param bridge 桥梁信息
	 * @return 状态信息
	 */
	StateInfo exportExcelTemplate(OutputStream os, Bridge bridge);

	/**
	 * 导出数据
	 *
	 * @param os     输出源
	 * @param bridge 桥梁信息
	 * @return 状态信息
	 */
	StateInfo exportData(OutputStream os, Bridge bridge, String keyword);

	/**
	 * 修改用户密码
	 *
	 * @param password 密码
	 * @param userid   用户账号
	 * @param bridge   桥梁对象
	 * @return 状态信息
	 */
	StateInfo updatePassword(String password, String userid, Bridge bridge);

	/**
	 * 保存个人账户信息
	 *
	 * @param userInfo
	 * @return
	 */
	StateInfo editProfile(UserInfo userInfo);

	/**
	 * 保存socket登录信息
	 *
	 * @param userInfo
	 * @return
	 */
	StateInfo editSocketInfo(UserInfo userInfo);

	/**
	 * 自动备份当月数据，先删除后插入
	 *
	 * @param year_month eg: 1990-09
	 */
	void backupMonthData(String year_month);
}