package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Celula;

public class CelulaDAO {

	private Session session;
	
	public CelulaDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from Celula";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public Celula buscarById(int id) {
		String sql = "from Celula as cel where cel.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		Celula item = (Celula)query.uniqueResult();
		return item;
	}

	public Celula buscarByNome(String nome) {
		String sql = "from Celula as cel where cel.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		Celula item = (Celula)query.uniqueResult();
		return item;
	}

	public void salvar(Celula item) {
		session.saveOrUpdate((Celula)item);
	}

	public void deletar(Celula item) {
		session.delete((Celula)item);
	}

}
