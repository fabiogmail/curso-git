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
			selecionaTecnologias();
			selecionaRelatorios();
			marcaSigiloTelefonico();
			break;
		default:
			alert ("Função não encontrada!");
			break;
	}
}

function selecionaTecnologias(){
	var lista = document.form1.ListaTec.value;
	var listaIds = document.form1.ListaIdTec.value;
	if(lista != null){
		var aListaTecnologias = lista.split("@");
		var aListaTecnologiasIds = listaIds.split("@");
		//indice do perfil
		var indice = document.form1.ListaPerfis.selectedIndex;
		if (indice != -1)
		{
			if(aListaTecnologias[indice] == null){
				alert("Erro! Lista esta nula");
				return;
			}
			if(aListaTecnologias[indice] == "todas" || aListaTecnologias[indice] == "null"){
					document.form1.tipoEscolha[0].checked = true;
					ocultarObjeto("selecionaTec");
			}else{
				//coloca tudo de volta nas tecnologias disponiveis
				for (i = 0; i < document.form1.ListaTecnologiasPermitidas.length; i++)
				   {      
				     Valor = document.form1.ListaTecnologiasPermitidas.options[i].value;
			         Texto = document.form1.ListaTecnologiasPermitidas.options[i].text;
			         document.form1.ListaTecnologiasDisponiveis.options[document.form1.ListaTecnologiasDisponiveis.length] = new Option(Texto, Valor, false, false);
			         document.form1.ListaTecnologiasPermitidas.options[i] = null;
			         i--;         
				   }
				//aqui ele coloca so as tecnologias que estao associadas a cada perfil
				document.form1.tipoEscolha[1].checked = true;
				mostrarObjeto("selecionaTec");
				var aListaTecnologiasII = aListaTecnologias[indice].split(";");	
				var aListaTecnologiasIIIds = aListaTecnologiasIds[indice].split(";");
				for (i = 0; i < aListaTecnologiasII.length; i++)
				{
					document.form1.ListaTecnologiasPermitidas[i] = new Option(aListaTecnologiasII[i], aListaTecnologiasIIIds[i], false, false);
					for ( j = 0; j < document.form1.ListaTecnologiasDisponiveis.length; j++)
					{     
					      if (document.form1.ListaTecnologiasDisponiveis[j].text == document.form1.ListaTecnologiasPermitidas[i].text)
					      {
					         document.form1.ListaTecnologiasDisponiveis.options[j] = null;
					         break;
					      }         
					}					
					sortSelect(document.form1.ListaTecnologiasDisponiveis, compareText);
				}			
			}
		}
	}
}

function selecionaRelatorios()
{
	var lista = document.form1.ListaRel.value;
	var listaIds = document.form1.ListaIdRel.value;
	if(lista != null){
		var aListaRelatorios = lista.split("@");
		var aListaRelatoriosIds = listaIds.split("@");
		//indice do perfil
		var indice = document.form1.ListaPerfis.selectedIndex;
		if (indice != -1)
		{
			if(aListaRelatorios[indice] == null){
				alert("Erro! Lista esta nula");
				return;
			}
			if(aListaRelatorios[indice] == "todas" || aListaRelatorios[indice] == "null"){
					document.form1.tipoEscolhaRel[0].checked = true;
					ocultarObjeto("selecionaRel");
			}else{
				//coloca tudo de volta nos relatorios disponiveis
				for (i = 0; i < document.form1.ListaRelatoriosPermitidos.length; i++)
				   {      
				     Valor = document.form1.ListaRelatoriosPermitidos.options[i].value;
			         Texto = document.form1.ListaRelatoriosPermitidos.options[i].text;
			         document.form1.ListaRelatoriosDisponiveis.options[document.form1.ListaRelatoriosDisponiveis.length] = new Option(Texto, Valor, false, false);
			         document.form1.ListaRelatoriosPermitidos.options[i] = null;
			         i--;         
				   }
				//aqui ele coloca so os relatorios que estao associadas a cada perfil
				document.form1.tipoEscolhaRel[1].checked = true;
				mostrarObjeto("selecionaRel");
				var aListaRelatoriosII = aListaRelatorios[indice].split(";");	
				var aListaRelatoriosIIIds = aListaRelatoriosIds[indice].split(";");
				for (i = 0; i < aListaRelatoriosII.length; i++)
				{
					document.form1.ListaRelatoriosPermitidos[i] = new Option(aListaRelatoriosII[i], aListaRelatoriosIIIds[i], false, false);
					for ( j = 0; j < document.form1.ListaRelatoriosDisponiveis.length; j++)
					{     
					      if (document.form1.ListaRelatoriosDisponiveis[j].text == document.form1.ListaRelatoriosPermitidos[i].text)
					      {
					         document.form1.ListaRelatoriosDisponiveis.options[j] = null;
					         break;
					      }         
					}					
					sortSelect(document.form1.ListaRelatoriosDisponiveis, compareText);
				}			
			}
		}
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
	else alert ("Servidor não enviou a lista de Perfis!");
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
    
    var tamanho = document.form1.ListaTecnologiasPermitidas.length;
	if (tamanho == 0 && document.form1.tipoEscolha[0].checked == false)
	{
		alert ("E necessario associar o perfil a uma tecnologia!");
		return false;
	}
	
	var Indice = document.form1.ListaModosDeAcesso.selectedIndex;
	if (document.form1.ListaModosDeAcesso.options[Indice].text == "admin")
	{
		alert ("Não se pode associar um outro perfil ao modo de acesso\"admin\"!");
		return false;
	}
	else
	{
		//---------------------tecnologias------------------------------
		var ids = "";
		for (i = 0; i < document.form1.ListaTecnologiasPermitidas.length; i++){
			valor = document.form1.ListaTecnologiasPermitidas.options[i].value;
			arrayTmp[i] = valor;
			ids += valor+";";
   		} 
   		
   		document.form2.tecnologias.value = ids;
   		
   		if(document.form1.tipoEscolha[0].checked){
   			document.form2.selecionaTodasTec.value = "true";
   		}
   		else{
   			document.form2.selecionaTodasTec.value = "false";
   		} 
   		
   		//--------------------------relatorios----------------------------
   		var idsRel = "";
		for (i = 0; i < document.form1.ListaRelatoriosPermitidos.length; i++){
			valor = document.form1.ListaRelatoriosPermitidos.options[i].value;
			arrayTmp[i] = valor;
			idsRel += valor+";";
   		} 
   		
   		document.form2.relatorios.value = idsRel;
   		
   		if(document.form1.tipoEscolhaRel[0].checked){
   			document.form2.selecionaTodosRel.value = "true";
   		}
   		else{
   			document.form2.selecionaTodosRel.value = "false";
   		}
   		
   		//-----------------------sigiloTelefonico-------------------------
   		
   		if(document.form1.habilita.checked){
   			document.form2.habilitaSigilo.value ="true";
   		}
   		else{
   			document.form2.habilitaSigilo.value ="false";
   		}
 
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
		alert ("N?o h? perfis para apagar!");
		return false;
	}

	var Indice = document.form1.ListaPerfis.selectedIndex;
	if (Indice != -1)
	{
		if (document.form1.ListaPerfis.options[Indice].text == "admin")
		{
			alert ("O perfil \"admin\" n&atilde;o pode ser exclu?do!");
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

function AdicionaPermissao()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.form1.ListaTecnologiasDisponiveis.selectedIndex == -1)
   {
      alert("Selecione um perfil para alterar sua permissão");
      return;
   }

   for (i = 0; i < document.form1.ListaTecnologiasDisponiveis.length; i++)
   {      
      if (document.form1.ListaTecnologiasDisponiveis.options[i].selected == true)
      {
         Valor = document.form1.ListaTecnologiasDisponiveis.options[i].value;
         Texto = document.form1.ListaTecnologiasDisponiveis.options[i].text;
         document.form1.ListaTecnologiasPermitidas.options[document.form1.ListaTecnologiasPermitidas.length] = new Option(Texto, Valor, false, false);
         document.form1.ListaTecnologiasDisponiveis.options[i] = null;
         i--;
      }         
   }

   sortSelect(document.form1.ListaTecnologiasPermitidas, compareText);
}

function AdicionaPermissaoRel()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.form1.ListaRelatoriosDisponiveis.selectedIndex == -1)
   {
      alert("Selecione um relat?rio para alterar sua permiss?o");
      return;
   }

   for (i = 0; i < document.form1.ListaRelatoriosDisponiveis.length; i++)
   {      
      if (document.form1.ListaRelatoriosDisponiveis.options[i].selected == true)
      {
         Valor = document.form1.ListaRelatoriosDisponiveis.options[i].value;
         Texto = document.form1.ListaRelatoriosDisponiveis.options[i].text;
         document.form1.ListaRelatoriosPermitidos.options[document.form1.ListaRelatoriosPermitidos.length] = new Option(Texto, Valor, false, false);
         document.form1.ListaRelatoriosDisponiveis.options[i] = null;
         i--;
      }         
   }

   sortSelect(document.form1.ListaRelatoriosPermitidos, compareText);
}

function RemovePermissao()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.form1.ListaTecnologiasPermitidas.selectedIndex == -1)
   {
      alert("Selecione um perfil para alterar sua permissão!");
      return;
   }

   for (i = 0; i < document.form1.ListaTecnologiasPermitidas.length; i++)
   {      
      if (document.form1.ListaTecnologiasPermitidas.options[i].selected == true)
      {
         Valor = document.form1.ListaTecnologiasPermitidas.options[i].value;
         Texto = document.form1.ListaTecnologiasPermitidas.options[i].text;
         document.form1.ListaTecnologiasDisponiveis.options[document.form1.ListaTecnologiasDisponiveis.length] = new Option(Texto, Valor, false, false);
         document.form1.ListaTecnologiasPermitidas.options[i] = null;
         i--;
      }         
   }

   sortSelect(document.form1.ListaTecnologiasDisponiveis, compareText);
}

function RemovePermissaoRel()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.form1.ListaRelatoriosPermitidos.selectedIndex == -1)
   {
      alert("Selecione um relat?rio para alterar sua permiss?o!");
      return;
   }

   for (i = 0; i < document.form1.ListaRelatoriosPermitidos.length; i++)
   {      
      if (document.form1.ListaRelatoriosPermitidos.options[i].selected == true)
      {
         Valor = document.form1.ListaRelatoriosPermitidos.options[i].value;
         Texto = document.form1.ListaRelatoriosPermitidos.options[i].text;
         document.form1.ListaRelatoriosDisponiveis.options[document.form1.ListaRelatoriosDisponiveis.length] = new Option(Texto, Valor, false, false);
         document.form1.ListaRelatoriosPermitidos.options[i] = null;
         i--;
      }         
   }

   sortSelect(document.form1.ListaRelatoriosDisponiveis, compareText);
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

