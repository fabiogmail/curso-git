//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpIncluiUsuario.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;



public class OpIncluiUsuario extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpIncluiUsuario - Carregando classe");
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C35E5A600CB
    */
   public void OpUsuarios() 
   {
      //System.out.println("OpIncluiUsuario - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C35E5A600DF
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpIncluiPerfil - iniciaOperacao()");
      try
      {
         setOperacao("Inclusão de Usuário");

         // Recupera os parâmetros
         String Usuario = m_Request.getParameter("usuario");
         String Perfil = m_Request.getParameter("perfil");

         OpUsuarios Usuarios = new OpUsuarios();
         Usuarios.setRequestResponse(getRequest(), getResponse());
         
         No no = NoUtil.buscaNobyNomePerfil(Perfil);
         
         int status = no.getConexaoServUtil().incluiUsuario(Usuario, Usuario, Perfil,Usuario,
        		 "","","","","","","","","",m_Request.getSession().getId(),"");
         if (status == 0){
            Usuarios.iniciaOperacao("Usuário incluído com sucesso!");
         }else{
            Usuarios.iniciaOperacao("Erro ao Incluir o Usuário! Usuário já existe!");
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiPerfil - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
