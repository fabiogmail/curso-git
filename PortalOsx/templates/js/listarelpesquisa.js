function AbreJanela(Operacao, Perfil, TipoRel, IDRel, Arquivo, NomeRel, DataGeracao, TipoArmazenamento)
{
   NovaJanela = "";
   var TipoRelAgendado = "";
   if (TipoArmazenamento == "0")
   { 
      if (TipoRel == "0")
         TipoRelAgendado = "Desempenho";
      else if (TipoRel == "1")
         TipoRelAgendado = "Pesquisa";
      else if (TipoRel == "2")
         TipoRelAgendado = "DetalheChamadas";
      else if (TipoRel == "13")
         TipoRelAgendado = "Matraf";
      else if (TipoRel == "7")
         TipoRelAgendado = "IndicadoresSMP";
      else if (TipoRel == "8" || TipoRel == "9" || TipoRel == "10" || TipoRel == "11" || TipoRel == "14" || TipoRel == "15" || TipoRel == "16" || TipoRel == "17")
         TipoRelAgendado = "AnatelSMP";
      else if (TipoRel == "18")
         TipoRelAgendado = "AnaliseCompletamento";

      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado" + TipoRelAgendado + "&suboperacao=paginicial&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo+"&datageracao="+DataGeracao+"&nomerel="+NomeRel;
      NovaJanela = window.open(URLStr,'AUX3','resizable=no,status=yes,menubar=no,scrollbars=yes,width=785,height=600');
   }
   else 
   {
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=paginicial&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo+"&nomerel="+NomeRel+"&datageracao="+DataGeracao;
      NovaJanela = window.open(URLStr,'AUX3','resizable=no,status=yes,menubar=no,scrollbars=yes,width=785,height=600');
   }

   NovaJanela.focus();
}
