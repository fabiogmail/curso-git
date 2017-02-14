/****
 * Argumentos na P?gina
 * --------------------
 * Formul?rio: Form1
 *    ListaPerfisNomes: lista de nomes de perfis existentes
 *    ListaPerfisIds: lista de ids dos perfis na mesma ordem da ListaPerfisNome
 *    ListaPerfisModosDeAcesso: lista de modos de acesso (relat?rio, gestor, ...)
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
			marcaSigiloTelefonico();
			break;
		default:
			alert ("Função não encontrada!");
			break;
	}
}

function marcaSigiloTelefonico(){
	var lista = document.form1.listaSigiloTefonico.value;
	if(lista != null){
		var aListaSigilo = lista.split(";");
		var Indice = document.form1.ListaPerfis.selectedIndex;
		if (Indice != -1)
		{
			if (aListaSigilo[Indice]=="true")
			{
				document.form1.habilita.checked = true;
			}else{
				document.form1.habilita.checked = false;
			}			
		}
	}
}

function VerificaMensagem()
{
    var Mensagem = document.form1.mensagem.value;
	if (Mensagem != null && Mensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function MontaListaPerfis()
{
	var Lista = document.form1.ListaPerfisNomes.value;
	if (Lista != null)
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
			document.form1.ListaPerfis[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaPerfis.selectedIndex = -1;
	}
	else alert ("Servidor n&atildeo enviou a lista de Perfis!");
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
			if (aListaRelac[Indice] == document.form1.ListaModosDeAcesso.options[i].text)
			{
				document.form1.ListaModosDeAcesso.selectedIndex = i;
				break;
			}
		}
	}
}

function VerificaCampo(NomeDoCampo, ValorDoCampo, TamMinimo, TamMaximo)
{
   if (ValorDoCampo.length < TamMinimo)
      return NomeDoCampo + " deve ter no minimo " + TamMinimo + " caracteres!";

   if (ValorDoCampo.length > TamMaximo)
      return NomeDoCampo + " deve ter no maximo " + TamMaximo + " caracteres!";

   Expressao = new RegExp("[^A-Za-z0-9]", "gi");
   Ret = ValorDoCampo.search(Expressao);
   if (Ret != -1)
      return NomeDoCampo + " somente pode conter caracteres alfanumericos!";

   return "";
}

function Inclui()
{
	var valor;
	var Retorno;
	var arrayTmp = new Array();
	var indicePerfil;
	var op;
	
	if(document.form1.perfil.value.length != 0)//se o usuario digitar o nome de um perfil novo
	{
    	Retorno = VerificaCampo("Perfil", document.form1.perfil.value, 3, 15);
    	if (Retorno.length > 0)
    	{
      		alert (Retorno);
      		return false;
    	}
    	op = "incPerfil";
    }else{//se ele n?o digitar e quiser alterar um perfil existente
    	indicePerfil = document.form1.ListaPerfis.selectedIndex;
    	if(indicePerfil == -1){
    		alert("Selecione um perfil ou digite o nome de um perfil novo");
    		return false;
    	}
    	op = "altPerfil";//se for ter altera??o mudar para outra op
    	document.form2.perfilAlteracao.value = document.form1.ListaPerfis.options[indicePerfil].text;
    }
	var Indice = document.form1.ListaModosDeAcesso.selectedIndex;
	if (document.form1.ListaModosDeAcesso.options[Indice].text == "admin")
	{
		alert ("Não se pode associar um outro perfil ao modo de acesso\"admin\"!");
		return false;
	}
	else
	{
   		if(document.form1.habilita.checked){
   			document.form2.habilitaSigilo.value ="true";
   		}
   		else{
   			document.form2.habilitaSigilo.value ="false";
   		}
 
   		document.form2.selecionaTodasTec.value = "true";
   		document.form2.selecionaTodosRel.value = "true";
   		document.form2.perfil.value = document.form1.perfil.value;
		document.form2.acesso.value = document.form1.ListaModosDeAcesso.options[Indice].text;;
		document.form2.operacao.value = op;
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
		// Preenche o formulario 3
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
		// Preenche o formulrio 4
		document.form4.perfil.value = document.form1.ListaPerfis.options[Indice].text;
		return true;
	}
	else 
	{
		alert ("Selecione um perfil para bloqueá-lo!");
		return false;
	}
}

function compareText (option1, option2)
{
  return option1.text < option2.text ? -1 :
    option1.text > option2.text ? 1 : 0;
}

function sortSelect (select, compareFunction)
{
  if (!compareFunction)
    compareFunction = compareText;
  var options = new Array (select.options.length);
  for (var i = 0; i < options.length; i++)
    options[i] = new Option (select.options[i].text, select.options[i].value, select.options[i].defaultSelected, select.options[i].selected);
  options.sort(compareFunction);
  select.options.length = 0;
  for (var i = 0; i < options.length; i++)
    select.options[i] = options[i];
}

function mostrarObjeto(id) {
	var el = document.getElementById(id).style; 	
	el.display = "block";		
}

function ocultarObjeto(id) {
	var el = document.getElementById(id).style; 	
	el.display = "none";		
}	

function alteraExibicao(id) {
	
	var el = document.getElementById(id).style; 
	
	if (el.display == "") {
		el.display = "none";
	}
	else if(el.display == "none") {
		el.display = "block";
	}
	else if(el.display == "block") {
		el.display = "none";
	}		
}

