var Imagens = null, Arquivos = null;
var Delecao = null;

/*
 * Esta função é faz um SLEEP através de contadores
 * Para aumentar/diminuir o tempo, deve-se aumentar/
 * diminuir o limite de contagem.
 */
function Sleep()
{
   var i, j, k = 0;

   for (i = 0; i < 800; i++)
      for (k = 0; k < 800; k++)
         j = k;
}

function IniciaDelecao ()
{
   var QtdMensagens = Math.ceil(document.form1.qtdmensagens.value);

   if (Math.ceil(document.form1.qtdmensagens.value) == 0)
      return;

   Arquivos = document.form1.arquivos.value.split(";");
   Delecao = new Array(QtdMensagens);
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

function MarcaTodosPagina()
{
	document.form1.marcaTodosPag.value = "true";
	MarcaTodos();
	return false;
}

function MarcaTodos()
{
   var QtdMensagens = Math.ceil(document.form1.qtdmensagens.value);
   if (QtdMensagens == 0)
      return false;

   for (i = 0; i < QtdMensagens; i++)
   {
      Imagem = "Image"+i;
      SwapImage(Imagem,"","/PortalOsx/imagens/lixo1.gif",1,i,Arquivos[i], false);
   }

   return false;
}

function ValidaForm()
{
   var Mensagens ="";

   if (Delecao == null)
      return false;

	for (i = 0; i < Delecao.length; i++)
	{
		if (Delecao[i][0] != null)
		{

         if (Delecao[i][2] == 'A')
         {
			   Mensagens += Delecao[i][0];
			   Mensagens += ";";
         }
		}
	}

   if (Mensagens.length > 0 && Mensagens.length-1 != 0)
	   Mensagens = Mensagens.substring(0, Mensagens.length-1);
   else
      return false;
 
	document.form1.mensagens.value = Mensagens;
	return true;
}

function LeMensagem(Arquivo)
{
   document.form2.arquivo.value = Arquivo;
   document.form2.submit();
}
