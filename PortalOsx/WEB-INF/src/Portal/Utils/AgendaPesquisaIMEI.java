package Portal.Utils;

import java.util.StringTokenizer;
import java.util.Vector;

import CDRView2.SelecaoIndicadorPesquisaIMEI;
import Portal.Conexao.CnxServUtil;


public class AgendaPesquisaIMEI extends Agenda
{ 
   private String[] m_Rec = null;
   private String m_Indicadores;
   public Vector m_RelatoriosProcessados[] = null;
   public Vector m_IndicadoresPesquisaIMEI;
   
   public AgendaPesquisaIMEI(CnxServUtil p_ConexUtil, String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, SelecaoIndicadorPesquisaIMEI p_SelecaoIndicadorPesquisaIMEI, String p_IndicadoresPesquisaIMEI, String p_NomeRel, String p_DataGer)
   {
      super(p_ConexUtil, p_Perfil, p_TipoRel, p_IdRel, p_Arquivo, p_NomeRel, p_DataGer);
      
      this.setM_SelecaoIndicador(p_SelecaoIndicadorPesquisaIMEI);
      this.setM_Rec(m_Rec);
      this.setM_ContadoresRelatorio(m_iRetRelatorio.fnGetContadores());
      if(Agenda.TIPO_REL_IMEI==0)
    	  m_IndicadoresPesquisaIMEI = VetorUtil.String2Vetor(p_IndicadoresPesquisaIMEI, ';');
      else{
    	  String indAux  = this.getIndicadoresDefinidos(p_TipoRel);
    	  m_IndicadoresPesquisaIMEI = VetorUtil.String2Vetor(indAux, ';');
    	  m_vIndicadores = m_IndicadoresPesquisaIMEI;
    	  this.setM_ContadoresRelatorio(indAux);
      }
    	  
         
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
      
      // Loop em que sao adicionados todos os contadores do relatorio Detalhe de Chamadas
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
         if(Agenda.TIPO_REL_IMEI==0)
        	 Aux = Aux.startsWith("0;") ? Aux.substring(2) : Aux;
         else
        	 Aux = Aux.startsWith("12;") ? Aux.substring(3) : Aux;


         LinhaRelProcessado = new Vector(5000,5000);

         if(Agenda.TIPO_REL_IMEI==0)
         {
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
         }
         else//gato que quebra a linha na tora e preenche a linha do relatorio sem chamar a lib java
         {
        	 //tirando o ponto e virgula do inicio
        	 Aux = Aux.substring(1);
        	 String ind = "";
        	 if(this.indicadoresDefinidosIMEI!="")
        	 {
        		 ind = this.indicadoresDefinidosIMEI;
        	 }
        	 else
        	 {
        		 ind = this.getIndicadoresDefinidos("30");//30 corresponde ao relatorio de pesquisaimei
        	 }
        	 String arrayInd[] = ind.split(";");
        	
        	
        	 //array que serve para saber quais ids podem ser vistos
        	 Vector idsIndPermitidos = new Vector();
        	 
        	 //coparação que serve para saber qual o indice de indicador esta presente exemplo
        	 // imei; origem; cham; ttc = 0;1;2;3
        	 for(int k=0; k< arrayInd.length; k++)
        	 {
        		 for (int j = 0; j < m_vIndicadores.size(); j++) 
        		 {
					if(arrayInd[k].equalsIgnoreCase(""+m_vIndicadores.get(j)))
					{
						idsIndPermitidos.add(new Integer(k));
					}
				}
        	 }
        	
        	 //colocando o m_vIndicadores na ordem certa
        	 m_vIndicadores = new Vector();
        	 for(int l=0; l<idsIndPermitidos.size();l++)
        	 {
        		 m_vIndicadores.add(arrayInd[((Integer)idsIndPermitidos.get(l)).intValue()]);
        	 }
        	 
        	 String[] valores = Aux.split(";");
        	 for (int j = 0; j < valores.length; j++)
             {
        		 if(idsIndPermitidos.contains(new Integer(j)))
        		 {
        			 LinhaRelProcessado.add(valores[j]); 
        		 }        		 
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