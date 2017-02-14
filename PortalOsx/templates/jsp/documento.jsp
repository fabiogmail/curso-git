<%@page import="java.util.*"%>
<%
List Documento = (List)request.getAttribute("Documento");
List Tecnologia = (List)request.getAttribute("Tecnologia");
List Centrais = (List)request.getAttribute("Centrais");
String docSelecionado = (String)request.getAttribute("docSele");
String tecSelecionada = (String)request.getAttribute("TecSele");
String centSelecionada = (String)request.getAttribute("CentSele");
String caminhoDoc = (String)request.getAttribute("caminhoDoc");
String caminhoSelecionado = (String)request.getAttribute("caminhoSelecionado");
String mensagem = (String)request.getAttribute("mensagem");
%>
<html>
<head>
	<title>DOCUMENTO</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Pragma" content="no-cache">
	<link rel="stylesheet" href="/PortalOsx/css/style.css">
	<script language=javascript>
	function validarForm()
	{
		var mensagem = "";
		var campo;
		return true;
	}
	function abrejanela()
	{
		janela = window.open("../"+window.form.caminhoSelecionado.value,"","");
        //interceptacao de erro na abertura da janela
        text = "Se a janela nao estava abrindo talvez seja porque voce tenha um programa bloqueador de pop-up! Observacao » O windows XP service pack 2 bloqueia pop-ups!";
        if(janela == null) { alert(text); return; }
        //fim
        
    }
</script>
</head>
<%if(caminhoSelecionado != null){%>
	<body bgcolor="#FFFFFF" style="margin:0px" onLoad="abrejanela()">
<%}else{%>
	<body bgcolor="#FFFFFF" style="margin:0px">
<%}%>
	<form name="form" method="post" action=action="/PortalOsx/servlet/Portal.cPortal" onsubmit="return validarForm(this);">
			
		<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
	  		<tr>
	  			<td><input type="hidden" name="operacao" value="documento"></td>
	    		<td width="13">&nbsp;</td>
	    		<td width="579" valign="top">
	      			<table width="546" border="0" cellspacing="0" cellpadding="0">
	        			<tr>
	          				<td><img src="/PortalOsx/imagens/docdisponiveis.gif"></td>
	        			</tr>
	        			<tr>
	          				<td>&nbsp;</td>
	        			</tr>
	        			<tr>
	          				<td>
								<!-- Inicio Listagem -->
									<table cellpadding="0" cellspacing="8" border="0">
										<td>
											<tr>
												<td class="campo"><b>Tipo de Documento:</b></td>
												<td>
													<select name="Documento" onchange="document.forms[0].action='/PortalOsx/servlet/Portal.cPortal';submit();">
														<option value="">ESCOLHA </option>
														<%if(Documento != null){%>
															<%for(int i=0;i<Documento.size();i++){%>
																<%if(docSelecionado != null){%>
																	<%if(docSelecionado.equals(""+i)){%>
																		<option value="<%=i%>" selected><%=((String)Documento.get(i))%></option>
																	<%}else{%>
																		<option value="<%=i%>"><%=((String)Documento.get(i))%></option>
																	<%}%>
																<%}else{%>
																	<option value="<%=i%>"><%=((String)Documento.get(i))%></option>
																<%}%>
															<%}%>
														<%}%>
														<input type="hidden" name="primeiraEscolha" value="<%=docSelecionado%>">
						                	        </select>
												</td>
											</tr>
												<%if(Tecnologia != null){%>
												<tr>
													<td class="campo"><b>Tipo de Tecnologia:</b></td>
													<input type="hidden" name="segundaEscolha" value="<%=tecSelecionada%>">
													<td>										
														<select name="Tecnologia" onchange="document.forms[0].action='/PortalOsx/servlet/Portal.cPortal';submit();">
															<option value="">ESCOLHA </option>
															<%for(int i=0;i<Tecnologia.size();i++){%>
																<%if(tecSelecionada != null){%>
																	<%if(tecSelecionada.equals(""+i)){%>
																		<option value="<%=i%>" selected><%=((String)Tecnologia.get(i))%></option>
																	<%}else{%>
																		<option value="<%=i%>"><%=((String)Tecnologia.get(i))%></option>
																	<%}%>
																<%}else{%>
																	<option value="<%=i%>"><%=((String)Tecnologia.get(i))%></option>
																<%}%>
															<%}%>
							                        	</select>
													</td>
												</tr>
					                        	<%}%>
												<%if(Centrais != null){%>
												<tr>
													<td class="campo"><b>Central:</b></td>
													<input type="hidden" name="terceiraEscolha" value="<%=centSelecionada%>">
													<td>										
														<select name="Central" onchange="document.forms[0].action='/PortalOsx/servlet/Portal.cPortal';submit();">
															<option value="">ESCOLHA </option>
															<%for(int i=0;i<Centrais.size();i++){%>
																<%if(centSelecionada != null){%>
																	<%if(centSelecionada.equals(""+i)){%>
																		<option value="<%=i%>" selected><%=((String)Centrais.get(i))%></option>
																	<%}else{%>
																		<option value="<%=i%>"><%=((String)Centrais.get(i))%></option>
																	<%}%>
																<%}else{%>
																	<option value="<%=i%>"><%=((String)Centrais.get(i))%></option>
																<%}%>
															<%}%>											
							                            </select>
													</td>
												</tr>
					                            <%}%>					                            
											
										</td>
										
										<tr>
											
											<td>
												<%if(caminhoDoc != null){%>
													<input type="hidden" name="carregaArquivo" value="true">
													<br><br><a href="#" onclick="document.forms[0].action='/PortalOsx/servlet/Portal.cPortal';submit();"><img border="0" src="/PortalOsx/imagens/abrirdoc.gif"></a>
												<%}%>												
												<%if(caminhoSelecionado != null){%>
													<br><br><input type="hidden" name="caminhoSelecionado" value="<%=caminhoSelecionado%>">
												<%}%>
												<%if(mensagem != null){%>
													<br><br><p><font color="red"><b><%=mensagem%></b></font></p>
												<%}%>
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
	          				<td align="left" valign="top">
	          				<p>Esta funcionalidade disponibiliza por meio da interface web a visualiza&ccedil;&atilde;o dos poss&iacute;veis arquivos de configura&ccedil;&atilde;o do sistema. Os formatos permitidos s&atilde;o .txt e .pdf.</p>
	          				</td>
	        			</tr>
	      			</table>
	    		</td>
	  		</tr>
		</table>
	</form>
</body>
</html>
