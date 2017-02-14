package Portal.Dto;

import java.io.Serializable;
import java.util.Date;

public class NumRepetidos implements Serializable{
	private int id;
	private int idBilhetador;
	private Date data;
	private int numerosRepetidos;
	private int qtdSeqNumerosRepetidos;
	
	
	public int getIdBilhetador() {
		return idBilhetador;
	}
	public void setIdBilhetador(int idBilhetador) {
		this.idBilhetador = idBilhetador;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
