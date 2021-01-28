package com.main.controller.prfm;

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
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.KaoHeBiLi;
import com.main.pojo.result.KaoHeBiLiResult;
import com.main.service.prfm.KaoHeBiLiService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class KaoHeBiLiController {
	@Resource
	private KaoHeBiLiService kaoHeBiLiService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/kaoHeBiLi/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowKaoHeBiLi";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/kaoHeBiLi/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String deptId = request.getParameter("deptId");
		List<KaoHeBiLiResult> list = kaoHeBiLiService.get(keyword,node,bridge,start,limit,deptId);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", kaoHeBiLiService.getCount(keyword,node,bridge));
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/kaoHeBiLi/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  kaoHeBiLiService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/kaoHeBiLi/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		KaoHeBiLi kaoHeBiLi = new KaoHeBiLi();
		kaoHeBiLi.setKid(request.getParameter("kid"));
		kaoHeBiLi.setCompanyid(request.getParameter("companyid"));
		kaoHeBiLi.setDeptId(request.getParameter("deptId"));
		kaoHeBiLi.setZbId(request.getParameter("zbId"));
		kaoHeBiLi.setZpbl(request.getParameter("zpbl"));
		kaoHeBiLi.setZkkbl(request.getParameter("zkkbl"));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = kaoHeBiLiService.add(parentid,kaoHeBiLi,bridge);
		}else {
			stateInfo = kaoHeBiLiService.edit(id,kaoHeBiLi,bridge);
		}
		Map<String,Object> map = new HashMap<>();
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
	@RequestMapping("/kaoHeBiLi/del")
	public StateInfo del(HttpServletRequest request){
		return kaoHeBiLiService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
}
