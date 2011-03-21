<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
    <%
    String id = (String)request.getParameter("id");
    String psd = (String)request.getParameter("psd");
    if (id.equals("root") && psd.equals("root"))
        pageContext.forward("sucess.jsp");
    else
        pageContext.forward("error.jsp");
    %>
</body>
</html>