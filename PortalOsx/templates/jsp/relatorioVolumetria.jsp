<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Relatorio de Volumetria</title>

<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpRelVolumetriaAjax.js'></script>

<style type="text/css">
#relatorio {
	height: 440px;
    width: 875px;
    font-family: Verdana, Helvetica, sans-serif;
    font-size: 10px;
    font-weight: normal;
    border: 0px solid #b5c3ce;
    background-color: #FFFFFF;       
    overflow: auto;
}
#relatorio a:hover {text-decoration: none}; 
</style>

<script type="text/javascript">
<!--
DWREngine.setErrorHandler(erro);
var numPaginas;
var paginaAtual = 0;

function erro(valor){
	alert(valor);
}

function selecPagina(pagina){
	OpRelVolumetriaAjax.setExportacao(1);//setar formato HTML como padrão para download.
	OpRelVolumetriaAjax.getRelatorioPagina(retRelatorio, pagina);	
}

function init(pagina){
	OpRelVolumetriaAjax.setExportacao(1);//setar formato HTML como padrão para download.
	OpRelVolumetriaAjax.getRelatorio(retRelatorio, pagina);	
}

function retRelatorio(valor){
	document.getElementById("relatorio").innerHTML = valor;	
	
	OpRelVolumetriaAjax.getQtdPaginas(retQtdPaginas);
	
}

function retQtdPaginas(valor){
	numPaginas = valor;
	document.getElementById("qtdPaginas").innerHTML = numPaginas;
	document.getElementById("paginaAtual").value = paginaAtual+1;
}

function exportar(){
	document.getElementById("download").src = "${pageContext.request.contextPath}/servlet/ExportaRel?exportacao=3&randon="+Math.random();		
}

function configurar(){
	window.open('/PortalOsx/templates/jsp/formatoExport.jsp','format','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=315,height=160');
}

function navegar(valor){
	if(valor == '+'){
		if(paginaAtual < numPaginas - 1){
			paginaAtual ++;
		}
		else
			paginaAtual = numPaginas - 1;
	}
	
	else if(valor == '-'){
		if(paginaAtual > 0)
			paginaAtual --;
		
		else
			paginaAtual=0;
	}
	else if(valor == '++')
		paginaAtual = numPaginas - 1;
	else if(valor == '--')
		paginaAtual = 0;
	else if(valor == 'salto'){
		
		var paginaAux = 0;
		paginaAux = document.getElementById("paginaAtual").value;
		if(paginaAux > numPaginas || paginaAux < 1){
			document.getElementById("paginaAtual").value = paginaAtual;
		}
		
		else{
			paginaAtual = parseInt(paginaAux) -1;
			
		}
	}	
		
	init(paginaAtual);
	selecPagina(paginaAtual);
}

function irPara(tecla){
var valor=window.event.keyCode;
	
	if(valor == 13){
		var pagina = document.getElementById("paginaAtual").value;
				
		validaInteiro(pagina);
	}
}
 
function validaInteiro(pStr)//testar se o valor informado no campo é válido.
{
	var reDigits = /^\d+$/;
	
	if (reDigits.test(pStr)) {
		navegar('salto');
	} else if (pStr != null && pStr != "") {
		alert(pStr + ": Valor inválido.");
		init(paginaAtual)
		selecPagina(paginaAtual);
	}
	
	}
	
function finaliza(){
	OpRelVolumetriaAjax.finaliza();
}

</script>
</head>
<body bgcolor="#BBBDBB" style="margin: 0px" onLoad="init(0);" onunload="finaliza();">
<table name="banner" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td height="1" bgcolor="#FFFFFF"><img src="/PortalOsx/imagens/1x1.gif" width="1" height="1"></td>
  </tr>
  <tr> 
    <td height="74" bgcolor="0D1237">
	   <img src="../imagens/logo.gif" width="315" height="74" border="0"><img src="../imagens/meio.gif" width="352" height="74"><img src="../imagens/direita.gif" width="207" height="74">	</td>
  </tr>
</table>

	<table id="tabelaGeral" width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
		
		<tr>
			<td height="20px">				
				<b>&ensp;Relat&oacute;rio de Volumetria</b>
			</td>
		</tr>
			
		<tr>			
			<td>
				<div id="relatorio"></div>						
			</td>
		</tr>

	</table>
	<table id="tabNavegar" width="30%" height="100%" align="center">
		<tr>
			<td>
			
				<div id="Layer1" style="position:absolute; left:500px; top:540px; width:174px; height:19px; z-index:2">
   				<img src="../imagens/iconesrelagendadoPG.gif" border="0" usemap="#Map2">
  				<map name="Map2">
     			<area shape=rect coords="1,1,19,18"    href="#"  alt="Vai para o in&iacute;cio"                         OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="navegar('--')">
     			<area shape=rect coords="22,0,41,18"   href="#"  alt="P&aacute;gina anterior"                           OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="navegar('-')">
     			<area shape=rect coords="134,0,153,18" href="#"  alt="Pr&oacute;xima p&aacute;gina"                     OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="navegar('+')">
     			<area shape=rect coords="156,0,175,18" href="#"  alt="Vai para o fim"                                   OnMouseOut="window.status=''; return true"  OnMouseOver="window.status=''; return true" onClick="navegar('++')">
  				</map>
				</div>
				
				<div id="Layer2" style="position:absolute; left:550px; top:536px; width:30px; height:20px; z-index:2">
					<table>
						<tr>
							<td>
								<input type="text" id="paginaAtual" name="paginacao" value="" class="inputtext" style="border:1px solid black;height: 15px;" size="2"  onkeypress="irPara(this.value);">
							</td>
							<td>De</td>
							<td>
								<div id="qtdPaginas" style="font-weight: bolder; font-size: 12px;"></div>
							</td>
						</tr>
					</table>
					
				</div>
				<div id="Layer3" style="position:absolute; left:430px; top:540px; width:60px; height:20px; z-index:2">
				<a href="javascript:configurar();"><img src="../imagens/editar.png" border="0" alt="Configurar formato"  ></a>
				<a href="javascript:exportar();"><img src="../imagens/iconeExportar.gif" border="0" alt="Download"  ></a>
				</div>
				
			</td>
		</tr>
	</table>
	<div id="divDownload">
		<iframe id="download" src="" style="display: none;"></iframe>
	</div>
</body>
</html>