package Portal.Operacoes;

import Portal.Configuracoes.DefsComum;

/**
 * Visent Informática.
 * Projeto: PortalOsx
 * 
 * @author Carlos Feijão
 * @version 1.0 
 * @created 04/12/2007 16:13:29
 */
public class OpNotificar extends OperacaoAbs 
{

	static {
		
	}

	public OpNotificar() {
		
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3C21407E018F
	 */
	public boolean iniciaOperacao(String p_Mensagem) {
		try {
			String URL = "/templates/segAcesso/notificar.jsp";
			if(DefsComum.s_CLIENTE.equalsIgnoreCase("Telemig")){
				URL = "/templates/segAcesso/notificar_Telemig.jsp";
			}
			else if(DefsComum.s_CLIENTE.equalsIgnoreCase("Nextel")){
				URL = "/templates/segAcesso/notificar_Nextel.jsp";
			}
				
			setOperacao("Notificar Usuarios");
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpNotificar - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
	}

}
