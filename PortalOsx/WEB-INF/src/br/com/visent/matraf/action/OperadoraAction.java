package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.OperadoraDAO;
import br.com.visent.matraf.domain.Operadora;
import br.com.visent.matraf.util.HibernateUtil;

public class OperadoraAction {
	
	public OperadoraAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        OperadoraDAO dao = new OperadoraDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        //session.getTransaction().commit();
        session.close();
		return lista;
	}
	
	public Operadora buscarById(int id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        OperadoraDAO dao = new OperadoraDAO(session);
        
        Operadora item = (Operadora)dao.buscarById(id);

        //session.getTransaction().commit();
        session.close();
		return item;
	}

}
