package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Rota;

public class RotaDAO {

	private Session session;
	
	public RotaDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from Rota";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public Rota buscarById(int id) {
		Rota item = (Rota) session.load(Rota.class, new Integer(id));
		return item;
	}

	public Rota buscarByNome(String nome) {
		String sql = "from Rota as rt where rt.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		Rota item = (Rota)query.uniqueResult();
		return item;
	}

	public void salvar(Rota item) {
		session.saveOrUpdate((Rota)item);
	}

	public void deletar(Rota item) {
		session.delete((Rota)item);
	}

}
