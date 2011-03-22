<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>

<%
    StringBuffer url = request.getRequestURL();
%>

<html>

<head>
    <title>eOrder Server</title>
</head>

<body>
<h2>It's a server for eOrder<br>
You can fetch data with following URL:
</h2>
<h4>
<%= url %>dishes.jsp<br>
<%= url %>dish_category.jsp<br>
<%= url %>dining_tables.jsp<br>
<%= url %>categories.jsp<br>
<%= url %>orders.jsp<br>
<%= url %>order_detail.jsp<br>
</h4>
</body>

</html>