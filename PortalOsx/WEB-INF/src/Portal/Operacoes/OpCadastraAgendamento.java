package Portal.Operacoes;

import java.util.Date;
import java.util.Vector;

import Interfaces.iAgenda;
import Portal.Cluster.NoUtil;
import Portal.ReproCdr.CentralItemAgn;
import Portal.ReproCdr.ItemAgenda;
import Portal.ReproCdr.ItemAgendaReprocCdrBruto;
import Portal.ReproCdr.ItemAgendaReprocCdrX;
import Portal.ReproCdr.RegraItemAgn;
import Portal.Utils.DataUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 18/08/2005
 *
 * @author Carlos Feijão
 * @version 1.0
 *
 * Classe que faz o cadastro dos agendamentos CDR-X e CDR-Bruto
 */
public class OpCadastraAgendamento extends OperacaoAbs {
	
	
	public boolean iniciaOperacao(String p_Mensagem) 
	{
	
		try
		{
			setOperacao("Cadastro de Agendamento");
			
			String tipoCdrString = m_Request.getParameter("tipoCdr");
			int tipoCdr = Integer.parseInt(tipoCdrString);
			
			//Obtendo valores
			String posicao = m_Request.getParameter("posicao");
			String centrais = m_Request.getParameter("centrais");
			String regras = m_Request.getParameter("regras");
			String status = m_Request.getParameter("status");
			String acao = m_Request.getParameter("acao");
			
			//caso o usuario estiver agendando, ele inicia o status do item
			boolean agendar = false;
			if(acao.equals("agendar")){
				agendar = true;
				status = ItemAgenda.STATUS_ITEM_AGENDADO+"";
			}
			
			
			// obtendo as datas
	 		String dataInicial = m_Request.getParameter("diaInicial")+"/"+m_Request.getParameter("mesInicial")+"/"+m_Request.getParameter("anoInicial");
	 		String horaInicial = m_Request.getParameter("horaInicial")+":"+m_Request.getParameter("minInicial")+":00";
	 		
	 		String dataFinal = m_Request.getParameter("diaFinal")+"/"+m_Request.getParameter("mesFinal")+"/"+m_Request.getParameter("anoFinal");
	 		String horaFinal = m_Request.getParameter("horaFinal")+":"+m_Request.getParameter("minFinal")+":00";
	 		
	 		String dataExec = m_Request.getParameter("diaExec")+"/"+m_Request.getParameter("mesExec")+"/"+m_Request.getParameter("anoExec");
	 		String horaExec = m_Request.getParameter("horaExec")+":"+m_Request.getParameter("minExec")+":00";
			
			
	 		dataInicial = DataUtil.dataToLong(dataInicial+" "+horaInicial);
	 		dataFinal = DataUtil.dataToLong(dataFinal+" "+horaFinal);
	 		dataExec = DataUtil.dataToLong(dataExec+" "+horaExec);
			
			Date dInicial = new Date(DataUtil.dataInMillis(dataInicial));
			Date dFinal = new Date(DataUtil.dataInMillis(dataFinal));
			Date dExec = new Date(DataUtil.dataInMillis(dataExec));
			
			//criando a lista de centrais
			String centraisArray[] = centrais.split("\t");
			Vector centraisVector = new Vector();
			for(int i = 0;i<centraisArray.length;i++)
			{
				CentralItemAgn cia = new CentralItemAgn();
				cia.desserializa(new StringBuffer(centraisArray[i]));
				//se estiver agendando inicializa o status de cada central
				if(agendar)
				{
					cia.inicializar();
				}
				centraisVector.add(cia); 
			}
			
			//criando a lista de regras, no caso de CDR-Bruto, essa lista fica vazia
			String regrasArray[] = regras.split("\t");
			Vector regrasVector = new Vector();
			if(!regras.equals(""))
			{
				for (int i = 0; i < regrasArray.length; i++)
				{
					RegraItemAgn ria = new RegraItemAgn();
					ria.desserializa(new StringBuffer(regrasArray[i]));
					regrasVector.add(ria);
				}
			}
			
			ItemAgenda itemAgenda;
			
		
			iAgenda m_iAgenda = NoUtil.getNoCentral().getConexaoServUtil().getM_iAgenda();
			if(tipoCdr == ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO)
			{
				itemAgenda = new ItemAgendaReprocCdrBruto();
				((ItemAgendaReprocCdrBruto)itemAgenda).setPosicao(Integer.parseInt(posicao));
				((ItemAgendaReprocCdrBruto)itemAgenda).setPeriodoInicio(dInicial);
				((ItemAgendaReprocCdrBruto)itemAgenda).setPeriodoFim(dFinal);
				((ItemAgendaReprocCdrBruto)itemAgenda).setDataHoraExecucao(dExec);
				((ItemAgendaReprocCdrBruto)itemAgenda).setCentrais(centraisVector);
				((ItemAgendaReprocCdrBruto)itemAgenda).setStatusItemAgenda(Integer.parseInt(status));
				((ItemAgendaReprocCdrBruto)itemAgenda).setTipoItemAgenda(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO);
				//serializando objeto e editando
				m_iAgenda.fnAgendarItens(((ItemAgendaReprocCdrBruto)itemAgenda).serializa(),(short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_BRUTO);
				
			}
			else
			{
				itemAgenda = new ItemAgendaReprocCdrX();
				((ItemAgendaReprocCdrX)itemAgenda).setPosicao(Integer.parseInt(posicao));
				((ItemAgendaReprocCdrX)itemAgenda).setPeriodoInicio(dInicial);
				((ItemAgendaReprocCdrX)itemAgenda).setPeriodoFim(dFinal);
				((ItemAgendaReprocCdrX)itemAgenda).setDataHoraExecucao(dExec);
				((ItemAgendaReprocCdrX)itemAgenda).setCentrais(centraisVector);
				((ItemAgendaReprocCdrX)itemAgenda).setRegras(regrasVector);
				((ItemAgendaReprocCdrX)itemAgenda).setStatusItemAgenda(Integer.parseInt(status));
				((ItemAgendaReprocCdrX)itemAgenda).setTipoItemAgenda(ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X);
				//serializando objeto e editando
				m_iAgenda.fnAgendarItens(((ItemAgendaReprocCdrX)itemAgenda).serializa(),(short)ItemAgenda.TIPO_ITEM_AGN_REPROC_CDR_X);
		
			}
			
			
			
			
			
			//Foi usado sendRedirect nesse caso por impossibilidade de setar o Atributo operacao no request
		 	m_Response.sendRedirect("/PortalOsx/servlet/Portal.cPortal?operacao=listaAgendamentoCDR&tipoCdr="+tipoCdr);
		 
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
