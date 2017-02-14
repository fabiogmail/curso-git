//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApresentaRelAgendadoAnatelSMP.java

package Portal.Operacoes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import CDRView2.Cliente;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Agenda;
import Portal.Utils.AgendaAnatelSMP;
import Portal.Utils.Arquivo;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

public class OpApresentaRelAgendadoAnatelSMP extends OperacaoAbs 
{
	/**
	 * Flag para verificar se Thread que realiza a contrução do arquivo de download está executando. Garantir que irá executar apenas uma por vez.
	 */
	private static boolean estadoThread = false;
	
   private String m_SubOperacao;
   private String m_NomeRelatorio;
   private String m_DataGeracao;
   private String m_Perfil;
   private String m_TipoRelatorio;
   private String m_IdRelatorio;
   private String m_NomeArquivoRelatorio;
   private String m_KeyCache;   
   private final String m_TODOS = "Todos";   

   private static final String m_COR_HEADER="#000066";         // Azul escuro
   private static final String m_COR_HEADER_APR="#33CCFF";     // Azul claro
   private static final String m_COR_PERIODOS="#000066";       // Azul escuro   
   private static final String m_COR_FONTEHEADER="#FFFFFF";    // Branco
   private static final String m_COR_LINHASIM="#FFFFFF";       // Branco
   private static final String m_COR_LINHASIM2="#C0C0C0";      // Cinza escuro   
   private static final String m_COR_LINHASIM3="#D9D9D9";      // Cinza entre o Cinza escuro e o Cinza claro
   private static final String m_COR_LINHANAO="#F0F0F";        // Cinza claro
   public static final String m_FONTE="\"verdana\" size=\"1\"";
   private boolean m_ApagarRelatorio;
   

   private int m_TipoSMP = 0;
   private boolean m_ListaNova[] = null;
   
   private static final String m_TEXTOSMP[][] = 
   {
      { // SMP3 
         "Chamadas atendidas pelo CA",
         "Tentativas de originar chamadas para o CA, contadas a partir da aloca&ccedil;&atilde;o do VC",
         "Fator de pondera&ccedil;&atilde;o",
         "Percentual "
      },
      { // SMP5
         "Chamadas originadas completadas",
         "Chamadas OKT",
         "Tentativas de originar chamadas contadas a partir da aloca&ccedil;&atilde;o do VC",
         "Chamadas reencaminhadas para o correio de voz, com atendimento",
         "Chamadas reencaminhadas para o correio de voz, sem atendimento po NR, LO, FAS ou DSL",
         "Chamadas interceptadas com orienta&ccedil;&atilde;o ao usu&aacute;rio",
         "Fator de pondera&ccedil;&atilde;o",
         "Percentual "         
      },
      { // SMP6
         "Chamadas originadas estabelecidas em at&eacute; 10s",
         "Chamadas estabelecidas",
         //"Chamadas reencaminhadas para o correio de voz, com atendimento",
         "Chamadas estabelecidas em tempo superior a 35s",
         "Fator de pondera&ccedil;&atilde;o",
         "Percentual "
      },
      { // SMP7
         "Chamadas interrompidas por queda de liga&ccedil;&atilde;o",
         "Chamadas completadas",
         "Fator de pondera&ccedil;&atilde;o",
         "Percentual "
      },
      { // LDN
         "Chamadas completadas",
         "Tentativas de originar chamadas",
         "Chamadas n&atilde;o completadas por congestionamento",
         "Chamadas para n&uacute;meros inexistentes",
         "Chamadas n&atilde;o completadas sem faultcode",
         "Fator de pondera&ccedil;&atilde;o",         
         "Percentual "
      },
      { // SMP3 Ericsson
          "Chamadas atendidas pelo CA",
          "Tentativas de originar chamadas para o CA, contadas a partir da aloca&ccedil;&atilde;o do VC",
          "Fator de pondera&ccedil;&atilde;o",
          "Percentual "
       },
       { // SMP5 Ericsson
          "Chamadas originadas completadas",
          "Chamadas OKT",
          "Tentativas de originar chamadas contadas a partir da aloca&ccedil;&atilde;o do VC",
          "Chamadas reencaminhadas para o correio de voz, com atendimento",
          "Chamadas reencaminhadas para o correio de voz, sem atendimento po NR, LO, FAS ou DSL",
          "Chamadas interceptadas com orienta&ccedil;&atilde;o ao usu&aacute;rio",
          "Fator de pondera&ccedil;&atilde;o",
          "Percentual "         
       },
       { // SMP6 Ericsson
          "Chamadas originadas estabelecidas em at&eacute; 10s",
          "Chamadas estabelecidas",
          //"Chamadas reencaminhadas para o correio de voz, com atendimento",
          "Chamadas estabelecidas em tempo superior a 35s",
          "Fator de pondera&ccedil;&atilde;o",
          "Percentual "
       },
       { // SMP7 Ericsson
          "Chamadas interrompidas por queda de liga&ccedil;&atilde;o",
          "Chamadas completadas",
          "Fator de pondera&ccedil;&atilde;o",
          "Percentual "
       }
   };

   private static final String m_TEXTOSMP_Download[][] = 
   {  
      { // SMP3
         "Chamadas atendidas pelo CASC",
         "Tentativas de originar chamadas para o CASC, contadas a partir da alocacao do VC",
         "Fator de ponderacao",
         "Percentual "
      },
      { // SMP5
        "Chamadas originadas completadas",
        "Chamadas OKT",
        "Tentativas de originar chamadas contadas a partir da alocacao do VC",
        "Chamadas reencaminhadas para o correio de voz, com atendimento",
        "Chamadas reencaminhadas para o correio de voz, sem atendimento po NR, LO, FAS ou DSL",
        "Chamadas interceptadas com orientacao ao usuario",
        "Fator de ponderacao",
        "Percentual "   
      },
      { // SMP6
         "Chamadas originadas estabelecidas em ate 10s",
         "Chamadas estabelecidas",
         //"Chamadas reencaminhadas para o correio de voz, com atendimento",
         "Chamadas estabelecidas em tempo superior a 35s",
         "Fator de ponderacao",
         "Percentual "
      },
      { // SMP7
         "Chamadas interrompidas por queda de ligacao",
         "Chamadas completadas",
         "Fator de ponderacao",
         "Percentual "
      },
      { // LDN
         "Chamadas completadas",
         "Tentativas de originar chamadas",
         "Chamadas nao completadas por congestionamento",
         "Chamadas para numeros inexistentes",
         "Chamadas nao completadas sem faultcode",
         "Fator de ponderacao",         
         "Percentual "
      },
      { // SMP3 Ericsson
          "Chamadas atendidas pelo CASC",
          "Tentativas de originar chamadas para o CASC, contadas a partir da alocacao do VC",
          "Fator de ponderacao",
          "Percentual "
       },
       { // SMP5 Ericsson
         "Chamadas originadas completadas",
         "Chamadas OKT",
         "Tentativas de originar chamadas contadas a partir da alocacao do VC",
         "Chamadas reencaminhadas para o correio de voz, com atendimento",
         "Chamadas reencaminhadas para o correio de voz, sem atendimento po NR, LO, FAS ou DSL",
         "Chamadas interceptadas com orientacao ao usuario",
         "Fator de ponderacao",
         "Percentual "   
       },
       { // SMP6 Ericsson
          "Chamadas originadas estabelecidas em ate 10s",
          "Chamadas estabelecidas",
          //"Chamadas reencaminhadas para o correio de voz, com atendimento",
          "Chamadas estabelecidas em tempo superior a 35s",
          "Fator de ponderacao",
          "Percentual "
       },
       { // SMP7 Ericsson
          "Chamadas interrompidas por queda de ligacao",
          "Chamadas completadas",
          "Fator de ponderacao",
          "Percentual "
       }
   };   

   //
   // Configuracoes da vizualizacao dos indicadores para cada tipo de relatorio
   // Forma: [TipoSMP][TipoApresentacao][Indicadores]
   //
   private static final String m_Indicadores_Rel[][][] = 
   {
      /**
       * SMP3
       */
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham OK","Cham", "F. Pond.", "SMP3"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham OK", "Cham", "F. Pond.", "SMP3"
         }
      },
      /**
       * SMP5
       */ 
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
//            "Cham OK", "Cham", "OK VM", "NR VM", "F. Pond.", "SMP5"
        		 "Cham OK", "Cham OKT", "Cham", "OK VM", "NR VM", "Cham ITCP", "F. Pond.", "SMP4"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
//            "Cham OK", "Cham", "OK VM", "NR_VM", "F. Pond.", "SMP5"
            "Cham OK", "Cham OKT", "Cham", "OK VM", "NR VM", "Cham ITCP", "F. Pond.", "SMP4"
         }
      },
      /**
       * SMP6
       */ 
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham Est", "Cham Est Menor 10s", "Cham Est Maior 35s", "F. Pond.", "SMP6"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham Est Menor 10s", "Cham Est", /*"OK VM",*/ "Cham Est Maior 35s", "F. Pond.", "SMP6"
         }
      },
      /**
       * SMP7
       */
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham Drop", "Cham OK", "F. Pond.", "SMP7"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham Drop", "Cham OK", "F. Pond.", "SMP7"
         }
      },
      /**
       * LDN
       */
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham OK", "Cham", "CO", "INEX", "NOKSEMFCODE", "F. Pond.", "LDN"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham OK", "Cham", "CO", "INEX", "NOKSEMFCODE", "F. Pond.", "LDN"            
         }
      },
      /**
       * SMP3 Ericsson
       */
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham OK","Cham", "F. Pond.", "SMP3"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham OK", "Cham", "F. Pond.", "SMP3"
         }
      },
      /**
       * SMP5 Ericsson
       */ 
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
//            "Cham OK", "Cham", "OK VM", "NR VM", "F. Pond.", "SMP5"
            "Cham OK", "Cham OKT", "Cham", "OK VM", "NR_VM", "Cham ITCP", "F. Pond.", "SMP5"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
//            "Cham OK", "Cham", "OK VM", "NR_VM", "F. Pond.", "SMP5"
            "Cham OK", "Cham OKT", "Cham", "OK VM", "NR_VM", "Cham ITCP", "F. Pond.", "SMP5"
         }
      },
      /**
       * SMP6 Ericsson
       */ 
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham Est", "Cham Est Menor 10s", "Cham Est Maior 35s", "F. Pond.", "SMP6"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham Est Menor 10s", "Cham Est", /*"OK VM",*/ "Cham Est Maior 35s", "F. Pond.", "SMP6"
         }
      },
      /**
       * SMP7 Ericsson
       */
      {
         // Tipo de Apresentacao 1
         {
            // Indicadores
            "Cham Drop", "Cham OK", "F. Pond.", "SMP7"
         },
         // Tipo de Apresentacao 2
         {
            // Indicadores
            "Cham Drop", "Cham OK", "F. Pond.", "SMP7"
         }
      }
   };

   //
   // Configuracoes dos nomes dos Numeradores e Denominadores dos indicadores Anatel
   // Forma: [TipoSMP][Nome do Contador]
   //
   private static final String m_Num_Dem[][] = 
   {
      /**
       * SMP3
       */       
      {"Cham OK", "Cham"},
         
      /**
       * SMP5
       */       
      {"Cham OK", "Cham"},
         
      /**
       * SMP6
       */       
      {"Cham Atd < 10s", "Cham Est"},

      /**
       * SMP7
       */       
      {"Cham Drop", "Cham"},

      /**
       * LDN
       */       
      {"Cham OK", "Cham"},
      /**
       * SMP3 Ericsson
       */       
      {"Cham OK", "Cham"},
         
      /**
       * SMP5 Ericsson
       */       
      {"Cham OK", "Cham"},
         
      /**
       * SMP6 Ericsson
       */       
      {"Cham Atd < 10s", "Cham Est"},

      /**
       * SMP7 Ericsson
       */       
      {"Cham Drop", "Cham"}
   };

   private static final String m_ContReferencia[] =
   {
      /**
       * SMP3
       */       
      "Cham",
      
      /**
       * SMP5
       */       
      "Cham",

      /**
       * SMP6
       */       
      "Cham Est",

      /**
       * SMP7
       */
      "Cham OK",
      
      /**
       * LDN
       */       
      "Cham", //"Cham OK"
      /**
       * SMP3 Ericsson
       */       
      "Cham",
      
      /**
       * SMP5 Ericsson
       */       
      "Cham",

      /**
       * SMP6 Ericsson
       */       
      "Cham Est",

      /**
       * SMP7 Ericsson
       */
      "Cham OK"
   };

   static 
   {
   }

   public OpApresentaRelAgendadoAnatelSMP() 
   {
		 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Oi"))
		 {
			 m_Indicadores_Rel[1][0][7] = "SMP4_5";
			 m_Indicadores_Rel[1][1][7] = "SMP4_5";
		 }
	   
   }

   public boolean iniciaOperacao(String p_Mensagem) 
   {     
      UsuarioDef Usuario = null;   
      setOperacao("Apresenta&ccedil;&atilde;o de Relat&oacute;rio de Agendado");

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());

      String PosAtual = m_Request.getParameter("posatual");
      // Recupera os argumentos passados pela página
      m_SubOperacao          = m_Request.getParameter("suboperacao");
      m_NomeRelatorio        = m_Request.getParameter("nomerel");
      m_DataGeracao          = m_Request.getParameter("datageracao");
      m_Perfil               = m_Request.getParameter("perfil");
      m_TipoRelatorio        = m_Request.getParameter("tiporel");
      m_IdRelatorio          = m_Request.getParameter("idrel");
      m_NomeArquivoRelatorio = m_Request.getParameter("arquivo");
      m_ApagarRelatorio      = new Boolean(m_Request.getParameter("apaga")).booleanValue();

      m_NomeRelatorio = m_NomeRelatorio.replace('@', ' ');

      // Pegando o tipo de relatorio Anatel;
      try
      {
         int tipoAnatel = Integer.parseInt(m_TipoRelatorio);
         switch(tipoAnatel)
         {
	        case 8: 
	           m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP3_ERICSSON;
	           break;
	        case 9: 
	           m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP5_ERICSSON;
	           break;
	        case 10: 
	           m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP6_ERICSSON;
	           break;
	        case 11:
	           m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP7_ERICSSON;
	           break;
            case 14: 
               m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP3;
               break;
            case 15: 
               m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP5;
               break;
            case 16: 
               m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP6;
               break;
            case 17:
               m_TipoSMP = AgendaAnatelSMP.ANATEL_SMP7;
               break;
            case 18:
               m_TipoSMP = AgendaAnatelSMP.ANATEL_LDN;
               break;
            case 19:
                m_TipoSMP = AgendaAnatelSMP.ANATEL_IDDF;
                break;
            case 20:
                m_TipoSMP = AgendaAnatelSMP.ANATEL_STFC;
                break;
            default:
               System.out.println("************************************");     
               System.out.println("Erro convertendo o Tipo de Relatorio: " + m_TipoRelatorio);
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpApresentaRelAgendadoAnatelSMP:iniciaOperacao(): Erro na conversao do Tipo de Relatorio: "+m_TipoRelatorio);
      }
      
      // Monta a chave para inser&ccedil;&atilde;o e busca na cache de históricos
      m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_NomeRelatorio + "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" + m_Request.getSession().getId();

      if (m_SubOperacao.toLowerCase().equals("paginicial"))
         apresentaPagInicial();
      else if (m_SubOperacao.toLowerCase().equals("paginicial2"))
         apresentaPagInicial2();
      else if (m_SubOperacao.toLowerCase().equals("relatorio"))
         apresentaRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("anatelsmp"))
         apresentaAnatelSMP();
      else if (m_SubOperacao.toLowerCase().equals("toporelatorio"))
         apresentaTopoRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("baserelatorio"))
         apresentaBaseRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("pagrecursos"))
         apresentaRecursos();
      else if (m_SubOperacao.toLowerCase().equals("paglogs"))
         apresentaLogs();
      else if (m_SubOperacao.toLowerCase().equals("alterarecursos"))
         alteraRecursos();         
      else if (m_SubOperacao.toLowerCase().equals("alteraindicadores"))
         alteraIndicadores();
      else if (m_SubOperacao.toLowerCase().equals("trocaperiodo"))
         trocaPeriodo();         
      else if (m_SubOperacao.toLowerCase().equals("download"))
         download();
      else if (m_SubOperacao.toLowerCase().equals("pagconfiguracao"))
         apresentaConfiguracao();
      else if (m_SubOperacao.toLowerCase().equals("alteraconfiguracao"))
         alteraConfiguracao();
      else if (m_SubOperacao.toLowerCase().equals("removeagenda"))
         removeAgenda();
      else
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:iniciaOperacao() - Suboperacao nao encontrada: "+ m_SubOperacao);
      return true;
   }

   public void apresentaPagInicial() 
   {
      try
      {
         AgendaAnatelSMP Ag = null;

         Ag = buscaAgenda();
         if (Ag == null)
         {
            enviaPaginaErro();
            return;
         }
      
         iniciaArgs(10);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "index.htm";
         m_Args[2] = Ag.m_NomeRelatorio + " - " + Ag.m_DataGeracao;

         m_Args[3] = "AnatelSMP";
         m_Args[4] = m_Perfil;
         m_Args[5] = m_TipoRelatorio;
         m_Args[6] = m_IdRelatorio;
         m_Args[7] = m_NomeArquivoRelatorio;
         m_Args[8] = m_NomeRelatorio;
         m_Args[9] = m_DataGeracao;
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaPagInicial(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaPagInicial2() 
   {
      try
      {
         iniciaArgs(10);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "index2.htm";
         m_Args[2] = m_NomeRelatorio + " - " + m_DataGeracao;
         m_Args[3] = "AnatelSMP";         
         
         m_Args[4] = m_Perfil;
         m_Args[5] = m_TipoRelatorio;
         m_Args[6] = m_IdRelatorio;
         m_Args[7] = m_NomeArquivoRelatorio;
         m_Args[8] = m_NomeRelatorio;
         m_Args[9] = m_DataGeracao;
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaPagInicial2(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaRelatorio() 
   {
      try
      {
         iniciaArgs(23);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "relatorio.htm";

         m_Args[2] = "AnatelSMP";
         m_Args[3] = m_Perfil;
         m_Args[4] = m_TipoRelatorio;
         m_Args[5] = m_IdRelatorio;
         m_Args[6] = m_NomeArquivoRelatorio;
         m_Args[7] = m_NomeRelatorio;
         m_Args[8] = m_DataGeracao;

         m_Args[9] = "AnatelSMP";
         m_Args[10] = m_Perfil;
         m_Args[11] = m_TipoRelatorio;
         m_Args[12] = m_IdRelatorio;
         m_Args[13] = m_NomeArquivoRelatorio;
         m_Args[14] = m_NomeRelatorio;
         m_Args[15] = m_DataGeracao;

         m_Args[16] = "AnatelSMP";
         m_Args[17] = m_Perfil;
         m_Args[18] = m_TipoRelatorio;
         m_Args[19] = m_IdRelatorio;
         m_Args[20] = m_NomeArquivoRelatorio;
         m_Args[21] = m_NomeRelatorio;
         m_Args[22] = m_DataGeracao;         

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaTopoRelatorio()
   {
      try
      {
         AgendaAnatelSMP Ag = null;
         int i, iTam, QtdRec1 = 0, QtdRec2 = 0;
         Iterator It;
         Map.Entry Ent;
         Object Obj;
         String TipoRecurso1, TipoRecurso2, TipoRecurso3 = "", Recurso, 
                Recursos1 = null, Recursos2 = null, Recursos3 = null,
                SelectRecurso1 = "", SelectRecurso2 = "", SelectRecurso3 = "",
                TipoRecurso = null, Recursos = null,
                RecursosSelecionados1 = null, RecursosSelecionados2 = null, RecursosSelecionados3 = null;

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         TipoRecurso1 = Ag.m_TituloRecurso[0];
         TipoRecurso2 = Ag.m_TituloRecurso[1];

         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
            TipoRecurso3 = Ag.m_TituloRecurso[2];

         TipoRecurso = m_Request.getParameter("tiporecurso");
         Recursos1 = m_Request.getParameter("recursos1");
         Recursos2 = m_Request.getParameter("recursos2");
         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
            Recursos3 = m_Request.getParameter("recursos3");

         //
         // Seta o primeiro recurso
         //
         if (TipoRecurso != null && TipoRecurso.equals("1") == true)
         {
            RecursosSelecionados1 = m_Request.getParameter("recursos");
            if (RecursosSelecionados1 != null)
               RecursosSelecionados1 = RecursosSelecionados1.replace('@','&');
         }
         else if (TipoRecurso != null && TipoRecurso.equals("2") == true)
         {
            RecursosSelecionados2 = m_Request.getParameter("recursos");
            if (RecursosSelecionados2 != null)
               RecursosSelecionados2 = RecursosSelecionados2.replace('@','&');
         }
         else if (TipoRecurso != null && TipoRecurso.equals("3") == true)
         {
            RecursosSelecionados3 = m_Request.getParameter("recursos");
            if (RecursosSelecionados3 != null)
               RecursosSelecionados3 = RecursosSelecionados3.replace('@','&');
         }
 
         if (RecursosSelecionados1 == null)
            RecursosSelecionados1 = Recursos1;
         if (RecursosSelecionados1 == null || RecursosSelecionados1.length() == 0)
            RecursosSelecionados1 = m_TODOS;

         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
         {
            if (RecursosSelecionados3 == null || RecursosSelecionados3.length() == 0)
               RecursosSelecionados3 = m_TODOS;
         }

         String area = (TipoRecurso1.equalsIgnoreCase("ROTAE") ? "Áreas Origem" : TipoRecurso1);
         
         SelectRecurso1  = "<b>" + area + ":</b>&nbsp;<a href=\"";
         	
         SelectRecurso1 += "javascript:AbreJanela('1','" + RecursosSelecionados1.replace('&','@') + "')";
         RecursosSelecionados1 = RecursosSelecionados1.replace('@','&');         
         if (RecursosSelecionados1.length() > 20)
            Recursos = RecursosSelecionados1.substring(0, 20) + "...";
         else Recursos = RecursosSelecionados1;
         SelectRecurso1 += "\" class=\"link\">" + Recursos + "</a>";

         //
         // Seta o segundo recurso
         //
         if (RecursosSelecionados2 == null)
            RecursosSelecionados2 = Recursos2;
         if (RecursosSelecionados2 == null || RecursosSelecionados2.length() == 0)
            RecursosSelecionados2 = m_TODOS;

         SelectRecurso2  = "<b>" + TipoRecurso2 + ":</b>&nbsp;<a href=\"";
         SelectRecurso2 += "javascript:AbreJanela('2','" + RecursosSelecionados2.replace('&','@') + "')";
         RecursosSelecionados2 = RecursosSelecionados2.replace('@','&');
         if (RecursosSelecionados2.length() > 20)
            Recursos = RecursosSelecionados2.substring(0, 20) + "...";
         else Recursos = RecursosSelecionados2;
         SelectRecurso2 += "\" class=\"link\">" + Recursos + "</a>";         

         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
         {
            //
            // Seta o terceiro recurso
            //
            if (RecursosSelecionados3 == null)
               RecursosSelecionados3 = Recursos3;
            if (RecursosSelecionados3 == null || RecursosSelecionados3.length() == 0)
               RecursosSelecionados3 = m_TODOS;

            SelectRecurso3  = "<b>" + TipoRecurso3 + ":</b>&nbsp;<a href=\"";
            SelectRecurso3 += "javascript:AbreJanela('3','" + RecursosSelecionados3.replace('&','@') + "')";
            RecursosSelecionados3 = RecursosSelecionados3.replace('@','&');
            if (RecursosSelecionados3.length() > 20)
               Recursos = RecursosSelecionados3.substring(0, 20) + "...";
            else Recursos = RecursosSelecionados3;
            SelectRecurso3 += "\" class=\"link\">" + Recursos + "</a>";
         }

         iniciaArgs(22);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "toporelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         m_Args[8] = RecursosSelecionados1;
         m_Args[9] = RecursosSelecionados2;
         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
            m_Args[10] = RecursosSelecionados3;
         else
            m_Args[10] = "";

         m_Args[11] = m_TODOS;
         m_Args[12] = m_TODOS;
         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
            m_Args[13] = m_TODOS;
         else
            m_Args[13] = "";

         m_Args[14] = Ag.m_MapRecursos[0].size() + "";
         m_Args[15] = Ag.m_MapRecursos[1].size() + "";
         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
            m_Args[16] = Ag.m_MapRecursos[2].size() + "";
         else
            m_Args[16] = "";

         m_Args[17] = Ag.m_TipoApresentacao;

         m_Args[18] = Ag.m_NomeRelatorio + " - Data de Referência: " + Ag.m_DataColeta + " - Data de Gera&ccedil;&atilde;o" + Ag.m_DataGeracao;
         m_Args[19] = SelectRecurso1;
         m_Args[20] = SelectRecurso2;

         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
            m_Args[21] = SelectRecurso3;         
         else
            m_Args[21] = "";

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaTopoRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaAnatelSMP()
   {
      try
      {
         AgendaAnatelSMP Ag = null;
         Set ListaRecursos[];
         int i = 0, iQtdRecursos = 2;
         String ExcluiLinhas = null;


         String TipoApresentacao = m_Request.getParameter("tipoapresentacao");
         String Periodo          = m_Request.getParameter("periodo");

         Ag = buscaAgenda();
         if (Ag == null)
         {
            enviaPaginaErro();
            return;
         }

         ExcluiLinhas = m_Request.getParameter("excluilinhas");
         if (ExcluiLinhas != null)
            Ag.m_ExcluiLinhas = ExcluiLinhas;

         switch (m_TipoSMP)
         {                            
            case Agenda.ANATEL_LDN:
               iQtdRecursos = 3;
               break;
         }

         if (TipoApresentacao == null)
            TipoApresentacao = "2";

         Ag.m_TipoApresentacao = TipoApresentacao;

         ListaRecursos = new Set[iQtdRecursos];
         m_ListaNova   = new boolean[iQtdRecursos];
         for (i = 0; i < iQtdRecursos; i++)
         {
            if (Ag.m_MapRecursosNovos[i].size() == 0)
            {
               ListaRecursos[i] = Ag.m_MapRecursos[i].entrySet();
               m_ListaNova[i] = false;
            }
            else
            {
               ListaRecursos[i] = Ag.m_MapRecursosNovos[i].entrySet();
               m_ListaNova[i] = true;
            }
         }

         m_Html.enviaInicio("", "", "anatelsmp.js");
         m_Html.envia("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\">\n");
         
         // Tipo de Apresentacao 1
         if (m_TipoSMP != Agenda.ANATEL_LDN)
         {
            if (TipoApresentacao.equals("1"))
               enviaApresentacao1_SMP(Ag, ListaRecursos);
            else
               enviaApresentacao2_SMP(Ag, ListaRecursos);
         }
         else
         {
            if (TipoApresentacao.equals("1"))
               enviaApresentacao1_LDN(Ag, ListaRecursos);
            else
               enviaApresentacao2_LDN(Ag, ListaRecursos);
         }

         m_Html.envia("</table>\n");
         m_Html.enviafinal(null, null, true);         
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaAnatelSMP(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void enviaApresentacao1_SMP(AgendaAnatelSMP Ag, Set ListaRecursos[])
   {
      Iterator It[];
      Map.Entry Ent[];   
      boolean bLinhaZerada = false;
      Object Obj, ObjLinha;
      String Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
      String NomePeriodo, Recurso = "", Recurso2 = "", ExcluiLinhas = null;
      Vector LinhaRelProcessado[];
      int j = 0, i = 0;
      float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
      float afFPond[], afFPondTotal[], afPorcent[];
      Integer Posicao;


      It = new Iterator[2];
      Ent = new Map.Entry[2];

      // Inicia vetor de linhas de indicadores processados
      LinhaRelProcessado = new Vector[Ag.m_PeriodosApresentaveis.size()];

      // Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
      afAcumTotal = new float [Ag.m_PeriodosApresentaveis.size()];
      afFPondTotal = new float [Ag.m_PeriodosApresentaveis.size()];         
      afAcum = new float [Ag.m_PeriodosApresentaveis.size()];
      afPorcent = new float[Ag.m_PeriodosApresentaveis.size()];         
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         afAcum[i] = 0;
         afAcumTotal[i] = 0;            
         afFPondTotal[i] = 0;
         afPorcent[i] = 0;
      }

      // Cabeçalho da tabela - início
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
            
      // Coluna do período
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Per&iacute;odo"
                    +"</b></font></td>");

      // Coluna da central
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[1]
                    +"</b></font></td>");
                          
      // Coluna da área
      String area = Ag.m_TituloRecurso[0].equalsIgnoreCase("ROTAE") ? "Áreas Origem" : Ag.m_TituloRecurso[0]; 

      	m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
      			+ area + "</b></font></td>");

      	// Colunas dos indicadores
      for (int k = 0; k < m_Indicadores_Rel[m_TipoSMP][0].length; k++)
      {
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                       +m_Indicadores_Rel[m_TipoSMP][0][k]
                       +"</b></font></td>");
      }

      // Cabeçalho da tabela - fim
      m_Html.envia("\n</tr>\n");

      It[1] = ListaRecursos[1].iterator();
      while (It[1].hasNext())
      {
         Ent[1] = (Map.Entry)It[1].next();
         Obj = Ent[1].getValue();
         Recurso2 = Obj.toString();

         if (Recurso2.equals(m_TODOS))
            continue;

         It[0] = ListaRecursos[0].iterator();
         while (It[0].hasNext())
         {
            Ent[0] = (Map.Entry)It[0].next();
            Obj = Ent[0].getValue();
            Recurso = Obj.toString();

            if (Recurso.equals(m_TODOS))
               continue;

            bLinhaZerada = false;
            for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
            {
               // Recupera a linha de indicadores
               ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
               if (ObjLinha != null)
                  LinhaRelProcessado[0] = (Vector)ObjLinha;
               else{
                  LinhaRelProcessado[0] = null;
                  bLinhaZerada = true;
                  break;
               }

               //
               // Não envia as linhas que tem a qtd de chamadas igual a ZERO
               // (Sai do laço for)
               //
               if (Ag.m_ExcluiLinhas != null && 
                   Ag.m_ExcluiLinhas.equals("true"))
               {
                  Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                  if (Posicao == null)
                  {
                     System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                  }
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
               m_Html.envia("<tr"+Cor+">\n   ");

               // Período
               if (Ag.m_NomesDatas != null)
                  NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
               else
                  NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
               m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+NomePeriodo+"</font></td>");

               // Recurso bilhetador
               m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso2+"</font></td>");

               // Recurso área
               m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso+"</font></td>");

               // Indicadores
               fNum = fDen = 0;
               for (int k = 0; k < m_Indicadores_Rel[m_TipoSMP][0].length; k++)
               {
                  //System.out.println("Indicador procurado: " + m_Indicadores_Rel[m_TipoSMP][0][k]);
                  Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][0][k]);
                  if (Posicao == null)
                  {
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">--</font></td>");
                     System.out.println(">>> Posicao NAO encontrada ("+m_Indicadores_Rel[m_TipoSMP][0][k]+")");
                  }
                  else
                  {
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString()+"</font></td>");
                           
                     if (i == 0 && m_Indicadores_Rel[m_TipoSMP][0][k].equals("F. Pond."))
                     {
                        try
                        {
                           Integer PosicaoAux;
                           PosicaoAux = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                           if (PosicaoAux == null)
                           {
                              System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                           }
                           else
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

                     if (m_Indicadores_Rel[m_TipoSMP][0][k].equals(m_Num_Dem[m_TipoSMP][0]))
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

                     if (m_Indicadores_Rel[m_TipoSMP][0][k].equals(m_Num_Dem[m_TipoSMP][1]))
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

                     if (m_Indicadores_Rel[m_TipoSMP][0][k].equals(Ag.m_TipoIndicador))
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
               m_Html.envia("\n</tr>\n");
            }

            // Seleciona a cor de apresentação da linha
            if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
            else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
            j++;

            if (!bLinhaZerada)
            {
               // Início da linha
               m_Html.envia("<tr"+Cor+">\n   ");
               m_Html.envia("<td colspan=\""+(m_Indicadores_Rel[m_TipoSMP][0].length + 3)+"\">&nbsp;</td>");
               m_Html.envia("</tr>\n");
            }
         }               
      }

      //
      // Resumo
      //

      // Títulos
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap colspan="+(3+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Resumo - Indicador "+Ag.m_TipoIndicador
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
            
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Per&iacute;odos"
                    +"</b></font></td>");

      m_Html.envia("<td align=\"right\" nowrap colspan="+(2+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Indicador"
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
      //

      // Períodos e indicadores resumidos
      int iPos;
      String strAux, strRes;
      j = 0;
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         iPos = 0;
         strAux = "";
         strRes = "";
            
         // Seleciona a cor de apresentação da linha
         if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
         else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
         j++;

         // Período
         if (Ag.m_NomesDatas != null)
            NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         else
            NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

         m_Html.envia("<tr"+ Cor+">\n   ");
         // Coluna do período
         m_Html.envia("<td align=\"center\"><font face="+m_FONTE+"><b>"
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
            strRes = "0";

         m_Html.envia("<td align=\"right\" nowrap nowrap colspan="+(2+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+"><b>"
                       + strRes
                       +"</b></font></td>");                             
         m_Html.envia("\n</tr>\n");
      }
   }
   
   public void enviaApresentacao2_SMP(AgendaAnatelSMP Ag, Set ListaRecursos[])
   {
      Iterator It[];
      Map.Entry Ent[];   
      boolean bLinhaZerada = false;
      Object Obj, ObjLinha;
      String Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
      String NomePeriodo, Recurso = "", Recurso2 = "", ExcluiLinhas = null;
      Vector LinhaRelProcessado[];
      int j = 0, i = 0;
      float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
      float afFPond[], afFPondTotal[], afPorcent[];
      Integer Posicao;


      It = new Iterator[2];
      Ent = new Map.Entry[2];

      // Inicia vetor de linhas de indicadores processados
      LinhaRelProcessado = new Vector[Ag.m_PeriodosApresentaveis.size()];

      // Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
      afAcumTotal = new float [Ag.m_PeriodosApresentaveis.size()];
      afFPondTotal = new float [Ag.m_PeriodosApresentaveis.size()];         
      afAcum = new float [Ag.m_PeriodosApresentaveis.size()];
      afPorcent = new float[Ag.m_PeriodosApresentaveis.size()];         
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         afAcum[i] = 0;
         afAcumTotal[i] = 0;            
         afFPondTotal[i] = 0;
         afPorcent[i] = 0;
      }
   
      afFPond   = new float[Ag.m_PeriodosApresentaveis.size()];
      afPorcent = new float[Ag.m_PeriodosApresentaveis.size()];
         
      // Cabeçalho da tabela - início
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
            
      // Coluna da central
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[1]
                    +"</b></font></td>");

      //    Coluna da área
      String area = Ag.m_TituloRecurso[0].equalsIgnoreCase("ROTAE") ? "Áreas Origem" : Ag.m_TituloRecurso[0]; 

      	m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
      			+ area + "</b></font></td>");
      
      // Coluna do título do indicador
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"&nbsp;"
                    +"</b></font></td>");

      // Colunas dos períodos
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         // Período
         if (Ag.m_NomesDatas != null)
            NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         else
            NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+NomePeriodo+"</b></font></td>");
      }
      m_Html.envia("</tr>\n");
      // Cabeçalho da tabela - fim

      int QtdLinhas;
      //
      // Percorre lista de bilhetadores
      //
      It[1] = ListaRecursos[1].iterator();
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
         QtdLinhas = ListaRecursos[0].size();
         if (Ag.m_ExcluiLinhas.equals("true"))
         {
            It[0] = ListaRecursos[0].iterator();
            while (It[0].hasNext())
            {
               Ent[0] = (Map.Entry)It[0].next();
               Obj = Ent[0].getValue();
               Recurso = Obj.toString();

               if (Recurso.equals(m_TODOS))
                  continue;

               bLinhaZerada = true;
               for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
               {
                  // Recupera a linha de indicadores
                  ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
                  if (ObjLinha != null)
                  {
                     LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

                     Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                     if (Posicao == null)
                        System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                     else
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
         if (Ag.m_ExcluiLinhas.equals("true"))
         {
            if (QtdLinhas != 1)
            {
               m_Html.envia("<tr>\n   ");
               m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (m_TEXTOSMP[m_TipoSMP].length))+"\"><font face="+m_FONTE+"><b>"
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

         It[0] = ListaRecursos[0].iterator();
         while (It[0].hasNext())
         {
            Ent[0] = (Map.Entry)It[0].next();
            Obj = Ent[0].getValue();
            Recurso = Obj.toString();

            if (Recurso.equals(m_TODOS))
               continue;

            bLinhaZerada = true;
            for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
            {
               // Recupera a linha de indicadores
               ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2);
//            	ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso2+"-"+Recurso);
               if (ObjLinha != null)
               {
                  LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

                  Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                  if (Posicao == null)
                     System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                  else
                  {
                     if (LinhaRelProcessado[i].elementAt(Posicao.intValue()).toString().startsWith("0") == false)
                        bLinhaZerada = false;
                  }
               }
               else
                  LinhaRelProcessado[i] = null;
            }

            if (Ag.m_ExcluiLinhas.equals("true") && (bLinhaZerada))
               continue;
 
            if (Cont != 0)
            {
               // Seleciona a cor de apresentação da linha
               if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHASIM2+"\"";
               else Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
               j++;

               m_Html.envia("<tr"+Cor+">\n");
            }

            if (Cont == 0)
            {
               m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap rowspan=\""+m_TEXTOSMP[m_TipoSMP].length+"><font face="+m_FONTE+"><b>"
                             +Recurso
                             +"</b></font></td>");

               //
               // Calcula o SMP ponderado
               //
               for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
               {                        
                  Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get(Ag.m_TipoIndicador);
                  if ((Posicao != null) && (LinhaRelProcessado[k] != null))
                  {
                     String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
                     strAux = strAux.replace(',','.');
                     
                     if(strAux.equalsIgnoreCase("erro"))
                     	strAux="0";
                     afPorcent[k] = Float.parseFloat(strAux);
                  }
                  else
                  {
                     afPorcent[k] = 0;
                  }

                  Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get("F. Pond.");
                  if ((Posicao != null) && (LinhaRelProcessado[k] != null))
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

               for (i = 0; i < m_TEXTOSMP[m_TipoSMP].length; i++)
               {
                  if (i != 0)
                     m_Html.envia("<tr>\n");
                  m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"left\"><font face="+m_FONTE+">"
                                +(m_TEXTOSMP[m_TipoSMP][i].equals("Percentual ") ? m_TEXTOSMP[m_TipoSMP][i] + Ag.m_TipoIndicador: m_TEXTOSMP[m_TipoSMP][i])
                                +"</font></td>");

                  for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
                  {
                	  Posicao = null;
                	  /*Será acrescentado esse teste (adaptação técnica) para verificar se o cliente é Brasil Telecom.
                	  	Essa adaptação foi necessária pois os indicadores no arquivo "indicadores.xml" da Brasil Telecom
                	  	está diferente das demais. Será acrescentado o "_"(underLine) nos indicadores SMP5.
                	  */
                	  if((Cliente.fnCliente() == Cliente.BrasilTelecomFixa) || (Cliente.fnCliente() == Cliente.Oi)){
                		  if(m_Indicadores_Rel[m_TipoSMP][1][i].equalsIgnoreCase("OK_VM") /*||  m_Indicadores_Rel[m_TipoSMP][1][i].equalsIgnoreCase("NR_VM")*/){
                			  Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get( (m_Indicadores_Rel[m_TipoSMP][1][i]).replaceAll(" ", "_") );
                   		  }else if(m_Indicadores_Rel[m_TipoSMP][1][i].equalsIgnoreCase("NR VM")){
                   			 Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get( (m_Indicadores_Rel[m_TipoSMP][1][i]).replaceAll(" ", "_") );
                   		  }
                		  else{
                			   Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][1][i]);
                		  }
                	  }
                	  
                	  else{
                		  Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][1][i]);
                	  }
                	  
                     /*
                      * Fim da adaptação para o cliente Brasil Telecom
                      */
                     if (Posicao == null)
                     {
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">--</font></td>");
                        System.out.println(">>> Posicao NAO encontrada ("+m_Indicadores_Rel[m_TipoSMP][1][i]+")");
     
                     }
                     else
                     {
                     	if (LinhaRelProcessado[k] != null)
                     	{
                     		m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap><font face="+m_FONTE+">"
                                      +LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString()
                                      +"</font></td>");
                     	}
                     	else 
                     	{
                     		m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap><font face="+m_FONTE+">"
                                    +"0"+"</font></td>");
                     	}
                     }
                  }

                  if (i != m_TEXTOSMP[m_TipoSMP].length-1)
                     m_Html.envia("\n</tr>\n");
               }
               m_Html.envia("\n</tr>\n");
            }
            else
            {
               m_Html.envia("<td align=\"center\" nowrap rowspan=\""+m_TEXTOSMP[m_TipoSMP].length+"><font face="+m_FONTE+"><b>"
                             +Recurso
                             +"</b></font></td>");

               //
               // Calcula o SMP ponderado
               //
               for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
               {                        
                  Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get(Ag.m_TipoIndicador);
                  if ((Posicao != null) && (LinhaRelProcessado[k] != null))
                  {
                     String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
                     strAux = strAux.replace(',','.');
                     afPorcent[k] = Float.parseFloat(strAux);
                  }
                  else
                     afPorcent[k] = 0;

                  Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get("F. Pond.");
                  if ((Posicao != null) && (LinhaRelProcessado[k] != null))
                  {
                     afFPond[k]   = Float.parseFloat(LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString());
                     fTotalFatorPond += afFPond[k];
                  }
                  else
                  {
                     afFPond[k] = 0;
                     
                  }

                  afAcum[k] = (afPorcent[k]/100) * afFPond[k];
                  afAcumTotal[k] += (afPorcent[k]/100) * afFPond[k];

                  if (afPorcent[k] != 0)
                	  afFPondTotal[k] += afFPond[k];
                  //else System.out.println("2)->Porc = ZERO");
               }

               for (i = 0; i < m_TEXTOSMP[m_TipoSMP].length; i++)
               {
                  if (i != 0)
                     m_Html.envia("<tr "+Cor+">\n");
                  m_Html.envia("<td align=\"left\"><font face="+m_FONTE+">"
                                +(m_TEXTOSMP[m_TipoSMP][i].equals("Percentual ") ? m_TEXTOSMP[m_TipoSMP][i] + Ag.m_TipoIndicador: m_TEXTOSMP[m_TipoSMP][i])                                      
                                +"</font></td>");

                  for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
                  {
                
                	 Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][1][i]);
           
                     
                     if (Posicao == null)
                     {
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">--</font></td>");
                        System.out.println(">>> Posicao NAO encontrada ("+m_Indicadores_Rel[m_TipoSMP][1][i]+")");
                     }
                     else
                     {
                     	if (LinhaRelProcessado[k] != null)
                     	{
                     		m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"
                                      +LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString()
                                      +"</font></td>");
                     	}
                     	else
                     	{
                     		m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"
                                   +"0"+"</font></td>");
                     	}
                     }
                  }
                                      
                  if (i != m_TEXTOSMP[m_TipoSMP].length-1)
                     m_Html.envia("\n</tr>\n");
               }
            }
            Cont++;
         }
         m_Html.envia("\n</tr>\n");

         m_Html.envia("\n<tr>\n");
         m_Html.envia("<td colspan="+(3+Ag.m_PeriodosApresentaveis.size())+">&nbsp;</td>");
         m_Html.envia("\n</tr>\n");
      }

      //
      // Resumo
      //

      // Títulos
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap colspan="+(3+Ag.m_PeriodosApresentaveis.size())+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Resumo - Indicador "+Ag.m_TipoIndicador
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
            
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Per&iacute;odos"
                    +"</b></font></td>");

      m_Html.envia("<td align=\"center\" nowrap colspan="+(2+Ag.m_PeriodosApresentaveis.size()/*+m_Indicadores_Rel[m_TipoSMP][0].length*/)+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Indicador"
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
      //

      // Períodos e indicadores resumidos - Apresentacao = 2
      int iPos;
      String strAux, strRes;
      j = 0;
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         iPos = 0;
         strAux = "";
         strRes = "";
               
         // Seleciona a cor de apresentação da linha
         if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
         else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
         j++;

         // Período
         if (Ag.m_NomesDatas != null)
            NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         else
            NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

         m_Html.envia("<tr"+ Cor+">\n   ");
         // Coluna do período
         m_Html.envia("<td align=\"center\"><font face="+m_FONTE+"><b>"
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
            strRes = "0";

         m_Html.envia("<td align=\"right\" nowrap nowrap colspan="+(2+Ag.m_PeriodosApresentaveis.size()/*m_Indicadores_Rel[m_TipoSMP][0].length*/)+"><font face="+m_FONTE+"><b>"
                       +strRes
                       +"</b></font></td>");                             
         m_Html.envia("\n</tr>\n");
      }
   }

   public void enviaApresentacao1_LDN(AgendaAnatelSMP Ag, Set ListaRecursos[])
   {
      Iterator It[];
      Map.Entry Ent[];   
      boolean bLinhaZerada = false;
      Object Obj, ObjLinha;
      String Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
      String NomePeriodo, Recurso = "", Recurso2 = "", Recurso3 = "", ExcluiLinhas = null;
      Vector LinhaRelProcessado[];
      int j = 0, i = 0;
      float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
      float afFPond[], afFPondTotal[], afPorcent[];
      Integer Posicao;


      It = new Iterator[3];
      Ent = new Map.Entry[3];

      // Inicia vetor de linhas de indicadores processados
      LinhaRelProcessado = new Vector[Ag.m_PeriodosApresentaveis.size()];

      // Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
      afAcumTotal = new float [Ag.m_PeriodosApresentaveis.size()];
      afFPondTotal = new float [Ag.m_PeriodosApresentaveis.size()];         
      afAcum = new float [Ag.m_PeriodosApresentaveis.size()];
      afPorcent = new float[Ag.m_PeriodosApresentaveis.size()];         
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         afAcum[i] = 0;
         afAcumTotal[i] = 0;            
         afFPondTotal[i] = 0;
         afPorcent[i] = 0;
      }

      // Cabeçalho da tabela - início
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
            
      // Coluna do período
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Per&iacute;odo"
                    +"</b></font></td>");

      // Coluna da central
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[1]
                    +"</b></font></td>");
                          
      // Coluna da área
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[0]
                    +"</b></font></td>");
   
      

      // Coluna do destino (CSP)
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[2]
                    +"</b></font></td>");

      // Colunas dos indicadores
      for (int k = 0; k < m_Indicadores_Rel[m_TipoSMP][0].length; k++)
      {
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                       +m_Indicadores_Rel[m_TipoSMP][0][k]
                       +"</b></font></td>");
      }

      // Cabeçalho da tabela - fim
      m_Html.envia("\n</tr>\n");

      It[1] = ListaRecursos[1].iterator();
      while (It[1].hasNext())
      {
         Ent[1] = (Map.Entry)It[1].next();
         Obj = Ent[1].getValue();
         Recurso2 = Obj.toString();

         if (Recurso2.equals(m_TODOS))
            continue;

         It[0] = ListaRecursos[0].iterator();
         while (It[0].hasNext())
         {
            Ent[0] = (Map.Entry)It[0].next();
            Obj = Ent[0].getValue();
            Recurso = Obj.toString();

            if (Recurso.equals(m_TODOS))
               continue;

            It[2] = ListaRecursos[2].iterator();
            while (It[2].hasNext())
            {
               Ent[2] = (Map.Entry)It[2].next();
               Obj = Ent[2].getValue();
               Recurso3 = Obj.toString();

               if (Recurso3.equals(m_TODOS))
                  continue;               

               bLinhaZerada = false;
               for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
               {
                  // Recupera a linha de indicadores
                  ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
                  if (ObjLinha != null)
                     LinhaRelProcessado[0] = (Vector)ObjLinha;
                  else
                     LinhaRelProcessado[0] = null;

                  //
                  // Não envia as linhas que tem a qtd de chamadas igual a ZERO
                  // (Sai do laço for)
                  //
                  if (Ag.m_ExcluiLinhas != null && 
                      Ag.m_ExcluiLinhas.equals("true"))
                  {
                     Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                     if (Posicao == null)
                     {
                        System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                     }
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
                  m_Html.envia("<tr"+Cor+">\n   ");

                  // Período
                  if (Ag.m_NomesDatas != null)
                     NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
                  else
                     NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+NomePeriodo+"</font></td>");

                  // Recurso bilhetador
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso2+"</font></td>");

                  // Recurso área
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso+"</font></td>");

                  // Recurso destino (CSP)
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+Recurso3+"</font></td>");

                  // Indicadores
                  fNum = fDen = 0;
                  for (int k = 0; k < m_Indicadores_Rel[m_TipoSMP][0].length; k++)
                  {
                     //System.out.println("Indicador procurado: " + m_Indicadores_Rel[m_TipoSMP][0][k]);
                     Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][0][k]);
                     if (Posicao == null)
                     {
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">--</font></td>");
                        System.out.println(">>> Posicao NAO encontrada ("+m_Indicadores_Rel[m_TipoSMP][0][k]+")");
                     }
                     else
                     {
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+(LinhaRelProcessado[0] != null ? LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString() : "-")+"</font></td>");
                           
                        if (i == 0 && m_Indicadores_Rel[m_TipoSMP][0][k].equals("F. Pond."))
                        {
                           try
                           {
                              Integer PosicaoAux;
                              PosicaoAux = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                              if (PosicaoAux == null)
                              {
                                 System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                              }
                              else
                              {
                                 if (LinhaRelProcessado[0] != null && LinhaRelProcessado[0].elementAt(PosicaoAux.intValue()).toString().startsWith("0") == false)
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
                           }
                           catch (Exception Exc)
                           {
                              System.out.println("Erro na conversao do indicador");
                           }
                        }

                        if (m_Indicadores_Rel[m_TipoSMP][0][k].equals(m_Num_Dem[m_TipoSMP][0]))
                        {
                           try
                           {
                              if (LinhaRelProcessado[0] != null)
                                 fNum = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
                              else
                                 fNum = -0;
                           }
                           catch (Exception Exc)
                           {
                              System.out.println("Erro na conversao do indicador");
                           }
                        }

                        if (m_Indicadores_Rel[m_TipoSMP][0][k].equals(m_Num_Dem[m_TipoSMP][1]))
                        {
                           try
                           {
                              if (LinhaRelProcessado[0] != null)
                                 fDen = Float.parseFloat(LinhaRelProcessado[0].elementAt(Posicao.intValue()).toString());
                              else
                                 fDen = 0;
                           }
                           catch (Exception Exc)
                           {
                              System.out.println("Erro na conversao do indicador");
                           }
                        }

                        if (m_Indicadores_Rel[m_TipoSMP][0][k].equals(Ag.m_TipoIndicador))
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
                  m_Html.envia("\n</tr>\n");
               }

               // Seleciona a cor de apresentação da linha
               if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
               else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
               j++;

               if (!bLinhaZerada)
               {
                  // Início da linha
                  m_Html.envia("<tr"+Cor+">\n   ");
                  m_Html.envia("<td colspan=\""+(m_Indicadores_Rel[m_TipoSMP][0].length + 4)+"\">&nbsp;</td>");
                  m_Html.envia("</tr>\n");
               }
            }
         }
      }

      //
      // Resumo
      //

      // Títulos
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap colspan="+(4+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Resumo - Indicador "+Ag.m_TipoIndicador
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
            
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap colspan=\"2\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Per&iacute;odos"
                    +"</b></font></td>");

      m_Html.envia("<td align=\"right\" nowrap colspan="+(2+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Indicador"
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
      //

      // Períodos e indicadores resumidos
      int iPos;
      String strAux, strRes;
      j = 0;
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         iPos = 0;
         strAux = "";
         strRes = "";
            
         // Seleciona a cor de apresentação da linha
         if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
         else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
         j++;

         // Período
         if (Ag.m_NomesDatas != null)
            NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         else
            NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

         m_Html.envia("<tr"+ Cor+">\n   ");
         // Coluna do período
         m_Html.envia("<td align=\"center\" colspan=\"2\"><font face="+m_FONTE+"><b>"
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
            strRes = "0";

         m_Html.envia("<td align=\"right\" nowrap nowrap colspan="+(2+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+"><b>"
                       +strRes
                       +"</b></font></td>");                             
         m_Html.envia("\n</tr>\n");
      }
   }

   public void enviaApresentacao2_LDN(AgendaAnatelSMP Ag, Set ListaRecursos[])
   {
      Iterator It[];
      Map.Entry Ent[];
      boolean bLinhaZerada = false, EnviouCentral = false;
      Object Obj, ObjLinha;
      String Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
      String NomePeriodo, Recurso = "", Recurso2 = "", Recurso3 = "", ExcluiLinhas = null;
      Vector LinhaRelProcessado[];
      int j = 0, i = 0, QtdLinhas = 0, Indice = 0, Indice2 = 0, QtdLinhasExc[];
      float afAcum[], afAcumTotal[], fTotalFatorPond = 0, fNum = 0, fDen = 0, fFatorPond = 0;
      float afFPond[], afFPondTotal[], afPorcent[];
      Integer Posicao;


      It = new Iterator[3];
      Ent = new Map.Entry[3];

      // Inicia vetor de linhas de indicadores processados
      LinhaRelProcessado = new Vector[Ag.m_PeriodosApresentaveis.size()];

      // Inicia os acumuladores do Fator de Ponderacao * Percentual do Indicador
      afAcumTotal = new float [Ag.m_PeriodosApresentaveis.size()];
      afFPondTotal = new float [Ag.m_PeriodosApresentaveis.size()];
      afAcum = new float [Ag.m_PeriodosApresentaveis.size()];
      afPorcent = new float[Ag.m_PeriodosApresentaveis.size()];
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         afAcum[i] = 0;
         afAcumTotal[i] = 0;
         afFPondTotal[i] = 0;
         afPorcent[i] = 0;
      }

      afFPond   = new float[Ag.m_PeriodosApresentaveis.size()];
      afPorcent = new float[Ag.m_PeriodosApresentaveis.size()];

      // Cabeçalho da tabela - início
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");

      // Coluna da central
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[1]
                    +"</b></font></td>");

      // Coluna da área
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +Ag.m_TituloRecurso[0]
                    +"</b></font></td>");

      // Coluna do destino (CSP)
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"&nbsp;&nbsp;&nbsp;"+Ag.m_TituloRecurso[2]+"&nbsp;&nbsp;&nbsp;"
                    +"</b></font></td>");

      // Coluna do título do indicador
      m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"&nbsp;"
                    +"</b></font></td>");

      // Colunas dos períodos
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         // Período
         if (Ag.m_NomesDatas != null)
            NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         else
            NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+NomePeriodo+"</b></font></td>");
      }
      m_Html.envia("</tr>\n");
      // Cabeçalho da tabela - fim

      QtdLinhasExc = new int[m_ListaNova[0] == true ? ListaRecursos[0].size() : ListaRecursos[0].size()-1];
      for (i = 0; i < QtdLinhasExc.length; i++)
         QtdLinhasExc[i] = 0;

      //
      // Percorre lista de bilhetadores
      //
      It[1] = ListaRecursos[1].iterator();
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
         if (Ag.m_ExcluiLinhas.equals("true"))
         {
            Indice = 0;         
            It[0] = ListaRecursos[0].iterator();
            while (It[0].hasNext())
            {
               Ent[0] = (Map.Entry)It[0].next();
               Obj = Ent[0].getValue();
               Recurso = Obj.toString();

               if (Recurso.equals(m_TODOS))
                  continue;

               int iTamListaRec0 = ListaRecursos[0].size()-1;
               int iTamListaRec2 = ListaRecursos[2].size()-1;

               QtdLinhas = iTamListaRec0 * iTamListaRec2;

               if (m_ListaNova[0] == true)
                  QtdLinhas++;
               if (m_ListaNova[2] == true)
                  QtdLinhas++;
//System.out.println("0) QtdLinhas: "+QtdLinhas);

               //
               // Percorre lista do Destino (CSP)
               //
               QtdLinhasExc[Indice] = 0;
               It[2] = ListaRecursos[2].iterator();
               while (It[2].hasNext())
               {
                  Ent[2] = (Map.Entry)It[2].next();
                  Obj = Ent[2].getValue();
                  Recurso3 = Obj.toString();

                  if (Recurso3.equals(m_TODOS))
                     continue;

                  bLinhaZerada = true;
                  for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
                  {
                     // Recupera a linha de indicadores
                     ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
                     if (ObjLinha != null)
                     {
                        LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

                        Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                        if (Posicao == null)
                           System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                        else
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
//System.out.println("Bil: "+Recurso2);
//for (i = 0; i < QtdLinhasExc.length; i++)
//   System.out.println("QtdLinhasExc["+i+"]: "+QtdLinhasExc[i]);

         It[0] = ListaRecursos[0].iterator();
         while (It[0].hasNext())
         {
            Ent[0] = (Map.Entry)It[0].next();
            Obj = Ent[0].getValue();
            Recurso = Obj.toString();

            if (EnviouCentral == false)
            {
               // Coluna da central
               if (Ag.m_ExcluiLinhas.equals("true"))
               {
                  if (QtdLinhas != 1)
                  {
                     QtdLinhas = 0;
                     for (i = 0; i < QtdLinhasExc.length; i++)
                        QtdLinhas += QtdLinhasExc[i] * m_TEXTOSMP[m_TipoSMP].length;
                     
//System.out.println("QtdLinhas: "+QtdLinhas);
                     m_Html.envia("<tr>\n   ");
                     m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+QtdLinhas+"\"><font face="+m_FONTE+"><b>"
                                   +Recurso2
                                   +"</b></font></td>");
                  }
                  else
                     continue;
               }
               else
               {
                  int iTamListaRec0 = ListaRecursos[0].size()-1;
                  int iTamListaRec2 = ListaRecursos[2].size()-1;

                  QtdLinhas = iTamListaRec0 * iTamListaRec2;

                  if (m_ListaNova[0] == true)
                     QtdLinhas++;
                  if (m_ListaNova[2] == true)
                     QtdLinhas++;               
                     
                  QtdLinhas *= /*QtdLinhasExc[i] * */m_TEXTOSMP[m_TipoSMP].length;
                  //m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+((QtdLinhas-1) * (m_TEXTOSMP[m_TipoSMP].length))+"\"><font face="+m_FONTE+"><b>"
                  m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM3+"\" align=\"left\" nowrap rowspan=\""+QtdLinhas+"\"><font face="+m_FONTE+"><b>"
                                +Recurso2
                                +"</b></font></td>");
               }
            
               Indice2++;
               EnviouCentral = true;
            }

            if (Recurso.equals(m_TODOS))
               continue;

            //
            // Percorre lista de destinos (CSP)
            //
            It[2] = ListaRecursos[2].iterator();
            while (It[2].hasNext())
            {
               Ent[2] = (Map.Entry)It[2].next();
               Obj = Ent[2].getValue();
               Recurso3 = Obj.toString();

               if (Recurso3.equals(m_TODOS))
                  continue;

               bLinhaZerada = true;
               for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
               {
                  // Recupera a linha de indicadores
                  ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(i))).intValue()].get(Recurso+"-"+Recurso2+"-"+Recurso3);
                  if (ObjLinha != null)
                  {
                     LinhaRelProcessado[i] = new Vector((Vector)ObjLinha);

                     Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_ContReferencia[m_TipoSMP]);
                     if (Posicao == null)
                        System.out.println(">>> Posicao NAO encontrada ("+m_ContReferencia[m_TipoSMP]+")");
                     else
                     {
                        if (LinhaRelProcessado[i].elementAt(Posicao.intValue()).toString().startsWith("0") == false)
                           bLinhaZerada = false;
                     }
                  }
                  else
                  {
                     //System.out.println("Não achou a chave na lista: '"+Recurso+"-"+Recurso2+"-"+Recurso3+"'");                  
                     LinhaRelProcessado[i] = null;
                  }
               }

               if (Ag.m_ExcluiLinhas.equals("true") && (bLinhaZerada))
                  continue;

               if (Cont != 0)
               {
                  // Seleciona a cor de apresentação da linha
                  if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHASIM2+"\"";
                  else Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
                  j++;

                  m_Html.envia("<tr"+Cor+">\n");
               }

               if (Cont == 0)
               {
                  m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap rowspan=\""+m_TEXTOSMP[m_TipoSMP].length+"><font face="+m_FONTE+"><b>"
                                +Recurso
                                +"</b></font></td>");
                  m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap rowspan=\""+m_TEXTOSMP[m_TipoSMP].length+"><font face="+m_FONTE+"><b>"
                                +"&nbsp;&nbsp;&nbsp;"+Recurso3+"&nbsp;&nbsp;&nbsp;"
                                +"</b></font></td>");

                  //
                  // Calcula o SMP ponderado
                  //
                  for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
                  {                        
                     Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get(Ag.m_TipoIndicador);
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

                     Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get("F. Pond.");
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

                  for (i = 0; i < m_TEXTOSMP[m_TipoSMP].length; i++)
                  {
                     if (i != 0)
                        m_Html.envia("<tr>\n");
                     m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"left\"><font face="+m_FONTE+">"
                                   +(m_TEXTOSMP[m_TipoSMP][i].equals("Percentual ") ? m_TEXTOSMP[m_TipoSMP][i] + Ag.m_TipoIndicador: m_TEXTOSMP[m_TipoSMP][i])
                                   +"</font></td>");

                     for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
                     {
                        Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][1][i]);
                        if (Posicao == null)
                        {
                           m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">--</font></td>");
                           System.out.println(">>> Posicao NAO encontrada ("+m_Indicadores_Rel[m_TipoSMP][1][i]+")");
                        }
                        else
                        {
                           m_Html.envia("<td bgcolor=\""+m_COR_LINHASIM2+"\" align=\"center\" nowrap><font face="+m_FONTE+">"
                                         +(LinhaRelProcessado[k] != null ? LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString() : "-")
                                         +"</font></td>");
                        }
                     }

                     if (i != m_TEXTOSMP[m_TipoSMP].length-1)
                        m_Html.envia("\n</tr>\n");
                  }
                  m_Html.envia("\n</tr>\n");
               }
               else
               {
                  m_Html.envia("<td align=\"center\" nowrap rowspan=\""+m_TEXTOSMP[m_TipoSMP].length+"><font face="+m_FONTE+"><b>"
                                +Recurso
                                +"</b></font></td>");
                  m_Html.envia("<td align=\"center\" nowrap rowspan=\""+m_TEXTOSMP[m_TipoSMP].length+"><font face="+m_FONTE+"><b>"
                                +"&nbsp;&nbsp;&nbsp;"+Recurso3+"&nbsp;&nbsp;&nbsp;"
                                +"</b></font></td>");

                  //
                  // Calcula o SMP ponderado
                  //
                  for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
                  {                        
                     Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get(Ag.m_TipoIndicador);
                     if (Posicao != null)
                     {
                        if (LinhaRelProcessado[k] != null)
                        {
                           String strAux = LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString();
                           strAux = strAux.replace(',','.');
                           afPorcent[k] = Float.parseFloat(strAux);
                        }
                        else
                        {
                           afPorcent[k] = 0;
                        }
                     }
                     else
                        afPorcent[k] = 0;

                     Posicao  = (Integer)Ag.m_IndicadoresAnatelSMP.get("F. Pond.");
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

                  for (i = 0; i < m_TEXTOSMP[m_TipoSMP].length; i++)
                  {
                     if (i != 0)
                        m_Html.envia("<tr "+Cor+">\n");
                     m_Html.envia("<td align=\"left\"><font face="+m_FONTE+">"
                                   +(m_TEXTOSMP[m_TipoSMP][i].equals("Percentual ") ? m_TEXTOSMP[m_TipoSMP][i] + Ag.m_TipoIndicador: m_TEXTOSMP[m_TipoSMP][i])                                      
                                   +"</font></td>");

                     for (int k = 0; k < Ag.m_PeriodosApresentaveis.size(); k++)
                     {
                        Posicao = (Integer)Ag.m_IndicadoresAnatelSMP.get(m_Indicadores_Rel[m_TipoSMP][1][i]);
                        if (Posicao == null)
                        {
                           m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">--</font></td>");
                           System.out.println(">>> Posicao NAO encontrada ("+m_Indicadores_Rel[m_TipoSMP][1][i]+")");
                        }
                        else
                        {
                           m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"
                                         +(LinhaRelProcessado[k] != null ? LinhaRelProcessado[k].elementAt(Posicao.intValue()).toString() : "-")
                                         +"</font></td>");
                        }
                     }
                                      
                     if (i != m_TEXTOSMP[m_TipoSMP].length-1)
                        m_Html.envia("\n</tr>\n");
                  }
               }
               Cont++;
            }
//            m_Html.envia("\n</tr>\n");
         }
         EnviouCentral = false;
         m_Html.envia("\n<tr>\n");
         m_Html.envia("<td colspan="+(4+Ag.m_PeriodosApresentaveis.size())+">&nbsp;</td>");
         m_Html.envia("\n</tr>\n");         
      }

      //
      // Resumo
      //

      // Títulos
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap colspan="+(4+Ag.m_PeriodosApresentaveis.size())+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Resumo - Indicador "+Ag.m_TipoIndicador
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
            
      m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
      m_Html.envia("<td align=\"center\" nowrap colspan=\"2\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Per&iacute;odos"
                    +"</b></font></td>");

      m_Html.envia("<td align=\"right\" nowrap colspan="+(2+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                    +"Indicador"
                    +"</b></font></td>");
      m_Html.envia("\n</tr>\n");
      //

      // Períodos e indicadores resumidos - Apresentacao = 2
      int iPos;
      String strAux, strRes;
      j = 0;
      for (i = 0; i < Ag.m_PeriodosApresentaveis.size(); i++)
      {
         iPos = 0;
         strAux = "";
         strRes = "";
               
         // Seleciona a cor de apresentação da linha
         if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
         else Cor = " bgcolor=\""+m_COR_LINHASIM3+"\"";
         j++;

         // Período
         if (Ag.m_NomesDatas != null)
            NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
         else
            NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

         m_Html.envia("<tr"+ Cor+">\n   ");
         // Coluna do período
         m_Html.envia("<td align=\"center\" colspan=\"2\"><font face="+m_FONTE+"><b>"
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
            strRes = "0";

         m_Html.envia("<td align=\"right\" nowrap nowrap colspan="+(2+m_Indicadores_Rel[m_TipoSMP][0].length)+"><font face="+m_FONTE+"><b>"
                       +strRes
                       +"</b></font></td>");
         m_Html.envia("\n</tr>\n");
      }
   }

   public void trocaPeriodo()
   {
      AgendaAnatelSMP Ag = null;

      Ag = buscaAgenda();
      if (Ag == null)
      {
         enviaPaginaErro();
         return;
      }
      Ag.m_Pag = 0;
      apresentaAnatelSMP();
   }

   /**
    * @return void
    * @exception 
    * Apresenta a página baserelatorioesquerda.htm
    * @roseuid 3F212FFA0045
    */
   public void apresentaBaseRelatorio()
   {

      try
      {
         AgendaAnatelSMP Ag;
         String Paginacao = "";
        
         Ag = buscaAgenda();
         if (Ag == null)
         {
            enviaPaginaErro();
            return;
         }

         iniciaArgs(9);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "baserelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = Ag.m_TipoApresentacao;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaBaseRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }


   public void apresentaRecursos()
   {
      try
      {
         AgendaAnatelSMP Ag;
         Iterator It;
         Map.Entry Ent;
         Object Obj;
         String NomeRecurso = "";
         String Recurso, SelectRecursos = "", RecursosSelecionados = "";
         int iTipoRecurso = 0;
         String TipoRecurso = m_Request.getParameter("tiporecurso");
         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         if (TipoRecurso.equals("1") == true) // ROTAE
         {
            NomeRecurso = Ag.m_TituloRecurso[0];
            iTipoRecurso = 0;
            // Insere o Todos no início da lista
            SelectRecursos += "            <option value=\"" + m_TODOS + "\">" + m_TODOS + "</option>\n";
         }
         else if (TipoRecurso.equals("2") == true) // BILHETADOR
         {
            NomeRecurso = Ag.m_TituloRecurso[1];
            iTipoRecurso = 1;
            // Insere o Todos no início da lista
            SelectRecursos += "            <option value=\"" + m_TODOS + "\">" + m_TODOS + "</option>\n";            
         }
         else if (TipoRecurso.equals("3") == true)
         {
            NomeRecurso = Ag.m_TituloRecurso[2];
            iTipoRecurso = 2;
            // Insere o Todos no início da lista
            SelectRecursos += "            <option value=\"" + m_TODOS + "\">" + m_TODOS + "</option>\n";
         }

         RecursosSelecionados = m_Request.getParameter("recursosselecionados");

         //
         // Varre a lista de recursos para preencher a lista
         //
         if (Ag.m_VecRecursos[iTipoRecurso] == null)
         {
            // Cria cache de recursos
            Ag.m_VecRecursos[iTipoRecurso] = new Vector();

            if (iTipoRecurso == 0)
               Ag.m_VecRecursos[iTipoRecurso].add(m_TODOS);
            
            It = Ag.m_MapRecursos[iTipoRecurso].entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry)It.next();
               Obj = Ent.getValue();
               Recurso = Obj.toString();

               if (Recurso.equals(m_TODOS) == false)
               {
                  SelectRecursos += "            <option value=\"" + Recurso + "\">" + Recurso + "</option>\n";
                  Ag.m_VecRecursos[iTipoRecurso].add(Recurso);                  
               }
            }
         }
         else
         {
            int i, iTam = Ag.m_VecRecursos[iTipoRecurso].size();
            if (iTipoRecurso == 0) 
               i = 1;
            else
               i = 0;               

            for (; i < iTam; i++)
               SelectRecursos += "            <option value=\"" + Ag.m_VecRecursos[iTipoRecurso].elementAt(i) + "\">" + Ag.m_VecRecursos[iTipoRecurso].elementAt(i) + "</option>\n";
         }

         iniciaArgs(13);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "recursos.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = RecursosSelecionados;
         m_Args[9] = TipoRecurso;
         m_Args[10] = Ag.m_TipoApresentacao;
         m_Args[11] = NomeRecurso;
         m_Args[12] = SelectRecursos;
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaRecursos(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void alteraRecursos() 
   {
      try
      {
         AgendaAnatelSMP Ag = null;
         String Recursos1 = null, Recursos2 = null, Recursos3 = null;
         String TipoRecurso;

         Ag = buscaAgenda();

         Recursos1 = m_Request.getParameter("recursos1");
         Recursos2 = m_Request.getParameter("recursos2");

         Ag.setMapRecursoNovo(0,Recursos1);
         Ag.setMapRecursoNovo(1,Recursos2);

         if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
         {
            Recursos3 = m_Request.getParameter("recursos3");
            Ag.setMapRecursoNovo(2,Recursos3);
         }
  
         apresentaAnatelSMP();
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:alteraRecursos(): "+ Exc);
         Exc.printStackTrace();
      }
   }   

   /**
    * @return void
    * @exception 
    * @roseuid 3F2FE7990131
    */
   public void apresentaSelecaoDePaginas() 
   {
      try
      {
         iniciaArgs(8);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "selecaopaginas.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaSelecaoDePaginas(): "+ Exc);
         Exc.printStackTrace();
      }   
   }

   public void apresentaLogs()
   {
     try
      {
         AgendaAnatelSMP Ag;
         int i, k;
         String strTipoAp, strAux, strLogs = "", Cabecalho = "", NomePeriodo;
         Vector Log;

         Ag = buscaAgenda();
         if (Ag == null)
            enviaPaginaErro();

         for (i = 0; i < Ag.m_Cabecalho.size(); i++)
         {
            strAux = (String)Ag.m_Cabecalho.elementAt(i);
            Cabecalho += "   <tr>\n      <td><font face="+m_FONTE+"><b>"+strAux.substring(0, strAux.indexOf(':')+1)+"</b>&nbsp;";
            Cabecalho += strAux.substring(strAux.indexOf(':')+1, strAux.length())+"</font></td>\n   </tr>\n";
         }

         strTipoAp = Ag.m_TipoApresentacao;
         //
         // !!!! Forçando para tipo 2!!!
         //
         strTipoAp = "2";

         if (strTipoAp.equals("2"))
         {
            i = 0;
            k = Ag.m_PeriodosApresentaveis.size();
         }
         else
         {
            i = Ag.m_UltProcessado;
            k = Ag.m_UltProcessado+1;
         }

         for (; i < k; i++)
         {
            if (Ag.m_NomesDatas != null) 
               NomePeriodo = Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();
            else 
               NomePeriodo = Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue()).toString();

            Log = (Vector)Ag.m_Logs.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(i)).intValue());

            strLogs += "   <tr>\n      <td><font face="+m_FONTE+"><b>Per&iacute;odo: </b>"+NomePeriodo+"</font></td>\n   </tr>\n";
            for (int j = 0; j < Log.size(); j++)
            {
               if (j == 0 && Log.elementAt(j).toString().equals("nolog") == true)
                  strLogs += "   <tr>\n      <td><font face="+m_FONTE+">N&atilde;o h&aacute; logs para este per&iacute;odo</font></td>\n   </tr>\n";
               else
                  strLogs += "   <tr>\n      <td><font face="+m_FONTE+">"+Log.elementAt(j).toString().substring(0, Log.elementAt(j).toString().length())+"</font></td>\n   </tr>\n";
            }
            strLogs += "   <tr>\n      <td>&nbsp;</td>\n   </tr>\n";
         }

         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "logs.htm";
         m_Args[2] = Cabecalho;
         m_Args[3] = strLogs;
         m_Html.enviaArquivo(m_Args);         
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaLogs(): "+ Exc);
         Exc.printStackTrace();
      }   
   }

   public void alteraIndicadores()
   {
      try
      {
         AgendaAnatelSMP Ag = null;
         String Indicadores = m_Request.getParameter("indicadores");
   
         Ag = buscaAgenda();
         if (Ag != null)
         {
            Indicadores = Indicadores.replace('@', '%');
            if (Ag.m_Rec[1] != null)
               Indicadores = Ag.m_vIndicadores.elementAt(0).toString()+";"+ Ag.m_vIndicadores.elementAt(1).toString()+";"+Indicadores;
            else
               Indicadores = Ag.m_vIndicadores.elementAt(0).toString()+";"+Indicadores;

            Ag.m_vIndicadores = VetorUtil.String2Vetor(Indicadores, ';');
            Ag.remontaRelatorios((short)m_TipoSMP);
            Ag.m_Pag = 0;
            apresentaAnatelSMP();
         }
         else
         {
            enviaPaginaErro();
         }
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:alteraIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaConfiguracao()
   {
      try
      {
         AgendaAnatelSMP Ag;
         String Periodos = "", Texto = "";
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = NoUtil.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         Vector Configuracao = UsuarioAux.getConfWebAgenda();

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);
            return;
         }

         iniciaArgs(15);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "configuracao.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8]  = (String)Configuracao.elementAt(0); // Tipo de visualização
         m_Args[9]  = (String)Configuracao.elementAt(2); // Cabeçalho
         m_Args[10] = (String)Configuracao.elementAt(3); // Logs
         
         m_Args[11] = "AnatelSMP";

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:apresentaConfiguracao(): "+ Exc);
         Exc.printStackTrace();
      }    
   }

   public void alteraConfiguracao() 
   {
      UsuarioDef Usuario = null;
      AgendaAnatelSMP Ag = null;      

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      if (Usuario != null)
      {
         Ag = buscaAgenda();
         if (Ag == null)
            return;

         String Configuracao = m_Request.getParameter("configuracao");
         Usuario.setConfWebAgenda(Configuracao);

         Usuario.getNo().getConexaoServUtil().alteraUsuario(Usuario);
      }
      else
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:alteraConfiguracao(): Usuario nao foi encontrado");
   }

   public void download()
   {
      try
      {
         AgendaAnatelSMP Ag = null;
         int iQtdRecursos = 2;
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         String NomeArq;
         Vector vCabecalho;
         
         
         Ag = buscaAgenda();

         if (Ag.m_TipoSMP == Agenda.ANATEL_LDN)
            iQtdRecursos = 3;

         m_ListaNova   = new boolean[iQtdRecursos];
         for (int i = 0; i < iQtdRecursos; i++)
         {
            if (Ag.m_MapRecursosNovos[i].size() == 0)
               m_ListaNova[i] = false;
            else
               m_ListaNova[i] = true;
         }         

         NomeArq = Ag.m_NomeRelatorio + "-"+m_KeyCache;
         NomeArq = NomeArq.replace(' ', '_');
        
         vCabecalho = new Vector (Ag.m_Cabecalho);

         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_anatelsmp/";
         m_Args[1] = "download.htm";
         if (Ag.m_GeraArqDownload != 3){
        	 m_Args[2] += "<meta http-equiv=\"refresh\" content=\"5\"/>\n";
         }
         else
            m_Args[2] = "";
         m_Args[3] = Ag.m_NomeRelatorio;
         m_Args[4] = Ag.m_DataGeracao;
         switch (Ag.m_GeraArqDownload)
         {
            case 0:
            m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!";                        
           
            // Testar se Thread já existe, sem sim não crio outra até que a primeira termine a execução.
            if(!estadoThread){
            	estadoThread = true;
            	
            	// new DownloadRelAgendadoAnatelSMP(NomeArq, vCabecalho, Ag, UsuarioAux.getConfWebAgenda(), m_ListaNova);
            	DownloadRelAgendadoAnatelSMP download = new DownloadRelAgendadoAnatelSMP(NomeArq, vCabecalho, Ag, UsuarioAux.getConfWebAgenda(), m_ListaNova); 
            	 new Thread(download).start();
            }
           
            break;
         case 1:
            m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!";
            break;
         case 2:
            m_Args[5] = "Arquivo sendo compactado!";
            break;            
         case 3:
            m_Args[5]  = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n";
            m_Args[5] += "<tr>\n";
            m_Args[5] +=  "<td align=\"center\">Clique no &iacute;cone abaixo para efetuar o download!</td>\n";
            m_Args[5] +=  "</tr>\n";
            m_Args[5] += "<tr>\n";
            m_Args[5] +=  "<td align=\"center\"><a href=\"/"+DefsComum.s_ContextoWEB+"/download/"+NomeArq+(UsuarioAux.getConfWebAgenda().elementAt(0).equals("1") ? ".csv" : ".htm")+".zip\" ><img src=\"/PortalOsx/imagens/reldownload.gif\" border=\"0\"  ></a></td>\n";
            m_Args[5] +=  "</tr>\n";
            m_Args[5] +=  "</table>\n";
            Ag.m_GeraArqDownload = 0;
            break;
         case 4:
            m_Args[5] = "Erro na gera&ccedil;&atilde; do arquivo!";
            break;
         default:
            m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!<br>";
            m_Args[5] += "<br>Linha: "+(Ag.m_GeraArqDownload - Ag.m_PeriodosApresentaveis.size()*100000);
            break;            
         }  
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:download(): "+ Exc);
         Exc.printStackTrace();         
      }
   }

   public void removeAgenda()
   {
      removeAgenda(this);
   }

   public static synchronized void removeAgenda(OpApresentaRelAgendadoAnatelSMP p_Operacao)
   {
      //System.out.println("removeAgenda");
      try
      {
         AgendaAnatelSMP Ag = null;
         String NomeArq[] = null;
         String Tipos[] = {".csv", ".htm"};         

         Ag = buscaAgenda(p_Operacao);
         if (Ag != null)
         {
            NomeArq = new String[2];
            for (int i = 0; i < 2; i++)
            {
               NomeArq[i] = Ag.m_NomeRelatorio + "-"+ p_Operacao.m_KeyCache + Tipos[i] +".zip";
               NomeArq[i] = NomeArq[i].replace(' ', '_');
               new java.io.File(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+NomeArq[i]).delete();
            }
         }

         s_MapAgendasAnatelSMP.remove(p_Operacao.m_KeyCache);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:removeAgenda(): Nao foi possivel remvoer a agenda com chave: "+p_Operacao.m_KeyCache);
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnatelSMP:removeAgenda(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public AgendaAnatelSMP buscaAgenda() 
   {
       if (m_ApagarRelatorio)
       {
           UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
           Agenda.fechaRelatoriosUsuario(Usuario);
       }
       AgendaAnatelSMP Ag = buscaAgenda(this);
       
       switch (m_TipoSMP)
       {
          case Agenda.ANATEL_SMP3:
             Ag.m_TipoIndicador = "SMP3";
             break;
          case Agenda.ANATEL_SMP5:
             Ag.m_TipoIndicador = "SMP4";
             break;               
          case Agenda.ANATEL_SMP6:
             Ag.m_TipoIndicador = "SMP6";
             break;              
          case Agenda.ANATEL_SMP7:
             Ag.m_TipoIndicador = "SMP7";
             break;                              
          case Agenda.ANATEL_LDN:
             Ag.m_TipoIndicador = "LDN";
            // iQtdRecursos = 3;
             break;
          case Agenda.ANATEL_SMP3_ERICSSON:
             Ag.m_TipoIndicador = "SMP3";
             break;
          case Agenda.ANATEL_SMP5_ERICSSON:
              Ag.m_TipoIndicador = "SMP5";
              break;
          case Agenda.ANATEL_SMP6_ERICSSON:
              Ag.m_TipoIndicador = "SMP6";
              break;
          case Agenda.ANATEL_SMP7_ERICSSON:
              Ag.m_TipoIndicador = "SMP7";
              break;
          case Agenda.ANATEL_STFC:
              Ag.m_TipoIndicador = "STFC";
              break;
          case Agenda.ANATEL_IDDF:
              Ag.m_TipoIndicador = "IDDF";
              break;
       }
       
      return Ag;
   }

   public static synchronized AgendaAnatelSMP buscaAgenda(OpApresentaRelAgendadoAnatelSMP p_Operacao)
   {
      Object ObjAg = null;
      AgendaAnatelSMP Ag = null;

      //System.out.println("m_KeyCache:= " + p_Operacao.m_KeyCache);
      
      if (s_MapAgendasAnatelSMP.size() == 0)
      {
         Ag = p_Operacao.criaAgenda();
         return Ag;
      }

      ObjAg = s_MapAgendasAnatelSMP.get(p_Operacao.m_KeyCache);
      if (ObjAg != null)
      {
         Ag = (AgendaAnatelSMP)ObjAg;
      }
      else
      {
         Ag = p_Operacao.criaAgenda();
      }
      return Ag;
   }

   public void insereAgenda(AgendaAnatelSMP p_Agenda) 
   {
      synchronized(s_MapAgendasAnatelSMP)
      {
         s_MapAgendasAnatelSMP.put(m_KeyCache, p_Agenda);
      }
   }

   public AgendaAnatelSMP criaAgenda() 
   {
      boolean Retorno;
      AgendaAnatelSMP Ag;
      UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
      
      Agenda.fechaRelatoriosUsuario(Usuario);

      No no = NoUtil.buscaNobyNomePerfil(m_Perfil);
      
      switch (m_TipoSMP)
      {
         case AgendaAnatelSMP.ANATEL_SMP3:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP3(m_SelecaoRecursosAnatelSMP3, m_SelecaoIndicadorAnatelSMP3, m_IndicadoresAnatelSMP3);
               break;            
         case AgendaAnatelSMP.ANATEL_SMP3e4:
              Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
              Ag.setAnatelSMP3e4(m_SelecaoRecursosAnatelSMP3e4, m_SelecaoIndicadorAnatelSMP3e4, m_IndicadoresAnatelSMP3e4);
              break;  
         case AgendaAnatelSMP.ANATEL_SMP5:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP5(m_SelecaoRecursosAnatelSMP5, m_SelecaoIndicadorAnatelSMP5, m_IndicadoresAnatelSMP5);
               break;
         case AgendaAnatelSMP.ANATEL_SMP6:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP6(m_SelecaoRecursosAnatelSMP6, m_SelecaoIndicadorAnatelSMP6, m_IndicadoresAnatelSMP6);
               break;
         case AgendaAnatelSMP.ANATEL_SMP7:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP7(m_SelecaoRecursosAnatelSMP7, m_SelecaoIndicadorAnatelSMP7, m_IndicadoresAnatelSMP7);
               break;
         case AgendaAnatelSMP.ANATEL_LDN:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelLDN(m_SelecaoRecursosAnatelLDN, m_SelecaoIndicadorAnatelLDN, m_IndicadoresAnatelLDN);
               break;
         case AgendaAnatelSMP.ANATEL_SMP3_ERICSSON:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP3Ericsson(m_SelecaoRecursosAnatelSMP3Ericsson, m_SelecaoIndicadorAnatelSMP3Ericsson, m_IndicadoresAnatelSMP3Ericsson);
               break;            
         case AgendaAnatelSMP.ANATEL_SMP5_ERICSSON:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP5Ericsson(m_SelecaoRecursosAnatelSMP5Ericsson, m_SelecaoIndicadorAnatelSMP5Ericsson, m_IndicadoresAnatelSMP5Ericsson);
               break;
         case AgendaAnatelSMP.ANATEL_SMP6_ERICSSON:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP6Ericsson(m_SelecaoRecursosAnatelSMP6Ericsson, m_SelecaoIndicadorAnatelSMP6Ericsson, m_IndicadoresAnatelSMP6Ericsson);
               break;
         case AgendaAnatelSMP.ANATEL_SMP7_ERICSSON:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP7Ericsson(m_SelecaoRecursosAnatelSMP7Ericsson, m_SelecaoIndicadorAnatelSMP7Ericsson, m_IndicadoresAnatelSMP7Ericsson);
               break;
         case AgendaAnatelSMP.ANATEL_SMP8e9:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP3(m_SelecaoRecursosAnatelSMP3, m_SelecaoIndicadorAnatelSMP3, m_IndicadoresAnatelSMP3);
               break;  
         case AgendaAnatelSMP.ANATEL_IDDF:
             Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
             Ag.setAnatelIDDF(m_SelecaoRecursosAnatelIDDF, m_SelecaoIndicadorAnatelIDDF, m_IndicadoresAnatelIDDF);
             break; 
         case AgendaAnatelSMP.ANATEL_STFC:
             Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
             Ag.setAnatelSTFC(m_SelecaoRecursosAnatelSTFC, m_SelecaoIndicadorAnatelSTFC, m_IndicadoresAnatelSTFC);
             break; 
         default:
               Ag = new AgendaAnatelSMP(no.getConexaoServUtil(), m_Perfil, m_TipoRelatorio, m_IdRelatorio, m_NomeArquivoRelatorio, m_NomeRelatorio,m_DataGeracao);
               Ag.setAnatelSMP3(m_SelecaoRecursosAnatelSMP3, m_SelecaoIndicadorAnatelSMP3, m_IndicadoresAnatelSMP3);
               break;
      }

      Ag.setQtdLinhas(UsuarioAux.getConfWebAgenda().elementAt(1).toString());
      // Insere agenda no cache
      insereAgenda(Ag);   
      
      if (!Usuario.getM_ListAgendasUsuario().contains(Ag))
      {
          Usuario.getM_ListAgendasUsuario().add(Ag);
      }
      
      return Ag;
   }

   public void enviaPaginaErro()
   {
      iniciaArgs(2);
      m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
      m_Args[1] = "relatorioinexistente.htm";
      m_Html.enviaArquivo(m_Args);            
   }

class DownloadRelAgendadoAnatelSMP implements Runnable
   {

	/**
	 * 
	 * @uml.property name="m_Ag"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	
	AgendaAnatelSMP m_Ag = null;

      boolean m_ListaNova[];
      String m_NomeArq;      
      Vector m_vCabecalho;
      Vector m_CfgWeb;

      public DownloadRelAgendadoAnatelSMP(String p_NomeArq, Vector p_vCabecalho, AgendaAnatelSMP p_Ag, Vector p_CfgWeb, boolean p_ListaNova[])
      {
         m_Ag = p_Ag;
         m_NomeArq = p_NomeArq;
         m_vCabecalho = p_vCabecalho;
         m_CfgWeb = p_CfgWeb;
         m_ListaNova = p_ListaNova;
         //start();
      }
      
      public void run()
      {
         Set ListaRecursos[] = null;

         try
         {
            int iQtdRecursos = 2;

            if (m_TipoSMP == AgendaAnatelSMP.ANATEL_LDN)
               iQtdRecursos = 3;
               
            ListaRecursos = new Set[iQtdRecursos];
            for (int i = 0; i < iQtdRecursos; i++)
            {
               if (m_Ag.m_MapRecursosNovos[i].size() == 0)
                  ListaRecursos[i] = m_Ag.m_MapRecursos[i].entrySet();
               else
                  ListaRecursos[i] = m_Ag.m_MapRecursosNovos[i].entrySet();      
            }
    
            m_Ag.m_GeraArqDownload = 1;
            
            m_Ag.criaArquivoAnatelSMP(m_NomeArq,
                                      m_vCabecalho,
                                      m_Ag.m_TituloRecurso,
                                      m_Indicadores_Rel[m_TipoSMP][Integer.parseInt(m_Ag.m_TipoApresentacao) - 1],
                                      m_Ag.m_PeriodosApresentaveis,
                                      m_Ag.m_NomesDatas,
                                      m_Ag.m_Datas,
                                      ListaRecursos,
                                      m_Ag.m_MapRelatorios, 
                                      m_Ag.m_Logs, 
                                      m_Ag.m_TipoApresentacao, 
                                      m_TEXTOSMP_Download[m_TipoSMP],
                                      m_Ag.m_IndicadoresAnatelSMP,
                                      m_Ag.m_ExcluiLinhas,
                                      m_Num_Dem[m_TipoSMP],
                                      m_Ag.m_TipoIndicador,
                                      m_ContReferencia[m_TipoSMP],
                                      m_CfgWeb,
                                      m_TipoSMP,
                                      m_ListaNova);

            m_Ag.m_GeraArqDownload = 2;
            Arquivo Arq = new Arquivo();
            Arq.zipaArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+m_NomeArq + (m_CfgWeb.elementAt(0).equals("1") ? ".csv": ".htm"));
            //System.out.println("Vai deletar o CSV: "+NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+m_NomeArq);
            new java.io.File(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+m_NomeArq).delete();
            m_Ag.m_GeraArqDownload = 3;
            estadoThread = false;
         }
         catch (Exception Exc)
         {
            Exc.printStackTrace();
            estadoThread = false;
         }
      }
   };   
}
