package Portal.Service;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JasperPrint;

import org.hibernate.Session;

import src.br.com.visent.dbdeteccao.corba.CorbaException;
import src.br.com.visent.dbdeteccao.corba.CorbaUtil;
import src.br.com.visent.dbdeteccao.corba.Format;
import src.br.com.visent.dbdeteccao.dao.LogDAO;
import src.br.com.visent.dbdeteccao.dto.LogSys;
import src.br.com.visent.dbdeteccao.util.FiltrosLog;
import Portal.Dto.Bilhetador;
import Portal.Jasper.RelatorioAcessoJasper;
import Portal.Operacoes.OpVisualizaAlarmesPlataformaAjax;
import Portal.Utils.DBDeteccaoProperties;

public class AlarmesPlataformaService {
	
	private LogDAO relAlarmeDAO;
	private LogSys volumetriaAlarmeLog; 
	
	private Session session;
	private Bilhetador bilhetador;
	private ArrayList listaCompleta;
	private RelatorioAcessoJasper relAcesso = new RelatorioAcessoJasper();
	
	public AlarmesPlataformaService(){
		
		setListaCompleta( new ArrayList());
		inicializarCorba();
	}
	
	
	
	/**
	 * @param pg
	 * @param paramOrdem
	 * @param group indica qual relatório será executado. TRUE=relatorio FALSE=alarmes
	 * @param path
	 * @param bilhetadores
	 * @param opRelVolumetriaAjax
	 * @return
	 */
	
	
	
	public ArrayList getAlarmePlataforma(String id) {
		// TODO Auto-generated method stub		
		try {
			relAlarmeDAO = new LogDAO();
			
			FiltrosLog filtros = new FiltrosLog();//Filtros a serem passados ao servidor
			filtros.setTipo("ESPACO_DIR_EXCEDIDO;QTD_ARQ_IN;ESPACO_DISCO;PROC_PARADO;CONVERSOR_OCIOSO");//retornando apenas os dados de volumetria
			String formato[] = relAlarmeDAO.getFormato("SYS");
			
			String dadosStr = relAlarmeDAO.buscarAlarmes(Integer.parseInt(id.substring(0, 7), 16),5,true,filtros.toString(),1);
			
			String dados[] = new String[0];
			if(!dadosStr.equals("")){
				dados = dadosStr.split("\n");
			}
			Format format = new Format(formato,LogSys.class);
			
			for (int i = 0; i < dados.length; i++) {
				int fator = 2*(1-1);
				LogSys log = (LogSys)format.formatar(dados[i]);
				log.setID(fator+i);			
				listaCompleta.add(log);
			}
			
			if(listaCompleta != null){
				listaCompleta.trimToSize();
			}
			
			return listaCompleta;
			
		} catch (CorbaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	public String getRelatorioAcesso(int pg, String paramOrdem, boolean group,  String path, OpVisualizaAlarmesPlataformaAjax opVisualizaAlarmesPlataformaAjax){	
		//RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
		try{
			relatorio.addAll(getAlarmePlataforma(opVisualizaAlarmesPlataformaAjax.getSessao().getId()));
			relatorio.trimToSize();
			relAcesso.setDadosRelatorioAcesso(relatorio,group,path,opVisualizaAlarmesPlataformaAjax);
			return relAcesso.relatorioHTMLAlarmePlataforma(pg,opVisualizaAlarmesPlataformaAjax);
			
		}
		catch (NullPointerException e) {
			// TODO: handle exception
			opVisualizaAlarmesPlataformaAjax.setJasperPrint(new JasperPrint());
			return "Erro na comunicação com o servidor de alarmes.";
		}
	}
	
	private void inicializarCorba() {
		// TODO Auto-generated method stub
		String host = DBDeteccaoProperties.getProperties("host");
		String porta = DBDeteccaoProperties.getProperties("porta");
		CorbaUtil util = new CorbaUtil(porta, host);
		try {
			util.init();
		} catch (CorbaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LogDAO getRelAlarmeDAO() {
		return relAlarmeDAO;
	}

	public void setRelAlarmeDAO(LogDAO relAlarmeDAO) {
		this.relAlarmeDAO = relAlarmeDAO;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Bilhetador getBilhetador() {
		return bilhetador;
	}

	public void setBilhetador(Bilhetador bilhetador) {
		this.bilhetador = bilhetador;
	}

	public ArrayList getListaCompleta() {
		return listaCompleta;
	}

	public void setListaCompleta(ArrayList listaCompleta) {
		this.listaCompleta = listaCompleta;
	}

	public RelatorioAcessoJasper getRelAcesso() {
		return relAcesso;
	}

	public void setRelAcesso(RelatorioAcessoJasper relAcesso) {
		this.relAcesso = relAcesso;
	}

	public LogSys getVolumetriaAlarmeLog() {
		return volumetriaAlarmeLog;
	}

	public void setVolumetriaAlarmeLog(LogSys volumetriaAlarmeLog) {
		this.volumetriaAlarmeLog = volumetriaAlarmeLog;
	}

}
