package Portal.Operacoes;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 19/08/2005
 *
 * @author Carlos Feijão
 * @version 1.0
 *
 * Classe para remover um agendamento
 */
public class OpRemoveAgendamento extends OperacaoAbs {
	
	public boolean iniciaOperacao(String p_Mensagem) {
		
		
		try
		{
			setOperacao("Remove Agendamento");
			
			String posicao = m_Request.getParameter("posicao");
			String tipoCdr = m_Request.getParameter("tipoCdr");
			int tipoInt = Integer.parseInt(tipoCdr);
			String posArray[] = posicao.split("\t");
			
			iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
			for (int i = 0; i < posArray.length; i++)
			{
				int posInt = Integer.parseInt(posArray[i]);
				try
				{
					m_iAgenda.fnRemoveItemAgenda((short)tipoInt,(short)posInt);
				}
				catch(Exception e)
				{
					e.printStackTrace();			
				}
			}
			//Foi usado sendRedirect nesse caso por impossibilidade de setar o Atributo operacao no request
			m_Response.sendRedirect("/PortalOsx/servlet/Portal.cPortal?operacao=listaAgendamentoCDR&tipoCdr="+tipoCdr);
		}
		catch (Exception Exc)
		{
			System.out.println("OpRemoveAgendamento - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
		return true;
	}
	

}
