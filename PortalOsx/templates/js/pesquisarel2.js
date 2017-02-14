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
         PesquisaSimples();
         return true;
		default:
			alert ("Função não encontrada!");
			return false;
	}
}

function PesquisaSimples()
{
   window.location = "/PortalOsx/servlet/Portal.cPortal?operacao=pesquisaRelatorios";
}


function verificaDataColeta()
{
   if (document.form1.DC_diainicio.selectedIndex == -1 ||
       document.form1.DC_mesinicio.selectedIndex == -1 ||
       document.form1.DC_anoinicio.selectedIndex == -1 || 
       document.form1.DC_diafim.selectedIndex == -1 ||
       document.form1.DC_mesfim.selectedIndex == -1 ||
       document.form1.DC_anofim.selectedIndex == -1 )

   {
      alert("Data de início ou fim de coleta vazia.");
      document.form1.DC_diainicio.focus;
   }
}

function Pesquisar()
{
   var NomeRel      = "";
   var Perfis       = "";
   var TiposRel     = "";
   var DC_Inicio    = "-";
   var DC_Fim       = "-";
   var DG_Inicio    = "-";
   var DG_Fim       = "-";
   var Bilhetadores = "";
   var Origem       = "";
   var Destino      = "";
   var RotaEnt      = "";
   var RotaSai      = "";
   var URLString    = "";
   var PC_Inicio    = "-";
   var PC_Fim       = "-";


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

   if (document.form1.DG_diainicio.selectedIndex != -1 &&
       document.form1.DG_mesinicio.selectedIndex != -1 &&
       document.form1.DG_anoinicio.selectedIndex != -1 )
   {
      DG_Inicio = document.form1.DG_anoinicio.options[document.form1.DG_anoinicio.selectedIndex].text;
      if (document.form1.DG_mesinicio.selectedIndex < 9)
         DG_Inicio += "0";
      DG_Inicio += document.form1.DG_mesinicio.selectedIndex + 1;
      DG_Inicio += document.form1.DG_diainicio.options[document.form1.DG_diainicio.selectedIndex].text;
   }

   if (document.form1.DG_diafim.selectedIndex != -1 &&
       document.form1.DG_mesfim.selectedIndex != -1 &&
       document.form1.DG_anofim.selectedIndex != -1 )
   {
      DG_Fim = document.form1.DG_anofim.options[document.form1.DG_anofim.selectedIndex].text;
      if (document.form1.DG_mesfim.selectedIndex < 9)
         DG_Fim += "0";
      DG_Fim += document.form1.DG_mesfim.selectedIndex + 1;
      DG_Fim += document.form1.DG_diafim.options[document.form1.DG_diafim.selectedIndex].text;
   }

   if (document.form1.DC_diainicio.selectedIndex != -1 &&
       document.form1.DC_mesinicio.selectedIndex != -1 &&
       document.form1.DC_anoinicio.selectedIndex != -1 )
   {
      DC_Inicio = document.form1.DC_anoinicio.options[document.form1.DC_anoinicio.selectedIndex].text;
      if (document.form1.DC_mesinicio.selectedIndex < 9)
         DC_Inicio += "0";
      DC_Inicio += document.form1.DC_mesinicio.selectedIndex + 1;
      DC_Inicio += document.form1.DC_diainicio.options[document.form1.DC_diainicio.selectedIndex].text;
   }

   if (document.form1.DC_diafim.selectedIndex != -1 &&
       document.form1.DC_mesfim.selectedIndex != -1 &&
       document.form1.DC_anofim.selectedIndex != -1 )
   {
      DC_Fim = document.form1.DG_anofim.options[document.form1.DC_anofim.selectedIndex].text;
      if (document.form1.DC_mesfim.selectedIndex < 9)
         DC_Fim += "0";
      DC_Fim += document.form1.DC_mesfim.selectedIndex + 1;
      DC_Fim += document.form1.DC_diafim.options[document.form1.DC_diafim.selectedIndex].text;
   }


   if (document.form1.horainicio.selectedIndex != -1 &&
       document.form1.minutoinicio.selectedIndex != -1 )
   {
      PC_Inicio = document.form1.horainicio.options[document.form1.horainicio.selectedIndex].text;
      PC_Inicio += document.form1.minutoinicio.options[document.form1.minutoinicio.selectedIndex].text;
   }

   if (document.form1.horafim.selectedIndex != -1 &&
       document.form1.minutofim.selectedIndex != -1 )
   {
      PC_Fim = document.form1.horafim.options[document.form1.horafim.selectedIndex].text;
      PC_Fim += document.form1.minutofim.options[document.form1.minutofim.selectedIndex].text;
   }


   Bilhetadores = document.form1.Bilhetadores.value;
   Origem  = document.form1.Origem.value;
   Destino = document.form1.Destino.value;
   RotaEnt = document.form1.RotaEnt.value;
   RotaSai = document.form1.RotaSai.value;

   //Retirando o ultimo ";"

   if (NomeRel.charAt(NomeRel.length - 1) == ';')
      NomeRel = NomeRel.substring(0, NomeRel.length - 1);
   if (Perfis.charAt(Perfis.length - 1) == ';')
      Perfis = Perfis.substring(0, Perfis.length - 1);
   if (TiposRel.charAt(TiposRel.length - 1) == ';')
      TiposRel = TiposRel.substring(0, TiposRel.length - 1);
   if (Bilhetadores.charAt(Bilhetadores.length - 1) == ';')
      Bilhetadores = Bilhetadores.substring(0, Bilhetadores.length - 1);
   if (Origem.charAt(Origem.length - 1) == ';')
      Origem = Origem.substring(0, Origem.length - 1);
   if (Destino.charAt(Destino.length - 1) == ';')
      Destino = Destino.substring(0, Destino.length - 1);
   if (RotaEnt.charAt(RotaEnt.length - 1) == ';')
      RotaEnt = RotaEnt.substring(0, RotaEnt.length - 1);
   if (RotaSai.charAt(RotaSai.length - 1) == ';')
      RotaSai = RotaSai.substring(0, RotaSai.length - 1);
    

   URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=pesquisaRelatorios&suboperacao=pesquisaAvancada" + 
               "&nomerel="        + NomeRel      +
               "&perfis="         + Perfis       +
               "&tiposrel="       + TiposRel     +
               "&datageracao="    + DG_Inicio    + "@" + DG_Fim +
               "&datacoleta="     + DC_Inicio    + "@" + DC_Fim +
               "&periodocoleta="  + PC_Inicio    + "@" + PC_Fim +
               "&bilhetadores="   + Bilhetadores + 
               "&origens="        + Origem       + 
               "&destinos="       + Destino      + 
               "&rotasent="       + RotaEnt      + 
               "&rotassai="       + RotaSai;

   window.location = URLString;   
}

function LimpaTudo()
{
   document.form1.NomeRel.value = "";
   document.form1.ListaPerfis.selectedIndex = -1;
   document.form1.ListaTipoRel.selectedIndex = -1;
   document.form1.DG_diainicio.selectedIndex = -1;
   document.form1.DG_mesinicio.selectedIndex = -1;
   document.form1.DG_anoinicio.selectedIndex = -1;
   document.form1.DG_diafim.selectedIndex = -1;
   document.form1.DG_mesfim.selectedIndex = -1;
   document.form1.DG_anofim.selectedIndex = -1;
   document.form1.DC_diainicio.selectedIndex = -1;
   document.form1.DC_mesinicio.selectedIndex = -1;
   document.form1.DC_anoinicio.selectedIndex = -1;
   document.form1.DC_diafim.selectedIndex = -1;
   document.form1.DC_mesfim.selectedIndex = -1;
   document.form1.DC_anofim.selectedIndex = -1;
   document.form1.horainicio.selectedIndex = -1;
   document.form1.minutoinicio.selectedIndex = -1;
   document.form1.horafim.selectedIndex = -1;
   document.form1.minutofim.selectedIndex = -1;
   document.form1.Bilhetadores.value = "";
   document.form1.Origem.value = "";
   document.form1.Destino.value = "";
   document.form1.RotaEnt.value = "";
   document.form1.RotaSai.value = "";

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


