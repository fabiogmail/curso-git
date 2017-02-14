var Imagens = null, Usuarios = null;
var Desconexao = null;


/*
 * Esta função faz um SLEEP através de contadores
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

function AbreJanela(Operacao, SessaoId)
{
   NovaJanela = "";
   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao=detalheUsuario&sessaoId="+SessaoId;
   NovaJanela = window.open(URLStr,'AUX','resizable=no,status=no,menubar=no,scrollbars=no,width=350,height=150');
   NovaJanela.focus();
}

function IniciaDesconexao ()
{
   var QtdUsuarios = Math.ceil(document.form1.qtdusuarios.value);

   if (Math.ceil(document.form1.qtdusuarios.value) == 0)
      return;

   Usuarios = document.form1.listausuarios.value.split(";");
   Desconexao = new Array(QtdUsuarios);
   for (i = 0; i < Desconexao.length; i++)
   {
      Desconexao[i] = new Array(3);        // 0: Usuario / 1: novo icone / [D]esconecta ou [N]ao desconecta
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

         if (Desconexao[a[4]][1] == null)
         {
            x.src = Imagens[1].src;
            Desconexao[a[4]][0] = a[5];
            Desconexao[a[4]][1] = x.src;
            Desconexao[a[4]][2] = 'D';
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
               Desconexao[a[4]][2] = 'D';
            }
            else
            {
               x.src = Imagens[0].src;
               if (bSleep)
                  Sleep();
               Desconexao[a[4]][2] = 'N';
            }
            Desconexao[a[4]][0] = a[5];
            Desconexao[a[4]][1] = x.src;
         }
      }
   }
}

function MarcaTodos()
{
   var QtdUsuarios = Math.ceil(document.form1.qtdusuarios.value);

   if (QtdUsuarios == 0)
      return false;

   for (i = 0; i < QtdUsuarios; i++)
   {
      Imagem = "Image"+i;
      SwapImage(Imagem,"","/PortalOsx/imagens/selec.gif",1,i,Usuarios[i], false);
   }

   return false;
}

function ValidaForm()
{
   var Usuarios ="";
   var Marcado =0;

   var QtdUsuarios = Math.ceil(document.form1.qtdusuarios.value);
   if (QtdUsuarios == 0)
      return false;

	for (i = 0; i < Desconexao.length; i++)
	{
      if (Desconexao[i][0] != null)
      {
         if (Desconexao[i][2] == 'D')
         {  
            Marcado++;
            Usuarios += Desconexao[i][0]; 
            Usuarios += ";";
         }
      }
	}
	if (Marcado == 0)
	{
	   alert("Nenhum usuário selecionado!");
	   return false;
	}
	Usuarios = Usuarios.substring(0, Usuarios.length-1);
	document.form1.usuarios.value = Usuarios;
   //alert (document.form1.usuarios.value);
	return true;
}
