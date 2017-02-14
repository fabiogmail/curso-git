//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpExcluiCfgParser.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;


/**
 */
public class OpExcluiCfgParser extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E4FFF038B
    */
   public OpExcluiCfgParser()
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8E4FFF03D1
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExcluiCfgParser - iniciaOperacao()");
      try
      {
         setOperacao("Exclusão de Cfg de Parser");

         // Recupera os parâmetros
         String Configuracao = m_Request.getParameter("configuracao");

         OpCfgParsers Parsers = new OpCfgParsers();
         Parsers.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.getNoCentral();
         
         if (noTmp.getConexaoServUtil().excluiCfgParser(Configuracao))
         {
             Parsers.iniciaOperacao("Configuração de parser excluída!");
         }
         else
         {
             Parsers.iniciaOperacao("Erro ao excluir configuração de parser!");
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpExcluiCfgParser - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
