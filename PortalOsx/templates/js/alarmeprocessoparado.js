function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0:
			VerificaMensagem();
			HabilitaAlarme();
			break;
		default:
			alert ("Função não encontrada!");
			break;
	}
}

function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;
    
	if (szMensagem.charAt(0) != "$")
		alert (document.form1.mensagem.value);
}

function HabilitaAlarme()
{
	document.form1.sms.selectedIndex = document.form1.opcaosms.value;
}

function VerificaTelefones(Valor)
{
	Expressao = new RegExp("[^0-9;]", "gi");
	Ret = Valor.search(Expressao);
	if (Ret != -1)
		return false;
	return true;
}

function ValidaForm()
{
	if (VerificaTelefones(document.form1.telefones.value) == false)
	{
		alert ("Erro no(s) telefone(s) configurado(s)! São aceitos apenas números separados por ';'!");
		return false;
	}

	if (document.form1.sms.selectedIndex != -1)
       document.form2.sms.value = document.form1.sms.selectedIndex;

	document.form2.telefones.value = document.form1.telefones.value;

	return true;
}