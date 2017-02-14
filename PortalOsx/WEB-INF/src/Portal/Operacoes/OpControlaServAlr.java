//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpControlaServAlr.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iPerfil;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpControlaServAlr extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CE3EB2B0173
    */
   public OpControlaServAlr() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3CE3EB2B0187
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Gerenciamento do ServAlr");
                  
         // Recupera os parâmetros
         String Tipo = m_Request.getParameter("tipo");
         String Cliente = m_Request.getParameter("cliente");
         String Opcao = m_Request.getParameter("opcao");
         No noTmp = null;
         
         
         try
         {
             noTmp = NoUtil.getNoCentral();
             
             if (Tipo.equals("Termina"))
             {
                m_Resultado = noTmp.getConexaoServUtil().controlaServAlr("Finaliza",Cliente,Opcao);
                if (m_Resultado.indexOf("sucesso") == -1)
                   m_Resultado = noTmp.getConexaoServUtil().controlaServAlr("Termina",Cliente,Opcao);
             }
             else
                m_Resultado = noTmp.getConexaoServUtil().controlaServAlr(Tipo,Cliente,Opcao);
             
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
         montaTabelaResultado("/PortalOsx/servlet/Portal.cPortal?operacao=operacao=controlaServAlr&tipo="+Tipo);

         // Envia resultado
         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";
         if (Tipo.toLowerCase().equals("inicia"))
            m_Args[2] = "deteccaoservalrstartup.gif";
         else
            m_Args[2] = "deteccaoservalrshutdown.gif";
         m_Args[3] = m_Html.m_Tabela.getTabelaString();
         m_Html.enviaArquivo(m_Args);

/*
         iniciaArgs(4);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";
         if (Operacao.toLowerCase().equals("inicia"))
            m_Args[2] = "deteccaoservalrstartup.gif";
         else
            m_Args[2] = "deteccaoservalrshutdown.gif";
         m_Args[3] = Resultado;
         m_Html.enviaArquivo(m_Args);
*/         
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaServAlr - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;   
   }
}
