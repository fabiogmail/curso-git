//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApresentaRelHistoricoTrafego.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Utils.HistoricoTrafego;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 */
public class OpApresentaRelHistoricoTrafego extends OperacaoAbs 
{
   private String m_SubOperacao;
   private String m_NomeRelatorio;
   private String m_DataGeracao;
   private String m_Perfil;
   private String m_TipoRelatorio;
   private String m_IdRelatorio;
   private String m_NomeArquivoRelatorio;
   private String m_KeyCache;
   private final short m_QtdFiltros = 5;
   private int m_QtdLinhasPagina = 19;
   
   static 
   {
   }
   
   public OpApresentaRelHistoricoTrafego() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3F212FF90378
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      UsuarioDef Usuario = null;   
      setOperacao("Apresenta&ccedil;&atilde;o de Relat&oacute;rio de Tr&aacute;fego Hist&oacute;rico");

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      setQtdLinhas((String)Usuario.getConfWebNormal().elementAt(1));

      String PosAtual = m_Request.getParameter("posatual");
      // Recupera os argumentos passados pela página
      m_SubOperacao          = m_Request.getParameter("suboperacao");
      m_NomeRelatorio        = m_Request.getParameter("nomerel");
      m_DataGeracao          = m_Request.getParameter("datageracao");
      m_Perfil               = m_Request.getParameter("perfil");
      m_TipoRelatorio        = m_Request.getParameter("tiporel");
      m_IdRelatorio          = m_Request.getParameter("idrel");
      m_NomeArquivoRelatorio = m_Request.getParameter("arquivo");

      m_NomeRelatorio = m_NomeRelatorio.replace('@', ' ');

      // Monta a chave para inserção e busca na cache de históricos
      m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_NomeRelatorio + "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" + m_Request.getSession().getId();

      System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistorico:iniciaOperacao() - Suboperacao: "+ m_SubOperacao);
      if (m_SubOperacao.toLowerCase().equals("paginicial"))
         apresentaPagInicial();
      else if (m_SubOperacao.toLowerCase().equals("paginicial2"))
         apresentaPagInicial2();
      else if (m_SubOperacao.toLowerCase().equals("relatorio"))
         apresentaRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("meiorelatorio"))
         apresentaMeioRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("toporelatorio"))
         apresentaTopoRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("baserelatorio"))
         apresentaBaseRelatorio();
      else if (m_SubOperacao.toLowerCase().equals("pagperiodos"))         
         apresentaPeriodos();
      else if (m_SubOperacao.toLowerCase().equals("pagconfiguracao"))
         apresentaConfiguracao();
      else if (m_SubOperacao.toLowerCase().equals("pagrecursos"))
         apresentaRecursos();
      else if (m_SubOperacao.toLowerCase().equals("pagselecaopaginas"))
         apresentaSelecaoDePaginas();
      else if (m_SubOperacao.toLowerCase().equals("pagrelatorio"))
         apresentaRelatorioFinal();
      else if (m_SubOperacao.toLowerCase().equals("alterarecursos"))
         alteraRecursos();
      else if (m_SubOperacao.toLowerCase().equals("alteraperiodos"))
         alteraPeriodos();
      else if (m_SubOperacao.toLowerCase().equals("alteraconfiguracao"))
         alteraConfiguracao();
      else if (m_SubOperacao.toLowerCase().equals("navegacao"))
         navega();
      else if (m_SubOperacao.toLowerCase().equals("ordenacao"))
         ordena();
      else if (m_SubOperacao.toLowerCase().equals("home"))
         home();
      else
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistorico:iniciaOperacao() - Suboperacao nao encontrada: "+ m_SubOperacao);
      return true;
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página index.htm
    * @roseuid 3F212FF9038C
    */
   public void apresentaPagInicial() 
   {
      try
      {
         iniciaArgs(9);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "index.htm";
         m_Args[2] = m_NomeRelatorio + " - " + m_DataGeracao;
         m_Args[3] = m_Perfil;
         m_Args[4] = m_TipoRelatorio;
         m_Args[5] = m_IdRelatorio;
         m_Args[6] = m_NomeArquivoRelatorio;
         m_Args[7] = m_NomeRelatorio;
         m_Args[8] = m_DataGeracao;
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaPagInicial(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página index2.htm
    * @roseuid 3F212FF903A0
    */
   public void apresentaPagInicial2() 
   {
      try
      {
         iniciaArgs(9);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "index2.htm";
         m_Args[2] = m_NomeRelatorio + " - " + m_DataGeracao;
         m_Args[3] = m_Perfil;
         m_Args[4] = m_TipoRelatorio;
         m_Args[5] = m_IdRelatorio;
         m_Args[6] = m_NomeArquivoRelatorio;
         m_Args[7] = m_NomeRelatorio;
         m_Args[8] = m_DataGeracao;
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaPagInicial2(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página relatorio.htm
    * @roseuid 3F212FF903B4
    */
   public void apresentaRelatorio() 
   {
      try
      {
         iniciaArgs(20);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "relatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         m_Args[8] = m_Perfil;
         m_Args[9] = m_TipoRelatorio;
         m_Args[10] = m_IdRelatorio;
         m_Args[11] = m_NomeArquivoRelatorio;
         m_Args[12] = m_NomeRelatorio;
         m_Args[13] = m_DataGeracao;

         m_Args[14] = m_Perfil;
         m_Args[15] = m_TipoRelatorio;
         m_Args[16] = m_IdRelatorio;
         m_Args[17] = m_NomeArquivoRelatorio;
         m_Args[18] = m_NomeRelatorio;
         m_Args[19] = m_DataGeracao;         

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página relatorioesquerda.htm
    * @roseuid 3F212FF903C8
    */
   public void apresentaRelatorioEsquerda() 
   {
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página relatoriodireita.htm
    * @roseuid 3F212FF903DD
    */
   public void apresentaRelatorioDireita() 
   {
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página toporelatorioesquerda.htm
    * @roseuid 3F212FFA0009
    */
   public void apresentaTopoRelatorio() 
   {
      try
      {
         HistoricoTrafego Hist = null;
         int i, iTam;
         String TipoRecurso[] = {"Central", "Rede", "Gerência", "Holding", "Prestadora"};
         String Recursos[] = {null, null, null, null, null};
         String SelectRecurso[] = {"",  "", "", "", ""};
         String RecursosSelecionados[] = {null, null, null, null, null};
         String TipoRecursoAux, Aux;
         Vector ListaRecurso[] = {null, null, null, null, null};

         Hist = buscaHistorico();
         if (Hist == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         for (i = 0; i < m_QtdFiltros; i++)
         {
            ListaRecurso[i] = Hist.fnListaRecursosVec(i);

            Aux = "recursos"+(i+1);
            Recursos[i] = m_Request.getParameter(Aux);
            if (Recursos[i] == null)
               Recursos[i] = "Sem Sele&ccedil;&atilde;o";
               
            RecursosSelecionados[i] = Recursos[i];
            if (RecursosSelecionados[i] == null || RecursosSelecionados[i].length() == 0)
               RecursosSelecionados[i] = "Sem Sele&ccedil;&atilde;o";
         }

         TipoRecursoAux = m_Request.getParameter("tiporecurso");
         if (TipoRecursoAux != null)
         {
            int Indice = Integer.parseInt(TipoRecursoAux)-1;
            RecursosSelecionados[Indice] = Recursos[Indice];
            if (RecursosSelecionados[Indice].equals("-"))
               RecursosSelecionados[Indice] = "Sem Sele&ccedil;&atilde;o";            
//System.out.println("TipoRecursoAux: "+TipoRecursoAux + " - " + Indice);
//System.out.println("TipoRecursoAux: "+ Indice + " - "+RecursosSelecionados[Indice]);
         }
/*
         if (TipoRecurso != null && TipoRecurso.equals("1") == true)
            RecursosSelecionados1 = m_Request.getParameter("recursos");
         else RecursosSelecionados2 = m_Request.getParameter("recursos");

         if (RecursosSelecionados1 == null)
            RecursosSelecionados1 = Recursos1;
         if (RecursosSelecionados1 == null || RecursosSelecionados1.length() == 0)
            RecursosSelecionados1 = ListaRecurso1.elementAt(0).toString();
*/
         for (i = 0; i < m_QtdFiltros; i++)
         {
            if (RecursosSelecionados[i].equals("Sem Sele&ccedil;&atilde;o") == false && RecursosSelecionados[i].length() > 15)
               Recursos[i] = RecursosSelecionados[i].substring(0, 15) + "...";
            else Recursos[i] = RecursosSelecionados[i];
         
            SelectRecurso[i]  = "<b>" + TipoRecurso[i] + ":</b>&nbsp;<a href=\"";
            SelectRecurso[i] += "javascript:AbreJanela('"+(i+1)+"','" + RecursosSelecionados[i] + "')";
            SelectRecurso[i] += "\" class=\"link\">" + Recursos[i] + "</a>";
         }

         iniciaArgs(19);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "toporelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         
         for (i = 0; i < m_QtdFiltros; i++)
         {
            if (Recursos[i].equals("Sem Sele&ccedil;&atilde;o") == false)
               m_Args[i+8] = RecursosSelecionados[i];
            else
               m_Args[i+8] = "";
         }
         
         m_Args[13]  = "<font face=\"verdana\" size=\"1\">" + m_NomeRelatorio + "&nbsp;&nbsp;&nbsp;&nbsp;";
         m_Args[13] += "-&nbsp;&nbsp;&nbsp;&nbsp;&Uacute;ltima Execu&ccedil;&atilde;o:&nbsp;" + m_DataGeracao + "</font>";
        
         for (i = 0; i < m_QtdFiltros; i++)
            m_Args[14+i] = SelectRecurso[i];

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistorico:apresentaTopoRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @param p_Hist
    * @return void
    * @exception 
    * Apresenta a página relatorioesquerda.htm
    * @roseuid 3F280CEA00B3
    */
   public void apresentaMeioRelatorio(HistoricoTrafego p_Hist) 
   {
      try
      {
         // Verifica se há mudança de página
         // Valores possíveis: "proxima"
         //                    "anterior"
         String TipoNavegacao = m_Request.getParameter("navega");
         int QtdPaginas = p_Hist.m_LinhasUltimaPesquisa.size()/m_QtdLinhasPagina;
         int Tam = p_Hist.m_LinhasUltimaPesquisa.size();
         int Posicao = 0;

         if (TipoNavegacao != null && TipoNavegacao.equals("proxima"))
         {
//System.ou
            if (p_Hist.m_Pag < QtdPaginas) p_Hist.m_Pag++;
            Posicao = p_Hist.m_Pag*m_QtdLinhasPagina;
         }
         else if (TipoNavegacao != null && TipoNavegacao.equals("anterior"))
         {
            if (p_Hist.m_Pag > 0) p_Hist.m_Pag--;
            Posicao = p_Hist.m_Pag*m_QtdLinhasPagina;
         }
         else if (TipoNavegacao != null)
         {
            int Pag = p_Hist.m_Pag;
            try
            {
               p_Hist.m_Pag = Integer.parseInt(TipoNavegacao) - 1;
               if (p_Hist.m_Pag > QtdPaginas) p_Hist.m_Pag = QtdPaginas;
            }
            catch (Exception Exc)
            {
               p_Hist.m_Pag = Pag;
            }
            Posicao = p_Hist.m_Pag*m_QtdLinhasPagina;         
         }
         else
         {
            Posicao = p_Hist.m_Pag*m_QtdLinhasPagina;
         }

         m_Html.enviaInicio("", "", "historicotrafegorelatorio.js");
         m_Html.envia("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
         m_Html.envia(p_Hist.fnHeader(true));
         int c = 0;
         for (int i = Posicao; i < Posicao + m_QtdLinhasPagina && i < Tam; i ++)
            m_Html.envia(p_Hist.fnLinha(c++, i));

         m_Html.envia("</table>\n");
         m_Html.envia("</body>\n");
         m_Html.enviafinal(null, null, true);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistorico:apresentaRelatorioDireita(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta a página relatorioesquerda.htm
    * @roseuid 3F212FFA001D
    */
   public void apresentaMeioRelatorio() 
   {
      try
      {
         HistoricoTrafego Hist = null;
         
         Hist = buscaHistorico();
         if (Hist == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }
         apresentaMeioRelatorio(Hist);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistorico:apresentaRelatorioDireita(): "+ Exc);
         Exc.printStackTrace();
      }
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
         HistoricoTrafego Hist;
         String Paginacao = "";

         Hist = buscaHistorico();
         if (Hist == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         Thread.sleep(600);

         Paginacao = (Hist.m_Pag + 1) + "/" + (((Hist.m_LinhasUltimaPesquisa.size()-1)/m_QtdLinhasPagina) + 1);
//System.out.println("Hist.m_LinhasUltimaPesquisa.size(): "+Hist.m_LinhasUltimaPesquisa.size());         
//System.out.println("m_QtdLinhasPagina: "+m_QtdLinhasPagina);         
         
         iniciaArgs(9);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "baserelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = Paginacao;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaBaseRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F212FFA0063
    */
   public void apresentaPeriodos() 
   {
      try
      {
         HistoricoTrafego Hist;
         int i, iQtdPeriodos;
         String Periodos1 = "", Periodos2 = "";

         iniciaArgs(10);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "periodo.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         Hist = buscaHistorico();
         if (Hist == null)
         {
            iniciaArgs(14);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico/";
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }
         
         for (i = 0; i < Hist.m_Datas.length; i++)
            Periodos1 += "                   <option value=\""+ Hist.m_Datas[i].fnConverte("dd/MM") +"\">" + Hist.m_Datas[i].fnConverte("dd/MM") + "</option>\n";

         if (Hist.m_Dias != null)
         {
            for (i = 0; i < Hist.m_Dias.length; i++)
               Periodos2 += "                   <option value=\"" + Hist.m_Datas[Hist.m_Dias[i]].fnConverte("dd/MM") + "\">" + Hist.m_Datas[Hist.m_Dias[i]].fnConverte("dd/MM") + "</option>\n";         
         }
         else
         {
            for (i = 0; i < Hist.m_Datas.length; i++)
               Periodos2 += "                   <option value=\""+ Hist.m_Datas[i].fnConverte("dd/MM") +"\">" + Hist.m_Datas[i].fnConverte("dd/MM") + "</option>\n";
         }
         
         m_Args[8] = Periodos1;
         m_Args[9] = Periodos2;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistorico:apresentaPeriodos(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F212FFA0077
    */
   public void apresentaConfiguracao() 
   {
      try
      {
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         Vector Configuracao = UsuarioAux.getConfWebNormal();

         iniciaArgs(21);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "configuracao.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         for (int i = 0; i < Configuracao.size(); i++)
            m_Args[i+8] = (String)Configuracao.elementAt(i);

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaConfiguracao(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * Apresenta o relatório numa nova janela no formato especificado pelo p_Formato
    * @roseuid 3F213636009A
    */
   public void apresentaRelatorioFinal() 
   {
      try
      {
         HistoricoTrafego Hist;
         int iPagInicial, iPagFinal, Posicao, Tam;         
         String Cabecalho = "";
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         Vector vCabecalho = null;
         Vector CfgHist = UsuarioAux.getConfWebNormal();
         String PagInicial = m_Request.getParameter("paginicial");
         String PagFinal = m_Request.getParameter("pagfinal");
         
         try
         {
            iPagInicial = Integer.parseInt(PagInicial) - 1;
         }
         catch (Exception Exc)
         {
            iPagInicial = 1;
         }

         try
         {
            iPagFinal = Integer.parseInt(PagFinal);
         }
         catch (Exception Exc)
         {
            iPagFinal = 1;
         }         

         if (CfgHist.elementAt(0).equals("1") == true)   // Tipo de visualização
            m_Response.setContentType("application/vnd.ms-excel");

         Hist = buscaHistorico();
         if (Hist == null)
         {
            iniciaArgs(14);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         if (CfgHist.elementAt(2).equals("1") == true)   // Cabeçalho
         {
            int i, iTam;
            String Aux1, Aux2;

            // Recupera o cabeçalho
            vCabecalho = new Vector (Hist.fnLinhasCabecalho());
            vCabecalho.remove(0);

            // Insere outras 
            vCabecalho.insertElementAt("Relat&oacute;rio: &nbsp;" + m_NomeRelatorio, 0);
            vCabecalho.insertElementAt("&Uacute;ltima Execu&ccedil;&atilde;o: &nbsp;" + m_DataGeracao, 1);
            
            Cabecalho += "<table border=\"0\">\n";
            iTam = vCabecalho.size();
            for (i = 0; i < iTam; i++)
            {
               Aux1 = (String)vCabecalho.elementAt(i);
               Aux2 = Aux1.substring(Aux1.indexOf(":")+2, Aux1.length());
               Aux1 = Aux1.substring(0, Aux1.indexOf(":")+1);        
         
               Cabecalho += "   <tr>\n";
               Cabecalho += "      <td><b>" + Aux1 + "</b> &nbsp;" + Aux2 + "</td>\n";
               Cabecalho += "   </tr>\n";         
            }
            Cabecalho += "   <tr>\n";
            Cabecalho += "      <td>&nbsp;</td>\n";
            Cabecalho += "   </tr>\n";
            Cabecalho += "</table>\n";
         }
         else Cabecalho  = "";
         Tam = Hist.m_LinhasUltimaPesquisa.size();

         m_Html.enviaInicio("CDRView", m_NomeRelatorio+" - "+m_DataGeracao, null);
         m_Html.envia("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

         m_Html.envia("<p>\n");
         m_Html.envia("<b>Visent - CDRView <sup>&reg;</sup></b><br>\n");
         m_Html.envia("<b><small><small>Copyright 1999 - 2003<small></small></b>\n");
         m_Html.envia("</p>\n");
         m_Html.envia(Cabecalho);
    
         Posicao = iPagInicial*m_QtdLinhasPagina;

         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
         m_Html.envia(Hist.fnHeader(false));
         int c = 0;
         for (int i = Posicao; i < iPagFinal*m_QtdLinhasPagina && i < Tam; i ++)
            m_Html.envia(Hist.fnLinha(c++, i));

         m_Html.envia("</table>\n");
         m_Html.envia("</body>\n");
         m_Html.enviafinal(null, null, true);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaRelatorioFinal(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F212FFA008B
    */
   public void apresentaRecursos() 
   {
      try
      {
         HistoricoTrafego Hist;
         int i, iTam;
         String TipoRecurso = m_Request.getParameter("tiporecurso");
         String Recurso = "", SelectRecursos = "", RecursosSelecionados = "";
         String TituloRecurso[] = {"Central", "Rede", "Gerência", "Holding", "Prestadora"};
         Vector ListaRecursos = null;

         Hist = buscaHistorico();
         if (Hist == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         Recurso = TituloRecurso[Integer.parseInt(TipoRecurso)-1];
         ListaRecursos = Hist.fnListaRecursosVec(Integer.parseInt(TipoRecurso)-1);

         RecursosSelecionados = m_Request.getParameter("recursosselecionados");
         iTam = ListaRecursos.size();
         for (i = 0; i < iTam; i++)
               SelectRecursos += "            <option value=\"" + ListaRecursos.elementAt(i) + "\">" + ListaRecursos.elementAt(i) + "</option>\n";
         
         iniciaArgs(12);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
         m_Args[1] = "recursos.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = TipoRecurso;
         m_Args[9] = RecursosSelecionados;         
         
         m_Args[10] = Recurso;
         m_Args[11] = SelectRecursos;         
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaRecursos(): "+ Exc);
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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"historico_reltrafego/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:apresentaSelecaoDePaginas(): "+ Exc);
         Exc.printStackTrace();
      }   
   }
   
   /**
    * @return void
    * @exception 
    * Altera os recursos do histórico
    * @roseuid 3F212FFA00A9
    */
   public void alteraRecursos()
   {
      int iIndice, Pos = 0;
      HistoricoTrafego Hist = null;
      String Recursos = null, Recurso[] = {null, null, null, null, null};

      Recursos = m_Request.getParameter("recursos");
      //System.out.println("Rec: " + Recursos);

      iIndice = Recursos.indexOf("@@");
      if (iIndice == -1)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:alteraRecursos(): 'Recursos' passados esta incorreto: "+Recursos);
         return;
      }

      do
      {
         Recurso[Pos] = Recursos.substring(0, iIndice);
         if (Recurso[Pos].equals("-")) Recurso[Pos] = null;
         Recursos = Recursos.substring(iIndice+2, Recursos.length());
         iIndice = Recursos.indexOf("@@");
         Pos++;
         if (iIndice == -1)
         {
            Recurso[Pos] = Recursos;
            if (Recurso[Pos].equals("-")) Recurso[Pos] = null;            
         }
      }  while (iIndice != -1);

      // Busca historico no cache
      Hist = buscaHistorico();
      if (Hist == null)
      {
         iniciaArgs(2);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "relatorioinexistente.htm";
         m_Html.enviaArquivo(m_Args);            
         return;
      }
      Hist.fnLinhas(Recurso[0], Recurso[1], Recurso[2], Recurso[3], Recurso[4]);
      Hist.m_Pag = 0;
      apresentaMeioRelatorio();
   }
   
   /**
    * @return void
    * @exception 
    * Altera os períodos do histórico
    * @roseuid 3F212FFA00BD
    */
   public void alteraPeriodos() 
   {
      int j = 0, aRepresDatas[];
      HistoricoTrafego Hist = null;      
      String Periodos = null;
      Vector Datas = null;

      // Recupera os periodos
      Periodos = m_Request.getParameter("periodos");
      Datas = VetorUtil.String2Vetor(Periodos, ';');

      aRepresDatas = new int[Datas.size()];

      Hist = buscaHistorico();
      if (Hist == null)
      {
         iniciaArgs(2);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "relatorioinexistente.htm";
         m_Html.enviaArquivo(m_Args);            
         return;
      }

      while (Datas.size() != 0)
      {
         for (int i = 0; i < Hist.m_Datas.length; i++)
         {
            if (Datas.elementAt(0).toString().equals(Hist.m_Datas[i].fnConverte("dd/MM")) == true)
            {
               aRepresDatas[j] = i;
               Datas.remove(0);
               j++;
               break;
            }
         }
      }

      Hist.m_Dias = aRepresDatas;
      apresentaMeioRelatorio();
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F212FFA0149
    */
   public void alteraConfiguracao() 
   {
      UsuarioDef Usuario = null;
      HistoricoTrafego Hist = null;      

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      if (Usuario != null)
      {
         Hist = buscaHistorico();
         if (Hist == null)
            return;
      
         String Configuracao = m_Request.getParameter("configuracao");
         //System.out.println(m_ConexUtil.getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:alteraConfiguracao(): " + Configuracao);

         setQtdLinhas(Configuracao.charAt(1)+"");
         for (int i = 3; i < 12; i++)
            Hist.m_Colunas[i-3] = Configuracao.charAt(i) != '0';

         Usuario.setConfWebNormal(Configuracao);
         Usuario.getNo().getConexaoServUtil().alteraUsuario(Usuario);
      }
      else
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:alteraConfiguracao(): Usuario nao foi encontrado");

      apresentaConfiguracao();
   }
   
   /**
    * @return void
    * @exception 
    * Realiza a navegação de páginas no relatório.
    * @roseuid 3F26D328000F
    */
   public void navega() 
   {
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F2803EF0342
    */
   public void ordena() 
   {
      HistoricoTrafego Hist = null;
      String Coluna = m_Request.getParameter("coluna");
      
      Hist = buscaHistorico();
      if (Hist == null)
      {
         iniciaArgs(2);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "relatorioinexistente.htm";
         m_Html.enviaArquivo(m_Args);            
         return;
      }

      Hist.m_Pag = 0;
      try
      {
         Hist.fnOrdena(Integer.parseInt(Coluna));
         apresentaMeioRelatorio(Hist);         
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:ordena(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F2803E5017B
    */
   public void home() 
   {
      HistoricoTrafego Hist = null;      
      
      Hist = buscaHistorico();
      if (Hist == null)
      {
         iniciaArgs(2);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "relatorioinexistente.htm";
         m_Html.enviaArquivo(m_Args);            
         return;
      }

      try
      {
         String Tipo = m_Request.getParameter("tipo");
         int QtdPaginas;

         if (Tipo.equals("inicio")) Hist.m_Pag = 0;
         else 
         {
            if (Hist.m_LinhasUltimaPesquisa.size()%m_QtdLinhasPagina == 0)
               QtdPaginas = Hist.m_LinhasUltimaPesquisa.size()/m_QtdLinhasPagina;
            else
               QtdPaginas = Hist.m_LinhasUltimaPesquisa.size()/m_QtdLinhasPagina + 1;
         
//            int QtdPaginas = Hist.m_LinhasUltimaPesquisa.size()/m_QtdLinhasPagina;

 
            Hist.m_Pag = QtdPaginas-1;
             

         }
         apresentaMeioRelatorio();
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelHistoricoTrafego:ordena(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   /**
    * @param p_Posicao
    * @return void
    * @exception 
    * @roseuid 3F2E7A25023C
    */
   public void setQtdLinhas(String p_Posicao) 
   {
      switch (Integer.parseInt(p_Posicao))
      {
         case 0:
            m_QtdLinhasPagina = 19;
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
   }
   
   /**
    * @return HistoricoTrafego
    * @exception 
    * Busca o objeto historico no cache e o retorna caso ele já se encontre na lista. Caso contrário, retorna null.
    * @roseuid 3F212FFA0167
    */
   public HistoricoTrafego buscaHistorico() 
   {
      return buscaHistorico(this);
   }
   
   /**
    * @param p_Operacao
    * @return HistoricoTrafego
    * @exception 
    * Busca o objeto historico no cache e o retorna caso ele já se encontre na lista. Caso contrário, retorna null.
    * @roseuid 3F212FFA017B
    */
   public static synchronized HistoricoTrafego buscaHistorico(OpApresentaRelHistoricoTrafego p_Operacao) 
   {
      Object ObjHist = null;
      HistoricoTrafego Hist = null;
      if (s_MapHistoricos.size() == 0)
      {
         Hist = p_Operacao.criaHistorico();
         return Hist;
      }

      ObjHist = s_MapHistoricos.get(p_Operacao.m_KeyCache);
      if (ObjHist != null)
      {
         Hist = (HistoricoTrafego)ObjHist;
      }
      else
      {
         Hist = p_Operacao.criaHistorico();
      }
      return Hist;
   }
   
   /**
    * @param p_Historico
    * @return void
    * @exception 
    * Insere um objeto Historico no cache de históricos.
    * @roseuid 3F212FFA01B7
    */
   public void insereHistorico(HistoricoTrafego p_Historico) 
   {        
      synchronized(s_MapHistoricos)
      {
         s_MapHistoricos.put(m_KeyCache, p_Historico);
      }    
   }
   
   /**
    * @return HistoricoTrafego
    * @exception 
    * Cria um objeto historico, realiza a leitura inicial do arquivo e o insere no cache de históricos.
    * @roseuid 3F212FFA01CB
    */
   public HistoricoTrafego criaHistorico() 
   {
      boolean Retorno;
      HistoricoTrafego Hist;
      UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());      
      UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());      

      Hist = new HistoricoTrafego(Usuario.getNo().getConexaoServUtil());
      Retorno = Hist.fnLeRelatorio(m_Perfil,
                                   m_TipoRelatorio,
                                   m_IdRelatorio,
                                   m_NomeArquivoRelatorio,
                                   m_NomeRelatorio,
                                   m_DataGeracao);
      Hist.fnLinhas(null, null, null, null, null);
      if (Usuario != null)
      {
         Vector Configuracao = UsuarioAux.getConfWebNormal();
         for (int i = 3; i < 12; i++)
            Hist.m_Colunas[i-3] = !Configuracao.elementAt(i).toString().equals("0");
      }
      if (Retorno == false)
         return null;

      // Insere histórico no cache
      insereHistorico(Hist);
      
      return Hist;
   }
}
