package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Central;

public class CentralDAO {

	private Session session;
	
	public CentralDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from Central";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public Central buscarById(int id) {
		String sql = "from Central as central where central.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		Central item = (Central)query.uniqueResult();
		return item;
	}

	public Central buscarByNome(String nome) {
		String sql = "from Central as central where central.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		Central item = (Central)query.uniqueResult();
		return item;
	}

	public void salvar(Central item) {
		session.saveOrUpdate((Central)item);
	}

	public void deletar(Central item) {
		session.delete((Central)item);
	}

}
