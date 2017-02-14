<%@page import="Portal.Utils.Versoes"%>
<!doctype html>
<html>
<head>
<title>CDRView | Visent - Versão:<%= Versoes.versaoPortal%></title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
</head>

<frameset rows="125,552*" frameborder="NO" border="1" framespacing="1" cols="*"> 
  <frame name="topo" scrolling="NO" noresize src="/PortalOsx/servlet/Portal.cPortal?operacao=logonOk">
  <frame name="frmMain" src="/PortalOsx/servlet/Portal.cPortal?operacao=logonSaudacao">
</frameset>
<noframes><body bgcolor="#FFFFFF">

</body></noframes>
</html>
