package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.PeriodoDAO;
import br.com.visent.matraf.domain.Periodo;
import br.com.visent.matraf.util.HibernateUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe action para controle da lista de periodos
 * 
 */
public class PeriodoAction {

	public PeriodoAction() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        PeriodoDAO dao = new PeriodoDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        session.close();
		return lista;
	}
	
	public Periodo buscarById(int id){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        PeriodoDAO dao = new PeriodoDAO(session);
        
        Periodo item = (Periodo)dao.buscarById(id);

        session.close();
		return item;
	}
	
}
