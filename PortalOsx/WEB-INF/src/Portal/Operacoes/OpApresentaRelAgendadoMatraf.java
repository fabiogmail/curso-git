//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApresentaRelAgendadoMatraf.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Agenda;
import Portal.Utils.AgendaMatraf;
import Portal.Utils.Arquivo;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;



public class OpApresentaRelAgendadoMatraf extends OperacaoAbs
{
   private String m_SubOperacao;
   private String m_NomeRelatorio;
   private String m_DataGeracao;
   private String m_Perfil;
   private String m_TipoRelatorio;
   private String m_IdRelatorio;
   private String m_NomeArquivoRelatorio;
   private String m_KeyCache;
   private short m_QtdColunasRecursos = 0;
   private short m_QtdColunasIndicadores = 0;
   private short m_QtdColunasRel = 0;
   private String m_TODOS = "Todos";

   private static final String m_COR_HEADER="#000066";         // Azul escuro
   private static final String m_COR_HEADER_APR="#33CCFF";     // Azul claro
   private static final String m_COR_HEADER2="#666666";        // Cinza escuro
   private static final String m_COR_HEADER3="#888888";        // Cinza escuro mais claro
   private static final String m_COR_PERIODOS="#000066";       // Azul escuro   
   private static final String m_COR_FONTEHEADER="#FFFFFF";    // Branco
   private static final String m_COR_FONTEHEADER2="#FOFOF0";    // Cinza Claro
   private static final String m_COR_LINHASIM="#FFFFFF";       // Branco
   private static final String m_COR_LINHANAO="#F0F0F";        // Cinza claro
   private static final String m_COR_LINHASIM2="#C0C0C0";       // Cinza escuro      
   public static final String m_FONTE="\"verdana\" size=\"1\"";
   public static final String m_FONTE2="\"verdana\" size=\"2\"";
   public static final String m_FONTE3="\"verdana\" size=\"3\"";
   public static final String m_FONTE4="\"verdana\" size=\"4\"";
   public static final String m_FONTE5="\"verdana\" size=\"5\"";   
   public static final String m_FONTE6="\"verdana\" size=\"6\"";
   public static final String m_FONTE_1="\"verdana\" size=\"-1\"";
   private boolean m_ApagarRelatorio; 
   
   static 
   {
   }
   
   public OpApresentaRelAgendadoMatraf() 
   {
   }
   
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Apresenta&ccedil;&atilde;o de Relat&oacute;rio Hist&oacute;rico");
         // Recupera os argumentos passados pela página
         m_SubOperacao          = m_Request.getParameter("suboperacao");
         m_NomeRelatorio        = m_Request.getParameter("nomerel");
         m_DataGeracao          = m_Request.getParameter("datageracao");
         m_Perfil               = m_Request.getParameter("perfil");
         m_TipoRelatorio        = m_Request.getParameter("tiporel");
         m_IdRelatorio          = m_Request.getParameter("idrel");
         m_NomeArquivoRelatorio = m_Request.getParameter("arquivo");
         m_ApagarRelatorio      = new Boolean(m_Request.getParameter("apaga")).booleanValue();
         //System.out.println(m_SubOperacao);
         m_NomeRelatorio = m_NomeRelatorio.replace('@', ' ');

         // Monta a chave para inserção e busca na cache de históricos
         m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_NomeRelatorio + "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" + m_Request.getSession().getId();
         //m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" + m_Request.getSession().getId();
         //System.out.println("m_KeyCache: "+m_KeyCache);
//   /*
         //System.out.println("* * * * * * * * * * * * *");
         //System.out.println(m_SubOperacao);
            //System.out.println(m_NomeRelatorio);
            //System.out.println(m_DataGeracao);
         //System.out.println(m_Perfil);
         //System.out.println(m_TipoRelatorio);
         //System.out.println(m_IdRelatorio);
         //System.out.println(m_NomeArquivoRelatorio);
         //System.out.println("* * * * * * * * * * * * *");
//   */
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:iniciaOperacao() - Suboperacao: "+ m_SubOperacao);

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
         else if (m_SubOperacao.toLowerCase().equals("matraf"))
            apresentaMatraf();
         else if (m_SubOperacao.toLowerCase().equals("baserelatorio"))
            apresentaBaseRelatorio();
         else if (m_SubOperacao.toLowerCase().equals("pagindicadores"))
            apresentaIndicadores();
         else if (m_SubOperacao.toLowerCase().equals("paglogs"))
            apresentaLogs();
         else if (m_SubOperacao.toLowerCase().equals("pagrecursos"))
            apresentaRecursos();
         else if (m_SubOperacao.toLowerCase().equals("alterarecursos"))
            alteraRecursos();
         else if (m_SubOperacao.toLowerCase().equals("alteraindicadores"))
            alteraIndicadores();
         else if (m_SubOperacao.toLowerCase().equals("download"))
            download();
         else if (m_SubOperacao.toLowerCase().equals("pagconfiguracao"))
            apresentaConfiguracao();
         else if (m_SubOperacao.toLowerCase().equals("alteraconfiguracao"))
            alteraConfiguracao();
         else if (m_SubOperacao.toLowerCase().equals("removeagenda"))
            removeAgenda();
         else
            System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:iniciaOperacao() - Suboperacao nao encontrada: "+ m_SubOperacao);
            
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpApresentaRelAgendadoMatraf - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   public void apresentaPagInicial() 
   {
      try
      {
         AgendaMatraf Ag = null;

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }
      
         iniciaArgs(9);        
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "index.htm";
         m_Args[2] = Ag.m_NomeRelatorio + " - " + Ag.m_DataGeracao;
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaPagInicial(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaPagInicial2()
   {
      try
      {
         iniciaArgs(9);
         
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaPagInicial2(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void apresentaRelatorio() 
   {
      try
      {
         iniciaArgs(14);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
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

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaTopoRelatorio() 
   {
      try
      {
         AgendaMatraf Ag = null;
         int i, iTam, QtdRec1 = 0, QtdRec2 = 0;
         String TipoRecurso1, TipoRecurso2, 
                Recursos1 = null, Recursos2 = null,
                SelectRecurso1 = "", SelectRecurso2 = "", 
                TipoRecurso = null, Recursos = null,
                RecursosSelecionados1 = null, RecursosSelecionados2 = null;

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

         TipoRecurso = m_Request.getParameter("tiporecurso");
         Recursos1 = m_Request.getParameter("recursos1");
         Recursos2 = m_Request.getParameter("recursos2");

         //
         // Seta o primeiro recurso
         //
         if (TipoRecurso != null && TipoRecurso.equals("1") == true)
         {
            RecursosSelecionados1 = m_Request.getParameter("recursos");
            if (RecursosSelecionados1 != null)
               RecursosSelecionados1 = RecursosSelecionados1.replace('@','&');
         }
         else
         {
            RecursosSelecionados2 = m_Request.getParameter("recursos");
            if (RecursosSelecionados2 != null)
               RecursosSelecionados2 = RecursosSelecionados2.replace('@','&');
         }

         if (RecursosSelecionados1 == null)
            RecursosSelecionados1 = Recursos1;
         if (RecursosSelecionados1 == null || RecursosSelecionados1.length() == 0)
            RecursosSelecionados1 = m_TODOS;

         SelectRecurso1  = "<b>" + TipoRecurso1 + ":</b>&nbsp;<a href=\"";
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
         else Recursos = m_TODOS;

         SelectRecurso2  = "&nbsp;&nbsp;&nbsp;<b>" + TipoRecurso2 + ":</b>&nbsp;<a href=\"";
         SelectRecurso2 += "javascript:AbreJanela('2','" + RecursosSelecionados2.replace('&','@') + "')";

         RecursosSelecionados2 = RecursosSelecionados2.replace('@','&');
         if (RecursosSelecionados2.length() > 20)
            Recursos = RecursosSelecionados2.substring(0, 20) + "...";
         else Recursos = RecursosSelecionados2;
         SelectRecurso2 += "\" class=\"link\">" + Recursos + "</a>";

         iniciaArgs(17);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "toporelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         m_Args[8] = RecursosSelecionados1;
         m_Args[9] = RecursosSelecionados2;

         m_Args[10] = m_TODOS;
         m_Args[11] = m_TODOS;

         m_Args[12] = Ag.m_MapRecursos[0].size() + "";
         m_Args[13] = Ag.m_MapRecursos[1].size() + "";

         m_Args[14] = Ag.m_NomeRelatorio + " - " + Ag.m_DataGeracao;
         m_Args[15] = SelectRecurso1;
         m_Args[16] = SelectRecurso2;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaTopoRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaMeioRelatorio() 
   {
      try
      {
         iniciaArgs(14);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "meiorelatorio.htm";

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

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaRelatorioDireita(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void apresentaMatraf()
   {
      try
      {
         AgendaMatraf Ag;
         Set ListaRecursos[];
         Iterator It[];
         Map.Entry Ent[];
         Object Obj, ObjLinha;
         boolean bPrimLinha = true, bTemTodos = false;
         int i, j, iQtdPeriodos, iQtdIndicadores = 1, iQtdRecursosTipo1, iCont = 0, iCont2 = 0;
         int iQtdRecursos;
         String Periodo = null, Recurso[], FonteRec0, Cor, Cor2;
         Vector LinhaRelProcessado = null;


         Ag = buscaAgenda();
         if (Ag == null)
            enviaPaginaErro();

         Periodo = m_Request.getParameter("periodo");
         try
         {
            if (Periodo != null)
               Ag.m_UltProcessado = Integer.parseInt(Periodo);
            else
               Ag.m_UltProcessado = 0;
         }
         catch (Exception Exc)
         {
            Ag.m_UltProcessado = 0;
         }
         // A posicao 0 (zero) referem-se às Op. Origem. Posicao 1 Op. Destino
         ListaRecursos = new Set[2]; 
         It = new Iterator[2];
         Ent = new Map.Entry[2];
         Recurso = new String[2];

         iQtdIndicadores = Ag.m_vIndicadores.size();

         if (Ag.m_MapRecursosNovos[0].size() == 0)
            ListaRecursos[0] = Ag.m_MapRecursos[0].entrySet();
         else
            ListaRecursos[0] = Ag.m_MapRecursosNovos[0].entrySet();

         if (Ag.m_MapRecursosNovos[1].size() == 0)
         {
            ListaRecursos[1] = Ag.m_MapRecursos[1].entrySet();
            iQtdRecursos = ListaRecursos[1].size()-1;
         }
         else
         {
            ListaRecursos[1] = Ag.m_MapRecursosNovos[1].entrySet();
            iQtdRecursos = ListaRecursos[1].size();
         }         

         iQtdPeriodos = Ag.m_PeriodosApresentaveis.size();         
         
         m_Html.enviaInicio("", "", "matraf.js");
         m_Html.envia("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

         Vector vPeriodos = new Vector(Ag.m_QtdPeriodos);
         Vector vLinks = new Vector(Ag.m_QtdPeriodos);
         String Link, NomePeriodo;
         for (i = 0; i < Ag.m_QtdPeriodos; i++)
         {
            Link = "";
            if (Ag.m_NomesDatas != null) NomePeriodo = Ag.m_NomesDatas.elementAt(i).toString();
            else NomePeriodo = Ag.m_Datas.elementAt(i).toString();

            Link += "javascript:TrocaPeriodo('";
            Link += m_Perfil+"','";
            Link += m_TipoRelatorio+"','";
            Link += m_IdRelatorio+"','";
            Link += m_NomeArquivoRelatorio+"','";
            Link += m_NomeRelatorio+"','";
            Link += m_DataGeracao+"','";
            Link += i+"')";   // Período para apresentação

            vPeriodos.add(NomePeriodo);
            vLinks.add(Link);
         }         
         vPeriodos.trimToSize();
         vLinks.trimToSize();

         // Essa tabela deve compreender todas as outras tabelas
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
         m_Html.envia("<tr>\n");
         m_Html.envia("<td>\n");

         m_Html.enviaPastasPeriodos(m_Html, vPeriodos, vLinks, Ag.m_UltProcessado);
        
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"2\" cellspacing=\"1\">\n");

         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
         m_Html.envia("<td bgcolor=\""+m_COR_HEADER+"\" align=\"center\" valign=\"middle\" nowrap rowspan=\"3\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+Ag.m_TituloRecurso[0]+"</b></font></td>");
         
         //m_Html.envia("<td bgcolor=\""+m_COR_FONTEHEADER+"\">&nbsp;</td>");
         //m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+Ag.m_TituloRecurso[0]+"</b></font></td>");
         m_Html.envia("<td align=\"center\" nowrap colspan=\""+ListaRecursos[1].size()*(iQtdIndicadores-2)+"\"><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+Ag.m_TituloRecurso[1]+"</b></font></td>");
         m_Html.envia("</tr>\n");

         //
         // Escreve a linha com os recursos de destino
         //
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER2+"\">\n");
         //m_Html.envia("<td bgcolor=\""+m_COR_FONTEHEADER+"\">&nbsp;</td>\n");
         It[1] = ListaRecursos[1].iterator();
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
            m_Html.envia("<td align=\"center\" nowrap colspan=\""+(iQtdIndicadores-2)+"\"><b><font face="+m_FONTE+ " color=\"" + m_COR_FONTEHEADER +"\">"+Recurso[1]+"</font><b></td>");
         }
         m_Html.envia("</tr>\n");

         //
         // Escreve a linha com os títulos dos indicadores presentes no relatório
         //
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER3+"\">\n");
         //m_Html.envia("<td>&nbsp;</td>\n");         
         //m_Html.envia("<td bgcolor=\""+m_COR_HEADER+"\" align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+Ag.m_TituloRecurso[0]+"</b></font></td>");
         if (bTemTodos)
            iQtdRecursosTipo1 = ListaRecursos[1].size()-1;
         else
            iQtdRecursosTipo1 = ListaRecursos[1].size();

         for (j = 0; j < iQtdRecursosTipo1; j++)
         {
            for (i = 2; i < iQtdIndicadores; i++)
               m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER2+"\">"+Ag.m_vIndicadores.elementAt(i).toString()+"</b></font></td>");
         }
         m_Html.envia("</tr>\n");         

         It[0] = ListaRecursos[0].iterator();
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
            m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+Recurso[0]+"</font><b></td>");

            It[1] = ListaRecursos[1].iterator();
            while (It[1].hasNext())
            {
               Ent[1] = (Map.Entry)It[1].next();
               Obj = Ent[1].getValue();
               Recurso[1] = Obj.toString();
               if (Recurso[1].equals(m_TODOS) == true)
                  continue;

//System.out.println("---> "+Recurso[0] + "-" + Recurso[1]);
               ObjLinha = Ag.m_MapRelatorios[((Integer)(Ag.m_PeriodosApresentaveis.elementAt(Ag.m_UltProcessado))).intValue()].get(Recurso[0] + "-" + Recurso[1]);
               if (ObjLinha != null)
                  LinhaRelProcessado = (Vector)ObjLinha;
               else
                  LinhaRelProcessado = null;

               //
               // Escreve os valores dos indicadores selecionados
               //
               for (int w = 2; w < iQtdIndicadores; w++)
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(w) : "-") +"</font></td>");
            }
            m_Html.envia("</tr>\n");
         }

         m_Html.envia("</table>\n");
         
         m_Html.envia("</td>\n");
         m_Html.envia("</tr>\n");
         m_Html.envia("</table>\n");         
         m_Html.enviafinal(null, null, true);                  
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaMatraf(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaBaseRelatorio() 
   {
      try
      {
         iniciaArgs(8);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "baserelatorio.htm";

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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaBaseRelatorioEsquerda(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void apresentaIndicadores() 
   {
      try
      {
         AgendaMatraf Ag;
         int i, QtdIndicadores;
         String Indicadores1 = "", Indicadores2 = "";

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         iniciaArgs(10);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "indicadores.htm";
         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         if (Ag.m_IndicadoresPossiveis == null)
         {
            QtdIndicadores = Ag.m_IndicadoresMatraf.size();
            for (i = 0; i < QtdIndicadores; i++)
               Indicadores1 += "                   <option value=\""+ Ag.m_IndicadoresMatraf.elementAt(i).toString() +"\">" + Ag.m_IndicadoresMatraf.elementAt(i).toString() + "</option>\n";
            Ag.m_IndicadoresPossiveis = Indicadores1;
         }

         if (Ag.m_Rec[1] != null) i = 2;
         else i = 1;
         for (; i < Ag.m_vIndicadores.size(); i++)
            Indicadores2 += "                   <option value=\""+ Ag.m_vIndicadores.elementAt(i).toString() +"\">" + Ag.m_vIndicadores.elementAt(i).toString() + "</option>\n";

         m_Args[8] = Ag.m_IndicadoresPossiveis;
         m_Args[9] = Indicadores2;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaConfiguracao()
   {
      try
      {
         AgendaMatraf Ag;
         String Periodos = "", Texto = "";
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
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
         
         m_Args[11] = "Matraf";

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaConfiguracao(): "+ Exc);
         Exc.printStackTrace();
      }    
   }

   public void alteraConfiguracao() 
   {
      UsuarioDef Usuario = null;
      AgendaMatraf Ag = null;      

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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:alteraConfiguracao(): Usuario nao foi encontrado");
   }

   public void apresentaRecursos()
   {
      try
      {
         AgendaMatraf Ag;
         Set ListaRecursos;
         Iterator It;
         Map.Entry Ent;
         int iTipoRecurso;
         Object Obj;
         String TipoRecurso = m_Request.getParameter("tiporecurso");
         String Recurso, SelectRecursos = "", RecursosSelecionados = "";

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         if (TipoRecurso.equals("1") == true)
         {
            TipoRecurso = Ag.m_TituloRecurso[0];
            iTipoRecurso = 0;
         }
         else
         {
            TipoRecurso = Ag.m_TituloRecurso[1];
            iTipoRecurso = 1;            
         }

         RecursosSelecionados = m_Request.getParameter("recursosselecionados");

         // Insere o Todos no início da lista
         SelectRecursos += "            <option value=\"" + m_TODOS + "\">" + m_TODOS + "</option>\n";  
         //
         // Varre a lista de recursos para preencher a lista
         //
         if (Ag.m_VecRecursos[iTipoRecurso] == null)
         {
            // Cria cache de recursos
            Ag.m_VecRecursos[iTipoRecurso] = new Vector();
            Ag.m_VecRecursos[iTipoRecurso].add(m_TODOS);
            
            ListaRecursos = Ag.m_MapRecursos[iTipoRecurso].entrySet();
            It = ListaRecursos.iterator();
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
            for (i = 1; i < iTam; i++)
               SelectRecursos += "            <option value=\"" + Ag.m_VecRecursos[iTipoRecurso].elementAt(i) + "\">" + Ag.m_VecRecursos[iTipoRecurso].elementAt(i) + "</option>\n";
         }

         iniciaArgs(12);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "recursos.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = (iTipoRecurso+1) + "";
         m_Args[9] = RecursosSelecionados;         
         m_Args[10] = TipoRecurso;
         m_Args[11] = SelectRecursos;         
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaRecursos(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void alteraRecursos() 
   {
      int iIndice;
      AgendaMatraf Ag = null;
      String Recursos = null;

      Ag = buscaAgenda();
      if (Ag == null)
         enviaPaginaErro();

      Recursos = m_Request.getParameter("recursos");
      //System.out.println("Rec: " + Recursos);

      iIndice = Recursos.indexOf("$$");
      if (iIndice == -1)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:alteraRecursos(): 'Recursos' passados esta incorreto: "+Recursos);
         return;
      }
      Ag.setMapRecursoNovo(0, Recursos.substring(0, iIndice));
      Ag.setMapRecursoNovo(1, Recursos.substring(iIndice+2, Recursos.length()));
      
      //System.out.println("OpApresentaRelAgendadoMatraf::alteraRecursos: Rec1: " + m_NovoRecurso[0] + " - Rec2: " + m_NovoRecurso[1]);

      // Reapresenta relatório
      apresentaMatraf();
   }

   public void apresentaLogs()
   {
      try
      {
         AgendaMatraf Ag;
         String strAux, strLogs = "", Cabecalho = "", NomePeriodo;
         Vector Log;

         Ag = buscaAgenda();
         if (Ag == null)
            enviaPaginaErro();

         for (int i = 0; i < Ag.m_Cabecalho.size(); i++)
         {
            strAux = (String)Ag.m_Cabecalho.elementAt(i);
            Cabecalho += "   <tr>\n      <td><font face="+m_FONTE+"><b>"+strAux.substring(0, strAux.indexOf(':')+1)+"</b>&nbsp;";
            Cabecalho += strAux.substring(strAux.indexOf(':')+1, strAux.length())+"</font></td>\n   </tr>\n";
         }

         Log = (Vector)Ag.m_Logs.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(Ag.m_UltProcessado)).intValue());

         if (Ag.m_NomesDatas != null) NomePeriodo = Ag.m_NomesDatas.elementAt(Ag.m_UltProcessado).toString();
         else NomePeriodo = Ag.m_Datas.elementAt(Ag.m_UltProcessado).toString();
         
         strLogs += "   <tr>\n      <td><font face="+m_FONTE+"><b>Per&iacute;odo: </b>"+NomePeriodo+"</font></td>\n   </tr>\n";
         for (int j = 0; j < Log.size(); j++)
         {
            if (j == 0 && Log.elementAt(j).toString().equals("nolog") == true)
               strLogs += "   <tr>\n      <td><font face="+m_FONTE+">N&atilde;o h&aacute; logs para este per&iacute;odo</font></td>\n   </tr>\n";
            else
               strLogs += "   <tr>\n      <td><font face="+m_FONTE+">"+Log.elementAt(j).toString().substring(0, Log.elementAt(j).toString().length()-1)+"</font></td>\n   </tr>\n";
         }
         strLogs += "   <tr>\n      <td>&nbsp;</td>\n   </tr>\n";

         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"matraf/";
         m_Args[1] = "logs.htm";
         m_Args[2] = Cabecalho;
         m_Args[3] = strLogs;
         m_Html.enviaArquivo(m_Args);         
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:apresentaLogs(): "+ Exc);
         Exc.printStackTrace();
      }   
   }

   public void alteraIndicadores()
   {
      try
      {
         AgendaMatraf Ag = null;
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
            Ag.remontaRelatorios(Short.parseShort(m_TipoRelatorio));
            apresentaMatraf();
         }
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:alteraIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void download()
   {
      try
      {
         AgendaMatraf Ag = null;
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         String CaracteresInvalidos = "\\/:*?<>|\"' &", NomeArq;
         

         // Recupera a agenda   
         Ag = buscaAgenda();
         if (Ag == null)
            enviaPaginaErro();

         // Monta o nome do arquivo e substitui os caracteres não válidos por '_'
         NomeArq = Ag.m_NomeRelatorio + "-"+m_KeyCache;
         for (int i = 0; i < CaracteresInvalidos.length(); i++)
            NomeArq = NomeArq.replace(CaracteresInvalidos.charAt(i), '_');

         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "download.htm";
         if (Ag.m_GeraArqDownload != 3)
            m_Args[2] = "<meta http-equiv=\"refresh\" content=\"5\">\n";
         else
            m_Args[2] = "";
         m_Args[3] = Ag.m_NomeRelatorio;
         m_Args[4] = Ag.m_DataGeracao;
         switch (Ag.m_GeraArqDownload)
         {
            case 0:
            m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!";                        
            new DownloadRelAgendadoMatraf(NomeArq, Ag, UsuarioAux.getConfWebAgenda());
            Thread.sleep(1000);
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
            m_Args[5] +=  "<td align=\"center\"><a href=\"/"+DefsComum.s_ContextoWEB+"/download/"+NomeArq+(UsuarioAux.getConfWebAgenda().elementAt(0).equals("1") ? ".csv" : ".htm")+".zip\"><img src=\"/PortalOsx/imagens/reldownload.gif\" border=\"0\"></a></td>\n";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:download(): "+ Exc);
         Exc.printStackTrace();         
      }
   }

   public void removeAgenda()
   {
      removeAgenda(this);
   }

   public static synchronized void removeAgenda(OpApresentaRelAgendadoMatraf p_Operacao)
   {
      try
      {
         AgendaMatraf Ag = null;
         String NomeArq[];
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
         s_MapAgendasMatraf.remove(p_Operacao.m_KeyCache);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:removeAgenda(): Nao foi possivel remover a agenda com chave: "+p_Operacao.m_KeyCache);
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoMatraf:removeAgenda(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public AgendaMatraf buscaAgenda() 
   {
       if (m_ApagarRelatorio)
       {
           UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
           Agenda.fechaRelatoriosUsuario(Usuario);
       }
       
      return buscaAgenda(this);
   }
   
   public static synchronized AgendaMatraf buscaAgenda(OpApresentaRelAgendadoMatraf p_Operacao)
   {
      Object ObjAg = null;
      AgendaMatraf Ag = null;
      
      if (s_MapAgendasMatraf.size() == 0)
      {
         Ag = p_Operacao.criaAgenda();
         return Ag;
      }

      ObjAg = s_MapAgendasMatraf.get(p_Operacao.m_KeyCache);
      if (ObjAg != null)
      {
         Ag = (AgendaMatraf)ObjAg;
      }
      else
      {
         Ag = p_Operacao.criaAgenda();
      }
      return Ag;
   }
   
   public void insereAgenda(AgendaMatraf p_Agenda) 
   {
      synchronized(s_MapAgendasMatraf)
      {
         s_MapAgendasMatraf.put(m_KeyCache, p_Agenda);
      }
      //System.out.println("OpApresentaRelAgendadoMatraf::insereAgenda(): Qtd no cache: " +s_MapAgendasMatraf.size());
   }
   
   public AgendaMatraf criaAgenda() 
   {
      boolean Retorno;
      AgendaMatraf Ag;
      UsuarioDef Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
      
      Agenda.fechaRelatoriosUsuario(Usuario);

      No no = NoUtil.buscaNobyNomePerfil(m_Perfil);
      
      Ag = new AgendaMatraf(no.getConexaoServUtil(), 
                            m_Perfil, 
                            m_TipoRelatorio, 
                            m_IdRelatorio, 
                            m_NomeArquivoRelatorio, 
                            m_SelecaoRecursosMatraf, 
                            m_SelecaoIndicadorMatraf,
                            m_IndicadoresMatraf,
                            m_NomeRelatorio,
                            m_DataGeracao);

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

class DownloadRelAgendadoMatraf extends java.lang.Thread
   {

	/**
	 * 
	 * @uml.property name="m_Ag"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	AgendaMatraf m_Ag = null;

      String m_NomeArq;      
      Vector m_vCabecalho;
      Vector m_CfgWeb;      

      public DownloadRelAgendadoMatraf(String p_NomeArq, AgendaMatraf p_Ag, Vector p_CfgWeb)
      {
         m_Ag = p_Ag;
         m_NomeArq = p_NomeArq;
         m_CfgWeb = p_CfgWeb;
         start();
      }
      
      public void run()
      {
         Set ListaRecursos[];

         try
         {
            ListaRecursos = new Set[2];         
            for (int i = 0; i < 2; i++)
            if (m_Ag.m_MapRecursosNovos[i].size() == 0)
               ListaRecursos[i] = m_Ag.m_MapRecursos[i].entrySet();
            else
               ListaRecursos[i] = m_Ag.m_MapRecursosNovos[i].entrySet();
       
            m_Ag.m_GeraArqDownload = 1;
            m_Ag.criaArquivoMatraf(m_NomeArq, 
                                   m_Ag.m_Cabecalho, 
                                   m_Ag.m_TituloRecurso, 
                                   m_Ag.m_vIndicadores, 
                                   m_Ag.m_PeriodosApresentaveis, 
                                   m_Ag.m_NomesDatas, 
                                   m_Ag.m_Datas, 
                                   m_Ag.m_UltProcessado,
                                   ListaRecursos, 
                                   m_Ag.m_MapRelatorios, 
                                   m_Ag.m_Logs,
                                   m_CfgWeb);
            m_Ag.m_GeraArqDownload = 2;            
            Arquivo Arq = new Arquivo();
            Arq.zipaArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+m_NomeArq + (m_CfgWeb.elementAt(0).equals("1") ? ".csv": ".htm"));
            m_Ag.m_GeraArqDownload = 3;
         }
         catch (Exception Exc)
         {
            Exc.printStackTrace();
         }
      }
   };      
}
