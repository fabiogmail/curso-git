/****
 * Argumentos na Página
 * --------------------
 * Formulário: Form1
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
			MontaListaModoDeAcesso();
			break;
		case 1: 
			return Inclui();
			break;
		case 2: 
			return Exclui();
			break;
		case 3: 
			return Bloqueia();
			break;
		case 4: 
			SelecionaModoDeAcesso();
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
		var ListaIds   = document.form1.ListaPerfisIds.value;
		var ListaRelac = document.form1.Relacionamento.value;

		aListaNomes = ListaNomes.split(";");
		aListaIds   = ListaIds.split(";");
		aListaRelac = ListaRelac.split(";");

		while (document.form1.ListaPerfis.length > 0)
			document.form1.ListaPerfis.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			//alert (ListaPerfis[i]+" - "+aListaIds[i]+" - "+aListaRelac[i]);
			document.form1.ListaPerfis[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaPerfis.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Perfis!");
}

function MontaListaModoDeAcesso()
{
	Lista = document.form1.ListaModosDeAcesso.value;
	if (Lista.charAt(0) != "$")	
	{
		var ListaModos = document.form1.ListaPerfisModosDeAcesso.value;

		aListaModos = ListaModos.split(";");
		
		while (document.form1.ListaModosDeAcesso.length > 0)
			document.form1.ListaModosDeAcesso.options[0] = null;

		for (i = 0; i < aListaModos.length; i++)
		{
//			alert (aListaModos[i]+" - "+aListaIds[i]);
			document.form1.ListaModosDeAcesso[i] = new Option(aListaModos[i], "", false, false);
		}
		document.form1.ListaModosDeAcesso.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Modos de Acesso");
}

/*
 *	Seleciona o Modo de Acesso correspondente ao perfil selecionado
 */
function SelecionaModoDeAcesso()
{
	var Indice = document.form1.ListaPerfis.selectedIndex;
	if (Indice != -1)
	{
		for (i = 0; i < document.form1.ListaModosDeAcesso.length; i++)
		{
			//alert (aListaRelac[Indice] +" - "+ document.form1.ListaModosDeAcesso.options[i].text);
			if (aListaRelac[Indice] == document.form1.ListaModosDeAcesso.options[i].text)
			{
				document.form1.ListaModosDeAcesso.selectedIndex = i;
				//alert (document.form1.ListaModosDeAcesso.options[i].text);
				break;
			}
		}
	}
}

function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMinimo, TamMaximo)
{
   if (ValorDoCampo.length < TamMinimo)
      return NomeDoCampo + " deve ter no mínimo " + TamMinimo + " caracteres!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ter no máximo " + TamMaximo + " caracteres!";

   Expressao = new RegExp("[^A-Za-z0-9]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " somente pode conter caracteres alfanuméricos!";

   return "";
}

function Inclui()
{
   Retorno = VerificaCampo("Perfil", document.form1.perfil.value, 3, 15);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }

	var Indice = document.form1.ListaModosDeAcesso.selectedIndex;
	if (Indice == -1)
	{
		alert ("É necessário associar o novo perfil a um modo de acesso!");
		return false;
	}
	else if (document.form1.ListaModosDeAcesso.options[Indice].text == "admin")
	{
		alert ("Não se pode associar um outro perfil ao modo de acesso\"admin\"!");
		return false;
	}
	else
	{
		// Preenche o formulário 2
		document.form2.perfil.value = document.form1.perfil.value;
		document.form2.acesso.value = document.form1.ListaModosDeAcesso.options[Indice].text;
		return true;
	}
}

function Exclui()
{
	if (document.form1.ListaPerfis.length == 0)
	{
		alert ("Não há perfis para apagar!");
		return false;
	}

	var Indice = document.form1.ListaPerfis.selectedIndex;
	if (Indice != -1)
	{
		if (document.form1.ListaPerfis.options[Indice].text == "admin")
		{
			alert ("O perfil \"admin\" não pode ser excluído!");
			return false;
		}
		// Preenche o formulário 3
		document.form3.perfil.value = document.form1.ListaPerfis.options[Indice].text;
		document.form3.id.value     = aListaIds[Indice];
/*
		document.form1.ListaPerfis.options[Indice] = null;	// Exclui perfil da Lista
		aListaIds.splice(Indice, 1);	// Exclui Id do perfil do array
		aListaRelac.splice(Indice, 1);	// Exclui Relacionamento do array
*/
		return true;
	}
	else 
	{
		alert ("Selecione um perfil para apagá-lo!");
		return false;
	}
}

function Bloqueia()
{
	if (document.form1.ListaPerfis.length == 0)
	{
		alert ("Não há perfis para bloquear!");
		return false;
	}

	var Indice = document.form1.ListaPerfis.selectedIndex;
	if (Indice != -1)
	{
		if (document.form1.ListaPerfis.options[Indice].text == "admin")
		{
			alert ("O perfil \"admin\" não pode ser bloqueado!");
			return false;
		}
		// Preenche o formulário 4
		document.form4.perfil.value = document.form1.ListaPerfis.options[Indice].text;
		return true;
	}
	else 
	{
		alert ("Selecione um perfil para bloqueá-lo!");
		return false;
	}
}
