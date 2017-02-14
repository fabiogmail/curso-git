//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpCDRViewCliente.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpCDRViewCliente extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8F5081032F
    */
   public OpCDRViewCliente() 
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8F508803BB
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpBilhetadores - iniciaOperacao()");
      try
      {
         if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
            setOperacao("Cliente SGDT");
         else
            setOperacao("Cliente CDRView An&aacute;lise");

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "clientegen_frm1.htm";
         m_Args[2] = "CDRView Cliente";
         m_Args[3] = "src=\"/PortalOsx/templates/js/fechacdrview.js\"";
         m_Args[4] = ""; // Onload
         m_Args[5] = "cdrviewcliente.gif";
         if (NoUtil.getNo().getConexaoServControle().ping())
            m_Args[6] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "cdrviewclienteok.form", montaFormulario(p_Mensagem));
         else
            m_Args[6] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "cdrviewclientenok.form", montaFormulario("Servidor de Controle não está respondendo!"));

         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpCDRViewCliente - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C8F56C5024F
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      UsuarioDef Usuario;
      String Args[];

      if (p_Mensagem.equals("$ARG;") == true)
      {
         Args = new String[4];
         Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         Args[0] = NoUtil.getNo().getHostName();
         Args[1] = Usuario.getUsuario();
         Args[2] = Usuario.getPerfil();
         Args[3] = NoUtil.getNo().getConexaoServControle().getM_IOR();
      }
      else
      {
         Args = new String[1];
         Args[0] = p_Mensagem;
      }

      return Args;
   }
}
