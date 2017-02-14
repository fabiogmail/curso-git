//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpFormAlteraSenha.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpFormAlteraSenha extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C87972D01CB
    */
   public OpFormAlteraSenha() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C87972D01DF
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpFormAlteraSenha - iniciaOperacao()");
      try
      {
         setOperacao("Alteração de Senha");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/alteracaosenha.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "alteracaosenha.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alteracaosenha.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "alteracaosenha.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpFormAlteraSenha - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C87972D01F3
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      int QtdElem;
      String Args[] = new String[4];
      UsuarioDef Usuario;

      for (short i = 0; i < Args.length; i++)
         Args[i] = "";
         
      Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());

      Args[0] = p_Mensagem;
      Args[1] = Usuario.getUsuario();      
      Args[2] = DefsComum.s_CLIENTE;
      Args[3] = Usuario.getUsuario();
      return Args;
   }
}
