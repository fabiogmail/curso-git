<%@page import="Portal.Utils.Versoes"%>

<html>
<head>
<title>CDRView | Visent - Versao: <%=Versoes.versaoClienteJava%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
</head>

<frameset cols="*" frameborder="NO" border="1" framespacing="1" rows="*"> 
  <frame name="central" scrolling="NO" noresize src="/PortalOsx/servlet/Portal.cPortal?operacao=cliente2">
<!--  <frame name="base"    scrolling="NO" noresize src="/PortalOsx/servlet/Portal.cPortal?operacao=refresh&pagina=clientegen_frm2.htm&texto=cdrviewcliente.txt"> -->
</frameset>

<noframes>
<body bgcolor="#FFFFFF">
</body>
</noframes>
</html>
