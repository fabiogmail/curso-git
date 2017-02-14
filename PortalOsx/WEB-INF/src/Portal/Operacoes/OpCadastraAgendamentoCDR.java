package Portal.Operacoes;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
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
 * Classe que monta a tela de edição do agendamento
 */
public class OpCadastraAgendamentoCDR extends OperacaoAbs
{

	
	public boolean iniciaOperacao(String p_Mensagem) 
	{
	
		try
		{
			setOperacao("Cadastro de CDR");

			String URL = null;
			URL="/templates/jsp/cdr-cadastro.jsp";
			
			String tipoCdrString = m_Request.getParameter("tipoCdr");
			String posicao = m_Request.getParameter("posicao");
			
			
			int tipoCdr = Integer.parseInt(tipoCdrString);
			
			ItemAgenda itemAgenda;
			if(tipoCdr == ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO)
			{
				itemAgenda = new ItemAgendaReprocCdrBruto();
			}
			else
			{
				itemAgenda = new ItemAgendaReprocCdrX();
			}
      
			String status = ItemAgenda.STATUS_ITEM_INATIVO+"";
			List centrais = new Vector();
			
			No noTmp = null;
			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
		    {
				try
		        {
					noTmp = (No) iter.next();
					List centraisTmp = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
					if(centraisTmp != null)
						centrais.addAll(centraisTmp);
		        
		        }
				catch(COMM_FAILURE comFail)
				{
					System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
				}
				catch(BAD_OPERATION badOp)
				{
					System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
					badOp.printStackTrace();
				}
		    }
			
			Collections.sort(centrais);
			
	 		Vector vetorItensAgn = new Vector();
	 		
	 		Vector regras = new Vector();
	 		
	 		Date dtInicial = null;
	 		Date dtFinal = null;
	 		Date dtExec = null;
 			
	 		
 			//Nas posições que tiverem algum valor, é agregado um sinal de '+' junto da posição
 			//isso é feito no cdr-listagem.jsp
	 		if(posicao.indexOf("+") != -1)//clicou em editar em uma posicao preenchida
	 		{

	 			posicao = posicao.substring(0,posicao.length()-1);
 				String objetoSerializado = "";
 				
 				if(tipoCdr == ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO)
 				{
 					objetoSerializado = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda().fnGetItemSerializado((short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO,(short)Integer.parseInt(posicao));
 					((ItemAgendaReprocCdrBruto)itemAgenda).desserializa(new StringBuffer(objetoSerializado));
 	 				dtInicial = (Date)((ItemAgendaReprocCdrBruto)itemAgenda).getPeriodoInicio();
 	 				dtFinal = (Date)((ItemAgendaReprocCdrBruto)itemAgenda).getPeriodoFim();
 	 				dtExec = (Date)((ItemAgendaReprocCdrBruto)itemAgenda).getDataHoraExecucao();
 	 				vetorItensAgn = ((ItemAgendaReprocCdrBruto)itemAgenda).getCentrais();
 	 				status = ((ItemAgendaReprocCdrBruto)itemAgenda).getStatusItemAgenda()+"";
 				}
 				else
 				{
 					objetoSerializado = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda().fnGetItemSerializado((short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X,(short)Integer.parseInt(posicao));
 					((ItemAgendaReprocCdrX)itemAgenda).desserializa(new StringBuffer(objetoSerializado));
 	 				dtInicial = (Date)((ItemAgendaReprocCdrX)itemAgenda).getPeriodoInicio();
 	 				dtFinal = (Date)((ItemAgendaReprocCdrX)itemAgenda).getPeriodoFim();
 	 				dtExec = (Date)((ItemAgendaReprocCdrX)itemAgenda).getDataHoraExecucao();
 	 				vetorItensAgn = ((ItemAgendaReprocCdrX)itemAgenda).getCentrais();
 	 				regras = ((ItemAgendaReprocCdrX)itemAgenda).getRegras();
 	 				status = ((ItemAgendaReprocCdrX)itemAgenda).getStatusItemAgenda()+"";
 				}

	 		}
	 		
	 		HashMap valores = new HashMap();
	 		
	 		valores.put("posicao", posicao);
	 		
	 		valores.put("diaInicial",  DataUtil.formataData(dtInicial, "dd"));
	 		valores.put("mesInicial",  DataUtil.formataData(dtInicial, "MM"));
	 		valores.put("anoInicial",  DataUtil.formataData(dtInicial, "yyyy"));
	 		valores.put("horaInicial", DataUtil.formataData(dtInicial, "HH"));
	 		valores.put("minInicial",  DataUtil.formataData(dtInicial, "mm"));
	 		
	 		valores.put("diaFinal",  DataUtil.formataData(dtFinal, "dd"));
	 		valores.put("mesFinal",  DataUtil.formataData(dtFinal, "MM"));
	 		valores.put("anoFinal",  DataUtil.formataData(dtFinal, "yyyy"));
	 		valores.put("horaFinal", DataUtil.formataData(dtFinal, "HH"));
	 		valores.put("minFinal",  DataUtil.formataData(dtFinal, "mm"));
	 		
	 		valores.put("diaExec",  DataUtil.formataData(dtExec, "dd"));
	 		valores.put("mesExec",  DataUtil.formataData(dtExec, "MM"));
	 		valores.put("anoExec",  DataUtil.formataData(dtExec, "yyyy"));
	 		valores.put("horaExec", DataUtil.formataData(dtExec, "HH"));
	 		valores.put("minExec",  DataUtil.formataData(dtExec, "mm"));
	 		
	 		valores.put("status", status);
	 		valores.put("centraisAgn", vetorItensAgn);
	 		valores.put("centrais", centrais);
	 		valores.put("regras", regras);
	 		valores.put("tipoCdr",tipoCdrString);
	 		
	 		m_Request.setAttribute("valores", valores);
	 		m_Request.setAttribute("tipoCdr",tipoCdrString);
	 		
	 		m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			
			
	        return true;
		}
		catch (Exception Exc)
		{
			System.out.println("OpCadastroAgendamentoCDR - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
	}
	 

}
