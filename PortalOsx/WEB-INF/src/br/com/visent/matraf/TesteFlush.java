package br.com.visent.matraf;

import java.util.ArrayList;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.exception.JDBCConnectionException;

import br.com.visent.matraf.util.HibernateUtil;

public class TesteFlush implements Runnable{
	
	Thread t; 
	
	public TesteFlush(){
		
		t = new Thread(this, "TesteFlush");
		t.start();
		
	}
	
	public void run() {
		while(true){
			Session session = HibernateUtil.getSessionFactory().openSession();
	    	
	    	try{
	    		
	    		session.beginTransaction();
		    	session.getTransaction().commit();
		    	
		    	String sql = "select distinct(data_periodo) as data from Periodo per";
				SQLQuery query = session.createSQLQuery(sql);
				query.addScalar("data", Hibernate.DATE);
				ArrayList lista = (ArrayList)query.list();
				for (int i = 0; i < lista.size(); i++) {
					System.out.print(lista.get(i)+";");
				}
				System.out.println("");
		    	
		        System.out.println("rodando...");
	    	}catch (TransactionException e) {
	    		e.printStackTrace();
	    		System.out.println("erro commit...");
	    		HibernateUtil.rebuild();
			}catch (JDBCConnectionException e){
				e.printStackTrace();
	    		System.out.println("erro na conexão...");
	    		HibernateUtil.rebuild();
			}
	    	
			session.close();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		TesteFlush teste = new TesteFlush();
	}
}
