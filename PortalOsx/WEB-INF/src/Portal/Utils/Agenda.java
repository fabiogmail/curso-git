package Portal.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import libjava.tipos.Vetor;
import CDRView2.SelecaoIndicadoresPortal;
import CDRView2.SelecaoRecursosPortal;
import Interfaces.iListaRelAgendaHistorico;
import Interfaces.iRetRelatorio;
import Portal.Cluster.NoUtil;
import Portal.Conexao.CnxServUtil;
import Portal.Configuracoes.DefsComum;

public class Agenda
{ 
	public String m_NomeRelatorio = null;
	public Vector m_Cabecalho = null;
	public Vector m_vIndicadoresOriginais;   
	public Vector m_vIndicadores;
	public Vector m_Periodo;
	private boolean m_AbreRetRelatorio = false;
	protected String linhaTotalizacao = ""; //variavel que contem a linha totalizadora

	public static final int STATUS_DOWNLOAD_PRONTO       = 0;
	public static final int STATUS_DOWNLOAD_MONTANDO_ARQ = 1;
	public static final int STATUS_DOWNLOAD_COMPACTANDO  = 2;
	public static final int STATUS_DOWNLOAD_CONCLUIDO    = 3;
	public static final int STATUS_DOWNLOAD_ERRO         = 4;

	public int m_GeraArqDownload = 0;  // 1: Gerando Arquivo; 2: Arquivo Gerado; 3: Erro na Geração
	public static final int GERANDO_ARQUIVO = 1;
	public static final int ARQUIVO_GERADO  = 2;
	public static final int ERRO_NA_GERACAO = 3;

	private SelecaoRecursosPortal m_SelecaoRecursos;
	private SelecaoIndicadoresPortal m_SelecaoIndicador;
	private String[] m_Rec=null;
	private Vector m_Recv=null;

	public static boolean podeRemoverZero = false;//gato para tim que vem com 0; alem do esperado

	private int m_QtdEliminados;

	/**
	 * 
	 * @uml.property name="m_RetRelatorio"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public RetRelatorio m_RetRelatorio;

	public iRetRelatorio m_iRetRelatorio;
	public iListaRelAgendaHistorico m_iListaRelatorios = null;
	public Vector m_QtdLinhasPer;

	public String m_IndicadoresPossiveis = null;   
	public String m_Periodos = null;   
	public String m_DataColeta = null;

	public Vector m_PeriodosApresentaveis = null;
	public Vector m_Linhas = null;
	public Vector m_Logs = null;
	public Vector m_Datas = null;
	public Vector m_NomesDatas = null;

	//hash map que comtem a quantidade de linhas por id  
	//exemplo: id 12 quantidade de linhas 1234
	public HashMap hashLinhasId = null;


	/**
	 * 
	 * @uml.property name="m_ConexUtil"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected CnxServUtil m_ConexUtil;

	protected String m_Perfil;
	protected String m_TipoRel;
	protected String m_IdRel;
	protected String m_Arquivo;   
	public int m_QtdPeriodos = 0;
	public String m_DataGeracao;
	protected String m_IndicadoresIniciais; // Sao os indicadores que o usuario selecionou na interface
	public String m_TipoColunas[];
	public Vector m_TipoColunasV;
	public int m_Pag = 0;
	public int m_QtdLinhasPagina = 21;
	public int m_UltProcessado = 0;
	final private int m_PosDataGeracao = 2;
	final private int m_PosIndicadoresIniciais = 14;
	final private int m_PosTipoColuna1 = 12;
	final private int m_PosTipoColuna2 = 13;
	final private String m_DirDownload = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/";

	private static final String m_COR_HEADER="#000066";         // Azul escuro
	private static final String m_COR_HEADER2="#666666";        // Cinza escuro
	private static final String m_COR_HEADER3="#888888";        // Cinza escuro mais claro
	private static final String m_COR_HEADER_APR="#33CCFF";     // Azul claro
	private static final String m_COR_PERIODOS="#000066";       // Azul escuro
	private static final String m_COR_FONTEHEADER="#FFFFFF";    // Branco
	private static final String m_COR_FONTEHEADER2="#FOFOF0";   // Cinza Claro
	private static final String m_COR_LINHASIM="#FFFFFF";       // Branco
	private static final String m_COR_LINHASIM2="#C0C0C0";      // Cinza escuro   
	private static final String m_COR_LINHASIM3="#D9D9D9";      // Cinza entre o Cinza escuro e o Cinza claro
	private static final String m_COR_LINHANAO="#F0F0F";        // Cinza claro
	public static final String m_FONTE="\"verdana\" size=\"1\"";

	private String m_TODOS = "Todos";   

	public static final int ANATEL_SMP3 = 0;
	public static final int ANATEL_SMP5 = 1;
	public static final int ANATEL_SMP6 = 2;
	public static final int ANATEL_SMP7 = 3;
	public static final int ANATEL_LDN  = 4; 
	
	public static final int ANATEL_SMP3_ERICSSON = 5;
	public static final int ANATEL_SMP5_ERICSSON = 6;
	public static final int ANATEL_SMP6_ERICSSON = 7;
	public static final int ANATEL_SMP7_ERICSSON = 8;
	
	public static final int ANATEL_IDDF = 9;
	public static final int ANATEL_STFC = 10;

	/**variavel estatica que contem o tipo de agenda que ser quer ver do relatorio de IMEI
	 * pois o mesmo possui dois tipos de visualização 0-detalhe 1-Resumo*/
	public static int TIPO_REL_IMEI = 0;

	/**String que contem os indicadores de resumo(linhas 11;) no relatorio de imei*/
	public String indicadoresDefinidosIMEI = "";

	/** Vetor que contem os contadores que geraram o relatorio */
	private Vetor m_ContadoresRelatorio = new Vetor();

	public Agenda(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, String p_NomeRel, String p_DataGer)
	{
		m_ConexUtil = p_ConexUtil;
		m_Perfil = p_Perfil;
		m_TipoRel = p_TipoRel;
		m_IdRel = p_IdRel;
		m_Arquivo = p_Arquivo;
		m_DataGeracao = p_DataGer;
		m_NomeRelatorio = p_NomeRel;

		m_Cabecalho = new Vector();
		m_Periodo = new Vector();
		m_Logs = new Vector();
		m_Datas = new Vector();
		m_Linhas = new Vector();
		m_Logs = new Vector();
		m_QtdLinhasPer = new Vector();
		m_TipoColunas = new String[2];
		m_TipoColunasV = new Vector();

		/*  Se o relatorio for do tipo DESEMPENHO ou DETALHE DE CHAMADAS ira
		 *  utilizar a implementacao de leitura particionada.
		 */

		// *** 
		// Nova implementacao      
		// ***      
		getRetRelatorio();
		abreRetRelatorio();
		getPeriodos();   // Depois que se abre o RetRelatorio, deve-se chamar
		// o metodo getPeriodos() OBRIGATORIAMENTE.
		getCabecalho();
		getHeaderInfo();

		for (int i = 0; i < m_QtdPeriodos; i++)       
			getLogs((short)i); 

		if (p_TipoRel.equalsIgnoreCase("0") || p_TipoRel.equalsIgnoreCase("1") || p_TipoRel.equalsIgnoreCase("2"))
		{
			getLinhasRelatorio(); 	
		}
		else if(p_TipoRel.equalsIgnoreCase("30")&& Agenda.TIPO_REL_IMEI==1)
		{		
			getLinhasIMEI();//forma de leitura somente para o rel IMEI tipo RESUMO
		}
		else
		{		  
			getLinhasAposCabecalho(); // Forma de leitura atual (Carrega todas as linhas)	   
		}
	}

	/*
	 * Metodo que ira buscar uma referencia do objeto iRetRelatorio e armazernar tal referencia
	 * no objeto RetRelatorio.
	 * 
	 * */

	public void getRetRelatorio() 
	{      
		if (m_iListaRelatorios == null)
		{
			m_iListaRelatorios = m_ConexUtil.getRelatorio3((short)0, m_Perfil, Integer.parseInt(m_TipoRel), Integer.parseInt(m_IdRel), m_DataGeracao, m_Arquivo, m_NomeRelatorio);      
			if (m_iListaRelatorios != null)                      
				m_iRetRelatorio = m_iListaRelatorios.fnGetRelatorio2((short)0, Integer.parseInt(m_TipoRel), m_Perfil, Integer.parseInt(m_IdRel), m_DataGeracao, m_Arquivo, m_NomeRelatorio);      
			else 
				System.out.println("\n Agenda: getRetRelatorio(): m_iListaRelatorios eh null!! \n");            
		}

		m_RetRelatorio = new RetRelatorio();
		m_RetRelatorio.setRetRelatorio(m_iRetRelatorio);
	}

	/*
	 * Metodo que ira retornar as informacoes do cabecalho que de fato sao utilizadas pelo
	 * Portal.
	 * 
	 * */

	public void getHeaderInfo()
	{
		m_DataGeracao = montaData(m_iRetRelatorio.fnGetDataRel());
		m_IndicadoresIniciais = m_iRetRelatorio.fnGetIndicadores();   
		m_TipoColunas[0] = m_iRetRelatorio.fnGetNomeColuna((short)1);
		m_TipoColunas[1] = m_iRetRelatorio.fnGetNomeColuna((short)2);

		for (int i = 1;; i++) {
			String nomeColuna = m_iRetRelatorio.fnGetNomeColuna((short)i);
			if(nomeColuna.equals("")){
				break;
			}else{
				m_TipoColunasV.add(nomeColuna);
			}

		}

		m_vIndicadoresOriginais = VetorUtil.String2Vetor(m_IndicadoresIniciais,';');    
		m_vIndicadores = new Vector(m_vIndicadoresOriginais.size());
		for (int i = 0; i < m_vIndicadoresOriginais.size(); i++)
		{
			m_vIndicadores.add(m_vIndicadoresOriginais.elementAt(i).toString());
		}
	}

	/*
	 * Método responsável em abrir o objeto m_iRetRelatorio
	 * 
	 * */

	public void abreRetRelatorio()
	{
		if (!m_AbreRetRelatorio)
		{ 
			m_iRetRelatorio.fnAbre();
			//  System.out.println("\n\nabriu retRelatorio...\n\n");
			m_AbreRetRelatorio = true;
		}
	}

	/*
	 * Método responsável em fechar o objeto m_iRetRelatorio
	 * 
	 * */

	public void fechaRetRelatorio()
	{
		if (m_AbreRetRelatorio)
		{ 
			m_iRetRelatorio.fnFecha();
			System.out.println("fechou retRelatorio...");
			m_AbreRetRelatorio = false;         
		}
	}

	/*
	 * Metodo que ira buscar o cabecalho de um relatorio. O retorno deste metodo será 
	 * uma String contendo todos os campos do cabeçalho separados por "\n".
	 * 
	 * */

	public void getCabecalho()
	{
		StringBuffer cabecalhoBuffer = new StringBuffer();
		String cabecalho = "";     
		String temp = "";     

		temp = m_iRetRelatorio.fnGetCabecalhos(); 

		while (!temp.equals(""))
		{        
			cabecalhoBuffer.append(temp);          
			temp = m_iRetRelatorio.fnGetCabecalhos();                 
		}

		cabecalho = cabecalhoBuffer.toString();     
		StringTokenizer cabecalhoToken = new StringTokenizer(cabecalho);
		while (cabecalhoToken.hasMoreTokens())
			m_Cabecalho.add(cabecalhoToken.nextToken("\n").toString());   
		m_Cabecalho.add(0, "Data de Referencia: "+m_DataColeta);
		m_Cabecalho.trimToSize();
		m_RetRelatorio.setCabecalho(m_Cabecalho);
	}

	/*
	 * Metodo que ira buscar todos os periodos de um relatorio. O retorno deste metodo será 
	 * uma String contendo todos os periodos do relatorios separados por "\n".
	 * Exemplo de retorno:
	 *    DataInicio;DataFim;NomePeriodo;QtdLinhasPeriodo\n
	 *    
	 * */

	public void getPeriodos()
	{
		StringBuffer periodoBuffer = new StringBuffer();     
		String periodo = "";  
		String nomePeriodo = "";
		String periodoInicial = "", periodoFinal = "";
		String temp = "";

		// Busca todos os periodos do Relatorio
		temp = m_iRetRelatorio.fnGetPeriodos();
		String[] t = m_iRetRelatorio.fnGetLinhas().split("\n");
		linhaTotalizacao = t[0];    //recebeu a linha numero 1 
		// Adiciona todos os periodos num Buffer
		periodoBuffer.append(temp);
		periodo = periodoBuffer.toString();   

		StringTokenizer periodoToken = new StringTokenizer(periodo);
		String qtdLinha = "", aux = "";      

		while (periodoToken.hasMoreTokens())
		{     
			/**
			 * Faz a separacao dos periodos (Caracter delimitador "\n").
			 * 
			 *               Per. Inicial   Per. Final                 Numero de linhas  
			 * Ex de linha: 20040820090000;20040820110000;NomePeriodo;26000
			 * 
			 */
			aux = periodoToken.nextToken("\n").toString();

			qtdLinha = aux.substring(aux.lastIndexOf(';')+1,aux.length());
			/**
			 * Caso o nome do periodo seja igual a "--", faz-se a substituicao por:
			 * "De Per.Inicial a Per. Final"
			 * 
			 * */
			if (aux.indexOf('-') != -1)
			{         
				periodoInicial = montaData(aux.substring(0,aux.indexOf(';')));
				periodoFinal   = montaData(aux.substring(aux.indexOf(';')+1,(aux.indexOf('-')-1)));
				nomePeriodo = "De: " + periodoInicial + " até: " + periodoFinal;
				aux = aux.replaceFirst("--",nomePeriodo);
			}
			m_Periodo.add(aux.substring(0,aux.lastIndexOf(';')));         
			m_QtdLinhasPer.add(qtdLinha);
		}   
		m_QtdLinhasPer.trimToSize();
		m_RetRelatorio.setPeriodo(m_Periodo);  

		m_QtdPeriodos = m_Periodo.size();
		for (int i = 0; i < m_QtdPeriodos; i++) 
		{   
			aux = m_Periodo.elementAt(i).toString();
			if (m_DataColeta == null  || m_DataColeta.length() == 0)
			{
				m_DataColeta = aux;    
				m_DataColeta = m_DataColeta.substring(0,m_DataColeta.indexOf(';'));            
				m_DataColeta = montaData(m_DataColeta);
				m_DataColeta = m_DataColeta.substring(0,m_DataColeta.indexOf(' '));
			}
			m_Datas.add(getPeriodo("2;"+aux));
		}

		m_PeriodosApresentaveis = new Vector(m_QtdPeriodos);
		for (int i = 0; i < m_QtdPeriodos; i++)
			m_PeriodosApresentaveis.add(new Integer(i));  
	}

	/*
	 * Metodo que ira buscar as logs de cada periodo. Caso nao haja logs para um periodo 
	 * especifico o servidor irá retornar vazio.
	 * 
	 * */

	public void getLogs(short periodo)
	{
		StringBuffer logBuffer = new StringBuffer();      
		String log = "";      
		String temp = "";      
		Vector aux = new Vector();

		temp = m_iRetRelatorio.fnGetLogs(periodo); 

		while (!temp.equals(""))
		{        
			logBuffer.append(temp);   
			temp = m_iRetRelatorio.fnGetLogs(periodo);    
		}

		log = logBuffer.toString();
		if (log.length() == 0) // Se nao houver logs, eh preenchido com a String "nolog"
		{
			log = "nolog";
			aux.add(log);        
		}
		else         
		{
			StringTokenizer logToken = new StringTokenizer(log);
			while (logToken.hasMoreTokens())
				aux.add(logToken.nextToken(";").toString());
		}  

		m_Logs.add(aux); 
		m_RetRelatorio.setLogs(m_Logs);
	}

	/**
	 * Metodo que ira buscar as linhas do relatorio de cada periodo. Antes de chamar o metodo
	 * que de fato busca as linhas do relatorio, deve-se chamar o metodo 
	 * "posicionaLinha(int periodo, int linha)" que recebe como parametro o periodo que tais 
	 * linhas se referem e a posicao do ponteiro no arquivo que contem o inicio das linhas.
	 * 
	 * */
	public void getLinhasRelatorio()
	{
		Vector tmp=null; 

		podeRemoverZero = false;

		for (short i = 0; i < m_RetRelatorio.getPeriodos().size(); i++) 
		{
			// Posiciona a linha no inicio de cada periodo.
			m_RetRelatorio.posicionaLinha(i,0);
			m_Linhas.add(m_RetRelatorio.getRelatorio(i,m_QtdLinhasPagina));
			tmp = (Vector) m_Linhas.elementAt(i);        
		}

		m_Linhas.trimToSize();
	}

	public Vector getLinhasAposCabecalho () 
	{	
		Vector l_vLinhasRel = null;
		boolean bSai = false;
		String l_LinhaRel = null, l_LinhaAnterior = null;
		int l_QtdPeriodos = 0;	 

		podeRemoverZero = true;

		if (m_iRetRelatorio != null)
		{
			m_Linhas.clear();
			m_iRetRelatorio.fnPosicionaAposCabecalho();		
			l_vLinhasRel = new Vector(500,500);

			int i = 0, numPeriodo = -1;
			do
			{
				// Limpando referencia l_LinhaRel para o GC
				l_LinhaRel = null;

				l_LinhaRel = m_iRetRelatorio.fnGetLinha(); 
				if (l_LinhaRel != null && l_LinhaRel.length() != 0)   
				{		   	
					if (l_LinhaRel.startsWith("2;")) // Se a linha comecar com 2, significa o inicio de um novo periodo.
					{
						numPeriodo++;
						/**
						 * Se a proxima linha a ser lida comecar com "2;", significa que as linhas do relatorio do
						 * periodo anterior acabaram. Portanto, adiciono tais linhas no Vector m_Linhas.
						 * 
						 * */  
						if (l_vLinhasRel.size() > 0)
						{
							l_vLinhasRel.trimToSize();     
							m_Linhas.add(l_QtdPeriodos++, l_vLinhasRel);
							l_vLinhasRel = null;
							l_vLinhasRel = new Vector(500,500);	
						}	
						/**
						 * Caso o periodo anterior nao possua linhas de relatorio, o periodo
						 * será removido.
						 * */
						if ((l_LinhaAnterior != null) && (l_LinhaAnterior.startsWith("2;")))
						{
							//Chama metodo para sincronizar os Vectors do periodo anterior
							//sincronizaVectorRelatorio(numPeriodo-1);
							sincronizaVectorRelatorio(numPeriodo-1,l_LinhaAnterior);
						}

						l_LinhaAnterior = l_LinhaRel;
					}      
					else if (l_LinhaRel.startsWith("0;")) // Se a linha comecar com 0, significa que são linhas do Relatorio.
					{ 
						l_vLinhasRel.add(l_LinhaRel);
						l_LinhaAnterior = l_LinhaRel;
					}	
					else if (l_LinhaRel.startsWith("9;"))
					{
						System.out.print("dfd");
					}
				}		   
				else
				{
					/**
					 * Faz um ultimo teste para o caso do ultimo periodo que não estava sendo
					 * testado la em cima.
					 * Caso o periodo anterior nao possua linhas de relatorio, o periodo
					 * será removido.
					 **/
					if ((l_LinhaAnterior != null) && (l_LinhaAnterior.startsWith("2;")))
					{
						//Chama metodo para sincronizar os Vectors do periodo anterior
						if(numPeriodo>0){
							//sincronizaVectorRelatorio(numPeriodo-1);
							sincronizaVectorRelatorio(numPeriodo-1,l_LinhaAnterior);
						}else{//correção para os anateis que não tem linha totalizadora
							//sincronizaVectorRelatorio(numPeriodo);
							sincronizaVectorRelatorio(numPeriodo,null);
						}
					}	
					bSai = true;
				}
				i++;
			}
			while (!bSai);
		}
		else
		{
			return null;
		}   

		l_vLinhasRel.trimToSize();	 
		m_Linhas.add(l_QtdPeriodos, l_vLinhasRel);

		/**
		 * Atualizando as posicoes dos periodos.
		 * */
		m_PeriodosApresentaveis.clear();
		for (int i = 0; i < m_QtdPeriodos; i++) 
		{
			m_PeriodosApresentaveis.addElement(new Integer(i));
		}

		return m_Linhas;
	}

	public void getLinhasIMEI () 
	{	
		Vector l_vLinhasRel = null;
		String linhas = null;	 

		if (m_iRetRelatorio != null)
		{
			m_Linhas.clear();
			m_iRetRelatorio.fnPosicionaDados((short)0,0,(short)12);						
			linhas = m_iRetRelatorio.fnGetDados((short)0,(short)12);	
			l_vLinhasRel = new Vector(500,500);


			while(linhas != null)
			{
				if (linhas != null && linhas.length() != 0)   
				{		   	
					StringTokenizer linhasToken = new StringTokenizer(linhas, "\n");
					while(linhasToken.hasMoreTokens())
					{
						l_vLinhasRel.add(linhasToken.nextToken());
					}	      
				}		   
				else
				{
					break;
				}
				// Limpando referencia l_LinhaRel para o GC
				linhas = null;
				linhas = m_iRetRelatorio.fnGetDados((short)0,(short)12);		   
			}
		}	    

		l_vLinhasRel.trimToSize();	 
		m_Linhas.add(l_vLinhasRel);
	}



	/**
	 * Metodo responsavel em minimizar as toskeiras. Qdo for removido um periodo
	 * de relatorio, todos os Vectors com dados referentes ao periodo devem ser 
	 * eliminados. Ex: m_Datas, m_NomesDatas, m_Periodo, m_Linhas, m_Logs etc.
	 * */
	public void sincronizaVectorRelatorio(int periodo, String linha)
	{
		if(linha != null){
			for(int i=0; i<m_Periodo.size(); i++){
				if(linha.indexOf(m_Periodo.elementAt(i).toString())!=-1){
					periodo = i;
					break;
				}
			}
		}
		m_QtdPeriodos--; // Decrementa a qtdade. periodos
		m_PeriodosApresentaveis.removeElementAt(periodo); // Decrementa a qtdade. periodos
		m_Datas.removeElementAt(periodo);
		m_NomesDatas.removeElementAt(periodo);
		m_Periodo.removeElementAt(periodo);
		m_Logs.removeElementAt(periodo);
	}

	public void getRelatorio()
	{      
		int TamLinhas;
		String Linha;
		Vector Linhas, LinhasRel = new Vector();
		int l_QtdPeriodos = -1;

		m_Linhas.clear();

		Linhas = this.getLinhasAposCabecalho();

		m_Linhas = Linhas;
		m_Linhas.trimToSize();     
	}

	public static String[] identificaRecursos(Vector p_Indicadores, String p_Coluna0, String p_Coluna1) 
	{
		String Rec[] = new String[3];

		for (int i = 0; i < Rec.length; i++)
			Rec[i] = null;

		if (p_Coluna0.equals("") == false)
			Rec[0] = (String)p_Indicadores.elementAt(0);
		if (p_Coluna1.equals("") == false)
			Rec[1] = (String)p_Indicadores.elementAt(1);

		return Rec;
	}

	public static Vector identificaRecursos(Vector p_Indicadores, Vector p_Coluna) 
	{
		Vector Rec = new Vector();
		for (int i = 0; i < p_Coluna.size(); i++) {
			Rec.add((String)p_Indicadores.elementAt(i));
		}  
		return Rec;
	}

	public String montaData(String p_Data) 
	{
		String Aux = null, Dia, Mes, Ano, Hora, Min, Seg;

		Dia = p_Data.substring(6,8);
		Mes = p_Data.substring(4, 6);
		//Ano = p_Data.substring(0, 4);
		Ano = p_Data.substring(2, 4);      
		Hora = p_Data.substring(8,10);
		Min = p_Data.substring(10,12);
		Seg = p_Data.substring(12,p_Data.length());

		Aux = Dia + "/" + Mes + "/" + Ano + " " + Hora + ":" + Min + ":" + Seg;
		return Aux;
	}   

	public String getPeriodo(String p_Aux)
	{
		String Aux1, Aux2, Periodo;
		String NomePeriodo = p_Aux.substring(p_Aux.lastIndexOf(";")+1, p_Aux.length());

		if (NomePeriodo.length() == 0)
			NomePeriodo = "N/A";
		Aux1 = p_Aux.substring(p_Aux.indexOf(";")+1, p_Aux.lastIndexOf(";"));
		Aux2 = Aux1.substring(0, Aux1.indexOf(";"));
		Aux1 = Aux1.substring(Aux2.length()+1, Aux1.length());

		if (NomePeriodo.equals("N/A") == false)
		{
			if (m_NomesDatas == null) m_NomesDatas = new Vector();
			m_NomesDatas.add(NomePeriodo);
		}
		Periodo = montaData(Aux2) + " - " + montaData(Aux1);

		return Periodo;
	}

	public void setIndicadores(String p_Indicadores)
	{
		m_vIndicadores = VetorUtil.String2Vetor(m_IndicadoresIniciais,';');
	}

	public void setQtdLinhas(String p_Posicao)
	{
		int QtdLinhasPaginaAntiga = m_QtdLinhasPagina;
		switch (Integer.parseInt(p_Posicao))
		{
		case 0:
			m_QtdLinhasPagina = 21;
			break;
		case 1:
			m_QtdLinhasPagina = 50;
			break;
		case 2:
			m_QtdLinhasPagina = 100;
			break;
		case 3:
			m_QtdLinhasPagina = 200;
			break;
		case 4:
			m_QtdLinhasPagina = 400;
			break;
		case 5:
			m_QtdLinhasPagina = 500;
			break;
		}

		if (m_QtdLinhasPagina != QtdLinhasPaginaAntiga)
			m_Pag = 0;
	}

	public boolean criaArquivo(String p_NomeArq, Vector p_Cabecalho, Vector p_Indicadores, Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, Vector p_Relatorio[], Vector p_Logs, Vector p_CfgAg)
	{
		Arquivo Arq = new Arquivo();   
		int i, Tam, Periodo, QtdPeridos, QtdIndicadores, QtdRelProcessado, QtdLogs;
		Vector RelProcessado, LinhaRelProcessado, Logs, Log;
		String strLog, strColuna, Cor = "", p_TipoSalvamento;

		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		p_NomeArq += "." + p_TipoSalvamento;

		Arq.setDiretorio(m_DirDownload);
		Arq.setNome(p_NomeArq);
		Arq.apaga();

		if (Arq.abre('w') == true)
		{
			if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
			{
				// Escreve cabeçalho
				for (i = 0; i < p_Cabecalho.size(); i++)
					Arq.escreveLN(p_Cabecalho.elementAt(i).toString());
				Arq.escreveLN("");

				// Recupera a qtd de elementos na linha do relatorio
				QtdIndicadores = p_Indicadores.size();
				// Escreve Relatorio
				QtdPeridos = p_PeriodosApresentaveis.size();
				for (i = 0; i < QtdPeridos; i++)
				{
					Periodo = Integer.parseInt(p_PeriodosApresentaveis.elementAt(i).toString());
					if (p_NomesPeriodos != null && p_NomesPeriodos.size() != 0)
						Arq.escreveLN("Periodo: "+p_NomesPeriodos.elementAt(Periodo)+": "+p_Datas.elementAt(Periodo));
					else
						Arq.escreveLN("Periodo: "+p_Datas.elementAt(Periodo));

					for (int j = 0; j < QtdIndicadores; j++)
						Arq.escreve(p_Indicadores.elementAt(j)+",");
					Arq.escreveLN("");

					RelProcessado = (Vector)p_Relatorio[Periodo];
					QtdRelProcessado = RelProcessado.size();
					for (int j = 0; j < QtdRelProcessado; j++)
					{
						m_GeraArqDownload = (QtdPeridos)*100000 + j + 1; 
						LinhaRelProcessado = (Vector)RelProcessado.elementAt(j);
						for (int k = 0; k < QtdIndicadores; k++)
						{
							strColuna = LinhaRelProcessado.elementAt(k).toString();
							strColuna = strColuna.replace(',','.');
							Arq.escreve(strColuna+",");
						}
						Arq.escreveLN("");
					}

					Arq.escreveLN("");
					Logs = (Vector)p_Logs.elementAt(Periodo);
					QtdLogs = Logs.size();
					if (QtdLogs != 0)
					{
						Arq.escreveLN("Logs:");
						for (int j = 0; j < Logs.size(); j++)
							if (j == 0 && Logs.elementAt(j).toString().equals("nolog") == true)
								Arq.escreveLN("Não há logs para o este período");
							else
								Arq.escreveLN(Logs.elementAt(j).toString().substring(0,Logs.elementAt(j).toString().length()-1));
						Arq.escreveLN("");
					}
					else Arq.escreveLN("");
				}
			}
			else if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
			{
				Arq.escreve("<html>\n<head>\n<title>CDRView | Visent</title>\n");
				Arq.escreve("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
				Arq.escreve("<meta http-equiv=\"Pragma\" content=\"no-cache\">\n</head>\n");

				Arq.escreve("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

				// Escreve cabeçalho
				Arq.escreve("<br>\n");
				// Arq.escreve("<b><font face="+m_FONTE+">CDRView<sup>&reg;</sup> by OSx Telecom</b></font><br>\n");
				Arq.escreve(HTMLUtil.getCabecalho());

				if (p_CfgAg.elementAt(2).equals("1") == true)
				{
					for (i = 0; i < p_Cabecalho.size(); i++)
						Arq.escreveLN("<font face="+m_FONTE+">"+ p_Cabecalho.elementAt(i).toString()+"</font><br>");
				}
				Arq.escreve("<br>\n");

				for (int z = 0; z < p_PeriodosApresentaveis.size(); z++)         
				{
					i = ((Integer)p_PeriodosApresentaveis.elementAt(z)).intValue();

					// Tabela de linhas do relatório para o período desejado
					Arq.escreve("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
					Arq.escreve("   <tr>\n   ");
					Arq.escreve("<td align=\"left\" colspan=\""+p_Indicadores.size()+"\">");
					Arq.escreve("<font face="+m_FONTE+">");
					if (p_NomesPeriodos != null)
						Arq.escreve("<b>Per&iacute;odo: </b>" + p_NomesPeriodos.elementAt(i) + "&nbsp;["+p_Datas.elementAt(i)+"]");
					else
						Arq.escreve(p_Datas.elementAt(i).toString());
					Arq.escreve("</font>");
					Arq.escreve("</td>\n");            
					Arq.escreve("   </tr>\n");            
					Arq.escreve("   <tr bgcolor=\""+m_COR_HEADER_APR+"\">\n   ");
					for (int k = 0; k < p_Indicadores.size(); k++)
					{
						Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\"#000000\"><b>"
								+p_Indicadores.elementAt(k).toString()
								+"</font></b></td>");
					}
					Arq.escreve("\n   </tr>\n");

					// Envia linhas do relatório
					Tam = p_Relatorio[i].size(); 
					RelProcessado = p_Relatorio[i];
					for (int j = 0; j < Tam; j++)            
					{
						if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
						else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";

						LinhaRelProcessado = (Vector)RelProcessado.elementAt(j);

						Arq.escreve("   <tr"+Cor+">\n   ");
						for (int k = 0; k < LinhaRelProcessado.size(); k++)
						{
							if (k == 0) Arq.escreve("      ");
							Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+LinhaRelProcessado.elementAt(k).toString()+"</font></td>");
						}
						Arq.escreve("\n   </tr>\n");
					}
					Arq.escreve("</table>\n");

					Arq.escreve("<br>");

					// Envia logs se estiver configurado para tal
					if (p_CfgAg.elementAt(3).equals("1") == true)   // Log
					{
						Log = (Vector)p_Logs.elementAt(i);
						if (Log.elementAt(0).toString().equals("nolog") == false)
						{
							Arq.escreve("<table>\n");
							Arq.escreve("   <tr>\n      <td align=\"left\"><font face="+m_FONTE+"><b>Logs:</b></font></td>\n   </tr>\n");
							int TamLog = Log.size();
							for (int j = 0; j < TamLog; j++)
								Arq.escreve("   <tr>\n      <td><font face="+m_FONTE+">"+Log.elementAt(j).toString().substring(0, Log.elementAt(j).toString().length()-1)+"</font></td>\n   </tr>\n");
							Arq.escreve("</table>\n");
							Arq.escreve("<br>");
						}
					}
				}

				Arq.escreve("\n</body>\n</html>");            
			}
			Arq.fecha();
			m_GeraArqDownload = 2;
			return true;
		}
		else
		{
			m_GeraArqDownload = 3;
			return false;
		}
	}



	public boolean criaArquivoMatraf(String p_NomeArq, Vector p_Cabecalho, String p_Recursos[], Vector p_Indicadores, Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, int p_UltProcessado, Set p_ListaRecursos[], Map p_MapRelatorios[], Vector p_Logs, Vector p_CfgAg)
	{
		Arquivo Arq = new Arquivo();
		boolean bTemTodos = false;
		int i, j, iCont = 0, Periodo, QtdPeridos, iQtdIndicadores, 
		QtdRelProcessado, QtdLogs, iQtdRecursosTipo1;
		Vector Chaves, LinhaRelProcessado, Logs;
		String strLog, strColuna, Recurso[], Cor = "", p_TipoSalvamento;
		Iterator It[];
		Map.Entry Ent[];
		Object Obj, ObjLinha;


		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		p_NomeArq += "." + p_TipoSalvamento;

		It = new Iterator[2];
		Ent = new Map.Entry[2];
		Recurso = new String[2];      
		Chaves = new Vector();

		Arq.setDiretorio(m_DirDownload);
		Arq.setNome(p_NomeArq);
		Arq.apaga();

		iQtdIndicadores = p_Indicadores.size();
		if (Arq.abre('w') == true)
		{
			if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
			{

				// Escreve cabeçalho
				//p_CfgAg.get(0) diz se o relatorio é csv. 
				//p_CfgAg.get(1) diz se o relatorio é html.          	
				//p_CfgAg.get(2) indica se foi selecionada a opção de cabeçalho
				//p_CfgAg.get(3) diz se foi selecionado a opcao de logs.
				if((""+p_CfgAg.get(2)).equals("1"))
				{
					for (i = 0; i < p_Cabecalho.size(); i++)
						Arq.escreveLN("\""+p_Cabecalho.elementAt(i).toString()+"\"");
				}

				if (iQtdIndicadores == 3)
					Arq.escreveLN("\"Coluna: "+p_Indicadores.elementAt(2).toString()+"\"");
				Arq.escreveLN("");

				for (int z = 0; z < p_PeriodosApresentaveis.size(); z++)
				{
					Arq.escreveLN("\""+p_Recursos[0]+"\",\""+p_Recursos[1]+"\"");
					if (p_ListaRecursos[1].size() > 2)
					{
						Arq.escreve("\"\",");
						It[1] = p_ListaRecursos[1].iterator();
						while (It[1].hasNext())
						{
							Ent[1] = (Map.Entry)It[1].next();
							Obj = Ent[1].getValue();
							Recurso[1] = Obj.toString();
							if (Recurso[1].equals("Todos") == true)
								continue;
							Arq.escreve("\"" + Recurso[1] + "\",");
							for (i = 2; i < iQtdIndicadores - 1; i++)
								Arq.escreve("\"\",");
						}
						Arq.escreveLN("");
					}

					Arq.escreve("\"\",");
					for (i = 0; i < p_ListaRecursos[1].size()-1; i++)
					{
						for (j = 2; j < iQtdIndicadores; j++)
						{
							Arq.escreve("\""+p_Indicadores.elementAt(j).toString()+"\"");
							if (j != iQtdIndicadores)
								Arq.escreve(",");
						}
					}
					Arq.escreveLN("");

					Periodo = Integer.parseInt(p_PeriodosApresentaveis.elementAt(z).toString());
					It[0] = p_ListaRecursos[0].iterator();
					while (It[0].hasNext())
					{
						Ent[0] = (Map.Entry)It[0].next();
						Obj = Ent[0].getValue();
						Recurso[0] = Obj.toString();
						if (Recurso[0].equals("Todos") == true)
							continue;

						Arq.escreve("\""+Recurso[0]+"\",");
						It[1] = p_ListaRecursos[1].iterator();
						while (It[1].hasNext())
						{
							Ent[1] = (Map.Entry)It[1].next();
							Obj = Ent[1].getValue();
							Recurso[1] = Obj.toString();
							if (Recurso[1].equals("Todos") == true)
								continue;

							Chaves.add(Recurso[0] + "-" + Recurso[1]);
							/*
                     Arq.escreve(",\""+Recurso[1]+"\"");
                     if (iQtdIndicadores != 3)
                     {
                        for (i = 2; i < iQtdIndicadores-1; i++)
                           Arq.escreve(",\"\"");
                     }
							 */                     
						}
						//Arq.escreveLN("");
						/*
                  if (iQtdIndicadores != 3)
                  {
                     Arq.escreve("\"\",");
                     for (i = 0; i < Chaves.size(); i++)
                     {
                        for (j = 2; j < iQtdIndicadores; j++)
                        {
                           Arq.escreve("\""+p_Indicadores.elementAt(j).toString()+"\"");
                           if (j != iQtdIndicadores)
                              Arq.escreve(",");
                        }
                     }
                     Arq.escreveLN("");
                  }
						 */                  
						//Arq.escreve("\"\",");            
						for (i = 0; i < Chaves.size(); i++)
						{
							ObjLinha = p_MapRelatorios[Periodo].get(Chaves.elementAt(i).toString());
							if (ObjLinha != null)
								LinhaRelProcessado = (Vector)ObjLinha;
							else
								LinhaRelProcessado = null;

							for (j = 2; j < iQtdIndicadores; j++)
							{
								Arq.escreve("\""+(LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(j) : "-") +"\"");
								if (j != iQtdIndicadores)
									Arq.escreve(",");
							}
						}
						Arq.escreveLN("");
						Chaves.clear();
					}

					Arq.escreveLN("");
					Logs = (Vector)p_Logs.elementAt(Periodo);
					//escreve logs no arquivo caso a chave p_CfgAg.get(3) seja iqual a 1.
					if((""+p_CfgAg.get(3)).equals("1"))
					{
						QtdLogs = Logs.size();
						if (QtdLogs != 0)
						{
							Arq.escreveLN("Logs:");
							for (j = 0; j < Logs.size(); j++)
								if (j == 0 && Logs.elementAt(j).toString().equals("nolog") == true)
									Arq.escreveLN("Não há logs para o este período");
								else
									Arq.escreveLN(Logs.elementAt(j).toString().substring(0,Logs.elementAt(j).toString().length()-1));
							Arq.escreveLN("");
						}
						else Arq.escreveLN("");
					}
				}
			}
			else if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
			{
				Arq.escreve("<html>\n<head>\n<title>CDRView | Visent</title>\n");
				Arq.escreve("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
				Arq.escreve("<meta http-equiv=\"Pragma\" content=\"no-cache\">\n</head>\n");

				Arq.escreve("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

				// Escreve cabeçalho
				Arq.escreve("<br>\n");
				Arq.escreve("<b><font face="+m_FONTE+">CDRView<sup>&reg;</sup> by Visent</b></font><br>\n");

				if (p_CfgAg.elementAt(2).equals("1") == true)
				{
					for (i = 0; i < p_Cabecalho.size(); i++)
						Arq.escreveLN("<font face="+m_FONTE+">"+ p_Cabecalho.elementAt(i).toString()+"</font><br>");
				}
				Arq.escreve("<br>\n");

				for (int z = 0; z < p_PeriodosApresentaveis.size(); z++)
				{
					Arq.escreve("<table width=\"100%\" border=\"0\" cellpadding=\"2\" cellspacing=\"1\">\n");            

					Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
					Periodo = Integer.parseInt(p_PeriodosApresentaveis.elementAt(z).toString());
					Arq.escreve("<td align=\"center\" colspan=\""+(p_ListaRecursos[1].size()*(iQtdIndicadores-2)+1)+"\">");
					if (p_NomesPeriodos != null)
						Arq.escreve("<font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Periodo:</b>&nbsp;"+p_NomesPeriodos.elementAt(Periodo).toString()+"</font>");
					else
						Arq.escreve("<font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Periodo:</b>&nbsp;"+p_Datas.elementAt(Periodo).toString()+"</font>");
					Arq.escreve("</td>");
					Arq.escreve("</tr>\n");

					Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
					Arq.escreve("<td bgcolor=\""+m_COR_HEADER+"\" align=\"center\" valign=\"middle\" nowrap rowspan=\"3\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+p_Recursos[0]+"</b></font></td>");

					Arq.escreve("<td align=\"center\" nowrap colspan=\""+p_ListaRecursos[1].size()*(iQtdIndicadores-2)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+p_Recursos[1]+"</b></font></td>");
					Arq.escreve("</tr>\n");

					//
					// Escreve a linha com os recursos de destino
					//
					Arq.escreve("<tr bgcolor=\""+m_COR_HEADER2+"\">\n");
					//Arq.escreveLN("<td bgcolor=\""+m_COR_FONTEHEADER+"\">&nbsp;</td>\n");
					It[1] = p_ListaRecursos[1].iterator();
					while (It[1].hasNext())
					{
						Ent[1] = (Map.Entry)It[1].next();
						Obj = Ent[1].getValue();
						Recurso[1] = Obj.toString();
						if (Recurso[1].equals(m_TODOS) == true)
						{
							bTemTodos = true;
							continue;
						}
						Arq.escreveLN("<td align=\"center\" nowrap colspan=\""+(iQtdIndicadores-2)+"\"><b><font face="+m_FONTE+ " color=\"" + m_COR_FONTEHEADER +"\">"+Recurso[1]+"</font><b></td>");
					}
					Arq.escreve("</tr>\n");

					//
					// Escreve a linha com os títulos dos indicadores presentes no relatório
					//
					Arq.escreve("<tr bgcolor=\""+m_COR_HEADER3+"\">\n");
					if (bTemTodos)
						iQtdRecursosTipo1 = p_ListaRecursos[1].size()-1;
					else
						iQtdRecursosTipo1 = p_ListaRecursos[1].size();

					for (j = 0; j < iQtdRecursosTipo1; j++)
					{
						for (i = 2; i < iQtdIndicadores; i++)
							Arq.escreveLN("<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER2+"\">"+p_Indicadores.elementAt(i).toString()+"</b></font></td>");
					}
					Arq.escreve("</tr>\n");         

					It[0] = p_ListaRecursos[0].iterator();
					while (It[0].hasNext())
					{
						Ent[0] = (Map.Entry)It[0].next();
						Obj = Ent[0].getValue();
						Recurso[0] = Obj.toString();
						if (Recurso[0].equals(m_TODOS) == true)
							continue;

						if ((iCont % 2) == 0)
							Cor = m_COR_LINHANAO;
						else
							Cor = m_COR_LINHASIM2;
						iCont++;
						Arq.escreve("<tr bgcolor=\""+Cor+"\">\n");
						Arq.escreveLN("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+Recurso[0]+"</font><b></td>");

						It[1] = p_ListaRecursos[1].iterator();
						while (It[1].hasNext())
						{
							Ent[1] = (Map.Entry)It[1].next();
							Obj = Ent[1].getValue();
							Recurso[1] = Obj.toString();
							if (Recurso[1].equals(m_TODOS) == true)
								continue;

							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(Periodo))).intValue()].get(Recurso[0] + "-" + Recurso[1]);
							if (ObjLinha != null)
								LinhaRelProcessado = (Vector)ObjLinha;
							else
								LinhaRelProcessado = null;

							//
							// Escreve os valores dos indicadores selecionados
							//
							if (this instanceof AgendaMatraf)
							{
								for (int w = 2; w < iQtdIndicadores; w++)
									Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(w) : "-") +"</font></td>");
							}
							else
							{
								for (i = 1; i < iQtdIndicadores-1; i++)
									Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(i) : "-") +"</font></td>");
							}
						}
						Arq.escreve("</tr>\n");
					}

					Arq.escreveLN("</table>\n");

					Arq.escreve("</td>\n");
					Arq.escreve("</tr>\n");
					Arq.escreve("</table>\n");

					if (p_CfgAg.elementAt(3).equals("1") == true)   // Log
					{
						Logs = (Vector)p_Logs.elementAt(z);
						if (Logs.elementAt(0).toString().equals("nolog") == false)
						{
							Arq.escreve("<table>\n");
							Arq.escreve("   <tr>\n      <td align=\"left\"><font face="+m_FONTE+"><b>Logs:</b></font></td>\n   </tr>\n");
							int TamLog = Logs.size();
							for (j = 0; j < TamLog; j++)
								Arq.escreve("   <tr>\n      <td><font face="+m_FONTE+">"+Logs.elementAt(j).toString().substring(0, Logs.elementAt(j).toString().length()-1)+"</font></td>\n   </tr>\n");
							Arq.escreve("</table>\n");
							Arq.escreve("<br>");
						}
					}

					Arq.escreve("</table>\n");
					if (z < p_PeriodosApresentaveis.size() - 1)
						Arq.escreve("<br>\n<br>\n");
				}
				Arq.escreve("\n</body>\n</html>");
			}
			Arq.fecha();
			m_GeraArqDownload = 2;
			return true;
		}
		else
		{
			m_GeraArqDownload = 3;
			return false;
		}
	}



	public boolean criaArquivoAnaliseCompletamento(String p_NomeArq, Vector p_Cabecalho, String p_TituloRecurso, Vector p_Indicadores, Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, int p_UltProcessado, Set p_ListaRecursos, Map p_MapRelatorios[], Vector p_Logs, int p_Nivel, Map p_IndicadoresAnaliseCompletamento)
	{
		Arquivo Arq = new Arquivo();   
		int i, j, k,Periodo, QtdPeridos, QtdIndicadores, QtdRelProcessado, QtdLogs;
		Vector Chaves, LinhaRelProcessado, Logs;
		String strLog, strColuna, Recurso, strTotal = "", Indicador;
		Iterator It;
		Map.Entry Ent;
		Object Obj, ObjLinha;
		String m_TODOS = "Todos";
		String strTipoCham[] = {"VC1", "LDN (VC2/VC3)", "LDI", "Total"};
		String strTipoChamArq[] = {"_VC1", "_LDN", "_LDI", ""};      
		String strClasseCham[] = {"Direto", "A Cobrar", "Total"};
		String strClasseChamArq[] = {"_DIRETO", "_COBRAR", ""};
		Integer Posicao = null;


		It = null;
		Ent = null;
		Recurso = null;
		Chaves = new Vector();

		Arq.setDiretorio(m_DirDownload);
		Arq.setNome(p_NomeArq);
		Arq.apaga();

		QtdIndicadores = p_Indicadores.size();
		if (Arq.abre('w') == true)
		{
			// Escreve cabeçalho
			for (i = 0; i < p_Cabecalho.size(); i++)
				Arq.escreveLN("\""+p_Cabecalho.elementAt(i).toString()+"\"");
			if (QtdIndicadores == 3)
				Arq.escreveLN("\"Coluna: "+p_Indicadores.elementAt(2).toString()+"\"");
			Arq.escreveLN("");

			for (int z = 0; z < p_PeriodosApresentaveis.size(); z++)
			{
				Periodo = Integer.parseInt(p_PeriodosApresentaveis.elementAt(z).toString());
				Arq.escreve("\"Periodo: ");
				if (p_NomesPeriodos != null)
					Arq.escreve(p_NomesPeriodos.elementAt(Periodo).toString());
				else
					Arq.escreve(p_Datas.elementAt(Periodo).toString());

				Arq.escreveLN("\"");

				switch (p_Nivel)
				{
				/**
				 * NIVEL 0
				 */
				case 0:
					try
					{
						// Linha com os Indicadores do relatório
						Arq.escreve("\""+p_TituloRecurso+"\"");
						for (i = 1; i < QtdIndicadores; i++)
							Arq.escreve(",\"" + p_Indicadores.elementAt(i).toString() + "\"");

						It = p_ListaRecursos.iterator();
						while (It.hasNext())
						{
							Ent = (Map.Entry)It.next();
							Obj = Ent.getValue();
							Recurso = Obj.toString();

							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(z))).intValue()].get(Recurso);
							if (ObjLinha != null)
								LinhaRelProcessado = (Vector)ObjLinha;
							else
								LinhaRelProcessado = null;

							if (Recurso.equals(m_TODOS) == true)
							{
								strTotal = "\"TOTAL\"";
								for (i = 1; i < QtdIndicadores; i++)
								{
									Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(p_Indicadores.elementAt(i));
									if (Posicao != null)
										strTotal += ",\"" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") + "\"";
									else
										strTotal += ",\"?\"";                  
								}
								continue;
							}

							Arq.escreveLN("");
							Arq.escreve("\""+Recurso+"\"");

							for (i = 1; i < QtdIndicadores; i++)
							{
								Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(p_Indicadores.elementAt(i));
								if (Posicao != null)
									Arq.escreve(",\"" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") + "\"");
								else
									Arq.escreve(",\"?\"");
							}
						}
						Arq.escreveLN("");
						Arq.escreve(strTotal);
						Arq.escreveLN("");
					}
					catch (Exception Exc)
					{
						System.out.println(m_ConexUtil.getDataHoraAtual() + " Agenda:criaArquivoAnaliseCompletamento() - Nivel 0 "+ Exc);
						Exc.printStackTrace();        
					}                  
					break;

					/**
					 * NIVEL 1
					 */
				case 1:
					try
					{
						// Linha com os Indicadores do relatório
						Arq.escreve("\""+p_TituloRecurso+"\",\"Tipo de Chamada\"");
						for (i = 1; i < QtdIndicadores; i++)
							Arq.escreve(",\"" + p_Indicadores.elementAt(i).toString() + "\"");
						Arq.escreveLN("");                        

						It = p_ListaRecursos.iterator();
						while (It.hasNext())
						{
							Ent = (Map.Entry)It.next();
							Obj = Ent.getValue();
							Recurso = Obj.toString();

							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(p_UltProcessado))).intValue()].get(Recurso);
							if (ObjLinha != null)
								LinhaRelProcessado = (Vector)ObjLinha;
							else
								LinhaRelProcessado = null;

							if (Recurso.equals(m_TODOS) == true)
							{
								strTotal = "\"TOTAL\",\"\"";
								for (i = 1; i < QtdIndicadores; i++)
								{
									Indicador = p_Indicadores.elementAt(i) + strTipoChamArq[3];
									Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);               
									if (Posicao != null)
										strTotal += ",\"" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") + "\"";
									else
									{
										strTotal += ",\"?\"";
									}
								}
								continue;
							}


							// **********************
							// Expandindo nivel 1
							// **********************

							Arq.escreve("\"" + Recurso +"\",");
							Arq.escreve("\"" + strTipoCham[0]+"\"");

							for (i = 1; i < QtdIndicadores; i++)
							{
								Indicador = p_Indicadores.elementAt(i) + strTipoChamArq[0];
								if (Indicador.indexOf("(%)") != -1 )
								{
									Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
									Indicador +=  strTipoChamArq[0] + "(%)";                     
								}               
								Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);               
								if (Posicao != null)
									Arq.escreve(",\"" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"\"");
								else
								{
									Arq.escreve(",\"?\"");
								}
							}
							Arq.escreveLN("");

							for (j = 0; j < 3; j++)
							{
								Arq.escreve("\"\",");
								Arq.escreve("\"" + strTipoCham[j+1] + "\"");                           
								for (i = 1; i < QtdIndicadores; i++)
								{
									Indicador = p_Indicadores.elementAt(i) + strTipoChamArq[j+1];                  
									if (Indicador.indexOf("(%)") != -1 )
									{
										Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
										Indicador +=  strTipoChamArq[j+1] + "(%)"; 
									}
									Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);                              
									if (Posicao != null)
										Arq.escreve(",\"" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"\"");                  
									else
									{
										Arq.escreve(",\"?\"");
									}
								}
								Arq.escreveLN("");
							}
						}
						Arq.escreve(strTotal);
						Arq.escreveLN("");
					}
					catch (Exception Exc)
					{
						System.out.println(m_ConexUtil.getDataHoraAtual() + " Agenda:criaArquivoAnaliseCompletamento() - Nivel 1 "+ Exc);
						Exc.printStackTrace();        
					}
					break;

					/**
					 * Nivel 2
					 */
				case 2:
				case 3:
					try
					{     
						// Linha com os Indicadores do relatório
						Arq.escreve("\""+ p_TituloRecurso + "\",");
						Arq.escreve("\"Tipo de Chamada\",");
						Arq.escreve("\"Classe da Chamada\"");
						for (i = 1; i < QtdIndicadores; i++)
							Arq.escreve(",\"" + p_Indicadores.elementAt(i).toString() + "\"");
						Arq.escreveLN("");

						It = p_ListaRecursos.iterator();
						while (It.hasNext())
						{
							Ent = (Map.Entry)It.next();
							Obj = Ent.getValue();
							Recurso = Obj.toString();

							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(p_UltProcessado))).intValue()].get(Recurso);
							if (ObjLinha != null)
								LinhaRelProcessado = (Vector)ObjLinha;
							else
								LinhaRelProcessado = null;

							if (Recurso.equals(m_TODOS) == true)
							{
								strTotal = "\"TOTAL\"";
								strTotal += ",\"\"";
								strTotal += ",\"\"";
								for (i = 1; i < QtdIndicadores; i++)
								{
									Indicador = p_Indicadores.elementAt(i).toString();
									Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);
									if (Posicao != null)
										strTotal += ",\"" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") + "\"";
									else
										strTotal += ",\"?\"";
								}
								continue;
							}


							// **********************
							// Expandindo nivel 2
							// **********************

							Arq.escreve("\""+Recurso+"\",");
							Arq.escreve("\""+strTipoCham[0]+"\",");
							Arq.escreve("\""+strClasseCham[0]+"\"");

							for (i = 1; i < QtdIndicadores; i++)
							{
								Indicador = p_Indicadores.elementAt(i) + strTipoChamArq[0] + strClasseChamArq[0];
								if (Indicador.indexOf("(%)") != -1 )
								{
									Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
									Indicador +=  strTipoChamArq[0] + strClasseChamArq[0] + "(%)";                     
								}               
								Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);               
								if (Posicao != null)
									Arq.escreve(",\""+(LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"\"");
								else
									Arq.escreve(",\"?\"");
							}
							Arq.escreveLN("");


							// Linha 2 e 3 do primeiro tipo de chamada
							for (j = 1; j < 3; j++)
							{
								Arq.escreve("\"\","); // Operadora
								Arq.escreve("\"\","); // Tipo de Chamada
								Arq.escreve("\""+strClasseCham[j]+"\"");            

								for (i = 1; i < QtdIndicadores; i++)
								{
									Indicador = p_Indicadores.elementAt(i) + strTipoChamArq[0] + strClasseChamArq[j];
									if (Indicador.indexOf("(%)") != -1 )
									{
										Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
										Indicador +=  strTipoChamArq[0] + strClasseChamArq[j] + "(%)";                     
									}               
									Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);               
									if (Posicao != null)
										Arq.escreve(",\""+(LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"\"");
									else
										Arq.escreve(",\"?\"");
								}
								Arq.escreveLN("");
							}          

							// Linhas restantes                                      
							for (j = 1; j < 3; j++) // For do Tipo de Chamada
							{
								Arq.escreve("\"\","); // Operadora
								Arq.escreve("\""+strTipoCham[j]+"\",");

								for (k = 0; k < 3; k++) // For da Classe da Chamada
								{  
									if (k > 0)
									{
										Arq.escreve("\"\","); // Operadora
										Arq.escreve("\"\","); // Tipo de Chamada
									}
									Arq.escreve("\""+strClasseCham[k]+"\"");               
									for (i = 1; i < QtdIndicadores; i++)
									{
										Indicador = p_Indicadores.elementAt(i) + strTipoChamArq[j] + strClasseChamArq[k];
										if (Indicador.indexOf("(%)") != -1 )
										{
											Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
											Indicador +=  strTipoChamArq[j] + strClasseChamArq[k] + "(%)"; 
										}
										Posicao = (Integer)p_IndicadoresAnaliseCompletamento.get(Indicador);                              
										if (Posicao != null)
											Arq.escreve(",\""+(LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"\"");                  
										else
											Arq.escreve(",\"?\"");
									}
									Arq.escreveLN("");
								}
							}
						}

						Arq.escreve(strTotal);
						Arq.escreveLN("");
					}
					catch (Exception Exc)
					{
						System.out.println(m_ConexUtil.getDataHoraAtual() + " Agenda:criaArquivoAnaliseCompletamento() - Nivel 2 "+ Exc);
						Exc.printStackTrace();        
					}
					break;
				default:
					System.out.println("Erro em Agenda.criaArquivoAnaliseCompletamento(). Nivel " + p_Nivel + " nao configurado.");
				return false;                  
				}

				// Escrevendo a Log
				Arq.escreveLN("");
				Logs = (Vector)p_Logs.elementAt(Periodo);
				QtdLogs = Logs.size();
				if (QtdLogs != 0)
				{
					Arq.escreveLN("Logs:");
					for (j = 0; j < Logs.size(); j++)
						if (j == 0 && Logs.elementAt(j).toString().equals("nolog") == true)
							Arq.escreveLN("Não há logs para o este período");
						else
							Arq.escreveLN(Logs.elementAt(j).toString().substring(0,Logs.elementAt(j).toString().length()-1));
					Arq.escreveLN("");
				}
				else Arq.escreveLN("");
			}

			Arq.fecha();
			m_GeraArqDownload = 2;
			return true;
		}
		else
		{
			m_GeraArqDownload = 3;
			return false;
		}
	}   

	/**
	 * Arquivo de download para os relatorios AnatelSMP3,5,6 e 7
	 */
	public void gravaArquivoAnatelSMP_Apresentacao1(Arquivo Arq, Vector p_Cabecalho, String p_TituloRecurso[], String p_Indicadores[], Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, Set p_ListaRecursos[], Map p_MapRelatorios[], Vector p_Logs, String p_TipoApresentacao, String p_TEXTOSMP[], Map p_IndicadoresAnatelSMP, String p_ExcluiLinhas, String p_Num_Den[], String p_strIndicador, String p_ContReferencia, Vector p_CfgAg)
	{
		boolean bLinhaZerada = false;
		int i, j, k, QtdPeridos, QtdIndicadores, QtdRelProcessado, QtdLogs;
		float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
		float afFPond[], afFPondTotal[], afPorcent[];
		Vector Chaves, LinhaRelProcessado[], Logs;
		String strLog, strColuna, strTotal = "", Indicador, Cor = "", p_TipoSalvamento;
		String m_TODOS = "Todos", NomePeriodo, Recurso = "", Recurso2 = "", Recurso3 = "";
		Integer Posicao;
		Iterator It[];
		Map.Entry Ent[];
		Object Obj, ObjLinha;


		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		It = new Iterator[3];
		Ent = new Map.Entry[3];
		Chaves = new Vector();
		LinhaRelProcessado = new Vector[p_PeriodosApresentaveis.size()];

		afAcumTotal  = new float [p_PeriodosApresentaveis.size()];
		afFPond      = new float [p_PeriodosApresentaveis.size()];
		afFPondTotal = new float [p_PeriodosApresentaveis.size()];
		afAcum       = new float [p_PeriodosApresentaveis.size()];
		afPorcent    = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
		{
			afAcum[i] = 0;
			afAcumTotal[i] = 0;            
			afFPondTotal[i] = 0;
			afPorcent[i] = 0;
		}      

		// Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
		afAcum = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			afAcum[i] = 0;

		if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
		{
			// Escreve cabeçalho
			for (i = 0; i < p_Cabecalho.size(); i++)
				Arq.escreveLN("\""+p_Cabecalho.elementAt(i).toString()+"\"");

			Arq.escreveLN("");

			// Coluna do período
			Arq.escreve("\"Período\",");
			// Coluna da central
			Arq.escreve("\""+p_TituloRecurso[1]+"\",");
			// Coluna da área            
			Arq.escreve("\""+p_TituloRecurso[0]+"\"");
			// Colunas dos indicadores
			for (k = 0; k < p_Indicadores.length; k++)
				Arq.escreve(",\""+p_Indicadores[k]+"\"");
			Arq.escreveLN("");

			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (Recurso.equals(m_TODOS))
						continue;

					for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
					{
						// Recupera a linha de indicadores
						ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
						if (ObjLinha != null)
							LinhaRelProcessado[0] = (Vector)ObjLinha;
						else
							LinhaRelProcessado[0] = null;

						//
						// Não envia as linhas que tem a qtd de chamadas igual a ZERO
						//
						bLinhaZerada = false;
						if (p_ExcluiLinhas != null && p_ExcluiLinhas.equals("true") && 
								LinhaRelProcessado[0].elementAt(0).toString().startsWith("0") == true)
						{
							bLinhaZerada = true;
							continue;
						}

						// Período
						if (p_NomesPeriodos != null) 
							NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
						else 
							NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
						Arq.escreve("\""+NomePeriodo+"\",");

						// Recurso bilhetador
						Arq.escreve("\""+Recurso2+"\",");

						// Recurso área
						Arq.escreve("\""+Recurso+"\"");

						// Indicadores
						fNum = fDen = 0;
						for (k = 0; k < p_Indicadores.length; k++)
						{
							Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[k]);
							if (Posicao == null)
								System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[k]+")");
							else
							{
								Arq.escreve(",\""+LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString()+"\"");

								if (i == 0 && p_Indicadores[k].equals("F. Pond."))
								{
									try
									{
										fFatorPond = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
										fTotalFatorPond += fFatorPond;
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}

								if (p_Indicadores[k].equals(p_Num_Den[0]))
								{
									try
									{
										fNum = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}
								if (p_Indicadores[k].equals(p_Num_Den[1]))
								{
									try
									{
										fDen = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}
							}
						}

						if (fDen != 0)
							afAcum[i] += (fNum/fDen)*fFatorPond;
						else
							afAcum[i] += 0;

						// Fim da linha
						Arq.escreveLN("");
					}
					if (!bLinhaZerada)
						Arq.escreveLN("");
				}               
			}

			//
			// Resumos
			//

			// Títulos
			Arq.escreveLN("");
			Arq.escreveLN("\"Resumo - Indicador "+p_strIndicador+"\"");
			Arq.escreve("\"Períodos\",");
			Arq.escreve("\"Indicador\"");
			Arq.escreveLN("");
			//

			// Períodos e indicadores resumidos
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreve("\""+NomePeriodo+"\"");

				// Coluna do indicador resumido
				float fRes = (afAcum[i]/fTotalFatorPond)*((float)100);
				String strRes = new java.text.DecimalFormat("##.##").format(fRes);
				Arq.escreveLN(",\""+strRes+"\"");
			}
		}
		else if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
		{
			// Cabeçalho da tabela - início
			Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");

			// Coluna do período
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Per&iacute;odo"
					+"</b></font></td>");

			// Coluna da central
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[1]
					                 +"</b></font></td>");

			// Coluna da área
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[0]
					                 +"</b></font></td>");

			// Colunas dos indicadores
			for (k = 0; k < p_Indicadores.length; k++)
			{
				Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
						+p_Indicadores[k]
						               +"</b></font></td>");
			}

			// Cabeçalho da tabela - fim
			Arq.escreve("\n</tr>\n");

			j = 0;
			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (Recurso.equals(m_TODOS))
						continue;

					bLinhaZerada = false;
					for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
					{
						// Recupera a linha de indicadores
						ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
						if (ObjLinha != null)
							LinhaRelProcessado[0] = (Vector)ObjLinha;
						else
							LinhaRelProcessado[0] = null;

						//
						// Não envia as linhas que tem a qtd de chamadas igual a ZERO
						// (Sai do laço for)
						//
						if (p_ExcluiLinhas != null && 
								p_ExcluiLinhas.equals("true"))
						{
							Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
							if (Posicao == null)
								System.out.println(">>> Posicao NAO encontrada ("+p_ContReferencia+")");
							else
							{
								if (LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString().startsWith("0") == true)
								{
									bLinhaZerada = true;
									break;
								}
							}
						}

						// Seleciona a cor de apresentação da linha
						if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
						else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
						j++;

						// Início da linha
						Arq.escreve("<tr"+Cor+">\n   ");

						// Período
						if (p_NomesPeriodos != null)
							NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
						else
							NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
						Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+NomePeriodo+"</font></td>");

						// Recurso bilhetador
						Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso2+"</font></td>");

						// Recurso área
						Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso+"</font></td>");

						// Indicadores
						fNum = fDen = 0;
						for (k = 0; k < p_Indicadores.length; k++)
						{
							Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[k]);
							if (Posicao == null)
								Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">??</font></td>");
							else
							{
								Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString()+"</font></td>");

								if (i == 0 && p_Indicadores[k].equals("F. Pond."))
								{
									try
									{
										Integer PosicaoAux;
										PosicaoAux = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
										if (PosicaoAux != null)
										{
											if (LinhaRelProcessado[0].elementAt(PosicaoAux.intValue()).toString().startsWith("0") == false)
											{
												fFatorPond = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
												fTotalFatorPond += fFatorPond;                                    
											}
										}
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}

								if (p_Indicadores[k].equals(p_Num_Den[0]))
								{
									try
									{
										fNum = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}

								if (p_Indicadores[k].equals(p_Num_Den[1]))
								{
									try
									{
										fDen = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}

								if (p_Indicadores[k].equals(p_strIndicador))
								{
									try
									{
										String strAux = LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString();
										strAux = strAux.replace(',','.');
										afPorcent[i] = Float.parseFloat(strAux);
									}
									catch (Exception Exc)
									{
										System.out.println("Erro na conversao do indicador");
									}
								}
							}
						}

						if (afPorcent[i] != 0) afAcum[i] += (afPorcent[i]/100) *fFatorPond;
						else afAcum[i] += 0;

						// Fim da linha
						Arq.escreve("\n</tr>\n");
					}

					// Seleciona a cor de apresentação da linha
					if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
					else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
					j++;

					if (!bLinhaZerada)
					{
						// Início da linha
						Arq.escreve("<tr"+Cor+">\n   ");
						Arq.escreve("<td colspan=\""+(p_Indicadores.length + 3)+"\">&nbsp;</td>");
						Arq.escreve("</tr>\n");
					}
				}               
			}

			//       escreve logs no arquivo caso a chave p_CfgAg.get(3) seja iqual a 1.
			if((""+p_CfgAg.get(3)).equals("1"))
			{
				Vector l_LogPeriodo = null;
				for (i = 0; i < p_Logs.size(); i++)
				{
					l_LogPeriodo = (Vector) p_Logs.elementAt(i);
					for (int w=0; w< l_LogPeriodo.size(); w++)
					{
						Arq.escreveLN("\n<tr><font face=\"verdana\" size=\"1\"><td colspan=\""+(3+p_PeriodosApresentaveis.size())+"\">"+l_LogPeriodo.elementAt(w)+"</td></font></tr>");    
					}
				}
			}
			//
			// Resumo
			//

			// Títulos
			Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreve("<td align=\"center\" nowrap colspan=\""+(3+p_Indicadores.length)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Resumo - Indicador "+p_strIndicador
					+"</b></font></td>");
			Arq.escreve("\n</tr>\n");

			Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Per&iacute;odos"
					+"</b></font></td>");

			Arq.escreve("<td align=\"right\" nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Indicador"
					+"</b></font></td>");
			Arq.escreve("\n</tr>\n");
			//

			// Períodos e indicadores resumidos
			int iPos;
			String strAux, strRes;
			j = 0;
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				iPos = 0;
				strAux = "";
				strRes = "";

				// Seleciona a cor de apresentação da linha
				if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
				else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
				j++;

				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreve("<tr"+ Cor+">\n   ");
				// Coluna do período
				Arq.escreve("<td align=\"center\"><font face="+m_FONTE+"><b>"
						+NomePeriodo
						+"</b></font></td>");

				// Coluna do indicador resumido
				if (afAcum[i] != 0 && fTotalFatorPond != 0)
				{
					float fRes = (afAcum[i]/fTotalFatorPond)*((float)100);

					strRes = new java.text.DecimalFormat("##.##").format(fRes);

					iPos = strRes.indexOf('.');
					if (iPos != -1)
						strAux = strRes.substring(iPos+1, strRes.length());
					else 
					{
						iPos = strRes.indexOf(',');
						if (iPos != -1)
							strAux = strRes.substring(iPos+1, strRes.length());
					}

					if (strAux.length() == 1)
					{
						strAux += "0";
						strRes = strRes.substring(0, iPos+1) + strAux;
					}
				}
				else
					strRes = "--";

				Arq.escreve("<td align=\"right\" nowrap nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+"><b>"
						+ strRes
						+"</b></font></td>");                             
				Arq.escreve("\n</tr>\n");
			}
			Arq.escreve("</table>\n");
			Arq.escreve("\n</body>\n</html>");
		}
	}

	public void gravaArquivoAnatelSMP_Apresentacao2(Arquivo Arq, Vector p_Cabecalho, String p_TituloRecurso[], String p_Indicadores[], Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, Set p_ListaRecursos[], Map p_MapRelatorios[], Vector p_Logs, String p_TipoApresentacao, String p_TEXTOSMP[], Map p_IndicadoresAnatelSMP, String p_ExcluiLinhas, String p_Num_Den[], String p_strIndicador, String p_ContReferencia, Vector p_CfgAg)
	{
		boolean bLinhaZerada = false;
		int i, j, k, QtdPeridos, QtdIndicadores, QtdRelProcessado, QtdLogs;
		float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
		float afFPond[], afFPondTotal[], afPorcent[];
		Vector Chaves, LinhaRelProcessado[], Logs;
		String strLog, strColuna, strTotal = "", Indicador, Cor = "", p_TipoSalvamento;
		String m_TODOS = "Todos", NomePeriodo, Recurso = "", Recurso2 = "";      
		Integer Posicao;
		Iterator It[];
		Map.Entry Ent[];
		Object Obj, ObjLinha;


		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		It = new Iterator[2];
		Ent = new Map.Entry[2];
		Chaves = new Vector();
		LinhaRelProcessado = new Vector[p_PeriodosApresentaveis.size()];

		afAcumTotal  = new float [p_PeriodosApresentaveis.size()];
		afFPond      = new float [p_PeriodosApresentaveis.size()];
		afFPondTotal = new float [p_PeriodosApresentaveis.size()];         
		afAcum       = new float [p_PeriodosApresentaveis.size()];
		afPorcent    = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
		{
			afAcum[i] = 0;
			afAcumTotal[i] = 0;            
			afFPondTotal[i] = 0;
			afPorcent[i] = 0;
		}      

		// Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
		afAcum = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			afAcum[i] = 0;

		if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
		{
			//afFPond   = new float[p_PeriodosApresentaveis.size()];
			//afPorcent = new float[p_PeriodosApresentaveis.size()];

			// Escreve cabeçalho
			for (i = 0; i < p_Cabecalho.size(); i++)
				Arq.escreveLN("\""+p_Cabecalho.elementAt(i).toString()+"\"");

			Arq.escreveLN("");

			// Coluna da central
			Arq.escreve("\""+p_TituloRecurso[1]+"\",");
			// Coluna da área
			Arq.escreve("\""+p_TituloRecurso[0]+"\",");
			// Coluna do título do indicador
			Arq.escreve("\" \"");
			// Colunas dos períodos
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				Arq.escreve(",\""+NomePeriodo+"\"");
			}
			Arq.escreveLN("");
			// 

			int QtdLinhas;
			//
			// Percorre lista de bilhetadores
			//
			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				int Cont = 0;
				j = 0;
				//
				// Percorre lista de áreas
				//
				QtdLinhas = p_ListaRecursos[0].size();
				if (p_ExcluiLinhas.equals("true"))
				{
					It[0] = p_ListaRecursos[0].iterator();
					while (It[0].hasNext())
					{
						Ent[0] = (Map.Entry)It[0].next();
						Obj = Ent[0].getValue();
						Recurso = Obj.toString();

						if (Recurso.equals(m_TODOS))
							continue;

						bLinhaZerada = true;
						for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
						{
							// Recupera a linha de indicadores
							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
							if (ObjLinha != null)
							{
								LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

							}
							else
							{
								LinhaRelProcessado[i] = null;
							}
						}
						//aqui vai ser feita a verificacao se as linhas do relatorio sao todas '0',
						//para essa linha desse recurso ser retirada.
						for(int z=0; z<LinhaRelProcessado.length; z++)
						{
							if(LinhaRelProcessado[z]!=null){
								for(int w=0;w<LinhaRelProcessado[z].size();w++)
								{
									if (LinhaRelProcessado[z].elementAt(w).toString().equals("0") == true ||
											LinhaRelProcessado[z].elementAt(w).toString().equals("0.0") == true ||
											LinhaRelProcessado[z].elementAt(w).toString().equals("0.00") == true	)
									{
										bLinhaZerada = true;
									}
									else
									{
										bLinhaZerada = false;
										break;
									} 
								}
								if(bLinhaZerada==false)
								{
									break;
								}
							}
						}
						if (bLinhaZerada)
							QtdLinhas--;
					}
				}

				// Coluna da central
				if (p_ExcluiLinhas.equals("true"))
				{
					if (QtdLinhas != 1)
						Arq.escreve("\""+Recurso2+"\",");
					else
						continue;                                   
				}
				else
					Arq.escreve("\""+Recurso2+"\",");

				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (Recurso.equals(m_TODOS))
						continue;

					bLinhaZerada = true;
					for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
					{
						// Recupera a linha de indicadores
						ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
						if (ObjLinha != null)
						{
							LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

						}
						else
							LinhaRelProcessado[i] = null;
					}
					//aqui vai ser feita a verificacao se as linhas do relatorio sao todas '0',
					//para essa linha desse recurso ser retirada.
					for(int z=0;z<LinhaRelProcessado.length;z++)
					{
						if(LinhaRelProcessado[z] != null)
							for(int l=0;l<LinhaRelProcessado[z].size();l++)
							{
								if (LinhaRelProcessado[z].elementAt(l).toString().equals("0") == true ||
										LinhaRelProcessado[z].elementAt(l).toString().equals("0.0") == true ||
										LinhaRelProcessado[z].elementAt(l).toString().equals("0.00") == true	)
								{
									bLinhaZerada = true;
								}
								else
								{
									bLinhaZerada = false;
									break;
								}		               	
							}
						if(bLinhaZerada==false)
						{
							break;
						}
					}

					if (p_ExcluiLinhas.equals("true") && (bLinhaZerada))
						continue;

					if (Cont == 0)
					{
						Arq.escreve("\""+Recurso+"\",");

						for (i = 0; i < p_TEXTOSMP.length; i++)
						{
							if (i == 0)
								Arq.escreve("\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador : p_TEXTOSMP[i])+"\"");
							else
								Arq.escreve("\"\",\"\",\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador : p_TEXTOSMP[i])+"\"");

							//
							// Calcula o SMP ponderado
							//
							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{
								Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
								if (Posicao != null && LinhaRelProcessado[k] != null)
								{	
									String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
									strAux = strAux.replace(',','.');
									afPorcent[k] = Float.parseFloat(strAux);
								}
								else
									afPorcent[k] = 0;

								Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
								if (Posicao != null && LinhaRelProcessado[k] != null)
								{
									afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
									fTotalFatorPond += afFPond[k];
								}
								else
									afFPond[k] = 0;

								afAcum[k] = (afPorcent[k] * afFPond[k])/100;
								afAcumTotal[k] += (afPorcent[k] * afFPond[k])/100;
								if (afPorcent[k] != 0)
									afFPondTotal[k] += afFPond[k];                           
							}

							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
								if (Posicao == null)
								{
									Arq.escreve(",\"??\"");
									System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
								}
								else
								{
									if (p_TEXTOSMP[i].equals("Fator de pondera&ccedil;&atilde;o"))
										Arq.escreve(",\""+new java.text.DecimalFormat("##").format(afAcum[k])+"\"");
									else if( LinhaRelProcessado[k] != null)
										Arq.escreve(",\""+LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString()+"\"");
								}
							}

							if (i != p_TEXTOSMP.length-1)
								Arq.escreveLN("");
						}
						Arq.escreveLN("");
					}
					else
					{
						Arq.escreve("\"\",\""+Recurso+"\",");

						for (i = 0; i < p_TEXTOSMP.length; i++)
						{
							if (i == 0)
								Arq.escreve("\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])+"\"");
							else
								Arq.escreve("\"\",\"\",\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])+"\"");                           

							//
							// Calcula o SMP ponderado
							//
							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{                        
								Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
								if (Posicao != null && LinhaRelProcessado[k] != null)
								{
									String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
									strAux = strAux.replace(',','.');
									afPorcent[k] = Float.parseFloat(strAux);
								}
								else
									afPorcent[k] = 0;

								Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
								if (Posicao != null && LinhaRelProcessado[k] != null)
								{
									afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
									fTotalFatorPond += afFPond[k];
								}
								else
									afFPond[k] = 0;

								afAcum[k] = (afPorcent[k] * afFPond[k])/100;
								afAcumTotal[k] += (afPorcent[k] * afFPond[k])/100;
								if (afPorcent[k] != 0)
									afFPondTotal[k] += afFPond[k];
							}

							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
								if (Posicao == null)
								{
									Arq.escreve(",\"??\"");
									System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
								}
								else
								{
									if (p_TEXTOSMP[i].equals("Fator de pondera&ccedil;&atilde;o"))
										Arq.escreve(",\""+new java.text.DecimalFormat("##").format(afAcum[k])+"\"");
									else if(LinhaRelProcessado[k] != null)
										Arq.escreve(",\""+LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString()+"\"");
								}
							}
							Arq.escreveLN("");                        

							//if (i != p_TEXTOSMP.length-1)
							//   Arq.escreveLN("");
						}
					}
					Cont++;
				}
				Arq.escreveLN("");

				Arq.escreveLN("");
			}

			//       escreve logs no arquivo caso a chave p_CfgAg.get(3) seja iqual a 1.
			if((""+p_CfgAg.get(3)).equals("1"))
			{
				for (i = 0; i < p_Logs.size(); i++)
					Arq.escreveLN("\""+p_Logs.elementAt(i).toString()+"\"");
			}

			//
			// Resumo
			//

			// Títulos
			Arq.escreve("\"Resumo - Indicador "+p_strIndicador+"\"");
			Arq.escreveLN("");

			Arq.escreve("\"Períodos\",");
			Arq.escreve("\"Indicador\"");
			Arq.escreveLN("");
			//

			// Períodos e indicadores resumidos
			int iPos;
			String strAux, strRes;            
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				iPos = 0;
				strAux = "";
				strRes = "";

				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreve("\""+NomePeriodo+"\",");

				// Coluna do indicador resumido
				float fRes = (afAcumTotal[i]/afFPondTotal[i])*((float)100);
				strRes = new java.text.DecimalFormat("##.##").format(fRes);
				iPos = strRes.indexOf('.');
				if (iPos != -1)
					strAux = strRes.substring(iPos+1, strRes.length());
				else 
				{
					iPos = strRes.indexOf(',');
					if (iPos != -1)
						strAux = strRes.substring(iPos+1, strRes.length());
				}

				if (strAux.length() == 1)
				{
					strAux += "0";
					strRes = strRes.substring(0, iPos+1) + strAux;
				}                  

				Arq.escreveLN("\""+strRes+"\"");
			}
		}
		else if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
		{
			//afFPond   = new float[p_PeriodosApresentaveis.size()];
			//afPorcent = new float[p_PeriodosApresentaveis.size()];

			// Cabeçalho da tabela - início
			Arq.escreveLN("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");

			// Coluna da central
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[1]
					                 +"</b></font></td>");

			// Coluna da área
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[0]
					                 +"</b></font></td>");

			// Coluna do título do indicador
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"&nbsp;"
					+"</b></font></td>");

			// Colunas dos períodos
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+NomePeriodo+"</b></font></td>");
			}
			Arq.escreveLN("</tr>\n");
			// Cabeçalho da tabela - fim

			int QtdLinhas;
			//
			// Percorre lista de bilhetadores
			//
			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				int Cont = 0;
				j = 0;
				//
				// Percorre lista de áreas
				//
				QtdLinhas = p_ListaRecursos[0].size();
				if (p_ExcluiLinhas.equals("true"))
				{
					It[0] = p_ListaRecursos[0].iterator();
					while (It[0].hasNext())
					{
						Ent[0] = (Map.Entry)It[0].next();
						Obj = Ent[0].getValue();
						Recurso = Obj.toString();

						if (Recurso.equals(m_TODOS))
							continue;

						bLinhaZerada = true;
						for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
						{
							// Recupera a linha de indicadores
							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
							if (ObjLinha != null)
							{
								LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
								if (Posicao != null)
								{
									if (LinhaRelProcessado[i].elementAt(Posicao.intValue()).toString().startsWith("0") == false)
									{
										bLinhaZerada = false;
										break;
									}
								}
							}
							else
								LinhaRelProcessado[i] = null;
						}

						if (bLinhaZerada)
							QtdLinhas--;
					}
				}

				// Coluna da central
				if (p_ExcluiLinhas.equals("true"))
				{
					if (QtdLinhas != 1)
					{
						Arq.escreveLN("<tr>\n   ");
						Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (p_TEXTOSMP.length))+"\"><font face="+m_FONTE+"><b>"
								+Recurso2
								+"</b></font></td>");
					}
					else
						continue;                                   
				}
				else
					Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (p_TEXTOSMP.length))+"\"><font face="+m_FONTE+"><b>"
							+Recurso2
							+"</b></font></td>");

				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (Recurso.equals(m_TODOS))
						continue;

					bLinhaZerada = true;
					for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
					{
						// Recupera a linha de indicadores
						ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
						if (ObjLinha != null)
						{
							LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

							Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
							if (Posicao != null)
							{
								if (LinhaRelProcessado[i].elementAt(Posicao.intValue()).toString().startsWith("0") == false)
									bLinhaZerada = false;
							}
						}
						else{
							// Foi alterado a forma de tratamento das linha vazias vindas do arquivo. Caso ele não contenha dados será atribuído o valor 0(zero).
							//
							LinhaRelProcessado[i] = new Vector();
							LinhaRelProcessado[i].add("0");
							LinhaRelProcessado[i].add("0");
							LinhaRelProcessado[i].add("0");
							LinhaRelProcessado[i].add("0");
							//LinhaRelProcessado[i] = null;
						}
							
					}

					if (p_ExcluiLinhas.equals("true") && (bLinhaZerada))
						continue;

					if (Cont != 0)
					{
						// Seleciona a cor de apresentação da linha
						if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHASIM2+"\"";
						else Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
						j++;

						Arq.escreveLN("<tr"+Cor+">\n");
					}

					if (Cont == 0)
					{
						Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap rowspan=\""+p_TEXTOSMP.length+"\"><font face="+m_FONTE+"><b>"
								+Recurso
								+"</b></font></td>");

						//
						// Calcula o SMP ponderado
						//
						for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
						{                        
							Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
							if (Posicao != null && LinhaRelProcessado[k] != null)
							{
								String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
								strAux = strAux.replace(',','.');
								afPorcent[k] = Float.parseFloat(strAux);
							}
							else
								afPorcent[k] = 0;

							Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
							
							if (Posicao != null && LinhaRelProcessado[k] != null)
							{
								afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
								fTotalFatorPond += afFPond[k];
							}
							else
								afFPond[k] = 0;

							afAcum[k] = (afPorcent[k]/100) * afFPond[k];
							afAcumTotal[k] += (afPorcent[k]/100) * afFPond[k];

							if (afPorcent[k] != 0)
								afFPondTotal[k] += afFPond[k];                           
							//else System.out.println("2)->Porc = ZERO");
						}

						for (i = 0; i < p_TEXTOSMP.length; i++)
						{
							if (i != 0)
								Arq.escreveLN("<tr>\n");
							Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"left\"><font face="+m_FONTE+">"
									+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])
									+"</font></td>");

							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
								if (Posicao == null)
								{
									Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">??</font></td>");
									System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
								}
								else if(LinhaRelProcessado[k] != null)
								{
									Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap><font face="+m_FONTE+">"
											+LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString()
											+"</font></td>");
								}
							}

							if (i != p_TEXTOSMP.length-1)
								Arq.escreveLN("\n</tr>\n");
						}
						Arq.escreveLN("\n</tr>\n");
					}
					else
					{
						Arq.escreveLN("<td align=\"center\" nowrap rowspan=\""+p_TEXTOSMP.length+"\"><font face="+m_FONTE+"><b>"
								+Recurso
								+"</b></font></td>");

						//
						// Calcula o SMP ponderado
						//
						for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
						{                        
							Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
							if (Posicao != null && LinhaRelProcessado[k] != null)
							{
								String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
								strAux = strAux.replace(',','.');
								afPorcent[k] = Float.parseFloat(strAux);
							}
							else
								afPorcent[k] = 0;

							Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
							if (Posicao != null && LinhaRelProcessado[k] != null)
							{
								afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
								fTotalFatorPond += afFPond[k];
							}
							else
								afFPond[k] = 0;

							afAcum[k] = (afPorcent[k]/100) * afFPond[k];
							afAcumTotal[k] += (afPorcent[k]/100) * afFPond[k];

							if (afPorcent[k] != 0)
								afFPondTotal[k] += afFPond[k];
						}

						for (i = 0; i < p_TEXTOSMP.length; i++)
						{
							if (i != 0)
								Arq.escreveLN("<tr "+Cor+">\n");
							Arq.escreveLN("<td align=\"left\"><font face="+m_FONTE+">"
									+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])
									+"</font></td>");

							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
								if (Posicao == null)
								{
									Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">??</font></td>");
									System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
								}
								else if( LinhaRelProcessado[k] != null)
								{
									Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">"
											+LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString()
											+"</font></td>");
								}
							}

							if (i != p_TEXTOSMP.length-1)
								Arq.escreveLN("\n</tr>\n");
						}
					}
					Cont++;
				}
				Arq.escreveLN("\n</tr>\n");

				Arq.escreveLN("\n<tr>\n");
				Arq.escreveLN("<td colspan=\""+(3+p_PeriodosApresentaveis.size())+"\">&nbsp;</td>");
				Arq.escreveLN("\n</tr>\n");
			}

			//       escreve logs no arquivo caso a chave p_CfgAg.get(3) seja iqual a 1.
			if((""+p_CfgAg.get(3)).equals("1"))
			{
				Vector l_LogPeriodo = null;
				for (i = 0; i < p_Logs.size(); i++)
				{
					l_LogPeriodo = (Vector) p_Logs.elementAt(i);
					for (int w=0; w< l_LogPeriodo.size(); w++)
					{
						Arq.escreveLN("\n<tr><td colspan=\""+(3+p_PeriodosApresentaveis.size())+"\"><font face=\"verdana\" size=\"1\">"+l_LogPeriodo.elementAt(w)+"</font></td></tr>");    
					}
				}
			}
			//
			// Resumo
			//

			// Títulos
			Arq.escreveLN("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreveLN("<td align=\"center\" nowrap colspan=\""+(3+p_PeriodosApresentaveis.size())+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Resumo - Indicador "+p_strIndicador
					+"</b></font></td>");
			Arq.escreveLN("\n</tr>\n");

			Arq.escreveLN("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Per&iacute;odos"
					+"</b></font></td>");

			Arq.escreveLN("<td align=\"right\" nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Indicador"
					+"</b></font></td>");
			Arq.escreveLN("\n</tr>\n");
			//

			// Períodos e indicadores resumidos - Apresentacao = 2
			int iPos;
			String strAux, strRes;
			j = 0;
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				iPos = 0;
				strAux = "";
				strRes = "";

				// Seleciona a cor de apresentação da linha
				if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
				else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
				j++;

				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreveLN("<tr"+ Cor+">\n   ");
				// Coluna do período
				Arq.escreveLN("<td align=\"center\"><font face="+m_FONTE+"><b>"
						+NomePeriodo
						+"</b></font></td>");

				// Coluna do indicador resumido
				if (afAcumTotal[i] != 0 && afFPondTotal[i] != 0)
				{
					float fRes = (afAcumTotal[i]/afFPondTotal[i])*((float)100);

					strRes = new java.text.DecimalFormat("##.##").format(fRes);
					iPos = strRes.indexOf('.');
					if (iPos != -1)
						strAux = strRes.substring(iPos+1, strRes.length());
					else 
					{
						iPos = strRes.indexOf(',');
						if (iPos != -1)
							strAux = strRes.substring(iPos+1, strRes.length());
					}

					if (strAux.length() == 1)
					{
						strAux += "0";
						strRes = strRes.substring(0, iPos+1) + strAux;
					}
				}
				else
					strRes = "--";

				Arq.escreve("<td align=\"right\" nowrap nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+"><b>"
						+strRes
						+"</b></font></td>");                             
				Arq.escreve("\n</tr>\n");
			}
			Arq.escreve("</table>\n");
			Arq.escreve("\n</body>\n</html>");
		}
	}   

	public void gravaArquivoAnatelLDN_Apresentacao1(Arquivo Arq, Vector p_Cabecalho, String p_TituloRecurso[], String p_Indicadores[], Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, Set p_ListaRecursos[], Map p_MapRelatorios[], Vector p_Logs, String p_TipoApresentacao, String p_TEXTOSMP[], Map p_IndicadoresAnatelSMP, String p_ExcluiLinhas, String p_Num_Den[], String p_strIndicador, String p_ContReferencia, Vector p_CfgAg)
	{
		boolean bLinhaZerada = false;
		int i, j, k, QtdPeridos, QtdIndicadores, QtdRelProcessado, QtdLogs;
		float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
		float afFPond[], afFPondTotal[], afPorcent[];
		Vector Chaves, LinhaRelProcessado[], Logs;
		String strLog, strColuna, strTotal = "", Indicador, Cor = "", p_TipoSalvamento;
		String m_TODOS = "Todos", NomePeriodo, Recurso = "", Recurso2 = "", Recurso3 = "";
		Integer Posicao;
		Iterator It[];
		Map.Entry Ent[];
		Object Obj, ObjLinha;


		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		It = new Iterator[3];
		Ent = new Map.Entry[3];
		Chaves = new Vector();
		LinhaRelProcessado = new Vector[p_PeriodosApresentaveis.size()];

		afAcumTotal  = new float [p_PeriodosApresentaveis.size()];
		afFPond      = new float [p_PeriodosApresentaveis.size()];
		afFPondTotal = new float [p_PeriodosApresentaveis.size()];
		afAcum       = new float [p_PeriodosApresentaveis.size()];
		afPorcent    = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
		{
			afAcum[i] = 0;
			afAcumTotal[i] = 0;            
			afFPondTotal[i] = 0;
			afPorcent[i] = 0;
		}      

		// Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
		afAcum = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			afAcum[i] = 0;

		if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
		{
			// Escreve cabeçalho
			for (i = 0; i < p_Cabecalho.size(); i++)
				Arq.escreveLN("\""+p_Cabecalho.elementAt(i).toString()+"\"");

			Arq.escreveLN("");

			// Coluna do período
			Arq.escreve("\"Período\",");
			// Coluna da central
			Arq.escreve("\""+p_TituloRecurso[1]+"\",");
			// Coluna da área            
			Arq.escreve("\""+p_TituloRecurso[0]+"\",");
			// Coluna do destino (CSP)
			Arq.escreve("\""+p_TituloRecurso[2]+"\"");
			// Colunas dos indicadores
			for (k = 0; k < p_Indicadores.length; k++)
				Arq.escreve(",\""+p_Indicadores[k]+"\"");
			Arq.escreveLN("");

			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (Recurso.equals(m_TODOS))
						continue;

					It[2] = p_ListaRecursos[2].iterator();
					while (It[2].hasNext())
					{
						Ent[2] = (Map.Entry)It[2].next();
						Obj = Ent[2].getValue();
						Recurso3 = Obj.toString();

						if (Recurso3.equals(m_TODOS))
							continue;

						for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
						{
							// Recupera a linha de indicadores
							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
							if (ObjLinha != null)
								LinhaRelProcessado[0] = (Vector)ObjLinha;
							else
								LinhaRelProcessado[0] = null;

							//
							// Não envia as linhas que tem a qtd de chamadas igual a ZERO
							//
							bLinhaZerada = false;
							if (p_ExcluiLinhas != null && p_ExcluiLinhas.equals("true") && 
									LinhaRelProcessado[0] != null && LinhaRelProcessado[0].elementAt(0).toString().startsWith("0") == true)
							{
								bLinhaZerada = true;
								continue;
							}

							// Período
							if (p_NomesPeriodos != null) 
								NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
							else 
								NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
							Arq.escreve("\""+NomePeriodo+"\",");

							// Recurso bilhetador
							Arq.escreve("\""+Recurso2+"\",");

							// Recurso área
							Arq.escreve("\""+Recurso+"\",");

							// Recurso destino
							Arq.escreve("\""+Recurso3+"\"");

							// Indicadores
							fNum = fDen = 0;
							for (k = 0; k < p_Indicadores.length; k++)
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[k]);
								if (Posicao == null)
									System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[k]+")");
								else
								{
									Arq.escreve(",\""+(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "-")+"\"");

									if (i == 0 && p_Indicadores[k].equals("F. Pond."))
									{
										try
										{
											if (LinhaRelProcessado[0] != null)
											{
												fFatorPond = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
												fTotalFatorPond += fFatorPond;
											}
											else
											{
												fFatorPond = 0;
												fTotalFatorPond += fFatorPond;
											}
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}

									if (p_Indicadores[k].equals(p_Num_Den[0]))
									{
										try
										{
											fNum = Float.parseFloat(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "0");
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}
									if (p_Indicadores[k].equals(p_Num_Den[1]))
									{
										try
										{
											fDen = Float.parseFloat(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "0");
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}
								}
							}

							if (fDen != 0)
								afAcum[i] += (fNum/fDen)*fFatorPond;
							else
								afAcum[i] += 0;

							// Fim da linha
							Arq.escreveLN("");
						}
						if (!bLinhaZerada)
							Arq.escreveLN("");
					}
				}
			}

			//
			// Resumos
			//

			// Títulos
			Arq.escreveLN("");
			Arq.escreveLN("\"Resumo - Indicador "+p_strIndicador+"\"");
			Arq.escreve("\"Períodos\",");
			Arq.escreve("\"Indicador\"");
			Arq.escreveLN("");
			//

			// Períodos e indicadores resumidos
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreve("\""+NomePeriodo+"\"");

				// Coluna do indicador resumido
				float fRes = (afAcum[i]/fTotalFatorPond)*((float)100);
				String strRes = new java.text.DecimalFormat("##.##").format(fRes);
				Arq.escreveLN(",\""+strRes+"\"");
			}
		}
		else if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
		{
			// Cabeçalho da tabela - início
			Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");

			// Coluna do período
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Per&iacute;odo"
					+"</b></font></td>");

			// Coluna da central
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[1]
					                 +"</b></font></td>");

			// Coluna da área
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[0]
					                 +"</b></font></td>");

			// Coluna da destino (CSP)
			Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[2]
					                 +"</b></font></td>");

			// Colunas dos indicadores
			for (k = 0; k < p_Indicadores.length; k++)
			{
				Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
						+p_Indicadores[k]
						               +"</b></font></td>");
			}

			// Cabeçalho da tabela - fim
			Arq.escreve("\n</tr>\n");

			j = 0;
			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (Recurso.equals(m_TODOS))
						continue;

					It[2] = p_ListaRecursos[2].iterator();
					while (It[2].hasNext())
					{
						Ent[2] = (Map.Entry)It[2].next();
						Obj = Ent[2].getValue();
						Recurso3 = Obj.toString();

						if (Recurso3.equals(m_TODOS))
							continue;

						bLinhaZerada = false;
						for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
						{
							// Recupera a linha de indicadores
							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
							if (ObjLinha != null)
								LinhaRelProcessado[0] = (Vector)ObjLinha;
							else
								LinhaRelProcessado[0] = null;

							//
							// Não envia as linhas que tem a qtd de chamadas igual a ZERO
							// (Sai do laço for)
							//
							if (p_ExcluiLinhas != null && 
									p_ExcluiLinhas.equals("true"))
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
								if (Posicao == null)
									System.out.println(">>> Posicao NAO encontrada ("+p_ContReferencia+")");
								else
								{
									if (LinhaRelProcessado[0] != null && LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString().startsWith("0") == true)
									{
										bLinhaZerada = true;
										break;
									}
								}
							}

							// Seleciona a cor de apresentação da linha
							if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
							else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
							j++;

							// Início da linha
							Arq.escreve("<tr"+Cor+">\n   ");

							// Período
							if (p_NomesPeriodos != null)
								NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
							else
								NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
							Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+NomePeriodo+"</font></td>");

							// Recurso bilhetador
							Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso2+"</font></td>");

							// Recurso área
							Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso+"</font></td>");

							// Recurso destino (CSP)
							Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso3+"</font></td>");

							// Indicadores
							fNum = fDen = 0;
							for (k = 0; k < p_Indicadores.length; k++)
							{
								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[k]);
								if (Posicao == null)
									Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">??</font></td>");
								else
								{
									Arq.escreve("<td align=\"center\" nowrap><font face="+m_FONTE+">"+(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "-")+"</font></td>");

									if (i == 0 && p_Indicadores[k].equals("F. Pond."))
									{
										try
										{
											Integer PosicaoAux;
											PosicaoAux = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
											if (PosicaoAux != null)
											{
												if (LinhaRelProcessado[0] != null)
												{
													if (LinhaRelProcessado[0].elementAt(PosicaoAux.intValue()).toString().startsWith("0") == false)
													{
														fFatorPond = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
														fTotalFatorPond += fFatorPond;                                    
													}
												}
												else
												{
													fFatorPond = 0;
													fTotalFatorPond += fFatorPond;                                    
												}
											}
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}

									if (p_Indicadores[k].equals(p_Num_Den[0]))
									{
										try
										{
											fNum = Float.parseFloat(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "0");
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}

									if (p_Indicadores[k].equals(p_Num_Den[1]))
									{
										try
										{
											fDen = Float.parseFloat(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "0");
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}

									if (p_Indicadores[k].equals(p_strIndicador))
									{
										try
										{
											if (LinhaRelProcessado[0] != null)
											{
												String strAux = LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString();
												strAux = strAux.replace(',','.');
												afPorcent[i] = Float.parseFloat(strAux);
											}
											else
												afPorcent[i] = 0;
										}
										catch (Exception Exc)
										{
											System.out.println("Erro na conversao do indicador");
										}
									}
								}
							}

							if (afPorcent[i] != 0) afAcum[i] += (afPorcent[i]/100) *fFatorPond;
							else afAcum[i] += 0;

							// Fim da linha
							Arq.escreve("\n</tr>\n");
						}

						// Seleciona a cor de apresentação da linha
						if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
						else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
						j++;

						if (!bLinhaZerada)
						{
							// Início da linha
							Arq.escreve("<tr"+Cor+">\n   ");
							Arq.escreve("<td colspan=\""+(p_Indicadores.length + 4)+"\">&nbsp;</td>");
							Arq.escreve("</tr>\n");
						}
					}
				}
			}

			//
			// Resumo
			//

			// Títulos
			Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreve("<td align=\"center\" nowrap colspan=\""+(4+p_Indicadores.length)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Resumo - Indicador "+p_strIndicador
					+"</b></font></td>");
			Arq.escreve("\n</tr>\n");

			Arq.escreve("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreve("<td align=\"center\" nowrap colspan=\"2\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Per&iacute;odos"
					+"</b></font></td>");

			Arq.escreve("<td align=\"right\" nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Indicador"
					+"</b></font></td>");
			Arq.escreve("\n</tr>\n");
			//

			// Períodos e indicadores resumidos
			int iPos;
			String strAux, strRes;
			j = 0;
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				iPos = 0;
				strAux = "";
				strRes = "";

				// Seleciona a cor de apresentação da linha
				if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
				else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
				j++;

				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreve("<tr"+ Cor+">\n   ");
				// Coluna do período
				Arq.escreve("<td align=\"center\" colspan=\"2\"><font face="+m_FONTE+"><b>"
						+NomePeriodo
						+"</b></font></td>");

				// Coluna do indicador resumido
				if (afAcum[i] != 0 && fTotalFatorPond != 0)
				{
					float fRes = (afAcum[i]/fTotalFatorPond)*((float)100);

					strRes = new java.text.DecimalFormat("##.##").format(fRes);

					iPos = strRes.indexOf('.');
					if (iPos != -1)
						strAux = strRes.substring(iPos+1, strRes.length());
					else 
					{
						iPos = strRes.indexOf(',');
						if (iPos != -1)
							strAux = strRes.substring(iPos+1, strRes.length());
					}

					if (strAux.length() == 1)
					{
						strAux += "0";
						strRes = strRes.substring(0, iPos+1) + strAux;
					}
				}
				else
					strRes = "--";

				Arq.escreve("<td align=\"right\" nowrap nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+"><b>"
						+strRes
						+"</b></font></td>");                             
				Arq.escreve("\n</tr>\n");
			}
			Arq.escreve("</table>\n");
			Arq.escreve("\n</body>\n</html>");
		}
	}

	public void gravaArquivoAnatelLDN_Apresentacao2(Arquivo Arq, Vector p_Cabecalho, String p_TituloRecurso[], String p_Indicadores[], Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, Set p_ListaRecursos[], Map p_MapRelatorios[], Vector p_Logs, String p_TipoApresentacao, String p_TEXTOSMP[], Map p_IndicadoresAnatelSMP, String p_ExcluiLinhas, String p_Num_Den[], String p_strIndicador, String p_ContReferencia, Vector p_CfgAg, boolean ListaNova[])
	{
		boolean bLinhaZerada = false, EnviouCentral = false;
		int i, j, k, QtdPeridos, QtdIndicadores, QtdRelProcessado, QtdLogs, QtdLinhas = 0, QtdLinhasExc[], Indice = 0, Indice2 = 0;
		float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
		float afFPond[], afFPondTotal[], afPorcent[];
		Vector Chaves, LinhaRelProcessado[], Logs;
		String strLog, strColuna, strTotal = "", Indicador, Cor = "", p_TipoSalvamento;
		String m_TODOS = "Todos", NomePeriodo, Recurso = "", Recurso2 = "", Recurso3 = "";
		Integer Posicao;
		Iterator It[];
		Map.Entry Ent[];
		Object Obj, ObjLinha;


		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		It = new Iterator[3];
		Ent = new Map.Entry[3];
		Chaves = new Vector();
		LinhaRelProcessado = new Vector[p_PeriodosApresentaveis.size()];

		afAcumTotal  = new float [p_PeriodosApresentaveis.size()];
		afFPond      = new float [p_PeriodosApresentaveis.size()];
		afFPondTotal = new float [p_PeriodosApresentaveis.size()];
		afAcum       = new float [p_PeriodosApresentaveis.size()];
		afPorcent    = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
		{
			afAcum[i] = 0;
			afAcumTotal[i] = 0;            
			afFPondTotal[i] = 0;
			afPorcent[i] = 0;
		}      

		// Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
		afAcum = new float [p_PeriodosApresentaveis.size()];
		for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			afAcum[i] = 0;

		QtdLinhasExc = new int[ListaNova[0] == true ? p_ListaRecursos[0].size() : p_ListaRecursos[0].size()-1];
		for (i = 0; i < QtdLinhasExc.length; i++)
			QtdLinhasExc[i] = 0;

		if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
		{
			//afFPond   = new float[p_PeriodosApresentaveis.size()];
			//afPorcent = new float[p_PeriodosApresentaveis.size()];            

			// Escreve cabeçalho
			//p_CfgAg.get(0) diz se o relatorio é html. 
			//p_CfgAg.get(1) diz se o relatorio é csv.
			//p_CfgAg.get(3) diz se foi selecionado a opcao de logs.
			//p_CfgAg.get(2) indica se foi selecionada a opção de cabeçalho
			if((""+p_CfgAg.get(2)).equals("1")){
				for (i = 0; i < p_Cabecalho.size(); i++)
					Arq.escreveLN("\""+p_Cabecalho.elementAt(i).toString()+"\"");
			}
			Arq.escreveLN("");

			// Coluna da central
			Arq.escreve("\""+p_TituloRecurso[1]+"\",");
			// Coluna da área
			Arq.escreve("\""+p_TituloRecurso[0]+"\",");
			// Coluna da destino (CSP)
			Arq.escreve("\""+p_TituloRecurso[2]+"\",");
			// Coluna do título do indicador
			Arq.escreve("\" \"");
			// Colunas dos períodos
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				Arq.escreve(",\""+NomePeriodo+"\"");
			}
			Arq.escreveLN("");
			// 

			/*
         int QtdLinhas;
         boolean bSomaMaisUm = true;
         QtdLinhas = p_ListaRecursos[2].size();
         It[2] = p_ListaRecursos[2].iterator();
         while (It[2].hasNext())
         {
            Ent[2] = (Map.Entry)It[2].next();
            Obj = Ent[2].getValue();
            Recurso3 = Obj.toString();

            if (Recurso3.equals(m_TODOS))
            {
               bSomaMaisUm = false;
               break;
            }
         }

         if (bSomaMaisUm)
            QtdLinhas++;
			 */
			//
			// Percorre lista de bilhetadores
			//
			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				int Cont = 0;
				j = 0;
				//
				// Percorre lista de áreas
				//
				if (p_ExcluiLinhas.equals("true"))
				{
					Indice = 0;
					It[0] = p_ListaRecursos[0].iterator();
					while (It[0].hasNext())
					{
						Ent[0] = (Map.Entry)It[0].next();
						Obj = Ent[0].getValue();
						Recurso = Obj.toString();

						if (Recurso.equals(m_TODOS))
							continue;

						int iTamListaRec0 = p_ListaRecursos[0].size()-1;
						int iTamListaRec2 = p_ListaRecursos[2].size()-1;

						QtdLinhas = iTamListaRec0 * iTamListaRec2;

						if (ListaNova[0] == true)
							QtdLinhas++;
						if (ListaNova[2] == true)
							QtdLinhas++;

						//
						// Percorre lista do Destino (CSP)
						//
						QtdLinhasExc[Indice] = 0;                  

						It[2] = p_ListaRecursos[2].iterator();
						while (It[2].hasNext())
						{
							Ent[2] = (Map.Entry)It[2].next();
							Obj = Ent[2].getValue();
							Recurso3 = Obj.toString();

							if (Recurso3.equals(m_TODOS))
								continue;

							bLinhaZerada = true;
							for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
							{
								// Recupera a linha de indicadores
								ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
								if (ObjLinha != null)
								{
									LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);
									//aqui vai ser feita a verificacao se as linhas do relatorio sao todas '0',
									//para essa linha desse recurso ser retirada.
									for(int z=0;z<LinhaRelProcessado[i].size();z++)
									{
										if (LinhaRelProcessado[i].elementAt(z).toString().equals("0") == true ||
												LinhaRelProcessado[i].elementAt(z).toString().equals("0.0") == true ||
												LinhaRelProcessado[i].elementAt(z).toString().equals("0.00") == true	)
										{
											bLinhaZerada = true;
										}
										else
										{
											bLinhaZerada = false;
											break;
										}
									}
								}
								else
									LinhaRelProcessado[i] = null;
							}

							if (!bLinhaZerada)
								QtdLinhasExc[Indice]++;
						}
						Indice++;
					}
				}
				///*
				// Coluna da central
				if (p_ExcluiLinhas.equals("true"))
				{
					if (QtdLinhas != 1)
						Arq.escreve("\""+Recurso2+"\",");
					else
						continue;                                   
				}
				else
					Arq.escreve("\""+Recurso2+"\",");
				//*/
				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();
					/*
               if (EnviouCentral == false)
               {
                  // Coluna da central
                  if (p_ExcluiLinhas.equals("true"))
                  {
                     if (QtdLinhas != 1)
                     {
                        QtdLinhas = 0;
                        for (i = 0; i < QtdLinhasExc.length; i++)
                           QtdLinhas += QtdLinhasExc[i] * m_TEXTOSMP[m_TipoSMP].length;

                        m_Html.envia("<tr>\n   ");
                        //m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (m_TEXTOSMP[m_TipoSMP].length))+"\"><font face="+m_FONTE+"><b>"
                        m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+QtdLinhas+"\"><font face="+m_FONTE+"><b>"
                                      +Recurso2
                                      +"</b></font></td>");
                     }
                     else
                        continue;
                  }
                  else
                     m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (m_TEXTOSMP[m_TipoSMP].length))+"\"><font face="+m_FONTE+"><b>"
                                   +Recurso2
                                   +"</b></font></td>");

                  Indice2++;
                  EnviouCentral = true;
               }               
					 */
					if (Recurso.equals(m_TODOS))
						continue;

					//
					// Percorre lista de destinos (CSP)
					//
					It[2] = p_ListaRecursos[2].iterator();
					while (It[2].hasNext())
					{
						Ent[2] = (Map.Entry)It[2].next();
						Obj = Ent[2].getValue();
						Recurso3 = Obj.toString();

						if (Recurso3.equals(m_TODOS))
							continue;

						bLinhaZerada = true;
						for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
						{
							// Recupera a linha de indicadores
							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
							if (ObjLinha != null)
							{
								LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);
								//aqui vai ser feita a verificacao se as linhas do relatorio sao todas '0',
								//para essa linha desse recurso ser retirada.
								for(int z=0;z<LinhaRelProcessado[i].size();z++)
								{
									if (LinhaRelProcessado[i].elementAt(z).toString().equals("0") == true ||
											LinhaRelProcessado[i].elementAt(z).toString().equals("0.0") == true ||
											LinhaRelProcessado[i].elementAt(z).toString().equals("0.00") == true	)
									{
										bLinhaZerada = true;
									}
									else
									{
										bLinhaZerada = false;
										break;
									}
								}
							}
							else
								LinhaRelProcessado[i] = null;
						}

						if (p_ExcluiLinhas.equals("true") && (bLinhaZerada))
							continue;

						if (Cont == 0)
						{
							Arq.escreve("\""+Recurso+"\",\""+Recurso3+"\",");

							for (i = 0; i < p_TEXTOSMP.length; i++)
							{
								if (i == 0)
									Arq.escreve("\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador : p_TEXTOSMP[i])+"\"");
								else
									Arq.escreve("\"\",\"\",\"\",\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador : p_TEXTOSMP[i])+"\"");

								//
								// Calcula o SMP ponderado
								//
								for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
								{
									Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
									if (Posicao != null)
									{
										if (LinhaRelProcessado[k] != null)
										{
											String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
											strAux = strAux.replace(',','.');
											afPorcent[k] = Float.parseFloat(strAux);
										}
										else
											afPorcent[k] = 0;
									}
									else
										afPorcent[k] = 0;

									Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
									if (Posicao != null)
									{
										if (LinhaRelProcessado[k] != null)
										{
											afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
											fTotalFatorPond += afFPond[k];
										}
										else
										{
											afFPond[k] = 0;
											fTotalFatorPond += afFPond[k];
										}
									}
									else
										afFPond[k] = 0;

									afAcum[k] = (afPorcent[k] * afFPond[k])/100;
									afAcumTotal[k] += (afPorcent[k] * afFPond[k])/100;
									if (afPorcent[k] != 0)
										afFPondTotal[k] += afFPond[k];                           
								}

								for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
								{
									Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
									if (Posicao == null)
									{
										Arq.escreve(",\"??\"");
										System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
									}
									else
									{
										if (p_TEXTOSMP[i].equals("Fator de pondera&ccedil;&atilde;o"))
											Arq.escreve(",\""+new java.text.DecimalFormat("##").format(afAcum[k])+"\"");
										else
											Arq.escreve(",\""+(LinhaRelProcessado[k] != null ? LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString() : "-")+"\"");
									}
								}

								if (i != p_TEXTOSMP.length-1)
									Arq.escreveLN("");
							}
							Arq.escreveLN("");
						}
						else
						{
							Arq.escreve("\"\",\""+Recurso+"\",\""+Recurso3+"\",");

							for (i = 0; i < p_TEXTOSMP.length; i++)
							{
								if (i == 0)
									Arq.escreve("\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])+"\"");
								else
									Arq.escreve("\"\",\"\",\"\",\""+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])+"\"");

								//
								// Calcula o SMP ponderado
								//
								for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
								{                        
									Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
									if (Posicao != null)
									{
										if (LinhaRelProcessado[k] != null)
										{
											String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
											strAux = strAux.replace(',','.');
											afPorcent[k] = Float.parseFloat(strAux);
										}
										else
											afPorcent[k] = 0;
									}
									else
										afPorcent[k] = 0;

									Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
									if (Posicao != null)
									{
										if (LinhaRelProcessado[k] != null)
										{
											afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
											fTotalFatorPond += afFPond[k];
										}
										else
										{
											afFPond[k] = 0;
											fTotalFatorPond += afFPond[k];
										}
									}
									else
										afFPond[k] = 0;

									afAcum[k] = (afPorcent[k] * afFPond[k])/100;
									afAcumTotal[k] += (afPorcent[k] * afFPond[k])/100;
									if (afPorcent[k] != 0)
										afFPondTotal[k] += afFPond[k];
								}

								for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
								{
									Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
									if (Posicao == null)
									{
										Arq.escreve(",\"??\"");
										System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
									}
									else
									{
										if (p_TEXTOSMP[i].equals("Fator de pondera&ccedil;&atilde;o"))
											Arq.escreve(",\""+new java.text.DecimalFormat("##").format(afAcum[k])+"\"");
										else
											Arq.escreve(",\""+(LinhaRelProcessado[k] != null ? LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString() : "-")+"\"");
									}
								}
								Arq.escreveLN("");                        

								//if (i != p_TEXTOSMP.length-1)
								//   Arq.escreveLN("");
							}
						}
						Cont++;
					}
					/*               
               EnviouCentral = false;
               Arq.escreveLN("");
               Arq.escreveLN("");
					 */               
				}
				EnviouCentral = false;
				Arq.escreveLN("");
				Arq.escreveLN("");
			}

			//escreve logs no arquivo caso a chave p_CfgAg.get(3) seja iqual a 1.
			if((""+p_CfgAg.get(3)).equals("1"))
			{
				for (i = 0; i < p_Logs.size(); i++)
					Arq.escreveLN("\""+p_Logs.elementAt(i).toString()+"\"");
			}


			//
			// Resumo
			//

			// Títulos
			Arq.escreve("\"Resumo - Indicador "+p_strIndicador+"\"");
			Arq.escreveLN("");

			Arq.escreve("\"Períodos\",");
			Arq.escreve("\"Indicador\"");
			Arq.escreveLN("");
			//

			// Períodos e indicadores resumidos
			int iPos;
			String strAux, strRes;            
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				iPos = 0;
				strAux = "";
				strRes = "";

				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreve("\""+NomePeriodo+"\",");

				// Coluna do indicador resumido
				float fRes = (afAcumTotal[i]/afFPondTotal[i])*((float)100);
				strRes = new java.text.DecimalFormat("##.##").format(fRes);
				iPos = strRes.indexOf('.');
				if (iPos != -1)
					strAux = strRes.substring(iPos+1, strRes.length());
				else 
				{
					iPos = strRes.indexOf(',');
					if (iPos != -1)
						strAux = strRes.substring(iPos+1, strRes.length());
				}

				if (strAux.length() == 1)
				{
					strAux += "0";
					strRes = strRes.substring(0, iPos+1) + strAux;
				}                  

				Arq.escreveLN("\""+strRes+"\"");
			}
		}
		else if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
		{
			//afFPond   = new float[p_PeriodosApresentaveis.size()];
			//afPorcent = new float[p_PeriodosApresentaveis.size()];

			// Cabeçalho da tabela - início
			Arq.escreveLN("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");

			// Coluna da central
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[1]
					                 +"</b></font></td>");

			// Coluna da área
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+p_TituloRecurso[0]
					                 +"</b></font></td>");

			// Coluna do destino (CSP)
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"&nbsp;&nbsp;&nbsp;"+p_TituloRecurso[2]+"&nbsp;&nbsp;&nbsp;"
					+"</b></font></td>");

			// Coluna do título do indicador
			Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"&nbsp;"
					+"</b></font></td>");

			// Colunas dos períodos
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+NomePeriodo+"</b></font></td>");
			}
			Arq.escreveLN("</tr>\n");
			// Cabeçalho da tabela - fim
			/*
         int QtdLinhas;
         boolean bSomaMaisUm = true;
         QtdLinhas = p_ListaRecursos[2].size();
         It[2] = p_ListaRecursos[2].iterator();
         while (It[2].hasNext())
         {
            Ent[2] = (Map.Entry)It[2].next();
            Obj = Ent[2].getValue();
            Recurso3 = Obj.toString();

            if (Recurso3.equals(m_TODOS))
            {
               bSomaMaisUm = false;
               break;
            }
         }
			 */
			//
			// Percorre lista de bilhetadores
			//
			It[1] = p_ListaRecursos[1].iterator();
			while (It[1].hasNext())
			{
				Ent[1] = (Map.Entry)It[1].next();
				Obj = Ent[1].getValue();
				Recurso2 = Obj.toString();

				if (Recurso2.equals(m_TODOS))
					continue;

				int Cont = 0;
				j = 0;
				//
				// Percorre lista de áreas
				//
				if (p_ExcluiLinhas.equals("true"))
				{
					Indice = 0;
					It[0] = p_ListaRecursos[0].iterator();
					while (It[0].hasNext())
					{
						Ent[0] = (Map.Entry)It[0].next();
						Obj = Ent[0].getValue();
						Recurso = Obj.toString();

						if (Recurso.equals(m_TODOS))
							continue;

						int iTamListaRec0 = p_ListaRecursos[0].size()-1;
						int iTamListaRec2 = p_ListaRecursos[2].size()-1;

						QtdLinhas = iTamListaRec0 * iTamListaRec2;

						if (ListaNova[0] == true)
							QtdLinhas++;
						if (ListaNova[2] == true)
							QtdLinhas++;

						//
						// Percorre lista do Destino (CSP)
						//
						QtdLinhasExc[Indice] = 0;
						It[2] = p_ListaRecursos[2].iterator();
						while (It[2].hasNext())
						{
							Ent[2] = (Map.Entry)It[2].next();
							Obj = Ent[2].getValue();
							Recurso3 = Obj.toString();

							if (Recurso3.equals(m_TODOS))
								continue;                     

							bLinhaZerada = true;
							for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
							{
								// Recupera a linha de indicadores
								ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
								if (ObjLinha != null)
								{
									LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

									Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
									if (Posicao != null)
									{
										if (LinhaRelProcessado[i].elementAt(Posicao.intValue()).toString().startsWith("0") == false)
										{
											bLinhaZerada = false;
											break;
										}
									}
								}
								else
									LinhaRelProcessado[i] = null;
							}
							if (!bLinhaZerada)
								QtdLinhasExc[Indice]++;
						}
						Indice++;               
					}
				}
				/*
            // Coluna da central
            if (p_ExcluiLinhas.equals("true"))
            {
               if (QtdLinhas != 1)
               {
                  Arq.escreveLN("<tr>\n   ");
                  Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (p_TEXTOSMP.length))+"\"><font face="+m_FONTE+"><b>"
                                +Recurso2
                                +"</b></font></td>");
               }
               else
                  continue;                                   
            }
            else
               Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (p_TEXTOSMP.length))+"\"><font face="+m_FONTE+"><b>"
                             +Recurso2
                             +"</b></font></td>");
				 */
				It[0] = p_ListaRecursos[0].iterator();
				while (It[0].hasNext())
				{
					Ent[0] = (Map.Entry)It[0].next();
					Obj = Ent[0].getValue();
					Recurso = Obj.toString();

					if (EnviouCentral == false)
					{
						// Coluna da central
						if (p_ExcluiLinhas.equals("true"))
						{
							if (QtdLinhas != 1)
							{
								QtdLinhas = 0;
								for (i = 0; i < QtdLinhasExc.length; i++)
									QtdLinhas += QtdLinhasExc[i] * p_TEXTOSMP.length;

								//System.out.println("QtdLinhas: "+QtdLinhas);
								Arq.escreveLN("<tr>\n   ");
								//m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (m_TEXTOSMP[m_TipoSMP].length))+"\"><font face="+m_FONTE+"><b>"
								Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+QtdLinhas+"\"><font face="+m_FONTE+"><b>"
										+Recurso2
										+"</b></font></td>");
							}
							else
								continue;
						}
						else
						{
							int iTamListaRec0 = p_ListaRecursos[0].size()-1;
							int iTamListaRec2 = p_ListaRecursos[2].size()-1;

							QtdLinhas = iTamListaRec0 * iTamListaRec2;

							if (ListaNova[0] == true)
								QtdLinhas++;
							if (ListaNova[2] == true)
								QtdLinhas++;               

							QtdLinhas *= p_TEXTOSMP.length;
							Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+QtdLinhas+"\"><font face="+m_FONTE+"><b>"
									+Recurso2
									+"</b></font></td>");
						}
						Indice2++;
						EnviouCentral = true;
					}

					if (Recurso.equals(m_TODOS))
						continue;

					It[2] = p_ListaRecursos[2].iterator();
					while (It[2].hasNext())
					{
						Ent[2] = (Map.Entry)It[2].next();
						Obj = Ent[2].getValue();
						Recurso3 = Obj.toString();

						if (Recurso3.equals(m_TODOS))
							continue;

						bLinhaZerada = true;
						for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
						{
							// Recupera a linha de indicadores
							ObjLinha = p_MapRelatorios[((Integer)(p_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
							if (ObjLinha != null)
							{
								LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

								Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_ContReferencia);
								if (Posicao != null)
								{
									if (LinhaRelProcessado[i].elementAt(Posicao.intValue()).toString().startsWith("0") == false)
										bLinhaZerada = false;
								}
							}
							else
								LinhaRelProcessado[i] = null;
						}

						if (p_ExcluiLinhas.equals("true") && (bLinhaZerada))
							continue;

						if (Cont != 0)
						{
							// Seleciona a cor de apresentação da linha
							if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHASIM2+"\"";
							else Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
							j++;

							Arq.escreveLN("<tr"+Cor+">\n");
						}

						if (Cont == 0)
						{
							Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap rowspan=\""+p_TEXTOSMP.length+"\"><font face="+m_FONTE+"><b>"
									+Recurso
									+"</b></font></td>");
							Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap rowspan=\""+p_TEXTOSMP.length+"\"><font face="+m_FONTE+"><b>"
									+Recurso3
									+"</b></font></td>");

							//
							// Calcula o SMP ponderado
							//
							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{                        
								Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
								if (Posicao != null)
								{
									if (LinhaRelProcessado[k] != null)
									{
										String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
										strAux = strAux.replace(',','.');
										afPorcent[k] = Float.parseFloat(strAux);
									}
									else
										afPorcent[k] = 0;
								}
								else
									afPorcent[k] = 0;

								Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
								if (Posicao != null)
								{
									if (LinhaRelProcessado[k] != null)
									{
										afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
										fTotalFatorPond += afFPond[k];
									}
									else
									{
										afFPond[k] = 0;
										fTotalFatorPond += afFPond[k];
									}
								}
								else
									afFPond[k] = 0;

								afAcum[k] = (afPorcent[k]/100) * afFPond[k];
								afAcumTotal[k] += (afPorcent[k]/100) * afFPond[k];

								if (afPorcent[k] != 0)
									afFPondTotal[k] += afFPond[k];                           
								//else System.out.println("2)->Porc = ZERO");
							}

							for (i = 0; i < p_TEXTOSMP.length; i++)
							{
								if (i != 0)
									Arq.escreveLN("<tr>\n");
								Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"left\"><font face="+m_FONTE+">"
										+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])
										+"</font></td>");

								for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
								{
									Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
									if (Posicao == null)
									{
										Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">??</font></td>");
										System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
									}
									else
									{
										Arq.escreveLN("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap><font face="+m_FONTE+">"
												+(LinhaRelProcessado[k] != null ? LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString() : "-")
												+"</font></td>");
									}
								}

								if (i != p_TEXTOSMP.length-1)
									Arq.escreveLN("\n</tr>\n");
							}
							Arq.escreveLN("\n</tr>\n");
						}
						else
						{
							Arq.escreveLN("<td align=\"center\" nowrap rowspan=\""+p_TEXTOSMP.length+"\"><font face="+m_FONTE+"><b>"
									+Recurso
									+"</b></font></td>");
							Arq.escreveLN("<td align=\"center\" nowrap rowspan=\""+p_TEXTOSMP.length+"\"><font face="+m_FONTE+"><b>"
									+Recurso3
									+"</b></font></td>");
							//
							// Calcula o SMP ponderado
							//
							for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
							{                        
								Posicao  = (Integer)p_IndicadoresAnatelSMP.get(p_strIndicador);
								if (Posicao != null)
								{
									if (LinhaRelProcessado[k] != null)
									{
										String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
										strAux = strAux.replace(',','.');
										afPorcent[k] = Float.parseFloat(strAux);
									}
									else
										afPorcent[k] = 0;
								}
								else
									afPorcent[k] = 0;

								Posicao  = (Integer)p_IndicadoresAnatelSMP.get("F. Pond.");
								if (Posicao != null)
								{
									if (LinhaRelProcessado[k] != null)
									{
										afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
										fTotalFatorPond += afFPond[k];
									}
									else
									{
										afFPond[k] = 0;
										fTotalFatorPond += afFPond[k];
									}
								}
								else
									afFPond[k] = 0;

								afAcum[k] = (afPorcent[k]/100) * afFPond[k];
								afAcumTotal[k] += (afPorcent[k]/100) * afFPond[k];

								if (afPorcent[k] != 0)
									afFPondTotal[k] += afFPond[k];
							}

							for (i = 0; i < p_TEXTOSMP.length; i++)
							{
								if (i != 0)
									Arq.escreveLN("<tr "+Cor+">\n");
								Arq.escreveLN("<td align=\"left\"><font face="+m_FONTE+">"
										+(p_TEXTOSMP[i].equals("Percentual ") ? p_TEXTOSMP[i] + p_strIndicador: p_TEXTOSMP[i])
										+"</font></td>");

								for (k = 0; k < p_PeriodosApresentaveis.size(); k++)
								{
									Posicao = (Integer)p_IndicadoresAnatelSMP.get(p_Indicadores[i]);
									if (Posicao == null)
									{
										Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">??</font></td>");
										System.out.println(">>> Posicao NAO encontrada ("+p_Indicadores[i]+")");
									}
									else
									{
										Arq.escreveLN("<td align=\"center\" nowrap><font face="+m_FONTE+">"
												+(LinhaRelProcessado[k] != null ? LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString() : "-")
												+"</font></td>");
									}
								}

								if (i != p_TEXTOSMP.length-1)
									Arq.escreveLN("\n</tr>\n");
							}
						}
						Cont++;
					}
					//               Arq.escreveLN("\n</tr>\n");
				}
				EnviouCentral = false;
				Arq.escreveLN("\n<tr>\n");
				Arq.escreveLN("<td colspan=\""+(3+p_PeriodosApresentaveis.size())+"\">&nbsp;</td>");
				Arq.escreveLN("\n</tr>\n");               
			}

			//       escreve logs no arquivo caso a chave p_CfgAg.get(3) seja iqual a 1.
			if((""+p_CfgAg.get(3)).equals("1"))
			{
				Vector l_LogPeriodo = null;
				for (i = 0; i < p_Logs.size(); i++)
				{
					l_LogPeriodo = (Vector) p_Logs.elementAt(i);
					for (int w=0; w< l_LogPeriodo.size(); w++)
					{
						Arq.escreveLN("\n<tr><td colspan=\""+(3+p_PeriodosApresentaveis.size())+"\"><font face=\"verdana\" size=\"1\">"+l_LogPeriodo.elementAt(w)+"</font></td></tr>");    
					}
				}
			}

			//
			// Resumo
			//

			// Títulos
			Arq.escreveLN("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreveLN("<td align=\"center\" nowrap colspan=\""+(4+p_PeriodosApresentaveis.size())+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Resumo - Indicador "+p_strIndicador
					+"</b></font></td>");
			Arq.escreveLN("\n</tr>\n");

			Arq.escreveLN("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
			Arq.escreveLN("<td align=\"center\" nowrap colspan=\"2\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Per&iacute;odos"
					+"</b></font></td>");

			Arq.escreveLN("<td align=\"right\" nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
					+"Indicador"
					+"</b></font></td>");
			Arq.escreveLN("\n</tr>\n");
			//

			// Períodos e indicadores resumidos - Apresentacao = 2
			int iPos;
			String strAux, strRes;
			j = 0;
			for (i = 0; i < p_PeriodosApresentaveis.size(); i++)
			{
				iPos = 0;
				strAux = "";
				strRes = "";

				// Seleciona a cor de apresentação da linha
				if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
				else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
				j++;

				// Período
				if (p_NomesPeriodos != null)
					NomePeriodo = p_NomesPeriodos.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
				else
					NomePeriodo = p_Datas.elementAt(((Integer)p_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

				Arq.escreveLN("<tr"+ Cor+">\n   ");
				// Coluna do período
				Arq.escreveLN("<td align=\"center\" colspan=\"2\"><font face="+m_FONTE+"><b>"
						+NomePeriodo
						+"</b></font></td>");

				// Coluna do indicador resumido
				if (afAcumTotal[i] != 0 && afFPondTotal[i] != 0)
				{
					float fRes = (afAcumTotal[i]/afFPondTotal[i])*((float)100);

					strRes = new java.text.DecimalFormat("##.##").format(fRes);
					iPos = strRes.indexOf('.');
					if (iPos != -1)
						strAux = strRes.substring(iPos+1, strRes.length());
					else 
					{
						iPos = strRes.indexOf(',');
						if (iPos != -1)
							strAux = strRes.substring(iPos+1, strRes.length());
					}

					if (strAux.length() == 1)
					{
						strAux += "0";
						strRes = strRes.substring(0, iPos+1) + strAux;
					}
				}
				else
					strRes = "--";

				Arq.escreve("<td align=\"right\" nowrap nowrap colspan=\""+(2+p_Indicadores.length)+"\"><font face="+m_FONTE+"><b>"
						+strRes
						+"</b></font></td>");                             
				Arq.escreve("\n</tr>\n");
			}
			Arq.escreve("</table>\n");
			Arq.escreve("\n</body>\n</html>");
		}
	}

	public boolean criaArquivoAnatelSMP(String p_NomeArq, Vector p_Cabecalho, String p_TituloRecurso[], String p_Indicadores[], Vector p_PeriodosApresentaveis, Vector p_NomesPeriodos, Vector p_Datas, Set p_ListaRecursos[], Map p_MapRelatorios[], Vector p_Logs, String p_TipoApresentacao, String p_TEXTOSMP[], Map p_IndicadoresAnatelSMP, String p_ExcluiLinhas, String p_Num_Den[], String p_strIndicador, String p_ContReferencia, Vector p_CfgAg, int p_TipoSMP, boolean p_ListaNova[])
	{
		Arquivo Arq = new Arquivo();
		int i;
		String p_TipoSalvamento;      


		if (p_CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
			p_TipoSalvamento = "csv";
		else
			p_TipoSalvamento = "htm";

		p_NomeArq += "." + p_TipoSalvamento;

		Arq.setDiretorio(m_DirDownload);
		Arq.setNome(p_NomeArq);
		Arq.apaga();

		if (Arq.abre('w') == true)
		{
			if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
			{
				Arq.escreve("<html>\n<head>\n<title>CDRView | Visent</title>\n");
				Arq.escreve("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
				Arq.escreve("<meta http-equiv=\"Pragma\" content=\"no-cache\">\n</head>\n");
				Arq.escreve("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

				// Escreve cabeçalho
				Arq.escreve("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
				Arq.escreve("   <tr>\n   ");
				Arq.escreve("			<td align=\"left\" ><p><b>Visent - CDRView <sup>&reg;</sup></b><br>\n");
				Arq.escreve("				<small>Copyright 1999 - 2004</small></p>  ");
				Arq.escreve("			</td> ");

				if (DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0)
				{
					Arq.escreve("		<td align=\"middle\"><img src=\"http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/"+DefsComum.s_ContextoWEB+"/imagens/logo_cliente2.gif\"> </td> ");
				}

				Arq.escreve("			<td align=\"right\" ><img src=\"http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/"+DefsComum.s_ContextoWEB+"/imagens/logo_cliente.gif\"> </td> ");
				Arq.escreve("   </tr>\n");
				Arq.escreve("</table>\n");
				if (p_CfgAg.elementAt(2).equals("1") == true)
				{
					for (i = 0; i < p_Cabecalho.size(); i++)
						Arq.escreve("<font face="+m_FONTE+">"+ p_Cabecalho.elementAt(i).toString()+"</font><br>\n");
				}
				Arq.escreve("<br>\n");
				Arq.escreve("<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\">\n");
			}

			// Tipo de Apresentacao 1
			if (p_TipoApresentacao.equalsIgnoreCase("1"))
			{
				if (p_TipoSMP != ANATEL_LDN)
					gravaArquivoAnatelSMP_Apresentacao1(Arq, p_Cabecalho, p_TituloRecurso, p_Indicadores, p_PeriodosApresentaveis, p_NomesPeriodos, p_Datas, p_ListaRecursos, p_MapRelatorios, p_Logs, p_TipoApresentacao, p_TEXTOSMP, p_IndicadoresAnatelSMP, p_ExcluiLinhas, p_Num_Den, p_strIndicador, p_ContReferencia, p_CfgAg);
				else
					gravaArquivoAnatelLDN_Apresentacao1(Arq, p_Cabecalho, p_TituloRecurso, p_Indicadores, p_PeriodosApresentaveis, p_NomesPeriodos, p_Datas, p_ListaRecursos, p_MapRelatorios, p_Logs, p_TipoApresentacao, p_TEXTOSMP, p_IndicadoresAnatelSMP, p_ExcluiLinhas, p_Num_Den, p_strIndicador, p_ContReferencia, p_CfgAg);            
			}
			else // Tipo de Apresentacao 2
			{
				if (p_TipoSMP != ANATEL_LDN)
					gravaArquivoAnatelSMP_Apresentacao2(Arq, p_Cabecalho, p_TituloRecurso, p_Indicadores, p_PeriodosApresentaveis, p_NomesPeriodos, p_Datas, p_ListaRecursos, p_MapRelatorios, p_Logs, p_TipoApresentacao, p_TEXTOSMP, p_IndicadoresAnatelSMP, p_ExcluiLinhas, p_Num_Den, p_strIndicador, p_ContReferencia, p_CfgAg);
				else
					gravaArquivoAnatelLDN_Apresentacao2(Arq, p_Cabecalho, p_TituloRecurso, p_Indicadores, p_PeriodosApresentaveis, p_NomesPeriodos, p_Datas, p_ListaRecursos, p_MapRelatorios, p_Logs, p_TipoApresentacao, p_TEXTOSMP, p_IndicadoresAnatelSMP, p_ExcluiLinhas, p_Num_Den, p_strIndicador, p_ContReferencia, p_CfgAg, p_ListaNova);
			}

			Arq.fecha();
			m_GeraArqDownload = 2;         
			return true;
		}
		else
		{
			m_GeraArqDownload = 3;
			return false;
		}
	}

	/**
	 * @return
	 * 
	 * @uml.property name="m_AbreRetRelatorio"
	 */
	public boolean isM_AbreRetRelatorio() {
		return m_AbreRetRelatorio;
	}

	/**
	 * @param b
	 * 
	 * @uml.property name="m_AbreRetRelatorio"
	 */
	public void setM_AbreRetRelatorio(boolean b) {
		m_AbreRetRelatorio = b;
	}

	/**
	 * @return Returns the m_GeraArqDownload.
	 */
	public int getStatusArqDownload() {
		return m_GeraArqDownload;
	}
	/**
	 * @param geraArqDownload The m_GeraArqDownload to set.
	 */
	public void setStatusArqDownload(int p_geraArqDownload) {
		m_GeraArqDownload = p_geraArqDownload;
	}
	/**
	 * @return Returns the m_Cabecalho.
	 */
	public Vector getM_Cabecalho() {
		return m_Cabecalho;
	}
	/**
	 * @param cabecalho The m_Cabecalho to set.
	 */
	public void setM_Cabecalho(Vector cabecalho) {
		m_Cabecalho = cabecalho;
	}
	/**
	 * @return Returns the m_Datas.
	 */
	public Vector getM_Datas() {
		return m_Datas;
	}
	/**
	 * @param datas The m_Datas to set.
	 */
	public void setM_Datas(Vector datas) {
		m_Datas = datas;
	}
	/**
	 * @return Returns the m_Linhas.
	 */
	public Vector getM_Linhas() {
		return m_Linhas;
	}
	/**
	 * @param linhas The m_Linhas to set.
	 */
	public void setM_Linhas(Vector linhas) {
		m_Linhas = linhas;
	}
	/**
	 * @return Returns the m_Logs.
	 */
	public Vector getM_Logs() {
		return m_Logs;
	}
	/**
	 * @param logs The m_Logs to set.
	 */
	public void setM_Logs(Vector logs) {
		m_Logs = logs;
	}
	/**
	 * @return Returns the m_Periodos.
	 */
	public String getM_Periodos() {
		return m_Periodos;
	}
	/**
	 * @param periodos The m_Periodos to set.
	 */
	public void setM_Periodos(String periodos) {
		m_Periodos = periodos;
	}
	/**
	 * @return Returns the m_vIndicadores.
	 */
	public Vector getM_vIndicadores() {
		return m_vIndicadores;
	}
	/**
	 * @param indicadores The m_vIndicadores to set.
	 */
	public void setM_vIndicadores(Vector indicadores) {
		m_vIndicadores = indicadores;
	}
	/**
	 * @return Returns the m_Periodo.
	 */
	public Vector getM_Periodo() {
		return m_Periodo;
	}
	/**
	 * @param periodo The m_Periodo to set.
	 */
	public void setM_Periodo(Vector periodo) {
		m_Periodo = periodo;
	}
	/**
	 * @return Returns the m_PeriodosApresentaveis.
	 */
	public Vector getM_PeriodosApresentaveis() {
		return m_PeriodosApresentaveis;
	}
	/**
	 * @param periodosApresentaveis The m_PeriodosApresentaveis to set.
	 */
	public void setM_PeriodosApresentaveis(Vector periodosApresentaveis) {
		m_PeriodosApresentaveis = periodosApresentaveis;
	}
	/**
	 * @return Returns the m_SelecaoIndicador.
	 */
	public SelecaoIndicadoresPortal getM_SelecaoIndicador() {
		return m_SelecaoIndicador;
	}
	/**
	 * @param selecaoIndicador The m_SelecaoIndicador to set.
	 */
	public void setM_SelecaoIndicador(SelecaoIndicadoresPortal selecaoIndicador) {
		m_SelecaoIndicador = selecaoIndicador;
	}
	/**
	 * @return Returns the m_SelecaoRecursos.
	 */
	public SelecaoRecursosPortal getM_SelecaoRecursos() {
		return m_SelecaoRecursos;
	}
	/**
	 * @param selecaoRecursos The m_SelecaoRecursos to set.
	 */
	public void setM_SelecaoRecursos(SelecaoRecursosPortal selecaoRecursos) {
		m_SelecaoRecursos = selecaoRecursos;
	}
	/**
	 * @return Returns the m_iRetRelatorio.
	 */
	public iRetRelatorio getM_iRetRelatorio() {
		return m_iRetRelatorio;
	}
	/**
	 * @param retRelatorio The m_iRetRelatorio to set.
	 */
	public void setM_iRetRelatorio(iRetRelatorio retRelatorio) {
		m_iRetRelatorio = retRelatorio;
	}
	public String[] getM_Rec() {
		return m_Rec;
	}
	public void setM_Rec(String[] rec) {
		m_Rec = rec;
	}
	public Vector getM_Recv() {
		return m_Recv;
	}
	public void setM_Recv(Vector rec) {
		m_Recv = rec;
	}
	/**
	 * @return Returns the m_TipoRel.
	 */
	public String getM_TipoRel() {
		return m_TipoRel;
	}
	/**
	 * @param tipoRel The m_TipoRel to set.
	 */
	public void setM_TipoRel(String tipoRel) {
		m_TipoRel = tipoRel;
	}
	/**
	 * @return Returns the m_ContadoresRelatorio (Contadores que geraram o relatorio).
	 */
	public Vetor getM_ContadoresRelatorio()
	{
		return m_ContadoresRelatorio;
	}
	/**
	 * @param contadoresRelatorio The m_ContadoresRelatorio to set.
	 */
	public void setM_ContadoresRelatorio(Vetor contadoresRelatorio)
	{
		m_ContadoresRelatorio = contadoresRelatorio;
	}

	/**
	 * Metodo para setar os contadores que geraram o historico
	 * 
	 * @param contadoresRelatorio 
	 * */
	public void setM_ContadoresRelatorio(String contadoresHistorico)
	{
		if ((contadoresHistorico != null) && (!contadoresHistorico.equals("")))
		{
			Vetor contadores = new Vetor();
			contadores.fnAdiciona(contadoresHistorico.split(";"));

			m_ContadoresRelatorio = contadores;
		}
		else
		{
			m_ContadoresRelatorio = null;
		}
	}

	/**
	 * Metodo criado para fechar todos os relatorios do usuario
	 * 
	 * @param usuario 
	 * */
	public static void fechaRelatoriosUsuario(UsuarioDef usuario)
	{
		List agendasUsuario = usuario.getM_ListAgendasUsuario();

		for (Iterator iter = agendasUsuario.iterator(); iter.hasNext();)
		{
			Agenda agenda = (Agenda) iter.next();
		//	agenda.m_iRetRelatorio.fnFecha();
			iter.remove();
		}
	}
	/**
	 * metodo que retorna uma linha que é como se fosse os indicadores
	 * no caso do relatorio de IMEI é a linha que comeca com 11;*/
	public String getIndicadoresDefinidos(String TipoRel){
		if(TipoRel.equalsIgnoreCase("30"))//caso do relatorio de pesquisaimei
		{
			if (m_iRetRelatorio != null)
			{
				m_iRetRelatorio.fnPosicionaAposCabecalho();
				m_iRetRelatorio.fnPosicionaDados((short)0,0,(short)11);
				String aux = m_iRetRelatorio.fnGetDados((short)0,(short)11);
				//tirando o ponto e virgula do inicio e do final 
				indicadoresDefinidosIMEI = aux.substring(1,aux.length()-2);
				return indicadoresDefinidosIMEI;
			}
		}
		return "";
	}

	/**
	 * metodo que recebe o identificador da linha e retorna quantas
	 * linhas existem no relatorio começando com essa chave
	 * */
	public int getQuantidadeLinhasPorId(String idLinha)
	{
		if(hashLinhasId == null)
		{
			hashLinhasId = new HashMap();
			hashLinhasId.put(idLinha,new Integer(leLinhasServidor(idLinha)));
			return ((Integer)hashLinhasId.get(idLinha)).intValue();
		}
		else
		{
			if(hashLinhasId.get(idLinha)==null)
			{
				hashLinhasId.put(idLinha,new Integer(leLinhasServidor(idLinha)));
				return ((Integer)hashLinhasId.get(idLinha)).intValue();
			}
			else
			{
				return ((Integer)hashLinhasId.get(idLinha)).intValue();
			}
		}

	}

	private int leLinhasServidor(String idLinha)
	{
		int qtd = 0;
		StringTokenizer linhasToken;

		if (m_iRetRelatorio != null)
		{
			m_iRetRelatorio.fnPosicionaDados((short)0,0,(short)12);						
			String linhas = m_iRetRelatorio.fnGetDados((short)0,(short)12);
			while(linhas != null && !linhas.equals(""))
			{
				linhasToken = new StringTokenizer(linhas, "\n");
				qtd+=linhasToken.countTokens();

				linhas = null;	
				linhas = m_iRetRelatorio.fnGetDados((short)0,(short)12); 
			}
		}
		return qtd;
	}

	public String getM_IdRel() {
		return m_IdRel;
	}

	public void setM_IdRel(String idRel) {
		m_IdRel = idRel;
	}

	public String getM_Arquivo() {
		return m_Arquivo;
	}

	public void setM_Arquivo(String arquivo) {
		m_Arquivo = arquivo;
	}
}