package Portal.Jasper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.view.JasperViewer;
import uk.ltd.getahead.dwr.WebContextFactory;
import Portal.Operacoes.OpRelSequenciaAjax;

public class RelatorioAcessoJasperSequencia {
	
	public static final String ARQUIVO = "Volumetria.jasper";
	public static final String ARQUIVO_CONSOLIDADO = "Registro_Acesso_C.jasper";
	
	/** Linhas do relatório, lista de objetos Log */
	private ArrayList dados;
	/** Parâmetros passados para o relatorio */
	private HashMap parametros = new HashMap();
	/** Relatorio conpilado */ 
	private JasperPrint jasperPrint = null;
	//private final int BRASILTELECOM  = 1;
	private static int quantPag = 0;
	private boolean consolidado;
	private OpRelSequenciaAjax sequenciaAjax;
	
	public void setDadosRelatorioAcesso(ArrayList dados, boolean consolidado, String path, OpRelSequenciaAjax opRelSequenciaAjax){
		this.dados = dados;
		this.consolidado = consolidado;
		setSequenciaAjax(opRelSequenciaAjax);
		
		compile(path);
		
	}
	
	private void compile(String arquivo){

		JRDataSource data = null;
		JRDataSource dataDownload = null;
		if(dados.size() > 0){
			data = new JRBeanCollectionDataSource(dados);
			ArrayList temp = new ArrayList();
			temp.addAll(dados);
			dataDownload = new JRBeanCollectionDataSource(temp);
		}else{
			data = new JREmptyDataSource(1);
			dataDownload = new JREmptyDataSource(1);
		}
//		jasperPrint = new JasperPrint();	
		//setJp(new JasperPrint());
		
		try {
//			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			
			if(getSequenciaAjax().isDrill()){
				getSequenciaAjax().setJasperPrintDrill(JasperFillManager.fillReport(arquivo, parametros, data));
				
				String caminhoImagem =getSequenciaAjax().getSessao().getServletContext().getRealPath("/")+"imagens\\visent.png";
				parametros.put("IMAGEM_TOPO", caminhoImagem);	
				parametros.put( "IS_IGNORE_PAGINATION", true );
				getSequenciaAjax().setJasperPrintDrillDownload(JasperFillManager.fillReport(getArquivoJasperDownload(arquivo), parametros, dataDownload));
				
				quantPag = getSequenciaAjax().getJasperPrintDrill().getPages().size();
				
				
			}
			else{
				
				String caminhoImagem =getSequenciaAjax().getSessao().getServletContext().getRealPath("/")+"imagens\\reldetalha.png";
				parametros.put("IMAGEM", caminhoImagem);
				getSequenciaAjax().setJasperPrint(JasperFillManager.fillReport(arquivo, parametros, data));
				
				String caminhoImagemDownload =getSequenciaAjax().getSessao().getServletContext().getRealPath("/")+"imagens\\visent.png";
				parametros.put("IMAGEM_TOPO", caminhoImagemDownload);
				parametros.put( "IS_IGNORE_PAGINATION", true );
				getSequenciaAjax().setJasperPrintDownload(JasperFillManager.fillReport(getArquivoJasperDownload(arquivo), parametros, dataDownload));
				
				
				quantPag = getSequenciaAjax().getJasperPrint().getPages().size();
			//quantPag = jasperPrint.getPages().size();
			}
			
			getSequenciaAjax().setQtdPages(quantPag);
			
		} catch (JRException e) {
			e.printStackTrace();

		}
		
		
	}
	
	public void setDadosRelatorioAcessoPdf(JasperPrint jasper){
		
		relatorioPdf(jasper);
		
		
	}
	
	
	public void relatorioPdf(JasperPrint jasper){  
		try {
			JasperExportManager.exportReportToPdfFile(jasper,"C:\\usr\\osx\\cdrview\\PortalOsx\\reports\\Relatorio.pdf");
//			JasperExportManager.ex
			JasperViewer.viewReport(jasper);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public int getNumeroPaginasRelAcesso()
	{
		return quantPag;
	}
	
	
public String relatorioHTML(int pg, OpRelSequenciaAjax opRelSequenciaAjax){
	
	JasperPrint jasper = null;
	
	if(opRelSequenciaAjax.isDrill()){
		jasper = opRelSequenciaAjax.getJasperPrintDrill();
	}
	else
		jasper = opRelSequenciaAjax.getJasperPrint();
		
		HttpSession sess = WebContextFactory.get().getSession();
//		HttpSession sess = (HttpSession) HibernateUtil.getSessionFactory().openSession();
		//sess = 
		sess.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasper);		
		JRHtmlExporter exporter = new JRHtmlExporter();
		StringBuffer sbuffer = new StringBuffer();		
		Map imagesMap = new HashMap();	
		sess.setAttribute("IMAGES_MAP", imagesMap);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/PortalOsx/servlets/image?rnd="+Math.random()+"&image=");
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
		if(pg == -1){
			exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, new Integer(0));
			exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, new Integer(jasper.getPages().size()-1));
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

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}
	
	public JasperPrint getJP(){
		return jasperPrint;
	}
	
	public boolean isConsolidado(){
		return this.consolidado;
	}

	public OpRelSequenciaAjax getSequenciaAjax() {
		return sequenciaAjax;
	}

	public void setSequenciaAjax(OpRelSequenciaAjax sequenciaAjax) {
		this.sequenciaAjax = sequenciaAjax;
	}
	

}
