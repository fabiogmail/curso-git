<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="Portal.Cluster.NoUtil" %>
<%
  boolean ordemInversa = (boolean)NoUtil.getNoCentral().getConexaoServUtil().getM_iUtil().fnGetOrdemInvProcParser();
  
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" href="/PortalOsx/css/style.css">
		<title>Ordem de Processamento de CDR</title>
		<script language=javascript>
			function editar(){
				document.form.submit();
				alert('Ordem Salva com sucesso');
			}
			
			function marca(op){
				if (op == 'op2') {
					document.getElementById('op1').checked=false;
					document.getElementById('op2').checked=true;
				} else {
					document.getElementById('op2').checked=false;
					document.getElementById('op1').checked=true;
				}

			}
			
		</script>
	</head>

<body bgcolor="#FFFFFF" style="margin:0px">

	<form name="form" method="post"	action="/PortalOsx/servlet/Portal.cPortal">
			
		<table width="780" height="383" border="0" valign="top" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
			<input type="hidden" name="operacao" value="salvaOrdemProcessamento">
	  		<tr>
	    		<td width="13">&nbsp;</td>
	    		<td width="579" valign="top">
	      			<table width="546" border="0" cellspacing="0" cellpadding="0">
	        			<tr>
	          				<td><img src="/PortalOsx/imagens/ordemproccdr.gif"></td>
	        			</tr>
	        			<tr>
	          				<td>&nbsp;</td>
	        			</tr>
	        			<tr>
	          				<td>
								<!-- Inicio Listagem -->
									<table cellpadding="2" cellspacing="0" border="0">
										<td>
												<tr bgcolor="#000033">
													<td align="left" width="250"><font color="#FFFFFF"><b>Ordem<font></b></td>
													<td align="center" width="149"><font color="#FFFFFF"><b>Habilitada<font></b></td>								
												</tr>		
												<tr bgcolor="#FFFFFF">											
													<td>														
														<b>Normal&nbsp;</b>
													</td>
													<%if(ordemInversa == true){%>
														<td align="center"><input type="checkbox" name="ordemProcessamento" id="op1" value="OrdemNormal" onClick="marca('op1')"></td>
													<%}else{%>
														<td align="center"><input type="checkbox" name="ordemProcessamento" id="op2" value="OrdemNormal" onClick="marca('op2')" checked="true"></td>
													<%}%>	
												</tr>
												<tr bgcolor="#F0F0F0">											
													<td>														
														<b>Inversa&nbsp;</b>
													</td>
													<%if(ordemInversa == true){%>
														<td align="center"><input type="checkbox" name="ordemProcessamento"  id="op2" value="OrdemInversa" onClick="marca('op2')" checked="true"></td>
													<%}else{%>
														<td align="center"><input type="checkbox" name="ordemProcessamento" id="op1" value="OrdemInversa" onClick="marca('op1')"></td>
													<%}%>
												</tr>
										</td>
									
										<tr>
											
											<td>
												<br><br><input type="image"	src="/PortalOsx/imagens/salvar.gif" onClick="return editar()">
											</td>
										</tr>
									</table>
								<!-- Fim Formul&aacute;rio -->
	          				</td>
	        			</tr>
	      			</table>
	    		</td>
	    		<td width="142" valign="top">
	      			<table width="100" border="0" cellspacing="0" cellpadding="0">
	        			<tr>
	          				<td><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" width="141" height="19"></td>
	        			</tr>
	        			<tr>
	          				<td>&nbsp;</td>
	        			</tr>
	        			<tr>
	          				<td align="left" valign="top"><p>Esta op&ccedil;&atilde;o funciona da seguinte forma:	Ordem Normal(Do mais antigo para o mais novo).	Ordem Inversa&nbsp;(Do mais novo para o mais antigo).</p></td>
	        			</tr>
	      			</table>
	    		</td>
	  		</tr>
		</table>
	</form>
</body>
</html>
