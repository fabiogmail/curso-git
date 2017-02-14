<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpRelVolumetriaAjax.js'></script>
<title>Tela Chamadas Volumetria</title>

<style type="text/css">

#volumetria {       
       background-color: #FFFFFF;
       
}
#relatorio a:hover {text-decoration: none}; 
</style>

<script language=javascript>
	//<!--
	var volumetria = new Array();

	
	function init(){
	OpRelVolumetriaAjax.getNomeBilhetador(retListar);

	}
	
	
	function retListar(valor){

			   volumetria = valor.split(";");
			   
               var html = '<table>';
               var cor = '';
               
               for(var i = 0; i < volumetria.length; i++){
                       
                       if(i % 2 == 0)
                               cor = ' bgcolor="#E8E8E8"';
                       else
                               cor = '';
                       
                       html += '<tr>';
                       html += '<td width="40%"'+cor+'> <b>'+volumetria[i]+'<b></td>';
                       html += '<td width="80%"'+cor+'>'+'<input style="width:70px;height:18px;font-size:9px" type="text" name="limMin" id="limMin'+volumetria[i]+'" value = ""></td>';
                       html += '<td width="80%"'+cor+'>'+'<input style="width:70px;height:18px;font-size:9px" type="text" name="limMax" id="limMax'+volumetria[i]+'" value = ""></td>'; 
                                     
                       html += '</tr>';
               }
               
               html += '</table>';
               document.getElementById("volumetria").innerHTML = html;
               OpRelVolumetriaAjax.getRecursoVolumetria(populaRecurso);
               //alert(html);
       }
       
    
 function populaRecurso(valor){
 	
 	var recurso= new Array();
 	var pojo = null;
 	recurso = valor;

 	for(var i = 0; i < recurso.length; i++){
 		pojo = recurso[i];
 		document.getElementById("limMin"+pojo.bilhetador.nome).value = pojo.limMinimo;
 		document.getElementById("limMax"+pojo.bilhetador.nome).value = pojo.limMaximo;
 	}
 
 }
 function salvar(){ 
 
 
 	var listaSalvar = new Array(volumetria.length);
	var limMax="";
	var limMin="";
	
	var nome;
	
	for(var i = 0; i < volumetria.length; i++){
	
		limMax = document.getElementById('limMax'+volumetria[i]).value;
		limMin = document.getElementById('limMin'+volumetria[i]).value;
		listaSalvar[i] = volumetria[i]+';'+limMax+';'+limMin;	
		
	}
	OpRelVolumetriaAjax.salvar(listaSalvar,retSalvar);
	
}

function retSalvar(valor){
	if(valor == true)
		alert('Limite salvo com sucesso!');
	else if(valor != true)
		alert('Problema ao salvar o Limite');
	
}

	//--> 
</script>

</head>

<body bgcolor="#FFFFFF" style="margin:0px" onLoad="init();">
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
				<td><img src="/PortalOsx/imagens/parametrizacao_de_volumetria.gif"></td>
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
							<table width="305px" border="0" cellspacing="0" cellpadding="0">
								<tr bgcolor="#000033">
									<td align="left" width="10" height="20"><font
										color="#FFFFFF"><b>Bilhetador</b></font></td>
									<td align="right" width="130" height="20"><font
										color="#FFFFFF"><b>Limite Mínimo</b></font></td>
									<td align="right" width="" height="20"><font 
										color="#FFFFFF"><b>Limite Maximo</b></font></td>
									
						</tr>					
															
							
						
								<!--////////////////////////////////////////////////Lista Items///////////////////////////////////////////////////////////////// -->				
						
									
								<!--//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
					</table>
					<table width="300px" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<div id="volumetria"></div>	
							</td>
						</tr>
					</table>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">&nbsp;</td>
						</tr>
					</table>
					<table width="545" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<div id="botaoSalvar">
		    				 	<a href="javascript:salvar();"><img src="/PortalOsx/imagens/salvar.gif" alt="Salvar" border="0"/></a>		
		    				</div></td>
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
			<!--  
				<td><p>Esta op&ccedil;&atilde;o permite a cria&ccedil;&atilde;o ou a visualiza&ccedil;&atilde;o das agendas de backup dos arquivos de CDRView Bruto.</p>
					<p>Selecione a checkbox correspondente a uma das 20 posi&ccedil;&otilde;es dispon&iacute;veis e, em seguida, clique em Criar/Alterar.</p>
					<p>ATEN&Ccedil;&Atilde;O: Caso a posi&ccedil;&atilde;o de agenda esteja sendo utilizada, quando clicar no Bot&atilde;o Criar/Alterar, ser&aacute; poss&iacute;vel alterar o agendamento.</p>
					<p>Caso a inten&ccedil;&atilde;o do administrador seja excluir o agendamento, dever&aacute; selecionar a combobox da posi&ccedil;&atilde;o desejada e, em seguida, clicar no Bot&atilde;o Remover.</p></td>
			-->
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
