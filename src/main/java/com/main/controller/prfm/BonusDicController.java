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
import com.main.pojo.prfm.BonusDic;
import com.main.service.prfm.BonusDicService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class BonusDicController {
	@Resource
	private BonusDicService bonusDicService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/bonusDic/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowBonusDic";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/bonusDic/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<BonusDic> list = bonusDicService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", list.size());
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/bonusDic/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  bonusDicService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/bonusDic/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		BonusDic bonusDic = new BonusDic();
		bonusDic.setCompanyid(bridge.getCompanyid());
		bonusDic.setBh(request.getParameter("bh"));
		bonusDic.setYear(Integer.parseInt(bridge.getYear()));
		bonusDic.setMonth(Integer.parseInt(bridge.getMonth()));
		bonusDic.setName(request.getParameter("name"));
		bonusDic.setGrade(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("grade"))));
		bonusDic.setFormula(request.getParameter("formula"));
		bonusDic.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = bonusDicService.add(parentid,bonusDic,bridge);
		}else {
			stateInfo = bonusDicService.edit(id,bonusDic,bridge);
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
	@RequestMapping("/bonusDic/del")
	public StateInfo del(HttpServletRequest request){
		return bonusDicService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/bonusDic/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return bonusDicService.autoInitFromAccess(new Bridge(request));
	}
	/**
	 *拷贝上年数据
	 */
	@ResponseBody
	@RequestMapping("/bonusDic/copyLastYearData")
	public StateInfo copyLastYearData(HttpServletRequest request){
		return bonusDicService.copyLasyYearData(new Bridge(request));
	}
}
