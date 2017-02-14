package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;

import br.com.visent.matraf.action.OperadoraAction;

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
public class OperadoraManager {
	
	public ArrayList getLista(){
		OperadoraAction action = new OperadoraAction();
		ArrayList list = action.listar();
		return list;
	}

}
