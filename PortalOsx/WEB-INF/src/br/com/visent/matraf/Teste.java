package br.com.visent.matraf;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.DataPeriodoDAO;
import br.com.visent.matraf.util.HibernateUtil;


public class Teste {

    public static void main(String[] args) {
    	Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
    	
		DataPeriodoDAO dao = new DataPeriodoDAO(session);
		
		session.close();
    	
    	String x = "13/45/4565";
    	String y = x.replace("/","");
    	System.out.println(y);
    }    

}
