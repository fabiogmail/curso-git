//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpLogAcesso.java

package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;

/**
 */
public class OpLogAcesso extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpLogAcesso - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C0A95A30332
    */
   public OpLogAcesso() 
   {
      //System.out.println("OpLogAcesso - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * Inicia a operação a ser realizada.
    * @roseuid 3C0A95A100E0
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpLogAcesso - iniciaOperacao()");
      try
      {
         setOperacao("Log de Acesso");
         String Args[] = new String[2];
         Args[0] = "visLogAcesso"; 
         Args[1] = "";
         
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             noTmp = (No) iter.next();
             Args[1] += "<option>"+noTmp.getHostName()+"</option>\n"; 
         }
         
               
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/logacesso.js\"";
         m_Args[3] = "";
         m_Args[4] = "logacesso.gif";
         if(NoUtil.isAmbienteEmCluster())
         {
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "logacessocluster.form", Args);
         }
         else
         {
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "logacesso.form", Args);
         }
         
         
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "logacesso.txt");
         m_Html.enviaArquivo(m_Args);
         return true;         
      }
      catch (Exception ExcLogAcesso)
      {
         System.out.println("OpLogAcesso - iniciaOperacao(): "+ExcLogAcesso);
         ExcLogAcesso.printStackTrace();
         return false;
      }
   }
}
