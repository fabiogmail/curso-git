package br.com.visent.segurancaAcesso.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.segurancaAcesso.domain.Config;

/**
 * Visent Inform�tica. Projeto: PortalOsx
 * 
 * @author Carlos Feij�o
 * @version 1.0
 * @created 30/10/2007 14:46:33
 */
public class ConfigDAO {

	private Session session;

	public ConfigDAO(Session session) {
		this.session = session;
	}

	/**
	 * Buscar lista de configura��es
	 * @return lista
	 */
	public ArrayList listar() {
		String sql = "from Config conf ";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList) query.list();
		return lista;
	}
	
	/**
	 * Buscar lista de configura��es por nome
	 * @param nome
	 * @return Config
	 */
	public Config buscarPorNome(String nome){
		String sql = "from Config as conf where conf.nome=? ";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		Config config = (Config) query.uniqueResult();
		return config;
	}
	
	/**
	 * Salvar configura��o
	 * @param config
	 */
	public void salvar(Config config){
		session.saveOrUpdate(config);
	}

}
