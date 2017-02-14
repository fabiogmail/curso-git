package Portal.Operacoes;

public class OpVisualizaAlarmesPlataforma extends OperacaoAbs {

	
	public boolean iniciaOperacao(String p_Mensagem){
		 String subOperacao = m_Request.getParameter("subOperacao");
		 
		 if(subOperacao == null)
			 subOperacao = "";
		 
		 
		 try {
			 String URL;
			 
			 if(subOperacao.equalsIgnoreCase("relSequencia")){
				 URL = "/templates/jsp/relatorioSequencia.jsp";
			 }
			 else if(subOperacao.equalsIgnoreCase("relSequenciaAlarme"))
				 URL = "/templates/jsp/relatorioSequenciaAlarme.jsp";
			 else if(subOperacao.equalsIgnoreCase("relSequenciaDrill"))
				 URL = "/templates/jsp/relatorioSequenciaDrill.jsp";
			 else
				 URL = "/templates/jsp/dadosRelatorioSequencia.jsp";
			 
			 setOperacao("Selecionar Dados do Relatório de Sequência");
			 m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			 return true;
		 } catch (Exception Exc) {
			 System.out.println("OpApresentaRelatorioSequencia - iniciaOperacao(): " + Exc);
			 Exc.printStackTrace();
			 return false;
		 }
	 }
}
