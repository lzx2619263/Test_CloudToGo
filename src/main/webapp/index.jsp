<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.lzx.www.model.BasicCell"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<title>基础组件</title>
<body>
	<form style="float:left;margin-right:100px" action="httpAction.jsp" method="get" >
		HTTP服务器域名：
		<input type="text" name="domain" autocomplete="off">
		<br><br>
		HTTP服务器端口：
		<input type="text" name="port" autocomplete="off">
		<br><br>
		服务位置：
		<input type="radio" name="location" value="in">内部
		<input type="radio" name="location" value="out">外部
		<br><br>
		<input type="submit" value=" 确定 ">
	</form>
	<form action="tcpAction.jsp" method="get">
		TCP服务器域名：
		<input type="text" name="domain" autocomplete="off">
		<br><br>
		TCP服务器端口：
		<input type="text" name="port" autocomplete="off">
		<br><br>
		服务位置：
		<input type="radio" name="location" value="in">内部
		<input type="radio" name="location" value="out">外部
		<br><br>
		<input type="submit" value=" 连接 ">
	</form>
	<hr>
	
	<%	
		BasicCell bc = new BasicCell();
		String hostName = bc.getServerName();
		List<String> childCellHttpResList = bc.requestToChildHttpCell();
		List<String> childCellTcpResList = bc.requestToChildTcpCell();
		if(!(childCellHttpResList.isEmpty())) {
			for(String HttpServerName : childCellHttpResList) {
	%>
				<p class="path"><%= hostName %> ==> <%= HttpServerName %></p>
	<%	
			}
		}else if(!(childCellTcpResList.isEmpty())) {
			for(String TcpMsg : childCellTcpResList) {
	%>
				<p class="path"><%= hostName %> ==> <%= TcpMsg %></p>
	<%
			}
		}else {
	%>
			<p class="path"><%= hostName %></p>
	<%	}
    %>	
	
</body>
</html>
