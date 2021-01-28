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
import com.main.pojo.prfm.BaseFormula;
import com.main.service.prfm.BaseFormulaService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class BaseFormulaController {
	@Resource
	private BaseFormulaService baseFormulaService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/baseFormula/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowBaseFormula";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/baseFormula/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<BaseFormula> list = baseFormulaService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", baseFormulaService.getCount(keyword,node,bridge));
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/baseFormula/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  baseFormulaService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/baseFormula/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		BaseFormula baseFormula = new BaseFormula();
		baseFormula.setCompanyid(bridge.getCompanyid());
		baseFormula.setName(request.getParameter("name"));
		baseFormula.setFormula(request.getParameter("formula"));
		baseFormula.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		baseFormula.setType(request.getParameter("type"));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = baseFormulaService.add(parentid,baseFormula,bridge);
		}else {
			stateInfo = baseFormulaService.edit(id,baseFormula,bridge);
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
	@RequestMapping("/baseFormula/del")
	public StateInfo del(HttpServletRequest request){
		return baseFormulaService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/baseFormula/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return baseFormulaService.autoInitFromAccess(new Bridge(request));
	}
}
