package com.main.controller.platform;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.Global;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.CommonUtil;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.DeptInfo;
import com.main.service.platform.DeptInfoService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 部门控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class DeptInfoController {
	@Resource
	private DeptInfoService deptInfoService;

	/**
	 * 跳转地址
	 */
	@RequestMapping("/deptInfo/show")
	public String toIndex(HttpServletRequest request, Model model) {
		model.addAttribute("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
		return "function/ShowDeptInfo";
	}

	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/get")
	public Map<String, Object> get(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		List<DeptInfo> list = deptInfoService.get(keyword, node, bridge);
		Map<String, Object> map = new HashMap<>();
		map.put("root", list);
		map.put("total", list.size());
		return map;
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/getListJson")
	public Map<String, Object> getListJson(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<DeptInfo> list = deptInfoService.getListJson(keyword, node, bridge, start, limit);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("root", list);
		map.put("total", deptInfoService.getCount(keyword, node, bridge));
		return map;
	}

	/**
	 * 验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/validator")
	public Map<String, Object> validator(HttpServletRequest request) {
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String, Object> map = new HashMap<>();
		StateInfo stateInfo = new StateInfo();
		if (!CommonUtil.isEmpty(key)) {
			stateInfo = deptInfoService.validator(key, value, new Bridge(request));
		}
		map.put("success", stateInfo.getFlag());
		map.put("msg", stateInfo.getMsg());
		return map;
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/save")
	public Map<String, Object> save(HttpServletRequest request) {
		StateInfo stateInfo;
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parent = request.getParameter("father");
		DeptInfo deptInfo = new DeptInfo();
		deptInfo.setPid(request.getParameter("pid"));
		deptInfo.setCompanyid(request.getParameter("companyid"));
		deptInfo.setDeptid(request.getParameter("deptid"));
		deptInfo.setDeptname(request.getParameter("deptname"));
		deptInfo.setDepttype(request.getParameter("depttype"));
		deptInfo.setInserttime(request.getParameter("inserttime"));
		deptInfo.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		if(deptInfo.getIsstop() == 1) {
			deptInfo.setStoptime(bridge.getDate());
		}else {
			deptInfo.setStoptime(Global.NULLSTRING);
		}
		if (CommonUtil.isEmpty(id)) {
			stateInfo = deptInfoService.add(parent, deptInfo, bridge);
		} else {
			stateInfo = deptInfoService.edit(id, deptInfo, bridge);
		}
		Map<String, Object> map = new HashMap<>();
		if (stateInfo.getFlag()) {
			map.put("success", true);
		} else {
			map.put("success", false);
			map.put("message", stateInfo.getMsg());
		}
		return map;
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/del")
	public StateInfo del(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		return deptInfoService.delete(id, parentId, bridge);
	}

	/**
	 * 从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request) {
		return deptInfoService.autoInitFromAccess(new Bridge(request));
	}


	/**
	 * 获取部门树同步json数据
	 * {id:'',text:'',leaf:'',children:[{...},{...}]}
	 */
	@ResponseBody
	@RequestMapping("/deptInfo/treePickerJson")
	public Map<String,Object> treePickerJson(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		map.put("id","test");
		map.put("name","text");
		map.put("leaf",true);
		return map;
	}

	/**
	 * 下载Excel导入模板
	 * @param response
	 * @param model
	 */
	@RequestMapping("/deptInfo/exportTemplate")
	public void exportTemplate(HttpServletResponse response, HttpServletRequest request, Model model){
		try {
			String fileName = "Excel模板_科室.xls";
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
			OutputStream os = response.getOutputStream();
			deptInfoService.exportExcelTemplate(os,new Bridge(request));
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}

	}

	/**
	 * 下载Excel导入模板
	 * @param response
	 * @param model
	 */
	@RequestMapping("/deptInfo/exportExcel")
	public void exportExcel(HttpServletResponse response,HttpServletRequest request, Model model){
		try {
			String fileName = "Excel数据_科室.xls";
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
			OutputStream os = response.getOutputStream();
			deptInfoService.exportData(os,new Bridge(request));
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}
	}

	/**
	 * 导入Excel数据
	 */
	@ResponseBody
	@RequestMapping(value="/deptInfo/importData", method = RequestMethod.POST)
	public Map<String, Object> importData(HttpServletRequest request, Model model,@RequestParam("excelFile") MultipartFile file) {
		return CommonUtil.changeFormResult(deptInfoService.importData(CommonUtil.uploadFileDeal(file), new Bridge(request)));
	}
	
	
	@ResponseBody
	@RequestMapping("/deptInfo/deptTree")
	public List<Map<String,Object>> getObjTree(HttpServletRequest request,Model model){
		String deptNo = request.getParameter("deptNo");
		return deptInfoService.getDeptTree(new Bridge(request));
	}
}