package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.TipoAssinante;

public class TipoAssinanteDAO {

	private Session session;
	
	public TipoAssinanteDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from TipoAssinante";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public TipoAssinante buscarById(int id) {
		TipoAssinante item = (TipoAssinante) session.load(TipoAssinante.class, new Integer(id));
		return item;
	}

	public TipoAssinante buscarByNome(String nome) {
		String sql = "from Rota as rt where rt.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		TipoAssinante item = (TipoAssinante)query.uniqueResult();
		return item;
	}

	public void salvar(TipoAssinante item) {
		session.saveOrUpdate((TipoAssinante)item);
	}

	public void deletar(TipoAssinante item) {
		session.delete((TipoAssinante)item);
	}

}
