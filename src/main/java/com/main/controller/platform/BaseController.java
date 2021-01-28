package com.main.controller.platform;

import com.common.CommonUtil;
import com.common.Global;
import com.main.pojo.platform.*;
import com.main.service.platform.DeptInfoService;
import com.main.service.platform.NoticeInfoService;
import com.main.service.platform.SystemService;
import com.main.service.platform.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础控制层，用于过滤一些通用的连接
 * Created by CFQ on 2017/9/26.
 */
@Controller
public class BaseController {

	/**
	 * 系统服务类
	 */
	@Resource
	private SystemService sysService;
	/**
	 * 用户服务类
	 */
	@Resource
	private UserInfoService userInfoService;

	/**
	 * 部门服务类
	 */
	@Resource
	private DeptInfoService deptInfoService;

	/**
	 * 消息通知服务类
	 */
	@Resource
	private NoticeInfoService nis;

	/**
	 * 显示初始化系统的配置页面
	 */
	@RequestMapping("/init")
	public String init() {
		return "init";
	}

	@Value("${wss.server.port}")
	private String socket_port;

	/**
	 * 初始化系统数据
	 */
	@RequestMapping("/init/do")
	@ResponseBody
	public Map<String, Object> doInit(@ModelAttribute("form") UserInfo user) {
		StateInfo si = sysService.init(user);
		Map<String, Object> map = new HashMap<>();
		if (si.getFlag()) {
			map.put("success", true);
		} else {
			map.put("success", false);
			map.put("message", si.getMsg());
		}
		return map;
	}

	/**
	 * 主界面
	 */
	@RequestMapping("/main")
	public String main(HttpServletRequest request, Model model) {
		//获取角色菜单等登录信息
		LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute("loginSession");
		UserInfo user = (UserInfo) request.getSession().getAttribute("userSession");
		userInfoService.getLoginInfo(user, loginInfo);
		request.getSession().setAttribute("loginSession", loginInfo);

		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		model.addAttribute("baseUrl", basePath);
		model.addAttribute("socket_port", socket_port);
		model.addAttribute("version", Global.VERSION);
		//未读消息  state 0 表示未读 1 表示已读
		List<NoticeInfo> noticeInfos = nis.getNoticeWithState(user.getUserid(), new Bridge(request), 0);
		int noticeNum = noticeInfos == null ? 0 : noticeInfos.size();
		model.addAttribute("noticeNum", noticeNum);
		//预览界面只显示最多5条
		if (noticeNum > 5) {
			model.addAttribute("noticeList", noticeInfos.subList(0, 5));
		} else {
			model.addAttribute("noticeList", noticeInfos);
		}

		//备份数据操作
		Bridge bridge = new Bridge(request);
		//判断是否有数据，没有就初始化这个数据
		if (Global.getDeptEmpRelaMonthBackupInfo() == null) {
			//系统启动的时候，第一个登录的人可能会被耗时。
			Global.setDeptEmpRelaMonthBackupInfo(sysService.getDeptEmpRelaMonthBackupInfo());
		}
		//判断当前月份是否备份了当前月份对应的部门和职员和部门的对应关系。
		if (CommonUtil.getMV(Global.getDeptEmpRelaMonthBackupInfo(), bridge.getYmstr()) == null) {
			//开启备份表线程
			new BackupMonthDataThread(new Bridge(request).getYmstr(), deptInfoService, userInfoService).run();
		}

		return "frame/widgets";
	}

	/**
	 * 变更登录的业务日期
	 */
	@ResponseBody
	@RequestMapping("/base/date")
	public StateInfo date(HttpServletRequest request) {
		StateInfo stateInfo = new StateInfo();
		request.getSession().setAttribute("loginSession", new LoginInfo(request));
		return stateInfo;
	}

	/**
	 * 主界面的上方页面
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/top")
	public String top(HttpServletRequest request, Model model) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		model.addAttribute("baseUrl", basePath);
		return "frame/Top";
	}
}
