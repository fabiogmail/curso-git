<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%
	String codebase = (String)request.getAttribute("codebase");
	String name = (String)request.getAttribute("name");
	String tipo = (String)request.getAttribute("type");
	String usuario = (String)request.getAttribute("usuario");
	String perfil = (String)request.getAttribute("perfil");
	String ior = (String)request.getAttribute("ior");
	String host = (String)request.getAttribute("host");
	String podeAbrir = (String)request.getAttribute("podeAbrir");
	String portaRmi = (String)request.getAttribute("portaRmi");
	String hostName = (String)request.getAttribute("hostName");
	String subCliente = (String)request.getAttribute("subCliente");
	
	System.out.println(codebase);
	System.out.println(name);
	System.out.println(tipo);
	System.out.println(ior);
	System.out.println(portaRmi);
	System.out.println(subCliente);
	System.out.println(usuario);
	System.out.println(perfil);
	System.out.println(host);
	System.out.println(hostName);
	System.out.println(podeAbrir);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0"> 
<style type="text/css">
</style>
<title>CDRView</title>
<script src="http://www.java.com/js/deployJava.js"></script>
<script type="text/javascript">
<!--
var codebase = '<%=codebase%>';
var name = '<%=name%>';
var tipo = '<%=tipo%>';
var usuario = '<%=usuario%>';
var perfil = '<%=perfil%>';
var ior = '<%=ior%>';
var host = '<%=host%>';
var podeAbrir = '<%=podeAbrir%>';
var portaRmi = '<%=portaRmi%>';
var hostName = '<%=hostName%>';
var subCliente = '<%=subCliente%>';


function abre(){
    var attributes = {codebase:codebase, 
    				  code:'cCDRView2.class',  
                      archive:'sCDRView2.jar,sInterfaces2.jar,cLibjava.jar,jfreechart-0.9.8.jar,jcommon-0.8.2a.jar,junit.jar,xerces.jar,xstream-1.3.1.jar,stax-api-1.0.1.jar,jettison-1.3.jar,poi-3.8-20120326.jar,ServAgenda.jar,poi-ooxml-3.8-20120326.jar,poi-ooxml-schemas-3.8-20120326.jar,xmlbeans-2.3.0.jar,dom4j-1.6.1.jar',  
                      width:'100%',  height:'100%', name:name };
    var parameters = {NAME:name, type:tipo, USUARIO:usuario, PERFIL:perfil, IOR:ior,
    				  HOST:host, PODEABRIR:podeAbrir, PORTARMI:portaRmi, HOSTNAME:hostName, SUBCLIENTE:subCliente} ;
    var version = '1.6';
    deployJava.runApplet(attributes, parameters, version);

    document.body.style.margin='0px';

}
-->
</script>

</head>
<body style="background-color: #ddd" onload="abre()">
<body style="margin:0px;" onload="abre()">
</body>
</html>