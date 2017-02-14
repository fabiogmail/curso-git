function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
	      VerificaMensagem();
			MontaListaPerfis();
         LimpaTudo();
			return true;
      case 1:
         Pesquisar();
         return true;
		case 2: 
         LimpaTudo();
			return false;
      case 3:
         PesquisaAvancada();
         return true;
		default:
			alert ("Função não encontrada!");
			return false;
	}
}


function PesquisaAvancada()
{
   window.location = "/PortalOsx/servlet/Portal.cPortal?operacao=pesquisaRelatorios&suboperacao=iniciaAvancada";
}

function Pesquisar()
{
   var NomeRel      = "";
   var Perfis       = "";
   var TiposRel     = "";

   NomeRel = document.form1.NomeRel.value;

   if (NomeRel == "") NomeRel = "*";

   if (document.form1.ListaPerfis.options.selectedIndex == -1)
   {
      for (i = 0; i < document.form1.ListaPerfis.length ; i++)
         Perfis += document.form1.ListaPerfis.options[i].value + ";";

   }
   else
   {
      for (i = 0; i < document.form1.ListaPerfis.length ; i++)
         if (document.form1.ListaPerfis.options[i].selected == true)
            Perfis += document.form1.ListaPerfis.options[i].value + ";";
   }

   if (document.form1.ListaTipoRel.options.selectedIndex == -1)
   {
      for (i = 0; i < document.form1.ListaTipoRel.length ; i++)
         TiposRel += document.form1.ListaTipoRel.options[i].value + ";";

   }
   else
   {
      for (i = 0; i < document.form1.ListaTipoRel.length ; i++)
         if (document.form1.ListaTipoRel.options[i].selected == true)
            TiposRel += document.form1.ListaTipoRel.options[i].value + ";";
   }

   //Retirando o ultimo ";"

   if (NomeRel.charAt(NomeRel.length - 1) == ';')
      NomeRel = NomeRel.substring(0, NomeRel.length - 1);
   if (Perfis.charAt(Perfis.length - 1) == ';')
      Perfis = Perfis.substring(0, Perfis.length - 1);
   if (TiposRel.charAt(TiposRel.length - 1) == ';')
      TiposRel = TiposRel.substring(0, TiposRel.length - 1);
   

   URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=pesquisaRelatorios&suboperacao=pesquisa" + 
               "&nomerel="  + NomeRel + 
               "&perfis="   + Perfis  +
               "&tiposrel=" + TiposRel;

   window.location = URLString;

}

function LimpaTudo()
{
   document.form1.NomeRel.value = "";
   document.form1.ListaPerfis.selectedIndex = -1;
   document.form1.ListaTipoRel.selectedIndex = -1;
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
   var ListaTipoRel      = document.form1.ListaTipoRelArg.value;
   var ListaPerfisId     = document.form1.ListaPerfisIdArg.value;

   aListaPerfis       = ListaPerfis.split(";");
   aListaTipoRel      = ListaTipoRel.split(";");
   aListaPerfisId     = ListaPerfisId.split(";");

   for (i=0;i<aListaPerfis.length; i++)
      document.form1.ListaPerfis[i] = new Option(aListaPerfis[i], aListaPerfisId[i], false, false);

   for (i=0;i<aListaTipoRel.length; i++)
   {
      aTipoRel = aListaTipoRel[i].split(":");
      document.form1.ListaTipoRel[i] = new Option(aTipoRel[1], aTipoRel[0], false, false);
   }

   sortSelect(document.form1.ListaTipoRel, compareText);
   sortSelect(document.form1.ListaPerfis, compareText);

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
      options[i] = 
               new Option (
                           select.options[i].text,
                           select.options[i].value,
                           select.options[i].defaultSelected,
                           select.options[i].selected );
   
   options.sort(compareFunction);
   select.options.length = 0;
   
   for (var i = 0; i < options.length; i++)
      select.options[i] = options[i];
}



