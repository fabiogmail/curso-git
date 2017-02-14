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
public class OpSegConfigurar extends OperacaoAbs 
{

	static {
		
	}

	public OpSegConfigurar() {
		
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3C21407E018F
	 */
	public boolean iniciaOperacao(String p_Mensagem) {
		try {
			System.out.println("CLIENTE: "+DefsComum.s_CLIENTE);
			String URL = "/templates/jsp/configGeral.jsp";
			if(DefsComum.s_CLIENTE.equalsIgnoreCase("Telemig")){
				URL = "/templates/jsp/configGeral_Telemig.jsp";
			}
			if(DefsComum.s_CLIENTE.equalsIgnoreCase("Nextel")){
				URL = "/templates/jsp/configGeral_Nextel.jsp";
			}
			setOperacao("Configurar Seg de Acesso");
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpSegConfigurar - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
	}

}
