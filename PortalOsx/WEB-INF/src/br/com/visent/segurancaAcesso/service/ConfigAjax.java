package br.com.visent.segurancaAcesso.service;

import br.com.visent.segurancaAcesso.action.ConfigAction;
import br.com.visent.segurancaAcesso.domain.Config;

/**
 * Classe service para manter as configurações do segurança
 * Visent Informática.
 * Projeto: PortalOsx
 * 
 * @author Carlos Feijão
 * @version 1.0 
 * @created 30/10/2007 17:11:56
 */
public class ConfigAjax {	
	
	public Config buscarConf(String nome){
		ConfigAction action = new ConfigAction();
		Config config = action.buscarPorNome(nome);
		return config;
	}
		
	public void salvar(String[] nome, String[] valor){
		ConfigAction action = new ConfigAction();
		for (int i = 0; i < nome.length; i++) {
			Config config = action.buscarPorNome(nome[i]);
			if (config == null) {
				config = new Config();
				config.setNome(nome[i]);
				config.setValor(valor[i]);
			}else{
				config.setValor(valor[i]);
			}
			action.salvar(config);
		}
		
				
	}
	 

}
