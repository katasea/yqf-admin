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
import com.main.pojo.platform.ResourcesInfo;
import com.main.service.platform.ResourcesInfoService;

/**
 * @author ATC[auto-code project create]
 */
@Controller
public class ResourcesInfoController {
	@Resource
	private ResourcesInfoService resourcesInfoService;

	/**
	 * 跳转地址
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/resourcesInfo/show")
	public String toIndex(HttpServletRequest request, Model model) {
		model.addAttribute("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
		return "function/ShowResourcesInfo";
	}

	/**
	 * 验证页面输入的编号数据库是否已经存在
	 */
//	@ResponseBody
//	@RequestMapping("/resourcesInfo/validator")
//	public Map<String, Object> validator(HttpServletRequest request, Model model) {
//		Bridge bridge = new Bridge(request);
//		String key = request.getParameter("keyid");//需要验证的字段名
//		String value = request.getParameter("value");//需要验证的内容
//		Map<String, Object> map = new HashMap<>();
//		StateInfo stateInfo = new StateInfo();
//		if (!CommonUtil.isEmpty(key)) {
//			stateInfo = resourcesInfoService.validator(key, value, bridge);
//		}
//		map.put("success", stateInfo.getFlag());
//		map.put("msg", stateInfo.getMsg());
//		return map;
//	}

	/**
	 * 获取主页面JSON数据
	 *
	 * @param request
	 * @param model
	 * @return root total
	 */
	//JSON注解
	@ResponseBody
	@RequestMapping("/resourcesInfo/get")
	public Map<String, Object> get(HttpServletRequest request, Model model) {
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<ResourcesInfo> list = resourcesInfoService.get(keyword, node, bridge, start, limit);
		Map<String, Object> map = new HashMap<>();
		map.put("root", list);
		map.put("total", list.size());
		return map;
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/resourcesInfo/save")
	public Map<String, Object> save(HttpServletRequest request, Model model) {
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		ResourcesInfo resourcesInfo = new ResourcesInfo();
		resourcesInfo.setPkid(request.getParameter("pkid"));
		resourcesInfo.setResid(request.getParameter("resid"));
		resourcesInfo.setName(request.getParameter("name"));
		resourcesInfo.setResurl(request.getParameter("resurl"));
		resourcesInfo.setFa(request.getParameter("fa"));
		if (CommonUtil.isEmpty(id)) {
			stateInfo = resourcesInfoService.add(parentid, resourcesInfo, bridge);
		} else {
			stateInfo = resourcesInfoService.edit(id, resourcesInfo, bridge);
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
	 * 删除
	 *
	 * @param request
	 * @param model
	 * @return root total
	 */
	@ResponseBody
	@RequestMapping("/resourcesInfo/del")
	public StateInfo del(HttpServletRequest request, Model model) {
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		stateInfo = resourcesInfoService.delete(id, parentId, bridge);
		return stateInfo;
	}

	/**
	 * 从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/resourcesInfo/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request, Model model) {
		return resourcesInfoService.autoInitFromAccess(new Bridge(request));
	}


	/**
	 * 获取Ztree角色关联资源树信息 或用户关联资源树信息
	 * @param request
	 * @param model
	 */
	//JSON注解
	@ResponseBody
	@RequestMapping("/resourcesInfo/getResZTree")
	public List<Map<String,Object>> getResZTree(HttpServletRequest request,Model model){
		Bridge bridge = new Bridge(request);
		String rolepkid = request.getParameter("rolepkid");
		String userpkid = request.getParameter("userpkid");
		//递归获取所有资源节点信息，并且根据页面传递的参数判断是否已经勾选
		return this.resourcesInfoService.getResZTree(bridge,rolepkid,userpkid);
	}
}
