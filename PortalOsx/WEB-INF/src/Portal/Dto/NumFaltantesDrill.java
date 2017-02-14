package Portal.Dto;

import java.io.Serializable;
import java.util.Date;

public class NumFaltantesDrill implements Serializable{
	
	private String bilhetador;
	private Date data;
	private int numerosFaltantes;
	private int qtdSeqNumerosFaltantes;
	private String tipo;
	
	
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getNumerosFaltantes() {
		return numerosFaltantes;
	}
	public void setNumerosFaltantes(int numerosFaltantes) {
		this.numerosFaltantes = numerosFaltantes;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getBilhetador() {
		return bilhetador;
	}
	public void setBilhetador(String bilhetador) {
		this.bilhetador = bilhetador;
	}
	public int getQtdSeqNumerosFaltantes() {
		return qtdSeqNumerosFaltantes;
	}
	public void setQtdSeqNumerosFaltantes(int qtdSeqNumerosFaltantes) {
		this.qtdSeqNumerosFaltantes = qtdSeqNumerosFaltantes;
	}
	

}
