function TrocaPeriodo(Perfil, TipoRel, IdRel, NomeArquivo, NomeRelatorio, DataGeracao, Periodo)
{
   TipoRelatorio = "AnaliseCompletamento";

   // Recupera o frame onde o relat�rio est� apresentado
   //
   JanelaRelatorio = window.parent.frames[0];
   //
   // Altera o "location" da janela do relat�rio para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=completamento&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&periodo="+Periodo;
}
