package br.com.visent.matraf.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe domain para os Operadoras
 * 
 */
public class Operadora {
	
	private int id;
	private String nome;
	private String descricao;
	private List listaCentralRef = new ArrayList();
	
	public Operadora(){}
	
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List getListaCentralRef() {
		return listaCentralRef;
	}
	public void setListaCentralRef(List listaCentralRef) {
		this.listaCentralRef = listaCentralRef;
	}
	public String toString(){
		return nome;
	}

	
}
