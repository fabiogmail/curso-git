package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.CentralRef;

public class CentralRefDAO {

	private Session session;
	
	public CentralRefDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from CentralRef";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public CentralRef buscarById(int id) {
		String sql = "from CentralRef as centralref where centralref.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		CentralRef item = (CentralRef)query.uniqueResult();
		return item;
	}

	public CentralRef buscarByNome(String nome) {
		String sql = "from CentralRef as central where centralref.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		CentralRef item = (CentralRef)query.uniqueResult();
		return item;
	}

	public void salvar(CentralRef item) {
		session.saveOrUpdate((CentralRef)item);
	}

	public void deletar(CentralRef item) {
		session.delete((CentralRef)item);
	}

}
