//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlteraBilhetador.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpAlteraBilhetador extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpAlteraBilhetador - Carregando classe");
   }

   /**
    * @return
    * @exception
    * @roseuid 3D7FB40902DF
    */
   public OpAlteraBilhetador()
   {
      //System.out.println("OpAlteraBilhetador - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception
    * @roseuid 3D7FB3FE0275
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      //System.out.println("OpExcluiBilhetador - iniciaOperacao()");
      try
      {
         setOperacao("Alteração de Bilhetador");
         // Recupera os parâmetros
         String Bilhetador = m_Request.getParameter("bilhetador");
         System.out.println("bilhetador: "+Bilhetador);
         String BilhetadorNovo = m_Request.getParameter("bilhetador_novo");
         System.out.println("bilhetador novo: "+BilhetadorNovo);
         String Tecnologia = m_Request.getParameter("tecnologia");
         System.out.println("tecnologia: "+Tecnologia);
         String Operadora = m_Request.getParameter("operadora");
         if (Operadora == null) Operadora = new String("");
         System.out.println("operadora: "+Operadora);         
         String Apelido = m_Request.getParameter("apelido");
         System.out.println("apelido: "+Apelido);
         String Fase = m_Request.getParameter("fase");
         System.out.println("fase: "+Fase);

         OpBilhetadores Bilhetadores = new OpBilhetadores();
         Bilhetadores.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = null;
         boolean alteracaoOk = false;
         
         try
         {
         	 noTmp = NoUtil.buscaNobyBilhetador(Bilhetador);
             alteracaoOk = noTmp.getConexaoServUtil().alteraBilhetador(Bilhetador, BilhetadorNovo, Tecnologia, Operadora, Apelido, Fase);
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
         

        
         if (alteracaoOk)
            Bilhetadores.iniciaOperacao("Bilhetador alterado!");
         else
            Bilhetadores.iniciaOperacao("Erro ao alterar bilhetador!");

      }
      catch (Exception Exc)
      {
         System.out.println("OpAlteraBilhetador - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
