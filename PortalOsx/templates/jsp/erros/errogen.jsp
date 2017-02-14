<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
</head>
<%
	String erro = (String)request.getAttribute("erro");
%>
<body bgcolor="#FFFFFF" style="margin:0px">
<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
  <tr> 
    <td width="12">&nbsp;</td>
    <td width="580" valign="top"> 
      <table width="546" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><img src="/PortalOsx/imagens/erro.gif"></td>
        </tr>
        <tr> 
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td>
		  <b><%=erro%></b>
		  </td>
        </tr>
      </table>
      <p>&nbsp;</p>
    </td>
  </tr>
</table>
</body>
</html>
	
