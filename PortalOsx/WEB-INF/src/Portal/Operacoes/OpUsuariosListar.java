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
public class OpUsuariosListar extends OperacaoAbs 
{

	static {
		
	}

	public OpUsuariosListar() {
		
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3C21407E018F
	 */
	public boolean iniciaOperacao(String p_Mensagem) {
		try {
			String URL = "/templates/segAcesso/listaUsuarios.jsp";
			if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro")){
				URL = "/templates/segAcesso/listaUsuarios_Claro.jsp";
			}
			setOperacao("Listar Usuarios");
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpUsuariosListar - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
	}

}
