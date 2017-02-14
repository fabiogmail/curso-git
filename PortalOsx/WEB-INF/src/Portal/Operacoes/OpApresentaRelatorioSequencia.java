package Portal.Operacoes;

public class OpApresentaRelatorioSequencia extends OperacaoAbs {
	
	public OpApresentaRelatorioSequencia(){
		
	}
	
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
		 else if(subOperacao.equalsIgnoreCase("relSequenciaDrillFaltante"))
			 URL = "/templates/jsp/relatorioSequenciaDrillFaltante.jsp";
		 else if(subOperacao.equalsIgnoreCase("relSequenciaDrillRepetido"))
			 URL = "/templates/jsp/relatorioSequenciaDrillRepetido.jsp";
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
