<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Portal.Utils.Tecnologia"%>
<%
	ArrayList tecnologias = (ArrayList)request.getAttribute("tecnologias");
	ArrayList relatorios = (ArrayList)request.getAttribute("relatorios");
	String listaNomeAcessos =(String)request.getAttribute("listaNomeAcessos");
	String listaRelacionamentos = (String)request.getAttribute("listaRelacionamentos");
	String listaNomesPerfis = (String)request.getAttribute("listaNomesPerfis");
	String listaIdsPerfis = (String)request.getAttribute("listaIdsPerfis");
	String listaSigiloTelefonico = (String) request.getAttribute("listaSigiloTelefonico");
	String listaNomesTecnologias = (String) request.getAttribute("listaNomesTecnologias");
	String listaIdsTecnologias = (String) request.getAttribute("listaIdsTecnologias");
	String listaNomesRelatorios = (String) request.getAttribute("listaNomesRelatorios");
	String listaIdsRelatorios = (String) request.getAttribute("listaIdsRelatorios");
	String alertaMensagem = (String)request.getAttribute("mensagem");
	
	String[] listaNomes = listaNomesPerfis.split(";");
	String[] listaIds = listaIdsPerfis.split(";");
	
%>
<html>
<head>
	<title>Perfis</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Pragma" content="no-cache">
	<link rel="stylesheet" href="/PortalOsx/css/style.css">
	<script type="text/javascript" language="JavaScript" charset="ISO-8859-1" src="/PortalOsx/templates/js/perfisSercomtel.js"></script>
</head>

<body bgcolor="#FFFFFF" style="margin:0px" onLoad="Processa(0)">
<table width="780" height="383" border="0" cellspacing="0"
	cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
	<tr>
		<td width="13">&nbsp;</td>
		<td width="579" valign="top">
		<table width="546" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/PortalOsx/imagens/perfis.gif"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><!-- Inicio Formul&aacute;rio -->
				<table width="545" border="0" cellspacing="0" cellpadding="0">
					<form name="form1" method="post" action="">
					<input type="hidden" name="ListaPerfisNomes" value="<%=listaNomesPerfis%>">
					<input type="hidden" name="ListaPerfisIds" value="<%=listaIdsPerfis%>">
					<input type="hidden" name="ListaPerfisModosDeAcesso"  value="<%=listaNomeAcessos%>"> 
					<input type="hidden" name="Relacionamento" value="<%=listaRelacionamentos%>">
					<input type="hidden" name="listaSigiloTefonico" value="<%=listaSigiloTelefonico%>">
					<input type="hidden" name="ListaTec" value="<%=listaNomesTecnologias%>">
					<input type="hidden" name="ListaIdTec" value="<%=listaIdsTecnologias%>">
					<input type="hidden" name="ListaRel" value="<%=listaNomesRelatorios%>">
					<input type="hidden" name="ListaIdRel" value="<%=listaIdsRelatorios%>">
					<input type="hidden" name="mensagem" value="<%=alertaMensagem%>">
					<tr>
						<td align="left"><b>Perfis: &nbsp;</b></td>
						<td align="left">
						
						<select name="ListaPerfis" size="1" onChange="Processa(4)">
						
						<%
						  for(int i=0; i<listaNomes.length; i++){%>
							<option value="<%=listaIds[i]%>"><%=listaNomes[i]%>
						<%}%>
						</select></td>
						<td align="left"><b>Acesso: &nbsp;</b>
						<select name="ListaModosDeAcesso" size="1">
						<%String listaAcessos[] = listaNomeAcessos.split(";");
							for(int i=0; i<listaAcessos.length; i++){%>
							<option><%=listaAcessos[i]%>
						<%}%>
						</select></td>
					</tr>
					<tr>
						<td align="left" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td align="left" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td align="left">
							<b>Novo Perfil: &nbsp;</b>
						</td>
						<td align="left">
							<input type="text" name="perfil">
						</td>

						<td align="left" colspan="0" width="273">
							<b>Sigilo Telef&ocirc;nico: &nbsp;</b>
							<input type="checkbox" name="habilita">
						</td>
					</tr>
					<tr>
						<td align="left" colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td align="left" colspan="2">&nbsp;</td>
					</tr>
					</form>
				</table>
				<table width="545" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="left" width="60">
						<form name="form2" method="post" action="/PortalOsx/servlet/Portal.cPortal">
							<input type="hidden" name="operacao" value=""> 
							<input type="hidden" name="perfil" value=""> 
							<input type="hidden" name="acesso"	value="">
							<input type="hidden" name="tecnologias" value=""> 
							<input type="hidden" name="relatorios" value=""> 
							<input type="hidden" name="habilitaSigilo" value="">
							<input type="hidden" name="selecionaTodasTec" value="">
							<input type="hidden" name="selecionaTodosRel" value="">							
							<input type="hidden" name="perfilAlteracao" value="">
							<input type="image" src="/PortalOsx/imagens/salvar.gif" onClick="return Processa(1)"></form>
						</td>
						<td align="left" width="60">
						<form name="form3" method="post" action="/PortalOsx/servlet/Portal.cPortal">
							<input type="hidden" name="operacao" value="excPerfil"> 
							<input type="hidden" name="perfil" value=""> 
							<input type="hidden" name="id" value="">
							<input type="image" src="/PortalOsx/imagens/excluir.gif" onClick="return Processa(2)"></form>
						</td>
						<td align="left">&nbsp;</td>
					</tr>

				</table>

				<!-- Fim Formul&aacute;rio --></td>
			</tr>
		</table>
		</td>
		<td width="142" valign="top">
		<table width="100" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" width="141"
					height="19"></td>

			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="left" valign="top">
				<p>Um Perfil define o tipo de acesso e permiss&otilde;es associados
				aos usu&aacute;rios.</p>
				<p>Para incluir um perfil, defina o nome do perfil no campo Novo
				Perfil, o tipo de acesso no campo Acesso e, em seguida, clique no
				bot&atilde;o Incluir.</p>
				<p>Para excluir um perfil existente, selecione o perfil desejado no
				campo Perfis e, em seguida, clique no bot&atilde;o Excluir.</p>
				<p>ATEN&Ccedil;&Atilde;O: o perfil "admin" n&atilde;o pode ser
				exclu&iacute;do. Tampouco pode ser criado um novo perfil com tipo de
				acesso igual a "admin".</p>

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
</body>
</html>
