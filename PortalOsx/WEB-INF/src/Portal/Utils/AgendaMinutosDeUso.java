package Portal.Utils;

import java.util.Vector;

import CDRView2.SelecaoIndicadorMinutosDeUso;
import CDRView2.SelecaoRecursosMinutosDeUso;
import Portal.Conexao.CnxServUtil;


public class AgendaMinutosDeUso extends Agenda
{
   public  Vector m_Recv = null;
   private String m_Indicadores;
   public  Vector m_RelatoriosProcessados[] = null;
   public  Vector m_IndicadoresMinutosDeUso;
   
   public AgendaMinutosDeUso(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, SelecaoRecursosMinutosDeUso p_SelecaoRecursosMinutosDeUso, SelecaoIndicadorMinutosDeUso p_SelecaoIndicadorMinutosDeUso, String p_IndicadoresMinutosDeUso, String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);

      this.setM_SelecaoRecursos(p_SelecaoRecursosMinutosDeUso);
      this.setM_SelecaoIndicador(p_SelecaoIndicadorMinutosDeUso);
      this.setM_ContadoresRelatorio(m_iRetRelatorio.fnGetContadores());
      
      m_IndicadoresMinutosDeUso = VetorUtil.String2Vetor(p_IndicadoresMinutosDeUso, ';');
      
      // Identifica quais os recursos do relat�rio
      m_Recv = identificaRecursos(VetorUtil.String2Vetor(m_IndicadoresIniciais,';'), m_TipoColunasV);
      this.setM_Recv(m_Recv);
      
      // Retira o primeiro elemento da lista de indicadores pois � recurso COM CERTEZA!!     
      m_Indicadores = m_IndicadoresIniciais.substring(m_IndicadoresIniciais.indexOf(";")+1, m_IndicadoresIniciais.length());           
      	
      for (int i = 0; i < m_Recv.size()-1; i++) {
          m_Indicadores = m_Indicadores.substring(m_Indicadores.indexOf(";")+1, m_Indicadores.length());
      }
	
      montaRelatorios(Short.parseShort(p_TipoRel));
   }

   public Vector montaLinhas(int p_Periodo, short p_TipoRel)
   {
      int InicioIndicador = m_Recv.size(), TamListaIndicadores, TamLinhas;
      String Aux = null, Aux1 = null, Aux2 = null, NomePeriodo = "";
      String StrIndicador;
      Vector ListaIndicadores, RelProcessado, LinhaRelProcessado, LinhasRel;

      ListaIndicadores = new Vector();

      
      for (int j = 0; j < InicioIndicador; j++)
      {
		ListaIndicadores.addElement(this.getM_SelecaoRecursos().fnGetIndicador(m_vIndicadores.elementAt(j).toString(),
		                                                                       m_Recv,
		                                                                       p_TipoRel,
		                                                                       this.getM_ContadoresRelatorio()));     
      }  

      // Cria vetor com todos os indicadores do relat�rio que irao aparecer na interface
      for (int j = InicioIndicador; j < m_vIndicadores.size(); j++)
      {         
          ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador(m_vIndicadores.elementAt(j).toString(),
                                                                                  m_Recv,
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
         
         // Processa os indicadores que devem ser apresentados
         for (int j = 0; j < TamListaIndicadores; j++)
         {            
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor)ListaIndicadores.elementAt(j);

            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), linhaTotalizacao.split(";") ).toString();//alterado para o tipo indicador participacao
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