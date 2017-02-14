//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpExcluiBilhetador.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpExcluiBilhetador extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpExcluiBilhetador - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C43A08A028A
    */
   public OpExcluiBilhetador() 
   {
      //System.out.println("OpExcluiBilhetador - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C43A05503C5
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExcluiBilhetador - iniciaOperacao()");
      try
      {
         setOperacao("Exclusão de Bilhetador");
         // Recupera os parâmetros
         String Bilhetador = m_Request.getParameter("bilhetador");

         OpBilhetadores Bilhetadores = new OpBilhetadores();
         Bilhetadores.setRequestResponse(getRequest(), getResponse());

         No noTmp = NoUtil.buscaNobyBilhetador(Bilhetador);
         
         if (noTmp.getConexaoServUtil().excluiBilhetador(Bilhetador))
         {
             Bilhetadores.iniciaOperacao("Bilhetador excluído!");
         }
         else
         {
             Bilhetadores.iniciaOperacao("Erro ao excluir bilhetador!");
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpExcluiBilhetador - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
