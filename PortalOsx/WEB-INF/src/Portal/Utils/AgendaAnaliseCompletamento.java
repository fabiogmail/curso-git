package Portal.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import CDRView2.SelecaoIndicadorAnaliseCompletamento;
import CDRView2.SelecaoRecursosAnaliseCompletamento;
import Portal.Conexao.CnxServUtil;


public class AgendaAnaliseCompletamento extends Agenda
{
   public String[] m_Rec = null;
   private String m_Indicadores;
   public Map m_IndicadoresAnaliseCompletamento;
   public String m_TituloRecurso = null;
   public String m_Recurso = null;   
   public Map m_MapRecursos;  
   public Map m_MapRecursosNovos;
   public Vector m_VecRecursos;   
   public Vector m_IndicadoresVisiveis;
   public Map m_MapRelatorios[] = null;   
   private String m_TODOS = "Todos";
   public int m_Nivel = 0;

   
   public AgendaAnaliseCompletamento(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, SelecaoRecursosAnaliseCompletamento p_SelecaoRecursosAnaliseCompletamento, SelecaoIndicadorAnaliseCompletamento p_SelecaoIndicadorAnaliseCompletamento, String p_IndicadoresAnaliseCompletamento, String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);

      int i = 0 ;
      String Indicador = "";
      Map.Entry Ent;
      Iterator It;

      m_MapRecursos = Collections.synchronizedMap(new TreeMap());
      m_MapRecursosNovos = Collections.synchronizedMap(new TreeMap());
      m_MapRecursos.put("Todos", "Todos");      

      this.setM_SelecaoRecursos(p_SelecaoRecursosAnaliseCompletamento);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorAnaliseCompletamento);
      this.setM_ContadoresRelatorio(this.getM_iRetRelatorio().fnGetContadores());

      // Populando os indicadores   
      m_IndicadoresVisiveis = new Vector();
      StringTokenizer st = new StringTokenizer(p_IndicadoresAnaliseCompletamento, ";");
      m_IndicadoresAnaliseCompletamento = Collections.synchronizedMap(new TreeMap());            
    
      while (st.hasMoreTokens())
      {
         Indicador = (String)st.nextElement();
         m_IndicadoresAnaliseCompletamento.put(Indicador, new Integer(i));
         System.out.println(Indicador);         
         if (i<5) 
            m_IndicadoresVisiveis.addElement(Indicador);
         i++;
      }     

      // Atribuindo as posicoes corretas dos indicadores
      It = m_IndicadoresAnaliseCompletamento.entrySet().iterator();
      i = 0;
      while (It.hasNext())
      {
         Ent = (Map.Entry)It.next();
         Ent.setValue(new Integer(i));
         i++;
      }
      
      // Identifica quais os recursos do relatório
      m_Rec = identificaRecursos(VetorUtil.String2Vetor(m_IndicadoresIniciais,';'), m_TipoColunas[0], m_TipoColunas[1]);
      this.setM_Rec(m_Rec);
      // Retira o primeiro elemento da lista de indicadores pois é recurso COM CERTEZA!!
      m_Indicadores = m_IndicadoresIniciais.substring(m_IndicadoresIniciais.indexOf(";")+1, m_IndicadoresIniciais.length());

      // Retira o segundo elemento da lista de indicadores caso ele seja recurso
      if (m_Rec[1] != null)
         m_Indicadores = m_Indicadores.substring(m_Indicadores.indexOf(";")+1, m_Indicadores.length());

      montaRelatorios(Short.parseShort(p_TipoRel));
   }

   public void montaLinhas(int p_Periodo, short p_TipoRel)
   {
      int InicioIndicador = 1, TamListaIndicadores, TamLinhas;
      String Aux = null, AuxTotal = null, Aux1 = null, Aux2 = null, NomePeriodo = "";
      String StrIndicador, strChave1, Linha, Recurso;
      Vector ListaIndicadores, RelProcessado, LinhaRelProcessado, LinhasRel;
      Iterator It;
      Map.Entry Ent;


      ListaIndicadores = new Vector();
      // Limpa o map do relatorio antes da leitura
      m_MapRelatorios[p_Periodo].clear(); 
      
      for (int j = 0; j < InicioIndicador; j++)
         ListaIndicadores.addElement(this.getM_SelecaoRecursos().fnGetIndicador(m_vIndicadores.elementAt(j).toString(), 
                                                                                p_TipoRel,
                                                                                this.getM_ContadoresRelatorio()));

      // Cria vetor com todos os indicadores do relatório

      It = m_IndicadoresAnaliseCompletamento.entrySet().iterator();
      while (It.hasNext())
      {
         Ent = (Map.Entry) It.next();        
         ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)Ent.getKey(),
                                                                                 p_TipoRel,
                                                                                 this.getM_ContadoresRelatorio()));
      }

      ListaIndicadores.trimToSize();
      TamListaIndicadores = ListaIndicadores.size();      

      RelProcessado = new Vector();
      LinhasRel = (Vector)m_Linhas.elementAt(p_Periodo);
      TamLinhas = LinhasRel.size();
      for (int i = 0; i < TamLinhas; i++)
      {
         Aux = (String)LinhasRel.elementAt(i);
         strChave1 = new String(Aux.substring(Aux.indexOf(';')+1, Aux.length()));
         strChave1 = strChave1.substring(0, strChave1.indexOf(';'));         

         // Monta os maps dos recursos a partir de todas as linhas do 1o. período do relatório
         m_Recurso = strChave1;
         if (i == 0)
         {
            m_TituloRecurso = m_Recurso;
            strChave1 = m_TODOS;
         }
         else
            m_MapRecursos.put(m_Recurso, m_Recurso);

         if (i == 0)
            AuxTotal = Aux;

         LinhaRelProcessado = new Vector();
         // Processa os indicadores que devem ser apresentados
         for (int j = 0; j < TamListaIndicadores; j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor)ListaIndicadores.elementAt(j);

            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), libjava.tipos.TipoDadoString.fnTokenLinha(AuxTotal)).toString();
               LinhaRelProcessado.add(StrIndicador);
            }
            LinhaRelProcessado.trimToSize();
         }
         m_MapRelatorios[p_Periodo].put(strChave1, LinhaRelProcessado);
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

   public void setMapRecursoNovo(String p_Recursos)
   {
      int iTam;
      Vector Recursos;
      Iterator It;
      Map.Entry Ent;
      Object Obj;
      String Recurso;
      
      p_Recursos = p_Recursos.replace('@', '&');
      Recursos = VetorUtil.String2Vetor(p_Recursos, ';');
      iTam = Recursos.size();

      m_MapRecursosNovos.clear();
      if (iTam == 1 && Recursos.elementAt(0).toString().equals(m_TODOS))
      {
         It = m_MapRecursos.entrySet().iterator();
         while (It.hasNext())
         {
            Ent = (Map.Entry)It.next();
            Obj = Ent.getValue();
            Recurso = Obj.toString();            
            m_MapRecursosNovos.put(Recurso, Recurso);
         }
      }
      else
      {
         for (int i = 0; i < iTam; i++)
            m_MapRecursosNovos.put(Recursos.elementAt(i).toString(), Recursos.elementAt(i).toString());
      }
   }   
}