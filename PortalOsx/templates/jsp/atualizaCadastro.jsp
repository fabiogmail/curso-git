<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%
	String usuario = (String)request.getSession().getAttribute("usuario_web");
	String senha = (String)request.getSession().getAttribute("senha_web");
	String perfil = (String)request.getSession().getAttribute("perfil_web");
	String ver_senha = (String)request.getSession().getAttribute("ver_senha");
%>
<html>
<head>
<title>Atualizar Cadastro</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpUsuariosAjax.js'></script>
<script type="text/javascript">
<!--
var usuario = '<%=usuario%>';
var senha = '<%=senha%>';
var perfil = '<%=perfil%>';
var ver_senha = '<%=ver_senha%>'

function $(id) {
	return document.getElementById(id);
}
function init(){
	if(ver_senha == '0'){
		ocultar('ver_senha');
		$('senha_atual').value = senha ;
		$('senha').value = senha;
		$('senha_conf').value = senha;
	}
	OpUsuariosAjax.iniciar(retIniciar);	
}
function retIniciar(){
	OpUsuariosAjax.getListaUsuarios(retListaUsuarios);
}
function retListaUsuarios(valor){
	for(var i = 0;i < valor.length;i++){
		if(valor[i].usuario == usuario){
			loadUsuario(valor[i]);
			break;
		}
	}
}

function loadUsuario(obUsuario){
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
function ehVazio(id,nome){
	if($(id).value == ''){
		alert('Campo '+nome+' em Branco');
		$(id).focus();
		return true;
	}else{
		return false;
	}
}
function salvar(){
	if(ehVazio('senha_atual','Senha Atual')){ return;}
	if(ehVazio('senha','Nova Senha')){ return;}
	if(ehVazio('senha_conf','Confirmar Senha')){ return;}
	if(ehVazio('nome','Nome')){ return;}
	if(ehVazio('ramal','Ramal')){ return;}
	if(ehVazio('telefone','Telefone')){ return;}
	if(ehVazio('email','E-Mail')){ return;}
	if(ehVazio('area','Area')){ return;}
	if(ehVazio('regional','Regional')){ return;}
	if(ehVazio('responsavel','Responsavel')){ return;}
	if(ehVazio('motivo','Motivo')){ return;}
	if(ehVazio('ativacao','Ativacao')){ return;}
	if(ehVazio('expiracao','Expiração')){ return;}
		
	if(senha != $('senha_atual').value){
		alert('Senha Atual esta incorreta!');
		$('senha_atual').focus();
		return;
	}else{
		if($('senha').value != $('senha_conf').value){
			alert('A Confirmação deve ser igual a senha Nova!');
			$('senha_conf').focus();
			return;
		}else{
			var expressao = new RegExp("[^A-Za-z0-9._-]", "gi");
			ret = $('senha').value.search(expressao);
			if(ret != -1){
				alert("A senha somente pode conter caracteres alfanumericos alem de '\.\', \'_\' e \'-\'!");
				$('senha').focus();	
				return;	
			}
		}
	}
	
	var novaSenha = $('senha').value;
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
	var novo = false;
	
	ocultar('botaoSalvar');
	$('status').innerHTML = 'Salvando...';
	OpUsuariosAjax.salvar(retsalvar,usuario,novaSenha,perfil,nome,email,ramal,telefone,regional,area,responsavel,motivo,ativacao,expiracao,novo);
}
function retsalvar(valor){
	$('status').innerHTML = '';
	mostrar('botaoSalvar');
	if(valor){
		//alert('Usuario salvo com sucesso!');
		$('redirect').submit();
	}else{
		alert('Problema ao salvar os dados, consulte o adminitrador do sistema');
		
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
	<form name="form" id="redirect" method="post" action="/PortalOsx/templates/paginas/index.jsp">
	</form>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td height="50" colspan="6" valign="top">
		<div id="ver_senha">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <!--DWLayoutTable-->
          <tr>
            <td width="100" height="25" valign="middle"><strong>Senha Atual: </strong></td>
        <td width="150" valign="middle"><input type="password" name="senha_atual" id="senha_atual"></td>
        <td width="42">&nbsp;</td>
          <td width="118" valign="middle"><strong>Nova Senha:</strong></td>
        <td width="150" valign="middle"><input type="password" name="senha" id="senha"></td>
        </tr>
          <tr>
            <td height="25">&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td valign="middle"><strong>Confirmar Senha:</strong> </td>
        <td valign="middle"><input type="password" name="senha_conf" id="senha_conf"></td>
        </tr>
        </table>
		</div>
		
        </td>
        <td width="53"></td>
      </tr>
      <tr>
        <td width="70" height="20">&nbsp;</td>
        <td width="30">&nbsp;</td>
        <td width="150">&nbsp;</td>
        <td width="60">&nbsp;</td>
        <td width="100">&nbsp;</td>
        <td width="150">&nbsp;</td>
        <td></td>
      </tr>
      
      <tr>
        <td height="25" colspan="6" valign="middle"><strong>Atualiza&ccedil;&atilde;o dos dados cadastrais: </strong></td>
        <td></td>
      </tr>
      
      <tr>
        <td height="19">&nbsp;</td>
        <td>&nbsp;</td>
        <td></td>
        <td></td>
        <td>&nbsp;</td>
        <td></td>
        <td></td>
      </tr>
      
      
      
      
      
      
      
      
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Nome:</strong></td>
        <td valign="middle"><input type="text" name="nome" id="nome"></td>
        <td></td>
        <td valign="middle"><strong>Email:</strong></td>
        <td valign="middle"><input type="text" name="email" id="email"></td>
        <td></td>
      </tr>
      
      <tr>
        <td height="19">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Ramal:</strong></td>
        <td valign="middle"><input type="text" name="ramal" id="ramal"></td>
        <td></td>
        <td valign="middle"><strong>Telefone:</strong></td>
        <td align="left" valign="middle"><input type="text" name="telefone" id="telefone"></td>
        <td></td>
      </tr>
      
      <tr>
        <td height="19">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>&Aacute;rea:</strong></td>
        <td valign="middle"><input type="text" name="area" id="area"></td>
        <td></td>
        <td valign="middle"><strong>Regional:</strong></td>
        <td valign="middle"><input type="text" name="regional" id="regional"></td>
        <td></td>
      </tr>
      
      <tr>
        <td height="19">&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Respons&aacute;vel:</strong></td>
        <td valign="middle"><input type="text" name="responsavel" id="responsavel"></td>
        <td></td>
        <td>&nbsp;</td>
        <td valign="middle"><input type="hidden" name="motivo" id="motivo"></td>
        <td></td>
      </tr>
      
      <tr>
        <td height="19">&nbsp;</td>
        <td>&nbsp;</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      
      
      
      <tr>
        <td height="25">&nbsp;</td>
        <td>&nbsp;</td>
        <td valign="middle"><input type="hidden" name="ativacao" id="ativacao"></td>
        <td></td>
        <td>&nbsp;</td>
        <td valign="middle"><input type="hidden" name="expiracao" id="expiracao"></td>
        <td></td>
      </tr>
      
      <tr>
        <td height="42" valign="bottom">
		  <div id="botaoSalvar">
		    <a href="javascript:salvar();"><img src="/PortalOsx/imagens/salvar.gif" alt="Salvar" border="0"/></a>		</div></td>
        <td></td>
        <td>&nbsp;</td>
        <td></td>
        <td></td>
        <td valign="top"><div id="status"></div></td>
        <td></td>
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