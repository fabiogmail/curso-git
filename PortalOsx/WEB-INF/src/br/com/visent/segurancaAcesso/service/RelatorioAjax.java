package br.com.visent.segurancaAcesso.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpSession;
import uk.ltd.getahead.dwr.WebContextFactory;
import Portal.Utils.QuickSort;
import br.com.visent.segurancaAcesso.action.RelatorioAcessoAction;
import br.com.visent.segurancaAcesso.action.RelatorioUsoAction;
import br.com.visent.segurancaAcesso.domain.Filtro;
import br.com.visent.segurancaAcesso.domain.RegistroAcesso;
import br.com.visent.segurancaAcesso.domain.RegistroUso;
import br.com.visent.segurancaAcesso.jasper.RelatorioAcessoJasper;
import br.com.visent.segurancaAcesso.jasper.RelatorioUsoJasper;

public class RelatorioAjax {

	private RelatorioAcessoJasper relAcesso = new RelatorioAcessoJasper();
	private RelatorioUsoJasper relUso = new RelatorioUsoJasper();
	private final int SEM_DADOS = 0;
	private final int COM_DADOS = 1;
	private String userUso, dtIniUso, dtFimUso; 
	private String userAcesso, dtIniAcesso, dtFimAcesso; 
	
	public RelatorioAjax(){}
	
	/**
	 * metodo que retorna 1 se tiver dados e 0 no caso de nulo os dados
	 * @param usuario
	 * @param dataIni
	 * @param dataFim
	 * @return
	 */
	
	public int verificaRelatorioAcesso(String usuario, String qtdDias, String dataIni, String dataFim, 
			int pg, boolean group)
	{
		int dias = 0;
        //se a quantidade de dias estiver preenchida, ela é subtraida da data atual e a data resultante
		//é passada como periodo inicial
		if(qtdDias != null && qtdDias != "")
		{
			dias  = Integer.parseInt(qtdDias);
			GregorianCalendar gc = new GregorianCalendar();
		    gc.add(GregorianCalendar.DATE, (-1)*dias);
		    int mes = gc.get(GregorianCalendar.MONTH)+1;//adiciona um ao mes por conta de comecar do zero. ex: janeiro = mes 0
		    dataIni = gc.get(GregorianCalendar.YEAR)+"-"+mes+"-"+ gc.get(GregorianCalendar.DAY_OF_MONTH)+" 00:00";		    
		}

		String relatorioAcessoHTML = buscaRelatorioAcesso(usuario,dataIni,dataFim,pg,group);
		if(relatorioAcessoHTML != null)
			return COM_DADOS;
		return SEM_DADOS;
	}
	
	private String buscaRelatorioAcesso(String usuario,String dataIni,String dataFim, int pg, boolean group)
	{
		boolean temUsuario = false;
		boolean temData = false;
		String relatorioAcessoHTML;
		
		this.userAcesso = usuario;
		this.dtIniAcesso= dataIni;
		this.dtFimAcesso = dataFim;
				
		if(usuario != null && usuario.length() > 0){
			temUsuario = true;
		}		
		if((dataIni != null && dataIni.length() > 0) || (dataFim != null && dataFim.length() > 0)){
			temData = true;
		}
		
		
		if(temUsuario && temData)
		{
			relatorioAcessoHTML = getRelatorioAcessoByUserData(usuario,dataIni,dataFim, pg, null, group);
		}
		else 
			if(temUsuario)
			{
				relatorioAcessoHTML = getRelatorioAcessoByUsuario(usuario, pg, null, group);
			}
			else 
				if(temData)
				{
					relatorioAcessoHTML = getRelatorioAcessoByData(dataIni,dataFim,pg, null, group);
				}		
				else
				{
					relatorioAcessoHTML = getRelatorioAcesso(pg, null, group);
				}
		
		return relatorioAcessoHTML;
	}
	
	/**
	 * copia do metodo acima so que com os atributos ja preenchidos
	 * @param usuario
	 * @param dataIni
	 * @param dataFim
	 * @param pg
	 * @return
	 */
	public String buscaRelatorioAcessoPreenchido(int pg, String paramOrdem, boolean group)
	{
		boolean temUsuario = false;
		boolean temData = false;
		String relatorioAcessoHTML;
		
		String usuario = this.userAcesso;
		String dataIni = this.dtIniAcesso;
		String dataFim = this.dtFimAcesso;
		
		if(usuario != null && usuario.length() > 0){
			temUsuario = true;
		}		
		if((dataIni != null && dataIni.length() > 0) || (dataFim != null && dataFim.length() > 0)){
			temData = true;
		}
		
		
		if(temUsuario && temData)
		{
			relatorioAcessoHTML = getRelatorioAcessoByUserData(usuario, dataIni, dataFim, pg, paramOrdem, group);
		}
		else 
			if(temUsuario)
			{
				relatorioAcessoHTML = getRelatorioAcessoByUsuario(usuario, pg, paramOrdem, group);
			}
			else 
				if(temData)
				{
					relatorioAcessoHTML = getRelatorioAcessoByData(dataIni, dataFim, pg, paramOrdem, group);
				}		
				else
				{
					relatorioAcessoHTML = getRelatorioAcesso(pg, paramOrdem, group);
				}
		
		return relatorioAcessoHTML;
	}
	
	
	public int verificaRelatorioUso(String usuario,String qtdDias, String dataIni,String dataFim, 
			int pg, boolean group)
	{
		int dias = 0;
        //se a quantidade de dias estiver preenchida, ela é subtraida da data atual e a data resultante
		//é passada como periodo inicial
		if(qtdDias != null && qtdDias != "")
		{
			dias  = Integer.parseInt(qtdDias);
			GregorianCalendar gc = new GregorianCalendar();
		    gc.add(GregorianCalendar.DATE, (-1)*dias);
		    int mes = gc.get(GregorianCalendar.MONTH)+1;//adiciona um ao mes por conta de comecar do zero. ex: janeiro = mes 0
		    dataIni = gc.get(GregorianCalendar.YEAR)+"-"+mes+"-"+ gc.get(GregorianCalendar.DAY_OF_MONTH)+" 00:00";		    
		}
		
		String relatorioUsoHTML = buscaRelatorioUso(usuario,dataIni,dataFim,pg,group);
		if(relatorioUsoHTML != null)
			return COM_DADOS;
		return SEM_DADOS;
	}
	/**
	 * 
	 * @param usuario
	 * @param dataIni
	 * @param dataFim
	 * @return
	 */
	private String buscaRelatorioUso(String usuario,String dataIni,String dataFim, int pg, boolean group){
		boolean temUsuario = false;
		boolean temData = false;
		String relatorioUsoHTML;
		
		this.userUso = usuario;
		this.dtIniUso = dataIni;
		this.dtFimUso = dataFim;
		
		if(usuario != null && usuario.length() > 0){
			temUsuario = true;
		}		
		if((dataIni != null && dataIni.length() > 0) || (dataFim != null && dataFim.length() > 0)){
			temData = true;
		}
		
		
		if(temUsuario && temData){
			relatorioUsoHTML = getRelatorioUsoByUserData(usuario, dataIni, dataFim, pg, null, group);
		}
		else
			if(temUsuario)
			{
				relatorioUsoHTML = getRelatorioUsoByUsuario(usuario, pg, null, group);
			}
			else
				if(temData)
				{
					relatorioUsoHTML = getRelatorioUsoByData(dataIni, dataFim, pg, null, group);
				}		
				else
				{
					relatorioUsoHTML = getRelatorioUso(pg, null, group);
				}
		return relatorioUsoHTML;
	}
	/**
	 * copia do metodo acima so que com os parametros preenchidos no de cima e so acessados no de baixo
	 * @param pg
	 * @return
	 */
	public String buscaRelatorioUsoPreenchido(int pg, String paramOrdem, boolean group)
	{
		boolean temUsuario = false;
		boolean temData = false;
		String relatorioUsoHTML;
		//inicializando variaveis ja preenchidas no metodo buscaRelatorioUso
		String usuario = this.userUso;
		String dataIni = this.dtIniUso;
		String dataFim = this.dtFimUso;
					
		
		if(usuario != null && usuario.length() > 0){
			temUsuario = true;
		}		
		if((dataIni != null && dataIni.length() > 0) || (dataFim != null && dataFim.length() > 0)){
			temData = true;
		}
		
		
		if(temUsuario && temData){
			relatorioUsoHTML = getRelatorioUsoByUserData(usuario, dataIni, dataFim, pg, paramOrdem, group);
		}
		else
			if(temUsuario)
			{
				relatorioUsoHTML = getRelatorioUsoByUsuario(usuario, pg, paramOrdem, group);
			}
			else
 				if(temData)
				{
					relatorioUsoHTML = getRelatorioUsoByData(dataIni, dataFim, pg, paramOrdem, group);
				}		
				else
				{
					relatorioUsoHTML = getRelatorioUso(pg, paramOrdem, group);
				}
		return relatorioUsoHTML;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getRelatorioAcesso(int pg, String paramOrdem, boolean group){
		RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.listar(paramOrdem));
		relAcesso.setDadosRelatorioAcesso(relatorio,group);
		return relAcesso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	private String getRelatorioAcessoByUsuario(String usuario,int pg, String paramOrdem, boolean group){
		RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.buscarByUsuario(usuario,paramOrdem));
		relAcesso.setDadosRelatorioAcesso(relatorio,group);
		return relAcesso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private String getRelatorioAcessoByData(String dataInicio,String dataFim, int pg, 
			String paramOrdem, boolean group){
		RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.buscarByData(dataInicio, dataFim, paramOrdem));
		relAcesso.setDadosRelatorioAcesso(relatorio,group);
		return relAcesso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param usuario
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private String getRelatorioAcessoByUserData(String usuario,String dataInicio,String dataFim, int pg, 
			String paramOrdem, boolean group){
		RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.buscarByUserData(usuario,dataInicio, dataFim, paramOrdem));
		relAcesso.setDadosRelatorioAcesso(relatorio,group);
		return relAcesso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public RegistroAcesso getRelatorioAcesso_ById(int id, String paramOrdem, String[] group){
		RelatorioAcessoAction action = new RelatorioAcessoAction();
		RegistroAcesso regAcesso = action.buscarById(id, paramOrdem);
		return regAcesso;
	}
	
	/**
	 * 
	 * @return
	 */
	public String executarRelatorioAcesso(int pg)	{
		return relAcesso.relatorioHTML(pg);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @return
	 */
	private String getRelatorioUso(int pg, String paramOrdem, boolean group){
		RelatorioUsoAction action = new RelatorioUsoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.listar(paramOrdem));
		relUso.setDadosRelatorioAcesso(relatorio,group);
		return relUso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @return
	 */ 
	public String executarRelatorioUso(int pg)	{
		return relUso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	private String getRelatorioUsoByUsuario(String usuario, int pg, String paramOrdem, boolean group){
		RelatorioUsoAction action = new RelatorioUsoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.buscarByUsuario(usuario, paramOrdem));
		relUso.setDadosRelatorioAcesso(relatorio,group);
		return relUso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private String getRelatorioUsoByData(String dataInicio,String dataFim, int pg, 
			String paramOrdem, boolean group){
		RelatorioUsoAction action = new RelatorioUsoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.buscarByData(dataInicio, dataFim, paramOrdem));
		relUso.setDadosRelatorioAcesso(relatorio, group);
		return relUso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param usuario
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	private String getRelatorioUsoByUserData(String usuario, String dataInicio, String dataFim, int pg, 
			String paramOrdem, boolean group){
		RelatorioUsoAction action = new RelatorioUsoAction();
		ArrayList relatorio = new ArrayList();
		relatorio.addAll(action.buscarByUserData(usuario, dataInicio, dataFim, paramOrdem));
		relUso.setDadosRelatorioAcesso(relatorio, group);
		return relUso.relatorioHTML(pg);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public RegistroUso getRelatorioUso_ById(int id, String paramOrdem){
		RelatorioUsoAction action = new RelatorioUsoAction();
		RegistroUso regUso = action.buscarById(id, paramOrdem);
		return regUso;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ArrayList getListaDeFiltros(int id){
		RelatorioUsoAction action = new RelatorioUsoAction();
		ArrayList lista = (ArrayList)action.buscarListaDeFiltros(id);
		ArrayList listaPronta = new ArrayList();
		//ordenação dos valores dos filtros
		for (int i = 0; i < lista.size(); i++)
		{
			Filtro f = (Filtro)lista.get(i);
			f.setValoresFiltro(ordenaValores(f.getValoresFiltro()));
		}
		return lista;
	}
	
	private String ordenaValores(String valoresFiltro)
	{
		String array[] = valoresFiltro.split(";");
		QuickSort.metodoQuickSort(array);
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < array.length; i++)
		{
			result.append(array[i]+";");
		}
		
		return result.toString().substring(0,result.length()-1);
	}
	

	
	/**
	 * método que chama e o jasper e retorna o numero de paginas do relatorio
	 * @return
	 */
	public int getNumPaginasRelUso()
	{
		return relUso.getNumeroPaginasRelUso();
	}
	
	public int getNumPaginasRelAcesso()
	{
		return relAcesso.getNumeroPaginasRelAcesso();
	}
	
	public void exportarUso(int exportacao){
		HttpSession sess = WebContextFactory.get().getSession();
		sess.setAttribute("jasperPrint",relUso.getJasperPrint());
		sess.setAttribute("exportacao",exportacao+"");
	}
	
	public void exportarAcesso(int exportacao){
		HttpSession sess = WebContextFactory.get().getSession();
		sess.setAttribute("jasperPrint",relAcesso.getJasperPrint());
		sess.setAttribute("exportacao",exportacao+"");
	}
	
	public boolean isConsolidadoAcesso(){
		return relAcesso.isConsolidado();
	}
	
	public boolean isConsolidadoUso(){
		return relUso.isConsolidado();
	}
	
}
