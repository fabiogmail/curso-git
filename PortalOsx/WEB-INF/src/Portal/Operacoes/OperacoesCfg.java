//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OperacoesCfg.java

package Portal.Operacoes;

import Portal.Utils.Arquivo;

/**
 * Classe de configuração das operações possíveis no portal.
 */

public class OperacoesCfg 
{
   private String[] m_Operacoes;

	/**
	 * 
	 * @uml.property name="m_ArqCfgOperacoes"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Arquivo m_ArqCfgOperacoes;

   
   public OperacoesCfg() 
   {
   }
   
   /**
    * @return boolean
    * @exception 
    * Realiza a leitura do arquivo de configuração de operações. Em caso de erro, retorna "false".
    * @roseuid 3BF082BA01E8
    */
   public boolean leOperacoesCfg() 
   {
      return false;
   }
}
