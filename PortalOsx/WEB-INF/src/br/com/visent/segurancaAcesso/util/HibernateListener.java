package br.com.visent.segurancaAcesso.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;
/**
 * 
 * Visent Informática.
 * Projeto: PortalOsx
 *
 * @author Rafael
 * @version 1.0
 * @created 20/04/2007 11:53:15
 */
public class HibernateListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
    	try{
    		System.out.print("(Segurança de Acesso) Inicializando Factory do Hibernate...");
            HibernateUtil.getSessionFactory(); // inicializa o factory quando sobe o tomcat 
            System.out.println("OK");
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            session.close();
    	}catch (Exception e) {
			System.out.println("Erro nas configurações do Hibernate: "+e);
		}        
    }

    public void contextDestroyed(ServletContextEvent event) {
    	System.out.print("(Segurança de Acesso) Fechando Factory do Hibernate...");
        HibernateUtil.getSessionFactory().close(); // Libera o factory
        System.out.println("OK");
    }
}
