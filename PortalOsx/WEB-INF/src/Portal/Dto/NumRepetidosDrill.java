package Portal.Dto;

import java.io.Serializable;
import java.util.Date;

public class NumRepetidosDrill implements Serializable{
	
	private String bilhetador;
	private Date data;
	private int numerosRepetidos;
	private int qtdSeqNumerosRepetidos;
	private String tipo;
	
	public String getBilhetador() {
		return bilhetador;
	}
	public void setBilhetador(String bilhetador) {
		this.bilhetador = bilhetador;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getNumerosRepetidos() {
		return numerosRepetidos;
	}
	public void setNumerosRepetidos(int numerosRepetidos) {
		this.numerosRepetidos = numerosRepetidos;
	}
	public int getQtdSeqNumerosRepetidos() {
		return qtdSeqNumerosRepetidos;
	}
	public void setQtdSeqNumerosRepetidos(int qtdSeqNumerosRepetidos) {
		this.qtdSeqNumerosRepetidos = qtdSeqNumerosRepetidos;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
