function Ordena(Perfil, TipoRel, IdRel, NomeArquivo, NomeRelatorio, DataGeracao, TipoOrdenacao, Coluna)
{
   JanelaRelatorio = window.parent.frames[1];
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=ordenacao&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tipoordenacao="+TipoOrdenacao+"&coluna="+Coluna;

   JanelaIcones = window.parent.frames[2];
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;
}
