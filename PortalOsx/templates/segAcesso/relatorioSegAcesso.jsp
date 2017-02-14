<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/templates/segAcesso/det2007.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/RelatorioAjax.js'></script>
<style type="text/css">
a:visited,a:link,a:active{ 
	text-decoration:none
}
a:hover{
	text-decoration:underline;
	text-align: right;
	color:#FFFFFF;
}
.box {
	height: 422px;
	width: 735px;
	font-family: Verdana, Helvetica, sans-serif;
	font-size: 10px;
	font-weight: normal;
	border: 0px solid #b5c3ce;
	background-color: #FFFFFF;
	
	overflow: auto;
}

.style1 {color: #FFFFFF}
</style>
<script type="text/javascript">
var numPag = 0;
var totalPg = 0;
var ordem = "";
function init()
{
	RelatorioAjax.isConsolidadoAcesso(retIsConsolidadoAcesso);
	RelatorioAjax.getNumPaginasRelAcesso(preenchePg);
}
function retIsConsolidadoAcesso(valor){
	if(valor){
		$('titulo').innerHTML = "Estat&iacute;stica de Acesso";
	}else{
		$('titulo').innerHTML = "Relat&oacute;rio de Acesso";
	}
}

function preenchePg(numeroPaginas)
{
	totalPg = numeroPaginas;
	RelatorioAjax.executarRelatorioAcesso(mostrarRel,numPag);//chama o relatorio com a pagina inicial
}

function Pagina(pg)
{
	var msgErro = "";
	if(pg=='anterior')
	{
		if(numPag==0)
		{
			msgErro = "Número de página inválido";
		}
		else
		{
			numPag = numPag - 1;
		}
	}
	else
	{
		if(totalPg <= numPag + 1)
		{
			msgErro = "Número de página inválido";
		}
		else
		{
			numPag = numPag + 1;
		}		
	}
	if(msgErro == "")
	{
			RelatorioAjax.executarRelatorioAcesso(mostrarRel,numPag);
	}
	else
	{
		alert(msgErro);
	}		
}
function Home(posicao)
{
	if(posicao=='inicio')
	{
		numPag = 0;
	}
	else
	{
		numPag = totalPg-1;
	}
	RelatorioAjax.executarRelatorioAcesso(mostrarRel,numPag);
}

function ordenaFiltros(nomeFiltro)
{
    var aux = ordem.indexOf(nomeFiltro);
	var parametroOrdenacao = "";
	if(aux!=-1)
	{
		var myArray = ordem.split("@");
		ordem = ""; 
		for (var i = 0; i < myArray.length; i++) 
		{
			if(myArray[i].indexOf(nomeFiltro)==-1)
			{
				if(i == (myArray.length - 1))
					ordem += myArray[i];
				else
					ordem += myArray[i]+"@";
			}
			else
			{
				if(myArray[i].indexOf("ASC")!=-1)
					parametroOrdenacao = nomeFiltro+" DESC";
				else
					parametroOrdenacao = nomeFiltro+" ASC";
					
				if(i == (myArray.length - 1))
					ordem += parametroOrdenacao; 
				else
					ordem += parametroOrdenacao+"@";	
					 
			}			
		}
	}
	else
	{
		if(ordem.length>0)
			ordem += "@"+nomeFiltro+" ASC"; 
		else
			ordem += nomeFiltro+" ASC";
			
		parametroOrdenacao = nomeFiltro+" ASC";  
	}
	
	RelatorioAjax.buscaRelatorioAcessoPreenchido(mostrarRel,numPag,parametroOrdenacao,false);	
}

function mostrarRel(html){
	$('idRel').innerHTML = html;	
	$('paginacao').value = (numPag+1)+"/"+(totalPg);//mostra com mais 1 pra comecar do 1 e não do zero
	mostrar('tabela_rel');
}

function exportar(){
	RelatorioAjax.exportarAcesso(resExportar,1);
}
function resExportar(){
	$('exportar').src = '../../servlets/Exportar';
}

function imprimir(){
	window.open("/PortalOsx/templates/segAcesso/imprimir.jsp?tipo=1",'Impressão',		'resizable=yes,status=yes,toolbar=yes,menubar=no,scrollbars=yes,width=680,height=560');
}

</script>
</head>

<body onLoad="init();" bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">


<table border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="1" bgcolor="#FFFFFF"><img src="/PortalOsx/imagens/1x1.gif" width="1" height="1"></td>
  </tr>
  <tr> 
    <td height="74" bgcolor="0D1237">
	   <img src="../../imagens/logo.gif" width="315" height="74" border="0"><img src="../../imagens/meio.gif" width="288" height="74"><img src="../../imagens/direita.gif" width="176" height="74">	</td>
  </tr>
</table>
<table width="785"  border="0" bgcolor="#BBBDBB">
  <!--DWLayoutTable-->
  <tr>
    <td bgcolor="#BBBDBB" width="199">
	<strong>
	<div id="titulo"></div>
	</strong>
	</td>
    <td width="570"></td>
  </tr>
   <tr>
    <td valign="top">
		<table id="tabela_rel" width="100%" border="0" cellpadding="0" cellspacing="0" style="display:none">
      		<tr>
        		<td valign="top">
				<div id="idRel" class="box">
				</div>			
				</td>
  			</tr>
     	</table>	 
		</td>
    <td></td>
   </tr>
   <tr>
     <td>
	 	<div id="Layer1" style="position:absolute; left:560px; top:530px; width:174px; height:19px; z-index:2">
   			<img src="../../imagens/iconesrelagendadoPG.gif" border="0" usemap="#Map2">
  			<map name="Map2">
     		<!--area shape=rect coords="0,0,19,18"     href="#"  alt="Visualiza relat&oacute;rio"                       OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="AbreJanela('paginas')"-->
     		<area shape=rect coords="43,0,131,18"   href="#"  alt="Indicadores"                                      OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="AbreJanela('indicadores')">
     		<!--area shape=rect coords="134,0,153,18"  href="#"  alt="Logs"                                             OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="AbreJanela('logs')"-->
     		<!--area shape=rect coords="156,0,175,18"  href="#"  alt="Configura&ccedil;&atilde;o de visualiza&ccedil;&atilde;o" OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="AbreJanela('configuracao')"-->
     		<!--area shape=rect coords="22,0,41,18"    href="#"  alt="Download"                                         OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="AbreJanela('download')"-->
     		<area shape=rect coords="0,0,19,18"    href="#"  alt="Vai para o in&iacute;cio"                         OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="Home('inicio')">
     		<area shape=rect coords="22,0,41,18"   href="#"  alt="P&aacute;gina anterior"                           OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="Pagina('anterior')">
     		<area shape=rect coords="134,0,153,18" href="#"  alt="Pr&oacute;xima p&aacute;gina"                     OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="Pagina('proxima')">
     		<area shape=rect coords="156,0,175,18" href="#"  alt="Vai para o fim"                                   OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="Home('fim')">
  		</map>
	</div>
	<div id="Layer2" style="position:absolute; left:605px; top:529px; width:30px; height:20px; z-index:2">
	<input type="text" id="paginacao" name="paginacao" value="" class="inputtext" style="border:1px solid black" size="15" onBlur="MudaPagina()">	 
	</div>
	<div id="Layer3" style="position:absolute; left:500px; top:530px; width:60px; height:20px; z-index:2">
	<a href="javascript:imprimir();"><img src="../../imagens/iconeImprimir.gif" border="0"></a>	
	<a href="javascript:exportar();"><img src="../../imagens/iconeExportar.gif" border="0"></a>	
	</div>
	</td>
   </tr>
   <tr>
     <td height="33"></td>
     <td></td>
   </tr>
</table>
<iframe src="" id="exportar" name="exportar" style="display:none"></iframe>
</body>
</html>
