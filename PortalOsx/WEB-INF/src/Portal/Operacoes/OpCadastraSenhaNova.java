//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlteraSenha.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpCadastraSenhaNova extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C877BB102B4
    */
   public OpCadastraSenhaNova() 
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
         UsuarioDef Usuario;
         
         setOperacao("Cadastro de nova Senha");
         
         String SenhaAntiga = m_Request.getParameter("senhaatual");
         String SenhaNova = m_Request.getParameter("senha1");

         Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         iniciaArgs(3);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB()+"templates/jsp/";
         m_Args[1] = "senhaNova.jsp";
         if (Usuario.getSenha().equals(SenhaAntiga) == false)//só pra reforçar que é mesmo o usuario!
         {        	
             m_Args[2] = "Senha Atual incorreta!";
             m_Html.enviaArquivo(m_Args);        	
         }
         else 
         {
        	 if(SenhaNova.equals(Usuario.getUsuario()))
        	 {       	 
	             m_Args[2] = "Nova senha deve ser diferente do login";
	             m_Html.enviaArquivo(m_Args); 
        	 }
        	 else
        	 {
        		 if (Usuario.getNo().getConexaoServUtil().alteraSenha(Usuario.getUsuario(), SenhaNova))
        		 {        		 
        			 m_Args[2] = "Senha alterada com sucesso!";
        			 m_Html.enviaArquivo(m_Args);
        		 }
        		 else
        		 {
        			 m_Args[2] = "Erro no ServUtil, senha não alterada";
        			 m_Html.enviaArquivo(m_Args);
        		 }
        	 }
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

