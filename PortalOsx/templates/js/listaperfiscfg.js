var Imagens = null, Perfis = null, Bloqueio = null;
var Desconexao = null;

/*
 * Esta função é faz um SLEEP através de contadores
 * Para aumentar/diminuir o tempo, deve-se aumentar/
 * diminuir o limite de contagem.
 */
function Sleep()
{
   var i, j, k = 0;

   for (i = 0; i < 800; i++)
      for (k = 0; k < 1000; k++)
         j = k;
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;

	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function IniciaDesconexao ()
{
   var QtdPerfis = Math.ceil(document.form1.qtdperfis.value);

   Perfis = document.form1.perfisexistentes.value.split(";");
   Bloqueio = document.form1.bloqueio.value.split(";");

   Desconexao = new Array(QtdPerfis);
   for (i = 0; i < Desconexao.length; i++)
   {
      Desconexao[i] = new Array(3);        // 0: Perfil / 1: -[d]esbloqueia ou -[b]loqueia/ 2: novo icone
      for (j = 0; j < 3; j++)
         Desconexao[i][j] = null;
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

         if (Desconexao[a[4]][2] == null)
         {
            if (Bloqueio[a[4]] == 1)
            {
               x.src = Imagens[0].src;
               Desconexao[a[4]][1] = '-d';
            }
            else
            {
               x.src = Imagens[1].src;
               Desconexao[a[4]][1] = '-b';
            }

            Desconexao[a[4]][0] = a[5];
            Desconexao[a[4]][2] = x.src;
            if (bSleep)
               Sleep();
         }
         else
         {
            if (x.src == Imagens[0].src)
            {
               x.src = Imagens[1].src;
               Desconexao[a[4]][1] = '-b';
               if (bSleep)
                  Sleep();

            }
            else
            {
               x.src = Imagens[0].src;
               Desconexao[a[4]][1] = '-d';
               if (bSleep)
                  Sleep();

            }
            Desconexao[a[4]][0] = a[5];
            Desconexao[a[4]][2] = x.src
         }
      }
   }
}

function MarcaTodos()
{
   var QtdPerfis = Math.ceil(document.form1.qtdperfis.value);

   if (QtdPerfis == 1)
      return false;

   for (i = 1; i < QtdPerfis; i++) // O perfil 0 é o "admin"
   {
      if (Bloqueio[i] == "0")
      {
         Imagem = "Image"+i;
         SwapImage(Imagem,"","/PortalOsx/imagens/fechado.gif",1,i,Perfis[i],false);
      }
   }

   return false;
}

function ValidaForm()
{
	var Perfis ="";
        var Marcado =0;
   
   var QtdPerfis = Math.ceil(document.form1.qtdperfis.value);
   if (QtdPerfis == 0)
      return false;

	for (i = 0; i < Desconexao.length; i++)
	{
		if (Desconexao[i][0] != null)
		{
                        Marcado++;
			Perfis += Desconexao[i][0] + Desconexao[i][1];
			Perfis += ";";
		}
	}
	if (Marcado == 0)
	{
		alert("Marque um perfil.");
		return false;
	}
           
	Perfis = Perfis.substring(0, Perfis.length-1);
	document.form1.perfis.value = Perfis;
   //alert (Perfis)
	return true;
}
