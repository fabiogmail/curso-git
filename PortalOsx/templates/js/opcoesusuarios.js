/****
 * Argumentos na P?gina
 * --------------------
 * Formul?rio: Form1
 *    mensagem: mensagem para ser apresentada no onLoad
 *    Multisess?o
 *    Quantidade m?xima de usu?rios
 *    N?vel de log
 *
****/

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function Processa()
{
	//document.form1.MultiSessao.options[document.form1.multi.value].selected = true;
	document.form1.NivelDeLog.options[document.form1.nivellog.value].selected = true;
	VerificaMensagem();	
}

function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMaximo)
{
   Expressao = new RegExp("[^0-9]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " incorreta!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ser no m&aacute;ximo "+ TamMaximo +" caracteres!";

   return "";
}


function VerificaQtd()
{
   Retorno = VerificaCampo("Quantidade M&aacute;xima", document.form1.qtdmax.value, 6)
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }

	return true;
}

function ValidaForm()
{
	if (VerificaQtd() == false)
		return false;
	else
	{
		//document.form1.multi.value = document.form1.MultiSessao.selectedIndex;
		document.form1.nivellog.value = document.form1.NivelDeLog.selectedIndex;
		return true;
	}
}
