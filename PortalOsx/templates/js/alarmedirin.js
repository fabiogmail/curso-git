var TEM_SMS = true;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0:
			VerificaMensagem();
			ApresentaQtd();
			ApresentaPeriodicidade();
			HabilitaAlarme();
			break;
		case 1:
			return VerificaNum("quantidade");
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

function ApresentaQtd()
{
   document.form1.quantidade.value = document.form1.Quantidade.value;
}

function ApresentaPeriodicidade()
{
   document.form1.periodicidade.value = document.form1.Periodicidade.value;
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
	var Variavel = "";

	if (tipo == "quantidade")
		Variavel = document.form1.quantidade.value;
	else
		Variavel = document.form1.periodicidade.value;

	if (Variavel != "")
	{
      Expressao = new RegExp("[^0-9]", "gi");
      Ret = Variavel.search(Expressao);
      if (Ret != -1)
      {
         alert ("Valor em \"" + tipo + "\" somente pode conter caracteres numéricos!");
         return false;
      }

      if (tipo == "periodicidade")
      {
         if (Variavel > 0 && Variavel <= 1800)
         {
            alert ("Periodicidade deve ser 0 (não periódico) ou maior que 1800!");
            return false;
         }
      }
/*      else
      {
         if (Variavel > 0 && Variavel <= 600)
         {
            alert ("Tempo Ocioso deve ser 0 (desabilita) ou maior que 600!");
            return false;
         }
      }
*/
	}
	else
	{
		alert ("É necessário digitar um valor em \"" + tipo + "\"!");
		return false;
	}
	
	return true;
}

function ValidaForm()
{
	if (VerificaNum("quantidade") == false)
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

	document.form2.quantidade.value = document.form1.quantidade.value;
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