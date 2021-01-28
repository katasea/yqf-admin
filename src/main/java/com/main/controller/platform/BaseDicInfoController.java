package com.main.controller.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.CommonUtil;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.BasDicInfo;
import com.main.service.platform.BaseDicInfoService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class BaseDicInfoController {
	@Resource
	private BaseDicInfoService basDicInfoService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/basDicInfo/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowBasDicInfo";
	}
	/**
	 * 获取主页面JSON数据
	 */
	//JSON注解
	@ResponseBody
	@RequestMapping("/basDicInfo/get")
	public Map<String,Object> get(HttpServletRequest request,Model model){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<BasDicInfo> list = basDicInfoService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", list.size());
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/basDicInfo/validator")
	public Map<String,Object> validator(HttpServletRequest request,Model model) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  basDicInfoService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/basDicInfo/save")
	public Map<String,Object> save(HttpServletRequest request,Model model){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		BasDicInfo basDicInfo = new BasDicInfo();
		basDicInfo.setDicid(request.getParameter("dicid"));
		basDicInfo.setDicname(request.getParameter("dicname"));
		basDicInfo.setDickey(request.getParameter("dickey"));
		basDicInfo.setDicval(request.getParameter("dicval"));
		basDicInfo.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = basDicInfoService.add(parentid,basDicInfo,bridge);
		}else {
			stateInfo = basDicInfoService.edit(id,basDicInfo,bridge);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		if(stateInfo.getFlag()){
			map.put("success",true);
		}else {
			map.put("success",false);
			map.put("message",stateInfo.getMsg());
		}
		return map;
	}
	
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/basDicInfo/del")
	public StateInfo del(HttpServletRequest request,Model model){
		return basDicInfoService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/basDicInfo/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request,Model model){
		return basDicInfoService.autoInitFromAccess();
	}

	/**
	 * 获取下拉框字典JSON
	 * @param request
	 * @param model
	 * @return {root:[{name:'dd',value:'ff'}...]}
	 */
	@ResponseBody
	@RequestMapping("/basDicInfo/getComboJson")
	public Map<String,Object> getComboJson(HttpServletRequest request,Model model) {
		Map<String,Object> result = new HashMap<>();
		String type = request.getParameter("type");
		result.put("root",basDicInfoService.getComboJson(type));
		return result;
	}

	/**
	 * 获取下拉框字典JSON
	 * @param request
	 * @param model
	 * @return {root:[{name:'dd',value:'ff'}...]}
	 */
	@ResponseBody
	@RequestMapping("/basDicInfo/getDicZTree")
	public List<Map<String,Object>> getDicZTree(HttpServletRequest request,Model model) {
		Map<String,Object> result = new HashMap<>();
		String type = request.getParameter("type");
		Bridge bridge = new Bridge(request);
		String userpkid = request.getParameter("userpkid");
		return this.basDicInfoService.getUserDicZTree(bridge,userpkid,type);
	}


}
