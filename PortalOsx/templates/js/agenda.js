function Ordena()
{
}

function TrocaPeriodo(Perfil, TipoRel, IdRel, NomeArquivo, NomeRelatorio, DataGeracao, Periodo)
{
   if (TipoRel == "0")
      TipoRelatorio = "Desempenho";
   else if (TipoRel == "1")
      TipoRelatorio = "Pesquisa";
   else if (TipoRel == "2")
      TipoRelatorio = "DetalheChamadas";
   else if (TipoRel == "7")
      TipoRelatorio = "IndicadoresSMP";
   else if (TipoRel == "22")
      TipoRelatorio = "MinutosDeUso";
   else if (TipoRel == "23")
      TipoRelatorio = "Chamadas";
   else if (TipoRel == "24")
      TipoRelatorio = "DistribuicaoDeFrequencia";
   else if (TipoRel == "25")
      TipoRelatorio = "Perseveranca";
   else if (TipoRel == "26")
      TipoRelatorio = "AuditoriaChamadas";
   else if (TipoRel == "30")
         TipoRelatorio = "PesquisaIMEI";
   else if (TipoRel == "31")
         TipoRelatorio = "ChamadasLongaDuracao";
   else if (TipoRel == "32")
         TipoRelatorio = "DestinosEspecificos";
   else if (TipoRel == "33")
         TipoRelatorio = "DestinosComuns";
   else if (TipoRel == "34")
         TipoRelatorio = "PesquisaPorERB";
   else if (TipoRel == "35")
         TipoRelatorio = "PrefixosDeRisco";
   else if (TipoRel == "36")
         TipoRelatorio = "DesempenhoDeRede";
   else if (TipoRel == "37")
         TipoRelatorio = "QoS";
   else if (TipoRel == "49")
         TipoRelatorio = "Agrupado";
   else if (TipoRel == "48")
         TipoRelatorio = "Despesa";
   else if (TipoRel == "47")
         TipoRelatorio = "ITXReceita";
   else if (TipoRel == "46")
         TipoRelatorio = "GRE";
   else if (TipoRel == "45")
         TipoRelatorio = "TrendAnalysis";
   else if (TipoRel == "43")
         TipoRelatorio = "DWGPRS";
   else if (TipoRel == "44")
         TipoRelatorio = "DWGERAL";
   else if (TipoRel == "50")
       TipoRelatorio = "AnatelSMP3e4";
   else if (TipoRel == "51")
       TipoRelatorio = "AnatelSmp8e9";
        

   // Recupera o frame onde o relat?rio est? apresentado
   //
   JanelaRelatorio = window.parent.frames[1];
   //
   // Altera o "location" da janela do relat?rio para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=trocaperiodo&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&periodo="+Periodo;

   // Recupera o frame onde os ?cones est?o apresentados
   //
   JanelaIcones = window.parent.frames[2];
   //
   // Altera o "location" da janela do relat?rio para atualizar o mesmo
   //
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;
}
