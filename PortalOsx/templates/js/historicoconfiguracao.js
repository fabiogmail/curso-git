function SelecionaConfiguracaoPadrao()
{
   document.configuracao.listatipovisualizacao.options[document.configuracao.tipovisualizacao.value].selected = true;

   if (document.configuracao.apresentacabecalho.value == "1")
      document.configuracao.checkbox[0].checked = true;

   if (document.configuracao.apresentarelatorio.value == "1")
      document.configuracao.checkbox[1].checked = true;

   if (document.configuracao.apresentabenchmarking.value == "1")
      document.configuracao.checkbox[2].checked = true;

   if (document.configuracao.apresentavaloresmedios.value == "1")
      document.configuracao.checkbox[3].checked = true;

   if (document.configuracao.apresentagrafico.value == "1")
      document.configuracao.checkbox[4].checked = true;
/*
   if (document.configuracao.apresentabotaoimprimir.value == "1")
      document.configuracao.checkbox[5].checked = true;
*/
}

function SelecionaConfiguracao()
{
   var bSelecionado = false;
   var Configuracao = "";
   var Indice = document.configuracao.listatipovisualizacao.selectedIndex;

   if (Indice == -1)
   {
      alert ("Não há informações para serem apresentadas!");
      return false;
   }

   Configuracao = Indice + ";"

   for (i = 0; i < document.configuracao.checkbox.length; i++)
   {
      if (document.configuracao.checkbox[i].checked == true)
         Configuracao += "1;";
      else
         Configuracao += "0;";
   }

   Configuracao = Configuracao.substring(0, Configuracao.length - 1);
   document.configuracao.configuracao.value = Configuracao;
   document.configuracao.submit;
   for (i = 0; i < 1000; i++)
      for (j = 0; j < 500; j++);
   window.close();
   return true;
}