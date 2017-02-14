//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpCDRViewCliente.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpCDRViewCliente2 extends OperacaoAbs 
{
   public static short qtdAberturas = 0;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8F5081032F
    */
   public OpCDRViewCliente2() 
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
      try
      {
         if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
            setOperacao("Cliente SGDT");
         else
            setOperacao("Cliente CDRView An&aacute;lise");

         iniciaArgs(8);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "clientegen_frm1.htm";
         m_Args[2] = "CDRView Cliente";
         m_Args[3] = "src=\"/PortalOsx/templates/js/fechacdrview.js\"";
         m_Args[4] = ""; // Onload
         m_Args[5] = "cdrviewcliente.gif";
         qtdAberturas +=1;
         if (NoUtil.getNo().getConexaoServControle().ping()) {
            m_Args[6] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "cdrviewcliente2ok.form", montaFormulario(p_Mensagem));
            m_Args[7] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(),"cdrviewcliente.txt");
         }
         else
            m_Args[6] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "cdrviewclientenok.form", montaFormulario("Servidor de Controle não está respondendo!"));
            m_Args[7] = "";
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
         Args = new String[9];
         Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         Args[0] = NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta();
         Args[1] = Usuario.getUsuario();
         Args[2] = Usuario.getPerfil();
         Args[3] = NoUtil.getNo().getConexaoServControle().getM_IOR();
         Args[4] = NoUtil.getNo().getIp();//Ip para conexão com servidor de Agenda via RMi.

         System.out.println("Args: " + qtdAberturas);
         if(qtdAberturas > 1)
        	 Args[5] = "false";
         else
        	 Args[5] = "true";
         
         System.out.println("Args: " + Args[5]);
         Args[6] = NoUtil.getNo().getPortaRMI();//Porta para conexão com servidor de Agenda via RMi.
         Args[7] = NoUtil.getNo().getHostName();//Nome do host para conexão RMI

         if(DefsComum.sSUB_CLIENTE != null){
        	 if(DefsComum.sSUB_CLIENTE.length() > 0 ){
        		 Args[8] = DefsComum.sSUB_CLIENTE;
        	 }
         }else{
        	 Args[8] = DefsComum.s_CLIENTE;
         }
      }
      else
      {
         Args = new String[1];
         Args[0] = p_Mensagem;
      }

      return Args;
   }
}
