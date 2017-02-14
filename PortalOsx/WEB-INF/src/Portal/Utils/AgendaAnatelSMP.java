package Portal.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

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
import CDRView2.SelecaoIndicadoresPortal;
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
import CDRView2.TelaCDRView;
import Portal.Conexao.CnxServUtil;


public class AgendaAnatelSMP extends Agenda
{ 
   public String[] m_Rec = null;
   private String m_Indicadores;
   public String m_TituloRecurso[] = null;
   public String m_ExcluiLinhas = "true";
   public String m_TipoIndicador = "";
   
   public Map m_IndicadoresAnatelSMP;
  
   public Vector m_VecRecursos[];
   public Map m_MapRecursos[];
   public Map m_MapRecursosNovos[];
   public Map m_MapRelatorios[] = null;
   public short m_TipoSMP;
   private String m_TODOS = "Todos"; 
   public String m_TipoApresentacao = "2";

   // Tipos de Relatorios Anatel   
   public static final int ANATEL_SMP3 = 0;
   public static final int ANATEL_SMP5 = 1;
   public static final int ANATEL_SMP6 = 2;
   public static final int ANATEL_SMP7 = 3;
   public static final int ANATEL_LDN  = 4;
   
   public static final int ANATEL_SMP3_ERICSSON = 5;
   public static final int ANATEL_SMP5_ERICSSON = 6;
   public static final int ANATEL_SMP6_ERICSSON = 7;
   public static final int ANATEL_SMP7_ERICSSON = 8;
   public static final int ANATEL_SMP3e4 = 9;
   public static final int ANATEL_SMP8e9 = 10;
   
   public static final int ANATEL_IDDF = 11;
   public static final int ANATEL_STFC = 12;

   private static final String ANATEL_LDN_ID = "18";
   private static final String NOME_REC_DESTINO = "Destino";
   
//   private String[] m_ContadoresCfg = null;
   
   public AgendaAnatelSMP(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo,  String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);

      boolean bAchou = false;
      int iQtdRecursos = 0;

      if (p_TipoRel.equals(ANATEL_LDN_ID))  // LDN
      {
         m_VecRecursos = new Vector[3];
         m_VecRecursos[0] = null;
         m_VecRecursos[1] = null;
         m_VecRecursos[2] = null;   // CSP
      }
      else
      {
         m_VecRecursos = new Vector[2];
         m_VecRecursos[0] = null;
         m_VecRecursos[1] = null;
      }

      // Recupera os tipos de recursos selecionados
      // Obs.: Deverá SEMPRE haver 2 recursos selecionados (Área x Bilhetador) !!
      if (p_TipoRel.equals(ANATEL_LDN_ID))  // LDN
      {
         m_TituloRecurso = new String[3];
         m_TituloRecurso[2] = "CSP";
      }
      else  // Outros tipos
      {
         m_TituloRecurso = new String[2];
      }

      String strVisualizacao[] = {"Visualizacao por: Area", "Visualizacao por: Rota"};
      //caso em que a String vem acentuada do servidor
      String strVisualizacaoComAcento[] = {"Visualização por: Area","Visualização por: Area"};
      for (int i = 0; i < m_Cabecalho.size(); i++)
      {
    	  /**caso em que a string vem com acento, próprio pra linux*/
         if (m_Cabecalho.elementAt(i).toString().compareToIgnoreCase(strVisualizacao[0]) == 0)
         {
             m_TituloRecurso[0] = "Areas";
             bAchou = true;
             break;
         }             
         else if (m_Cabecalho.elementAt(i).toString().compareToIgnoreCase(strVisualizacao[1]) == 0)
         {
             m_TituloRecurso[0] = "Rotas";
             bAchou = true;
             break;
         }
         /**caso em que a string vem com acento, próprio pra windows*/
         if (m_Cabecalho.elementAt(i).toString().compareToIgnoreCase(strVisualizacaoComAcento[0]) == 0)
         {
             m_TituloRecurso[0] = "Areas";
             bAchou = true;
             break;
         }             
         else if (m_Cabecalho.elementAt(i).toString().compareToIgnoreCase(strVisualizacaoComAcento[1]) == 0)
         {
             m_TituloRecurso[0] = "Rotas";
             bAchou = true;
             break;
         }
      }

      if (bAchou)
      {
         for (int i = 0; i < m_Cabecalho.size(); i++)
         {      
            if (m_Cabecalho.elementAt(i).toString().compareToIgnoreCase("Referencia: Origem") == 0)
            {
               m_TituloRecurso[0] += " Origem";
               break;
            }
            if (m_Cabecalho.elementAt(i).toString().compareToIgnoreCase("Referencia: Destino") == 0)
            {
               m_TituloRecurso[0] += " Destino";
               break;
            }            
         }
      }
      if (!bAchou)
         m_TituloRecurso[0] = m_vIndicadores.elementAt(0).toString();

      if (p_TipoRel.equals(ANATEL_LDN_ID))  // LDN
         m_TituloRecurso[1] = "Bilhetador";
      else
         m_TituloRecurso[1] = m_vIndicadores.elementAt(1).toString();      

      //
      // Monta lista de recursos disponiveis
      //
      if (p_TipoRel.equals(ANATEL_LDN_ID))  // LDN
      {
    	  //String area = (TipoRecurso1.equalsIgnoreCase("ROTAE") ? "Áreas Origem" : TipoRecurso1);
    	 m_TituloRecurso[1] =(m_vIndicadores.elementAt(0).toString());
    	 m_TituloRecurso[0] = m_vIndicadores.elementAt(1).toString().equalsIgnoreCase("ROTAE") ? "Áreas Origem" : m_vIndicadores.elementAt(0).toString();
         m_MapRecursos = new Map[3];
         m_MapRecursosNovos = new Map[3];
         iQtdRecursos = 3;
      }
      else
      {
         m_MapRecursos = new Map[2];
         m_MapRecursosNovos = new Map[2];
         iQtdRecursos = 2;
      }

      for (int i = 0; i < iQtdRecursos; i++)
      {
         m_MapRecursos[i] = Collections.synchronizedMap(new TreeMap());
         m_MapRecursosNovos[i] = Collections.synchronizedMap(new TreeMap());

        // Inclui o recurso Todas as áreas no map
         m_MapRecursos[i].put(m_TODOS, m_TODOS);
      }

      // Identifica quais os recursos do relatório
      m_Rec = identificaRecursos(VetorUtil.String2Vetor(m_IndicadoresIniciais,';'), m_TipoColunas[0], m_TipoColunas[1]);
      
      // Retira o primeiro elemento da lista de indicadores pois é recurso COM CERTEZA!!
      m_Indicadores = m_IndicadoresIniciais.substring(m_IndicadoresIniciais.indexOf(";")+1, m_IndicadoresIniciais.length());

      // Retira o segundo elemento da lista de indicadores caso ele seja recurso
      if (m_Rec[1] != null)
         m_Indicadores = m_Indicadores.substring(m_Indicadores.indexOf(";")+1, m_Indicadores.length());

      // Se for Anatel LDN, retira o recurso Destino: BIL ; AREA ; DESTINO
      if (p_TipoRel.equals(ANATEL_LDN_ID))  // LDN
         m_Indicadores = m_Indicadores.substring(m_Indicadores.indexOf(";")+1, m_Indicadores.length());

      if (p_TipoRel.equals(ANATEL_LDN_ID))
         m_Rec[2] = NOME_REC_DESTINO;
      
      this.setM_Rec(m_Rec);
      this.setM_ContadoresRelatorio(getM_iRetRelatorio().fnGetContadores());

//03/12/2013 FEITO PARA PEGAR OS CONTADORCFG, POREM NÃO FOI PRECISO
//      try{
//    	  m_ContadoresCfg =  fnFromCSV(SelecaoIndicadoresPortal.m_iIndicadores.fnContadoresCfg(Integer.parseInt(p_TipoRel)));
//      }catch (Exception e){
//    	  System.out.println("m_ContadoresCfg " + m_ContadoresCfg);
//      }
//      if(m_ContadoresCfg != null)
//      {
//    	  for(int i=0; i<m_ContadoresCfg.length; i++){
//    		  this.getM_ContadoresRelatorio().fnAdiciona(m_ContadoresCfg[i]);
//    	  }
//      }
//      System.out.println();
   }
   
//   static private String [] fnFromCSV(String p_CSV)
//   {
//      if (p_CSV == null)
//         return null;
//      StringTokenizer l_Token = new StringTokenizer(p_CSV, ";", false);
//      int l_QtdTokens = l_Token.countTokens();
//      if (l_QtdTokens <= 0)
//         return null;
//      String l_Ret [] = new String[l_QtdTokens];
//      for (int a=0; a<l_QtdTokens; a++)
//         l_Ret[a] = l_Token.nextToken();
//      return l_Ret;
//   }

   /**
    * Anatel SMP3
    */
   public void setAnatelSMP3(SelecaoRecursosAnatelSMP3 p_SelecaoRecursosAnatelSMP3, SelecaoIndicadorAnatelSMP3 p_SelecaoIndicadorAnatelSMP3, String p_IndicadoresAnatelSMP3)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_SMP3;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP3);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP3);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP3, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP3);
   }

   
   /**
    * Anatel STFC
    */
   public void setAnatelSTFC(SelecaoRecursosAnatelSTFC p_SelecaoRecursosAnatelSTFC, SelecaoIndicadorAnatelSTFC p_SelecaoIndicadorAnatelSTFC, String p_IndicadoresAnatelSTFC)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_STFC;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSTFC);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSTFC);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSTFC, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_STFC);
   }
   
   /**
    * Anatel IDDF
    */
   public void setAnatelIDDF(SelecaoRecursosAnatelIDDF p_SelecaoRecursosAnatelIDDF, SelecaoIndicadorAnatelIDDF p_SelecaoIndicadorAnatelIDDF, String p_IndicadoresAnatelIDDF)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_STFC;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelIDDF);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelIDDF);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelIDDF, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_IDDF);
   }
   
   /**
    * Anatel SMP3e4
    */
   public void setAnatelSMP3e4(SelecaoRecursosAnatelSMP3e4 p_SelecaoRecursosAnatelSMP3e4, SelecaoIndicadorAnatelSMP3e4 p_SelecaoIndicadorAnatelSMP3e4, String p_IndicadoresAnatelSMP3e4)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_SMP3e4;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP3e4);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP3e4);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP3e4, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP34);
   }

   /**
    * Anatel SMP5
    */
   public void setAnatelSMP5(SelecaoRecursosAnatelSMP5 p_SelecaoRecursosAnatelSMP5, SelecaoIndicadorAnatelSMP5 p_SelecaoIndicadorAnatelSMP5, String p_IndicadoresAnatelSMP5)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_SMP5;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP5);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP5);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP5, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP5);
   }

   /**
    * Anatel SMP6
    */
   public void setAnatelSMP6(SelecaoRecursosAnatelSMP6 p_SelecaoRecursosAnatelSMP6, SelecaoIndicadorAnatelSMP6 p_SelecaoIndicadorAnatelSMP6, String p_IndicadoresAnatelSMP6)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      m_TipoSMP = ANATEL_SMP6;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP6);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP6);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP6, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP6);
   }

   /**
    * Anatel SMP7
    */
   public void setAnatelSMP7(SelecaoRecursosAnatelSMP7 p_SelecaoRecursosAnatelSMP7, SelecaoIndicadorAnatelSMP7 p_SelecaoIndicadorAnatelSMP7, String p_IndicadoresAnatelSMP7)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;   
      m_TipoSMP = ANATEL_SMP7;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP7);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP7);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP7, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         //System.out.println("Indicador: "+Indicador);
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP7);
   }
   

   /**
    * Anatel SMP8e9
    */
   public void setAnatelSMP8e9(SelecaoRecursosAnatelSMP8e9 p_SelecaoRecursosAnatelSMP8e9, SelecaoIndicadorAnatelSMP8e9 p_SelecaoIndicadorAnatelSMP8e9, String p_IndicadoresAnatelSMP8e9)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_SMP8e9;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP8e9);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP8e9);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP8e9, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP8e9);
   }
   /**
    * Anatel SMP3 Ericsson
    */
   public void setAnatelSMP3Ericsson(SelecaoRecursosAnatelSMP3Ericsson p_SelecaoRecursosAnatelSMP3Ericsson, SelecaoIndicadorAnatelSMP3Ericsson p_SelecaoIndicadorAnatelSMP3Ericsson, String p_IndicadoresAnatelSMP3Ericsson)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_SMP3_ERICSSON;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP3Ericsson);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP3Ericsson);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP3Ericsson, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP3_ERICSSON);
   }
   
   /**
    * Anatel SMP5 Ericsson
    */
   public void setAnatelSMP5Ericsson(SelecaoRecursosAnatelSMP5Ericsson p_SelecaoRecursosAnatelSMP5Ericsson, SelecaoIndicadorAnatelSMP5Ericsson p_SelecaoIndicadorAnatelSMP5Ericsson, String p_IndicadoresAnatelSMP5Ericsson)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      
      m_TipoSMP = ANATEL_SMP5_ERICSSON;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP5Ericsson);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP5Ericsson);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP5Ericsson, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP5_ERICSSON);
   }

   /**
    * Anatel SMP6 Ericsson
    */
   public void setAnatelSMP6Ericsson(SelecaoRecursosAnatelSMP6Ericsson p_SelecaoRecursosAnatelSMP6Ericsson, SelecaoIndicadorAnatelSMP6Ericsson p_SelecaoIndicadorAnatelSMP6Ericsson, String p_IndicadoresAnatelSMP6Ericsson)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;
      m_TipoSMP = ANATEL_SMP6_ERICSSON;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP6Ericsson);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP6Ericsson);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP6Ericsson, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP6_ERICSSON);
   }
   
   /**
    * Anatel SMP7 Ericsson
    */
   public void setAnatelSMP7Ericsson(SelecaoRecursosAnatelSMP7Ericsson p_SelecaoRecursosAnatelSMP7Ericsson, SelecaoIndicadorAnatelSMP7Ericsson p_SelecaoIndicadorAnatelSMP7Ericsson, String p_IndicadoresAnatelSMP7Ericsson)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;   
      m_TipoSMP = ANATEL_SMP7_ERICSSON;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelSMP7Ericsson);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelSMP7Ericsson);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelSMP7Ericsson, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         //System.out.println("Indicador: "+Indicador);
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));        
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELSMP7_ERICSSON);
   }
   
   /**
    * Anatel LDN
    */
   public void setAnatelLDN(SelecaoRecursosAnatelLDN p_SelecaoRecursosAnatelLDN, SelecaoIndicadorAnatelLDN p_SelecaoIndicadorAnatelLDN, String p_IndicadoresAnatelLDN)
   {
      int i = 0;
      String Indicador;
      Map.Entry Ent;
      Iterator It;   
      m_TipoSMP = ANATEL_LDN;

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnatelLDN);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnatelLDN);
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnatelLDN, ";");
      m_IndicadoresAnatelSMP = Collections.synchronizedMap(new TreeMap());            
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         //System.out.println("setAnatelLDN: Indicador: "+Indicador);
         m_IndicadoresAnatelSMP.put(Indicador, new Integer(i));
         i++;
      }

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnatelSMP.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));
         i++;
      }    
   
      montaRelatorios(TelaCDRView.REL_ANATELLDN);
   }   
   

   public void montaLinhas(int p_Periodo, short p_TipoRel)
   {
      int InicioIndicador = 1, TamListaIndicadores, TamLinhas;
      String Aux = null, Aux1 = null, Aux2 = null, NomePeriodo = "";
      String StrIndicador, strChave1, strChave2, strChave3 = "";
      Vector ListaIndicadores, LinhaRelProcessado, LinhasRel;
      Iterator It, itHelper;
      Map.Entry Ent;
      String l_Recursos[];

      ListaIndicadores = new Vector();
      
      switch (m_TipoSMP)
      {
         case ANATEL_SMP3:
            It = m_IndicadoresAnatelSMP.entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;

         case ANATEL_SMP3e4:
            It = m_IndicadoresAnatelSMP.entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;
         case ANATEL_SMP5:
            It = m_IndicadoresAnatelSMP.entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;
         case ANATEL_SMP6:
            It = m_IndicadoresAnatelSMP.entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;
         case ANATEL_SMP7:             
            It = m_IndicadoresAnatelSMP.entrySet().iterator();           
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;

         case ANATEL_SMP8e9:
            It = m_IndicadoresAnatelSMP.entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;
         case ANATEL_SMP3_ERICSSON:
             It = m_IndicadoresAnatelSMP.entrySet().iterator();
             while (It.hasNext())
             {
                Ent = (Map.Entry) It.next();
                ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                        m_Rec,
                                                                                        p_TipoRel,
                                                                                        this.getM_ContadoresRelatorio()));
             }            
             break;
          case ANATEL_SMP5_ERICSSON:
             It = m_IndicadoresAnatelSMP.entrySet().iterator();
             while (It.hasNext())
             {
                Ent = (Map.Entry) It.next();
                ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                        m_Rec,
                                                                                        p_TipoRel,
                                                                                        this.getM_ContadoresRelatorio()));
             }            
             break;
          case ANATEL_SMP6_ERICSSON:
             It = m_IndicadoresAnatelSMP.entrySet().iterator();
             while (It.hasNext())
             {
                Ent = (Map.Entry) It.next();
                ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                        m_Rec,
                                                                                        p_TipoRel,
                                                                                        this.getM_ContadoresRelatorio()));
             }            
             break;
          case ANATEL_SMP7_ERICSSON:             
             It = m_IndicadoresAnatelSMP.entrySet().iterator();           
             while (It.hasNext())
             {
                Ent = (Map.Entry) It.next();
                ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                        m_Rec,
                                                                                        p_TipoRel,
                                                                                        this.getM_ContadoresRelatorio()));
             }            
             break; 
          
         case ANATEL_LDN:
            It = m_IndicadoresAnatelSMP.entrySet().iterator();
            while (It.hasNext())
            {
               Ent = (Map.Entry) It.next();
               ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                       m_Rec,
                                                                                       p_TipoRel,
                                                                                       this.getM_ContadoresRelatorio()));
            }            
            break;
      }

      ListaIndicadores.trimToSize();
      TamListaIndicadores = ListaIndicadores.size();

      LinhasRel = (Vector)m_Linhas.elementAt(p_Periodo);
      TamLinhas = LinhasRel.size();
      for (int i = 0; i < TamLinhas; i++)
      {
         Aux = (String)LinhasRel.elementAt(i);         
         
         if (m_TipoSMP == ANATEL_LDN)
         {
            strChave1 = new String(Aux.substring(Aux.indexOf(';')+1, Aux.length()));           
            // Primeiro recurso: Bilhetador (strChave2)
            strChave2 = new String(Aux.substring(Aux.indexOf(';')+1, Aux.length()));
            strChave2 = strChave1.substring(0, strChave1.indexOf(';'));
            // Segundo recurso: Area ou Rota (strChave1)
            strChave1 = strChave1.substring(strChave1.indexOf(';')+1, strChave1.length());
            strChave3 = new String(strChave1);
            strChave1 = strChave1.substring(0, strChave1.indexOf(';'));

            // Terceiro recurso: Destino (strChave3)
            strChave3 = strChave3.substring(strChave3.indexOf(';')+1, strChave3.length());
            strChave3 = strChave3.substring(0, strChave3.indexOf(';'));
            // Retira o ASTERISCO (*) do nome do destino!
            if (strChave3.indexOf("*") != -1)
               strChave3 = strChave3.substring(0, strChave3.indexOf("*"));
         }
         else
         {
            // Primeiro recurso: Área ou Rota (strChave1)
         	String tmp = Aux.substring(Aux.indexOf(';')+1, Aux.length());
            strChave1 = tmp.substring(0, tmp.indexOf(';'));
            // Segundo recurso: Bilhetador (strChave2)
            tmp = tmp.substring(tmp.indexOf(';')+1, tmp.length());
            strChave2 = tmp.substring(0, tmp.indexOf(';'));
         }

         m_MapRecursos[0].put(strChave1, strChave1);  // Área ou Rota
         m_MapRecursos[1].put(strChave2, strChave2);  // Bilhetador
         
         if (m_TipoSMP == ANATEL_LDN)
            m_MapRecursos[2].put(strChave3, strChave3);  // Destino
 

         if (Aux.startsWith("0;"))
         {
	         // Retirando o codigo de linha "0;"
	         Aux = Aux.substring(Aux.indexOf(';')+1, Aux.length());
         }
         
         LinhaRelProcessado = new Vector();
         
         // Processa os indicadores que devem ser apresentados
         for (int j = 0; j < TamListaIndicadores; j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor)ListaIndicadores.elementAt(j);

            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), null).toString();               
               LinhaRelProcessado.add(StrIndicador);
            }
            
         }
         LinhaRelProcessado.trimToSize();        
         if (m_TipoSMP == ANATEL_LDN)
            m_MapRelatorios[p_Periodo].put(strChave1 + "-" + strChave2 + "-" + strChave3, LinhaRelProcessado);
         else
            m_MapRelatorios[p_Periodo].put(strChave1 + "-" + strChave2, LinhaRelProcessado);

      }
   }

   public void montaRelatorios(short p_TipoRel)
   {
      if (m_MapRelatorios == null)
      {
         m_MapRelatorios = new Map[m_QtdPeriodos];
         for (int i = 0; i < m_QtdPeriodos; i++)
         {
            m_MapRelatorios[i] = Collections.synchronizedMap(new TreeMap());      
            montaLinhas(i,p_TipoRel);
         }
      }
   }

   public void remontaRelatorios(short p_TipoRel)
   {
      for (int i = 0; i < m_QtdPeriodos; i++)
         m_MapRelatorios[i].clear();
      m_MapRelatorios = null;

      montaRelatorios(p_TipoRel);
   }

   public void setMapRecursoNovo(int p_iTipoRecurso, String p_Recursos)
   {
      int iTam;
      Vector Recursos;
      Set ListaRecursos;
      Iterator It;
      Map.Entry Ent;
      Object Obj;
      String Recurso;
      
      p_Recursos = p_Recursos.replace('@', '&');
      Recursos = VetorUtil.String2Vetor(p_Recursos, ';');
      iTam = Recursos.size();

      m_MapRecursosNovos[p_iTipoRecurso].clear();
      m_MapRecursosNovos[0].put(m_TODOS, m_TODOS);
      if (iTam == 1 && Recursos.elementAt(0).toString().equals(m_TODOS))
      {
         ListaRecursos = m_MapRecursos[p_iTipoRecurso].entrySet();
         It = ListaRecursos.iterator();
         while (It.hasNext())
         {
            Ent = (Map.Entry)It.next();
            Obj = Ent.getValue();
            Recurso = Obj.toString();            
            m_MapRecursosNovos[p_iTipoRecurso].put(Recurso, Recurso);
         }
      }
      else
      {
         for (int i = 0; i < iTam; i++)
         {
            m_MapRecursosNovos[p_iTipoRecurso].put(Recursos.elementAt(i).toString(), Recursos.elementAt(i).toString());
         }
      }
   }

   public String getBilhetadorSelecionado()
   {
      Set ListaRecursos;
      Iterator It;
      Map.Entry Ent;
      Object Obj;

      if (m_MapRecursosNovos[1].size() == 0)
         It = m_MapRecursos[1].entrySet().iterator();
      else
         It = m_MapRecursosNovos[1].entrySet().iterator();

      Ent = (Map.Entry)It.next();
      Obj = Ent.getValue();
      return Obj.toString();      
   }
}