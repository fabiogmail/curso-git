//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpRefresh.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpRefresh extends OperacaoAbs
{

   static
   {
   }

   /**
    * @return
    * @exception
    * @roseuid 3DB3FDF30294
    */
   public OpRefresh()
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception
    * @roseuid 3DB3FDF302A8
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      try
      {
         String Sessao = "";
         String Pagina = m_Request.getParameter("pagina");
         String Texto  = m_Request.getParameter("texto");

         if (Texto.toLowerCase().equals("none") == false)
         {
            if (Pagina.toLowerCase().startsWith("cliente") == true)
            {
               if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
                  setOperacao("Cliente SGDT");
               else
                  setOperacao("Cliente CDRView An&aacute;lise");
            }
            else
            {
               if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
                  setOperacao("Cliente SOLQ");
               else
                  setOperacao("Cliente CDRView Detec&ccedil;&atilde;o");
            }

            iniciaArgs(6);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = Pagina;
            m_Args[2] = "";  // js
            m_Args[3] = "";  // onload
            m_Args[4] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), Texto, null);
         }
         else
         {
            UsuarioDef Usuario = null;
            Usuario = (UsuarioDef) NoUtil.getNo().getUsuarioLogados().get(m_Request.getSession().getId());
            String QtdAlr, QtdMsg, Mensagem = "";
            
            if (Usuario == null)
               return true;
            if (Usuario.getUsuario().toLowerCase().equals("administrador") == true)
            {
              QtdAlr = Short.toString(Usuario.getNo().getConexaoServUtil().getQtdAlarmes());
              if (QtdAlr.equals("0") == false)
                Mensagem = "<img src=\"/PortalOsx/imagens/alarmeicone1.gif\">";
                //Mensagem += "<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=listaAlarmes\"><img src=\"/PortalOsx/imagens/alarmeicone1.gif\" border=\"0\" onmouseover=\"window.status='Visualizar alarmes';return true;\" onmouseout=\"window.status='';return true;\">";

              if (Mensagem.length() > 0)
                Mensagem += " &nbsp; ";
            }
            
            QtdMsg = Short.toString(Usuario.getNo().getConexaoServUtil().getQtdMensagens(Usuario.getUsuario()));
         //   if (QtdMsg.equals("0") == false)
         //      Mensagem += "<img src=\"/PortalOsx/imagens/mensagemicone1.gif\" width=\"25\">";
               //Mensagem += "<td width=\"25\"><a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=listaMensagens\"><img src=\"/PortalOsx/imagens/mensagemicone1.gif\" border=\"0\" onmouseover=\"window.status='Ler mensagens';return true;\" onmouseout=\"window.status='';return true;\">";

            iniciaArgs(3);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = Pagina;
            m_Args[2] = Mensagem;
         }
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpRefresh - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }   
   }
}
