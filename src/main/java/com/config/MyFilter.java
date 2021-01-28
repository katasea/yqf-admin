package com.config;
import com.common.auto.db.AutoFixTable;
import com.common.CommonUtil;
import com.common.Global;
import com.main.pojo.platform.Bridge;
import com.main.service.prfm.BaseFormulaService;
import org.apache.log4j.Logger;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName="myFilter",urlPatterns="/*")
public class MyFilter implements Filter {

    @Resource
    private AutoFixTable autoFixTable;

    @Resource
    private BaseFormulaService bb;

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
        String date = request.getParameter("date");
        if(CommonUtil.isEmpty(date)) {
            date = Global.year_month.format(new Date());
        }
        //自动修复表结构
        if(CommonUtil.isEmpty(Global.isCreatedTable.get(date.split("-")[0]))) {
            autoFixTable.run(Integer.parseInt(date.split("-")[0]));
            Global.isCreatedTable.put(date.split("-")[0],"1");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {}

}