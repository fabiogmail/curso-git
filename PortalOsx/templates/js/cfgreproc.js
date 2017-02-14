var aListaRelac = new Array;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
         VerificaMensagem();
			MontaListaConfiguracoes();
			MontaListaServidores();
			MontaListaReprocessadores();
			MontaListaBilhetadores();
			MontaRelacionamento();
			break;
		case 1: 
			return Inclui("inclusao");
			break;
		case 2: 
			return Exclui();
			break;
		case 3: 
			return Inclui("alteracao");
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
			document.form1.ListaServidores[i] = new Option(aListaServidores[i], "", false, false);

		document.form1.ListaServidores.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Servidores de Processos!");
}

function MontaListaReprocessadores()
{
	ListaReprocessadores = document.form1.ListaReprocessadoresNomes.value;
	if (ListaReprocessadores.charAt(0) != "$")	
	{
		aListaReprocessadores = ListaReprocessadores.split(";");
		
		while (document.form1.ListaReprocessadores.length > 0)
			document.form1.ListaReprocessadores.options[0] = null;

		for (i = 0; i < aListaReprocessadores.length; i++)
			document.form1.ListaReprocessadores[i] = new Option(aListaReprocessadores[i], "", false, false);

		document.form1.ListaReprocessadores.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Reprocessadores!");
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
//			alert (aListaBilhetadores[i]);
			document.form1.ListaBilhetadores[i] = new Option(aListaBilhetadores[i], "", false, false);
		}
		document.form1.ListaBilhetadores.selectedIndex = -1;
	}
	else alert ("Não existem bilhetadores configurados!");
}

function MontaRelacionamento()
{
	var Relacionamento = document.form1.Relacionamento.value;
	if (Relacionamento.charAt(0) != "$")
	{
		ConfigNivel1 = Relacionamento.split(";");
		for (i = 0; i < ConfigNivel1.length; i++)
		{
			//alert (ConfigNivel1[i]);
			ConfigNivel2 = ConfigNivel1[i].split("@");
			aListaRelac[i] = new Array(4);
			for (j = 0; j < ConfigNivel2.length; j++)
			{
				//alert (ConfigNivel2[j]);
				if (j != 3)
				{
					aListaRelac[i][j] = ConfigNivel2[j];
					//alert (aListaRelac[i][j]);
				}
				else
				{
					ConfigNivel3 = ConfigNivel2[3].split(",");
					aListaRelac[i][j] = new Array(ConfigNivel3.length);
					aListaRelac[i][j] = ConfigNivel3;
					//for (k = 0; k < ConfigNivel3.length; k++)
					//	alert (ConfigNivel3[k]);
				}
			}
		}
/*
		// Verificação do vetor de relacionametos (aListaRelac)
		for (i = 0; i < aListaRelac.length; i++)
		{
			for (j = 0; j < aListaRelac[i].length; j++)
			{
				if (j != aListaRelac[i].length-1)
					alert (i+ " " + j + " - " + aListaRelac[i][j]);
				else
				{
					for (k = 0; k < aListaRelac[i][j].length; k++)
						alert (i+ " " + j + " " + k + " - " +aListaRelac[i][j][k]);
				}
			}
		}
*/
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
			//alert (aListaRelac[Indice][1] +" - "+ document.form1.ListaServidores.options[i].text);
			if (aListaRelac[Indice][1] == document.form1.ListaServidores.options[i].text)
			{
				document.form1.ListaServidores.selectedIndex = i;
				//alert (document.form1.ListaServidores.options[i].text);
				break;
			}
		}

		// Seleciona reprocessador correspondente
		for (i = 0; i < document.form1.ListaReprocessadores.length; i++)
		{
			//alert (aListaRelac[Indice][2] +" - "+ document.form1.ListaReprocessadores.options[i].text);
			if (aListaRelac[Indice][2] == document.form1.ListaReprocessadores.options[i].text)
			{
				document.form1.ListaReprocessadores.selectedIndex = i;
				//alert (document.form1.ListaReprocessadores.options[i].text);
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
				//alert (aListaRelac[Indice][3][j] +" - "+ document.form1.ListaBilhetadores.options[i].text);
				if (aListaRelac[Indice][3][j] == document.form1.ListaBilhetadores.options[i].text)
				{
					document.form1.ListaBilhetadores.options[i].selected = true;
					//alert (document.form1.ListaBilhetadores.options[i].text);
					break;
				}
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

   if (ValorDoCampo.substring(0,2) != "r_")
      return NomeDoCampo + " deve iniciar com \'r_\'!";

   Expressao = new RegExp("[^A-Za-z0-9._-]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " somente pode conter caracteres alfanuméricos alem de '\.\', \'_\' e \'-\'!";

   return "";
}

function Inclui(Operacao)
{
	if (Operacao == "inclusao")
	{
      Retorno = VerificaCampo("Configuracao", document.form1.configuracao.value, 5, 20);
      if (Retorno.length > 0)
      {
         alert (Retorno);
         return false;
      }
	}
	else
	{
		if (document.form1.ListaConfiguracoes.selectedIndex == -1)
		{
			alert ("Selecione uma configuração para alterá-la!");
			return false;
		}
	}

	{	
		// Verifica se tem servidor
		var Indice = document.form1.ListaServidores.selectedIndex;
		if (Indice == -1)
		{
			alert ("É necessário associar a nova configuração a um servidor!");
			return false;
		}

		// Verifica se tem reprocessador
		Indice = document.form1.ListaReprocessadores.selectedIndex;
		if (Indice == -1)
		{
			alert ("É necessário associar a nova configuração a um reprocessador!");
			return false;
		}

		// Verifica se tem bilhetador
		var Bilhetadores = "";
		for (i = 0; i < document.form1.ListaBilhetadores.length; i++)
		{
			if (document.form1.ListaBilhetadores.options[i].selected == true)
			{
				Bilhetadores += document.form1.ListaBilhetadores.options[i].text;
				Bilhetadores += ",";
			}
		}
		
		if (Bilhetadores == "")
		{
			alert ("É necessário associar a nova configuração a um ou mais bilhetadores!");
			return false;
		}
		else
			Bilhetadores = Bilhetadores.substring(0, Bilhetadores.length-1); // Retira o último ";"

		// Preenche o formulário 2
		if (Operacao == "inclusao")
		{
			document.form2.configuracao.value = document.form1.configuracao.value;
			document.form2.servidor.value = document.form1.ListaServidores.options[document.form1.ListaServidores.selectedIndex].text;
			document.form2.reprocessador.value = document.form1.ListaReprocessadores.options[document.form1.ListaReprocessadores.selectedIndex].text;
			document.form2.bilhetadores.value = Bilhetadores;
		}
		else
		{
			document.form4.configuracao.value = document.form1.ListaConfiguracoes.options[document.form1.ListaConfiguracoes.selectedIndex].text;
			document.form4.servidor.value = document.form1.ListaServidores.options[document.form1.ListaServidores.selectedIndex].text;
			document.form4.reprocessador.value = document.form1.ListaReprocessadores.options[document.form1.ListaReprocessadores.selectedIndex].text;
			document.form4.bilhetadores.value = Bilhetadores;
		}
		return true;
	}	
}

function Exclui()
{
	if (document.form1.ListaConfiguracoes.length == 0)
	{
		alert ("Não há configuração para apagar!");
		return false;
	}

	var Indice = document.form1.ListaConfiguracoes.selectedIndex;
	if (Indice != -1)
	{
		// Preenche o formulário 3
		document.form3.configuracao.value = document.form1.ListaConfiguracoes.options[Indice].text;
		return true;
	}
	else 
	{
		alert ("Selecione uma configuração para apagá-la!");
		return false;
	}
}