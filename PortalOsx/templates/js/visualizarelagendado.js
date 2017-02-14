function AbreJanela(Perfil, TipoRel, IDRel, Arquivo)
{

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=visualizaRelAgendado&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo;
   auxiliar = window.open(URLStr,'AUX','resizable=no,status=no,menubar=no,scrollbars=no,width=540,height=400');
   auxiliar.focus();
}
