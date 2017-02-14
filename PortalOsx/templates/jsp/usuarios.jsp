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
var usuarios;
var perfis;
function $(id) {
	return document.getElementById(id);
}
function init(){
	DWRUtil.removeAllOptions("usuarios");
	DWRUtil.removeAllOptions("perfis");
	DWRUtil.addOptions("usuarios",[{ id:'', nome:'Carregando...' }],"id","nome");
	DWRUtil.addOptions("perfis",[{ id:'', nome:'Carregando...' }],"id","nome");
	$('usuario').style.backgroundColor = "#FFFFFF"
	$('usuario').disabled = false;
	OpUsuariosAjax.iniciar(retIniciar);	
}
function retIniciar(){
	OpUsuariosAjax.getListaUsuarios(retUsuarios);
	OpUsuariosAjax.getListaPerfis(retPerfis);
}
function retUsuarios(valor){
	usuarios = valor;
	DWRUtil.removeAllOptions("usuarios");
	DWRUtil.addOptions("usuarios",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("usuarios", valor, "idUsuario", "usuario");
}
function retPerfis(valor){
	perfis = valor;
	DWRUtil.removeAllOptions("perfis");
	DWRUtil.addOptions("perfis",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("perfis", valor, "id", "perfil");
	
}
function loadUsuario(){
	var usuario = getSelecionadoText('usuarios');
	var idUsuario =	getSelecionado('usuarios');
	var obUsuario = null;
	for(var i = 0; i < usuarios.length; i++){
		if(usuarios[i].idUsuario == idUsuario){
			obUsuario = usuarios[i];
			selecionarCombo('perfis',usuarios[i].idPerfil)
		}
	}
	if(idUsuario == ''){
		$('usuario').style.backgroundColor = "#FFFFFF"
		$('usuario').disabled = false;
	}else{
		$('usuario').style.backgroundColor = "#E6E6E6"
		$('usuario').disabled = true;
		$('usuario').value = obUsuario.usuario;
		$('senha').value = obUsuario.senha;
		$('nome').value = obUsuario.nome;
		$('ramal').value = obUsuario.ramal;
		$('telefone').value = obUsuario.telefone;
		$('email').value = obUsuario.email;
		$('area').value = obUsuario.area;
		$('regional').value = obUsuario.regional;
		$('responsavel').value = obUsuario.responsavel;
		$('motivo').value = obUsuario.motivo;
		$('ativacao').value = obUsuario.ativacao;
		$('expiracao').value = obUsuario.expiracao;
	}
}

function salvar(){
	var usuario = getSelecionadoText('usuarios');
	var idUsuario = getSelecionado('usuarios');
	var login = $('usuario').value;
	var senha = $('senha').value;
	var perfil = getSelecionadoText('perfis');
	var idPerfil = getSelecionado('perfis');
	var nome = $('nome').value;
	var ramal = $('ramal').value;
	var telefone = $('telefone').value;
	var email = $('email').value;
	var area = $('area').value;
	var regional = $('regional').value;
	var responsavel = $('responsavel').value;
	var motivo = $('motivo').value;
	var ativacao = $('ativacao').value;
	var expiracao = $('expiracao').value;
	var novo = true;
	
	if(idUsuario == ''){
		novo = true;
		if(login == ''){
			$('usuario').focus();
			alert('Nome do usuario em branco');
			return;
		}	
		if(idPerfil == ''){
			$('perfis').focus();
			alert('Selecione o perfil do usuario');
			return;
		}
		if(login.length < 3){
			$('usuario').focus();
			alert('O login deve ter no minimo 3 caracteres');
			return
		}
	}else{
		novo = false;
		login = getSelecionadoText('usuarios');
		if(idPerfil == ''){
			$('perfis').focus();
			alert('Selecione o perfil do usuario');
			return;
		}
	}	
	
	if(novo){
		senha = login;
	}
	ocultar('botaoSalvar');
	$('status').innerHTML = 'Salvando...';
	OpUsuariosAjax.salvar(retsalvar,login,senha,perfil,nome,email,ramal,telefone,regional,area,responsavel,motivo,ativacao,expiracao,novo);
}
function retsalvar(valor){
	$('status').innerHTML = '';
	mostrar('botaoSalvar');
	if(valor){
		alert('Usuario salvo com sucesso!');
		limpar();
		init();
	}else{
		alert('Problema ao salvar o usuario');
	}
}
function excluir(){
	$('status').innerHTML = 'Excluindo...';
	ocultar('botaoExcluir');
	var usuario = getSelecionadoText('usuarios');
	if(usuario == ''){
		$('usuarios').focus();
		alert('Selecione o usuario');
		return;
	}
	OpUsuariosAjax.excluir(retexcluir,usuario);
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
	$('usuario').value = '';
	$('nome').value = '';
	$('ramal').value = '';
	$('telefone').value = '';
	$('email').value = '';
	$('area').value = '';
	$('regional').value = '';
	$('responsavel').value = '';
	$('motivo').value = '';
	$('ativacao').value = '';
	$('expiracao').value = '';
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
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/usuarios.gif" /></td>
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
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td height="25" colspan="2" align="left" valign="middle"><strong>Usu&aacute;rios:</strong></td>
        <td colspan="3" align="left" valign="middle">
	      <SELECT id="usuarios" SIZE="1" onChange="loadUsuario();">
	      <OPTION value="">Carregando...</option>
          </SELECT>		</td>
    <td width="60">&nbsp;</td>
      <td width="100" valign="middle"><strong>Novo Usu&aacute;rio: </strong></td>
        <td valign="middle"><input type="text" name="usuario" id="usuario"></td>
        <td width="53" valign="middle">(login)</td>
        </tr>
      
      <tr>
        <td width="70" height="20">&nbsp;</td>
        <td width="30">&nbsp;</td>
        <td width="40">&nbsp;</td>
        <td width="70">&nbsp;</td>
        <td width="40">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td width="150">&nbsp;</td>
        <td>&nbsp;</td>
        </tr>
      <tr>
        <td height="25" colspan="2" align="left" valign="middle"><strong>Perfis:</strong></td>
      <td colspan="3" align="left" valign="middle">
	    <SELECT id="perfis" SIZE="1">
	      <OPTION value="">Carregando...</option>
	      </SELECT>	  </td>
      <td>&nbsp;</td>
        <td>&nbsp;</td>
      <td>&nbsp;</td>
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
        <td height="25" colspan="2" valign="middle"><strong>Ramal:</strong></td>
        <td colspan="3" valign="middle"><input type="text" name="ramal" id="ramal"></td>
        <td>&nbsp;</td>
        <td valign="middle"><strong>Telefone:</strong></td>
        <td align="left" valign="middle"><input type="text" name="telefone" id="telefone"></td>
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
        <td height="25" colspan="2" valign="middle"><strong>&Aacute;rea:</strong></td>
        <td colspan="3" valign="middle"><input type="text" name="area" id="area"></td>
        <td>&nbsp;</td>
        <td valign="middle"><strong>Regional:</strong></td>
        <td valign="middle"><input type="text" name="regional" id="regional"></td>
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
        <td height="25" colspan="2" valign="middle"><strong>Dt. Ativa&ccedil;&atilde;o: </strong></td>
        <td colspan="3" valign="middle"><input type="text" name="ativacao" id="ativacao"></td>
        <td>&nbsp;</td>
        <td valign="middle"><strong>Dt. Expira&ccedil;&atilde;o:</strong></td>
        <td valign="middle"><input type="text" name="expiracao" id="expiracao"></td>
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
		<a href="javascript:salvar();"><img src="/PortalOsx/imagens/salvar.gif" alt="Salvar" border="0"/></a>		</div>		</td>
        <td colspan="2" valign="top">
		  <div id="botaoExcluir">
		    <a href="javascript:excluir();"><img src="/PortalOsx/imagens/excluir.gif" alt="Excluir" border="0"/></a>		</div></td>
        <td valign="top"><a href="javascript:limpar();"><img src="/PortalOsx/imagens/limpar.gif" alt="Limpar Campos" border="0"/></a></td>
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