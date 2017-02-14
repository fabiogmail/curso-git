//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpIncluiPerfil.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;



public class OpIncluiPerfilAntigo extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpIncluiPerfil - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * Construtor.
    * @roseuid 3C335E28002C
    */
   public OpIncluiPerfilAntigo() 
   {
      //System.out.println("OpIncluiPerfil - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C335E210130
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpIncluiPerfil - iniciaOperacao()");
      try
      {
         setOperacao("Inclusão de Perfil");

         // Recupera os parâmetros
         String Perfil = m_Request.getParameter("perfil");
         String Acesso = m_Request.getParameter("acesso");

         OpPerfisAntigo Perfis = new OpPerfisAntigo();
         Perfis.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = null;
         No no = null; // No que contem a menor qtd de usuarios.
         int qtdUsuarios = Integer.MAX_VALUE, qtdUsuariosTmp = -1;
         boolean perfilExistente = false;
         
         /**
          * Se o sistema estiver rodando em cluster, a criacao de um novo
          * PERFIL se dara na maquina que possuir a menor quantidade de 
          * usuarios. Eh importante lembrar que o perfil so sera criado 
          * desde que o mesmo nao exista (em TODOS os Nos do cluster).
          * */
         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             try
             {
	             noTmp = (No) iter.next();
	             
	             if (noTmp.getConexaoServUtil().trataPerfil(Perfil))
	             {
	                 perfilExistente = true;
	             }
	             
	             qtdUsuariosTmp = noTmp.getConexaoServUtil().getListaUsuariosCfg().size();
	             
	             if (qtdUsuariosTmp < qtdUsuarios)
	             {
	                 qtdUsuarios = qtdUsuariosTmp;
	                 no = noTmp;
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
         
         if (!perfilExistente)
         {
             if (no.getConexaoServUtil().incluiPerfil(Perfil, Acesso,false,"",true,"",true,"",true))
             {
                 Perfis.iniciaOperacao("Perfil incluído!");
             }
             else
             {
                 Perfis.iniciaOperacao("Erro ao incluir perfil!");
             }
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiPerfil - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
