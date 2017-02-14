function SetaPeriodos()
{
   var Perfil = document.periodos.perfil.value;
   var TipoRel = document.periodos.tiporel.value;
   var IdRel = document.periodos.idrel.value;
   var NomeArquivo = document.periodos.arquivo.value;
   var NomeRelatorio = document.periodos.nomerel.value;
   var DataGeracao = document.periodos.datageracao.value;
   var Periodo1 = "", Periodo2 = "", Periodos = "";
   var i, j;


   for (i = 0; i < document.periodos.periodo1.length; i++)
   {
      if (document.periodos.periodo1.options[i].selected == true && i != 0)
      {
         Periodo1 = document.periodos.periodo1.options[i].text;
         break;
      }
   }

   if (Periodo1.length == 0)
   {
      alert ("Selecione o per�odo inicial!")
      return false;
   }

   for (j = 0; j < document.periodos.periodo2.length; j++)
   {
      if (document.periodos.periodo2.options[j].selected == true && j != 0)
      {
         Periodo2 = document.periodos.periodo2.options[j].text;
         break;
      }
   }

   if (Periodo2.length == 0)
   {
      alert ("Selecione o per�odo final!")
      return false;
   }

   if (i > j)
   {
      alert ("Per�odo inicial deve ser menor que per�odo final!")
      return false;
   }

   Periodos = Periodo1 + "@" + Periodo2;

   //
   // Recupera o frame onde o gr�fico est� apresentado
   // Antes verifica se a janela do gr�fico est� aberta
   //
   JanelaGrafico = window.opener.parent.parent.frames[0];

   //
   // Altera o "location" da janela do gr�fico para atualizar o mesmo
   //
   JanelaGrafico.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=alteraperiodos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&periodos="+Periodos;

   //
   // Altera o "location" da janela de valores m�dios para atualizar os mesmos
   //
   JanelaValoresMedios = window.opener.parent.frames[1];
   JanelaValoresMedios.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=valoresmedios&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   //
   // Seta o focus para a janela que apresenta o gr�fico
   //
   JanelaGrafico.focus();

   //window.close();

   return false;
}
