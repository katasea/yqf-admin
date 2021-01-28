package com.main.controller.prfm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.common.CommonUtil;
import com.common.constants.GlobalConstants;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.Norm;
import com.main.service.prfm.NormService;
/**
 * 对象控制层
 * @author Tim[ATC.Pro Generate]
 */
@Controller
public class NormController {
	@Resource
	private NormService normService;
	/**
	 * 跳转地址
	 */
	@RequestMapping("/norm/show")
	public String toIndex(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
        String deptOrPerson = request.getParameter("deptOrPerson");
        model.addAttribute("deptOrPerson",deptOrPerson);
		return "function/ShowNorm";
	}
	/**
	 * 获取主页面JSON数据
	 */
	@ResponseBody
	@RequestMapping("/norm/get")
	public Map<String,Object> get(HttpServletRequest request){
		Bridge bridge = new Bridge(request);
		String keyword = request.getParameter("keyword");
		String node = request.getParameter("node");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String deptOrPerson = request.getParameter("deptOrPerson");
		List<Norm> list = normService.get(keyword,node,bridge,start,limit,deptOrPerson);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("root", list);
        map.put("total", list.size());
		return map;
	}
	
	/**
	 *验证页面输入的编号数据库是否已经存在
	 */
	@ResponseBody
	@RequestMapping("/norm/validator")
	public Map<String,Object> validator(HttpServletRequest request) {
		Bridge bridge = new Bridge(request);
		String key = request.getParameter("keyid");//需要验证的字段名
		String value = request.getParameter("value");//需要验证的内容
		Map<String,Object> map = new HashMap<String,Object>();
		StateInfo stateInfo = new StateInfo();
		if(!CommonUtil.isEmpty(key)) {
			stateInfo =  normService.validator(key,value,bridge);
		}
		map.put("success",stateInfo.getFlag());
		map.put("msg",stateInfo.getMsg());
		return map;
	}
	
	/**
	 *保存
	 */
	@ResponseBody
	@RequestMapping("/norm/save")
	public Map<String,Object> save(HttpServletRequest request){
		StateInfo stateInfo = new StateInfo();
		Bridge bridge = new Bridge(request);
		String id = request.getParameter("id");
		String deptOrPerson = request.getParameter("deptOrPerson");
		String parentid = request.getParameter("father");
		Norm norm = new Norm();
		norm.setUid(request.getParameter("id"));
		norm.setText(request.getParameter("text"));
		norm.setChecknorm(request.getParameter("checknorm"));
		norm.setRecordnorm(request.getParameter("recordnorm"));
		norm.setKouFenType(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("kouFenType"))));
		norm.setZhiBiaoType(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("zhiBiaoType"))));
		norm.setJiXiaoZhanBi(Double.parseDouble(CommonUtil.nullToZero(request.getParameter("jiXiaoZhanBi"))));
		norm.setFormula(request.getParameter("formula"));
		norm.setIscomp(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("iscomp"))));
		norm.setIsstop(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("isstop"))));
		norm.setScore(Double.parseDouble(CommonUtil.nullToZero(request.getParameter("score"))));
		norm.setRec1(Double.parseDouble(CommonUtil.nullToZero(request.getParameter("rec1"))));
		norm.setRecsum(Double.parseDouble(CommonUtil.nullToZero(request.getParameter("recsum"))));
		norm.setRecforce(Integer.parseInt(CommonUtil.nullToZero(request.getParameter("recforce"))));
		norm.setBigtype(Integer.parseInt(deptOrPerson));
		if(CommonUtil.isEmpty(id)) {
			stateInfo = normService.add(parentid,norm,bridge);
		}else {
			stateInfo = normService.edit(id,norm,bridge);
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
	@RequestMapping("/norm/del")
	public StateInfo del(HttpServletRequest request){
		return normService.delete(request.getParameter("id"),request.getParameter("parentId"),new Bridge(request));
	}
	
	/**
	 *从access 初始化对应表名的内置数据【带年份的则去掉年份字符】
	 */
	@ResponseBody
	@RequestMapping("/norm/autoInitFromAccess")
	public StateInfo autoInitFromAccess(HttpServletRequest request){
		return normService.autoInitFromAccess(new Bridge(request));
	}
	
	/**
	 * 下载Excel导入模板
	 */
	@RequestMapping("/norm/exportTemplate")
	public void exportTemplate(HttpServletResponse response, HttpServletRequest request) {
		try {
			String fileName = "Excel模板_质量综合指标.xls";
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			OutputStream os = response.getOutputStream();
			normService.exportExcelTemplate(os, new Bridge(request));
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}

	}

	/**
	 * 下载Excel
	 */
	@RequestMapping("/norm/exportExcel")
	public void exportExcel(HttpServletResponse response, HttpServletRequest request) {
		try {
			String fileName = "Excel数据_质量综合指标.xls";
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			OutputStream os = response.getOutputStream();
			normService.exportData(os, new Bridge(request),request.getParameter("keyword"));
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
	@RequestMapping(value = "/norm/importData", method = RequestMethod.POST)
	public Map<String, Object> importData(HttpServletRequest request, @RequestParam("excelFile") MultipartFile file) throws FileNotFoundException {
		return CommonUtil.changeFormResult(normService.importData(CommonUtil.uploadFileDeal(file), new Bridge(request)));
	}
	
	
	@ResponseBody
	@RequestMapping("/norm/normTree")
	public List<Map<String,Object>> getObjTree(HttpServletRequest request,Model model){
		String deptOrPerson = request.getParameter("deptOrPerson");
		String zhiBiaoType = request.getParameter("zhiBiaoType");
		if(zhiBiaoType==null){
			 zhiBiaoType=""+GlobalConstants.ZHIBIAOTYPE_YUE;
		}
		return normService.getDeptTree(new Bridge(request),deptOrPerson,zhiBiaoType);
	}
}
