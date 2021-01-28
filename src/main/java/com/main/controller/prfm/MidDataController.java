package com.main.controller.prfm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.CommonUtil;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.MidData;
import com.main.service.prfm.MidDataService;
/**
 * 辅助数据录入
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class MidDataController {
	@Resource
	private MidDataService midDataService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/midData/show/{deptOrper}")
	public String toIndex(HttpServletRequest request, Model model, @PathVariable(name = "deptOrper") String deptOrper){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
        model.addAttribute("deptOrper",deptOrper);// 0 部门 1 个人
		return "function/ShowMidData";
	}

	/**
	 * 获取动态列
	 */
	@ResponseBody
	@RequestMapping("/midData/getDynamicColumn")
	public StateInfo getDynamicColumn(HttpServletRequest request,
      @RequestParam(name="chooseParentId") String chooseParentId,
      @RequestParam(name="deptOrper") String deptOrper) {
		Bridge bridge = new Bridge(request);
		return this.midDataService.getDynamicColumn(bridge,chooseParentId,deptOrper);
	}

	/**
	 * 获取动态列数据
	 */
	@ResponseBody
	@RequestMapping("/midData/getDynamicJson")
	public Map<String,Object> getDynamicJson(HttpServletRequest request,
			@RequestParam(name="deptOrper") String deptOrper,
			@RequestParam(name="chooseParentId") String chooseParentId,
            @RequestParam(name="start") String start,
            @RequestParam(name="limit") String limit,
            @RequestParam(name="keyword") String keyword) {
		return this.midDataService.getDynamicJson(new Bridge(request),deptOrper,chooseParentId,start,limit,keyword);
	}

	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/midData/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String midBh = request.getParameter("midBh");
		String deptorper = request.getParameter("deptorper");
		List<MidData> list = midDataService.get(keyword,midBh,bridge,start,limit,deptorper);
		Map<String,Object> map = new HashMap<>();
		map.put("root", list);
        map.put("total", midDataService.getCount(keyword,midBh,bridge,deptorper));
		return map;
	}


	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/midData/input/save")
	public StateInfo inputSave(HttpServletRequest request){
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String bh = request.getParameter("bh");
		String field = request.getParameter("field");
		String value = request.getParameter("value");
		MidData midData = new MidData();
		midData.setCompanyid(bridge.getCompanyid());
		midData.setId(field.substring(1));
		midData.setBh(bh);
		midData.setDeptorper(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("deptorper"))));
		midData.setMonth(Integer.parseInt(bridge.getMonth()));
		midData.setData(value);
		midData.setProcess(bridge.getUserid()+":"+bridge.getUsername()+"录入");
		stateInfo = midDataService.delete(bh,null,bridge);
		if(stateInfo.getFlag()) {
			stateInfo = midDataService.add(null,midData,bridge);
		}
		return stateInfo;
	}

	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/midData/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		MidData midData = new MidData();
		midData.setCompanyid(request.getParameter("companyid"));
		midData.setId(request.getParameter("id"));
		midData.setDeptorper(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("deptorper"))));
		midData.setMonth(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("month"))));
		midData.setData(request.getParameter("data"));
		midData.setProcess(request.getParameter("process"));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = midDataService.add(parentid,midData,bridge);
		}else {
			stateInfo = midDataService.edit(id,midData,bridge);
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
	@RequestMapping("/midData/del")
	public StateInfo del(HttpServletRequest request){
		return midDataService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
}
