/****
 * Argumentos na Página
 * --------------------
 * Formulário: Form1
 *    mensagem: mensagem para ser apresentada no onLoad
 *
****/

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
         VerificaMensagem();
			MontaListaBilhetadores();
         MontaListaTiposCham();
			MontaListaFDS();
         SetaCampos();
			return true;
		case 1: 
			return LimpaForm();
		case 2: 
			return ValidaForm();
		default:
			alert ("Função não encontrada!");
			return false;
	}
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function MontaListaBilhetadores()
{
	Lista = document.form1.ListaBilhetadoresNomes.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes = document.form1.ListaBilhetadoresNomes.value;
		aListaNomes = ListaNomes.split(";");

		while (document.form1.ListaBilhetadores.length > 0)
			document.form1.ListaBilhetadores.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			document.form1.ListaBilhetadores[i] = new Option(aListaNomes[i], "", false, false);
		}
		document.form1.ListaBilhetadores.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Bilhetadores!");
}

function MontaListaTiposCham()
{
	Lista = document.form1.ListaTiposCham.value;
	if (Lista.charAt(0) != "$")
	{
		var ListaNomes = document.form1.ListaTiposChamadas.value;
		aListaNomes = ListaNomes.split(";");

		while (document.form1.ListaTiposCham.length > 0)
			document.form1.ListaTiposCham.options[0] = null;

		for (i = 0; i < aListaNomes.length; i++)
		{
			document.form1.ListaTiposCham[i] = new Option(aListaNomes[i], i, false, false);
		}
		document.form1.ListaTiposCham.selectedIndex = -1;
	}
	else document.form1.ListaTiposCham[i] = new Option("N/A", "-1", false, false);
}

function MontaListaFDS()
{
	Lista = document.form1.FDS.value;
	if (Lista.charAt(0) != "$")
	{
      var ListaFDS = document.form1.FDS.value;
		aListaFDS = ListaFDS.split(";");

		while (document.form1.ListaFDS.length > 0)
			document.form1.ListaFDS.options[0] = null;

		for (i = 0; i < aListaFDS.length; i++)
		{
			document.form1.ListaFDS[i] = new Option(aListaFDS[i], "", false, false);
		}
		document.form1.ListaFDS.selectedIndex = -1;
	}
	else alert ("Servidor não enviou a lista de Finais de Seleção!");
}


function VerificaData(Dia, Mes, Ano)
{
   var QtdDiasMes;
   QtdDiasMes = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
   var QtdDiaMesCorreta = QtdDiasMes[Mes-1];

   if (Mes == 2)
   {
      if (Ano % 4 == 0)
         QtdDiaMesCorreta = 29;
   }

   if (Dia > QtdDiaMesCorreta)
      return false;

   return true;
}


function SetaCampos()
{
   document.form1.diainicio.selectedIndex = document.form1.diainiciodef.value - 1;
   document.form1.mesinicio.selectedIndex = document.form1.mesiniciodef.value -1;
   for (i = 0; i < document.form1.anoinicio.length; i++)
   {
      if (document.form1.anoinicio.options[i].text == document.form1.anoiniciodef.value)
      {
         document.form1.anoinicio.selectedIndex = i;
         break;
      }
   }

   document.form1.horainicio.selectedIndex = document.form1.horainiciodef.value;
   document.form1.minutoinicio.selectedIndex = document.form1.minutoiniciodef.value;
   document.form1.segundoinicio.selectedIndex = document.form1.segundoiniciodef.value;

   document.form1.diafim.selectedIndex = document.form1.diafimdef.value - 1;
   document.form1.mesfim.selectedIndex = document.form1.mesfimdef.value -1;
   for (i = 0; i < document.form1.anofim.length; i++)
   {
      if (document.form1.anofim.options[i].text == document.form1.anofimdef.value)
      {
         document.form1.anofim.selectedIndex = i;
         break;
      }
   }

   document.form1.horafim.selectedIndex = document.form1.horafimdef.value;
   document.form1.minutofim.selectedIndex = document.form1.minutofimdef.value;
   document.form1.segundofim.selectedIndex = document.form1.segundofimdef.value;

   if (document.form1.origemsel.value != "*")
      document.form1.origem.value = document.form1.origemsel.value;
   if (document.form1.destinosel.value != "*")
      document.form1.destino.value = document.form1.destinosel.value;
   if (document.form1.encaminhadosel.value != "*")
      document.form1.encaminhado.value = document.form1.encaminhadosel.value;
   if (document.form1.rotaentradasel.value != "*")
      document.form1.rotaentrada.value = document.form1.rotaentradasel.value;
   if (document.form1.rotasaidasel.value != "*")
      document.form1.rotasaida.value = document.form1.rotasaidasel.value;
   if (document.form1.qtdcdrssel.value != -1)
      document.form1.qtdcdrs.value = document.form1.qtdcdrssel.value;

   if (document.form1.bilhetadorsel.value.length != 0)
   {
      var aBil = document.form1.bilhetadorsel.value.split(";");
      for (i = 0; i < aBil.length; i++)
      {
         for (j = 0; j < document.form1.ListaBilhetadores.length; j++)
            if (document.form1.ListaBilhetadores.options[j].text == aBil[i])
            {
               document.form1.ListaBilhetadores.options[j].selected = true;
               break;
            }
      }
   }

   if (document.form1.tipochamadasel.value.length != 0)
   {
      var aTiposCham = document.form1.tipochamadasel.value.split(";");
      for (i = 0; i < aTiposCham.length; i++)
         document.form1.ListaTiposCham.options[aTiposCham[i]].selected = true;
   }

   if (document.form1.fdssel.value.length != 0)
   {
      var aFDS = document.form1.fdssel.value.split(";");
      for (i = 0; i < aFDS.length; i++)
      {
         for (j = 0; j < document.form1.ListaFDS.length; j++)
            if (document.form1.ListaFDS.options[j].text == aFDS[i])
            {
               document.form1.ListaFDS.options[j].selected = true;
               break;
            }
      }
   }
}


function LimpaForm()
{
   document.form1.ListaBilhetadores.selectedIndex = -1;
   document.form1.diainicio.selectedIndex = 0;
   document.form1.mesinicio.selectedIndex = 0;
   document.form1.anoinicio.selectedIndex = 0;
   document.form1.horainicio.selectedIndex = 0;
   document.form1.minutoinicio.selectedIndex = 0;
   document.form1.segundoinicio.selectedIndex = 0;
   document.form1.diafim.selectedIndex = 0;
   document.form1.mesfim.selectedIndex = 0;
   document.form1.anofim.selectedIndex = 0;
   document.form1.horafim.selectedIndex = 0;
   document.form1.minutofim.selectedIndex = 0;
   document.form1.segundofim.selectedIndex = 0;

   document.form1.origem.value = "";
   document.form1.destino.value = "";
   document.form1.encaminhado.value = "";
   document.form1.rotaentrada.value = "";
   document.form1.rotasaida.value = "";
   document.form1.qtdcdrs.value = "";
   document.form1.arquivo.value = "";

   for (i = 0; i < document.form1.ListaFDS.length; i++)
   {
      if (document.form1.ListaFDS.options[i].selected == true)
      {
         document.form1.ListaFDS.options[i].selected = false;
      }
   }

   for (i = 0; i < document.form1.ListaTiposCham.length; i++)
   {
      if (document.form1.ListaTiposCham.options[i].selected == true)
      {
         document.form1.ListaTiposCham.options[i].selected = false;
      }
   }

   return false;
}

function ValidaForm()
{
   //
   // Verifica a data/hora iniciais
   //
   DiaInicio = document.form1.diainicio.selectedIndex;
   if (DiaInicio == -1)
   {
      alert ("Dia inicial não foi selecionado!");
      return false;
   }
   DiaInicio = document.form1.diainicio.options[DiaInicio].text;

   MesInicio = document.form1.mesinicio.selectedIndex;
   if (MesInicio == -1)
   {
      alert ("Mês inicial não foi selecionado!");
      return false;
   }
   MesInicio = document.form1.mesinicio.options[MesInicio].value;

   AnoInicio = document.form1.anoinicio.selectedIndex;
   if (AnoInicio == -1)
   {
      alert ("Ano inicial não foi selecionado!");
      return false;
   }
   AnoInicio = document.form1.anoinicio.options[AnoInicio].text;

   HoraInicio = document.form1.horainicio.selectedIndex;
   if (HoraInicio == -1)
   {
      alert ("Hora inicial não foi selecionado!");
      return false;
   }
   HoraInicio = document.form1.horainicio.options[HoraInicio].text;

   MinutoInicio = document.form1.minutoinicio.selectedIndex;
   if (MinutoInicio == -1)
   {
      alert ("Minutos iniciais não foram selecionados!");
      return false;
   }
   MinutoInicio = document.form1.minutoinicio.options[MinutoInicio].text;

   SegundoInicio = document.form1.segundoinicio.selectedIndex;
   if (SegundoInicio == -1)
   {
      alert ("Segundos iniciais não foram selecionados!");
      return false;
   }
   SegundoInicio = document.form1.segundoinicio.options[SegundoInicio].text;

   //
   // Verifica a data/hora finais
   //
   DiaFim = document.form1.diafim.selectedIndex;
   if (DiaFim == -1)
   {
      alert ("Dia final não foi selecionado!");
      return false;
   }
   DiaFim = document.form1.diafim.options[DiaFim].text;

   MesFim = document.form1.mesfim.selectedIndex;
   if (MesFim == -1)
   {
      alert ("Mês final não foi selecionado!");
      return false;
   }
   MesFim = document.form1.mesfim.options[MesFim].value;

   AnoFim = document.form1.anofim.selectedIndex;
   if (AnoFim == -1)
   {
      alert ("Ano final não foi selecionado!");
      return false;
   }
   AnoFim = document.form1.anofim.options[AnoFim].text;

   HoraFim = document.form1.horafim.selectedIndex;
   if (HoraInicio == -1)
   {
      alert ("Hora final não foi selecionada!");
      return false;
   }
   HoraFim = document.form1.horafim.options[HoraFim].text;

   MinutoFim = document.form1.minutofim.selectedIndex;
   if (MinutoFim == -1)
   {
      alert ("Minutos finais não foram selecionados!");
      return false;
   }
   MinutoFim = document.form1.minutofim.options[MinutoFim].text;

   SegundoFim = document.form1.segundofim.selectedIndex;
   if (SegundoFim == -1)
   {
      alert ("Segundos finais não foram selecionados!");
      return false;
   }
   SegundoFim = document.form1.segundofim.options[SegundoFim].text;

   if (document.form1.ListaBilhetadores.length == 0)
   {
      alert ("Não há bilhetadores configurados!");
      return false;
   }

   if (document.form1.ListaBilhetadores.selectedIndex == -1)
   {
      alert ("Selecione um bilhetador!");
      return false;
   }

   var Bilhetadores = "";
   for (i = 0; i < document.form1.ListaBilhetadores.length; i++)
   {
      if (document.form1.ListaBilhetadores.options[i].selected == true)
         Bilhetadores = Bilhetadores + document.form1.ListaBilhetadores.options[i].text + ";"

   }
   Bilhetadores = Bilhetadores.substring (0, Bilhetadores.length-1);

   var TiposCham = "";
   for (i = 0; i < document.form1.ListaTiposCham.length; i++)
   {
      if (document.form1.ListaTiposCham.options[i].selected == true)
      {
         TiposCham = TiposCham + document.form1.ListaTiposCham.options[i].value + ";"
      }

   }
   if (TiposCham.length != 0)
      TiposCham = TiposCham.substring (0, TiposCham.length-1);

   Expressao = new RegExp("[^0-9]", "gi");
   Ret = document.form1.qtdcdrs.value.search(Expressao);
   if (Ret != -1)
   {
      alert ("Quantidade de CDRs incorreta!");
      return false;
   }

   Expressao = new RegExp("[^A-Za-z0-9._-]", "gi");
   Ret = document.form1.arquivo.value.search(Expressao);
   if (Ret != -1 || document.form1.arquivo.value.length == 0)
   {
      alert ("Nome do arquivo para exportação está incorreto!");
      return false;
   }

   var NomeArquivo = document.form1.arquivo.value;
   if (NomeArquivo.length < 5 && NomeArquivo.length > 20)
   {
      alert ("Nome do arquivo para exportação deve ter de 5 a 20 caracteres!");
      return false;
   }

   var Extensao = NomeArquivo.substring(NomeArquivo.length-4, NomeArquivo.length);
   if (Extensao != ".txt" && Extensao != ".csv")
   {
      alert ("A extensão do arquivo deve ser TXT ou CSV!");
      return false;
   }

   document.form2.bilhetador.value = Bilhetadores;
   document.form2.tiposcham.value = TiposCham;
   document.form2.inicio.value = DiaInicio + "/" + MesInicio + "/" + AnoInicio + " " + HoraInicio + ":" + MinutoInicio + ":" + SegundoInicio;
   document.form2.fim.value = DiaFim + "/" + MesFim + "/" + AnoFim + " " + HoraFim + ":" + MinutoFim + ":" + SegundoFim;

   if (VerificaData(DiaInicio, MesInicio, AnoInicio) == false)
   {
      alert ("Data inicial está incorreta!");
      return false;
   }

   if (VerificaData(DiaFim, MesFim, AnoFim) == false)
   {
      alert ("Data final está incorreta!");
      return false;
   }

   document.form2.numa.value = document.form1.origem.value;
   document.form2.numb.value = document.form1.destino.value;
   document.form2.numc.value = document.form1.encaminhado.value;
   document.form2.rotaent.value = document.form1.rotaentrada.value;
   document.form2.rotasai.value = document.form1.rotasaida.value;
   document.form2.qtdcdrs.value = document.form1.qtdcdrs.value;
   document.form2.arquivo.value = document.form1.arquivo.value;

   FDS = "";
   for (i = 0; i < document.form1.ListaFDS.length; i++)
      if (document.form1.ListaFDS.options[i].selected == true)
         FDS += document.form1.ListaFDS.options[i].text + ";";

   FDS = FDS.substring(0, FDS.length-1);
   document.form2.fds.value = FDS; 
   return true;
}