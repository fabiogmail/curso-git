//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpBloqueiaPerfil.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iPerfil;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpBloqueiaPerfil extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpBloqueiaPerfil - Carregando classe");
   }

   /**
    * @return
    * @exception
    * @roseuid 3C3CE4140370
    */
   public OpBloqueiaPerfil()
   {
      //System.out.println("OpBloqueiaPerfil - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C3CE41F02F4
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpBloqueiaPerfil - iniciaOperacao()");
      String PerfisBloq = null, PerfilBloq = null, Perfil = null, Op = null ;
      try
      {
         setOperacao("Bloqueio de Perfis");

         // Recupera os parâmetros
         PerfisBloq = m_Request.getParameter("perfis");

         OpListaPerfisCfg Perfis = new OpListaPerfisCfg();
         Perfis.setRequestResponse(getRequest(), getResponse());

         // Exemplo de PerfisBloq/parser:
         // 1) PerfisBloq: manutencao-b;planejamento-d
         // 2) PerfilBloq: manutencao-b
         // 3) Perfil: manutencao  Op: b
         //    ...
         // 4) Perfil: planejamento  Op: d
         while (PerfisBloq != null)
         {
            if (PerfisBloq.length() == 0)
               PerfisBloq = null;
            else if (PerfisBloq.indexOf(';') != -1)
            {
               PerfilBloq = PerfisBloq.substring(0, PerfisBloq.indexOf(';'));
               Perfil = PerfilBloq.substring(0, PerfilBloq.indexOf('-'));
               Op     = PerfilBloq.substring(PerfilBloq.indexOf('-')+1, PerfilBloq.length());

               PerfisBloq = PerfisBloq.substring(PerfisBloq.indexOf(';')+1, PerfisBloq.length());
               
               No noTmp = null;

               for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
               {
                   try
                   {
	                   noTmp = (No) iter.next();
	                   
	                   iPerfil perfilServ = noTmp.getConexaoServUtil().getM_iListaPerfis().fnPerfilExiste(Perfil);
	                   
	                   if (perfilServ != null)
	                   {
	                       noTmp.getConexaoServUtil().bloqueiaPerfil(Perfil, Op);
	                       break;
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
            else  // Último/Único perfil
            {
               Perfil = PerfisBloq.substring(0, PerfisBloq.indexOf('-'));
               Op     = PerfisBloq.substring(PerfisBloq.indexOf('-')+1, PerfisBloq.length());
               
               No noTmp = null;

               for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
               {
                   noTmp = (No) iter.next();
                   
                   iPerfil perfilServ = noTmp.getConexaoServUtil().getM_iListaPerfis().fnPerfilExiste(Perfil);
                   
                   if (perfilServ != null)
                   {
                       noTmp.getConexaoServUtil().bloqueiaPerfil(Perfil, Op);
                       break;
                   }
               }
               PerfisBloq = null;
            }
         }

         Perfis.setMensagem(Perfil);
         Perfis.iniciaOperacao("Operação concluída!");
      }
      catch (Exception Exc)
      {
         System.out.println("OpBloqueiaPerfil - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
