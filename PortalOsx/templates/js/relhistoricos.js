/****
 * Argumentos na P?gina
 * --------------------
 * Formulário: Form1
 *    ListaPerfisNomes: lista dos perfis que podem ter os relatórios acessados
 *    mensagem: mensagem para ser apresentada no onLoad
 *
****/

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
         VerificaMensagem();
			MontaListaPerfis();
         document.form1.ListaTipoRel.selectedIndex = -1;
			return true;
		case 1: 
			return ValidaForm();
		default:
			alert ("Função não encontrada!");
			return false;
	}
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function MontaListaPerfis()
{
	Lista = document.form1.ListaPerfisNomes.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes = document.form1.ListaPerfisNomes.value;
		aListaNomes = ListaNomes.split(";");

		while (document.form1.ListaPerfis.length > 0)
			document.form1.ListaPerfis.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			document.form1.ListaPerfis[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaPerfis.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Perfis!");
}


function ValidaForm()
{
   if (document.form1.ListaPerfis.selectedIndex == -1)
   {
      alert ("Selecione um perfil!");
      return false;
   }

   document.form2.perfil.value = document.form1.ListaPerfis.options[document.form1.ListaPerfis.selectedIndex].text;
   document.form2.tiporel.value = document.form1.ListaTipoRel.options[document.form1.ListaTipoRel.selectedIndex].value;
   return true;
}