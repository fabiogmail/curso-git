package br.com.visent.segurancaAcesso.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;

/**
 * 
 * Visent Inform�tica.
 * Projeto: PortalOsx
 *
 * @author Rafael
 * @version 1.0
 * @created 20/04/2007 11:53:00
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	// iniciando a sessionFactory
	static {
		try {
			// Criando o session factory a partir do hibernate.cfg.xml
			sessionFactory = new Configuration().configure("hibernateSeguranca.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Erro ao iniciar a SessionFactory." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		//sessionFactory.evictQueries();
		return sessionFactory;
	}
	
	public static void rebuild(){
		sessionFactory.close();
	}
	
	public static void refresh(Session session){
		try{
			session.beginTransaction();
			session.getTransaction().commit();
		}catch (TransactionException e) {
			System.out.println("Falha na transa��o");
			rebuild();
		}catch (JDBCConnectionException e){
    		System.out.println("Falhou conex�o com o banco");
    		rebuild();
		}
	}

}
