<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<html>
<head>
<title>Usuários</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpUsuariosAjax.js'></script>
<script type="text/javascript">
<!--
DWREngine.setErrorHandler(erro);
function erro(msg, ex) {
	mostrar('botaoSalvar');
	$('status').innerHTML = msg;
}
var perfis;
function $(id) {
	return document.getElementById(id);
}
function init(){
	DWRUtil.removeAllOptions("perfis");
	DWRUtil.addOptions("perfis",[{ id:'', nome:'Carregando...' }],"id","nome");
	OpUsuariosAjax.iniciarPerfis(retIniciarPerfis);
}
function retIniciarPerfis(){
	OpUsuariosAjax.getListaPerfis(retPerfis);
}
function retPerfis(valor){
	perfis = valor;
	DWRUtil.removeAllOptions("perfis");
	DWRUtil.addOptions("perfis",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("perfis", valor, "perfil", "perfil");	
}

function salvar(){
	var login = $('usuario').value;
	var senha = $('senha').value;
	var perfil = getSelecionado('perfis');
	var nome = $('nome').value;
	var ramal = $('ramal').value;
	var telefone = $('telefone').value;
	var email = $('email').value;
	var area = $('area').value;
	var responsavel = $('responsavel').value;
	var motivo = $('motivo').value;
	var ativacao = $('ativacao').value;

	var novo = true;	
	if(login == ''){
		$('usuario').focus();
		alert('Nome do usuario em branco');
		return;
	}	
	if(perfil == ''){
		$('perfis').focus();
		alert('Selecione o perfil do usuario');
		return;
	}
	else if (perfil == "admin")
	{
		alert ("Não se pode associar um outro usuário ao perfil \"admin\"!");
		return;
	}
	
	if(login.length < 3){
		$('usuario').focus();
		alert('O login deve ter no minimo 3 caracteres');
		return
	}
	if(telefone != ''){
		if(!ehNumero(telefone)){
			$('telefone').focus();
			alert('O Campo Telefone aceita somente numeros');
			return
		}
	}
	if(email != ''){
		if(!verificaEmail(email)){  
			$('email').focus();
			alert('Email invalido');
			return
		}		
	}
	
	senha = login;
	ocultar('botaoSalvar');
	$('status').innerHTML = 'Salvando...';
	OpUsuariosAjax.salvar(retsalvar,login,senha,perfil,nome,email,ramal,telefone,'',area,responsavel,motivo,ativacao,'',novo);
}
function retsalvar(valor){
	$('status').innerHTML = '';
	mostrar('botaoSalvar');
	if(valor == 0){
		alert('Usuario salvo com sucesso!');
		limpar();
		init();
	}else if(valor == 1){
		alert('Problema ao salvar o usuario');
	}else if(valor == 2){
		alert('Este usuario ja existe!');
	}
}

function limpar(){
	$('usuario').value = '';
	$('nome').value = '';
	$('ramal').value = '';
	$('telefone').value = '';
	$('email').value = '';
	$('area').value = '';
	$('responsavel').value = '';
	$('motivo').value = '';
	selecionarCombo('perfis','');
}

function mostrar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="block";
}

function ocultar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="none";
}
function getSelecionado(id){
	var valor;
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].selected){
			valor = $(id).options[i].value;
		}
	}
	return valor;
}
function getSelecionadoText(id){
	var valor;
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].selected){
			valor = $(id).options[i].text;
		}
	}
	return valor;
}
function selecionarCombo(id,tipo){
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].value == tipo){
			$(id).options[i].selected = true;
		}else{
			$(id).options[i].selected = false;
		}
	}
}

function ehNumero(valor)
{
	var expressao = /^\d+$/;
	if (expressao.test(valor)) {
		return true;
	} else if (valor != null && valor != "") {
		return false;
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
<table width="780" height="383" border="0" cellspacing="0" cellpadding="0" background="/PortalOsx/imagens/fundo.gif">
  <!--DWLayoutTable-->
  <tr>
    <td width="15" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="15" height="389"></td>
        </tr>
    </table></td>
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/inclusao_uso.gif" /></td>
  <td width="142" valign="top"><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" /></td>
    <td width="10" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="10" height="389"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="357" valign="top">
	
	<div id="pagina">
	<input type="hidden" name="senha" id="senha">
	<input type="hidden" name="ativacao" id="ativacao">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Novo Usu&aacute;rio: </strong></td>
        <td colspan="2" valign="middle"><input type="text" name="usuario" id="usuario"></td>
        <td width="60">&nbsp;</td>
      <td width="100" align="left" valign="middle"><strong>Perfis:</strong></td>
      <td width="150" align="left" valign="middle">
	    <SELECT id="perfis" SIZE="1">
	      <OPTION value="">Carregando...</option>
	      </SELECT>	  </td>
      <td width="53">&nbsp;</td>
        </tr>
      
      <tr>
        <td width="70" height="40">&nbsp;</td>
        <td width="30">&nbsp;</td>
        <td width="40">&nbsp;</td>
        <td width="110">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        </tr>
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Nome:</strong></td>
        <td colspan="2" valign="middle"><input type="text" name="nome" id="nome"></td>
        <td>&nbsp;</td>
        <td valign="middle"><strong>Email:</strong></td>
        <td valign="middle"><input type="text" name="email" id="email"></td>
        <td>&nbsp;</td>
      </tr>
      
      <tr>
        <td height="20">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        </tr>
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Telefone:</strong></td>
        <td colspan="2" align="left" valign="middle"><input type="text" name="telefone" id="telefone"></td>
        <td>&nbsp;</td>
        <td valign="middle"><strong>Ramal:</strong></td>
        <td valign="middle"><input type="text" name="ramal" id="ramal"></td>
        <td>&nbsp;</td>
      </tr>
      
      
      <tr>
        <td height="20">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        </tr>
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>&Aacute;rea:</strong></td>
        <td colspan="2" valign="middle"><input type="text" name="area" id="area"></td>
        <td>&nbsp;</td>
        <td valign="middle"><strong>Respons&aacute;vel:</strong></td>
        <td valign="middle"><input type="text" name="responsavel" id="responsavel"></td>
        <td>&nbsp;</td>
      </tr>
      
      
      
      <tr>
        <td height="20">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        </tr>
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Motivo:</strong></td>
        <td colspan="2" valign="middle"><input type="text" name="motivo" id="motivo"></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
      
      <tr>
        <td height="30">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        </tr>
      <tr>
        <td height="20" colspan="2" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="42" valign="top">
		  <div id="botaoSalvar">
		    <a href="javascript:salvar();"><img src="/PortalOsx/imagens/salvar.gif" alt="Salvar" border="0"/></a>		</div></td>
        <td colspan="2" valign="top"><a href="javascript:limpar();"><img src="/PortalOsx/imagens/limpar.gif" alt="Limpar Campos" border="0"/></a></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td valign="top"><div id="status"></div></td>
        <td>&nbsp;</td>
      </tr>
    </table>   
	</div>
	
	
	
	
	
	 </td>
    <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="142" height="357" valign="top">Dicas...</td>
      </tr>
    </table>    </td>
  </tr>
</table>

</body>
</html>