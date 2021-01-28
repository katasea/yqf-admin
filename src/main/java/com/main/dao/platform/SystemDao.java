package com.main.dao.platform;


import com.main.pojo.platform.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台系统公共部分
 */
public interface SystemDao {
	/**
	 * 初始化单位信息
	 *
	 * @param companyid   单位编号
	 * @param companyname 单位名称
	 */
	void initCompany(@Param("companyid") String companyid, @Param("companyname") String companyname);

	/**
	 * 初始化管理员信息
	 * 密码要加密不然无法登陆
	 *
	 * @param userInfo 用户信息
	 */
	void initAdminUser(@Param("userInfo") UserInfo userInfo);

	/**
	 * 通过登录账号获取用户信息
	 *
	 * @param userid 用户账号
	 * @return 用户信息
	 */
	UserInfo getUserByUserid(@Param("userid") String userid);
	/**
	 * 通过登录账号获取角色信息
	 *
	 * @param userid 用户账号
	 * @return 角色集合
	 */
	List<RoleInfo> getRolesByUserid(@Param("userid") String userid);

	/**
	 * 通过登录账号获取菜单信息
	 *
	 * @param userid 用户账号
	 * @return 菜单资源集合
	 */
	List<ResourcesInfo> getResourcesByUserid(@Param("userid") String userid);

	/**
	 * 获取所有单位信息
	 *
	 * @return 单位集合
	 */
	List<CompanyInfo> getAllCompanyInfos();

	/**
	 * 获取明细部门的信息
	 *
	 * @param companyid 单位编号
	 * @return 明细部门集合
	 */
	List<DeptInfo> getMxDeptInfos(@Param("companyid") String companyid);
}
