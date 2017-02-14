//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpIncluiBilhetador.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpIncluiBilhetador extends OperacaoAbs 
{
   
   static 
   {
      System.out.println("OpIncluiBilhetador - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C43A07800CC
    */
   public OpIncluiBilhetador() 
   {
      System.out.println("OpIncluiBilhetador - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C43A04A03D3
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      System.out.println("OpIncluiBilhetador - iniciaOperacao()");
      try
      {
         setOperacao("Inclusão de Bilhetador");

         // Recupera os parâmetros
         String Bilhetador = m_Request.getParameter("bilhetador");
         String Tecnologia = m_Request.getParameter("tecnologia");
         String Operadora  = m_Request.getParameter("operadora");
         String Servidor   = m_Request.getParameter("servidor");
         if (Operadora == null) Operadora = new String("");
         String Apelido = m_Request.getParameter("apelido");
         String Fase = m_Request.getParameter("fase");         

         OpBilhetadores Bilhetadores = new OpBilhetadores();
         Bilhetadores.setRequestResponse(getRequest(), getResponse());

//         No noTmp = NoUtil.buscaNobyBilhetador(Bilhetador);
         No noTmp = null;
         boolean sucessoInclusaoBilhetador = true;
        
         try
         {
         	 if(Servidor != null)
         	 {
         	 	noTmp = NoUtil.getNoByHostName(Servidor);
         	 }
         	 else
         	 {
         	 	noTmp = NoUtil.getNoCentral();
         	 }
             
             
             if (noTmp.getConexaoServUtil().incluiBilhetador(Bilhetador, Tecnologia, Operadora, Apelido, Fase) == false)
             {
                 sucessoInclusaoBilhetador = false;
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
         

         if (sucessoInclusaoBilhetador)
         {
             Bilhetadores.iniciaOperacao("Bilhetador incluído!");
         }
         else
         {
             Bilhetadores.iniciaOperacao("Bilhetador já existe!");
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiBilhetador - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
