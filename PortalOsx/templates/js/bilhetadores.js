var aListaRelac, aApelidos, aFases;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
			VerificaMensagem();
			MontaListaBilhetadores();
			MontaListaTecnologias();
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
		var ListaNomes = document.form1.ListaBilhetadoresNomes.value;
		var ListaRelac = document.form1.Relacionamento.value;
		var ListaApelidos = document.form1.Apelidos.value;
		var ListaFases = document.form1.Fases.value;

		aListaNomes = ListaNomes.split(";");
		aListaRelac = ListaRelac.split(";");
      aFases      = ListaFases.split(";");
      aApelidos   = ListaApelidos.split(";");

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

/*
 *	Seleciona a Tecnologia correspondente ao bilhetador selecionado
 */
function SelecionaRelacionamentos()
{
	var Indice = document.form1.ListaBilhetadores.selectedIndex;

	if (Indice != -1)
	{
		for (i = 0; i < document.form1.ListaTecnologias.length; i++)
		{
			if (aListaRelac[Indice] == document.form1.ListaTecnologias.options[i].text)
			{
				document.form1.ListaTecnologias.selectedIndex = i;
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
	else
	{
		// Preenche o formulário 2
		document.form2.bilhetador.value = document.form1.bilhetador.value;
		document.form2.tecnologia.value = document.form1.ListaTecnologias.options[Indice].text;
		document.form2.apelido.value = document.form1.apelido.value;
		document.form2.fase.value = document.form1.Fase.options[document.form1.Fase.selectedIndex].value;
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
		alert ("Não há bilhetadores para alterar!");
		return false;
	}

	var Indice = document.form1.ListaBilhetadores.selectedIndex;
	if (Indice != -1)
	{
		// Preenche o formulário 4
		document.form4.bilhetador.value = document.form1.ListaBilhetadores.options[document.form1.ListaBilhetadores.selectedIndex].text;
		document.form4.bilhetador_novo.value = document.form1.bilhetador.value
		document.form4.tecnologia.value = document.form1.ListaTecnologias.options[document.form1.ListaTecnologias.selectedIndex].text;
		document.form4.apelido.value = document.form1.apelido.value;
		document.form4.fase.value = document.form1.Fase.options[document.form1.Fase.selectedIndex].value;

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