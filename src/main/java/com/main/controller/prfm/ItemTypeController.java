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
import com.main.pojo.prfm.ItemType;
import com.main.service.prfm.ItemTypeService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class ItemTypeController {
	@Resource
	private ItemTypeService itemTypeService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/itemType/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowItemType";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/itemType/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<ItemType> list = itemTypeService.get(keyword,node,bridge,start,limit);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", list.size());
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/itemType/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  itemTypeService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/itemType/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		ItemType itemType = new ItemType();
		itemType.setPid(request.getParameter("pid"));
		itemType.setCompanyid(request.getParameter("companyid"));
		itemType.setCode(request.getParameter("code"));
		itemType.setText(request.getParameter("text"));
		itemType.setItemtype(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("itemtype"))));
		itemType.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = itemTypeService.add(parentid,itemType,bridge);
		}else {
			stateInfo = itemTypeService.edit(id,itemType,bridge);
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
	@RequestMapping("/itemType/del")
	public StateInfo del(HttpServletRequest request){
		return itemTypeService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/itemType/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return itemTypeService.autoInitFromAccess(new Bridge(request));
	}
}
