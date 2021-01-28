package com;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.common.Global;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
/**
 * 扫描MyFilter过滤器实现自动建表
 */
@ServletComponentScan
@MapperScan("com.main.dao.*")
@EnableTransactionManagement
public class PMApplication {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PMApplication.class);

	@Value("${wss.server.host}")
	private String host;

	@Value("${wss.server.port}")
	private Integer port;

	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);

		//该处可以用来进行身份验证
		config.setAuthorizationListener(new AuthorizationListener() {
			@Override
			public boolean isAuthorized(HandshakeData data) {
				//http://localhost:8081?username=test&password=test
				//例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息，本文不做身份验证
//				String username = data.getSingleUrlParam("username");
//				String password = data.getSingleUrlParam("password");
				return true;
			}
		});
		logger.info("SOCKET SERVER: " + host + " " + port + " STARTED!!!");
		final SocketIOServer server = new SocketIOServer(config);
		Global.NETTY_SOCKET_SERVER = server;
		return server;
	}

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}


	//DataSource配置
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		List filterList = new ArrayList<>();
		filterList.add(wallFilter());
		druidDataSource.setProxyFilters(filterList);
		return druidDataSource;
//        return new org.apache.tomcat.jdbc.pool.DataSource();
	}

	//DataSource配置
	@Bean(name="secondDatasource")
	@ConfigurationProperties(prefix = "spring.second-datasource")
	public DataSource secondDatasource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
	}

	@Bean
	public WallFilter wallFilter() {
		WallFilter wallFilter = new WallFilter();
		wallFilter.setConfig(wallConfig());
		return wallFilter;
	}

	@Bean
	public WallConfig wallConfig() {
		WallConfig config = new WallConfig();
		config.setMultiStatementAllow(true);//允许一次执行多条语句
		config.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
		return config;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	public static void main(String[] args) {
		SpringApplication.run(PMApplication.class, args);  
	}

}
