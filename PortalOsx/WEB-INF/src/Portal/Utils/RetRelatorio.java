//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/RetRelatorio.java

package Portal.Utils;

import java.util.StringTokenizer;
import java.util.Vector;

import Interfaces.iRetRelatorio;

/**
 * Imports nao gerados pelo rose:
 * 
 * import Interfaces.*;
 */
public class RetRelatorio 
{ 
   private iRetRelatorio m_iRetRelatorio;
   private int m_LinhaInicio;
   private int m_LinhaFim;
   private int m_QtdLinhas;
   private Vector m_Periodos;
   private Vector m_Logs;
   private Vector m_Cabecalho;
   private Vector m_Header;
   private Vector m_LinhasRelatorio;
   private Vector m_LinhasIndicadores;
   private int m_PonteiroLinhas = 0;

  
   static 
   {
   }
   
   public RetRelatorio() 
   {
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 4124BAF001C1
    */
   public void setRetRelatorio(iRetRelatorio p_iRetRelatorio) 
   {
      m_iRetRelatorio = p_iRetRelatorio;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 4124C25A0152
    */
    /*
     * Metodo que ira buscar as linhas do Relatorio. Esse metodo recebe 2 parametros: 
     *  - O periodo do Relatorio
     *  - O numero de Linhas que o usuario quer ver (20, 50, 100, 200, 400 etc...)
     *  Caso o numero de linhas retornadas pelo metodo fnGetLinhas seja MENOR do que o numero de 
     *  linhas que o usuario quer ver  (armazenados na variavel numLinhas), será feito um novo
     *  posicionamento na linha do relatorio e sera chamada novamente o metodo getRelatorio, ate 
     *  que 
     * */
   public Vector getRelatorio(short periodo, int numLinhasRequisitadas) 
   {  
      int numLinhasRetornadas = 0;
      String linha="";
      StringTokenizer linhasToken;
      Vector linhasRel = new Vector();
		int novasLinhas=0;
		
      linha = m_iRetRelatorio.fnGetLinhas(); 
    
      while (!linha.equals(""))
      {                  
         linhasToken = new StringTokenizer(linha, "\n");
         numLinhasRetornadas = linhasToken.countTokens();      
       //  System.out.println("numLinhasRequisitadas: "+numLinhasRequisitadas);
      //   System.out.println("numLinhasRetornadas: "+numLinhasRetornadas);
			novasLinhas = numLinhasRequisitadas - numLinhasRetornadas;    
      
			for (int i = 0; (i < numLinhasRetornadas) && (i < numLinhasRequisitadas); i++)             
			   linhasRel.add(linhasToken.nextToken().toString());
			 
			if (novasLinhas > 0)
			{				
				linha = m_iRetRelatorio.fnGetLinhas();
				numLinhasRequisitadas = novasLinhas;
				continue;
			}          
          break;       
         //linha = m_iRetRelatorio.fnGetLinhas(); 
      }

      linhasRel.trimToSize(); 
      System.out.println("size(): "+linhasRel.size());
      return linhasRel;
   }
   
   public Vector getDados(short periodo, int numLinhasRequisitadas, short id) 
   {  
      int numLinhasRetornadas = 0;
      String linhas="";
      StringTokenizer linhasToken;
      Vector linhasRel = new Vector();
	  int novasLinhas=0;

      linhas = m_iRetRelatorio.fnGetDados(periodo,id); 
    
      while (!linhas.equals(""))
      {
    	  linhasToken = new StringTokenizer(linhas, "\n");
          numLinhasRetornadas = linhasToken.countTokens(); 
          novasLinhas = numLinhasRequisitadas - numLinhasRetornadas; 
          for (int i = 0; (i < numLinhasRetornadas) && (i < numLinhasRequisitadas); i++)             
        	  linhasRel.add(linhasToken.nextToken().toString());
			 
		  if (novasLinhas > 0)
		  {				
				linhas = m_iRetRelatorio.fnGetDados(periodo,id);
				numLinhasRequisitadas = novasLinhas;
				continue;
		  }          
         break; 
	  }

      linhasRel.trimToSize(); 
      System.out.println("size(): "+linhasRel.size());
      return linhasRel;
   }
   

   public void posicionaLinha (short periodo, int linha)
   {
      m_iRetRelatorio.fnPosiciona(periodo, linha);
   }
   
   public void posicionaDados (short periodo, int linha, short id)
   {
      m_iRetRelatorio.fnPosicionaDados(periodo, linha, id);
   }
   
   public void posicionaAposCabecalho ()
   {
      m_iRetRelatorio.fnPosicionaAposCabecalho();
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 4124C27B0195
    */
   public Vector getLogs() 
   {
      return m_Logs;
   }

   public void setLogs(Vector p_Logs)
   {
      m_Logs = p_Logs;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 4124C28B03BF
    */
   public Vector getCabecalho() 
   {
      return m_Cabecalho;
   }

   public void setCabecalho(Vector p_Cabecalho)
   {
      m_Cabecalho = p_Cabecalho;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 4124C2B0012D
    */
   public Vector getPeriodos() 
   {
      return m_Periodos;
   }

   public void setPeriodo(Vector p_Periodo)
   {
      m_Periodos = p_Periodo;
   }

   public Vector getHeader() 
   {
      return m_Header;
   }

   public void setHeader(Vector p_Header)
   {
      m_Header = p_Header;
   }
/**
 * @return
 */
public int getM_PonteiroLinhas()
{
	return m_PonteiroLinhas;
}

/**
 * @param i
 */
public void setM_PonteiroLinhas(int i)
{
	m_PonteiroLinhas = i;
}

}

