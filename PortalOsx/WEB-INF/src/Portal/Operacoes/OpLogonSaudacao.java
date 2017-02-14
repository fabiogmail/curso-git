//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpLogonSaudacao.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpLogonSaudacao extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8FD0F6030D
    */
   public OpLogonSaudacao() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8FD0F603A3
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpLogonSaudacao - iniciaOperacao()");
      try
      {
         setOperacao("Saudação");
         UsuarioDef Usuario = null;
         Usuario = NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(m_Request.getSession().getId());

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "basegen.htm";
         m_Args[2] = DefsComum.s_CLIENTE.toLowerCase()+"_saudacao.gif";
         m_Args[3] = getQtdMensagens(Usuario.getUsuario());
         m_Args[4] = getQtdAlarmes(Usuario.getUsuario());
         m_Args[5] = ""; //getMensagensAdmin();

         if (Usuario.getUsuario().equals(DefsComum.s_USR_ADMIN))
            m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "base_adm.txt");
         else
            m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "base.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpLogonSaudacao - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Usuario
    * @return String
    * @exception 
    * @roseuid 3C90AE09002E
    */
   public String getQtdMensagens(String p_Usuario) 
   {
      String QtdMsg, Mensagem = "";

      No no = NoUtil.buscaNobyNomeUsuario(p_Usuario);
      
      QtdMsg = Short.toString(no.getConexaoServUtil().getQtdMensagens(p_Usuario));
      if (QtdMsg.equals("0") == true)
         Mensagem = "<td width=\"25\" align=\"center\"><img src=\"/PortalOsx/imagens/mensagemicone0.gif\" border=\"0\" width=\"25\"></td><td align=\"left\" valign=\"middle\">N&atilde;o h&aacute; mensagens para você!</td>";
      else if (QtdMsg.equals("1") == true)
         Mensagem = "<td width=\"25\" align=\"center\"><a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=listaMensagens\"><img src=\"/PortalOsx/imagens/mensagemicone1.gif\" border=\"0\" width=\"25\" onmouseover=\"window.status='Ler mensagens';return true;\" onmouseout=\"window.status='';return true;\"></a></td><td align=\"left\" valign=\"middle\">Você tem 1 mensagem!</td>";
      else
         Mensagem = "<td width=\"25\" align=\"center\"><a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=listaMensagens\"><img src=\"/PortalOsx/imagens/mensagemicone1.gif\" border=\"0\" width=\"25\" onmouseover=\"window.status='Ler mensagens';return true;\" onmouseout=\"window.status='';return true;\"></a></td><td align=\"left\" valign=\"middle\">Você tem " + QtdMsg + " mensagens!</td>";

      return Mensagem;
   }
   
   /**
    * @param p_Usuario
    * @return String
    * @exception 
    * @roseuid 3C90AE6201F9
    */
   public String getQtdAlarmes(String p_Usuario) 
   {
      String QtdAlr, Mensagem = "";

      if (p_Usuario.equals(DefsComum.s_USR_ADMIN))
      {
         No no = NoUtil.buscaNobyNomePerfil(DefsComum.s_PRF_ADMIN);
         QtdAlr = Short.toString(no.getConexaoServUtil().getQtdAlarmes());
         if (QtdAlr.equals("0") == true)
            Mensagem = "<td width=\"25\"><img src=\"/PortalOsx/imagens/alarmeicone0.gif\" border=\"0\"></td><td align=\"left\" valign=\"middle\">N&atilde;o h&aacute; alarmes do sistema!</td>";
         else if (QtdAlr.equals("1") == true)
            Mensagem = "<td width=\"25\"><a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=listaAlarmes\"><img src=\"/PortalOsx/imagens/alarmeicone1.gif\" border=\"0\" onmouseover=\"window.status='Visualizar alarmes';return true;\" onmouseout=\"window.status='';return true;\"></a></td><td align=\"left\" valign=\"middle\">Existe 1 alarme do sistema! Verifique-o!</td>";
         else
            Mensagem = "<td width=\"25\"><a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=listaAlarmes\"><img src=\"/PortalOsx/imagens/alarmeicone1.gif\" border=\"0\" onmouseover=\"window.status='Visualizar alarmes';return true;\" onmouseout=\"window.status='';return true;\"></a></td><td align=\"left\" valign=\"middle\">Existem " + QtdAlr + " alarmes do sistema! Verifique-os!</td>";
      }

      return Mensagem;
   }
   
   /**
    * @param p_Usuario
    * @return String
    * @exception 
    * @roseuid 3C90AE630359
    */
   public String getMensagensAdmin(String p_Usuario) 
   {
      return "";
   }
}
