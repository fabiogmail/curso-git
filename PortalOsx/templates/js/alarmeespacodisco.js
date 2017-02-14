var TEM_SMS = true;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0:
			VerificaMensagem();
			HabilitaAlarme();
			break;
		case 1:
			return VerificaNum("espaco");
			break;
		case 2:
			return VerificaNum("periodicidade");
			break;
		default:
			alert ("Função não encontrada!");
			break;
	}
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function HabilitaAlarme()
{
	if (document.form1.habilitado.value == "true")
		document.form1.habilita.checked = true;
	else
		document.form1.habilita.checked = false;

    if (TEM_SMS)
       document.form1.sms.selectedIndex = document.form1.opcaosms.value;
}

function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMinimo, TamMaximo)
{
   if (ValorDoCampo.length < TamMinimo)
      return NomeDoCampo + " deve ter no mínimo " + TamMinimo + " caracteres!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ter no máximo " + TamMaximo + " caracteres!";

   Expressao = new RegExp("[^0-9]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " somente pode conter caracteres numéricos!";

   if (NomeDoCampo == "Espaco")
   {
      if (ValorDoCampo == 0)
         return NomeDoCampo + " deve ser no mínimo 1!";
   }
   else
   {
      if (ValorDoCampo > 0 && ValorDoCampo <= 1800)
         return NomeDoCampo + " deve ser 0 (não periódico) ou maior que 1800!";
   }

   return "";
}

function VerificaTelefones(Valor)
{
	Expressao = new RegExp("[^0-9;]", "gi");
	Ret = Valor.search(Expressao);
	if (Ret != -1)
		return false;
	return true;
}

function VerificaNum(tipo)
{
	var Variavel = "", Campo, Min, Max;

	if (tipo == "espaco")
   {
		Variavel = document.form1.espaco.value;
      Campo = "Espaco";
      Min = 1;
      Max = 3;
   }
	else
   {
      Variavel = document.form1.periodicidade.value;
      Campo = "Periodicidade";
      Min = 1;
      Max = 6;
   }

   Retorno = VerificaCampo(Campo, Variavel, Min, Max);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }
	
	return true;
}

function ValidaForm()
{
	if (VerificaNum("espaco") == false)
      return false;
	if (VerificaNum("periodicidade") == false)
      return false;

    if (TEM_SMS)
	{
       if (VerificaTelefones(document.form1.telefones.value) == false)
       {
          alert ("Erro no(s) telefone(s) configurado(s)! São aceitos apenas números separados por ';'!");
          return false;
       }
    }

	document.form2.espaco.value = document.form1.espaco.value;

	document.form2.periodicidade.value = document.form1.periodicidade.value;
	if (document.form1.habilita.checked == true)
		document.form2.habilita.value = "true";
	else
		document.form2.habilita.value = "false";

    if (TEM_SMS)
	{
       if (document.form1.sms.selectedIndex != -1)
          document.form2.sms.value = document.form1.sms.selectedIndex;

       document.form2.telefones.value = document.form1.telefones.value;
	}
	else
	{
       document.form2.sms.value = 0;
       document.form2.telefones.value = "";
	}

	return true;
}