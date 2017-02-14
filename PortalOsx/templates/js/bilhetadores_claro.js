var aListaRelac, aApelidos, aFases, aListaOpRelac, aListaServRelac;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
			VerificaMensagem();
			MontaListaBilhetadores();
			MontaListaTecnologias();
			MontaListaOperadoras();
			MontaListaServidores();
			break;
		case 1: 
			return Inclui();
			break;
		case 2: 
			return Exclui();
			break;
		case 3: 
			SelecionaRelacionamentos();
			break;
		case 4: return Altera();
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

   document.form1.Fase.selectedIndex = -1;
}

function MontaListaBilhetadores()
{
	Lista = document.form1.ListaBilhetadoresNomes.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes    = document.form1.ListaBilhetadoresNomes.value;
		var ListaRelac    = document.form1.Relacionamento.value;
		var ListaOpRelac  = document.form1.OpRelacionamento.value;
		var ListaApelidos = document.form1.Apelidos.value;
		var ListaFases    = document.form1.Fases.value;
		var ListaServRelac = document.form1.ServRelacionamento.value;

		aListaNomes			 = ListaNomes.split(";");
		aListaRelac			 = ListaRelac.split(";");
		aListaOpRelac        = ListaOpRelac.split(";");
		aFases				 = ListaFases.split(";");
		aApelidos			 = ListaApelidos.split(";");
		aListaServRelac      = ListaServRelac.split(";");

		for (i = 0; i < aFases.length; i++)
			aFases[i] = Math.ceil(aFases[i]);

		while (document.form1.ListaBilhetadores.length > 0)
			document.form1.ListaBilhetadores.options[0] = null;
		for (i = 0; i < aListaNomes.length; i++)
			document.form1.ListaBilhetadores[i] = new Option(aListaNomes[i], "", false, false);

		document.form1.ListaBilhetadores.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Bilhetadores!");
}

function MontaListaTecnologias()
{
	Lista = document.form1.ListaTecnologiasNomes.value;
	if (Lista.charAt(0) != "$")	
	{
		var ListaTecnologias = document.form1.ListaTecnologiasNomes.value;
		aListaTecnologias = ListaTecnologias.split(";");
		
		while (document.form1.ListaTecnologias.length > 0)
			document.form1.ListaTecnologias.options[0] = null;

		for (i = 0; i < aListaTecnologias.length; i++)
		{
			document.form1.ListaTecnologias[i] = new Option(aListaTecnologias[i], "", false, false);
		}
		document.form1.ListaTecnologias.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Tecnologias");
}


function MontaListaOperadoras()
{
	Lista = document.form1.ListaOperadorasNomes.value;
	if (Lista.charAt(0) != "$")	
	{
		var ListaOperadoras = document.form1.ListaOperadorasNomes.value;
		aListaOperadoras = ListaOperadoras.split(";");
		
		while (document.form1.ListaOperadoras.length > 0)
			document.form1.ListaOperadoras.options[0] = null;

		for (i = 0; i < aListaOperadoras.length; i++)
		{
			document.form1.ListaOperadoras[i] = new Option(aListaOperadoras[i], "", false, false);
		}
		document.form1.ListaOperadoras.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Operadoras");
}

function MontaListaServidores()
{
	Lista = document.form1.ListaServidoresNomes.value;
	if (Lista.charAt(0) != "$")	
	{
		var ListaServidores = document.form1.ListaServidoresNomes.value;
		aListaServidores = ListaServidores.split(";");
		while (document.form1.ListaServidores.length > 0)
			document.form1.ListaServidores.options[0] = null;
		for (i = 0; i < aListaServidores.length; i++)
		{
			document.form1.ListaServidores[i] = new Option(aListaServidores[i], "", false, false);
		}
		document.form1.ListaServidores.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Servidores");
}

/*
 *	Seleciona a Tecnologia correspondente ao bilhetador selecionado
 */
function SelecionaRelacionamentos()
{
	var Indice = document.form1.ListaBilhetadores.selectedIndex;

	if (Indice != -1)
	{
		// Colocando na situacao inicial
		document.form1.ListaTecnologias.selectedIndex = -1;
		document.form1.ListaOperadoras.selectedIndex  = -1;				

		//Lista de Tecnologias
		for (i = 0; i < document.form1.ListaTecnologias.length; i++)
		{
			if (aListaRelac[Indice] == document.form1.ListaTecnologias.options[i].text)
			{
				document.form1.ListaTecnologias.selectedIndex = i;
				break;
			}
		}
		// Lista de Operadoras
		for (i = 0; i < document.form1.ListaOperadoras.length; i++)
		{
			if (aListaOpRelac[Indice] == document.form1.ListaOperadoras.options[i].text)
			{
				document.form1.ListaOperadoras.selectedIndex = i;				
				break;
			}
		}
		
		// Lista de Servidores
		for (i = 0; i < document.form1.ListaServidores.length; i++)
		{
			if (aListaServRelac[Indice] == document.form1.ListaServidores.options[i].text)
			{
				document.form1.ListaServidores.selectedIndex = i;				
				break;
			}
		}
		
		document.form1.apelido.value = aApelidos[Indice];
		document.form1.Fase.selectedIndex = aFases[Indice];
	}
}

function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMinimo, TamMaximo)
{
   if (ValorDoCampo.length < TamMinimo)
      return NomeDoCampo + " deve ter no mínimo " + TamMinimo + " caracteres!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ter no máximo " + TamMaximo + " caracteres!";

   Expressao = new RegExp("[^A-Za-z0-9._-]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " somente pode conter caracteres alfanuméricos além de '\.\', \'_\' e \'-\'!";

   return "";
}

function Inclui()
{
	Retorno = VerificaCampo("Bilhetador", document.form1.bilhetador.value, 3, 15);
	if (Retorno.length > 0)
	{
	  alert (Retorno);
	  return false;
	}
	Retorno = VerificaCampo("Apelido", document.form1.apelido.value, 3, 15);
	if (Retorno.length > 0)
	{
	  alert (Retorno);
	  return false;
	}
	var Indice = document.form1.ListaTecnologias.selectedIndex;
	if (Indice == -1)
	{
		alert ("É necessário associar o novo bilhetador a uma tecnologia!");
		return false;
	}
	else if (document.form1.Fase.selectedIndex == -1)
	{
		alert ("Selecione a fase do bilhetador: teste ou produção!");
		return false;
	}
	else if (document.form1.ListaOperadoras.selectedIndex == -1)
	{
		alert ("Selecione a regional do bilhetador!");
		return false;
	}
	else if (document.form1.ListaServidores.selectedIndex == -1)
	{
		alert ("Selecione o servidor responsável pelo bilhetador!");
		return false;
	}
	else
	{
		// Preenche o formulário 2
		document.form2.bilhetador.value = document.form1.bilhetador.value;
		document.form2.tecnologia.value = document.form1.ListaTecnologias.options[Indice].text;
		document.form2.operadora.value	= document.form1.ListaOperadoras.options[document.form1.ListaOperadoras.selectedIndex].text;
		document.form2.servidor.value	= document.form1.ListaServidores.options[document.form1.ListaServidores.selectedIndex].text;
		document.form2.apelido.value	= document.form1.apelido.value;
		document.form2.fase.value		= document.form1.Fase.options[document.form1.Fase.selectedIndex].value;
		return true;
	}
}

function Exclui()
{
	if (document.form1.ListaBilhetadores.length == 0)
	{
		alert ("Não há bilhetadores para apagar!");
		return false;
	}

	var Indice = document.form1.ListaBilhetadores.selectedIndex;
	if (Indice != -1)
	{
		// Preenche o formulário 3
		document.form3.bilhetador.value = document.form1.ListaBilhetadores.options[Indice].text;
		return true;
	}
	else 
	{
		alert ("Selecione um bilhetador para apagá-lo!");
		return false;
	}
}
function Altera()
{
	if (document.form1.ListaBilhetadores.length == 0)
	{
		alert ("Náo há bilhetadores para alterar!");
		return false;
	}

	var Indice = document.form1.ListaBilhetadores.selectedIndex;
	if (Indice != -1)
	{
		// Preenche o formulário 4
		document.form4.bilhetador.value			= document.form1.ListaBilhetadores.options[document.form1.ListaBilhetadores.selectedIndex].text;
		document.form4.bilhetador_novo.value	= document.form1.bilhetador.value
		document.form4.tecnologia.value			= document.form1.ListaTecnologias.options[document.form1.ListaTecnologias.selectedIndex].text;
		document.form4.operadora.value			= document.form1.ListaOperadoras.options[document.form1.ListaOperadoras.selectedIndex].text;
		document.form4.apelido.value			= document.form1.apelido.value;
		document.form4.fase.value				= document.form1.Fase.options[document.form1.Fase.selectedIndex].value;

		Retorno = VerificaCampo("Bilhetador", document.form4.bilhetador.value, 3, 15);
		if (Retorno.length > 0)
		{
			alert (Retorno);
			return false;
		}
		if (document.form4.bilhetador_novo.value == "")
		{
			document.form4.bilhetador_novo.value = document.form4.bilhetador.value;
		}
		Retorno = VerificaCampo("Bilhetador", document.form4.bilhetador_novo.value, 3, 15);
		if (Retorno.length > 0)
		{
			alert (Retorno);
			return false;
		}

		Retorno = VerificaCampo("Apelido", document.form4.apelido.value, 3, 15);
		if (Retorno.length > 0)
		{
		   alert (Retorno);
		   return false;
		}
		return true;
	}
	else 
	{
		alert ("Selecione um bilhetador para alterá-lo!");
		return false;
	}
}