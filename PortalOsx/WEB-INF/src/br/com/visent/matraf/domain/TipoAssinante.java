package br.com.visent.matraf.domain;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe domain para os Tipo de Assinante
 * 
 */
public class TipoAssinante {
	
	private int id;
	private String nome;
	
	public TipoAssinante(){}
	
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
	
	public String toString(){
		return nome;
	}

}
