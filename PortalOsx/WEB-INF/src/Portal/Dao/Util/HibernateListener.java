
package Portal.Dao.Util;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;

import Portal.Service.VolumetriaService;
/**
 * 
 * Visent Informática.
 * Projeto: PortalOsx
 *
 * @author Bruno
 * @version 1.0
 * @created 28/05/2010 11:53:00
 */
public class HibernateListener implements ServletContextListener {

	
    public void contextInitialized(ServletContextEvent event) {
    	try{
    		
    		System.out.print("(Garantia de Receita) Inicializando Factory do Hibernate...");
            HibernateUtil.inicializa(event.getServletContext().getRealPath("WEB-INF/classes/hibernateReceita.cfg.xml")); // inicializa o factory quando sobe o tomcat
            System.out.println("OK");
            Session session = HibernateUtil.getSessionFactory().openSession();
//            String pathRels = event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
//            String pathRels = event.getServletContext().getRealPath("reports/");
//            pathRels+= "/Volumetria.jasper";
//            VolumetriaService volumetriaService = new VolumetriaService(session);
//            String relVolumetria = volumetriaService.getRelatorioAcesso( 0, "", true,pathRels);
//            System.out.println(relVolumetria);
            session.close();
            
            System.out.println();
    	}catch (Exception e) {
			System.out.println("Erro nas configurações do Hibernate: "+e);
		}        
    }

    public void contextDestroyed(ServletContextEvent event) {
    	System.out.print("(Garantia de Receita) Fechando Factory do Hibernate...");
        HibernateUtil.getSessionFactory().close(); // Libera o factory
        System.out.println("OK");
    }
}
