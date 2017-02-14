var aListaRelac, aApelidos, aFases;

function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0: 
			FechaCDRView();
			break;
		default:
			alert ("Função não encontrada!");
			break;
	}
}

function FechaCDRView()
{
   top.close();
}

