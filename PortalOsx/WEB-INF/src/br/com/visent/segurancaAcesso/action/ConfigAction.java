package br.com.visent.segurancaAcesso.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.segurancaAcesso.dao.ConfigDAO;
import br.com.visent.segurancaAcesso.domain.Config;
import br.com.visent.segurancaAcesso.util.HibernateUtil;

public class ConfigAction {
	
	/**
	 * Buscar lista de configurações
	 * @return lista
	 */
	public ArrayList listar() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		ConfigDAO dao = new ConfigDAO(session);
		ArrayList lista = (ArrayList) dao.listar();
		session.close();
		return lista;
	}
	
	/**
	 * Buscar lista de configurações por nome
	 * @param nome
	 * @return Config
	 */
	public Config buscarPorNome(String nome){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		ConfigDAO dao = new ConfigDAO(session);
		Config config = (Config) dao.buscarPorNome(nome);
		session.close();
		return config;
	}
	
	/**
	 * Salvar configuração
	 * @param config
	 */
	public void salvar(Config config){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        ConfigDAO dao = new ConfigDAO(session);
        dao.salvar(config);
        session.getTransaction().commit();
        session.close();
	}

}
