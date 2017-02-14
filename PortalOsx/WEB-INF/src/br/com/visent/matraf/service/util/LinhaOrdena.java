package br.com.visent.matraf.service.util;

import java.util.ArrayList;

/**
 * Visent Telecomunica��es LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descri��o: classe usada para fazer a ordena��o das linhas
 * 
 */
public class LinhaOrdena implements Comparable {

	/** arraylist da linha */
	private ArrayList linha;
	/** fator da ordena��o */
	private Object fator;
	/** se o fator � um numero ou n�o */
	private boolean ehNumber;
	
	public LinhaOrdena(ArrayList linha, Object fator, boolean ehNumber){
		this.linha = linha;
		this.fator = fator;
		this.ehNumber = ehNumber;
	}
	
	public int compareTo(Object o) {
		LinhaOrdena ob = (LinhaOrdena)o;
		if(ehNumber){
			Float number = (Float)ob.getFator();
			return ((Float)fator).compareTo(number);
		}else{
			String string = (String)ob.getFator();
			return ((String)fator).compareTo(string);
		}
	}

	public Object getFator() {
		return fator;
	}

	public ArrayList getLinha() {
		return linha;
	}
	
	
	
	

}
