package com.main.dao.platform;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.UserInfo;
import org.apache.ibatis.annotations.Param;
import com.main.pojo.platform.Bridge;

/**
 * 对象持久层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface UserInfoDao {

	/**
	 * 获取树形对应节点数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return 对象集
	 */
	List<UserInfo> get(Map<String, Object> map);

	/**
	 * 获取对应数量
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return map {count:11}
	 */
	Map<String, Object> getCount(Map<String, Object> map);

	/**
	 * 获取列表数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:'',start:1,end:10}
	 * @return 对象集
	 */
	List<UserInfo> getPage(Map<String, Object> map);

	/**
	 * 获取所有数据包括停用
	 *
	 * @param companyid 单位id
	 * @return 对象集
	 */
	List<UserInfo> getAll(@Param("companyid") String companyid);

	/**
	 * 插入对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
	int insert(Map<String, Object> map);

	/**
	 * 修改对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
	int update(Map<String, Object> map);

	/**
	 * 通过主键删除对象
	 *
	 * @param map {year:2018,id:''}
	 * @return 无
	 */
	int deleteByPrimaryKey(Map<String, Object> map);


	/**
	 * 通过主键获取对象
	 *
	 * @param userpkid 用户id
	 * @return 对象
	 */
	UserInfo selectByuserid(@Param("userpkid") String userpkid);

	/**
	 * 验证数据是否存在，返回具体数据
	 *
	 * @param key    字段
	 * @param value  值
	 * @param bridge 桥梁对象
	 * @return 查询结果
	 */
	List<Map<String, Object>> validator(@Param("key") String key, @Param("value") String value, @Param("bridge") Bridge bridge);

	/**
	 * 从数据看获取最大id用于自动计算生成id
	 *
	 * @param parentId 父节点
	 * @param bridge   桥梁对象
	 * @return map {id:''}
	 */
	Map<String, Object> getAutoGeneralID(@Param("parentId") String parentId, @Param("bridge") Bridge bridge);

	/**
	 * 删除所有记录
	 *
	 * @param year 年
	 */
	void deleteAll(@Param("year") String year,@Param("yearmonth") String yearmonth);

	List<UserInfo> getUserMgrInfoPage(Map<String, Object> map);

	Map<String, Object> getCountOfUserMgrInfo(Map<String, Object> map);

	/**
	 * 通过角色来移除用户角色对应关系
	 *
	 * @param userpkid 用户账号
	 * @param rolepkid 角色主键
	 */
	void removeAuthFromUserRoleByRole(@Param("userpkid") String userpkid, @Param("rolepkid") String rolepkid);

	/**
	 * 通过资源来移除用户角色对应关系
	 *
	 * @param userpkid     用户账号
	 * @param resourcepkid 资源主键
	 */
	void removeAuthFromUserRoleByRes(@Param("userpkid") String userpkid, @Param("resourcepkid") String resourcepkid);

	/**
	 * 移除用户资源关联权限
	 *
	 * @param userpkid     用户账号
	 * @param resourcepkid 资源主键
	 */
	void removeAuthFromUserRes(@Param("userpkid") String userpkid, @Param("resourcepkid") String resourcepkid);

	/**
	 * 新增用户资源关联权限
	 *
	 * @param userpkid     用户账号
	 * @param resourcepkid 资源主键
	 */
	void addAuthFromUserRes(@Param("userpkid") String userpkid, @Param("resourcepkid") String resourcepkid);

	/**
	 * 获取当前参数条件下的关联数量，用于判断当前参数条件下是否有记录
	 *
	 * @param bridge       桥梁对象
	 * @param resourcePkid 资源主键
	 * @param userpkid     用户主键
	 * @return 数量
	 */
	int getCountOfUserResAuth(@Param("bridge") Bridge bridge, @Param("resourcepkid") String resourcePkid, @Param("userpkid") String userpkid);

	/**
	 * 批量新增用户
	 *
	 * @param users 用户集合
	 */
	void insertUsers(@Param("users") List<UserInfo> users);

	/**
	 * 修改密码
	 *
	 * @param userInfo 用户对象
	 */
	void updatePassword(@Param("userInfo") UserInfo userInfo);

	/**
	 * 保存个人信息，仅修改部分字段。
	 *
	 * @param userInfo 用户对象
	 */
	void editProfile(@Param("userInfo") UserInfo userInfo);

	/**
	 * 保存个人socket信息
	 *
	 * @param userInfo 用户对象
	 */
	void editSocketInfo(@Param("userInfo") UserInfo userInfo);

	/**
	 * 先删除后插入
	 * 备份数据到user_dept_rela
	 *
	 * @param year_month eg : 1990-09
	 */
	void backupMonthData(@Param("year_month") String year_month,@Param("year") String year);
}
