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
 * Breve Descrição: classe domain para as centrais de referência
 * 
 */
public class CentralRef {
	
	private int id;
	private String nome;
	private int tipoTec;
	private List grupoCelulas = new ArrayList();
	private List centrais = new ArrayList();
	private List operadoras = new ArrayList();
	
	public CentralRef(){}

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
	public int getTipoTec() {
		return tipoTec;
	}
	public void setTipoTec(int tipoTec) {
		this.tipoTec = tipoTec;
	}
	public List getCentrais() {
		return centrais;
	}
	public void setCentrais(List centrais) {
		this.centrais = centrais;
	}
	public List getGrupoCelulas() {
		return grupoCelulas;
	}
	public void setGrupoCelulas(List grupoCelulas) {
		this.grupoCelulas = grupoCelulas;
	}
	public List getOperadoras() {
		return operadoras;
	}
	public void setOperadoras(List operadoras) {
		this.operadoras = operadoras;
	}
	public String toString(){
		return nome;
	}
	
	
	
	
	

}
