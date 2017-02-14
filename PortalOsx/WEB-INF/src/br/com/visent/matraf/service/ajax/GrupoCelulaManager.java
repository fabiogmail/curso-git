package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;

import br.com.visent.matraf.action.GrupoCelulaAction;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe para uso do AJAX
 * 
 */
public class GrupoCelulaManager {
	
	public ArrayList getLista(){
		GrupoCelulaAction action = new GrupoCelulaAction();
		ArrayList list = action.listar();
		return list;
	}

}
