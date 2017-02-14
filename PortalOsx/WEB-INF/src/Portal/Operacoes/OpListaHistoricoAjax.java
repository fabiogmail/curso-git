package Portal.Operacoes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Dao.ConexaoBDAgenda;
import Portal.Dto.AgendaDTO;
import agenda.beans.dominio.Agenda;
import agenda.beans.dominio.PeriodosExecutados;
import agenda.beans.dominio.Agenda.PERIODICIDADE;
import agenda.beans.dominio.Agenda.STATUS;

public class OpListaHistoricoAjax extends OperacaoAbs {
	
	
	
	public OpListaHistoricoAjax() 
	{
	}

	No noTmp = null;
	
	public String listaHistoricos(String idSessao){
		try {
			ConexaoBDAgenda bd = new ConexaoBDAgenda();
			StringBuffer retorno = new StringBuffer();;
			//Lista de STATUS a serem procurados nos servidores de agenda.
			ArrayList<STATUS> listStatus = new ArrayList<STATUS>();
			listStatus.add(STATUS.AGENDADO);
			listStatus.add(STATUS.ANDAMENTO);
			//fim da lista de STATUS
			
				List<AgendaDTO> lista = null;
				
			int idPerfil = NoUtil.getUsuarioLogado(idSessao).getIdPerfil();
			String host = NoUtil.getNo().getHostName();
				
			lista = bd.listaAgendas(idPerfil,host , listStatus, true);
			
			//for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			for (int i = 0; i < lista.size(); i++) {
				AgendaDTO agenda = (AgendaDTO) lista.get(i);
				if(agenda.getRelatoriosCfg() != null /*&& agenda.isEh_historico()&& (agenda.getStatus() == STATUS.AGENDADO || agenda.getStatus() == STATUS.ANDAMENTO)*/){
					//if(NoUtil.getUsuarioLogado(idSessao).getPerfil().equalsIgnoreCase(agenda.getRelatoriosCfg().getPerfil().getNome())
							//|| NoUtil.getUsuarioLogado(idSessao).getPerfil().equalsIgnoreCase("admin")){
						retorno.append(agenda.getRelatoriosCfg().getPerfil().getNome() + ";");//perfil
						retorno.append(agenda.getRelatoriosCfg().getTipoRelatorio().getNome() + ";");//tipo
						retorno.append(agenda.getRelatoriosCfg().getNome() + ";");//nome relatorio
						retorno.append(agenda.getUsuario() + ";");//usuario
						retorno.append(formataData(agenda.getInicio()) + ";");//data inicial
						retorno.append(formataData(agenda.getTermino()) + ";");//data inicial
				if(agenda.getHoraInicio() != null && agenda.getHoraTermino() != null){
							retorno.append(formataHora(agenda.getHoraInicio()) + ";");//consolidação inicial
							retorno.append(formataHora(agenda.getHoraTermino()) + ";");//consolidação final
				}else{
							retorno.append(formataHora(agenda.getInicio()) + ";");//hora inicial
							retorno.append(formataHora(agenda.getTermino()) + ";");//hora inicial
				}
				
						retorno.append(calculaLatencia(agenda.getLatencia()) + ";");
						retorno.append(agenda.getPeriodicidade() + ";");
						retorno.append(criaStatus(agenda, bd)+";");
//						retorno.append(getExecucao(agenda));//execuções
						retorno.append("\n");
					//}
					}
			}
			System.gc();
			if(retorno.length() > 0){
				if(retorno.lastIndexOf("\n") == retorno.length() -1)
					return retorno.deleteCharAt(retorno.lastIndexOf("\n")).toString();

				return retorno.toString();
			} else if(retorno.length() == 0){
				return retorno.toString();
			}else
				return "erro";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "erro";
		}
		
	}
	
	
	private String calculaLatencia(int latencia){
	
		String retorno = "";
		
		int segundos = latencia;

		int dia = (segundos/86400);		
		segundos = (segundos%86400);	

		int hora = (segundos/3600);
		segundos = (segundos%3600);	

		int minuto = (segundos/60);
		
		if(dia < 10)
			retorno += "0"+dia+":";
		else
			retorno += dia+":";
		
		if(hora < 10)
			retorno += "0"+hora+":";
		else retorno += hora+":";
		
		if(minuto < 10)
			retorno += "0"+minuto;
		else
			retorno += minuto;
		return  retorno+" (dd:hh:mm)";
	}

	/** Retorna a configuração de execução da série Histórica
	 * @param agenda
	 * @return
	 */
	private String getExecucao(Agenda agenda) {
		// TODO Auto-generated method stub
		
		String retorno = "";

		if(agenda.getPeriodicidade() == PERIODICIDADE.MES /*&& agenda.getDiasDoMes() != null && agenda.getDiasDoMes().split(",").length > 1*/ ){
//			retorno += "Mensal,";
			retorno += "Dias: "+agenda.getDiasDoMes().replace(",", "-");
//			retorno += "Dias: "+getDiasMesExecutar(agenda);
		} else if( agenda.getPeriodicidade() == PERIODICIDADE.SEMANA /*&& agenda.getDiasDaSemana()!= null && agenda.getDiasDaSemana().length() > 0 && agenda.getDiasDaSemana().split(",").length > 0*/){
			retorno += "Semanal,";
			retorno += "Dias da Semana: "+agenda.getDiasDaSemana().replace(",", "-")+",";
			retorno += "Ocorrência: "+agenda.getSemanasDeOcorrencia().replace(",", "-")+",";
		} else{
			retorno += "&Uacutenica";
		}
		
		
		
		return retorno;
	}



	/** Retorna as execucoes da série hiostórica.
	 * @param agenda
	 * @return
	 */
	private String getExecs(Agenda agenda) {
		Set<PeriodosExecutados> set = agenda.getPeriodosExecutados();
		StringBuffer retorno = new StringBuffer();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			PeriodosExecutados periodos = (PeriodosExecutados) iterator.next();
			retorno.append(formataData(periodos.getData()));
			
			if(iterator.hasNext())
				retorno.append(",");
			
		}
		
		
		return retorno.toString();
	}

	/** Método responsável por criar a item de STATUS na interface de listagem das agendas.
	 * Aqui também é apresentada a progressão da execução.
	 * @param name
	 * @return
	 */
	private String criaStatus(AgendaDTO agenda, ConexaoBDAgenda bd) {
		// TODO Auto-generated method stub
		String status = bd.getStatusPercentual(agenda);
		
//		double progresso = (agenda.getProgresso()*100);
//		DecimalFormat format = new DecimalFormat();  
//		 format.setMaximumFractionDigits(2); 
//		 format.setMinimumFractionDigits(2); 
		
		if(agenda.getStatus() == STATUS.ANDAMENTO){
				if(agenda.isProcessando())
			return agenda.getStatus().name() + " ("+status+")";
				else
					return STATUS.AGENDADO.name();
		}else
			return agenda.getStatus().name();
		
	}

	/** Retorna a data no formato DD/MM/YYYY
	 * @param data
	 * @return
	 */
	private String formataData(Date data) {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();
		
		gc.setTime(data);
		
		int dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
		int mes = gc.get(GregorianCalendar.MONTH)+1;
		int ano = gc.get(GregorianCalendar.YEAR);
		
		String sDia = "";
		if(dia < 10)
			sDia = "0"+Integer.toString(dia);
		else
			sDia = Integer.toString(dia);
		
		String sMes = "";
		if(mes < 10)
			sMes = "0"+Integer.toString(mes);
		else
			sMes = Integer.toString(mes);
		
		String sAno = "";
		sAno = Integer.toString(ano);
		
		return sDia+"/"+sMes+"/"+sAno;
		
		
	}
	
	/** Retorna a data no formato HH:MI:SS
	 * @param data
	 * @return
	 */
	private String formataHora(Date data) {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();
		
		gc.setTime(data);
		
		String retorno = "";
		
		int hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
		int minuto = gc.get(GregorianCalendar.MINUTE);
		int segundo = gc.get(GregorianCalendar.SECOND);
				
		String sHora = "";
		if(hora < 10)
			sHora = "0"+Integer.toString(hora);
		else
			sHora = Integer.toString(hora);
		
		String sMinuto = "";
		if(minuto < 10)
			sMinuto = "0"+Integer.toString(minuto);
		else
			sMinuto = Integer.toString(minuto);
		
		String sSegundo = "";
		if(segundo < 10)
			sSegundo = "0"+Integer.toString(segundo);
		else
			sSegundo = Integer.toString(segundo);
		
		return sHora+":"+sMinuto+":"+sSegundo;
		
		
	}
	
	/** Retorna a data no formato DD/MM/YYYY HH:MI:SS
	 * @param data
	 * @return
	 */
	private String formataDataCompleta(Date data) {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();
		
		gc.setTime(data);
		
		String retorno = "";
		
		int dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
		int mes = gc.get(GregorianCalendar.MONTH)+1;
		int ano = gc.get(GregorianCalendar.YEAR);
		int hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
		int minuto = gc.get(GregorianCalendar.MINUTE);
		int segundo = gc.get(GregorianCalendar.SECOND);
		
		String sDia = "";
		if(dia < 10)
			sDia = "0"+Integer.toString(dia);
		else
			sDia = Integer.toString(dia);
		
		String sMes = "";
		if(mes < 10)
			sMes = "0"+Integer.toString(mes);
		else
			sMes = Integer.toString(mes);
		
		String sAno = "";
		sAno = Integer.toString(ano);
		
		String sHora = "";
		if(hora < 10)
			sHora = "0"+Integer.toString(hora);
		else
			sHora = Integer.toString(hora);
		
		String sMinuto = "";
		if(minuto < 10)
			sMinuto = "0"+Integer.toString(minuto);
		else
			sMinuto = Integer.toString(minuto);
		
		String sSegundo = "";
		if(segundo < 10)
			sSegundo = "0"+Integer.toString(segundo);
		else
			sSegundo = Integer.toString(segundo);
		
		return sDia+"/"+sMes+"/"+sAno+" "+sHora+":"+sMinuto+":"+sSegundo;
		
		
	}

	public static void main(String[] args) {
		
//		IManutencaoAgenda manutencaoAgenda = AgendaRMI.retornaManutencaoAgenda("192.168.200.151");
//		IManutencaoAgenda manutencaoAgenda = AgendaRMI.retornaManutencaoAgenda();
		
//		try {
//			List<agenda.beans.dominio.Agenda> lista = manutencaoAgenda.buscarTudo(true);

//			for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
//				Agenda agenda = (Agenda) iterator.next();

//				System.out.println(agenda.getNome());
//				System.out.println(agenda.getIdAgenda());

//			}
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}
