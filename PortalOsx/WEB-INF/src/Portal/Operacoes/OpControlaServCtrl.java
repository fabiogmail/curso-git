//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpControlaServCtrl.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;

/**
 */
public class OpControlaServCtrl extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C9235DB02B8
    */
   public OpControlaServCtrl() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C9235E901BE
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Gerenciamento do ServCtrl");
         // Recupera os parâmetros
         String Tipo = m_Request.getParameter("tipo");
//         m_Resultado = m_ConexUtil.controlaServCtrl(Tipo);

         // Inicia tabela de resposta
         montaTabelaResultado("/PortalOsx/servlet/Portal.cPortal?operacao=controlaServCtrl&tipo=Inicia");

         // Envia resultado
         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";
         if (Tipo.toLowerCase().equals("inicia"))
            m_Args[2] = "analisestartup.gif";
         else
            m_Args[2] = "analiseshutdown.gif";
         m_Args[3] = m_Html.m_Tabela.getTabelaString();
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaServCtrl - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
