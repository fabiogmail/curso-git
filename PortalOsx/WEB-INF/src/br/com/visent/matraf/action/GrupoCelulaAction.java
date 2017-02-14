package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.GrupoCelulaDAO;
import br.com.visent.matraf.domain.GrupoCelula;
import br.com.visent.matraf.util.HibernateUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe action para controle da lista de Bsc's
 * 
 */
public class GrupoCelulaAction {
	
	public GrupoCelulaAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        GrupoCelulaDAO dao = new GrupoCelulaDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        session.close();
		return lista;
	}
	
	public GrupoCelula buscarById(int id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        GrupoCelulaDAO dao = new GrupoCelulaDAO(session);
        
        GrupoCelula item = (GrupoCelula)dao.buscarById(id);

        //session.getTransaction().commit();
        session.close();
		return item;
	}
	
}
