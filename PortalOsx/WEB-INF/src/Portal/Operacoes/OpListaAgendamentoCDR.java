package Portal.Operacoes;

import java.util.HashMap;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;
import Portal.ReproCdr.ItemAgenda;
import Portal.ReproCdr.ItemAgendaReprocCdrBruto;
import Portal.ReproCdr.ItemAgendaReprocCdrX;
import Portal.Utils.DataUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 18/08/2005
 *
 * @author Carlos Feijão
 * @version 1.0
 *
 * Classe que lista todos os agendamentos cadastrados, faiz a listagem de acordo
 * com o tipo de CDR
 */
public class OpListaAgendamentoCDR extends OperacaoAbs{

	
	public boolean iniciaOperacao(String p_Mensagem) 
	{
	      //System.out.println("OpBilhetadores - iniciaOperacao()");
		try
		{
			setOperacao("Listagem de CDR");
			
			//verifica qual tipo de listagem
			String tipoCdrStr = m_Request.getParameter("tipoCdr");
			int tipoCdr = 0;
			if(tipoCdrStr != null)
			{
				tipoCdr = Integer.parseInt(tipoCdrStr);
			}
			
			if(tipoCdr == ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO)
			{
				tipoCdr = ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO;
			}
			else
			{
				tipoCdr = ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X;
			}
			
			String URL = null;
		 	URL="/templates/jsp/cdr-listagem.jsp";
		 	
		 	iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
		 	String items = "";
		 	
		 	//buscando itemAgenda de acordo com o tipo
		 	if(tipoCdr == ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO)
		 	{
		 		items = m_iAgenda.fnGetItensAgendados((short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO);
		 	}
		 	else
		 	{
		 		items = m_iAgenda.fnGetItensAgendados((short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X);
		 	}
		 	
		 	//Lista de HashMaps do itemAgenda, os maps têm as informações de cada item
		 	//No máximo 20 itens, hash com tamanho 21, para que exista o listaCdr[20];
		 	HashMap[] listaCdr = new HashMap[21];
		 	
		 	if(!items.equals(""))
		 	{
		 		//monta um array com cada item serializado
			 	String[] itemsArray = items.split("\n");
			 	
			 	StringBuffer sb;
			 	for (int i = 0; i < itemsArray.length; i++) 
			 	{
			 		if(tipoCdr == ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO)
			 		{
			 			ItemAgendaReprocCdrBruto itemAgenda = new ItemAgendaReprocCdrBruto();
			 			itemAgenda.desserializa(new StringBuffer(itemsArray[i]));
			 			//As chaves desse hash não podem ser alteradas, pois são usadas no jsp
						HashMap valores = new HashMap();
						valores.put("posicao",itemAgenda.getPosicao()+"");
						valores.put("dataExec",DataUtil.formataData(itemAgenda.getDataHoraExecucao(),"dd/MM/yyyy HH:mm:ss"));
						valores.put("dataInicial",DataUtil.formataData(itemAgenda.getPeriodoInicio(),"dd/MM/yyyy HH:mm:ss"));
						valores.put("dataFinal",DataUtil.formataData(itemAgenda.getPeriodoFim(),"dd/MM/yyyy HH:mm:ss"));
						valores.put("centrais",itemAgenda.getCentrais());
						valores.put("status",itemAgenda.getStatusItemAgenda()+"");
			 			//adicionando o Hash na posição correspondente ao ItemAgenda
						listaCdr[itemAgenda.getPosicao()] = valores;
			 		}
			 		else
			 		{
			 			ItemAgendaReprocCdrX itemAgenda = new ItemAgendaReprocCdrX();
			 			itemAgenda.desserializa(new StringBuffer(itemsArray[i]));
			 			//As chaves desse hash não podem ser alteradas, pois são usadas no jsp
						HashMap valores = new HashMap();
						valores.put("posicao",itemAgenda.getPosicao()+"");
						valores.put("dataExec",DataUtil.formataData(itemAgenda.getDataHoraExecucao(),"dd/MM/yyyy HH:mm:ss"));
						valores.put("dataInicial",DataUtil.formataData(itemAgenda.getPeriodoInicio(),"dd/MM/yyyy HH:mm:ss"));
						valores.put("dataFinal",DataUtil.formataData(itemAgenda.getPeriodoFim(),"dd/MM/yyyy HH:mm:ss"));
						valores.put("centrais",itemAgenda.getCentrais());
						valores.put("status",itemAgenda.getStatusItemAgenda()+"");
						//adicionando o Hash na posição correspondente ao ItemAgenda
						listaCdr[itemAgenda.getPosicao()] = valores;
			 			
			 		}
			 		
			 		
				} 
		 	}
		 	
	 		m_Request.setAttribute("listaCdr", listaCdr);
	 		m_Request.setAttribute("tipoCdr",tipoCdr+"");
		 	
	 		m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			
			return true;
		}
		catch (Exception Exc)
		{
			System.out.println("OpListaAgendamentoCDR - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
	}
	


}