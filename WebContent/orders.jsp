<%@ include file="/WEB-INF/jspf/include.jspf" %>

<%
    out.println(DataBaseHelper.getOrders(request));
%>
