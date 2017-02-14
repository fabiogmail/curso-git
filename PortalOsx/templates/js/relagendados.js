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
			alert ("Função não encontrada!");
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
   var ListaTipoRel      = document.form1.ListaTipoRelArg.value;

   aListaPermissoesID = ListaPermissoes.split("@");
   aListaPerfis       = ListaPerfis.split(";");
   aListaPerfisId     = ListaPerfisId.split(";");
   aListaTipoRel      = ListaTipoRel.split(";");

   PerfilLogado = aListaPerfisId[0];

   for (i=0;i<aListaPerfis.length; i++)
         document.form1.ListaPerfis[i] = new Option(aListaPerfis[i], aListaPerfisId[i], false, false);

   for (i=0;i<aListaTipoRel.length; i++)
   {
      aTipoRel = aListaTipoRel[i].split(":");
      document.form1.ListaTipoRel[i] = new Option(aTipoRel[1], aTipoRel[0], false, false);
   }

   sortSelect(document.form1.ListaTipoRel, compareText);
   sortSelect(document.form1.ListaPerfis, compareText);

   for (i=0;i<aListaPerfis.length; i++)
      if (document.form1.ListaPerfis.options[i].value == PerfilLogado)
      {
         document.form1.ListaPerfis.selectedIndex = i;
         break;
      }
}


function AtualizaTipoRel()
{
   var PermissaoAux;
   var IdTipoRel;
   var IndiceTipoRel;
   var PerfilId = document.form1.ListaPerfis.options[document.form1.ListaPerfis.selectedIndex].value;

   // usuario = admin
   if (aListaPermissoesID == "null")
      return;

   for (i=0;document.form1.ListaTipoRel.length;i++)
      document.form1.ListaTipoRel[0] = null;

   if (PerfilId == PerfilLogado)
   {
      for (i=0;i<aListaTipoRel.length; i++)
      {
         aTipoRel = aListaTipoRel[i].split(":");
         document.form1.ListaTipoRel[i] = new Option(aTipoRel[1], aTipoRel[0], false, false);
      }
      sortSelect(document.form1.ListaTipoRel, compareText);   
      return;
   }

   IndiceTipoRel = 0;
   for (i=0;i<aListaPermissoesID.length; i++)
   {
      PermissaoAux = aListaPermissoesID[i].split(":");      
      if (PermissaoAux[1].search(PerfilId) != -1)
      {
         IdTipoRel = PermissaoAux[0].slice(PermissaoAux[0].indexOf("-")+1);         
         for(j=0;j<aListaTipoRel.length; j++)
         {
            aTipoRel = aListaTipoRel[j].split(":");
            if (aTipoRel[0] == IdTipoRel)
            {
               document.form1.ListaTipoRel[IndiceTipoRel] = new Option(aTipoRel[1], aTipoRel[0], false, false);
               IndiceTipoRel++;
               break;
            }
         }
         sortSelect(document.form1.ListaTipoRel, compareText);
      }
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

   if (document.form1.ListaTipoRel.selectedIndex == -1)
   {
      alert ("Selecione um tipo de relatório!");
      return false;
   }

   TipoRelAux = document.form1.ListaTipoRel.options[document.form1.ListaTipoRel.selectedIndex].value;
   TipoRel = TipoRelAux.split("-");
   
   if (TipoRel[0] == 0)
      document.form2.operacao.value = "listaRelAgendados";
   else
      document.form2.operacao.value = "listaRelHistoricos";
    
   document.form2.perfil.value = document.form1.ListaPerfis.options[document.form1.ListaPerfis.selectedIndex].text;
   document.form2.tiporel.value = TipoRel[1];

   return true;
}

function AbreJanela (Perfil, TipoRel, IDRel, Arquivo)
{
      URLStr = "/PortalOsx/servlet/Portal.Portal?operacao=detalhaRelAgendado&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo;
alert (URLStr);

//&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+ColunaAux.elementAt(2)+"&arquivo="+ColunaAux.elementAt(0)
      auxiliar = window.open(URLStr,'Detalhes de Relat&oacute;rio Agendado','resizable=no,status=no,menubar=no,scrollbars=no,width=400,height=200');
      auxiliar.focus();
/*
      form.target='AUX';
      form.action = modulo+'/'+'rnd=d102qxXViN4_4SQemNAevtyffQ/51006103';
      form.nummsg.value = msgnum;
      form.uidl.value = uidl;
      form.acao.value = acao;
      form.pagina.value = pagina;
      form.campo.value = campo;
      form.method = 'POST';
      form.submit ();  
*/
}



