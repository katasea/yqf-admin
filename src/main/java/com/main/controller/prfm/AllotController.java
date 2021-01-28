package com.main.controller.prfm;

import com.main.pojo.platform.Bridge;
import com.main.service.prfm.AllotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/***
 * 分配计算工分
 * */
@Controller
public class AllotController {

    @Resource
    private AllotService service;

    @RequestMapping("/Allot/calculatePage")
    public String calculatePage(HttpServletRequest request,Model model){
        model.addAttribute("baseUrl",request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
        return "function/ShowCalculatePage";
    }
    /**
     *  进行计算第二步，分配，计算工分
     *
     * */
    @RequestMapping("/Allot/calculateAllot")
    @ResponseBody
    public Map<String,Object> calculateAllot(HttpServletRequest request){

        Map<String,Object> resultMap = new HashMap<String,Object>();
        Bridge bridge = new Bridge(request);

        resultMap = service.calculateAllot(bridge);

        return resultMap;
    }
}
