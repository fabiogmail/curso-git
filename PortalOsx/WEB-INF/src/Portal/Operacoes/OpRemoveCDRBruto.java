package Portal.Operacoes;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;
import Portal.ReproCdr.ItemAgenda;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsxCluster
 * Arquivo criado em: 28/10/2005
 * 
 * @author Erick Rodrigo
 * @version 1.0
 * 
 * Breve Descrição: classe que remove os cdrs brutos
 * 
 */

public class OpRemoveCDRBruto extends OperacaoAbs {
public boolean iniciaOperacao(String p_Mensagem) {
		
		
		try
		{
			setOperacao("Remove CDR Bruto");
			
			String posicao = m_Request.getParameter("posicao");

			String posArray[] = posicao.split("\t");
			
			iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
			for (int i = 0; i < posArray.length; i++)
			{
				int posInt = Integer.parseInt(posArray[i]);
				try
				{
					m_iAgenda.fnRemoveItemAgenda((short)ItemAgenda.TIPO_ITEM_CDR_BRUTO,(short)posInt);
				}
				catch(Exception e)
				{
					e.printStackTrace();			
				}
			}
			//Foi usado sendRedirect nesse caso por impossibilidade de setar o Atributo operacao no request
			m_Response.sendRedirect("/PortalOsx/servlet/Portal.cPortal?operacao=listagemCDRBruto&tipoCdr="+(short)ItemAgenda.TIPO_ITEM_CDR_BRUTO);
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

