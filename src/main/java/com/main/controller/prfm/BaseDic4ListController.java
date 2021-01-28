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
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.BaseDic4List;
import com.main.service.prfm.BaseDic4ListService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class BaseDic4ListController {
	@Resource
	private BaseDic4ListService baseDic4ListService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/baseDic4List/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowBaseDic4List";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/baseDic4List/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<BaseDic4List> list = baseDic4ListService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", baseDic4ListService.getCount(keyword,node,bridge));
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/baseDic4List/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  baseDic4ListService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/baseDic4List/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		BaseDic4List baseDic4List = new BaseDic4List();
		baseDic4List.setCompanyid(bridge.getCompanyid());
		baseDic4List.setMkey(request.getParameter("mkey"));
		baseDic4List.setType(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("type"))));
		baseDic4List.setMvalue(request.getParameter("mvalue"));
		baseDic4List.setReverse(request.getParameter("reverse"));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = baseDic4ListService.add(parentid,baseDic4List,bridge);
		}else {
			stateInfo = baseDic4ListService.edit(id,baseDic4List,bridge);
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
	@RequestMapping("/baseDic4List/del")
	public StateInfo del(HttpServletRequest request){
		return baseDic4ListService.delete(request.getParameter("id"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/baseDic4List/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return baseDic4ListService.autoInitFromAccess(new Bridge(request));
	}
}
