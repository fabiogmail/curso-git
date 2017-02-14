function SelecionaConfiguracaoPadrao()
{
   document.configuracao.listatipovisualizacao.options[document.configuracao.tipovisualizacao.value].selected = true;

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
   var IndiceQtdLinhas = 0;
   var Periodos = "";

   TipoRelatorio = "Matraf";

   if (IndiceTipoVisualizacao == -1)
   {
      alert ("Não há informações para serem apresentadas!");
      return false;
   }

   Configuracao = IndiceTipoVisualizacao+"";
   Configuracao += IndiceQtdLinhas+"";

   for (i = 0; i < document.configuracao.checkbox.length; i++)
   {
      if (document.configuracao.checkbox[i].checked == true)
         Configuracao += "1";
      else
         Configuracao += "0";
   }

   window.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=alteraconfiguracao&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&configuracao="+Configuracao;

   for (i = 0; i < 1000; i++)
      for (j = 0; j < 500; j++);
   window.close();

   return false;
}
