function ComparePeriodos(a, b)
{
   var DiaA = a.substring(0,2);
   var DiaB = b.substring(0,2);
   var MesA = a.substring(3,5);
   var MesB = b.substring(3,5);

   //alert (DiaA + " - " + DiaB + " - " +MesA + " - " + MesB )

   //if (a is less than b by some ordering criterion)
   if (MesA < MesB)
      return -1;
   else if (MesA == MesB)
   {
      if (DiaA < DiaB)
         return -1;
      else
         return 1;
   }

   //if (a is greater than b by the ordering criterion)
   if (MesA > MesB)
      return 1   // a must be equal to b
      
   return 0
}

function Adiciona()
{
   var ListaDatasAAdicionar = new Array();
   var ListaDatasApresentadas = new Array();

   // Acrescenta os periodos ja apresentados num array a parte
   for (i = 0; i < document.periodos.periodo2.length; i++)
      ListaDatasApresentadas.push(document.periodos.periodo2.options[i].text);

   // Monta array de periodos a adicionar
   var Adiciona = true;
   for (i = 0; i < document.periodos.periodo1.length; i++)
   {
      if (document.periodos.periodo1.options[i].selected == true)
      {
         Adiciona = true;
         for (j = 0; j < ListaDatasApresentadas.length; j++)
         {
            if (ListaDatasApresentadas[j] == document.periodos.periodo1.options[i].value)
            {
               Adiciona = false;
               break;
            }
         }
         if (Adiciona == true)
            ListaDatasAAdicionar.push(document.periodos.periodo1.options[i].value);
      }
   }

   if (ListaDatasAAdicionar.length == 0)
      return;

   // Monta um so array
   for (i = 0; i < ListaDatasApresentadas.length; i++)
      ListaDatasAAdicionar.push(ListaDatasApresentadas[i]);

   if (ListaDatasAAdicionar.length == 0)
      return;

   while (document.periodos.periodo2.length != 0)
      document.periodos.periodo2.options[0] = null;

   ListaDatasAAdicionar.sort(ComparePeriodos);
   for (i = 0; i < ListaDatasAAdicionar.length; i++)
   {
      document.periodos.periodo2[i] = new Option(ListaDatasAAdicionar[i], ListaDatasAAdicionar[i], false, false);
   }
}

function Remove()
{
   var ListaDatasARemover = new Array();

   for (i = 0; i < document.periodos.periodo2.length; i++)
   {
      if (document.periodos.periodo2.options[i].selected == true)
         ListaDatasARemover.push(document.periodos.periodo2.options[i].value);
   }

   for (i = 0; i < ListaDatasARemover.length; i++)
   {
      for (j = 0; j < document.periodos.periodo2.length; j++)
      {
         if (document.periodos.periodo2.options[j].value == ListaDatasARemover[i])
         {
            document.periodos.periodo2.options[j] = null;
            break;
         }
      }
   }
}

function SetaPeriodos()
{
   var Perfil = document.periodos.perfil.value;
   var TipoRel = document.periodos.tiporel.value;
   var IdRel = document.periodos.idrel.value;
   var NomeArquivo = document.periodos.arquivo.value;
   var NomeRelatorio = document.periodos.nomerel.value;
   var DataGeracao = document.periodos.datageracao.value;
   var Periodo1 = "", Periodo2 = "", Periodos = "";
   var i, j;
   var Periodos = "";

   if (document.periodos.periodo2.length == 0)
      return false;

   for (i = 0; i < document.periodos.periodo2.length; i++)
      Periodos += document.periodos.periodo2.options[i].value + ";";

   //
   // Recupera o frame onde o relatório está apresentado
   // Antes verifica se a janela do gráfico está aberta
   //
   JanelaRelatorio = window.opener.parent.parent.frames[1].frames[1];

   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=alteraperiodos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&periodos="+Periodos;

   window.close();

   //
   // Seta o focus para a janela que apresenta o gráfico
   //
   JanelaRelatorio.focus();

   return false;
}
