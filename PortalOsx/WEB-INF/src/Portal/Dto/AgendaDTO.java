package Portal.Dto;

import java.sql.Timestamp;
import java.util.ArrayList;

import agenda.beans.dominio.Agenda;
import agenda.beans.dominio.RelatoriosCfg;
import agenda.beans.dominio.Agenda.PERIODICIDADE;
import agenda.beans.dominio.Agenda.STATUS;

public class AgendaDTO {
	private String nome;
	
	private boolean eh_historico; 
	
	private Integer idAgenda;
	private PERIODICIDADE periodicidade;
	private STATUS status;
	private Timestamp executadoEm;
	private Timestamp ultimaExec;

	private float progresso;

	/**
	 * 0 - Historico normal
	 * 1 - Historico infinito
	 * 2 - Agenda com todos os meses selecionado
	 * 3 - Agenda com todos os anos selecionado
	 * 4 - Agenda com todos os meses e todos os anos 
	 */
	private short infinito;

	private Timestamp inicio;

	private Timestamp termino;
	
	private Timestamp horaInicio;
	
	private Timestamp horaTermino;

	private int latencia;

	private int periodo;

	private String diasDoMes;
	private String semanasDeOcorrencia;

	private String diasDaSemana;

	private RelatoriosCfg relatoriosCfg;
	
	private int reprocessarUltimasHoras=0;
	
	private short exportacao = 0;
	
	private String usuario;
	
	private int privacidade=0;
	
	private int periodoReprocessarUltimasHoras=0;
	
	private long diferencaHoraInicioFim=0;
	
	private boolean nextScheduler;
	
	private boolean isProcessando = false;

	private ArrayList<ExecucoesAgendaDTO> execucoesAgenda;
	
	public AgendaDTO(){
		setExecucoesAgenda(new ArrayList<ExecucoesAgendaDTO>());
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isEh_historico() {
		return eh_historico;
	}

	public void setEh_historico(boolean eh_historico) {
		this.eh_historico = eh_historico;
	}

	public Integer getIdAgenda() {
		return idAgenda;
	}

	public void setIdAgenda(Integer idAgenda) {
		this.idAgenda = idAgenda;
	}

	public PERIODICIDADE getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(PERIODICIDADE periodicidade) {
		this.periodicidade = periodicidade;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public Timestamp getExecutadoEm() {
		return executadoEm;
	}

	public void setExecutadoEm(Timestamp executadoEm) {
		this.executadoEm = executadoEm;
	}

	public Timestamp getUltimaExec() {
		return ultimaExec;
	}

	public void setUltimaExec(Timestamp ultimaExec) {
		this.ultimaExec = ultimaExec;
	}

	public float getProgresso() {
		return progresso;
	}

	public void setProgresso(float progresso) {
		this.progresso = progresso;
	}

	public short getInfinito() {
		return infinito;
	}

	public void setInfinito(short infinito) {
		this.infinito = infinito;
	}

	public Timestamp getInicio() {
		return inicio;
	}

	public void setInicio(Timestamp inicio) {
		this.inicio = inicio;
	}

	public Timestamp getTermino() {
		return termino;
	}

	public void setTermino(Timestamp termino) {
		this.termino = termino;
	}

	public Timestamp getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Timestamp horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Timestamp getHoraTermino() {
		return horaTermino;
	}

	public void setHoraTermino(Timestamp horaTermino) {
		this.horaTermino = horaTermino;
	}

	public int getLatencia() {
		return latencia;
	}

	public void setLatencia(int latencia) {
		this.latencia = latencia;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public String getDiasDoMes() {
		return diasDoMes;
	}

	public void setDiasDoMes(String diasDoMes) {
		this.diasDoMes = diasDoMes;
	}

	public String getSemanasDeOcorrencia() {
		return semanasDeOcorrencia;
	}

	public void setSemanasDeOcorrencia(String semanasDeOcorrencia) {
		this.semanasDeOcorrencia = semanasDeOcorrencia;
	}

	public String getDiasDaSemana() {
		return diasDaSemana;
	}

	public void setDiasDaSemana(String diasDaSemana) {
		this.diasDaSemana = diasDaSemana;
	}

	public RelatoriosCfg getRelatoriosCfg() {
		return relatoriosCfg;
	}

	public void setRelatoriosCfg(RelatoriosCfg relatoriosCfg) {
		this.relatoriosCfg = relatoriosCfg;
	}

	public int getReprocessarUltimasHoras() {
		return reprocessarUltimasHoras;
	}

	public void setReprocessarUltimasHoras(int reprocessarUltimasHoras) {
		this.reprocessarUltimasHoras = reprocessarUltimasHoras;
	}

	public short getExportacao() {
		return exportacao;
	}

	public void setExportacao(short exportacao) {
		this.exportacao = exportacao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getPrivacidade() {
		return privacidade;
	}

	public void setPrivacidade(int privacidade) {
		this.privacidade = privacidade;
	}

	public int getPeriodoReprocessarUltimasHoras() {
		return periodoReprocessarUltimasHoras;
	}

	public void setPeriodoReprocessarUltimasHoras(int periodoReprocessarUltimasHoras) {
		this.periodoReprocessarUltimasHoras = periodoReprocessarUltimasHoras;
	}

	public long getDiferencaHoraInicioFim() {
		return diferencaHoraInicioFim;
	}

	public void setDiferencaHoraInicioFim(long diferencaHoraInicioFim) {
		this.diferencaHoraInicioFim = diferencaHoraInicioFim;
	}

	public boolean isNextScheduler() {
		return nextScheduler;
	}

	public void setNextScheduler(boolean nextScheduler) {
		this.nextScheduler = nextScheduler;
	}

	public boolean isProcessando() {
		return isProcessando;
	}

	public void setProcessando(boolean isProcessando) {
		this.isProcessando = isProcessando;
	}

	public ArrayList<ExecucoesAgendaDTO> getExecucoesAgenda() {
		return execucoesAgenda;
	}

	public void setExecucoesAgenda(ArrayList<ExecucoesAgendaDTO> execucoesAgenda) {
		this.execucoesAgenda = execucoesAgenda;
	}

	

}
