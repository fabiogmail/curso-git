package br.com.visent.matraf.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataPeriodo implements Serializable {
	
	private int id;
	private Relatorio relatorio;
	private Periodo periodo;
	private Date dataTabela;
	private List listaCentral = new ArrayList();
	
	public DataPeriodo(){}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getDataTabela() {
		return dataTabela;
	}

	public void setDataTabela(Date dataTabela) {
		this.dataTabela = dataTabela;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public Relatorio getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(Relatorio relatorio) {
		this.relatorio = relatorio;
	}
	
	public List getListaCentral() {
		return listaCentral;
	}
	
	public void setListaCentral(List listaCentral) {
		this.listaCentral = listaCentral;
	}
	
	

}
