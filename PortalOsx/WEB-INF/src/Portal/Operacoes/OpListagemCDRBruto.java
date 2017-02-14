package Portal.Operacoes;

import java.util.HashMap;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;
import Portal.ReproCdr.ItemAgenda;
import Portal.ReproCdr.ItemAgendaCdrBruto;
import Portal.ReproCdr.ItemAgendaReprocCdrBruto;
import Portal.ReproCdr.ItemAgendaReprocCdrX;
import Portal.Utils.DataUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsxCluster
 * Arquivo criado em: 27/10/2005
 * 
 * @author Erick Rodrigo
 * @version 1.0
 * 
 * Breve Descrição: classe que lista os cdrs brutos
 * 
 */

public class OpListagemCDRBruto extends OperacaoAbs{

	
	public boolean iniciaOperacao(String p_Mensagem) 
	{
	      //System.out.println("OpBilhetadores - iniciaOperacao()");
		try
		{
			setOperacao("Listagem de CDRBruto");
	
			String URL = null;
		 	URL="/templates/jsp/cdr-listagemCDRBruto.jsp";
		 	iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
		 	String items = ""; 

		 	items = m_iAgenda.fnGetItensAgendados((short)ItemAgenda.TIPO_ITEM_CDR_BRUTO);

		 	//Lista de HashMaps do itemAgenda, os maps têm as informações de cada item
		 	//No máximo 20 itens, hash com tamanho 21, para que exista o listaCdr[20];
		 	HashMap[] listaCdr = new HashMap[21];
		 	
		 	if(!items.equals(""))
		 	{
		 		//monta um array com cada item serializado
			 	String[] itemsArray = items.split("\n");
			 	
			 	for (int i = 0; i < itemsArray.length; i++) 
			 	{
			 		ItemAgendaCdrBruto itemAgendaCdrBruto = new ItemAgendaCdrBruto();
			 		itemAgendaCdrBruto.desserializa(new StringBuffer(itemsArray[i]));
		 			//As chaves desse hash não podem ser alteradas, pois são usadas no jsp
					HashMap valores = new HashMap();
					valores.put("posicao",itemAgendaCdrBruto.getPosicao()+"");
					valores.put("dataExec",DataUtil.formataData(itemAgendaCdrBruto.getDataHoraExecucao(),"dd/MM/yyyy"));
					valores.put("centrais",itemAgendaCdrBruto.getCentrais());
		 			//adicionando o Hash na posição correspondente ao ItemAgenda
					listaCdr[itemAgendaCdrBruto.getPosicao()] = valores; 		
			 		
				} 
		 	}
		 	
	 		m_Request.setAttribute("listaCdr", listaCdr);
	 		m_Request.setAttribute("tipoCdr",ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO+"");
		 	
	 		m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		}
		catch (Exception Exc)
		{
			System.out.println("OpListagemCDRBruto - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
	}
}