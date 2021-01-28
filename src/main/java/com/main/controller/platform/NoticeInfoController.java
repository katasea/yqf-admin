package com.main.controller.platform;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.common.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.CommonUtil;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.NoticeInfo;
import com.main.service.platform.NoticeInfoService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class NoticeInfoController {
	@Resource
	private NoticeInfoService noticeInfoService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/noticeInfo/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowNoticeInfo";
	}

	/**
	 * 用户查看所有消息
	 */
	@RequestMapping("/noticeInfo/view/ALL")
	public String view(HttpServletRequest request, Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowUserNoticeList";
	}

	/**
	 * 标记某消息为已读
	 */
	@ResponseBody
	@RequestMapping("/noticeInfo/hadread/{pkid}")
	public StateInfo hadread(@PathVariable("pkid") String pkid, HttpServletRequest request, Model model){
		return noticeInfoService.hadread(pkid,new Bridge(request).getUserid());
	}

	/**
	 * 首页获取某人未读消息json信息，只获取前5条。
	 */
	@ResponseBody
	@RequestMapping("/noticeInfo/unread/json")
	public List<NoticeInfo> unreadjson( HttpServletRequest request, Model model){
		Bridge bridge = new Bridge(request);
		//未读消息  state 0 表示未读 1 表示已读
		List<NoticeInfo> noticeInfos = noticeInfoService.getNoticeWithState(bridge.getUserid(),new Bridge(request),0);
		//预览界面只显示最多5条
		if(noticeInfos.size()>5) {
			return noticeInfos.subList(0,5);
		}else {
			return noticeInfos;
		}
	}


	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/noticeInfo/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String type = request.getParameter("type");
		String view = request.getParameter("view");
		List<NoticeInfo> list = noticeInfoService.get(keyword,node,bridge,start,limit,view,type);
		Map<String,Object> map = new HashMap<>();
		map.put("root", list);
        map.put("total", noticeInfoService.getCount(keyword,node,bridge,view,type));
		return map;
	}

	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/noticeInfo/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		NoticeInfo noticeInfo = new NoticeInfo();
		noticeInfo.setPkid(id);
		noticeInfo.setTitle(request.getParameter("title"));
		noticeInfo.setContent(request.getParameter("content"));
		noticeInfo.setSenttime(Global.df.format(new Date()));
		noticeInfo.setFromwho(bridge.getUserid());
		noticeInfo.setFromwhoname(bridge.getUsername());
		noticeInfo.setType(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("type"))));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = noticeInfoService.add(noticeInfo,bridge);
		}else {
			stateInfo = noticeInfoService.edit(id,noticeInfo,bridge);
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
	@RequestMapping("/noticeInfo/del")
	public StateInfo del(HttpServletRequest request){
		return noticeInfoService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
}
