package Portal.Operacoes;

import Portal.Cluster.NoUtil;

public class OpSalvaOrdemProcessamento extends OperacaoAbs {
	public boolean iniciaOperacao(String p_Mensagem)
	{
		setOperacao("Salva Ordem de processamento de CDR");
		String URL = "/templates/jsp/ordemProcessamentoCDR.jsp";
		try{
			String ordem = m_Request.getParameter("ordemProcessamento");
			boolean ordemInversa = false;
			if(ordem.equalsIgnoreCase("ordemInversa")){
				ordemInversa = true;
			}
			try{
				NoUtil.getNoCentral().getConexaoServUtil().getM_iUtil().fnSetOrdemInvProcParser(ordemInversa);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		}catch (Exception Exc)
		{
			System.out.println("OpListaAgendamentoCDR - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
	}
}
