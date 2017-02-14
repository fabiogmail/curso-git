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

   TipoRelatorio = "Matraf";

   if (document.indicadores.indicadores2.length == 0)
      return false;

   for (i = 0; i < document.indicadores.indicadores2.length; i++)
      Indicadores += document.indicadores.indicadores2.options[i].value + ";";

   //
   // Recupera o frame onde o relatório está apresentado
   // Antes verifica se a janela do gráfico está aberta
   //
   JanelaRelatorio = window.opener.parent.parent.frames[1].frames[0];

   RegExp = /%/gi;
   Indicadores = Indicadores.replace(RegExp, "@");

   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado"+TipoRelatorio+"&suboperacao=alteraindicadores&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&indicadores="+Indicadores;

   window.close();

   return false;
}
