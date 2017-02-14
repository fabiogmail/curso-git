<%@page import="Portal.Utils.Versoes"%>
<!doctype html>
<html>
<head>
<title>CDRView | Visent - Versão:<%= Versoes.versaoPortal%></title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
</head>

<frameset rows="657,25" frameborder="NO" border="0" framespacing="0" cols="*"> 
  <frame name="central" scrolling="NO" noresize src="/PortalOsx/templates/paginas/index2.jsp" >
  <frame name="base" src="/PortalOsx/servlet/Portal.cPortal?operacao=refresh&pagina=rodape.htm&texto=none" scrolling="no">
</frameset>
<noframes><body bgcolor="#FFFFFF">

</body></noframes>
</html>
