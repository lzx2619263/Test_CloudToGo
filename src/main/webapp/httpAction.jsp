<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.jsoup.Jsoup"%>
<%@ page import="org.jsoup.nodes.Document"%>
<!DOCTYPE html>
<html>
	<title>基础组件</title>
	<body>
		<% 
			String domain = request.getParameter("domain");
			String port = request.getParameter("port");
			String url = "http://" + domain + ":" + port;
			System.out.println(url);
			Document doc=Jsoup.connect(url).get();
		%>
		<%= doc %>
	</body>
</html>
