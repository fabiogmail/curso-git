//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpControlaAgnCDR.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Interfaces.iPerfil;

/**
 */
public class OpControlaAgnCDR extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CE3EB300012
    */
   public OpControlaAgnCDR() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3CE3EB300076
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Gerenciamento do AgnCDR");

         // Recupera os parâmetros
         String Tipo = m_Request.getParameter("tipo");
         No noTmp = null;
        
         try
         {
             noTmp = NoUtil.getNoCentral();
             
             if (Tipo.equals("Termina"))
             {
                m_Resultado = noTmp.getConexaoServUtil().controlaAgnCDR("Finaliza");
                if (m_Resultado.indexOf("sucesso") == -1)
                   m_Resultado = noTmp.getConexaoServUtil().controlaAgnCDR("Termina");
             }
             else
             {
                m_Resultado = noTmp.getConexaoServUtil().controlaAgnCDR(Tipo);
             }
             
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
         

         // Inicia tabela de resposta
         montaTabelaResultado("/PortalOsx/servlet/Portal.cPortal?operacao=operacao=controlaAgnCDR&tipo="+Tipo);

         // Envia resultado
         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";
         if (Tipo.toLowerCase().equals("inicia"))
            m_Args[2] = "deteccaoagncdrstartup.gif";
         else
            m_Args[2] = "deteccaoagncdrshutdown.gif";
         m_Args[3] = m_Html.m_Tabela.getTabelaString();
         m_Html.enviaArquivo(m_Args);

/*
         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";
         if (Operacao.toLowerCase().equals("inicia"))
            m_Args[2] = "deteccaoagncdrstartup.gif";
         else
            m_Args[2] = "deteccaoagncdrshutdown.gif";
         m_Args[3] = Resultado;
         m_Html.enviaArquivo(m_Args);
*/
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaAgnCDR - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}

