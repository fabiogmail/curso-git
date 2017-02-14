package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;

import br.com.visent.matraf.action.CentralAction;

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
public class CentralManager {
	
	public ArrayList getLista(){
		CentralAction action = new CentralAction();
		ArrayList list = action.listar();
		return list;
	}
	
}
