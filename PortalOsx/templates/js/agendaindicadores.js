function Adiciona()
{
   var ListaIndicadoresAAdicionar = new Array();
   var ListaIndicadoresApresentados = new Array();

   // Acrescenta os indicadores ja apresentados num array a parte
   for (i = 0; i < document.indicadores.indicadores2.length; i++)
      ListaIndicadoresApresentados.push(document.indicadores.indicadores2.options[i].text);

   // Monta um so array
   for (i = 0; i < ListaIndicadoresApresentados.length; i++)
      ListaIndicadoresAAdicionar.push(ListaIndicadoresApresentados[i]);

   // Monta array de indicadores a adicionar
   var Adiciona = true;
   for (i = 0; i < document.indicadores.indicadores1.length; i++)
   {
      if (document.indicadores.indicadores1.options[i].selected == true)
      {
         Adiciona = true;
         for (j = 0; j < ListaIndicadoresApresentados.length; j++)
         {
            if (ListaIndicadoresApresentados[j] == document.indicadores.indicadores1.options[i].value)
            {
               Adiciona = false;
               break;
            }
         }
         if (Adiciona == true)
            ListaIndicadoresAAdicionar.push(document.indicadores.indicadores1.options[i].value);
      }
   }

   if (ListaIndicadoresAAdicionar.length == 0)
      return;

   if (ListaIndicadoresAAdicionar.length == 0)
      return;

   while (document.indicadores.indicadores2.length != 0)
      document.indicadores.indicadores2.options[0] = null;

   for (i = 0; i < ListaIndicadoresAAdicionar.length; i++)
      document.indicadores.indicadores2[i] = new Option(ListaIndicadoresAAdicionar[i], ListaIndicadoresAAdicionar[i], false, false);
}

function Remove()
{
   var ListaIndicadoresARemover = new Array();

   for (i = 0; i < document.indicadores.indicadores2.length; i++)
   {
      if (document.indicadores.indicadores2.options[i].selected == true)
         ListaIndicadoresARemover.push(document.indicadores.indicadores2.options[i].value);
   }

   for (i = 0; i < ListaIndicadoresARemover.length; i++)
   {
      for (j = 0; j < document.indicadores.indicadores2.length; j++)
      {
         if (document.indicadores.indicadores2.options[j].value == ListaIndicadoresARemover[i])
         {
            document.indicadores.indicadores2.options[j] = null;
            break;
         }
      }
   }
}

function SelecionaIndicadores()
{
   var Perfil = document.indicadores.perfil.value;
   var TipoRel = document.indicadores.tiporel.value;
   var IdRel = document.indicadores.idrel.value;
   var NomeArquivo = document.indicadores.arquivo.value;
   var NomeRelatorio = document.indicadores.nomerel.value;
   var DataGeracao = document.indicadores.datageracao.value;
   var indicadores1 = "", indicadores2 = "", Periodos = "";
   var i, j;
   var Indicadores = "";

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


   if (document.indicadores.indicadores2.length == 0)
      return false;

   for (i = 0; i < document.indicadores.indicadores2.length; i++)
      Indicadores += document.indicadores.indicadores2.options[i].value + ";";

   //
   // Recupera o frame onde o relat?rio est? apresentado
   // Antes verifica se a janela do gr?fico est? aberta
   //
   JanelaRelatorio = window.opener.parent.parent.frames[1].frames[1];

   RegExp = /%/gi;
   Indicadores = Indicadores.replace(RegExp, "@");

   //
   // Altera o "location" da janela do relat?rio para atualizar o mesmo
   //
   
   var ListaIndicadoresApresentados = new Array();
//
   // Acrescenta os indicadores ja apresentados num array a parte
   for (i = 0; i < document.indicadores.indicadores2.length; i++)
      ListaIndicadoresApresentados.push(document.indicadores.indicadores2.options[i].text);

   // Monta um so array
//   for (i = 0; i < ListaIndicadoresApresentados.length; i++)
//      ListaIndicadoresAAdicionar.push(ListaIndicadoresApresentados[i]);
  
   if( ListaIndicadoresApresentados.length < 250){
	   
	   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=alteraindicadores&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&indicadores="+Indicadores;
	   
	   JanelaIcones = window.opener.parent.parent.frames[1].frames[2];
	   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;
		
	   window.close();
	}
   
   else{
	   alert('Você não pode adicionar mais de 250 indicadores')
}

   return false;
}
