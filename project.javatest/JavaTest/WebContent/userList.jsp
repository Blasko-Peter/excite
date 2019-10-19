<%@page import="java.net.URLDecoder"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.json.JSONArray"%>
<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ page import="com.xcite.core.servlet.ProcessResult"%>
<%	ProcessResult result = (ProcessResult)request.getAttribute("processResult"); 
	String content = result.getStringParameter("content");%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>EXCITE - JAVA TEST</title>
		<script src='https://kit.fontawesome.com/a076d05399.js'></script>
		<script type="text/javascript" defer>
			function deleteRow(id){
				let rowId = "row" + id;
				let row = document.getElementById(rowId);
				let tableBody = document.getElementById("tbody");
				tableBody.removeChild(row);
				fetch("/userlist?type=delete&id=" + id,{
				    method: "POST"
				})
			}
		</script>	
	</head>
	<body>
		Hello World
		<%=content %>
	</body>	
		
</html>