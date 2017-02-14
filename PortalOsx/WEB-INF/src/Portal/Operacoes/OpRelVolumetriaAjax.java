//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpDataReprocessamentoSalvarAjax.java

package Portal.Operacoes;


import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperPrint;

import org.hibernate.Session;
import org.hibernate.Transaction;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Dao.Util.HibernateUtil;
import Portal.Service.VolumetriaService;


/**
 * @author Bruno
 * 
 * Salva data inserida no formulário dataProcessamento.jsp no servidor
 *
 */
public class OpRelVolumetriaAjax extends OperacaoAbs 
{

	private String listaBilhetador = "";
	//public ArrayList dados = new ArrayList();//variável para armazenar os dados do relatório para caso de exportação. Garante que a exportação mostre os mesmos dados que estão sendo apresentados em caso de atualização da base.
	private JasperPrint jasperPrint = new JasperPrint();
	private JasperPrint jasperPrintDownload = new JasperPrint();
	private HttpSession sessao;
	private Session session;
	VolumetriaService volumetriaService = null;

	static 
	{
	}

	/**
	 * @return 
	 * @exception 
	 * @roseuid 3D19FE860367
	 */
	public OpRelVolumetriaAjax() 
	{
		System.out.println("OpRelVolumetriaAjax()");
		WebContext ctx = WebContextFactory.get();
		setSessao(ctx.getHttpServletRequest().getSession());
		session = HibernateUtil.getSessionFactory().getCurrentSession();

	}

	public OpRelVolumetriaAjax(String teste){

		System.out.println(teste);
	}
	public Boolean salvar(ArrayList listaOrdenada){
		try
		{
			Session sess = HibernateUtil.getSessionFactory().openSession();
			Transaction tr = sess.beginTransaction();
			
			if(volumetriaService == null || volumetriaService.getTipo() == VolumetriaService.ALARME)//se o relatório setado anteriormente for alarme(corba), tem que criar novo objeto para conexão com banco
				volumetriaService = new VolumetriaService(sess);
			else 
				volumetriaService.setSession(session);
			
			volumetriaService.salvar(listaOrdenada);
			tr.commit();
			
			return true;
		}
		catch (org.hibernate.exception.JDBCConnectionException e) {
			// TODO: handle exception
			return false;
		}
	}

	public String getNomeBilhetador(){
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
			
			if(volumetriaService == null || volumetriaService.getTipo() == VolumetriaService.ALARME)//se o relatório setado anteriormente for alarme(corba), tem que criar novo objeto para conexão com banco
				volumetriaService = new VolumetriaService(session);
			else 
				volumetriaService.setSession(session);
			
			String retorno = volumetriaService.getNomeBilhetador("");
			tr.commit();
			return retorno;
		}
		catch (org.hibernate.exception.JDBCConnectionException e) {
			// TODO: handle exception
			return "Erro na comunicação com o banco de dados.";
		}

	}

	public ArrayList getRecursoVolumetria(){
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tr = session.beginTransaction();
			
			if(volumetriaService == null || volumetriaService.getTipo() == VolumetriaService.ALARME)//se o relatório setado anteriormente for alarme(corba), tem que criar novo objeto para conexão com banco
				volumetriaService = new VolumetriaService(session);
			else 
				volumetriaService.setSession(session);
			
			ArrayList retorno = new ArrayList();
			retorno.addAll(volumetriaService.getRecursoVolumetria());
			return retorno;
		}
		catch (org.hibernate.exception.JDBCConnectionException e) {
			// TODO: handle exception
			return null;
		}
	}


	No noTmp = null;


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
			session = HibernateUtil.getSessionFactory().getCurrentSession();

			Transaction tr = session.beginTransaction();

			String rel = getArquivo();

			if(volumetriaService == null || volumetriaService.getTipo() == VolumetriaService.ALARME)//se o relatório setado anteriormente for alarme(corba), tem que criar novo objeto para conexão com banco
				volumetriaService = new VolumetriaService(session);
			
			String relVolumetria = volumetriaService.getRelatorioAcesso( pagina, "", true,rel, getListaBilhetador(),this);
			//Obtendo sessão

			tr.commit();

			getSessao().setAttribute("jasperPrint", getJasperPrintDownload());
			//

			//System.out.println(relVolumetria);


			if(relVolumetria == null )
				return "Relatório Vazio";
			else if(relVolumetria.length() < 1)
				return "Relatório Vazio";

			return relVolumetria;
		}
		catch (org.hibernate.exception.JDBCConnectionException e) {
			// TODO: handle exception
			setJasperPrintDownload(new JasperPrint());
			return "Erro na comunicação com o banco de dados.";
		}
	}

	public String getRelatorioPagina(int pagina){
//		VolumetriaService volumetriaService = new VolumetriaService(session);
		String relVolumetria = "";
		if(volumetriaService != null)
			relVolumetria = volumetriaService.getRelatorioPagina(pagina, this);
		
		if(relVolumetria == null )
			return "Relatório Vazio";
		else if(relVolumetria.length() < 1)
			return "Relatório Vazio";

		return relVolumetria;
	}

	public String getRelatorioAlarme(int pagina){

//		Session session = HibernateUtil.getSessionFactory().openSession();
		String rel = getArquivoAlarme();
		if(volumetriaService == null || volumetriaService.getTipo() == VolumetriaService.RELATORIO)//se o relatório setado anteriormente for relatorio(banco), tem que criar novo objeto para conexão com servidor de alarmes
			volumetriaService = new VolumetriaService();

		String relVolumetria = volumetriaService.getRelatorioAcesso( pagina, "", false,rel, getListaBilhetador(),this);

		//Obtendo sessão
		getSessao().setAttribute("jasperPrint", getJasperPrintDownload());

		if(relVolumetria == null )
			return "Relatório Vazio";
		else if(relVolumetria.length() < 1)
			return "Relatório Vazio";

		return relVolumetria;
	}

	
	
	/**
	 * Finalizar a utilização da página. Aparagar dados da memória na tentativa de poupar recursos.
	 */
	public void finaliza(){
		getSessao().removeAttribute("jasperPrint");
		getSessao().removeAttribute("OpRelVolumetriaAjax");
		volumetriaService = null;
		System.gc();
		
	}

	public String getContexto(){

		return getSessao().getServletContext().getRealPath("/");
	}

	private String getArquivo(){
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		String pathxml = path+"reports/"+"Volumetria.jasper";
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		//File arq =  new File(pathRels);
		//return arq.getParentFile().getParent() + "\\reports\\Volumetria.jasper";	
		return pathxml;

	}

	private String getArquivoAlarme(){
		String path = WebContextFactory.get().getServletContext().getRealPath("/");
		String pathxml = path+"reports/"+"VolumetriaAlarme.jasper";
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		//File arq =  new File(pathRels);
		//return arq.getParentFile().getParent() + "\\reports\\VolumetriaAlarme.jasper";	
		return pathxml;

	}

	public int getQtdPaginas(){

		return getJasperPrint().getPages().size();
	}

	public String getListaBilhetador() {

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

	public void setListaBilhetador(String listaBilhetador) {
		this.listaBilhetador = listaBilhetador.substring(0, listaBilhetador.length()-1);
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

	public JasperPrint getJasperPrintDownload() {
		return jasperPrintDownload;
	}

	public void setJasperPrintDownload(JasperPrint jasperPrintDownload) {
		this.jasperPrintDownload = jasperPrintDownload;
	}


}
