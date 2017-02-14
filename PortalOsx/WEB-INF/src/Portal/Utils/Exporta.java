package Portal.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.visent.segurancaAcesso.util.DataUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
/**
 * 
 * Visent Informática.
 * Projeto: Deteccao2007
 *
 * @author Rafael
 * @version 1.0
 * @created 01/03/2007 08:53:50
 */
public class Exporta extends HttpServlet{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int HTML = 1;
	private final int XLS = 2;
	private final int PDF = 3;

	public void doGet (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
    {
		
		HttpSession session = request.getSession(false);
		int tipo = 1;
		
		if(session.getAttribute("exportacao") != null)
			tipo = Integer.parseInt(""+session.getAttribute("exportacao"));
		
		JasperPrint jasper = (JasperPrint)session.getAttribute("jasperPrint");
		switch(tipo){	
			case HTML:
				exportacaoHTML(request,response,jasper);
				break;
			case XLS:
				exportacaoXLS(request,response,jasper);
				break;
			case PDF:
				exportacaoPDF(request,response,jasper);
				break;
		}
    }
	
	/**
	 * Esse metodo irá formatar o arquivo jasper para o formato XLS e irá em seguida disponibilizar o
	 * arquivo para exportação.
	 * @param request
	 * @param response
	 * @param jasper
	 */
	private void exportacaoXLS(HttpServletRequest request, HttpServletResponse response,JasperPrint jasper)
	{
		
		response.setContentType("application/vnd.ms-excel");
		//String dt = formataDataAtual();
		//response.setHeader("Content-Disposition","attachment;filename="+dt+"relatorio.xls");
		 
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		
		JExcelApiExporter exporter = new JExcelApiExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
		
        try {
			exporter.exportReport();
			OutputStream out = response.getOutputStream();
			out.write(xlsReport.toByteArray(), 0, xlsReport.toByteArray().length);
			out.flush();
			out.close();
		} catch (JRException e) {
			e.printStackTrace();
		}catch(IOException io){
			io.printStackTrace();
		}
		
	}
	
	/**
	 * Esse metodo irá formatar o arquivo jasper para o formato PDF e irá em seguida disponibilizar o
	 * arquivo para exportação.
	 * @param request
	 * @param response
	 * @param jasper
	 */
	private void exportacaoPDF(HttpServletRequest request, HttpServletResponse response,JasperPrint jasper)
	{
		response.setContentType("application/pdf");
		//String dt = formataDataAtual();
		//response.setHeader("Content-Disposition","attachment;filename="+dt+"relatorio.pdf");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		JRPdfExporter exporter = new JRPdfExporter();	
		
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,baos);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasper);
        
		try{
			
			response.setHeader("Content-Disposition","attachment;filename=relatorio.pdf");

			
	        exporter.exportReport();
	        OutputStream out = response.getOutputStream();
			out.write(baos.toByteArray(), 0, baos.toByteArray().length);			
			out.flush();
			out.close();
		}catch(JRException jr){
			jr.printStackTrace();
		}catch(IOException io){
			io.printStackTrace();
		}
	}
	
	private void exportacaoHTML(HttpServletRequest request, HttpServletResponse response, JasperPrint jasper) throws IOException
	{
		HttpSession session = request.getSession(false);
		
		
		String path = getServletContext().getRealPath("/");
		String pathIncludes = path+"\\WEB-INF\\";
		String pathImg = path+"\\exportacao\\img"+session.getId()+"\\";
		
		response.setContentType("text/html");
		String dt = formataDataAtual();
		response.setHeader("Content-Disposition","attachment;filename="+dt+"relatorio.zip");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		JRHtmlExporter exporter = new JRHtmlExporter();
		Map imagesMap = new HashMap();	
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
		
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "img\\");
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, pathImg);
		exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);

		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper);
		//exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(0));
		exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, getTexto(pathIncludes+"\\headerExport.txt"));
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<br style=\"page-break-before:always;\">");
		exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, getTexto(pathIncludes+"\\footerExport.txt"));
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.TRUE);
		
		try {
			exporter.exportReport();
		} catch (JRException e) {
			System.out.println("Problema ao exportar relatorio "+e);			
		}
		
		
		
		OutputStream out = response.getOutputStream();
		int TAMANHO_BUFFER = 2048;
		int i, cont;
		byte[] dados = new byte[TAMANHO_BUFFER];
		String arquivos[] = null;
		File dir = null;
		BufferedInputStream origem = null;
		FileInputStream streamDeEntrada = null;
		ZipOutputStream saida = null;
		ZipEntry entry = null;		
		
		try {						
			saida = new ZipOutputStream(out);
			entry = new ZipEntry(dt+"relatorio.html");	
			saida.putNextEntry(entry);
			saida.write(baos.toByteArray(), 0, baos.toByteArray().length);
			baos.close();
			
			dir = new File(pathImg); // Todos os arquivos da pasta onde a classe está
			arquivos = dir.list();			
			for (i = 0; i < arquivos.length; i++) {				
				File arquivo = new File(pathImg+arquivos[i]);
				streamDeEntrada = new FileInputStream(arquivo);
				origem = new BufferedInputStream(streamDeEntrada,TAMANHO_BUFFER);
				entry = new ZipEntry("img\\"+arquivos[i]);				
				saida.putNextEntry(entry);
				while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
					saida.write(dados, 0, cont);
				}
				origem.close();				
			}
			
			saida.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int j = 0; j < arquivos.length; j++) {
			File arquivo = new File(pathImg+arquivos[j]);
			arquivo.delete();
		}
		dir.delete();
		
		out.flush();
		out.close();
		
	}
	
	
	/**
	 * Método para ler o conteudo de um determinado arquivo
	 * @param path caminho do arquivo
	 * @return String o arquivo
	 */
	private String getTexto(String path){
		File arquivo = new File(path);
		StringBuffer buffer = new StringBuffer();
		try {
	        BufferedReader in = new BufferedReader(new FileReader(arquivo));
	        String str;
	        while ((str = in.readLine()) != null) {
	        	buffer.append(str);
	        }
	        in.close();
	    } catch (IOException e) {
	    	
	    }
	    return buffer.toString();
	}
	
	/**
	 * Metodo que irá formata a data/hora atual para ser concatenado ao nome do relatorio
	 * para que caso seja gerado relatorios ao "mesmo tempo" e com o mesmo nome, eles possão ser
	 * diferenciados pelo tempo que eles foram gerados. 
	 * @return data/hora no formato Ex: 02/03/2007 08:30:59 o formato enviado é : 232007083059
	 */
	private String formataDataAtual(){		    
        Calendar calendar = Calendar.getInstance();
        Date hoje = calendar.getTime();
        return DataUtil.getDataFormatada(hoje,"dd-MM-yyyy_HH-mm-ss_");
	}
}
