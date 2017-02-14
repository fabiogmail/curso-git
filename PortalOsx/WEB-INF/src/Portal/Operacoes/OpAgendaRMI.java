package Portal.Operacoes;

public class OpAgendaRMI extends OperacaoAbs{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public boolean iniciaOperacao(String p_Mensagem) 
	   {
		   try {
			   String tipo = m_Request.getParameter("tipo");
			   String URL = "";
			   
			   if(tipo.equalsIgnoreCase("Agenda"))
				   URL = "/templates/jsp/agendaRMI.jsp";
			   else if(tipo.equalsIgnoreCase("Historico"))
				   URL = "/templates/jsp/historicoRMI.jsp";
			   
				setOperacao("Listar agenda do servidor RMI");
				m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
				return true;
			} catch (Exception Exc) {
				System.out.println("OpAgendaRMI - iniciaOperacao(): " + Exc);
				Exc.printStackTrace();
				return false;
			}
	      
	   }  

}
