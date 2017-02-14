var ID_REL_LDN = 18;

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


   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnatelSMP&suboperacao=pagrecursos&tiporecurso="+TipoRecurso+"&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursosselecionados="+RecursosSelecionados;
   Features  = 'status=no,scrollbars=no,width=190,height=280'
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
   var Recursos1 = "", Recursos2 = "";
   var TipoRecurso = document.recursos.tiporecurso.value;
   var TipoApresentacao = document.recursos.tipoapresentacao.value;
   var QtdRecursos = 0;

   JanelaTopo = window.opener;
   JanelaTopo.document.recursos.tipoapresentacao.value = TipoApresentacao;

   for (i = 0; i < document.recursos.recursos.length; i++)
   {
	  if (document.recursos.recursos.options[i].selected == true)
	  {
         Recursos += document.recursos.recursos.options[i].value + ";";
		 QtdRecursos++;
	  }
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

   if (TipoRecurso == "1") Recursos1 = Recursos;
   else Recursos1 = JanelaRecursos.document.forms[0].recursos1.value;

   if (TipoRecurso == "2") Recursos2 = Recursos;
   else Recursos2 = JanelaRecursos.document.forms[0].recursos2.value;

   if (TipoRel == ID_REL_LDN)
   {
	   if (TipoRecurso == "3") Recursos3 = Recursos;
	   else Recursos3 = JanelaRecursos.document.forms[0].recursos3.value;
   }

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

   if (TipoRel == ID_REL_LDN)
   {
	   if (Recursos3.indexOf("&") != -1)
	   {
		  // Troca "&" por "@" para passar para o servidor
		  RegExp = /&/gi;
		  Recursos3 = Recursos3.replace(RegExp, "@");
	   }
   }

   if (TipoRel == ID_REL_LDN)
      URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnatelSMP&suboperacao=toporelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursos="+Recursos+"&recursos1="+Recursos1+"&recursos2="+Recursos2+"&recursos3="+Recursos3;
   else
      URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnatelSMP&suboperacao=toporelatorio&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&tiporecurso="+TipoRecurso+"&recursos="+Recursos+"&recursos1="+Recursos1+"&recursos2="+Recursos2;

   JanelaRecursos.location = URLString;
   window.close();

   return false;
}

//
// Seleciona na lista os recursos que foram pré-selecionados
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
   var Recursos1 = "", Recursos2 = "", Recursos3 = "";
   var Perfil = document.recursos.perfil.value;
   var TipoRel = document.recursos.tiporel.value;
   var IdRel = document.recursos.idrel.value;
   var NomeArquivo = document.recursos.arquivo.value;
   var NomeRelatorio = document.recursos.nomerel.value;
   var DataGeracao = document.recursos.datageracao.value;
   var TipoApresentacao = document.recursos.tipoapresentacao.value;
   var QtdRec1, QtdRec2, QtdRec3;

   Recursos1 = document.recursos.recursos1.value;
   Recursos2 = document.recursos.recursos2.value;
   if (TipoRel == ID_REL_LDN)
      Recursos3 = document.recursos.recursos3.value;

   RegExp = /&/gi;
   Recursos1 = Recursos1.replace(RegExp, "@");
   Recursos2 = Recursos2.replace(RegExp, "@");
   if (TipoRel == ID_REL_LDN)
      Recursos3 = Recursos3.replace(RegExp, "@");

   QtdRec1 = document.recursos.qtdelementosrecurso1.value;
   QtdRec2 = document.recursos.qtdelementosrecurso2.value;
   if (TipoRel == ID_REL_LDN)
      QtdRec3 = document.recursos.qtdelementosrecurso3.value;

   //
   // Recupera o frame onde o relatório está apresentado
   //
   JanelaRelatorio = window.parent.frames[1];
/*
alert ("Recursos1: "+Recursos1)
alert ("Recursos2: "+Recursos2)
if (TipoRel == ID_REL_LDN)
   alert ("Recursos3: "+Recursos3)
*/
   //
   // Altera o "location" da janela do relatório para atualizar o mesmo
   //
   if (TipoRel == ID_REL_LDN)
//{
      URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnatelSMP&suboperacao=alterarecursos&tipoapresentacao="+TipoApresentacao+"&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos1="+Recursos1+"&recursos2="+Recursos2+"&recursos3="+Recursos3;
//	  alert (URLString)
//}
   else
      URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendadoAnatelSMP&suboperacao=alterarecursos&tipoapresentacao="+TipoApresentacao+"&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IdRel+"&arquivo="+NomeArquivo+"&nomerel="+NomeRelatorio+"&datageracao="+DataGeracao+"&recursos1="+Recursos1+"&recursos2="+Recursos2;
   JanelaRelatorio.location = URLString;

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
