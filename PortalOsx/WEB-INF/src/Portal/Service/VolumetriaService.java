package Portal.Service;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.jasperreports.engine.JasperPrint;

import org.hibernate.Session;

import src.br.com.visent.dbdeteccao.corba.CorbaException;
import src.br.com.visent.dbdeteccao.corba.CorbaUtil;
import src.br.com.visent.dbdeteccao.corba.Format;
import src.br.com.visent.dbdeteccao.dao.LogDAO;
import src.br.com.visent.dbdeteccao.dto.LogSys;
import src.br.com.visent.dbdeteccao.util.FiltrosLog;

import Portal.Dao.Hibernate.Conf.RelatoriosVolumetriaDAO;
import Portal.Dao.Util.HibernateUtil;
import Portal.Dto.Bilhetador;
import Portal.Dto.RecursoVolumetria;
import Portal.Dto.RelatorioVolumetria;
import Portal.Dto.RelatorioVolumetriaDomingo;
import Portal.Dto.RelatorioVolumetriaQuarta;
import Portal.Dto.RelatorioVolumetriaQuinta;
import Portal.Dto.RelatorioVolumetriaSabado;
import Portal.Dto.RelatorioVolumetriaSegunda;
import Portal.Dto.RelatorioVolumetriaSexta;
import Portal.Dto.RelatorioVolumetriaTerca;
import Portal.Jasper.RelatorioAcessoJasper;
import Portal.Operacoes.OpRelVolumetriaAjax;
import Portal.Utils.DBDeteccaoProperties;

public class VolumetriaService {
	private RelatorioVolumetriaDomingo domingoDto;	
	private RelatorioVolumetriaSegunda segundaDto;
	private RelatorioVolumetriaTerca tercaDto;
	private RelatorioVolumetriaQuarta quartaDto;
	private RelatorioVolumetriaQuinta quintaDto;
	private RelatorioVolumetriaSexta sextaDto;
	private RelatorioVolumetriaSabado sabadoDto;
	private RelatoriosVolumetriaDAO relVolumetria;
	private RelatorioVolumetria relatorioVolumetria;
	
	public static final int RELATORIO = 0;
	public static final int ALARME = 1;
	
	private int tipo = -1;
	
	
	/**
	 * DTO com os dados retornados pelo servidor a respeito dos alarmes
	 */
	private LogSys volumetriaAlarmeLog;
	/**
	 * Objeto de comunicação com o servidor para obtenção dos dados.
	 */
	private LogDAO relAlarmeDAO;
	
	private Session session;
	private Bilhetador bilhetador;
	private ArrayList listaCompleta;
	private RelatorioAcessoJasper relAcesso = new RelatorioAcessoJasper();
	
	public VolumetriaService(Session session){
		this.session = session;	
		setListaCompleta( new ArrayList());
		setTipo(RELATORIO);
//		setRelatorioVolumetria(new RelatorioVolumetria());
	}
	
	public VolumetriaService(){
		
		setListaCompleta( new ArrayList());
		inicializarCorba();
		setTipo(ALARME);
	}
	
	public String getNomeBilhetador(String bilhetarores){
		setRelVolumetria(new RelatoriosVolumetriaDAO(session));
		listaCompleta = relVolumetria.getBilhetadores(bilhetarores);
		
		StringBuffer nomeBilhetador =  new StringBuffer();
		for (Iterator iterator = listaCompleta.iterator(); iterator.hasNext();) {
			Bilhetador bilhetador = (Bilhetador) iterator.next();
			nomeBilhetador.append(bilhetador.getNome()+";");
		}  
			
		nomeBilhetador.deleteCharAt(nomeBilhetador.lastIndexOf(";"));
		
		return nomeBilhetador.toString();
		
	}
	
	public ArrayList getRecursoVolumetria(){
		setRelVolumetria(new RelatoriosVolumetriaDAO(session));
		ArrayList listaRecurso = relVolumetria.getRecursoVolumetria();
		return listaRecurso;
	}
	
	
	public Boolean salvar(ArrayList listaOrdenada){
		ArrayList listaVolumetria = listaOrdenada;
		//Session session = HibernateUtil.getSessionFactory().openSession();
		RelatoriosVolumetriaDAO dao = new RelatoriosVolumetriaDAO(session);
		
		for (Iterator iterator = listaOrdenada.iterator(); iterator.hasNext();) {
			
			RecursoVolumetria recurso = new RecursoVolumetria();
			String recursoVol = (String) iterator.next();
			String[]dados =  recursoVol.split(";");
			Bilhetador bil = dao.getBilhetador(dados[0]);
			recurso.setBilhetador(bil);
			recurso.setIdBilhetador(bil.getIdBilhetador());
			recurso.setLimMinimo(0);
			recurso.setLimMaximo(0);
			
			if(dados.length>1 && !dados[1].equals("")){
				recurso.setLimMaximo(Integer.parseInt(dados[1]));
			}
			
			if(dados.length>2 && !dados[2].equals("")){
				recurso.setLimMinimo(Integer.parseInt(dados[2]));
			}
			
			dao.salvar(recurso);
		} 
		return true;	
	}
	
	public ArrayList getRelVolumetria (String bilhetadores){
		
		setRelVolumetria(new RelatoriosVolumetriaDAO(session));
		ArrayList dao = new ArrayList();
		String queryBilhetadores = configuraWhere(bilhetadores);
		
		 dao = relVolumetria.getDomingo(queryBilhetadores);	
		 criaObjetosVolumetria(dao,"domingo");
		 
		 dao.clear();
		
		 dao = relVolumetria.getSegunda(queryBilhetadores);	
		 criaObjetosVolumetria(dao,"segunda");
		 
		 dao.clear();
		 
		 dao = relVolumetria.getTerca(queryBilhetadores);
		 criaObjetosVolumetria(dao,"terca");
		 
		 dao.clear();
		 
		 dao = relVolumetria.getQuarta(queryBilhetadores);	
		 criaObjetosVolumetria(dao,"quarta");
		 
		 dao.clear();
		
		 dao = relVolumetria.getQuinta(queryBilhetadores);
		 criaObjetosVolumetria(dao,"quinta");	 
		 
		 dao.clear();
		 
		 dao = relVolumetria.getSexta(queryBilhetadores);	
		 criaObjetosVolumetria(dao,"sexta");
		 
		 dao.clear();
		
		 dao = relVolumetria.getSabado(queryBilhetadores);	
		 criaObjetosVolumetria(dao,"sabado");
		 
		 if(listaCompleta != null){
			 listaCompleta.trimToSize();
		 }
		 
		 
		 return listaCompleta;
	}
	
	public ArrayList getRelVolumetriaAlarme(String bilhetadores, String id) {
		// TODO Auto-generated method stub		
		try {
			
			relAlarmeDAO = new LogDAO();
			
			FiltrosLog filtros = new FiltrosLog();//Filtros a serem passados ao servidor
			filtros.setTipo("VOLUMETRIA");//retornando apenas os dados de volumetria
			
			if(bilhetadores.length() > 0)
				filtros.setRecurso(bilhetadores);
			
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
			return null;
		}
		
		
		
//		return null;
	}
	
	private String configuraWhere(String bilhetadores) {
		// TODO Auto-generated method stub
		
		if(bilhetadores == null )
			bilhetadores = "";
		
		if(bilhetadores.length() < 1){
			return "";
		}
		else{
			String where = "where";
			String []arrayBilhetadores = bilhetadores.split(";");
			
			for (int i = 0; i < arrayBilhetadores.length; i++) {
				if(i == 0)
					where += " bil.nome = '"+ arrayBilhetadores[i]+"'";
				
				else
					where += " or bil.nome = '"+ arrayBilhetadores[i]+"'";
			}
			
			return where;
		}
		
		
	}

	private void criaObjetosVolumetria(ArrayList listaVolumetria, String diaSemana){
		Object []lista;
				
		for (int i = 0; i < listaVolumetria.size(); i++) {
			lista = (Object[]) listaVolumetria.get(i);
			mesclaObjetosVolumetria(lista,diaSemana);
		}	
	}
	
	private void mesclaObjetosVolumetria(Object[] objetos, String diaSemana){
		
		setRelatorioVolumetria(new RelatorioVolumetria());
		
		if(diaSemana.equalsIgnoreCase("domingo")){
			ArrayList listaSemana = new ArrayList(); 
			setDomingoDto((RelatorioVolumetriaDomingo) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getDomingoDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getDomingoDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getDomingoDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getDomingoDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getDomingoDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getDomingoDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getDomingoDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
			
		}
		if(diaSemana.equalsIgnoreCase("segunda")){
			ArrayList listaSemana = new ArrayList(); 
			setSegundaDto((RelatorioVolumetriaSegunda) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getSegundaDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getSegundaDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getSegundaDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getSegundaDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getSegundaDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getSegundaDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getSegundaDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
		}
		if(diaSemana.equalsIgnoreCase("terca")){
			ArrayList listaSemana = new ArrayList(); 
			setTercaDto((RelatorioVolumetriaTerca) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getTercaDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getTercaDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getTercaDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getTercaDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getTercaDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getTercaDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getTercaDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
		}
		if(diaSemana.equalsIgnoreCase("quarta")){
			ArrayList listaSemana = new ArrayList(); 
			setQuartaDto((RelatorioVolumetriaQuarta) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getQuartaDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getQuartaDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getQuartaDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getQuartaDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getQuartaDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getQuartaDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getQuartaDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
		}
		if(diaSemana.equalsIgnoreCase("quinta")){
			ArrayList listaSemana = new ArrayList(); 
			setQuintaDto((RelatorioVolumetriaQuinta) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getQuintaDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getQuintaDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getQuintaDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getQuintaDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getQuintaDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getQuintaDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getQuintaDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
		}
		if(diaSemana.equalsIgnoreCase("sexta")){
			ArrayList listaSemana = new ArrayList(); 
			setSextaDto((RelatorioVolumetriaSexta) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getSextaDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getSextaDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getSextaDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getSextaDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getSextaDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getSextaDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getSextaDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
		}
		if(diaSemana.equalsIgnoreCase("sabado")){
			ArrayList listaSemana = new ArrayList(); 
			setSabadoDto((RelatorioVolumetriaSabado) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getRelatorioVolumetria().setBilhetador(getBilhetador().getNome());
			getRelatorioVolumetria().setId(getSabadoDto().getId());
			getRelatorioVolumetria().setDataUltimoProc(getSabadoDto().getDataUltimoProc());
			getRelatorioVolumetria().setSemanaDois(getSabadoDto().getSemanaDois());
			getRelatorioVolumetria().setSemanaTres(getSabadoDto().getSemanaTres());
			getRelatorioVolumetria().setSemanaUm(getSabadoDto().getSemanaUm());
			getRelatorioVolumetria().setSemanaQuatro(getSabadoDto().getSemanaQuatro());
			getRelatorioVolumetria().setValorAtual(getSabadoDto().getValorAtual());
			getRelatorioVolumetria().setDia(diaSemana);
			listaCompleta.add(getRelatorioVolumetria());
		}
		
		setRelatorioVolumetria(null);
		
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
	public String getRelatorioAcesso(int pg, String paramOrdem, boolean group,  String path, String bilhetadores, OpRelVolumetriaAjax opRelVolumetriaAjax){	
		//RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();

		if(group){
			relatorio.addAll(getRelVolumetria(bilhetadores));			
		}
		else{
			try{
				relatorio.addAll(getRelVolumetriaAlarme(bilhetadores, opRelVolumetriaAjax.getSessao().getId()));
			}
			catch (NullPointerException e) {
				opRelVolumetriaAjax.setJasperPrint(new JasperPrint()); 
				opRelVolumetriaAjax.setJasperPrintDownload(new JasperPrint());
				return "Erro na comunicação com o servidor de alarmes.";
			}
		}
		
		relatorio.trimToSize();
		relAcesso.setDadosRelatorioAcesso(relatorio,group,path,opRelVolumetriaAjax);
		
		return relAcesso.relatorioHTML(pg,opRelVolumetriaAjax);
	}
	
	public String getRelatorioPagina(int pg, OpRelVolumetriaAjax opRelVolumetriaAjax){
		return relAcesso.relatorioHTML(pg,opRelVolumetriaAjax);
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

	public RelatorioVolumetriaDomingo getDomingoDto() {
		return domingoDto;
	}

	public void setDomingoDto(RelatorioVolumetriaDomingo domingoDto) {
		this.domingoDto = domingoDto;
	}

	public RelatorioVolumetriaSegunda getSegundaDto() {
		return segundaDto;
	}

	public void setSegundaDto(RelatorioVolumetriaSegunda segundaDto) {
		this.segundaDto = segundaDto;
	}

	public RelatorioVolumetriaTerca getTercaDto() {
		return tercaDto;
	}

	public void setTercaDto(RelatorioVolumetriaTerca tercaDto) {
		this.tercaDto = tercaDto;
	}

	public RelatorioVolumetriaQuarta getQuartaDto() {
		return quartaDto;
	}

	public void setQuartaDto(RelatorioVolumetriaQuarta quartaDto) {
		this.quartaDto = quartaDto;
	}

	public RelatorioVolumetriaQuinta getQuintaDto() {
		return quintaDto;
	}

	public void setQuintaDto(RelatorioVolumetriaQuinta quintaDto) {
		this.quintaDto = quintaDto;
	}

	public RelatorioVolumetriaSexta getSextaDto() {
		return sextaDto;
	}

	public void setSextaDto(RelatorioVolumetriaSexta sextaDto) {
		this.sextaDto = sextaDto;
	}

	public RelatorioVolumetriaSabado getSabadoDto() {
		return sabadoDto;
	}

	public void setSabadoDto(RelatorioVolumetriaSabado sabadoDto) {
		this.sabadoDto = sabadoDto;
	}

	public void setRelVolumetria(RelatoriosVolumetriaDAO relVolumetria) {
		this.relVolumetria = relVolumetria;
	}

	public RelatorioVolumetria getRelatorioVolumetria() {
		return relatorioVolumetria;
	}

	public void setRelatorioVolumetria(RelatorioVolumetria relatorioVolumetria) {
		this.relatorioVolumetria = relatorioVolumetria;
	}

	public ArrayList getListaCompleta() {
		return listaCompleta;
	}

	public void setListaCompleta(ArrayList listaCompleta) {
		this.listaCompleta = listaCompleta;
	}

	public Bilhetador getBilhetador() {
		return bilhetador;
	}

	public void setBilhetador(Bilhetador bilhetador) {
		this.bilhetador = bilhetador;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
