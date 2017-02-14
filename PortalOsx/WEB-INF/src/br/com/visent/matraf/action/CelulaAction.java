package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.CelulaDAO;
import br.com.visent.matraf.domain.Celula;
import br.com.visent.matraf.util.HibernateUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe action para controle da lista de celulas
 * 
 */
public class CelulaAction {
	
	public CelulaAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        CelulaDAO dao = new CelulaDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        session.close();
		return lista;
	}
	
	public Celula buscarById(int id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        CelulaDAO dao = new CelulaDAO(session);
        
        Celula item = (Celula)dao.buscarById(id);

        //session.getTransaction().commit();
        session.close();
		return item;
	}

}
