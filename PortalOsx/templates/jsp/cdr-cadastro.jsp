<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Portal.Utils.BilhetadorCfgDef"%>
<%@page import="Portal.ReproCdr.CentralItemAgn"%>
<%@page import="Portal.ReproCdr.ItemAgenda"%>
<%@page import="Portal.ReproCdr.RegraItemAgn"%>

<%
	HashMap val = (HashMap)request.getAttribute("valores");
	String tipoCdr = (String)request.getAttribute("tipoCdr");
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0"> 
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<title>Tela CDR</title>

<script language=javascript>
	<!--
     
     function Incluir(agendar)
     {
     	if(agendar)
     	{
     		if(!confirm('Deseja realmente agendar esse item?'))
     		{
     			return false;
     		}
     		document.form.acao.value = 'agendar';
     	}
     	else
     	{
     		document.form.acao.value = 'salvar';
     	}
     	
     	var checou;
     	for(var i=0;i<document.getElementById("op2").length;i++)
     	{     		
     		var x = document.getElementById("op2").options[i];     		
     		
     		checou = true;
     		document.form.centrais.value += x.value+'\t';
     		     		
     	}     	
     	
     	for (var i=0; i<document.form.elements.length; i++) 
   		{
     		var x = document.form.elements[i];
	 		/*if (x.name == 'UIDL[]') 
 			{ 	alert("ola");
 			    if(x.checked)
 		    	{
 		    		checou = true;
 		    		document.form.centrais.value += x.value+'\t';
	 		    }
			}*/ 
			
			if (x.name == 'REGRA[]') 
 			{ 
 			    if(x.checked)
 		    	{
 		    		document.form.regras.value += x.value+'\t';
	 		    }
			}
			
     	}
     	
    	
     	var dExec = new Date(document.form.anoExec.value,
     						 document.form.mesExec.value-1,
     						 document.form.diaExec.value,
     						 document.form.horaExec.value,
     						 document.form.minExec.value,00);
     						 
     	var dInicial = new Date(document.form.anoInicial.value,
     						 document.form.mesInicial.value-1,
     						 document.form.diaInicial.value,
     						 document.form.horaInicial.value,
     						 document.form.minInicial.value,00);
     						 
     	var dFinal = new Date(document.form.anoFinal.value,
     						 document.form.mesFinal.value-1,
     						 document.form.diaFinal.value,
     						 document.form.horaFinal.value,
     						 document.form.minFinal.value,00);
     	
     	var dAtual = new Date();
     	
     	var erros = '';
     	
     	if(dExec < dAtual){ 
     		erros = 'Data de Execucao incorreta, anterior a data atual\n';
     		document.form.diaExec.focus();
     	}
     	if(dFinal < dInicial){ 
     		erros += 'Data Final incorreta, anterior a data inicial\n';
     		document.form.diaInicial.focus();
     	}
     	var diferenca = dFinal-dInicial;
     	if(diferenca == 0){
     		erros += 'Data inicial igual data final\n';
     		document.form.diaInicial.focus();
     	}
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
	     	if(document.form.horaInicial.disabled)
	     	{
	     		
	     		document.form.horaInicial.value = '00';document.form.horaInicial.disabled = false;
	     		document.form.minInicial.value = '00';document.form.minInicial.disabled = false;
	     		document.form.horaFinal.value = '00';document.form.horaFinal.disabled = false;
	     		document.form.minFinal.value = '00';document.form.minFinal.disabled = false;
	     		
	     	}
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
     
     
     
    //-->
</script>
	
</head>
<body bgcolor="#FFFFFF" style="margin:0px" >
	<form name="form" method="post" action="/PortalOsx/servlet/Portal.cPortal">
		<input type="hidden" name="operacao" value="cadastraAgendamento">
		<input type="hidden" name="centrais" value="">
		<input type="hidden" name="regras" value="">
		<input type="hidden" name="posicao" value="<%=val.get("posicao")%>">
		<input type="hidden" name="status" value="<%=val.get("status")%>">
		<input type="hidden" name="tipoCdr" value="<%=tipoCdr%>">
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
											<%
												String dis = "";
												if(tipoCdr.equals(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO+""))
												{
													dis = "disabled";
												}
											%>
												<tr bgcolor="#000033">
													<td align="left" width="100%" colspan="2">
														<font color="#FFFFFF"><b>&nbsp;Periodo</b></font>
													</td>
												</tr>
																									
												<tr>														
													<td align="left"><b>Data Inicial:</b></td>
													<td align=left>
														<select name="diaInicial" size="1" class="lista">
															<%for(int i=1; i <= 31; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("diaInicial").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="mesInicial" size="1" class="lista">
															<%for(int i=1; i <= 12; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("mesInicial").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="anoInicial" size="1" class="lista">
															<%for(int i=2002; i <= 2030; i++){
																String valor = i+"";
																String chk = ""; if(val.get("anoInicial").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														
														<select name="horaInicial" size="1" class="lista" <%=dis%> >
															<%for(int i=0; i < 24; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("horaInicial").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														:
														<select name="minInicial" size="1" class="lista" <%=dis%> >
															<%for(int i=0; i < 60; i+=15){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("minInicial").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
													</td>
												</tr>
												
												<tr>			
													<td align="left"><b>Data Final:</b></td>
													<td align=left>
														<select name="diaFinal" size="1" class="lista">
															<%for(int i=1; i <= 31; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("diaFinal").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="mesFinal" size="1" class="lista">
															<%for(int i=1; i <= 12; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("mesFinal").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="anoFinal" size="1" class="lista">
															<%for(int i=2002; i <= 2030; i++){
																String valor = i+"";
																String chk = ""; if(val.get("anoFinal").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													
														<select name="horaFinal" size="1" class="lista" <%=dis%> >
															<%for(int i=0; i < 24; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("horaFinal").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														:
														<select name="minFinal" size="1" class="lista" <%=dis%> >
															<%for(int i=0; i < 60; i+=15){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("minFinal").equals(valor)) chk = "selected";%>
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
												
												
												<tr bgcolor="#000033">
													<td align="left" width="100%" colspan="2">
														<font color="#FFFFFF"><b>&nbsp;Execu&ccedil;&atilde;o:</b></font>
													</td>
												</tr>
												
												<tr>
													<td align="left"><b>Data:</b></td>
													<td align=left>
														<select name="diaExec" size="1" class="lista">
															<%for(int i=1; i <= 31; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("diaExec").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="mesExec" size="1" class="lista">
															<%for(int i=1; i <= 12; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("mesExec").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														/
														<select name="anoExec" size="1" class="lista">
															<%for(int i=2005; i <= 2030; i++){
																String valor = i+"";
																String chk = ""; if(val.get("anoExec").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														
														<select name="horaExec" size="1" class="lista">
															<%for(int i=0; i < 24; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("horaExec").equals(valor)) chk = "selected";%>
																<option <%=chk%> value="<%=valor%>"><%=valor%></option>
															<%}%>
														</select>
														:
														<select name="minExec" size="1" class="lista">
															<%for(int i=0; i < 60; i++){
																String valor = i+""; if(i<10) valor = "0"+i;
																String chk = ""; if(val.get("minExec").equals(valor)) chk = "selected";%>
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
												<%if(tipoCdr.equals(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X+"")){%>
												
													<tr><td>&nbsp;</td></tr>
													<tr bgcolor="#000033">
														<td align="left" width="100%" colspan="2">
															<font color="#FFFFFF"><b>&nbsp;Regras</b></font>
														</td>
													</tr>
													<tr>	
														<td align="left">
														<%
															Vector regras = (Vector)val.get("regras");
															String chkFDS = "";
															String chkTipoCham = "";
															String chkTipoAss = "";
															String chkOp = "";
															String chkArea = "";
															for(int i = 0;i < regras.size(); i++ )
															{
																RegraItemAgn ria = (RegraItemAgn)regras.get(i);
																if(ria.getNome().equals(RegraItemAgn.REGRA_FDS)) chkFDS = "checked";
																if(ria.getNome().equals(RegraItemAgn.REGRA_TIPOCHAM)) chkTipoCham = "checked";
																if(ria.getNome().equals(RegraItemAgn.REGRA_TIPOASS)) chkTipoAss = "checked";
																if(ria.getNome().equals(RegraItemAgn.REGRA_OP)) chkOp = "checked";
																if(ria.getNome().equals(RegraItemAgn.REGRA_AREA)) chkArea = "checked";
															}
														%>
															<input type="checkbox" <%=chkFDS%> name="REGRA[]" value="<%=RegraItemAgn.REGRA_FDS%>">Resultado de Chamada (FDS)<br>
															<input type="checkbox" <%=chkTipoCham%> name="REGRA[]" value="<%=RegraItemAgn.REGRA_TIPOCHAM%>">Tipo de Chamada<br>
															<input type="checkbox" <%=chkTipoAss%> name="REGRA[]" value="<%=RegraItemAgn.REGRA_TIPOASS%>">Tipo de Assinante<br>
															<input type="checkbox" <%=chkOp%> name="REGRA[]" value="<%=RegraItemAgn.REGRA_OP%>">Operadoras<br>
															<input type="checkbox" <%=chkArea%> name="REGRA[]" value="<%=RegraItemAgn.REGRA_AREA%>">&Aacute;rea
														</td>
													</tr>
												
												<%}%>
												
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
										<td align="left" >&nbsp;											
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr bgcolor="#000033">
													<font color="#FFFFFF"><b>Centrais</b></font>
												</tr>
												<%if(centrais!=null){%>
												<tr>
													<td width="33%">
													<select name="centrais1" id="op1" size="5" class="lista" multiple style="width:200px">
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
													<select name="centrais2" id="op2" size="5" class="lista" multiple style="width:200px">
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
											<input type="image" src="/PortalOsx/imagens/salvarb.gif" onClick="return Incluir(false)">
										</td>
										<%if(val.get("status").equals(ItemAgenda.STATUS_ITEM_INATIVO+"")){%>
										<td align="left" width="60">
											<input type="image" src="/PortalOsx/imagens/agendar.gif" onClick="return Incluir(true)">
										</td>
										<%}%>
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
	          				<%if(tipoCdr.equals(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO+"")){%>
	          				<p>Para cadastramento de uma agenda de reprocessamento, ser&aacute; necess&aacute;rio os seguintes passos:</p>
							<p>Selecionar as centrais desejadas;</p>							
							<p>No campo Per&iacute;odo, definir a data in&iacute;cio e data final do reprocessamento do CDR e, em seguida, definir no campo Execu&ccedil;&atilde;o a data do reprocessamento;, clicar primeiro no bot&atilde;o Agendar e em seguida no bot&atilde;o Salvar.</p>
							<p>ATEN&Ccedil;&Atilde;O: Este procedimento de Clicar no bot&atilde;o Salvar ap&oacute;s ter clicado no bot&atilde;o Agendar &eacute; para atualizar o arquivo de agenda do reprocessamento.</p>	          				
	          				<%}else{%>
	          				<p>Para cadastramento de uma agenda de reprocessamento, ser&aacute; necess&aacute;rio os seguintes passos:</p>
							<p>Selecionar as centrais desejadas;</p>
							<p>No campo Regras o administrador ter&aacute; a opç&atilde;o de reprocessar &agrave;s regras de classifica&ccedil;&atilde;o;</p>
							<p>No campo Per&iacute;odo, definir a data in&iacute;cio e data final do reprocessamento do CDR e, em seguida, definir no campo Execu&ccedil;&atilde;o a data do reprocessamento;</p>
							<p>ATEN&Ccedil;&Atilde;O: Este procedimento de Clicar no bot&atilde;o Salvar ap&oacute;s ter clicado no bot&atilde;o Agendar &eacute; para atualizar o arquivo de agenda do reprocessamento.</p>	
           				    <%}%>
	          				</td>	          				
	        			</tr>
	      			</table>
	    		</td>
	  		</tr>
		</table>
	</form>
</body>
</html>    