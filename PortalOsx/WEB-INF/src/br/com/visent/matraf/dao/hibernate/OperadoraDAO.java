package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Operadora;

public class OperadoraDAO {

	private Session session;
	
	public OperadoraDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from Operadora";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public Operadora buscarById(int id) {
		String sql = "from Operadora as op where op.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		Operadora item = (Operadora)query.uniqueResult();
		return item;
	}

	public Operadora buscarByNome(String nome) {
		String sql = "from Operadora as op where op.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		Operadora item = (Operadora)query.uniqueResult();
		return item;
	}

	public void salvar(Operadora item) {
		session.saveOrUpdate((Operadora)item);
	}

	public void deletar(Operadora item) {
		session.delete((Operadora)item);
	}

}
