<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<html>
<head>
<title>Reprocessamento</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css">
<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpDataReprocessamentoSalvarAjax.js'></script>
<script type="text/javascript">
<!--
DWREngine.setErrorHandler(erro);


function erro(msg, ex) {
	mostrar('botaoSalvar');
	$('status').innerHTML = msg;
}

function init(){
		
	$('data').value = 'dd/mm/aaaa';
}

function salvar(){

	
	
	var Data = $('data').value;
	
	
	
	var DataDet = new Array();
      DataDet[0] = Data.substring(0, 2);
      DataDet[1] = Data.substring(3, 5);
      DataDet[2] = Data.substring(6, 10);

      var Erro = false;
    
      if(Data.length > 10){
		Erro = true;
	  }
      
      for (i = 0; i < 3; i++)
      {
         Expressao = new RegExp("[^0-9]", "gi");
         Ret = DataDet[i].search(Expressao);

         if (Ret != -1)
            Erro = true;

         switch (i)
         {
            case 0:  // Dia
               if (Math.ceil(DataDet[0]) == 0 || Math.ceil(DataDet[0]) > 31)
                  Erro = true;
               break;
            case 1:  // Mes
               if (Math.ceil(DataDet[1]) == 0 || Math.ceil(DataDet[1]) > 12)
                  Erro = true;
               break;
            case 2:  // Ano
               if (Math.ceil(DataDet[2]) < 2000 || Math.ceil(DataDet[2]) > 2050)
                  Erro = true;
               break;
         }

         if (Erro == true)
         {
            alert("Data incorreta. Corrija-a!");
            return;
         }
      }
	
	var data = $('data').value;
		
	if(data == ''){
		$('data').focus();
		alert('Data em branco');
		return;
	}
	
	$('status').innerHTML = 'Salvando...';	
	
	OpDataReprocessamentoSalvarAjax.salvar(retSalvar,data);
	
}

function retSalvar(valor){
	alert(valor);
	$('status').innerHTML = '';
}

function limpar(){
	$('data').value = '';
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
    <td width="613" height="32" valign="top"><img src="/PortalOsx/imagens/reprocessamento_de_contador.gif" /></td>
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
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <!--DWLayoutTable-->
      <tr>
        <td height="25" colspan="2" valign="middle"><strong>Data: </strong></td>
        <td colspan="2" valign="middle"><input type="text" name="data" id="data"></td>
        <td width="60">&nbsp;</td>
      
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