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
public class OpUsuariosInserir extends OperacaoAbs 
{

	static {
		
	}

	public OpUsuariosInserir() {
		
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3C21407E018F
	 */
	public boolean iniciaOperacao(String p_Mensagem) {
		try {
			String URL = "/templates/jsp/inserirUsuario.jsp";
			if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro")){
				URL = "/templates/jsp/inserirUsuario_Claro.jsp";
			}
			setOperacao("Inserir Usuario");
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpUsuariosInserir - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
	}

}
