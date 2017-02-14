package Portal.Dto;

public class RelatorioVolumetria {
	
	private Integer id;
	private Double semanaUm;
	private Double semanaDois;
	private Double semanaTres;
	private Double semanaQuatro;
	private Double valorAtual;
	private Integer dataUltimoProc;
	private String bilhetador;
	private String dia;
	//private Double mediana;
//Get & Set	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getSemanaUm() {
		return semanaUm;
	}
	public void setSemanaUm(Double semanaUm) {
		this.semanaUm = semanaUm;
	}
	public Double getSemanaDois() {
		return semanaDois;
	}
	public void setSemanaDois(Double semanaDois) {
		this.semanaDois = semanaDois;
	}
	public Double getSemanaTres() {
		return semanaTres;
	}
	public void setSemanaTres(Double semanaTres) {
		this.semanaTres = semanaTres;
	}
	public Double getSemanaQuatro() {
		return semanaQuatro;
	}
	public void setSemanaQuatro(Double semanaQuatro) {
		this.semanaQuatro = semanaQuatro;
	}
	public Double getValorAtual() {
		return valorAtual;
	}
	public void setValorAtual(Double valorAtual) {
		this.valorAtual = valorAtual;
	}
	
	public String getBilhetador() {
		return bilhetador;
	}
	public void setBilhetador(String bilhetador) {
		this.bilhetador = bilhetador;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public Integer getDataUltimoProc() {
		return dataUltimoProc;
	}
	public void setDataUltimoProc(Integer dataUltimoProc) {
		this.dataUltimoProc = dataUltimoProc;
	}
	
	
	
	
	
}
