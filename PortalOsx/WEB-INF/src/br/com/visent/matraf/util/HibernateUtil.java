package br.com.visent.matraf.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: Classe que controla a sessionFactory do hibernate
 * 
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	// iniciando a sessionFactory
	static {
		try {
			// Criando o session factory a partir do hibernate.cfg.xml
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Erro ao iniciar a SessionFactory." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
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
