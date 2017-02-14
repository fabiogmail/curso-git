package Portal.Operacoes;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import net.sf.jasperreports.engine.JasperPrint;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Dao.Util.HibernateUtil;
import Portal.Utils.ErroUtil;
import Portal.Service.SequenciaService;
import Portal.Service.VolumetriaService;

public class OpRelSequenciaAjax extends OperacaoAbs {
	
	private String listaBilhetador = "";
	private String listaData = "";
	private String bilhetador = "";
	//public ArrayList dados = new ArrayList();//variável para armazenar os dados do relatório para caso de exportação. Garante que a exportação mostre os mesmos dados que estão sendo apresentados em caso de atualização da base.
	private JasperPrint jasperPrint = new JasperPrint();
	private JasperPrint jasperPrintDrill = new JasperPrint();
	private JasperPrint jasperPrintDownload = new JasperPrint();
	private JasperPrint jasperPrintDrillDownload = new JasperPrint();
	
	private HttpSession sessao;
	private Session session;
	private int qtdPages;
	
	private boolean drill;
	
	SequenciaService sequenciaService = null;
	SequenciaService sequenciaServiceDrill = null;
	
	static 
	{
	}
	
	public OpRelSequenciaAjax() 
	{
		System.out.println("OpRelSequenciaAjax()");
		WebContext ctx = WebContextFactory.get();
		setSessao(ctx.getHttpServletRequest().getSession());
		session = HibernateUtil.getSessionFactory().openSession();
	}

	No noTmp = null;

	public String salvar(String data) throws ErroUtil {
		String resp = "";

		try {
			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
			{
				noTmp = (No) iter.next();

				//resp = noTmp.getConexaoServUtil().setDataReprocessamento(mudaFormatoData(data)) ;
			}

			return resp;			


		} catch (Exception e) {
			System.out.println("OpDataReprocessamento - salvar(): " + e);
			e.printStackTrace();
			return "Não foi possivel reprocessar a data informada ";
		}
	}

	private boolean verificaServUtil(No no) {
		if (no.getConexaoServUtil().getM_iUtil() == null) {
			no.createConexao(DefsComum.s_ServUtil, no.getConexaoServUtil()
					.getModoConexao(), no.getHostName(), no
					.getConexaoServUtil().getNomeObjetoCorba(), no
					.getConexaoServUtil().getPorta());
			if (no.getConexaoServUtil().getM_iUtil() == null) {
				return false;
			} else {
				no.setConexaoServUtil(no.getConexaoServUtil());
				return true;
			}
		} else {
			return true;
		}
	}

	public String getBilhetadores(){

		String resp = "";

		try {
			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
			{
				noTmp = (No) iter.next();

				resp += noTmp.getConexaoServUtil().getBilhetadores() ;
				if(iter.hasNext())
					resp += ";";
			}

			return resp;			


		} catch (Exception e) {
			System.out.println("OpDataReprocessamento - salvar(): " + e);
			e.printStackTrace();
			return "Não foi possivel reprocessar a data informada ";
		}

	}
	
	public String getRelatorio(int pagina){
		try{
			setDrill(false);

			session = HibernateUtil.getSessionFactory().getCurrentSession();

			Transaction tr = session.beginTransaction();
			String rel = getArquivo();

			if(sequenciaService == null || sequenciaService.getTipo() == SequenciaService.ALARME)//se o relatório setado anteriormente for alarme(corba), tem que criar novo objeto para conexão com banco
				sequenciaService = new SequenciaService(session);
			
			String relSequencia = sequenciaService.getRelatorioAcesso( pagina, "", true,rel, getListaBilhetador(), getListaData(), this);
			//Obtendo sessão
			tr.commit();
			getSessao().setAttribute("jasperPrint", getJasperPrintDownload());
			//

			if(relSequencia == null)
				return "Relatório Vazio";
			else if(relSequencia.length() < 1)
				return "Relatório Vazio";
			return relSequencia;
		}
		catch (org.hibernate.exception.JDBCConnectionException e) {
			// TODO: handle exception
			setJasperPrintDownload(new JasperPrint());
			return "Erro na comunicação com o banco de dados.";
		}
	}
	
	public String getRelatorioPagina(int pagina){
//		VolumetriaService volumetriaService = new VolumetriaService(session);
		String relSequencia = "";
		setDrill(false);
		if(sequenciaService != null)
			relSequencia = sequenciaService.getRelatorioPagina(pagina, this);
		if(relSequencia == null )
			return "Relatório Vazio";
		else if(relSequencia.length() < 1)
			return "Relatório Vazio";

		return relSequencia;
	}
	
	public String getRelatorioDrillPagina(int pagina){
//		VolumetriaService volumetriaService = new VolumetriaService(session);
		setDrill(true);
		String relSequencia = "";
		if(sequenciaServiceDrill != null)
			relSequencia = sequenciaServiceDrill.getRelatorioPagina(pagina, this);
		
		if(relSequencia == null )
			return "Relatório Vazio";
		
		else if(relSequencia.length() < 1)
			return "Relatório Vazio";

		return relSequencia;
	}
	
	public String getRelatorioAlarme(int pagina){		
		setDrill(false);
		if(sequenciaService == null || sequenciaService.getTipo() == SequenciaService.RELATORIO)//se o relatório setado anteriormente for relatorio(banco), tem que criar novo objeto para conexão com servidor de alarmes
			sequenciaService = new SequenciaService();
		
		String rel = getArquivoAlarme();
		
		String relSequencia = sequenciaService.getRelatorioAcesso( pagina, "", false,rel, getListaBilhetador(), getListaData(), this);
		//Obtendo sessão
		
		getSessao().setAttribute("jasperPrint", getJasperPrint());
		//

		//System.out.println(relVolumetria);
		
		if(relSequencia == null)
			return "Relatório Vazio";
		else if(relSequencia.length() < 1)
			return "Relatório Vazio";
		
		return relSequencia;
	}
	
	public String getDrillRepetido(int pagina){		
		
		setDrill(true);
		
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		
		String rel = getArquivoDrillRepetido();

		Transaction tr = session.beginTransaction();
		
		//if(sequenciaServiceDrill == null || sequenciaServiceDrill.getTipo() == SequenciaService.ALARME)//se o relatório setado anteriormente for alarme(corba), tem que criar novo objeto para conexão com banco
		sequenciaServiceDrill = new SequenciaService(session);
		
		
		String relSequencia = sequenciaServiceDrill.getRelatorioAcessoDrillRepetido( pagina, "",rel, getBilhetador(), getListaData(), this);
		
		//Obtendo sessão
		
		getSessao().setAttribute("jasperPrint", getJasperPrintDrillDownload());
		//
		tr.commit();

		
		if(relSequencia == null)
			return "Relatório Vazio";
		else if(relSequencia.length() < 1)
			return "Relatório Vazio";
		
		return relSequencia;
	}
	
	
	public String getDrillFaltante(int pagina){		
		
		setDrill(true);
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		String rel = getArquivoDrillFaltante();
		
		Transaction tr = session.beginTransaction();

		sequenciaServiceDrill = new SequenciaService(session);
		String relSequencia = sequenciaServiceDrill.getRelatorioAcessoDrillFaltante( pagina, "",rel, getBilhetador(), getListaData(), this);
		//Obtendo sessão
		
		getSessao().setAttribute("jasperPrint", getJasperPrintDrillDownload());
		//

		tr.commit();
		
		if(relSequencia == null)
			return "Relatório Vazio";
		else if(relSequencia.length() < 1)
			return "Relatório Vazio";
		
		return relSequencia;
	}
	
	
	public boolean atualizaJasper(String tipo){
		
		getSessao().removeAttribute("jasperPrint");
		
		if(tipo.length() > 0){
			getSessao().setAttribute("jasperPrint", getJasperPrintDrillDownload());
			return true;
		}
	
		getSessao().setAttribute("jasperPrint", getJasperPrintDownload());
		System.out.println("atualizando jasper. Sequencia AJAX");
		return true;
	}
	
	private String getArquivo() {
		 String path = WebContextFactory.get().getServletContext().getRealPath("/");
//       String path = "C:/visent/fontes/Deteccao2007/";
      
		 String pathxml = path+"reports/"+"Sequencia.jasper";
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		
		//File arq =  new File(pathRels);
		//return arq.getParentFile().getParent() + "\\reports\\Sequencia.jasper";
		 //System.out.println(pathxml);
		 return pathxml;
		
	}
	
	private String getArquivoAlarme() {
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		String pathxml = path+"reports/"+"SequenciaAlarme.jasper";
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		
		//File arq =  new File(pathRels);
		//return arq.getParentFile().getParent() + "\\reports\\SequenciaAlarme.jasper";	
		return pathxml;
	}
	
	private String getArquivoDrillFaltante() {
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		String pathxml = path+"reports/"+"SequenciaFaltantesDrill.jasper";
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		//File arq =  new File(pathRels);
		//return arq.getParentFile().getParent() + "\\reports\\SequenciaFaltantesDrill.jasper";	
		return pathxml;
	}
	
	private String getArquivoDrillRepetido() {
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		String pathxml = path+"reports/"+"SequenciaRepetidosDrill.jasper";
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		//File arq =  new File(pathRels);
		//return arq.getParentFile().getParent() + "\\reports\\SequenciaRepetidosDrill.jasper";	
		return pathxml;
	}
	
	

	
	public int getQtdPaginas(){
		
		return getQtdPages();
	}
	
	public void setListaBilhetador(String listaBilhetador) {
		this.listaBilhetador = listaBilhetador.substring(0, listaBilhetador.length()-1);
	}
	

	public String getListaBilhetador() {
		//Adaptação feita para não retornar bilhetadores de outras requisições da mesma sessão
		String aux = listaBilhetador;
		listaBilhetador = "";
		
		return aux;
	}
	
	public int setExportacao(int export){
		
		getSessao().setAttribute("exportacao", export);
		
		return 1;
	}
	
	public int getExportacao(){
		
		if(getSessao().getAttribute("exportacao") != null){
			return Integer.parseInt(""+getSessao().getAttribute("exportacao"));
		}
		
		return 0;
		
		
	}

	/**
	 * Finalizar a utilização da página. Aparagar dados da memória na tentativa de poupar recursos.
	 */
	public void finaliza(){
		getSessao().removeAttribute("jasperPrint");
		getSessao().removeAttribute("OpRelSequenciaAjax");
		sequenciaService = null;
		System.gc();
		
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}

	public HttpSession getSessao() {
		return sessao;
	}

	public void setSessao(HttpSession sessao) {
		this.sessao = sessao;
	}

	public String getListaData() {
		return listaData;
	}

	public void setListaData(String listaData) {	
		if(listaData.length() < 9)
			this.listaData = "";
		else 
			this.listaData = listaData;	
	}

	public String getBilhetador() {
		return bilhetador;
	}

	public void setBilhetador(String bilhetador) {
		this.bilhetador = bilhetador;
	}

	public JasperPrint getJasperPrintDrill() {
		return jasperPrintDrill;
	}

	public void setJasperPrintDrill(JasperPrint jasperPrintDrill) {
		this.jasperPrintDrill = jasperPrintDrill;
	}

	public boolean isDrill() {
		return drill;
	}

	public void setDrill(boolean drill) {
		this.drill = drill;
	}

	public int getQtdPages() {
		return qtdPages;
	}

	public void setQtdPages(int qtdPages) {
		this.qtdPages = qtdPages;
	}

	public JasperPrint getJasperPrintDownload() {
		return jasperPrintDownload;
	}

	public void setJasperPrintDownload(JasperPrint jasperPrintDownload) {
		this.jasperPrintDownload = jasperPrintDownload;
	}

	public JasperPrint getJasperPrintDrillDownload() {
		return jasperPrintDrillDownload;
	}

	public void setJasperPrintDrillDownload(JasperPrint jasperPrintDrillDownload) {
		this.jasperPrintDrillDownload = jasperPrintDrillDownload;
	}

}
