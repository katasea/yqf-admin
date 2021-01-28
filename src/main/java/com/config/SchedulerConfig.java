package com.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.catalina.webresources.FileResource;
//import org.quartz.Scheduler;
//import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 需要quatz的时候把下面的两个注解打开。
 */
//@Configuration
//@EnableScheduling
public class SchedulerConfig {

	@Bean(name="SchedulerFactory")
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setQuartzProperties(quartzProperties());
		return factory;
	}




	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		System.out.println(new ClassPathResource("/application.properties").getPath());
		System.out.println(new PathResource(System.getProperty("user.dir")+"/config/application.properties").getPath());
		propertiesFactoryBean.setLocation(new PathResource(System.getProperty("user.dir")+"/config/application.properties"));
		//在quartz.properties中的属性被读取并注入后再初始化对象
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	/*
	 * quartz初始化监听器
	 */
//	@Bean
//	public QuartzInitializerListener executorListener() {
//		return new QuartzInitializerListener();
//	}

	/*
	 * 通过SchedulerFactoryBean获取Scheduler的实例
	 */
//	@Bean(name="Scheduler")
//	public Scheduler scheduler() throws IOException {
//		return schedulerFactoryBean().getScheduler();
//	}

}
