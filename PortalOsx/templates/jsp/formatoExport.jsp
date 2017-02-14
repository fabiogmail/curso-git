<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type='text/javascript' src='/PortalOsx/dwr/engine.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/util.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpRelVolumetriaAjax.js'></script>
<script type='text/javascript' src='/PortalOsx/dwr/interface/OpRelSequenciaAjax.js'></script>

<script type="text/javascript">
function init(){
	OpRelVolumetriaAjax.getExportacao(retInit);	
}

function retInit(valor){
	if(valor == 1){
		document.getElementById("formato")[0].selected = true;
	
	}
	else if(valor == 2)
		document.getElementById("formato")[1].selected = true;
}	
	function selecionar(){
		var indice = document.getElementById("formato").selectedIndex;
		
		if(indice == 1)
			OpRelVolumetriaAjax.setExportacao(2);
		else if(indice == 0)
			OpRelVolumetriaAjax.setExportacao(1);
			
		window.close();
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Configuração de Formato para Download</title>
</head>
<body onload="init()">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td><img src="/PortalOsx/imagens/configuracao.gif"></td>
    </tr>
    <tr> 
      <td><font size="1" face="Verdana, Arial, Helvetica, sans-serif">
	         Configure como ser&aacute; visualizado o relat&oacute;rio:
		  </font>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
  </table>

  <table width="100%" border="0" cellspacing="0" cellpadding="2">

      <tr>
         <td colspan="2"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Forma de visualiza&ccedil;&atilde;o:</font>
            <select name="listatipovisualizacao" class="select" id="formato">
            <option>HTML</option>
            <option>XLS</option>
            </select>
         </td>
      </tr>
      
      <tr>
		<td align="center" height="50px">
			<p><input type="submit" name="button" value="Selecionar" class="button" onClick="selecionar()"></p>
		</td>
	</tr>

  </table>

</body>
</html>