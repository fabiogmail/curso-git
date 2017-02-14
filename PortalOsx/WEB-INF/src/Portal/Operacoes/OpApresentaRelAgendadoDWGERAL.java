//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApresentaRelAgendadoDesempenho.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Agenda;
import Portal.Utils.AgendaDWGERAL;
import Portal.Utils.Arquivo;
import Portal.Utils.HTMLUtil;
import Portal.Utils.NovoDownload;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 */
public class OpApresentaRelAgendadoDWGERAL extends OperacaoAbs 
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
   private Agenda m_Agenda;

   private static final String m_COR_HEADER="#000066";         // Azul escuro
   private static final String m_COR_HEADER_APR="#33CCFF";      // Azul claro
   private static final String m_COR_PERIODOS="#000066";         // Azul escuro   
   private static final String m_COR_FONTEHEADER="#FFFFFF";    // Branco
   private static final String m_COR_LINHASIM="#FFFFFF";       // Branco
   private static final String m_COR_LINHANAO="#F0F0F";        // Cinza claro
   public static final String m_FONTE="\"verdana\" size=\"1\"";
   
   private boolean m_ApagarRelatorio;
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3F212FF90378
    */
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

      // Monta a chave para inserção e busca na cache de históricos
      m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_NomeRelatorio + "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" + m_Request.getSession().getId();
      
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
/*
      else if (m_SubOperacao.toLowerCase().equals("pagpastasperiodos"))
         apresentaPastasPeriodos();
      else if (m_SubOperacao.toLowerCase().equals("pagdadosrelatorio"))
         apresentaDadosRelatorio();
*/         
      else if (m_SubOperacao.toLowerCase().equals("pagindicadores"))         
         apresentaIndicadores();
      else if (m_SubOperacao.toLowerCase().equals("pagconfiguracao"))
         apresentaConfiguracao();
      else if (m_SubOperacao.toLowerCase().equals("pagselecaopaginas"))
         apresentaSelecaoDePaginas();
      else if (m_SubOperacao.toLowerCase().equals("paglogs"))
         apresentaLogs();
      else if (m_SubOperacao.toLowerCase().equals("pagrelatoriofinal"))
         apresentaRelatorioFinal();
      else if (m_SubOperacao.toLowerCase().equals("alteraindicadores"))
         alteraIndicadores();
      else if (m_SubOperacao.toLowerCase().equals("alteraconfiguracao"))
         alteraConfiguracao();
      else if (m_SubOperacao.toLowerCase().equals("trocaperiodo"))
         trocaPeriodo();         
      else if (m_SubOperacao.toLowerCase().equals("ordenacao"))
         ordena();
      else if (m_SubOperacao.toLowerCase().equals("home"))
         home();
      else if (m_SubOperacao.toLowerCase().equals("download"))
         download();
      else if (m_SubOperacao.toLowerCase().equals("removeagenda"))
         removeAgenda();
      else
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:iniciaOperacao() - Suboperacao nao encontrada: "+ m_SubOperacao);
      
      return true;
   }

   public void apresentaPagInicial() 
   {
      try
      {
         AgendaDWGERAL Ag = null;

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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "index.htm";
         m_Args[2] = Ag.m_NomeRelatorio + " - " + Ag.m_DataGeracao;

         m_Args[3] = "DWGERAL";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaPagInicial(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaPagInicial2() 
   {
      try
      {
         iniciaArgs(10);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "index2.htm";
         m_Args[2] = m_NomeRelatorio + " - " + m_DataGeracao;
         m_Args[3] = "DWGERAL";         
         
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaPagInicial2(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaRelatorio() 
   {
      try
      {
         iniciaArgs(23);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "relatorio.htm";

         m_Args[2] = "DWGERAL";
         m_Args[3] = m_Perfil;
         m_Args[4] = m_TipoRelatorio;
         m_Args[5] = m_IdRelatorio;
         m_Args[6] = m_NomeArquivoRelatorio;
         m_Args[7] = m_NomeRelatorio;
         m_Args[8] = m_DataGeracao;

         m_Args[9] = "DWGERAL";
         m_Args[10] = m_Perfil;
         m_Args[11] = m_TipoRelatorio;
         m_Args[12] = m_IdRelatorio;
         m_Args[13] = m_NomeArquivoRelatorio;
         m_Args[14] = m_NomeRelatorio;
         m_Args[15] = m_DataGeracao;

         m_Args[16] = "DWGERAL";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaTopoRelatorio() 
   {
      try
      {
         AgendaDWGERAL Ag = null;

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         iniciaArgs(17);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "toporelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = Ag.m_NomeRelatorio;
         m_Args[7] = Ag.m_DataGeracao;

         m_Args[8] = m_Perfil;
         m_Args[9] = m_TipoRelatorio;
         m_Args[10] = m_IdRelatorio;
         m_Args[11] = m_NomeArquivoRelatorio;
         m_Args[12] = Ag.m_NomeRelatorio;
         m_Args[13] = Ag.m_DataGeracao;

         m_Args[14] = Ag.m_NomeRelatorio;
         m_Args[15] = Ag.m_DataColeta;         
         m_Args[16] = Ag.m_DataGeracao;         
         

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaTopoRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaMeioRelatorio() 
   {
      try
      {
         AgendaDWGERAL Ag = null;
         Vector RelProcessado, LinhaRelProcessado;
         String Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
         String NomePeriodo;

         // Verifica se há mudança de página
         // Valores possíveis: "proxima"
         //                    "anterior"
         String TipoNavegacao = m_Request.getParameter("navega");
         String Periodo = m_Request.getParameter("periodo");
         int Tam, Posicao = 0, iPeriodo; 
         int QtdPaginas=0;

         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }       
            
         try
         {
            if (Periodo == null && Ag.m_UltProcessado == 0) iPeriodo = 0;
            else if (Periodo == null && Ag.m_UltProcessado != 0) iPeriodo = Ag.m_UltProcessado;
            else iPeriodo = Integer.parseInt(Periodo);
         }
         catch (Exception Exc)
         {
            iPeriodo = 0;
         }

         Ag.m_UltProcessado = iPeriodo;

         // Recupera o relatório a apresentar calcula páginas
         RelProcessado = Ag.m_RelatoriosProcessados[iPeriodo];
         QtdPaginas = Integer.parseInt(""+Ag.m_QtdLinhasPer.elementAt(iPeriodo))/Ag.m_QtdLinhasPagina;

         Tam = RelProcessado.size();             

         if (TipoNavegacao != null && TipoNavegacao.equals("proxima"))
         {            
            if (Ag.m_Pag < QtdPaginas)
               Ag.m_Pag++;

            Posicao = Ag.m_Pag*Ag.m_QtdLinhasPagina;  

            Ag.m_Linhas.removeElementAt(iPeriodo);
            Ag.m_RetRelatorio.posicionaLinha((short)iPeriodo,Posicao); 
            Vector tmp = Ag.m_RetRelatorio.getRelatorio((short)iPeriodo,Ag.m_QtdLinhasPagina);
  
            Ag.m_Linhas.add(iPeriodo, tmp);
            RelProcessado = Ag.montaLinhas(iPeriodo,Short.parseShort(m_TipoRelatorio)); 
         }
         else if (TipoNavegacao != null && TipoNavegacao.equals("anterior"))
         {
            if (Ag.m_Pag > 0) 
               Ag.m_Pag--;
               
            Posicao = Ag.m_Pag*Ag.m_QtdLinhasPagina;           
                                                            
            Ag.m_Linhas.removeElementAt(iPeriodo);
            Ag.m_RetRelatorio.posicionaLinha((short)iPeriodo,Posicao);
            Ag.m_Linhas.add(iPeriodo, Ag.m_RetRelatorio.getRelatorio((short)iPeriodo,Ag.m_QtdLinhasPagina));
				RelProcessado = Ag.montaLinhas(iPeriodo,Short.parseShort(m_TipoRelatorio));                    
         }
         else if (TipoNavegacao != null)
         {
            int Pag = Ag.m_Pag;     
            try
            {
               Ag.m_Pag = Integer.parseInt(TipoNavegacao) - 1;
               if (Ag.m_Pag > QtdPaginas) Ag.m_Pag = QtdPaginas;
            }
            catch (Exception Exc)
            {
               Ag.m_Pag = Pag;
            }
            Posicao = Ag.m_Pag*Ag.m_QtdLinhasPagina;    
            Ag.m_Linhas.removeElementAt(iPeriodo);
            Ag.m_RetRelatorio.posicionaLinha((short)iPeriodo,Posicao);
            Ag.m_Linhas.add(iPeriodo, Ag.m_RetRelatorio.getRelatorio((short)iPeriodo,Ag.m_QtdLinhasPagina));
            RelProcessado = Ag.montaLinhas(iPeriodo,Short.parseShort(m_TipoRelatorio));
         }
         else
         {
        	
            Posicao = Ag.m_Pag*Ag.m_QtdLinhasPagina;
            Ag.m_RetRelatorio.posicionaLinha((short)iPeriodo,Posicao);
            Vector linhasAux = Ag.m_RetRelatorio.getRelatorio((short)iPeriodo,Ag.m_QtdLinhasPagina);
            if(linhasAux.size() == 0){
            	removeAgenda();
            	Ag = criaAgenda();
            	Ag.m_RetRelatorio.posicionaLinha((short)iPeriodo,Posicao);
            	linhasAux = Ag.m_RetRelatorio.getRelatorio((short)iPeriodo,Ag.m_QtdLinhasPagina);
            }
            Ag.m_Linhas.removeElementAt(iPeriodo);
            Ag.m_RetRelatorio.posicionaLinha((short)iPeriodo,Posicao);
            Ag.m_Linhas.add(iPeriodo, linhasAux);                    
            Ag.remontaRelatorios(Short.parseShort(m_TipoRelatorio));
            RelProcessado = Ag.m_RelatoriosProcessados[iPeriodo]; 
         }
         
         m_Html.enviaInicio("", "", "agenda.js");
         m_Html.envia("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

         Vector vPeriodos = new Vector(Ag.m_QtdPeriodos);
         Vector vLinks = new Vector(Ag.m_QtdPeriodos);         
         String Link;

         for (int i = 0; i < Ag.m_QtdPeriodos; i++)
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

         // Tabela de linhas do relatório para o período desejado
         m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
         m_Html.envia("<tr bgcolor=\""+m_COR_HEADER+"\">\n   ");
       
         for (int k = 0; k < Ag.m_vIndicadores.size(); k++)
         {
            m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\""+m_COR_FONTEHEADER+"\"><b>"
                          /*
                          +"<a href=\"javascript:Ordena('"
                          +m_Perfil+"','"
                          +m_TipoRelatorio+"','"
                          +m_IdRelatorio+"','"
                          +m_NomeArquivoRelatorio+"','"
                          //+m_NomeRelatorio+"','"
                          //+m_DataGeracao+"','"
                          +"ascendente"+"','"
                          +k+"')\""   // Coluna para ordenação
                          +" class=\"link2\" "
                          +"onmouseover=\"window.status='Ordenar por "+Ag.m_vIndicadores.elementAt(k).toString()+"';return true;\" onmouseout=\"window.status='';return true;\""
                          +">"
                          */
                          +Ag.m_vIndicadores.elementAt(k).toString()
                          //+"</a>"
                          +"</font></b></td>");
         }
         m_Html.envia("\n</tr>\n");

         // Envia linhas do relatório          

         for (int j = 0; j < RelProcessado.size(); j++) 
         {
            if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
            else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";

            LinhaRelProcessado = (Vector)RelProcessado.elementAt(j);
            
            m_Html.envia("<tr"+Cor+">\n   ");
            for (int k = 0; k < LinhaRelProcessado.size(); k++)
            {
               m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+LinhaRelProcessado.elementAt(k).toString()+"</font></td>");
            }
            m_Html.envia("\n</tr>\n");
         }

         m_Html.envia("</table>\n");

         m_Html.envia("</td>\n");
         m_Html.envia("</tr>\n");
         m_Html.envia("</table>\n");
         
         m_Html.enviafinal(null, null, true);       
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaMeioRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void trocaPeriodo()
   {
      AgendaDWGERAL Ag = null;

      Ag = buscaAgenda();
      if (Ag == null)
      {
         iniciaArgs(2);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "relatorioinexistente.htm";
         m_Html.enviaArquivo(m_Args);            
         return;
      }
      Ag.m_Pag = 0;
      apresentaMeioRelatorio();
   }

   public void apresentaBaseRelatorio()
   {
      try
      {
         AgendaDWGERAL Ag;
         String Paginacao = "";

         Thread.sleep(600);
         
         Ag = buscaAgenda();
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);
            return;
         }

         while (Ag.m_RelatoriosProcessados[Ag.m_UltProcessado] == null)
         {
            Thread.sleep(200);
         }

         Paginacao = (Ag.m_Pag + 1) + "/" + (((Integer.parseInt(""+Ag.m_QtdLinhasPer.elementAt(Ag.m_UltProcessado))-1)/Ag.m_QtdLinhasPagina) + 1);
         
         iniciaArgs(10);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "baserelatorio.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;
         m_Args[8] = Ag.m_QtdPeriodos+"";
         m_Args[9] = Paginacao;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaBaseRelatorio(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaIndicadores()
   {
      try
      {
         AgendaDWGERAL Ag;
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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "indicadores.htm";
         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         if (Ag.m_IndicadoresPossiveis == null)
         {
            QtdIndicadores = Ag.m_IndicadoresDWGERAL.size();            
            for (i = 0; i < QtdIndicadores; i++)
               Indicadores1 += "                   <option value=\""+ Ag.m_IndicadoresDWGERAL.elementAt(i).toString() +"\">" + Ag.m_IndicadoresDWGERAL.elementAt(i).toString() + "</option>\n";
            Ag.m_IndicadoresPossiveis = Indicadores1;
         }

//         if (Ag.m_Rec[1] != null) i = 2;
//         else i = 1;
         if (Ag.m_Recv.size() > 1 ){ 
          	i = 2;
          }else{ 
          	i = 1;
          }
          boolean ehRecurso;
          
//         for (; i < Ag.m_vIndicadores.size(); i++)
//            Indicadores2 += "                   <option value=\""+ Ag.m_vIndicadores.elementAt(i).toString() +"\">" + Ag.m_vIndicadores.elementAt(i).toString() + "</option>\n";
          
          for (; i < Ag.m_vIndicadores.size(); i++){
             	ehRecurso = false;
             	for(int j=0;j<Ag.m_Recv.size();j++){
    	         	if(Ag.m_Recv.elementAt(j).toString().equals(Ag.m_vIndicadores.elementAt(i).toString())){
    	         		ehRecurso = true;
    	         		break;
    	         	}         		
             	}
             	if(!ehRecurso){
             		Indicadores2 += "                   <option value=\""+ Ag.m_vIndicadores.elementAt(i).toString() +"\">" + Ag.m_vIndicadores.elementAt(i).toString() + "</option>\n";
             	}
           }
          
           m_Args[8] = Ag.m_IndicadoresPossiveis;
           m_Args[9] = Indicadores2;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaConfiguracao() 
   {
      try
      {
         AgendaDWGERAL Ag;      
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
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "configuracao.htm";

         m_Args[2] = m_Perfil;
         m_Args[3] = m_TipoRelatorio;
         m_Args[4] = m_IdRelatorio;
         m_Args[5] = m_NomeArquivoRelatorio;
         m_Args[6] = m_NomeRelatorio;
         m_Args[7] = m_DataGeracao;

         for (int i = 0; i < 4; i++)
            m_Args[i+8] = (String)Configuracao.elementAt(i);
         
         if (Ag.m_Periodos == null && Ag.m_QtdPeriodos > 1)
         {
            Periodos += "      <tr>\n";
            Periodos += "         <td valign=\"top\"><font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">Per&iacute;odos:</font></td>\n";
            Periodos += "         <td>\n";
            Periodos += "            <select name=\"listaperiodos\" class=\"lista\" size=\"3\" multiple>\n";

            for (int i = 0; i < Ag.m_QtdPeriodos; i++)
            {
               if (Ag.m_NomesDatas != null)
                  Texto = Ag.m_NomesDatas.elementAt(i).toString();
               else
                  Texto = Ag.m_Datas.elementAt(i).toString();
            
               Periodos += "                   <option value=\""+ Texto +"\" selected>" + Texto + "</option>\n";
            }

            Periodos += "            </select>\n";
            Periodos += "         </td>\n";
            Periodos += "      </tr>\n";
         
            Ag.m_Periodos = Periodos;
         }
         if (Ag.m_QtdPeriodos > 1) m_Args[12] = "1";
         else m_Args[12] = "0";

         m_Args[13] = "DWGERAL";
         m_Args[14] = Ag.m_Periodos == null ? "" : Ag.m_Periodos;

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaConfiguracao(): "+ Exc);
         Exc.printStackTrace();
      }    
   }

   public void apresentaRelatorioFinal() 
   {
      try
      {
         AgendaDWGERAL Ag;
         int i, iPagInicial, iPagFinal, Posicao, Tam;         
         String Cabecalho = "";
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         Vector vCabecalho = null, RelProcessado, LinhaRelProcessado, Log;
         Vector CfgAg = UsuarioAux.getConfWebAgenda();
         String PagInicial = m_Request.getParameter("paginicial");
         String PagFinal = m_Request.getParameter("pagfinal");
         String Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";
         
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

         if (CfgAg.elementAt(0).equals("1") == true)   // Tipo de visualização
            m_Response.setContentType("application/vnd.ms-excel");

         Ag = buscaAgenda();
         Ag.getLinhasAposCabecalho();
         Ag.remontaRelatorios(Short.parseShort(m_TipoRelatorio));
         
         if (Ag == null)
         {
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "relatorioinexistente.htm";
            m_Html.enviaArquivo(m_Args);            
            return;
         }

         if (CfgAg.elementAt(2).equals("1") == true)   // Cabeçalho
         {
            int iTam;
            String Aux1, Aux2;

            // Recupera o cabeçalho
            vCabecalho = new Vector (Ag.m_Cabecalho);

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

         m_Html.enviaInicio("CDRView", Ag.m_NomeRelatorio+" - "+Ag.m_DataGeracao, null);
         m_Html.envia("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");
              
         /* Monta tabela com os dados do Cabecalho do Relatorio */
         m_Html.envia(HTMLUtil.getCabecalho());
        
         m_Html.envia("<font face="+m_FONTE+">");
         m_Html.envia(Cabecalho);

         for (int z = 0; z < Ag.m_PeriodosApresentaveis.size(); z++)         
         {
            i = ((Integer)Ag.m_PeriodosApresentaveis.elementAt(z)).intValue();
            
            // Tabela de linhas do relatório para o período desejado
            m_Html.envia("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
            m_Html.envia("   <tr>\n   ");
            m_Html.envia("<td align=\"left\" colspan=\""+Ag.m_vIndicadores.size()+"\">");
            m_Html.envia("<font face="+m_FONTE+">");
            if (Ag.m_NomesDatas != null)
               m_Html.envia("<b>Per&iacute;odo: </b>" + Ag.m_NomesDatas.elementAt(i) + "&nbsp;["+Ag.m_Datas.elementAt(i)+"]");
            else
               m_Html.envia(Ag.m_Datas.elementAt(i).toString());
            m_Html.envia("</font>");
            m_Html.envia("</td>\n");            
            m_Html.envia("   </tr>\n");            
            m_Html.envia("   <tr bgcolor=\""+m_COR_HEADER_APR+"\">\n   ");
            for (int k = 0; k < Ag.m_vIndicadores.size(); k++)
            {
               m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+" color=\"#000000\"><b>"
                             +Ag.m_vIndicadores.elementAt(k).toString()
                             +"</font></b></td>");
            }
            m_Html.envia("\n   </tr>\n");

            // Envia linhas do relatório
            Tam = Ag.m_RelatoriosProcessados[i].size(); 
            RelProcessado = Ag.m_RelatoriosProcessados[i];
            for (int j = 0; j < Tam; j++)            
            {
               if (j%2 != 0) Cor = " bgcolor=\""+m_COR_LINHANAO+"\"";
               else Cor = " bgcolor=\""+m_COR_LINHASIM+"\"";

               LinhaRelProcessado = (Vector)RelProcessado.elementAt(j);

               m_Html.envia("   <tr"+Cor+">\n   ");
               for (int k = 0; k < LinhaRelProcessado.size(); k++)
               {
                  if (k == 0) m_Html.envia("      ");
                  m_Html.envia("<td align=\"center\" nowrap><font face="+m_FONTE+">"+LinhaRelProcessado.elementAt(k).toString()+"</font></td>");
               }
               m_Html.envia("\n   </tr>\n");
            }
            m_Html.envia("</table>\n");
            
            m_Html.envia("<br>");

            // Envia logs se estiver configurado para tal
            if (CfgAg.elementAt(3).equals("1") == true)   // Log
            {
               Log = (Vector)Ag.m_Logs.elementAt(i);
               if (Log.elementAt(0).toString().equals("nolog") == false)
               {
                  m_Html.envia("<table>\n");
                  m_Html.envia("   <tr>\n      <td align=\"left\"><font face="+m_FONTE+"><b>Logs:</b></font></td>\n   </tr>\n");
                  int TamLog = Log.size();
                  for (int j = 0; j < TamLog; j++)
                     m_Html.envia("   <tr>\n      <td><font face="+m_FONTE+">"+Log.elementAt(j).toString().substring(0, Log.elementAt(j).toString().length()-1)+"</font></td>\n   </tr>\n");
                  m_Html.envia("</table>\n");
                  m_Html.envia("<br>");
               }
            }
         }

         m_Html.envia("</body>\n");
         m_Html.enviafinal(null, null, true);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaRelatorioFinal(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void apresentaSelecaoDePaginas() 
   {
      try
      {
         iniciaArgs(8);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaSelecaoDePaginas(): "+ Exc);
         Exc.printStackTrace();
      }   
   }

   public void apresentaLogs()
   {
      try
      {
         AgendaDWGERAL Ag;

         Ag = buscaAgenda();
         Vector Log = (Vector)Ag.m_Logs.elementAt(Ag.m_UltProcessado);
         String Logs = "", Cabecalho = "", strAux;

         for (int i = 0; i < Ag.m_Cabecalho.size(); i++)
         {
            strAux = (String)Ag.m_Cabecalho.elementAt(i);
            Cabecalho += "   <tr>\n      <td><font face="+m_FONTE+"><b>"+strAux.substring(0, strAux.indexOf(':')+1)+"</b>&nbsp;";
            Cabecalho += strAux.substring(strAux.indexOf(':')+1, strAux.length())+"</font></td>\n   </tr>\n";
         }
         
         int TamLog = Log.size();         
         for (int j = 0; j < TamLog; j++)
         {      
            if (j == 0 && Log.elementAt(j).toString().equals("nolog") == true)           
               Logs += "   <tr>\n      <td><font face="+m_FONTE+">N&atilde;o h&aacute; logs para este per&iacute;odo</font></td>\n   </tr>\n";
            else
               Logs += "   <tr>\n      <td><font face="+m_FONTE+">"+Log.elementAt(j).toString().substring(0, Log.elementAt(j).toString().length()-1)+"</font></td>\n   </tr>\n";
         }

         iniciaArgs(5);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "logs.htm";

         m_Args[2] = Cabecalho;
         if (Ag.m_NomesDatas != null) m_Args[3] = Ag.m_NomesDatas.elementAt(Ag.m_UltProcessado).toString();
         else m_Args[3] = Ag.m_Datas.elementAt(Ag.m_UltProcessado).toString();
         m_Args[4] = Logs;         

         m_Html.enviaArquivo(m_Args);         
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:apresentaLogs(): "+ Exc);
         Exc.printStackTrace();
      }   
   }

   public void alteraIndicadores()
   {
      try
      {
         AgendaDWGERAL Ag = null;
         String Indicadores = m_Request.getParameter("indicadores");
   
         Ag = buscaAgenda();
         if (Ag != null)
         {
            Indicadores = Indicadores.replace('@', '%');
//            if (Ag.m_Rec[1] != null)
//               Indicadores = Ag.m_vIndicadores.elementAt(0).toString()+";"+ Ag.m_vIndicadores.elementAt(1).toString()+";"+Indicadores;
            if(Ag.m_Recv.size() > 1){
            	String copiaDosIndicadores = Indicadores;
            	Indicadores = "";
            	for(int i=0;i<Ag.m_Recv.size();i++){
            		Indicadores += Ag.m_vIndicadores.elementAt(i).toString()+";";
            	}
            	Indicadores += copiaDosIndicadores;
            }
            else
               Indicadores = Ag.m_vIndicadores.elementAt(0).toString()+";"+Indicadores;

            Ag.m_vIndicadores = VetorUtil.String2Vetor(Indicadores, ';');
            Ag.remontaRelatorios(Short.parseShort(m_TipoRelatorio));
            Ag.m_Pag = 0;
            apresentaMeioRelatorio();
         }
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:alteraIndicadores(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public void alteraConfiguracao() 
   {
      UsuarioDef Usuario = null;
      AgendaDWGERAL Ag = null;      
      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      if (Usuario != null)
      {
         Ag = buscaAgenda();
         if (Ag == null)
            return;

         String Configuracao = m_Request.getParameter("configuracao");
         String Periodos = m_Request.getParameter("periodos");
         int l_QtdLinhasPaginasAnt = Ag.m_QtdLinhasPagina;
         Ag.setQtdLinhas(Configuracao.charAt(1)+"");
         
         if (Ag.m_QtdLinhasPagina != l_QtdLinhasPaginasAnt)
         {            
            Vector tmp=null; 
            for (short i = 0; i < Ag.m_RetRelatorio.getPeriodos().size(); i++) 
            {
               // Posiciona a linha no inicio de cada periodo.                   
               Ag.m_RetRelatorio.posicionaLinha(i,0);               
               Ag.m_Linhas.removeElementAt(i);
               Ag.m_Linhas.add(i,Ag.m_RetRelatorio.getRelatorio(i,Ag.m_QtdLinhasPagina));
               tmp = (Vector) Ag.m_Linhas.elementAt(i);               
            }
      
            Ag.m_Linhas.trimToSize();
            Ag.remontaRelatorios(Short.parseShort(m_TipoRelatorio));
         }
         
         Usuario.setConfWebAgenda(Configuracao);
         Usuario.getNo().getConexaoServUtil().alteraUsuario(Usuario);

         Vector PeriodosApresentaveis = VetorUtil.String2Vetor(Periodos, ';');
         Ag.m_PeriodosApresentaveis = new Vector(PeriodosApresentaveis.size());    
         for (int i = 0; i < PeriodosApresentaveis.size(); i++)
         {
            Ag.m_PeriodosApresentaveis.add(new Integer(PeriodosApresentaveis.elementAt(i).toString()));           
         }
      }
      else
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:alteraConfiguracao(): Usuario nao foi encontrado");
   }

   public void ordena() 
   {
/*   
      AgendaDWGERAL Ag = null;
      String Coluna = m_Request.getParameter("coluna");
      
      Ag = buscaAgenda();
      if (Ag == null)
      {
         iniciaArgs(2);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "relatorioinexistente.htm";
         m_Html.enviaArquivo(m_Args);            
         return;
      }

      Ag.m_Pag = 0;
      try
      {
         Ag.fnOrdena(Integer.parseInt(Coluna));
         apresentaMeioRelatorio(Ag);         
      }
      catch (Exception Exc)
      {
         System.out.println(m_ConexUtil.getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:ordena(): "+ Exc);
         Exc.printStackTrace();
      }
*/      
   }

   public void home() 
   {
      AgendaDWGERAL Ag = null;      
      
      Ag = buscaAgenda();
      if (Ag == null)
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

         if (Tipo.equals("inicio")) Ag.m_Pag = 0;
         else 
         {
         	if ((Integer.parseInt(""+Ag.m_QtdLinhasPer.elementAt(Ag.m_UltProcessado)))%Ag.m_QtdLinhasPagina == 0)
					QtdPaginas = (Integer.parseInt(""+Ag.m_QtdLinhasPer.elementAt(Ag.m_UltProcessado)))/Ag.m_QtdLinhasPagina;
				else
					QtdPaginas = (Integer.parseInt(""+Ag.m_QtdLinhasPer.elementAt(Ag.m_UltProcessado)))/Ag.m_QtdLinhasPagina + 1;
					
				Ag.m_Pag = QtdPaginas-1;
         }
         apresentaMeioRelatorio();
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:home(): "+ Exc);
         Exc.printStackTrace();
      }
   }  

   public void download()
   {
      try
      {
         AgendaDWGERAL Ag = null;
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         UsuarioDef UsuarioAux = NoUtil.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
         String NomeArq;
         Vector vCabecalho;
   
         Ag = buscaAgenda();
         
         NomeArq = Ag.m_NomeRelatorio + "-"+m_KeyCache;// + ".csv";
         NomeArq = NomeArq.replace(' ', '_');
        
//         vCabecalho = new Vector (Ag.m_Cabecalho);

         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+"agenda/";
         m_Args[1] = "download.htm";
         if (Ag.getStatusArqDownload() != Agenda.STATUS_DOWNLOAD_CONCLUIDO)
            m_Args[2] = "<meta http-equiv=\"refresh\" content=\"5\">\n";
         else
            m_Args[2] = "";
         m_Args[3] = Ag.m_NomeRelatorio;
         m_Args[4] = Ag.m_DataGeracao;
         
         NovoDownload download = NovoDownload.getInstance(Ag);
         download.setConfiguracaoDownload(NomeArq, Ag, UsuarioAux.getConfWebAgenda());
         
         switch (Ag.getStatusArqDownload())
         {
             case Agenda.STATUS_DOWNLOAD_PRONTO:
            	m_Args[5] = "Aguarde a cria&ccedil;&atilde;o do arquivo para download!" +
    		    			"<br/><center> "+download.getProgressao()+" % </center>" +
    		    			"<BR>Tempo Decorrido: "+download.getTempoDecorrido();
	            new Thread(download).start();
	            //Thread.sleep(1000);
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
	            m_Args[5] +=  "<td align=\"center\"><a href=\"/"+DefsComum.s_ContextoWEB+"/download/"+NomeArq+(UsuarioAux.getConfWebAgenda().elementAt(0).equals("1") ? ".csv" : ".htm")+".zip\"><img src=\"/PortalOsx/imagens/reldownload.gif\" border=\"0\"></a></td>\n";
	            m_Args[5] +=  "</tr></table>\n";
	            m_Args[5] +=  "<BR><center>Tempo Decorrido: "+download.getTempoDecorrido()+"\n </center>";
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
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:download(): "+ Exc);
         Exc.printStackTrace();         
      }
   }

   public void removeAgenda()
   {
      removeAgenda(this);
   }

   public static synchronized void removeAgenda(OpApresentaRelAgendadoDWGERAL p_Operacao)
   {
      try
      {
         AgendaDWGERAL Ag = null;
         String NomeArq[] = null;
         String Tipos[] = {".csv", ".htm"};

         Ag = buscaAgenda(p_Operacao);
         if (Ag != null)         {
            
            NomeArq = new String[2];
            for (int i = 0; i < 2; i++)
            {
               NomeArq[i] = Ag.m_NomeRelatorio + "-"+ p_Operacao.m_KeyCache + Tipos[i] +".zip";
               NomeArq[i] = NomeArq[i].replace(' ', '_');
               new java.io.File(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+NomeArq[i]).delete();
            }
         }
         
         Ag.m_iRetRelatorio.fnFecha();
         Ag.m_iListaRelatorios.fnDestroy();
         s_MapAgendasDWGERAL.remove(p_Operacao.m_KeyCache);
      }
      catch (Exception Exc)
      {
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:removeAgenda(): Nao foi possivel remvoer a agenda com chave: "+p_Operacao.m_KeyCache);
         System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " OpApresentaRelAgendadoDWGERAL:removeAgenda(): "+ Exc);
         Exc.printStackTrace();
      }
   }

   public AgendaDWGERAL buscaAgenda() 
   {
       if (m_ApagarRelatorio)
       {
           UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
           Agenda.fechaRelatoriosUsuario(Usuario);
       }
       
       return buscaAgenda(this);
   }

   public static synchronized AgendaDWGERAL buscaAgenda(OpApresentaRelAgendadoDWGERAL p_Operacao)
   {
      Object ObjAg = null;
      AgendaDWGERAL Ag = null;
      
      if (s_MapAgendasDWGERAL.size() == 0)
      {
         Ag = p_Operacao.criaAgenda();
         return Ag;
      }

      ObjAg = s_MapAgendasDWGERAL.get(p_Operacao.m_KeyCache);
      if (ObjAg != null)
      {
         Ag = (AgendaDWGERAL)ObjAg;
      }
      else
      {
         Ag = p_Operacao.criaAgenda();
      }
      
      return Ag;
   }

   public void insereAgenda(AgendaDWGERAL p_Agenda) 
   {
      synchronized(s_MapAgendasDWGERAL)
      {
         s_MapAgendasDWGERAL.put(m_KeyCache, p_Agenda);
      }      
   }

   public AgendaDWGERAL criaAgenda() 
   {
      boolean Retorno;
      AgendaDWGERAL Ag;
      UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
      
      Agenda.fechaRelatoriosUsuario(Usuario);
      No no = NoUtil.buscaNobyNomePerfil(m_Perfil);
      Ag = new AgendaDWGERAL(no.getConexaoServUtil(), 
                                m_Perfil, 
                                m_TipoRelatorio, 
                                m_IdRelatorio, 
                                m_NomeArquivoRelatorio, 
                                m_SelecaoRecursosDwGeral, 
                                m_SelecaoIndicadorDwGeral,
                                m_IndicadoresDwGeral,
                                m_NomeRelatorio,
                                m_DataGeracao);
      m_Agenda = Ag;
                                
      int l_QtdLinhasPaginasAnt = Ag.m_QtdLinhasPagina;                         
      Ag.setQtdLinhas(UsuarioAux.getConfWebAgenda().elementAt(1).toString());
         
      if (Ag.m_QtdLinhasPagina != l_QtdLinhasPaginasAnt)
      {            
         Vector tmp=null; 
         for (short i = 0; i < Ag.m_RetRelatorio.getPeriodos().size(); i++) 
         {
            // Posiciona a linha no inicio de cada periodo.
   //         m_RetRelatorio.posicionaLinha(Short.parseShort(""+m_RetRelatorio.getPeriodos().elementAt(i)),0);
            Ag.m_RetRelatorio.posicionaLinha(i,0);               
            Ag.m_Linhas.removeElementAt(i);
            Ag.m_Linhas.add(i,Ag.m_RetRelatorio.getRelatorio(i,Ag.m_QtdLinhasPagina));
            tmp = (Vector) Ag.m_Linhas.elementAt(i);            
         }
      
         Ag.m_Linhas.trimToSize();
         Ag.remontaRelatorios(Short.parseShort(m_TipoRelatorio));
      }
      // Insere agenda no cache
      insereAgenda(Ag); 
      
      if (!Usuario.getM_ListAgendasUsuario().contains(Ag))
      {
          Usuario.getM_ListAgendasUsuario().add(Ag);
      }

      return Ag;
   }

class DownloadRelAgendadoDWGERAL extends java.lang.Thread
   {

	/**
	 * 
	 * @uml.property name="m_Ag"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	AgendaDWGERAL m_Ag = null;

      String m_NomeArq;      
      Vector m_vCabecalho;
      Vector m_CfgWeb;

      public DownloadRelAgendadoDWGERAL(String p_NomeArq, Vector p_vCabecalho, AgendaDWGERAL p_Ag, Vector p_CfgWeb)
      {
         m_Ag = p_Ag;
         m_NomeArq = p_NomeArq;
         m_vCabecalho = p_vCabecalho;
         m_CfgWeb = p_CfgWeb;
         start();
      }
      
      public void run()
      {
         m_Ag.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_MONTANDO_ARQ);
         
         m_Ag.criaArquivo(m_NomeArq, 
                          m_vCabecalho, 
                          m_Ag.m_vIndicadores, 
                          m_Ag.m_PeriodosApresentaveis, 
                          m_Ag.m_NomesDatas, 
                          m_Ag.m_Datas, 
                          m_Ag.m_RelatoriosProcessados, 
                          m_Ag.m_Logs,
                          m_CfgWeb);
         m_Ag.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_COMPACTANDO);            
         Arquivo Arq = new Arquivo();
         Arq.zipaArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"download/"+m_NomeArq + (m_CfgWeb.elementAt(0).equals("1") ? ".csv": ".htm"));
         m_Ag.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_CONCLUIDO);
      }
   };
}
