function SelecionaConfiguracaoPadrao()
{
   document.configuracao.listatipovisualizacao.options[document.configuracao.tipovisualizacao.value].selected = true;
   document.configuracao.listaqtdlinhas.options[document.configuracao.qtdlinhas.value].selected = true;

   if (document.configuracao.apresentacabecalho.value == "1")
      document.configuracao.checkbox[0].checked = true;

   if (document.configuracao.apresentalogs.value == "1")
      document.configuracao.checkbox[1].checked = true;
}

function SelecionaConfiguracao()
{
   var Perfil = document.configuracao.perfil.value;
   var TipoRel = document.configuracao.tiporel.value;
   var IdRel = document.configuracao.idrel.value;
   var NomeArquivo = document.configuracao.arquivo.value;
   var NomeRelatorio = document.configuracao.nomerel.value;
   var DataGeracao = document.configuracao.datageracao.value;
   var bSelecionado = false;
   var Configuracao = "";
   var IndiceTipoVisualizacao = document.configuracao.listatipovisualizacao.selectedIndex;
   var IndiceQtdLinhas = document.configuracao.listaqtdlinhas.selectedIndex;
   var Periodos = "";

   if (TipoRel == "0")
      TipoRelatorio = "Desempenho";
   else if (TipoRel == "1")
      TipoRelatorio = "Pesquisa";
   else if (TipoRel == "2")
      TipoRelatorio = "DetalheChamadas";
   else if (TipoRel == "7")
      TipoRelatorio = "IndicadoresSMP";
   else if (TipoRel == "22")
      TipoRelatorio = "MinutosDeUso";
   else if (TipoRel == "23")
      TipoRelatorio = "Chamadas";
   else if (TipoRel == "24")
      TipoRelatorio = "DistribuicaoDeFrequencia";
   else if (TipoRel == "25")
      TipoRelatorio = "Perseveranca";
   else if (TipoRel == "26")
      TipoRelatorio = "AuditoriaChamadas";
   else if (TipoRel == "30")
         TipoRelatorio = "PesquisaIMEI";
   else if (TipoRel == "31")
         TipoRelatorio = "ChamadasLongaDuracao";
   else if (TipoRel == "32")
         TipoRelatorio = "DestinosEspecificos";
   else if (TipoRel == "33")
         TipoRelatorio = "DestinosComuns";
   else if (TipoRel == "34")
         TipoRelatorio = "PesquisaPorERB";
   else if (TipoRel == "35")
         TipoRelatorio = "PrefixosDeRisco";
   else if (TipoRel == "36")
         TipoRelatorio = "DesempenhoDeRede";
   else if (TipoRel == "37")
         TipoRelatorio = "QoS";
   else if (TipoRel == "49")
         TipoRelatorio = "Agrupado";
   else if (TipoRel == "48")
         TipoRelatorio = "Despesa";
   else if (TipoRel == "47")
         TipoRelatorio = "ITXReceita";
   else if (TipoRel == "46")
         TipoRelatorio = "GRE";
   else if (TipoRel == "45")
         TipoRelatorio = "TrendAnalysis";
   else if (TipoRel == "43")
         TipoRelatorio = "DWGPRS";
   else if (TipoRel == "44")
         TipoRelatorio = "DWGERAL";
   else if (TipoRel == "50")
       TipoRelatorio = "AnatelSMP3e4";
   else if (TipoRel == "51")
       TipoRelatorio = "AnatelSmp8e9";

   if (IndiceTipoVisualizacao == -1)
   {
      alert ("Não há informacões para serem apresentadas!");
      return false;
   }

   if (IndiceQtdLinhas == -1)
      IndiceQtdLinhas = 0;

   Configuracao = IndiceTipoVisualizacao+"";
   Configuracao += IndiceQtdLinhas+"";

   for (i = 0; i < document.configuracao.checkbox.length; i++)
   {
      if (document.configuracao.checkbox[i].checked == true)
         Configuracao += "1";
      else
         Configuracao += "0";
   }

   if (document.configuracao.temperiodos.value == "1")
   {
      for (i = 0; i < document.configuracao.listaperiodos.length; i++)
      {
         if (document.configuracao.listaperiodos.options[i].selected == true)
            Periodos += i +";";
      }
   }
   else Periodos = "0";

//alert ("Configuracao: "+Configuracao);

   window.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=alteraconfiguracao&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&configuracao="+Configuracao+"&periodos="+Periodos;

 //  for (i = 0; i < 1000; i++)
 //     for (j = 0; j < 1500; j++);
   window.close();

   JanelaIcones = window.opener.parent.frames[2];
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   JanelaRelatorio = window.opener.parent.frames[1];
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=meiorelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   

   return false;
}
