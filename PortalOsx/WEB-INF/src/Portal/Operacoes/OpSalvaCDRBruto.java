package Portal.Operacoes;

import java.util.Date;
import java.util.Vector;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;
import Portal.ReproCdr.CentralItemAgn;
import Portal.ReproCdr.ItemAgenda;
import Portal.ReproCdr.ItemAgendaCdrBruto;
import Portal.Utils.DataUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsxCluster
 * Arquivo criado em: 31/10/2005
 * 
 * @author Erick Rodrigo
 * @version 1.0
 * 
 * Breve Descrição: operação que salva o cdr bruto
 * 
 */


public class OpSalvaCDRBruto extends OperacaoAbs {

	public boolean iniciaOperacao(String p_Mensagem) 
	{
		try
		{
			setOperacao("Cadastro de CDR Bruto");
				
			//Obtendo valores
			String posicao = m_Request.getParameter("posicao");
			String centrais = m_Request.getParameter("centrais");
			String status = "0";//m_Request.getParameter("status");
	 		
			// so tem a data mas para ficar compativel coloca-se zero na hora pra ficar assim dd/mm/yyyy hh:mm:ss
	 		String dataExec = m_Request.getParameter("dia")+"/"+m_Request.getParameter("mes")+"/"+m_Request.getParameter("ano")+" "+"00"+":"+"00"+":"+"00";

	 		dataExec = DataUtil.dataToLong(dataExec);
	 		
			Date dExec = new Date(DataUtil.dataInMillis(dataExec));
			
			//criando a lista de centrais
			String centraisArray[] = centrais.split("\t");
			Vector centraisVector = new Vector();
			for(int i = 0;i<centraisArray.length;i++)
			{
				CentralItemAgn cia = new CentralItemAgn();
				cia.desserializa(new StringBuffer(centraisArray[i]));
				centraisVector.add(cia); 
			}
			
			//criando a lista de regras, no caso de CDR-Bruto, essa lista fica vazia
			iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
			ItemAgendaCdrBruto itemAgendaCDRBruto = new ItemAgendaCdrBruto();
			itemAgendaCDRBruto.setPosicao(Integer.parseInt(posicao));
			itemAgendaCDRBruto.setDataHoraExecucao(dExec);
			itemAgendaCDRBruto.setCentrais(centraisVector);
			itemAgendaCDRBruto.setStatusItemAgenda(Integer.parseInt(status));
			itemAgendaCDRBruto.setTipoItemAgenda(ItemAgenda.TIPO_ITEM_CDR_BRUTO);
			//serializando objeto e editando
			m_iAgenda.fnAgendarItens(((ItemAgendaCdrBruto)itemAgendaCDRBruto).serializa(),(short)ItemAgenda.TIPO_ITEM_CDR_BRUTO);

	
			//Foi usado sendRedirect nesse caso por impossibilidade de setar o Atributo operacao no request
		 	m_Response.sendRedirect("/PortalOsx/servlet/Portal.cPortal?operacao=listagemCDRBruto&tipoCdr="+ItemAgenda.TIPO_ITEM_CDR_BRUTO);
		 
	        return true;
		}
		catch (Exception Exc)
		{
			System.out.println("OpCadastroAgendamento - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
	}
		

}
