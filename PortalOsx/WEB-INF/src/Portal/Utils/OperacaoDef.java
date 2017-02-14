//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OperacaoDef.java

package Portal.Utils;


/**
 * Classe para representa��o das opera��es v�lidas/configuraradas. Armazena as informa��es lidas a partir do arquivo de configura��es de opera��es.
 */
public class OperacaoDef 
{
   private String m_NomeOp = null;
   private String m_Chave = null;
   
   /**
    * @param p_Operacao
    * @param p_Chave
    * @return 
    * @exception 
    * Construtor: recebe Opera��o e Chave.
    * @roseuid 3BF543E50188
    */
   public OperacaoDef(String p_Operacao, String p_Chave) 
   {
      m_NomeOp = p_Operacao;
      m_Chave  = p_Chave;
   }
   
   /**
    * @return String
    * @exception 
    * Recupera o nome da opera��o.
    * @roseuid 3BF543B2018E
    */
   public String getOperacao() 
   {
      return m_NomeOp;
   }
   
   /**
    * @return String
    * @exception 
    * Recupera a chave da opera��o.
    * @roseuid 3BF543BA00DC
    */
   public String getChave() 
   {
      return m_Chave;
   }
}
