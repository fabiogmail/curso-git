<%
	String senha = (String)request.getAttribute("senha");
%>
<html>

<head>
<title>CDRView | Visent</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type="text/javascript" language="JavaScript" charset="ISO-8859-1">
	function validaForm(){
		if(document.form.senha1.value.length < 4){
			alert('Nova senha deve ter tamanho maior que três caracteres');
			document.form.senha1.value = "";
			document.form.senha2.value = "";
			return false;
		}
		if(document.form.senha1.value != document.form.senha2.value){
			alert('A Confirmação deve ser igual a senha Nova!');
			document.form.senha1.value = "";
			document.form.senha2.value = "";
			return false;
		}
		var expressao = new RegExp("[^A-Za-z0-9._-]", "gi");
   		Ret = document.form.senha1.value.search(expressao);
   		if (Ret != -1)
   		{
      		alert("A senha somente pode conter caracteres alfanumericos alem de '\.\', \'_\' e \'-\'!");
      		return false;
      	}
		return true;		
	}
function VerificaMensagem()
{
    szMensagem = document.form.mensagem.value;

	if (szMensagem.charAt(0) != "$" && szMensagem.charAt(0) != "@")
	{
		alert (document.form.mensagem.value);
		if(document.form.mensagem.value == 'Senha alterada com sucesso!'){
			document.form.operacao.value ="redirecionaIndex";
			document.form.submit();
		}
		return false;
	}
}
	

</script>


</head>

<body bgcolor="#FFFFFF" style="margin:0px" onLoad="VerificaMensagem()">
<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
  <tr>
    <td height="1" bgcolor="#FFFFFF"><img src="/PortalOsx/imagens/1x1.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td height="74" bgcolor="0D1237"><img src="/PortalOsx/imagens/logo.gif" width="316" height="74" border="0"><img src="/PortalOsx/imagens/meio.gif" width="288" height="74"><img src="/PortalOsx/imagens/direita.gif" width="176" height="74"></td>
  </tr>
  <tr>
    <td height="1" bgcolor="#FFFFFF"><img src="/PortalOsx/imagens/1x1.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td  valign="top"> 
      <table width="780" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><center><img src="/PortalOsx/imagens/alteracaosenha.gif"></center></td>
          <td><center><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" width="141" height="19"></center></td>
        </tr>
        <tr> 
          <td>&nbsp;</td>
        </tr>
        <tr> 
          <td>
			<!-- Inicio Formul&aacute;rio -->
			<form name="form" method="post" action="/PortalOsx/servlet/Portal.cPortal">
			<input type="hidden" name="operacao" value="cadastraSenhaNova">
			<input type="hidden" name="senhaAntiga" value="<%=senha%>">			
				<table width="545" border="0" cellspacing="0" cellpadding="0">			
				<input type="hidden" name="mensagem" value="$ARG;">
				
				<tr>
					<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" width="135">
						<b>Senha Atual: &nbsp;</b>
					</td>
					<td align="left">
						<input type="password" name="senhaatual" value="" maxsize="10" size="12">
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" width="135">
					<b>Nova Senha: &nbsp;</b>
					</td>
					<td align="left">
						<input type="password" name="senha1" value="" maxsize="10" size="12">
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" width="135">
						<b>Confirma&ccedil;&atilde;o de Senha: &nbsp;</b>
					</td>
					<td align="left">
						<input type="password" name="senha2" maxsize="10" size="12">
					</td>
					</tr>
					<tr>
						<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						&nbsp;
					</td>
				</tr>			
			</table>			
			<table width="545" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left" width="60">
						<input type="image" src="/PortalOsx/imagens/alterar.gif" onClick="return validaForm()">
					</td>
					<td align="left" width="60">
						&nbsp;
					</td>
					<td align="left">
						&nbsp;
					</td>
				</tr>
			</table>
			</form>
          </td>
          <td width="142" valign="top"> 
      <table width="100" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td align="left" valign="top">
			   <p>A senha &eacute; igual ao nome para sua seguran&ccedil;a ela deve ser trocada </p> <p>Para alterar a sua senha, preencha os campos apresentados ao lado e, em seguida, clique em Alterar.</p>
             </td>
           </tr>
           <tr>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td>&nbsp;</td>
           </tr>
          </table>
         </td>	
        </tr>
      </table>
    </td>
    
  </tr>
</table>
<div id="Layer1" style="position:absolute; left:635px; top:25px; width:135px; height:66px; z-index:1"><img src="/PortalOsx/imagens/logo_cliente.gif"></div>
</body>
</html>
