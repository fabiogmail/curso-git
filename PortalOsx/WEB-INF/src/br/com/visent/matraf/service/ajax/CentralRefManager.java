package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;

import br.com.visent.matraf.action.CentralRefAction;
import br.com.visent.matraf.domain.CentralRef;

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
public class CentralRefManager {
	
	public ArrayList getLista(){
		CentralRefAction action = new CentralRefAction();
		ArrayList lista = new ArrayList();
		lista.addAll(action.listar());
		return lista;
	}
	
	public CentralRef getCentralRef(int id){
		CentralRefAction centralRefAction = new CentralRefAction();
		CentralRef centralref = centralRefAction.buscarById(id);
		return centralref;
	}
	
}
