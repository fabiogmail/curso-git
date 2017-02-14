package Portal.Dto;

import java.io.Serializable;

public class RecursoVolumetria implements Serializable{
	
	private int idBilhetador;
	private int limMaximo;
	private int limMinimo;
	private Bilhetador bilhetador;
	
	
	public int getIdBilhetador() {
		return idBilhetador;
	}
	public void setIdBilhetador(int idBilhetador) {
		this.idBilhetador = idBilhetador;
	}
	public int getLimMaximo() {
		return limMaximo;
	}
	public void setLimMaximo(int limMaximo) {
		this.limMaximo = limMaximo;
	}
	public int getLimMinimo() {
		return limMinimo;
	}
	public void setLimMinimo(int limMinimo) {
		this.limMinimo = limMinimo;
	}
	public Bilhetador getBilhetador() {
		return bilhetador;
	}
	public void setBilhetador(Bilhetador bilhetador) {
		this.bilhetador = bilhetador;
	}

}
