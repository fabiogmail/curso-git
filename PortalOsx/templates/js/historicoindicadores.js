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
//
// Armazena, no campo do formulário que vai para o servidor,
// os indicadores que deverão ser tratados no gráfico
//
function SelecionaIndicadores()
{
   var Perfil = document.indicadores.perfil.value;
   var TipoRel = document.indicadores.tiporel.value;
   var IdRel = document.indicadores.idrel.value;
   var NomeArquivo = document.indicadores.arquivo.value;
   var NomeRelatorio = document.indicadores.nomerel.value;
   var DataGeracao = document.indicadores.datageracao.value;
   var Indicadores = "";

   for (i = 0; i < document.indicadores.indicadores2.length; i++)
      Indicadores += document.indicadores.indicadores2.options[i].value + ";";

   if (Indicadores.length != 0)
      Indicadores = Indicadores.substr(0, Indicadores.length-1);
   else
   {
      alert ("Selecione pelo menos um indicador!");
      //window.close();
      return false;
   }

   //
   // Substitui todas as ocorrências de "%" por "/". Senão dá erro no servlet!!
   //
   RExp = /%/gi;
   Indicadores = Indicadores.replace(RExp, "@");

   //
   // Recupera o frame onde o gráfico está apresentado
   // Antes verifica se a janela do gráfico está aberta
   //
   if (window.opener.closed == true)
   {
      alert ("Janela do gráfico foi fechada! Abra-a novamente!");
      return false;
   }

   JanelaGrafico = window.opener.parent.parent.frames[0];

   //
   // Recupera os frames do Benchmarking e dos Valores Médios
   //
   JanelaBenchmarking  = window.opener.parent.parent.frames[1].frames[0];
   JanelaValoresMedios = window.opener.parent.parent.frames[1].frames[1];

   //
   // Altera o "location" da janela do gráfico para atualizar o mesmo
   //
   if( ListaIndicadoresApresentados.length < 250){
	   JanelaGrafico.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=alteraindicadores&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&indicadores="+Indicadores;

	   //
	   // Altera o "location" da janela do Benchmarking e dos Valores Médios para atualizar os mesmos
	   //
	   JanelaBenchmarking.location  = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=benchmarking&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;
	   JanelaValoresMedios.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=valoresmedios&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

	   //for (i = 0; i < 500; i++);
	   //
	   // Fecha a janela de indicadores
	   //
	   window.close();
   }
   //
   // Seta o focus para a janela que apresenta o gráfico
   //
   JanelaGrafico.focus();

   return false;
}
