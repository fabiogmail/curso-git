<%
	String tipo = (String)request.getParameter("tipo");	
%>

<html>
<head>
<title>CDRView | Visent<%=tipo%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/templates/segAcesso/det2007.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/RelatorioAjax.js'></script>
<script type="text/javascript">
<!--
function init(){
	$('relatorio').innerHTML = "Carregando...";
	carregar();
}
<%if(tipo.equals("1")){%>
function carregar(){
	RelatorioAjax.executarRelatorioAcesso(mostrarRel,-1);
}
<%}else{%>
function carregar(){
	RelatorioAjax.executarRelatorioUso(mostrarRel,-1);
}
<%}%>
function mostrarRel(valor){
	$('relatorio').innerHTML = valor;
	window.print();
}
-->
</script>
</head>

<body onload="init();">
<div id="relatorio"></div>
</body>
</html>
