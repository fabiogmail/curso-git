<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<% 
String idSessao = request.getSession().getId();
System.out.println(idSessao);
%>
<html>
<head>
	<title>Perfis</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Pragma" content="no-cache">
	<link rel="stylesheet" href="/PortalOsx/css/style.css">
	<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
	<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
	<script type='text/javascript' src='/PortalOsx/dwr/interface/OpListaHistoricoAjax.js'></script>
	
	<style type="text/css">
		#tabela {
			margin-left: 10px;
		}
		
		#agenda {
			height: 383px;
			overflow: auto;
		}
		
		#divGeral{
			width: 780px;
		}
		
		.nivel1 {
			margin-left:0px;
		}
		.nivel1 p {
			color:#071f64;
			font-weight:bold;
			display:inline;
		}
		
		.nivel2 {
			margin-left:20px;
		}
		
		.nivel2 p{
			color:#0F59E0;
			font-weight:bold;
			display:inline;
		}
		
		
		.nivel3 {
			margin-left:40px;
		}
		.nivel4 {
			margin-left:102px;
		}
		
		.nivel3 p, .nivel4 p {
			color:#1487E3;
			font-weight:bold;
			display:inline;
		}
	</style>
	
	<script type="text/javascript">
		var ultimoPerfil = "";
		var ultimoTipoRel = "";
	
		function lista(idSessao){
			document.getElementById("agenda").innerHTML = "Carregando informa&ccedil;&otilde;es...";
			OpListaHistoricoAjax.listaHistoricos(idSessao, retLista);
		}
		
		function retLista(valor){
		var html = '';
			if(valor.length < 1){
			html += "<table id='tabela' width='100%' bgColor='#FFFFFF' style='margin:0px' cellspacing='0' cellpadding='2'> ";
				html+="<tr bgColor='#F0F0F0'>";				
					html+="<td>";
						html+="<div class='nivel1'><p>N&atilde;o existe(m) historico(s) executado(s)</p></div>";
					html+="</td>";
				html+="</tr>";
			html +="</table>" ;
			}else if(valor == "erro"){
				html += "<table id='tabela' width='100%' bgColor='#FFFFFF' style='margin:0px' cellspacing='0' cellpadding='2'> ";
				html+="<tr bgColor='#F0F0F0'>";				
					html+="<td>";
						html+="<div class='nivel1'><p>Erro ao listar hist&oacute;ricos!</p></div>";
					html+="</td>";
				html+="</tr>";
			html +="</table>" ;
			}else{
				var agendas = new Array();
				agendas = valor.split("\n");
				html = "<table id='tabela' width='100%' bgColor='#FFFFFF' style='margin:0px' cellspacing='0' cellpadding='2'> ";			
			
				for(var i=0;i<agendas.length;i++){
					var campos = new Array();
					campos = agendas[i].split(";");
					
					//var exec = new Array(); 						
					//exec = campos[11].split(",");//obtendo execu��es
					
	
					if(campos[0] != ultimoPerfil){
					html+="<tr bgColor='#F0F0F0'>";				
						html+="<td>";
							html+="<div class='nivel1'><p>Perfil: </p> "+campos[0]+"</div>";
						html+="</td>";
					html+="</tr>";
					}
					if( (campos[1] != ultimoTipoRel || campos[0] != ultimoPerfil) ){
					html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
							html+="<div class='nivel2'><p>Tipo: </p>"+campos[1]+"</div>";
						html+="</td>";
					html+="</tr>";
					}
					ultimoPerfil = campos[0];
					ultimoTipoRel = campos[1];
					html+="<tr bgColor='#F0F0F0'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Relat&oacute;rio: </p> "+campos[2]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Usu&aacute;rio: </p> "+campos[3]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#F0F0F0'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Data de Inicio: </p> "+campos[4]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Data de T&eacute;rmino: </p> "+campos[5]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#F0F0F0'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Consolida&ccedil;&atilde;o/Hora Inicial: </p> "+campos[6]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Consolida&ccedil;&atilde;o/Hora Final: </p> "+campos[7]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#F0F0F0'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Lat&ecirc;ncia: </p> "+campos[8]+"</div>";
						html+="</td>";
					html+="</tr>";
					html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Periodicidade: </p> "+campos[9]+"</div>";
						html+="</td>";
					html+="</tr>";
						html+="<tr bgColor='#F0F0F0'>";
							html+="<td>";
						html+="<div class='nivel3'><p>Status: </p> "+campos[10]+"</div>";
					html+="</td>";
				html+="</tr>";
					/*html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
							html+="<div class='nivel3'><p>Execuções: </p> "+exec+"</div>";
						html+="</td>";
					html+="</tr>";
//Organizando as varias execucoes possiveis
/*
					var label = "<p>Execu&ccedil;&otilde;es: </p>";
					var nivel = "nivel3";
					for(var j = 0;j< exec.length;j++){
					if(j == 1){
						label = "";
						nivel ="nivel4";	
					}
					var cor = '#F0F0F0';
				
					if(j%2 == 0)
						cor = '#F0F0F0';
					else
						cor = '#FFFFFF';
					
						html+="<tr bgColor='"+cor+"'>";
						html+="<td>";
							html+="<div class='"+nivel+"'>"+label+exec[j]+"</div>";
						html+="</td>";
					html+="</tr>";
//Final da organiza��o das execu��es poss�veis.				
				
				}*/
					html+="<tr bgColor='#FFFFFF'>";
						html+="<td>";
						html+="&nbsp;";
						html+="</td>";
					html+="</tr>";
					}
				html+="</table>";
			}
			document.getElementById("agenda").innerHTML = html;
		}		 
	</script>
</head>

<body bgcolor="#FFFFFF" style="margin:0px" onload="lista('<%=idSessao%>')">
<div id="divGeral" style="height:383px;background:url(/PortalOsx/imagens/fundo.gif)">
<table id="tabela" width="99%" >
	<tr bgcolor="#000033">
		<td>
			<b> <font color="#ffffff">Informa&ccedil;&otilde;es</font></b>
		</td>
	</tr>
	<tr>
		<td>
			<div id="agenda"></div>
		</td>
	</tr>
</table>
</div>
</td>
</tr>
</boby>
</html>