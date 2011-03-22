<%@page import="com.androidodc.eorder.DiningTable"%>
<%@ page language="java" pageEncoding="GB2312"%>

<%@page import="com.androidodc.eorder.DataBaseHelper"%>

<%
    out.println(DataBaseHelper.getOrderDetail());
%>
