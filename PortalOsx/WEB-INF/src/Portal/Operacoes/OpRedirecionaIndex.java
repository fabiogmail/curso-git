//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlteraSenha.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;

/**
 */
public class OpRedirecionaIndex extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C877BB102B4
    */
   public OpRedirecionaIndex() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C877BB102BE
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
  
         setOperacao("Cadastro de nova Senha");
         
         m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/paginas/index.htm");
        	
         
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlteraSenha - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
      
      return true;

   }
}

