package br.com.visent.segurancaAcesso.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.segurancaAcesso.dao.RelatorioUsoDAO;
import br.com.visent.segurancaAcesso.domain.RegistroUso;
import br.com.visent.segurancaAcesso.util.HibernateUtil;
/**
 * 
 * Visent Informática.
 * Projeto: PortalOsx
 *
 * @author Rafael
 * @version 1.0
 * @created 20/04/2007 11:59:52
 */
public class RelatorioUsoAction {

	public RelatorioUsoAction() {
	}

	/**
	 * 
	 * 
	 * @return 
	 */
	public ArrayList listar(String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioUsoDAO dao = new RelatorioUsoDAO(session);

		ArrayList lista = (ArrayList) dao.listar(paramOrdem);

		session.close();
		return lista;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public RegistroUso buscarById(int id, String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioUsoDAO dao = new RelatorioUsoDAO(session);

		RegistroUso item = (RegistroUso) dao.buscarById(id, paramOrdem);

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
		RelatorioUsoDAO dao = new RelatorioUsoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarByUsuario(usuario,paramOrdem);

		session.close();
		return lista;
	}
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public ArrayList buscarByData(String dataInicio,String dataFim, String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioUsoDAO dao = new RelatorioUsoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarByData(dataInicio, dataFim, paramOrdem);

		session.close();
		return lista;
	}
	
	/**
	 * 
	 * @param usuario
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public ArrayList buscarByUserData(String usuario, String dataInicio, String dataFim, String paramOrdem) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioUsoDAO dao = new RelatorioUsoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarByUserData(usuario, dataInicio, dataFim, paramOrdem);

		session.close();
		return lista;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ArrayList buscarListaDeFiltros(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		RelatorioUsoDAO dao = new RelatorioUsoDAO(session);

		ArrayList lista = (ArrayList) dao.buscarListaDeFiltros(id);

		session.close();
		return lista;
	}
}
