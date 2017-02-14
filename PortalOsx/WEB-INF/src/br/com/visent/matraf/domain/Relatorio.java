package br.com.visent.matraf.domain;

public class Relatorio {
	
	private int id;
	private String nome;
	private int tipo;
	private int tipoRel;
	
	public Relatorio(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getTipoRel() {
		return tipoRel;
	}

	public void setTipoRel(int tipoRel) {
		this.tipoRel = tipoRel;
	}
	
	

}
