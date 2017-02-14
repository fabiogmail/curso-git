var aListaRelac = new Array;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
		   VerificaMensagem();
			MontaListaConfiguracoes();
			MontaListaServidores();
			MontaListaConversores();
			MontaListaBilhetadores();
			MontaRelacionamento();
			break;
		case 1: 
			return Inicia();
			break;
		case 2: 
			return Finaliza();
			break;
		case 3: 
			return Termina();
			break;
		case 4: 
			SelecionaConfiguracao();
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

function MontaListaConfiguracoes()
{
	Lista = document.form1.ListaConfiguracoesNomes.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes = Lista;
		//var ListaRelac = document.form1.Relacionamento.value;

		aListaNomes = ListaNomes.split(";");
		//aListaRelac = ListaRelac.split(";");

		while (document.form1.ListaConfiguracoes.length > 0)
			document.form1.ListaConfiguracoes.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			//alert (ListaConfiguracoes[i]);
			document.form1.ListaConfiguracoes[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaConfiguracoes.selectedIndex = -1;
	}
	//else alert ("Servidor não enviou a lista de Configurações!");
}

function MontaListaServidores()
{
	ListaServidores = document.form1.ListaServidoresNomes.value;
	if (ListaServidores.charAt(0) != "$")	
	{
		aListaServidores = ListaServidores.split(";");
		
		while (document.form1.ListaServidores.length > 0)
			document.form1.ListaServidores.options[0] = null;

		for (i = 0; i < aListaServidores.length; i++)
		{
			document.form1.ListaServidores[i] = new Option(aListaServidores[i], "", false, false);
		}
		document.form1.ListaServidores.selectedIndex = -1;
	}
	//else alert ("Servidor não enviou a lista de Servidores de Processos!");
}

function MontaListaConversores()
{
	ListaConversores = document.form1.ListaConversoresNomes.value;
	if (ListaConversores.charAt(0) != "$")	
	{
		aListaConversores = ListaConversores.split(";");
		
		while (document.form1.ListaConversores.length > 0)
			document.form1.ListaConversores.options[0] = null;

		for (i = 0; i < aListaConversores.length; i++)
		{
			document.form1.ListaConversores[i] = new Option(aListaConversores[i], "", false, false);
		}
		document.form1.ListaConversores.selectedIndex = -1;
	}
	//else alert ("Servidor não enviou a lista de Conversores!");
}


function MontaListaBilhetadores()
{
	ListaBilhetadores = document.form1.ListaBilhetadoresNomes.value;
	if (ListaBilhetadores .charAt(0) != "$")	
	{
		aListaBilhetadores = ListaBilhetadores.split(";");
		
		while (document.form1.ListaBilhetadores.length > 0)
			document.form1.ListaBilhetadores.options[0] = null;

		for (i = 0; i < aListaBilhetadores.length; i++)
		{
			document.form1.ListaBilhetadores[i] = new Option(aListaBilhetadores[i], "", false, false);
		}
		document.form1.ListaBilhetadores.selectedIndex = -1;
	}
	//else alert ("Servidor não enviou a lista de Bilhetadores!");
}

function MontaRelacionamento()
{
	var Relacionamento = document.form1.Relacionamento.value;
	if (Relacionamento.charAt(0) != "$")
	{
		ConfigNivel1 = Relacionamento.split(";");
		for (i = 0; i < ConfigNivel1.length; i++)
		{
			ConfigNivel2 = ConfigNivel1[i].split("@");
			aListaRelac[i] = new Array(4);
			for (j = 0; j < ConfigNivel2.length; j++)
			{
				if (j != 3)
				{
					aListaRelac[i][j] = ConfigNivel2[j];
				}
				else
				{
					ConfigNivel3 = ConfigNivel2[3].split(",");
					aListaRelac[i][j] = new Array(ConfigNivel3.length);
					aListaRelac[i][j] = ConfigNivel3;
				}
			}
		}
	}
	//else alert ("Servidor não enviou a lista de Relacionamentos!");
}

/*
 *	Seleciona a configuração correspondente ao nome da configuração selecionada em ListaConfiguracoes
 */
function SelecionaConfiguracao()
{
	var Indice = document.form1.ListaConfiguracoes.selectedIndex;
	if (Indice != -1)
	{
		// Seleciona servidor correspondente
		for (i = 0; i < document.form1.ListaServidores.length; i++)
		{
			if (aListaRelac[Indice][1] == document.form1.ListaServidores.options[i].text)
			{
				document.form1.ListaServidores.selectedIndex = i;
				break;
			}
		}

		// Seleciona conversor correspondente
		for (i = 0; i < document.form1.ListaConversores.length; i++)
		{
			if (aListaRelac[Indice][2] == document.form1.ListaConversores.options[i].text)
			{
				document.form1.ListaConversores.selectedIndex = i;
				break;
			}
		}

		// Seleciona bilhetadores correspondentes. Antes limpa a lista de bilhetadores.
		for (i = 0; i < document.form1.ListaBilhetadores.length; i++)
			document.form1.ListaBilhetadores.options[i].selected = false;

		for (i = 0; i < document.form1.ListaBilhetadores.length; i++)
		{
			for (j = 0; j < aListaRelac[Indice][3].length; j++)
			{
				if (aListaRelac[Indice][3][j] == document.form1.ListaBilhetadores.options[i].text)
				{
					document.form1.ListaBilhetadores.options[i].selected = true;
					break;
				}
			}
		}
	}
}

function Inicia()
{
   if (document.form1.ListaConfiguracoes.length == 0)
	{
		alert ("Não há configurações de conversores para iniciar!");
		return false;
	}

	var Indice = document.form1.ListaConfiguracoes.selectedIndex;
   if (Indice == -1)
	{
		alert ("Selecione uma configuração de conversor para iniciá-la!");
		return false;
	}

   // Preenche o formulário 2
   document.form2.cfgconversor.value = document.form1.ListaConfiguracoes.options[Indice].text;
   return true;
}

function Finaliza()
{
	if (document.form1.ListaConfiguracoes.length == 0)
	{
		alert ("Não há configurações de conversores para finalizar!");
		return false;
	}

	var Indice = document.form1.ListaConfiguracoes.selectedIndex;
	if (Indice == -1)
	{
		alert ("Selecione uma configuração de conversor para finalizá-la!");
		return false;
	}

	// Preenche o formulário 3
	document.form3.cfgconversor.value = document.form1.ListaConfiguracoes.options[Indice].text;
	return true;

}

function Termina()
{
	if (document.form1.ListaConfiguracoes.length == 0)
	{
		alert ("Não há configurações de conversores para terminar!");
		return false;
	}

	var Indice = document.form1.ListaConfiguracoes.selectedIndex;
	if (Indice == -1)
   {
		alert ("Selecione uma configuração de conversor para terminá-la!");
		return false;
	}

	// Preenche o formulário 4
	document.form4.cfgconversor.value = document.form1.ListaConfiguracoes.options[Indice].text;
	return true;
}