//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlarmeEspacoDisco.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpAlarmeEspacoDisco extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C5E98F30130
    */
   public OpAlarmeEspacoDisco() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C5E98F30159
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpAlarmeEspacoDisco - iniciaOperacao()");
      try
      {
         setOperacao("Alarme de Espaço Disco");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/alarmeespacodisco.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "alarmeespacodisco.gif";
         
         if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro"))
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeespacodisco_claro.form", montaFormulario(p_Mensagem));
         else
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeespacodisco.form", montaFormulario(p_Mensagem));
         
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "alarmeespacodisco.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlarmeEspacoDisco - iniciaOperacao(): "+Exc);
		 Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C5E98F30163
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      int Cont = 2;
      String Args[] = null, CfgAlr[] = null;
      

      CfgAlr = NoUtil.getNo().getConexaoServUtil().getCfgAlarme((short)1);
      Args = new String[CfgAlr.length + 1];
      Args[0] = p_Mensagem;
      Args[1] = CfgAlr[3];

      for (int i = 0; i < CfgAlr.length; i++)
      {
         //System.out.println("->CfgAlr["+i+"]: "+CfgAlr[i]);
         if (i == 3)
            continue;
         else
         {
            Args[Cont] = CfgAlr[i];
            Cont++;
         }
      }

      return Args;
   }
}
