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
var usuarios;
var perfis;
var regionais;
function $(id) {
	return document.getElementById(id);
}
function init(){
	DWRUtil.removeAllOptions("usuarios");
	DWRUtil.removeAllOptions("perfis");
	DWRUtil.removeAllOptions("regionais");
	DWRUtil.addOptions("usuarios",[{ id:'', nome:'Carregando...' }],"id","nome");
	DWRUtil.addOptions("perfis",[{ id:'', nome:'Carregando...' }],"id","nome");
	DWRUtil.addOptions("regionais",[{ id:'', nome:'Carregando...' }],"id","nome");
	
	OpUsuariosAjax.iniciar(retIniciar);	
}
function retIniciar(){
	OpUsuariosAjax.getListaUsuarios(retUsuarios);
	OpUsuariosAjax.getListaPerfis(retPerfis);
	OpUsuariosAjax.getListaRegionais(retRegionais);
}
function retUsuarios(valor){
	usuarios = valor;
	DWRUtil.removeAllOptions("usuarios");
	DWRUtil.addOptions("usuarios",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("usuarios", valor, "usuario", "usuario");
}
function retPerfis(valor){
	perfis = valor;
	DWRUtil.removeAllOptions("perfis");
	DWRUtil.addOptions("perfis",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("perfis", valor, "perfil", "perfil");	
}
function retRegionais(valor){
	regionais = valor;
	DWRUtil.removeAllOptions("regionais");
	DWRUtil.addOptions("regionais",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("regionais", valor, "operadora", "operadora");	
}
function loadUsuario(){
	var usuario = getSelecionado('usuarios');
	var obUsuario = null;
	for(var i = 0; i < usuarios.length; i++){
		if(usuarios[i].usuario == usuario){
			obUsuario = usuarios[i];
		}
	}
	selecionarCombo('perfis',obUsuario.perfil);
	selecionarCombo('regionais',obUsuario.regional);
	$('usuario').value = obUsuario.usuario;
	$('senha').value = obUsuario.senha;
	$('nome').value = obUsuario.nome;
	$('ramal').value = obUsuario.ramal;
	$('telefone').value = obUsuario.telefone;
	$('email').value = obUsuario.email;
	$('area').value = obUsuario.area;
	$('responsavel').value = obUsuario.responsavel;
	$('motivo').value = obUsuario.motivo;
	$('ativacao').value = obUsuario.ativacao;
	$('ordem').value = obUsuario.ordem;
}

function salvar(){
	var usuario = getSelecionado('usuarios');
	var login = $('usuario').value;
	var senha = $('senha').value;
	var perfil = getSelecionado('perfis');
	var nome = $('nome').value;
	var ramal = $('ramal').value;
	var telefone = $('telefone').value;
	var email = $('email').value;
	var area = $('area').value;
	var regional = getSelecionado('regionais');
	var responsavel = $('responsavel').value;
	var motivo = $('motivo').value;
	var ativacao = $('ativacao').value;
	var ordem = $('ordem').value;
	var novo = false;

	login = getSelecionadoText('usuarios');
	if(perfil == ''){
		$('perfis').focus();
		alert('Selecione o perfil do usuario');
		return;
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
	ocultar('botaoSalvar');
	$('status').innerHTML = 'Salvando...';
	OpUsuariosAjax.salvar(retsalvar,login,senha,perfil,nome,email,ramal,telefone,regional,area,responsavel,motivo,ativacao,ordem,novo);
}
function retsalvar(valor){
	$('status').innerHTML = '';
	mostrar('botaoSalvar');
	if(valor == 0){
		alert('Usuario salvo com sucesso!');
		limpar();
		init();
	}else{
		alert('Problema ao salvar o usuario');
	}
}
function excluir(){
	var usuario = getSelecionadoText('usuarios');
	if(confirm('Deseja excluir o usuario '+usuario)){
		$('status').innerHTML = 'Excluindo...';
		ocultar('botaoExcluir');
		
		if(usuario == ''){
			$('usuarios').focus();
			alert('Selecione o usuario');
			return;
		}
		OpUsuariosAjax.excluir(retexcluir,usuario);
	}	
}
function retexcluir(valor){
	$('status').innerHTML = '';
	mostrar('botaoExcluir');
	if(valor){
		alert('Usuario excluido com sucesso!');
		limpar();
		init();
	}else{
		alert('Problema ao excluir o usuario');
	}
}
function limpar(){
	$('nome').value = '';
	$('ramal').value = '';
	$('telefone').value = '';
	$('email').value = '';
	$('area').value = '';
	$('responsavel').value = '';
	$('motivo').value = '';
	$('ordem').value = '';
	selecionarCombo('usuarios','');
	selecionarCombo('perfis','');
	selecionarCombo('regionais','');
}
function redefinirSenha(){
	var login = getSelecionadoText('usuarios');
	if(login == ''){
		$('usuarios').focus();
		alert('Selecione o usuario para restaurar sua senha');
		return;
	}
	
	if(confirm('Deseja restaurar a senha do usuario: '+login+' para senha padrao')){
		OpUsuariosAjax.reiniciaSenha(retredefinirsenha,login);
	}
}
function retredefinirsenha(retorno){
	if(retorno){
		alert('Senha padrao do usuario restaurada!');
		limpar();
		init();
	}else{
		alert('Problema ao restaurar a senha padrao do usuario');
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
        <td width="15" height="358"></td>
        </tr>
    </table></td>
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/alteracao_uso.gif" /></td>
  <td width="142" valign="top"><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" /></td>
    <td width="10" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="10" height="358"></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td height="326" valign="top">
	
	  <div id="pagina">
		<input type="hidden" name="usuario" id="usuario">
	    <input type="hidden" name="senha" id="senha">
		<input type="hidden" name="ativacao" id="ativacao">
	    <table width="100%" border="0" cellpadding="0" cellspacing="0">
	      <!--DWLayoutTable-->
	      <tr>
	        <td height="25" colspan="2" align="left" valign="middle"><strong>Usu&aacute;rios:</strong></td>
          <td colspan="3" align="left" valign="middle">
            <SELECT id="usuarios" SIZE="1" onChange="loadUsuario();">
              <OPTION value="">Carregando...</option>
            </SELECT>		</td>
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
          <td width="70">&nbsp;</td>
          <td width="40">&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          </tr>
	      <tr>
	        <td height="25" colspan="2" valign="middle"><strong>Nome:</strong></td>
          <td colspan="3" valign="middle"><input type="text" name="nome" id="nome"></td>
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
          <td>&nbsp;</td>
          </tr>
	      <tr>
	        <td height="25" colspan="2" valign="middle"><strong>Telefone:</strong></td>
          <td colspan="3" align="left" valign="middle"><input type="text" name="telefone" id="telefone"></td>
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
          <td>&nbsp;</td>
          </tr>
	      <tr>
	        <td height="25" colspan="2" valign="middle"><strong>Regional:</strong></td>
          <td colspan="3" valign="middle">
		  <SELECT id="regionais" SIZE="1">
              <OPTION value="">Carregando...</option>
          </SELECT>
		  </td>
          <td>&nbsp;</td>
          <td valign="middle"><strong>&Aacute;rea:</strong></td>
          <td valign="middle"><input type="text" name="area" id="area"></td>
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
          <td>&nbsp;</td>
          </tr>
	      <tr>
	        <td height="25" colspan="2" valign="middle"><strong>Motivo:</strong></td>
          <td colspan="3" valign="middle"><input type="text" name="motivo" id="motivo"></td>
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
          <td>&nbsp;</td>
          </tr>
          <tr>
        	<td height="25" colspan="2" valign="middle"><strong>Ordem de ServiÃ§o:</strong></td>
        	<td colspan="2" valign="middle"><input type="text" name="ordem" id="ordem"></td>
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
          <td>&nbsp;</td>
        </tr>
	      <tr>
	        <td height="42" valign="top">
              <div id="botaoSalvar">
            <a href="javascript:salvar();"><img src="/PortalOsx/imagens/salvar.gif" alt="Salvar" border="0"/></a>		</div></td>
          <td colspan="2" valign="top">
            <div id="botaoExcluir">
            <a href="javascript:excluir();"><img src="/PortalOsx/imagens/excluir.gif" alt="Excluir" border="0"/></a>		</div></td>
          <td valign="top"><a href="javascript:limpar();"><img src="/PortalOsx/imagens/limpar.gif" alt="Limpar Campos" border="0"/></a></td>
          <td valign="top"><a href="javascript:redefinirSenha();"><img src="/PortalOsx/imagens/restaurarsenha.gif" alt="Restaurar senha padrão" border="0"/></a></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td valign="top"><div id="status"></div></td>
          <td>&nbsp;</td>
        </tr>
      </table>   
      </div>	</td>
    <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="142" height="326" valign="top">Dicas...</td>
        </tr>
      
    </table></td>
  </tr>
  
  <tr>
    <td height="31">&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>

</body>
</html>