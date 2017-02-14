package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Periodo;

public class DataPeriodoDAO {
	
	private Session session;

	public DataPeriodoDAO(Session session){
		this.session = session;
	}
	
	public ArrayList listar(){
		String sql = "from DataPeriodo";
		Query query = session.createQuery(sql);
		ArrayList lista = new ArrayList();
		lista.addAll(query.list());
		return lista;
	}
	
	public ArrayList listarDatas(String central){
		String sql = "select distinct(per.data_tabela) as data from data_periodo per "+
					 "inner join data_periodo_central dpc on per.id_data_periodo = dpc.id_data_periodo and "+
					 "dpc.id_central = "+central;
		SQLQuery query = session.createSQLQuery(sql);
		query.addScalar("data", Hibernate.DATE);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList listarByData(Date data, String central) {
		String sql = " select {per.*} from data_periodo as d " +
		 " left outer join periodo per on d.id_periodo = per.id_periodo " +
		 " inner join data_periodo_central dpc on d.id_data_periodo = dpc.id_data_periodo and "+
		 " dpc.id_central = "+central+
		 " where d.data_tabela = ?";
		SQLQuery query = session.createSQLQuery(sql);
		query.setDate(0, data);
		query.addEntity("per", Periodo.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}

}
