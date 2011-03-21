<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@page import="com.androidodc.eorder.PrintHelper" %>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
    <form action="identify.jsp" method="get">
    Account<input type="text" name="id"><br>
    Password<input type="text" name="psd"><br>
    <input type="submit"> 
    </form>
    <%
        PrintHelper.print();
    %>
</body>
</html>