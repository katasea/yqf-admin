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
import com.main.pojo.platform.CompanyInfo;
import com.main.service.platform.CompanyInfoService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class CompanyInfoController {
	@Resource
	private CompanyInfoService companyInfoService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/companyInfo/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowCompanyInfo";
	}
	/**
	 * 获取主页面JSON数据
	 */
	//JSON注解
	@ResponseBody
	@RequestMapping("/companyInfo/get")
	public Map<String,Object> get(HttpServletRequest request,Model model){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<CompanyInfo> list = companyInfoService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", companyInfoService.getCount(keyword,node,bridge));
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/companyInfo/validator")
	public Map<String,Object> validator(HttpServletRequest request,Model model) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  companyInfoService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/companyInfo/save")
	public Map<String,Object> save(HttpServletRequest request,Model model){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setCompanyid(request.getParameter("companyid"));
		companyInfo.setCompanyname(request.getParameter("companyname"));
		companyInfo.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = companyInfoService.add(parentid,companyInfo,bridge);
		}else {
			stateInfo = companyInfoService.edit(id,companyInfo,bridge);
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
	@RequestMapping("/companyInfo/del")
	public StateInfo del(HttpServletRequest request,Model model){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		stateInfo = companyInfoService.delete(id,parentId,bridge);
		return stateInfo;
	}
	
	
}
