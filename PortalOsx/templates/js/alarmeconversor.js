var TEM_SMS = false;
var aBilhetadores;

function Processa(Funcao)
{
    switch (Funcao)
    {
        case 0:
            VerificaMensagem();
            MontaBilhetadores();
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

function MontaBilhetadores()
{
   Bilhetadores = document.form1.bilhetadores.value;

   aBilhetadores = Bilhetadores.split(";");
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
         else
         {
            if (Valor > 0 && Valor <= 600)
               return false;
         }
    }
    else
        return false;

    return true;
}

function ValidaForm()
{
    QtdBil = document.form1.qtd.value;
    var Ociosidades = "", Habilitacao = "";

	if(QtdBil > 1){
	    for (i = 0; i < QtdBil; i++)
	    {
	       // Validação da ociosidade
	       if (VerificaNum(document.form1.ociosidade[i].value, "ociosidade") == false)
	       {
	          alert ("Ociosidade definida para \"" + aBilhetadores[i] + "\" está incorreta!\nOciosidade deve ser 0 (desabilita) ou maior que 600!");
	          return false;
	       }
	
	       // Testa se tem habilitacao sem configuracao de ociosidade
	       if (document.form1.habilita[i].checked == true)
	       {
	          
	          if (document.form1.ociosidade[i].value == "0")
	          {
	             document.form1.habilita[i].checked = false;
	             alert ("Configure ociosidade para \"" + aBilhetadores[i] + "\" para poder habilitar alarme!");
	             return false;
	          }
	       }
	    }
	}else{
		if (document.form1.ociosidade.value == "0"){
			 alert ("Ociosidade definida está incorreta!\nOciosidade deve ser 0 (desabilita) ou maior que 600!");
          	return false;
		}
		
		if (document.form1.habilita.checked == true)
       	{
          if (document.form1.ociosidade.value == "0")
          {
             document.form1.habilita.checked = false;
             alert ("Configure ociosidade para \"" + aBilhetadores[0] + "\" para poder habilitar alarme!");
             return false;
          }
       }
	}
	if(QtdBil>1){
	    for (i = 0; i < QtdBil; i++)
	    {
	 
	       Ociosidades += document.form1.ociosidade[i].value + ";" ;
	       if (document.form1.habilita[i].checked == true)
	          Habilitacao += "1" + ";" ;
	       else
	          Habilitacao += "0" + ";" ;
	    }
	}else{

		Ociosidades += document.form1.ociosidade.value + ";" ;
       	if (document.form1.habilita.checked == true)
          Habilitacao += "1" + ";" ;
       	else
          Habilitacao += "0" + ";" ;
	}
    if (TEM_SMS)
	{
       if (VerificaTelefones(document.form1.telefones.value) == false)
       {
          alert ("Erro no(s) telefone(s) configurado(s)! São aceitos apenas números separados por ';'!");
          return false;
       }
    }

    document.form2.bilhetadores.value = document.form1.bilhetadores.value;
    document.form2.ociosidades.value = Ociosidades;
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