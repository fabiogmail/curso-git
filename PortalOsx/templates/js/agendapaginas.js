function SelecionaPaginas()
{
   var NovaJanela;
   var Perfil = document.paginacao.perfil.value;
   var TipoRel = document.paginacao.tiporel.value;
   var IdRel = document.paginacao.idrel.value;
   var NomeArquivo = document.paginacao.arquivo.value;
   //var NomeRelatorio = document.paginacao.nomerel.value;
   //var DataGeracao = document.paginacao.datageracao.value;
   var PagInicial = document.paginacao.paginicial.value;
   var PagFinal   = document.paginacao.pagfinal.value

   if (TipoRel == "0")
      TipoRelatorio = "Desempenho";
   else if (TipoRel == "0")
      TipoRelatorio = "Pesquisa";
   else if (TipoRel == "0")
      TipoRelatorio = "DetalheChamadas";

   if (PagInicial.length == 0)
   {
      alert ("P�gina inicial em branco!");
      return false;
   }

   if (PagFinal.length == 0)
   {
      alert ("P�gina final em branco!");
      return false;
   }

   Expressao = new RegExp("[^0-9]", "gi");
   Ret = PagInicial.search(Expressao);
   if (Ret != -1)
   {
      alert ("P�gina inicial incorreta!");
      return false;
   }

   Ret = PagFinal.search(Expressao);
   if (Ret != -1)
   {
      alert ("P�gina final incorreta!");
      return false;
   }

   if (Math.ceil(PagInicial) > Math.ceil(PagFinal))
   {
      alert ("Numera��o inconsistente! Corrija!");
      return false;
   }

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRel+"&suboperacao=pagrelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo /* +"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao *\ +"&paginicial="+PagInicial+"&pagfinal="+PagFinal;
   Features = "menubar=yes,resizable=yes,status=no,scrollbars=yes,width=700,height=450,left=1,top=1";
   NovaJanela = window.open(URLStr, 'Relatorio', Features);

   window.close();

   //
   // Seta o focus para a nova janela
   //
   JanelaRelatorio.focus();

   return true;
}
