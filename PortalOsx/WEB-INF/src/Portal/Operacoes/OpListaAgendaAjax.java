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
import Portal.Dto.ExecucoesAgendaDTO;
import agenda.beans.dominio.Agenda;
import agenda.beans.dominio.PeriodosExecutados;
import agenda.beans.dominio.Agenda.STATUS;

public class OpListaAgendaAjax extends OperacaoAbs {



	public OpListaAgendaAjax() 
	{
	}

	No noTmp = null;




	public String getRelatorio(int pagina){
		return "";
	}

	public String listaAgendas(String idSessao){



		try {
			ConexaoBDAgenda bd = new ConexaoBDAgenda();
			StringBuffer retorno = new StringBuffer();			
			//Lista de STATUS a serem procurados nos servidores de agenda.
			ArrayList<STATUS> listStatus = new ArrayList<STATUS>();
			listStatus.add(STATUS.AGENDADO);
			listStatus.add(STATUS.ANDAMENTO);

			List<AgendaDTO> lista = null;
			int idPerfil = NoUtil.getUsuarioLogado(idSessao).getIdPerfil();
			String host = NoUtil.getNo().getHostName();

			lista = bd.listaAgendas(idPerfil,host , listStatus, false);

			for (int i = 0; i < lista.size(); i++) {

				AgendaDTO agenda = (AgendaDTO) lista.get(i);
				if(agenda.getRelatoriosCfg() != null /*&& !agenda.isEh_historico() && (agenda.getStatus() == STATUS.AGENDADO || agenda.getStatus() == STATUS.ANDAMENTO)*/){
					retorno.append(agenda.getRelatoriosCfg().getPerfil().getNome() + ";");//perfil
					retorno.append(agenda.getRelatoriosCfg().getTipoRelatorio().getNome() + ";");//tipo
					retorno.append(agenda.getRelatoriosCfg().getNome() + ";");//nome relatório
					retorno.append(agenda.getUsuario() + ";");//usuario
					retorno.append(formataData(agenda.getInicio())+";");//data inicio
					retorno.append(formataHora(agenda.getInicio())+";");//hora inicio	
					retorno.append(criaStatus(agenda, bd) + ";"); // status
					//							retorno.append(getExecucao(agenda));//execucoes
					retorno.append(getCenarioExecucao(agenda, bd));
					retorno.append("\n");
				}
			}

			System.gc();
			if(retorno.length() > 0){
				if(retorno.lastIndexOf("\n") == retorno.length() -1 )
					return retorno.deleteCharAt(retorno.lastIndexOf("\n")).toString();

				return retorno.toString();
			} else if(retorno.length() == 0){
				return retorno.toString();
			} else
				return "erro";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "erro";
		}

	}





	/** Método responsável por criar a item de STATUS na interface de listagem das agendas.
	 * Aqui também é apresentada a progressão da execução.
	 * @param name
	 * @return
	 */
	private String criaStatus(AgendaDTO agenda, ConexaoBDAgenda bd) {
		// TODO Auto-generated method stub

		//String status = bd.getStatusPercentual(agenda);

		//		double progresso = (agenda.getProgresso()*100);
		//		 DecimalFormat format = new DecimalFormat();  
		//		 format.setMaximumFractionDigits(2); 
		//		 format.setMinimumFractionDigits(2); 

		if(agenda.getStatus() == STATUS.ANDAMENTO){
			if(agenda.isProcessando())
				return agenda.getStatus().name();
			else
				return STATUS.AGENDADO.name();
		}else
			return agenda.getStatus().name();

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
		// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}


	/** Metodo responsável por retornar, para cada agenda, a execução corrente (se houver) e as próximas X execucoes.
	 * @param agenda
	 * @param bd 
	 * @return
	 */
	private String getCenarioExecucao(AgendaDTO agenda, ConexaoBDAgenda bd){

		bd.listaCenarioExecucoes(agenda);
		int proximaExec = bd.getExecucaoAtual(agenda);

		StringBuffer retorno = new StringBuffer();

		for(int i=0; i< agenda.getExecucoesAgenda().size();i++){
			ExecucoesAgendaDTO exec = agenda.getExecucoesAgenda().get(i);
			GregorianCalendar dataExecucao = new GregorianCalendar();
			dataExecucao.setTime(exec.getDataExecucao());

			GregorianCalendar dataAgenda = new GregorianCalendar();
			dataAgenda.setTime(exec.getData());

			GregorianCalendar dataAtual = new GregorianCalendar();
			dataAtual.setTime(new Date());

			if(dataAgenda.get(GregorianCalendar.MONTH) > dataAtual.get(GregorianCalendar.MONTH))//como a lista vem ordenada pela data em que a agenda será executada, caso o mes dessa data for maior que a atual, posso desconsiderar
				break;

			else if(dataAgenda.get(GregorianCalendar.MONTH) == dataAtual.get(GregorianCalendar.MONTH)){
				retorno.append(formataDataAgenda(dataAgenda));
				if(exec.isExecutou()){
					retorno.append(" - Executado em "+ formataDataAgenda(dataExecucao));
				}else if(exec.getIdExecucao() == proximaExec && agenda.isProcessando()){
					double progresso = (agenda.getProgresso()*100);
					DecimalFormat format = new DecimalFormat();  
					format.setMaximumFractionDigits(2); 
					format.setMinimumFractionDigits(2); 
					retorno.append(" - Em andamento ("+format.format(progresso).replace(",", ".")+"%)");
				}

				retorno.append(",");
			}
		}

		if(retorno.length() > 0){
			if(retorno.lastIndexOf(",") == retorno.length()-1){
				retorno.deleteCharAt(retorno.lastIndexOf(","));
			}
		}

		return retorno.toString();

	}

	/** Formata a data retornada do banco para as execucoes no formato que será apresentado na interface.
	 * @param dataAgenda
	 * @return
	 */
	private String formataDataAgenda(GregorianCalendar dataAgenda) {

		int diaI = dataAgenda.get(GregorianCalendar.DAY_OF_MONTH);
		int mesI = dataAgenda.get(GregorianCalendar.MONTH)+1;
		int anoI = dataAgenda.get(GregorianCalendar.YEAR);
		int horaI = dataAgenda.get(GregorianCalendar.HOUR_OF_DAY);
		int minutoI = dataAgenda.get(GregorianCalendar.MINUTE);


		String dia = Integer.toString(diaI);
		String mes = Integer.toString(mesI);
		String ano = Integer.toString(anoI);
		String hora = Integer.toString(horaI);
		String minuto = Integer.toString(minutoI);


		String retorno = (String) (diaI < 10 ? "0"+dia : dia) + "/";
		retorno +=  (String) (mesI < 10 ? "0"+mes : mes) + "/";
		retorno +=  ano+" ";
		retorno +=  (String) (horaI < 10 ? "0"+hora : hora) + ":";
		retorno +=  (String) (minutoI < 10 ? "0"+minuto : minuto);

		return retorno;

	}

	/** Retorna os parâmetros de execução da agenda
	 * @param agenda
	 * @return
	 */
	private String getExecucao(AgendaDTO agenda) {
		// TODO Auto-generated method stub

		String retorno = "";

		if(agenda.getDiasDoMes() != null && agenda.getDiasDoMes().split(",").length > 1 ){
			//			retorno += "Mensal,";
			retorno += "Dias: "+agenda.getDiasDoMes().replace(",", "-");
			//			retorno += "Dias: "+getDiasMesExecutar(agenda);
		} else if(agenda.getDiasDaSemana()!= null && agenda.getDiasDaSemana().split(",").length > 0){
			retorno += "Semanal,";
			retorno += "Dias da Semana: "+agenda.getDiasDaSemana().replace(",", "-")+",";
			retorno += "Ocorrência: "+agenda.getSemanasDeOcorrencia().replace(",", "-")+",";
		} else{
			retorno += "&Uacutenica";
		}

		return retorno;


	}

	/** Retorna as futuras execuções da agenda mensal (dias do mês a executar), excluindo os dias anteriores a última execução
	 * @param agenda
	 * @return
	 */
	private String getDiasMesExecutar(Agenda agenda) {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(agenda.getUltimaExec());

		GregorianCalendar calAtual = new GregorianCalendar();
		calAtual.setTime(new Date());

		int dia = cal.get(GregorianCalendar.DAY_OF_MONTH);

		if(cal.get(GregorianCalendar.MONTH) < calAtual.get(GregorianCalendar.MONTH) || cal.get(GregorianCalendar.YEAR) < calAtual.get(GregorianCalendar.YEAR) || agenda.getUltimaExec() == null){//se a última execução for no mes anterior
			dia = 0;
		}

		String []dias = agenda.getDiasDoMes().split(",");

		StringBuffer retorno = new StringBuffer();

		for (int i = 0; i < dias.length; i++) {
			if(Integer.parseInt(dias[i]) > dia){
				retorno.append(dias[i]);
				retorno.append("-");
			}
		}

		if(retorno.length() > 1)
			retorno.deleteCharAt(retorno.lastIndexOf("-"));
		return retorno.toString();
	}

	/** Retorna as execucoes da série histórica.
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

	/** Retorna a data no formato DD/MM/YYYY
	 * @param data
	 * @return
	 */
	private String formataData(Date data) {
		// TODO Auto-generated method stub
		GregorianCalendar gc = new GregorianCalendar();

		gc.setTime(data);

		String retorno = "";

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

}
