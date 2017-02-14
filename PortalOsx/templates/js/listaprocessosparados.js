var Imagens = null, PIDS = null, USUARIOS = null;
var Finalizacao = null;

/*
 * Esta função faz um SLEEP através de contadores
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

function AbreJanela(Operacao, Tipo, PID)
{
   NovaJanela = "";
   URLStr = "/PortalOsx/servlet/Portal.cPortal?operacao="+Operacao+"&tipo="+Tipo+"&pid="+PID;
   NovaJanela = window.open(URLStr,'AUX','resizable=no,status=no,menubar=no,scrollbars=no,width=410,height=140');
   NovaJanela.focus();
}

function IniciaFinalizacao ()
{
   var QtdProcessos = Math.ceil(document.form1.qtdprocessos.value);
   var PosIni = Math.ceil(document.form1.posini.value);

   if (QtdProcessos == 0)
      return;

   PIDS = document.form1.processosid.value.split(";");
   if (document.form1.usuariosnomes.value != "N/A")
      USUARIOS = document.form1.usuariosnomes.value.split(";");
   else
   {
      USUARIOS = new Array(PIDS.length);
      for (i = 0; i < USUARIOS.length; i++)
         USUARIOS[i] = "-";
   }

   //Finalizacao = new Array(QtdProcessos+PosIni);
   Finalizacao = new Array(QtdProcessos);
   for (i = 0; i < Finalizacao.length; i++)
   {
      Finalizacao[i] = new Array(4);        // 0: PID / 1: novo icone / [F]inaliza ou [N]ao finaliza
      for (j = 0; j < 4; j++)
         Finalizacao[i][j] = null;
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

   if (a.length == 8)
   {
      b = new Array(7);
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
//alert (a[4]-1 + " - "+ Finalizacao.length)
//alert (PosIni)
//alert (Finalizacao[a[4]-1][1])
         if (Finalizacao[a[4]-1][1] == null)
         {
            x.src = Imagens[0].src;
            Finalizacao[a[4]-1][0] = a[5];
            Finalizacao[a[4]-1][1] = x.src;
            Finalizacao[a[4]-1][2] = 'F';
            Finalizacao[a[4]-1][3] = a[6];
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
               Finalizacao[a[4]-1][2] = 'N';
            }
            else
            {
               x.src = Imagens[0].src;
               if (bSleep)
                  Sleep();
               Finalizacao[a[4]-1][2] = 'F';
            }
            Finalizacao[a[4]-1][0] = a[5];
            Finalizacao[a[4]-1][1] = x.src;
            Finalizacao[a[4]-1][3] = a[6];
         }
      }
   }
}

function MarcaTodos()
{
   var QtdProcessos = Math.ceil(document.form1.qtdprocessos.value);
   var PosIni = Math.ceil(document.form1.posini.value);

   if (QtdProcessos == 0)
      return false;

   PosIni = 1;
   //for (i = PosIni; i < QtdProcessos+PosIni; i++)
   for (i = PosIni; i <= QtdProcessos; i++)
   {
      Imagem = "Image"+i;
      if (PosIni == 0)    // !!! Para conversores e parsers
         SwapImage(Imagem,"","/PortalOsx/imagens/selec.gif",1,i,PIDS[i], USUARIOS[i], false);
      else                // !!! Para os servidores do CDRView Analise
         SwapImage(Imagem,"","/PortalOsx/imagens/selec.gif",1,i,PIDS[i-PosIni], USUARIOS[i-PosIni], false);
   }

   return false;
}

function Processa(Funcao)
{
   switch(Funcao)
   {
      case 1:
         return MarcaTodos();
      case 2:
         return ValidaForm();
      default:
         alert ("Função não configurada!");
         return false;
   }
}

function ValidaForm()
{
	var Processos = "", Usuarios = "";
	var Marcado =0;

   var QtdProcessos = Math.ceil(document.form1.qtdprocessos.value);
   if (QtdProcessos == 0)
      return false;

	for (i = 0; i < Finalizacao.length; i++)
	{
		if (Finalizacao[i][0] != null)
		{
         if (Finalizacao[i][2] == 'F')
         {
			   Marcado++;
			   Processos += Finalizacao[i][0];
			   Processos += ";";

            Usuarios += Finalizacao[i][3];
            Usuarios += ";";
         }
		}
	}
	if (Marcado == 0)
	{
		alert("Selecione um ou mais processos.");
		return false;
	}

	Processos = Processos.substring(0, Processos.length-1);
	Usuarios = Usuarios.substring(0, Usuarios.length-1);
	document.form1.processos.value = Processos;
	document.form1.usuarios.value = Usuarios;
   //alert (Usuarios)

	return true;
}
