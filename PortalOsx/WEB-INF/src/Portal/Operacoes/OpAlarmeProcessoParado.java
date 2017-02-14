//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlarmeProcessoParado.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;

/**
 */
public class OpAlarmeProcessoParado extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 4059DC8F02CD
    */
   public OpAlarmeProcessoParado() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 4059DC8F02E1
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpAlarmeProcessoParado - iniciaOperacao()");
      try
      {
         setOperacao("Alarme de Processo Parado");
         String Args[], ArgsAux[];
         Args = new String[3];
         
         ArgsAux = NoUtil.getNo().getConexaoServUtil().getCfgAlarme((short)0);
         Args[0] = p_Mensagem;
         Args[1] = ArgsAux[0];
         Args[2] = ArgsAux[1];

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/alarmeprocessoparado.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "alarmeprocessoparado.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeprocessoparado.form", Args);
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "alarmeprocessoparado.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlarmeProcessoParado - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
