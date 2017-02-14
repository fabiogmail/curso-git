package Portal.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import CDRView2.SelecaoIndicadorMatraf;
import CDRView2.SelecaoRecursosMatraf;
import Portal.Conexao.CnxServUtil;



public class AgendaMatraf extends Agenda
{ 
   public String[] m_Rec = null;
   private String m_Indicadores;
   public Vector m_IndicadoresMatraf;
   public Vector m_VecRecursos[];
   public String[] m_Recurso = null;
   public String[] m_TituloRecurso = null;
   public Map m_MapRecursos[];
   public Map m_MapRecursosNovos[];   
   public Map m_MapRelatorios[] = null;
   public Map m_Periodos = null;   
   private String m_TODOS = "Todos";


   public AgendaMatraf(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, SelecaoRecursosMatraf p_SelecaoRecursosMatraf, SelecaoIndicadorMatraf p_SelecaoIndicadorMatraf, String p_IndicadoresMatraf, String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);
      int iTam, iPeriodo;
      String Linha, Recurso;
      Vector LinhasRel = null;
      
      m_Recurso = new String[2];
      m_TituloRecurso = new String[2];
      m_VecRecursos = new Vector[2];
      m_VecRecursos[0] = m_VecRecursos[1] = null;

      m_MapRecursosNovos = new Map[2];

      //
      // Monta lista de recursos disponíveis
      //
      m_MapRecursos = new Map[2];
      for (int i = 0; i < 2; i++)      
      {
         m_MapRecursos[i] = Collections.synchronizedMap(new TreeMap());
         m_MapRecursosNovos[i] = Collections.synchronizedMap(new TreeMap());
         m_MapRecursos[i].put("Todos", "Todos");
      }

      this.setM_SelecaoRecursos(p_SelecaoRecursosMatraf);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorMatraf);
      this.setM_ContadoresRelatorio(getM_iRetRelatorio().fnGetContadores());

      m_IndicadoresMatraf = VetorUtil.String2Vetor(p_IndicadoresMatraf, ';');
      
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
      int InicioIndicador = 1, TamListaIndicadores, TamLinhas, i, j;
      String Aux = null, Linha, Recurso;
      String StrIndicador, strChave1 = "", strChave2 = "";
      Vector ListaIndicadores, LinhaRelProcessado, LinhasRel;

      ListaIndicadores = new Vector();
      // Limpa o map do relatorio antes da leitura
      m_MapRelatorios[p_Periodo].clear();      

      // Cria vetor com todos os recursos do relatório
      if (m_Rec[1] != null)
         InicioIndicador++;

      for (j = 0; j < InicioIndicador; j++)
      {
         ListaIndicadores.addElement(this.getM_SelecaoRecursos().fnGetIndicador(m_vIndicadores.elementAt(j).toString(),
                                                                                m_Rec,
                                                                                p_TipoRel,
                                                                                this.getM_ContadoresRelatorio()));
      }

      // Cria vetor com todos os indicadores do relatório
      for (j = InicioIndicador; j < m_vIndicadores.size(); j++)
      {
         ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador(m_vIndicadores.elementAt(j).toString(),
                                                                                 m_Rec,
                                                                                 p_TipoRel,
                                                                                 this.getM_ContadoresRelatorio()));
      }

      ListaIndicadores.trimToSize();
      TamListaIndicadores = ListaIndicadores.size();

      LinhasRel = (Vector)m_Linhas.elementAt(p_Periodo);
      TamLinhas = LinhasRel.size();
      
      m_TituloRecurso[0] = ""+ListaIndicadores.elementAt(0);
      m_TituloRecurso[1] = ""+ListaIndicadores.elementAt(1);
      
      for (i = 0; i < TamLinhas; i++)
      {
         Aux = (String)LinhasRel.elementAt(i);

         Recurso = new String(Aux.substring(Aux.indexOf(';')+1, Aux.length()));
         strChave1 = Recurso.substring(0, Recurso.indexOf(';'));
         Recurso = Recurso.substring(Recurso.indexOf(';')+1, Recurso.length());            
         strChave2 = Recurso.substring(0, Recurso.indexOf(';'));         

         //
         // Monta os maps dos recursos a partir de todas as linhas do 1o. período do relatório
         //
         if (p_Periodo == 0)
         {
            m_Recurso[0] = strChave1;
            m_Recurso[1] = strChave2;
//            if (i == 0)
//            {
//               m_TituloRecurso[0] = m_Recurso[0];
//               m_TituloRecurso[1] = m_Recurso[1];
//            }
//            else
//            {
               m_MapRecursos[0].put(m_Recurso[0], m_Recurso[0]);
               m_MapRecursos[1].put(m_Recurso[1], m_Recurso[1]);
//            }
         }
         else
         {
            Linha = new String(Aux);
            Recurso = Linha.substring(Linha.indexOf(';')+1, Linha.length());
            strChave1 = Recurso.substring(0, Recurso.indexOf(';'));
         }

/* Ainda Falta CDRView NOVO */
         // Traduz a linha para uma estrutura de contadores
//         Contador Cont = NoCampoArvoreRelatorioMatraf.fnGetContador(m_Rec, Aux, false);
         
         // Retira o codigo 0;
         Aux = Aux.substring(2);
         LinhaRelProcessado = new Vector();
         // Processa os indicadores que devem ser apresentados
         for (j = 0; j < TamListaIndicadores; j++)
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
         m_MapRelatorios[p_Periodo].put(strChave1 + "-" + strChave2, LinhaRelProcessado);
/* */         
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
      if (iTam == 1 && Recursos.elementAt(0).toString().equals(m_TODOS))
      {
         //m_MapRecursosNovos[p_iTipoRecurso] = m_MapRecursos[p_iTipoRecurso];

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
}