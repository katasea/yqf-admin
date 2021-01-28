package com.main.controller.platform;

import com.common.Global;
import com.main.service.platform.DeptInfoService;
import com.main.service.platform.UserInfoService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 备份当月数据线程
 */
public class BackupMonthDataThread implements Runnable{
	private DeptInfoService deptService;
	private UserInfoService userService;
	private String year_month; //eg:1990-09
	public BackupMonthDataThread(String year_month,DeptInfoService deptInfoService,UserInfoService userInfoService) {
		this.year_month = year_month;
		this.deptService = deptInfoService;
		this.userService = userInfoService;
	}
	@Override
	public void run() {
		try{
			Logger.getLogger(this.getClass()).info("开始备份部门和个人的数据，备份日期为："+year_month);
			//备份当前的数据到月份部门表中
			deptService.backupMonthData(year_month);
			//备份当前的数据到月份职员和部门对应关系表中。
			userService.backupMonthData(year_month);
			//标识这个月已经完成了备份
			Map<String,Boolean> map = Global.getDeptEmpRelaMonthBackupInfo();
			map.put(year_month,true);
			Global.setDeptEmpRelaMonthBackupInfo(map);
		}catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e);
		}
	}
}
