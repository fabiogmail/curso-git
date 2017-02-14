var aListaPerfis, aListaPerfisId, aListaPermissoesID, aListaTipoRel;

function MontaListaPerfis()
{
   var ListaPerfis       = document.form1.ListaPerfisArg.value;
   var ListaPerfisId     = document.form1.ListaPerfisIdArg.value;
   var ListaPermissoes   = document.form1.ListaPermissoesArg.value;
   var ListaTipoRel      = document.form1.ListaTipoRelArg.value;
   var Mensagem          = document.form1.MensagemArg.value;

   if (Mensagem.charAt(0) != "$") alert (Mensagem);

   aListaPermissoesID = ListaPermissoes.split("@");
   aListaPerfis       = ListaPerfis.split(";");
   aListaPerfisId     = ListaPerfisId.split(";");
   aListaTipoRel      = ListaTipoRel.split(";");

   for (i = 0; i < aListaPerfis.length; i++)
   {
			document.form1.ListaPerfis[i]            = new Option(aListaPerfis[i], aListaPerfisId[i], false, false);
         document.form1.ListaPerfisDisponiveis[i] = new Option(aListaPerfis[i], aListaPerfisId[i], false, false);         
   }
	document.form1.ListaPerfis.selectedIndex = -1;  

   for (i=0;i<aListaTipoRel.length; i++)
   {
      aTipoRel = aListaTipoRel[i].split(":");
      document.form1.ListaTipoRel[i] = new Option(aTipoRel[1], aTipoRel[0], false, false);
   }
   sortSelect(document.form1.ListaTipoRel, compareText);
}

function MontaRelacionamentos()
{
   var PermissaoAux, Permissao, Chave, aListaPerfisPermitidosID;
   var Encontrou = false;
   var PerfilSelecionado;
   
   if (document.form1.ListaPerfis.selectedIndex < 0)
      return;

   PerfilSelecionado = document.form1.ListaPerfis.options[document.form1.ListaPerfis.selectedIndex].value;
   Chave = PerfilSelecionado + "-" + document.form1.ListaTipoRel[document.form1.ListaTipoRel.selectedIndex].value;

   for (i = 0; i < aListaPermissoesID.length; i++)
   {
      PermissaoAux = aListaPermissoesID[i].split(":");
      if (PermissaoAux[0] == Chave)
      {
         Permissao = PermissaoAux[1];
         Encontrou = true;
         break;
      }
   } 

   // Limpando as listas
   while (document.form1.ListaPerfisDisponiveis.length > 0)
      document.form1.ListaPerfisDisponiveis[0] = null;
   
   while (document.form1.ListaPerfisPermitidos.length > 0)
      document.form1.ListaPerfisPermitidos[0] = null;

   if (Encontrou)
   {
      var Permitido = false;
      var IndexPermitido = 0; 
      var IndexDisponivel = 0;
      var Valor;
      var Texto;
      aListaPerfisPermitidosID = Permissao.split(";");
      

      // Populando as listas de permitdos e nao permitidos
      for (i = 0; i < document.form1.ListaPerfis.length; i++)
      {         
         Permitido = false;
         Valor = document.form1.ListaPerfis.options[i].value;
         Texto = document.form1.ListaPerfis.options[i].text;
         for (j = 0; j < aListaPerfisPermitidosID.length; j++)
         {
            if (Valor == aListaPerfisPermitidosID[j])
            {
               Permitido = true;
               // adiciona na lista de permitidos
               document.form1.ListaPerfisPermitidos[IndexPermitido] = new Option(Texto, Valor, false, false);
               IndexPermitido++;
               break;
            }           
         }
         if (!Permitido && (PerfilSelecionado != Valor) )
         {
            //adiciona na lista de disponivel
            document.form1.ListaPerfisDisponiveis[IndexDisponivel] = new Option(Texto, Valor, false, false);
            IndexDisponivel++;
         }
      }
   }
   else
   {
      var j = 0;
      for (i = 0; i < document.form1.ListaPerfis.length; i++)
      {         
         Valor = document.form1.ListaPerfis.options[i].value;
         Texto = document.form1.ListaPerfis.options[i].text;
         if (PerfilSelecionado != Valor)
         {
            document.form1.ListaPerfisDisponiveis[j] = new Option(Texto, Valor, false, false);
            j++;
         }
      }

      while (document.form1.ListaPerfisPermitidos.length > 0)
         document.form1.ListaPerfisPermitidos[0] = null;
   }

   sortSelect(document.form1.ListaPerfisPermitidos, compareText);
   sortSelect(document.form1.ListaPerfisDisponiveis, compareText);

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



function AdicionaPermissao()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.form1.ListaPerfis.selectedIndex == -1)
   {
      alert("Selecione um perfil para alterar sua permissão");
      return;
   }

   for (i = 0; i < document.form1.ListaPerfisDisponiveis.length; i++)
   {      
      if (document.form1.ListaPerfisDisponiveis.options[i].selected == true)
      {
         Valor = document.form1.ListaPerfisDisponiveis.options[i].value;
         Texto = document.form1.ListaPerfisDisponiveis.options[i].text;
         document.form1.ListaPerfisPermitidos.options[document.form1.ListaPerfisPermitidos.length] = new Option(Texto, Valor, false, false);
         document.form1.ListaPerfisDisponiveis.options[i] = null;
         i--;
      }         
   }

   sortSelect(document.form1.ListaPerfisPermitidos, compareText);
}

function RemovePermissao()
{
   var Valor;
   var Texto;
   // Verificando se pode alterar
   if (document.form1.ListaPerfis.selectedIndex == -1)
   {
      alert("Selecione um perfil para alterar sua permissão");
      return;
   }

   for (i = 0; i < document.form1.ListaPerfisPermitidos.length; i++)
   {      
      if (document.form1.ListaPerfisPermitidos.options[i].selected == true)
      {
         Valor = document.form1.ListaPerfisPermitidos.options[i].value;
         Texto = document.form1.ListaPerfisPermitidos.options[i].text;
         document.form1.ListaPerfisDisponiveis.options[document.form1.ListaPerfisDisponiveis.length] = new Option(Texto, Valor, false, false);
         document.form1.ListaPerfisPermitidos.options[i] = null;
         i--;
      }         
   }

   sortSelect(document.form1.ListaPerfisDisponiveis, compareText);
}


function AtualizaPermissao()
{

   var IdPerfil;
   var Chave;
   var Permissao = new Array();

   if (document.form1.ListaPerfis.selectedIndex == -1)
   {
      alert("Nenhum perfil foi selecionado");
      return false;
   }

   // preenchendo o formulario 2
   Chave = "" + aListaPerfisId[document.form1.ListaPerfis.selectedIndex] + "-" + document.form1.ListaTipoRel[document.form1.ListaTipoRel.selectedIndex].value;

/*   Tentativa de garantir a permissao de acesso a mais de um tipo de relatório ao mesmo tempo
     para um determinado perfil. Nao foi feito devido à alteração necessária ainda na hora de apresentar
     os perfis permissionários para cada tipo de relatório...
   Chave = "" + aListaPerfisId[document.form1.ListaPerfis.selectedIndex] + "-";
   for (i = 0;i < document.form1.ListaTipoRel.length; i++)
   {
	   if (document.form1.ListaTipoRel[i].selected == true)
	   {
		   Chave += document.form1.ListaTipoRel[i].value+";";
		   //alert (document.form1.ListaTipoRel[i].text + " - " +document.form1.ListaTipoRel[i].value);
	   }
   }
   alert (Chave)
   return false;
*/

   document.form2.Chave.value = Chave;

   for (i = 0; i < document.form1.ListaPerfisPermitidos.length ; i++)
   {            
      IdPerfil = document.form1.ListaPerfisPermitidos.options[i].value;
      Permissao[i] = IdPerfil;
   }
   document.form2.ListaPerfisPermitidos.value = Permissao;  

   return true;
}

