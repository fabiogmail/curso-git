var aListaPerfis, aListaPerfisId, aListaPermissoesID, aListaTipoRel, PerfilLogado;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
         VerificaMensagem();
			MontaListaPerfis();
			return true;
		case 1: 
			return ValidaForm();
		default:
			alert ("Funcao nao encontrada!");
			return false;
	}
}

function VerificaMensagem()
{
   var Mensagem = document.form1.MensagemArg.value;
   if (Mensagem.charAt(0) != "$") 
      alert (Mensagem);
}

function MontaListaPerfis()
{
   var aTipoRel;   
   var ListaPerfis       = document.form1.ListaPerfisArg.value;
   var ListaPerfisId     = document.form1.ListaPerfisIdArg.value;
   var ListaPermissoes   = document.form1.ListaPermissoesArg.value;

   aListaPermissoesID = ListaPermissoes.split("@");
   aListaPerfis       = ListaPerfis.split(";");
   aListaPerfisId     = ListaPerfisId.split(";");


   PerfilLogado = aListaPerfisId[0];

   for (i=0;i<aListaPerfis.length; i++)
         document.form1.ListaPerfis[i] = new Option(aListaPerfis[i], aListaPerfisId[i], false, false);


   sortSelect(document.form1.ListaPerfis, compareText);

   for (i=0;i<aListaPerfis.length; i++)
      if (document.form1.ListaPerfis.options[i].value == PerfilLogado)
      {
         document.form1.ListaPerfis.selectedIndex = i;
         break;
      }
}


function compareText (option1, option2) {
  return option1.text < option2.text ? -1 :
    option1.text > option2.text ? 1 : 0;
}

function sortSelect (select, compareFunction) {
  if (!compareFunction)
    compareFunction = compareText;
  var options = new Array (select.options.length);
  for (var i = 0; i < options.length; i++)
    options[i] = 
      new Option (
        select.options[i].text,
        select.options[i].value,
        select.options[i].defaultSelected,
        select.options[i].selected
      );
  options.sort(compareFunction);
  select.options.length = 0;
  for (var i = 0; i < options.length; i++)
    select.options[i] = options[i];
}


function ValidaForm()
{
   var TipoRelAux;
   var TipoRel;
   if (document.form1.ListaPerfis.selectedIndex == -1)
   {
      alert ("Selecione um perfil!");
      return false;
   }
   document.form2.operacao.value = "listaRelExportados";
    
   document.form2.perfil.value = document.form1.ListaPerfis.options[document.form1.ListaPerfis.selectedIndex].value;

   return true;
}



