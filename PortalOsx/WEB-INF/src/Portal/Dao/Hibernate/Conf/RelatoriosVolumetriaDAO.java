package Portal.Dao.Hibernate.Conf;

import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;



import Portal.Dto.Bilhetador;
import Portal.Dto.RecursoVolumetria;
import Portal.Dto.RelatorioVolumetriaDomingo;
import Portal.Dto.RelatorioVolumetriaQuarta;
import Portal.Dto.RelatorioVolumetriaQuinta;
import Portal.Dto.RelatorioVolumetriaSabado;
import Portal.Dto.RelatorioVolumetriaSegunda;
import Portal.Dto.RelatorioVolumetriaSexta;
import Portal.Dto.RelatorioVolumetriaTerca;

public class RelatoriosVolumetriaDAO {
	
private Session session;
	
	public RelatoriosVolumetriaDAO(Session session){
		this.session = session;
	}
	
	public ArrayList getDomingo(String where){
		String sql = "select {dto.*},{bil.*} from domingo_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaDomingo.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getSegunda(String where){
		String sql = "select {dto.*},{bil.*} from segunda_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaSegunda.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getTerca(String where){
		String sql = "select {dto.*},{bil.*} from terca_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaTerca.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getQuarta(String where){
		String sql = "select {dto.*},{bil.*} from quarta_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaQuarta.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getQuinta(String where){
		String sql = "select {dto.*},{bil.*} from quinta_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaQuinta.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getSexta(String where){
		String sql = "select {dto.*},{bil.*} from sexta_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaSexta.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getSabado(String where){
		String sql = "select {dto.*},{bil.*} from sabado_volumetria dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",RelatorioVolumetriaSabado.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getBilhetadores(String where){
		String sql = "select {bil.*} from bilhetador bil";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public int getIdBilhetador(String list){
		String sql= "select {bil.*} from bilhetador bil where bil.NOME like '"+list+"'";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();	
		Bilhetador bilhetador = (Bilhetador) lista.get(0);
		return bilhetador.getIdBilhetador();
	}
	
	public Bilhetador getBilhetador(String nome){
		String sql= "select {bil.*} from bilhetador bil where bil.nome=?";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("bil",Bilhetador.class);
		query.setString(0,nome);
		Bilhetador bilhetador = (Bilhetador) query.uniqueResult();
		return bilhetador;
	}
	
	public ArrayList getRecursoVolumetria(){
		String sql = "select {rec.*} from recurso_volumetria rec";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("rec",RecursoVolumetria.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public void salvar(RecursoVolumetria item) {
		// session.beginTransaction();  
		 session.saveOrUpdate(item);  
		 //session.beginTransaction().commit();
		 //((Session) session.beginTransaction()).close();
		 //session.save(item);
	}
}
