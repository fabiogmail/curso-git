<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Portal.ReproCdr.*"%>
<%@page import="Portal.Utils.DataUtil"%>

<%
	String tipoCdr = (String)request.getAttribute("tipoCdr");
	ItemAgenda itemAgenda = null;
	String objetoSerializado = "";
	
	String posicao = "";
	String dataInicio = "";
	String dataFinal = "";
	String dataExecucao = "";
	Vector centrais = new Vector();
	Vector regras = new Vector();
	String status = "";
	
	if(tipoCdr.equals(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO+""))
	{
		itemAgenda = (ItemAgendaReprocCdrBruto)request.getAttribute("itemAgenda");
		
		posicao = ((ItemAgendaReprocCdrBruto)itemAgenda).getPosicao()+"";
		
		Date dInicio = ((ItemAgendaReprocCdrBruto)itemAgenda).getPeriodoInicio();
		Date dFinal = ((ItemAgendaReprocCdrBruto)itemAgenda).getPeriodoFim();
		Date dExecucao = ((ItemAgendaReprocCdrBruto)itemAgenda).getDataHoraExecucao();
		
		dataInicio = DataUtil.formataData(dInicio,"dd/MM/yyyy HH:mm:ss");
		dataFinal = DataUtil.formataData(dFinal,"dd/MM/yyyy HH:mm:ss");
		dataExecucao = DataUtil.formataData(dExecucao,"dd/MM/yyyy HH:mm:ss");
		
		centrais = ((ItemAgendaReprocCdrBruto)itemAgenda).getCentrais();
		status = ((ItemAgendaReprocCdrBruto)itemAgenda).getStatusItemAgenda()+"";
		
	}
	else
	{
		itemAgenda = (ItemAgendaReprocCdrX)request.getAttribute("itemAgenda");
		posicao = ((ItemAgendaReprocCdrX)itemAgenda).getPosicao()+"";
		
		Date dInicio = ((ItemAgendaReprocCdrX)itemAgenda).getPeriodoInicio();
		Date dFinal = ((ItemAgendaReprocCdrX)itemAgenda).getPeriodoFim();
		Date dExecucao = ((ItemAgendaReprocCdrX)itemAgenda).getDataHoraExecucao();
		
		dataInicio = DataUtil.formataData(dInicio,"dd/MM/yyyy HH:mm:ss");
		dataFinal = DataUtil.formataData(dFinal,"dd/MM/yyyy HH:mm:ss");
		dataExecucao = DataUtil.formataData(dExecucao,"dd/MM/yyyy HH:mm:ss");
		
		centrais = ((ItemAgendaReprocCdrX)itemAgenda).getCentrais();
		regras = ((ItemAgendaReprocCdrX)itemAgenda).getRegras();
		status = ((ItemAgendaReprocCdrX)itemAgenda).getStatusItemAgenda()+"";
	}
	
	
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0"> 
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<title>Detalhar CDR</title>

<script language=javascript>
	<!--
		function Processar(tipo)
		{
			document.form.acao.value = tipo;
			var msg = ''
			if(tipo == 'mover')
			{
				msg = 'Voce quer realmente mover esta base reprocessada para a base ativa?'
			}
			else
			{
				msg = 'Voce quer realmente apagar esta base reprocessada?'
			}
			
			if(confirm(msg))
			{
				return true;
			}
			else
			{
				return false;
			}
		
		}
		
		function Voltar()
		{
			document.form.operacao.value = 'listaAgendamentoCDR';
			return true;
		}
    
     
    //-->
</script>
	
</head>
<body bgcolor="#FFFFFF" style="margin:0px" >
	<form name="form" method="post" action="/PortalOsx/servlet/Portal.cPortal">
		<input type="hidden" name="operacao" value="baseReprocessada">
		<input type="hidden" name="tipoCdr" value="<%=tipoCdr%>">
		<input type="hidden" name="acao" value="">
		<input type="hidden" name="posicao" value="<%=posicao%>">

		<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
	  		<tr>
	    		<td width="13">&nbsp;</td>
	    		<td width="579" valign="top">
	      			<table width="546" border="0" cellspacing="0" cellpadding="2">
	        			<tr>
	          				<td><img src="/PortalOsx/imagens/infdetalhes.gif"></td>
	        			</tr>
	        			<tr>
	          				<td>&nbsp;</td>
	        			</tr>
						<tr>
							<%if(status.equals(ItemAgenda.STATUS_ITEM_PRONTO+"") || status.equals(ItemAgenda.STATUS_ITEM_INCOMPLETO+"")){%>
								<%if(status.equals(ItemAgenda.STATUS_ITEM_PRONTO+"")){%>
								<td align="left" width="60">
									<input type="image" src="/PortalOsx/imagens/moverbase.gif" onClick="return Processar('mover')">
								</td>
								<%}%>
								<td align="left" width="60">
									<input type="image" src="/PortalOsx/imagens/apagarbase.gif" onClick="return Processar('apagar')">
								</td>
							<%}%>
	        			</tr>
	        			
	        			<tr bgcolor="#000033">
							<td align="left" width="272"><font color="#FFFFFF"><b>Informa&ccedil;&atilde;o<font></b></a></td>
							<td align="center" width="271"><font color="#FFFFFF"><b>Valor<font></b></td>
						</tr>
						<tr bgcolor="#F0F0F0">
							<td align="left">Item</td>
							<td align="center"><%=(Integer.parseInt(posicao))%></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="left">Data Inicial</td>
							<td align="center"><%=dataInicio%></td>
						</tr>
						<tr bgcolor="#F0F0F0">
							<td align="left">Data Final</td>
							<td align="center"><%=dataFinal%></td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="left">Data Execu&ccedil;&atilde;o</td>
							<td align="center"><%=dataExecucao%></td>
						</tr>
						<tr bgcolor="#F0F0F0">
							<td align="left">Centrais</td>
							<td align="center">
							<select size="1" name="centrais" class="lista">
								<%for(int i=0;i<centrais.size();i++){%>
									<option value=""><%=((CentralItemAgn)centrais.get(i)).getNome()%>
								<%}%>
							</select>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="left">Status</td>
							<td align="center">
								<%
									if(status.equals(ItemAgenda.STATUS_ITEM_AGENDADO+""))
										out.println("Agendado");
									if(status.equals(ItemAgenda.STATUS_ITEM_EM_EXECUCAO+""))
										out.println("Em Execu&ccedil;&atilde;o");
									if(status.equals(ItemAgenda.STATUS_ITEM_PRONTO+""))
										out.println("Pronto");
									if(status.equals(ItemAgenda.STATUS_ITEM_INCOMPLETO+""))
										out.println("Incompleto");
								%>
							</td>
						</tr>
						
<!--//////////////////////////////////////Regras CDR-X////////////////////////////////////////// -->						
						<%if(tipoCdr.equals(""+ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X)){%>
							<tr bgcolor="#000033">
								<td align="left" width="272"><font color="#FFFFFF"><b>Regras<font></b></a></td>
								<td align="center" width="271"></td>
							</tr>
							
							
							<%for(int i = 0; i < regras.size() ; i++){
								RegraItemAgn ria = (RegraItemAgn)regras.get(i);
							%>
							<tr bgcolor="#FFFFFF">
								<td align="left">
								<%
								if(ria.getNome().equals(RegraItemAgn.REGRA_FDS)) out.println("Resultado de Chamada (FDS)");
								if(ria.getNome().equals(RegraItemAgn.REGRA_TIPOCHAM)) out.println("Tipo de Chamada");
								if(ria.getNome().equals(RegraItemAgn.REGRA_TIPOASS)) out.println("Tipo de Assinante");
								if(ria.getNome().equals(RegraItemAgn.REGRA_OP)) out.println("Operadoras");
								if(ria.getNome().equals(RegraItemAgn.REGRA_AREA)) out.println("&Aacute;rea");
								%>
								</td>
								<td align="left">&nbsp;</td>
							</tr>
							<%}%>
						<%}%>
<!--/////////////////////////////////////////////////////////////////////////////////////////// -->																		
						
						<tr>
							<td align="left">&nbsp;</td>
						</tr>
	      			</table>
<!--//////////////////////////////////////////////////LOG CENTRAIS//////////////////////////////////////////////// --> 	      			
	      			<table width="546" border="0" cellspacing="0" cellpadding="2">
	      			
	      				<tr bgcolor="#000033">
							<td align="left" width="20%"><font color="#FFFFFF"><b>Central<font></b></a></td>
							<td align="left" width="20%"><font color="#FFFFFF"><b>Status<font></b></td>
							<td align="left" width="60%"><font color="#FFFFFF"><b>Log<font></b></td>
						</tr>
						<%
						CentralItemAgn  central = null;
						for(int i=0 ; i<centrais.size() ;i++ ) {
							central = (CentralItemAgn)centrais.get(i);
							boolean logGerada = false;
							String log = "";%>
							<%if(i%2==0){%>
								<tr bgcolor="#F0F0F0">
							<%}else{%>
								<tr bgcolor="#FFFFFF">
							<%}%>
							
								<td align="left"><%=central.getNome()%></td>
								<td align="left">
								<%
									if(central.getStatus() == CentralItemAgn.STATUS_CENTRAL_AGENDADO)
										out.println("Agendado");
									if(central.getStatus() == CentralItemAgn.STATUS_CENTRAL_EM_EXECUCAO)
										out.println("Em Execu&ccedil;&atilde;o");
									if(central.getStatus() == CentralItemAgn.STATUS_CENTRAL_PRONTO)
										out.println("Pronto");
								%>
								</td>
								<%if(central.getStatus() == CentralItemAgn.STATUS_CENTRAL_PRONTO){
									if(central.getQtdArqEncontrados() == 0){
										log = "Arquivos n&atilde;o encontrados para a central/plataforma";
										logGerada = true;
									}
									if(central.getQtdArqEncontrados() > 0 && central.getQtdCdrProcessados() == 0){
										log = "Arquivo fora do per&iacute;odo configurado para a central/plataforma";
										logGerada = true;
									}
									if(!logGerada){
										log = "Arquivos Encontrados = "+central.getQtdArqEncontrados()+" CDRs Encontrados = "+central.getQtdCdrEncontrados()+" CDRs Processados = "+central.getQtdCdrProcessados();
									}
								}%>
									
								<td align="left"><%=log%></td>
								
							</tr>	
							
<!-- //////////////////////////////////Somente para CDR-X///////////////////////////////////////////// -->								
							<%if(tipoCdr.equals(""+ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X)){%>
								<%if(i%2==0){%>
									<tr bgcolor="#F0F0F0">
								<%}else{%>
									<tr bgcolor="#FFFFFF">
								<%}%>
								
								<td align="left"></td>
								<td align="left"></td>
								<td align="left">
								<%for(int j = 0;j < regras.size(); j++){
									RegraItemAgn ria = (RegraItemAgn)regras.get(j);
									int statusRegra = 0;
									String nomeRegra = "";
									if(ria.getNome().equals(RegraItemAgn.REGRA_FDS)){ 
										statusRegra = central.getStatusRegraFds();
										nomeRegra = "Resultado de Chamada (FDS)";
									}if(ria.getNome().equals(RegraItemAgn.REGRA_TIPOCHAM)){ 
										statusRegra = central.getStatusRegraTipoCham();
										nomeRegra = "Tipo de Chamada";
									}if(ria.getNome().equals(RegraItemAgn.REGRA_TIPOASS)){ 
										statusRegra = central.getStatusRegraTipoAss();
										nomeRegra = "Tipo de Assinante";
									}if(ria.getNome().equals(RegraItemAgn.REGRA_OP)){
										statusRegra = central.getStatusRegraOperadora();
										nomeRegra = "Operadoras";
									}if(ria.getNome().equals(RegraItemAgn.REGRA_AREA)){ 
										statusRegra = central.getStatusRegraArea();
										nomeRegra = "&Aacute;rea";
									}
									
									String statusRegraStr = "";
									if(statusRegra == RegraItemAgn.INVALIDO) statusRegraStr = "<font color=#CC0000>Inv&aacute;lido</font>";
									if(statusRegra == RegraItemAgn.ARQ_CONFIG_INEXISTENTE) statusRegraStr = "<font color=#CC0000>Arquivo de Configura&ccedil;&atilde;o Inexistente</font>";
									if(statusRegra == RegraItemAgn.ARQ_CONFIG_INVALIDO) statusRegraStr = "<font color=#CC0000>Arquivo de Configura&ccedil;&atilde;o Inv&aacute;lido</font>";
									if(statusRegra == RegraItemAgn.NAO_EXISTE_TEC) statusRegraStr = "<font color=#CC0000>N&atilde;o Existe Tecnologia</font>";
									if(statusRegra == RegraItemAgn.CAMPO_INVALIDO) statusRegraStr = "<font color=#CC0000>Campo Inv&aacute;lido</font>";
									if(statusRegra == RegraItemAgn.OK) statusRegraStr = "<font color=#009900>OK</font>";
								%>
									<%=nomeRegra%> = <%=statusRegraStr%><br>
									
								<%}%>
								</td>
								</tr>
							<%}%>
<!-- ///////////////////////////////////////////////////////////////////////////////////////// -->								
							
												
						<%}%>
	      				<tr>
							<td align="left">&nbsp;</td>
						</tr>
	      			</table>
<!--///////////////////////////////////////////////////////////////////////////////////////////////////////////// --> 	      			     			

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
	          				<td align="left" valign="top"><p>No CDRView, os arquivos de CDR s&atilde;o associados &agrave;s Centrais ou Bilhetadores que os geram</p></td>
	        			</tr>
	      			</table>
	    		</td>
	  		</tr>
		</table>
	</form>
</body>
</html>    