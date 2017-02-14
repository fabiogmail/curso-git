//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVisualizaRelAgendado.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.VetorUtil;

/**
 */
public class OpVisualizaRelAgendado extends OperacaoAbs 
{
   public String m_Perfil  = null;
   public String m_TipoRel = null;
   public String m_IdRel   = null;
   public String m_Arquivo = null;
   public String[] m_Rec = null;
   public String m_Indicadores = null;
   public Vector m_Linhas = null;   

   static 
   {
   }
   
   /**
    * @return 
    * @exception
    * @roseuid 3C7006A600CC
    */
   public OpVisualizaRelAgendado()
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception
    * @roseuid 3C7006A600E0
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      try
      {
         setOperacao("Visualização de Relatório Agendado");

         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "visrelformgen1.htm";
         m_Args[2] = "Relatório Agendado";
         m_Args[3] = "src=\"PortalOsx/templates/js/indicadores.js\"";
         m_Args[4] = "";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "visrelformgen1.form", montaFormulario("$ARG;"));
         m_Html.enviaArquivo(m_Args);

         // Monta e ENVIA a tabela contendo o relatório
         montaTabela(m_Rec, m_Indicadores, (String)m_Linhas.elementAt(15), m_Linhas);

         iniciaArgs(3);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "visrelformgen2.htm";
         m_Args[2] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "visrelformgen2.form", montaFormulario2());
         m_Html.enviaArquivo(m_Args);

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpVisualizaRelAgendado - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();

         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";
         m_Args[2] = "erro.gif";
         m_Args[3] = "<b>&nbsp;N&atilde;o foi poss&iacute;vel apresentar o relat&oacute;rio!!</b><br>";
         m_Html.enviaArquivo(m_Args);

         return false;
      }
   }
   
   /**
    * @param p_Indicadores
    * @param p_Linhas
    * @param p_Rec
    * @return Vector
    * @exception 
    * @roseuid 3C7006A600F4
    */
   public Vector montaLinhasDesempenho(Vector p_Indicadores, Vector p_Linhas, String[] p_Rec)
   {
      boolean bTotal = false, bPrimeira = true, bSetaCor = false;
      int InicioIndicador = 1;
      String Aux = null, Aux1 = null, Aux2 = null, NomePeriodo = "";
      String Rec[] = new String [3], StrIndicador, DataInicial, DataFinal, Periodo, NomeIndicador;
      String TabelaResp = "", Cor = "", Alinhamento = "center";
      Vector Linhas, Coluna, ListaIndicadores;

      Rec = identificaRecursos(p_Indicadores, (String)p_Linhas.elementAt(12), (String)p_Linhas.elementAt(13));
      ListaIndicadores = new Vector();

      // Cria vetor com todos os recursos do relatório
      if (Rec[1] != null)
         InicioIndicador++;
      for (int j = 0; j < InicioIndicador; j++)
         ListaIndicadores.addElement(m_SelecaoRecursosDesempenho.fnGetIndicador((String)p_Indicadores.elementAt(j), 
                                                                                Short.parseShort(m_TipoRel),
                                                                                null));

      // Cria vetor com todos os indicadores do relatório
      for (int j = InicioIndicador; j < p_Indicadores.size(); j++)
         ListaIndicadores.addElement(m_SelecaoIndicadorDesempenho.fnGetIndicador((String)p_Indicadores.elementAt(j), 
                                                                                 Short.parseShort(m_TipoRel),
                                                                                 null));
      ListaIndicadores.trimToSize();

      DataInicial = (String)p_Linhas.elementAt(3);
      DataFinal = (String)p_Linhas.elementAt(4);

      TabelaResp += "<table width=\"580\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\n";
      Linhas = new Vector ();

      // As linhas do relatório começam na posição 16!!
      for (int i = 16; i < p_Linhas.size(); i++)
      {
         Aux = (String)p_Linhas.elementAt(i);
         if (Aux.indexOf("5;") == 0)
            continue;

         if (Aux.indexOf("2;") == 0)
         {
            NomePeriodo = Aux.substring(Aux.lastIndexOf(";")+1, Aux.length());
            if (NomePeriodo.length() == 0)
               NomePeriodo = "N/A";
            Aux1 = Aux.substring(Aux.indexOf(";")+1, Aux.lastIndexOf(";"));
            Aux2 = Aux1.substring(0, Aux1.indexOf(";"));
            Aux1 = Aux1.substring(Aux2.length()+1, Aux1.length());

            bTotal = true;

            if (!bPrimeira)
            {
               //if (bSetaCor)
               TabelaResp += "<tr bgcolor=\"" + Cor + "\">\n";
               TabelaResp += "<td align=\"left\" colspan=\"" + ListaIndicadores.size() + "\"><font color=\"#FFFFFF\">&nbsp;<font></td>\n";
               TabelaResp += "</tr>\n";
            }

            Periodo = "Per&iacute;odo: ["+ NomePeriodo + "]&nbsp;&nbsp;&nbsp;&nbsp;In&iacute;cio: ["+ geraData(Aux2) + "] &nbsp;&nbsp; " + "Fim: [" + geraData(Aux1) + "]";

            TabelaResp += "<tr bgcolor=\"#000033\">\n";
            TabelaResp += "<td align=\"left\" colspan=\"" + ListaIndicadores.size() + "\"><font color=\"#FFFFFF\"><b>"+Periodo+"</b><font></td>\n";
            TabelaResp += "</tr>\n";

            TabelaResp += "<tr bgcolor=\"#000033\">\n";
            for (int k = 0; k < p_Indicadores.size(); k++)
            {
               if (k == 0)
                  Alinhamento = "left";
               else if (p_Rec[1] != null && k == 1)
                  Alinhamento = "left";
               else
                  Alinhamento = "center";
               TabelaResp += "<td align=\"" + Alinhamento + "\"><font color=\"#FFFFFF\"><b>"+(String)p_Indicadores.elementAt(k)+"</b><font></td>\n";
            }
            TabelaResp += "</tr>\n";
            
            bPrimeira = false;

            Cor = "#FFFFFF";   // Branco

            continue;
         }

      /* Ainda Falta CDRView NOVO */
         // Traduz a linha para uma estrutura de contadores
//         Contador Cont = NoCampoArvoreRelatorioDesempenho.fnGetContador(Rec, Aux, false);
//         Cont.fnDataInicial(DataInicial);
//         Cont.fnDataFinal(DataFinal);

         TabelaResp += "<tr bgcolor=\"" + Cor + "\">\n";
         // Processa os indicadores que devem ser apresentados
         for (int j = 0; j < ListaIndicadores.size(); j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor) ListaIndicadores.elementAt(j);

            if (NoCampoInd != null)
            {
//               StrIndicador = NoCampoInd.fnProcessa(Cont).toString();
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), null).toString();

               if (j == 0)
                  Alinhamento = "left";
               else if (p_Rec[1] != null && j == 1)
                  Alinhamento = "left";
               else
                  Alinhamento = "center";

               if (bTotal)
                  TabelaResp += "<td align=\"" + Alinhamento + "\"><b>"+StrIndicador+"</b></td>\n";
               else
                  TabelaResp += "<td align=\"" + Alinhamento + "\">"+StrIndicador+"</td>\n";
            }
         }

/*         */

         TabelaResp += "</tr>\n";

         if (bTotal)
            bSetaCor = true;

         if (bSetaCor == true)
         {
            Cor = "#F0F0F0";   // Cinza
            bSetaCor = false;
         }
         else
         {
            Cor = "#FFFFFF";   // Branco
            bSetaCor = true;            
         }

         bTotal = false;
      }

      TabelaResp += "</table>\n";

      Coluna = new Vector();
      Coluna.add(TabelaResp);
      Coluna.trimToSize();
      
      Linhas = new Vector();
      Linhas.add(Coluna);
      Linhas.trimToSize();

      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @param p_Indicadores
    * @param p_Linhas
    * @return Vector
    * @exception 
    * @roseuid 3C7D808702E4
    */
   public Vector montaLinhasDetalhe(Vector p_Indicadores, Vector p_Linhas)
   {
      String Aux = null;
      String StrIndicador;
      Vector Linhas, Coluna, ListaIndicadores;

      ListaIndicadores = new Vector();

      // Cria vetor com todos os indicadores do relatório
      for (int j = 0; j < p_Indicadores.size(); j++)
         ListaIndicadores.addElement(m_SelecaoIndicadorDetalhe.fnGetIndicador((String)p_Indicadores.elementAt(j), 
                                                                              Short.parseShort(m_TipoRel),
                                                                              null));
      ListaIndicadores.trimToSize();

      Linhas = new Vector ();
      // **********
      // As linhas do relatório começam na posição 16!!
      // **********      
      for (int i = 16; i < p_Linhas.size(); i++)
      {
         Aux = (String)p_Linhas.elementAt(i);
         if (Aux.indexOf("2;") == 0 || Aux.indexOf("5;") == 0)
            continue;
//System.out.println("Linha No.: "+ (i-16));
         Coluna = new Vector();

      /* Ainda Falta CDRView NOVO */
         // Traduz a linha para uma estrutura de contadores
//         Cont = NoCampoArvoreRelatorioDetalheChamada.fnGetContador(Aux);
         // Processa os indicadores que devem ser apresentados
         for (int j = 0; j < ListaIndicadores.size(); j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor) ListaIndicadores.elementAt(j);
            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), null).toString();
//               StrIndicador = NoCampoInd.fnProcessa(Cont).toString();
               Coluna.add(StrIndicador);
            }
         }
/*    */
         Coluna.trimToSize();
         Linhas.add(Coluna);
      }

      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @param p_Indicadores
    * @param p_Linhas
    * @return Vector
    * @exception 
    * @roseuid 3C7D809900A5
    */
   public Vector montaLinhasPesquisa(Vector p_Indicadores, Vector p_Linhas, String[] p_Rec)
   {
      String Aux = null;
      String Rec[] = new String [3], StrIndicador, DataInicial, DataFinal;
      Vector Linhas, Coluna, ListaIndicadores;

      Rec = identificaRecursos(p_Indicadores, (String)p_Linhas.elementAt(12), (String)p_Linhas.elementAt(13));
      ListaIndicadores = new Vector();

      // N O V O: 01/08/2003 - Pesquisa de Codigo Avancada
      ListaIndicadores.addElement(m_SelecaoRecursosPesquisa.fnGetIndicador((String)p_Indicadores.elementAt(0), 
                                                                           Short.parseShort(m_TipoRel),
                                                                           null));

      // Cria vetor com todos os indicadores do relatório
      for (int j = 0; j < p_Indicadores.size(); j++)
         ListaIndicadores.addElement(m_SelecaoIndicadorPesquisa.fnGetIndicador((String)p_Indicadores.elementAt(j), 
                                                                               Short.parseShort(m_TipoRel),
                                                                               null));
      ListaIndicadores.trimToSize();

      DataInicial = (String)p_Linhas.elementAt(3);
      DataFinal = (String)p_Linhas.elementAt(4);

      Linhas = new Vector ();
      // As linhas do relatório começam na posição 16!!
      for (int i = 16; i < p_Linhas.size(); i++)
      {
         Aux = (String)p_Linhas.elementAt(i);
         if (Aux.indexOf("2;") == 0 || Aux.indexOf("5;") == 0)
            continue;

         Coluna = new Vector();

      /* Ainda Falta CDRView NOVO */
         // Traduz a linha para uma estrutura de contadores
//         Cont = NoCampoArvoreRelatorioPesquisaCodigo.fnGetContador(Rec, Aux);
//         Cont.fnDataInicial(DataInicial);
//         Cont.fnDataFinal(DataFinal);
         
         // Processa os indicadores que devem ser apresentados
         for (int j = 0; j < ListaIndicadores.size(); j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor) ListaIndicadores.elementAt(j);
            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), null).toString();
//               StrIndicador = NoCampoInd.fnProcessa(Cont).toString();
               Coluna.add(StrIndicador);
            }
         }
/*      */
         Coluna.trimToSize();
         Linhas.add(Coluna);
      }

      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @param p_Rec
    * @param p_Indicadores
    * @param p_IndicadoresCores
    * @param p_Linhas
    * @return void
    * @exception
    * @roseuid 3C7006A60112
    */
   public void montaTabela(String[] p_Rec, String p_Indicadores, String p_IndicadoresCores, Vector p_Linhas)
   {
      String TipoRel = null, Indicadores[] = null, Alinhamento[];
      short Filtros[];
      Vector Linhas = null, Header;

      // Recupera o tipo do relatório
      TipoRel = m_Request.getParameter("tiporel");
      Indicadores = m_Request.getParameterValues("indicadores");

      if (Indicadores == null)
      {
         Indicadores = new String[1];
         Indicadores[0] = p_Indicadores;
      }

      if (TipoRel.equals("0") || TipoRel.equals("1")) // Somente para relatório de desempenho
      {
         String Aux = p_Rec[0]+";";
         if (p_Rec[1] != null)
            Indicadores[0] = Aux + p_Rec[1] +";"+ Indicadores[0];
         else
            Indicadores[0] = Aux + Indicadores[0];
      }

      // Separa os indicadores em vetor de strings
      // Vai ser o Header da tabela
      Header = VetorUtil.String2Vetor(Indicadores[0],';');

      if (TipoRel.equals("0"))
         Linhas = montaLinhasDesempenho(Header, p_Linhas, p_Rec);
      else if (TipoRel.equals("2"))
         Linhas = montaLinhasDetalhe(Header, p_Linhas);
      else if (TipoRel.equals("1"))
         Linhas = montaLinhasPesquisa(Header, p_Linhas, p_Rec);

      // Centraliza as colunas que não são recursos
      if (TipoRel.equals("0") == false)
      {
         Alinhamento = new String[Header.size()];
         for (int i = 0; i < Header.size(); i++)
            Alinhamento[i] = "center";
      }
      else
      {
         Alinhamento = new String[1];
         Alinhamento[0] = "center";

         Header = new Vector();
         Header.add("Relat&oacute;rio");
         Header.trimToSize();
      }

      if (TipoRel.equals("0") == true)
         m_Html.setTabela((short)1, false);
      else
         m_Html.setTabela((short)Header.size(), false);

      m_Html.m_Tabela.setLargura((short)580);
      m_Html.m_Tabela.setBorder((short)0);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=visualizaRelAgendado");
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);
      //m_Html.m_Tabela.enviaTabela2String();
      m_Html.m_Tabela.enviaTabela();
   }

   /**
    * @param p_Mensagem
    * @return String[]
    * @exception
    * @roseuid 3C79381A0338
    */
   public String[] montaFormulario(String p_Mensagem)
   {
      String Args[];

      m_Perfil  = m_Request.getParameter("perfil");
      m_TipoRel = m_Request.getParameter("tiporel");
      m_IdRel   = m_Request.getParameter("idrel");
      m_Arquivo = m_Request.getParameter("arquivo");
      
// !!!! TESTE - INICIO
/*
      //AgendaDesempenho Ag = new AgendaDesempenho(m_ConexUtil, m_Perfil, m_TipoRel, m_IdRel, m_Arquivo, m_SelecaoRecursosDesempenho, m_SelecaoIndicadorDesempenho);
      //AgendaDetalheChamadas Ag = new AgendaDetalheChamadas(m_ConexUtil, m_Perfil, m_TipoRel, m_IdRel, m_Arquivo, m_SelecaoIndicadorDetalhe);
      //AgendaPesquisa Ag = new AgendaPesquisa(m_ConexUtil, m_Perfil, m_TipoRel, m_IdRel, m_Arquivo, m_SelecaoRecursosPesquisa, m_SelecaoIndicadorPesquisa);
      Vector RelProcessado, LinhaRelProcessado;

      for (int i = 0; i < Ag.m_Cabecalho.size(); i++)      
         System.out.println(Ag.m_Cabecalho.elementAt(i).toString());
      System.out.println(Ag.m_DataGeracao);
      for (int i = 0; i < Ag.m_QtdPeriodos; i++)
      {
         //RelProcessado = Ag.montaLinhas(i);
         RelProcessado = Ag.m_RelatoriosProcessados[i];
         System.out.println("Periodo: "+ Ag.m_Datas.elementAt(i));
         for (int k = 0; k < Ag.m_vIndicadores.size(); k++)
            System.out.print(Ag.m_vIndicadores.elementAt(k).toString()+"\t");
         System.out.println("\n-------------------------------------------------");            
         for (int j = 0; j < RelProcessado.size(); j++)
         {
            LinhaRelProcessado = (Vector)RelProcessado.elementAt(j);
            for (int k = 0; k < LinhaRelProcessado.size(); k++)
               System.out.print(LinhaRelProcessado.elementAt(k).toString()+"\t");
            System.out.println();
         }
         System.out.println("\n-------------------------------------------------");
      }
*/      
// !!!! TESTE - FIM

      No no = NoUtil.buscaNobyNomePerfil(m_Perfil);
      m_Linhas = no.getConexaoServUtil().getRelatorio((short)0, m_Perfil, Integer.parseInt(m_TipoRel), Integer.parseInt(m_IdRel), m_Arquivo);

      if (m_Linhas != null)
      {
         Args = new String[12];  // 12 do Header
         m_Indicadores = (String)m_Linhas.elementAt(14);
         for (int i = 0; i < 12; i++)
         {
            switch (i)
            {
               case 2:
               case 3:
               case 4:
                  if (m_TipoRel.equals("0") == false)
                  {
                     String Aux = (String)m_Linhas.elementAt(i);
                     Args[i] = Aux.substring(6,8) +"/"+ Aux.substring(4,6) +"/"+  Aux.substring(0,4)+" "+
                               Aux.substring(8,10) +":"+ Aux.substring(10,12) +":"+ Aux.substring(12,Aux.length());
                  }
                  else Args[i] = "-";
                  break;
               default:
                  Args[i] = (String)m_Linhas.elementAt(i);
                  if (Args[i].length() == 0)
                     Args[i] = "Sem Filtro";
                  break;
            }
         }

         if (m_TipoRel.equals("0") || m_TipoRel.equals("1"))
         {
            // Identifica quais os recursos do relatório
            m_Rec = identificaRecursos(VetorUtil.String2Vetor(m_Indicadores,';'), (String)m_Linhas.elementAt(12), (String)m_Linhas.elementAt(13));
            // Retira o primeiro elemento da lista de indicadores pois é recurso COM CERTEZA!!
            m_Indicadores = m_Indicadores.substring(m_Indicadores.indexOf(";")+1, m_Indicadores.length());
            // Retira o segundo elemento da lista de indicadores caso ele seja recurso
            if (m_Rec[1] != null)
               m_Indicadores = m_Indicadores.substring(m_Indicadores.indexOf(";")+1, m_Indicadores.length());
         }

         return Args;
      }
      else
         return null;
   }

   public String[] montaFormulario2()
   {
      String[] Args;
      Args = new String[6];

      Args[0] = "visualizaRelAgendado";
      Args[1] = m_Indicadores.substring(0, m_Indicadores.length() - 1);  // Lista de Indicadores
      Args[2] = m_Perfil;
      Args[3] = m_TipoRel;
      Args[4] = m_IdRel;
      Args[5] = m_Arquivo;
      return Args;
   }


   /**
    * @param p_Indicadores
    * @param p_Coluna0
    * @param p_Coluna1
    * @return String[]
    * @exception 
    * @roseuid 3C8229810067
    */
   public String[] identificaRecursos(Vector p_Indicadores, String p_Coluna0, String p_Coluna1) 
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
   
   /**
    * @param p_Data
    * @return String
    * @exception 
    * @roseuid 3D6550C203AF
    */
   public String geraData(String p_Data) 
   {
      String Aux = null, Dia, Mes, Ano, Hora, Min, Seg;

      Dia = p_Data.substring(6,8);
      Mes = p_Data.substring(4, 6);
      Ano = p_Data.substring(0, 4);
      Hora = p_Data.substring(8,10);
      Min = p_Data.substring(10,12);
      Seg = p_Data.substring(11,13);

      Aux = Dia + "/" + Mes + "/" + Ano + " " + Hora + ":" + Min + ":" + Seg;
      return Aux;
   }
}
