//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpDetalhaUsuario.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpDetalhaUsuario extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CB4A4D70374
    */
   public OpDetalhaUsuario() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * Inicia a operação a ser realizada.
    * @roseuid 3CB4A4D70388
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpDetalhaUsuario - iniciaOperacao()");
      try
      {
         //setOperacao("Detalha Usuário");
         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "detalheusr.htm";
         m_Args[2] = "Detalhamento de Usuário Portal";
         m_Args[3] = "";
         m_Args[4] = "";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "detalheusrportal.form", montaFormulario());
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpDetalhaUsuario - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return String[]
    * @exception 
    * @roseuid 3CB4A54B0263
    */
   public String[] montaFormulario() 
   {
      String Args[] = new String[7];
      UsuarioDef Usuario = null;

      String SessaoId  = m_Request.getParameter("sessaoId");
      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(SessaoId);
      if (Usuario != null)
      {
         Args[0] = Usuario.getUsuario();
         Args[1] = Usuario.getPerfil();
         Args[2] = Usuario.getIP();
         Args[3] = Usuario.getHost();
         Args[4] = Usuario.getIDSessao();
         Args[5] = Usuario.getDataAcessoStr();
         Args[6] = Usuario.getOperacao();
      }
      else
      {
         for (int i = 0; i < Args.length; i++)
            Args[i] = "Sem Informação";
      }
      return Args;
   }
}
