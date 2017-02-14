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
import Portal.ReproCdr.ItemAgendaCdrBruto;
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
public class OpCadastraCDRBruto extends OperacaoAbs
{
	public boolean iniciaOperacao(String p_Mensagem) 
	{
	
		try
		{
			setOperacao("Cadastro de CDR Bruto");

			String URL = null;
			URL="/templates/jsp/cdr-cadastroCDRBruto.jsp";
			
			String posicao = m_Request.getParameter("posicao");
	
			ItemAgendaCdrBruto itemAgendaCdrBruto = new ItemAgendaCdrBruto();

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
	 		
	 		Date dtExec = null;
		
 			//Nas posições que tiverem algum valor, é agregado um sinal de '+' junto da posição
 			//isso é feito no cdr-listagem.jsp
	 		if(posicao.indexOf("+") != -1)//clicou em editar em uma posicao preenchida
	 		{

	 			posicao = posicao.substring(0,posicao.length()-1);
 				String objetoSerializado = "";
 				 			
				objetoSerializado = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda().fnGetItemSerializado((short)ItemAgenda.TIPO_ITEM_CDR_BRUTO,(short)Integer.parseInt(posicao));
				itemAgendaCdrBruto.desserializa(new StringBuffer(objetoSerializado));
 				dtExec = (Date)itemAgendaCdrBruto.getDataHoraExecucao();
 				vetorItensAgn = itemAgendaCdrBruto.getCentrais(); 	 				

	 		}
	 		
	 		HashMap valores = new HashMap();
	 		
	 		valores.put("posicao", posicao);
	 		
	 		valores.put("dia",  DataUtil.formataData(dtExec, "dd"));
	 		valores.put("mes",  DataUtil.formataData(dtExec, "MM"));
	 		valores.put("ano",  DataUtil.formataData(dtExec, "yyyy"));
	 		
	 		valores.put("centraisAgn", vetorItensAgn);
	 		valores.put("centrais", centrais);
	 		valores.put("regras", regras);

	 		m_Request.setAttribute("valores", valores);
	 		
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
