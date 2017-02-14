package Portal.Dao.Util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;

/**
 * 
 * Visent Informática.
 * Projeto: PortalOsx
 *
 * @author Bruno
 * @version 1.0
 * @created 28/05/2010 11:53:00
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Configuration configuration;

	// iniciando a sessionFactory
	public static void inicializa(String arquivo){
		try {			
			File xml = new File(arquivo);
			sessionFactory = new Configuration().configure("hibernateReceita.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			ex.printStackTrace();
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
			System.out.println("Falha na transação");
			rebuild();
		}catch (JDBCConnectionException e){
    		System.out.println("Falhou conexão com o banco");
    		rebuild();
		}
	}

}
