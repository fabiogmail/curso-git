var TEM_SMS = true;
var aDiretorios;

function Processa(Funcao)
{
    switch (Funcao)
    {
        case 0:
            VerificaMensagem();
            MontaDiretorios();
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

function MontaDiretorios()
{
   var Diretorios = document.form1.diretorios.value;

   aDiretorios = Diretorios.split(";");
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

function VerificaNum(Valor, Tipo)
{
    if (Valor.length > 15)
        return false;

    if (Valor != "")
    {
         Expressao = new RegExp("[^0-9]", "gi");
         Ret = Valor.search(Expressao);
         if (Ret != -1)
             return false;

         if (Tipo == "periodicidade")
         {
            if (Valor > 0 && Valor <= 1800)
               return false;
         }
    }
    else
        return false;
    
    return true;
}

function ValidaForm()
{
    QtdDir = document.form1.qtd.value;
    var Limites = "", Periodicidades = "", Habilitacao = "";

    for (i = 0; i < QtdDir; i++)
    {
       // Validação de espaço máximo
       if (VerificaNum(document.form1.espaco[i].value, "espaco") == false)
       {
          alert ("Espaço definido em \"" + aDiretorios[i] + "\" está incorreto!");
          return false;
       }

       // Validação da periodicidade
       if (VerificaNum(document.form1.periodicidade[i].value, "periodicidade") == false)
       {
          alert ("Periodicidade definida para \"" + aDiretorios[i] + "\" está incorreta!\nPeriodicidade deve ser 0 (não periódico) ou maior que 1800!");
          return false;
       }

       // Testa se tem habilitacao sem configuracao de espaço
       if (document.form1.habilita[i].checked == true)
       {
          if (document.form1.espaco[i].value == "0")
          {
             document.form1.habilita[i].checked = false;
             alert ("Configure espaço máximo em \"" + aDiretorios[i] + "\" para poder habilitar alarme!");
             return false;
          }
       }
    }

    if (TEM_SMS)
	{
		if (VerificaTelefones(document.form1.telefones.value) == false)
		{
			alert ("Erro no(s) telfone(s) configurado(s)! São aceitos apenas números separados por ';'!");
			return false;
		}
	}

    for (i = 0; i < QtdDir; i++)
    {
       Limites += document.form1.espaco[i].value + ";" ;
       Periodicidades += document.form1.periodicidade[i].value + ";" ;
       if (document.form1.habilita[i].checked == true)
          Habilitacao += "1" + ";" ;
       else
          Habilitacao += "0" + ";" ;
    }

    document.form2.diretorios.value = document.form1.diretorios.value;
    document.form2.limites.value = Limites;
    document.form2.periodicidades.value = Periodicidades;
    document.form2.habilitacao.value = Habilitacao;

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