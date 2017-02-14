package Portal.Operacoes;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import Portal.Dao.Util.HibernateUtil;
import Portal.Service.AlarmesPlataformaService;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import net.sf.jasperreports.engine.JasperPrint;

public class OpVisualizaAlarmesPlataformaAjax extends OperacaoAbs{
	
	private JasperPrint jasperPrint = new JasperPrint();
	private HttpSession sessao;

	static 
	{
	}
	
	public OpVisualizaAlarmesPlataformaAjax() 
	{
		System.out.println("OpVisualizaAlarmesPlataformaAjax()");
		WebContext ctx = WebContextFactory.get();
		setSessao(ctx.getHttpServletRequest().getSession());
	}
	
	public String getRelatorioAlarme(int pagina){
	//	return "teste";
		
//		Session session = HibernateUtil.getSessionFactory().openSession();
		String rel = getArquivoAlarme();

		//VolumetriaService volumetriaService = new VolumetriaService(session);
		AlarmesPlataformaService alarmesPlataformaService = new AlarmesPlataformaService();	
		String relVolumetria = alarmesPlataformaService.getRelatorioAcesso( pagina, "", false,rel,this);
		
		//Obtendo sessão
		getSessao().setAttribute("jasperPrint", getJasperPrint());
		//

		//System.out.println(relVolumetria);
		
		if(relVolumetria == null )
			return "Relatório Vazio";
		else if(relVolumetria.length() < 1)
			return "Relatório Vazio";
		
		return relVolumetria;
	}
	
	public String getContexto(){
		
			return getSessao().getServletContext().getRealPath("/");
	}
	
	private String getArquivoAlarme(){
		//String pathRels =  this.getClass().getResource("/").getPath(); //event.getServletContext().getRealPath("WEB-INF/classes/Portal/Dao/Relatorios/");
		String pathAlternativo = WebContextFactory.get().getServletContext().getRealPath("/");	
		//System.out.println("Atual: " + pathRels);
		//System.out.println("Novo: " + pathAlternativo);	
		File arq =  new File(pathAlternativo);
		return arq.getPath()+ "\\reports\\AlarmesPlataforma.jasper";	
		
	}
	
	public String getListaBilhetador() {
		
//		String aux = listaBilhetador;
//		listaBilhetador = "";
		
		return null; //aux;
	}
	
	public int getQtdPaginas(){
		
		return getJasperPrint().getPages().size();
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



}
