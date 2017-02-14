//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlteraSenha.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpAlteraSenha extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C877BB102B4
    */
   public OpAlteraSenha() 
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
      //System.out.println("OpAlteraSenha - iniciaOperacao()");
      try
      {
         UsuarioDef Usuario;
         setOperacao("Alteração de Senha");
         // Recupera os parâmetros
         String SenhaOld = m_Request.getParameter("senhaold");
         String SenhaNew = m_Request.getParameter("senhanew");
         //String DicaSenha = m_Request.getParameter("dicasenha");         

         OpFormAlteraSenha AlteraSenha = new OpFormAlteraSenha();
         AlteraSenha.setRequestResponse(getRequest(), getResponse());

         Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         if (Usuario.getSenha().equals(SenhaOld) == false)
         {
            AlteraSenha.iniciaOperacao("Senha atual incorreta!");
         }
         else
         {
            if (Usuario.getNo().getConexaoServUtil().alteraSenha(Usuario.getUsuario(), SenhaNew))
            {
               Usuario.setSenha(SenhaNew);
               //Usuario.setDicaSenha(DicaSenha);
               AlteraSenha.iniciaOperacao("Senha alterada com sucesso!");
            }
            else
               AlteraSenha.iniciaOperacao("Erro ao alterar a senha!");
         }
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

