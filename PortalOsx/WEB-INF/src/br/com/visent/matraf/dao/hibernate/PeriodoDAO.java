package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Periodo;

public class PeriodoDAO {
	
private Session session;
	
	public PeriodoDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from Periodo";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public Periodo buscarById(int id) {
		String sql = "from Periodo as per where per.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		Periodo item = (Periodo)query.uniqueResult();
		return item;
	}

}
