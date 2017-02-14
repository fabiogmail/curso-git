package Portal.Dto;

import java.io.Serializable;
import java.util.Date;

public class NumFaltantes implements Serializable{
	private int id;
	private int idBilhetador;
	private Date data;
	private int numerosFaltantes;
	
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
	public int getNumerosFaltantes() {
		return numerosFaltantes;
	}
	public void setNumerosFaltantes(int numerosFaltantes) {
		this.numerosFaltantes = numerosFaltantes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
