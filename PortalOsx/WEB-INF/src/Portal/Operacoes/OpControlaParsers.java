//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpControlaParsers.java

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
public class OpControlaParsers extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E505B0333
    */
   public OpControlaParsers() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8E505B03B5
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpControlaConversores - iniciaOperacao()");
      try
      {
         setOperacao("Gerenciamento de Parsers");

         // Recupera os parâmetros
         String Operacao = m_Request.getParameter("tipo");
         String CfgParser = m_Request.getParameter("cfgparser");
         String Resultado = null;

         OpGerenciaParsers GerParsers = new OpGerenciaParsers();
         GerParsers.setRequestResponse(getRequest(), getResponse());

         No noTmp = null;
         iPerfil perfilAdmin = null;
         
         try
         {
         	 noTmp = NoUtil.getNoCentral();
             perfilAdmin = noTmp.getConexaoServUtil().getM_iListaPerfis().fnPerfilExiste(DefsComum.s_PRF_ADMIN);
             
             if (perfilAdmin != null)
             {
                 Resultado = noTmp.getConexaoServUtil().controlaParser(Operacao, CfgParser);
                 GerParsers.iniciaOperacao(Resultado);
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
         
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaConversores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
