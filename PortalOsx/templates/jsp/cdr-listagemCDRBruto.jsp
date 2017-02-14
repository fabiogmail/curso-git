<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Portal.ReproCdr.CentralItemAgn"%>


<%HashMap[] listaCdr = (HashMap[]) request.getAttribute("listaCdr");
			//String tipoCdr = (String)request.getAttribute("tipoCdr");

			%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<title>Tela CDR</title>

<script language=javascript>
	<!--
		cont = 0;
		function marcaTodos() 
		{ 
		    
			
   			for (var i=0; i<document.form.elements.length; i++) 
   			{
     			var x = document.form.elements[i];
     			if (x.name == 'UIDL[]') 
     			{ 
					x.checked = document.form.selecionaTodos.checked;
				} 
			}
			if (cont == 0)
			{    
				var elem = document.getElementById("checar");
				//elem.innerHTML = "Desmarcar todos";escreve esta string
				cont = 1;
			}
			else 
			{
				var elem = document.getElementById("checar");
				//elem.innerHTML = "Marcar todos";escreve marcar todos na tabela
				cont = 0;
			}
		}
		
		function Editar()
		{
		    var qtd = 0;
			for (var i=0; i<document.form.elements.length; i++) 
   			{
   			    
     			var x = document.form.elements[i];
     			var tamanho = x.value.length;
     			if (x.name == 'UIDL[]') 
     			{ 
     			    if(x.checked)
     			    {
   			    		document.form.posicao.value = x.value;
     			    	qtd++;
     			    }
				} 
			}
			if(qtd > 1)
			{
				alert('Marque somente um Item para Editar');
				return false;
			}
			if(qtd == 0)
			{
				alert('Marque um Item para Editar');
				return false;
			}
			
			document.form.operacao.value = 'cadastraCDRBruto';
			document.form.submit();
		}
		
		function Remover()
		{
			for (var i=0; i<document.form.elements.length; i++) 
   			{
     			var x = document.form.elements[i];
     			if (x.name == 'UIDL[]') 
     			{ 
     			    if(x.checked)
     			    {
     			        var tamanho = x.value.length;
     			    	if(x.value.charAt(tamanho-1) == '+')
     			    	{
     			    		document.form.posicao.value = document.form.posicao.value + x.value.substring(0,tamanho-1) + '\t';
     			    	}
     			    	else
     			    	{
     			    		alert('Selecione somente os agendamentos existentes para Remover')
     			    		document.form.posicao.value = ''
     			    		return false;
     			    	}
     			       	
     			    }
				} 
			}
			if(document.form.posicao.value != '')
			{
				if(confirm('Deseja realmente excluir os agendamentos selecionados'))
				{
					document.form.operacao.value = 'removeCDRBruto';
			    	document.form.submit();
				}
				else
				{
					document.form.posicao.value = ''
					return false;
				}
			}
			else
			{
				alert('Selecione um item para excluir');
				return false;
			}
		}
		
	//--> 
</script>

</head>

<body bgcolor="#FFFFFF" style="margin:0px">
<form name="form" method="post"
	action="/PortalOsx/servlet/Portal.cPortal">
	<input type="hidden" name="posicao" value="">
	<input type="hidden" name="operacao" value="">
<table width="780" height="383" border="0" cellspacing="0"
	cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
	<tr>
		<td width="13">&nbsp;</td>
		<td width="579" valign="top">
		<table width="546" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/PortalOsx/imagens/agncdrbruto.gif"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><!-- Inicio Lista -->
					<table width="545" border="0" cellspacing="0" cellpadding="0">
	
						<!--  input type="hidden" name="tipoCdr" value="<%--tipoCdr--%>">-->
	
						<tr>
							<td align="left" colspan="2">
							<table width="450" border="0" cellspacing="0" cellpadding="3">
								<tr bgcolor="#000033">
									<td align="left" width="10" height="20"><font
										color="#FFFFFF"><b>Agenda</b></font></td>
									<td align="center" width="220" height="20"><font
										color="#FFFFFF"><b>Central</b></font></td>
									<td align="center" width="220" height="20"><font color="#FFFFFF"><b>Data
									Execu&ccedil;&atilde;o</b></font></td>
									<td align="right" width="10" height="20"><font color="#FFFFFF"><b><input
										type="checkbox" name="selecionaTodos" onClick="marcaTodos()"><span
										id="checar"></span></font></td>
								</tr>
								<!--////////////////////////////////////////////////Lista Items///////////////////////////////////////////////////////////////// -->
								<%for (int i = 1; i < listaCdr.length; i++) {
	
									String bgcolor = "";
									HashMap valores = (HashMap) listaCdr[i];%>
									<%if(valores != null) {%>
										<%if (i % 2 == 0)
											bgcolor = "#F0F0F0";
										else
											bgcolor = "#FFFFFF";%>
	
										<tr bgcolor="<%=bgcolor%>">
										<td align="left"><%=i%></td>
		
										<td align="center"><select style="width:100px" size="1" name="centrais"
											class="lista">
											<%Vector centrais = null;
												
											 		centrais = (Vector)valores.get("centrais");
																														
												for (int j = 0; j < centrais.size(); j++) {%>
													<option value=""><%=((CentralItemAgn) centrais.get(j)).getNome()%>
												<%}%>
											

										</select></td>
		
										<td align="center"><%=valores.get("dataExec")%></td>
		
										<td align="center">
											<input type="checkbox" name="UIDL[]" value="<%=i%>+">
										</td>
		
										<%} else {%>
											<%if (i % 2 == 0)
												bgcolor = "#F0F0F0";
											else
												bgcolor = "#FFFFFF";%>
											<tr bgcolor="<%=bgcolor%>">
											<td align="left"><%=i%></td>
											<td align="center">&nbsp;</td>
											<td align="center">&nbsp;</td>
											<td align="center"><input type="checkbox" name="UIDL[]"
												value="<%=i%>"></td>
										<%}%>
									<%}%>
									</tr>
								<!--//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
							</table>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">&nbsp;</td>
						</tr>
					</table>
					<table width="545" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="left" width="60"><input type="image"
								src="/PortalOsx/imagens/alterar.gif" onClick="return Editar()"></td>
							<td align="left" width="60"><input type="image"
								src="/PortalOsx/imagens/remover.gif" onClick="return Remover()">
							</td>
						</tr>
					</table>
				</td><!-- Fim Lista -->				
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
				<td><p>Esta op&ccedil;&atilde;o permite a cria&ccedil;&atilde;o ou a visualiza&ccedil;&atilde;o das agendas de backup dos arquivos de CDRView Bruto.</p>
					<p>Selecione a checkbox correspondente a uma das 20 posi&ccedil;&otilde;es dispon&iacute;veis e, em seguida, clique em Criar/Alterar.</p>
					<p>ATEN&Ccedil;&Atilde;O: Caso a posi&ccedil;&atilde;o de agenda esteja sendo utilizada, quando clicar no Bot&atilde;o Criar/Alterar, ser&aacute; poss&iacute;vel alterar o agendamento.</p>
					<p>Caso a inten&ccedil;&atilde;o do administrador seja excluir o agendamento, dever&aacute; selecionar a combobox da posi&ccedil;&atilde;o desejada e, em seguida, clicar no Bot&atilde;o Remover.</p></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
