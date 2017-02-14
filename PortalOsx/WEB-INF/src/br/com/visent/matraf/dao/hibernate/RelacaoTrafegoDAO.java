package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.matraf.domain.RelacaoTrafego;

public class RelacaoTrafegoDAO {

	private Session session;
	
	public RelacaoTrafegoDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from RelacaoTrafego";
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public RelacaoTrafego buscarById(int id) {
		String sql = "from RelacaoTrafego as rt where rt.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		RelacaoTrafego item = (RelacaoTrafego)query.uniqueResult();
		return item;
	}

	public ArrayList buscarByOrigem(String origem) {
		String sql = "from RelacaoTrafego as rt where rt.origem=?";
		Query query = session.createQuery(sql);
		query.setString(0,origem);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList buscarByDestino(String destino) {
		String sql = "from RelacaoTrafego as rt where rt.destino=?";
		Query query = session.createQuery(sql);
		query.setString(0,destino);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}

	public void salvar(RelacaoTrafego item) {
		session.saveOrUpdate((RelacaoTrafego)item);
	}

	public void deletar(RelacaoTrafego item) {
		session.delete((RelacaoTrafego)item);
	}

}
