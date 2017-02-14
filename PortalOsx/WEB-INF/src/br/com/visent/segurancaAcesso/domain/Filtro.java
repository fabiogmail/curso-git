package br.com.visent.segurancaAcesso.domain;

public class Filtro {
	
	private int id;
	private String nomeFiltro;
	private String nomeGrupo;
	private RegistroUso registroUso;
	private String valoresFiltro;
	private int tipoFiltro;
	
	public Filtro(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public RegistroUso getRegistroUso() {
		return registroUso;
	}
	public void setRegistroUso(RegistroUso registroUso) {
		this.registroUso = registroUso;
	}
	
	public String getNomeFiltro() {
		return nomeFiltro;
	}
	public void setNomeFiltro(String nomeFiltro) {
		this.nomeFiltro = nomeFiltro;
	}
	public String getNomeGrupo() {
		return nomeGrupo;
	}
	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}
	public String getValoresFiltro() {
		return valoresFiltro;
	}
	public void setValoresFiltro(String valoresFiltro) {
		this.valoresFiltro = valoresFiltro;
	}

	/**
	 * @return Returns the tipoFiltro.
	 */
	public int getTipoFiltro()
	{
		return tipoFiltro;
	}

	/**
	 * @param tipoFiltro The tipoFiltro to set.
	 */
	public void setTipoFiltro(int tipoFiltro)
	{
		this.tipoFiltro = tipoFiltro;
	}	
}
