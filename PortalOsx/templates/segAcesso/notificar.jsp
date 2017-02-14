<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<html>
<head>
<title>Notificar</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/EmailAjax.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpUsuariosAjax.js'></script>
<style type="text/css">
a:visited,a:link,a:active{ 
	color:#000000;
	text-decoration:none
}
a:hover{
	text-decoration:underline;
	text-align: right;
	color:#000000;
}
div#lista {
	height: 125px;
	width: 450px;
	border: 1px solid #b5c3ce;
	background-color: #FFFFFF;	
	overflow: auto;
}
</style>
<script type="text/javascript">
<!--
var usuarios;
var podeEmail;
var seltodos_ver = false;
function $(id) {
	return document.getElementById(id);
}
function init(){
	EmailAjax.iniciar(retIniciarEmail);
}
function retIniciarEmail(valor){
	podeEmail = valor;
	/*if(!valor){
		$('msg').style.backgroundColor = "#E6E6E6"
		$('msg').disabled = true;
		$('assunto').style.backgroundColor = "#E6E6E6"
		$('assunto').disabled = true;
		$('lista').style.backgroundColor = "#E6E6E6"
		ocultar('botaoEnviar');
		$('status').innerHTML = '<font color="#CC0000">SMTP ou E-mail para envio nao foram configurados</font>';
	}else{
		$('lista').innerHTML = 'Carregando...';
		OpUsuariosAjax.iniciar(retIniciarUsu);
	}*/
	$('lista').innerHTML = 'Carregando...';
	OpUsuariosAjax.iniciar(retIniciarUsu);
}
function retIniciarUsu(){
	OpUsuariosAjax.getListaUsuarios(retUsuarios);
}
/*
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td valign="middle" height="20" width="20"><input type="checkbox" id="usu1"/></td>
<td valign="middle" height="20" width="325">Feijao</td>
</tr>
</table>
*/
function retUsuarios(valor){
	usuarios = valor;
	var html = '<table width="100%" border="0" cellpadding="0" cellspacing="0">';
	for(var i = 0; i < usuarios.length; i++){
		html += '<tr>';
		html += '<td valign="middle" height="20" width="20"><input type="checkbox" id="'+usuarios[i].usuario+'"/></td>';
		html += '<td valign="middle" height="20" width="430">';
		html += usuarios[i].usuario+' ';
		if(usuarios[i].email == ''){
			html += '(E-mail nao cadastrado)';
		}else{
			if(verificaEmail(usuarios[i].email)){
				html += '('+usuarios[i].email+')';
			}else{
				html += '(E-mail '+usuarios[i].email+' invalido)';
			}			
		}
		html += ' - ';
		if(usuarios[i].telefone == ''){
			html += '(Telefone nao cadastrado)';
		}else{
			html += '('+usuarios[i].telefone+')';
		}		
		html += '</td>';
		html += '</tr>';		
	}
	html += '</table>';
	$('lista').innerHTML = html;
}

function enviar(){
	var ehEmail = false;
	if($('radio_email').checked){
		ehEmail = true;
	}

	if($('assunto').value == '' && ehEmail){
		$('assunto').focus();
		alert('Assunto em branco');
		return;
	}
	if($('msg').value == ''){
		$('msg').focus();
		alert('Mensagem em branco');
		return;
	}
	
	if(ehEmail){
		if(!podeEmail){
			alert('SMTP ou E-mail para envio nao foram configurados');
			return;
		}
		var emails = new Array();
		var indice = 0;
		for(var i = 0; i < usuarios.length; i++){
			if($(usuarios[i].usuario).checked){
				if(usuarios[i].email != ''){
					if(verificaEmail(usuarios[i].email)){
						emails[indice] = usuarios[i].email;
						indice++;
					}					
				}				
			}
		}
		if(emails.length <= 0){
			alert('Selecione algum usuario que possua e-mail cadastrado para enviar a mensagem');
			return;
		}
		
		ocultar('botaoEnviar');
		$('status').innerHTML = 'Enviando...';
		EmailAjax.enviar(retEnviar,emails,$('assunto').value,$('msg').value);
	}else{
		var telefones = '';
		for(var i = 0; i < usuarios.length; i++){
			if($(usuarios[i].usuario).checked){
				if(usuarios[i].telefone != ''){
					telefones += usuarios[i].telefone+';';
				}
			}
		}
		
		if(telefones == ''){
			alert('Selecione algum usuario que possua telefone cadastrado para enviar a mensagem');
			return;
		}
		telefones = telefones.substring(0,telefones.length-1);
		ocultar('botaoEnviar');
		$('status').innerHTML = 'Enviando...';
		EmailAjax.enviarSMS(retEnviar,telefones,$('msg').value);
	}

}
function retEnviar(valor){
	$('status').innerHTML = '';
	mostrar('botaoEnviar');
	if(valor){
		alert('Mensagem enviada com sucesso!');
	}else{
		alert('Problema ao enviar a mensagem');
	}
}


function mostrar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="block";
}

function ocultar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="none";
}

function sel_todos(){
	if(seltodos_ver){
		seltodos_ver = false;
	}else{
		seltodos_ver = true;
	}
	var indice = 0;
	for(var i = 0; i < usuarios.length; i++){
		$(usuarios[i].usuario).checked = seltodos_ver;
	}
}
function verificaEmail(valor){
	var reEmail1 = /^[\w!#$%&'*+\/=?^`{|}~-]+(\.[\w!#$%&'*+\/=?^`{|}~-]+)*@(([\w-]+\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail2 = /^[\w-]+(\.[\w-]+)*@(([\w-]{2,63}\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail3 = /^[\w-]+(\.[\w-]+)*@(([A-Za-z\d][A-Za-z\d-]{0,61}[A-Za-z\d]\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	
	if(reEmail1.test(valor)){
		return true;
	}else{
		return false;
	}
}
-->
</script>
</head>

<body bgcolor="#FFFFFF" style="margin:0px" onLoad="init();">
<table width="780" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
  <!--DWLayoutTable-->
  <tr>
    <td width="15" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="15" height="389"></td>
        </tr>
    </table></td>
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/enviamensagem.gif" /></td>
  <td width="142" valign="top"><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" /></td>
    <td width="10" rowspan="3" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="10" height="390"></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td rowspan="2" valign="top">
	
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	    <!--DWLayoutTable-->
	    <tr>
	      <td width="100" height="25" valign="middle"><strong>Assunto</strong></td>
          <td width="160" valign="middle"><input type="text" id="assunto"></td>
          <td width="180">&nbsp;</td>
          <td width="33"></td>
        </tr>
	    
	    <tr>
	      <td height="25" valign="middle"><strong>Mensagem:</strong></td>
          <td colspan="2" rowspan="2" valign="middle"><textarea cols="53" rows="6" id="msg"></textarea></td>
          <td>&nbsp;</td>
          <td></td>
        </tr>
	    <tr>
	      <td height="74">&nbsp;</td>
          <td>&nbsp;</td>
          <td></td>
        </tr>
	    <tr>
	      <td height="31">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td align="center" valign="bottom">
		  <p style="">
		  <a href="javascript:sel_todos();">Selecionar Todos</a>		  </p>		  </td>
	      <td>&nbsp;</td>
	      <td></td>
        </tr>
	    

	    <tr>
	      <td height="25" valign="middle"><strong>Usu&aacute;rios:</strong></td>
          <td colspan="3" rowspan="2" valign="top">
          <div id="lista"></div></td>
          <td></td>
        </tr>
	    <tr>
	      <td height="100">&nbsp;</td>
          <td></td>
        </tr>
	    
	    <tr>
	      <td height="20" valign="middle"><strong>Tipo de Envio: </strong></td>
          <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
            <!--DWLayoutTable-->
            <tr>
              <td width="20" height="20" align="center" valign="middle"><input type="radio" name="radio" id="radio_email" checked></td>
                <td width="60" valign="middle"><strong>E-Mail</strong></td>
                <td width="20" align="center" valign="middle"><input type="radio" name="radio" id="radio_sms"></td>
                <td width="60" valign="middle"><strong>SMS</strong></td>
              </tr>
          </table></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
	    <tr>
	      <td height="16"></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
	    <tr>
	      <td height="42" colspan="2" valign="top">
	        <div id="botaoEnviar">
          <a href="javascript:enviar();"><img src="/PortalOsx/imagens/enviarmensagem.gif" alt="Enviar" border="0"/></a>		</div></td>
          <td colspan="2" valign="top"><div id="status"></div></td>
          <td></td>
        </tr>
            </table></td>
    <td height="357" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="142" height="357" valign="top">Dicas...</td>
      </tr>
    </table>    </td>
  </tr>
  <tr>
    <td height="1"></td>
    <td></td>
  </tr>
</table>
</body>
</html>
