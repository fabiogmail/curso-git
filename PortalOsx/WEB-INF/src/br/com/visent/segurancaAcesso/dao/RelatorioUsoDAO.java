package br.com.visent.segurancaAcesso.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.visent.segurancaAcesso.domain.RegistroUso;
/**
 * 
 * Visent Informática.
 * Projeto: PortalOsx
 *
 * @author Rafael
 * @version 1.0
 * @created 20/04/2007 12:07:49
 */
public class RelatorioUsoDAO {

	private Session session;
	
	public RelatorioUsoDAO(Session session) {
		this.session = session;
	}
	
	/**
	 * 
	 * @return 
	 */
	public ArrayList listar(String paramOrdem)
	{
		String sql = "from RegistroUso ob ";
		
		if(paramOrdem != null)
			sql += "order by ob."+paramOrdem;
		
		Query query = session.createQuery(sql);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	/**
	 * 
	 * @param 
	 * @return 
	 */
	public RegistroUso buscarById(int id, String paramOrdem) {
		String sql = "from RegistroUso as ob where ob.id=? ";
		
		if(paramOrdem != null)
			sql += "order by ob."+paramOrdem;
		
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		RegistroUso item = (RegistroUso)query.uniqueResult();
		return item;
	}
	/**
	 * 
	 * @param 
	 * @return 
	 */
	public ArrayList buscarByUsuario(String usuario, String paramOrdem) 
	{
		String sql = "from RegistroUso as ob where ob.usuario = ? ";
		
		if(paramOrdem != null)			
			sql += "order by ob."+paramOrdem;
		
		Query query = session.createQuery(sql);
		query.setString(0,usuario);		
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}	
	
	/**
	 * 
	 * @param 
	 * @return 
	 */
	public ArrayList buscarByData(String periodoInicio,String periodoFim, String paramOrdem) {
		String sql = "";
		ArrayList lista;
		if(periodoInicio!= null && periodoFim!=null)
		{
			sql = "from RegistroUso as ob where ob.dtRegistro between ? and ? ";
			
			if(paramOrdem != null)
				sql += "order by ob."+paramOrdem;

			Query query = session.createQuery(sql);
			query.setString(0,periodoInicio);
			query.setString(1,periodoFim);
			lista = (ArrayList)query.list();
			return lista;
		}
		else
			if(periodoInicio!= null)
			{
				sql = "from RegistroUso as ob where ob.dtRegistro > ? ";
				
				if(paramOrdem != null)
					sql += "order by ob."+paramOrdem;

				Query query = session.createQuery(sql);
				query.setString(0,periodoInicio);
				lista = (ArrayList)query.list();
			}
			else
			{
				sql = "from RegistroUso as ob where ob.dtRegistro < ? ";
				
				if(paramOrdem != null)
					sql += "order by ob."+paramOrdem;
				
				Query query = session.createQuery(sql);
				query.setString(0,periodoFim);
				lista = (ArrayList)query.list();
			}		
		return lista;
	}	
	/**
	 * 
	 * @param usuario
	 * @param periodoInicio
	 * @param periodoFim
	 * @return
	 */
	public ArrayList buscarByUserData(String usuario,String periodoInicio,String periodoFim, String paramOrdem) 
	{
		String sql = "";
		ArrayList lista;
		if(periodoInicio!= null && periodoFim!=null)
		{
			sql = "from RegistroUso as ob where ob.usuario = ? and ob.dtRegistro between ? and ? ";
		
			if(paramOrdem != null)
				sql += "order by ob."+paramOrdem;
		
			Query query = session.createQuery(sql);
			query.setString(0,usuario);
			query.setString(1,periodoInicio);
			query.setString(2,periodoFim);
			lista = (ArrayList)query.list();
		}
		else
			if(periodoInicio!= null)
			{
				sql = "from RegistroUso as ob where ob.usuario = ? and ob.dtRegistro > ?";
				
				if(paramOrdem!=null)
					sql += "order by ob."+paramOrdem;
				
				Query query = session.createQuery(sql);
				query.setString(0,usuario);
				query.setString(1,periodoInicio);
				lista = (ArrayList)query.list();
			}
			else
			{
				sql = "from RegistroUso as ob where ob.usuario = ? and ob.dtRegistro < ?";
				
				if(paramOrdem!=null)
					sql += "order by ob."+paramOrdem;
				
				Query query = session.createQuery(sql);
				query.setString(0,usuario);
				query.setString(1,periodoFim);
				lista = (ArrayList)query.list();
			}
		return lista;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ArrayList buscarListaDeFiltros(int id){
		String sql = "from Filtro as ob where ob.registroUso.id=?";
		Query query = session.createQuery(sql);
		query.setInteger(0,id);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	
	
}
