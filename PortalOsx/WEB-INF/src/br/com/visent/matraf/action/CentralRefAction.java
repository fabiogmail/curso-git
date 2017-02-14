package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.CentralRefDAO;
import br.com.visent.matraf.domain.CentralRef;
import br.com.visent.matraf.util.HibernateUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe action para controle da lista de centrais de referência
 * 
 */
public class CentralRefAction {
	
	public CentralRefAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        CentralRefDAO dao = new CentralRefDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        //session.getTransaction().commit();
        session.close();
		return lista;
	}
	
	public CentralRef buscarById(int id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        CentralRefDAO dao = new CentralRefDAO(session);
        
        CentralRef item = (CentralRef)dao.buscarById(id);

        //session.getTransaction().commit();
        session.close();
		return item;
	}

}
