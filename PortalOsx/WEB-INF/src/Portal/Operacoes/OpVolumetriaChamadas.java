package Portal.Operacoes;

import java.io.IOException;

import javax.servlet.ServletException;

import Portal.Configuracoes.DefsComum;

public class OpVolumetriaChamadas extends OperacaoAbs{
	
	public boolean iniciaOperacao(String p_Mensagem)
	   {
	      String URL; 
	      setOperacao("Listagem de Chamadas Volumetria");
		//if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro")){
			 try {	 
				 	URL = "/templates/jsp/chamadasVolumetria.jsp";	
					m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
					
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException Exc) {
					 System.out.println("OpListaAlarmes - iniciaOperacao(): " + Exc);
					 Exc.printStackTrace();
					 return false;
				}
				return true;


	   }
}
