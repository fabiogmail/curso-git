/****
 * Argumentos na P?gina
 * --------------------
 * Formul?rio: Form1
 *    ListaDatas: lista de datas existentes para consulta de 
 *                resumo di�rio
 *    mensagem: mensagem para ser apresentada no onLoad
 *
****/

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
         VerificaMensagem();
			MontaListaDatas();
			break;
		case 1: 
			return ValidaForm("verifica");
			break;
		default:
			alert ("Fun��o n�o encontrada!");
			break;
	}
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function MontaListaDatas()
{
	Lista = document.form1.ListaDatas.value;
	if (Lista.charAt(0) != "$")
	{
		aLista = Lista.split(";");

		while (document.form1.Datas.length > 0)
			document.form1.Datas.options[0] = null;

		for (i = 0; i < aLista.length; i++)
		{
			document.form1.Datas[i] = new Option(aLista[i], "", false, false);
		}
		document.form1.Datas.selectedIndex = -1;
	}
	else alert ("N�o h� informa��es para resumo di�rio!");
}

function ValidaForm()
{
   Indice = document.form1.Datas.selectedIndex;

   if (Indice == -1)
   {
      alert ("Selecione uma data para consultar o resumo di�rio!");
      return false;
   }

   document.form2.data.value = document.form1.Datas.options[Indice].text;
   //alert (document.form2.data.value);
   return true;
}