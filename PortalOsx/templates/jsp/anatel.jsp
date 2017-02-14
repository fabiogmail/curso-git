<%@ page language="java" %>
<%@page import="Portal.Cluster.NoUtil"%>
<%@page import="Portal.Cluster.No"%>


<%
    No noCent = NoUtil.getNoCentral();
    String host = noCent.getHostName();
    String anatel = "http://"+host+"/DWeb";
    System.out.println(anatel);
    response.sendRedirect(anatel);
%>

