//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpExcluiCfgConversor.java

package Portal.Operacoes;

import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpExcluiCfgConversor extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpExcluiCfgConversor - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C46CD12035F
    */
   public OpExcluiCfgConversor() 
   {
      //System.out.println("OpExcluiCfgConversor - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C46CD120373
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExcluiCfgConversor - iniciaOperacao()");
      try
      {
         setOperacao("Exclusão de Cfg de Conversor");

         // Recupera os parâmetros
         String Configuracao = m_Request.getParameter("configuracao");

         OpCfgConversores Conversores = new OpCfgConversores();
         Conversores.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.buscaNobyCfgConversor(Configuracao);
         if(noTmp == null){
         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Conversor " + Configuracao);
         }
         
	     if (noTmp.getConexaoServUtil().excluiCfgConversor(Configuracao))
	     {
	         Conversores.iniciaOperacao("Configuração de conversor excluída!");
	     }
	     else
	     {
	         Conversores.iniciaOperacao("Erro ao excluir configuração de conversor!");
	     }
      }
      catch (Exception Exc)
      {
         System.out.println("OpExcluiCfgConversor - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
