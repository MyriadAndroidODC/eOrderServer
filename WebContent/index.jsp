<%@ include file="/WEB-INF/jspf/include.jspf" %>

<%
    StringBuffer url = request.getRequestURL();
%>

<html>

<head>
    <title>eOrder Server</title>
</head>

<body>
<center><h1>eOrder Server<h1></center>
<h2>Fetch data with following URL:</h2>
<h4>
<%= url %>dishes.jsp<br>
<%= url %>dish_category.jsp<br>
<%= url %>dining_tables.jsp<br>
<%= url %>categories.jsp<br>
<%= url %>orders.jsp<br>
<%= url %>order_detail.jsp<br>
</h4>
<h2>Post data with following URL:</h2>
<h4>
<%= url %>post_order.jsp<br>
</h4>
</body>

</html>