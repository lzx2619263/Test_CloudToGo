<%@page import="com.lzx.www.model.TcpClient"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<title>基础组件</title>
	<body>
	<p>请稍后</p>
		<% 
			String domain = request.getParameter("domain");
			String port = request.getParameter("port");
			TcpClient tcl = new TcpClient();
			tcl.init(domain, (Integer.parseInt(port)));
			String msg = tcl.receiveMsg;
			while(msg == null) {
				System.out.println("TCP连接完成");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				msg = tcl.receiveMsg;
			}
		%>
		<p><%= msg %></p>
		<% tcl.close(); %>
	</body>
</html>