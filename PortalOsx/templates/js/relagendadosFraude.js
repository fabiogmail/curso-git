var aListaPerfis, aListaPerfisId, aListaPermissoesID, aListaTipoRel;

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
   var ListaTipoRel      = document.form1.ListaTipoRelArg.value;
   var listaPerfis 		 = document.form1.ListaPerfisArg.value;
   
   aListaTipoRel      = ListaTipoRel.split(";");

   for (i=0;i<aListaTipoRel.length; i++)
   {
      aTipoRel = aListaTipoRel[i].split(":");
      document.form1.ListaTipoRel[i] = new Option(aTipoRel[1], aTipoRel[0], false, false);
   }

   sortSelect(document.form1.ListaTipoRel, compareText);
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

   if (document.form1.ListaTipoRel.selectedIndex == -1)
   {
      alert ("Selecione um tipo de relat?rio!");
      return false;
   }

   TipoRelAux = document.form1.ListaTipoRel.options[document.form1.ListaTipoRel.selectedIndex].value;
   TipoRel = TipoRelAux.split("-");
   
   if (TipoRel[0] == 0)
      document.form2.operacao.value = "listaRelAgendados";
   else
      document.form2.operacao.value = "listaRelHistoricos";
    
   document.form2.perfil.value = document.form1.perfilAtual.value;
   document.form2.tiporel.value = TipoRel[1];

   return true;
}



