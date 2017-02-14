package br.com.visent.matraf.dao;

import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Celula;
import br.com.visent.matraf.domain.Central;
import br.com.visent.matraf.domain.CentralRef;
import br.com.visent.matraf.domain.GrupoCelula;
import br.com.visent.matraf.domain.Operadora;
import br.com.visent.matraf.domain.Periodo;
import br.com.visent.matraf.domain.RelacaoTrafego;
import br.com.visent.matraf.domain.Rota;
import br.com.visent.matraf.domain.TipoAssinante;
import br.com.visent.matraf.util.HibernateUtil;

public class TesteDAO {
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.getTransaction().commit();
        //lr_05102006 lr_13092007
		String sql = 
				"select  {per.*}, {cref.*}, {c2.*}, {op.*}, {cele.*}, {cels.*}, {gc.*}, {ta.*}, {rt.*}, {rotas.*} , {rotae.*} ," + 
				"sum(lr.NUM_CHAMADAS) as cham,  sum(lr.OKT) as okt, sum(lr.TTC) as ttc, " +
				"sum(lr.TTO) as tto , sum(lr.CHAM_CONV) as chamconv " +
				"from lr_13092007 lr "+
				"left outer join periodo per on per.id_periodo = lr.id_periodo "+ 
				"left outer join centralref cref on cref.id_centralref = lr.id_centralref "+ 
				"left outer join central c2 on c2.id_central = lr.id_central "+ 
				"left outer join operadora op on op.id_operadora = lr.id_operadora "+ 
				"left outer join celula cele on cele.id_celula = lr.id_celulae "+ 
				"left outer join celula cels on cels.id_celula = lr.id_celulas "+ 
				"left outer join grupo_celula gc on gc.id_grupo_celula = lr.id_grupo_celula "+ 
				"left outer join tipo_assinante ta on ta.id_tipo_assinante = lr.id_tipo_assinante "+ 
				"left outer join relacao_trafego rt on rt.id_relacao_trafego = lr.id_relacao_trafego "+ 
				"left outer join rota rotas on rotas.id_rota = lr.id_rotas "+ 
				"left outer join rota rotae on rotae.id_rota = lr.id_rotae "+ 
				"where lr.id_periodo = ? and lr.id_centralref = ? and referencial = ? and rt.nome like '%1%'  "+ 
				"group by  lr.id_relacao_trafego, lr.id_operadora order by lr.id_operadora";
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.setInteger(0,0);
		query.setInteger(1,1);
		query.setInteger(2,0);
		
		query.addScalar("cham");
		query.addScalar("okt");
		query.addScalar("ttc");
		query.addScalar("tto");
		query.addScalar("chamconv");
		
		query.addEntity("per", Periodo.class);
		query.addEntity("cref", CentralRef.class);
		
		query.addEntity("cele", Celula.class);
		query.addEntity("cels", Celula.class);
		query.addEntity("c2", Central.class);
		query.addEntity("op", Operadora.class);
		query.addEntity("gc", GrupoCelula.class);
		
		query.addEntity("ta", TipoAssinante.class);
		query.addEntity("rt", RelacaoTrafego.class);
		query.addEntity("rotas", Rota.class);
		query.addEntity("rotae", Rota.class);
		
		ArrayList lista = new ArrayList();
		try {
			lista = (ArrayList)query.list();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		
        session.close();
		
		
	}


}
