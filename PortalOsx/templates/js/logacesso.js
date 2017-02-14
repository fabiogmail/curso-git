function Processa(Funcao)
{
   switch (Funcao)
   {
      case 1:
         return ValidaForm();
      case 2:
         return LimpaForm();
      default:
         alert ("Função não encontrada!");
         return false;
   }
}

function LimpaForm()
{
   document.form1.DiaDaSemana.selectedIndex = -1;
   document.form1.HoraIni.value = "00:00:00";
   document.form1.HoraFim.value = "23:59:59";
   return false;
}

function VerificaHora(VarHora)
{
	var HoraIncorreta = false;

	if (VarHora == 'inicial')
	{
		Hora = document.form1.HoraIni.value;
	}
	else
	{
		Hora = document.form1.HoraFim.value;
	}

	if (Hora != "")
	{
		Segmentos = Hora.split(':');
		if (Segmentos.length != 3)
		{
			alert ("Hora "+VarHora+" está incorreta!");
			return false;
		}
		for (i = 0; i < Segmentos.length; i++)
		{
			if (i == 0 && (Segmentos[i] < 0 || Segmentos[i] > 23))
				HoraIncorreta = true;
			if (i == 1 && (Segmentos[i] < 0 || Segmentos[i] > 59))
				HoraIncorreta = true;
			if (i == 2 && (Segmentos[i] < 0 || Segmentos[i] > 59))
				HoraIncorreta = true;
		}
		if (HoraIncorreta == true)
		{
			alert ("Hora "+VarHora+" está incorreta!");
			return false;
		}
	}
	else
	{
		alert ("Hora "+VarHora+" está incorreta!");
		return false;
	}
	return true;
}

function ValidaForm()
{
	if (document.form1.DiaDaSemana.selectedIndex == -1)
	{
		alert ("Não há dia da semana selecionado!");
		return false;
	}
	else
	{
		if (VerificaHora('inicial') == false)
			return false;
		if (VerificaHora('final') == false)
			return false;

		document.form1.dia.value = document.form1.DiaDaSemana.selectedIndex;
		return true;
	}
	return true;
}