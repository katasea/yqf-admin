package com.main.service.platform;

import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.UserInfo;

import java.util.Map;

public interface SystemService {

	/**
	 * 初始化系统
	 * @param userInfo
	 * @return
	 */
	StateInfo init(UserInfo userInfo);

	/**
	 * 从Access初始化菜单
	 * @return
	 */
	StateInfo initResource();

	/**
	 * 从Access初始化角色
	 * @return
	 */
	StateInfo initRole();

	/**
	 * 从Access初始化角色和菜单关联
	 * @return
	 */
	StateInfo initRoleResRela();

	/**
	 * 从Access初始化用户和角色关联
	 * @return
	 */
	StateInfo initUserRoleRela();

	Map<String,Boolean> getDeptEmpRelaMonthBackupInfo();
}
