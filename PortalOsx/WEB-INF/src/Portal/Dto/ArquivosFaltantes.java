package Portal.Dto;

import java.io.Serializable;
import java.util.Date;

public class ArquivosFaltantes implements Serializable {
	private int idBilhetador;
	private Date data;
	private int idPrimeiroArquivo;
	private int idUltimoArquivo;
	private int qntArquivos;
	
	
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
	public int getIdPrimeiroArquivo() {
		return idPrimeiroArquivo;
	}
	public void setIdPrimeiroArquivo(int idPrimeiroArquivo) {
		this.idPrimeiroArquivo = idPrimeiroArquivo;
	}
	public int getIdUltimoArquivo() {
		return idUltimoArquivo;
	}
	public void setIdUltimoArquivo(int idUltimoArquivo) {
		this.idUltimoArquivo = idUltimoArquivo;
	}
	public int getQntArquivos() {
		return qntArquivos;
	}
	public void setQntArquivos(int qntArquivos) {
		this.qntArquivos = qntArquivos;
	}
	

}
