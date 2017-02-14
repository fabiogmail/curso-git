package Portal.Jasper;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import Portal.Operacoes.OpRelSequenciaAjax;
import Portal.Operacoes.OpRelVolumetriaAjax;
import Portal.Operacoes.OpVisualizaAlarmesPlataformaAjax;

public class RelatorioAcessoJasper {

	/** Arquivo com a estrutura do relatorio de RegistroAcesso no jasper */
	public static final String ARQUIVO = "Volumetria.jasper";
	public static final String ARQUIVO_CONSOLIDADO = "Registro_Acesso_C.jasper";
	
	/** Linhas do relatório, lista de objetos Log */
	private ArrayList dados;
	/** Parâmetros passados para o relatorio */
	private HashMap parametros = new HashMap();
	/** Relatorio conpilado */ 
	private JasperPrint jasperPrint = null;
	private JasperPrint jasperPrintDownload = null;
	//private final int BRASILTELECOM  = 1;
	private static int quantPag = 0;
	private boolean consolidado;
	private OpRelVolumetriaAjax volumetriaAjax;
	private OpRelSequenciaAjax sequenciaAjax;
	private OpVisualizaAlarmesPlataformaAjax visualizaAlarmesPlataformaAjax;
	
	
	
	
	public RelatorioAcessoJasper(){}
	
	/**
	 * Inicializa design do relatório e variaveis
	 * @param dados
	 * @param jasperPrint2 
	 * @param sessaoHTTP 
	 */
	public void setDadosRelatorioAcesso(ArrayList dados, boolean consolidado, String path, OpRelVolumetriaAjax opRelVolumetriaAjax){
		this.dados = dados;
		//this.consolidado = consolidado;
		setVolumetriaAjax(opRelVolumetriaAjax);
		
		compileVolumetria(path);
		
	}
	

	public void setDadosRelatorioAcesso(ArrayList dados, boolean consolidado, String path, OpRelSequenciaAjax opRelSequenciaAjax){
		this.dados = dados;
		this.consolidado = consolidado;
		setSequenciaAjax(opRelSequenciaAjax);
		
		compileSequencia(path);
		
	}
	
	public void setDadosRelatorioAcesso(ArrayList dados, boolean consolidado, String path, OpVisualizaAlarmesPlataformaAjax opVisualizaAlarmesPlataformaAjax){
		this.dados = dados;
		this.consolidado = consolidado;
		setVisualizaAlarmesPlataformaAjax(opVisualizaAlarmesPlataformaAjax);
		
		compilePlataforma(path);
		
	}
	
	
	/**
	 * Metódo para compilar o relatorio
	 */
	private void compileVolumetria(String arquivo){

		JRDataSource data = null;
		JRDataSource dataDownload = null;
		if(dados.size() > 0){
			data = new JRBeanCollectionDataSource(dados);
			dataDownload = new JRBeanCollectionDataSource(dados);
		}else{
			data = new JREmptyDataSource(1);
		}
		jasperPrint = new JasperPrint();
		jasperPrintDownload = new JasperPrint();
		
		//setJp(new JasperPrint());
		try {
//			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			String caminhoImagem =getVolumetriaAjax().getSessao().getServletContext().getRealPath("/")+"imagens\\visent.png";
			parametros.put("IMAGEM_TOPO", caminhoImagem);
			parametros.put( "IS_IGNORE_PAGINATION", true );    
			jasperPrintDownload = JasperFillManager.fillReport(getArquivoJasperDownload(arquivo), parametros, dataDownload);
			getVolumetriaAjax().setJasperPrint(jasperPrint);
			getVolumetriaAjax().setJasperPrintDownload(jasperPrintDownload);
			//quantPag = jasperPrint.getPages().size();
			quantPag = getVolumetriaAjax().getJasperPrint().getPages().size();
		} catch (JRException e) {
			e.printStackTrace();

		}
		
		
	}
	
	private void compileSequencia(String arquivo){

		JRDataSource data = null;
		JRDataSource dataDownload = null;
		if(dados.size() > 0){
			data = new JRBeanCollectionDataSource(dados);
			dataDownload = new JRBeanCollectionDataSource(dados);
		}else{
			data = new JREmptyDataSource(1);
		}
		jasperPrint = new JasperPrint();	
		jasperPrintDownload = new JasperPrint();
		//setJp(new JasperPrint());
		try {
//			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			String caminhoImagem =getSequenciaAjax().getSessao().getServletContext().getRealPath("/")+"imagens\\visent.png";
			parametros.put("IMAGEM_TOPO", caminhoImagem);
			jasperPrintDownload = JasperFillManager.fillReport(getArquivoJasperDownload(arquivo), parametros, dataDownload);
			getSequenciaAjax().setJasperPrintDrill(jasperPrint);
			getSequenciaAjax().setJasperPrintDownload(jasperPrintDownload);
			
			//quantPag = jasperPrint.getPages().size();
			quantPag = getSequenciaAjax().getJasperPrint().getPages().size();
		} catch (JRException e) {
			e.printStackTrace();

		}
		
		
	}
	private void compilePlataforma(String arquivo){

		JRDataSource data = null;
		if(dados.size() > 0){
			data = new JRBeanCollectionDataSource(dados);
		}else{
			data = new JREmptyDataSource(1);
		}
		jasperPrint = new JasperPrint();	
		//setJp(new JasperPrint());
		try {
//			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			getVisualizaAlarmesPlataformaAjax().setJasperPrint(jasperPrint);
			//quantPag = jasperPrint.getPages().size();
			quantPag = getVisualizaAlarmesPlataformaAjax().getJasperPrint().getPages().size();
		} catch (JRException e) {
			e.printStackTrace();

		}
		
		
	}
	
	public int getNumeroPaginasRelAcesso()
	{
		return quantPag;
	}
	
	/**
	 * Metodo para carregar o Logo do cliente que irá aparecer no relatorio.
	 * @param cliente
	 * @return a imagem de Logo
	 */
//	private Image carregaLog(int cliente){
//		String path = WebContextFactory.get().getServletContext().getRealPath("/");
//		//String path = "D:/usr/osx/cdrview/PortalOsx/";
//		path += "imagens\\";
//		ImageIcon imagem = null;		
//        
//		switch(cliente){
//			
//			case BRASILTELECOM:
//				imagem = new ImageIcon(path+"logo_clientebrt.gif");
//				break;
//				
//		}		
//		return imagem.getImage();
//	}
	
	/**
	 * Carregar a logo da visent
	 * @return Image
	 */
//	private Image carregaVisent(){
//		String path = WebContextFactory.get().getServletContext().getRealPath("/");
//		//String path = "D:/usr/osx/cdrview/PortalOsx/";
//		path += "imagens\\";
//		ImageIcon imagem = null;		
//		imagem = new ImageIcon(path+"logo_cliente.gif");
//		return imagem.getImage();
//	}
//	
	
	/**
	 * Exportar relatorio para HTML
	 * @param opRelVolumetriaAjax.getJasperPrint() 
	 * @return Html do relatorio
	 */
	public String relatorioHTML(int pg, OpRelVolumetriaAjax opRelVolumetriaAjax){
		
//		HttpSession sess = WebContextFactory.get().getSession();
		//HttpSession sess = HibernateUtil.getSessionFactory().openSession();
		//sess = 
		//sess.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);		
		JRHtmlExporter exporter = new JRHtmlExporter();
		StringBuffer sbuffer = new StringBuffer();		
		//Map imagesMap = new HashMap();	
		//sess.setAttribute("IMAGES_MAP", imagesMap);
//		exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
//		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/PortalOsx/servlets/image?rnd="+Math.random()+"&image=");
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, opRelVolumetriaAjax.getJasperPrint());
		if(pg == -1){
			exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, new Integer(0));
			exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, new Integer(opRelVolumetriaAjax.getJasperPrint().getPages().size()-1));
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<br style=\"page-break-before:always;\">");
		}else{
			exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pg));
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		}		
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");		
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
		
		try {
			if(dados.size() > 0){
				exporter.exportReport();
			}else{
				return null;//sem dados
			}
			
		} catch (JRException e) {
			System.out.println("Problema ao exportar relatorio "+e);
			//e.printStackTrace();
		}
	
		return sbuffer.toString();
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}
	
	public String relatorioHTMLAlarmePlataforma(int pg, OpVisualizaAlarmesPlataformaAjax opVisualizaAlarmesPlataformaAjax){
		
//		HttpSession sess = WebContextFactory.get().getSession();
		//HttpSession sess = HibernateUtil.getSessionFactory().openSession();
		//sess = 
		//sess.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);		
		JRHtmlExporter exporter = new JRHtmlExporter();
		StringBuffer sbuffer = new StringBuffer();		
		//Map imagesMap = new HashMap();	
		//sess.setAttribute("IMAGES_MAP", imagesMap);
//		exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
//		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/PortalOsx/servlets/image?rnd="+Math.random()+"&image=");
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, opVisualizaAlarmesPlataformaAjax.getJasperPrint());
		if(pg == -1){
			exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, new Integer(0));
			exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, new Integer(opVisualizaAlarmesPlataformaAjax.getJasperPrint().getPages().size()-1));
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<br style=\"page-break-before:always;\">");
		}else{
			exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pg));
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		}		
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");		
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
		
		try {
			if(dados.size() > 0){
				exporter.exportReport();
			}else{
				return null;//sem dados
			}
			
		} catch (JRException e) {
			System.out.println("Problema ao exportar relatorio "+e);
			//e.printStackTrace();
		}
	
		return sbuffer.toString();
	}
	
	private String getArquivoJasperDownload(String arquivo){
		StringBuffer s = new StringBuffer(arquivo);
		s.insert(s.indexOf(".jasper"),"_download");
		
		return s.toString();
	}

	
	/**
	 * @return objeto jasper print gerado pela visualização do relatório
	 */
	public JasperPrint getJP(){
		return jasperPrint;
	}
	
	public boolean isConsolidado(){
		return this.consolidado;
	}

	public OpRelVolumetriaAjax getVolumetriaAjax() {
		return volumetriaAjax;
	}

	public void setVolumetriaAjax(OpRelVolumetriaAjax volumetriaAjax) {
		this.volumetriaAjax = volumetriaAjax;
	}

	public OpRelSequenciaAjax getSequenciaAjax() {
		return sequenciaAjax;
	}

	public void setSequenciaAjax(OpRelSequenciaAjax sequenciaAjax) {
		this.sequenciaAjax = sequenciaAjax;
	}

	public OpVisualizaAlarmesPlataformaAjax getVisualizaAlarmesPlataformaAjax() {
		return visualizaAlarmesPlataformaAjax;
	}

	public void setVisualizaAlarmesPlataformaAjax(
			OpVisualizaAlarmesPlataformaAjax visualizaAlarmesPlataformaAjax) {
		this.visualizaAlarmesPlataformaAjax = visualizaAlarmesPlataformaAjax;
	}

	public JasperPrint getJasperPrintDownload() {
		return jasperPrintDownload;
	}

	public void setJasperPrintDownload(JasperPrint jasperPrintDownload) {
		this.jasperPrintDownload = jasperPrintDownload;
	}
	
	
}
