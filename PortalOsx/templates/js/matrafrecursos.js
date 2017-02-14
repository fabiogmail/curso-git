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

//   CoordX = event.screenX;
//   CoordY = event.screenY;

//   CoordX -= 80;
//   CoordY -= 100;

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoMatraf&suboperacao=pagrecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursosselecionados="+RecursosSelecionados;

   Features  = 'status=no,scrollbars=no,width=190,height=280'
   //Features += ",left=" + CoordX + ",top=" + CoordY;
   NovaJanela = window.open(URLStr, 'Recursos'+TipoRecurso, Features);

   NovaJanela.focus();
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
   var Recursos1 = "", Recursos2 = "";

   for (i = 0; i < document.recursos.recursos.length; i++)
   {
      if (document.recursos.recursos.options[i].selected == true)
         Recursos += document.recursos.recursos.options[i].value + ";";
   }

   if (Recursos.length != 0)
      Recursos = Recursos.substring(0, Recursos.length-1);
   else
   {
      alert ("N?o h? recursos selecionados!");
      return false;
   }

   if (Recursos.indexOf("&") != -1)
   {
      // Troca "&" por "@" para passar para o servidor
      RegExp = /&/gi;
      Recursos = Recursos.replace(RegExp, "@");
   }

   JanelaRecursos = window.opener;

   if (TipoRecurso == "1") Recursos1 = Recursos
   else Recursos1 = JanelaRecursos.document.forms[0].recursos1.value;

   if (TipoRecurso == "2") Recursos2 = Recursos
   else Recursos2 = JanelaRecursos.document.forms[0].recursos2.value;

   if (Recursos1.indexOf("&") != -1)
   {
      // Troca "&" por "@" para passar para o servidor
      RegExp = /&/gi;
      Recursos1 = Recursos1.replace(RegExp, "@");
   }

   if (Recursos2.indexOf("&") != -1)
   {
      // Troca "&" por "@" para passar para o servidor
      RegExp = /&/gi;
      Recursos2 = Recursos2.replace(RegExp, "@");
   }

   JanelaRecursos.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoMatraf&suboperacao=toporelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursos="+Recursos+"&recursos1="+Recursos1+"&recursos2="+Recursos2;
   window.close();

   return false;
}

//
// Seleciona na lista os recursos que foram pr?-selecionados
//
function SelecionaRecursosPadrao()
{
   var Rec = document.recursos.recursosselecionados.value;
   var Recursos;

   // Troca "@" por "&"
   RegExp = /@/gi;
   Rec = Rec.replace(RegExp, "&");

   Recursos = Rec.split(";");

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
// Processa o novo relat?rio para os recursos selecionados
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

   RegExp = /&/gi;
   Recursos1 = Recursos1.replace(RegExp, "@");
   Recursos2 = Recursos2.replace(RegExp, "@");

   QtdRec1 = document.recursos.qtdelementosrecurso1.value;
   QtdRec2 = document.recursos.qtdelementosrecurso2.value;

   //
   // Recupera o frame onde o relat?rio est? apresentado
   //
   JanelaRelatorio = window.parent.frames[1].frames[0];

   //
   // Altera o "location" da janela do relat?rio para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoMatraf&suboperacao=alterarecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos="+Recursos1+"$$"+Recursos2;
   return false;
}

//
// N?o permite a sele??o de TODOS com outros recursos
//
function VerificaSelecao()
{
   if (document.recursos.recursos.options[0].selected == true)
   {
      QtdRec = document.recursos.recursos.length;
      for (i = 1; i < QtdRec; i++)
         document.recursos.recursos.options[i].selected = false;
   }
}
