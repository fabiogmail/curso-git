<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<html>
<head>
<title>Configurações Gerais</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/ConfigAjax.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpUsuariosAjax.js'></script>
<script type="text/javascript">
<!--
var perfis;
function $(id) {
	return document.getElementById(id);
}
function init(){
	DWRUtil.addOptions("perfis",[{ id:'', nome:'Carregando...' }],"id","nome");
	OpUsuariosAjax.iniciar(retIniciar);	
}
function retIniciar(){
	OpUsuariosAjax.getListaPerfis(retPerfis);
}
function retPerfis(valor){
	perfis = valor;	
	ConfigAjax.buscarConf(retBuscarSMTP,'SMTP');	
}
function retBuscarSMTP(valor){
	if(valor == null){
		$('smtp').value = '';
	}else{
		$('smtp').value = valor.valor;
	}
	ConfigAjax.buscarConf(retBuscarEMAIL,'EMAIL');
}
function retBuscarEMAIL(valor){
	if(valor == null){
		$('email').value = '';
	}else{
		$('email').value = valor.valor;
	}
	ConfigAjax.buscarConf(retBuscarPERFIS_MONITORADOS,'PERFIS_MONITORADOS');
}
function retBuscarPERFIS_MONITORADOS(valor){
	var lista = new Array();
	if(valor != null){
		lista = valor.valor.split(';');
		for(var i = 0; i < lista.length; i++){
			DWRUtil.addOptions("sel_perfis",[{ id:lista[i], nome:lista[i]}],"id","nome");
		}
	}
	DWRUtil.removeAllOptions("perfis");
	for(var i = 0; i < perfis.length; i++){
		var adiciona = true;		
		for(var j = 0; j < lista.length; j++){
			if(perfis[i].perfil == lista[j]){
				adiciona = false;
			}
		}
		if(adiciona){
			DWRUtil.addOptions("perfis",[{ id:perfis[i].id, perfil:perfis[i].perfil}], "id", "perfil");	
		}		
	}	
	ConfigAjax.buscarConf(retBuscarTELEFONES_ALM,'TELEFONES_ALM');
}
function retBuscarTELEFONES_ALM(valor){
	var lista = new Array();
	if(valor != null){
		lista = valor.valor.split(';');
		for(var i = 0; i < lista.length; i++){
			DWRUtil.addOptions("telefones",[{ id:lista[i], nome:lista[i]}],"id","nome");
		}		
	}
	ConfigAjax.buscarConf(retBuscarEMAILS_ALM,'EMAILS_ALM');
}
function retBuscarEMAILS_ALM(valor){
	var lista = new Array();
	if(valor != null){
		lista = valor.valor.split(';');
		for(var i = 0; i < lista.length; i++){
			DWRUtil.addOptions("emails",[{ id:lista[i], nome:lista[i]}],"id","nome");
		}		
	}
	ConfigAjax.buscarConf(retBuscarHABILITAR_EMAIL,'HABILITAR_EMAIL');
}

function retBuscarHABILITAR_EMAIL(valor){
	if(valor == null){
		$('habilitar_email').checked = false;
	}else{
		if(valor.valor == '0'){
			$('habilitar_email').checked = false;
		}else{
			$('habilitar_email').checked = true;
		}
	}
	ConfigAjax.buscarConf(retBuscarHABILITAR_SMS,'HABILITAR_SMS');
}

function retBuscarHABILITAR_SMS(valor){
	if(valor == null){
		$('habilitar_sms').checked = false;
	}else{
		if(valor.valor == '0'){
			$('habilitar_sms').checked = false;
		}else{
			$('habilitar_sms').checked = true;
		}
	}
}

function salvar(){
	var smtp = $('smtp').value;
	var email = $('email').value;
	var selperfis = getListaLD('sel_perfis');
	var telefones = getListaLD('telefones');
	var emails = getListaLD('emails');
	var hab_email = 0; 
	if($('habilitar_email').checked) hab_email = 1;
	var hab_sms = 0; 
	if($('habilitar_sms').checked) hab_sms = 1;
	
	ocultar('botaoSalvar');
	$('status').innerHTML = 'Salvando...';
	//alert([smtp,email,selperfis,telefones,emails]);
	ConfigAjax.salvar(retsalvar,['SMTP','EMAIL','PERFIS_MONITORADOS','TELEFONES_ALM','EMAILS_ALM','HABILITAR_EMAIL','HABILITAR_SMS'],[smtp,email,selperfis,telefones,emails,hab_email,hab_sms]);
}
function retsalvar(){
	mostrar('botaoSalvar');
	$('status').innerHTML = '';
	alert('Salvo com sucesso!');
}

function add_telefone(){
	DWRUtil.addOptions("telefones",[{ id:$('ins_telefone').value, nome:$('ins_telefone').value }],"id","nome");
	$('ins_telefone').value = '';
	return false;
}
function add_email(){
	if(verificaEmail($('ins_email').value)){
		DWRUtil.addOptions("emails",[{ id:$('ins_email').value, nome:$('ins_email').value }],"id","nome");
		$('ins_email').value = '';
		return false;
	}else{
		return false;
	}	
}
function del_telefone(){
	for(var i = 0; i < $('telefones').options.length; i++){
		if($('telefones').options[i].selected){
			$('telefones').options[i] = null;
			i--;
		}
	}
	return false;
}
function del_email(){
	for(var i = 0; i < $('emails').options.length; i++){
		if($('emails').options[i].selected){
			$('emails').options[i] = null;
			i--;
		}
	}
	return false;
}

function mostrar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="block";
}

function ocultar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="none";
}
function listdouble(from, to){
	fromList = $(from);
  	toList = $(to);
	var lista = new Array();
	for(var i = 0; i < fromList.options.length; i++){
		if(fromList.options[i].selected){
			var text = fromList.options[i].text;
			var value = fromList.options[i].value;
			toList.options[toList.length] = new Option(text,value);
			fromList.options[i] = null;
			i--;
		}
	}
}

function getListaLD(from){
	fromList = $(from);
	var lista = '';
	for(var i = 0; i < fromList.options.length; i++){
		lista += fromList.options[i].text+';';
	}
	if(lista.length > 0){
		lista = lista.substring(0,lista.length-1);
	}
	return lista;
}
function verificaEmail(valor){
	var reEmail1 = /^[\w!#$%&'*+\/=?^`{|}~-]+(\.[\w!#$%&'*+\/=?^`{|}~-]+)*@(([\w-]+\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail2 = /^[\w-]+(\.[\w-]+)*@(([\w-]{2,63}\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	var reEmail3 = /^[\w-]+(\.[\w-]+)*@(([A-Za-z\d][A-Za-z\d-]{0,61}[A-Za-z\d]\.)+[A-Za-z]{2,6}|\[\d{1,3}(\.\d{1,3}){3}\])$/;
	
	if(reEmail1.test(valor)){
		return true;
	}else{
		alert('Email '+valor+' invalido');
		return false;
	}
}
-->
</script>
<style type="text/css">
<!--
.style2 {
	color: #FFFFFF;
	font-weight: bold;
}
-->
</style>
</head>

<body bgcolor="#FFFFFF" style="margin:0px" onLoad="init();">
<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
  <!--DWLayoutTable-->
  <tr>
    <td width="15" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="15" height="389"></td>
        </tr>
    </table></td>
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/configuracao.gif" /></td>
  <td width="142" valign="top"><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" /></td>
    <td width="10" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="10" height="389"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td rowspan="2" valign="top">
	
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	    <!--DWLayoutTable-->
	    <tr>
	      <td width="25" height="20">&nbsp;</td>
          <td width="75">&nbsp;</td>
          <td width="29">&nbsp;</td>
          <td width="21">&nbsp;</td>
          <td width="50">&nbsp;</td>
          <td width="25">&nbsp;</td>
          <td width="75">&nbsp;</td>
          <td width="37">&nbsp;</td>
          <td width="13">&nbsp;</td>
          <td width="50">&nbsp;</td>
          <td width="213">&nbsp;</td>
	    </tr>
	    <tr>
	      <td height="20" colspan="11" valign="middle" bgcolor="#000033" class="style2">&nbsp;Perfis que ser&atilde;o monitorados</td>
        </tr>
	    <tr>
	      <td colspan="4" rowspan="5" valign="middle">
            <select name="perfis" id="perfis" size="5" multiple style="width:140px;">
            </select>		</td>
          <td height="10"></td>
	      <td colspan="4" rowspan="5" valign="middle">
            <select name="sel_perfis" id="sel_perfis" size="5" multiple style="width:140px;">
            </select>		</td>
          <td></td>
	      <td></td>
        </tr>
	    
	    
	    
	    
	    <tr>
	      <td height="25" align="center" valign="middle">
          <input type="button" name="add" value=" >> " class="button" onClick="return listdouble('perfis','sel_perfis')"></td>
          <td>&nbsp;</td>
	      <td>&nbsp;</td>
        </tr>
	    <tr>
	      <td height="40">&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
        </tr>
	    
	    
	    <tr>
	      <td height="25" align="center" valign="middle">
          <input type="button" name="remove" value=" << " class="button" onClick="return listdouble('sel_perfis','perfis')"></td>
          <td></td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="10"></td>
	      <td></td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="10"></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="20" colspan="11" valign="middle" bgcolor="#000033">
		  <span class="style2">&nbsp;Configura&ccedil;&otilde;es para envio de notifica&ccedil;&atilde;o</span></td>
        </tr>
	    <tr>
	      <td height="5"></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
        </tr>
	    
	    <tr>
	      <td height="25" colspan="4" valign="middle"><strong>E-mail do remetente:</strong></td>
          <td colspan="3" valign="middle"><input type="text" id="email"></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td></td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="25" colspan="2" valign="middle"><strong>SMTP:</strong></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td colspan="3" valign="middle"><input type="text" id="smtp"></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td></td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="20">&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="25" align="left" valign="middle"><input type="checkbox" id="habilitar_sms" name="habilitar_sms"/></td>
          <td colspan="2" valign="middle">Habilitar SMS </td>
          <td>&nbsp;</td>
          <td></td>
	      <td align="left" valign="middle"><input type="checkbox" id="habilitar_email" name="habilitar_email"/></td>
	      <td colspan="2" valign="middle">Habilitar E-Mail </td>
	      <td>&nbsp;</td>
	      <td></td>
	      <td></td>
        </tr>
	    
	    <tr>
	      <td colspan="4" rowspan="2" valign="top">
		    <select name="telefones" id="telefones" size="5" multiple style="width:140px;">
                </select>		  </td>
	      <td height="25" valign="top">
	        <input type="button" value=" x " class="button" onClick="return del_telefone()">		  </td>
	      
		  <td colspan="4" rowspan="2" valign="top">
		    <select name="emails" id="emails" size="5" multiple style="width:140px;">
                </select>		  </td>
	      <td valign="top">
	        <input type="button" value=" x " class="button" onClick="return del_email()">		  </td>
	      <td></td>
        </tr>
	    
	    <tr>
	      <td height="65"></td>
	      <td>&nbsp;</td>
	      <td></td>
        </tr>
	    
	    <tr>
		<form name="ins_telefone_form" onSubmit="return add_telefone();">
	      <td height="25" colspan="4" valign="middle">
		    <input name="ins_telefone" type="text" id="ins_telefone" style="width:140px;">		  </td>
	      <td valign="middle">
		    <input type="submit" name="add2" value=" + " class="button" onClick="return add_telefone();">		  </td>
		</form>
		
		<form name="ins_email_form" onSubmit="return add_email();">
		<td colspan="4" valign="middle">
		    <input name="ins_email" type="text" id="ins_email" style="width:140px;">		  </td>
	      <td valign="middle">
		    <input type="submit" name="add3" value=" + " class="button" onClick="return add_email();">		  </td>
		<td></td>
		</form>
        </tr>
	    
	    <tr>
	      <td height="30">&nbsp;</td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="42" colspan="4" valign="top">
            <div id="botaoSalvar">
          <a href="javascript:salvar();"><img src="/PortalOsx/imagens/salvar.gif" alt="Salvar" border="0"/></a>		</div></td>
          <td></td>
	      <td colspan="5" valign="top"><div id="status"></div></td>
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
    <td height="110"></td>
    <td></td>
    <td></td>
  </tr>
</table>
</body>
</html>
