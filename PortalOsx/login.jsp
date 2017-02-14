<%@page import="Portal.Utils.Versoes"%>
	
<%String msg = (String)request.getSession().getAttribute("mensagem");
	request.getSession().setAttribute("mensagem",null);%>

<html>
<head>
<title>CDRView | Visent - Versão:<%= Versoes.versaoPortal %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpLogonAjax.js'></script>
<style type="text/css">
div#div_motivo {
	position:absolute;
	left: 452px;
	top: 210px;
	background:#FFFFFF;
	z-index:100;
}
</style>
<script language="JavaScript" charset="ISO-8859-1">
<!--
function verificaLogon(){
	if (document.form1.text_usuario.value != "" && document.form1.text_senha.value != ""){
		OpLogonAjax.check(retCheck,document.form1.text_usuario.value);		
		return false;
	}else{
		alert("Preencha os campos corretamente!");
   		return false;
	}	
}
function retCheck(valor){
	OpLogonAjax.verificaLogon(retVerificaLogin,document.form1.text_usuario.value,document.form1.text_senha.value);
}
function retVerificaLogin(valor){
	if(valor[0]){
		document.form1.usuario.value = document.form1.text_usuario.value;
		document.form1.senha.value = document.form1.text_senha.value;
		document.form1.text_usuario.style.backgroundColor = "#E6E6E6"
		document.form1.text_usuario.disabled = true;
		document.form1.text_senha.style.backgroundColor = "#E6E6E6"
		document.form1.text_senha.disabled = true;
		ocultar('login_ok');
		OpLogonAjax.verificaMonitorado(retVerificaMonitorado,document.form1.usuario.value);
	}else{
		if(valor[1] == 2){
			alert("Senha invalida!");
		}
		if(valor[1] == 3){
			alert("Usuario nao existe!");
		}
		if(valor[1] == 4){
			alert("Servidor de Util esta fora do ar! Contate o administrador do sistema.");
		}		
	}
}
function retVerificaMonitorado(valor){
	if(valor[0]){
		mostrar('div_motivo');
		$('text_motivo').focus();
	}else{
		if(valor[1] == 2){
			efetuarSubmit('');
		}		
	}	
}
function fnMotivo(){
	if($('text_motivo').value == ''){
		alert('Preencha o motivo do acesso!')
	}else{
		efetuarSubmit($('text_motivo').value);
	}
	return false;	
}
function efetuarSubmit(motivo){
	document.form1.motivo.value = motivo;
	document.form1.submit();
}
function ValidaForm()
{
   if (document.form1.usuario.value != "" &&
       document.form1.senha.value != "")
   {
		return true;
   }

   alert("Preencha os campos corretamente!");
   return false;
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;

	if(szMensagem != "null")
	{
		if (szMensagem.charAt(0) != "$" && szMensagem.charAt(0) != "@")
		{
			if (szMensagem.charAt(0) == "!")
			{
				Pos = szMensagem.indexOf("+"); // Caracter separador de host/data
				Usuario = szMensagem.substring(1, Pos);
				szMensagem = szMensagem.substring(Pos+1, szMensagem.length);
			    Pos = szMensagem.indexOf("+"); // Caracter separador de host/data
				Host = szMensagem.substring(0, Pos);
				Data = szMensagem.substring(Pos+1, szMensagem.length);
	
				//Mensagem = "Usu&aacute;rio j&aacute; logado a partir de" + Host + " desde "+ Data + "!\nDeseja prosseguir?"
				Mensagem = "Atencao!!!\nUsuario \"" + Usuario + "\" ja esta logado!\nHost: " + Host + "\nInicio: "+ Data + "\nDeseja prosseguir?"
	
				// Realiza o logout do usu&aacute;rio j&aacute; conectado e o logon deste
				if (confirm(Mensagem))
				{
	                document.form2.usuarios.value = "USR:"+Usuario;
					document.form2.submit();
				}
			}
			else alert (document.form1.mensagem.value);
		}
	}
//	if (szMensagem.charAt(0) == "@")
//       window.open("/PortalOsx/templates/paginas/index.htm","CDRView","status=yes,width=780,height=523");
}

function mostrar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="block";
}

function ocultar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="none";
}
//-->
</script>
<%
	String erroConfig = (String)getServletContext().getAttribute("ErroConfig");
	if(erroConfig != null)
	{
		request.setAttribute("erro", erroConfig);
		request.getRequestDispatcher("/templates/jsp/erros/errogen.jsp").forward(request, response);
	}
	
	
%>

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF" onLoad="VerificaMensagem()">
<table width="100" border="0" cellspacing="0" cellpadding="0" >
   <tr>
    <td height="1" bgcolor="#FFFFFF"><img src="/PortalOsx/imagens/1x1.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td height="1">
      <table width="780" border="0" cellpadding="0" cellspacing="0" bgcolor="#fff" background="/PortalOsx/imagens/login/bg.gif">
        <tr>
          <td width="780" height="80" valign="bottom" ><img src="/PortalOsx/imagens/login/logo.png" width="315" height="74"></td>
        </tr>
        <tr>
          <td height="426" colspan="3" > </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="1" bgcolor="#FFFFFF"> <!-- linha -->
      <div align="right"><img src="/PortalOsx/imagens/login/branco.gif" width="156" height="1"></div>
    </td>
  </tr>
  <tr bgcolor="#070C23"> <!-- rodapé -->
    <td height="10">&nbsp;</td>
  </tr>
</table> 

<div style="background:#f7f7f7; border:4px solid #e0e0e0; position:absolute; left:160px; top:145px; width:360px; height:200px; padding:35px; -moz-border-radius:5px; -webkit-border-radius:5px; border-radius:5px; -moz-box-shadow:1px 1px 18px #000; -webkit-box-shadow:1px 1px 18px #000; box-shadow:1px 1px 18px #000;"> 
	
	<div id="Layer1" style="margin:-20px 0 5px 240px; z-index:1"><img src="/PortalOsx/imagens/logo_cliente.gif"></div>
	
	<div id="Layer2" style="z-index:2">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td><h3>Login</h3> </td>
	    </tr>
	    <tr>
	      <td>
	        <form name="form1" method="post" action="/PortalOsx/servlet/Portal.cPortal">
		        <input type="hidden" name="operacao" value="indicaLogon">
		        <input type="hidden" name="motivo" value="">
				<input type="hidden" name="usuario" value="">
				<input type="hidden" name="senha" value="">
		        <input type="hidden" name="mensagem" value="<%=msg%>">
		        <table width="134" border="0" cellspacing="0" cellpadding="0">
		          <tr>
		            <td colspan="2"><font color="#000000">Usu&aacute;rio</font>:</td>
		          </tr>
		          <tr>
		            <td colspan="3">
		              <input type="text" name="text_usuario" class="forms" style="border:1px solid #dfdfdf" padding="5px" size="25" value="administrador">
		            </td>
		          </tr>
		          <tr>
		            <td colspan="2"><font color="#000000">Senha:</font></td>
		          </tr>
		          <tr>
		            <td valign="top" width="106">
		            	<input type="password" name="text_senha" class="forms" style="border:1px solid #dfdfdf" padding="5px" size="25" value="admin">
		            </td>
		            <td width="36">
						<input type="submit"  onfocus="true" name="Submit2" value="ok" id="login_ok" onClick="return verificaLogon()">
		            </td>
		          </tr>
		        </table>
	        </form>
	
	        <form name="form2" method="post" action="/PortalOsx/servlet/Portal.cPortal">
		        <input type="hidden" name="operacao" value="desconexaoPortal">
		        <input type="hidden" name="usuarios" value="">
			</form>
	      </td>
	    </tr>
	    <tr>
	      <td height="7">&nbsp;</td>
	    </tr>
	    <tr>
	      <td><h3>Ajuda</h3> </td>
	    </tr>
	    <tr>
	      <td>
	        <p>Em caso de d&uacute;vidas entre em contato com o administrador do sistema
	          ou com o suporte t&eacute;cnico atrav&eacute;s do e-mail <b>suporte@visent.com.br</b></p>
	        <p>&nbsp;</p>
	      </td>
	    </tr>
	  </table>
	</div>
		
</div>
</body>
</html>

<!-- style="display:none" -->
<div id="div_motivo" style="display:none; background:#f7f7f7;" >	  
<table width="100" border="0" cellspacing="0" cellpadding="0">
	<form name="form10">
		  <tr>
			<td colspan="2" align="left"><font color="#000000">Motivo do Acesso:</font></td>
		  </tr>
		  <tr>
		  	<td valign="top" width="106" align="left">
	      		<textarea cols="15" rows="7" id="text_motivo" class="forms" style="border:1px solid #dfdfdf""></textarea>
	      	</td>
		  </tr>
		  <tr>
		  	<td width="36" align="left">
		  		<input type="submit"  onfocus="true" name="Submit2" value="ok" onClick="return fnMotivo()">
	      	</td>
		  </tr>
	</form>
</table>	  
</div>

