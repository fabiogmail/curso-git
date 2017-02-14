<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<%
	ArrayList tags = (ArrayList)request.getAttribute("tags");
	ArrayList tagsaida = (ArrayList)request.getAttribute("tagsSaida");
%>
<html>
<head>
	
	<title>Editando Xml do Parser</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="Pragma" content="no-cache">
	<link rel="stylesheet" href="/PortalOsx/css/style.css">	

<style type="text/css"> 
<!--
.style1 {font-family: Verdana, Arial, Helvetica, sans-serif}
.style3 {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10pt;
	font-weight: bold;
}
-->
</style>


<script type="text/javascript">

function adiciona(){
	var sel = document.getElementById('tags');	
	for(var x=0;x < sel.length;x++){
		if(sel.options[x].selected){
			var texto = sel.options[x].text;
			insertOption(texto,'tagsEscolhidas');
			sel.remove(x);
			x--;
		}
	}
	
}

function insertOption(nome,sel)
{
	var y = document.createElement('option');
  	y.text = nome;
	y.value = nome;
	var selecte = document.getElementById(sel);  
  	try
    {
    	selecte.add(y,null); // standards compliant
    }
  	catch(ex)
    {
    	selecte.add(y); // IE only
    }
}


function remove(){
	var sel = document.getElementById('tagsEscolhidas');
	for(var x=0;x < sel.length;x++){
		if(sel.options[x].selected){
			var texto = sel.options[x].text;
			insertOption(texto,'tags');
			sel.remove(x);
			x--;
		}
	}
}

function removeTodos(){
	var sel = document.getElementById('tagsEscolhidas');
	for(var x=0;x < sel.length;x++){		
		var texto = sel.options[x].text;
		insertOption(texto,'tags');
		sel.remove(x);
		x--;		
	}
}

function subir(){
	var sel = document.getElementById('tagsEscolhidas');
	for(var x=0;x < sel.length;x++){		
		if(sel.options[x].selected){
			if(x != 0){			
				var aux2 = sel.options[x-1].text;
				var aux2V = sel.options[x-1].value;
				sel.options[x-1].text = sel.options[x].text;
				sel.options[x-1].value = sel.options[x].value;
				sel.options[x].text = aux2;
				sel.options[x].value = aux2V;
				
				sel.options[x].selected = false;
				sel.options[x-1].selected = true;
			}
		}			
	}
}

function descer(){
	var sel = document.getElementById('tagsEscolhidas');
	
	for(var x=sel.length-1;x >= 0;x--){		
		if(sel.options[x].selected){
			if(x != sel.length-1){
				var aux2 = sel.options[x+1].text;
				var aux2V = sel.options[x+1].value;
				sel.options[x+1].text = sel.options[x].text;
				sel.options[x+1].value = sel.options[x].value;
				sel.options[x].text = aux2;
				sel.options[x].value = aux2V;
				
				sel.options[x].selected = false;
				sel.options[x+1].selected = true;				
			}
		}	
	}
}

function salvar(){
	var sel = document.getElementById('tagsEscolhidas');
	var temp;
	//alert(sel.options[0]);
	if(sel.options[0] != null){
		temp = sel.options[0].text;
		for(var x=1;x < sel.length;x++){	
			temp += ";"+sel.options[x].text;
		}
		document.getElementById("escolhidos").value = temp;
	}
	document.forms[0].submit();
}

</script>
<link href="../../css/style.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style5 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12pt;
}
.style8 {font-size: 12px}
-->
</style>
</head>

<body bgcolor="#FFFFFF" style="margin:0px" >
<form method="post" name="editarXmlParser" action="/PortalOsx/servlet/Portal.cPortal?operacao=salvarXmlParser">
<input type="hidden" name="suboperacao" value="">
<table width="779" height="383" border="0" cellpadding="0" cellspacing="0" background="/PortalOsx/imagens/fundo.gif">
  <!--DWLayoutTable-->
<div id='mensagem'></div>
<tr>
<td width="20" height="383">&nbsp;</td>
<td width="613" valign="top">
  <table width="491" border="0" cellspacing="0" cellpadding="0">
    <!--DWLayoutTable-->
    <tr>
      <td width="479" height="2"></td>
      <td width="41"></td>
      <td width="3"></td>
      </tr>
    <tr>
      <td height="23" colspan="2" align="left" valign="top"><span class="style3 style5"><span class="inputtext style8">Editando Xml do Parser</span></span></td>
      <td></td>
    </tr>
    <tr>
      <td height="23">&nbsp;</td>
      <td></td>
      <td></td>
    </tr>
    
    
    
    
  
    <tr>
      <td height="220" colspan="2">	
        <div id="tabParse">
          <table width="520" height="209" border="0" align="left" class="titulo1">
            <!--DWLayoutTable-->
            <tr>
              <td height="14" colspan="3" align="center" valign="bottom" bgcolor="#000000"><div align="center"><span><font color="#FFFFFF"> <b>Saida do Parser &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Campos Configura&ccedil;&atilde;o</b></font> </span></div></td>
	  </tr>
            <tr>
              <td width="230" height="200" valign="top" class="style1"><select style="border: 1px solid black;width:230px;" multiple="multiple" name="tags" id="tags" size="12" >
                <%
		for(int i=0; i < tags.size(); i++){		
		%>
                <option value="<%=(""+tags.get(i))%>"><%=(""+tags.get(i))%></option>
                <%}%>
              </select></td>
          <td width="46" align="center" valign="middle" class="style1">
            <input name="adicionar" type="button" class="button" id="adicionar" onClick="adiciona()" value="&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;" alt="adicionar item" width="30" />
<br>
<br>
            <input name="remover" type="button" class="button" id="remover" style="margin:" onClick="remove()" value="&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;" alt="remover o item selecionado" />
<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
            <!--input name="sobe" type="button" class="button" id="sobe" onClick="subir()" value="/\" alt="sobe o item selecionado" /-->
<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
            <!--input name="desce" type="button" class="button" id="desce" onClick="descer()" value="\/" alt="desce o item selecionado" /-->
<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
            <!--input name="removerTodos" type="button" class="button" id="removerTodos" onClick="removeTodos()" value="X" alt="remover todos" /-->
<!--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->		  </td>
          <td width="230" class="style1">
            <select style="border: 1px solid black;width:230px;" multiple="multiple" name="tagsEscolhidas" id="tagsEscolhidas" size="12" >
			<%
			for(int i=0; i < tagsaida.size(); i++){		
			%>
                <option value="<%=(""+tagsaida.get(i))%>"><%=(""+tagsaida.get(i))%></option>
            <%}%>
              </select>		</td>
        </tr>
            </table></div>	</td>
      <td>&nbsp;</td>
      </tr>
    <tr>
      <td height="89" valign="top"><br><br><input name="salvarXml" type="image" src="/PortalOsx/imagens/salvarb.gif" class="button" id="salvarXml" onClick="salvar()" value="S" alt="salvar xml" /></td>
      <td valign="top"><input type="hidden" id="escolhidos" name="escolhidos" value=""  /></td>
      <td>&nbsp;</td>
      </tr>
    <tr>
      <td height="26"></td>
      <td></td>
      <td></td>
      </tr>
  </table></td>
<td width="143" align="left" valign="top">
	<table width="100" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img src="/PortalOsx/imagens/dicasDeUtilizacao.gif" width="141"
					height="19"></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="left" valign="top">
				<p>Edita a saida do Xml do parser .</p>
				<p>Selecione os campos que devem est&aacute; na saida do arquivo, e click no bot&atilde;o de salvar. </p></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>

				<td>&nbsp;</td>
			</tr>
		</table></td>
</tr>
</table>
</form>
</body>
</html>
