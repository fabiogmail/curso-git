var Imagens = null, Arquivos = null;
var Delecao = null;

/*
 * Esta fun??o faz um SLEEP atrav?s de contadores
 * Para aumentar/diminuir o tempo, deve-se aumentar/
 * diminuir o limite de contagem.
 */
function Sleep()
{
   var i, j, k = 0;

   for (i = 0; i < 1000; i++)
      for (k = 0; k < 800; k++)
         j = k;
}

function DesmarcaDatas()
{
   var QtdElementos = document.form1.elements.length;

   for (i = 0; i < QtdElementos; i++)
   {
      if (document.form1.elements[i].name.indexOf("listadatas", 0) != -1)
         document.form1.elements[i].selectedIndex = -1;
   }
   return false;
}

function ApagaRelatorios()
{
   var QtdElementos = document.form1.elements.length;
   var Relatorios = "";

   for (i = 0; i < QtdElementos; i++)
   {
      if (document.form1.elements[i].name == "checkbox" && document.form1.elements[i].checked == true)
         Relatorios += document.form1.elements[i].value + ";";
   }
   if (Relatorios.length != 0)
      Relatorios = Relatorios.substring(0, Relatorios.length-1);
   else
   {
      alert ("Selecione pelo menos um relat?rio para apagar.");
      return false;
   }

   document.form1.relatoriosaapagar.value = Relatorios;
  
   JanelaPrincipal = window.opener.parent.frames[1];
   
   var URLString = "/PortalOsx/servlet/Portal.cPortal?operacao=listaRelApagar&suboperacao=apagar&perfil="+document.form1.perfil.value+"&tiporel="+document.form1.tiporel.value+"&nomerel="+document.form1.nomerel.value+"&tipoarmazenamento="+document.form1.tipoarmazenamento.value+"&relatoriosaapagar="+Relatorios;
   
   window.location = URLString;

   window.close();

   JanelaPrincipal.focus();

   var op;
   if (document.form1.tipoarmazenamento.value == 0)
	   op = "listaRelAgendados";
   else
	   op = "listaRelHistoricos";

   JanelaPrincipal.location = "/PortalOsx/servlet/Portal.cPortal?operacao="+op+"&perfil="+document.form1.perfil.value+"&tiporel="+document.form1.tiporel.value; 

   return false;
}

function ListarApresentar(Tipo)
{
   var Perfis = "";
   var NomesRels = "";
   var DatasRels = "";
   var IdRel = "";
   var TipoRel = document.form1.tiporel.value;
   var QtdElementos = document.form1.elements.length;
   var bContinua = false;
   var QtdRelNaData = "", Arquivo = "";
   var TipoRelatorio = "";
	
	if(Tipo == 'apresentarResumo')
	{
		TipoRelatorio = "resumo";
		Tipo = 'apresentar';
	}
	else
	{
		TipoRelatorio = "detalhe";
	}

   for (i = 0; i < QtdElementos; i++)
   {
      if (document.form1.elements[i].name.indexOf("listadatas", 0) != -1)
      {

		 if (document.form1.elements[i].selectedIndex != -1)
         {
            bContinua = true;
            Aux1 = document.form1.elements[i].name;
            Aux1 = Aux1.substring(Aux1.indexOf("-")+1, Aux1.length);
            Aux2 = Aux1.substring(0, Aux1.indexOf("-"));
            Aux1 = Aux1.substring(Aux1.indexOf("-")+1, Aux1.length);
            Aux3 = document.form1.elements[i].options[document.form1.elements[i].selectedIndex].text;
            Aux3 = Aux3.substring(6, Aux3.length) + Aux3.substring(3, 5) + Aux3.substring(0, 2);
            Aux4 = document.form1.elements[i].options[document.form1.elements[i].selectedIndex].value;

            Perfis += Aux2 + ";";
            NomesRels += Aux1 + ";";
            DatasRels += Aux3 + ";";

            //20030606@1-1054905425-4.txt$06/06/2003 10:17:00
            QtdRelNaData = Aux4.substring(Aux4.indexOf("@")+1, Aux4.indexOf("-"));
            IdRel = Aux4.substring(Aux4.indexOf("@")+1, Aux4.length);
            IdRel = Aux4.substring(Aux4.indexOf("-")+1, Aux4.indexOf("$"));
            if (IdRel.indexOf("-") != -1)
               IdRel = IdRel.substring(IdRel.indexOf("-")+1, IdRel.indexOf("."));
            else
               IdRel = IdRel.substring(0, IdRel.indexOf("."));

            Arquivo = Aux4.substring(Aux4.indexOf("@")+1, Aux4.indexOf("$"));
            Arquivo = Arquivo.substring(Arquivo.indexOf("-")+1, Arquivo.length);

            if (QtdRelNaData != "1" && Tipo == "apresentar")
            {
               Tipo = "listar";
               alert ("Ha mais de um relatorio nessa data. Selecione um relatorio da lista!");
               break;
            }

            if (Tipo == 'apresentar')
            {
               Perfis = Perfis.substring(0, Perfis.length-1);
               NomesRels = NomesRels.substring(0, NomesRels.length-1);
               DatasRels = Aux4.substring(Aux4.indexOf("$")+1, Aux4.length);
               break;
            }
         }
      }
   }

   if (!bContinua)
   {
      alert ("Selecione pelo menos uma data para "+Tipo+"!");
      return false;
   }

   RExp = / /gi;
   NomesRels = NomesRels.replace(RExp, "@");

   if (Tipo == "listar")
   {
      document.form1.operacao.value = "listaRelAgendados";
      document.form1.suboperacao.value = "listar";
   }
   else
   {
   //alert ("apresentar :"+Perfis + " - " + TipoRel + " - " + IdRel+ " - " +Arquivo + " - "+NomesRels + " - " + DatasRels)
      AbreJanela("apresentar", Perfis, TipoRel, IdRel, Arquivo, NomesRels, DatasRels, TipoRelatorio)
      return false;
   }

   document.form1.perfis.value = Perfis;
   document.form1.nomesrels.value = NomesRels;
   document.form1.datasrels.value = DatasRels;

   return true;
}


function AbreJanela(Operacao, Perfil, TipoRel, IDRel, Arquivo, NomeRel, DataGeracao, TipoRelatorio)
{
   NovaJanela = "";
   var TipoRelAgendado = "";
   if (Operacao == "detalhar")
   { 
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=detalhaRelAgendado&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&nomerel="+NomeRel+"&arquivo="+Arquivo+"&datageracao="+DataGeracao;
      NovaJanela = window.open(URLStr,'AUX1','resizable=no,status=no,menubar=no,scrollbars=no,width=475,height=200');
   }
   else if (Operacao == "listaapagar")
   { 
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=listaRelApagar&suboperacao=listar&perfil="+Perfil+"&tipoarmazenamento=0&tiporel="+TipoRel+"&nomerel="+NomeRel;
      NovaJanela = window.open(URLStr,'AUX4','resizable=no,status=no,menubar=no,scrollbars=yes,width=350,height=220');
   }
   else if (Operacao == "visualizar")
   {
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=visualizaRelAgendado&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo+"&datageracao="+DataGeracao;
      NovaJanela = window.open(URLStr,'AUX2','resizable=yes,status=no,menubar=no,scrollbars=yes,width=580,height=400');
   }
   else if (Operacao == "apresentar")
   { 
      if (TipoRel == "0")
         TipoRelAgendado = "Desempenho";
      else if (TipoRel == "1")
         TipoRelAgendado = "Pesquisa";
      else if (TipoRel == "2")
         TipoRelAgendado = "DetalheChamadas";
      else if (TipoRel == "7")
         TipoRelAgendado = "IndicadoresSMP";
      else if (TipoRel == "13")
         TipoRelAgendado = "Matraf";
      else if ((TipoRel == "7") || (TipoRel == "8") || (TipoRel == "9") || (TipoRel == "10") || (TipoRel == "11") || (TipoRel == "14") || (TipoRel == "15") || (TipoRel == "16") || (TipoRel == "17") || (TipoRel == "18")|| (TipoRel == "50")|| (TipoRel == "51"))
         TipoRelAgendado = "AnatelSMP";
	  else if (TipoRel == "19")
         TipoRelAgendado = "AnaliseCompletamento";
	  else if (TipoRel == "20")
         TipoRelAgendado = "InterconexaoAudit";
	  else if (TipoRel == "21")
         TipoRelAgendado = "InterconexaoForaRota";
      else if (TipoRel == "22")
         TipoRelAgendado = "MinutosDeUso";
      else if (TipoRel == "23")
         TipoRelAgendado = "Chamadas";
      else if (TipoRel == "24")
         TipoRelAgendado = "DistribuicaoDeFrequencia";
      else if (TipoRel == "25")
         TipoRelAgendado = "Perseveranca";
      else if (TipoRel == "26")
         TipoRelAgendado = "AuditoriaChamadas";
      else if (TipoRel == "30")
      	 TipoRelAgendado = "PesquisaIMEI";
      else if (TipoRel == "31")
         TipoRelAgendado = "ChamadasLongaDuracao";
      else if (TipoRel == "32")
         TipoRelAgendado = "DestinosEspecificos";
      else if (TipoRel == "33")
         TipoRelAgendado = "DestinosComuns";
      else if (TipoRel == "34")
         TipoRelAgendado = "PesquisaPorERB";
      else if (TipoRel == "35")
         TipoRelAgendado = "PrefixosDeRisco";
      else if (TipoRel == "36")
         TipoRelAgendado = "DesempenhoDeRede";
      else if (TipoRel == "37")
         TipoRelAgendado = "QoS";
      else if (TipoRel == "49")
         TipoRelAgendado = "Agrupado";
      else if (TipoRel == "48")
         TipoRelAgendado = "Despesa";
   	  else if (TipoRel == "47")
         TipoRelAgendado = "ITXReceita";
      else if (TipoRel == "46")
         TipoRelAgendado = "GRE";
      else if (TipoRel == "45")
         TipoRelAgendado = "TrendAnalysis";
      else if (TipoRel == "43")
         TipoRelAgendado = "DWGPRS";
      else if (TipoRel == "44")
         TipoRelAgendado = "DWGERAL";

      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelAgendado" + TipoRelAgendado + "&suboperacao=paginicial&apaga=true&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo+"&datageracao="+DataGeracao+"&nomerel="+NomeRel+"&tipoRelApresentado="+TipoRelatorio;
      NovaJanela = window.open(URLStr,'AUX3','resizable=no,status=yes,menubar=no,scrollbars=yes,width=785,height=600');
   }
   NovaJanela.focus();
}

function ValidaForm()
{
   var Relatorios = "";

   if (Delecao.length == 0)
      return false;

	for (i = 0; i < Delecao.length; i++)
	{
		if (Delecao[i][0] != null)
		{
			if (Delecao[i][2] == 'A')
			{
				Relatorios += Delecao[i][0];
				Relatorios += ";";
			}
		}
	}

   if (Relatorios.length == 0)
      return false;

   document.form1.relatorios.value = Relatorios; 

   return true;
}

function Volta()
{
	document.form1.operacao.value="relAgendados";
	return true;
}

function Volta2()
{
	document.form1.operacao.value="listaRelAgendados";
	return true;
}