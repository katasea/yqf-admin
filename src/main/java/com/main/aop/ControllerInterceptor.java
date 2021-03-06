package com.main.aop;

import com.alibaba.fastjson.JSON;
import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.UserInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Aspect
@Component
@Order(-5)
public class ControllerInterceptor {
	@Value("${server.port}")
	private String port;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 定义一个切入点.
	 * 解释下：
	 * ~ 第一个 * 代表任意修饰符及任意返回值.
	 * ~ 第二个 * 任意包名
	 * ~ 第三个 * 代表任意方法.
	 * ~ 第四个 * 定义在web包或者子包
	 * ~ 第五个 * 任意方法
	 * ~ .. 匹配任意数量的参数.
	 */

	@Pointcut("execution(public * com.main.controller..*.*(..))")

	public void webLog() {
	}


	@Before("webLog()")

	public void doBefore(JoinPoint joinPoint) {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		UserInfo user = (UserInfo) request.getSession().getAttribute("userSession");
		try{
			if(user!=null) {
				//add TraceID
				MDC.put("requestId", user.getUserid() + "|" + user.getUsername());
			}
		}catch (Exception e) {
			org.apache.log4j.Logger.getLogger(this.getClass()).error(e.getMessage(),e);
		}
		// 记录下请求内容
		logger.info("==========================CONTROLLER=============================");
		if (user != null) {
			logger.info("用户信息 : [" + user.getUserid() + "/" + user.getUsername() + "] ");
		}
		logger.info("访问地址  : " + request.getRequestURL().toString());
		logger.info("调用类型  : " + request.getMethod());
		logger.info("客户地址  : " + request.getRemoteAddr() + ":" + port);
		logger.info("服务方法  : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        logger.info("PARAMETER: " + Arrays.toString(joinPoint.getArgs()));
		//获取所有参数方法一：
		Enumeration<String> enu = request.getParameterNames();
		StringBuilder params = new StringBuilder();
		params.append("[");
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			params.append(paraName + ":" + JSON.toJSONString(request.getParameter(paraName))).append(",");
		}
		params.append("]");
		logger.info("参数列表  :" + params);
//		logger.info("=================================================================");
//		logger.info("==> 控制层传递 ");
//		logger.info("                                                                 ");
//		logger.info("                                                                 ");
	}

	@AfterReturning("webLog()")
	public void doAfterReturning(JoinPoint joinPoint) {
		// 处理完请求，返回内容
//		logger.info("<== 控制层结束 ");
	}


}
