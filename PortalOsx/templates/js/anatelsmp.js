function TrocaPeriodo(Perfil, TipoRel, IdRel, NomeArquivo, NomeRelatorio, DataGeracao, Periodo)
{
   TipoRelatorio = "AnatelSMP";

   // Recupera o frame onde o relatório está apresentado
   //
   JanelaRelatorio = window.parent.frames[1];
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=trocaperiodo&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&periodo="+Periodo;
}
