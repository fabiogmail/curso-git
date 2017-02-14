package br.com.visent.segurancaAcesso.jasper;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import uk.ltd.getahead.dwr.WebContextFactory;

public class RelatorioAcessoJasper {

	/** Arquivo com a estrutura do relatorio de RegistroAcesso no jasper */
	public static final String ARQUIVO = "Registro_Acesso.jasper";
	public static final String ARQUIVO_CONSOLIDADO = "Registro_Acesso_C.jasper";
	
	/** Linhas do relatório, lista de objetos Log */
	private ArrayList dados;
	/** Parâmetros passados para o relatorio */
	private HashMap parametros = new HashMap();
	/** Relatorio conpilado */ 
	private JasperPrint jasperPrint = null;
	private final int BRASILTELECOM  = 1;
	private static int quantPag = 0;
	private boolean consolidado;
	
	public RelatorioAcessoJasper(){}
	
	/**
	 * Inicializa design do relatório e variaveis
	 * @param dados
	 */
	public void setDadosRelatorioAcesso(ArrayList dados, boolean consolidado){
		this.dados = dados;
		this.consolidado = consolidado;
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		//String path = "D:/usr/osx/cdrview/PortalOsx/";
		if(consolidado){
			compile(path+"reports/"+ARQUIVO_CONSOLIDADO);
		}else{
			compile(path+"reports/"+ARQUIVO);
		}
				
	}
	
	/**
	 * Metódo para compilar o relatorio
	 */
	private void compile(String arquivo){				
		JRDataSource data = null;
		if(dados.size() > 0){
			data = new JRBeanCollectionDataSource(dados);
		}else{
			data = new JREmptyDataSource(1);
		}
		jasperPrint = new JasperPrint();
		parametros.put("USUARIO","USUARIO");
		parametros.put("PERFIL","PERFIL");
		parametros.put("MODULO","MODULO");
		parametros.put("EVENTO","EVENTO");
		parametros.put("DTREGISTRO","DATA/HORA");
		parametros.put("IP","IP");
		parametros.put("IMGVISENT",carregaVisent());
		parametros.put("IMGCLIENTE",carregaLog(BRASILTELECOM));
		try {
			jasperPrint = JasperFillManager.fillReport(arquivo, parametros, data);
			quantPag = jasperPrint.getPages().size();
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
	private Image carregaLog(int cliente){
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		//String path = "D:/usr/osx/cdrview/PortalOsx/";
		path += "imagens\\";
		ImageIcon imagem = null;		
        
		switch(cliente){
			
			case BRASILTELECOM:
				imagem = new ImageIcon(path+"logo_clientebrt.gif");
				break;
				
		}		
		return imagem.getImage();
	}
	
	/**
	 * Carregar a logo da visent
	 * @return Image
	 */
	private Image carregaVisent(){
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		//String path = "D:/usr/osx/cdrview/PortalOsx/";
		path += "imagens\\";
		ImageIcon imagem = null;		
		imagem = new ImageIcon(path+"logo_cliente.gif");
		return imagem.getImage();
	}
	
	
	/**
	 * Exportar relatorio para HTML
	 * @return Html do relatorio
	 */
	public String relatorioHTML(int pg){
		
		HttpSession sess = WebContextFactory.get().getSession();
		sess.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);		
		JRHtmlExporter exporter = new JRHtmlExporter();
		StringBuffer sbuffer = new StringBuffer();		
		Map imagesMap = new HashMap();	
		sess.setAttribute("IMAGES_MAP", imagesMap);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/PortalOsx/servlets/image?rnd="+Math.random()+"&image=");
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		if(pg == -1){
			exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, new Integer(0));
			exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, new Integer(jasperPrint.getPages().size()-1));
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
	
	public boolean isConsolidado(){
		return this.consolidado;
	}
	
}
