package br.com.visent.matraf.dao.hibernate;

import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import br.com.visent.matraf.domain.Celula;
import br.com.visent.matraf.domain.Central;
import br.com.visent.matraf.domain.CentralRef;
import br.com.visent.matraf.domain.GrupoCelula;
import br.com.visent.matraf.domain.LinhaRelatorio;
import br.com.visent.matraf.domain.Operadora;
import br.com.visent.matraf.domain.Periodo;
import br.com.visent.matraf.domain.RelacaoTrafego;
import br.com.visent.matraf.domain.Rota;
import br.com.visent.matraf.domain.TipoAssinante;
import br.com.visent.matraf.service.ajax.Pesquisa;

public class LinhaRelatorioDAO {
	
	private Session session;
	
	public LinhaRelatorioDAO(Session session) {
		this.session = session;
	}
	
	public ArrayList listar(Pesquisa pesquisa){
		
		String dataTabela = pesquisa.getDataStr().replace("/","");
		
		String groupBy = " group by ";
		for (int i = 0; i < pesquisa.getRecursos().size(); i++) {
			String recurso = (String)pesquisa.getRecursos().get(i);
			if(!recurso.equalsIgnoreCase(LinhaRelatorio.C_ORIGEM_DESTINO)){
				groupBy += " lr."+recurso+",";
			}
		}
		groupBy = groupBy.substring(0,groupBy.length()-1);

		String sql = 
			"select  {per.*}, {cref.*}, {c2.*}, {op.*}, {cele.*}, {cels.*}, {gc.*}, {ta.*}, {rt.*}, {rotas.*} , {rotae.*} , " +
			"sum(lr.NUM_CHAMADAS) as cham,  sum(lr.OKT) as okt, sum(lr.TTC) as ttc, sum(lr.TTO) as tto , sum(lr.CHAM_CONV) as chamconv "+
			"from lr_matrizinteresse_"+dataTabela+" lr " +
			"left outer join periodo per on per.id_periodo = lr.id_periodo " +
			"left outer join centralref cref on cref.id_centralref = lr.id_centralref " +
			"left outer join central c2 on c2.id_central = lr.id_central " +
			"left outer join operadora op on op.id_operadora = lr.id_operadora " +
			"left outer join celula cele on cele.id_celula = lr.id_celulae " +
			"left outer join celula cels on cels.id_celula = lr.id_celulas " +
			"left outer join grupo_celula gc on gc.id_grupo_celula = lr.id_grupo_celula " +
			"left outer join tipo_assinante ta on ta.id_tipo_assinante = lr.id_tipo_assinante " +
			"left outer join relacao_trafego rt on rt.id_relacao_trafego = lr.id_relacao_trafego " +
			"left outer join rota rotas on rotas.id_rota = lr.id_rotas " +
			"left outer join rota rotae on rotae.id_rota = lr.id_rotae " +
			"where lr.id_periodo = ? and lr.id_centralref = ? " +
			"and lr."+pesquisa.getCampoBilhetador()+" = ? and referencial = ? and " +
			"rt.nome like '%"+pesquisa.getValorRelTraf()+"%' "+
			groupBy;
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.setInteger(0,pesquisa.getPeriodo().getId());
		query.setInteger(1,pesquisa.getCentralRef().getId());
		query.setInteger(2,pesquisa.getIdBilhetador());
		if(pesquisa.ehOrigem()){
			query.setInteger(3,0);
		}else{
			query.setInteger(3,1);
		}
		
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
		
		
		ArrayList lista = (ArrayList)query.list();
		
		return lista;
	}
	
	public ArrayList resumo(Pesquisa pesquisa){
		
		String dataTabela = pesquisa.getDataStr().replace("/","");
		
		String groupBy = " group by ";
		for (int i = 0; i < pesquisa.getRecursos().size(); i++) {
			String recurso = (String)pesquisa.getRecursos().get(i);
			if(!recurso.equalsIgnoreCase(LinhaRelatorio.C_ORIGEM_DESTINO)){
				groupBy += " lr."+recurso+",";
			}
		}
		groupBy = groupBy + " lr."+pesquisa.getCampoBilhetador();

		String sql = 
			"select  {per.*}, {cref.*}, {c2.*}, {op.*}, {cele.*}, {cels.*}, {gc.*}, {ta.*}, {rt.*}, {rotas.*} , {rotae.*} , " +
			"sum(lr.NUM_CHAMADAS) as cham,  sum(lr.OKT) as okt, sum(lr.TTC) as ttc, sum(lr.TTO) as tto , sum(lr.CHAM_CONV) as chamconv "+
			"from lr_matrizinteresse_"+dataTabela+" lr " +
			"left outer join periodo per on per.id_periodo = lr.id_periodo " +
			"left outer join centralref cref on cref.id_centralref = lr.id_centralref " +
			"left outer join central c2 on c2.id_central = lr.id_central " +
			"left outer join operadora op on op.id_operadora = lr.id_operadora " +
			"left outer join celula cele on cele.id_celula = lr.id_celulae " +
			"left outer join celula cels on cels.id_celula = lr.id_celulas " +
			"left outer join grupo_celula gc on gc.id_grupo_celula = lr.id_grupo_celula " +
			"left outer join tipo_assinante ta on ta.id_tipo_assinante = lr.id_tipo_assinante " +
			"left outer join relacao_trafego rt on rt.id_relacao_trafego = lr.id_relacao_trafego " +
			"left outer join rota rotas on rotas.id_rota = lr.id_rotas " +
			"left outer join rota rotae on rotae.id_rota = lr.id_rotae " +
			"where lr.id_periodo = ? and lr.id_centralref = ? " +
			"and referencial = ? and " +
			"rt.nome like '%"+pesquisa.getValorRelTraf()+"%' "+
			groupBy + " order by lr."+pesquisa.getCampoBilhetador();
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.setInteger(0,pesquisa.getPeriodo().getId());
		query.setInteger(1,pesquisa.getCentralRef().getId());
		if(pesquisa.ehOrigem()){
			query.setInteger(2,0);
		}else{
			query.setInteger(2,1);
		}
		
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
		
		
		ArrayList lista = (ArrayList)query.list();
		
		return lista;
	}
	
	
	public ArrayList resumoGeral(Pesquisa pesquisa){
		
		String dataTabela = pesquisa.getDataStr().replace("/","");
		
		String sql = 
			"select  {per.*}, {cref.*}, {c2.*}, {op.*}, {cele.*}, {cels.*}, {gc.*}, {ta.*}, {rt.*}, {rotas.*} , {rotae.*} , lr.referencial as refer , " +
			"sum(lr.NUM_CHAMADAS) as cham,  sum(lr.OKT) as okt, sum(lr.TTC) as ttc, sum(lr.TTO) as tto , sum(lr.CHAM_CONV) as chamconv "+
			"from lr_matrizinteresse_"+dataTabela+" lr " +
			"left outer join periodo per on per.id_periodo = lr.id_periodo " +
			"left outer join centralref cref on cref.id_centralref = lr.id_centralref " +
			"left outer join central c2 on c2.id_central = lr.id_central " +
			"left outer join operadora op on op.id_operadora = lr.id_operadora " +
			"left outer join celula cele on cele.id_celula = lr.id_celulae " +
			"left outer join celula cels on cels.id_celula = lr.id_celulas " +
			"left outer join grupo_celula gc on gc.id_grupo_celula = lr.id_grupo_celula " +
			"left outer join tipo_assinante ta on ta.id_tipo_assinante = lr.id_tipo_assinante " +
			"left outer join relacao_trafego rt on rt.id_relacao_trafego = lr.id_relacao_trafego " +
			"left outer join rota rotas on rotas.id_rota = lr.id_rotas " +
			"left outer join rota rotae on rotae.id_rota = lr.id_rotae " +
			"where lr.id_periodo = ? and lr.id_centralref = ? " +
			" group by lr.referencial , lr.id_relacao_trafego ";
		
		SQLQuery query = session.createSQLQuery(sql);
		
//		if(pesquisa.ehOrigem()){
//			query.setInteger(0,0);
//		}else{
//			query.setInteger(0,1);
//		}
		query.setInteger(0,pesquisa.getPeriodo().getId());
		query.setInteger(1,pesquisa.getCentralRef().getId());
		
		query.addScalar("cham");
		query.addScalar("okt");
		query.addScalar("ttc");
		query.addScalar("tto");
		query.addScalar("chamconv");
		
		query.addScalar("refer");
		
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
		
		
		ArrayList lista = (ArrayList)query.list();
		
		return lista;
	}

	public ArrayList resumoCentral(Pesquisa pesquisa){
		
		String dataTabela = pesquisa.getDataStr().replace("/","");
		
		String groupBy = " group by ";
		for (int i = 0; i < pesquisa.getRecursos().size(); i++) {
			String recurso = (String)pesquisa.getRecursos().get(i);
			if(!recurso.equalsIgnoreCase(LinhaRelatorio.C_ORIGEM_DESTINO)){
				groupBy += " lr."+recurso+",";
			}
		}
		groupBy = groupBy + " lr."+pesquisa.getCampoBilhetador();

		String sql = 
			"select  {per.*}, {cref.*}, {c2.*}, {op.*}, {cele.*}, {cels.*}, {gc.*}, {ta.*}, {rt.*}, {rotas.*} , {rotae.*} , " +
			"sum(lr.NUM_CHAMADAS) as cham,  sum(lr.OKT) as okt, sum(lr.TTC) as ttc, sum(lr.TTO) as tto , sum(lr.CHAM_CONV) as chamconv "+
			"from lr_matrizinteresse_"+dataTabela+" lr " +
			"left outer join periodo per on per.id_periodo = lr.id_periodo " +
			"left outer join centralref cref on cref.id_centralref = lr.id_centralref " +
			"left outer join central c2 on c2.id_central = lr.id_central " +
			"left outer join operadora op on op.id_operadora = lr.id_operadora " +
			"left outer join celula cele on cele.id_celula = lr.id_celulae " +
			"left outer join celula cels on cels.id_celula = lr.id_celulas " +
			"left outer join grupo_celula gc on gc.id_grupo_celula = lr.id_grupo_celula " +
			"left outer join tipo_assinante ta on ta.id_tipo_assinante = lr.id_tipo_assinante " +
			"left outer join relacao_trafego rt on rt.id_relacao_trafego = lr.id_relacao_trafego " +
			"left outer join rota rotas on rotas.id_rota = lr.id_rotas " +
			"left outer join rota rotae on rotae.id_rota = lr.id_rotae " +
			"where lr.id_periodo = ? and lr.id_centralref = ? " +
			"and referencial = ? and " +
			"rt.nome like '%"+pesquisa.getValorRelTraf()+"%' "+
			groupBy + " order by lr."+pesquisa.getCampoBilhetador();
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.setInteger(0,pesquisa.getPeriodo().getId());
		query.setInteger(1,pesquisa.getCentralRef().getId());
		if(pesquisa.ehOrigem()){
			query.setInteger(2,0);
		}else{
			query.setInteger(2,1);
		}
		
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
		
		
		ArrayList lista = (ArrayList)query.list();
		
		return lista;
	}
	
}
