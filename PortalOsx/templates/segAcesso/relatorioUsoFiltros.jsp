<html>
<head>
<title>Segurança de Acesso</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/templates/segAcesso/det2007.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/RelatorioAjax.js'></script>
<style type="text/css">

.selectBoxNormalMax {
background-color: #F7F7F7; 
width:145; color: #1A4256; 
font-size:10px;
font-family: Verdana, Arial, Helvetica, sans-serif 
}

</style>
<script type="text/javascript">

function init(){
	var id = window.opener.getLinha();
	$('imgLoad').innerHTML = '<br><br><img src="/PortalOsx/imagens/loading.gif" align="absmiddle" border="0"/>&nbsp;&nbsp;<fmt:message key="LOAD"/>Carregando...';
	RelatorioAjax.getListaDeFiltros(mostraFiltros,id);
}

var buffer = new StringBuffer();

function StringBuffer() { 
     this.buffer = []; 
};

StringBuffer.prototype.append = function append(string) {
      this.buffer.push(string); 
     return this; 
}; 

StringBuffer.prototype.toString = function toString() {
     return this.buffer.join(""); 
};

/*funcões usada para monitorar o tempo quando necessario*/
function visitor_in() 
{
       enter=new Date();
}

function visitor_out() 
{
        exit=new Date();
        time_dif=(exit.getTime()-enter.getTime())/1000;
        time_dif=Math.round(time_dif);
        alert ("Voce ficou  " + time_dif + " segundos na página")
}

function mostraFiltros(filtros){
	$('imgLoad').innerHTML = '';
	var htmlFiltrosPrimarios = new StringBuffer();
	var htmlFiltrosSecundarios = new StringBuffer();
	
	
	for(var i=0; i < filtros.length; i++)
	{	
		var filtross = '';
		if(filtros[i].valoresFiltro.length != 0)
		{
			filtross = filtros[i].valoresFiltro.split(";");
		}
		if(filtros[i].tipoFiltro==1)
		{		
			htmlFiltrosPrimarios.append('<table width="300" border="1" cellpadding="0" cellspacing="0">');	
			htmlFiltrosPrimarios.append('<tr>');	
			htmlFiltrosPrimarios.append('<td height="20" align="left" bgcolor="DEE7EF" valign="middle"  class="titulo3" ><b>Filtro:</b> '+ filtros[i].nomeFiltro+' - <b>Grupo:</b> '+filtros[i].nomeGrupo+'</td>');
			
			if(filtross.length>0)
			{
				htmlFiltrosPrimarios.append('<td  width="30" height="20" align="center" valign="middle" bgcolor="DEE7EF" class="titulo4" onClick="mudarFigura('+i+','+filtros[i].id+');"><a href="#">');
				htmlFiltrosPrimarios.append('<img id="imagem_'+i+'" src="/PortalOsx/imagens/minimizar.gif" width="16" height="16" border="0" /></a>');
			}
			htmlFiltrosPrimarios.append('</td></tr>');
			htmlFiltrosPrimarios.append('</table>');
			if(filtross.length>0)
			{
				htmlFiltrosPrimarios.append('<table width="300" border="0" cellpadding="0" cellspacing="0" id="filtro_'+filtros[i].id+'" style="display:none">');    	
				htmlFiltrosPrimarios.append('<td valign="top">');
				htmlFiltrosPrimarios.append('<div >');
				htmlFiltrosPrimarios.append('<select size="10" class="selectBoxNormalMax" style="width:300;" id="listaRegras_'+i+'">');
				for(var j=0; j < filtross.length; j++){
					htmlFiltrosPrimarios.append('<option>'+filtross[j]+'</option>');
				}
				htmlFiltrosPrimarios.append('</select>');
				htmlFiltrosPrimarios.append('</div></td>');
				htmlFiltrosPrimarios.append('</table>');
			}
		}
		else//pra filtro secundario
		{
			htmlFiltrosSecundarios.append('<table width="300" border="1" cellpadding="0" cellspacing="0" >');
			htmlFiltrosSecundarios.append('<tr>');		
			if(filtross.length>0)
			{
				htmlFiltrosSecundarios.append('<td  height="20" align="left" bgcolor="DEE7EF" valign="middle" class="titulo3"><b>Filtro:</b> '+ filtros[i].nomeFiltro+' - <b>Grupo:</b> '+filtros[i].nomeGrupo+'</td>');
				htmlFiltrosSecundarios.append('<td width="30" height="20" align="center" valign="middle" bgcolor="DEE7EF" class="titulo4" onClick="mudarFigura('+i+','+filtros[i].id+');"><a href="#">');
				htmlFiltrosSecundarios.append('<img id="imagem_'+i+'" src="/PortalOsx/imagens/minimizar.gif" width="16" height="16" border="0" /></a>');
			}
			else
			{
				htmlFiltrosSecundarios.append('<td  height="20" align="left" bgcolor="DEE7EF" valign="middle" class="titulo3"><b>Filtro:</b> '+ filtros[i].nomeFiltro+' - <b>Valor:</b> '+filtros[i].nomeGrupo+'</td>');
			}
			htmlFiltrosSecundarios.append('</td></tr>');
			htmlFiltrosSecundarios.append('</table>');
			if(filtross.length>0)
			{
				htmlFiltrosSecundarios.append('<table width="300" border="0" cellpadding="0" cellspacing="0" id="filtro_'+filtros[i].id+'" style="display:none">');    	
				htmlFiltrosSecundarios.append('<td valign="top">');
				htmlFiltrosSecundarios.append('<div >');
				htmlFiltrosSecundarios.append('<select size="10" class="selectBoxNormalMax" style="width:300;" id="listaRegras_'+i+'">');
				for(var j=0; j < filtross.length; j++){
					htmlFiltrosSecundarios.append('<option>'+filtross[j]+'</option>');
				}
				htmlFiltrosSecundarios.append('</select>');
				htmlFiltrosSecundarios.append('</div></td>');
				htmlFiltrosSecundarios.append('</table>');
			}
		}	
	}

	if(htmlFiltrosPrimarios!='')
	{
		$('relFiltrosTitulo').innerHTML = '<img src="/PortalOsx/imagens/primarios.gif">';
	}
	$('relFiltros').innerHTML = htmlFiltrosPrimarios.toString();
	if(htmlFiltrosSecundarios!='')
	{
		$('relFiltrosSecundariosTitulo').innerHTML = '<img src="/PortalOsx/imagens/secundarios.gif">';
	}
	$('relFiltrosSecundarios').innerHTML = htmlFiltrosSecundarios.toString();
}


/*function mostraFiltros(filtros){
visitor_in();
	$('imgLoad').innerHTML = '';
	var htmlFiltrosPrimarios = '';
	var htmlFiltrosSecundarios = '';
	
	
	for(var i=0; i < filtros.length; i++)
	{	
		var filtross = '';
		if(filtros[i].valoresFiltro.length != 0)
		{
			filtross = filtros[i].valoresFiltro.split(";");
		}
		if(filtros[i].tipoFiltro==1)
		{		
			htmlFiltrosPrimarios += '<table width="300" border="1" cellpadding="0" cellspacing="0">';	
			htmlFiltrosPrimarios += '<tr>';	
			htmlFiltrosPrimarios += '<td height="20" align="left" bgcolor="DEE7EF" valign="middle"  class="titulo3" ><b>Filtro:</b> '+ filtros[i].nomeFiltro+' - <b>Grupo:</b> '+filtros[i].nomeGrupo+'</td>';
			
			if(filtross.length>0)
			{
				htmlFiltrosPrimarios += '<td  width="30" height="20" align="center" valign="middle" bgcolor="DEE7EF" class="titulo4" onClick="mudarFigura('+i+','+filtros[i].id+');"><a href="#">';
				htmlFiltrosPrimarios += '<img id="imagem_'+i+'" src="/PortalOsx/imagens/minimizar.gif" width="16" height="16" border="0" /></a>';
			}
			htmlFiltrosPrimarios += '</td></tr>';
			htmlFiltrosPrimarios += '</table>';
			if(filtross.length>0)
			{
				htmlFiltrosPrimarios += '<table width="300" border="0" cellpadding="0" cellspacing="0" id="filtro_'+filtros[i].id+'" style="display:none">';    	
				htmlFiltrosPrimarios += '<td valign="top">';
				htmlFiltrosPrimarios += '<div >';
				htmlFiltrosPrimarios += '<select size="10" class="selectBoxNormalMax" style="width:300;" id="listaRegras_'+i+'">';
				for(var j=0; j < filtross.length; j++){
					htmlFiltrosPrimarios +='<option>'+filtross[j]+'</option>';
				}
				htmlFiltrosPrimarios += '</select>';
				htmlFiltrosPrimarios += '</div></td>';
				htmlFiltrosPrimarios += '</table>';
			}
		}
		else//pra filtro secundario
		{
			htmlFiltrosSecundarios += '<table width="300" border="1" cellpadding="0" cellspacing="0" >';
			htmlFiltrosSecundarios += '<tr>';		
			htmlFiltrosSecundarios += '<td width="300" height="20" align="left" bgcolor="DEE7EF" valign="middle" class="titulo3"><b>Filtro:</b> '+ filtros[i].nomeFiltro+' - <b>Grupo:</b> '+filtros[i].nomeGrupo+'</td>';
			if(filtross.length>0)
			{
				htmlFiltrosSecundarios += '<td width="30" height="20" align="center" valign="middle" bgcolor="DEE7EF" class="titulo4" onClick="mudarFigura('+i+','+filtros[i].id+');"><a href="#">';
				htmlFiltrosSecundarios += '<img id="imagem_'+i+'" src="/PortalOsx/imagens/minimizar.gif" width="16" height="16" border="0" /></a>';
			}
			htmlFiltrosSecundarios += '</td></tr>';
			htmlFiltrosSecundarios += '</table>';
			if(filtross.length>0)
			{
				htmlFiltrosSecundarios += '<table width="260" border="0" cellpadding="0" cellspacing="0" id="filtro_'+filtros[i].id+'" style="display:none">';    	
				htmlFiltrosSecundarios += '<td height="126" valign="top">';
				htmlFiltrosSecundarios += '<div >';
				htmlFiltrosSecundarios += '<select size="10" class="selectBoxNormalMax" style="width:300;" id="listaRegras_'+i+'">';
				for(var j=0; j < filtross.length; j++){
					htmlFiltrosSecundarios +='<option>'+filtross[j]+'</option>';
				}
				htmlFiltrosSecundarios += '</select>';
				htmlFiltrosSecundarios += '</div></td>';
				htmlFiltrosSecundarios += '</table>';
			}
		}	
	}

	if(htmlFiltrosPrimarios!='')
	{
		$('relFiltrosTitulo').innerHTML = '<img src="/PortalOsx/imagens/primarios.gif">';
	}
	$('relFiltros').innerHTML = htmlFiltrosPrimarios;
	if(htmlFiltrosSecundarios!='')
	{
		$('relFiltrosSecundariosTitulo').innerHTML = '<img src="/PortalOsx/imagens/secundarios.gif">';
	}
	$('relFiltrosSecundarios').innerHTML = htmlFiltrosSecundarios;
	visitor_out();
}*/

function mudarFigura(i,id){
	mostraFil(id);
	var imagem = $('imagem_'+i).src;	
	if(imagem.indexOf('minimizar') != -1){
		$('imagem_'+i).src = "../../imagens/maximizar.gif";
	}else{
		$('imagem_'+i).src = "../../imagens/minimizar.gif";
	}
}

function mostraFil(id){
	if(isVisible('filtro_'+id)){
		ocultar('filtro_'+id);
	}else{
		mostrar('filtro_'+id);
	}
}

</script>
</head>

<body onLoad="init()" >
<table width="300" border="0" cellpadding="0" cellspacing="0">
  <!--DWLayoutTable-->
  
  <!--tr>
    <td width="400" height="70" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0"-->
      <!--DWLayoutTable-->
      <!--tr>
        <td width="121" height="68">&nbsp;</td-->
          <!--td width="312" align="center" valign="middle"><a href="#" ><img src="/PortalOsx/imagens/logo_cliente.gif" width="120" height="47"  border="0"/></a></td-->
          <!--td width="167">&nbsp;</td>
        </tr>
      <tr>
        <td height="1"></td>
          <td></td>
          <td></td>
        </tr>
      
      <!--DWLayoutTable-->      
    <!--/table></td>
  </tr-->
  <tr>
  	<td><div id="imgLoad"></div></td>
    <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
	  <tr>
        <td align="left" valign="bottom">
			<div id="relFiltrosTitulo"></div>
		</td>
      </tr>
      <tr>
        <td align="center" valign="top" style="border-width: 1px;border-style: double;border-color: #7D7D7D;">
			<div id="relFiltros"></div>
		</td>
      </tr>
	  <tr>
	  	<td>&nbsp;</td>
	  </tr>
	  <tr>
	  <td align="left" valign="bottom">
			<div id="relFiltrosSecundariosTitulo"></div>
		</td>
	  </tr>
	  <tr>
	 	 <td align="center" valign="top" style="border-width: 1px;border-style: double;border-color: #7D7D7D;">
			<div id="relFiltrosSecundarios"></div>
		 </td>
	  </tr>
      <tr>
        <td></td>
      </tr>
      <tr>
        <td ></td>
      </tr>
       
      <!--DWLayoutTable-->
      
      
      <!--DWLayoutTable-->
      
      
      <!--DWLayoutTable-->
      
      
    </table></td>
  </tr>
  <tr>
    <td height="3"></td>
  </tr>
</table>
</body>
</html>
