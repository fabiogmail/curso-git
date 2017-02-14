function AbreJanela(Perfil, IDRel, Arquivo)
{

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=visualizaRelHistorico&perfil="+Perfil+"&idrel="+IDRel+"&arquivo="+Arquivo;
   auxiliar = window.open(URLStr,'AUX','resizable=yes,status=no,menubar=no,scrollbars=no,width=800,height=700');
   auxiliar.focus();
}
