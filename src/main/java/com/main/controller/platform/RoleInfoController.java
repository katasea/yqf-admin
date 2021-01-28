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
import com.main.pojo.platform.RoleInfo;
import com.main.service.platform.RoleInfoService;
/**
 * @author ATC[auto-code project create]
 *
 */
@Controller
public class RoleInfoController {
	@Resource
	private RoleInfoService roleInfoService;
	/**
	 * 跳转地址
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/roleInfo/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowRoleInfo";
	}
	/**
	 * 获取主页面JSON数据
	 * @param request
	 * @param model
	 * @return root total
	 */
	//JSON注解
	@ResponseBody
	@RequestMapping("/roleInfo/get")
	public Map<String,Object> get(HttpServletRequest request,Model model){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<RoleInfo> list = roleInfoService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<>();
		map.put("root", list);
        map.put("total", roleInfoService.getCount(keyword,node,bridge));
		return map;
	}

	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/roleInfo/validator")
	public Map<String,Object> validator(HttpServletRequest request,Model model) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  roleInfoService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/roleInfo/save")
	public Map<String,Object> save(HttpServletRequest request,Model model){
		StateInfo stateInfo = null;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setPkid(request.getParameter("pkid"));
		roleInfo.setRoleid(request.getParameter("roleid"));
		roleInfo.setRoledesc(request.getParameter("roledesc"));

		/**
		 * 界面上选取的用户和角色关联信息 userpkid
		 */
		String chooseUserAddRoleAuth = request.getParameter("chooseUserAddRoleAuth");
		/**
		 * 界面上选取的资源和角色关联信息 respkid
		 */
		String chooseResAddRoleAuth = request.getParameter("chooseResAddRoleAuth");

		if(CommonUtil.isEmpty(id)) {
			stateInfo = roleInfoService.add(roleInfo,bridge,chooseResAddRoleAuth,chooseUserAddRoleAuth);
		}else {
			stateInfo = roleInfoService.edit(id,roleInfo,bridge,chooseResAddRoleAuth,chooseUserAddRoleAuth);
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
	 * @param request
	 * @param model
	 * @return root total
	 */
	@ResponseBody
	@RequestMapping("/roleInfo/del")
	public StateInfo del(HttpServletRequest request,Model model){
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		StateInfo stateInfo = roleInfoService.delete(id,parentId,bridge);
		return stateInfo;
	}

	/**
	 * 获取Ztree用户关联角色
	 * @param request
	 * @param model
	 */
	//JSON注解
	@ResponseBody
	@RequestMapping("/roleInfo/getRoleZTree")
	public List<Map<String,Object>> getResZTree(HttpServletRequest request,Model model){
		Bridge bridge = new Bridge(request);
		String userpkid = request.getParameter("userpkid");
		//递归获取所有资源节点信息，并且根据页面传递的参数判断是否已经勾选
		return this.roleInfoService.getUserRoleZTree(bridge,userpkid);
	}
}
