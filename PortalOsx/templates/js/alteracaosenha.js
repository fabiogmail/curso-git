function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMinimo, TamMaximo)
{
   if (ValorDoCampo.length < TamMinimo)
      return NomeDoCampo + " deve ter no m?nimo " + TamMinimo + " caracteres!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ter no m?ximo " + TamMaximo + " caracteres!";

   Expressao = new RegExp("[^A-Za-z0-9._-]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " somente pode conter caracteres alfanum?ricos alem de '\.\', \'_\' e \'-\'!";

   return "";
}

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
			VerificaMensagem();
			break;
		case 1: 
			return ValidaForm();
			break;
		default:
			alert ("Fun??o n?o encontrada!");
			break;
	}
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function ValidaForm()
{
   Retorno = VerificaCampo("Senha Atual", document.form1.senhaatual.value, 3, 50);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }

   Retorno = VerificaCampo("Nova Senha", document.form1.senha1.value, 4, 50);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }

   Retorno = VerificaCampo("Confirmac?o de Senha", document.form1.senha2.value, 4, 50);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }

   if (document.form1.senha1.value != document.form1.senha2.value)
   {
      alert ("Nova senha e confirma??o de senha devem ser iguais!");
      return false;
   }

   if(document.form1.cliente.value == 'Claro'){
   	if(document.form1.user.value == document.form1.senha1.value){
   	  alert ("Nova senha deve ser diferente do nome de usu?rio");
      return false;
   	}
   }

   document.form2.senhaold.value = document.form1.senhaatual.value;
   document.form2.senhanew.value = document.form1.senha1.value;

   return true;
}