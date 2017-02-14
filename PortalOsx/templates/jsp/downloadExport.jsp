<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/download.tld" prefix="do" %>

<%
    String usuario = (String) request.getParameter("usuario");
    String arquivo = (String) request.getParameter("arquivo");
    String dir     = (String) request.getParameter("diretorio");
    dir = dir+usuario;
    System.out.println("\n\narquivo: "+arquivo);
    System.out.println("dir: "+dir+"\n\n");
%>

<do:download file="<%= arquivo %>" dir="<%= dir %>"/>