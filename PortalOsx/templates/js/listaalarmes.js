var Imagens = null, Arquivos = null;
var Delecao = null;

function IniciaDelecao ()
{
   var QtdAlarmes = Math.ceil(document.form1.qtdalarmes.value);

   if (Math.ceil(document.form1.qtdalarmes.value) == 0)
      return;

   Arquivos = document.form1.arquivos.value.split(";");
   Delecao = new Array(QtdAlarmes);
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
   var i, j = 0, x, a = SwapImage.arguments, Imagem = "";
   document.MM_sr = new Array;

   for (i = 0; i < (a.length-2); i+=5)
   {
      if ((x = FindObj(a[i])) != null)
      {
         document.MM_sr[j++] = x;
         if (!x.oSrc)
            x.oSrc = x.src;
         if (Delecao[a[4]][1] == null)
            x.src = a[i+2];
         else
            x.src = Delecao[a[4]][1];
      }
   }

   if (Delecao[a[4]][0] == null)
   {
      Delecao[a[4]][0] = a[5];
      if (a[2] == "/PortalOsx/imagens/lixo1.gif")
      {
         Delecao[a[4]][1] = "/PortalOsx/imagens/lixo0.gif";
         Delecao[a[4]][2] = 'A';
      }
      else
      {
         Delecao[a[4]][1] = "/PortalOsx/imagens/lixo1.gif";
         Delecao[a[4]][2] = 'N';
      }
   }
   else
   {
      if (Delecao[a[4]][1] == "/PortalOsx/imagens/lixo1.gif")
      {
         Delecao[a[4]][1] = "/PortalOsx/imagens/lixo0.gif";
         Delecao[a[4]][2] = 'A';
      }
      else
      {
         Delecao[a[4]][1] = "/PortalOsx/imagens/lixo1.gif";
         Delecao[a[4]][2] = 'N';
      }
   }
}

function MarcaTodos()
{
   var QtdAlarmes = Math.ceil(document.form1.qtdalarmes.value);

   if (QtdAlarmes == 0)
      return false;

   for (i = 0; i < QtdAlarmes; i++)
   {
      Imagem = "Image"+i;
      SwapImage(Imagem,"","/PortalOsx/imagens/lixo1.gif",1,i,Arquivos[i]);
   }

   return false;
}

function ValidaForm()
{
	var Alarmes = "";

   var QtdAlarmes = Math.ceil(document.form1.qtdalarmes.value);
   
   if (QtdAlarmes == 0)
      return false;
	
	for (i = 0; i < Delecao.length; i++)
	{
		if (Delecao[i][0] != null)
		{
         if (Delecao[i][2] == 'A')
         {
			   Alarmes += Delecao[i][0];
			   Alarmes += ";";
         }
		}
	}

	if(Alarmes=="" || Alarmes.length==0)
		return false;
	Alarmes = Alarmes.substring(0, Alarmes.length-1);
	document.form1.alarmes.value = Alarmes;
	return true;
}
