package br.com.visent.matraf.action;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.DataPeriodoDAO;
import br.com.visent.matraf.util.HibernateUtil;

public class DataPeriodoAction {

	public DataPeriodoAction(){}
	
	public ArrayList listar(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		DataPeriodoDAO dao = new DataPeriodoDAO(session);
        
        ArrayList lista = (ArrayList)dao.listar();

        session.close();
		return lista;
	}
	
	public ArrayList listarDatas(String central){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		DataPeriodoDAO dao = new DataPeriodoDAO(session);
        
        ArrayList lista = (ArrayList)dao.listarDatas(central);

        session.close();
		return lista;
	}
	
	public ArrayList listarPeriodos(Date data, String central){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
		DataPeriodoDAO dao = new DataPeriodoDAO(session);
        
        ArrayList lista = (ArrayList)dao.listarByData(data, central);

        session.close();
		return lista;
	}
	
}
