//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpControlaConversores.java

package Portal.Operacoes;


import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpControlaConversores extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E1D620113
    */
   public OpControlaConversores()
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception
    * @roseuid 3C8E1D620127
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      //System.out.println("OpControlaConversores - iniciaOperacao()");
      try
      {
         setOperacao("Gerenciamento de Conversores");
         // Recupera os parâmetros
         String Operacao = m_Request.getParameter("tipo");
         String CfgConversor = m_Request.getParameter("cfgconversor");
         String Resultado = null;
         OpGerenciaConversores GerConversores = new OpGerenciaConversores();
         GerConversores.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.buscaNobyCfgConversor(CfgConversor);
         if(noTmp == null){
         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Conversor " + CfgConversor);
         }

         Resultado = noTmp.getConexaoServUtil().controlaConversor(Operacao, CfgConversor);
         GerConversores.iniciaOperacao(Resultado);
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaConversores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
