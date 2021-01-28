package com.main.controller.prfm;

import com.common.CommonUtil;
import com.common.constants.GlobalConstants;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.AuthCenter;
import com.main.pojo.prfm.BaseFormula;
import com.main.service.prfm.AuthCenterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthCenterController {
	@Resource
	private AuthCenterService acs;

	/**
	 * 跳转地址
	 */
	@RequestMapping("/acc/show")
	public String toIndex(HttpServletRequest request, Model model){
		model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowAuthCenter";
	}

	/**
	 * 获取权限类别下拉数据
	 */
	@RequestMapping("/acc/type/list")
	@ResponseBody
	public Map<String,Object> list4Type(HttpServletRequest request) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		List<Map<String,Object>> list = acs.getList4Type(null);
		for(Map<String,Object> info : list) {
			Map<String,Object> map = new HashMap<>();
			map.put("dickey",String.valueOf(info.get("code")+"-"+String.valueOf(info.get("add"))));
			if(String.valueOf(info.get("add")).equals(GlobalConstants.ALLOWADDOPT)) {
				map.put("dicval",String.valueOf(info.get("code"))+ " " + String.valueOf(info.get("name")) + "*")  ;
			}else {
				map.put("dicval",String.valueOf(info.get("code"))+ " " + String.valueOf(info.get("name")));
			}
			resultList.add(map);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", resultList);
		return map;
	}

	/**
	 * 获取左边列表数据
	 */
	@ResponseBody
	@RequestMapping("/acc/getMorig")
	public Map<String,Object> getMorig(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String funcode = request.getParameter("funcode");
		String keyword = request.getParameter("keyword");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		Map<String,Object> map = acs.getMorigList(CommonUtil.decodeKeyWord(this.getClass(),keyword),funcode,bridge,start,limit);
		return map;
	}

	/**
	 * 获取右边列表数据
	 */
	@ResponseBody
	@RequestMapping("/acc/getMtarg")
	public Map<String,Object> getMtarg(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String funcode = request.getParameter("funcode");
		String keyword = request.getParameter("keyword");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String morig = request.getParameter("morig");
		Map<String,Object> map = acs.getMtargList(CommonUtil.decodeKeyWord(this.getClass(),keyword),funcode,bridge,start,limit,morig);
		return map;
	}

	/**
	 * 获取中间列表数据
	 */
	@ResponseBody
	@RequestMapping("/acc/getAuthList")
	public Map<String,Object> getAuthList(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String funcode = request.getParameter("funcode");
		String keyword = request.getParameter("keyword");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String morig = request.getParameter("morig");
		List<Map<String,Object>> list = acs.getAuthList(keyword,funcode,bridge,morig,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
		map.put("total", acs.getAuthCount(keyword,funcode,bridge,morig));
		return map;
	}

	/**
	 * 新增权限对应
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/acc/addAuth")
	public StateInfo addAuth(@ModelAttribute AuthCenter authCenter,HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		return acs.addAuth(bridge,authCenter);
	}

	/**
	 * 删除权限对应
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/acc/delAuth")
	public StateInfo delAuth(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String pkid = request.getParameter("pkid");
		return acs.delAuth(bridge,pkid);
	}

	/**
	 * 删除权限对应
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/acc/delAuthByMorig")
	public StateInfo delAuthByMorig(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String morig = request.getParameter("morig");
		return acs.delAuthByMorig(bridge,morig);
	}
}
