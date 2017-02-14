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

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=pagrecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursosselecionados="+RecursosSelecionados;
   Features  = 'status=no,scrollbars=no,width=190,height=280'
   //Features += ",left=" + CoordX + ",top=" + CoordY;
   NovaJanela = window.open(URLStr, 'Recursos'+TipoRecurso, Features);

   NovaJanela.focus();
}

function Limpa()
{
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;


   // Recupera o frame onde o relatório está apresentado
   //
   JanelaRelatorio = window.parent.frames[1];
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=alterarecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos=-@@-@@-@@-@@-";

   //
   // Recupera o frame onde o relatório está apresentado
   //
   JanelaIcones = window.parent.frames[2];
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   //
   // Recupera o frame onde o relatório está apresentado
   //
   JanelaTopo = window.parent.frames[0];
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaTopo.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=toporelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;

   return false;
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
   var Recursos1 = "", Recursos2 = "", Recursos3 = "", Recursos4 = "", Recursos5 = "";

   for (i = 0; i < document.recursos.recursos.length; i++)
   {
      if (document.recursos.recursos.options[i].selected == true)
         Recursos += document.recursos.recursos.options[i].value + ";";
   }


   if (Recursos.length != 0)
      Recursos = Recursos.substring(0, Recursos.length-1);
   else
      Recursos = "-";

   JanelaRecursos = window.opener;

   if (TipoRecurso == "1") Recursos1 = Recursos;
   else Recursos1 = JanelaRecursos.document.forms[0].recursos1.value;

   if (TipoRecurso == "2") Recursos2 = Recursos;
   else Recursos2 = JanelaRecursos.document.forms[0].recursos2.value;

   if (TipoRecurso == "3") Recursos3 = Recursos;
   else Recursos3 = JanelaRecursos.document.forms[0].recursos3.value;

   if (TipoRecurso == "4") Recursos4 = Recursos;
   else Recursos4 = JanelaRecursos.document.forms[0].recursos4.value;

   if (TipoRecurso == "5") Recursos5 = Recursos;
   else Recursos5 = JanelaRecursos.document.forms[0].recursos5.value;  

   JanelaRecursos.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=toporelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursos="+Recursos+"&recursos1="+Recursos1+"&recursos2="+Recursos2+"&recursos3="+Recursos3+"&recursos4="+Recursos4+"&recursos5="+Recursos5;

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
// Processa o novo relatório para os filtros selecionados
//
function SelecionaRecursos()
{
   var Recursos1 = "", Recursos2 = "", Recursos3 = "" , Recursos4 = "", Recursos5 = "";
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;

   Recursos1 = document.recursos.recursos1.value;
   Recursos2 = document.recursos.recursos2.value;
   Recursos3 = document.recursos.recursos3.value;
   Recursos4 = document.recursos.recursos4.value;
   Recursos5 = document.recursos.recursos5.value;

   if (Recursos1.length == 0)
      Recursos1 = "-";

   if (Recursos2.length == 0)
      Recursos2 = "-";

   if (Recursos3.length == 0)
      Recursos3 = "-";

   if (Recursos4.length == 0)
      Recursos4 = "-";

   if (Recursos5.length == 0)
      Recursos5 = "-";

   //
   // Recupera o frame onde o relatório está apresentado
   //
   JanelaRelatorio = window.parent.frames[1];
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=alterarecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos="+Recursos1+"@@"+Recursos2+"@@"+Recursos3+"@@"+Recursos4+"@@"+Recursos5;

   //
   // Recupera o frame onde o relatório está apresentado
   //
   JanelaIcones = window.parent.frames[2];
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaIcones.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=baserelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao;
   return false;
}
