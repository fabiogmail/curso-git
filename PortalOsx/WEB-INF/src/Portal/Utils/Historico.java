package Portal.Utils;

import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import libjava.indicadores.IndicadorValor;
import libjava.indicadores.IndicadorValorParticipacao;
import libjava.indicadores.IndicadorValorTaxa;
import libjava.tipos.TipoDado;
import libjava.tipos.TipoData;
import libjava.tipos.Vetor;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.HorizontalDateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.VerticalNumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.StandardXYItemRenderer;
import org.jfree.chart.renderer.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import CDRView2.SelecaoIndicadorDesempenho;
import CDRView2.SelecaoIndicadoresPortal;
import CDRView2.SelecaoRecursosDesempenho;
import CDRView2.TelaCDRView;
import Portal.Conexao.CnxServUtil;

// import Componentes.*;
public class Historico
{ 
	private static final String FORMATO_DATA = "dd/MM/yyyy HH:mm";

	// Qtd de linhas de filtros no relatorio
	private static final int QTD_FILTROS = 12;

	// Marca a linha inicial dos contadores
	private static final int INICIO_LINHAS = 16;
	private static final int MAX_ZOOMX = 4;
	private static final int MAX_ZOOMY = 4;

	// Indica a qtd de recursos encontradas no relatorio
	private int m_QtdRecursos = 0;

	// Guarda os primeiros campos de recursos do relatorio
	private String[] m_Recursos = new String[5];
	private Vector m_RecursosSelecionados = new Vector();

	// Guarda os campos de filtros do relatório (no cabecalho)
	private String[] m_Filtros = new String[QTD_FILTROS];

	// Lista de Indicadores selecionados
	private Vector m_IndicadoresSelecionados = new Vector();	

	// Lista de recursos na coluna 1 (encontrados no relatório)
	private Vector m_RecursosCol1 = new Vector();

	// Lista de recursos na coluna 2 (encontrados no relatório)
	private Vector m_RecursosCol2 = new Vector();

	// Lista de contadores do relatório
	private Vector m_ListaContadores = new Vector();

	// Linhas do relatorio
	private Vector m_Linhas = null;

	// Lista de Contadores
	private String[] m_Contadores = null;

	private String[] contadoresArquivo = null;

	// Qtd de contadores
	private int m_QtdContadores = 0;
	private double m_ValorSuperior = 0;
	private Vector m_RecursosAtivosCol1 = new Vector();
	private Vector m_RecursosAtivosCol2 = new Vector();
	
	private libjava.indicadores.IndicadorValor m_IndicadorOK = null;
	private Vector m_ListaBenchmarking = new Vector();
	private Vector m_ListaValoresMedios = new Vector();
	private Vector m_LinhasCabecalho = new Vector();
	private Rectangle.Double m_DataArea = null;
	private boolean m_Aberto;
	private long m_Passo = 0;
	private int m_QtdZoomX;
	private int m_QtdZoomY;
	private double m_PosRelY = -1;
	
	private boolean linhaDuplicadoArquivo = false;
	/**
	 *
	 * @uml.property name="m_ConexUtil"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private CnxServUtil m_ConexUtil;

	/**
	 *
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private SelecaoRecursosDesempenho m_SelecaoRecursosDesempenho;

	/**
	 *
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private SelecaoIndicadorDesempenho m_SelecaoIndicadorDesempenho;
	private Vector m_ListaPeriodos = new Vector();
	private Vector m_ListaPeriodosFinais = new Vector();
	private Vector m_ListaPeriodosApresentados = new Vector();
	private String m_PeriodoInicialVerdadeiro = null;
	private String m_PeriodoFinalVerdadeiro = null;
	private String m_PeriodoInicial = null;
	private String m_PeriodoFinal = null;
	public String m_UltimoFiltroRec1 = null;
	public String m_UltimoFiltroRec2 = null;
	public String m_Periodo1 = null;
	public String m_Periodo2 = null;
	private int m_PosInicio = 0;
	private int m_PosFim = 0;
	public String[] m_ListaIndicadores;
	private String m_IndicadoresEscolhidos = null;
	protected String linhaTotalizacao = "";

	// Vector que possui uma lista com todos os periodos
	private Vector m_PeriodoAnterior = new Vector();

	// Possui Todas as colunas do relatorio processadas
	private String[] m_TodasColunasProcessadas;

	// Possui todos as colunas que foram configuradas para aparecer no relatorio
	private String[] m_TodasColunas;
	private List m_ListaIndicadoresTaxa = new ArrayList();

	/**
	 *
	 * @uml.property name="m_SerieInicial"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private PosAnterior m_SerieInicial;

	/**
	 * @param p_ConexUtil
	 * @param p_SelecaoRecursosDesempenho
	 * @param p_SelecaoIndicadorDesempenho
	 * @param parametros
	 */
	public Historico(CnxServUtil p_ConexUtil, SelecaoRecursosDesempenho p_SelecaoRecursosDesempenho, SelecaoIndicadorDesempenho p_SelecaoIndicadorDesempenho,Vector parametros)
	{
		m_ConexUtil = p_ConexUtil;
		m_SelecaoRecursosDesempenho = p_SelecaoRecursosDesempenho;
		m_SelecaoIndicadorDesempenho = p_SelecaoIndicadorDesempenho;
		String ContadoresDoArquivo = null;
		/**
		* Aqui é feito um 'GATO' para pegar os contadores do Arquivo.
		* Aqui vamos pegar os contadores do relatorio.
		*
		*aqui não vamos receber os dados pq não é eles que nos interessa, esse passo so é executado para 
		*abilitar os contadores.
		*/
//		long tempo = System.currentTimeMillis();
//		m_ConexUtil.getRelatorio2((short) 1,
//				parametros.get(0).toString(),//p_Perfil
//				Integer.parseInt(parametros.get(1).toString()),//p_TipoRel
//				Integer.parseInt(parametros.get(2).toString()),//p_IdRel
//				parametros.get(5).toString(),//p_DataGeracao
//				parametros.get(3).toString(),//p_Arquivo
//				parametros.get(4).toString());//p_NomeRelatorio
//		
//		System.out.println("TEMPO GASTO ANTIGO: "+(System.currentTimeMillis()-tempo));
		/**
		 * possível correção para o "GATO" citado acima. 
		 * 
		 */
		//long tempo2 = System.currentTimeMillis();
		m_ConexUtil.iniciaContadores((short) 1, 
				parametros.get(0).toString(),//p_Perfil 
				Integer.parseInt(parametros.get(1).toString()),//p_TipoRel
				Integer.parseInt(parametros.get(2).toString()),//p_IdRel
				parametros.get(5).toString(),//p_DataGeracao
				parametros.get(3).toString(),//p_Arquivo
				parametros.get(4).toString());//p_NomeRelatorio
		
		//System.out.println("TEMPO GASTO NOVO: "+(System.currentTimeMillis()-tempo2));
		
		ContadoresDoArquivo = m_ConexUtil.getContadoresDoRelatorio();
		/*
		 * m_ListaIndicadores - Array que contem todos os indicadores do
		 * Relatorio de Desempenho
		 *
		 */
		m_ListaIndicadores = fnFromCSV(m_SelecaoIndicadorDesempenho.fnGetListaIndicadores(TelaCDRView.REL_DESEMPENHO));

		for (int a = 0; a < m_ListaIndicadores.length; a++)
		{
			//System.out.println("m_ListaIndicadores: "+m_ListaIndicadores[a]);
			if (((m_ListaIndicadores[a].indexOf("OK") != -1) || (m_ListaIndicadores[a].indexOf("Ok") != -1) || (m_ListaIndicadores[a].indexOf("ok") != -1)) && (m_ListaIndicadores[a].indexOf("%") != -1))
			{
				m_IndicadorOK = m_SelecaoIndicadorDesempenho.fnGetIndicador(m_ListaIndicadores[a], TelaCDRView.REL_DESEMPENHO, null);
				
				break;
			}
		}

		/*
		 * m_Contadores - Array de String que contem todos os Contadores do
		 * Relatorio de Desempenho.
		 *
		 */
		String[] contadores = fnFromCSV(SelecaoIndicadoresPortal.m_iIndicadores.fnContadores(TelaCDRView.REL_DESEMPENHO));
		String[] m_ContadoresCfg =  fnFromCSV(SelecaoIndicadoresPortal.m_iIndicadores.fnContadoresCfg(TelaCDRView.REL_DESEMPENHO));
		
		if(m_ContadoresCfg != null){
			m_Contadores = new String[contadores.length+m_ContadoresCfg.length];
			System.arraycopy(contadores, 0, m_Contadores, 0, contadores.length);
			System.arraycopy(m_ContadoresCfg, 0, m_Contadores, contadores.length, m_ContadoresCfg.length);
		}else{
			m_Contadores = new String[contadores.length];
			System.arraycopy(contadores, 0, m_Contadores, 0, contadores.length);
		}
		
		m_QtdContadores = m_Contadores.length;
		
	}

	/**
	 * Metodo responsavel em formatar a Data.
	 *
	 * @param p_Data -
	 *            Data no formato AAAAMMDD. Ex: 20041125
	 *
	 * @return - Data no Formato DD/MM/AAAA. Ex: 25/11/2004
	 */
//	public long fnDataHora(String p_Data)
//	{
//		TipoData data = new TipoData(p_Data);
//		return data.getTimeInMillis();
//	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Data
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String fnStrDataHora(String p_Data)
	{
		/**
		 * aqui ele chama a função transforma data que corrige a falha nos meses
		 * formatando com um objeto simpleDateFormat ao inves de fnconverte
		 * */
		SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);		
		return formatoData.format(transformaData(p_Data));
	}

	/**
	 * Método responsavel em limpar a lista de Series Historicas.
	 *
	 */
	public void fnZera()
	{
		m_QtdZoomX = 0;
		m_QtdZoomY = 0;
		m_PosRelY = 0;
		m_ValorSuperior = 0;

		int i = m_PeriodoAnterior.size();
		System.out.println("\n ZERA - m_PeriodoAnterior.size(): " + m_PeriodoAnterior.size() + "\n");

		System.out.println("\n ZERA - m_ListaContadores.size(): " + m_ListaContadores.size() + "\n");
		m_PeriodoAnterior.removeAllElements();
		m_PeriodoAnterior.addElement(m_SerieInicial);
		fnReCarrega(m_SerieInicial.m_Recurso1, m_SerieInicial.m_Recurso2, m_SerieInicial.m_PeriodoInicial, m_SerieInicial.m_PeriodoFinal);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param from
	 *            DOCUMENT ME!
	 * @param to
	 *            DOCUMENT ME!
	 * @param low
	 *            DOCUMENT ME!
	 * @param high
	 *            DOCUMENT ME!
	 */
	public void fnSort(long[] from, long[] to, int low, int high)
	{
		if ((high - low) < 2)
		{
			return;
		}

		int middle = (low + high) / 2;
		fnSort(to, from, low, middle);
		fnSort(to, from, middle, high);

		int p = low;
		int q = middle;

		if (from[middle - 1] < from[middle])
		{
			for (int i = low; i < high; i++)
				to[i] = from[i];

			return;
		}

		for (int i = low; i < high; i++)
		{
			if ((q >= high) || ((p < middle) && (from[p] < from[q])))
			{
				to[i] = from[p++];
			}
			else
			{
				to[i] = from[q++];
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Vector fnGetPeriodosIniciais()
	{
		return m_ListaPeriodos;
	}
	
	public Vector fnGetPeriodosFinais()
	{
		return m_ListaPeriodosFinais;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Vector fnLinhasCabecalho()
	{
		return m_LinhasCabecalho;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String fnPeriodoInicial()
	{
		return m_PeriodoInicial;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String fnPeriodoFinal()
	{
		return m_PeriodoFinal;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Perfil
	 *            DOCUMENT ME!
	 * @param p_TipoRel
	 *            DOCUMENT ME!
	 * @param p_IdRel
	 *            DOCUMENT ME!
	 * @param p_Arquivo
	 *            DOCUMENT ME!
	 * @param p_NomeRelatorio
	 *            DOCUMENT ME!
	 * @param p_DataGeracao
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean fnLeRelatorio(String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, String p_NomeRelatorio, String p_DataGeracao)
	{
		m_QtdZoomX = 0;
		m_QtdZoomY = 0;
		m_PosRelY = 0;
		m_ValorSuperior = 0;
		
		m_Linhas = m_ConexUtil.getRelatorio2((short) 1,
											 p_Perfil,
											 Integer.parseInt(p_TipoRel),
											 Integer.parseInt(p_IdRel),
											 p_DataGeracao,
											 p_Arquivo,
											 p_NomeRelatorio);
		
		System.out.println("Quantidade de Linhas no arquivo: "+m_Linhas.size());
		
		
		if (m_Linhas != null)
		{
			// Le os filtros
			for (int i = 5; i < QTD_FILTROS; i++)
			{
				m_Filtros[i] = (String) m_Linhas.elementAt(i);
				
				//System.out.println("m_linhas: "+);

				if (m_Filtros[i].length() == 0)
				{
					m_Filtros[i] = "Sem Filtro";
				}

				break;
			}

			// Le a lista de colunas do relatorio
			if (m_IndicadoresEscolhidos == null)
			{
				//fnSetColunas (String p_Recurso1, String p_Recurso2, String
				// p_LinhaColunas)
				fnSetColunas((String) m_Linhas.elementAt(12), (String) m_Linhas.elementAt(13), (String) m_Linhas.elementAt(14));
			}
			else
			{
				//         	fnSetColunas (String p_Recurso1, String p_Recurso2, String
				// p_LinhaColunas)
				fnSetColunas((String) m_Linhas.elementAt(12), (String) m_Linhas.elementAt(13), m_IndicadoresEscolhidos);
			}

			//fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, null, null);
			if (m_UltimoFiltroRec1 == null)
			{
				m_UltimoFiltroRec1 = (String) m_Linhas.elementAt(12);
			}

			if (m_UltimoFiltroRec2 == null)
			{
				m_UltimoFiltroRec2 = (String) m_Linhas.elementAt(13);
			}
			/**
			 * aqui ele chama a função transforma data que corrige a falha nos meses
			 * formatando com um objeto simpleDateFormat ao inves de fnconverte
			 * */
			SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
			String dataInicio = formatoData.format(transformaData((String) m_Linhas.elementAt(3)));
			String dataFim = formatoData.format(transformaData((String) m_Linhas.elementAt(4)));
			//antes era assim new TipoData(formataData((String) m_Linhas.elementAt(3))).fnConverte(FORMATO_DATA);

			fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, dataInicio, dataFim);

			return true;
		}
		else
		{
			System.out.println("Linha == null");

			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_PeriodoInicial
	 *            DOCUMENT ME!
	 * @param p_PeriodoFinal
	 *            DOCUMENT ME!
	 */
	public void fnSetPeriodos(String p_PeriodoInicial, String p_PeriodoFinal)
	{
		fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, p_PeriodoInicial, p_PeriodoFinal);
	}

	/**
	 * DOCUMENT ME!
	 */
	public void fnEsquerda()
	{
		if ((m_PosInicio <= 0) || (m_PosFim == -1))
		{
			return;
		}

		fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, (String) m_ListaPeriodos.elementAt(m_PosInicio - 1), (String) m_ListaPeriodos.elementAt(m_PosFim - 1));
	}

	/**
	 * DOCUMENT ME!
	 */
	public void fnDireita()
	{
		if ((m_PosInicio == -1) || (m_PosFim == -1) || (m_PosFim == (m_ListaPeriodos.size() - 1)))
		{
			return;
		}

		fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, (String) m_ListaPeriodos.elementAt(m_PosInicio + 1), (String) m_ListaPeriodos.elementAt(m_PosFim + 1));
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_FiltroRec1
	 *            DOCUMENT ME!
	 * @param p_FiltroRec2
	 *            DOCUMENT ME!
	 * @param p_PeriodoInicial
	 *            DOCUMENT ME!
	 * @param p_PeriodoFinal
	 *            DOCUMENT ME!
	 */
	public void fnReCarrega(String p_FiltroRec1, String p_FiltroRec2, String p_PeriodoInicial, String p_PeriodoFinal)
	{
		m_PeriodoInicial = null;
		m_PeriodoFinal = null;
		m_PeriodoInicialVerdadeiro = null;
		m_PeriodoFinalVerdadeiro = null;
		m_PosInicio = -1;
		m_PosFim = -1;
		
		String[] l_FiltroRec1 = fnFromCSV(p_FiltroRec1);
		String[] l_FiltroRec2 = fnFromCSV(p_FiltroRec2);

		if (((l_FiltroRec1 != null) && (l_FiltroRec1.length > 1)) || ((l_FiltroRec2 != null) && (l_FiltroRec2.length > 1)))
		{
			m_Aberto = true; // Significa q ha mais de um recurso selecionado.
		}
		else
		{
			m_Aberto = false;
		}

		int a = 0;
		long l_SegundoAnterior = 0;
		m_Passo = 0;

		boolean l_Comecou = false;
		boolean l_Tem2 = false;
		boolean l_EhPraParar = false;
		boolean latencia = false;
		String l_RecursoPrincipal1 = null;
		String l_RecursoPrincipal2 = null;
		
		if (m_Linhas != null)
		{			
			m_LinhasCabecalho.removeAllElements();
			m_ListaContadores.removeAllElements();
			m_ListaPeriodos.removeAllElements();
			m_ListaPeriodosFinais.removeAllElements();
			m_ListaPeriodosApresentados.removeAllElements();
			m_RecursosCol1.removeAllElements();
			m_RecursosCol2.removeAllElements();
			m_RecursosAtivosCol1.removeAllElements();
			m_RecursosAtivosCol2.removeAllElements();

			if(p_PeriodoInicial == null && p_PeriodoFinal == null){
				p_PeriodoInicial = m_SerieInicial.m_PeriodoInicial;
				p_PeriodoFinal = m_SerieInicial.m_PeriodoFinal;
			}
			
			String periodoInicialLatencia = "";
			
			Vector nova_m_linha = new Vector(m_Linhas);
			boolean flag1=false,flag2=false, flag3=false,removeu=false;
			String periodo_inicial_backup = p_PeriodoInicial;
			/**
			 *flag 1 e flag2 são usadas para determinar quais as linha do novo grafico a ser formado devem
			 * ser retiradas.
			 * A flag removeu e usada para determinar se uma linha foi removida ou não, porque caso
			 * ela tenha sido removida as operações que se sequem a baixo não devem ser feitas. 
			 * */
			
			for (int i = INICIO_LINHAS; i < nova_m_linha.size(); i++)
			{				
				String l_Linha = (String) nova_m_linha.elementAt(i);
				String l_Linha2 = (String) nova_m_linha.elementAt(i);
				if(((l_Linha2.indexOf("0;"))==0)&&(flag3==false)){
					flag3=true;
					linhaTotalizacao = l_Linha2;
				}

				if(l_Linha2.startsWith("9;")){
					contadoresArquivo = l_Linha2.substring(2, l_Linha2.length()).split(";");
				}
				/**Este novo if, foi criado para a opcao selecionar periodo, o que ela faz é 
				 * retirar do arquivo principal as coordenadas(coordenadas estao no vector m_Linhas)
				 * desnecessarias. 
				 * */
				if ((l_Linha2.indexOf("0;") == 0) || (l_Linha2.indexOf("6;") == 0))
				{

					String[] l_LinhaToken = fnFromCSV(l_Linha2);					
					String[] posicao1 = new String[1];					
					posicao1[0] = l_LinhaToken[1];					
					String[] posicao2 = new String[1]; 
					posicao2[0] = l_LinhaToken[2];
					
					
					
					/**
					 * aqui ele chama a função transforma data que corrige a falha nos meses
					 * formatando com um objeto simpleDateFormat ao inves de fnconverte
					 * */
					SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
					String hora_Inicial = formatoData.format(transformaData(posicao1[0]));
					String hora_Final = formatoData.format(transformaData((posicao2[0])));
					
					double hora_certa_do_arquivo = Double.parseDouble(posicao1[0]);
					
					String[] horas2 = p_PeriodoInicial.split("/"); //aqui esta convertendo a data/hora 					
					String[] p_hora1 = horas2[2].split(" ");    //do formato 20/08/2004 08:00
					String[] p_hora2 = p_hora1[1].split(":");   //para 20040820080000
					String p_hora3 = p_hora1[0]+horas2[1]+horas2[0]+p_hora2[0]+p_hora2[1]+"00";
					double hora_certa = Double.parseDouble(p_hora3);
					/**aqui senta um novo periodo inicial caso o numero de periodos existente
					*entre os intervalos selecionados seja maior que os periodos que o usuario 
					*informou.
					**/
					if(hora_certa_do_arquivo > hora_certa){
						p_PeriodoInicial = hora_Inicial;
					}
					
					
					if(hora_Inicial.equals(p_PeriodoInicial)){
						flag1 = true;
					}
					if(!hora_Inicial.equals(p_PeriodoInicial)){						
						if(flag1 != true){
							nova_m_linha.removeElementAt(i);
							removeu = true;
						}
					}
					if(hora_Final.equals(p_PeriodoFinal)){
						flag2 = true;
					}else
					{
						if(flag2 == true){
							nova_m_linha.removeElementAt(i);
							removeu = true;
						}
					}
				}
				if(removeu == false){
					if ((l_Linha.indexOf("0;") == 0) || (l_Linha.indexOf("6;") == 0))
					{
						String[] l_LinhaToken = fnFromCSV(l_Linha);
						String l_NomeRecurso1 = l_LinhaToken[3];
						String l_NomeRecurso2 = null;
	
						if (m_QtdRecursos > 1)
						{
							l_NomeRecurso2 = l_LinhaToken[4];
						}
	
						if (l_RecursoPrincipal1 == null)
						{
							l_RecursoPrincipal1 = l_NomeRecurso1;
						}
	
						if (l_RecursoPrincipal2 == null)
						{
							l_RecursoPrincipal2 = l_NomeRecurso2;
						}
	
						if ((l_Linha.indexOf("0;") == 0) || (!l_RecursoPrincipal1.equals(l_NomeRecurso1)) || ((l_RecursoPrincipal2 != null) && (!l_RecursoPrincipal2.equals(l_NomeRecurso2))))
						{ // MUDAR!!!
						  // IMPL
						  // AMARRADA A 1
						  // POSICAO DOS
						  // INDICADORES!!!!
	
							long l_QtdCham = Integer.parseInt(l_LinhaToken[3 + m_QtdRecursos]);
	
							String[] l_Contadores = libjava.tipos.TipoDadoString.fnTokenLinha(l_Linha.substring(2)); //null; //
																													 // BIRA
																													 // FALTA -
																													 // NoCampoArvoreRelatorioDesempenho.fnGetContador(m_Recursos,
																													 // l_Linha,
																													 // true);
	
							// long l_Segundo = fnDataHora(l_Contadores);
							if ((l_Linha.indexOf("0;") == 0) && !latencia)
							{
								/*
								 * Pega o 1 periodo com linhas de Relatorio. Levando
								 * em consideração existe um periodo de Latencia
								 * configurado. Exemplo: Para um relatorio com as
								 * config.: DataIni=20031128080100 (28/12/2003
								 * 08:01hs) DataFim=20031129000100 (29/12/2003
								 * 00:01hs) Latencia de 30 min. A primeira linha do
								 * Relatorio começará com:
								 * 0;20031128083100;20031128084600;... (28/12/2003
								 * 08:31hs até 28/12/2003 08:46hs)
								 *
								 */
	
								//periodoInicialLatencia = l_Contadores[0];
								/**
								 * aqui ele chama a função transforma data que corrige a falha nos meses
								 * formatando com um objeto simpleDateFormat ao inves de fnconverte
								 * */
								SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
								//periodoInicialLatencia = new TipoData (formataData(l_Contadores[0])).fnConverte(FORMATO_DATA);
								periodoInicialLatencia = formatoData.format(transformaData(l_Contadores[0]));
								latencia = true;
							}
	
							long l_Segundo = formataData(l_Contadores[0]);
	
							if ((l_SegundoAnterior != 0) && ((l_Segundo - l_SegundoAnterior) > m_Passo))
							{
								m_Passo = l_Segundo - l_SegundoAnterior;
							}
	
							l_SegundoAnterior = l_Segundo;
	
							boolean l_Aceita = true;
	
							if ((l_NomeRecurso1 != null) && !l_NomeRecurso1.equals(""))
							{
								if (l_FiltroRec1 == null)
								{
									l_FiltroRec1 = new String[1];
									l_FiltroRec1[0] = l_NomeRecurso1;
								}
								else
								{
									l_Aceita = false;
	
									for (int x = 0; x < l_FiltroRec1.length; x++)
									{
										if (l_FiltroRec1[x].equals(l_NomeRecurso1))
										{
											l_Aceita = true;
	
											break;
										}
									}
								}
	
								SerieRecursos l_Serie = (SerieRecursos) fnProcura(m_RecursosCol1, l_NomeRecurso1);
	
								if (l_Serie == null)
								{
									l_Serie = new SerieRecursos(l_NomeRecurso1);
									m_RecursosCol1.add(l_Serie);
								}
	
								l_Serie.fnTrataContadores(l_Contadores);
	
								if ((l_NomeRecurso2 != null) && !l_NomeRecurso2.equals(""))
								{
									l_Tem2 = true;
	
									if (l_FiltroRec2 == null)
									{
										l_FiltroRec2 = new String[1];
										l_FiltroRec2[0] = l_NomeRecurso2;
									}
									else
									{
										if (l_Aceita)
										{
											l_Aceita = false;
	
											for (int x = 0;
												 x < l_FiltroRec2.length; x++)
											{
												if (l_FiltroRec2[x].equals(l_NomeRecurso2))
												{
													l_Aceita = true;
	
													break;
												}
											}
										}
									}
	
									//                        System.out.println("
									// l_NomeRecurso2="+l_NomeRecurso2+",
									// m_QtdRecursos="+m_QtdRecursos);
									if (fnProcura(m_RecursosCol2, l_NomeRecurso2) == null)
									{
										System.out.println("E novo");
										m_RecursosCol2.add(l_NomeRecurso2);
									}
								}
	
								//Convertendo o periodo inicial de cada linha do
								// relatorio em um formato mais amigavel
								//String l_Periodo = new TipoData(formataData(l_Contadores[0])).fnConverte(FORMATO_DATA);
								/**
								 * aqui ele chama a função transforma data que corrige a falha nos meses
								 * formatando com um objeto simpleDateFormat ao inves de fnconverte
								 * */
								SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
								String l_Periodo = formatoData.format(transformaData(l_Contadores[0]));
								String l_PeriodoFinal = formatoData.format(transformaData(l_Contadores[1]));
								
								if (m_PeriodoInicialVerdadeiro == null)
								{
									m_PeriodoInicialVerdadeiro = l_Periodo; // Marca
																			// o
																			// periodo
																			// inicial
																			// da
																			// serie
																			// historica
								}
	
								m_PeriodoFinalVerdadeiro = l_PeriodoFinal; // Marca o
																	  // periodo
																	  // final da
																	  // serie
																	  // historica
	
								if (l_Aceita)
								{
									int l_Pos = 0;
	
									// System.out.println("l_NomeRecurso1="+l_NomeRecurso1+",
									// l_NomeRecurso2="+l_NomeRecurso2+", Aceitando
									// linha "+l_Linha);
									if (fnProcura(m_ListaPeriodos, l_Periodo) == null)
									{
										l_Pos = m_ListaPeriodos.size();
										m_ListaPeriodos.add(l_Periodo);
									}
									if (fnProcura(m_ListaPeriodosFinais, l_PeriodoFinal) == null)
									{
										l_Pos = m_ListaPeriodosFinais.size();
										m_ListaPeriodosFinais.add(l_PeriodoFinal);
									}
	
									// System.out.println(l_Periodo+"
									// Comecou="+l_Comecou+" De:"+p_PeriodoInicial+"
									// até "+p_PeriodoFinal);
									// if ((!l_Comecou) && /*(p_PeriodoInicial ==
									// null*/ l_Periodo.equals(p_PeriodoInicial))
									if ((!l_Comecou) && (l_Periodo.equals(p_PeriodoInicial) || l_Periodo.equals(periodoInicialLatencia)))
									{
										l_Comecou = true;
	
										if (p_PeriodoInicial != null)
										{
											m_PosInicio = l_Pos;
										}
									}
	
									//if (l_EhPraParar && p_PeriodoFinal != null &&
									// !l_Periodo.equals(p_PeriodoFinal))
									if (l_EhPraParar && (p_PeriodoFinal != null) && !l_Contadores[0].equals(p_PeriodoFinal))
									{
										l_Comecou = false;
									}
	
									if (l_Comecou)
									{
										a++;
										m_ListaPeriodosApresentados.add(l_Periodo);
										m_ListaContadores.add(new ContadoresHistorico(l_Contadores, l_NomeRecurso1, l_NomeRecurso2));
										
										if (m_PeriodoInicial == null)
										{
											m_PeriodoInicial = l_Periodo;
										}
	
										m_PeriodoFinal = l_Periodo;
	
										if (fnProcura(m_RecursosAtivosCol1, l_NomeRecurso1) == null)
										{
											m_RecursosAtivosCol1.add(l_NomeRecurso1);
										}
	
										if ((l_NomeRecurso2 != null) && (fnProcura(m_RecursosAtivosCol2, l_NomeRecurso2) == null))
										{
											m_RecursosAtivosCol2.add(l_NomeRecurso2);
										}
									}
	
									//if (l_Comecou && p_PeriodoFinal != null &&
									// l_Periodo.equals(p_PeriodoFinal))
									if (l_Comecou && (p_PeriodoFinal != null) && l_Contadores[0].equals(p_PeriodoFinal))
									{
										l_EhPraParar = true;
										m_PosFim = l_Pos;
									}
								}
							}
						}
					}				
				}
				if (l_Linha.indexOf("5;") == 0)
				{
					//               System.out.println(l_Linha);
					String[] l_LinhaToken = fnFromCSV(l_Linha);
					m_LinhasCabecalho.add(l_LinhaToken[1] + ((l_LinhaToken.length > 2) ? (" = " + l_LinhaToken[2]) : ""));
				}
				removeu = false;
			}//final do for
			p_PeriodoInicial = periodo_inicial_backup;
			m_ListaContadores.trimToSize();
			

			/*
			 * System.out.println("RECARREGA - m_ListaContadores.size(): " +
			 * m_ListaContadores.size()); System.out.println("RECARREGA -
			 * m_ListaPeriodosApresentados.size(): " +
			 * m_ListaPeriodosApresentados.size());
			 */			
		}

		if (p_FiltroRec1 == null)
		{
			m_UltimoFiltroRec1 = l_FiltroRec1[0];
		}
		else
		{
			m_UltimoFiltroRec1 = p_FiltroRec1;
		}

		if (l_Tem2)
		{
			if (p_FiltroRec2 == null)
			{
				m_UltimoFiltroRec2 = l_FiltroRec2[0];
			}
			else
			{
				m_UltimoFiltroRec2 = p_FiltroRec2;
			}
		}

		PosAnterior l_PosAnterior = new PosAnterior();
		l_PosAnterior.m_PeriodoInicial = p_PeriodoInicial;
		l_PosAnterior.m_PeriodoFinal = p_PeriodoFinal;
		l_PosAnterior.m_Recurso1 = p_FiltroRec1;
		l_PosAnterior.m_Recurso2 = p_FiltroRec2;
		l_PosAnterior.m_QtdZoomX = m_QtdZoomX;
		l_PosAnterior.m_QtdZoomY = m_QtdZoomY;
		l_PosAnterior.m_PosRelY = m_PosRelY;
		l_PosAnterior.m_ValorSuperior = m_ValorSuperior;
		m_PeriodoAnterior.addElement(l_PosAnterior);

		/*
		 * System.out.println("\n RECARREGA - m_PeriodoAnterior.size(): " +
		 * m_PeriodoAnterior.size() + "\n");
		 */
		if (m_SerieInicial == null)
		{
			m_SerieInicial = l_PosAnterior;
		}

		// System.out.println("Adicionando a undo");

		/* */
	}

	/**
	 * DOCUMENT ME!
	 */
	public void fnUndo()
	{
		// Retira o ultimo periodo da pilha
		fnPop();

		// Mostra o periodo que esta no topo da pilha
		PosAnterior l_PosAnterior = fnTop();

		m_Periodo1 = l_PosAnterior.m_Recurso1;
		m_Periodo2 = l_PosAnterior.m_Recurso2;
		
		if (l_PosAnterior != null)
		{
			m_QtdZoomX = l_PosAnterior.m_QtdZoomX;
			m_QtdZoomY = l_PosAnterior.m_QtdZoomY;
			m_PosRelY = l_PosAnterior.m_PosRelY;
			m_ValorSuperior = l_PosAnterior.m_ValorSuperior;
			fnReCarrega(l_PosAnterior.m_Recurso1, l_PosAnterior.m_Recurso2, l_PosAnterior.m_PeriodoInicial, l_PosAnterior.m_PeriodoFinal);
			fnPop();
		}
		else
		{
			m_QtdZoomX = 0;
			m_QtdZoomY = 0;
			m_PosRelY = 0;
			m_ValorSuperior = 0;
			fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, null, null);
		}

		System.out.println("\n UNDO - m_PeriodoAnterior.size(): " + m_PeriodoAnterior.size() + "\n");
		m_PeriodoAnterior.trimToSize();
		System.out.println("\n UNDO - m_ListaContadores.size(): " + m_ListaContadores.size() + "\n");
	}

	/**
	 * DOCUMENT ME!
	 */
	public void fnPop()
	{
		int l_Ultimo = m_PeriodoAnterior.size() - 1;

		if (l_Ultimo > 0)
		{
			m_PeriodoAnterior.removeElementAt(l_Ultimo);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public PosAnterior fnTop()
	{
		int l_Ultimo = m_PeriodoAnterior.size() - 1;

		if (l_Ultimo < 0)
		{
			return null;
		}

		PosAnterior l_PosAnterior = (PosAnterior) m_PeriodoAnterior.elementAt(l_Ultimo);

		return l_PosAnterior;
	}

	/**
	 * Retorna a lista de filtros do cabeçalho do relatório
	 */
	public String[] fnFiltros()
	{
		return m_Filtros;
	}

	/**
	 * Retorna se o indicador está ou não setado
	 */
	public boolean fnIndicadorSetado(String p_NomeIndicador)
	{
		for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
		{
			if (((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(a)).fnTitulo().equals(p_NomeIndicador))
			{
				//            System.out.println(p_NomeIndicador+" Encontrado");
				return true;
			}
		}

		//      System.out.println(p_NomeIndicador+" Não Encontrado");
		return false;
	}

	/**
	 * Seta a lista de indicadores selecionados para o relatório
	 */
	public String fnGetTabelaIndicadores()
	{
		return fnToCSV(m_IndicadoresSelecionados);
	}

	/**
	 * Seta a lista de indicadores selecionados para o relatório
	 */
	public void fnSetTabelaIndicadores(String p_LinhaIndicadores)
	{
		m_IndicadoresEscolhidos = p_LinhaIndicadores;

		//fnSetTabelaIndicadores(fnFromCSV(p_LinhaIndicadores), 0);
		fnSetTabelaIndicadores(fnFromCSV(m_IndicadoresEscolhidos),
							   0);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Colunas
	 *            DOCUMENT ME!
	 * @param p_Inicio
	 *            DOCUMENT ME!
	 */
	public void fnSetTabelaIndicadores(String[] p_Colunas, int p_Inicio)
	{
		m_IndicadoresSelecionados.removeAllElements();

		m_ListaIndicadoresTaxa.clear();				
		for (int j = p_Inicio; j < p_Colunas.length; j++)
		{
			libjava.indicadores.IndicadorValor l_Indicador = m_SelecaoIndicadorDesempenho.fnGetIndicador(p_Colunas[j], m_Recursos, TelaCDRView.REL_DESEMPENHO, contadoresArquivo==null?null:new Vetor(contadoresArquivo));

			if (l_Indicador != null)
			{
				m_IndicadoresSelecionados.addElement(l_Indicador);
			}
		}

		m_IndicadoresSelecionados.trimToSize();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Recurso1
	 *            DOCUMENT ME!
	 * @param p_Recurso2
	 *            DOCUMENT ME!
	 * @param p_LinhaColunas
	 *            DOCUMENT ME!
	 */
	public void fnSetColunas(String p_Recurso1, String p_Recurso2, String p_LinhaColunas)
	{
		String[] l_Colunas = fnFromCSV(p_LinhaColunas); // Array com todas as
														// colunas configuradas
														// para aparecer no
														// relatorio

		String[] l_Rec = new String[3]; // Array que contem todos os Recursos
		String l_Indicadores = null;
		m_QtdRecursos = 0;

		int l_Pos = 0;

		if (l_Colunas[0].equals("DataInicial"))
		{
			l_Pos = 2;
		}

		// Identifica quais os recursos do relatório
		l_Rec = Agenda.identificaRecursos(VetorUtil.String2Vetor(p_LinhaColunas, ';'),
										  p_Recurso1,
										  p_Recurso2);

		// Retira o primeiro elemento da lista de indicadores SELECIONADOS pois
		// é recurso COM CERTEZA!!
		l_Indicadores = p_LinhaColunas.substring(p_LinhaColunas.indexOf(";") + 1,
												 p_LinhaColunas.length());

		// Retira o segundo elemento da lista de indicadores SELECIONADOS caso
		// ele seja recurso
		if (l_Rec[1] != null)
		{
			l_Indicadores = l_Indicadores.substring(l_Indicadores.indexOf(";") + 1,
													l_Indicadores.length());
		}

		m_RecursosSelecionados.removeAllElements();

		for (int a = 0; a < m_Recursos.length; a++)
			m_Recursos[a] = null;

		m_Recursos[0] = "DtInicio";
		m_Recursos[1] = "DtFim";

		if (!p_Recurso1.equals(""))
		{
			m_Recursos[2 + m_QtdRecursos++] = l_Colunas[l_Pos];
			if(m_SelecaoRecursosDesempenho.fnGetIndicador(l_Colunas[l_Pos], l_Rec, TelaCDRView.REL_DESEMPENHO, null) != null)
				m_RecursosSelecionados.addElement(m_SelecaoRecursosDesempenho.fnGetIndicador(l_Colunas[l_Pos], l_Rec, TelaCDRView.REL_DESEMPENHO, null));
			else
				m_RecursosSelecionados.addElement(p_Recurso1);
		}

		if (!p_Recurso2.equals(""))
		{
			m_Recursos[2 + m_QtdRecursos++] = l_Colunas[l_Pos + 1];
			if(m_SelecaoRecursosDesempenho.fnGetIndicador(l_Colunas[l_Pos + 1], l_Rec, TelaCDRView.REL_DESEMPENHO, null) != null)
				m_RecursosSelecionados.addElement(m_SelecaoRecursosDesempenho.fnGetIndicador(l_Colunas[l_Pos + 1], l_Rec, TelaCDRView.REL_DESEMPENHO, null));
			else//caso do historico com desempenho2 em que pode vir recursos sem ser bilhetador, rotas, troncos
				m_RecursosSelecionados.add(p_Recurso2);
		}

		m_RecursosSelecionados.trimToSize();

		if (m_IndicadoresEscolhidos == null)
		{
			fnSetTabelaIndicadores(l_Colunas, m_QtdRecursos);
		}
		else
		{
			fnSetTabelaIndicadores(l_Colunas, 0);
		}
	}

	/**
	 * Retorna o título do recurso 1
	 */
	public String fnTituloRecurso1()
	{
		return m_Recursos[2];
	}

	/**
	 * Retorna o título do recurso 2
	 */
	public String fnTituloRecurso2()
	{
		return m_Recursos[3];
	}

	/**
	 * Retorna a lista de recursos 1
	 */
	public Vector fnListaRecursosVec1()
	{
		return m_RecursosCol1;
	}

	/**
	 * Retorna a lista de recursos 1
	 */
	public Vector fnListaRecursosVec2()
	{
		return m_RecursosCol2;
	}

	
	private void adicionaSerieHistorica(TimeSeriesCollection p_SeriePrincipal, 
	                                    TimeSeriesCollection p_EixoY, 
	                                    TimeSeries p_Serie,
	                                    IndicadorValor p_Indicador )
	{
	    /**
		 * Verifica se o indicador selecionado é um indicador do Tipo TAXA.
		 * Caso positivo o objeto TimesSeries respectivo sera adicionado em uma outra colecao de 
		 * series (seriesCollectionSegundoEixoY). 
		 * Por se tratarem de indicadores do tipo TAXA o intervalo do eixo Y será entre 0% e 100%.
		 * */
		if ((p_Indicador instanceof IndicadorValorTaxa) || 
			 p_Indicador instanceof IndicadorValorParticipacao)
		{
			//p_EixoY.removeSeries(p_Serie);// 'GATO para não adicionar series repetidas'
		    p_EixoY.addSeries(p_Serie);
		}
		/**
		 * Adiciona a serie historica de cada contador a uma colecao de series historicas.
		 * Esse objeto l_TimeSeriesCollection é o objeto que contem todas as series historicas dos
		 * indicadores selecionados pelo usuario na interface.
		 * */
		else
		{
			//p_SeriePrincipal.removeSeries(p_Serie);// 'GATO para não adicionar series repetidas'
			p_SeriePrincipal.addSeries(p_Serie);
		}
	}
	
	/**
	 * Metodo responsavel em gerar uma imagem correspondente ao grafico.
	 * Os parametros X e Y passados para esse metodo sao utilizados como base para 
	 * saber qual a área a ser realizado o zoom do grafico.
	 *
	 * @param p_X - coordenada X da imagem
	 * @param p_Y - coordenada Y da imagem
	 * @param p_NomeArq - Nome da imagem gerada
	 */
	public void fnGeraRelatorioGrafico(int p_X, int p_Y, String p_NomeArq)
	{
		double l_MaiorValor = 0; // Variavel utilizada para determinar o valor superior do eixo Y
		boolean temRecursos = false;

		/**
		 * Objeto que ira armazenar a colecao de objetos TimesSeries (referentes à série histórica
		 * de cada Indicador).
		 * */
		TimeSeriesCollection l_TimeSeriesCollection = new TimeSeriesCollection();
		
		/**
		 * Objeto que ira armazenar a colecao de objetos TimesSeries (referentes à série histórica
		 * de cada Indicador do tipo TAXA).
		 * */
		TimeSeriesCollection seriesCollectionSegundoEixoY = new TimeSeriesCollection();
		
		/**
		 * Objeto que ira armazenar a serie historica de cada Indicador Selecionado.
		 * */
		TimeSeries l_TimeSeries = null;
		
		/** Objeto do JFreechar responsavel em gerar um grafico estilo XY  */
		XYPlot l_XYPlot = null;

		/**
		 * Laco que percorre todos os indicadores selecionados.
		 * Nesse laco eh calculado o valor de cada indicador durante o periodo configurado
		 * para montar a serie historica de cada um deles.
		 * */
		for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
		{
			libjava.indicadores.IndicadorValor l_Indicador = ((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(a));
			
			/**
			 * O GATO "m_Aberto" estará setada como TRUE se houver mudança nos recursos selecionados.
			 * Exemplo: Inicialmente a configuracao estava setada para mostrar TODOS os bilhetadores e,
			 * agora o usuario selecionou apenas um Bilhetador especifico.
			 * 
			 * Essa FLAG eh setada no metodo "fnRecarrega()" 
			 * */
			if (m_Aberto)
			{
				/**
				 * Laco que percorre todos os recursos (pode ser mais de 1) setados na 1 coluna.
				 * A 1 coluna refere-se ao recurso BILHETADOR.
				 * 
				 * Ex: O usuario setou os bilhetadores BLM e BIL1.
				 * */
				for (int c = 0; c < m_RecursosAtivosCol1.size(); c++)
				{
					String l_Recurso1 = m_RecursosAtivosCol1.elementAt(c).toString();
					/**
					 * Teste para verficar se há recursos selecionados na Segunda coluna.
					 * A segunda coluna pode ser ROTAE ou ROTAS.
					 * */
					if (m_RecursosAtivosCol2.size() == 0) // Se nao houverem recursos setados para 2º coluna...
					{
						l_TimeSeries = new TimeSeries(l_Recurso1 + " " + l_Indicador.fnTitulo(), Minute.class);
						for (int b = 0; b < m_ListaContadores.size(); b++)
						{
							ContadoresHistorico l_ContadoresHistorico = (ContadoresHistorico) m_ListaContadores.elementAt(b);
							String[] l_Contadores = l_ContadoresHistorico.m_Contadores;

							/** l_Contadores[2]: é assumido que posicao 2 do array l_Contadores refere-se 
							 * ao Recurso da 1 coluna, isto é, ao BILHETADOR.
							 * */
							if (l_Contadores[2].equals(l_Recurso1)) 
							{
								// De forma análoga, é assumido que a posicao 0 do array l_Contadores refere-se a DATA INICIAL 
								TipoData l_Data = new TipoData(l_Contadores[0]); 
								// Processamento de todos os contadores necessarios para se calcular o valor do indicador.
								double l_Valor = ((Number) l_Indicador.fnProcessa(l_Contadores,linhaTotalizacao.split(";")).fnValor()).doubleValue();

								/**
								 * Verifica se o valor do contador eh maior do que o maior valor "achado".
								 * Essa variavel l_MaiorValor é utilizada para determinar o maior valor do eixo Y.
								 * */
								if (l_Valor > l_MaiorValor)
								{
									l_MaiorValor = l_Valor;
								}
								/**
								 * Cria cada ponto da serie do contador (baseado nas coordenadas Tempo x Valor)
								 * */

								l_TimeSeries.add(new Minute(transformaData(l_Data.fnStrValor())),l_Valor);
							}
						}
						temRecursos = true;//teste
						adicionaSerieHistorica(l_TimeSeriesCollection,seriesCollectionSegundoEixoY,l_TimeSeries,l_Indicador);
					}
					else  // Caso hajam recursos setados para 2º coluna...
					{
						for (int d = 0; d < m_RecursosAtivosCol2.size(); d++)
						{
							String l_Recurso2 = m_RecursosAtivosCol2.elementAt(d).toString();
							l_TimeSeries = new TimeSeries(l_Recurso1 + " " + l_Recurso2 + " " + l_Indicador.fnTitulo(), Minute.class);

							for (int b = 0; b < m_ListaContadores.size(); b++)
							{
								ContadoresHistorico l_ContadoresHistorico = (ContadoresHistorico) m_ListaContadores.elementAt(b);
								String[] l_Contadores = l_ContadoresHistorico.m_Contadores;
								/**
								 * É assumido que na posicao 2 do array l_Contadores se encontra o Recurso Bilhetador (Coluna 1) e,
								 * na posicao 3 outro recurso (Coluna 2)
								 * */
								if (l_Contadores[2].equals(l_Recurso1) && l_Contadores[3].equals(l_Recurso2)) 
								{
									TipoData l_Data = new TipoData(l_Contadores[0]); 
									double l_Valor = ((Number) l_Indicador.fnProcessa(l_Contadores,linhaTotalizacao.split(";")).fnValor()).doubleValue();

									if (l_Valor > l_MaiorValor)
									{
										l_MaiorValor = l_Valor;
									}

									l_TimeSeries.add(new Minute(transformaData(l_Data.fnStrValor())),l_Valor);
								}
							}
							temRecursos = true;
							adicionaSerieHistorica(l_TimeSeriesCollection,seriesCollectionSegundoEixoY,l_TimeSeries,l_Indicador);
						} // Fim do "for" que percorre "m_RecursosAtivosCol2"
					} // Fim do "else" que verifica se (m_RecursosAtivosCol2.size() == 0)
				} // Fim do "for" que percorre "m_RecursosAtivosCol1"
			} // Fim do "if" que verifica o GATO "m_Aberto"
			else
			{  
				/** Cria uma serie (de tempo) para o indicador. O parametro de tempo eh em MINUTOS. */
				l_TimeSeries = new TimeSeries(l_Indicador.fnTitulo(), Minute.class);

				/**
				 * Laco que ira "plotar" a serie de um Contador especifico.
				 * Ex: Ira plotar todos os valores do Contador CHAM durante o periodo das 8:00hs às 12:00hs
				 * */
				for (int b = 0; b < m_ListaContadores.size(); b++)
				{
					compatibilizaRelatorios(l_Indicador);
					ContadoresHistorico l_ContadoresHistorico = (ContadoresHistorico) m_ListaContadores.elementAt(b);
					String[] l_Contadores = l_ContadoresHistorico.m_Contadores;
					TipoDado tipoDado = l_Indicador.fnProcessa(l_Contadores, linhaTotalizacao.split(";"));
					if(tipoDado.fnEhNumerico()){
						double l_Valor = ((Number) tipoDado.fnValor()).doubleValue();
						
						/**
						 * Verifica se o valor do contador eh maior do que o maior valor "achado".
						 * Essa variavel l_MaiorValor é utilizada para determinar o maior valor do eixo Y.
						 * */
						if (l_Valor > l_MaiorValor)
						{
							l_MaiorValor = l_Valor;
						}
	
						/**
						 * Cria cada ponto da serie do contador (baseado nas coordenadas Tempo x Valor)
						 * */
						
						/**
						 * cria um novo objeto Calendar e o seta com a data do periodo em milissegundos
						 * depois ele pega o mes do objeto e seta com o mes anterior, isso porque o mes 
						 * 1 do date é fevereiro e não janeiro como o Minute esta esperando,
						 * EX: mes 1 eu seto pra zero pra pega-lo como janeiro
						 * */
						try{
							l_TimeSeries.add(new Minute(transformaData(l_Contadores[0])),l_Valor);
						}catch(Exception e){
							linhaDuplicadoArquivo = true;
							System.out.println(">>>>>>OCORREU ERRO NA CRIAÇÃO DO GRAFICO DO RELATORIO HISTORICO<<<<<<");
							System.out.println(">>>>>>>>Erro pode ter sido causado por haver linhas duplicadas no arquivo<<<<<<<<");						
						}
					}
				}
			}
			
			if (!temRecursos)
			    adicionaSerieHistorica(l_TimeSeriesCollection,seriesCollectionSegundoEixoY,l_TimeSeries,l_Indicador);
			
		}
		/** Objeto referente à configuração do eixo X (tempo) */
		ValueAxis l_ValueAxis = new HorizontalDateAxis(null); 
		l_ValueAxis.setLowerMargin(0.02);
		l_ValueAxis.setUpperMargin(0.02);
		((HorizontalDateAxis) l_ValueAxis).setVerticalTickLabels(true);
		/** O valor 3600000 representa 1 hora em milessegundos */
		if (m_Passo <= 3600000)
		{   /** Se o numero de series historicas for menor do que 10, o eixo referente ao TEMPO será mensurado em MINUTOS (de 15 em 15) */

			if (m_ListaPeriodos.size() < 10)
			{   
				((HorizontalDateAxis) l_ValueAxis).setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 15));
			}
			else
			{ /** Caso contrario, será mensurado em HORAS (de hora em hora) */
				((HorizontalDateAxis) l_ValueAxis).setTickUnit(new DateTickUnit(DateTickUnit.HOUR, 1));
			}
			/** Cria uma máscara para formatar os valores do eixo referente ao TEMPO */
			((HorizontalDateAxis) l_ValueAxis).setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		}
		else
		{
			((HorizontalDateAxis) l_ValueAxis).setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
			((HorizontalDateAxis) l_ValueAxis).setDateFormatOverride(new SimpleDateFormat("dd-MMM")); //"dd-MMM-yyyy"
		}
		/** Objeto referente à configuração do eixo Y */
		NumberAxis l_NumberAxis = new VerticalNumberAxis("Valores Absolutos"); 
		
		if ((m_PosRelY > 0) && (m_QtdZoomY > 0))
		{
			double l_ValorInferior = 0;

			if (m_ValorSuperior == 0)
			{
				m_ValorSuperior = l_MaiorValor;
			}

			double l_ValorDiferenca = m_ValorSuperior - l_ValorInferior;
			double l_Centro = (m_PosRelY * l_ValorDiferenca) + l_ValorInferior;
			double l_Diferenca = (l_ValorDiferenca / ((MAX_ZOOMY + 1) * 2)) * (MAX_ZOOMY - m_QtdZoomY);

			if (l_Diferenca < 2)
			{
				l_Diferenca = 2;
			}

			l_NumberAxis.setRange(((l_Centro - l_Diferenca) < 0) ? 0 : (l_Centro - l_Diferenca), l_Centro + l_Diferenca);
			m_ValorSuperior = l_Centro + l_Diferenca;
		}
		
		/** Caso haja apenas indicadores do tipo TAXA selecionados não haverá eixo Y secundário */
		if (l_TimeSeriesCollection.getSeriesCount() == 0 && seriesCollectionSegundoEixoY != null)
		{
			l_NumberAxis.setRange(0,105);
			l_NumberAxis.setLabel("Valores em termos Percentuais");
			l_XYPlot = new XYPlot(seriesCollectionSegundoEixoY, l_ValueAxis, l_NumberAxis);
		}
		else 
		{
			l_XYPlot = new XYPlot(l_TimeSeriesCollection, l_ValueAxis, l_NumberAxis);
			
			if (seriesCollectionSegundoEixoY != null && seriesCollectionSegundoEixoY.getSeriesCount() > 0)
			{
				/** Objeto referente à configuração do segundo eixo Y */
				NumberAxis l_NumberAxis2 = new VerticalNumberAxis("Valores em termos Percentuais");
				
				/** Criando o segundo eixo Y para os indicadores do tipo TAXA */
				l_XYPlot.setSecondaryDataset(seriesCollectionSegundoEixoY);
				
				/** Setando o intervalo do eixo. Minimo 0. Maximo 100. */
				l_NumberAxis2.setRange(0,105);
				l_XYPlot.setSecondaryRangeAxis(l_NumberAxis2);
			}
		}

		JFreeChart l_JFreeChart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, l_XYPlot, true);

		StandardLegend l_StandardLegend = (StandardLegend) l_JFreeChart.getLegend();
		l_StandardLegend.setDisplaySeriesShapes(true);

		XYItemRenderer l_XYItemRenderer = l_XYPlot.getRenderer();

		if (l_XYItemRenderer instanceof StandardXYItemRenderer)
		{
			StandardXYItemRenderer l_StandardXYItemRenderer = (StandardXYItemRenderer) l_XYItemRenderer;
			l_StandardXYItemRenderer.setPlotShapes(true);
		}

		try
		{
			org.jfree.chart.ChartRenderingInfo l_Info = new org.jfree.chart.ChartRenderingInfo();
			org.jfree.chart.ChartUtilities.saveChartAsPNG(new java.io.File(p_NomeArq),
														  l_JFreeChart,
														  p_X,
														  p_Y,
														  l_Info);
			m_DataArea = (Rectangle.Double) l_Info.getDataArea();
		}
		catch (Exception e)
		{
			System.out.println("Erro ao gerar a imagem " + e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * @param data - Data a ser convertida.
	 *  Formato esperado: AAAAMMDDHHMMSS. Ex: 20050101100000 (01/01/2005 10:00hs)
	 * */
	public static long formataData(String data)
	{
		if (data == null)
			throw new IllegalArgumentException("Parametro data esta null!");
		else if (data.length() < 14)
			throw new IllegalArgumentException("Data com formato Invalido." +
											   " Formato esperado: AAAAMMDDHHMMSS. " +
											   "Ex: 20050101100000 (01/01/2005 10:00hs)");
		
		int ano = Integer.parseInt(data.substring(0,4));
		int mes = Integer.parseInt(data.substring(4,6));
		int dia = Integer.parseInt(data.substring(6,8));
		int hora = Integer.parseInt(data.substring(8,10));
		int minuto = Integer.parseInt(data.substring(10,12));
		int segundo = Integer.parseInt(data.substring(12,14));
		
		Calendar cal = new GregorianCalendar(ano, mes, dia, hora, minuto, segundo);
		
		return cal.getTimeInMillis();
	}

	/**
	 * Retorna a área referente a tabela
	 */
	public String fnAreaRelatorioTabela()
	{
		int x = 0;
		String[] l_Contadores;
		StringBuffer l_Ret = new StringBuffer(); 
		l_Ret.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> \n");
		String l_PeriodoInicial;
		String l_PeriodoFinal;

		// Imprime a linha de titulo
		l_Ret.append(" <tr>\n");

		l_Ret.append("   <td align=\"center\" bgcolor=\"#33CCFF\"><b>Per&iacute;odo Inicial</b></td>\n");
		l_Ret.append("   <td align=\"center\" bgcolor=\"#33CCFF\"><b>Per&iacute;odo Final</b></td>\n");

		for (int a = 0; a < m_RecursosSelecionados.size(); a++)
		{
			l_Ret.append("   <td align=\"center\" bgcolor=\"#33CCFF\">");
			try{
				l_Ret.append(("     <b>" + ((libjava.indicadores.IndicadorValor) m_RecursosSelecionados.elementAt(a)).fnTitulo() + "</b>"));
			}catch(Exception ce){//caso em que é só uma string devido ao fato ser recurso do campocdr
				l_Ret.append(("     <b>" + m_RecursosSelecionados.elementAt(a) + "</b>"));
			}
			l_Ret.append("  </td>\n");
		}

		for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
		{
			l_Ret.append("  <td align=\"center\" bgcolor=\"#33CCFF\">");
			l_Ret.append(("     <b>" + ((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(a)).fnTitulo() + "</b>"));
			l_Ret.append("  </td>\n");
		}

		l_Ret.append(" </tr>\n");

		// Imprime as linhas do relatório
		for (int a = 0; a < m_ListaContadores.size(); a++)
		{
			String l_Cor = " bgcolor=\"#FFFFFF\"";

			if ((a % 2) != 0)
			{
				l_Cor = " bgcolor=\"#F0F0F0\"";
			}

			l_Contadores = ((ContadoresHistorico) m_ListaContadores.elementAt(a)).m_Contadores;
			l_Ret.append(" <tr> \n");
			/**
			 * aqui ele chama a função transforma data que corrige a falha nos meses
			 * formatando com um objeto simpleDateFormat ao inves de fnconverte
			 * */
			SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
			l_PeriodoInicial = formatoData.format(transformaData(l_Contadores[0]));
			l_PeriodoFinal = formatoData.format(transformaData(l_Contadores[1]));
			
			// Período inicial
			l_Ret.append(("   <td align=\"center\" " + l_Cor + ">" + l_PeriodoInicial + "</td>\n"));

			// Período Final
			l_Ret.append(("   <td align=\"center\" " + l_Cor + ">" + l_PeriodoFinal + "</td>\n"));

			// MUDAR ISSO!!!
			String[] contadores = new String[l_Contadores.length - 2];
			System.arraycopy(l_Contadores, 2, contadores, 0, l_Contadores.length - 2);

			for (int b = 0; b < m_RecursosSelecionados.size(); b++)
			{
				l_Ret.append(("   <td align=\"center\" " + l_Cor + ">"));
				try{
					libjava.indicadores.IndicadorValor l_NoCampoIndicador = (libjava.indicadores.IndicadorValor) m_RecursosSelecionados.elementAt(b);
					l_Ret.append(l_NoCampoIndicador.fnProcessa(contadores, linhaTotalizacao.split(";")).toString());
				}catch(Exception e){//gato quando o recurso é um string e não um indicador
					l_Ret.append(contadores[b]);//pega a posição do recurso nos contadores
				}
				l_Ret.append("   </td>\n");
			}

			for (int b = 0; b < m_IndicadoresSelecionados.size(); b++)
			{
				l_Ret.append("   <td align=\"center\" " + l_Cor + ">");

				libjava.indicadores.IndicadorValor l_NoCampoIndicador = (libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(b);
				l_Ret.append(l_NoCampoIndicador.fnProcessa(l_Contadores, linhaTotalizacao.split(";")).toString());
				l_Ret.append("   </td>\n");
			}

			l_Ret.append(" </tr> \n");
						
			//    contadores = null;
		}

		l_Ret.append("</table> \n");

		return l_Ret.toString();
	}

	/** Funções que podem ser colocados em uma biblioteca */
	static public Object fnProcura(Vector p_Vetor, String p_Chave)
	{
		for (int a = 0; a < p_Vetor.size(); a++)
		{
			if (p_Vetor.elementAt(a).toString().equals(p_Chave))
			{
				return p_Vetor.elementAt(a);
			}
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_CSV
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	static public String[] fnFromCSV(String p_CSV)
	{
		if (p_CSV == null)
		{
			return null;
		}
		
		StringTokenizer l_Token = new StringTokenizer(p_CSV, ";", false);
		int l_QtdTokens = l_Token.countTokens();

		if (l_QtdTokens <= 0)
		{
			return null;
		}

		String[] l_Ret = new String[l_QtdTokens];

		for (int a = 0; a < l_QtdTokens; a++)
			l_Ret[a] = l_Token.nextToken();

		return l_Ret;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Linhas
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	static public String fnToCSV(String[] p_Linhas)
	{
		String l_Ret = "";

		for (int a = 0; a < p_Linhas.length; a++)
		{
			if (a != 0)
			{
				l_Ret += ";";
			}

			l_Ret += p_Linhas[a];
		}

		if (l_Ret.equals(""))
		{
			return null;
		}

		return l_Ret;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Linhas
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	static public String fnToCSV(Vector p_Linhas)
	{
		String l_Ret = "";

		for (int a = 0; a < p_Linhas.size(); a++)
		{
			if (a != 0)
			{
				l_Ret += ";";
			}

			l_Ret += p_Linhas.elementAt(a).toString();
		}

		if (l_Ret.equals(""))
		{
			return null;
		}

		return l_Ret;
	}

	

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_PosX
	 *            DOCUMENT ME!
	 * @param p_PosY
	 *            DOCUMENT ME!
	 * @param p_InOut
	 *            DOCUMENT ME!
	 * @param p_ZoomXY
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean fnZoom(int p_PosX, int p_PosY, char p_InOut, String p_ZoomXY)
	{
		boolean l_EhMais = true;

		if (p_ZoomXY.indexOf("Y") != -1)
		{
			if (p_InOut == '+')
			{
				if (m_QtdZoomY == MAX_ZOOMY)
				{
					return false;
				}

				m_QtdZoomY++;
			}
			else
			{
				if (m_QtdZoomY == 0)
				{
					return false;
				}

				m_QtdZoomY--;
				l_EhMais = false;
			}

			if (m_QtdZoomY != 0)
			{
				m_PosRelY = 1 - fnGetPosicaoRelativaY(p_PosY);
			}
			else
			{
				m_PosRelY = 0;
				m_ValorSuperior = 0;
			}

			if (p_ZoomXY.indexOf("X") == -1)
			{
				PosAnterior l_PosAnterior = new PosAnterior();
				l_PosAnterior.m_PeriodoInicial = m_PeriodoInicialVerdadeiro;
				l_PosAnterior.m_PeriodoFinal = m_PeriodoFinalVerdadeiro;
				l_PosAnterior.m_Recurso1 = m_UltimoFiltroRec1;
				l_PosAnterior.m_Recurso2 = m_UltimoFiltroRec2;
				l_PosAnterior.m_QtdZoomX = m_QtdZoomX;
				l_PosAnterior.m_QtdZoomY = m_QtdZoomY;
				l_PosAnterior.m_PosRelY = m_PosRelY;
				l_PosAnterior.m_ValorSuperior = m_ValorSuperior;
				m_PeriodoAnterior.addElement(l_PosAnterior);
			}
		}

		if (p_ZoomXY.indexOf("X") != -1)
		{
			// System.out.println("Início da fnZoom -
			// m_QtdZoomX="+m_QtdZoomX+",p_InOut="+p_InOut);
			Vector l_Vetor = null;

			if (p_InOut == '+')
			{
				if (m_QtdZoomX == MAX_ZOOMX)
				{
					return false;
				}

				m_QtdZoomX++;
				l_Vetor = m_ListaPeriodosApresentados;
			}
			else
			{
				if (m_QtdZoomX == 0)
				{
					return false;
				}

				m_QtdZoomX--;
				l_EhMais = false;
				l_Vetor = m_ListaPeriodos;

				//         fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, null,
				// null);
				//         return true;
			}

			// System.out.println("Meio da fnZoom - m_QtdZoomX="+m_QtdZoomX);
			if (m_QtdZoomX != 0)
			{
				// System.out.println("Meio da fnZoom - Antes de pedir posição
				// relativa");
				double l_PosRelX = fnGetPosicaoRelativaX(p_PosX);

				// System.out.println("Meio da fnZoom - Depois de pedir posição
				// relativa");
				if (l_PosRelX == -1)
				{
					return false;
				}

				//         double l_PosRelY = fnGetPosicaoRelativaY(p_PosY);
				int l_Passos = l_Vetor.size() / (MAX_ZOOMX * 2);
				int l_QtdZoom = l_Passos * (MAX_ZOOMX - m_QtdZoomX);

				if (l_QtdZoom < 2)
				{
					l_QtdZoom = 2;
				}

				int l_PosLista = (int) (l_Vetor.size() * l_PosRelX);
				int l_PosInicio = l_PosLista - l_QtdZoom;

				if ((l_PosInicio < 0) || (l_PosInicio >= l_Vetor.size()))
				{
					l_PosInicio = 0;
				}

				int l_PosFim = l_PosLista + l_QtdZoom;

				if (l_PosFim >= l_Vetor.size())
				{
					l_PosFim = l_Vetor.size() - 1;
				}

				String l_PeriodoInicio = (String) l_Vetor.elementAt(l_PosInicio);
				String l_PeriodoFim = (String) l_Vetor.elementAt(l_PosFim);

				// System.out.println("m_QtdZoomX="+m_QtdZoomX+",p_InOut="+p_InOut+",l_Passos="+l_Passos+",l_PosRelX="+l_PosRelX+",
				// p_PosX="+p_PosX+", l_QtdZoom="+l_QtdZoom+",
				// l_PosLista="+l_PosLista+",
				// l_PeriodoInicio="+l_PeriodoInicio+",
				// l_PosInicio="+l_PosInicio+", l_PeriodoFim="+l_PeriodoFim+",
				// l_PosFim="+l_PosFim+", l_Vetor.size()="+l_Vetor.size());
				fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, l_PeriodoInicio, l_PeriodoFim);
			}
			else
			{
				fnReCarrega(m_UltimoFiltroRec1, m_UltimoFiltroRec2, null, null);
			}

			// System.out.println("Fim da fnZoom - m_QtdZoomX="+m_QtdZoomX);
		}

		System.out.println("\n ZOOM - m_PeriodoAnterior.size(): " + m_PeriodoAnterior.size() + "\n");
		System.out.println("\n ZOOM - m_ListaContadores.size(): " + m_ListaContadores.size() + "\n");

		return true;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_dPosX
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public double fnGetPosicaoRelativaX(double p_dPosX)
	{
		if (m_DataArea == null)
		{
			System.out.println("m_DataArea=null");
		}

		// System.out.println("Vou chamar m_DataArea.getX()");
		double l_X = m_DataArea.getX();

		// System.out.println("Vou chamar m_DataArea.getWidth()");
		double l_Largura = m_DataArea.getWidth();

		// System.out.println("Passou");
		if ((p_dPosX < l_X) || (p_dPosX > (l_X + l_Largura)))
		{
			return -1.0;
		}

		return ((p_dPosX - l_X) / l_Largura);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_dPosY
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public double fnGetPosicaoRelativaY(double p_dPosY)
	{
		double l_Y = m_DataArea.getY();
		double l_Altura = m_DataArea.getHeight();

		if ((p_dPosY < l_Y) || (p_dPosY > (l_Y + l_Altura)))
		{
			return -1.0;
		}

		return ((p_dPosY - l_Y) / l_Altura);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Vector fnBenchMarking()
	{
		//System.out.println("fnBenchMarking()
		// m_RecursosCol1.size()="+m_RecursosCol1.size());
		SimpleDateFormat formatoData = new SimpleDateFormat(FORMATO_DATA);
		String l_PrimeiraLinha = "";
		double l_Melhor2 = 0;
		SerieRecursos l_MaiorSerie = null;
		long[] l_ContadoresMaiorSerie = null;

		double[] l_Melhor = new double[m_RecursosCol1.size()];
		long[] l_Medianas = new long[m_RecursosCol1.size()];
		long[] l_MedianasTmp = new long[m_RecursosCol1.size()];
		long[][] l_Contadores = new long[m_RecursosCol1.size()][];
		SerieRecursos[] l_Serie = new SerieRecursos[m_RecursosCol1.size()];

		for (int a = 0; a < m_RecursosCol1.size(); a++)
		{
			l_Serie[a] = (SerieRecursos) m_RecursosCol1.elementAt(a);

			if (l_Serie[a] == null)
			{
				System.out.println("fnBenchMarking() A Série é null");
			}

			//         else
			//            System.out.println("fnBenchMarking() A Série é
			// "+l_Serie[a].toString());
			l_Contadores[a] = l_Serie[a].fnCalculaMaior();
			l_Medianas[a] = l_Serie[a].m_ChamMediana;
			l_MedianasTmp[a] = l_Serie[a].m_ChamMediana;
			l_Melhor[a] = l_Serie[a].m_Melhor;
		}

		fnSort((long[]) l_MedianasTmp.clone(), l_MedianasTmp, 0, l_MedianasTmp.length);

		int l_PrimNaoZero = 0;

		for (; l_PrimNaoZero < l_MedianasTmp.length; l_PrimNaoZero++)
		{
			if (l_MedianasTmp[l_PrimNaoZero] > 0)
			{
				l_PrimNaoZero--;

				break;
			}
		}

		int l_Pos = l_PrimNaoZero + ((l_MedianasTmp.length - l_PrimNaoZero) / 2);
		long l_ChamMediana = 0;

		if (l_Pos < l_MedianasTmp.length)
		{
			l_ChamMediana = l_MedianasTmp[l_Pos];
		}

		//System.out.println("Mediana das medianas -
		// l_ChamMediana="+l_ChamMediana);
		for (int a = 0; a < m_RecursosCol1.size(); a++)
		{
			if (((l_Melhor[a] > l_Melhor2) && (l_Melhor[a] > 0) && (l_Medianas[a] > l_ChamMediana)) || (l_Melhor2 == 0))
			{
				l_Melhor2 = l_Melhor[a];
				l_MaiorSerie = l_Serie[a];
				l_ContadoresMaiorSerie = l_Contadores[a];

				l_PrimeiraLinha = "<b>" + m_Recursos[2] + ":</b>" + l_Serie[a].toString() + "<BR><b>De:</b>" + /*new TipoData(l_Serie[a].m_ContadoresBenchmark[0]).fnConverte(FORMATO_DATA)*/ formatoData.format(transformaData(l_Serie[a].m_ContadoresBenchmark[0]))+ "<BR><b>At&eacute;:</b>" + /*new TipoData(l_Serie[a].m_ContadoresBenchmark[1]).fnConverte(FORMATO_DATA)*/formatoData.format(transformaData(l_Serie[a].m_ContadoresBenchmark[1]));
			}
		}

		m_ListaBenchmarking.removeAllElements();

		if (l_MaiorSerie != null)
		{
			m_ListaBenchmarking.addElement(l_PrimeiraLinha);

			//String l_Contador[] = l_MaiorSerie.fnContadores();
			String[] l_Contador = l_MaiorSerie.getContadoresBenchmark();

			for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
			{
				libjava.indicadores.IndicadorValor l_Indicador = ((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(a));

				/* Ainda Falta CDRView NOVO */
				TipoDado tipoDado = l_Indicador.fnProcessa(l_Contador, linhaTotalizacao.split(";"));
				if(tipoDado.fnEhNumerico()){
				String l_Texto = l_Indicador.fnTitulo() + "=" + ((Number) tipoDado.fnValor()).toString();
				m_ListaBenchmarking.addElement(l_Texto);
				}
				

				/* */
			}
		}

		return m_ListaBenchmarking;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Indicador
	 *            DOCUMENT ME!
	 * @param p_Valor
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String fnConverte(libjava.indicadores.IndicadorValor p_Indicador, double p_Valor)
	{
		/* Ainda Falta CDRView NOVO */
		libjava.tipos.TipoDadoNumber l_Num = new libjava.tipos.TipoDadoNumber();

		if (p_Indicador instanceof libjava.indicadores.IndicadorValorTaxa)
		{
			l_Num.fnIncrementa(new Double(p_Valor));
		}
		else if (p_Indicador instanceof libjava.indicadores.IndicadorValorNumber)
		{
			l_Num.fnIncrementa(new Integer((int) p_Valor));
		}

		/* */
		return l_Num.toString();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Vector fnValoresMedios()
	{
		//System.out.println("fnValoresMedios()");
		String l_PrimeiraLinha = "<b>De:</b>" + m_PeriodoInicial + "<BR><b>At&eacute;:</b>" + m_PeriodoFinal;

		if ((m_PeriodoInicial != null) && (m_PeriodoFinal != null))
		{
			m_ListaValoresMedios.removeAllElements();
			m_ListaValoresMedios.addElement(l_PrimeiraLinha);
		}

		double[] l_ValoresMedios = new double[m_IndicadoresSelecionados.size()];
		double[] l_ValoresDesvio = new double[m_IndicadoresSelecionados.size()];

		for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
		{
			l_ValoresMedios[a] = 0;
			l_ValoresDesvio[a] = 0;
		}

		for (int a = 0; a < m_ListaContadores.size(); a++)
		{
			String[] l_Contador = ((ContadoresHistorico) m_ListaContadores.elementAt(a)).m_Contadores;
			int qtdItemFormula = 0;
			/// (Contador) m_ListaContadores.elementAt(a);
			for (int b = 0; b < m_IndicadoresSelecionados.size(); b++)
			{
				libjava.indicadores.IndicadorValor l_Indicador = ((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(b));
				compatibilizaRelatorios(l_Indicador);

				/* Ainda Falta CDRView NOVO */
				TipoDado tipoDado = l_Indicador.fnProcessa(l_Contador, linhaTotalizacao.split(";"));
				if(tipoDado.fnEhNumerico()){
					double l_Valor = ((Number) tipoDado.fnValor()).doubleValue();
					l_ValoresMedios[b] += l_Valor;
				}

				/* */
			}
		}

		for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
			l_ValoresMedios[a] /= m_ListaContadores.size();

		for (int a = 0; a < m_ListaContadores.size(); a++)
		{
			String[] l_Contador = ((ContadoresHistorico) m_ListaContadores.elementAt(a)).m_Contadores;

			for (int b = 0; b < m_IndicadoresSelecionados.size(); b++)
			{
				libjava.indicadores.IndicadorValor l_Indicador = ((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(b));

				/* Ainda Falta CDRView NOVO */
				TipoDado tipoDado = l_Indicador.fnProcessa(l_Contador, linhaTotalizacao.split(";"));
				if(tipoDado.fnEhNumerico()){
					double l_Valor = ((Number) tipoDado.fnValor() ).doubleValue() - l_ValoresMedios[b];
					l_ValoresDesvio[b] += (l_Valor * l_Valor);
				}

				/* */
			}
		}

		for (int a = 0; a < m_IndicadoresSelecionados.size(); a++)
		{
			l_ValoresDesvio[a] = Math.sqrt(l_ValoresDesvio[a] / (m_ListaContadores.size() - 1));

			libjava.indicadores.IndicadorValor l_Indicador = ((libjava.indicadores.IndicadorValor) m_IndicadoresSelecionados.elementAt(a));
			String l_Texto = l_Indicador.fnTitulo() + "=" + fnConverte(l_Indicador, l_ValoresMedios[a]) + "&#177;" + fnConverte(l_Indicador, l_ValoresDesvio[a]);
			m_ListaValoresMedios.addElement(l_Texto);
		}

		return m_ListaValoresMedios;
	}

	/**
	 * @return Returns the m_ListaContadores.
	 */
	public Vector getListaContadores()
	{
		return m_ListaContadores;
	}

	/**
	 * @param listaContadores
	 *            The m_ListaContadores to set.
	 */
	public void setListaContadores(Vector listaContadores)
	{
		m_ListaContadores = listaContadores;
	}

	/**
	 * @return Returns the m_SerieInicial.
	 */
	public PosAnterior getSerieInicial()
	{
		return m_SerieInicial;
	}

	/**
	 * @param serieInicial
	 *            The m_SerieInicial to set.
	 */
	public void setSerieInicial(PosAnterior serieInicial)
	{
		m_SerieInicial = serieInicial;
	}
	/**
	 * função que pega uma data e seta para o mes anterior
	 * */
	public java.util.Date transformaData(String dt){
		int ano = Integer.parseInt(dt.substring(0,4));
		int mes = Integer.parseInt(dt.substring(4,6));
		int dia = Integer.parseInt(dt.substring(6,8));
		int hora = Integer.parseInt(dt.substring(8,10));
		int minuto = Integer.parseInt(dt.substring(10,12));
		int segundo = Integer.parseInt(dt.substring(12,14));
		Calendar calendario = new GregorianCalendar(ano, mes-1, dia, hora, minuto, segundo);
		Date data = new Date(calendario.getTimeInMillis());
		return data;
	}

	class PosAnterior
	{
		public String m_Recurso1;
		public String m_Recurso2;
		public String m_PeriodoInicial;
		public String m_PeriodoFinal;
		public int m_QtdZoomX;
		public int m_QtdZoomY;
		public double m_PosRelY;
		public double m_ValorSuperior;
	}

	class ContadoresHistorico
	{
		public String[] m_Contadores;
		public String m_Rec1 = null;
		public String m_Rec2 = null;

		public ContadoresHistorico(String[] p_Contadores, String p_Rec1, String p_Rec2)
		{
			m_Contadores = p_Contadores;
			m_Rec1 = p_Rec1;
			m_Rec2 = p_Rec2;
		}
	}

	class SerieRecursos
	{
		private String m_Titulo;
		private Vector m_ListaContadoresSerie = new Vector();
		private String[] m_Contadores = null;
		private String[] m_ContadoresBenchmark = null;
		public double m_Melhor = 0;
		public long m_ChamMediana = 0;

		public SerieRecursos(String p_Titulo)
		{
			m_Titulo = p_Titulo;
		}

		public String[] getContadoresBenchmark()
		{
			return m_ContadoresBenchmark;
		}

		public String[] fnContadores()
		{
			return m_Contadores;
		}

		public void fnTrataContadores(String[] p_Contadores)
		{
			if (m_Contadores == null)
			{
				m_Contadores = p_Contadores;
			}

			if (m_QtdContadores == 0)
			{
				m_QtdContadores = m_Contadores.length;
			}

			m_ListaContadoresSerie.add(p_Contadores);
		}

		public String toString()
		{
			return m_Titulo;
		}

		/*
		 * Este método retornar um array de long contendo a linha do relatorio
		 * que possui a maior quantidade de chamadas com o maior percentual de
		 * OK.
		 *
		 */
		public long[] fnCalculaMaior()
		{
			long[] l_Contadores = null;
			double l_Maior = -1;
			String l_Dt = null;
			String l_DtFim = null;
			long l_ChamMedia = 0;
			long[] l_Cham = new long[m_ListaContadoresSerie.size()];

			for (int a = 0; a < m_ListaContadoresSerie.size(); a++)
			{
				String[] l_Contador = (String[]) m_ListaContadoresSerie.elementAt(a);

				//            String l_ContadoresTmp [] = l_Contador.fnLinhaContadores();
				String l_NovoValor = l_Contador[2 + m_QtdRecursos]; // ou 4 se
																	// tiver 2
																	// recursos
																	// - Ainda
																	// Falta
																	// CDRView
																	// NOVO

				//            l_NovoValor.replace('.', ',');
				l_Cham[a] = (long) Double.parseDouble(l_NovoValor);
				l_ChamMedia += l_Cham[a];
			}

			l_ChamMedia /= m_ListaContadoresSerie.size();
			fnSort((long[]) l_Cham.clone(), l_Cham, 0, l_Cham.length);

			int l_PrimNaoZero = 0;

			for (; l_PrimNaoZero < l_Cham.length; l_PrimNaoZero++)
			{
				if (l_Cham[l_PrimNaoZero] > 0)
				{
					l_PrimNaoZero--;

					break;
				}
			}

			int l_Pos = l_PrimNaoZero + ((l_Cham.length - l_PrimNaoZero) / 2);
			m_ChamMediana = l_ChamMedia;

			if (l_Pos < l_Cham.length)
			{
				m_ChamMediana = l_Cham[l_Pos];
			}

			for (int a = 0; a < m_ListaContadoresSerie.size(); a++)
			{
				String[] l_Contador = (String[]) m_ListaContadoresSerie.elementAt(a);

				double l_Este;

				/*
				 * Comeca na posicao 2+Qtd_Recursos pq a posicao 0 e 1 sao
				 * respectivamente Data Inicial e Data Final de cada linha do
				 * relatorio. Esta sendo assumido aqui que o contador CHAM é o
				 * primeiro contador após os recursos. (Ver o arquivo
				 * indicadores.xml) Ta tosco demais!!!
				 *
				 */
				String l_NovoValor = l_Contador[2 + m_QtdRecursos]; // ou 4 se
																	// tiver 2
																	// recursos
																	// - Ainda
																	// Falta
																	// CDRView
																	// NOVO

				long l_Chamadas = (long) Double.parseDouble(l_NovoValor);

				//Verifica o percentual de chamadas OK dessa linha do
				// relatorio.
				if (m_IndicadorOK != null)
				{
					l_Este = ((Number) m_IndicadorOK.fnProcessa(l_Contador, linhaTotalizacao.split(";")).fnValor()).doubleValue();
				}
				else
				{
					l_Este = l_Chamadas;
				}

				if ((l_Este > l_Maior) && (l_Chamadas >= m_ChamMediana) /*
				 * &&
				 * l_Chamadas >=
				 * 100
				 */)
				{
					l_Maior = l_Este;
					l_Dt = l_Contador[0];
					l_DtFim = l_Contador[1];
					l_Contadores = new long[m_Contadores.length];

					/*
					 * Transfere APENAS os valores dos contadores de l_Contador
					 * (Contem cada a linha do relatorio) para o array
					 * l_Contadores
					 */
					for (int i = (2 + m_QtdRecursos), j = 0;
						 i < l_Contador.length && j < l_Contadores.length; i++, j++)
					{
						try
						{
							l_Contadores[j] = Long.parseLong(l_Contador[i]);
						}
						catch (NumberFormatException nfe)
						{
							//							System.out.println("Valor " + l_Contador[i] + " não pode ser convertido");
							l_Contadores[j] = 0;
						}
					}
				}
			}

			m_Melhor = l_Maior;

			//         String l_StrContadores [] = m_Contadores.fnLinhaContadores();
			int l_Dif = m_Contadores.length - m_QtdContadores;
			m_ContadoresBenchmark = new String[m_Contadores.length+2];

			for (int a = 0; a < m_Contadores.length; a++)
			{
				m_ContadoresBenchmark[a+2] = "" + l_Contadores[a];
			}

			m_ContadoresBenchmark[0] = l_Dt;
			m_ContadoresBenchmark[1] = l_DtFim;

			return l_Contadores;
		}
	}
	/**
	 * metodo que retorna 'true' quando o arquivo que é usado para plotar os pontos do grafico está
	 * com linhas duplicadas(contem linhas iquais).
	 * @return 'true' se deu erro na hora de plotar os pontos do grafico.'False' se de tudo certo.
	 * CRIADO: Metodo criado para que quando esse erro ocorre-se não desse tela branca na visualização do 
	 * grafico, mas uma mensagem informe o ocorrido.
	 */
	public boolean getErroLinhaDuplicadaArquivo(){
		return linhaDuplicadoArquivo;
	}

	/** Método para compatibilizar os relatórios gerados antes de alterações de arquivos de indicadores.
	 * @param l_Indicador
	 */
	public void compatibilizaRelatorios(IndicadorValor l_Indicador){
		boolean achouFormula;
		boolean achouFormulaDen;

		String []contadorDen = {};		
		String []formula = {}; 		

		if(l_Indicador.getFormula().contains("/")){
			contadorDen = l_Indicador.getFormula().substring(l_Indicador.getFormula().indexOf("/")+1, l_Indicador.getFormula().length()).replace("*", ";").replace("-", ";").replace("+", ";").replace("(", "").replace(")", "").split(";");
			formula = l_Indicador.getFormula().substring(0, l_Indicador.getFormula().indexOf("/")).replace("/", ";").replace("*", ";").replace("-", ";").replace("+", ";").replace("(", "").replace(")", "").split(";");
		}else{
			formula = l_Indicador.getFormula().replace("/", ";").replace("*", ";").replace("-", ";").replace("+", ";").replace("(", "").replace(")", "").split(";");
		}

		StringBuffer contServ = new StringBuffer();
		StringBuffer contDenServ = new StringBuffer();

		String contTmp = "";

		for(int j = 0; j < contadorDen.length; j++){//mainupações com contador denominador
			achouFormulaDen = false;
			if(!contadorDen[j].matches("^[0-9]*$")){
				for(int x = 0; x < contadoresArquivo.length; x++){
					if(contadorDen[j].equalsIgnoreCase(contadoresArquivo[x])){
						achouFormulaDen = true;
						break;
					}
				}
				
				
				if(!achouFormulaDen){

					if(contadorDen[j].toLowerCase().endsWith("cfg")){
						String aux =contadorDen[j].substring(contadorDen[j].length()-3, contadorDen[j].length());
						contadorDen[j]= contadorDen[j].replace(aux, "");

						for(int k = 0; k < contadorDen.length; k++){
							for(int x = 0; x < contadoresArquivo.length; x++){
								if(contadorDen[k].equalsIgnoreCase(contadoresArquivo[x])){
									achouFormulaDen = true;
									//											l_Indicador.setM_PosDen(new int[]{i+2});
									contDenServ.append(x+2);
									contDenServ.append(";");
									break;
								}
							}


						}

						

					}
				}

			}
		}

		for (int j = 0; j < formula.length; j++) {//mainupações com contador numerador
			achouFormula = false;

			if(formula[j] != "/" && formula[j] != "*" && formula[j] != "-" && formula[j] != "+" && !formula[j].matches("^[0-9]*$")){
				for(int i = 0; i < contadoresArquivo.length;i++){
					if(formula[j].equalsIgnoreCase(contadoresArquivo[i])){//identificando se item da formula existe na lista de contadores do arquivo
						achouFormula = true;
						//						l_Indicador.setM_Pos(new int[]{i+2});
						contTmp = "";
						contServ.append(i+2);
						contServ.append(";");

						contTmp = (i+2)+";";
						break;
					}
				}

			}
	


				if(!achouFormula){//se não achar formula, verifico se existe cfg no final do contador e reprocuro na lista de inficadores
					if(formula[j].toLowerCase().endsWith("cfg")){
						String tmp = formula[j];//para auxiliar na verificação do denominador
						String aux =formula[j].substring(formula[j].length()-3, formula[j].length());
						formula[j]= formula[j].replace(aux, "");


						achouFormula = false;
						for(int i = 0; i < contadoresArquivo.length;i++){
							if(formula[j].equalsIgnoreCase(contadoresArquivo[i])){
								//								l_Indicador.setM_Pos(new int[]{i+2});
								contServ.append(i+2);
								contServ.append(";");
								achouFormula = true;
								break;
							}
						}

						


						if(!achouFormula){
							System.out.println("Formula "+formula[j]+" não encontrado na lista de contadores no arquivo.");
						}
					}
				}

			



		}
		
		if(contDenServ.length() > 0){

			String []auxCont;

			if(contDenServ.toString().endsWith(";"))
				contDenServ.deleteCharAt(contDenServ.lastIndexOf(";"));

			auxCont = contDenServ.toString().split(";");


			int []posicaoCont = new int[auxCont.length];
			for (int i = 0; i < posicaoCont.length; i++) {
				posicaoCont[i] = Integer.parseInt(auxCont[i]);
			}
			l_Indicador.setM_PosDen(posicaoCont);
		}
		
		if(contServ.length() > 0){

			String []auxCont;

			if(contServ.toString().endsWith(";"))
				contServ.deleteCharAt(contServ.lastIndexOf(";"));

			auxCont = contServ.toString().split(";");

			int []posicaoCont = new int[auxCont.length];
			for (int i = 0; i < posicaoCont.length; i++) {
				posicaoCont[i] = Integer.parseInt(auxCont[i]);
			}
			l_Indicador.setM_Pos(posicaoCont);
		}
	}
}
