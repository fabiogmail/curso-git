package Portal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.Timer;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import CDRView2.Cliente;
import CDRView2.PortavelCDRView;
import CDRView2.SelecaoIndicadorAgrupado;
import CDRView2.SelecaoIndicadorAnaliseCompletamento;
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
import CDRView2.SelecaoIndicadorAuditoriaChamadas;
import CDRView2.SelecaoIndicadorChamadas;
import CDRView2.SelecaoIndicadorDesempenho;
import CDRView2.SelecaoIndicadorDespesa;
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
import CDRView2.SelecaoIndicadorTrendAnalysis;
import CDRView2.SelecaoIndicadoresPortal;
import CDRView2.SelecaoIndicadorChamadasLongaDuracao;
import CDRView2.SelecaoIndicadorPesquisaIMEI;
import CDRView2.SelecaoIndicadorPesquisaPorERB;
import CDRView2.SelecaoIndicadorDestinosEspecificos;
import CDRView2.SelecaoIndicadorDestinosComuns;
import CDRView2.SelecaoIndicadorPrefixosDeRisco;
import CDRView2.SelecaoIndicadorDesempenhoDeRede;
import CDRView2.SelecaoIndicadorQoS;
import CDRView2.SelecaoRecursosAgrupado;
import CDRView2.SelecaoRecursosAnaliseCompletamento;
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
import CDRView2.SelecaoRecursosChamadas;
import CDRView2.SelecaoRecursosDesempenho;
import CDRView2.SelecaoRecursosDespesa;
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
import CDRView2.SelecaoRecursosDestinosComuns;
import CDRView2.SelecaoRecursosDestinosEspecificos;
import CDRView2.SelecaoRecursosPesquisaIMEI;
import CDRView2.SelecaoRecursosPesquisaPorERB;
import CDRView2.SelecaoRecursosPrefixosDeRisco;
import CDRView2.SelecaoRecursosDesempenhoDeRede;
import CDRView2.SelecaoRecursosQoS;
import CDRView2.SelecaoRecursosTrendAnalysis;
import CDRView2.TelaCDRView;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Configuracoes.DefsComum;
import Portal.Configuracoes.DiretoriosDefs;
import Portal.Operacoes.OpDesconectaUsuarios;
import Portal.Utils.Arquivo;
import Portal.Utils.HTMLUtil;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 * --> Imports que o Rose não Adiciona <-- import libjava.tipos.TipoData; import
 * Base.cCliente; import java.lang.reflect.Method; import javax.swing.Timer;
 * ---------------------------------------------------------------
 * 
 * Objeto principal. Responsável por atender as conexões dos clientes e
 * instanciar o objeto da classe de operação correspondente.
 */
public final class cPortal extends HttpServlet
{ 

   /**
    * Vetor contendo todas as operações possíveis do Portal.
    */
   private Map m_Operacoes = null;

   /**
    * Stream de saída HTML do cliente.
    */
   private PrintWriter  m_SaidaHtml = null;
   private HTMLUtil m_Html;
   private SelecaoRecursosDesempenho m_SelecaoRecursosDesempenho = null;
   private SelecaoIndicadorDesempenho m_SelecaoIndicadorDesempenho = null;
   private String m_IndicadoresDesempenho = null;
   
   private SelecaoRecursosDespesa m_SelecaoRecursosDespesa = null;
   private SelecaoIndicadorDespesa m_SelecaoIndicadorDespesa = null;
   private String m_IndicadoresDespesa = null;
   
   private SelecaoRecursosITXReceita m_SelecaoRecursosITXReceita = null;
   private SelecaoIndicadorITXReceita m_SelecaoIndicadorITXReceita = null;
   private String m_IndicadoresITXReceita = null;
   
   private SelecaoRecursosAgrupado m_SelecaoRecursosAgrupado = null;
   private SelecaoIndicadorAgrupado m_SelecaoIndicadorAgrupado = null;
   private String m_IndicadoresAgrupado = null;
   
   private SelecaoRecursosTrendAnalysis m_SelecaoRecursosTrendAnalysis = null;
   private SelecaoIndicadorTrendAnalysis m_SelecaoIndicadorTrendAnalysis = null;
   private String m_IndicadoresTrendAnalysis = null;
   
   private SelecaoRecursosGRE m_SelecaoRecursosGRE = null;
   private SelecaoIndicadorGRE m_SelecaoIndicadorGRE = null;
   private String m_IndicadoresGRE = null;
   
   private SelecaoRecursosIndicadoresSMP m_SelecaoRecursosIndicadoresSMP = null;
   private SelecaoIndicadorIndicadoresSMP m_SelecaoIndicadorIndicadoresSMP = null;
   private String m_IndicadoresIndicadoresSMP = null;
   
   private SelecaoRecursosDwGprs m_SelecaoRecursosDwGprs = null;
   private SelecaoIndicadorDwGprs m_SelecaoIndicadorDwGprs = null;
   private String m_IndicadoresDwGprs = null;
   
   private SelecaoRecursosDwGeral m_SelecaoRecursosDwGeral = null;
   private SelecaoIndicadorDwGeral m_SelecaoIndicadorDwGeral = null;
   private String m_IndicadoresDwGeral = null;
   
   private SelecaoRecursosChamadas m_SelecaoRecursosChamadas = null;
   private SelecaoIndicadorChamadas m_SelecaoIndicadorChamadas = null;
   private String m_IndicadoresChamadas = null;
   
   private SelecaoRecursosMinutosDeUso m_SelecaoRecursosMinutosDeUso = null;
   private SelecaoIndicadorMinutosDeUso m_SelecaoIndicadorMinutosDeUso = null;
   private String m_IndicadoresMinutosDeUso = null;
   
   private SelecaoRecursosDistFrequencia m_SelecaoRecursosDistFrequencia = null;
   private SelecaoIndicadorDistFrequencia m_SelecaoIndicadorDistFrequencia = null;
   private String m_IndicadoresDistFrequencia = null;
   
   private SelecaoRecursosPerseveranca m_SelecaoRecursosPerseveranca = null;
   private SelecaoIndicadorPerseveranca m_SelecaoIndicadorPerseveranca = null;
   private String m_IndicadoresPerseveranca = null;
   
   private SelecaoIndicadorDetalheChamada m_SelecaoIndicadorDetalhe = null;
   private String m_IndicadoresDetalhe = null;
   private SelecaoIndicadorAuditoriaChamadas m_SelecaoIndicadorAuditoria = null;
   private String m_IndicadoresAuditoria = null;
   private SelecaoRecursosPesquisaCodigo m_SelecaoRecursosPesquisa;
   private SelecaoIndicadorPesquisaCodigo m_SelecaoIndicadorPesquisa = null;
   private String m_IndicadoresPesquisa = null;
   
   private SelecaoIndicadorChamadasLongaDuracao m_SelecaoIndicadorChamadasLongaDuracao = null;
   private String m_IndicadoresChamadasLongaDuracao = null;
   
   private SelecaoRecursosDestinosEspecificos m_SelecaoRecursosDestinosEspecificos;
   private SelecaoIndicadorDestinosEspecificos m_SelecaoIndicadorDestinosEspecificos = null;
   private String m_IndicadoresDestinosEspecificos = null;
   
   private SelecaoRecursosDestinosComuns m_SelecaoRecursosDestinosComuns;
   private SelecaoIndicadorDestinosComuns m_SelecaoIndicadorDestinosComuns = null;
   private String m_IndicadoresDestinosComuns = null;
   
   private SelecaoRecursosPesquisaPorERB m_SelecaoRecursosPesquisaPorERB;
   private SelecaoIndicadorPesquisaPorERB m_SelecaoIndicadorPesquisaPorERB = null;
   private String m_IndicadoresPesquisaPorERB = null;
   
   //private SelecaoRecursosPrefixosDeRisco m_SelecaoRecursosPrefixosDeRisco;
   private SelecaoIndicadorPrefixosDeRisco m_SelecaoIndicadorPrefixosDeRisco = null;
   private String m_IndicadoresPrefixosDeRisco = null;
   
   private SelecaoIndicadorPesquisaIMEI m_SelecaoIndicadorPesquisaIMEI = null;
   private String m_IndicadoresPesquisaIMEI = null;
   
   private SelecaoRecursosDesempenhoDeRede m_SelecaoRecursosDesempenhoDeRede = null;
   private SelecaoIndicadorDesempenhoDeRede m_SelecaoIndicadorDesempenhoDeRede = null;
   private String m_IndicadoresDesempenhoDeRede = null;
   
   private SelecaoRecursosQoS m_SelecaoRecursosQoS = null;
   private SelecaoIndicadorQoS m_SelecaoIndicadorQoS = null;
   private String m_IndicadoresQoS = null;
   
   private SelecaoRecursosAnaliseCompletamento m_SelecaoRecursosAnaliseCompletamento = null;
   private SelecaoIndicadorAnaliseCompletamento m_SelecaoIndicadorAnaliseCompletamento = null;
   private String m_IndicadoresAnaliseCompletamento = null;
   private Timer m_Timer = null;
   private Calendar m_DataAtual = null;
   private Date m_HoraAtual = null;
   private SelecaoRecursosMatraf m_SelecaoRecursosMatraf = null;
   private SelecaoIndicadorMatraf m_SelecaoIndicadorMatraf = null;
   private String m_IndicadoresMatraf = null;
   private SelecaoRecursosAnatelSMP3 m_SelecaoRecursosAnatelSMP3 = null;
   private SelecaoIndicadorAnatelSMP3 m_SelecaoIndicadorAnatelSMP3 = null;
   private String m_IndicadoresAnatelSMP3 = null;
   private SelecaoRecursosAnatelSMP3e4 m_SelecaoRecursosAnatelSMP3e4 = null;
   private SelecaoIndicadorAnatelSMP3e4 m_SelecaoIndicadorAnatelSMP3e4 = null;
   private String m_IndicadoresAnatelSMP3e4 = null;
   private SelecaoRecursosAnatelSMP5 m_SelecaoRecursosAnatelSMP5 = null;
   private SelecaoIndicadorAnatelSMP5 m_SelecaoIndicadorAnatelSMP5 = null;
   private String m_IndicadoresAnatelSMP5 = null;
   private SelecaoRecursosAnatelSMP6 m_SelecaoRecursosAnatelSMP6 = null;
   private SelecaoIndicadorAnatelSMP6 m_SelecaoIndicadorAnatelSMP6 = null;
   private String m_IndicadoresAnatelSMP6 = null;
   private SelecaoRecursosAnatelSMP7 m_SelecaoRecursosAnatelSMP7 = null;
   private SelecaoIndicadorAnatelSMP7 m_SelecaoIndicadorAnatelSMP7 = null;
   private String m_IndicadoresAnatelSMP7 = null;
   private SelecaoRecursosAnatelSMP8e9 m_SelecaoRecursosAnatelSMP8e9 = null;
   private SelecaoIndicadorAnatelSMP8e9 m_SelecaoIndicadorAnatelSMP8e9 = null;
   private String m_IndicadoresAnatelSMP8e9 = null;
   private SelecaoRecursosAnatelSMP3Ericsson m_SelecaoRecursosAnatelSMP3Ericsson = null;
   private SelecaoIndicadorAnatelSMP3Ericsson m_SelecaoIndicadorAnatelSMP3Ericsson = null;
   private String m_IndicadoresAnatelSMP3Ericsson = null;
   private SelecaoRecursosAnatelSMP5Ericsson m_SelecaoRecursosAnatelSMP5Ericsson = null;
   private SelecaoIndicadorAnatelSMP5Ericsson m_SelecaoIndicadorAnatelSMP5Ericsson = null;
   private String m_IndicadoresAnatelSMP5Ericsson = null;
   private SelecaoRecursosAnatelSMP6Ericsson m_SelecaoRecursosAnatelSMP6Ericsson = null;
   private SelecaoIndicadorAnatelSMP6Ericsson m_SelecaoIndicadorAnatelSMP6Ericsson = null;
   private String m_IndicadoresAnatelSMP6Ericsson = null;
   private SelecaoRecursosAnatelSMP7Ericsson m_SelecaoRecursosAnatelSMP7Ericsson = null;
   private SelecaoIndicadorAnatelSMP7Ericsson m_SelecaoIndicadorAnatelSMP7Ericsson = null;
   private String m_IndicadoresAnatelSMP7Ericsson = null;
   private SelecaoRecursosAnatelLDN m_SelecaoRecursosAnatelLDN;
   private SelecaoIndicadorAnatelLDN m_SelecaoIndicadorAnatelLDN = null;
   private String m_IndicadoresAnatelLDN = null;
/*
 * private SelecaoRecursosInterconexaoAudit m_SelecaoRecursosInterconexaoAudit =
 * null; private SelecaoIndicadorInterconexaoAudit
 * m_SelecaoIndicadorInterconexaoAudit = null; private String
 * m_IndicadoresInterconexaoAudit = null; private
 * SelecaoRecursosInterconexaoForaRota m_SelecaoRecursosInterconexaoForaRota =
 * null; private SelecaoIndicadorInterconexaoForaRota
 * m_SelecaoIndicadorInterconexaoForaRota = null; private String
 * m_IndicadoresInterconexaoForaRota = null;
 */
   private Map m_MapAgendasDesempenho = null;
   private Map m_MapAgendasDespesa = null;
   private Map m_MapAgendasITXReceita = null;
   private Map m_MapAgendasTrendAnalysis = null;
   private Map m_MapAgendasGRE = null;
   private Map m_MapAgendasDWGERAL = null;
   private Map m_MapAgendasDWGPRS = null;
   private Map m_MapAgendasAgrupado = null;
   
   private Map m_MapAgendasIndicadoresSMP = null;
   private Map m_MapAgendasChamadas = null;
   private Map m_MapAgendasMinutosDeUso = null;
   
   private Map m_MapAgendasDistFrequencia = null;
   private Map m_MapAgendasPerseveranca = null;
   
   private Map m_MapAgendasDetalheChamadas = null;
   private Map m_MapAgendasAuditoriaChamadas = null;
   private Map m_MapAgendasPesquisa = null;
   private Map m_MapHistoricosTrafego = null;
   private Map m_MapAgendasMatraf = null;
   private Map m_MapAgendasAnatelSMP = null;
   private Map m_MapAgendasAnaliseCompletamento = null;
//   private Map m_MapAgendasInterconexaoAudit = null;
   private Map m_MapAgendasInterconexaoForaRota = null;
   private Map m_MapAgendasChamadasLongaDuracao = null;
   private Map m_MapAgendasDestinosEspecificos = null;
   private Map m_MapAgendasDestinosComuns = null;
   private Map m_MapAgendasPesquisaPorERB = null;
   private Map m_MapAgendasPrefixosDeRisco = null;
   private Map m_MapAgendasPesquisaIMEI = null;
   
   private Map m_MapAgendasDesempenhoDeRede = null;
   private Map m_MapAgendasQoS = null;
   
   private Map m_MapHistoricos = null;
   private Arquivo m_ArquivoOpCfg;
   private ArquivosDefs m_ArquivosDefs;
   private DiretoriosDefs m_DiretoriosDefs;

   public cPortal()
   {
      
      m_ArquivosDefs = new ArquivosDefs();

      /** Instanciando a PortavelCDRview para carregar o arquivo de properties */
      new PortavelCDRView(null);

      if (leArquivoOpCfg() == false)
      {
         System.out.println("Portal - Portal(): Arquivo de configuracao de operacoes nao encontrado");
      }

      /**
       * Seta a referencia do Servidor de Util no No em que esta rodando o Tomcat.
       */
      No no = NoUtil.getNo();
      
      /**
       * Caso o No nao possua pegar a referencia do servutil tenta-se novamente.
       * */
      if (no.getConexaoServUtil().getM_iUtil() == null)
      {
          no.createConexao(DefsComum.s_ServUtil,
                           no.getConexaoServUtil().getModoConexao(), 
                           no.getHostName(), 
                           no.getConexaoServUtil().getNomeObjetoCorba(),
                           no.getConexaoServUtil().getPorta());
      }
      
      /**
       * So faz o bloco abaixo se a referencia corba do servutil foi obtida apos a tentativa acima
       * */
      if (no.getConexaoServUtil().getM_iUtil() != null)
      {
//        Seta o cliente
          Cliente.fnCliente(DefsComum.s_CLIENTE);
//          for (int i = 0; i < TelaCDRView.m_TemTipoRelatorio.length; i++)
//    		{
//    			TelaCDRView.m_TemTipoRelatorio[i] = true;
//    		}

          // #######
          // # Inicia estruturas de contadores para visualização de relatórios
          try
          {
             iniciaContadoresDesempenho();
          }
          catch (Exception Exc)
          {
             System.out.println("Erro ao iniciar contadores de Desempenho");
             Exc.printStackTrace();
          }

          try
          {
             iniciaContadoresDetalhe();
          }
          catch (Exception Exc)
          {
             System.out.println("Erro ao iniciar contadores de Detalhe");
             Exc.printStackTrace();
          }
          
          try
          {
             iniciaContadoresAuditoria();
          }
          catch (Exception Exc)
          {
             System.out.println("Erro ao iniciar contadores de Auditoria");
             Exc.printStackTrace();
          }

          try
          {
             iniciaContadoresPesquisa();
          }
          catch (Exception Exc)
          {
             System.out.println("Erro ao iniciar contadores de Pesquisa");
             Exc.printStackTrace();
          }

          if (DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("timsul") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("vivo") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("ctbc") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("gvt") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("nextel") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("telemig")==0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("embratel")==0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("acme")==0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("oi")==0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("tim")==0)
          {
             try
             {
                iniciaContadoresMatraf();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Matraf");
                Exc.printStackTrace();
             }

             try
             {
                iniciaContadoresAnaliseCompletamento();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Analise de Completamento");
                Exc.printStackTrace();
             }
          }

          if (DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("amazonia_celular") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("timsul") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("vivo") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("sercomtel") == 0 ||
    		  DefsComum.s_CLIENTE.compareToIgnoreCase("telemig") == 0 ||
          	  DefsComum.s_CLIENTE.compareToIgnoreCase("ctbc") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("gvt") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("nextel") == 0 ||
              DefsComum.s_CLIENTE.compareToIgnoreCase("oi") == 0 ||
        	  DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0)

          {
             try
             {
                iniciaContadoresAnatelSMP3();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Anatel SMP3");
                Exc.printStackTrace();
             }

             try
             {
                iniciaContadoresAnatelSMP3e4();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Anatel SMP3e4");
                Exc.printStackTrace();
             }

             try
             {
                iniciaContadoresAnatelSMP5();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Anatel SMP5");
                Exc.printStackTrace();
             }

             try
             {
                iniciaContadoresAnatelSMP6();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Anatel SMP6");
                Exc.printStackTrace();
             }

             try
             {
                iniciaContadoresAnatelSMP7();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Anatel SMP7");
                Exc.printStackTrace();
             }

             try
             {
                iniciaContadoresAnatelSMP8e9();
             }
             catch (Exception Exc)
             {
                System.out.println("Erro ao iniciar contadores de Anatel SMP8e9");
                Exc.printStackTrace();
             }

              try
              {
                iniciaContadoresAnatelLDN();
              }
              catch (Exception Exc)
              {
                System.out.println("Erro ao iniciar contadores de Anatel LDN");
       		  Exc.printStackTrace();
              }
          }
          if( DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("oi") == 0)
          {
    	    try
              {
        	  iniciaContadoresIndicadoresSMP();
              }
              catch (Exception Exc)
              {
       		  System.out.println("Erro ao iniciar contadores de IndicadoresSMP");
       		  Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresAnatelSMP3Ericsson();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Anatel SMP3Ericsson");
                 Exc.printStackTrace();
              }

              try
              {
                 iniciaContadoresAnatelSMP5Ericsson();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Anatel SMP5Ericsson");
                 Exc.printStackTrace();
              }

              try
              {
                 iniciaContadoresAnatelSMP6Ericsson();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Anatel SMP6Ericsson");
                 Exc.printStackTrace();
              }

              try
              {
                 iniciaContadoresAnatelSMP7Ericsson();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Anatel SMP7Ericsson");
                 Exc.printStackTrace();
              }
          }
          
          if(DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("telemig") == 0
        		  || DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0)
          {
        	  try
              {
                 iniciaContadoresChamadas();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Chamadas");
                 Exc.printStackTrace();
              } 
              try
              {
                 iniciaContadoresMinutosDeUso();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Minutos de Uso");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresDistFrequencia();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Distribuição de Frequencia");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresPerseveranca();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Perseverança");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresChamadasLongaDuracao();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Chamadas Longa Duração");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresPesquisaIMEI();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Pesquisa IMEI x NTC");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresDestinosEspecificos();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Destinos Especificos");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresDestinosComuns();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Destinos Comuns");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresPesquisaPorERB();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Pesquisa Por ERB");
                 Exc.printStackTrace();
              }
              try
              {
                 iniciaContadoresPrefixosDeRisco();
              }
              catch (Exception Exc)
              {
                 System.out.println("Erro ao iniciar contadores de Prefixos de Risco");
                 Exc.printStackTrace();
              }
          }
          if(DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0)
          {
	          try
	          {
	             iniciaContadoresDesempenhoDeRede();
	          }
	          catch (Exception Exc)
	          {
	             System.out.println("Erro ao iniciar contadores de Desempenho De Rede");
	             Exc.printStackTrace();
	          }
	          
	          try
	          {
	             iniciaContadoresQoS();
	          }
	          catch (Exception Exc)
	          {
	             System.out.println("Erro ao iniciar contadores de QoS");
	             Exc.printStackTrace();
	          }
          }
	          
	      if(DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0){

	        	  try
	        	  {
	        		  iniciaContadoresDespesa();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de Despesa");
	        		  Exc.printStackTrace();
	        	  }

	        	  try
	        	  {
	        		  iniciaContadoresITXReceita();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de ITXReceita");
	        		  Exc.printStackTrace();
	        	  }

	        	  try
	        	  {
	        		  iniciaContadoresAgrupado();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de Agrupado");
	        		  Exc.printStackTrace();
	        	  }

	        	  try
	        	  {
	        		  iniciaContadoresGRE();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de GRE");
	        		  Exc.printStackTrace();
	        	  }

	        	  try
	        	  {
	        		  iniciaContadoresTrendAnalysis();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de TrendAnalysis");
	        		  Exc.printStackTrace();
	        	  }

	        	  try
	        	  {
	        		  iniciaContadoresDwGeral();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de Dw Geral");
	        		  Exc.printStackTrace();
	        	  }

	        	  try
	        	  {
	        		  iniciaContadoresDwGprs();
	        	  }
	        	  catch (Exception Exc)
	        	  {
	        		  System.out.println("Erro ao iniciar contadores de Dw Gprs");
	        		  Exc.printStackTrace();
	        	  }

	          }
          
    /*
     * try { iniciaContadoresInterconexaoAudit(); } catch (Exception Exc) {
     * System.out.println("Erro ao iniciar contadores de Interconexao Auditoria");
     * Exc.printStackTrace(); }
     * 
     * try { iniciaContadoresInterconexaoForaRota(); } catch (Exception Exc) {
     * System.out.println("Erro ao iniciar contadores de Interconexao Fora de
     * Rota"); Exc.printStackTrace(); }
     */
          // #######

          // Inicia o map de históricos (cache de históricos)
          m_MapHistoricos = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de históricos (cache de históricos de relatórios de
          // tráfego)
          m_MapHistoricosTrafego = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasDesempenho = Collections.synchronizedMap(new TreeMap());
          
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasDespesa = Collections.synchronizedMap(new TreeMap());
          
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasITXReceita = Collections.synchronizedMap(new TreeMap());
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasTrendAnalysis = Collections.synchronizedMap(new TreeMap());
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasGRE = Collections.synchronizedMap(new TreeMap());
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasDWGERAL = Collections.synchronizedMap(new TreeMap());
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasDWGPRS = Collections.synchronizedMap(new TreeMap());
       // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasAgrupado = Collections.synchronizedMap(new TreeMap());
          
          // Inicia o map de agendas de IndicadoresSMP (cache de agendas de relatórios
          // de desempenho)
          m_MapAgendasIndicadoresSMP = Collections.synchronizedMap(new TreeMap());
         
          // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de chamadas)
          m_MapAgendasChamadas = Collections.synchronizedMap(new TreeMap());
          
          // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de MinutosDeUso)
          m_MapAgendasMinutosDeUso = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de Distribuição de Frequência)
          m_MapAgendasDistFrequencia = Collections.synchronizedMap(new TreeMap());
          
          // Inicia o map de agendas de desempenho (cache de agendas de relatórios
          // de Perseverança)
          m_MapAgendasPerseveranca = Collections.synchronizedMap(new TreeMap());
          
          // Inicia o map de agendas de detalhe de chamadas (cache de agendas de
          // relatórios de detalhe de chamadas)
          m_MapAgendasDetalheChamadas = Collections.synchronizedMap(new TreeMap());
          
//        Inicia o map de agendas de Auditoria de chamadas (cache de agendas de
          // relatórios de Auditoria de chamadas)
          m_MapAgendasAuditoriaChamadas = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de pesquisa (cache de agendas de relatórios de
          // pesquisa)
          m_MapAgendasPesquisa = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de matraf (cache de agendas de relatórios de
          // matraf)
          m_MapAgendasMatraf = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de SMP (cache de agendas de relatórios de SMP)
          m_MapAgendasAnatelSMP = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de Analise de Completamento (cache de agendas
          // de relatórios de Analise de Completamento)
          m_MapAgendasAnaliseCompletamento = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de Interconexao - Auditoria (cache de agendas
          // de relatórios de Interconexão-Auditoria)
//          m_MapAgendasInterconexaoAudit = Collections.synchronizedMap(new TreeMap());

          // Inicia o map de agendas de Interconexao - Fora de Rota (cache de
          // agendas de relatórios de Interconexão-Fora de Rota)
//          m_MapAgendasInterconexaoForaRota = Collections.synchronizedMap(new
//     TreeMap());
          m_MapAgendasChamadasLongaDuracao = Collections.synchronizedMap(new TreeMap());
          m_MapAgendasDestinosEspecificos = Collections.synchronizedMap(new TreeMap());
          m_MapAgendasDestinosComuns = Collections.synchronizedMap(new TreeMap());
          m_MapAgendasPesquisaPorERB = Collections.synchronizedMap(new TreeMap());
          m_MapAgendasPrefixosDeRisco = Collections.synchronizedMap(new TreeMap());
          m_MapAgendasPesquisaIMEI = Collections.synchronizedMap(new TreeMap());
          
          m_MapAgendasDesempenhoDeRede = Collections.synchronizedMap(new TreeMap());
          m_MapAgendasQoS = Collections.synchronizedMap(new TreeMap());

          try
          {
             m_Timer = new Timer(200000,
             //m_Timer = new Timer(2000,
                     new ActionListener()
                     {
                        int i = 0;

                        public void actionPerformed(ActionEvent e)
                        {
                           String DataStr = ""+new java.util.Date();
                           System.out.println(DataStr + " - Servlet Portal executando...");
                           manutencao();
                           i++;
                        }
                     });
             m_Timer.start();
          }
          catch (Exception ExcTimer)
          {
             System.out.println("Erro no timer");
             ExcTimer.printStackTrace();
          }
          String DataStartUp = ""+new java.util.Date();//new java.util.Date();
          System.out.println(DataStartUp + " - * * * * * * * * * * * * * * * * * * * * * * *");
          System.out.println(DataStartUp + " - * Servlet Portal Iniciado");
          System.out.println(DataStartUp + " - * * * * * * * * * * * * * * * * * * * * * * *\n");
      }
      
      
   }

   /**
    * @return boolean
    * @exception Lê
    *                o arquivo de configuração de operações. Caso o arquivo não
    *                exista, retorna "false".
    * @roseuid 3BF4234B0263
    */
   private boolean leArquivoOpCfg()
   {

      m_Operacoes = Collections.synchronizedMap(new TreeMap());
      m_ArquivoOpCfg = new Arquivo();
      m_ArquivoOpCfg.setDiretorio(NoUtil.getNo().getDiretorioDefs().getS_DIR_CDRVIEW()+
                                  DiretoriosDefs.s_DIR_CFG_WEB+
                                  DiretoriosDefs.s_DIR_ARQS_CFG);
      m_ArquivoOpCfg.setNome(m_ArquivosDefs.m_ARQ_OP_CFG);

      if (m_ArquivoOpCfg.abre('r'))
      {
         String LinhaCfg = null, Operacao, Chave;
         System.out.println("******** Operacoes Configuradas - Inicio");
         while ((LinhaCfg = m_ArquivoOpCfg.leLinha()) != null)
         {
            //System.out.println("Linha: "+LinhaCfg);
            if (LinhaCfg.length() > 0 && LinhaCfg.charAt(0) != '#')
            {
               // Separa a operação e a palavra-chave
               Operacao = LinhaCfg.substring(0, LinhaCfg.indexOf(' '));
               Chave    = LinhaCfg.substring(LinhaCfg.lastIndexOf(' ')+1, LinhaCfg.length());
               System.out.println("Operacao: "+Operacao + "\t\t\t\t - Chave: " +Chave);
               m_Operacoes.put(Chave, Operacao);
            }
         }
         m_Operacoes.put("downloadExport","OpDownloadExport");
         m_ArquivoOpCfg.fecha();
         System.out.println("******** Operacoes Configuradas - Fim");
         return true;
      }
      else
         return false;
   }

   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresDesempenho()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores desempenho...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDesempenho = new SelecaoRecursosDesempenho();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDesempenho.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDesempenho = new SelecaoIndicadorDesempenho();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDesempenho.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDesempenho = m_SelecaoIndicadorDesempenho.fnGetListaIndicadores(TelaCDRView.REL_DESEMPENHO);

      if (m_IndicadoresDesempenho.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDesempenho = m_IndicadoresDesempenho.substring("Frequência;Aglutinado;".length(), m_IndicadoresDesempenho.length());

      System.out.println(new java.util.Date()+" - Contadores desempenho iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresDespesa()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Despesa...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDespesa = new SelecaoRecursosDespesa();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDespesa.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDespesa = new SelecaoIndicadorDespesa();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDespesa.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDespesa = m_SelecaoIndicadorDespesa.fnGetListaIndicadores(TelaCDRView.REL_DESPESA);

      if (m_IndicadoresDespesa.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDespesa = m_IndicadoresDespesa.substring("Frequência;Aglutinado;".length(), m_IndicadoresDespesa.length());

      System.out.println(new java.util.Date()+" - Contadores despesa iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresITXReceita()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Receita...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosITXReceita = new SelecaoRecursosITXReceita();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosITXReceita.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorITXReceita = new SelecaoIndicadorITXReceita();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorITXReceita.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresITXReceita = m_SelecaoIndicadorITXReceita.fnGetListaIndicadores(TelaCDRView.REL_ITXRECEITA);

      if (m_IndicadoresITXReceita.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresITXReceita = m_IndicadoresITXReceita.substring("Frequência;Aglutinado;".length(), m_IndicadoresITXReceita.length());

      System.out.println(new java.util.Date()+" - Contadores ITXReceita iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresTrendAnalysis()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores TrendAnalysis...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosTrendAnalysis = new SelecaoRecursosTrendAnalysis();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosTrendAnalysis.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorTrendAnalysis = new SelecaoIndicadorTrendAnalysis();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorTrendAnalysis.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresTrendAnalysis = m_SelecaoIndicadorTrendAnalysis.fnGetListaIndicadores(TelaCDRView.REL_TRENDANALYSIS);

      if (m_IndicadoresTrendAnalysis.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresTrendAnalysis = m_IndicadoresTrendAnalysis.substring("Frequência;Aglutinado;".length(), m_IndicadoresTrendAnalysis.length());

      System.out.println(new java.util.Date()+" - Contadores TrendAnalysis iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresAgrupado()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Agrupado...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAgrupado = new SelecaoRecursosAgrupado();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAgrupado.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAgrupado = new SelecaoIndicadorAgrupado();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAgrupado.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAgrupado = m_SelecaoIndicadorAgrupado.fnGetListaIndicadores(TelaCDRView.REL_AGRUPADO);

      if (m_IndicadoresAgrupado.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAgrupado = m_IndicadoresAgrupado.substring("Frequência;Aglutinado;".length(), m_IndicadoresAgrupado.length());

      System.out.println(new java.util.Date()+" - Contadores Agrupado iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresGRE()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores GRE...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosGRE = new SelecaoRecursosGRE();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosGRE.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorGRE = new SelecaoIndicadorGRE();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorGRE.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresGRE = m_SelecaoIndicadorGRE.fnGetListaIndicadores(TelaCDRView.REL_GRE);

      if (m_IndicadoresGRE.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresGRE = m_IndicadoresGRE.substring("Frequência;Aglutinado;".length(), m_IndicadoresGRE.length());

      System.out.println(new java.util.Date()+" - Contadores GRE iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresDwGeral()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores DwGeral...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDwGeral = new SelecaoRecursosDwGeral();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDwGeral.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDwGeral = new SelecaoIndicadorDwGeral();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDwGeral.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDwGeral = m_SelecaoIndicadorDwGeral.fnGetListaIndicadores(TelaCDRView.REL_DW_GERAL);

      if (m_IndicadoresDwGeral.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDwGeral = m_IndicadoresDwGeral.substring("Frequência;Aglutinado;".length(), m_IndicadoresDwGeral.length());

      System.out.println(new java.util.Date()+" - Contadores DwGeral iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresDwGprs()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores DwGprs...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDwGprs = new SelecaoRecursosDwGprs();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDwGprs.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDwGprs = new SelecaoIndicadorDwGprs();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDwGprs.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDwGprs = m_SelecaoIndicadorDwGeral.fnGetListaIndicadores(TelaCDRView.REL_DW_GPRS);

      if (m_IndicadoresDwGprs.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDwGprs = m_IndicadoresDwGprs.substring("Frequência;Aglutinado;".length(), m_IndicadoresDwGprs.length());

      System.out.println(new java.util.Date()+" - Contadores DwGprs iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresChamadas()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores chamadas...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosChamadas = new SelecaoRecursosChamadas();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosChamadas.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorChamadas = new SelecaoIndicadorChamadas();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorChamadas.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresChamadas = m_SelecaoIndicadorChamadas.fnGetListaIndicadores(TelaCDRView.REL_CHAMADAS);

      if (m_IndicadoresChamadas.indexOf("Frequência;Aglutinado;") != -1)
    	  m_IndicadoresChamadas = m_IndicadoresChamadas.substring("Frequência;Aglutinado;".length(), m_IndicadoresChamadas.length());

      System.out.println(new java.util.Date()+" - Contadores chamadas iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C838658016D
    */
   private void iniciaContadoresMinutosDeUso()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores minutos de uso...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosMinutosDeUso = new SelecaoRecursosMinutosDeUso();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosMinutosDeUso.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorMinutosDeUso = new SelecaoIndicadorMinutosDeUso();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorMinutosDeUso.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresMinutosDeUso = m_SelecaoIndicadorMinutosDeUso.fnGetListaIndicadores(TelaCDRView.REL_MINUTOS_USO);

      if (m_IndicadoresMinutosDeUso.indexOf("Frequência;Aglutinado;") != -1)
    	  m_IndicadoresMinutosDeUso = m_IndicadoresMinutosDeUso.substring("Frequência;Aglutinado;".length(), m_IndicadoresMinutosDeUso.length());

      System.out.println(new java.util.Date()+" - Contadores minutos de uso iniciados");
   }
   /**
    * 
    *
    */
   private void iniciaContadoresDistFrequencia()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Distribuição de Frequência...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDistFrequencia = new SelecaoRecursosDistFrequencia();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDistFrequencia.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDistFrequencia = new SelecaoIndicadorDistFrequencia();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDistFrequencia.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDistFrequencia = m_SelecaoIndicadorDistFrequencia.fnGetListaIndicadores(TelaCDRView.REL_DISTFREQUENCIA);

      if (m_IndicadoresDistFrequencia.indexOf("Frequência;Aglutinado;") != -1)
    	  m_IndicadoresDistFrequencia = m_IndicadoresDistFrequencia.substring("Frequência;Aglutinado;".length(), m_IndicadoresDistFrequencia.length());

      System.out.println(new java.util.Date()+" - Contadores minutos de uso iniciados");
   }
   
   private void iniciaContadoresPrefixosDeRisco()
   {
	   System.out.println(new java.util.Date()+" - Iniciando contadores Prefixos...");
	      // Cria a lista de indicadores para este cliente (somente a primeira vez)
			m_SelecaoIndicadorPrefixosDeRisco = new SelecaoIndicadorPrefixosDeRisco();

	      // Torna a lista ativa (por ser estática, este procedimento é necessário)
	      m_SelecaoIndicadorPrefixosDeRisco.fnAtiva();

	      // Recupera lista de indicadores possíveis
	      m_IndicadoresPrefixosDeRisco = m_SelecaoIndicadorPrefixosDeRisco.fnGetListaIndicadores(TelaCDRView.REL_PREF_DE_RISCO);
	      if (m_IndicadoresPrefixosDeRisco.indexOf("Frequência;Aglutinado;") != -1)
	         m_IndicadoresPrefixosDeRisco = m_IndicadoresPrefixosDeRisco.substring("Frequência;Aglutinado;".length(), m_IndicadoresPrefixosDeRisco.length());

	      System.out.println(new java.util.Date()+" - Contadores PrefixosDeRisco iniciados");
   }
   
   private void iniciaContadoresPesquisaPorERB()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores PesquisaPorERB...");

	   // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosPesquisaPorERB = new SelecaoRecursosPesquisaPorERB();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosPesquisaPorERB.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorPesquisaPorERB = new SelecaoIndicadorPesquisaPorERB();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorPesquisaPorERB.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresPesquisaPorERB = m_SelecaoIndicadorPesquisaPorERB.fnGetListaIndicadores(TelaCDRView.REL_PESQ_POR_ERB);
      if (m_IndicadoresPesquisaPorERB.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresPesquisaPorERB = m_IndicadoresPesquisaPorERB.substring("Frequência;Aglutinado;".length(), m_IndicadoresPesquisaPorERB.length());

      System.out.println(new java.util.Date()+" - Contadores PesquisaPorERB iniciados");
   }
   
   private void iniciaContadoresDestinosComuns()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores DestinosComuns...");

	   // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDestinosComuns = new SelecaoRecursosDestinosComuns();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDestinosComuns.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDestinosComuns = new SelecaoIndicadorDestinosComuns();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDestinosComuns.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDestinosComuns = m_SelecaoIndicadorDestinosComuns.fnGetListaIndicadores(TelaCDRView.REL_DEST_COMUNS);
      if (m_IndicadoresDestinosComuns.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDestinosComuns = m_IndicadoresDestinosComuns.substring("Frequência;Aglutinado;".length(), m_IndicadoresDestinosComuns.length());

      System.out.println(new java.util.Date()+" - Contadores DestinosComuns iniciados");
   }
   
   private void iniciaContadoresDestinosEspecificos()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores DestinosEspecificos...");

	   // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDestinosEspecificos = new SelecaoRecursosDestinosEspecificos();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDestinosEspecificos.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDestinosEspecificos = new SelecaoIndicadorDestinosEspecificos();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDestinosEspecificos.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDestinosEspecificos = m_SelecaoIndicadorDestinosEspecificos.fnGetListaIndicadores(TelaCDRView.REL_DEST_ESPEC);
      if (m_IndicadoresDestinosEspecificos.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDestinosEspecificos = m_IndicadoresDestinosEspecificos.substring("Frequência;Aglutinado;".length(), m_IndicadoresDestinosEspecificos.length());

      System.out.println(new java.util.Date()+" - Contadores DestinosEspecificos iniciados");
   }
   
   private void iniciaContadoresChamadasLongaDuracao()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores ChamadaLongaDuracao...");
      // Cria a lista de indicadores para este cliente (somente a primeira vez)
		m_SelecaoIndicadorChamadasLongaDuracao = new SelecaoIndicadorChamadasLongaDuracao();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorChamadasLongaDuracao.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresChamadasLongaDuracao = m_SelecaoIndicadorChamadasLongaDuracao.fnGetListaIndicadores(TelaCDRView.REL_CHAM_LONGA_DUR);
      if (m_IndicadoresChamadasLongaDuracao.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresChamadasLongaDuracao = m_IndicadoresChamadasLongaDuracao.substring("Frequência;Aglutinado;".length(), m_IndicadoresChamadasLongaDuracao.length());

      System.out.println(new java.util.Date()+" - Contadores ChamadasLongaDuração iniciados");
   }
   
   private void iniciaContadoresPesquisaIMEI()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores PesquisaIMEI...");
      // Cria a lista de indicadores para este cliente (somente a primeira vez)
		m_SelecaoIndicadorPesquisaIMEI = new SelecaoIndicadorPesquisaIMEI();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorPesquisaIMEI.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresPesquisaIMEI = m_SelecaoIndicadorPesquisaIMEI.fnGetListaIndicadores(TelaCDRView.REL_PESQ_IMEI);
      if (m_IndicadoresPesquisaIMEI.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresPesquisaIMEI = m_IndicadoresPesquisaIMEI.substring("Frequência;Aglutinado;".length(), m_IndicadoresPesquisaIMEI.length());

      System.out.println(new java.util.Date()+" - Contadores PesquisaIMEI iniciados");
   }
   
   private void iniciaContadoresPerseveranca()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Perseverança...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosPerseveranca = new SelecaoRecursosPerseveranca();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosPerseveranca.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorPerseveranca = new SelecaoIndicadorPerseveranca();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorPerseveranca.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresPerseveranca = m_SelecaoIndicadorPerseveranca.fnGetListaIndicadores(TelaCDRView.REL_PERSEVERANCA);

      if (m_IndicadoresPerseveranca.indexOf("Frequência;Aglutinado;") != -1)
    	  m_IndicadoresPerseveranca = m_IndicadoresPerseveranca.substring("Frequência;Aglutinado;".length(), m_IndicadoresPerseveranca.length());

      System.out.println(new java.util.Date()+" - Contadores minutos de uso iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C8386690027
    */
   private void iniciaContadoresDetalhe()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores detalhe...");
      // Cria a lista de indicadores para este cliente (somente a primeira vez)
		m_SelecaoIndicadorDetalhe = new SelecaoIndicadorDetalheChamada();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDetalhe.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDetalhe = m_SelecaoIndicadorDetalhe.fnGetListaIndicadores(TelaCDRView.REL_DETALHECHAMADAS);
      if (m_IndicadoresDetalhe.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDetalhe = m_IndicadoresDetalhe.substring("Frequência;Aglutinado;".length(), m_IndicadoresDetalhe.length());

      System.out.println(new java.util.Date()+" - Contadores detalhe iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3C8386690027
    */
   private void iniciaContadoresAuditoria()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Auditoria...");
      // Cria a lista de indicadores para este cliente (somente a primeira vez)
		m_SelecaoIndicadorAuditoria = new SelecaoIndicadorAuditoriaChamadas();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAuditoria.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAuditoria = m_SelecaoIndicadorAuditoria.fnGetListaIndicadores(TelaCDRView.REL_AUDITORIACHAMADAS);
      if (m_IndicadoresAuditoria.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAuditoria = m_IndicadoresAuditoria.substring("Frequência;Aglutinado;".length(), m_IndicadoresAuditoria.length());

      System.out.println(new java.util.Date()+" - Contadores Auditoria iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3C83866A0187
    */
   private void iniciaContadoresPesquisa()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores pesquisa...");

	   // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosPesquisa = new SelecaoRecursosPesquisaCodigo();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosPesquisa.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorPesquisa = new SelecaoIndicadorPesquisaCodigo();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorPesquisa.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresPesquisa = m_SelecaoIndicadorPesquisa.fnGetListaIndicadores(TelaCDRView.REL_PESQUISACODIGO);
      if (m_IndicadoresPesquisa.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresPesquisa = m_IndicadoresPesquisa.substring("Frequência;Aglutinado;".length(), m_IndicadoresPesquisa.length());

      System.out.println(new java.util.Date()+" - Contadores pesquisa iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3FE74C83015D
    */
   private void iniciaContadoresMatraf()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Matraf...");
      SelecaoIndicadorMatraf.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosMatraf = new SelecaoRecursosMatraf();
      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosMatraf.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorMatraf = new SelecaoIndicadorMatraf();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorMatraf.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresMatraf = m_SelecaoIndicadorMatraf.fnGetListaIndicadores(TelaCDRView.REL_MATRAF);
      if (m_IndicadoresMatraf != null && m_IndicadoresMatraf.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresMatraf = m_IndicadoresMatraf.substring("Frequência;Aglutinado;".length(), m_IndicadoresMatraf.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel Matraf iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3FE7350C037D
    */
   private void iniciaContadoresAnatelSMP3()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP3...");
      SelecaoIndicadorAnatelSMP3.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP3 = new SelecaoRecursosAnatelSMP3();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP3.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP3 = new SelecaoIndicadorAnatelSMP3();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP3.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP3 = m_SelecaoIndicadorAnatelSMP3.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP3);
      if (m_IndicadoresAnatelSMP3 != null && m_IndicadoresAnatelSMP3.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP3 = m_IndicadoresAnatelSMP3.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP3.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP3 iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3FE7351802DA
    */
   private void iniciaContadoresAnatelSMP3e4()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP3e4...");
      SelecaoIndicadorAnatelSMP3e4.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP3e4 = new SelecaoRecursosAnatelSMP3e4();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP3e4.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP3e4 = new SelecaoIndicadorAnatelSMP3e4();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP3e4.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP3e4 = m_SelecaoIndicadorAnatelSMP3e4.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP34);
      if (m_IndicadoresAnatelSMP3e4 != null && m_IndicadoresAnatelSMP3e4.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP3e4 = m_IndicadoresAnatelSMP3e4.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP3e4.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP3e4 iniciados");
   }
   /**
    * @return void
    * @exception
    * @roseuid 3FE7350C037D
    */
   private void iniciaContadoresAnatelSMP3Ericsson()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP3 Ericsson...");
      SelecaoIndicadorAnatelSMP3Ericsson.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP3Ericsson = new SelecaoRecursosAnatelSMP3Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP3Ericsson.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP3Ericsson = new SelecaoIndicadorAnatelSMP3Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP3Ericsson.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP3Ericsson = m_SelecaoIndicadorAnatelSMP3Ericsson.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP3_ERICSSON);
      if (m_IndicadoresAnatelSMP3Ericsson != null && m_IndicadoresAnatelSMP3Ericsson.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP3Ericsson = m_IndicadoresAnatelSMP3Ericsson.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP3Ericsson.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP3 Ericsson iniciados");
   }
   
   private void iniciaContadoresIndicadoresSMP()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores IndicadoresSMP...");
      SelecaoIndicadorIndicadoresSMP.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosIndicadoresSMP = new SelecaoRecursosIndicadoresSMP();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosIndicadoresSMP.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorIndicadoresSMP = new SelecaoIndicadorIndicadoresSMP();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorIndicadoresSMP.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresIndicadoresSMP = m_SelecaoIndicadorIndicadoresSMP.fnGetListaIndicadores(TelaCDRView.REL_ANATELRESUMIDO);
      if (m_IndicadoresIndicadoresSMP != null && m_IndicadoresIndicadoresSMP.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresIndicadoresSMP = m_IndicadoresIndicadoresSMP.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP3Ericsson.length());

      System.out.println(new java.util.Date()+" - Contadores IndicadoresSMP iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3FE735170346
    */
   private void iniciaContadoresAnatelSMP5()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP5...");
      SelecaoIndicadorAnatelSMP5.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP5 = new SelecaoRecursosAnatelSMP5();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP5.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP5 = new SelecaoIndicadorAnatelSMP5();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP5.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP5 = m_SelecaoIndicadorAnatelSMP5.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP5);
      if (m_IndicadoresAnatelSMP5 != null && m_IndicadoresAnatelSMP5.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP5 = m_IndicadoresAnatelSMP5.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP5.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP5 iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 3FE735170346
    */
   private void iniciaContadoresAnatelSMP5Ericsson()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP5 Ericsson...");
      SelecaoIndicadorAnatelSMP5Ericsson.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP5Ericsson = new SelecaoRecursosAnatelSMP5Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP5Ericsson.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP5Ericsson = new SelecaoIndicadorAnatelSMP5Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP5Ericsson.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP5Ericsson = m_SelecaoIndicadorAnatelSMP5Ericsson.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP5_ERICSSON);
      if (m_IndicadoresAnatelSMP5Ericsson != null && m_IndicadoresAnatelSMP5Ericsson.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP5Ericsson = m_IndicadoresAnatelSMP5Ericsson.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP5Ericsson.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP5 Ericsson iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3FE7351802DA
    */
   private void iniciaContadoresAnatelSMP6()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP6...");
      SelecaoIndicadorAnatelSMP6.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP6 = new SelecaoRecursosAnatelSMP6();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP6.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP6 = new SelecaoIndicadorAnatelSMP6();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP6.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP6 = m_SelecaoIndicadorAnatelSMP6.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP6);
      if (m_IndicadoresAnatelSMP6 != null && m_IndicadoresAnatelSMP6.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP6 = m_IndicadoresAnatelSMP6.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP6.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP6 iniciados");
   }
   

   /**
    * @return void
    * @exception
    * @roseuid 3FE7351802DA
    */
   private void iniciaContadoresAnatelSMP8e9()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP8e9...");
      SelecaoIndicadorAnatelSMP8e9.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP8e9 = new SelecaoRecursosAnatelSMP8e9();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP8e9.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP8e9 = new SelecaoIndicadorAnatelSMP8e9();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP8e9.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP8e9 = m_SelecaoIndicadorAnatelSMP8e9.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP8e9);
      if (m_IndicadoresAnatelSMP8e9 != null && m_IndicadoresAnatelSMP8e9.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP8e9 = m_IndicadoresAnatelSMP8e9.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP8e9.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP8e9 iniciados");
   }
   
   
   /**
    * @return void
    * @exception
    * @roseuid 3FE7351802DA
    */
   private void iniciaContadoresAnatelSMP6Ericsson()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP6 Ericsson...");
      SelecaoIndicadorAnatelSMP6Ericsson.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP6Ericsson = new SelecaoRecursosAnatelSMP6Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP6Ericsson.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP6Ericsson = new SelecaoIndicadorAnatelSMP6Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP6Ericsson.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP6Ericsson = m_SelecaoIndicadorAnatelSMP6Ericsson.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP6_ERICSSON);
      if (m_IndicadoresAnatelSMP6Ericsson != null && m_IndicadoresAnatelSMP6Ericsson.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP6Ericsson = m_IndicadoresAnatelSMP6Ericsson.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP6Ericsson.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP6 Ericsson iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3FE7351902EF
    */
   private void iniciaContadoresAnatelSMP7()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP7...");
      SelecaoIndicadorAnatelSMP7.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP7 = new SelecaoRecursosAnatelSMP7();
      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP7.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP7 = new SelecaoIndicadorAnatelSMP7();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP7.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP7 = m_SelecaoIndicadorAnatelSMP7.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP7);
      if (m_IndicadoresAnatelSMP7 != null && m_IndicadoresAnatelSMP7.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP7 = m_IndicadoresAnatelSMP7.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP7.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP7 iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 3FE7351902EF
    */
   private void iniciaContadoresAnatelSMP7Ericsson()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel SMP7 Ericsson...");
      SelecaoIndicadorAnatelSMP7Ericsson.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelSMP7Ericsson = new SelecaoRecursosAnatelSMP7Ericsson();
      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelSMP7Ericsson.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelSMP7Ericsson = new SelecaoIndicadorAnatelSMP7Ericsson();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelSMP7Ericsson.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelSMP7Ericsson = m_SelecaoIndicadorAnatelSMP7Ericsson.fnGetListaIndicadores(TelaCDRView.REL_ANATELSMP7_ERICSSON);
      if (m_IndicadoresAnatelSMP7Ericsson != null && m_IndicadoresAnatelSMP7Ericsson.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelSMP7Ericsson = m_IndicadoresAnatelSMP7Ericsson.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelSMP7Ericsson.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel SMP7 Ericsson iniciados");
   }
   
   /**
    * @return void
    * @exception
    * @roseuid 40AE51140059
    */
   private void iniciaContadoresAnatelLDN()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Anatel LDN...");
      SelecaoIndicadorAnatelLDN.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnatelLDN = new SelecaoRecursosAnatelLDN();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnatelLDN.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnatelLDN = new SelecaoIndicadorAnatelLDN();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnatelLDN.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnatelLDN = m_SelecaoIndicadorAnatelLDN.fnGetListaIndicadores(TelaCDRView.REL_ANATELLDN);
      if (m_IndicadoresAnatelLDN != null && m_IndicadoresAnatelLDN.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnatelLDN = m_IndicadoresAnatelLDN.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnatelLDN.length());

      System.out.println(new java.util.Date()+" - Contadores Anatel LDN iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 4002FBE8036F
    */
   private void iniciaContadoresAnaliseCompletamento()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores Analise de Completamento...");
      SelecaoIndicadorAnaliseCompletamento.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();


      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosAnaliseCompletamento = new SelecaoRecursosAnaliseCompletamento();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosAnaliseCompletamento.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorAnaliseCompletamento = new SelecaoIndicadorAnaliseCompletamento();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorAnaliseCompletamento.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresAnaliseCompletamento = m_SelecaoIndicadorAnaliseCompletamento.fnGetListaIndicadores(TelaCDRView.REL_ANALISECOMPLETAMENTO);
      if (m_IndicadoresAnaliseCompletamento != null && m_IndicadoresAnaliseCompletamento.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresAnaliseCompletamento = m_IndicadoresAnaliseCompletamento.substring("Frequência;Aglutinado;".length(), m_IndicadoresAnaliseCompletamento.length());

      System.out.println(new java.util.Date()+" - Contadores Analise de Completamento iniciados");
   }

   /**
    * @return void
    * @exception
    * @roseuid 40AE511D019D
    */
   private void iniciaContadoresInterconexaoAudit()
   {
/*
 * System.out.println(new java.util.Date()+" - Iniciando contadores
 * Interconexao-Auditoria..."); SelecaoIndicadorInterconexaoAudit.m_iIndicadores =
 * NoUtil.getNo().getConexaoServUtil().getIndicadores();
 *  // Cria a lista de indicadores para este cliente (somente a primeira vez)
 * m_SelecaoRecursosInterconexaoAudit = new
 * SelecaoRecursosInterconexaoAudit(null);
 *  // Torna a lista ativa (por ser estática, este procedimento é necessário)
 * m_SelecaoRecursosInterconexaoAudit.fnAtiva();
 *  // Cria a lista de indicadores para este cliente (somente a primeira vez)
 * m_SelecaoIndicadorInterconexaoAudit = new
 * SelecaoIndicadorInterconexaoAudit(null);
 *  // Torna a lista ativa (por ser estática, este procedimento é necessário)
 * m_SelecaoIndicadorInterconexaoAudit.fnAtiva();
 *  // Recupera lista de indicadores possíveis m_IndicadoresInterconexaoAudit =
 * m_SelecaoIndicadorInterconexaoAudit.fnGetListaIndicadores(TelaCDRView.REL_ITX_AUDIT);
 * if (m_IndicadoresInterconexaoAudit != null &&
 * m_IndicadoresInterconexaoAudit.indexOf("Frequência;Aglutinado;") != -1)
 * m_IndicadoresInterconexaoAudit =
 * m_IndicadoresInterconexaoAudit.substring("Frequência;Aglutinado;".length(),
 * m_IndicadoresAnaliseCompletamento.length());
 * 
 * System.out.println(new java.util.Date()+" - Contadores
 * Interconexao-Auditoria iniciados");
 */
   }

   /**
    * @return void
    * @exception
    * @roseuid 40AE5130017C
    */
   private void iniciaContadoresInterconexaoForaRota()
   {
/*
 * System.out.println(new java.util.Date()+" - Iniciando contadores
 * Interconexao-Fora de Rota...");
 * SelecaoIndicadorInterconexaoForaRota.m_iIndicadores =
 * NoUtil.getNo().getConexaoServUtil().getIndicadores();
 *  // Cria a lista de indicadores para este cliente (somente a primeira vez)
 * m_SelecaoRecursosInterconexaoForaRota = new
 * SelecaoRecursosInterconexaoForaRota(null);
 *  // Torna a lista ativa (por ser estática, este procedimento é necessário)
 * m_SelecaoRecursosInterconexaoForaRota.fnAtiva();
 *  // Cria a lista de indicadores para este cliente (somente a primeira vez)
 * m_SelecaoIndicadorInterconexaoForaRota = new
 * SelecaoIndicadorInterconexaoForaRota(null);
 *  // Torna a lista ativa (por ser estática, este procedimento é necessário)
 * m_SelecaoIndicadorInterconexaoForaRota.fnAtiva();
 *  // Recupera lista de indicadores possíveis m_IndicadoresInterconexaoForaRota =
 * m_SelecaoIndicadorInterconexaoForaRota.fnGetListaIndicadores(null); if
 * (m_IndicadoresInterconexaoForaRota != null &&
 * m_IndicadoresInterconexaoForaRota.indexOf("Frequência;Aglutinado;") != -1)
 * m_IndicadoresInterconexaoForaRota =
 * m_IndicadoresInterconexaoForaRota.substring("Frequência;Aglutinado;".length(),
 * m_IndicadoresAnaliseCompletamento.length());
 * 
 * System.out.println(new java.util.Date()+" - Contadores
 * Interconexao-Fora de Rota iniciados");
 */
   }
  
   private void iniciaContadoresDesempenhoDeRede()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores desempenho de rede...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosDesempenhoDeRede = new SelecaoRecursosDesempenhoDeRede();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosDesempenhoDeRede.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorDesempenhoDeRede = new SelecaoIndicadorDesempenhoDeRede();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorDesempenhoDeRede.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresDesempenhoDeRede = m_SelecaoIndicadorDesempenhoDeRede.fnGetListaIndicadores(TelaCDRView.REL_DESEMPENHO_DE_REDE);

      if (m_IndicadoresDesempenhoDeRede.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresDesempenhoDeRede = m_IndicadoresDesempenhoDeRede.substring("Frequência;Aglutinado;".length(), m_IndicadoresDesempenhoDeRede.length());

      System.out.println(new java.util.Date()+" - Contadores desempenho de rede iniciados");
   }
   
   private void iniciaContadoresQoS()
   {
      System.out.println(new java.util.Date()+" - Iniciando contadores QoS...");
      SelecaoIndicadoresPortal.m_iIndicadores = NoUtil.getNo().getConexaoServUtil().getIndicadores();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoRecursosQoS = new SelecaoRecursosQoS();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoRecursosQoS.fnAtiva();

      // Cria a lista de indicadores para este cliente (somente a primeira vez)
      m_SelecaoIndicadorQoS = new SelecaoIndicadorQoS();

      // Torna a lista ativa (por ser estática, este procedimento é necessário)
      m_SelecaoIndicadorQoS.fnAtiva();

      // Recupera lista de indicadores possíveis
      m_IndicadoresQoS = m_SelecaoIndicadorQoS.fnGetListaIndicadores(TelaCDRView.REL_QOS);

      if (m_IndicadoresQoS.indexOf("Frequência;Aglutinado;") != -1)
         m_IndicadoresQoS = m_IndicadoresQoS.substring("Frequência;Aglutinado;".length(), m_IndicadoresQoS.length());

      System.out.println(new java.util.Date()+" - Contadores QoS iniciados");
   }
   

   /**
    * @param p_SessaoId
    * @return void
    * @exception Libera
    *                o usuário que tenta o logon caso o mesmo encontre-se na
    *                lista de usuários conectados. Limpa a lista interna do
    *                Portal e a do servidor. A busca é realizada pelo Id da
    *                Sessão.
    * @roseuid 3CB2E9BF0115
    */
   private void liberaUsuariosSessao(String p_SessaoId)
   {
      UsuarioDef usuario = null;
      
      usuario = NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(p_SessaoId);
      
      if (usuario != null)
      {
         System.out.println("Usuario esta logado. Limpando lista (Servidor)... "+p_SessaoId);
         usuario.getNo().getConexaoServUtil().logout(usuario, usuario.getIP(), usuario.getHost());
         System.out.println("                     Limpando lista (Portal)  ... "+p_SessaoId);

		 if (usuario != null && usuario.getIDSessao().equals(p_SessaoId) == true)
		 {
		 	/**
              * Necessario para garantir q apenas 1 thread modifique o
              * estado da colecao.
              */ 
		 	synchronized(usuario.getNo().getUsuarioLogados())
			{
		 	   usuario.getNo().getUsuarioLogados().remove(p_SessaoId);
			}	
		 }
      }
   }

   /**
    * @param p_IP
    * @return void
    * @exception Libera
    *                o usuário que tenta o logon caso o mesmo encontre-se na
    *                lista de usuários conectados. Limpa a lista interna do
    *                Portal e a do servidor. A busca é realizada pelo IP do
    *                cliente.
    * @roseuid 3CB2E9CD03D2
    */
   private void liberaUsuariosIP(String p_IP)
   {
       UsuarioDef usuario = null;
       String httpSessionId = null;
       
           No noTmp = NoUtil.getNo();
           try
           {
	           usuario = noTmp.getConexaoServUtil().usuarioLogadoIP(p_IP);
	           
	           if (usuario != null)
	           {
	              System.out.println("Usuario esta logado. Limpando lista (Servidor)... "+p_IP);
	              usuario.getNo().getConexaoServUtil().logout(usuario, usuario.getIP(), usuario.getHost());
	              System.out.println("                     Limpando lista (Portal)  ... "+p_IP);
	
	              httpSessionId = usuario.getIDSessao();
	              usuario = (UsuarioDef)NoUtil.getUsuarioLogado(httpSessionId);
	              
	     		  if (usuario != null && usuario.getIP().equals(p_IP) == true)
	     		  {
	     		 	/**
                       * Necessario para garantir q apenas 1 thread modifique o
                       * estado da colecao.
                       */ 
	     		 	synchronized(usuario.getNo().getUsuarioLogados())
	     			{
	     		 	   usuario.getNo().getUsuarioLogados().remove(httpSessionId);
	     			}	
	     		  }
	           }  
         }
         catch(COMM_FAILURE comFail)
   	     {
   	        System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
   	     }
   	     catch(BAD_OPERATION badOp)
   	     {
   	        System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
   	        badOp.printStackTrace();
   	     }
       
   }

   /**
    * @return void
    * @exception Libera
    *                todos os usuários cujos arquivos de lock foram apagados no
    *                servidor. Limpa a lista interna do Portal e a do servidor.
    * @roseuid 3CB2E9CE03B6
    */
   private void liberaUsuariosLock()
   {
      UsuarioDef usuario = null;
      StringTokenizer st = null;
      String httpSessionId = null;
      String usuariosLiberados = null;

      usuariosLiberados = NoUtil.getNo().getConexaoServUtil().liberaUsuarios();

	  if (usuariosLiberados != null)
      {
         Vector listaUsuarios = VetorUtil.String2Vetor(usuariosLiberados,';');

         for (int i = 0; i < listaUsuarios.size(); i++)
         {
            st            = new StringTokenizer((String)listaUsuarios.elementAt(i), "-");
            httpSessionId = (String)st.nextElement();
			usuario       = (UsuarioDef)NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(httpSessionId);

			if (usuario != null)
			{
				/**
                 * Necessario para garantir q apenas 1 thread modifique o
                 * estado da colecao.
                 */ 
		 		synchronized(usuario.getNo().getUsuarioLogados())
     			{
     		 	   usuario.getNo().getUsuarioLogados().remove(httpSessionId);
     			}
			}
         }
      }
   }

   /**
    * @param p_Request
    * @param p_Response
    * @return void
    * @exception IOException,ServletException
    *                Recupera as operações a serem executadas e inicia a classe
    *                correspondente para o tratamento da operação. É usada em
    *                chamadas diretas (sem formulário).
    * @roseuid 3BF07D740179
    */
   public void doGet(HttpServletRequest p_Request, HttpServletResponse  p_Response) throws IOException,ServletException
   {
      try
      {
         HttpSession Sessao;
         String Operacao = null;
         String OperacaoForm = null, HostRemoto = null,
         SessaoId = null, SessaoIP = null;

         /**
          * Seta o tipo de conteúdo da página
          */
         p_Response.setContentType("text/html");
         /**
          * Recupera o stream de saída do cliente
          */
         m_SaidaHtml = p_Response.getWriter();
         /**
          * Recupera a operação solicitada e a sessão do requisitor
          */
         OperacaoForm = p_Request.getParameter("operacao");
         Sessao       = p_Request.getSession();
         SessaoId     = Sessao.getId();
         SessaoIP     = p_Request.getRemoteAddr();

         /**
          * Imprime operacao e usuario para debug
          */
         boolean bAchou = false;
         UsuarioDef Usuario = null;
         String NomeUsr = null, NomeOperacao = null, DataStr = ""+new java.util.Date();

         NomeOperacao = OperacaoForm;
         while (NomeOperacao.length() < 20)
            NomeOperacao = NomeOperacao.concat(" ");
         
         No no = null;
         try
         {
             if(!NoUtil.noLocalSetado)
             {
            	 InetAddress localaddr = InetAddress.getLocalHost();
            	 NoUtil.setNoLocal(localaddr.getHostName(), p_Request.getServerPort()+"");
             }
             
             no = NoUtil.getNo();
             if (no.getConexaoServUtil().getM_iListaUsuarioWeb() == null)
             {
                 throw new COMM_FAILURE();
             }
             Usuario = (UsuarioDef) no.getConexaoServUtil().getUsuarioSessao(p_Request.getSession().getId());
         }
         catch(COMM_FAILURE comFail)
         {
             no.createConexao(DefsComum.s_ServUtil,
                              no.getConexaoServUtil().getModoConexao(), 
                              no.getHostName(), 
                              no.getConexaoServUtil().getNomeObjetoCorba(),
                              no.getConexaoServUtil().getPorta());
         }
         catch(org.omg.CORBA.OBJECT_NOT_EXIST corbaNExist)
         {
             no.createConexao(DefsComum.s_ServUtil,
                              no.getConexaoServUtil().getModoConexao(), 
                              no.getHostName(), 
                              no.getConexaoServUtil().getNomeObjetoCorba(),
                              no.getConexaoServUtil().getPorta());
             
             /**
              * Situacao em que o servutil foi reiniciado..
              * */
             if (no.getConexaoServUtil().getM_iUtil() == null)
             {
                 String Args[] = new String[4];
                 Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
                 Args[1] = "errogen.htm";
                 Args[2] = "desconexao.gif";
                 Args[3] = "<b>&nbsp;Aten&ccedil;&atilde;o!! Servidor de Util foi reiniciado! Favor fazer o login novamente.</b><br>"+
                           "&nbsp;&nbsp;&nbsp; <a href=\"http://"+NoUtil.getNoCentral().getHostName()+"\"> Login </a><br>";
                 m_Html = new HTMLUtil(m_SaidaHtml);
                 m_Html.enviaArquivo(Args);
                 return;
             }
         }
         /** Verificacao se a referencia do objeto corba do servidor de util esta valida apos as 
          * tentativas de reconexao.
          */
         if (no.getConexaoServUtil().getM_iUtil() == null)
         {
             String Args[] = new String[4];
             Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
             Args[1] = "errogen.htm";
             Args[2] = "desconexao.gif";
             Args[3] = "<b>&nbsp;Aten&ccedil;&atilde;o!! Servidor de Util esta fora do ar! Contate o administrador do sistema.</b><br>"+
                       "&nbsp;Verifique se o sistema foi colocado em manutenção.<br>";
             m_Html = new HTMLUtil(m_SaidaHtml);
             m_Html.enviaArquivo(Args);
             return;
         }
         
         if (Usuario != null)
         {
            NomeUsr = Usuario.getUsuario();
            while (NomeUsr.length() < 20)
               NomeUsr = NomeUsr.concat(" ");

            System.out.println(DataStr + " - Operacao: " +  NomeOperacao + " - Usr: " + NomeUsr + " - IP: " + Usuario.getIP());
         }
         else
         {
             if (!OperacaoForm.equalsIgnoreCase("logon"))
             {
                 Sessao.invalidate();
             }
             
             System.out.println(DataStr + " - Operacao: " +  NomeOperacao + " - Usr: Desconhecido         - IP: Desconhecido");
         }
         
         System.out.println(DataStr + " - SubOperacao: " +  p_Request.getParameter("suboperacao") + " - Usr: Desconhecido         - IP: Desconhecido");
         System.out.println(DataStr + " - Pagina: " +  p_Request.getParameter("pagina") + " - Usr: Desconhecido         - IP: Desconhecido");

         /**
          * Faz logout de usuário caso esteja preso ou já logado a partir dessa
          * sessão O logout é feito de 3 formas: - Pelo ID da sessão do usuários -
          * Pelo IP do usuários - Pela verificação de arquivos de lock no
          * servidor
          */
         if (OperacaoForm.equals("logon") == true)
         {
            manutencao();
            liberaUsuariosSessao(SessaoId);
            if(!(DefsComum.s_CLIENTE.compareToIgnoreCase("gvt") == 0)){
            	liberaUsuariosIP(SessaoIP);
            }
            liberaUsuariosLock();
         }

         //
         // * * *
         // Atencao: esse if deve ser comentado para testes via debug
         // * * *
         //
         if (OperacaoForm.equals("logon") == false &&
             OperacaoForm.equals("desconexaoPortal") == false &&
             OperacaoForm.equals("indicaLogon") == false &&
             NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(Sessao.getId()) == null)
         {
            System.out.println(new java.util.Date() + " - Portal - doGet()/doPost(): Usuario desconectado tentando realizar operação");
            String Args[] = new String[4];
            Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            Args[1] = "errogen.htm";
            Args[2] = "desconexao.gif";
            Args[3] = "<b>&nbsp;Você foi desconectado do CDRView Web pelo administrador!!</b><br>"+
                      "&nbsp;Verifique se o sistema foi colocado em manutenção.<br>";
            m_Html = new HTMLUtil(m_SaidaHtml);
            m_Html.enviaArquivo(Args);
            return;
         }

         Operacao = (String) m_Operacoes.get(OperacaoForm);
         if (Operacao != null)
         {
            //System.out.println (new java.util.Date() +" Portal -
            // doGet(): Operacao: "+Operacao.getOperacao());
            try
            {
               Class ClasseOperacao, ParameterTypes[];
               Method Metodo;
               Object OperacaoDin, Argumentos[];

               // Recupera a classe da operação
               ClasseOperacao = Class.forName("Portal.Operacoes."+Operacao);
               // Instancia o objeto da classe da operação
               OperacaoDin = ClasseOperacao.newInstance();

               // Seta HttpServletRequest da operação
               ParameterTypes = new Class[] {HttpServletRequest.class, HttpServletResponse.class};
               Argumentos = new Object[] {p_Request, p_Response};
               Metodo = ClasseOperacao.getMethod("setRequestResponse", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);
               
//               // Seta ConexUtil
//               ParameterTypes = new Class[] {CnxServUtil.class};
//               No no = (No) NoUtil.listaDeNos.get(0);
//               
//               Argumentos = new Object[] {NoUtil.getNo().getConexaoServUtil()};
//               Metodo = ClasseOperacao.getMethod("setConexUtil", ParameterTypes);
//               Metodo.invoke(OperacaoDin, Argumentos);
//
//               // Seta ConexCtrl
//               ParameterTypes = new Class[] {CnxServCtrl.class};
//               
//               Argumentos = new Object[] {m_ConexCtrl};
//               Metodo = ClasseOperacao.getMethod("setConexCtrl", ParameterTypes);
//               Metodo.invoke(OperacaoDin, Argumentos);
//
//               // Seta ConexAlr
//               ParameterTypes = new Class[] {CnxServAlr.class};
//               
//               Argumentos = new Object[] {m_ConexAlr};
//               Metodo = ClasseOperacao.getMethod("setConexAlr", ParameterTypes);
//               Metodo.invoke(OperacaoDin, Argumentos);

               // Seta a SelecaoIndicadoresDesempenho
               ParameterTypes = new Class[] {SelecaoRecursosDesempenho.class, SelecaoIndicadorDesempenho.class, String.class};
               Argumentos = new Object[] {m_SelecaoRecursosDesempenho, m_SelecaoIndicadorDesempenho, m_IndicadoresDesempenho};
               Metodo = ClasseOperacao.getMethod("setIndicadoresDesempenho", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);
               
               if (DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0)
               {
            	   ParameterTypes = new Class[] {SelecaoRecursosDespesa.class, SelecaoIndicadorDespesa.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosDespesa, m_SelecaoIndicadorDespesa, m_IndicadoresDespesa};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresDespesa", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);

            	   ParameterTypes = new Class[] {SelecaoRecursosITXReceita.class, SelecaoIndicadorITXReceita.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosITXReceita, m_SelecaoIndicadorITXReceita, m_IndicadoresITXReceita};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresITXReceita", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);

            	   ParameterTypes = new Class[] {SelecaoRecursosAgrupado.class, SelecaoIndicadorAgrupado.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosAgrupado, m_SelecaoIndicadorAgrupado, m_IndicadoresAgrupado};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresAgrupado", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);

            	   ParameterTypes = new Class[] {SelecaoRecursosGRE.class, SelecaoIndicadorGRE.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosGRE, m_SelecaoIndicadorGRE, m_IndicadoresGRE};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresGRE", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);

            	   ParameterTypes = new Class[] {SelecaoRecursosTrendAnalysis.class, SelecaoIndicadorTrendAnalysis.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosTrendAnalysis, m_SelecaoIndicadorTrendAnalysis, m_IndicadoresTrendAnalysis};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresTrendAnalysis", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);

            	   ParameterTypes = new Class[] {SelecaoRecursosDwGprs.class, SelecaoIndicadorDwGprs.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosDwGprs, m_SelecaoIndicadorDwGprs, m_IndicadoresDwGprs};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresDwGprs", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);

            	   ParameterTypes = new Class[] {SelecaoRecursosDwGeral.class, SelecaoIndicadorDwGeral.class, String.class};
            	   Argumentos = new Object[] {m_SelecaoRecursosDwGeral, m_SelecaoIndicadorDwGeral, m_IndicadoresDwGeral};
            	   Metodo = ClasseOperacao.getMethod("setIndicadoresDwGeral", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
               }
               if (DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("telemig")==0
            		   || DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("tim")==0)
               {
	               // Seta a SelecaoIndicadoresChamadas
	               ParameterTypes = new Class[] {SelecaoRecursosChamadas.class, SelecaoIndicadorChamadas.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosChamadas, m_SelecaoIndicadorChamadas, m_IndicadoresChamadas};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresChamadas", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               // Seta a SelecaoIndicadoresMinutosDeUso
	               ParameterTypes = new Class[] {SelecaoRecursosMinutosDeUso.class, SelecaoIndicadorMinutosDeUso.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosMinutosDeUso, m_SelecaoIndicadorMinutosDeUso, m_IndicadoresMinutosDeUso};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresMinutosDeUso", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //Seta a SelecaoIndicadoresMinutosDeUso
	               ParameterTypes = new Class[] {SelecaoRecursosDistFrequencia.class, SelecaoIndicadorDistFrequencia.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosDistFrequencia, m_SelecaoIndicadorDistFrequencia, m_IndicadoresDistFrequencia};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresDistFrequencia", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //Seta a SelecaoIndicadoresMinutosDeUso
	               ParameterTypes = new Class[] {SelecaoRecursosPerseveranca.class, SelecaoIndicadorPerseveranca.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosPerseveranca, m_SelecaoIndicadorPerseveranca, m_IndicadoresPerseveranca};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresPerseveranca", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //seta a selecaoIndicadoresRelatoriosNovosParaFraude
	               ParameterTypes = new Class[] {SelecaoIndicadorChamadasLongaDuracao.class, String.class};
	               Argumentos = new Object[] {m_SelecaoIndicadorChamadasLongaDuracao, m_IndicadoresChamadasLongaDuracao};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresChamadasLongaDuracao", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //seta a selecaoIndicadoresRelatoriosNovosParaFraude
	               ParameterTypes = new Class[] {SelecaoRecursosDestinosEspecificos.class, SelecaoIndicadorDestinosEspecificos.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosDestinosEspecificos, m_SelecaoIndicadorDestinosEspecificos, m_IndicadoresDestinosEspecificos};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresDestinosEspecificos", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //seta a selecaoIndicadoresRelatoriosNovosParaFraude
	               ParameterTypes = new Class[] {SelecaoRecursosDestinosComuns.class, SelecaoIndicadorDestinosComuns.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosDestinosComuns, m_SelecaoIndicadorDestinosComuns, m_IndicadoresDestinosComuns};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresDestinosComuns", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //seta a selecaoIndicadoresRelatoriosNovosParaFraude
	               ParameterTypes = new Class[] {SelecaoRecursosPesquisaPorERB.class, SelecaoIndicadorPesquisaPorERB.class, String.class};
	               Argumentos = new Object[] {m_SelecaoRecursosPesquisaPorERB, m_SelecaoIndicadorPesquisaPorERB, m_IndicadoresPesquisaPorERB};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresPesquisaPorERB", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //seta a selecaoIndicadoresRelatoriosNovosParaFraude
	               ParameterTypes = new Class[] {SelecaoIndicadorPrefixosDeRisco.class, String.class};
	               Argumentos = new Object[] {m_SelecaoIndicadorPrefixosDeRisco, m_IndicadoresPrefixosDeRisco};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresPrefixosDeRisco", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);

	               
	               //seta a selecaoIndicadoresRelatoriosNovosParaFraude
	               ParameterTypes = new Class[] {SelecaoIndicadorPesquisaIMEI.class, String.class};
	               Argumentos = new Object[] {m_SelecaoIndicadorPesquisaIMEI, m_IndicadoresPesquisaIMEI};
	               Metodo = ClasseOperacao.getMethod("setIndicadoresPesquisaIMEI", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
               }
               if(DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0)
               {
            	   ParameterTypes = new Class[] {SelecaoRecursosDesempenhoDeRede.class, SelecaoIndicadorDesempenhoDeRede.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosDesempenhoDeRede, m_SelecaoIndicadorDesempenhoDeRede, m_IndicadoresDesempenhoDeRede};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresDesempenhoDeRede", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);
                   
                   ParameterTypes = new Class[] {SelecaoRecursosQoS.class, SelecaoIndicadorQoS.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosQoS, m_SelecaoIndicadorQoS, m_IndicadoresQoS};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresQoS", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);
               }
/*
 * if (DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0) { // Seta
 * a SelecaoIndicadoresInterconexaoAudit ParameterTypes = new Class[]
 * {SelecaoRecursosInterconexaoAudit.class,
 * SelecaoIndicadorInterconexaoAudit.class, String.class}; Argumentos = new
 * Object[] {m_SelecaoRecursosInterconexaoAudit,
 * m_SelecaoIndicadorInterconexaoAudit, m_IndicadoresInterconexaoAudit}; Metodo =
 * ClasseOperacao.getMethod("setIndicadoresInterconexaoAudit", ParameterTypes);
 * Metodo.invoke(OperacaoDin, Argumentos);
 *  // Seta a SelecaoIndicadoresInterconexaoForaRota ParameterTypes = new
 * Class[] {SelecaoRecursosInterconexaoForaRota.class,
 * SelecaoIndicadorInterconexaoForaRota.class, String.class}; Argumentos = new
 * Object[] {m_SelecaoRecursosInterconexaoForaRota,
 * m_SelecaoIndicadorInterconexaoForaRota, m_IndicadoresInterconexaoForaRota};
 * Metodo = ClasseOperacao.getMethod("setIndicadoresInterconexaoForaRota",
 * ParameterTypes); Metodo.invoke(OperacaoDin, Argumentos); }
 */
               if (DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 ||
            	  DefsComum.s_CLIENTE.compareToIgnoreCase("sercomtel") == 0 ||
            	  DefsComum.s_CLIENTE.compareToIgnoreCase("amazonia_celular") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("timsul") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("vivo") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("telemig") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("ctbc") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("gvt") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("nextel") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("embratel")==0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("acme")==0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("oi")==0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("tim")==0)
               {
                  // Seta a SelecaoIndicadoresMatraf
                  ParameterTypes = new Class[] {SelecaoRecursosMatraf.class, SelecaoIndicadorMatraf.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosMatraf, m_SelecaoIndicadorMatraf, m_IndicadoresMatraf};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresMatraf", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta a SelecaoIndicadoresAnatelSMP3
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP3.class, SelecaoIndicadorAnatelSMP3.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP3, m_SelecaoIndicadorAnatelSMP3, m_IndicadoresAnatelSMP3};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP3", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);



//                   Seta a SelecaoIndicadoresAnatelSMP3e4
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP3e4.class, SelecaoIndicadorAnatelSMP3e4.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP3e4, m_SelecaoIndicadorAnatelSMP3e4, m_IndicadoresAnatelSMP3e4};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP3e4", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta a SelecaoIndicadoresAnatelSMP5
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP5.class, SelecaoIndicadorAnatelSMP5.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP5, m_SelecaoIndicadorAnatelSMP5, m_IndicadoresAnatelSMP5};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP5", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta a SelecaoIndicadoresAnatelSMP6
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP6.class, SelecaoIndicadorAnatelSMP6.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP6, m_SelecaoIndicadorAnatelSMP6, m_IndicadoresAnatelSMP6};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP6", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta a SelecaoIndicadoresAnatelSMP7
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP7.class, SelecaoIndicadorAnatelSMP7.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP7, m_SelecaoIndicadorAnatelSMP7, m_IndicadoresAnatelSMP7};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP7", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

//                   Seta a SelecaoIndicadoresAnatelSMP8e9
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP8e9.class, SelecaoIndicadorAnatelSMP8e9.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP8e9, m_SelecaoIndicadorAnatelSMP8e9, m_IndicadoresAnatelSMP8e9};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP8e9", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta a SelecaoIndicadoresAnatelLDN
                  ParameterTypes = new Class[] {SelecaoRecursosAnatelLDN.class, SelecaoIndicadorAnatelLDN.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnatelLDN, m_SelecaoIndicadorAnatelLDN, m_IndicadoresAnatelLDN};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelLDN", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta a SelecaoIndicadoresAnaliseCompletamento
                  ParameterTypes = new Class[] {SelecaoRecursosAnaliseCompletamento.class, SelecaoIndicadorAnaliseCompletamento.class, String.class};
                  Argumentos = new Object[] {m_SelecaoRecursosAnaliseCompletamento, m_SelecaoIndicadorAnaliseCompletamento, m_IndicadoresAnaliseCompletamento};
                  Metodo = ClasseOperacao.getMethod("setIndicadoresAnaliseCompletamento", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);
               }
               if(DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom")==0 || DefsComum.s_CLIENTE.compareToIgnoreCase("oi")==0 ){
            	   // Seta a SelecaoIndicadoresAnatelSMP3
                   ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP3Ericsson.class, SelecaoIndicadorAnatelSMP3Ericsson.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP3Ericsson, m_SelecaoIndicadorAnatelSMP3Ericsson, m_IndicadoresAnatelSMP3Ericsson};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP3Ericsson", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);

                   // Seta a SelecaoIndicadoresAnatelSMP5
                   ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP5Ericsson.class, SelecaoIndicadorAnatelSMP5Ericsson.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP5Ericsson, m_SelecaoIndicadorAnatelSMP5Ericsson, m_IndicadoresAnatelSMP5Ericsson};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP5Ericsson", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);

                   // Seta a SelecaoIndicadoresAnatelSMP6
                   ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP6Ericsson.class, SelecaoIndicadorAnatelSMP6Ericsson.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP6Ericsson, m_SelecaoIndicadorAnatelSMP6Ericsson, m_IndicadoresAnatelSMP6Ericsson};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP6Ericsson", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);

                   // Seta a SelecaoIndicadoresAnatelSMP7
                   ParameterTypes = new Class[] {SelecaoRecursosAnatelSMP7Ericsson.class, SelecaoIndicadorAnatelSMP7Ericsson.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosAnatelSMP7Ericsson, m_SelecaoIndicadorAnatelSMP7Ericsson, m_IndicadoresAnatelSMP7Ericsson};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresAnatelSMP7Ericsson", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);
                   
                   // Seta a SelecaoIndicadoresIndicadoresSMP
                   ParameterTypes = new Class[] {SelecaoRecursosIndicadoresSMP.class, SelecaoIndicadorIndicadoresSMP.class, String.class};
                   Argumentos = new Object[] {m_SelecaoRecursosIndicadoresSMP, m_SelecaoIndicadorIndicadoresSMP, m_IndicadoresIndicadoresSMP};
                   Metodo = ClasseOperacao.getMethod("setIndicadoresIndicadoresSMP", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);
               }
               
               // Seta a SelecaoIndicadoresDetalhe
               ParameterTypes = new Class[] {SelecaoIndicadorDetalheChamada.class, String.class};
               Argumentos = new Object[] {m_SelecaoIndicadorDetalhe, m_IndicadoresDetalhe};
               Metodo = ClasseOperacao.getMethod("setIndicadoresDetalhe", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);
               
//             Seta a SelecaoIndicadoresAuditoria
               ParameterTypes = new Class[] {SelecaoIndicadorAuditoriaChamadas.class, String.class};
               Argumentos = new Object[] {m_SelecaoIndicadorAuditoria, m_IndicadoresAuditoria};
               Metodo = ClasseOperacao.getMethod("setIndicadoresAuditoria", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               // Seta a SelecaoIndicadoresPesquisa
               ParameterTypes = new Class[] {SelecaoRecursosPesquisaCodigo.class, SelecaoIndicadorPesquisaCodigo.class, String.class};
               Argumentos = new Object[] {m_SelecaoRecursosPesquisa, m_SelecaoIndicadorPesquisa, m_IndicadoresPesquisa};
               Metodo = ClasseOperacao.getMethod("setIndicadoresPesquisa", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               // Chama o método "iniciaOperacao" do objeto operação
               ParameterTypes = new Class[] {String.class}; // Não tem
                                                            // parâmetros
               Argumentos = new Object[] {"$ARG;"};
               Metodo = ClasseOperacao.getMethod("iniciaOperacao", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               // Seta o chache de séries históricas
               ParameterTypes = new Class[] {Map.class};
               Argumentos = new Object[] {m_MapHistoricos};
               Metodo = ClasseOperacao.getMethod("setMapHistoricos", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               // Seta o chache de históricos de relatórios de tráfego
               ParameterTypes = new Class[] {Map.class};
               Argumentos = new Object[] {m_MapHistoricosTrafego};
               Metodo = ClasseOperacao.getMethod("setMapHistoricosTrafego", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               // Seta o chache de agendas de relatórios de desempenho
               ParameterTypes = new Class[] {Map.class};
               Argumentos = new Object[] {m_MapAgendasDesempenho};
               Metodo = ClasseOperacao.getMethod("setMapAgendasDesempenho", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);
               
               if(DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0){
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasDespesa};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasDespesa", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
            	   
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasITXReceita};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasITXReceita", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
            	   
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasTrendAnalysis};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasTrendAnalysis", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
            	   
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasGRE};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasGRE", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
            	   
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasAgrupado};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasAgrupado", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
            	   
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasDWGERAL};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasDWGERAL", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
            	   
            	   ParameterTypes = new Class[] {Map.class};
            	   Argumentos = new Object[] {m_MapAgendasDWGPRS};
            	   Metodo = ClasseOperacao.getMethod("setMapAgendasDWGPRS", ParameterTypes);
            	   Metodo.invoke(OperacaoDin, Argumentos);
               }
               if(DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("telemig")==0
            		   || DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0 || DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0)
               {
            	   // Seta o chache de agendas de relatórios de desempenho
            	   ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasChamadas};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasChamadas", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               // Seta o chache de agendas de relatórios de desempenho
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasMinutosDeUso};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasMinutosDeUso", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasDistFrequencia};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasDistFrequencia", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasPerseveranca};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasPerseveranca", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               //---------relatorios novos para claro perfil fraude
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasPesquisaIMEI};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasPesquisaIMEI", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasChamadasLongaDuracao};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasChamadasLongaDuracao", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasDestinosEspecificos};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasDestinosEspecificos", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasDestinosComuns};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasDestinosComuns", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasPesquisaPorERB};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasPesquisaPorERB", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
	               ParameterTypes = new Class[] {Map.class};
	               Argumentos = new Object[] {m_MapAgendasPrefixosDeRisco};
	               Metodo = ClasseOperacao.getMethod("setMapAgendasPrefixosDeRisco", ParameterTypes);
	               Metodo.invoke(OperacaoDin, Argumentos);
	               
               }

               // Seta o chache de agendas de relatórios de detalhe de chamadas
               ParameterTypes = new Class[] {Map.class};
               Argumentos = new Object[] {m_MapAgendasDetalheChamadas};
               Metodo = ClasseOperacao.getMethod("setMapAgendasDetalheChamadas", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);
               
//             Seta o chache de agendas de relatórios de Auditoria de chamadas
               ParameterTypes = new Class[] {Map.class};
               Argumentos = new Object[] {m_MapAgendasAuditoriaChamadas};
               Metodo = ClasseOperacao.getMethod("setMapAgendasAuditoriaChamadas", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               // Seta o chache de agendas de relatórios de pesquisa
               ParameterTypes = new Class[] {Map.class};
               Argumentos = new Object[] {m_MapAgendasPesquisa};
               Metodo = ClasseOperacao.getMethod("setMapAgendasPesquisa", ParameterTypes);
               Metodo.invoke(OperacaoDin, Argumentos);

               if (DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0 ||
            	  DefsComum.s_CLIENTE.compareToIgnoreCase("sercomtel") == 0 ||
            	  DefsComum.s_CLIENTE.compareToIgnoreCase("amazonia_celular") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("timsul") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("vivo") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("ctbc") == 0 ||
		      	  DefsComum.s_CLIENTE.compareToIgnoreCase("telemig") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("gvt") == 0||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("nextel") == 0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("embratel")==0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("acme")==0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("oi")==0 ||
		          DefsComum.s_CLIENTE.compareToIgnoreCase("tim")==0)
               {
                  // Seta o chache de agendas de relatórios de matraf
                  ParameterTypes = new Class[] {Map.class};
                  Argumentos = new Object[] {m_MapAgendasMatraf};
                  Metodo = ClasseOperacao.getMethod("setMapAgendasMatraf", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta o chache de SMP3
                  ParameterTypes = new Class[] {Map.class};
                  Argumentos = new Object[] {m_MapAgendasAnatelSMP};
                  Metodo = ClasseOperacao.getMethod("setMapAgendasAnatelSMP", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);

                  // Seta o chache de Analise de Completamento
                  ParameterTypes = new Class[] {Map.class};
                  Argumentos = new Object[] {m_MapAgendasAnaliseCompletamento};
                  Metodo = ClasseOperacao.getMethod("setMapAgendasAnaliseCompletamento", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);
                  
                  // Seta o chache de IndicadoresSMP
                  ParameterTypes = new Class[] {Map.class};
                  Argumentos = new Object[] {m_MapAgendasIndicadoresSMP};
                  Metodo = ClasseOperacao.getMethod("setMapAgendasIndicadoresSMP", ParameterTypes);
                  Metodo.invoke(OperacaoDin, Argumentos);
               }
               if(DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0)
               {
            	   ParameterTypes = new Class[] {Map.class};
                   Argumentos = new Object[] {m_MapAgendasDesempenhoDeRede};
                   Metodo = ClasseOperacao.getMethod("setMapAgendasDesempenhoDeRede", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);
                   
                   ParameterTypes = new Class[] {Map.class};
                   Argumentos = new Object[] {m_MapAgendasQoS};
                   Metodo = ClasseOperacao.getMethod("setMapAgendasQoS", ParameterTypes);
                   Metodo.invoke(OperacaoDin, Argumentos);
               }
               //System.out.println("QTD USUARIOS: "+m_Usuarios.size());
            }
            catch (NoSuchMethodException MExc)
            {
               System.out.println ("Portal - doGet(): Metodo nao encontrado");
               System.out.println ("Portal - doGet(): "+MExc);
            }
            catch (Exception Exc)
            {
               System.out.println ("Portal - doGet(): Erro ao instanciar a classe: "+ Operacao);
               System.out.println ("Portal - doGet(): "+Exc);
               Exc.printStackTrace();
            }
         }
         else
         {
            System.out.println ("Operacao nao encontrada: "+OperacaoForm);
            String Args[] = {NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML(),
                             "errogen.htm",
                             "erro.gif",
                             "Opera&ccedil;&atilde;o <b>"+OperacaoForm+"</b> n&atilde;o instalada.<br>Contacte o administrador do sistema!<br>"};
            m_Html = new HTMLUtil(m_SaidaHtml);
            m_Html.enviaArquivo(Args);
         }
      }
      catch (Exception Exc)
      {
         System.out.println("***********************");
         System.out.println(new java.util.Date() + " - doGet() gerou excecao");
         System.out.println(new java.util.Date() + " - " + Exc.getMessage());
         Exc.printStackTrace();
         System.out.println("***********************");
      }
   }

   /**
    * @param p_Request
    * @param p_Response
    * @return void
    * @exception IOException,ServletException
    *                Recupera as operações a serem executadas e inicia a classe
    *                correspondente para o tratamento da operação. É usada em
    *                chamadas via formulário.
    * @roseuid 3C0A314802E6
    */
   public void doPost(HttpServletRequest p_Request, HttpServletResponse  p_Response) throws IOException,ServletException
   {
      try
      {
         doGet(p_Request, p_Response);
      }
      catch (Exception Exc)
      {
         System.out.println("***********************");
         System.out.println(new java.util.Date() + " - doPost() gerou excecao");
         System.out.println(new java.util.Date() + " - " + Exc.getMessage());
         Exc.printStackTrace();
         System.out.println("***********************");
      }
   }

   /**
    * @return void
    * @exception Percorre
    *                a lista de usuários conectados e invalida aqueles que
    *                mantiverem a sessão aberta sem atividade por mais que o
    *                TIMEOUT especificado.
    * @roseuid 3DB052F101F2
    */
   private void manutencao()
   {
      int i = 1, QtdUsr;
      HttpSession Sessao;
      long UltimoAcesso=0, Agora, Dif;
      UsuarioDef Usuario;
      String Data, NomeUsr = null;
      
      /**
       * Necessario para garantir q apenas 1 thread modifique o estado da
       * colecao.
       */ 
	 	synchronized(NoUtil.getNo().getUsuarioLogados())
		{
	 		Iterator ItUsuarios = NoUtil.getNo().getUsuarioLogados().values().iterator();
	 		QtdUsr = NoUtil.getNo().getUsuarioLogados().size();
		    System.out.println(new java.util.Date() + " - Portal - Manutencao() - QtdUsr: " + QtdUsr);
		    
		    while (ItUsuarios.hasNext())
		    {
		         //System.out.println("I = "+i + " - Qtd Usr: " + QtdUsr);
		         Usuario = (UsuarioDef) ItUsuarios.next();
		         NomeUsr = Usuario.getUsuario();
		         while (NomeUsr.length() < 20)
		            NomeUsr = NomeUsr.concat(" ");
		         System.out.println("                      Usuario["+i+"]: "+ NomeUsr + " - Desde: " + Usuario.getDataAcessoStr());
		         i++;
		     }
		}
   }

   /**
    * @param p_Sessao
    * @param p_Nome
    * @param p_Usuario
    * @return boolean
    * @exception Realiza
    *                o logout de um usuário no servidor remoto e no servlet.
    * @roseuid 3DB451E1004B
    */
   private boolean logout(HttpSession p_Sessao, String p_Nome, UsuarioDef p_Usuario)
   {
      if (p_Usuario != null)
      {
         try
         {
            p_Usuario.getSessaoHTTP().invalidate();
            System.out.println (new java.util.Date()+" - Removendo da lista do servlet: " + p_Usuario.getNo().getUsuarioLogados().remove(p_Usuario.getSessaoHTTP().getId()));
            p_Usuario.getNo().getConexaoServUtil().logout(p_Usuario, p_Usuario.getIP(), p_Usuario.getHost());
            return true;
         }
         catch (Exception Exc)
         {
            System.out.println(new java.util.Date()+" - Erro ao realizar o logout do usuario: " + p_Usuario.getUsuario());
            Exc.printStackTrace();
            return false;
         }
      }
      return false;
   }
}
