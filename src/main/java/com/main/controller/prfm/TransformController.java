package com.main.controller.prfm;

import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.Transform;
import com.main.service.prfm.TransformService;
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
 * @Author: sjl
 * @Date: 2018/4/30 21:07
 * @param
 * @Description:
 *
 * 转换定义
 */
@Controller
public class TransformController {

    @Resource
    private TransformService transformService;

    /**
     * 跳转地址
     */
    @RequestMapping("/transform/show")
    public String toIndex(HttpServletRequest request, Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
        return "function/ShowTransformList";
    }

    @ResponseBody
    @RequestMapping("/transform/get")
    public Map<String,Object> getData(HttpServletRequest request){

        Map<String,Object> resultMap = new HashMap<>();
        Bridge bridge = new Bridge(request);
        String typeID = request.getParameter("typeID");

        List<Transform> resultList = transformService.getDataByType(typeID,bridge);
        resultMap.put("root",resultList);
        return resultMap;
    }


    @ResponseBody
    @RequestMapping("/transform/add")
    public Map<String,Object> save(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Bridge bridge = new Bridge(request);

        Transform transform = new Transform();
        transform.setId(request.getParameter("id"));
        transform.setTheyear(Integer.parseInt(bridge.getYear()));
        transform.setThemonth(Integer.parseInt(bridge.getMonth()));
        transform.setCompanyid(bridge.getCompanyid());
        transform.setTypeID(request.getParameter("typeID"));
        transform.setType_name(request.getParameter("type_name"));
        transform.setCondition_id(request.getParameter("condition_id"));
        transform.setCondition_name(request.getParameter("condition_name"));
        transform.setCondition_value(request.getParameter("condition_value"));
        transform.setAdjustment_id(request.getParameter("adjustment_id"));
        transform.setAdjustment_name(request.getParameter("adjustment_name"));
        transform.setAdjustment_value(request.getParameter("adjustment_value"));
        transform.setTransform_percent(request.getParameter("transform_percent"));

        resultMap.put("success", this.transformService.save(transform));

        return resultMap;
    }


    @ResponseBody
    @RequestMapping("/transform/del")
    public Map<String,Object> del(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Bridge bridge = new Bridge(request);
        Transform transform = new Transform();

        transform.setTheyear(Integer.parseInt(bridge.getYear()));
        transform.setThemonth(Integer.parseInt(bridge.getMonth()));
        transform.setCompanyid(bridge.getCompanyid());
        transform.setId(request.getParameter("id"));

        resultMap.put("success", this.transformService.del(transform));

        return resultMap;
    }

    /**
     *  进行原始数据的转换
     *
     * */
    @RequestMapping("/transform/transform")
    @ResponseBody
    public Map<String,Object> transform(HttpServletRequest request){

        Map<String,Object> resultMap = new HashMap<>();
        String month1 = request.getParameter("month1");//转换开始月份
        String month2 = request.getParameter("month2");//准换结束月份
        Bridge bridge = new Bridge(request);

        this.transformService.transform(bridge,month1,month2);

        resultMap.put("success",true);
        resultMap.put("msg","转换完成");

        return resultMap;
    }
}
