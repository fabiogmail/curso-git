package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;

import br.com.visent.matraf.action.CelulaAction;

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
public class CelulaManager { 
	
	public ArrayList getLista(){
		CelulaAction action = new CelulaAction();
		ArrayList list = action.listar();
		return list;
	}

}
