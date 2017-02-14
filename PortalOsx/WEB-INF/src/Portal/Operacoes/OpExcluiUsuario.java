//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpExcluiUsuario.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;


/**
 */
public class OpExcluiUsuario extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpExcluiUsuario - Carregando classe");
   }

   /**
    * @return
    * @exception
    * @roseuid 3C35E5B6013C
    */
   public OpExcluiUsuario()
   {
      //System.out.println("OpExcluiUsuario - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C35E5B60150
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExcluiUsuario - iniciaOperacao()");
      try
      {
         setOperacao("Exclusão de Usuário");

         // Recupera os parâmetros
         String Usuario   = m_Request.getParameter("usuario");

         OpUsuarios Usuarios = new OpUsuarios();
         Usuarios.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = null;
         UsuarioDef usuario = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             try
             {
	             noTmp = (No) iter.next();
	             usuario = noTmp.getConexaoServUtil().getUsuario(Usuario);
	             
	             if (usuario != null)
	             {
	                 if (noTmp.getConexaoServUtil().excluiUsuario(Usuario))
	                     Usuarios.iniciaOperacao("Usuário excluído!");
	                  else
	                     Usuarios.iniciaOperacao("Erro ao excluir usuário!"); 
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
      }
      catch (Exception Exc)
      {
         System.out.println("OpExcluiUsuario - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
