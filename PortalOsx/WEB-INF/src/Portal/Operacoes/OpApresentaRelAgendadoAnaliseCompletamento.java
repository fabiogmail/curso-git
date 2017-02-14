//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApresentaRelAgendadoAnaliseCompletamento.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Agenda;
import Portal.Utils.AgendaAnaliseCompletamento;
import Portal.Utils.Arquivo;
import Portal.Utils.NovoDownload;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;



public class OpApresentaRelAgendadoAnaliseCompletamento extends OperacaoAbs
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
   
   private String m_IndicadoresNOK[] = {"LO",
                                        "CO1",
                                        "CO2",
                                        "CO3",
                                        "NR",
                                        "NR_VM",
                                        "DSA",
                                        "LOS",
                                        "ACB",
                                        "DROP",
                                        "SC",
                                        "SRP",
                                        "BLOQ",
                                        "DESL",
                                        "INEX",
                                        "OU"};

   private static final String m_COR_HEADER="#000066";         // Azul escuro
   private static final String m_COR_HEADER2="#666666";        // Cinza escuro   
   private static final String m_COR_HEADER_APR="#33CCFF";      // Azul claro
   private static final String m_COR_PERIODOS="#000066";         // Azul escuro   
   private static final String m_COR_FONTEHEADER="#FFFFFF";    // Branco
   private static final String m_COR_LINHASIM="#FFFFFF";       // Branco
   private static final String m_COR_LINHANAO="#F0F0F0";        // Cinza claro
   private static final String m_COR_LINHASIM2="#C0C0C0";       // Cinza escuro
   public static final String m_FONTE="\"verdana\" size=\"1\"";
   public static final String m_FONTE2="\"verdana\" size=\"2\"";
   public static final String m_FONTE3="\"verdana\" size=\"3\"";
   public static final String m_FONTE4="\"verdana\" size=\"4\"";
   public static final String m_FONTE5="\"verdana\" size=\"5\"";   
   public static final String m_FONTE6="\"verdana\" size=\"6\"";
   public static final String m_FONTE_1="\"verdana\" size=\"-1\"";
   
   static 
   {
   }
   
   public OpApresentaRelAgendadoAnaliseCompletamento() 
   {
   }
   
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Apresenta&ccedil;&atilde;o de Relat&oacute;rio An&aacute;lise de Completamento");
         // Recupera os argumentos passados pela página
         m_SubOperacao          = m_Request.getParameter("suboperacao");
         m_NomeRelatorio        = m_Request.getParameter("nomerel");
         m_DataGeracao          = m_Request.getParameter("datageracao");
         m_Perfil               = m_Request.getParameter("perfil");
         m_TipoRelatorio        = m_Request.getParameter("tiporel");
         m_IdRelatorio          = m_Request.getParameter("idrel");
         m_NomeArquivoRelatorio = m_Request.getParameter("arquivo");
         //System.out.println(m_SubOperacao);
         m_NomeRelatorio = m_NomeRelatorio.replace('@', ' ');

         // Monta a chave para inserção e busca na cache de históricos
         m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_NomeRelatorio + "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" + m_Request.getSession().getId();

         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:iniciaOperacao() - Suboperacao: "+ m_SubOperacao);

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
         else if (m_SubOperacao.toLowerCase().equals("completamento"))
            apresentaAnaliseCompletamento();
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
         else if (m_SubOperacao.toLowerCase().equals("removeagenda"))
            removeAgenda();
         else
            System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:iniciaOperacao() - Suboperacao nao encontrada: "+ m_SubOperacao);
            
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpApresentaRelAgendadoAnaliseCompletamento - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   public void apresentaPagInicial() 
   {
      try
      {
         AgendaAnaliseCompletamento Ag = null;

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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaPagInicial(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaPagInicial2()
   {
      try
      {
         iniciaArgs(9);
         
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaPagInicial2(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void apresentaRelatorio() 
   {
      try
      {
         iniciaArgs(14);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaTopoRelatorio() 
   {
      try
      {
         AgendaAnaliseCompletamento Ag = null;
         int i, iTam, QtdRec1 = 0;
         String SelectRecurso = "",
                NomeRecurso = null, Recursos = null,
                RecursosSelecionados = null;

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         NomeRecurso = Ag.m_TituloRecurso;
         //
         // Seta o recurso
         //
         RecursosSelecionados = m_Request.getParameter("recursos");
         if (RecursosSelecionados != null)
            RecursosSelecionados = RecursosSelecionados.replace('@','&');

         if (RecursosSelecionados == null || RecursosSelecionados.length() == 0)
            RecursosSelecionados = m_TODOS;

         SelectRecurso  = "<b>" + NomeRecurso + ":</b>&nbsp;<a href=\"";
         SelectRecurso += "javascript:AbreJanela('1','" + RecursosSelecionados.replace('&','@') + "')";

         
         if (RecursosSelecionados.length() > 20)
            Recursos = RecursosSelecionados.substring(0, 20) + "...";
         else 
            Recursos = RecursosSelecionados;

         SelectRecurso += "\" class=\"link\">" + Recursos + "</a>";

         iniciaArgs(13);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
         m_Args[1] = "toporelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = RecursosSelecionados;
         m_Args[9] = m_TODOS;
         m_Args[10] = Ag.m_MapRecursos.size() + "";
         m_Args[11] = Ag.m_NomeRelatorio + " - " + Ag.m_DataGeracao;
         m_Args[12] = SelectRecurso;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaTopoRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaMeioRelatorio() 
   {
      try
      {
         iniciaArgs(14);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaRelatorioDireita(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void apresentaAnaliseCompletamento()
   {
      try
      {
         AgendaAnaliseCompletamento Ag;
         Set ListaRecursos;

         int i;
         String Periodo = null, Recurso, strAux = "";

         Ag = buscaAgenda();
         if (Ag == null)
            enviaPaginaErro();

         // Pegando periodo
         Periodo = m_Request.getParameter("periodo");

         // Pegando o nivel
         strAux  = m_Request.getParameter("nivel");
         if (strAux != null)
         {
            try 
            {
               Ag.m_Nivel = Integer.parseInt(strAux);
            }
            catch (NumberFormatException nfe)
            {
               Ag.m_Nivel = 0;
               System.out.println("Erro convertendo nivel \"" + strAux + "\" para inteiro.");
               nfe.printStackTrace();
            }
         }
         
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

         if (Ag.m_MapRecursosNovos.size() == 0)
            ListaRecursos = Ag.m_MapRecursos.entrySet();
         else
            ListaRecursos = Ag.m_MapRecursosNovos.entrySet();
        
         m_Html.enviaInicio("", "", "analisecompletamento.js");
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

         // * * * * 
         // Essa tabela deve compreender todas as outras tabelas
         // * * * *          
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
         m_Html.envia("<tr>\n");
         m_Html.envia("<td>\n");

         m_Html.enviaPastasPeriodos(m_Html, vPeriodos, vLinks, Ag.m_UltProcessado);

         switch (Ag.m_Nivel)
         {
            case 1:            
               // Removendo os indicadores NOK do nivel 3
               for(i = 0; i < m_IndicadoresNOK.length; i++)
                  if (Ag.m_vIndicadores.contains(m_IndicadoresNOK[i]))
                     Ag.m_vIndicadores.remove(m_IndicadoresNOK[i]);
               apresentaNivel_1(Ag, ListaRecursos);
               break;
            case 2:
               // Removendo os indicadores NOK do nivel 3
               for(i = 0; i < m_IndicadoresNOK.length; i++)
                  if (Ag.m_vIndicadores.contains(m_IndicadoresNOK[i]))
                     Ag.m_vIndicadores.remove(m_IndicadoresNOK[i]);
               apresentaNivel_2(Ag, ListaRecursos);
               break;
            case 3:
               if (m_SubOperacao.toLowerCase().equals("completamento"))
               {
                  //Adicionando os indicadores NOK
                  for(i = 0; i < m_IndicadoresNOK.length; i++)
                     Ag.m_vIndicadores.addElement(m_IndicadoresNOK[i]);                  
               }
               apresentaNivel_3(Ag, ListaRecursos);
               break;            
            default:
               apresentaNivel_0(Ag, ListaRecursos);
         }                     

         // * * * * 
         // Fechamento da tabela principal
         // * * * *          
         m_Html.envia("</td>\n");
         m_Html.envia("</tr>\n");
         m_Html.envia("</table>\n");         
         m_Html.enviafinal(null, null, true);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaAnaliseCompletamento(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   /**
    * Apresenta Nivel 0
    */
   public void apresentaNivel_0(AgendaAnaliseCompletamento p_Ag, Set p_ListaRecursos)
   {
      Iterator It;
      Map.Entry Ent;
      Object Obj, ObjLinha;
      int iQtdIndicadores = 1, i = 0, iCont = 0;
      String Recurso = null, strTotal = "", Cor = null;
      Vector LinhaRelProcessado = null;
      Integer Posicao = null;           

      try
      {
         iQtdIndicadores = p_Ag.m_vIndicadores.size();
      
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\">\n");

         // Linha com os Indicadores do relatório
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+p_Ag.m_TituloRecurso+"</b></font></td>");
         for (i = 1; i < iQtdIndicadores; i++)
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">"+p_Ag.m_vIndicadores.elementAt(i).toString()+"</b></font></td>");
         m_Html.envia("</tr>\n");

         It = p_ListaRecursos.iterator();
         while (It.hasNext())
         {
            Ent = (Map.Entry)It.next();
            Obj = Ent.getValue();
            Recurso = Obj.toString();

            ObjLinha = p_Ag.m_MapRelatorios[((Integer)(p_Ag.m_PeriodosApresentaveis.elementAt(p_Ag.m_UltProcessado))).intValue()].get(Recurso);
            if (ObjLinha != null)
               LinhaRelProcessado = (Vector)ObjLinha;
            else
               LinhaRelProcessado = null;
            
            if (Recurso.equals(m_TODOS) == true)
            {
               strTotal = "<tr bgcolor=\""+m_COR_HEADER2+"\">\n";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">TOTAL</font><b></td>";
               for (i = 1; i < iQtdIndicadores; i++)
               {
//System.out.println("--> 0: "+p_Ag.m_vIndicadores.elementAt(i));

                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(p_Ag.m_vIndicadores.elementAt(i));
                  if (Posicao != null)
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>" + (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</b></font></td>";
                  else
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>?</b></font></td>";
               }
               strTotal += "</tr>\n";
               continue;
            }

            if ((iCont % 2) == 0)
               Cor = m_COR_LINHASIM2;
            else
               Cor = m_COR_LINHANAO;
            iCont++;
            m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+Recurso+"</font><b></td>");

            for (i = 1; i < iQtdIndicadores; i++)
            {
               Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(p_Ag.m_vIndicadores.elementAt(i));
System.out.println("--> 1: "+p_Ag.m_vIndicadores.elementAt(i)+" -- Posicao: "+Posicao);
               if (Posicao != null)
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");
               else
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
            }
            m_Html.envia("</tr>\n");
         }
         m_Html.envia(strTotal);
         m_Html.envia("</table>\n");
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaNivel_0(): "+ Exc);
         Exc.printStackTrace();        
      }
   }

   /**
    * Apresenta Nivel 1
    */
   public void apresentaNivel_1(AgendaAnaliseCompletamento p_Ag, Set p_ListaRecursos)
   {
      Iterator It;
      Map.Entry Ent;
      Object Obj, ObjLinha;
      int iQtdIndicadores = 1, i = 0, iCont = 0, j = 0;
      String Recurso = null, strTotal = "", Cor = null;
      Vector LinhaRelProcessado = null;
      String strTipoCham[] = {"VC1", "LDN (VC2/VC3)", "LDI", "Total"};
      String strTipoChamArq[] = {"_VC1", "_LDN", "_LDI", ""};
      String Indicador;
      Integer Posicao;


      try
      {
         iQtdIndicadores = p_Ag.m_vIndicadores.size();
      
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\">\n");

         // Linha com os Indicadores do relatório
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+p_Ag.m_TituloRecurso+"</b></font></td>");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Tipo de Chamada</b></font></td>");
         for (i = 1; i < iQtdIndicadores; i++)
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">"+p_Ag.m_vIndicadores.elementAt(i).toString()+"</b></font></td>");
         m_Html.envia("</tr>\n");

         It = p_ListaRecursos.iterator();
         while (It.hasNext())
         {
            Ent = (Map.Entry)It.next();
            Obj = Ent.getValue();
            Recurso = Obj.toString();

            ObjLinha = p_Ag.m_MapRelatorios[((Integer)(p_Ag.m_PeriodosApresentaveis.elementAt(p_Ag.m_UltProcessado))).intValue()].get(Recurso);
            if (ObjLinha != null)
               LinhaRelProcessado = (Vector)ObjLinha;
            else
               LinhaRelProcessado = null;
            
            if (Recurso.equals(m_TODOS) == true)
            {
               strTotal = "<tr bgcolor=\""+m_COR_HEADER2+"\">\n";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">TOTAL</font><b></td>";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">&nbsp;</font><b></td>";
               for (i = 1; i < iQtdIndicadores; i++)
               {
                  Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[3];
                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);               
                  if (Posicao != null)
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</b></font></td>";
                  else
                  {
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>?</b></font></td>";
                     System.out.println("Indicador " + Indicador + " nao encontrado.");
                  }
               }
               strTotal += "</tr>\n";
               continue;
            }

            if ((iCont % 2) == 0)
               Cor = m_COR_LINHASIM2;
            else
               Cor = m_COR_LINHANAO;
            iCont++;


            // **********************
            // Expandindo nivel 1
            // **********************
            
            m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
            m_Html.envia("<td align=\"center\" nowrap rowspan=\"4\"><b><font face="+m_FONTE+">"+Recurso+"</font><b></td>");               
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strTipoCham[0]+"</font><b></td>");                           

            for (i = 1; i < iQtdIndicadores; i++)
            {
               Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[0];
               if (Indicador.indexOf("(%)") != -1 )
               {
                  Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                  Indicador +=  strTipoChamArq[0] + "(%)";                     
               }               
               Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);               
               if (Posicao != null)
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
               else
               {
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                  System.out.println("Indicador " + Indicador + " nao encontrado.");
               }
            }
            m_Html.envia("</tr>\n");

            for (j = 0; j < 3; j++)
            {
               m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
               m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strTipoCham[j+1]+"</font><b></td>");                           
               for (i = 1; i < iQtdIndicadores; i++)
               {
                  Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[j+1];                  
                  if (Indicador.indexOf("(%)") != -1 )
                  {
                     Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                     Indicador +=  strTipoChamArq[j+1] + "(%)"; 
                  }
                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);                              
                  if (Posicao != null)
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
                  else
                  {
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                     System.out.println("Indicador " + Indicador + " nao encontrado.");
                  }
               }
               m_Html.envia("</tr>\n");
            }
         }
         m_Html.envia(strTotal);
         m_Html.envia("</table>\n");
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaNivel_1(): "+ Exc);
         Exc.printStackTrace();        
      }
   
   }

   /**
    * Apresenta Nivel 2 
    */
   public void apresentaNivel_2(AgendaAnaliseCompletamento p_Ag, Set p_ListaRecursos)
   {
      Iterator It;
      Map.Entry Ent;
      Object Obj, ObjLinha;
      int iQtdIndicadores = 1, i = 0, iCont = 0, j = 0, k = 0;
      String Recurso = null, strTotal = "", Cor = null;
      Vector LinhaRelProcessado = null;
      String strTipoCham[] = {"VC1", "LDN (VC2/VC3)", "LDI", "Total"};
      String strTipoChamArq[] = {"_VC1", "_LDN", "_LDI", ""};
      String strClasseCham[] = {"Direto", "A Cobrar", "Total"};
      String strClasseChamArq[] = {"_DIRETO", "_COBRAR", ""};
      String Indicador;
      Integer Posicao;


      try
      {
         iQtdIndicadores = p_Ag.m_vIndicadores.size();
      
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\">\n");

         // Linha com os Indicadores do relatório
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+p_Ag.m_TituloRecurso+"</b></font></td>");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Tipo de Chamada</b></font></td>");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Classe da Chamada</b></font></td>");
         for (i = 1; i < iQtdIndicadores; i++)
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">"+p_Ag.m_vIndicadores.elementAt(i).toString()+"</b></font></td>");
         m_Html.envia("</tr>\n");

         It = p_ListaRecursos.iterator();
         while (It.hasNext())
         {
            Ent = (Map.Entry)It.next();
            Obj = Ent.getValue();
            Recurso = Obj.toString();

            ObjLinha = p_Ag.m_MapRelatorios[((Integer)(p_Ag.m_PeriodosApresentaveis.elementAt(p_Ag.m_UltProcessado))).intValue()].get(Recurso);
            if (ObjLinha != null)
               LinhaRelProcessado = (Vector)ObjLinha;
            else
               LinhaRelProcessado = null;
            
            if (Recurso.equals(m_TODOS) == true)
            {
               strTotal = "<tr bgcolor=\""+m_COR_HEADER2+"\">\n";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>TOTAL</b></font><b></td>";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>&nbsp;</b></font><b></td>";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>&nbsp;</b></font><b></td>";
               for (i = 1; i < iQtdIndicadores; i++)
               {
                  //Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[3];
                  Indicador = p_Ag.m_vIndicadores.elementAt(i).toString();
                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);
                  if (Posicao != null)
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</b></font></td>";
                  else
                  {
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>?</b></font></td>";               
                     System.out.println("Indicador " + Indicador + " nao encontrado.");
                  }
               }
               strTotal += "</tr>\n";
               continue;
            }

            if ((iCont % 2) == 0)
               Cor = m_COR_LINHASIM2;
            else
               Cor = m_COR_LINHANAO;
            iCont++;


            // **********************
            // Expandindo nivel 2
            // **********************
            
            m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
            m_Html.envia("<td align=\"center\" nowrap rowspan=\"9\"><b><font face="+m_FONTE+">"+Recurso+"</font><b></td>");
            m_Html.envia("<td align=\"center\" nowrap rowspan=\"3\"><b><font face="+m_FONTE+">"+strTipoCham[0]+"</font><b></td>");
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strClasseCham[0]+"</font><b></td>");            

            for (i = 1; i < iQtdIndicadores; i++)
            {
               Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[0] + strClasseChamArq[0];
               if (Indicador.indexOf("(%)") != -1 )
               {
                  Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                  Indicador +=  strTipoChamArq[0] + strClasseChamArq[0] + "(%)";                     
               }               
               Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);               
               if (Posicao != null)
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
               else
               {
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                  System.out.println("Indicador " + Indicador + " nao encontrado.");
               }
            }
            m_Html.envia("</tr>\n");
           

            // Linha 2 e 3 do primeiro tipo de chamada
            for (j = 1; j < 3; j++)
            {
               m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
               m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strClasseCham[j]+"</font><b></td>");            

               for (i = 1; i < iQtdIndicadores; i++)
               {
                  Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[0] + strClasseChamArq[j];
                  if (Indicador.indexOf("(%)") != -1 )
                  {
                     Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                     Indicador +=  strTipoChamArq[0] + strClasseChamArq[j] + "(%)";                     
                  }               
                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);               
                  if (Posicao != null)
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
                  else
                  {
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                     System.out.println("Indicador " + Indicador + " nao encontrado.");
                  }
               }
               m_Html.envia("</tr>\n");               
            }          

            // Linhas restantes                                      
            for (j = 1; j < 3; j++) // For do Tipo de Chamada
            {
               m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
               m_Html.envia("<td align=\"center\" nowrap rowspan=\"3\"><b><font face="+m_FONTE+">"+strTipoCham[j]+"</font><b></td>");

               for (k = 0; k < 3; k++) // For da Classe da Chamada
               {
                  if (k > 0) 
                     m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
                  m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strClasseCham[k]+"</font><b></td>");               
                  for (i = 1; i < iQtdIndicadores; i++)
                  {
                     Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[j] + strClasseChamArq[k];
                     if (Indicador.indexOf("(%)") != -1 )
                     {
                        Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                        Indicador +=  strTipoChamArq[j] + strClasseChamArq[k] + "(%)"; 
                     }
                     Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);                              
                     if (Posicao != null)
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
                     else
                     {
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                        System.out.println("Indicador " + Indicador + " nao encontrado.");
                     }
                  }
                  m_Html.envia("</tr>\n");
               }
            }
         }
         
         m_Html.envia(strTotal);
         m_Html.envia("</table>\n");
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaNivel_2(): "+ Exc);
         Exc.printStackTrace();        
      }
         

   }


    /**
    * Apresenta Nivel 3 
    */
   public void apresentaNivel_3(AgendaAnaliseCompletamento p_Ag, Set p_ListaRecursos)
   {
      Iterator It;
      Map.Entry Ent;
      Object Obj, ObjLinha;
      int iQtdIndicadores = 1, i = 0, iCont = 0, j = 0, k = 0;
      String Recurso = null, strTotal = "", Cor = null;
      Vector LinhaRelProcessado = null;
      String strTipoCham[] = {"VC1", "LDN (VC2/VC3)", "LDI", "Total"};
      String strTipoChamArq[] = {"_VC1", "_LDN", "_LDI", ""};
      String strClasseCham[] = {"Direto", "A Cobrar", "Total"};
      String strClasseChamArq[] = {"_DIRETO", "_COBRAR", ""};
      String Indicador;
      Integer Posicao;


      try
      {
         iQtdIndicadores = p_Ag.m_vIndicadores.size();
      
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\">\n");

         // Linha com os Indicadores do relatório
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+p_Ag.m_TituloRecurso+"</b></font></td>");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Tipo de Chamada</b></font></td>");
         m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>Classe da Chamada</b></font></td>");
         for (i = 1; i < iQtdIndicadores; i++)
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\">"+p_Ag.m_vIndicadores.elementAt(i).toString()+"</b></font></td>");            
         m_Html.envia("</tr>\n");

         It = p_ListaRecursos.iterator();
         while (It.hasNext())
         {
            Ent = (Map.Entry)It.next();
            Obj = Ent.getValue();
            Recurso = Obj.toString();

            ObjLinha = p_Ag.m_MapRelatorios[((Integer)(p_Ag.m_PeriodosApresentaveis.elementAt(p_Ag.m_UltProcessado))).intValue()].get(Recurso);
            if (ObjLinha != null)
               LinhaRelProcessado = (Vector)ObjLinha;
            else
               LinhaRelProcessado = null;
            
            if (Recurso.equals(m_TODOS) == true)
            {
               strTotal = "<tr bgcolor=\""+m_COR_HEADER2+"\">\n";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>TOTAL</b></font><b></td>";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>&nbsp;</b></font><b></td>";
               strTotal += "<td align=\"center\" nowrap><b><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>&nbsp;</b></font><b></td>";
               for (i = 1; i < iQtdIndicadores; i++)
               {
                  //Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[3];
                  Indicador = p_Ag.m_vIndicadores.elementAt(i).toString();
                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);
                  if (Posicao != null)
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</b></font></td>";
                  else
                  {
                     strTotal += "<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>?</b></font></td>";               
                     System.out.println("Indicador " + Indicador + " nao encontrado.");
                  }
               }
               strTotal += "</tr>\n";
               continue;
            }

            if ((iCont % 2) == 0)
               Cor = m_COR_LINHASIM2;
            else
               Cor = m_COR_LINHANAO;
            iCont++;


            // **********************
            // Expandindo nivel 2
            // **********************
            
            m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
            m_Html.envia("<td align=\"center\" nowrap rowspan=\"9\"><b><font face="+m_FONTE+">"+Recurso+"</font><b></td>");
            m_Html.envia("<td align=\"center\" nowrap rowspan=\"3\"><b><font face="+m_FONTE+">"+strTipoCham[0]+"</font><b></td>");
            m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strClasseCham[0]+"</font><b></td>");            

            for (i = 1; i < iQtdIndicadores; i++)
            {
               Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[0] + strClasseChamArq[0];
               if (Indicador.indexOf("(%)") != -1 )
               {
                  Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                  Indicador +=  strTipoChamArq[0] + strClasseChamArq[0] + "(%)";                     
               }               
               Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);               
               if (Posicao != null)
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
               else
               {
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                  System.out.println("Indicador " + Indicador + " nao encontrado.");
               }
            }
            m_Html.envia("</tr>\n");
           

            // Linha 2 e 3 do primeiro tipo de chamada
            for (j = 1; j < 3; j++)
            {
               m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
               m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strClasseCham[j]+"</font><b></td>");            

               for (i = 1; i < iQtdIndicadores; i++)
               {
                  Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[0] + strClasseChamArq[j];
                  if (Indicador.indexOf("(%)") != -1 )
                  {
                     Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                     Indicador +=  strTipoChamArq[0] + strClasseChamArq[j] + "(%)";                     
                  }               
                  Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);               
                  if (Posicao != null)
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
                  else
                  {
                     m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                     System.out.println("Indicador " + Indicador + " nao encontrado.");
                  }
               }
               m_Html.envia("</tr>\n");               
            }          

            // Linhas restantes                                      
            for (j = 1; j < 3; j++) // For do Tipo de Chamada
            {
               m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
               m_Html.envia("<td align=\"center\" nowrap rowspan=\"3\"><b><font face="+m_FONTE+">"+strTipoCham[j]+"</font><b></td>");

               for (k = 0; k < 3; k++) // For da Classe da Chamada
               {
                  if (k > 0) 
                     m_Html.envia("<tr bgcolor=\""+Cor+"\">\n");
                  m_Html.envia("<td align=\"center\" nowrap><b><font face="+m_FONTE+">"+strClasseCham[k]+"</font><b></td>");               
                  for (i = 1; i < iQtdIndicadores; i++)
                  {
                     Indicador = p_Ag.m_vIndicadores.elementAt(i) + strTipoChamArq[j] + strClasseChamArq[k];
                     if (Indicador.indexOf("(%)") != -1 )
                     {
                        Indicador = Indicador.substring(0, Indicador.indexOf("(%)"));
                        Indicador +=  strTipoChamArq[j] + strClasseChamArq[k] + "(%)"; 
                     }
                     Posicao = (Integer)p_Ag.m_IndicadoresAnaliseCompletamento.get(Indicador);                              
                     if (Posicao != null)
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+ (LinhaRelProcessado != null ? LinhaRelProcessado.elementAt(Posicao.intValue()) : "-") +"</font></td>");                  
                     else
                     {
                        m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">?</font></td>");                                                         
                        System.out.println("Indicador " + Indicador + " nao encontrado.");
                     }
                  }
                  m_Html.envia("</tr>\n");
               }
            }
         }
         
         m_Html.envia(strTotal);
         m_Html.envia("</table>\n");
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaNivel_2(): "+ Exc);
         Exc.printStackTrace();        
      }
         

   }


   public void apresentaBaseRelatorio() 
   {
      try
      {
         iniciaArgs(8);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaBaseRelatorioEsquerda(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void apresentaIndicadores() 
   {
      try
      {
         AgendaAnaliseCompletamento Ag;
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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
         m_Args[1] = "indicadores.htm";
         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         // Adicionan o Indicadores Visiveis
         QtdIndicadores = Ag.m_IndicadoresVisiveis.size();            
         for (i = 0; i < QtdIndicadores; i++)
            Indicadores1 += "                   <option value=\""+ (String)Ag.m_IndicadoresVisiveis.elementAt(i) +"\">" + (String)Ag.m_IndicadoresVisiveis.elementAt(i) + "</option>\n";

         if (Ag.m_Nivel == 3) // Adiciona o indicadores de NOK
         {
            QtdIndicadores = m_IndicadoresNOK.length;
            for (i = 0; i < QtdIndicadores; i++)
               Indicadores1 += "                   <option value=\""+ m_IndicadoresNOK[i] +"\">" + m_IndicadoresNOK[i] + "</option>\n";         
         }
         
         Ag.m_IndicadoresPossiveis = Indicadores1;

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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaRecursos()
   {
      try
      {
         AgendaAnaliseCompletamento Ag;
         Set ListaRecursos;
         Iterator It;
         Map.Entry Ent;
         Object Obj;
         String NomeRecurso = "";
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

         NomeRecurso = Ag.m_TituloRecurso;
         RecursosSelecionados = m_Request.getParameter("recursosselecionados");

         // Insere o Todos no início da lista
         SelectRecursos += "            <option value=\"" + m_TODOS + "\">" + m_TODOS + "</option>\n";  

         //
         // Varre a lista de recursos para preencher a lista
         //

         if (Ag.m_VecRecursos == null)
         {
            // Cria cache de recursos
            Ag.m_VecRecursos = new Vector();
            Ag.m_VecRecursos.add(m_TODOS);
            
            ListaRecursos = Ag.m_MapRecursos.entrySet();
            It = ListaRecursos.iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry)It.next();
               Obj = Ent.getValue();
               Recurso = Obj.toString();
               if (Recurso.equals(m_TODOS) == false)
               {
                  SelectRecursos += "            <option value=\"" + Recurso + "\">" + Recurso + "</option>\n";
                  Ag.m_VecRecursos.add(Recurso);                  
               }
            }
         }
         else
         {
            int i, iTam = Ag.m_VecRecursos.size();
            for (i = 1; i < iTam; i++)
               SelectRecursos += "            <option value=\"" + Ag.m_VecRecursos.elementAt(i) + "\">" + Ag.m_VecRecursos.elementAt(i) + "</option>\n";
         }

         iniciaArgs(11);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
         m_Args[1] = "recursos.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = RecursosSelecionados;         
         m_Args[9]  = NomeRecurso;
         m_Args[10] = SelectRecursos;         
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaRecursos(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public void alteraRecursos() 
   {
      AgendaAnaliseCompletamento Ag = null;
      String Recursos = null;

      Ag = buscaAgenda();
      if (Ag == null)
         enviaPaginaErro();

      Recursos = m_Request.getParameter("recursos");
   
      Ag.setMapRecursoNovo(Recursos);

      apresentaAnaliseCompletamento();
   }

   public void apresentaLogs()
   {
      try
      {
         AgendaAnaliseCompletamento Ag;
         String strAux, strLogs = "", Cabecalho = "";
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

         if (Ag.m_NomesDatas != null)
            strLogs += "   <tr>\n      <td><font face="+m_FONTE+"><b>Per&iacute;odo: </b>"+Ag.m_NomesDatas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(Ag.m_UltProcessado)).intValue())+"</font></td>\n   </tr>\n";
         else
            strLogs += "   <tr>\n      <td><font face="+m_FONTE+"><b>Per&iacute;odo: </b>"+Ag.m_Datas.elementAt(((Integer)Ag.m_PeriodosApresentaveis.elementAt(Ag.m_UltProcessado)).intValue())+"</font></td>\n   </tr>\n";            

         for (int j = 0; j < Log.size(); j++)
         {
            if (j == 0 && Log.elementAt(j).toString().equals("nolog") == true)
               strLogs += "   <tr>\n      <td><font face="+m_FONTE+">N&atilde;o h&aacute; logs para este per&iacute;odo</font></td>\n   </tr>\n";
            else
               strLogs += "   <tr>\n      <td><font face="+m_FONTE+">"+Log.elementAt(j).toString().substring(0, Log.elementAt(j).toString().length()-1)+"</font></td>\n   </tr>\n";
         }
         strLogs += "   <tr>\n      <td>&nbsp;</td>\n   </tr>\n";

         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda_analisecompletamento/";
         m_Args[1] = "logs.htm";
         m_Args[2] = Cabecalho;
         m_Args[3] = strLogs;
         m_Html.enviaArquivo(m_Args);         
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:apresentaLogs(): "+ Exc);
         Exc.printStackTrace();
      }   
   }

   public void alteraIndicadores()
   {
      try
      {
         AgendaAnaliseCompletamento Ag = null;
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
            apresentaAnaliseCompletamento();
         }
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:alteraIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void download()
   {
      try
      {
         AgendaAnaliseCompletamento Ag = null;
         String CaracteresInvalidos = "\\/:*?<>|\"' &", NomeArq;
         

         // Recupera a agenda   
         Ag = buscaAgenda();
         if (Ag == null)
            enviaPaginaErro();

         // Monta o nome do arquivo e substitui os caracteres não válidos por '_'
         NomeArq = Ag.m_NomeRelatorio + "-"+m_KeyCache + ".csv";
         for (int i = 0; i < CaracteresInvalidos.length(); i++)
            NomeArq = NomeArq.replace(CaracteresInvalidos.charAt(i), '_');

         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "download.htm";
         if (Ag.getStatusArqDownload() != Agenda.STATUS_DOWNLOAD_CONCLUIDO)
            m_Args[2] = "<meta http-equiv=\"refresh\" content=\"5\">\n";
         else
            m_Args[2] = "";
         m_Args[3] = Ag.m_NomeRelatorio;
         m_Args[4] = Ag.m_DataGeracao;

         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = NoUtil.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         
         NovoDownload download = NovoDownload.getInstance(Ag);
         download.setConfiguracaoDownload(NomeArq, Ag, UsuarioAux.getConfWebAgenda());        
         
         switch (Ag.getStatusArqDownload())
         {
            case Agenda.STATUS_DOWNLOAD_PRONTO:
	            m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!" +
			    			"<br/><center> "+download.getProgressao()+" % </center>" +
			    			"<BR>Tempo Decorrido: "+download.getTempoDecorrido();
			    new Thread(download).start();
            break;
            case Agenda.STATUS_DOWNLOAD_MONTANDO_ARQ:
            	m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!" +
			    			"<br/><center> "+download.getProgressao()+" % </center>" +
			    			"<BR>Tempo Decorrido: "+download.getTempoDecorrido();
    
            break;
            case Agenda.STATUS_DOWNLOAD_COMPACTANDO:
            	m_Args[5] = "Arquivo sendo compactado!";
            break;            
            case Agenda.STATUS_DOWNLOAD_CONCLUIDO:
	            m_Args[5]  = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n";
	            m_Args[5] += "<tr>\n";
	            m_Args[5] +=  "<td align=\"center\">Clique no &iacute;cone abaixo para efetuar o download!</td>\n";
	            m_Args[5] +=  "</tr>\n";
	            m_Args[5] += "<tr>\n";
	            m_Args[5] +=  "<td align=\"center\"><a href=\"/"+DefsComum.s_ContextoWEB+"/download/"+NomeArq+".zip\"><img src=\"/PortalOsx/imagens/reldownload.gif\" border=\"0\"></a></td>\n";
	            m_Args[5] +=  "</tr></table>\n";
	            m_Args[5] +=  "<BR>Tempo Decorrido: "+download.getTempoDecorrido()+"\n";
	            Ag.m_GeraArqDownload = 0;
            break;
            case Agenda.STATUS_DOWNLOAD_ERRO:
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:download(): "+ Exc);
         Exc.printStackTrace();         
      }
   }

   public void removeAgenda()
   {
      removeAgenda(this);
   }

   public static synchronized void removeAgenda(OpApresentaRelAgendadoAnaliseCompletamento p_Operacao)
   {
      try
      {
         AgendaAnaliseCompletamento Ag = null;
         String NomeArq;

         Ag = buscaAgenda(p_Operacao);
         if (Ag != null)
         {
            NomeArq = Ag.m_NomeRelatorio + "-"+ p_Operacao.m_KeyCache + ".csv.zip";
            NomeArq = NomeArq.replace(' ', '_');      
            new java.io.File(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+NomeArq).delete();         
         }
         Object obj = s_MapAgendasMatraf.remove(p_Operacao.m_KeyCache);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:removeAgenda(): Nao foi possivel remover a agenda com chave: "+p_Operacao.m_KeyCache);
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoAnaliseCompletamento:removeAgenda(): "+ Exc);
         Exc.printStackTrace();
      }
   }
   
   public AgendaAnaliseCompletamento buscaAgenda() 
   {
      return buscaAgenda(this);
   }
   
   public static synchronized AgendaAnaliseCompletamento buscaAgenda(OpApresentaRelAgendadoAnaliseCompletamento p_Operacao)
   {
      Object ObjAg = null;
      AgendaAnaliseCompletamento Ag = null;
      
      if (s_MapAgendasMatraf.size() == 0)
      {
         Ag = p_Operacao.criaAgenda();
         return Ag;
      }

      ObjAg = s_MapAgendasMatraf.get(p_Operacao.m_KeyCache);
      if (ObjAg != null)
      {
         Ag = (AgendaAnaliseCompletamento)ObjAg;
      }
      else
      {
         Ag = p_Operacao.criaAgenda();
      }
      return Ag;
   }
   
   public void insereAgenda(AgendaAnaliseCompletamento p_Agenda) 
   {
      synchronized(s_MapAgendasMatraf)
      {
         s_MapAgendasMatraf.put(m_KeyCache, p_Agenda);
      }
      //System.out.println("OpApresentaRelAgendadoAnaliseCompletamento::insereAgenda(): Qtd no cache: " +s_MapAgendasMatraf.size());
   }
   
   public AgendaAnaliseCompletamento criaAgenda() 
   {
      boolean Retorno;
      AgendaAnaliseCompletamento Ag;
      UsuarioDef Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());

      No no = NoUtil.buscaNobyNomePerfil(m_Perfil);
      
      Ag = new AgendaAnaliseCompletamento(no.getConexaoServUtil(), 
                            m_Perfil, 
                            m_TipoRelatorio, 
                            m_IdRelatorio, 
                            m_NomeArquivoRelatorio, 
                            m_SelecaoRecursosAnaliseCompletamento, 
                            m_SelecaoIndicadorAnaliseCompletamento,
                            m_IndicadoresAnaliseCompletamento,
                            m_NomeRelatorio,
                            m_DataGeracao);

      Ag.setQtdLinhas(UsuarioAux.getConfWebAgenda().elementAt(1).toString());
      // Insere agenda no cache
      insereAgenda(Ag);      
      return Ag;
   }

   public void enviaPaginaErro()
   {
      iniciaArgs(2);
      m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
      m_Args[1] = "relatorioinexistente.htm";
      m_Html.enviaArquivo(m_Args);            
   }

class DownloadRelAgendadoAnaliseCompletamento extends java.lang.Thread
   {

	/**
	 * 
	 * @uml.property name="m_Ag"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	AgendaAnaliseCompletamento m_Ag = null;

      String m_NomeArq;      
      Vector m_vCabecalho;

      public DownloadRelAgendadoAnaliseCompletamento(String p_NomeArq, AgendaAnaliseCompletamento p_Ag)
      {
         m_Ag = p_Ag;
         m_NomeArq = p_NomeArq;
         start();
      }
      
      public void run()
      {
         Set ListaRecursos = null;

         try
         {          
            if (m_Ag.m_MapRecursosNovos.size() == 0)
               ListaRecursos = m_Ag.m_MapRecursos.entrySet();
            else
               ListaRecursos = m_Ag.m_MapRecursosNovos.entrySet();

            m_Ag.m_GeraArqDownload = 1;
            m_Ag.criaArquivoAnaliseCompletamento( m_NomeArq, 
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
                                                  m_Ag.m_Nivel,
                                                  m_Ag.m_IndicadoresAnaliseCompletamento);

            m_Ag.m_GeraArqDownload = 2;            
            Arquivo Arq = new Arquivo();
            Arq.zipaArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+m_NomeArq);
            m_Ag.m_GeraArqDownload = 3;
         }
         catch (Exception Exc)
         {
            Exc.printStackTrace();
         }
      }
   }; 
}
