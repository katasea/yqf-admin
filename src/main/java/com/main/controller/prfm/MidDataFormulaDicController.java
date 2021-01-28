package com.main.controller.prfm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.common.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.CommonUtil;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.MidDataFormulaDic;
import com.main.service.prfm.MidDataFormulaDicService;
/**
 * 辅助数据公式字典
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class MidDataFormulaDicController {
	@Resource
	private MidDataFormulaDicService midDataFormulaDicService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/midDataFormulaDic/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowMidDataFormulaDic";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/midDataFormulaDic/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String midBh = request.getParameter("midBh");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<MidDataFormulaDic> list = midDataFormulaDicService.get(keyword,node,bridge,start,limit,midBh);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", midDataFormulaDicService.getCount(keyword,node,bridge,midBh));
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/midDataFormulaDic/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  midDataFormulaDicService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/midDataFormulaDic/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		//传递的是pkid
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		MidDataFormulaDic midDataFormulaDic = new MidDataFormulaDic();
		midDataFormulaDic.setCompanyid(bridge.getCompanyid());
		midDataFormulaDic.setId(request.getParameter("midBh"));
		midDataFormulaDic.setOrderid(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("orderid"))));
		midDataFormulaDic.setName(request.getParameter("name"));
		midDataFormulaDic.setFormula(request.getParameter("formula"));
		midDataFormulaDic.setDeptorper(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("deptorper"))));
		midDataFormulaDic.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		midDataFormulaDic.setDec(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("dec"))));
		midDataFormulaDic.setCaclfalg(0);
		midDataFormulaDic.setFailreason(Global.NULLSTRING);
		if(CommonUtil.isEmpty(id)) {
			stateInfo = midDataFormulaDicService.add(parentid,midDataFormulaDic,bridge);
		}else {
			stateInfo = midDataFormulaDicService.edit(id,midDataFormulaDic,bridge);
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
	@RequestMapping("/midDataFormulaDic/del")
	public StateInfo del(HttpServletRequest request){
		return midDataFormulaDicService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}

	/**
	 * 试算公式
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/midDataFormulaDic/calc")
	public StateInfo calc(HttpServletRequest request) {
		return midDataFormulaDicService.calc(request.getParameter("midBh"),new Bridge(request));
	}

	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/midDataFormulaDic/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return midDataFormulaDicService.autoInitFromAccess(new Bridge(request));
	}
}
