/****
 * Argumentos na P?gina
 * --------------------
 * Formul?rio: Form1
 *    ListaPerfisNomes: lista de nomes de perfis existentes
 *    ListaPerfisIds: lista de ids dos perfis na mesma ordem da ListaPerfisNome
 *    ListaPerfisModosDeAcesso: lista de modos de acesso (relatório, gestor, ...)
 *    Relacionamento: nome do perfil/tipo de acesso
 *    mensagem: mensagem para ser apresentada no onLoad
 *
****/

var aListaIds, aListaRelac;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
		    VerificaMensagem();
			MontaListaPerfis();
			break;
		case 1: 
			return Inconsistencia("verifica");
			break;
		case 2: 
			return Inconsistencia("corrige");
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

function Inconsistencia(funcao)
{
	if (document.form1.ListaPerfis.length == 0)
	{
		if (funcao == "verifica")
			alert ("Não há perfis para verificar!");
		else
			alert ("Não há perfis para corrigir!");
		return false;
	}

	var Indice = document.form1.ListaPerfis.selectedIndex;
	if (Indice != -1)
	{
		// Preenche o formulário 2
		Perfis = "";
		for (i = 0; i < document.form1.ListaPerfis.length; i++)
		{
			if (document.form1.ListaPerfis.options[i].selected == true)
			{
				Perfis += document.form1.ListaPerfis.options[i].text;
				Perfis += ";";
			}
		}
		Perfis = Perfis.substring(0, Perfis.length-1);
		//alert (Perfis);
		//return false;
		if (funcao == "verifica")
			document.form2.perfis.value = Perfis;
		else
			document.form3.perfis.value = Perfis;
		return true;
	}
	else 
	{
		if (funcao == "verifica")
			alert ("Selecione um perfil para verificá-lo!");
		else
			alert ("Selecione um perfil para corrigí-lo!");
		return false;
	}
}