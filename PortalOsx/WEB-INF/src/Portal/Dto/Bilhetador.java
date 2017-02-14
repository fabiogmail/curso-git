package Portal.Dto;

public class Bilhetador {
	private int idBilhetador;
	private String nome;
	private String mascara;
	private String arqConfig;
	private int delay;
	private int dataHoraVolCham;
	
// Get & Set
	public int getIdBilhetador() {
		return idBilhetador;
	}
	public void setIdBilhetador(int idBilhetador) {
		this.idBilhetador = idBilhetador;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMascara() {
		return mascara;
	}
	public void setMascara(String mascara) {
		this.mascara = mascara;
	}
	public String getArqConfig() {
		return arqConfig;
	}
	public void setArqConfig(String arqConfig) {
		this.arqConfig = arqConfig;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public int getDataHoraVolCham() {
		return dataHoraVolCham;
	}
	public void setDataHoraVolCham(int dataHoraVolCham) {
		this.dataHoraVolCham = dataHoraVolCham;
	}
	
	
	
	
}
