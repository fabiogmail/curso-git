//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpControlaReprocessadores.java

package Portal.Operacoes;


import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpControlaReproc extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E1D620113
    */
   public OpControlaReproc()
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
      //System.out.println("OpControlaReprocessadores - iniciaOperacao()");
      try
      {
         setOperacao("Gerenciamento de Reprocessadores");
         // Recupera os parâmetros
         String Operacao = m_Request.getParameter("tipo");
         String CfgReprocessador = m_Request.getParameter("cfgreprocessador");
         String Resultado = null;
         OpGerenciaReproc GerReprocessadores = new OpGerenciaReproc();
         GerReprocessadores.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.buscaNobyCfgReprocessador(CfgReprocessador);
         if(noTmp == null){
         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Reprocessador " + CfgReprocessador);
         }

         Resultado = noTmp.getConexaoServUtil().controlaReprocessador(Operacao, CfgReprocessador);
         GerReprocessadores.iniciaOperacao(Resultado);
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaReprocessadores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
