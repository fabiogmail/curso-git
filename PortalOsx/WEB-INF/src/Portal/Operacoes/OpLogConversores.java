//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpLogConversores.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.ConfiguracaoConvCfgDef;

/**
 */
public class OpLogConversores extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D5C09070147
    */
   public OpLogConversores() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D5C09070151
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Logs de Conversores");

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/logconversores.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "conversoreslogsprocessos.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "logconversores.form", getConversores());
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "logconversores.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpLogConversores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return String[]
    * @exception 
    * @roseuid 3D5C0907015B
    */
   public String[] getConversores() 
   {
      String Temp = "", Args[];
      Vector Configuracoes = new Vector();

      // Recupera lista de Configurações
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          Vector conf = noTmp.getConexaoServUtil().getListaCfgConversores();
	          if(conf != null)
	          	Configuracoes.addAll(conf);
		  }
          catch(COMM_FAILURE comFail)
          {
              System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
          }
          catch(BAD_OPERATION badOp)
          {
              System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
              badOp.printStackTrace();
          }
      }
      
      // Inicializa array de retorno
      Args = new String[1];
      Args[0] = "";

      if (Configuracoes.size() > 0)
      {
         // Monta lista com nomes das configuracoes
         ConfiguracaoConvCfgDef Configuracao = null;
         Args[0] = "";
         for (short i = 0; i < Configuracoes.size(); i++)
         {
            Configuracao = (ConfiguracaoConvCfgDef) Configuracoes.elementAt(i);
            Args[0] += Configuracao.getNome() + ";";
         }
         if (Args[0].charAt(Args[0].length()-1) == ';')
            Args[0] = Args[0].substring(0, Args[0].length()-1);
      }
      else
      {
         Args[0] = "$ARG;";
      }
      
      return Args;
   }
}
