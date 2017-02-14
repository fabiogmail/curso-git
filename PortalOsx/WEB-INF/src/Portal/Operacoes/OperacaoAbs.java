//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OperacaoAbs.java

package Portal.Operacoes;

import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import CDRView2.SelecaoIndicadorAgrupado;
import CDRView2.SelecaoIndicadorAnaliseCompletamento;
import CDRView2.SelecaoIndicadorAnatelIDDF;
import CDRView2.SelecaoIndicadorAnatelLDN;
import CDRView2.SelecaoIndicadorAnatelSMP3;
import CDRView2.SelecaoIndicadorAnatelSMP3Ericsson;
import CDRView2.SelecaoIndicadorAnatelSMP3e4;
import CDRView2.SelecaoIndicadorAnatelSMP5;
import CDRView2.SelecaoIndicadorAnatelSMP5Ericsson;
import CDRView2.SelecaoIndicadorAnatelSMP6;
import CDRView2.SelecaoIndicadorAnatelSMP6Ericsson;
import CDRView2.SelecaoIndicadorAnatelSMP7;
import CDRView2.SelecaoIndicadorAnatelSMP7Ericsson;
import CDRView2.SelecaoIndicadorAnatelSMP8e9;
import CDRView2.SelecaoIndicadorAnatelSTFC;
import CDRView2.SelecaoIndicadorAuditoriaChamadas;
import CDRView2.SelecaoIndicadorChamadas;
import CDRView2.SelecaoIndicadorChamadasLongaDuracao;
import CDRView2.SelecaoIndicadorDesempenho;
import CDRView2.SelecaoIndicadorDesempenhoDeRede;
import CDRView2.SelecaoIndicadorDespesa;
import CDRView2.SelecaoIndicadorDestinosComuns;
import CDRView2.SelecaoIndicadorDestinosEspecificos;
import CDRView2.SelecaoIndicadorDetalheChamada;
import CDRView2.SelecaoIndicadorDistFrequencia;
import CDRView2.SelecaoIndicadorDwGeral;
import CDRView2.SelecaoIndicadorDwGprs;
import CDRView2.SelecaoIndicadorGRE;
import CDRView2.SelecaoIndicadorITXReceita;
import CDRView2.SelecaoIndicadorIndicadoresSMP;
import CDRView2.SelecaoIndicadorMatraf;
import CDRView2.SelecaoIndicadorMinutosDeUso;
import CDRView2.SelecaoIndicadorPerseveranca;
import CDRView2.SelecaoIndicadorPesquisaCodigo;
import CDRView2.SelecaoIndicadorPesquisaIMEI;
import CDRView2.SelecaoIndicadorPesquisaPorERB;
import CDRView2.SelecaoIndicadorPrefixosDeRisco;
import CDRView2.SelecaoIndicadorQoS;
import CDRView2.SelecaoIndicadorTrendAnalysis;
import CDRView2.SelecaoRecursosAgrupado;
import CDRView2.SelecaoRecursosAnaliseCompletamento;
import CDRView2.SelecaoRecursosAnatelIDDF;
import CDRView2.SelecaoRecursosAnatelLDN;
import CDRView2.SelecaoRecursosAnatelSMP3;
import CDRView2.SelecaoRecursosAnatelSMP3Ericsson;
import CDRView2.SelecaoRecursosAnatelSMP3e4;
import CDRView2.SelecaoRecursosAnatelSMP5;
import CDRView2.SelecaoRecursosAnatelSMP5Ericsson;
import CDRView2.SelecaoRecursosAnatelSMP6;
import CDRView2.SelecaoRecursosAnatelSMP6Ericsson;
import CDRView2.SelecaoRecursosAnatelSMP7;
import CDRView2.SelecaoRecursosAnatelSMP7Ericsson;
import CDRView2.SelecaoRecursosAnatelSMP8e9;
import CDRView2.SelecaoRecursosAnatelSTFC;
import CDRView2.SelecaoRecursosChamadas;
import CDRView2.SelecaoRecursosDesempenho;
import CDRView2.SelecaoRecursosDesempenhoDeRede;
import CDRView2.SelecaoRecursosDespesa;
import CDRView2.SelecaoRecursosDestinosComuns;
import CDRView2.SelecaoRecursosDestinosEspecificos;
import CDRView2.SelecaoRecursosDistFrequencia;
import CDRView2.SelecaoRecursosDwGeral;
import CDRView2.SelecaoRecursosDwGprs;
import CDRView2.SelecaoRecursosGRE;
import CDRView2.SelecaoRecursosITXReceita;
import CDRView2.SelecaoRecursosIndicadoresSMP;
import CDRView2.SelecaoRecursosMatraf;
import CDRView2.SelecaoRecursosMinutosDeUso;
import CDRView2.SelecaoRecursosPerseveranca;
import CDRView2.SelecaoRecursosPesquisaCodigo;
import CDRView2.SelecaoRecursosPesquisaPorERB;
import CDRView2.SelecaoRecursosQoS;
import CDRView2.SelecaoRecursosTrendAnalysis;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Arquivo;
import Portal.Utils.HTMLUtil;
import Portal.Utils.UsuarioDef;

/**
 * Classe a ser especializada para todas as operações existentes no portal.
 */

public class OperacaoAbs 
{ 
   protected HttpServletRequest m_Request;
   protected HttpServletResponse m_Response;

	/**
	 * Stream de saída de páginas para o usuário
	 * 
	 * @uml.property name="m_Html"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected HTMLUtil m_Html;

   
   /**
    * Argumentos que vão substituir marcas nas páginas
    */
   protected String[] m_Args;
   protected String m_IPRemoto;
   protected String m_HostRemoto;
   
   /**
    * Mensagem para ser apresentada na página HTML
    */
   protected String m_Mensagem = "$ARG;";

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosDesempenho m_SelecaoRecursosDesempenho;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosDespesa m_SelecaoRecursosDespesa;
	
	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosTrendAnalysis m_SelecaoRecursosTrendAnalysis;
	
	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosDwGprs m_SelecaoRecursosDwGprs;
	
	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosDwGeral m_SelecaoRecursosDwGeral;
	
	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosITXReceita m_SelecaoRecursosITXReceita;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
//	protected SelecaoIndicadorDesempenho m_SelecaoIndicadorDesempenho;
//
//	protected String m_IndicadoresDesempenho = null;
   
   /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorTrendAnalysis m_SelecaoIndicadorTrendAnalysis;

  protected String m_IndicadoresTrendAnalysis = null;
  
  /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorDwGprs m_SelecaoIndicadorDwGprs;

  protected String m_IndicadoresDwGprs = null;
  
  /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorDwGeral m_SelecaoIndicadorDwGeral;

  protected String m_IndicadoresDwGeral = null;

   /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorDespesa m_SelecaoIndicadorDespesa;

  protected String m_IndicadoresDespesa = null;
  
  /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorITXReceita m_SelecaoIndicadorITXReceita;

	protected String m_IndicadoresITXReceita = null; 
   /**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAgrupado m_SelecaoRecursosAgrupado;
   /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAgrupado m_SelecaoIndicadorAgrupado;

    protected String m_IndicadoresAgrupado = null;
   
   /**
	 * 
	 * @uml.property name="m_SelecaoRecursosDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosGRE m_SelecaoRecursosGRE;
 /**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorGRE m_SelecaoIndicadorGRE;

	protected String m_IndicadoresGRE = null;

	protected SelecaoRecursosIndicadoresSMP m_SelecaoRecursosIndicadoresSMP;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorDesempenho"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorDesempenho m_SelecaoIndicadorDesempenho;

	protected SelecaoIndicadorIndicadoresSMP m_SelecaoIndicadorIndicadoresSMP;

   protected String m_IndicadoresDesempenho = null;
   
   protected String m_IndicadoresIndicadoresSMP = null;
   
   /**
	 * 
	 * @uml.property name="m_SelecaoRecursosChamadas"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosChamadas m_SelecaoRecursosChamadas;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorChamadas"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorChamadas m_SelecaoIndicadorChamadas;

	protected String m_IndicadoresChamadas = null;
  
  /**
	 * 
	 * @uml.property name="m_SelecaoRecursosChamadas"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosMinutosDeUso m_SelecaoRecursosMinutosDeUso;
	
	/**
	 * @uml.property name="m_SelecaoRecursosDistFrequencia"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosDistFrequencia m_SelecaoRecursosDistFrequencia;

	/**
	 * @uml.property name="m_SelecaoRecursosPerseveranca"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosPerseveranca m_SelecaoRecursosPerseveranca;
	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorChamadas"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorMinutosDeUso m_SelecaoIndicadorMinutosDeUso;

	protected String m_IndicadoresMinutosDeUso = null;

	protected SelecaoIndicadorDistFrequencia m_SelecaoIndicadorDistFrequencia;

	protected String m_IndicadoresDistFrequencia = null;

	protected SelecaoIndicadorPerseveranca m_SelecaoIndicadorPerseveranca;

	protected String m_IndicadoresPerseveranca = null;

	
	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosMatraf"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosMatraf m_SelecaoRecursosMatraf;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorMatraf"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorMatraf m_SelecaoIndicadorMatraf;

   protected String m_IndicadoresMatraf = null;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelSMP3"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelSMP3 m_SelecaoRecursosAnatelSMP3;
	
	protected SelecaoRecursosAnatelSTFC m_SelecaoRecursosAnatelSTFC;
	
	protected SelecaoRecursosAnatelIDDF m_SelecaoRecursosAnatelIDDF;
	
	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelSMP3"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelSMP3 m_SelecaoIndicadorAnatelSMP3;

   protected String m_IndicadoresAnatelSMP3 = null;
   

   protected SelecaoIndicadorAnatelSTFC m_SelecaoIndicadorAnatelSTFC;
   
   protected String m_IndicadoresAnatelSTFC = null;
   
   
   protected SelecaoIndicadorAnatelIDDF m_SelecaoIndicadorAnatelIDDF;
   
   protected String m_IndicadoresAnatelIDDF = null;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelSMP3"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelSMP3e4 m_SelecaoRecursosAnatelSMP3e4;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelSMP3e4"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelSMP3e4 m_SelecaoIndicadorAnatelSMP3e4;

  protected String m_IndicadoresAnatelSMP3e4 = null;


	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelSMP3e4"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelSMP8e9 m_SelecaoRecursosAnatelSMP8e9;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelSMP8e9"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelSMP8e9 m_SelecaoIndicadorAnatelSMP8e9;

 protected String m_IndicadoresAnatelSMP8e9 = null;

	

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelSMP5"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelSMP5 m_SelecaoRecursosAnatelSMP5;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelSMP5"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelSMP5 m_SelecaoIndicadorAnatelSMP5;

   protected String m_IndicadoresAnatelSMP5 = null;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelSMP6"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelSMP6 m_SelecaoRecursosAnatelSMP6;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelSMP6"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelSMP6 m_SelecaoIndicadorAnatelSMP6;

   protected String m_IndicadoresAnatelSMP6 = null;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelSMP7"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelSMP7 m_SelecaoRecursosAnatelSMP7;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelSMP7"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelSMP7 m_SelecaoIndicadorAnatelSMP7;

   protected String m_IndicadoresAnatelSMP7 = null;
   
   
   //---------------relatorios novos SMP que usam id dos relatorios PVM's--04/11/2009
   protected SelecaoRecursosAnatelSMP3Ericsson m_SelecaoRecursosAnatelSMP3Ericsson;
   protected SelecaoIndicadorAnatelSMP3Ericsson m_SelecaoIndicadorAnatelSMP3Ericsson;
   protected String m_IndicadoresAnatelSMP3Ericsson = null;
   
   protected SelecaoRecursosAnatelSMP5Ericsson m_SelecaoRecursosAnatelSMP5Ericsson;
   protected SelecaoIndicadorAnatelSMP5Ericsson m_SelecaoIndicadorAnatelSMP5Ericsson;
   protected String m_IndicadoresAnatelSMP5Ericsson = null;
   
   protected SelecaoRecursosAnatelSMP6Ericsson m_SelecaoRecursosAnatelSMP6Ericsson;
   protected SelecaoIndicadorAnatelSMP6Ericsson m_SelecaoIndicadorAnatelSMP6Ericsson;
   protected String m_IndicadoresAnatelSMP6Ericsson = null;
   
   protected SelecaoRecursosAnatelSMP7Ericsson m_SelecaoRecursosAnatelSMP7Ericsson;
   protected SelecaoIndicadorAnatelSMP7Ericsson m_SelecaoIndicadorAnatelSMP7Ericsson;
   protected String m_IndicadoresAnatelSMP7Ericsson = null;
   

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnatelLDN"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnatelLDN m_SelecaoRecursosAnatelLDN;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnatelLDN"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnatelLDN m_SelecaoIndicadorAnatelLDN;

   protected String m_IndicadoresAnatelLDN = null;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosAnaliseCompletamento"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosAnaliseCompletamento m_SelecaoRecursosAnaliseCompletamento;

	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorAnaliseCompletamento"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorAnaliseCompletamento m_SelecaoIndicadorAnaliseCompletamento;

   protected String m_IndicadoresAnaliseCompletamento = null;

/**
 * 
 * @uml.property name="m_SelecaoIndicadorDetalhe"
 * @uml.associationEnd multiplicity="(0 1)"
 */
//   protected SelecaoRecursosInterconexaoAudit m_SelecaoRecursosInterconexaoAudit;
//   protected SelecaoIndicadorInterconexaoAudit m_SelecaoIndicadorInterconexaoAudit;
//   protected String m_IndicadoresInterconexaoAudit = null;
//   protected SelecaoRecursosInterconexaoForaRota m_SelecaoRecursosInterconexaoForaRota;
//   protected SelecaoIndicadorInterconexaoForaRota m_SelecaoIndicadorInterconexaoForaRota;
//   protected String m_IndicadoresInterconexaoForaRota = null;
   protected SelecaoIndicadorDetalheChamada m_SelecaoIndicadorDetalhe = null;

   protected String m_IndicadoresDetalhe = null;
   
   protected SelecaoIndicadorPesquisaIMEI m_SelecaoIndicadorPesquisaIMEI = null;

   protected String m_IndicadoresPesquisaIMEI = null;
   
   protected SelecaoIndicadorChamadasLongaDuracao m_SelecaoIndicadorChamadasLongaDuracao = null;

   protected String m_IndicadoresChamadasLongaDuracao = null;
   
   protected SelecaoIndicadorAuditoriaChamadas m_SelecaoIndicadorAuditoria = null;

   protected String m_IndicadoresAuditoria = null;

	/**
	 * 
	 * @uml.property name="m_SelecaoRecursosPesquisa"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoRecursosPesquisaCodigo m_SelecaoRecursosPesquisa;

	protected SelecaoRecursosDestinosEspecificos m_SelecaoRecursosDestinosEspecificos;
	
	protected SelecaoRecursosDestinosComuns m_SelecaoRecursosDestinosComuns;
	
	protected SelecaoRecursosPesquisaPorERB m_SelecaoRecursosPesquisaPorERB;
	
	
	/**
	 * 
	 * @uml.property name="m_SelecaoIndicadorPesquisa"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected SelecaoIndicadorPesquisaCodigo m_SelecaoIndicadorPesquisa = null;
	
	
	protected SelecaoIndicadorDestinosEspecificos m_SelecaoIndicadorDestinosEspecificos = null;
	
	protected SelecaoIndicadorDestinosComuns m_SelecaoIndicadorDestinosComuns = null;
	
	protected SelecaoIndicadorPesquisaPorERB m_SelecaoIndicadorPesquisaPorERB = null;
	
	protected SelecaoIndicadorPrefixosDeRisco m_SelecaoIndicadorPrefixosDeRisco = null;
	
	//relatorios novos modelo acme
	protected SelecaoRecursosDesempenhoDeRede m_SelecaoRecursosDesempenhoDeRede;
	protected SelecaoIndicadorDesempenhoDeRede m_SelecaoIndicadorDesempenhoDeRede;
    protected String m_IndicadoresDesempenhoDeRede = null;
    
    protected SelecaoRecursosQoS m_SelecaoRecursosQoS;
	protected SelecaoIndicadorQoS m_SelecaoIndicadorQoS;
    protected String m_IndicadoresQoS = null;

   protected String m_IndicadoresPesquisa = null;
   protected String m_IndicadoresDestinosEspecificos = null;
   protected String m_IndicadoresDestinosComuns = null;
   protected String m_IndicadoresPesquisaPorERB = null;
   protected String m_IndicadoresPrefixosDeRisco = null;
   protected String m_Resultado = null;
   public static Map s_MapHistoricos;
   public static Map s_MapHistoricosTrafego;
   public static Map s_MapAgendasDesempenho;
   public static Map s_MapAgendasTrendAnalysis;
   public static Map s_MapAgendasDWGPRS;
   public static Map s_MapAgendasDWGERAL;
   public static Map s_MapAgendasGRE;
   public static Map s_MapAgendasAgrupado;
   public static Map s_MapAgendasDespesa;
   public static Map s_MapAgendasITXReceita;
   public static Map s_MapAgendasMinutosDeUso;
   public static Map s_MapAgendasChamadas;
   public static Map s_MapAgendasDetalheChamadas;
   public static Map s_MapAgendasChamadasLongaDuracao;
   public static Map s_MapAgendasAuditoriaChamadas;
   public static Map s_MapAgendasPesquisa;
   public static Map s_MapAgendasDestinosEspecificos;
   public static Map s_MapAgendasDestinosComuns;
   public static Map s_MapAgendasPesquisaPorERB;
   public static Map s_MapAgendasPesquisaIMEI;
   public static Map s_MapAgendasPrefixosDeRisco;
   public static Map s_MapAgendasMatraf;
   public static Map s_MapAgendasAnatelSMP;
   public static Map s_MapAgendasAnaliseCompl;
   public static Map s_MapAgendasInterconexaoAudit;
   public static Map s_MapAgendasInterconexaoForaRota;
   public static Map s_MapAgendasDistFrequencia;
   public static Map s_MapAgendasPerseveranca;
   public static Map s_MapAgendasDesempenhoDeRede;
   public static Map s_MapAgendasQoS;
   public static Map s_MapAgendasIndicadoresSMP;


	/**
	 * 
	 * @uml.property name="m_Arquivo"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected Arquivo m_Arquivo = null;

   
   static 
   {
   }
   
   public OperacaoAbs() 
   {
   }
   
   /**
    * @param p_Request
    * @param p_Response
    * @return 
    * @exception 
    * @roseuid 3BF081340256
    */
   public OperacaoAbs(HttpServletRequest p_Request, HttpServletResponse p_Response) 
   {
      m_Request  = p_Request;
      m_Response = p_Response;
   }
   
   /**
    * @param p_Request
    * @param p_Response
    * @return void
    * @exception 
    * @roseuid 3C06CD7301A5
    */
   public void setRequestResponse(HttpServletRequest p_Request, HttpServletResponse p_Response) 
   {         
      m_Request  = p_Request;
      m_Response = p_Response;

      // Inicializa a saída HTML
      setHtml();
      setRequisitor();
   }
   
   /**
    * @return boolean
    * @exception 
    * Cria o stream Hmtl de resposta.
    * @roseuid 3C0A3873023B
    */
   public boolean setHtml() 
   {
      try
      {
         m_Html = new HTMLUtil(m_Response.getWriter());
         return true;         
      }
      catch (Exception ExcHTML)
      {
         ExcHTML.printStackTrace();         
		 return false;
      }
   }
   
   /**
    * @param p_QtdArgs
    * @return void
    * @exception 
    * Inicia o array de argumentos (m_Args).
    * @roseuid 3C2A7E9302AC
    */
   public void iniciaArgs(int p_QtdArgs) 
   {
      m_Args = new String [p_QtdArgs];
      for (int i = 0; i < p_QtdArgs; i++)
         m_Args[i] = "";
   }
   
   /**
    * @return HttpServletRequest
    * @exception 
    * @roseuid 3C3364F0014A
    */
   public HttpServletRequest getRequest() 
   {
      return m_Request;
   }
   
   /**
    * @return HttpServletResponse
    * @exception 
    * @roseuid 3C3365050191
    */
   public HttpServletResponse getResponse() 
   {
      return m_Response;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C38F2E9003D
    */
   public void setRequisitor() 
   {
      m_IPRemoto = m_Request.getRemoteAddr();
      m_HostRemoto = m_Request.getRemoteHost();
      //System.out.println(m_IPRemoto+" - "+ m_HostRemoto);
   }
   
   /**
    * @param p_Mensagem
    * @return void
    * @exception 
    * Seta o atributo mensagem.
    * @roseuid 3C33665C0049
    */
   public void setMensagem(String p_Mensagem) 
   {
      m_Mensagem = p_Mensagem;
   }
   
   /**
    * @param p_Operacao
    * @return void
    * @exception 
    * @roseuid 3C3BA5BF016B
    */
   public void setOperacao(String p_Operacao) 
   {
      UsuarioDef Usuario;
      try
	  {
//      	Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
          Usuario = NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(m_Request.getSession().getId());
      	Usuario.setOperacao(p_Operacao);
      }catch(Exception e )
	  {
         System.out.println("OperacaoAbs - setOperacao(): Usuario null");
	  }
      
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresDesempenho(SelecaoRecursosDesempenho p_SelecaoRecursosDesempenho, SelecaoIndicadorDesempenho p_SelecaoIndicadorDesempenho, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosDesempenho = p_SelecaoRecursosDesempenho;
      m_SelecaoIndicadorDesempenho = p_SelecaoIndicadorDesempenho;
      m_IndicadoresDesempenho = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresDespesa(SelecaoRecursosDespesa p_SelecaoRecursosDespesa, SelecaoIndicadorDespesa p_SelecaoIndicadorDespesa, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosDespesa = p_SelecaoRecursosDespesa;
      m_SelecaoIndicadorDespesa = p_SelecaoIndicadorDespesa;
      m_IndicadoresDespesa = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresITXReceita(SelecaoRecursosITXReceita p_SelecaoRecursosITXReceita, SelecaoIndicadorITXReceita p_SelecaoIndicadorITXReceita, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosITXReceita = p_SelecaoRecursosITXReceita;
      m_SelecaoIndicadorITXReceita = p_SelecaoIndicadorITXReceita;
      m_IndicadoresITXReceita = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresTrendAnalysis(SelecaoRecursosTrendAnalysis p_SelecaoRecursosTrendAnalysis, SelecaoIndicadorTrendAnalysis p_SelecaoIndicadorTrendAnalysis, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosTrendAnalysis = p_SelecaoRecursosTrendAnalysis;
      m_SelecaoIndicadorTrendAnalysis = p_SelecaoIndicadorTrendAnalysis;
      m_IndicadoresTrendAnalysis = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresGRE(SelecaoRecursosGRE p_SelecaoRecursosGRE, SelecaoIndicadorGRE p_SelecaoIndicadorGRE, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosGRE = p_SelecaoRecursosGRE;
      m_SelecaoIndicadorGRE = p_SelecaoIndicadorGRE;
      m_IndicadoresGRE = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresDwGprs(SelecaoRecursosDwGprs p_SelecaoRecursosDwGprs, SelecaoIndicadorDwGprs p_SelecaoIndicadorDwGprs, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosDwGprs = p_SelecaoRecursosDwGprs;
      m_SelecaoIndicadorDwGprs = p_SelecaoIndicadorDwGprs;
      m_IndicadoresDwGprs = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresDwGeral(SelecaoRecursosDwGeral p_SelecaoRecursosDwGeral, SelecaoIndicadorDwGeral p_SelecaoIndicadorDwGeral, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosDwGeral = p_SelecaoRecursosDwGeral;
      m_SelecaoIndicadorDwGeral = p_SelecaoIndicadorDwGeral;
      m_IndicadoresDwGeral = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresAgrupado(SelecaoRecursosAgrupado p_SelecaoRecursosAgrupado, SelecaoIndicadorAgrupado p_SelecaoIndicadorAgrupado, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAgrupado = p_SelecaoRecursosAgrupado;
      m_SelecaoIndicadorAgrupado = p_SelecaoIndicadorAgrupado;
      m_IndicadoresAgrupado = p_IndicadoresPossiveis;
   }

   public void setIndicadoresIndicadoresSMP(SelecaoRecursosIndicadoresSMP p_SelecaoRecursosIndicadoresSMP, SelecaoIndicadorIndicadoresSMP p_SelecaoIndicadorIndicadoresSMP, String p_IndicadoresPossiveis) 
   {
	  m_SelecaoRecursosIndicadoresSMP = p_SelecaoRecursosIndicadoresSMP;
      m_SelecaoIndicadorIndicadoresSMP = p_SelecaoIndicadorIndicadoresSMP;
      m_IndicadoresIndicadoresSMP = p_IndicadoresPossiveis;
   }
   
   /**
    * @return SelecaoRecursosDesempenho
    * @exception 
    * @roseuid 3EDCEB170267
    */
   public SelecaoRecursosDesempenho getRecursosDesempenho() 
   {
      return m_SelecaoRecursosDesempenho;
   }
   
   
   public SelecaoRecursosIndicadoresSMP getRecursosIndicadoresSMP() 
   {
      return m_SelecaoRecursosIndicadoresSMP;
   }
   /**
    * @return SelecaoIndicadorDesempenho
    * @exception 
    * @roseuid 3EDCE9AE0146
    */
   public SelecaoIndicadorDesempenho getIndicadoresDesempenho() 
   {
      return m_SelecaoIndicadorDesempenho;
   }
   
   public SelecaoIndicadorIndicadoresSMP getIndicadoresIndicadoresSMP() 
   {
      return m_SelecaoIndicadorIndicadoresSMP;
   }
   
   /**
    * @param p_SelecaoRecursosChamadas
    * @param p_SelecaoIndicadorChamadas
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresChamadas(SelecaoRecursosChamadas p_SelecaoRecursosChamadas, SelecaoIndicadorChamadas p_SelecaoIndicadorChamadas, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosChamadas = p_SelecaoRecursosChamadas;
      m_SelecaoIndicadorChamadas = p_SelecaoIndicadorChamadas;
      m_IndicadoresChamadas = p_IndicadoresPossiveis;
   }
   
   /**
    * @return SelecaoRecursosChamadas
    * @exception 
    * @roseuid 3EDCEB170267
    */
   public SelecaoRecursosChamadas getRecursosChamadas() 
   {
      return m_SelecaoRecursosChamadas;
   }
   
   /**
    * @return SelecaoIndicadorChamadas
    * @exception 
    * @roseuid 3EDCE9AE0146
    */
   public SelecaoIndicadorChamadas getIndicadoresChamadas() 
   {
      return m_SelecaoIndicadorChamadas;
   }
   
   /**
    * @param p_SelecaoRecursosMinutosDeUso
    * @param p_SelecaoIndicadorMinutosDeUso
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresMinutosDeUso(SelecaoRecursosMinutosDeUso p_SelecaoRecursosMinutosDeUso, SelecaoIndicadorMinutosDeUso p_SelecaoIndicadorMinutosDeUso, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosMinutosDeUso = p_SelecaoRecursosMinutosDeUso;
      m_SelecaoIndicadorMinutosDeUso = p_SelecaoIndicadorMinutosDeUso;
      m_IndicadoresMinutosDeUso = p_IndicadoresPossiveis;
   }
   /**
    * 
    * @param p_SelecaoRecursosDistFrequencia
    * @param p_SelecaoIndicadorDistFrequencia
    * @param p_IndicadoresPossiveis
    */
   public void setIndicadoresDistFrequencia(SelecaoRecursosDistFrequencia p_SelecaoRecursosDistFrequencia, SelecaoIndicadorDistFrequencia p_SelecaoIndicadorDistFrequencia, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosDistFrequencia = p_SelecaoRecursosDistFrequencia;
      m_SelecaoIndicadorDistFrequencia = p_SelecaoIndicadorDistFrequencia;
      m_IndicadoresDistFrequencia = p_IndicadoresPossiveis;
   }
   /**
    * 
    * @param p_SelecaoRecursosPerseveranca
    * @param p_SelecaoIndicadorPerseveranca
    * @param p_IndicadoresPossiveis
    */
   public void setIndicadoresPerseveranca(SelecaoRecursosPerseveranca p_SelecaoRecursosPerseveranca, SelecaoIndicadorPerseveranca p_SelecaoIndicadorPerseveranca, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosPerseveranca = p_SelecaoRecursosPerseveranca;
      m_SelecaoIndicadorPerseveranca = p_SelecaoIndicadorPerseveranca;
      m_IndicadoresPerseveranca = p_IndicadoresPossiveis;
   }
   
   /**
    * @return SelecaoRecursosMinutosDeUso
    * @exception 
    * @roseuid 3EDCEB170267
    */
   public SelecaoRecursosMinutosDeUso getRecursosMinutosDeUso() 
   {
      return m_SelecaoRecursosMinutosDeUso;
   }
   /**
    * 
    * @return
    */
   public SelecaoRecursosDistFrequencia getRecursosDistFrequencia() 
   {
      return m_SelecaoRecursosDistFrequencia;
   }
   /**
    * 
    * @return
    */
   public SelecaoRecursosPerseveranca getRecursosPerseveranca() 
   {
      return m_SelecaoRecursosPerseveranca;
   }
   /**
    * @return SelecaoIndicadorMinutosDeUso
    * @exception 
    * @roseuid 3EDCE9AE0146
    */
   public SelecaoIndicadorMinutosDeUso getIndicadoresMinutosDeUso() 
   {
      return m_SelecaoIndicadorMinutosDeUso;
   }
   
   public SelecaoIndicadorDistFrequencia getIndicadoresDistFrequencia() 
   {
      return m_SelecaoIndicadorDistFrequencia;
   }

   public SelecaoIndicadorPerseveranca getIndicadoresPerseveranca() 
   {
      return m_SelecaoIndicadorPerseveranca;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3EDCEB720362
    */
   public String getIndicadoresDesempenhoPossiveis() 
   {
      return m_IndicadoresDesempenho;
   }
   
   /**
    * @param p_SelecaoIndicadorDetalhe
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C83896802E7
    */
   public void setIndicadoresDetalhe(SelecaoIndicadorDetalheChamada p_SelecaoIndicadorDetalhe, String p_IndicadoresPossiveis) 
   {
      m_SelecaoIndicadorDetalhe = p_SelecaoIndicadorDetalhe;
      m_IndicadoresDetalhe = p_IndicadoresPossiveis;
   }
   /**
    * @param p_SelecaoIndicadorDetalhe
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C83896802E7
    */
   public void setIndicadoresPesquisaIMEI(SelecaoIndicadorPesquisaIMEI p_SelecaoIndicadorPesquisaIMEI, String p_IndicadoresPossiveis) 
   {
      m_SelecaoIndicadorPesquisaIMEI = p_SelecaoIndicadorPesquisaIMEI;
      m_IndicadoresPesquisaIMEI = p_IndicadoresPossiveis;
   }
   
   /**
    * @return SelecaoIndicadorDetalheChamada
    * @exception 
    * @roseuid 3EDCE9AE0178
    */
   public SelecaoIndicadorPesquisaIMEI getIndicadoresPesquisaIMEI()
   {
      return m_SelecaoIndicadorPesquisaIMEI;
   }
   
   /**
    * @param p_SelecaoIndicadorChamadasLongaDuracao
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C83896802E7
    */
   public void setIndicadoresChamadasLongaDuracao(SelecaoIndicadorChamadasLongaDuracao p_SelecaoIndicadorChamadasLongaDuracao, String p_IndicadoresPossiveis) 
   {
      m_SelecaoIndicadorChamadasLongaDuracao = p_SelecaoIndicadorChamadasLongaDuracao;
      m_IndicadoresChamadasLongaDuracao = p_IndicadoresPossiveis;
   }
   
   /**
    * @return SelecaoIndicadorDetalheChamada
    * @exception 
    * @roseuid 3EDCE9AE0178
    */
   public SelecaoIndicadorDetalheChamada getIndicadoresDetalhe()
   {
      return m_SelecaoIndicadorDetalhe;
   }
   
   /**
    * @return SelecaoIndicadorChamadasLongaDuracao
    * @exception 
    * @roseuid 3EDCE9AE0178
    */
   public SelecaoIndicadorChamadasLongaDuracao getIndicadoresChamadasLongaDuracao()
   {
      return m_SelecaoIndicadorChamadasLongaDuracao;
   }
   
   /**
    * @param p_SelecaoIndicadorAuditoria
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C83896802E7
    */
   public void setIndicadoresAuditoria(SelecaoIndicadorAuditoriaChamadas p_SelecaoIndicadorAuditoria, String p_IndicadoresPossiveis) 
   {
      m_SelecaoIndicadorAuditoria = p_SelecaoIndicadorAuditoria;
      m_IndicadoresAuditoria = p_IndicadoresPossiveis;
   }
   
   /**
    * @return SelecaoIndicadorAuditoriaChamada
    * @exception 
    * @roseuid 3EDCE9AE0178
    */
   public SelecaoIndicadorAuditoriaChamadas getIndicadoresAuditoria()
   {
      return m_SelecaoIndicadorAuditoria;
   }
   
   /**
    * @param p_SelecaoRecursosPesquisa
    * @param p_SelecaoIndicadorPesquisa
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C83CB1402F5
    */
   public void setIndicadoresPesquisa(SelecaoRecursosPesquisaCodigo p_SelecaoRecursosPesquisa, SelecaoIndicadorPesquisaCodigo p_SelecaoIndicadorPesquisa, String p_IndicadoresPossiveis)
   {
      m_SelecaoIndicadorPesquisa = p_SelecaoIndicadorPesquisa;
      m_SelecaoRecursosPesquisa = p_SelecaoRecursosPesquisa;
      m_IndicadoresPesquisa = p_IndicadoresPossiveis;
   }
   
 
   public void setIndicadoresDestinosEspecificos(SelecaoRecursosDestinosEspecificos p_SelecaoRecursosDestinosEspecificos, SelecaoIndicadorDestinosEspecificos p_SelecaoIndicadorDestinosEspecificos, String p_IndicadoresPossiveis)
   {
      m_SelecaoIndicadorDestinosEspecificos = p_SelecaoIndicadorDestinosEspecificos;
      m_SelecaoRecursosDestinosEspecificos = p_SelecaoRecursosDestinosEspecificos;
      m_IndicadoresDestinosEspecificos = p_IndicadoresPossiveis;
   }
   
   public void setIndicadoresDestinosComuns(SelecaoRecursosDestinosComuns p_SelecaoRecursosDestinosComuns, SelecaoIndicadorDestinosComuns p_SelecaoIndicadorDestinosComuns, String p_IndicadoresPossiveis)
   {
      m_SelecaoIndicadorDestinosComuns = p_SelecaoIndicadorDestinosComuns;
      m_SelecaoRecursosDestinosComuns = p_SelecaoRecursosDestinosComuns;
      m_IndicadoresDestinosComuns = p_IndicadoresPossiveis;
   }
   
   public void setIndicadoresPesquisaPorERB(SelecaoRecursosPesquisaPorERB p_SelecaoRecursosPesquisaPorERB, SelecaoIndicadorPesquisaPorERB p_SelecaoIndicadorPesquisaPorERB, String p_IndicadoresPossiveis)
   {
      m_SelecaoIndicadorPesquisaPorERB = p_SelecaoIndicadorPesquisaPorERB;
      m_SelecaoRecursosPesquisaPorERB = p_SelecaoRecursosPesquisaPorERB;
      m_IndicadoresPesquisaPorERB = p_IndicadoresPossiveis;
   }
   
   public void setIndicadoresPrefixosDeRisco(SelecaoIndicadorPrefixosDeRisco p_SelecaoIndicadorPrefixosDeRisco, String p_IndicadoresPossiveis)
   {
      m_SelecaoIndicadorPrefixosDeRisco = p_SelecaoIndicadorPrefixosDeRisco;
      m_IndicadoresPrefixosDeRisco = p_IndicadoresPossiveis;
   }
   
   
   /**
    * @return SelecaoIndicadorPesquisaCodigo
    * @exception 
    * @roseuid 3EDCE9AE01B4
    */
   public SelecaoIndicadorPesquisaCodigo getIndicadoresPesquisa()
   {
      return m_SelecaoIndicadorPesquisa;
   }
   
   public SelecaoIndicadorDestinosEspecificos getIndicadoresDestinosEspecificos()
   {
      return m_SelecaoIndicadorDestinosEspecificos;
   }
   
   public SelecaoIndicadorDestinosComuns getIndicadoresDestinosComuns()
   {
      return m_SelecaoIndicadorDestinosComuns;
   }
   
   public SelecaoIndicadorPesquisaPorERB getIndicadoresPesquisaPorERB()
   {
      return m_SelecaoIndicadorPesquisaPorERB;
   }
   
   public SelecaoIndicadorPrefixosDeRisco getIndicadoresPrefixosDeRisco()
   {
      return m_SelecaoIndicadorPrefixosDeRisco;
   }
   
   /**
    * @param p_SelecaoRecursosMatraf
    * @param p_SelecaoIndicadorMatraf
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753AF0384
    */
   public void setIndicadoresMatraf(SelecaoRecursosMatraf p_SelecaoRecursosMatraf, SelecaoIndicadorMatraf p_SelecaoIndicadorMatraf, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosMatraf = p_SelecaoRecursosMatraf;
      m_SelecaoIndicadorMatraf = p_SelecaoIndicadorMatraf;
      m_IndicadoresMatraf = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP3
    * @param p_SelecaoIndicadorAnatelSMP3
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753CE02E9
    */
   public void setIndicadoresAnatelSMP3(SelecaoRecursosAnatelSMP3 p_SelecaoRecursosAnatelSMP3, SelecaoIndicadorAnatelSMP3 p_SelecaoIndicadorAnatelSMP3, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP3 = p_SelecaoRecursosAnatelSMP3;
      m_SelecaoIndicadorAnatelSMP3 = p_SelecaoIndicadorAnatelSMP3;
      m_IndicadoresAnatelSMP3 = p_IndicadoresPossiveis;
   }
   
   
   public void setIndicadoresAnatelIDDF(SelecaoRecursosAnatelIDDF p_SelecaoRecursosAnatelIDDF, SelecaoIndicadorAnatelIDDF p_SelecaoIndicadorAnatelIDDF, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelIDDF = p_SelecaoRecursosAnatelIDDF;
      m_SelecaoIndicadorAnatelIDDF = p_SelecaoIndicadorAnatelIDDF;
      m_IndicadoresAnatelIDDF = p_IndicadoresPossiveis;
   }
   
   public void setIndicadoresAnatelSTFC(SelecaoRecursosAnatelSTFC p_SelecaoRecursosAnatelSTFC, SelecaoIndicadorAnatelSTFC p_SelecaoIndicadorAnatelSTFC, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSTFC = p_SelecaoRecursosAnatelSTFC;
      m_SelecaoIndicadorAnatelSTFC = p_SelecaoIndicadorAnatelSTFC;
      m_IndicadoresAnatelSTFC = p_IndicadoresPossiveis;
   }

   /**
    * @param p_SelecaoRecursosAnatelSMP3e4
    * @param p_SelecaoIndicadorAnatelSMP3e4
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753CE02E9
    */
   public void setIndicadoresAnatelSMP3e4(SelecaoRecursosAnatelSMP3e4 p_SelecaoRecursosAnatelSMP3e4, SelecaoIndicadorAnatelSMP3e4 p_SelecaoIndicadorAnatelSMP3e4, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP3e4 = p_SelecaoRecursosAnatelSMP3e4;
      m_SelecaoIndicadorAnatelSMP3e4 = p_SelecaoIndicadorAnatelSMP3e4;
      m_IndicadoresAnatelSMP3e4 = p_IndicadoresPossiveis;
   }
   /**
    * @param p_SelecaoRecursosAnatelSMP5
    * @param p_SelecaoIndicadorAnatelSMP5
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F50230
    */
   public void setIndicadoresAnatelSMP5(SelecaoRecursosAnatelSMP5 p_SelecaoRecursosAnatelSMP5, SelecaoIndicadorAnatelSMP5 p_SelecaoIndicadorAnatelSMP5, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP5 = p_SelecaoRecursosAnatelSMP5;
      m_SelecaoIndicadorAnatelSMP5 = p_SelecaoIndicadorAnatelSMP5;
      m_IndicadoresAnatelSMP5 = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP6
    * @param p_SelecaoIndicadorAnatelSMP6
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F7011B
    */
   public void setIndicadoresAnatelSMP6(SelecaoRecursosAnatelSMP6 p_SelecaoRecursosAnatelSMP6, SelecaoIndicadorAnatelSMP6 p_SelecaoIndicadorAnatelSMP6, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP6 = p_SelecaoRecursosAnatelSMP6;
      m_SelecaoIndicadorAnatelSMP6 = p_SelecaoIndicadorAnatelSMP6;
      m_IndicadoresAnatelSMP6 = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP7
    * @param p_SelecaoIndicadorAnatelSMP7
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F8037F
    */
   public void setIndicadoresAnatelSMP7(SelecaoRecursosAnatelSMP7 p_SelecaoRecursosAnatelSMP7, SelecaoIndicadorAnatelSMP7 p_SelecaoIndicadorAnatelSMP7, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP7 = p_SelecaoRecursosAnatelSMP7;
      m_SelecaoIndicadorAnatelSMP7 = p_SelecaoIndicadorAnatelSMP7;
      m_IndicadoresAnatelSMP7 = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP3
    * @param p_SelecaoIndicadorAnatelSMP3
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F8037F
    */
   public void setIndicadoresAnatelSMP3Ericsson(SelecaoRecursosAnatelSMP3Ericsson p_SelecaoRecursosAnatelSMP3Ericsson, SelecaoIndicadorAnatelSMP3Ericsson p_SelecaoIndicadorAnatelSMP3Ericsson, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP3Ericsson = p_SelecaoRecursosAnatelSMP3Ericsson;
      m_SelecaoIndicadorAnatelSMP3Ericsson = p_SelecaoIndicadorAnatelSMP3Ericsson;
      m_IndicadoresAnatelSMP3Ericsson = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP5
    * @param p_SelecaoIndicadorAnatelSMP5
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F8037F
    */
   public void setIndicadoresAnatelSMP5Ericsson(SelecaoRecursosAnatelSMP5Ericsson p_SelecaoRecursosAnatelSMP5Ericsson, SelecaoIndicadorAnatelSMP5Ericsson p_SelecaoIndicadorAnatelSMP5Ericsson, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP5Ericsson = p_SelecaoRecursosAnatelSMP5Ericsson;
      m_SelecaoIndicadorAnatelSMP5Ericsson = p_SelecaoIndicadorAnatelSMP5Ericsson;
      m_IndicadoresAnatelSMP5Ericsson = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP6
    * @param p_SelecaoIndicadorAnatelSMP6
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F8037F
    */
   public void setIndicadoresAnatelSMP6Ericsson(SelecaoRecursosAnatelSMP6Ericsson p_SelecaoRecursosAnatelSMP6Ericsson, SelecaoIndicadorAnatelSMP6Ericsson p_SelecaoIndicadorAnatelSMP6Ericsson, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP6Ericsson = p_SelecaoRecursosAnatelSMP6Ericsson;
      m_SelecaoIndicadorAnatelSMP6Ericsson = p_SelecaoIndicadorAnatelSMP6Ericsson;
      m_IndicadoresAnatelSMP6Ericsson = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnatelSMP7
    * @param p_SelecaoIndicadorAnatelSMP7
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753F8037F
    */
   public void setIndicadoresAnatelSMP7Ericsson(SelecaoRecursosAnatelSMP7Ericsson p_SelecaoRecursosAnatelSMP7Ericsson, SelecaoIndicadorAnatelSMP7Ericsson p_SelecaoIndicadorAnatelSMP7Ericsson, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP7Ericsson = p_SelecaoRecursosAnatelSMP7Ericsson;
      m_SelecaoIndicadorAnatelSMP7Ericsson = p_SelecaoIndicadorAnatelSMP7Ericsson;
      m_IndicadoresAnatelSMP7Ericsson = p_IndicadoresPossiveis;
   }
   

   /**
    * @param p_SelecaoRecursosAnatelSMP8e9
    * @param p_SelecaoIndicadorAnatelSMP8e9
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3FE753CE02E9
    */
   public void setIndicadoresAnatelSMP8e9(SelecaoRecursosAnatelSMP8e9 p_SelecaoRecursosAnatelSMP8e9, SelecaoIndicadorAnatelSMP8e9 p_SelecaoIndicadorAnatelSMP8e9, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelSMP8e9 = p_SelecaoRecursosAnatelSMP8e9;
      m_SelecaoIndicadorAnatelSMP8e9 = p_SelecaoIndicadorAnatelSMP8e9;
      m_IndicadoresAnatelSMP8e9 = p_IndicadoresPossiveis;
   }
   /**
    * @param p_SelecaoRecursosAnatelLDN
    * @param p_SelecaoIndicadorAnatelLDN
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 40AE4AA00144
    */
   public void setIndicadoresAnatelLDN(SelecaoRecursosAnatelLDN p_SelecaoRecursosAnatelLDN, SelecaoIndicadorAnatelLDN p_SelecaoIndicadorAnatelLDN, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnatelLDN = p_SelecaoRecursosAnatelLDN;
      m_SelecaoIndicadorAnatelLDN = p_SelecaoIndicadorAnatelLDN;
      m_IndicadoresAnatelLDN = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosAnaliseCompletamento
    * @param p_SelecaoIndicadorAnaliseCompletamento
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 4002FFC70310
    */
   public void setIndicadoresAnaliseCompletamento(SelecaoRecursosAnaliseCompletamento p_SelecaoRecursosAnaliseCompletamento, SelecaoIndicadorAnaliseCompletamento p_SelecaoIndicadorAnaliseCompletamento, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosAnaliseCompletamento = p_SelecaoRecursosAnaliseCompletamento;
      m_SelecaoIndicadorAnaliseCompletamento = p_SelecaoIndicadorAnaliseCompletamento;
      m_IndicadoresAnaliseCompletamento = p_IndicadoresPossiveis;
   }
   
   /**
    * @param p_SelecaoRecursosInterconexaoAudit
    * @param p_SelecaoIndicadorInterconexaoAudit
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 40AE4AA40032
    */
/*
   public void setIndicadoresInterconexaoAudit(SelecaoRecursosInterconexaoAudit p_SelecaoRecursosInterconexaoAudit, SelecaoIndicadorInterconexaoAudit p_SelecaoIndicadorInterconexaoAudit, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosInterconexaoAudit = p_SelecaoRecursosInterconexaoAudit;
      m_SelecaoIndicadorInterconexaoAudit = p_SelecaoIndicadorInterconexaoAudit;
      m_IndicadoresInterconexaoAudit = p_IndicadoresPossiveis;
   }
*/
   /**
    * @param p_SelecaoRecursosInterconexaoForaRota
    * @param p_SelecaoIndicadorInterconexaoForaRota
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 40AE4AA5020A
    */
/*
   public void setIndicadoresInterconexaoForaRota(SelecaoRecursosInterconexaoForaRota p_SelecaoRecursosInterconexaoForaRota, SelecaoIndicadorInterconexaoForaRota p_SelecaoIndicadorInterconexaoForaRota, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosInterconexaoForaRota = p_SelecaoRecursosInterconexaoForaRota;
      m_SelecaoIndicadorInterconexaoForaRota = p_SelecaoIndicadorInterconexaoForaRota;
      m_IndicadoresInterconexaoForaRota = p_IndicadoresPossiveis;
   }
*/
   
   /**
    * @param p_SelecaoRecursosDesempenho
    * @param p_SelecaoIndicadorDesempenho
    * @param p_IndicadoresPossiveis
    * @return void
    * @exception 
    * @roseuid 3C8108F202AA
    */
   public void setIndicadoresDesempenhoDeRede(SelecaoRecursosDesempenhoDeRede p_SelecaoRecursosDesempenhoDeRede, SelecaoIndicadorDesempenhoDeRede p_SelecaoIndicadorDesempenhoDeRede, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosDesempenhoDeRede = p_SelecaoRecursosDesempenhoDeRede;
      m_SelecaoIndicadorDesempenhoDeRede = p_SelecaoIndicadorDesempenhoDeRede;
      m_IndicadoresDesempenhoDeRede = p_IndicadoresPossiveis;
   }

   public SelecaoRecursosDesempenhoDeRede getRecursosDesempenhoDeRede() 
   {
      return m_SelecaoRecursosDesempenhoDeRede;
   }
   
   public SelecaoIndicadorDesempenhoDeRede getIndicadoresDesempenhoDeRede() 
   {
      return m_SelecaoIndicadorDesempenhoDeRede;
   }
   

   public void setIndicadoresQoS(SelecaoRecursosQoS p_SelecaoRecursosQoS, SelecaoIndicadorQoS p_SelecaoIndicadorQoS, String p_IndicadoresPossiveis) 
   {
      m_SelecaoRecursosQoS = p_SelecaoRecursosQoS;
      m_SelecaoIndicadorQoS = p_SelecaoIndicadorQoS;
      m_IndicadoresQoS = p_IndicadoresPossiveis;
   }
   
   public SelecaoRecursosQoS getRecursosQoS() 
   {
      return m_SelecaoRecursosQoS;
   }
   
   public SelecaoIndicadorQoS getIndicadoresQoS() 
   {
      return m_SelecaoIndicadorQoS;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3E414E2A000D
    */
   public Vector montaLinhasResultado() 
   {
      String Linha;
      Vector Linhas = null, Colunas = null;

      Linhas = new Vector();
      if (m_Resultado != null)
      {
          while (m_Resultado != null)
          {
        	 System.out.println("---"+m_Resultado);
             Linha = m_Resultado.substring(0, m_Resultado.indexOf('\n'));
            
             Colunas = new Vector();
             m_Resultado = m_Resultado.substring(m_Resultado.indexOf('\n') + 1, m_Resultado.length());
             if (m_Resultado.length() == 0)
                m_Resultado = null;
             Colunas.add(Linha);
             Colunas.trimToSize();
             Linhas.add(Colunas);
          }
      }
      else
      {
          Colunas = new Vector();
          Colunas.add("Sem informa&ccedil;&otilde;es");
          Colunas.trimToSize();
          Linhas.add(Colunas);
      }

      Linhas.trimToSize();
      return (Linhas);
   }
   
   /**
    * @param p_Operacao
    * @return void
    * @exception 
    * @roseuid 3E414E2A003F
    */
   public void montaTabelaResultado(String p_Operacao) 
   {
      String Header[] = {"Informa&ccedil;&otilde;es"};
      String Alinhamento[] = {"left"};
      String Largura[] = {"546"};
      short Filtros[] = {0};
      Vector Linhas = null;

      Linhas = montaLinhasResultado();
      m_Html.setTabela((short)Header.length, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink(p_Operacao);
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos historicos
    * @roseuid 3EDE002201DC
    */
   public void setMapHistoricos(Map p_Map) 
   {
      s_MapHistoricos = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos historicos de tráfego
    * @roseuid 3F2165C701B2
    */
   public void setMapHistoricosTrafego(Map p_Map) 
   {
      s_MapHistoricosTrafego = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Desempenho
    * @roseuid 3FD4904D014B
    */
   public void setMapAgendasDesempenho(Map p_Map) 
   {
      s_MapAgendasDesempenho = p_Map;
   }
   
   public void setMapAgendasTrendAnalysis(Map p_Map) 
   {
      s_MapAgendasTrendAnalysis = p_Map;
   }
   
   public void setMapAgendasGRE(Map p_Map) 
   {
      s_MapAgendasGRE = p_Map;
   }
   
   public void setMapAgendasDWGPRS(Map p_Map) 
   {
	   s_MapAgendasDWGPRS = p_Map;
   }
   
   public void setMapAgendasDWGERAL(Map p_Map) 
   {
	   s_MapAgendasDWGERAL = p_Map;
   }
   
   public void setMapAgendasAgrupado(Map p_Map) 
   {
      s_MapAgendasAgrupado = p_Map;
   }
   
   public void setMapAgendasDespesa(Map p_Map) 
   {
	   s_MapAgendasDespesa = p_Map;
   }
   
   public void setMapAgendasITXReceita(Map p_Map) 
   {
	   s_MapAgendasITXReceita = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Minutos de Uso
    * @roseuid 3FD4904D014B setMapAgendasMinutosDeUso
    */
   public void setMapAgendasMinutosDeUso(Map p_Map) 
   {
      s_MapAgendasMinutosDeUso = p_Map;
   }
   
   public void setMapAgendasDistFrequencia(Map p_Map) 
   {
      s_MapAgendasDistFrequencia = p_Map;
   }
   
   public void setMapAgendasPerseveranca(Map p_Map) 
   {
      s_MapAgendasPerseveranca = p_Map;
   }
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Minutos de Uso
    * @roseuid 3FD4904D014B
    */
   public void setMapAgendasChamadas(Map p_Map) 
   {
      s_MapAgendasChamadas = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Detalhe
    * @roseuid 3FD4905000C3
    */
   public void setMapAgendasDetalheChamadas(Map p_Map) 
   {
      s_MapAgendasDetalheChamadas = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos PesquisaIMEI
    * @roseuid 3FD4905000C3
    */
   public void setMapAgendasPesquisaIMEI(Map p_Map) 
   {
      s_MapAgendasPesquisaIMEI = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos ChamadasLongaDuracao
    * @roseuid 3FD4905000C3
    */
   public void setMapAgendasChamadasLongaDuracao(Map p_Map) 
   {
      s_MapAgendasChamadasLongaDuracao = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Auditoria
    * @roseuid 3FD4905000C3
    */
   public void setMapAgendasAuditoriaChamadas(Map p_Map) 
   {
      s_MapAgendasAuditoriaChamadas = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Pesquisa
    * @roseuid 3FD490520347
    */
   public void setMapAgendasPesquisa(Map p_Map) 
   {
      s_MapAgendasPesquisa = p_Map;
   }
   
   /**
    * setando as agendas para os relatorios novos da claro perfil : fraude
    */
   public void setMapAgendasDestinosEspecificos(Map p_Map) 
   {
      s_MapAgendasDestinosEspecificos = p_Map;
   }
   
   public void setMapAgendasDestinosComuns(Map p_Map) 
   {
      s_MapAgendasDestinosComuns = p_Map;
   }
   
   public void setMapAgendasPesquisaPorERB(Map p_Map) 
   {
      s_MapAgendasPesquisaPorERB = p_Map;
   }
   
   public void setMapAgendasPrefixosDeRisco(Map p_Map) 
   {
      s_MapAgendasPrefixosDeRisco = p_Map;
   }
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Matraf
    * @roseuid 3FD490950376
    */
   public void setMapAgendasMatraf(Map p_Map) 
   {
      s_MapAgendasMatraf = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos SMP3
    * @roseuid 3FE732B30260
    */
   public void setMapAgendasAnatelSMP(Map p_Map) 
   {
      s_MapAgendasAnatelSMP = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos LDN
    * @roseuid 40AE48ED01F3
    */
   public void setMapAgendasAnatelLDN(Map p_Map) 
   {
      // Retirar do Rose
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos relatorios de Analise de Completamento
    * @roseuid 3FFEE9A90320
    */
   public void setMapAgendasAnaliseCompletamento(Map p_Map) 
   {
      s_MapAgendasAnaliseCompl = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos Interconexao Audit
    * @roseuid 40AE48EF0015
    */
   public void setMapAgendasInterconexaoAudit(Map p_Map) 
   {
      s_MapAgendasInterconexaoAudit = p_Map;
   }
   
   /**
    * @param p_Map
    * @return void
    * @exception 
    * Seta o map dos objetos InterconexaoForaRota
    * @roseuid 40AE48F00143
    */
   public void setMapAgendasInterconexaoForaRota(Map p_Map) 
   {
      s_MapAgendasInterconexaoForaRota = p_Map;
   }
   
   public void setMapAgendasDesempenhoDeRede(Map p_Map) 
   {
      s_MapAgendasDesempenhoDeRede = p_Map;
   }
   
   public void setMapAgendasQoS(Map p_Map) 
   {
      s_MapAgendasQoS = p_Map;
   }
   
   public void setMapAgendasIndicadoresSMP(Map p_Map)
   {
	   s_MapAgendasIndicadoresSMP = p_Map;
   }
}
