package com.main.service.platform.impl;


import com.common.*;
import com.main.aop.LogAnnotation;
import com.main.dao.platform.*;
import com.main.pojo.platform.*;
import com.main.service.platform.BaseDicInfoService;
import com.main.service.platform.UserInfoService;
import com.main.service.platform.UserResRoleRelaService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象服务层实现类
 *
 * @author Tim[ATC.Pro Generate]
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private SystemDao sysDao;
	@Resource
	private UserResRoleRelaService u3rService;
	@Resource
	private UserResRoleRelaDao u3rDao;
	@Resource
	private BaseDicInfoService basDicService;
	@Resource
	private RoleInfoDao roleInfoDao;
	@Resource
	private DeptInfoDao deptInfoDao;

	@LogAnnotation(moduleName = "用户服务", option = "查找是否重复编号")
	@Override
	public StateInfo validator(String key, String value, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (!CommonUtil.isEmpty(userInfoDao.validator(key, value, bridge))) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "已录入重复的userid编号，请修改！", null);
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "获取用户数量")
	@Override
	public int getCount(String keyword, String node, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("year", bridge.getYear());
		map.put("yearmonth", bridge.getYmstr());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.userInfoDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@Override
	public int getCountWithAuth(String keyword, Bridge bridge, String authcode) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("authcode", authcode);
		map.put("userid", bridge.getUserid());
		map.put("yearmonth", bridge.getYmstr());
		Map<String, Object> resultMap = this.userInfoDao.getCount(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@LogAnnotation(moduleName = "用户服务", option = "获取用户列表")
	@Override
	public List<UserInfo> get(String keyword, String node, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("node", node);
		map.put("yearmonth", bridge.getYmstr());
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		List<UserInfo> users = this.userInfoDao.getPage(map);

		//获取岗位
//		Map<String, String> postMap = basDicService.getDicKeyValue(bridge, "3");
		//获取性别
		Map<String, String> sexMap = basDicService.getDicKeyValue(bridge, "2");
		//获取人员类型
		Map<String, String> uTypeMap = basDicService.getDicKeyValue(bridge, "4");
		//获取人员状态
		Map<String, String> uStatusMap = basDicService.getDicKeyValue(bridge, "5");

		for (UserInfo user : users) {
			user.setSexText(user.getSex() + " " + sexMap.get(user.getSex()));
			user.setUserstatusText(user.getUserstatus() + " " + uStatusMap.get(user.getUserstatus()));
			user.setUserstyleText(user.getUserstyle() + " " + uTypeMap.get(user.getUserstyle()));
//			user.setPostText(user.getPost()+" "+postMap.get(user.getPost()));
		}

		return users;
	}

	@Override
	public List<UserInfo> getWithAuth(String keyword, Bridge bridge, String start, String limit, String authcode) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("authcode", authcode);
		map.put("userid", bridge.getUserid());
		map.put("year", bridge.getYear());
		map.put("yearmonth", bridge.getYmstr());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		return this.userInfoDao.getPage(map);
	}

	@LogAnnotation(moduleName = "用户服务", option = "删除用户")
	@Override
	public StateInfo delete(String code, String parentId, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(code)) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("year", bridge.getYear());
				map.put("id", code);
				map.put("yearmonth",bridge.getYmstr());
				//顺序不能乱
				this.userInfoDao.deleteByPrimaryKey(map);
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
	private Map<String, Object> getBeanInfoMap(UserInfo userInfo, Bridge bridge) {
		userInfo.setCompanyid(bridge.getCompanyid());

		Map<String, Object> map = new HashMap<>();
		map.put("year", bridge.getYear());
		map.put("userid", userInfo.getUserid());
		map.put("phone", userInfo.getPhone());
		map.put("email", userInfo.getEmail());
		map.put("username", userInfo.getUsername());
		map.put("zjm", userInfo.getZjm());
		map.put("password", userInfo.getPassword());
		map.put("enable", userInfo.getEnable());
		map.put("birthday", userInfo.getBirthday());
		map.put("idcard", userInfo.getIdcard());
		map.put("sex", userInfo.getSex());
		map.put("post", userInfo.getPost());
		map.put("userstyle", userInfo.getUserstyle());
		map.put("userstatus", userInfo.getUserstatus());
		map.put("entrytime", userInfo.getEntrytime());
		map.put("deptid", userInfo.getDeptid());
		map.put("companyid", userInfo.getCompanyid());
		return map;
	}

	@LogAnnotation(moduleName = "用户服务", option = "修改用户")
	@Override
	public StateInfo edit(String userid, UserInfo userInfo, Bridge bridge, String roles, String res) {
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(userid) && userInfo != null) {
			Map<String, Object> map = this.getBeanInfoMap(userInfo, bridge);
			map.put("whereId", userid);
			map.put("year",bridge.getYear());
			map.put("yearmonth",bridge.getYmstr());
			try {
				this.userInfoDao.update(map);
				//增加角色对应
				stateInfo = u3rService.manageUserRela(bridge, userid, roles, 1);
				if (stateInfo.getFlag()) {
					//增加资源对应
					stateInfo = u3rService.manageUserRela(bridge, userid, res, 2);
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


	@LogAnnotation(moduleName = "用户服务", option = "新增用户")
	@Override
	public StateInfo add(UserInfo userInfo, Bridge bridge, String roles, String res) {
		//重新验证一下是否重复了编号
		StateInfo stateInfo = this.validator("userid", userInfo.getUserid(), bridge);
		//初始化密码为录入的生日日期  如：19900827
		userInfo.setPassword(userInfo.getBirthday().replace("-", Global.NULLSTRING));
		//加密
		PasswordHelper.encryptPassword(userInfo);
		if (stateInfo.getFlag()) {
			Map<String, Object> map = this.getBeanInfoMap(userInfo, bridge);
			map.put("year",bridge.getYear());
			map.put("yearmonth",bridge.getYmstr());
			try {
				this.userInfoDao.insert(map);
				//增加角色对应
				stateInfo = u3rService.manageUserRela(bridge, userInfo.getUserid(), roles, 1);
				if (stateInfo.getFlag()) {
					//增加资源对应
					stateInfo = u3rService.manageUserRela(bridge, userInfo.getUserid(), res, 2);
				}
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		}
		return stateInfo;
	}


	@LogAnnotation(moduleName = "用户服务", option = "获取权限归属用户的数量")
	@Override
	public int getCountOfUserMgrInfo(String keyword, String resourcePkid, String rolePkid, Bridge bridge) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("resourcePkid", resourcePkid);
		map.put("rolePkid", rolePkid);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		Map<String, Object> resultMap = this.userInfoDao.getCountOfUserMgrInfo(map);
		return Integer.parseInt(CommonUtil.nullToZero(String.valueOf(resultMap.get("count"))));
	}

	@LogAnnotation(moduleName = "用户服务", option = "获取权限归属用户的列表")
	@Override
	public List<UserInfo> getUserMgrInfo(String keyword, String resourcePkid, String rolePkid, Bridge bridge, String start, String limit) {
		Map<String, Object> map = new HashMap<>();
		keyword = CommonUtil.decodeKeyWord(this.getClass(), keyword);
		map.put("keyword", keyword);
		map.put("resourcePkid", resourcePkid);
		map.put("rolePkid", rolePkid);
		map.put("year", bridge.getYear());
		map.put("companyid", bridge.getCompanyid());
		map.put("start", start);
		map.put("end", Integer.parseInt(start) + Integer.parseInt(limit));
		return this.userInfoDao.getUserMgrInfoPage(map);
	}

	@LogAnnotation(moduleName = "人员服务", option = "查找用户")
	@Override
	public UserInfo selectByUserid(Bridge bridge, String userid) {
		UserInfo user = this.userInfoDao.selectByuserid(userid);
		if (bridge != null) {
			//获取岗位
//			Map<String, String> postMap = basDicService.getDicKeyValue(bridge, "3");
			//获取性别
			Map<String, String> sexMap = basDicService.getDicKeyValue(bridge, "2");
			//获取人员类型
			Map<String, String> uTypeMap = basDicService.getDicKeyValue(bridge, "4");
			//获取人员状态
			Map<String, String> uStatusMap = basDicService.getDicKeyValue(bridge, "5");
			user.setSexText(CommonUtil.getMV(sexMap,user.getSex()));
			user.setUserstatusText(CommonUtil.getMV(uStatusMap,user.getUserstatus()));
			user.setUserstyleText(CommonUtil.getMV(uTypeMap,user.getUserstyle()));
//			user.setPostText(postMap.get(user.getPost()));
		}
		return user;
	}

	@LogAnnotation(moduleName = "用户服务", option = "获取登录信息")
	@Override
	public void getLoginInfo(UserInfo user, LoginInfo loginInfo) {
		//获取登录用户角色信息列表
		List<RoleInfo> roles = sysDao.getRolesByUserid(user.getUserid());

		//获取登录用户菜单信息列表
		List<ResourcesInfo> resources = sysDao.getResourcesByUserid(user.getUserid());

		//获取单位信息
		Map<String, String> companyid_companynameMap = new HashMap<>();
		List<CompanyInfo> list = sysDao.getAllCompanyInfos();
		for (CompanyInfo cp : list) {
			companyid_companynameMap.put(cp.getCompanyid(), cp.getCompanyname());
		}

		//获取科室信息
		Map<String, String> deptid_deptnameMap = new HashMap<>();
		List<DeptInfo> depts = sysDao.getMxDeptInfos(loginInfo.getCompanyid());
		for (DeptInfo dept : depts) {
			if (dept.getIsstop() == 1) {
				deptid_deptnameMap.put(dept.getDeptid(), dept.getDeptname() + "【停用】");
			} else {
				deptid_deptnameMap.put(dept.getDeptid(), dept.getDeptname());
			}
		}
		loginInfo.setRoles(roles);
		loginInfo.setResources(resources);
		loginInfo.setCompanyname(CommonUtil.nullToStr(companyid_companynameMap.get(loginInfo.getCompanyid())));
		loginInfo.setDeptname(loginInfo.getDeptid() + " " + CommonUtil.nullToStr(deptid_deptnameMap.get(loginInfo.getDeptid())));

	}

	@LogAnnotation(moduleName = "用户服务", option = "添加用户资源权限")
	@Override
	public StateInfo addUserResAuth(Bridge bridge, String resourcePkid, String userpkid) {
		StateInfo stateInfo = new StateInfo();
		try {
			//若记录不存在再进行新增权限
			if (userInfoDao.getCountOfUserResAuth(bridge, resourcePkid, userpkid) == 0) {
				userInfoDao.addAuthFromUserRes(userpkid, resourcePkid);
			} else {
				stateInfo.setMsg(this.getClass(), "保存成功，但权限早已存在", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "删除用户角色资源权限")
	@Override
	public StateInfo delUserResRoleAuth(Bridge bridge, String resourcepkid, String rolepkid, String userpkid, String resfrom) {
		StateInfo stateInfo = new StateInfo();
		try {
			if (resfrom.equals(Global.RESFROM_ROLE)) {
				//如果角色是空，说明要通过资源来删除用户和角色的对应
				if (CommonUtil.isEmpty(rolepkid)) {
					userInfoDao.removeAuthFromUserRoleByRes(userpkid, resourcepkid);
				} else {
					userInfoDao.removeAuthFromUserRoleByRole(userpkid, rolepkid);
				}
			} else if (resfrom.equals(Global.RESFROM_USER)) {
				userInfoDao.removeAuthFromUserRes(userpkid, resourcepkid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "导入EXCEL用户信息")
	@Override
	public StateInfo importData(File targetFile, Bridge bridge) {
		HSSFWorkbook wb;
		StateInfo stateInfo = new StateInfo();
		try {
			//1. 获取已有的所有账号，防止重复添加。
			List<UserInfo> existUserList = this.getAllUsers(bridge);
			Map<String, String> existUser = new HashMap<>();
			for (UserInfo user : existUserList) {
				existUser.put(user.getUserid(), user.getUsername());
			}
			//2. 获取已有的角色，防止角色id填写错误,获取部门号码，防止部门id填写错误。
			//获取所有的树信息
			List<RoleInfo> existRoleList = roleInfoDao.getAll();
			//已有的角色
			Map<String, String> existRoleMap = new HashMap<>();
			for (RoleInfo role : existRoleList) {
				existRoleMap.put(role.getRoleid(), role.getRoledesc());
			}
			List<DeptInfo> existDeptList = deptInfoDao.getLeafs(bridge.getCompanyid());
			//已有的部门
			Map<String, String> existDeptMap = new HashMap<>();
			for (DeptInfo dept : existDeptList) {
				existDeptMap.put(dept.getDeptid(), dept.getDeptname());
			}
			//3. 获取已有的字典，防止字典输入错误。
			//获取岗位
			Map<String, String> postMap = basDicService.getDicKeyValue(bridge, "3");
			//获取性别
			Map<String, String> sexMap = basDicService.getDicKeyValue(bridge, "2");
			//获取人员类型
			Map<String, String> uTypeMap = basDicService.getDicKeyValue(bridge, "4");
			//获取人员状态
			Map<String, String> uStatusMap = basDicService.getDicKeyValue(bridge, "5");

			//开始解析excel数据
			List<UserInfo> users = new ArrayList<>();
			wb = new HSSFWorkbook(new FileInputStream(targetFile));
			HSSFSheet sheet = wb.getSheetAt(0);
			int beginIndex = 1; // 数据的起始行
			int rows = sheet.getLastRowNum();
			int j;
			List<Map<String, String>> userRoles = new ArrayList<>();
			for (int i = beginIndex; i <= rows; i++) {
				j = 0;
				UserInfo user = new UserInfo();
				user.setUserid(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getUserid())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户编号为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				if (CommonUtil.isNotEmpty(existUser.get(user.getUserid()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户编号已存在，请校对编辑后重新上传", null);
					return stateInfo;
				}

				user.setUsername(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getUsername())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户名称为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setZjm(ZjmUtil.generateZJM(user.getUsername()));
				//如果填写的状态不是123，那么默认为2锁定
				user.setEnable(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getEnable())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(),
							"第" + (i + 1) + "行，第" + j + "列，用户状态为空，可以填写【1启用 2锁定无法登录 3删除状态】，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setDeptid(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getDeptid())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户编号为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				if (CommonUtil.isEmpty(existDeptMap.get(user.getDeptid()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，部门编号未存在，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setPhone(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getPhone())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，电话号码为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setEmail(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getEmail())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，邮箱为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setBirthday(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getBirthday())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，生日为空，请校对编辑后重新上传", null);
					return stateInfo;
				} else {
					try {
						Global.year_month_day.parse(user.getBirthday());
					} catch (Exception e) {
						stateInfo.setFlag(false);
						stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，日期格式不对！统一格式为：2018-03-14，请校对编辑后重新上传", null);
						return stateInfo;
					}
				}
				user.setIdcard(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				user.setSex(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getSex())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户性别为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				if (CommonUtil.isEmpty(sexMap.get(user.getSex()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，性别编号未存在，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setPost(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getPost())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户岗位为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				if (CommonUtil.isEmpty(postMap.get(user.getPost()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，用户岗位编号未存在，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setUserstyle(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getUserstyle())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，人员类型为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				if (CommonUtil.isEmpty(uTypeMap.get(user.getUserstyle()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，人员类型编号未存在，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setUserstatus(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getUserstatus())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，人员状态为空，请校对编辑后重新上传", null);
					return stateInfo;
				}
				if (CommonUtil.isEmpty(uStatusMap.get(user.getUserstatus()))) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，人员状态编号未存在，请校对编辑后重新上传", null);
					return stateInfo;
				}
				user.setEntrytime(CommonUtil.getCellValue(sheet.getRow(i).getCell(j++)));
				if (CommonUtil.isEmpty(user.getEntrytime())) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，入职时间为空，请校对编辑后重新上传", null);
					return stateInfo;
				} else {
					try {
						Global.year_month_day.parse(user.getEntrytime());
					} catch (Exception e) {
						stateInfo.setFlag(false);
						stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，日期格式不对！统一格式为：2018-03-14，请校对编辑后重新上传", null);
						return stateInfo;
					}
				}

				user.setCompanyid(bridge.getCompanyid());
				//默认密码等于生日 19900827
				user.setPassword(user.getBirthday().replace("-", Global.NULLSTRING));
				//密码加密
				PasswordHelper.encryptPassword(user);
				users.add(user);
				//auth
				String roles = CommonUtil.getCellValue(sheet.getRow(i).getCell(j));
				for (String role : roles.split(",")) {
					if (CommonUtil.isEmpty(existRoleMap.get(role))) {
						stateInfo.setFlag(false);
						stateInfo.setMsg(this.getClass(), "第" + (i + 1) + "行，第" + j + "列，角色编号未存在，请校对编辑后重新上传", null);
						return stateInfo;
					}
					Map<String, String> map = new HashMap<>();
					map.put("rolepkid", role);
					map.put("userpkid", user.getUserid());
					userRoles.add(map);
				}
			}
			//save users
			userInfoDao.insertUsers(users);
			//重新备份当前月份数据
			userInfoDao.backupMonthData(bridge.getYmstr(),bridge.getYear());
			//save auth
			u3rDao.manageRoleUsers(userRoles);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		} finally {
			if(!targetFile.delete()) {
				targetFile.deleteOnExit();
			}
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "导出用户模板")
	@Override
	public StateInfo exportExcelTemplate(OutputStream os, Bridge bridge) {
		StateInfo stateInfo = new StateInfo();
		String sheetName = "用户信息";   //导出的Excel名称
		String[] headers = {"*账号", "*姓名", "*账号状态", "*科室ID", "*电话", "*邮箱",
				"*出生日期[xxxx-xx-xx]", "身份证", "*性别[业务编号]", "*岗位[业务编号]", "*职员类型[业务编号]", "*职员状态[业务编号]", "*入职时间[xxxx-xx-xx]", "*角色编号[xx,xx]"};
		String[] columns = {"userid", "username", "enable", "deptname", "phone",
				"email", "birthday", "idcard", "sex", "post", "userstyle", "userstatus", "entrytime", "resfrom"};
		List<UserInfo> users = new ArrayList<>();
		UserInfo user = new UserInfo();
		user.setUserid("00001");
		user.setUsername("模板举例用户");
		user.setEnable("1[正常]/2[锁定]/3[删除]");
		user.setDeptname("填科室ID:1010101");
		user.setPhone("18650093333");
		user.setEmail("xxxx@xx.xx");
		user.setBirthday("1990-09-09");
		user.setIdcard("350521xxxxxx");
		user.setSex("1[男]2[女]");
		user.setPost("1");
		user.setUserstyle("1");
		user.setUserstatus("1");
		user.setEntrytime("2019-09-09");
		user.setResfrom("001[多角色：001,002]");
		users.add(user);
		ExportExcelUtil<UserInfo> util = new ExportExcelUtil<>();
		try {
			util.export(sheetName, headers, columns, users, os);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "导出用户EXCEL数据")
	@Override
	public StateInfo exportData(OutputStream os, Bridge bridge,String keyword) {
		StateInfo stateInfo = new StateInfo();
		String sheetName = "用户信息";   //导出的Excel名称
		String[] headers = {"账号", "姓名", "账号状态", "科室名称", "电话", "邮箱", "出生日期", "身份证", "性别", "岗位", "职员类型", "职员状态", "入职时间"};
		String[] columns = {"userid", "username", "enable", "deptname", "phone",
				"email", "birthday", "idcard", "sexText", "postText", "userstyleText", "userstatusText", "entrytime"};
		List<UserInfo> users = this.get(keyword, null, bridge, "0", "999999999");
		ExportExcelUtil<UserInfo> util = new ExportExcelUtil<>();
		try {
			util.export(sheetName, headers, columns, users, os);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "修改密码")
	@Override
	public StateInfo updatePassword(String password, String userid, Bridge bridge) {
		UserInfo userInfo = userInfoDao.selectByuserid(userid);
		StateInfo stateInfo = new StateInfo();
		//密码不为空就修改密码，否则就重置密码
		if (CommonUtil.isNotEmpty(password)) {
			userInfo.setPassword(password);
			//密码加密
			PasswordHelper.encryptPassword(userInfo);
			try {
				userInfoDao.updatePassword(userInfo);
			} catch (Exception e) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), e.getMessage(), e);
			}
		} else {
			if (CommonUtil.isEmpty(userInfo.getBirthday())) {
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(), "此用户的出生日期为空，无法重置密码，请修改出生日期后再重置密码！", null);
			} else {
				userInfo.setPassword(userInfo.getBirthday().replace("-", Global.NULLSTRING));
				//密码加密
				PasswordHelper.encryptPassword(userInfo);
				try {
					userInfoDao.updatePassword(userInfo);
				} catch (Exception e) {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), e.getMessage(), e);
				}
			}
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "修改个人账户信息")
	@Override
	public StateInfo editProfile(UserInfo userInfo) {
		StateInfo stateInfo = new StateInfo();
		try {
			userInfoDao.editProfile(userInfo);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@LogAnnotation(moduleName = "用户服务", option = "登录socket记录")
	@Override
	public StateInfo editSocketInfo(UserInfo userInfo) {
		StateInfo stateInfo = new StateInfo();
		try {
			userInfoDao.editSocketInfo(userInfo);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	@Override
	public void backupMonthData(String year_month) {
		userInfoDao.backupMonthData(year_month,year_month.split("-")[0]);
	}

	private List<UserInfo> getAllUsers(Bridge bridge) {
		return userInfoDao.getAll(bridge.getCompanyid());
	}
}