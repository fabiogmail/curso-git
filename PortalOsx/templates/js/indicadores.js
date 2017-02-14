function AbreJanela(OperacaoPortal, Indicadores, Perfil, TipoRel, IDRel, Arquivo)
{
   // Abre nova janela
   NovaJanela = "";
   NovaJanela = window.open('','AUX3','resizable=no,status=no,menubar=no,scrollbars=no,width=360,height=380');
   NovaJanela.focus();

   // Seta formulário e submete o mesmo
   // Deve ser por formulário devido aos caracteres que aparecem nos indicadores
   document.form1.target = "AUX3";
   document.form1.operacao.value = "indicadoresRelArmazenado";
   document.form1.operacaoportal.value = OperacaoPortal;
   document.form1.indicadores.value = Indicadores;
   document.form1.perfil.value = Perfil;
   document.form1.tiporel.value = TipoRel;
   document.form1.idrel.value = IDRel;
   document.form1.arquivo.value = Arquivo;
   document.form1.method = "POST";
   document.form1.submit();
}
