<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Portal.Utils.BilhetadorCfgDef"%>
<%@page import="Portal.ReproCdr.CentralItemAgn"%>

<%
	HashMap val = (HashMap)request.getAttribute("valores");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0"> 
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<title>Tela CDR</title>

<script language=javascript>
	
     
     function Incluir()
     {     	
     	
     	var checou;
     	for(var i=0;i<document.getElementById("op2").length;i++)
     	{     		
     		var x = document.getElementById("op2").options[i];     		
     		
     		checou = true;
     		document.form.centrais.value += x.value+'\t';
     		     		
     	}    
     	
    	
     	var dExec = new Date(document.form.ano.value,
     						 document.form.mes.value-1,
     						 document.form.dia.value,
     						 00,
     						 00,
     						 00);
     						 
     	
     	var dAtual = new Date();
 
     	var erros = '';
     	
     	/*if(dExec < dAtual){ 
     		erros = 'Data de Execucao incorreta, anterior a data atual\n';
     		document.form.dia.focus();
     	}*/
  
     	if (!checou) {
     		erros += 'Selecione pelo menos uma central\n';
     	}  		
     	
     	if(erros != ''){
     		alert(erros);
     		document.form.centrais.value = '';
     		document.form.regras.value = '';
     		return false;
     	}
     	else{				 
	     	return true;
     	}
     }
     
     function Adiciona() {
		servidoresList = document.getElementById('op1');
		servidoresSelecionados = document.getElementById('op2');
						
		for(i=0;i<servidoresList.length;i++){
			
			if(servidoresList.options[i].selected){				
				tranferirOpcaoCombo1ToCombo2(servidoresList,servidoresSelecionados,i);
				i--;
			}
		}
				
	}
	
	function Remove() {
		servidoresList = document.getElementById('op1');
		servidoresSelecionados = document.getElementById('op2');
		for(i=0;i<servidoresSelecionados.length;i++){			
			if(servidoresSelecionados.options[i].selected){
				tranferirOpcaoCombo1ToCombo2(servidoresSelecionados,servidoresList,i);
				i--;
			}
		}		
		
	}
	
	function tranferirOpcaoCombo1ToCombo2(combo1,combo2,indice) {
		if (combo1.options[combo1.selectedIndex].text != '') {
			var posicao = combo2.length;
			combo2.options[posicao] = new Option();
			combo2.options[posicao].value = combo1.options[indice].value;
			combo2.options[posicao].text = combo1.options[indice].text;
			combo1.remove(indice);	
		}
	}
     
     
</script>
	
</head>
<body bgcolor="#FFFFFF" style="margin:0px" >
	<form name="form" method="post" action="/PortalOsx/servlet/Portal.cPortal">
		<input type="hidden" name="operacao" value="salvaCDRBruto">
		<input type="hidden" name="centrais" value="">
		<input type="hidden" name="regras" value="">
		<input type="hidden" name="posicao" value="<%=val.get("posicao")%>">
		<input type="hidden" name="acao" value="">
		<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
	  		<tr>
	    		<td width="13">&nbsp;</td>
	    		<td width="579" valign="top">
	      			<table width="546" border="0" cellspacing="0" cellpadding="0">
	        			<tr>
	          				<td><img src="/PortalOsx/imagens/cadagenda.gif"></td>
	        			</tr>
	        			<tr>
	          				<td>&nbsp;</td>
	        			</tr>
	        			<tr>
	          				<td>
								<!-- Inicio Formul&aacute;rio -->
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">																								
												<tr>														
													<td align="left"><b>Data :</b></td>
													<td align=left>
														<select name="dia" size="1" class="lista">
															<%for(int i=1; i <= 31; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("dia").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="mes" size="1" class="lista">
															<%for(int i=1; i <= 12; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("mes").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="ano" size="1" class="lista">
															<%for(int i=2002; i <= 2030; i++){
																String valor = i+"";
																String chk = ""; if(val.get("ano").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
													</td>
												</tr>
												
												<tr>
													<td>
														&nbsp;
													</td>
												</tr>
		
												<tr>
													<td>
														&nbsp;
													</td>
												</tr>												
											</table>
										</td>
									</tr>
									
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									
									<tr>
									<%
										Vector centrais = (Vector)val.get("centrais");
										Vector vetorItensAgn = (Vector)val.get("centraisAgn");
									%>
										<td align="left">&nbsp;											
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr bgcolor="#000033">
													<td align="left" width="100%" colspan="3">
														<font color="#FFFFFF"><b>Centrais</b></font>
													</td>
												</tr>
												<%if(centrais!=null){%>
												<tr>
													<td width="33%">
													<select name="centrais1" id="op1" size="10" class="lista" multiple style="width:200px">
													<%for(int i=0; i<centrais.size(); i++){%>
															<%
															BilhetadorCfgDef central = (BilhetadorCfgDef)centrais.get(i);
															
															boolean temCentral = false;
															CentralItemAgn centralItemAgn = null;
															for(int j=0; j<vetorItensAgn.size(); j++){
															
																centralItemAgn = (CentralItemAgn)vetorItensAgn.get(j);
																
																if(centralItemAgn.getNome().equalsIgnoreCase(central.getBilhetador())){
																	temCentral = true;
																	break;
																}
																
															}
															%>
															<%if(!temCentral){%>
																<%
																	CentralItemAgn centralItemAgnNovo = new CentralItemAgn(); 
																%>
																<option value="<%=centralItemAgnNovo.serializaNovo(central.getBilhetador())%>"><%=central.getBilhetador()%></option>																
																																
															<%}%>														
													<%}%>
													</select>
													</td >
													<td align="center" width="33%">
														&nbsp;&nbsp;<input type="button" name="button1" value="   >>   "  class="button" onClick="return Adiciona()">
														<br><br>
														&nbsp;&nbsp;<input type="button" name="button1" value="   <<   "  class="button" onClick="return Remove()">
													</td>
													<td align="center" width="33%">
													<select name="centrais2" id="op2" size="10" class="lista" multiple style="width:200px">
													<%for(int i=0; i<centrais.size(); i++){%>
															<%
															BilhetadorCfgDef central = (BilhetadorCfgDef)centrais.get(i);
															
															boolean temCentral = false;
															CentralItemAgn centralItemAgn = null;
															for(int j=0; j<vetorItensAgn.size(); j++){
															
																centralItemAgn = (CentralItemAgn)vetorItensAgn.get(j);
																
																if(centralItemAgn.getNome().equalsIgnoreCase(central.getBilhetador())){
																	temCentral = true;
																	break;
																}
																
															}
															%>
															<%if(temCentral){%>
																<option value="<%=centralItemAgn.serializa()%>"><%=central.getBilhetador()%></option>																
															<%}%>																													
													<%}%>
													</select>
													</td>
												</tr>
												<%}%>
											</table>
										</td>
									</tr>
																		
									<tr>
										<td align="left" colspan="2" height="  ">&nbsp;</td>
									</tr>
									<tr>
										<td align="left" colspan="2" height="  ">&nbsp;</td>
									</tr>
								</table>
								<table width="545" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left" width="60">
											<input type="image" src="/PortalOsx/imagens/salvarb.gif" onClick="return Incluir()">
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
	          				<p>Para configurar &agrave; Agenda, Selecione as Centrais desejadas e, em seguida, a Data do CDR e, em seguida clique no Bot&atilde;o Salvar.</p>
	          				</td>
	        			</tr>
	      			</table>
	    		</td>
	  		</tr>
		</table>
	</form>
</body>
</html>    