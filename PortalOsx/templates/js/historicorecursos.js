function AbreJanela(TipoRecurso, RecursosSelecionados)
{
   var NovaJanela;
   var CoordX, CoordY;
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=pagrecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursosselecionados="+RecursosSelecionados;
   Features  = 'status=no,scrollbars=no,width=190,height=280'
   //Features += ",left=" + CoordX + ",top=" + CoordY;
   NovaJanela = window.open(URLStr, 'Recursos'+TipoRecurso, Features);

   NovaJanela.focus();
}

function LimpaRecursos()
{
   var QtdRec = document.recursos.recursos.length;

   for (i = 0; i < QtdRec; i++)
      document.recursos.recursos.options[i].selected = false;
}

//
// Seleciona os recursos da lista de recursos
//
function SelecionaRecursosLista()
{
   var Recursos = "";
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;
   var TipoRecurso = document.recursos.tiporecurso.value;
   var QtdRec = document.recursos.recursos.length;
   var Recursos1 = "", Recursos2 = "";
   
   for (i = 0; i < QtdRec; i++)
   {
      if (document.recursos.recursos.options[i].selected == true)
         Recursos += document.recursos.recursos.options[i].value + ";";
   }

   if (Recursos.length != 0)
      Recursos = Recursos.substring(0, Recursos.length-1);
   else
   {
      alert ("Não há recursos selecionados!");
      return false;
   }

   JanelaRecursos = window.opener;

   if (TipoRecurso == "1") Recursos1 = Recursos
   else Recursos1 = JanelaRecursos.document.forms[0].recursos1.value;

   if (TipoRecurso == "2") Recursos2 = Recursos
   else Recursos2 = JanelaRecursos.document.forms[0].recursos2.value;

   JanelaRecursos.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=toporelatorioesquerda&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursos="+Recursos+"&recursos1="+Recursos1+"&recursos2="+Recursos2;
   window.close();
   return false;
}

//
// Seleciona na lista os recursos que foram pré-selecionados
//
function SelecionaRecursosPadrao()
{
   var Recursos = document.recursos.recursosselecionados.value.split(";")
   for (i = 0; i < Recursos.length; i++)
   {
      for (j = 0; j < document.recursos.recursos.length; j++)
      {
         if (document.recursos.recursos.options[j].value == Recursos[i])
            document.recursos.recursos.options[j].selected = true;
      }
   }
}

//
// Processa o novo gráfico para os recursos selecionados
//
function SelecionaRecursos()
{
   var Recursos1 = "", Recursos2 = "";
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;
   var QtdRec1, QtdRec2;

   Recursos1 = document.recursos.recursos1.value;
   Recursos2 = document.recursos.recursos2.value;

   QtdRec1 = document.recursos.qtdelementosrecurso1.value;
   QtdRec2 = document.recursos.qtdelementosrecurso2.value;

   if (document.recursos.qtdrecursos.value == "2")
   {
      if ((Recursos1 == document.recursos.primelementorecurso1.value &&
           Recursos2 != document.recursos.primelementorecurso2.value && QtdRec1 != 1) ||
          (Recursos2 == document.recursos.primelementorecurso2.value &&
           Recursos1 != document.recursos.primelementorecurso1.value && QtdRec2 != 1))
         {
            alert ("Cruzamento de recursos não é válido!");
            return false;
         }

   }

   //
   // Recupera o frame onde o gráfico está apresentado
   //
   JanelaGrafico = window.parent.frames[1].frames[0];

   //
   // Recupera o frame onde o benchmarking está apresentado
   //
   JanelaBenchmarking = window.parent.frames[1].frames[1].frames[1];

   //
   // Altera o "location" da janela do gráfico para atualizar o mesmo
   //
   JanelaGrafico.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=alterarecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos="+Recursos1+"@@"+Recursos2;

   //
   // Altera o "location" da janela do benchmarking para atualizar o mesmo
   //
   JanelaBenchmarking.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=valoresmedios&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos="+Recursos1+"@@"+Recursos2;

   return false;
}
