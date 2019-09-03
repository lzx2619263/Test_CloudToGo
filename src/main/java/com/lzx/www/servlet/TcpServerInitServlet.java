package com.lzx.www.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.lzx.www.model.TcpServer;

/**
 * 启动TCP服务器的类实现ServletContextListener接口
 * 在web.xml中配置listener，tomcat启动时会执行该类的contextInitialized方法
 * @author lzx
 *
 */
public class TcpServerInitServlet extends HttpServlet implements ServletContextListener{
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("开始启动TCP服务器");
		TcpServer tcpServer = new TcpServer();
		tcpServer.init(6666);
		
	}

}
