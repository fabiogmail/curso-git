var aListaRelac, aApelidos, aFases;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
			FechaCDRView();
			break;
		default:
			alert ("Fun��o n�o encontrada!");
			break;
	}
}

function FechaCDRView()
{
   top.close();
}

