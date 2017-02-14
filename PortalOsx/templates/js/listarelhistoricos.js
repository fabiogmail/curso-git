var Imagens = null, Arquivos = null;
var Delecao = null;

/*
 * Esta função faz um SLEEP através de contadores
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
      alert ("Selecione pelo menos um relatório para apagar.");
      return false;
   }
   document.form1.relatoriosaapagar.value = Relatorios;
   return true;
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
      document.form1.operacao.value = "listaRelHistoricos";
      document.form1.suboperacao.value = "listar";
   }
   else
   {
   //alert ("apresentar :"+Perfis + " - " + TipoRel + " - " + IdRel+ " - " +Arquivo + " - "+NomesRels + " - " + DatasRels)
      AbreJanela("apresentar", Perfis, TipoRel, IdRel, Arquivo, NomesRels, DatasRels)
      return false;
   }

   document.form1.perfis.value = Perfis;
   document.form1.nomesrels.value = NomesRels;
   document.form1.datasrels.value = DatasRels;

   return true;
}


function AbreJanela(Operacao, Perfil, TipoRel, IDRel, Arquivo, NomeRel, DataGeracao)
{
   NovaJanela = "";
   if (Operacao == "detalhar")
   { 
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=detalhaRelHistorico&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&nomerel="+NomeRel+"&arquivo="+Arquivo+"&datageracao="+DataGeracao;
      NovaJanela = window.open(URLStr,'AUX1','resizable=no,status=no,menubar=no,scrollbars=no,width=475,height=200');
   }
   else if (Operacao == "listaapagar")
   { 
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=listaRelApagar&suboperacao=listar&perfil="+Perfil+"&tipoarmazenamento=1&tiporel="+TipoRel+"&nomerel="+NomeRel;
      NovaJanela = window.open(URLStr,'AUX4','resizable=no,status=no,menubar=no,scrollbars=yes,width=350,height=220');
   }
   else if (Operacao == "visualizar")
   {
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=visualizaRelHistorico&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo;
      NovaJanela = window.open(URLStr,'AUX2','resizable=yes,status=no,menubar=no,scrollbars=yes,width=580,height=400');
   }
   else if (Operacao == "apresentar")
   { 
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistorico&suboperacao=paginicial&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo+"&nomerel="+NomeRel+"&datageracao="+DataGeracao;
      NovaJanela = window.open(URLStr,'AUX3','resizable=no,status=yes,menubar=no,scrollbars=yes,width=785,height=600');
   }
   else if (Operacao == "apresentartrafego")
   { 
      URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=paginicial&perfil="+Perfil+"&tiporel="+TipoRel+"&idrel="+IDRel+"&arquivo="+Arquivo+"&nomerel="+NomeRel+"&datageracao="+DataGeracao;
      NovaJanela = window.open(URLStr,'AUX3','resizable=no,status=yes,menubar=no,scrollbars=yes,width=785,height=600');
   }
   NovaJanela.focus();
}

function IniciaDelecao ()
{
   var QtdRelatorios = Math.ceil(document.form1.qtdrelatorios.value);

   if (Math.ceil(document.form1.qtdrelatorios.value) == 0)
      return;

   Arquivos = document.form1.arquivos.value.split(";");
   Delecao = new Array(QtdRelatorios);
   for (i = 0; i < Delecao.length; i++)
   {
      Delecao[i] = new Array(3);        // 0: Arquivo / 1: novo icone / [A]paga ou [N]ao apaga
      for (j = 0; j < 3; j++)
         Delecao[i][j] = null;
   }
}

function PreloadImages()
{
   var d = document;

   if (d.images)
   {
      if(Imagens == null)
         Imagens = new Array();

      var i, j = 0, a = PreloadImages.arguments;
      for(i = 0; i < a.length; i++)
      {
         if (a[i].indexOf("#") != 0)
         {
            Imagens[j] = new Image;
            Imagens[j].src = a[i];
            j++;
         }
      }
    }
}

function FindObj(n, d)
{
   var p, i, x;

   if (!d)
      d = document;
   if ((p = n.indexOf("?")) > 0 && parent.frames.length)
   {
      d = parent.frames[n.substring(p+1)].document;
      n = n.substring(0,p);
   }
   if(!(x = d[n]) && d.all)
      x = d.all[n];
   for (i=0; !x && i < d.forms.length; i++)
      x = d.forms[i][n];
   for (i = 0; !x && d.layers && i < d.layers.length; i++)
      x = FindObj(n,d.layers[i].document);
   if (!x && document.getElementById)
      x = document.getElementById(n);
   return x;
}

function SwapImage()
{
   var i, j = 0, x, a = SwapImage.arguments, b;
   var bSleep = true;
   document.MM_sr = new Array;

   if (a.length == 7)
   {
      b = new Array(6);
      for (i = 0; i < a.length - 1; i++)
         b[i] = a[i];

      a = b;
      bSleep = false;
   }

   for (i = 0; i < (a.length-2); i+=5)
   {
      if ((x = FindObj(a[i])) != null)
      {
         document.MM_sr[j++] = x;

         if (!x.oSrc)
            x.oSrc = x.src;

         if (Delecao[a[4]][1] == null)
         {
            x.src = Imagens[1].src;
            Delecao[a[4]][0] = a[5];
            Delecao[a[4]][1] = x.src;
            Delecao[a[4]][2] = 'A';
            if (bSleep)
               Sleep();
         }
         else
         {
            if (x.src == Imagens[0].src)
            {
               x.src = Imagens[1].src;
               if (bSleep)
                  Sleep();
               Delecao[a[4]][2] = 'A';
            }
            else
            {
               x.src = Imagens[0].src;
               if (bSleep)
                  Sleep();
               Delecao[a[4]][2] = 'N';
            }
            Delecao[a[4]][0] = a[5];
            Delecao[a[4]][1] = x.src;
         }
      }
   }
}

function MarcaTodos()
{
   var Perfil = document.form1.perfil.value;
   var TipoRel = document.form1.tiporel.value;
   var QtdRelatorios = Math.ceil(document.form1.qtdrelatorios.value);
   var Arquivo = "", ArquivoDel = "";

   if (QtdRelatorios == 0)
      return false;

   for (i = 0; i < QtdRelatorios; i++)
   {
      Imagem = "Image"+i;
      Pos = Arquivos[i].indexOf(',');

      if (Pos != -1)
      {
         Arquivo = Arquivos[i].substring(Pos+1, Arquivos[i].length);
         ArquivoDel = Perfil + "," + TipoRel + "," + Arquivo;
         SwapImage(Imagem,"","/PortalOsx/imagens/lixo1.gif",1,i,ArquivoDel,false);
      }
   }

   return false;
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
