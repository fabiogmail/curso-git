/****
 * Argumentos na P?gina
 * --------------------
 * Formulário: Form1
 *    ListaPerfisNomes: lista de nomes de perfis existentes
 *    ListaUsuariosNomes: lista de nomes de usuários existentes
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
			MontaListaUsuarios();
			break;
		case 1: 
			return DesabilitaUsuarios();
			break;
		case 2: 
			return DesabilitaPerfis();
			break;
		case 3: 
			return LimpaForm();
			break;
		case 4: 
			return ValidaForm();
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

function MontaListaUsuarios()
{
	Lista = document.form1.ListaUsuariosNomes.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes = document.form1.ListaUsuariosNomes.value;
		aListaNomes = ListaNomes.split(";");

		while (document.form1.ListaUsuarios.length > 0)
			document.form1.ListaUsuarios.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			document.form1.ListaUsuarios[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaUsuarios.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Usuários!");
}

function DesabilitaPerfis()
{
   Tam = document.form1.ListaUsuarios.length;
   for (i = 0; i < Tam; i++)
      if (document.form1.ListaUsuarios.options[i].selected == true)
      {
         Tam = document.form1.ListaPerfis.length;
         for (i = 0; i < Tam; i++)
            document.form1.ListaPerfis.options[i].selected = false;

         break;
      }

   return true;
}

function DesabilitaUsuarios()
{
   Tam = document.form1.ListaPerfis.length;
   for (i = 0; i < Tam; i++)
      if (document.form1.ListaPerfis.options[i].selected == true)
      {
         Tam = document.form1.ListaUsuarios.length;
         for (i = 0; i < Tam; i++)
            document.form1.ListaUsuarios.options[i].selected = false;

         break;
      }

   return true;
}

function LimpaForm()
{
   Tam = document.form1.ListaPerfis.length;
   for (i = 0; i < Tam; i++)
      document.form1.ListaPerfis.options[i].selected = false;

   Tam = document.form1.ListaUsuarios.length;
   for (i = 0; i < Tam; i++)
      document.form1.ListaUsuarios.options[i].selected = false;

   document.form1.assunto.value = "";
   document.form1.textomsg.value = "";

   return false;
}

function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMinimo, TamMaximo)
{
   if (ValorDoCampo.length < TamMinimo)
      return NomeDoCampo + " deve ter no mínimo " + TamMinimo + " caracteres!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ter no máximo " + TamMaximo + " caracteres!";

   Expressao = new RegExp("[^A-Za-z0-9???????????????!() #]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " não pode conter os caracteres '\?\', \'.\', \'_\', \'=\', \'@\', \'$\' e \'-\'!";

   return "";
}

function ValidaForm()
{
   Perfis = "", Usuarios = "";

   Retorno = VerificaCampo("Assunto", document.form1.assunto.value, 3, 60);
   if (Retorno.length > 0)
   {
      alert (Retorno);
      return false;
   }
   
   Tam = document.form1.ListaPerfis.length;
   for (i = 0; i < Tam; i++)
   {
      if (document.form1.ListaPerfis.options[i].selected == true)
         Perfis += document.form1.ListaPerfis.options[i].text + ";";
   }
   if (Perfis.length != 0)
      Perfis = Perfis.substring(0, Perfis.length-1);

   Tam = document.form1.ListaUsuarios.length;
   for (i = 0; i < Tam; i++)
   {
      if (document.form1.ListaUsuarios.options[i].selected == true)
         Usuarios += document.form1.ListaUsuarios.options[i].text + ";";
   }
   if (Usuarios.length != 0)
      Usuarios = Usuarios.substring(0, Usuarios.length-1);

   if (Usuarios.length == 0 && Perfis.length == 0)
   {
      alert ("Destinatário não selecionado!");
      return false;
   }
/*
   if (document.form1.assunto.value.length == 0)
   {
      alert ("Assunto está vazio!");
      return false;      
   }
*/
   if (document.form1.textomsg.value.length == 0)
   {
      alert ("Mensagem está vazia!");
      return false;      
   }

   document.form2.perfis.value = Perfis;
   document.form2.usuarios.value = Usuarios;
   document.form2.assunto.value = document.form1.assunto.value;
   document.form2.mensagem.value = document.form1.textomsg.value;

   return true;
}
