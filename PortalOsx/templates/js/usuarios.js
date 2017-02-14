var aListaIds, aListaRelac;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
         VerificaMensagem();
			MontaListaUsuarios();
			MontaListaPerfis();
			break;
		case 1: 
			return Inclui();
			break;
		case 2: 
			return Exclui();
			break;
		case 3: 
			SelecionaPerfil();
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

function MontaListaUsuarios()
{
	Lista = document.form1.ListaUsuariosNomes.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes = document.form1.ListaUsuariosNomes.value;
		var ListaIds   = document.form1.ListaUsuariosIds.value;
		var ListaRelac = document.form1.Relacionamento.value;

		aListaNomes = ListaNomes.split(";");
		aListaIds   = ListaIds.split(";");
		aListaRelac = ListaRelac.split(";");

		while (document.form1.ListaUsuarios.length > 0)
			document.form1.ListaUsuarios.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			//alert (ListaPerfis[i]+" - "+aListaIds[i]+" - "+aListaRelac[i]);
			document.form1.ListaUsuarios[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaUsuarios.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Usuários!");
}

function MontaListaPerfis()
{
	Lista = document.form1.ListaPerfis.value;
	if (Lista.charAt(0) != "$")	
	{
		var ListaPerfis = document.form1.ListaPerfis.value;

		aListaPerfis = ListaPerfis.split(";");
		
		while (document.form1.ListaPerfisSel.length > 0)
			document.form1.ListaPerfisSel.options[0] = null;

		for (i = 0; i < aListaPerfis.length; i++)
		{
//			alert (aListaPerfis[i]+" - "+aListaIds[i]);
			document.form1.ListaPerfisSel[i] = new Option(aListaPerfis[i], "", false, false);
		}
		document.form1.ListaPerfisSel.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Perfis");
}

/*
 *	Seleciona o perfil correspondente ao usuário selecionado
 */
function SelecionaPerfil()
{
	var Indice = document.form1.ListaUsuarios.selectedIndex;
	if (Indice != -1)
	{
		for (i = 0; i < document.form1.ListaPerfisSel.length; i++)
		{
			//alert (aListaRelac[Indice] +" - "+ document.form1.ListaPerfisSel.options[i].text);
			if (aListaRelac[Indice] == document.form1.ListaPerfisSel.options[i].text)
			{
				document.form1.ListaPerfisSel.selectedIndex = i;
				//alert (document.form1.ListaPerfisSel.options[i].text);
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
      return NomeDoCampo + " somente pode conter caracteres alfanum&eacute;ricos!";

   return "";
}

function Inclui()
{
   Retorno = VerificaCampo("Usuario", document.form1.usuario.value, 3, 50);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }

	var Indice = document.form1.ListaPerfisSel.selectedIndex;
	if (Indice == -1)
	{
		alert ("É necessário associar o novo usuário a um perfil!");
		return false;
	}
	else if (document.form1.ListaPerfisSel.options[Indice].text == "admin")
	{
		alert ("Não se pode associar um outro usuario ao perfil \"admin\"!");
		return false;
	}
	else
	{
		// Preenche o formulário 2
		document.form2.usuario.value = document.form1.usuario.value;
		document.form2.perfil.value  = document.form1.ListaPerfisSel.options[Indice].text;
		//document.form2.ordem.value = document.form1.ordem.value;
		return true;
	}
}

function Exclui()
{
	if (document.form1.ListaUsuarios.length == 0)
	{
		alert ("Não há usuários para apagar!");
		return false;
	}

	var Indice = document.form1.ListaUsuarios.selectedIndex;
	if (Indice != -1)
	{
		if (document.form1.ListaUsuarios.options[Indice].text == "administrador")
		{
			alert ("O usuário \"administrador\" não pode ser excluído!");
			return false;
		}
		// Preenche o formulário 3
		document.form3.usuario.value = document.form1.ListaUsuarios.options[Indice].text;
		return true;
	}
	else 
	{
		alert ("Selecione um usuário para apagá-lo!");
		return false;
	}
}