<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Erro XML</title>
<script type="text/javascript">
function mensagem(){
alert("FALHA DE ARQUIVO XML:"+document.getElementById('mensagem').value);
//	alert("VALOR: "+document.getElementById('mensagem').innerHTML);
}
</script>

</head>

<body onload='mensagem()'>
<input type="hidden" id="mensagem" value='<%=(String)request.getParameter("mensagem")%>'/>
</body>
</html>