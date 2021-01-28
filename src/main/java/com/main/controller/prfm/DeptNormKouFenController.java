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
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.DeptNormKouFen;
import com.main.pojo.result.DeptNormKouFenResult;
import com.main.service.prfm.DeptNormKouFenService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class DeptNormKouFenController {
	@Resource
	private DeptNormKouFenService deptNormKouFenService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/deptNormKouFen/show")
	public String deptNormKouFenShow(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowDeptNormKouFen";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/deptNormKouFen/get")
	public Map<String,Object> deptNormKouFenGet(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String deptId = request.getParameter("deptId");
		String normId = request.getParameter("normId");
		String deptOrPerson = request.getParameter("deptOrPerson");
		String zhiBiaoType = request.getParameter("zhiBiaoType");
		String isKouFen = request.getParameter("isKouFen");
		String isMarkNull = request.getParameter("isMarkNull");
		String zhiBiaoInFo =  request.getParameter("zhiBiaoInFo");
		List<DeptNormKouFenResult> list = deptNormKouFenService.getJoin(keyword,node,bridge,start,limit,
				deptId,normId,deptOrPerson,zhiBiaoType,isKouFen,isMarkNull,zhiBiaoInFo);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", deptNormKouFenService.getCountJoin(keyword,node,bridge,
        		deptId,normId,deptOrPerson,zhiBiaoType,isKouFen,isMarkNull,zhiBiaoInFo));
		return map;
	}
	

	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/deptNormKouFen/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  deptNormKouFenService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/deptNormKouFen/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentid = request.getParameter("father");
		DeptNormKouFen deptNormKouFen = new DeptNormKouFen();
		deptNormKouFen.setKid(request.getParameter("kid"));
		deptNormKouFen.setCompanyid(request.getParameter("companyid"));
		deptNormKouFen.setDeptOrPersonId(request.getParameter("deptOrPersonId"));
		deptNormKouFen.setZhiBiaoId(request.getParameter("zhiBiaoId"));
		deptNormKouFen.setKouFen(request.getParameter("kouFen"));
		deptNormKouFen.setRealKouFen(request.getParameter("realKouFen"));
		deptNormKouFen.setKouFenMark(request.getParameter("kouFenMark"));
		deptNormKouFen.setZkkKouFen(request.getParameter("zkkKouFen"));
		deptNormKouFen.setQiTaKouFen(request.getParameter("qiTaKouFen"));
		deptNormKouFen.setKouFenRenId(request.getParameter("kouFenRenId"));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = deptNormKouFenService.add(parentid,deptNormKouFen,bridge);
		}else {
			stateInfo = deptNormKouFenService.edit(id,deptNormKouFen,bridge);
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
	@RequestMapping("/deptNormKouFen/del")
	public StateInfo del(HttpServletRequest request){
		return deptNormKouFenService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	
	
	
	//========================== 指标部门或者个人扣分
	
	/**
	 * 跳转地址
	 */
	@RequestMapping("/normDeptKouFen/show")
	public String normDeptKouFenShow(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
        String deptOrPerson = request.getParameter("deptOrPerson");
        model.addAttribute("deptOrPerson",deptOrPerson);
		return "function/ShowNormDeptKouFen";
	}
	
	//===============质控科部分
	/**
	 * 质控科部门指标评审
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/zkkDeptNormKouFen/show")
	public String zkkDeptNormKouFenShow(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return "function/ShowZkkDeptNormKouFen";
	}
	
	
	/**
	 * 质控科部门评审以及个人评审
	 */
	@RequestMapping("/zkkNormDeptKouFen/show")
	public String zkkNormDeptKouFenShow(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
        String deptOrPerson = request.getParameter("deptOrPerson");
        model.addAttribute("deptOrPerson",deptOrPerson);
		return "function/ShowZkkNormDeptKouFen";
	}
	
}
