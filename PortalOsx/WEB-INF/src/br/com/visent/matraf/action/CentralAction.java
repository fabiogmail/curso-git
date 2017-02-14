package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.CentralDAO;
import br.com.visent.matraf.domain.Central;
import br.com.visent.matraf.util.HibernateUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe action para controle da lista de centrais
 * 
 */
public class CentralAction {
	
	public CentralAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        CentralDAO dao = new CentralDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        //session.getTransaction().commit();
        session.close();
		return lista;
	}
	
	public Central buscarById(int id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        CentralDAO dao = new CentralDAO(session);
        
        Central item = (Central)dao.buscarById(id);

        //session.getTransaction().commit();
        session.close();
		return item;
	}

}
