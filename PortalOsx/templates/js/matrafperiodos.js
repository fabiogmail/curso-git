function SetaPeriodos()
{
   var Perfil = document.periodos.perfil.value;
   var TipoRel = document.periodos.tiporel.value;
   var IdRel = document.periodos.idrel.value;
   var NomeArquivo = document.periodos.arquivo.value;
   //var NomeRelatorio = document.periodos.nomerel.value;
   //var DataGeracao = document.periodos.datageracao.value;
   var Periodos = "";
   var QtdElementos = document.periodos.elements.length;


   for (i = 0; i < QtdElementos; i++)
   {
      if (document.periodos.elements[i].name == "periodo")
      {
         if (document.periodos.elements[i].checked == true)
            Periodos += "1;";
         else
            Periodos += "0;";
      }
   }

   if (Periodos.indexOf('1') == -1)
   {
      alert ("N?o h? per?odos selecionados!");
      return false;
   }

   //
   // Recupera o frame onde o relat?rio est? apresentado
   // Antes verifica se a janela do gr?fico est? aberta
   //
   JanelaRelatorio = window.opener.parent.parent.frames[1].frames[0];
   

   //
   // Altera o "location" da janela do relat?rio para atualizar o mesmo
   //
   //JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoMatraf&suboperacao=alteraperiodos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&periodos="+Periodos;
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoMatraf&suboperacao=alteraperiodos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&periodos="+Periodos;
   window.close();

   //
   // Seta o focus para a janela que apresenta o gr?fico
   //
   JanelaRelatorio.focus();
   return false;
}
