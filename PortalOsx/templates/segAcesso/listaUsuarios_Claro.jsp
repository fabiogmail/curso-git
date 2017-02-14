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
<style type="text/css">
div#tabListaUsuarios {
	overflow: auto;
	width: 755px;
	height: 400px;
	border-bottom: 1px solid #000000;
	border-left: 1px solid #000000;
	border-right: 1px solid #000000;
	background-color: #FFFFFF;	
}
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
</style>
<script type="text/javascript">
<!--
DWREngine.setErrorHandler(erro);
var regionais;
function erro(msg, ex) {
	$('status').innerHTML = '';
  	alert(msg);
}
function $(id) {
	return document.getElementById(id);
}
function init(){
	DWRUtil.removeAllOptions("regionais");
	DWRUtil.addOptions("regionais",[{ id:'', nome:'Carregando...' }],"id","nome");
	OpUsuariosAjax.iniciarRegionais(retIniciarRegionais);
}
function retIniciarRegionais(){
	OpUsuariosAjax.getListaRegionais(retRegionais);
}
function retRegionais(valor){
	regionais = valor;
	DWRUtil.removeAllOptions("regionais");
	DWRUtil.addOptions("regionais",[{ id:'', nome:' ' }],"id","nome");
	DWRUtil.addOptions("regionais", valor, "operadora", "operadora");	
}
function listar(){
	$('status').innerHTML = 'Carregando...';
	var login = $('login').value;
	var nome = $('nome').value;
	var regional = getSelecionado('regionais');
	var area = $('area').value;
	var perfil = $('perfil').value;
	//var acesso = $('acesso').value;
	var ativacao = getDatas();
	var ativacao_s = getSelecionado('ativacao_s');
	if(ativacao == null){
		return;
	}

	var filtros = '';
	if(login != '')	filtros += 'login:=:'+login+'\n';
	if(nome != '') filtros += 'nome:=:'+nome+'\n';
	if(regional != '') filtros += 'regional:=:'+regional+'\n';
	if(area != '') filtros += 'area:=:'+area+'\n';
	if(perfil != '') filtros += 'perfil:=:'+perfil+'\n';
	if(ativacao != '') filtros += 'ativacao:'+ativacao_s+':'+ativacao+'\n';
	
	OpUsuariosAjax.executarListaUsuarios(retUsuariosFiltros,filtros);
}

function retUsuariosFiltros(valor){
	$('status').innerHTML = '';
	if(valor){
		window.open("/PortalOsx/templates/segAcesso/relatorioUsuario.jsp","Relatorio",'resizable=false,status=yes,menubar=no,scrollbars=no,width=740,height=560');
	}else{
		$('status').innerHTML = 'N&atilde;o h&aacute; dados para os filtros selecionados';
	}	
}
function getDatas(){
	var mensagem = '';

	var dia = getSelecionadoText('dia');
	var mes = getSelecionado('mes');
	var ano = getSelecionadoText('ano');
	var hora = getSelecionadoText('hora');
	
	var data = '';

	
	if(ano != '' && mes != 0 && dia != '' && hora != '' )//todos preenchidos
	{
		data = dia+'/'+mes+'/'+ano+' '+hora+':00';
	}
	else if(!(ano == '' && mes == 0 && dia == '' && hora == ''))//nem todos não preenchidos é erro
	{
		mensagem = 'Devem ser preenchidos todos ou nenhum dos campos da Data de Ativação.\n';
	}
	
	if(mensagem != ''){
		alert(mensagem);
		return null;
	}
	return data;
}

function limpar(){
	$('status').innerHTML = '';
	selecionarComboText('dia','');
	selecionarCombo('mes','');
	selecionarComboText('ano','');
	selecionarComboText('hora','');
	$('login').value = '';
	$('nome').value = '';
	selecionarCombo('regionais','');
	$('area').value = '';
	$('perfil').value = '';
	selecionarCombo('ativacao_s','=');

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
function selecionarComboText(id,tipo){
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].text == tipo){
			$(id).options[i].selected = true;
		}else{
			$(id).options[i].selected = false;
		}
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
        <td width="15" height="364"></td>
        </tr>
    </table></td>
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/pesquisa_uso.gif" /></td>
  <td width="142" valign="top"><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" /></td>
    <td width="10" rowspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="10" height="364"></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td height="332" valign="top">
	
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
	    <!--DWLayoutTable-->
	    <tr>
	      <td width="100" height="20">&nbsp;</td>
          <td width="94">&nbsp;</td>
          <td width="116">&nbsp;</td>
          <td width="41">&nbsp;</td>
          <td width="45">&nbsp;</td>
          <td width="14">&nbsp;</td>
          <td width="61">&nbsp;</td>
          <td width="89">&nbsp;</td>
          <td width="53">&nbsp;</td>
        </tr>
	    <tr>
	      <td height="20" colspan="9" valign="middle" bgcolor="#000033"><span class="style1">&nbsp;Filtros</span></td>
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
        </tr>
	    <tr>
	      <td height="25" valign="middle"><strong>Login:</strong></td>
          <td colspan="2" valign="middle"><input type="text" id="login"></td>
          <td colspan="3" valign="middle"><strong>&Aacute;rea:</strong></td>
          <td colspan="2" valign="middle"><input type="text" id="area"></td>
          <td></td>
        </tr>
	    
	    <tr>
	      <td height="25" valign="middle"><strong>Nome:</strong></td>
          <td colspan="2" valign="middle"><input type="text" id="nome"></td>
          <td colspan="3" valign="middle"><strong>Perfil:</strong></td>
          <td colspan="2" valign="middle"><input type="text" id="perfil"></td>
          <td></td>
        </tr>
	    
	    <tr>
	      <td height="25" valign="middle"><strong>Regional:</strong></td>
          <td colspan="2" valign="middle">
		  <SELECT id="regionais" SIZE="1">
	      <OPTION value="">Carregando...</option>
	      </SELECT>
          </td>
          <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
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
	    </tr>
	    <tr>
	      <td height="20" colspan="9" valign="middle" bgcolor="#000033"><span class="style1">&nbsp;Data de Ativa&ccedil;&atilde;o </span></td>
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
        </tr>
	    <tr>
	      <td height="25" valign="middle"><strong>Data: </strong></td>
          <td colspan="3" valign="middle">
		  <select name="dia" class="selectBoxNormal1" id="dia">
              <option></option>
              <option>01</option>
              <option>02</option>
              <option>03</option>
              <option>04</option>
              <option>05</option>
              <option>06</option>
              <option>07</option>
              <option>08</option>
              <option>09</option>
              <option>10</option>
              <option>11</option>
              <option>12</option>
              <option>13</option>
              <option>14</option>
              <option>15</option>
              <option>16</option>
              <option>17</option>
              <option>18</option>
              <option>19</option>
              <option>20</option>
              <option>21</option>
              <option>22</option>
              <option>23</option>
              <option>24</option>
              <option>25</option>
              <option>26</option>
              <option>27</option>
              <option>28</option>
              <option>29</option>
              <option>30</option>
              <option>31</option>
            </select>
            /
            <select name="mes" class="selectBoxNormal1" id="mes">
              <option value=""></option>
              <option value="01">Janeiro</option>
              <option value="02">Fevereiro</option>
              <option value="03">Mar&ccedil;o</option>
              <option value="04">Abril</option>
              <option value="05">Maio</option>
              <option value="06">Junho</option>
              <option value="07">Julho</option>
              <option value="08">Agosto</option>
              <option value="09">Setembro</option>
              <option value="10">Outubro</option>
              <option value="11">Novembro</option>
              <option value="12">Dezembro</option>
            </select>
            /
            <select name="ano" class="selectBoxNormal1" id="ano">
              <option></option>
              <option>2007</option>
              <option>2008</option>
              <option>2009</option>
              <option>2010</option>
			  <option>2011</option>
              <option>2012</option>
              <option>2013</option>
              <option>2014</option>
              <option>2015</option>
              <option>2016</option>
            </select>		  </td>
          <td valign="middle"><strong>Hora:</strong></td>
          <td colspan="2" valign="middle">
		  <select name="hora" class="selectBoxNormal1" id="hora">
              <option></option>
              <option>00:00</option>
              <option>01:00</option>
              <option>02:00</option>
              <option>03:00</option>
              <option>04:00</option>
              <option>05:00</option>
              <option>06:00</option>
              <option>07:00</option>
              <option>08:00</option>
              <option>09:00</option>
              <option>10:00</option>
              <option>11:00</option>
              <option>12:00</option>
              <option>13:00</option>
              <option>14:00</option>
              <option>15:00</option>
              <option>16:00</option>
              <option>17:00</option>
              <option>18:00</option>
              <option>19:00</option>
              <option>20:00</option>
              <option>21:00</option>
              <option>22:00</option>
              <option>23:00</option>
          </select>		  </td>
          <td>&nbsp;</td>
          <td></td>
        </tr>
	    
	    <tr>
	      <td height="25" valign="middle"><strong>Comparador:</strong></td>
	      <td align="left" valign="middle">
            <select id="ativacao_s">
              <option value="=">=</option>
              <option value="<">< </option>
              <option value=">">></option>
              <option value=">=">>=</option>
              <option value="<="><=</option>
                            </select></td>
          <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td></td>
        </tr>
	    <tr>
	      <td height="24"></td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td>&nbsp;</td>
	      <td></td>
        </tr>
	    
	    
	    <tr>
	      <td height="32" valign="middle">
	        <div id="botaoEnviar">
          <a href="javascript:listar();"><img src="/PortalOsx/imagens/listar.gif" alt="Listar" border="0"/></a>		</div></td>
          <td valign="middle">
		  <a href="javascript:limpar();" >
		  <img src="/PortalOsx/imagens/limpar.gif" border="0" align="middle" alt="Limpar" /></a></td>
          <td>&nbsp;</td>
          <td colspan="5" valign="middle"><div id="status"></div></td>
          <td></td>
        </tr>
	    <tr>
	      <td height="61">&nbsp;</td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
	      <td></td>
        </tr>
      </table></td>
    <td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="142" height="332" valign="top">Dicas...</td>
        </tr>
      
      
    </table></td>
  </tr>
  
  <tr>
    <td height="19">&nbsp;</td>
    <td>&nbsp;</td>
    <td></td>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>
