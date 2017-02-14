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

   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnaliseCompletamento&suboperacao=pagrecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursosselecionados="+RecursosSelecionados;

   Features  = 'status=no,scrollbars=no,width=260,height=280'
   //Features += ",left=" + CoordX + ",top=" + CoordY;
   NovaJanela = window.open(URLStr, 'Recursos', Features);

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

   for (i = 0; i < document.recursos.recursos.length; i++)
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

   if (Recursos.indexOf("&") != -1)
   {
      // Troca "&" por "@" para passar para o servidor
      RegExp = /&/gi;
      Recursos = Recursos.replace(RegExp, "@");
   }

   JanelaRecursos = window.opener;

   JanelaRecursos.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnaliseCompletamento&suboperacao=toporelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos="+Recursos;
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
// Processa o novo relatório para os recursos selecionados
//
function SelecionaRecursos()
{
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;
   var QtdRec1, QtdRec2;

   Recursos = document.recursos.recursos1.value;

   RegExp = /&/gi;
   Recursos = Recursos.replace(RegExp, "@");

   QtdRec1 = document.recursos.qtdelementosrecurso1.value;

   //
   // Recupera o frame onde o relatório está apresentado
   //
   JanelaRelatorio = window.parent.frames[1].frames[0];

   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   JanelaRelatorio.location = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnaliseCompletamento&suboperacao=alterarecursos&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos="+Recursos;
   return false;
}

//
// Não permite a seleção de TODOS com outros recursos
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
