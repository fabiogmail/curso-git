package Portal.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;

import src.br.com.visent.dbdeteccao.corba.CorbaException;
import src.br.com.visent.dbdeteccao.corba.CorbaUtil;
import src.br.com.visent.dbdeteccao.corba.Format;
import src.br.com.visent.dbdeteccao.dao.LogDAO;
import src.br.com.visent.dbdeteccao.dto.LogSys;
import src.br.com.visent.dbdeteccao.util.FiltrosLog;


import net.sf.jasperreports.engine.JasperPrint;

import Portal.Dao.Hibernate.Conf.RelatoriosSequenciaDAO;
import Portal.Dao.Hibernate.Conf.RelatoriosVolumetriaDAO;
import Portal.Dto.ArquivosFaltantes;
import Portal.Dto.Bilhetador;
import Portal.Dto.NumFaltantes;
import Portal.Dto.NumFaltantesDrill;
import Portal.Dto.NumRepetidos;
import Portal.Dto.NumRepetidosDrill;
import Portal.Dto.RelatorioSequencia;
import Portal.Dto.RelatorioVolumetria;
import Portal.Dto.RelatorioVolumetriaDomingo;
import Portal.Dto.RelatorioVolumetriaQuarta;
import Portal.Dto.RelatorioVolumetriaQuinta;
import Portal.Dto.RelatorioVolumetriaSabado;
import Portal.Dto.RelatorioVolumetriaSegunda;
import Portal.Dto.RelatorioVolumetriaSexta;
import Portal.Dto.RelatorioVolumetriaTerca;
import Portal.Jasper.RelatorioAcessoJasper;
import Portal.Jasper.RelatorioAcessoJasperSequencia;
import Portal.Operacoes.OpRelSequenciaAjax;
import Portal.Operacoes.OpRelVolumetriaAjax;
import Portal.Utils.DBDeteccaoProperties;

public class SequenciaService {
	
	private NumFaltantes numfaltantes;
	private ArquivosFaltantes arqFaltantes;
	private NumRepetidos numRepetidos;
	private Session session;
	private Bilhetador bilhetador;
	private RelatorioSequencia relatorioSequencia;
	private RelatoriosSequenciaDAO relSequencia;
	private ArrayList listaCompleta;
	private RelatorioAcessoJasper relAcesso = new RelatorioAcessoJasper();
	private RelatorioAcessoJasperSequencia relAcessoSequencia = new RelatorioAcessoJasperSequencia();
	private RelatorioAcessoJasperSequencia relAcessoSequenciaDrill = new RelatorioAcessoJasperSequencia();
	private NumRepetidosDrill repetidosDrill = new NumRepetidosDrill();
	private NumFaltantesDrill faltantesDrill = new NumFaltantesDrill();
	
	public static final int RELATORIO = 0;
	public static final int ALARME = 1;
	
	private int tipo = -1;
	
	/**
	 * Objeto de comunicação com o servidor para obtenção dos dados.
	 */
	private LogDAO relAlarmeDAO;
	
	public SequenciaService(Session session){
		this.session = session;	
		setListaCompleta( new ArrayList());
		setTipo(RELATORIO);

	}
	
	public SequenciaService(){
		
		setListaCompleta( new ArrayList());
		
		inicializarCorba();
		setTipo(ALARME);
	}
	

public ArrayList getRelSequencia (String bilhetadores, String data){
		
		setRelSequencia(new RelatoriosSequenciaDAO(session));
		ArrayList dao = new ArrayList();
		String queryBilhetadores = configuraWhere(bilhetadores,data);
		
		 dao.addAll(relSequencia.getNumerosRepetidosArq(queryBilhetadores));	
		 criaObjetosSequencia(dao,"numeros repetidos");
		 dao.clear();
		 
		 dao.addAll(relSequencia.getNumerosFaltantesArq(queryBilhetadores));
		 criaObjetosSequencia(dao,"numeros faltantes");
		 ////
		 
		 Collections.sort (listaCompleta, new Comparator() {  
	            public int compare(Object o1, Object o2) {  
	                RelatorioSequencia b1 = (RelatorioSequencia) o1;  
	                RelatorioSequencia b2 = (RelatorioSequencia) o2;  
	                return b1.getBilhetador().compareToIgnoreCase(b2.getBilhetador()) < 0 ? -1 : (b1.getBilhetador().compareTo(b2.getBilhetador()) > 0 ? +1 : 0);  
	            }  
	        });  
		 
		 
		 ////
		 if(listaCompleta != null){
			 listaCompleta.trimToSize();
		 }
		 return listaCompleta;
	}

public ArrayList getRelSequenciaRepetido (String bilhetadores, String data){
	
	setRelSequencia(new RelatoriosSequenciaDAO(session));
	ArrayList dao = new ArrayList();
	String queryBilhetadores = configuraWhere(bilhetadores,data);
	
	dao.addAll(relSequencia.getNumerosRepetidos(queryBilhetadores));
	criaObjetosSequenciaDrill(dao,"numeros repetidos");
	
	return listaCompleta;
	
}

public ArrayList getRelSequenciaFaltante (String bilhetadores, String data){
	
	setRelSequencia(new RelatoriosSequenciaDAO(session));
	ArrayList dao = new ArrayList();
	String queryBilhetadores = configuraWhere(bilhetadores,data);
	
	dao.addAll(relSequencia.getNumerosFaltantes(queryBilhetadores));
	criaObjetosSequenciaDrill(dao,"numeros faltantes");
	
	return listaCompleta;
	
}

public ArrayList getRelSequenciaAlarme (String bilhetadores, String data, String id){

	try{
		ArrayList dao = new ArrayList();
		
		relAlarmeDAO = new LogDAO();
		

		FiltrosLog filtros = new FiltrosLog();

		filtros.setTipo("SEQ_NUM_FALTANTES;SEQ_NUM_REPETIDOS;SEQ_QNT_ARQUIVO");//strings que indicam os tipos de arquivos que serão considerados para a obtenção dos dados.
		
		if(bilhetadores.length() > 0)
			filtros.setRecurso(bilhetadores);
		
		if(data.length() > 0){
			filtros.setDataPrimeiro(getData(data));
			//filtros.setDataUltimoOp(getDataProx(data), "<");//implementação não possível pois o servidor não trata intervalos de datas
		}

		String dadosStr = relAlarmeDAO.buscarAlarmes(Integer.parseInt(id.substring(0, 7), 16),5,true,filtros.toString(),100);

		String dados[] = new String[0];
		if(!dadosStr.equals("")){
			dados = dadosStr.split("\n");
		}

		String formato[] = relAlarmeDAO.getFormato("SYS");

		Format format = new Format(formato,LogSys.class);

		for (int i = 0; i < dados.length; i++) {
			int fator = 2*(1-1);
			LogSys log = (LogSys)format.formatar(dados[i]);
			log.setID(fator+i);			
			listaCompleta.add(log);
		}

		listaCompleta.trimToSize();

		return listaCompleta;

	} catch (CorbaException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return null;
}
	
	/** Converte o formato da data para dd/mm/aaaa hh:mm:ss
	 * @param data
	 * @return data no formato dd/mm/aaaa hh:mm:ss
	 */
	private String getData(String data) {
	// TODO Auto-generated method stub
		
		String []dataCompleta = data.split("-");
		
		String dia = dataCompleta[2];
		String mes = getNumeroMes(dataCompleta[1]);
		String ano = dataCompleta[0];
		
	return dia+"/"+mes+"/"+ano+" 00:00:00";
}

	/**
	 * @param data
	 * @return próximo dia da data informada
	 */
	private String getDataProx(String data) {
	// TODO Auto-generated method stub
		try {
			
			int mesAux;
			int diaAux;
			
			String []dataCompleta = data.split("-");
			
			String dia = dataCompleta[2];
			String mes = getNumeroMes(dataCompleta[1]);
			String ano = dataCompleta[0];
			
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
			Date dataFormat = dt.parse(dia+"/"+mes+"/"+ano);
			
			GregorianCalendar g = (GregorianCalendar) GregorianCalendar.getInstance();
			
			g.setTime(dataFormat);
			
			g.add(GregorianCalendar.DAY_OF_MONTH, 1);
			
			diaAux = g.get(GregorianCalendar.DAY_OF_MONTH);
			
			if(diaAux < 10)
				dia = "0"+Integer.toString(diaAux); 
			else
				dia = Integer.toString(diaAux);
		
		
			mesAux = g.get(GregorianCalendar.MONTH)+1;//os meses dão inciados por 0, por isso o incremento
			
			
			if(mesAux < 10)
				mes = "0"+Integer.toString(mesAux);
			else
				mes = Integer.toString(mesAux);
			
			ano = Integer.toString(g.get(GregorianCalendar.YEAR));
			
			return dia+ "/"+ mes+ "/"+ ano + " 00:00:00";
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
		
}
	
	private String getNumeroMes(String mes){
		if(mes.equalsIgnoreCase("janeiro"))
			return "01";
		
		else if(mes.equalsIgnoreCase("fevereiro"))
			return "02";
	
		else if(mes.equalsIgnoreCase("março"))
			return "03";
		
		else if(mes.equalsIgnoreCase("abril"))
			return "04";
		
		else if(mes.equalsIgnoreCase("maio"))
			return "05";
		
		else if(mes.equalsIgnoreCase("junho"))
			return "06";
		
		else if(mes.equalsIgnoreCase("julho"))
			return "07";
		
		else if(mes.equalsIgnoreCase("agosto"))
			return "08";
		
		else if(mes.equalsIgnoreCase("setembro"))
			return "09";
		
		else if(mes.equalsIgnoreCase("outubro"))
			return "10";
		
		else if(mes.equalsIgnoreCase("novembro"))
			return "11";
		
		else
			return "12";
		
	}

	private String configuraWhere(String bilhetadores, String data) {
		// TODO Auto-generated method stub
		
		if(bilhetadores.length() < 1 && data.length() < 1){
			return "";
			
		}
		else{
			String where = "";
			String []arrayBilhetadores = bilhetadores.split(";");
			String QueryData = "";
			String QueryBilhetador = "";
			
			for (int i = 0; i < arrayBilhetadores.length; i++) {
				if(i == 0){
					QueryBilhetador += " bil.nome = '"+ arrayBilhetadores[i]+"'";
					
				}
				else{
					QueryBilhetador += " or bil.nome = '"+ arrayBilhetadores[i]+"'";
					
				}
			}			
			QueryData += " dto.data = '"+ data + "'";
			
			if(data.length() > 0)
				where += QueryData;
			if(bilhetadores.length() > 0){
				if(data.length() > 0)
					where +=  " and (" + QueryBilhetador + ")";
				else
					where += QueryBilhetador;
			}
			return "where "+where;
		}
		
		
	}
	
	private void criaObjetosSequenciaDrill(ArrayList listaSequencia, String status){
		Object []lista;
				
		for (int i = 0; i < listaSequencia.size(); i++) {
			lista = (Object[]) listaSequencia.get(i);
			mesclaObjetosSequenciaDrill(lista,status);
		}	
	}
	
	private void mesclaObjetosSequenciaDrill(Object[] objetos, String status){
		
		setRepetidosDrill(new NumRepetidosDrill());
		
		
		if(status.equalsIgnoreCase("numeros faltantes")){
			setFaltantesDrill(new NumFaltantesDrill());
			
			setNumfaltantes((NumFaltantes) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			getFaltantesDrill().setBilhetador(getBilhetador().getNome());
			getFaltantesDrill().setNumerosFaltantes(getNumfaltantes().getNumerosFaltantes());
			getFaltantesDrill().setData(getNumfaltantes().getData());
			getFaltantesDrill().setTipo(status);
			listaCompleta.add(getFaltantesDrill());
			
		}
		else if(status.equalsIgnoreCase("numeros repetidos")){
			setRepetidosDrill(new NumRepetidosDrill());
			setBilhetador((Bilhetador) objetos[1]);
			setNumRepetidos((NumRepetidos) objetos[0]);
			getRepetidosDrill().setBilhetador(getBilhetador().getNome());
			getRepetidosDrill().setData(getNumRepetidos().getData());
			getRepetidosDrill().setNumerosRepetidos(getNumRepetidos().getNumerosRepetidos()); 
			getRepetidosDrill().setQtdSeqNumerosRepetidos(getNumRepetidos().getQtdSeqNumerosRepetidos());
			getRepetidosDrill().setTipo(status);
			
			listaCompleta.add(getRepetidosDrill());
		}
		
		setRepetidosDrill(null);
		
		
	}
	
	private void criaObjetosSequencia(ArrayList listaSequencia, String status){
		Object []lista;
				
		for (int i = 0; i < listaSequencia.size(); i++) {
			lista = (Object[]) listaSequencia.get(i);
			mesclaObjetosSequencia(lista,status);
		}	
	}
	
	private void mesclaObjetosSequencia(Object[] objetos, String status){
		
		setRelatorioSequencia(new RelatorioSequencia());
		
		if(status.equalsIgnoreCase("arquivos faltantes")){
			
			setBilhetador((Bilhetador) objetos[1]);
			setArqFaltantes((ArquivosFaltantes) objetos[0]);
			getRelatorioSequencia().setBilhetador(getBilhetador().getNome());
			getRelatorioSequencia().setPrimeiroArquivo(getArqFaltantes().getIdPrimeiroArquivo());
			getRelatorioSequencia().setUltimoArquivo(getArqFaltantes().getIdUltimoArquivo());
			getRelatorioSequencia().setQntArquivos(getArqFaltantes().getQntArquivos());
			getRelatorioSequencia().setTipo(status);
			
			listaCompleta.add(getRelatorioSequencia());
			
		}
		
		else if(status.equalsIgnoreCase("numeros faltantes")){
			setNumfaltantes((NumFaltantes) objetos[0]);
			setBilhetador((Bilhetador) objetos[1]);
			setArqFaltantes((ArquivosFaltantes)objetos[2]);
			
			getRelatorioSequencia().setBilhetador(getBilhetador().getNome());
			getRelatorioSequencia().setNumFaltantes(getNumfaltantes().getNumerosFaltantes());
			getRelatorioSequencia().setTipo(status);
			getRelatorioSequencia().setPrimeiroArquivo(getArqFaltantes().getIdPrimeiroArquivo());
			getRelatorioSequencia().setUltimoArquivo(getArqFaltantes().getIdUltimoArquivo());
			getRelatorioSequencia().setQntArquivos(getArqFaltantes().getQntArquivos());
			
			listaCompleta.add(getRelatorioSequencia());
			
		}
		else if(status.equalsIgnoreCase("numeros repetidos")){
			setBilhetador((Bilhetador) objetos[1]);
			setNumRepetidos((NumRepetidos) objetos[0]);
			setArqFaltantes((ArquivosFaltantes)objetos[2]);
			getRelatorioSequencia().setBilhetador(getBilhetador().getNome());
			getRelatorioSequencia().setNumRepetidos(getNumRepetidos().getNumerosRepetidos()); 
			getRelatorioSequencia().setQtdNumRepetidos(getNumRepetidos().getQtdSeqNumerosRepetidos());
			getRelatorioSequencia().setTipo(status);
			getRelatorioSequencia().setPrimeiroArquivo(getArqFaltantes().getIdPrimeiroArquivo());
			getRelatorioSequencia().setUltimoArquivo(getArqFaltantes().getIdUltimoArquivo());
			getRelatorioSequencia().setQntArquivos(getArqFaltantes().getQntArquivos());
			
			listaCompleta.add(getRelatorioSequencia());
		}
		
		setRelatorioSequencia(null);
		
	}
	
	public String getRelatorioAcesso(int pg, String paramOrdem, boolean group,  String path, String bilhetadores, String data, OpRelSequenciaAjax opRelSequenciaAjax){	
		//RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();

		if(group)		
			relatorio.addAll(getRelSequencia(bilhetadores,data));
		else{
			try{
				relatorio.addAll(getRelSequenciaAlarme(bilhetadores,data, opRelSequenciaAjax.getSessao().getId()));
			}
			catch (NullPointerException e) {
				opRelSequenciaAjax.setJasperPrint(new JasperPrint()); 
				opRelSequenciaAjax.setJasperPrintDownload(new JasperPrint());
				opRelSequenciaAjax.setQtdPages(0);
				return "Erro na comunicação com o servidor de alarmes.";
			}
		}

		relatorio.trimToSize();
		relAcessoSequencia.setDadosRelatorioAcesso(relatorio,group,path,opRelSequenciaAjax);
		return relAcessoSequencia.relatorioHTML(pg,opRelSequenciaAjax);
		//	return null;
	}
	
	public String getRelatorioPagina(int pg,OpRelSequenciaAjax opRelSequenciaAjax){
		return relAcessoSequencia.relatorioHTML(pg,opRelSequenciaAjax);
	}
	
	public String getRelatorioAcessoDrillRepetido(int pg, String paramOrdem,  String path, String bilhetadores, String data, OpRelSequenciaAjax opRelSequenciaAjax){	
		//RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
			
			relatorio.addAll(getRelSequenciaRepetido(bilhetadores, data));
		
		relatorio.trimToSize();
		relAcessoSequencia.setDadosRelatorioAcesso(relatorio,true,path,opRelSequenciaAjax);
		return relAcessoSequencia.relatorioHTML(pg,opRelSequenciaAjax);
	//	return null;
	}
	
	public String getRelatorioAcessoDrillFaltante(int pg, String paramOrdem,  String path, String bilhetadores, String data, OpRelSequenciaAjax opRelSequenciaAjax){	
		//RelatorioAcessoAction action = new RelatorioAcessoAction();
		ArrayList relatorio = new ArrayList();
			
			relatorio.addAll(getRelSequenciaFaltante(bilhetadores, data));
		
		relatorio.trimToSize();
		relAcessoSequencia.setDadosRelatorioAcesso(relatorio,true,path,opRelSequenciaAjax);
		return relAcessoSequencia.relatorioHTML(pg,opRelSequenciaAjax);
	//	return null;
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
	
	public NumFaltantes getNumfaltantes() {
		return numfaltantes;
	}
	public void setNumfaltantes(NumFaltantes numfaltantes) {
		this.numfaltantes = numfaltantes;
	}
	public ArquivosFaltantes getArqFaltantes() {
		return arqFaltantes;
	}
	public void setArqFaltantes(ArquivosFaltantes arqFaltantes) {
		this.arqFaltantes = arqFaltantes;
	}
	public NumRepetidos getNumRepetidos() {
		return numRepetidos;
	}
	public void setNumRepetidos(NumRepetidos numRepetidos) {
		this.numRepetidos = numRepetidos;
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

	public RelatorioSequencia getRelatorioSequencia() {
		return relatorioSequencia;
	}

	public void setRelatorioSequencia(RelatorioSequencia relatorioSequencia) {
		this.relatorioSequencia = relatorioSequencia;
	}

	public RelatoriosSequenciaDAO getRelSequencia() {
		return relSequencia;
	}

	public void setRelSequencia(RelatoriosSequenciaDAO relSequencia) {
		this.relSequencia = relSequencia;
	}


	public ArrayList getListaCompleta() {
		return listaCompleta;
	}


	public void setListaCompleta(ArrayList listaCompleta) {
		this.listaCompleta = listaCompleta;
	}

	public NumRepetidosDrill getRepetidosDrill() {
		return repetidosDrill;
	}

	public void setRepetidosDrill(NumRepetidosDrill repetidosDrill) {
		this.repetidosDrill = repetidosDrill;
	}

	public RelatorioAcessoJasper getRelAcesso() {
		return relAcesso;
	}

	public void setRelAcesso(RelatorioAcessoJasper relAcesso) {
		this.relAcesso = relAcesso;
	}

	public RelatorioAcessoJasperSequencia getRelAcessoSequencia() {
		return relAcessoSequencia;
	}

	public void setRelAcessoSequencia(
			RelatorioAcessoJasperSequencia relAcessoSequencia) {
		this.relAcessoSequencia = relAcessoSequencia;
	}

	public NumFaltantesDrill getFaltantesDrill() {
		return faltantesDrill;
	}

	public void setFaltantesDrill(NumFaltantesDrill faltantesDrill) {
		this.faltantesDrill = faltantesDrill;
	}

	public LogDAO getRelAlarmeDAO() {
		return relAlarmeDAO;
	}

	public void setRelAlarmeDAO(LogDAO relAlarmeDAO) {
		this.relAlarmeDAO = relAlarmeDAO;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	

}
