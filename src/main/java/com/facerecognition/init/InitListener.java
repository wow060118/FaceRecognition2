package com.facerecognition.init;

import java.nio.charset.Charset;
import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 项目初始化类
 * 
 * @author Mr.Bug
 *
 */
public class InitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
			System.out.println("================================项目初始化========================");
			DataSource ds = (DataSource) ac.getBean("pooledDataSource");
			Connection connection = ds.getConnection();// 获取容器与相关的Service对象
			ScriptRunner runner = new ScriptRunner(connection);
			Resources.setCharset(Charset.forName("UTF-8")); // 设置字符集,不然中文乱码插入错误
			runner.setLogWriter(null);// 设置是否输出日志
			runner.runScript(Resources.getResourceAsReader("sql/init.sql"));//执行项目初始化sql
			runner.closeConnection();
			connection.close();
			System.out.println("================================初始化成功========================");

		} catch (Exception e) {
			System.out.println("================================初始化失败========================");
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
