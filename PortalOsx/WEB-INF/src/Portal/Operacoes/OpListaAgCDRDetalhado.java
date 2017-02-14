package Portal.Operacoes;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;
import Portal.ReproCdr.ItemAgenda;
import Portal.ReproCdr.ItemAgendaReprocCdrBruto;
import Portal.ReproCdr.ItemAgendaReprocCdrX;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 22/08/2005
 *
 * @author Carlos Feijão
 * @version 1.0
 *
 * Classe que lista a configuração detalhada do agendamento
 */
public class OpListaAgCDRDetalhado extends OperacaoAbs {
	
	public boolean iniciaOperacao(String p_Mensagem) {
		
		
		try
		{
			setOperacao("Detalhar Agendamento");
			String URL = null;
		 	URL="/templates/jsp/cdr-detalhado.jsp";
		 	
			String posicao = m_Request.getParameter("posicao");
			String tipoCdr = m_Request.getParameter("tipoCdr");
			
			iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
			
			ItemAgenda itemAgenda = null;
			String objetoSerializado = "";
			if(tipoCdr.equals(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO+""))
			{
				objetoSerializado = m_iAgenda.fnGetItemSerializado((short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO,(short)Integer.parseInt(posicao));
				itemAgenda = new ItemAgendaReprocCdrBruto();
				((ItemAgendaReprocCdrBruto)itemAgenda).desserializa(new StringBuffer(objetoSerializado));
			}
			else
			{
				objetoSerializado = m_iAgenda.fnGetItemSerializado((short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X,(short)Integer.parseInt(posicao));
				itemAgenda = new ItemAgendaReprocCdrX();
				((ItemAgendaReprocCdrX)itemAgenda).desserializa(new StringBuffer(objetoSerializado));
			}
			
			m_Request.setAttribute("itemAgenda",itemAgenda);
			m_Request.setAttribute("tipoCdr",tipoCdr);

			
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
		}
		catch (Exception Exc)
		{
			System.out.println("OpListaAgCDRDetalhado - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
		return true;
	}
	

}

