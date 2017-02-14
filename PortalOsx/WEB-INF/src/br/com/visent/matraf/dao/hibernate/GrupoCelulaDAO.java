package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.GrupoCelula;

public class GrupoCelulaDAO {

	private Session session;
	
	public GrupoCelulaDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from GrupoCelula";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public GrupoCelula buscarById(int id) {
		String sql = "from GrupoCelula as GrupoCelula where GrupoCelula.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		GrupoCelula item = (GrupoCelula)query.uniqueResult();
		return item;
	}

	public GrupoCelula buscarByNome(String nome) {
		String sql = "from GrupoCelula as GrupoCelula where GrupoCelula.nome=?";
		Query query = session.createQuery(sql);
		query.setString(0,nome);
		GrupoCelula item = (GrupoCelula)query.uniqueResult();
		return item;
	}

	public void salvar(GrupoCelula item) {
		session.saveOrUpdate((GrupoCelula)item);
	}

	public void deletar(GrupoCelula item) {
		session.delete((GrupoCelula)item);
	}

}
