package Portal.Utils;

import java.util.Vector;

import CDRView2.SelecaoIndicadorDestinosComuns;
import CDRView2.SelecaoRecursosDestinosComuns;
import Portal.Conexao.CnxServUtil;


public class AgendaDestinosComuns extends Agenda
{
   public String[] m_Rec = null;
   private String m_Indicadores;
   //private SelecaoRecursosPesquisaCodigo m_SelecaoRecursosPesquisa;
   //private SelecaoIndicadorPesquisaCodigo m_SelecaoIndicadorPesquisa;
   public Vector m_RelatoriosProcessados[] = null;
   public Vector m_IndicadoresDestinosComuns;   

   
   public AgendaDestinosComuns(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, SelecaoRecursosDestinosComuns p_SelecaoRecursosDestinosComuns, SelecaoIndicadorDestinosComuns p_SelecaoIndicadorDestinosComuns, String p_IndicadoresDestinosComuns, String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);

//      this.setM_SelecaoRecursos(p_SelecaoRecursosPesquisa);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorDestinosComuns);
      this.setM_ContadoresRelatorio(m_iRetRelatorio.fnGetContadores());

      m_IndicadoresDestinosComuns = VetorUtil.String2Vetor(p_IndicadoresDestinosComuns, ';');

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

   public Vector montaLinhas(int p_Periodo, short p_TipoRel)
   {
      int InicioIndicador = 1, TamListaIndicadores, TamLinhas;
      String Aux = null, Aux1 = null, Aux2 = null, NomePeriodo = "";
      String StrIndicador;
      Vector ListaIndicadores, RelProcessado, LinhaRelProcessado, LinhasRel;

      ListaIndicadores = new Vector();
     // ListaIndicadores.addElement(m_SelecaoRecursosPesquisa.fnGetIndicador((String)m_vIndicadores.elementAt(0),p_TipoRel));

      // Cria vetor com todos os indicadores do relatório
      for (int j = 0; j < m_vIndicadores.size(); j++)
      {
         ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)m_vIndicadores.elementAt(j),
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
         
         /**
          * Caso a linha contenha a flag de controle "0;" a mesma eh 
          * removida, para evitar problemas de deslocamento dos contadores.
          * */
         Aux = Aux.startsWith("0;") ? Aux.substring(2) : Aux;

         LinhaRelProcessado = new Vector();

         for (int j = 0; j < TamListaIndicadores; j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor) ListaIndicadores.elementAt(j);
            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), null).toString();
               LinhaRelProcessado.add(StrIndicador);
            }
         }
         
         LinhaRelProcessado.trimToSize();
         RelProcessado.add(LinhaRelProcessado);
      }
      
      RelProcessado.trimToSize();
      return RelProcessado;
   }

   public void montaRelatorios(short p_TipoRel)
   {
      if (m_RelatoriosProcessados == null)
      {
         m_RelatoriosProcessados = new Vector[m_QtdPeriodos];
         for (int i = 0; i < m_QtdPeriodos; i++)
            m_RelatoriosProcessados[i] = montaLinhas(i,p_TipoRel);
      }
   }   

   public void remontaRelatorios(short p_TipoRel)
   {
      for (int i = 0; i < m_QtdPeriodos; i++)
         m_RelatoriosProcessados[i] = null;   
      m_RelatoriosProcessados = null;

      montaRelatorios(p_TipoRel);
   }   
}