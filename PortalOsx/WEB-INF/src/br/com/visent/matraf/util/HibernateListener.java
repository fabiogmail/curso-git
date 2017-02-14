package br.com.visent.matraf.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: Classe que inicializa e fecha o factory
 * 
 */
public class HibernateListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
    	System.out.print("(Matriz) Inicializando Factory do Hibernate...");
        HibernateUtil.getSessionFactory(); // inicializa o factory quando sobe o tomcat
        System.out.println("OK");
    }

    public void contextDestroyed(ServletContextEvent event) {
    	System.out.println("Fechando Factory do Hibernate");
        HibernateUtil.getSessionFactory().close(); // Libera o factory
    }
}
