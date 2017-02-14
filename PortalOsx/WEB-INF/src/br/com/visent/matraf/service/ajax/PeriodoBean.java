package br.com.visent.matraf.service.ajax;

import java.util.Date;


/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe para uso do AJAX
 * 
 */
public class PeriodoBean {
	
	private int id;
	private Date data;
	private Date horaInicial;
	private Date horaFinal;
	
	private String dataStr;
	private String horaInicialStr;
	private String horaFinalStr;
	private String intervalo;
	
	
	public String getIntervalo() {
		return intervalo;
	}
	public void setIntervalo(String intervalo) {
		this.intervalo = intervalo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getDataStr() {
		return dataStr;
	}
	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}
	public Date getHoraFinal() {
		return horaFinal;
	}
	public void setHoraFinal(Date horaFinal) {
		this.horaFinal = horaFinal;
	}
	public String getHoraFinalStr() {
		return horaFinalStr;
	}
	public void setHoraFinalStr(String horaFinalStr) {
		this.horaFinalStr = horaFinalStr;
	}
	public Date getHoraInicial() {
		return horaInicial;
	}
	public void setHoraInicial(Date horaInicial) {
		this.horaInicial = horaInicial;
	}
	public String getHoraInicialStr() {
		return horaInicialStr;
	}
	public void setHoraInicialStr(String horaInicialStr) {
		this.horaInicialStr = horaInicialStr;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	
	
}
