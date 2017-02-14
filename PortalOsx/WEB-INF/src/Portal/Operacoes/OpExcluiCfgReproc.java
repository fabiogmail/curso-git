//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpExcluiCfgConversor.java

package Portal.Operacoes;

import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpExcluiCfgReproc extends OperacaoAbs 
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
   public OpExcluiCfgReproc() 
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
         setOperacao("Exclusão de Cfg de Reprocessador");

         // Recupera os parâmetros
         String Configuracao = m_Request.getParameter("configuracao");

         OpCfgReprocessadores Reprocessadores = new OpCfgReprocessadores();
         Reprocessadores.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.buscaNobyCfgReprocessador(Configuracao);
         if(noTmp == null){
         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Reprocessador " + Configuracao);
         }
         
	     if (noTmp.getConexaoServUtil().excluiCfgReprocessador(Configuracao))
	     {
	     	 Reprocessadores.iniciaOperacao("Configuração de reprocessador excluída!");
	     }
	     else
	     {
	     	 Reprocessadores.iniciaOperacao("Erro ao excluir configuração de reprocessador!");
	     }
      }
      catch (Exception Exc)
      {
         System.out.println("OpExcluiCfgReproc - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}