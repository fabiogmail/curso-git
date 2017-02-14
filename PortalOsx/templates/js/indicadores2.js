function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
			MontaListaIndicadoresDisponiveis();
         MontaListaIndicadoresSelecionados();
			break;
		case 1: 
			return Adicionar();
			break;
		case 2: 
			return Remover();
			break;
		case 3: 
			return ValidaForm();
			break;
		default:
			alert ("Função não encontrada!");
			break;
	}
}


function MontaListaIndicadoresDisponiveis()
{
	Lista = document.form1.ListaIndicadoresDisponiveis.value;
	if (Lista.charAt(0) != "$")
	{
		aListaNomes = Lista.split(";");

		while (document.form1.ListaDisponiveis.length > 0)
			document.form1.ListaDisponiveis.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
			document.form1.ListaDisponiveis[i] = new Option(aListaNomes[i], "", false, false);
		document.form1.ListaDisponiveis.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Indicadores!");
}

function MontaListaIndicadoresSelecionados()
{
	Lista = document.form1.ListaIndicadoresSelecionados.value;
	if (Lista.charAt(0) != "$")
	{
		aListaNomes = Lista.split(";");

		while (document.form1.ListaSelecionados.length > 0)
			document.form1.ListaSelecionados.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
			document.form1.ListaSelecionados[i] = new Option(aListaNomes[i], "", false, false);
		document.form1.ListaSelecionados.selectedIndex = -1;
	}
}

function Adicionar()
{
   aSelecionados = new Array();
   Limite = false, Existe = false;

   Cont = 0;

   for (i = 0; i < document.form1.ListaDisponiveis.length; i++)
   {
      if (document.form1.ListaDisponiveis.options[i].selected == true)
      {
         Existe = false;
         for (j = 0; j < document.form1.ListaSelecionados.length; j++)
            if (document.form1.ListaSelecionados.options[j].text == document.form1.ListaDisponiveis.options[i].text)
            {
               Existe = true;
               break;
            }

         if (Existe == false)
         {
            aSelecionados[Cont] = document.form1.ListaDisponiveis.options[i].text;
            document.form1.ListaDisponiveis.options[i].selected = false;
            Cont++;
         }
      }
   }

   if (document.form1.ListaSelecionados.length < 20)
   {
      for (i = 0; i < aSelecionados.length; i++)
      {
         document.form1.ListaSelecionados[document.form1.ListaSelecionados.length] = new Option(aSelecionados[i], "", false, false);      
         if (document.form1.ListaSelecionados.length == 20)
         {
            Limite = true;
            break;
         }
      }
   }
   else Limite = true;

   if (Limite == true)
      alert ("Máximo de 20 colunas alcançado!");

   return true;
}

function Remover()
{
   aSelecionados = new Array();
   Cont = 0;

   for (i = 0; i < document.form1.ListaSelecionados.length; i++)
   {
      if (document.form1.ListaSelecionados.options[i].selected == true)
      {
         aSelecionados[Cont] = document.form1.ListaSelecionados.options[i].text;
         Cont++;
      }
   }

   Cont = 0;
   while (Cont < aSelecionados.length)
   {
      for (i = 0; i < document.form1.ListaSelecionados.length; i++)
      {
         if (document.form1.ListaSelecionados.options[i].text == aSelecionados[Cont])
         {
            document.form1.ListaSelecionados.options[i] = null;
            Cont++;
            break;
         }
      }
   }

   return true;
}


function ValidaForm()
{
   Indicadores = "";

   if (document.form1.ListaSelecionados.length == 0)
   {
      alert ("Não há indicadores selecionados!");
      return false;
   }

   for (i = 0; i < document.form1.ListaSelecionados.length; i++)
   {
      Indicadores = Indicadores + document.form1.ListaSelecionados.options[i].text + ";";
   }
   
   document.form2.indicadores.value = Indicadores;
   document.form2.target='AUX2';
   document.form2.submit();
}