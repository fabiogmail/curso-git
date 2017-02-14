package br.com.visent.matraf.domain;

/**
 * Visent Telecomunica��es LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descri��o: classe domain para as Rela��es de Trafego
 * 
 */
public class RelacaoTrafego {
	
	private int id;
	private String origem;
	private String destino;
	private String nome;
	
	public RelacaoTrafego(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String toString(){
		return id + ": " + origem + " - " + destino+": "+nome;
	}
	

}
