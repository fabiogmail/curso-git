//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVisualizaRelHistorico.java

package Portal.Operacoes;

import java.util.Vector;

import CDRView2.TelaCDRView;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.VetorUtil;

/**
 */
public class OpVisualizaRelHistorico extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C71BDF803AA
    */
   public OpVisualizaRelHistorico() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C84D54802DE
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      System.out.println("OpVisualizaRelHistorico - iniciaOperacao()");   
      try
      {
         //setOperacao("Visualização de Relatório Agendado");
         String Args[] = montaFormulario("$ARG;");

         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "visrelformgen.htm";
         m_Args[2] = "Relatório Histórico";
         m_Args[4] = "";

         if (Args != null)
         {
            m_Args[3] = "src=\"/PortalOsx/templates/js/indicadores.js\"";
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "visrelformgen.form", Args);
         }
         else
         {
            Args = new String [1];
            Args[0] = "Relat&oacute;rio foi alterado. Recarregue a p&aacute;gina de listagem de relat&oacute;rios!";
            m_Args[3] = "";         
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "detalherelarmazenadoerro.form", Args);
         }
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpVisualizaRelHistorico - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @param p_Indicadores
    * @param p_Linhas
    * @return Vector
    * @exception
    * @roseuid 3C84D54802F2
    */
   public Vector montaLinhasDesempenho(Vector p_Indicadores, Vector p_Linhas)
   {
      int InicioIndicador = 1;
      String Aux = null;
      String Rec[] = new String [3], StrIndicador;
      Vector Linhas, Coluna, ListaIndicadores;

      Rec = identificaRecursos(p_Indicadores, (String)p_Linhas.elementAt(12), (String)p_Linhas.elementAt(13));
      ListaIndicadores = new Vector();

      // Cria vetor com todos os recursos do relatório
      if (Rec[1] != null)
         InicioIndicador++;

      /* Ainda Falta CDRView NOVO
      ListaIndicadores.addElement(new Indicadores.NoCampoIndicador_DataHoraInicial(null));
      ListaIndicadores.addElement(new Indicadores.NoCampoIndicador_DataHoraFinal(null));
      */
      
      for (int j = 2; j < InicioIndicador+2; j++)
         ListaIndicadores.addElement(m_SelecaoRecursosDesempenho.fnGetIndicador((String)p_Indicadores.elementAt(j),TelaCDRView.REL_DESEMPENHO, null));

      // Cria vetor com todos os indicadores do relatório
      for (int j = InicioIndicador+2; j < p_Indicadores.size(); j++)
         ListaIndicadores.addElement(m_SelecaoIndicadorDesempenho.fnGetIndicador((String)p_Indicadores.elementAt(j),TelaCDRView.REL_DESEMPENHO, null));

      Linhas = new Vector ();
      // As linhas do relatório começam na posição 16!!
      for (int i = 16; i < p_Linhas.size(); i++)
      {
         Aux = (String)p_Linhas.elementAt(i);
         if (Aux.startsWith("2;") == true || Aux.startsWith("5;") == true || Aux.startsWith("6;") == true)
            continue;

         Coluna = new Vector();

      /* Ainda Falta CDRView NOVO */
         // Traduz a linha para uma estrutura de contadores
//         Cont = NoCampoArvoreRelatorioDesempenho.fnGetContador(Rec, Aux, true);
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
      /* */
      
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
    * @roseuid 3C84D5480338
    */
   public void montaTabela(String[] p_Rec, String p_Indicadores, String p_IndicadoresCores, Vector p_Linhas)
   {
      String Alinhamento[];
      String Largura[], TipoRel = null, Indicadores[] = null;
      short Filtros[];
      Vector Linhas = null, Header;

      // Recupera o tipo do relatório
      Indicadores = m_Request.getParameterValues("indicadores");

      if (Indicadores == null)
      {
         Indicadores = new String[1];
         Indicadores[0] = p_Indicadores;
      }

      String Aux = p_Rec[0]+";";
      if (p_Rec[1] != null)
         Indicadores[0] = Aux + p_Rec[1] +";"+ Indicadores[0];
      else
         Indicadores[0] = Aux + Indicadores[0];

      // Separa os indicadores em vetor de strings
      // Vai ser o Header da tabela
      Header = VetorUtil.String2Vetor(Indicadores[0],';');
      Header.add(0, "DataInicial");
      Header.add(1, "DataFinal");      

      // Centraliza as colunas que não são recursos
      Alinhamento = new String[Header.size()];
      for (int i = 0; i < Header.size(); i++)
         Alinhamento[i] = "center";

      Linhas = montaLinhasDesempenho(Header, p_Linhas);
      m_Html.setTabela((short)Header.size(), false);
      m_Html.m_Tabela.setLargura((short)580);
      m_Html.m_Tabela.setBorder((short)0);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      //m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      //m_Html.m_Tabela.setQtdItensPagina(DefsGerais.s_QTD_ITENS_TABELA);
      //m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=visualizaRelHistorico");
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C84D548036A
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[], Rec[] = null, Indicadores = null;
      Vector Linhas;

      String Perfil  = m_Request.getParameter("perfil");
      String IdRel   = m_Request.getParameter("idrel");
      String Arquivo = m_Request.getParameter("arquivo");

      No no = NoUtil.buscaNobyNomePerfil(Perfil);
      Linhas = no.getConexaoServUtil().getRelatorio((short)1, Perfil, (short)0, Integer.parseInt(IdRel), Arquivo);

      if (Linhas != null)
      {
         Args = new String[19];  // 12 do Header + 1 da tabela
         Indicadores = (String)Linhas.elementAt(14);
         for (int i = 0; i < 12; i++)
         {
            switch (i)
            {
               case 2:
               case 3:
               case 4:
                  String Aux = (String)Linhas.elementAt(i);
                  Args[i] = Aux.substring(6,8) +"/"+ Aux.substring(4,6) +"/"+  Aux.substring(0,4)+" "+
                            Aux.substring(8,10) +":"+ Aux.substring(10,12) +":"+ Aux.substring(12,Aux.length());
                  break;
               default:
                  Args[i] = (String)Linhas.elementAt(i);
                  if (Args[i].length() == 0)
                     Args[i] = "Sem Filtro";
                  break;
            }
         }

         // Identifica quais os recursos do relatório
         Rec = identificaRecursos(VetorUtil.String2Vetor(Indicadores,';'), (String)Linhas.elementAt(12), (String)Linhas.elementAt(13));
         // Retira o primeiro elemento da lista de indicadores pois é recurso COM CERTEZA!!
         Indicadores = Indicadores.substring(Indicadores.indexOf(";")+1, Indicadores.length());
         // Retira o segundo elemento da lista de indicadores caso ele seja recurso
         if (Rec[1] != null)
            Indicadores = Indicadores.substring(Indicadores.indexOf(";")+1, Indicadores.length());

         // Monta a tabela contendo o relatório
         montaTabela(Rec, Indicadores, (String)Linhas.elementAt(15), Linhas);
         Args[12] = m_Html.m_Tabela.getTabelaString();  // Relatório
         Args[13] = "visualizaRelHistorico";
         Args[14] = Indicadores.substring(0, Indicadores.length() - 1);  // Lista de Indicadores
         Args[15] = Perfil;
         Args[16] = "0"; // Tipo do relatório: Sempre é desempenho
         Args[17] = IdRel;
         Args[18] = Arquivo;
         return Args;
      }
      else
         return null;
   }
   
   /**
    * @param p_Indicadores
    * @param p_Coluna0
    * @param p_Coluna1
    * @return String[]
    * @exception 
    * @roseuid 3C84D548037E
    */
   public String[] identificaRecursos(Vector p_Indicadores, String p_Coluna0, String p_Coluna1) 
   {
      String Rec[] = new String[3];
      int Pos = 0;

      if (((String)p_Indicadores.elementAt(0)).equals("DataInicial"))
         Pos = 2;

      for (int i = 0; i < Rec.length; i++)
         Rec[i] = null;

      if (p_Coluna0.equals("") == false)
         Rec[0] = (String)p_Indicadores.elementAt(Pos);
      if (p_Coluna1.equals("") == false)
         Rec[1] = (String)p_Indicadores.elementAt(Pos+1);

      return Rec;
   }
}
