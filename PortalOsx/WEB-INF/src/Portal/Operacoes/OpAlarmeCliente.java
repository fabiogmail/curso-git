//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlarmeCliente.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpAlarmeCliente extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CC9B07B0015
    */
   public OpAlarmeCliente() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3CC9B07B0047
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpAlarmeCliente - iniciaOperacao()");
      try
      {
         if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
            setOperacao("Cliente SOLQ");
         else
            setOperacao("Cliente CDRView Detec&ccedil;&atilde;o");

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "alarmegen_frm1.htm";
         m_Args[2] = "CDRView Alarmes";
         m_Args[3] = "src=\"/PortalOsx/templates/js/fechacdrview.js\"";
         m_Args[4] = ""; // onLoad
         m_Args[5] = "cdrviewalarmes.gif";

         if (NoUtil.getNo().getConexaoServAlarme().ping())
            m_Args[6] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeclienteok.form", montaFormulario(p_Mensagem));
         else
            m_Args[6] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeclientenok.form", montaFormulario("Servidor de Alarmes não está respondendo!"));

         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlarmeCliente - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }   
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3CC9B07B005B
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      UsuarioDef Usuario;
      String Args[];

      if (p_Mensagem.equals("$ARG;") == true)
      {
         Args = new String[6];
         Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         Args[0] = NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta();
         Args[1] = Usuario.getUsuario();
         Args[2] = NoUtil.getNo().getConexaoServAlarme().getHost();
         Args[3] = Integer.toString(NoUtil.getNo().getConexaoServAlarme().getPorta());
         Args[4] = NoUtil.getNo().getConexaoServAlarme().getIOR();         
         Args[5] = Usuario.getPerfil();
      }
      else
      {
         Args = new String[1];
         Args[0] = p_Mensagem;
      }

      return Args;   
   }
}
