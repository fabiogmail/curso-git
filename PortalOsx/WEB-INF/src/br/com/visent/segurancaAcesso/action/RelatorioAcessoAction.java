package br.com.visent.segurancaAcesso.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.segurancaAcesso.dao.RelatorioAcessoDAO;
import br.com.visent.segurancaAcesso.domain.RegistroAcesso;
import br.com.visent.segurancaAcesso.util.HibernateUtil;

public class RelatorioAcessoAction {

	public RelatorioAcessoAction() {
	}

	/**
	 * 
	 * 
	 * @return 
	 */
	public ArrayList listar(String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioAcessoDAO dao = new RelatorioAcessoDAO(session);

		ArrayList lista = (ArrayList) dao.listar(paramOrdem);

		session.close();
		return lista;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public RegistroAcesso buscarById(int id, String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioAcessoDAO dao = new RelatorioAcessoDAO(session);

		RegistroAcesso item = (RegistroAcesso) dao.buscarById(id, paramOrdem);

		session.close();
		return item;
	}

	/**
	 * 
	 * @param usuario
	 * @return
	 */
	public ArrayList buscarByUsuario(String usuario, String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioAcessoDAO dao = new RelatorioAcessoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarByUsuario(usuario, paramOrdem);

		session.close();
		return lista;
	}
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public ArrayList buscarByData(String dataInicio, String dataFim, String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioAcessoDAO dao = new RelatorioAcessoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarByData(dataInicio, dataFim, paramOrdem);

		session.close();
		return lista;
	}
	
	/**
	 * 
	 * @param usuario
	 * @param dataInicio
	 * @param dataFim
	 * @param paramOrdem
	 * @param group
	 * @return
	 */
	public ArrayList buscarByUserData(String usuario, String dataInicio, String dataFim, 
			String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioAcessoDAO dao = new RelatorioAcessoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarByUserData(usuario, dataInicio, dataFim, paramOrdem);

		session.close();
		return lista;
	}
	
}
