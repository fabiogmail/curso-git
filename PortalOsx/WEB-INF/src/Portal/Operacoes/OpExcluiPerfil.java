//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpExcluiPerfil.java

package Portal.Operacoes;

import java.util.Date;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iPerfil;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;


public class OpExcluiPerfil extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpExcluiPerfil - Carregando classe");
   }

   /**
    * @return
    * @exception
    * @roseuid 3C337E160198
    */
   public OpExcluiPerfil()
   {
      //System.out.println("OpExcluiPerfil - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C337E220203
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExcluiPerfil - iniciaOperacao()");
      try
      {
         setOperacao("Exclusão de Perfil");

         // Recupera os parâmetros
         String Perfil   = m_Request.getParameter("perfil");
         String IdPerfil = m_Request.getParameter("ID");

         OpPerfis Perfis = null;
         OpPerfisAntigo perfisAntigo = null;
         boolean isOld = false;
         if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro") || DefsComum.s_CLIENTE.equalsIgnoreCase("BrasilTelecom")
        		 || DefsComum.s_CLIENTE.equalsIgnoreCase("Sercomtel")
        		 || DefsComum.s_CLIENTE.equalsIgnoreCase("TimSul") || DefsComum.s_CLIENTE.equalsIgnoreCase("Amazonia_Celular")
        		 || DefsComum.s_CLIENTE.equalsIgnoreCase("Embratel")|| DefsComum.s_CLIENTE.equalsIgnoreCase("Telemig") 
        		 || DefsComum.s_CLIENTE.equalsIgnoreCase("Tim") || DefsComum.s_CLIENTE.equalsIgnoreCase("Oi")){
        	 Perfis = new OpPerfis();
        	 Perfis.setRequestResponse(getRequest(), getResponse());        	
         }else{
        	 isOld = true;
        	 perfisAntigo = new OpPerfisAntigo();
        	 perfisAntigo.setRequestResponse(getRequest(), getResponse());
         }
         
         
         No noTmp = NoUtil.buscaNobyNomePerfil(Perfil);
         iPerfil perfilSelecionado = null;
         

         try
         {
             perfilSelecionado = noTmp.getConexaoServUtil().getM_iListaPerfis().fnPerfilExiste(Perfil);
             
             if (perfilSelecionado != null)
             {
                 if (noTmp.getConexaoServUtil().excluiPerfil(Perfil, IdPerfil))
                 {
                	 if(isOld)
                	 {
                		 perfisAntigo.iniciaOperacao("Perfil excluído!");
                	 }
                	 else
                	 {
                		 Perfis.iniciaOperacao("Perfil excluído!");
                	 }
                 }
                 else
                 {
                	 if(isOld)
                	 {
                		 perfisAntigo.iniciaOperacao("Erro ao excluir perfil!");
                	 }
                	 else
                	 {
                		 Perfis.iniciaOperacao("Erro ao excluir perfil!");
                	 }                     
                 }
                 
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
         System.out.println("OpExcluiPerfil - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
