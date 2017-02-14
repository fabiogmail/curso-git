//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OperacaoDef.java

package Portal.Utils;


/**
 * Classe para representação das operações válidas/configuraradas. Armazena as informações lidas a partir do arquivo de configurações de operações.
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
    * Construtor: recebe Operação e Chave.
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
    * Recupera o nome da operação.
    * @roseuid 3BF543B2018E
    */
   public String getOperacao() 
   {
      return m_NomeOp;
   }
   
   /**
    * @return String
    * @exception 
    * Recupera a chave da operação.
    * @roseuid 3BF543BA00DC
    */
   public String getChave() 
   {
      return m_Chave;
   }
}
