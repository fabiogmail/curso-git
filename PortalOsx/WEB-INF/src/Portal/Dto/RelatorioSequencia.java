package Portal.Dto;

import java.util.Date;

public class RelatorioSequencia {
	
	//private Integer id;
	private String bilhetador;
	//private Date data;
	private Integer numFaltantes;
	private Integer numRepetidos;
	private Integer ultimoArquivo;
	private Integer primeiroArquivo;
	private Integer qntArquivos;
	private Integer qtdNumRepetidos;
	private String tipo;
	
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public String getBilhetador() {
		return bilhetador;
	}
	public void setBilhetador(String bilhetador) {
		this.bilhetador = bilhetador;
	}
//	public Date getData() {
//		return data;
//	}
//	public void setData(Date data) {
//		this.data = data;
//	}
	public Integer getNumFaltantes() {
		if(numFaltantes == null)
			return new Integer(0);
		
		return numFaltantes;
	}
	public void setNumFaltantes(Integer numFaltantes) {
		this.numFaltantes = numFaltantes;
	}
	public Integer getNumRepetidos() {
		if(numRepetidos == null)
			return new Integer(0);
		return numRepetidos;
		
	}
	public void setNumRepetidos(Integer numRepetidos) {
		this.numRepetidos = numRepetidos;
	}
	public Integer getUltimoArquivo() {
		if(ultimoArquivo == null)
			return new Integer(0);
		return ultimoArquivo;
	}
	public void setUltimoArquivo(Integer ultimoArquivo) {
		this.ultimoArquivo = ultimoArquivo;
	}
	public Integer getPrimeiroArquivo() {
		if(primeiroArquivo == null)
			return new Integer(0);
		return primeiroArquivo;
	}
	public void setPrimeiroArquivo(Integer primeiroArquivo) {
		this.primeiroArquivo = primeiroArquivo;
	}
	public Integer getQntArquivos() {
		if(qntArquivos == null)
			return new Integer(0);
		return qntArquivos;
	}
	public void setQntArquivos(Integer qntArquivos) {
		this.qntArquivos = qntArquivos;
	}
	public Integer getQtdNumRepetidos() {
		if(qtdNumRepetidos == null)
			return new Integer(0);
		return qtdNumRepetidos;
	}
	public void setQtdNumRepetidos(Integer qtdNumRepetidos) {
		this.qtdNumRepetidos = qtdNumRepetidos;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

}
