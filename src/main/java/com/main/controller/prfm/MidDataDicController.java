package com.main.controller.prfm;

import com.common.CommonUtil;
import com.common.constants.GlobalConstants;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.MidDataDic;
import com.main.service.prfm.MidDataDicParentService;
import com.main.service.prfm.MidDataDicService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 辅助数据字典
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class MidDataDicController {
	@Resource
	private MidDataDicService midDataDicService;
	@Resource
	private MidDataDicParentService midDataDicParentService;

	/**
	 * 跳转地址
	 */
	@RequestMapping("/midDataDic/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowMidDataDic";
	}

	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/midDataDic/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<MidDataDic> list = midDataDicService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", midDataDicService.getCount(keyword,node,bridge));
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/midDataDic/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  midDataDicService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/midDataDic/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = null;
		Bridge bridge = new Bridge(request);
		String uuid = request.getParameter("uuid");
		String parentid = request.getParameter("father");
		MidDataDic midDataDic = new MidDataDic();
		midDataDic.setCompanyid(bridge.getCompanyid());
		midDataDic.setId(request.getParameter("id"));
		midDataDic.setName(request.getParameter("name"));
		midDataDic.setType(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("type"))));
		midDataDic.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		midDataDic.setParentid(request.getParameter("parentid"));
		midDataDic.setDeptorper(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("deptorper"))));
		midDataDic.setDec(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("dec"))));
		Logger.getLogger(this.getClass()).info("保存功能的ID:"+uuid);
		if(CommonUtil.isEmpty(uuid)) {
			stateInfo = midDataDicService.add(parentid,midDataDic,bridge);
		}else {
			stateInfo = midDataDicService.edit(uuid,midDataDic,bridge);
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


	@ResponseBody
	@RequestMapping("/midDataDic/formulaTree")
	public List<Map<String,Object>> getObjTree(HttpServletRequest request,Model model){
		return midDataDicService.getTreeJson(new Bridge(request), GlobalConstants.MIDDIC_FORMULA_TYPE);
	}
	@ResponseBody
	@RequestMapping("/midDataDic/nFormulaTree")
	public List<Map<String,Object>> getObjTreeOfInput(HttpServletRequest request,Model model){
		return midDataDicService.getTreeJson(new Bridge(request), GlobalConstants.MIDDIC_INPUT_TYPE);
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/midDataDic/del")
	public StateInfo del(HttpServletRequest request){
		return midDataDicService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}


	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/midDataDic/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		StateInfo stateInfo = midDataDicParentService.autoInitFromAccess(new Bridge(request));
		if(stateInfo.getFlag()) {
			stateInfo = midDataDicService.autoInitFromAccess(new Bridge(request));
		}
		return stateInfo;
	}
}
