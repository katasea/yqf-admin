package com.main.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.common.CommonUtil;
import com.common.Global;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.LoginInfo;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.UserInfo;
import com.main.service.platform.UserInfoService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 对象控制层
 *
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class UserInfoController {
	@Resource
	private UserInfoService userInfoService;

	@Value("${spring.redis.expire:86400}")
	private long ttl;
	@Resource
	private RedisTemplate<String,Object> redisTemplate;
	/**
	 * 当启用多个app应用
	 * 可以知道是哪个端口的应用
	 */
	@Value("${server.port}")
	private String port;

	/**
	 * 登录页面
	 */
	@RequestMapping(value = {"/","/login"})
	public String toRoot(Model model) {
		Logger.getLogger(this.getClass()).info("PORT:" + port);
		String date = Global.year_month_day.format(new Date());
		model.addAttribute("date", date);
		return "login";
	}



	/**
	 * 登出页面
	 */
	@RequestMapping("/logout")
	public String toLogout(Model model,HttpServletRequest request) {
		Logger.getLogger(this.getClass()).info("PORT:" + port);
		String login_token = (String) request.getSession().getAttribute("login_token");
		redisTemplate.delete(login_token);
		SecurityUtils.getSubject().logout();
		String date = Global.year_month_day.format(new Date());
		model.addAttribute("date", date);
		return "login";
	}

	/**
	 * 这里简单的判断页面登陆的用户名和密码是否和配置的sql 登陆用户名和密码是否匹配。
	 * 登录判断
	 */
	@ResponseBody
	@RequestMapping("/valiad")
	public StateInfo valiad(HttpServletRequest request) {
		Logger.getLogger(UserInfoController.class).info("[" + port + "] USERNAME [" + request.getParameter("userid") + "] LOGIN SYSTEM");
		// 状态信息
		StateInfo stateInfo = new StateInfo();
		// 登录后存放进shiro token
		UsernamePasswordToken token = new UsernamePasswordToken(request.getParameter("userid"), request.getParameter("password"));
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			LoginInfo loginInfo = new LoginInfo(request);
			request.getSession().setAttribute("loginSession", loginInfo);
			//当验证都通过后，把用户信息放redis里面
			String login_token = Global.createUUID();
			request.getSession().setAttribute("login_token", login_token);
			redisTemplate.opsForValue().set(login_token, JSONObject.toJSONString(loginInfo),ttl, TimeUnit.SECONDS);
			stateInfo.setData(login_token);
		} catch (UnknownAccountException e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "账号或密码错误!", null);
		} catch (LockedAccountException e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "账号被锁定！", null);
		} catch (AuthenticationException e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "账号或密码错误!", null);
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), e.getMessage(), e);
		}
		return stateInfo;
	}

	/**
	 * 获取所属用户列表
	 */
	@ResponseBody
	@RequestMapping("/userInfo/getUserMgrInfo")
	public Map<String, Object> getUserMgrInfo(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		//资源 对应的用户过滤
		String resourcePkid = request.getParameter("resourcePkid");
		//角色 对应的用户过滤
		String rolePkid = request.getParameter("rolePkid");
		List<UserInfo> list = userInfoService.getUserMgrInfo(keyword, resourcePkid, rolePkid, bridge, start, limit);
		Map<String, Object> map = new HashMap<>();
		map.put("root", list);
		map.put("total", userInfoService.getCountOfUserMgrInfo(keyword, resourcePkid, rolePkid, bridge));
		return map;
	}

	/**
	 * 删除所属用户查看对应菜单资源的权限
	 */
	@ResponseBody
	@RequestMapping("/userInfo/delUserResRoleAuth")
	public StateInfo delUserResRoleAuth(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String resfrom = request.getParameter("resfrom");
		String userpkid = request.getParameter("userpkid");
		String resourcepkid = request.getParameter("resourcePkid");
		String rolepkid = request.getParameter("rolepkid");
		return userInfoService.delUserResRoleAuth(bridge, resourcepkid, rolepkid, userpkid, resfrom);
	}

	/**
	 * 新增所属用户查看对应菜单资源的权限
	 */
	@ResponseBody
	@RequestMapping("/userInfo/addUserResAuth")
	public StateInfo addUserResAuth(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String userpkid = request.getParameter("userpkid");
		String resourcePkid = request.getParameter("resourcePkid");
		return userInfoService.addUserResAuth(bridge, resourcePkid, userpkid);
	}

	/**
	 * 跳转地址
	 */
	@RequestMapping("/userInfo/show")
	public String toIndex(HttpServletRequest request, Model model) {
		model.addAttribute("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
		return "function/ShowUserInfo";
	}

	/**
	 * 跳转地址
	 */
	@RequestMapping("/userInfo/profile")
	public String profile(HttpServletRequest request, Model model) {
		model.addAttribute("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
		Bridge bridge = new Bridge(request);
		model.addAttribute("me", userInfoService.selectByUserid(bridge, bridge.getUserid()));
		return "function/ShowUserProfile";
	}
	/**
	 * 跳转地址
	 */
	@RequestMapping("/userInfo/mainpage")
	public String mainpage(HttpServletRequest request, Model model) {
		model.addAttribute("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
		Bridge bridge = new Bridge(request);
		model.addAttribute("me", userInfoService.selectByUserid(bridge, bridge.getUserid()));
		return "function/ShowUserMainPage";
	}

	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/userInfo/get")
	public Map<String, Object> get(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<UserInfo> list = userInfoService.get(keyword, node, bridge, start, limit);
		Map<String, Object> map = new HashMap<>();
		map.put("root", list);
		map.put("total", userInfoService.getCount(keyword, node, bridge));
		return map;
	}

	/**
	 * 获取用户下拉列表数据
	 */
	@ResponseBody
	@RequestMapping("/userInfo/list")
	public Map<String,Object> list(HttpServletRequest request) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		List<UserInfo> list = userInfoService.get(keyword, node, bridge,"0", String.valueOf(Global.TABLE_MAX_RECORD));
		for(UserInfo bean : list) {
			Map<String,Object> map = new HashMap<>();
			map.put("dickey",bean.getUserid());
			map.put("dicval",bean.getUserid()+" "+bean.getUsername());
			resultList.add(map);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", resultList);
		return map;
	}

	/**
	 * 验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/userInfo/validator")
	public Map<String, Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String, Object> map = new HashMap<>();
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(key)) {
			stateInfo = userInfoService.validator(key, value, bridge);
		}
		map.put("success", stateInfo.getFlag());
		map.put("msg", stateInfo.getMsg());
		return map;
	}

	/**
	 * 在个人信息里面保存个人信息
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/userInfo/saveProfile")
	public StateInfo saveProfile(HttpServletRequest request) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserid(request.getParameter("userid"));
		userInfo.setPhone(request.getParameter("phone"));
		userInfo.setEmail(request.getParameter("email"));
		userInfo.setBirthday(request.getParameter("birthday"));
		userInfo.setIdcard(request.getParameter("idcard"));
		return userInfoService.editProfile(userInfo);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/userInfo/save")
	public Map<String, Object> save(HttpServletRequest request) {
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		UserInfo userInfo = new UserInfo();
		userInfo.setUserid(request.getParameter("userid"));
		userInfo.setDeptid(request.getParameter("deptid"));
		userInfo.setPhone(request.getParameter("phone"));
		userInfo.setEmail(request.getParameter("email"));
		userInfo.setUsername(request.getParameter("username"));
		userInfo.setPassword(request.getParameter("password"));
		userInfo.setEnable(request.getParameter("enable"));
		userInfo.setBirthday(request.getParameter("birthday"));
		userInfo.setIdcard(request.getParameter("idcard"));
		userInfo.setSex(request.getParameter("sex"));
		userInfo.setPost(request.getParameter("poststr"));
		userInfo.setUserstyle(request.getParameter("userstyle"));
		userInfo.setUserstatus(request.getParameter("userstatus"));
		userInfo.setEntrytime(request.getParameter("entrytime"));
		if (CommonUtil.isEmpty(id)) {
			stateInfo = userInfoService.add(userInfo, bridge, request.getParameter("rolestr"), request.getParameter("resstr"));
		} else {
			stateInfo = userInfoService.edit(id, userInfo, bridge, request.getParameter("rolestr"), request.getParameter("resstr"));
		}
		Map<String, Object> map = new HashMap<>();
		if (stateInfo.getFlag()) {
			map.put("success", true);
		} else {
			map.put("success", false);
			map.put("message", stateInfo.getMsg());
		}
		return map;
	}

	/**
	 * 修改密码
	 */
	@ResponseBody
	@RequestMapping("/userInfo/resetPassword")
	public StateInfo resetPassword(HttpServletRequest request) {
		//TODO 防止恶意访问
		//当此参数为空时候，会重置密码，否则就会按传递的密码进行修改
		String userid = request.getParameter("userid");
		return userInfoService.updatePassword(null, userid, new Bridge(request));
	}

	/**
	 * 修改个人密码
	 */
	@ResponseBody
	@RequestMapping("/userInfo/updatePassword")
	public StateInfo updatePassword(HttpServletRequest request) {
		StateInfo stateInfo = new StateInfo();
		//当此参数为空时候，会重置密码，否则就会按传递的密码进行修改
		String oldpassword = request.getParameter("oldpassword");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");
		Bridge bridge = new Bridge(request);
		// 验证旧密码
		UsernamePasswordToken token = new UsernamePasswordToken(bridge.getUserid(), oldpassword);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			//旧密码验证通过
			if (oldpassword.equals(password)) {
				//旧密码和新密码一致
				stateInfo.setFlag(false);
				stateInfo.setMsg(this.getClass(),"新密码应该与旧密码不同！请重新输入新密码！",null);
			} else {
				if (password.equals(repassword)) {
					//验证通过允许改密码
					userInfoService.updatePassword(password, bridge.getUserid(), new Bridge(request));
				} else {
					stateInfo.setFlag(false);
					stateInfo.setMsg(this.getClass(), "两次输入的新密码不一致！请重新输入！", null);
				}
			}
		} catch (Exception e) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(), "旧密码输入错误！", null);
		}
		return stateInfo;
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/userInfo/del")
	public StateInfo del(HttpServletRequest request) {
		return userInfoService.delete(request.getParameter("id"), request.getParameter("parentId"), new Bridge(request));
	}

	/**
	 * 下载Excel导入模板
	 */
	@RequestMapping("/userInfo/exportTemplate")
	public void exportTemplate(HttpServletResponse response, HttpServletRequest request) {
		try {
			String fileName = "Excel模板_用户.xls";
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			OutputStream os = response.getOutputStream();
			userInfoService.exportExcelTemplate(os, new Bridge(request));
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}

	}

	/**
	 * 下载Excel导入模板
	 */
	@RequestMapping("/userInfo/exportExcel")
	public void exportExcel(HttpServletResponse response, HttpServletRequest request) {
		try {
			String fileName = "Excel数据_用户.xls";
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			OutputStream os = response.getOutputStream();
			userInfoService.exportData(os, new Bridge(request),request.getParameter("keyword"));
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}
	}

	/**
	 * 导入Excel数据
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo/importData", method = RequestMethod.POST)
	public Map<String, Object> importData(HttpServletRequest request, @RequestParam("excelFile") MultipartFile file) {
		return CommonUtil.changeFormResult(userInfoService.importData(CommonUtil.uploadFileDeal(file), new Bridge(request)));
	}
}
