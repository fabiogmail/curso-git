package Portal.Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import Portal.Dto.AgendaDTO;
import Portal.Dto.ExecucoesAgendaDTO;
import agenda.beans.dominio.Agenda;
import agenda.beans.dominio.Perfil;
import agenda.beans.dominio.RelatoriosCfg;
import agenda.beans.dominio.TipoRelatorio;
import agenda.beans.dominio.Agenda.PERIODICIDADE;
import agenda.beans.dominio.Agenda.STATUS;


public class ConexaoBDAgenda {

	private ConexaoDAO dao;
	private Connection conexao;
	public ConexaoBDAgenda() {
		dao = new ConexaoDAO();
		conexao = dao.getConexao();
	}


	/**
	 * @param perfil: perfil do usuário que será feita a pesquisa. Perfil do cdrview (campo perfilCDRVew na tabela perfil)
	 * @param host: nome do host em questão. Onde existe o perfil do usuário.
	 * @param status: lista de status que deve ser retornada
	 * @param isHistorico: indica se devem ser retornadas agendas(false) ou históricos (true)
	 * @return
	 */
	public ArrayList<AgendaDTO> listaAgendas(int perfil, String host, ArrayList<STATUS> status,boolean isHistorico ){
		try {

			Statement st = conexao.createStatement();



			String listaStatus = "";

			for (int i = 0; i < status.size(); i++) {
				listaStatus += "'"+status.get(i).name()+"'";

				if(i < status.size() -1){
					listaStatus += ",";
				}

			}
			String query = "Select a.id_agenda, p.nome as PERFIL, tr.nome as TIPO, r.nome as RELATORIO, a.usuario as USUARIO, " +
					"a.inicio as DATA_INICIO, a.termino as DATA_FIM, " +
					"a.horaInicio as HORA_INICIO, a.horaTermino as HORA_TERMINO, a.status as STATUS, " +
					"a.dias_do_mes as DIAS_MES, a.dias_da_semana as DIAS_SEMANA, a.semanas_de_ocorrencia as SEMANA_OCORRENCIA, " +
					"a.periodicidade as PERIODICIDADE, a.progresso as PROGRESSO, " +
					"a.latencia as LATENCIA, "+
					"a.processando as PROCESSANDO "+
					"from Agenda a " +
					"inner join relatorios_cfg r on r.id_agenda = a.id_agenda " +
					"inner join perfil p on r.id_perfil = p.id_perfil " +
					"inner join tipo_relatorio tr on tr.id_tipo_relatorio = r.id_tipo_relatorio " +
					"inner join host h on p.id_host = h.id_host " +
					//"inner join usuario u on r.id_usuario = u.id_usuario " +
					"where 1=1 "; 

			if(perfil != 1){//se não for administrador
				query += "and p.id_perfilcdrview = "+perfil+" ";
				query += "and (h.nome = '"+host+"' or h.ip = '"+host+"') ";
			}

			if(status.size() > 0){
				query += " and a.status in ("+listaStatus+") ";
			}

			if(isHistorico){
				query += " and a.eh_historico = 1";
			}else{
				query += " and a.eh_historico = 0";
			}

			query +=" order by p.nome, tr.nome, a.usuario";
			System.out.println("QUERY: "+query);
			ResultSet rs = st.executeQuery(query);

			ArrayList<AgendaDTO> retorno = new ArrayList<AgendaDTO>();

			while(rs.next()){
				AgendaDTO ag = new AgendaDTO();
				RelatoriosCfg rel = new RelatoriosCfg();
				Perfil p = new Perfil();
				TipoRelatorio tr = new TipoRelatorio();
				//				Usuario usu = new Usuario();

				p.setNome(rs.getString("PERFIL"));

				tr.setNome(rs.getString("TIPO"));

				rel.setNome(rs.getString("RELATORIO"));
				rel.setTipoRelatorio(tr);
				rel.setPerfil(p);

				//				usu.setNome("USUARIO");
				ag.setIdAgenda(rs.getInt("id_agenda"));
				ag.setProcessando(rs.getBoolean("PROCESSANDO"));
				ag.setRelatoriosCfg(rel);
				ag.setUsuario(rs.getString("USUARIO"));
				ag.setInicio(rs.getTimestamp("DATA_INICIO"));
				ag.setTermino(rs.getTimestamp("DATA_FIM"));
				ag.setHoraInicio(rs.getTimestamp("HORA_INICIO"));
				ag.setHoraTermino(rs.getTimestamp("HORA_TERMINO"));
				if(ag.isProcessando())
					ag.setStatus(STATUS.ANDAMENTO);
				else
					ag.setStatus(STATUS.valueOf(rs.getString("STATUS")));
				ag.setDiasDoMes(rs.getString("DIAS_MES"));
				ag.setDiasDaSemana(rs.getString("DIAS_SEMANA"));
				ag.setPeriodicidade(PERIODICIDADE.valueOf(rs.getString("PERIODICIDADE")));
				ag.setSemanasDeOcorrencia(rs.getString("SEMANA_OCORRENCIA"));
				ag.setProgresso(rs.getFloat("PROGRESSO"));
				ag.setLatencia(rs.getInt("LATENCIA"));

				retorno.add(ag);

			}

			rs.close();
			st.close();

			return retorno;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


	}


	/**
	 * takes schedule and returns the result of success executions per number of executions
	 */
	public String getStatusPercentual(AgendaDTO agenda){
		String status = "";
		try {

			Statement st = conexao.createStatement();

			String query = "select CONCAT(FORMAT((COALESCE((count(1) / b.total),0)),2) * 100,'%')  as tot " +
					"from datas_execucoes a, " +
					"(select count(1) as \"total\" from datas_execucoes where id_agenda =" +agenda.getIdAgenda()+") b " +
					"where id_agenda = "+agenda.getIdAgenda() +
					" and executou = 1 ";		
			System.out.println("QUERY calculando status: "+query);
			ResultSet rs = st.executeQuery(query);

			while(rs.next()){
				status = rs.getString(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status == null)
			status = "0%";
		return status;
	}


	public void listaCenarioExecucoes(AgendaDTO agenda){
		try {

			Statement st = conexao.createStatement();

			//			String query = "select data, executou, data_execucao " +
			//					"from datas_execucoes exec " +
			//					"where exec.id_agenda = "+agenda.getIdAgenda() +" " +
			//						//"and executou = 0 " +
			//					"order by data, data_execucao";

			String query = "select id, data, executou, data_execucao " +
					"from datas_execucoes exec " +
					"where exec.id_agenda = "+agenda.getIdAgenda() +" " +
					//"and executou = 0 " +
					"order by data, data_execucao";

			System.out.println("QUERY recuperando execucoes agenda: "+query);
			ResultSet rs = st.executeQuery(query);

			while(rs.next()){
				ExecucoesAgendaDTO execucaoDTO = new ExecucoesAgendaDTO();
				execucaoDTO.setIdExecucao(rs.getInt("id"));
				execucaoDTO.setData(rs.getTimestamp("data"));
				execucaoDTO.setExecutou(new Boolean(rs.getBoolean("executou")));
				if(execucaoDTO.isExecutou())
					execucaoDTO.setDataExecucao(rs.getTimestamp("data_execucao"));
				else
					execucaoDTO.setDataExecucao(new Date());


				agenda.getExecucoesAgenda().add(execucaoDTO);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/** Metodo responsavel por retornar a próxima execucao de determinada agenda, ou seja, a agenda mais proxima que ainda nao possue o campo executou com valor 1(executado)
	 * @param agenda
	 * @return
	 */
	public int getExecucaoAtual(AgendaDTO agenda){
		try{
			Statement st = conexao.createStatement();
			String query = "select id "+
					"from datas_execucoes exec "+
					"where exec.id_agenda = "+agenda.getIdAgenda() +" and data = (select min(data) from datas_execucoes where executou = 0 and id_agenda = "+agenda.getIdAgenda() +") "+
					"order by data, data_execucao";

			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				return rs.getInt("id");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

		return 0;
	}

	public static void main(String[] args) {
		ConexaoBDAgenda bd = new ConexaoBDAgenda();

		ArrayList<STATUS> status = new ArrayList<STATUS>();

		status.add(STATUS.PROCESSADO);

		ArrayList<AgendaDTO> ret = bd.listaAgendas(1,"vemaguete" , status, false);

		for (int i = 0; i < ret.size(); i++) {
			System.out.println(ret.get(i).getRelatoriosCfg().getNome() + " - "+ret.get(i).getStatus());
		}
	}


}
