function SelecionaConfiguracaoPadrao()
{
   document.configuracao.listatipovisualizacao.options[document.configuracao.tipovisualizacao.value].selected = true;
   document.configuracao.listaqtdlinhas.options[document.configuracao.qtdlinhas.value].selected = true;

   if (document.configuracao.apresentacabecalho.value == "1")
      document.configuracao.checkbox[0].checked = true;

   if (document.configuracao.apresentacentral.value == "1")
      document.configuracao.checkbox[1].checked = true;

   if (document.configuracao.apresentarotafabricante.value == "1")
      document.configuracao.checkbox[2].checked = true;

   if (document.configuracao.apresentarotaembratel.value == "1")
      document.configuracao.checkbox[3].checked = true;

   if (document.configuracao.apresentarede.value == "1")
      document.configuracao.checkbox[4].checked = true;

   if (document.configuracao.apresentagerencia.value == "1")
      document.configuracao.checkbox[5].checked = true;

   if (document.configuracao.apresentaholding.value == "1")
      document.configuracao.checkbox[6].checked = true;
      
   if (document.configuracao.apresentaprestadora.value == "1")
      document.configuracao.checkbox[7].checked = true;

   if (document.configuracao.apresentacircuitosativos.value == "1")
      document.configuracao.checkbox[8].checked = true;

   if (document.configuracao.apresentatrafegolimite.value == "1")
      document.configuracao.checkbox[9].checked = true;
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

   if (IndiceTipoVisualizacao == -1)
   {
      alert ("Não há informações para serem apresentadas!");
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

   document.configuracao.configuracao.value = Configuracao;
   document.configuracao.submit;
/*
   JanelaRelatorio = window.opener.parent.frames[1];
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=meiorelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   JanelaIcones = window.opener.parent.frames[2];
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;
*/
   for (i = 0; i < 1000; i++)
      for (j = 0; j < 500; j++);
   //window.close();

   return true;
}

function Atualiza()
{
   var Perfil = document.configuracao.perfil.value;
   var TipoRel = document.configuracao.tiporel.value;
   var IdRel = document.configuracao.idrel.value;
   var NomeArquivo = document.configuracao.arquivo.value;
   var NomeRelatorio = document.configuracao.nomerel.value;
   var DataGeracao = document.configuracao.datageracao.value;

   JanelaRelatorio = window.opener.parent.frames[1];
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=meiorelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   JanelaIcones = window.opener.parent.frames[2];
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   window.close();
}
