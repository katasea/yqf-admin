package com.main.controller.prfm;

import java.util.ArrayList;
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
import com.main.pojo.prfm.MidDataDicParent;
import com.main.service.prfm.MidDataDicParentService;
/**
 * 辅助数据字典分类
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class MidDataDicParentController {
	@Resource
	private MidDataDicParentService midDataDicParentService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/midDataDicParent/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowMidDataDicParent";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/midDataDicParent/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<MidDataDicParent> list = midDataDicParentService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", midDataDicParentService.getCount(keyword,node,bridge));
		return map;
	}

	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/midDataDicParent/list")
	public Map<String,Object> list(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String deptOrper = request.getParameter("deptOrper");
		List<MidDataDicParent> list = midDataDicParentService.get(keyword,deptOrper,bridge,"0","9999999");
		List<Map<String,Object>> resultList = new ArrayList<>();
		for(MidDataDicParent bean : list) {
			Map<String,Object> map = new HashMap<>();
			map.put("dickey",bean.getId());
			map.put("dicval",bean.getId()+" "+bean.getName());
			resultList.add(map);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", resultList);
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/midDataDicParent/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  midDataDicParentService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/midDataDicParent/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		MidDataDicParent midDataDicParent = new MidDataDicParent();
		midDataDicParent.setCompanyid(request.getParameter("companyid"));
		midDataDicParent.setId(request.getParameter("id"));
		midDataDicParent.setName(request.getParameter("name"));
		midDataDicParent.setOrderid(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("orderid"))));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = midDataDicParentService.add(parentid,midDataDicParent,bridge);
		}else {
			stateInfo = midDataDicParentService.edit(id,midDataDicParent,bridge);
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
	@RequestMapping("/midDataDicParent/del")
	public StateInfo del(HttpServletRequest request){
		return midDataDicParentService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/midDataDicParent/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return midDataDicParentService.autoInitFromAccess(new Bridge(request));
	}
}
