<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Relatorio de Sequencia</title>

<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpRelVolumetriaAjax.js'></script>

<script type="text/javascript">
<!--
DWREngine.setErrorHandler(erro);


function erro(msg, ex) {
	mostrar('botaoSalvar');
	$('status').innerHTML = msg;
}

function init(){


	OpRelVolumetriaAjax.getBilhetadores(retInit);

}

function retInit(valor){
	var lista = new String(valor);
	
	var listaCampo = new Array();
	
	listaCampo = lista.split(";");
	
	//alert(listaCampo.length);
	
	var option;
	var texto;
	
	for(var i = 0; i < listaCampo.length; i++){

			option = document.createElement('option');
	
			texto = document.createTextNode(listaCampo[i]);

			option.appendChild(texto);

			option.value = listaCampo[i];

			document.getElementById("listaDisponiveis").appendChild(option);
			
	}
	var el = document.getElementById("listaDisponiveis").style; 	
	el.display = "none";
	el.display = "";
}

function AdicionaPermissao()
{
   var Valor="";
   var Texto="";
	
	//for(var j=0; j<document.getElementById("listaDisponiveis").length; j++ ){
	//	teste+= document.getElementById("listaDisponiveis")[j].value;
	//}

   for (i = 0; i < document.getElementById("listaDisponiveis").length; i++)
   {     

      if (document.getElementById("listaDisponiveis")[i].selected == true)
      {      
         Valor = document.getElementById("listaDisponiveis")[i].text;
         Texto = document.getElementById("listaDisponiveis")[i].text;
         document.getElementById("listaSelecionados").options[document.getElementById("listaSelecionados").length] = new Option(Texto, Valor, false, false);
         document.getElementById("listaDisponiveis")[i] = null;
         i--;
      }         
   }

   sortSelect(document.getElementById("listaSelecionados"), compareText);
}


function RemovePermissao()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.getElementById("listaSelecionados").selectedIndex == -1)
   {
      alert("Selecione um perfil para alterar sua permiss?o!");
      return;
   }

   for (i = 0; i < document.getElementById("listaSelecionados").length; i++)
   {  
   //alert("1");    
      if (document.getElementById("listaSelecionados")[i].selected == true)
      {
      //alert("2");
         Valor = document.getElementById("listaSelecionados")[i].text;
         //alert("Valor: "+Valor);
         Texto = document.getElementById("listaSelecionados")[i].text;
         //alert("Texto: "+Texto);
         document.getElementById("listaDisponiveis").options[document.getElementById("listaDisponiveis").length] = new Option(Texto, Valor, false, false);
         document.getElementById("listaSelecionados")[i] = null;
         i--;
      }         
   }

   sortSelect(document.getElementById("listaDisponiveis"), compareText);
}

function sortSelect (select, compareFunction)
{
  if (!compareFunction)
    compareFunction = compareText;
  var options = new Array (select.options.length);
  for (var i = 0; i < options.length; i++)
    options[i] = new Option (select.options[i].text, select.options[i].value, select.options[i].defaultSelected, select.options[i].selected);
  options.sort(compareFunction);
  select.options.length = 0;
  for (var i = 0; i < options.length; i++)
    select.options[i] = options[i];
}

function compareText (option1, option2)
{
  return option1.text < option2.text ? -1 :
    option1.text > option2.text ? 1 : 0;
}

function geraRelatorio(){
	
	var bilhetadores = new String();
	
	 //pegar lista de bilhetadores selecionados
	
	 for (i = 0; i < document.getElementById("listaSelecionados").length; i++){
	 	bilhetadores += document.getElementById("listaSelecionados")[i].text + ";";
	 }
	 
	 OpRelVolumetriaAjax.setListaBilhetador(bilhetadores);//setados os bilhetadores na sessão
	
	window.open('/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelatorioVolumetria&subOperacao=relVolumetria','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=875,height=560');
}

function geraRelatorioAlarme(){
	var bilhetadores = new String();
	
	 //pegar lista de bilhetadores selecionados
	
	 for (i = 0; i < document.getElementById("listaSelecionados").length; i++){
	 	bilhetadores += document.getElementById("listaSelecionados")[i].text + ";";
	 }
	
	OpRelVolumetriaAjax.setListaBilhetador(bilhetadores);//setados os bilhetadores na sessão
	
	window.open('/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelatorioVolumetria&subOperacao=relVolumetriaAlarme','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=809,height=560');
}


-->

</script>
</head>
<body bgcolor="#FFFFFF" style="margin: 0px" onLoad="init();">
<table width="780" height="383" border="0" cellspacing="0"
	cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
	<!--DWLayoutTable-->
	<tr>
		<td width="15" rowspan="2" valign="top">
		<table width="50%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="15" height="389"></td>
			</tr>
		</table>
		</td>
		<td width="613" height="32" valign="top"><img
			src="/PortalOsx/imagens/relatorio_de_volumetria.gif" /></td>
		<td width="142" valign="top"><img
			src="/PortalOsx/imagens/dicasDeUtilizacao.gif" /></td>
		<td width="10" rowspan="2" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="10" height="389"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="357" valign="top">

		<div id="pagina">
		<table width="80%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" colspan="3">
				<div id="selecionaRel">
				
					<tr>
						<td align="center">
						<b><font color="000000">Bilhetadores Disponíveis:</font></b>
						</td>
						<td align="center"  height="1">&nbsp;</td>
						<td align="center" colspan="3" height="1">
						<b><font color="000000">Bilhetadores Selecionados:</font></b>
						</td>

					</tr>
		
					<tr>
						<td align="center">
						<p><select name="ListaBilhetadoresDisponiveis" id="listaDisponiveis" size="5" multiple="multiple">

						</select>
						</td>
						<td align="center" >&nbsp;
						<table width="50" border="0" cellspacing="" cellpadding="">
							<tr>
								<td align="center">&nbsp;&nbsp; <input type="button" name="button1" value="   >>   " class="button" onClick="return AdicionaPermissao()"></td>
							</tr>
							<tr>
								<td align="center">&nbsp;</td>
							</tr>

							<tr>
								<td align="center">&nbsp;</td>
							</tr>
							<tr>
								<td align="center">&nbsp;&nbsp; <input type="button" name="button1" value="   <<   " class="button" onClick="return RemovePermissao()"></td>
							</tr>
						</table>
						&nbsp;</td>
						<td align="center" colspan="3" height="1">
						<p>&nbsp;<select name="ListaBilhetadoresSelecionados" id="listaSelecionados" size="5"
							multiple>
						</select>
						</td>
					</tr>
				
				</div>
				</td>
			</tr>

			<tr>
				<td height="90" valign="center">
				<div id="botaoSalvar"><a href="javascript:geraRelatorio();"><img
					src="/PortalOsx/imagens/relatorio.gif" alt="Relatório" border="0" /></a></div>
				</td>
				<td colspan="2" valign="center"><a href="javascript:geraRelatorioAlarme();"><img
					src="/PortalOsx/imagens/alarmes.gif" alt="Alarmes" border="0" /></a></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td valign="top">
				<div id="status"></div>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</div>

		</td>
		<td valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="142" height="357" valign="top">Dicas...</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</body>
</html>