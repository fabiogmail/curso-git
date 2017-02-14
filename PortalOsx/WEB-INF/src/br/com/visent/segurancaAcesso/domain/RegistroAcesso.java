package br.com.visent.segurancaAcesso.domain;

import java.util.Date;

public class RegistroAcesso {

	private int id;
	private String usuario;
	private String perfil; 
	private String ipMaquina;
	private String modulo;
	private String evento;
	private Date dtRegistro;
	private String motivo;
	private int tempoAcesso;
	
	public RegistroAcesso(){}
	
	public String getEvento() {
		return evento;
	}
	public void setEvento(String evento) {
		this.evento = evento;
	}
	public String getIpMaquina() {
		return ipMaquina;
	}
	public void setIpMaquina(String ipMaquina) {
		this.ipMaquina = ipMaquina;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDtRegistro() {
		return dtRegistro;
	}
	public void setDtRegistro(Date dtRegistro) {
		this.dtRegistro = dtRegistro;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public int getTempoAcesso() {
		return tempoAcesso;
	}
	public void setTempoAcesso(int tempoAcesso) {
		this.tempoAcesso = tempoAcesso;
	}
	
	
	
}
