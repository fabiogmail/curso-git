<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Relatorio de Sequencia</title>

<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpRelSequenciaAjax.js'></script>

<script type="text/javascript">
<!--
DWREngine.setErrorHandler(erro);


function erro(msg, ex) {
	mostrar('botaoSalvar');
	$('status').innerHTML = msg;
}

function init(){
//	document.getElementById("dataInicio").value ="dd/mm/aaaa";
//	document.getElementById("dataFim").value ="dd/mm/aaaa";		
	OpRelSequenciaAjax.getBilhetadores(retInit);
	
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
	if (document.getElementById("listaDisponiveis").selectedIndex == -1)
   {
      alert("Selecione uma opção!");
      return;
   }
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
      alert("Selecione uma opção!");
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

function validaDataFim(dataRecebida){
	var Data = dataRecebida;
	var DataDet = new Array();
      DataDet[0] = Data.substring(0, 2);
      DataDet[1] = Data.substring(3, 5);
      DataDet[2] = Data.substring(6, 10);

      var Erro = false;
    
      if(Data.length > 10){
		Erro = true;
	  }
      
      for (i = 0; i < 3; i++)
      {
         Expressao = new RegExp("[^0-9]", "gi");
         Ret = DataDet[i].search(Expressao);

         if (Ret != -1)
            Erro = true;

         switch (i)
         {
            case 0:  // Dia
               if (Math.ceil(DataDet[0]) == 0 || Math.ceil(DataDet[0]) > 31)
                  Erro = true;
               break;
            case 1:  // Mes
               if (Math.ceil(DataDet[1]) == 0 || Math.ceil(DataDet[1]) > 12)
                  Erro = true;
               break;
            case 2:  // Ano
               if (Math.ceil(DataDet[2]) < 2000 || Math.ceil(DataDet[2]) > 2050)
                  Erro = true;
               break;
         }
			
         if (Erro == true)
         {
            alert("Data incorreta. Corrija-a!");
            return;
         }
         
         
      }
}

function comparaDatas(dataInicio, dataFim){

	var dt1 = dataInicio;
	var dt2 = dataFim;
	var DataDet1 = new Array();
	var DataDet2 = new Array();
	
      DataDet1[0] = dt1.substring(0, 2);
      DataDet1[1] = dt1.substring(3, 5);
      DataDet1[2] = dt1.substring(6, 10);
	  
	  DataDet2[0] = dt2.substring(0, 2);
      DataDet2[1] = dt2.substring(3, 5);
      DataDet2[2] = dt2.substring(6, 10);	
      
      if (Math.ceil(DataDet1[2]) > Math.ceil(DataDet2[2])){
		    alert("Data incorreta. Corrija-a!");
            return;
	  }
	  else if (Math.ceil(DataDet1[1]) > Math.ceil(DataDet2[1])){
		  alert("Data incorreta. Corrija-a!");
          return;
	  }
	  else if (Math.ceil(DataDet1[0]) > Math.ceil(DataDet2[0])){
	      alert("Data incorreta. Corrija-a!");
          return;
      }
      else if (Math.ceil(DataDet1[0]) == Math.ceil(DataDet2[0])){
      		alert("Datas Iguais. Corrija-a!");
            return;	
      }
}	

function geraRelatorio(){
	var bilhetadores = new String();
	var data = new String();

	 //pegar lista de bilhetadores selecionados
	 for (i = 0; i < document.getElementById("listaSelecionados").length; i++){
	 	bilhetadores += document.getElementById("listaSelecionados")[i].text + ";"; 	
	 }
	 var diaIndi = document.getElementById("dia_inicio").selectedIndex;
	 var mesIndi = document.getElementById("mes_inicio").selectedIndex;
	 var anoIndi = document.getElementById("ano_inicio").selectedIndex;
	 
	 var dia = document.getElementById("dia_inicio")[diaIndi].text;
	 //var mes = document.getElementById("mes_inicio")[mesIndi].text;
	 var ano = document.getElementById("ano_inicio")[anoIndi].text;
	 var mes = mesIndi;

	 
	 if(dia == ''){
	 	alert("Selecione um dia!");
	 	return;	
	 }
	 if(mes == '0'){
	 	alert("Selecione um mes!");
	 	return;		
	 }
	 
	 if(ano == ''){
	 	alert("Selecione um ano!");
	 	return;		
	 }
	 		
	 data = ano + "-" + mes + "-" + dia;
	 
	 
	 
	 OpRelSequenciaAjax.setListaBilhetador(bilhetadores);//setados os bilhetadores na sessão
	 OpRelSequenciaAjax.setListaData(data);//setados a data na sessão
	 
	 window.open('/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelatorioSequencia&subOperacao=relSequencia','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=875,height=560');
	
}

function geraRelatorioAlarme(){
	var bilhetadores = new String();
	var data = new String();

	 //pegar lista de bilhetadores selecionados
	 for (i = 0; i < document.getElementById("listaSelecionados").length; i++){
	 	bilhetadores += document.getElementById("listaSelecionados")[i].text + ";"; 	
	 }
	 var diaIndi = document.getElementById("dia_inicio").selectedIndex;
	 var mesIndi = document.getElementById("mes_inicio").selectedIndex;
	 var anoIndi = document.getElementById("ano_inicio").selectedIndex;
	 
	 var dia = document.getElementById("dia_inicio")[diaIndi].text;
	 var mes = document.getElementById("mes_inicio")[mesIndi].text;
	 var ano = document.getElementById("ano_inicio")[anoIndi].text;
	 
	 
	 if(dia == ''){
	 	alert("Selecione um dia!");
	 	return;	
	 }
	 if(mes == '0'){
	 	alert("Selecione um mes!");
	 	return;		
	 }
	 
	 if(ano == ''){
	 	alert("Selecione um ano!");
	 	return;		
	 }
	 
	 data +=ano + "-" + mes + "-" + dia;
	 OpRelSequenciaAjax.setListaBilhetador(bilhetadores);//setados os bilhetadores na sessão
	 OpRelSequenciaAjax.setListaData(data);//setados a data na sessão
	 
	 window.open('/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelatorioSequencia&subOperacao=relSequenciaAlarme','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=809,height=560');
	
}

function limpar(){
	$('dataInicio').value = '';
}

function limpar(){
	$('dataFim').value = '';
}
function retSalvar(valor){
	alert(valor);
	$('status').innerHTML = '';
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
		<td width="683" height="32" valign="top"><img
			src="/PortalOsx/imagens/relatorio_de_sequencia.gif" /></td>
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
			<!--DWLayoutTable-->
                
              <tr>
                <td height="24" align="left" valign="middle"><strong>Data:</strong></td>
                <td align="center" valign="middle">
                  <select id="dia_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
                    <option></option>                
                    <option>01</option>
                    <option>02</option>
                    <option>03</option>
                    <option>04</option>
                    <option>05</option>
                    <option>06</option>
                    <option>07</option>
                    <option>08</option>
                    <option>09</option>
                    <option>10</option>
                    <option>11</option>
                    <option>12</option>
                    <option>13</option>
                    <option>14</option>
                    <option>15</option>
                    <option>16</option>
                    <option>17</option>
                    <option>18</option>
                    <option>19</option>
                    <option>20</option>
                    <option>21</option>
                    <option>22</option>
                    <option>23</option>
                    <option>24</option>
                    <option>25</option>
                    <option>26</option>
                    <option>27</option>
                    <option>28</option>
                    <option>29</option>
                    <option>30</option>
                    <option>31</option>
                  </select>			</td>
                <td align="center" valign="middle"> / </td>
                <td align="center" valign="middle">
                  <select id="mes_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
                    <option value="0"></option>                
                    <option value="01">Janeiro</option>
                    <option value="02">Fevereiro</option>
                    <option value="03">Mar&ccedil;o</option>
                    <option value="04">Abril</option>
                    <option value="05">Maio</option>
                    <option value="06">Junho</option>
                    <option value="07">Julho</option>
                    <option value="08">Agosto</option>
                    <option value="09">Setembro</option>
                    <option value="10">Outubro</option>
                    <option value="11">Novembro</option>
                    <option value="12">Dezembro</option>
                  </select>			</td>
                <td align="center" valign="middle">/</td>
                <td align="left" valign="middle">
                  <select id="ano_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
					  <option></option>
					  <option>2007</option>
					  <option>2008</option>
					  <option>2009</option>
					  <option>2010</option>
					  <option>2011</option>
					  <option>2012</option>
					  <option>2013</option>
					  <option>2014</option>
					  <option>2015</option>
					  <option>2016</option>
                  </select>			</td>
			<tr>
			<td>&nbsp; </td>				
			</tr>
			<tr>
			<td>&nbsp; </td>				
			</tr>
			<tr>
			<td>&nbsp; </td>				
			</tr>
			<tr>
			<td>&nbsp;</td>				
			</tr>
		</table>
		<table width="80%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" colspan="3">
				<div id="selecionaRel">
				
					<tr>
						<td align="center">
						<font color="000000"><strong>Bilhetadores Disponíveis:</strong></font>
						</td>
						<td align="center"  height="1">&nbsp;</td>
						<td align="center" colspan="3" height="1">
						<font color="000000"><strong>Bilhetadores Selecionados:</strong></font>
						</td>

					</tr>
					<tr>
						<td align="center">
						<p><select name="ListaBilhetadoresDisponiveis" id="listaDisponiveis" size="5" multiple>

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