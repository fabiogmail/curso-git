//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpPerfis.java

package Portal.Operacoes;
/**
 * 
 * Visent 
 * Projeto PortalOsx 
 * @author Erick
 * @versao 1.0
 * data:17/05/2007 11:49:15
 */
public class OpSegAcessoUso extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpPerfis - Carregando classe");
   }
   
   public OpSegAcessoUso() 
   {
      //System.out.println("OpPerfis - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C21407E018F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
    	 String URL = "/templates/segAcesso/segAcessoUso.jsp";	 
         setOperacao("Segurança de Acesso de Uso");
         m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpPerfis - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
