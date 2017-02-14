package Portal.Utils;

import java.util.Vector;

import CDRView2.SelecaoIndicadorPrefixosDeRisco;
import Portal.Conexao.CnxServUtil;


public class AgendaPrefixosDeRisco extends Agenda
{ 
   private String[] m_Rec = null;
   private String m_Indicadores;
   public Vector m_RelatoriosProcessados[] = null;
   public Vector m_IndicadoresPrefixosDeRisco;
   
   public AgendaPrefixosDeRisco(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, SelecaoIndicadorPrefixosDeRisco p_SelecaoIndicadorPrefixosDeRisco, String p_IndicadoresPrefixosDeRisco, String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);
      
      this.setM_SelecaoIndicador(p_SelecaoIndicadorPrefixosDeRisco);
      this.setM_Rec(m_Rec);
      this.setM_ContadoresRelatorio(m_iRetRelatorio.fnGetContadores());
      
      m_IndicadoresPrefixosDeRisco = VetorUtil.String2Vetor(p_IndicadoresPrefixosDeRisco, ';');      
         
      montaRelatorios(Short.parseShort(p_TipoRel));
   }

   public Vector montaLinhas(int p_Periodo, short p_TipoRel)
   {      
      String Aux = null, StrIndicador;
      Vector LinhasRel, RelProcessado, LinhaRelProcessado, ListaIndicadores;
      final int l_TamListaIndicadores;
      final int l_NumLinhas; // Variavel que guarda o numero de linhas do relatorio lido

      // Cria vetor com todos os indicadores do relatório      
      ListaIndicadores = new Vector(100,100);
      // Vector que possui todos as linhas do relatorio processado.
      RelProcessado = new Vector(5000,5000);   
      
      // Loop em que sao adicionados todos os contadores do relatorio PrefixosDeRisco
      for (int j = 0; j < m_vIndicadores.size(); j++)      
      {         
         ListaIndicadores.addElement(this.getM_SelecaoIndicador().fnGetIndicador((String)m_vIndicadores.elementAt(j), 
                                                                                 p_TipoRel,
                                                                                 this.getM_ContadoresRelatorio()));
      }
      
      ListaIndicadores.trimToSize();
      l_TamListaIndicadores = ListaIndicadores.size();
      
      LinhasRel = (Vector)m_Linhas.elementAt(p_Periodo);
      l_NumLinhas = LinhasRel.size();      
     
      for (int i = 0; i < l_NumLinhas; i++)
      {
         Aux = (String)LinhasRel.elementAt(i);
         
         /**
          * Caso a linha contenha a flag de controle "0;" a mesma eh 
          * removida, para evitar problemas de deslocamento dos contadores.
          * */
         Aux = Aux.startsWith("0;") ? Aux.substring(2) : Aux;

         LinhaRelProcessado = new Vector(5000,5000);

         for (int j = 0; j < l_TamListaIndicadores; j++)
         {
            libjava.indicadores.IndicadorValor NoCampoInd = 
               (libjava.indicadores.IndicadorValor) ListaIndicadores.elementAt(j);
            if (NoCampoInd != null)
            {
               StrIndicador = NoCampoInd.fnProcessa(libjava.tipos.TipoDadoString.fnTokenLinha(Aux), null).toString();
               LinhaRelProcessado.add(StrIndicador);
            }
         }
         
         //Limpando as referencias dos objetos para o GC.                  
         
         LinhaRelProcessado.trimToSize();
         RelProcessado.add(LinhaRelProcessado);
         LinhaRelProcessado = null;
      }
   
      RelProcessado.trimToSize();  
      
      // Chamando o GC para liberar memoria.
      System.gc();
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
   	if (m_RelatoriosProcessados != null)
    {
      for (int i = 0; i < m_QtdPeriodos; i++)
         m_RelatoriosProcessados[i] = null;   
      m_RelatoriosProcessados = null;
    }

      montaRelatorios(p_TipoRel);
   }
/**
 * @return Returns the m_RelatoriosProcessados.
 */
public Vector[] getRelatoriosProcessados() {
	return m_RelatoriosProcessados;
}
/**
 * @param relatoriosProcessados The m_RelatoriosProcessados to set.
 */
public void setRelatoriosProcessados(Vector[] relatoriosProcessados) {
	m_RelatoriosProcessados = relatoriosProcessados;
}
}