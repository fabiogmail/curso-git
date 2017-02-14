<head>

<title>Relat&oacute;rio de Uso</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!--link href="det2007.css" rel="stylesheet" type="text/css"-->
<link rel="stylesheet" href="/PortalOsx/css/style.css" type="text/css">
<script type='text/javascript' src='/PortalOsx/templates/segAcesso/det2007.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/RelatorioAjax.js'></script>
<script type="text/javascript">
function init(){
	$('radio_rel_uso').checked = true;
	$('radio_est_uso').checked = false;
}
function limparSelecao(){

    document.getElementById("usuario").value = "";
	document.getElementById("dia_inicio").options[0].selected = true;
	document.getElementById("mes_inicio").options[0].selected = true;
	document.getElementById("ano_inicio").options[0].selected = true;
	document.getElementById("periodo_inicio").options[0].selected = true;
	
	document.getElementById("dia_fim").options[0].selected = true;
	document.getElementById("mes_fim").options[0].selected = true;
	document.getElementById("ano_fim").options[0].selected = true;
	document.getElementById("periodo_fim").options[0].selected = true;

	document.getElementById("qtdDias").value = "";

    habilitar("qtdDias");
}

function desabilitaCampoDias()
{
	if(getSelecionadoText("dia_inicio")=="" &&
	   getSelecionadoText("mes_inicio")=="" &&
	   getSelecionadoText("ano_inicio")=="" &&
	   getSelecionadoText("periodo_inicio")=="" &&
	   getSelecionadoText("dia_fim")=="" &&
	   getSelecionadoText("mes_fim")=="" &&
	   getSelecionadoText("ano_fim")=="" &&
	   getSelecionadoText("periodo_fim")== "")
	 {
	 	habilitar("qtdDias");
	 }
	 else
	 {
	    desabilitar('qtdDias');
	    document.getElementById("qtdDias").value = "";
	 }
}

function executaRelatorio(){
	var usuario = getUsuario();
	var data = getDatas();
	var dias;
	var pg = 0;//carrega a pagina 0
	
	if(document.getElementById("qtdDias").disabled)
	{
	    dias = null;
	}
	else
	{
		var reDigits = /^\d+$/;
		dias = document.getElementById("qtdDias").value;
		if (dias != null)
		{
			if(dias == "")
			{
				dias = null;
			}
			else
				if(!reDigits.test(dias))
				{
					alert("Digitar somente números na quantidade de dias");
					return false;
				}
		}		
	}
	
	var consolidado = false;
	if($('radio_est_uso').checked){
		consolidado = true;
	}
	
	if(data != null)
	{
		$('imgLoad').innerHTML = 'Carregando...';
		RelatorioAjax.verificaRelatorioUso(carregaRelatorio, usuario, dias, data[0], data[1], pg, consolidado);
	}
}

function carregaRelatorio(isEmpty){
	$('imgLoad').innerHTML = '';
	if(isEmpty==0)
	{
		alert('Não há dados para o período selecionado');
	}
	else
	{
		window.open("/PortalOsx/templates/segAcesso/relatorioSegUso.jsp",'Relatório','resizable=false,status=yes,menubar=no,scrollbars=false,width=740,height=560');
	}
}

var idLinha;
function detalheFiltros(id){
	idLinha = id;
	window.open("relatorioUsoFiltros.jsp",'filtros','resizable=yes,status=yes,menubar=no,scrollbars=yes,width=650,height=500');	
}

function getLinha(){
	return idLinha;
}

function getUsuario(){
	return $('usuario').value;
}

function getDatas(){
	var mensagem = '';

	var dia = getSelecionadoText('dia_inicio');
	var mes = getSelecionado('mes_inicio');
	var ano = getSelecionadoText('ano_inicio');
	var hora = getSelecionadoText('periodo_inicio');
	
	var diaF = getSelecionadoText('dia_fim');
	var mesF = getSelecionado('mes_fim');
	var anoF = getSelecionadoText('ano_fim');
	var horaF = getSelecionadoText('periodo_fim');
	
	var data = new Array(2);
	var data1 = null;
	var data2 = null;
	if(ano != '' && mes != 0 && dia != '' && hora != '' )//todos preenchidos
	{
		data1 = ano+'-'+mes+'-'+dia+' '+hora+':00';
	}
	else 
		if(!(ano == '' && mes == 0 && dia == '' && hora == ''))//nem todos não preenchidos é erro
		{
			mensagem = 'Devem ser preenchidos todos ou nenhum dos campos do Período inicial.\n';
		}
	
	
	if(anoF != '' && mesF != 0 && diaF != '' && horaF != '' )// todos preenchidos
	{
		data2 = anoF+'-'+mesF+'-'+diaF+' '+horaF+':00';
	}
	else
		if(!(anoF == '' && mesF == 0 && diaF == '' && horaF == ''))
		{
			mensagem += 'Devem ser preenchidos todos ou nenhum dos campos do Período final.';
		}
	
	
	data[0] = data1;
	data[1] = data2;
	
	if(mensagem != ''){
		alert(mensagem);
		return null;
	}
	return data;
}

</script>
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
-->
</style>
</head>

<body onLoad="init();" bottommargin="0" leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" rightmargin="0">
<table width="780" border="0" cellpadding="0" cellspacing="0" background="/PortalOsx/imagens/fundo.gif">
  <!--DWLayoutTable-->
  <tr>
    <td width="630" height="20" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="630" height="20" align="left" valign="middle">&nbsp;&nbsp;&nbsp;<img src="/PortalOsx/imagens/reluso.gif" border="0" align="middle" alt="Dica de Utilização" /></td>
      </tr>
    </table>    </td>
    <td width="150" rowspan="3" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="150" height="20" align="center"  valign="middle" >
		  <img src="/PortalOsx/imagens/dicasDeUtilizacao.gif"  align="middle" alt="Dica de Utilização" />		</td>
      </tr>
      <tr>
        <td height="378" valign="top" >
		<p>
		&nbsp;&nbsp;Dica de utilização do relatório de Controle de Uso		</p>		</td>
      </tr>
      
    </table>    </td>
  </tr>
  <tr>
    <td height="225" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td width="11" height="19"></td>
          <td width="25">&nbsp;</td>
          <td width="42">&nbsp;</td>
          <td width="33"></td>
          <td width="56"></td>
          <td width="25"></td>
          <td width="266"></td>
          <td width="172"></td>
        </tr>
      <tr>
        <td height="22"></td>
        <td colspan="2" align="left" valign="middle"><b>Usu&aacute;rio:</b></td>
        <td colspan="4" valign="middle"><input type="text" id="usuario" alt="informe o nome do usuario a ser pesquisado" style="color:#000000; background-color:#FFFFFF" />
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
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td height="22"/> 
        <td colspan="4" align="left" valign="middle"><b>&Uacute;ltimos "n" dias:</b></td>
        <td colspan="2" valign="middle">
		<input type="text" id="qtdDias" size="5" value="7" 
		alt="informe o nome de dias que serão pesquisados" style="color:#000000; background-color:#FFFFFF" />
        <td></td>
      </tr>
      
      <!--DWLayoutTable-->
      
        <tr>
          <td height="129">&nbsp;</td>
          <td colspan="6" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
              <!--DWLayoutTable-->
              <tr>
                <td width="60" height="16"></td>
                <td width="52"></td>
                <td width="8"></td>
                <td width="96"></td>
                <td width="8"></td>
                <td width="66"></td>
                <td width="25"></td>
                <td width="61"></td>
                <td width="71"></td>
              </tr>
              <tr>
                <td height="19" colspan="9" align="left" valign="middle" bgcolor="#000033"><font color="#FFFFFF"><b>Per&iacute;odo Inicial</b></font></td>
              </tr>
              
              
              
              
              
              
              
              <tr>
                <td height="24" align="left" valign="middle"><strong>Data:</strong></td>
                <td align="center" valign="middle">
                  <select id="dia_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
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
                  </select>			</td>
                <td align="center" valign="middle"> / </td>
                <td align="center" valign="middle">
                  <select id="mes_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
                    <option value="0"></option>                
                    <option value="01">Janeiro</option>
                    <option value="02">Fevereiro</option>
                    <option value="03">Março</option>
                    <option value="04">Abril</option>
                    <option value="05">Maio</option>
                    <option value="06">Junho</option>
                    <option value="07">Julho</option>
                    <option value="08">Agosto</option>
                    <option value="09">Setembro</option>
                    <option value="10">Outubro</option>
                    <option value="11">Novembro</option>
                    <option value="12">Dezembro</option>
                  </select>			</td>
                <td align="center" valign="middle">/</td>
                <td align="left" valign="middle">
                  <select id="ano_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
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
                  </select>			</td>
                <td>&nbsp;</td>
                <td align="center" valign="middle"><strong>Hora:</strong></td>
                <td align="left" valign="middle">
                  <select id="periodo_inicio" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
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
              </tr>
              
              <tr>
                <td height="27">&nbsp;</td>
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
                <td height="19" colspan="9" align="left" valign="middle" bgcolor="#000033"><font color="#FFFFFF"><b>Per&iacute;odo Final </b></font></td>
              </tr>
              
              
              
              
              
              
              
              
              
          
          
              <tr>
                <td height="24" align="left" valign="middle"><b>Data : </b></td>
                <td align="center" valign="middle">
                  <select id="dia_fim" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
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
                  </select>			</td>
                <td align="center" valign="middle">/</td>
                <td align="center" valign="middle">
                  <select id="mes_fim" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
                    <option value="0"></option>                
                    <option value="01">Janeiro</option>
                    <option value="02">Fevereiro</option>
                    <option value="03">Março</option>
                    <option value="04">Abril</option>
                    <option value="05">Maio</option>
                    <option value="06">Junho</option>
                    <option value="07">Julho</option>
                    <option value="08">Agosto</option>
                    <option value="09">Setembro</option>
                    <option value="10">Outubro</option>
                    <option value="11">Novembro</option>
                    <option value="12">Dezembro</option>
                  </select>			</td>
                <td align="center" valign="middle">/</td>
                <td align="left" valign="middle">
                  <select id="ano_fim" class="selectBoxNormal1" onChange="desabilitaCampoDias()"> 
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
                  </select>			</td>
                <td>&nbsp;</td>
                <td align="center" valign="middle"><strong>Hora:</strong></td>
                <td align="left" valign="middle">
                  <select id="periodo_fim" class="selectBoxNormal1" onChange="desabilitaCampoDias()">
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
                  </select>		   </td>
              </tr>
              
              
          </table></td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td height="29"></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td></td>
          <td>&nbsp;</td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td height="19"></td>
          <td colspan="6" valign="middle" bgcolor="#000033"><span class="style1">&nbsp;Tipos de Relat&oacute;rios</span></td>
          <td></td>
        </tr>
        
        
        
        
        <tr>
          <td height="20"></td>
          <td align="center" valign="middle"><input type="radio" id="radio_rel_uso" name="radio_rel"/></td>
          <td colspan="3" valign="middle">Relat&oacute;rio de Uso </td>
          <td valign="top"><input type="radio" id="radio_est_uso" name="radio_rel"/></td>
          <td valign="middle">Estat&iacute;stica de Uso </td>
          <td></td>
        </tr>
        <tr>
          <td height="31"></td>
          <td>&nbsp;</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td height="32"></td>
          <td colspan="3" align="left" valign="bottom" >
            <a href="#" ><img src="/PortalOsx/imagens/apresentar.gif"  onclick="executaRelatorio()" border="0" align="middle" alt="Dica de Utilização" />         </a></td>
		  <td valign="bottom" >
            <a href="#" ><img src="/PortalOsx/imagens/limpar.gif"  onclick="limparSelecao()" border="0" align="middle" alt="Dica de Utilização" />          </a>	</td>
          
          <td colspan="2" align="center" valign="bottom" ><div id="imgLoad"></div></td>
		<td></td>
        </tr>
        
        <tr>
          <td height="36"></td>
          <td ></td>
          <td ></td>
          <td ></td>
          <td ></td>
          <td ></td>
          <td ></td>
          <td></td>
        </tr>
        
        
        
		
      
    </table></td>
  </tr>
 
</table>
</body>
</html>
