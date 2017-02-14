package Portal.Operacoes;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 02/09/2005
 *
 * @author Carlos Feijão
 * @version 1.0
 *
 * Classe para manipular a base do reprocessamento
 */
public class OpBaseReprocessada extends OperacaoAbs {
	
	public boolean iniciaOperacao(String p_Mensagem) {
		
		
		try
		{
			setOperacao("Base Reprocessada");
			
			String posicao = m_Request.getParameter("posicao");
			String tipoCdr = m_Request.getParameter("tipoCdr");
			String acao    = m_Request.getParameter("acao");
			int tipoCdrInt = Integer.parseInt(tipoCdr);
			int posicaoInt = Integer.parseInt(posicao);
			
			
						
			iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
			
			if(acao.equals("mover"))
			{
				m_iAgenda.fnMoveBaseRep((short)tipoCdrInt,(short)posicaoInt,true);
			}
			else if (acao.equals("apagar"))
			{
				m_iAgenda.fnMoveBaseRep((short)tipoCdrInt,(short)posicaoInt,false);
			}
			
			
			//Foi usado sendRedirect nesse caso por impossibilidade de setar o Atributo operacao no request
			m_Response.sendRedirect("/PortalOsx/servlet/Portal.cPortal?operacao=listaAgendamentoCDR&tipoCdr="+tipoCdr);
		}
		catch (Exception Exc)
		{
			System.out.println("OpBaseReprocessada - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
		return true;
	}

}
