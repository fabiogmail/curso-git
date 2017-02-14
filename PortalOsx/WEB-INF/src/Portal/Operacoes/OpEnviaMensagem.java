//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpEnviaMensagem.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 */
public class OpEnviaMensagem extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6FCABB0124
    */
   public OpEnviaMensagem() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6FCABB0142
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpEnviaMensagem - iniciaOperacao()");
      try
      {
         setOperacao("Envia Mensagem");
         // Recupera os parâmetros
         
         // String contendo os nomes dos perfis selecionados (Separados por ; qdo houver mais de 1)
         String Perfis   = m_Request.getParameter("perfis"); 
         String Usuarios = m_Request.getParameter("usuarios");
         String Assunto  = m_Request.getParameter("assunto");
         String Mensagem = m_Request.getParameter("mensagem");
         String Tipo     = m_Request.getParameter("tipo");
         UsuarioDef Origem = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         
         OpFormEnviaMensagem FormMensagem = new OpFormEnviaMensagem();
         FormMensagem.setRequestResponse(getRequest(), getResponse());

         if (Perfis.length() == 0)
            Perfis = null;
         if (Usuarios.length() == 0)
            Usuarios = null;

         No noTmp = null;
         StringBuffer mensagem = new StringBuffer(100);
         Vector perfisDoNo = new Vector();
         Vector usuariosDoNo = new Vector();
         String perfilString, usuarioString = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             try
             {
	             noTmp = (No) iter.next();

	             //////////////////Perfis///////////////
	             if(Perfis != null)
	             {
		             perfilString = noTmp.getConexaoServUtil().trataListaPerfil(Perfis);
	             	 
		             if (perfilString.indexOf(";") != -1)
		             {
		                 perfisDoNo = VetorUtil.String2Vetor(perfilString,';');
		             }
		             else if (!perfilString.equals(""))
		             {
		                 perfisDoNo.add(perfilString);
		             }
		             
		             if (perfisDoNo.size() > 0)
		             {
		                 if (noTmp.getConexaoServUtil().enviaMensagem(Origem.getUsuario(), 
		                                                              Perfis, 
		                                                              Usuarios, 
		                                                              Assunto, 
		                                                              Mensagem, 
		                                                              Short.parseShort(Tipo)))
		                 {
		                     if (NoUtil.listaDeNos.size() > 1)
		                     {
		                         mensagem.append("Servidor "+noTmp.getHostName()+"- Mensagem Enviada com Sucesso para os perfis: "+perfilString+" !\n");
		                     }
		                     else
		                     {
		                         mensagem.append("Mensagem Enviada com Sucesso!");
		                     }
		                 }
		                 else
		                 {
		                     if (NoUtil.listaDeNos.size() > 1)
		                     {
		                         mensagem.append("Servidor "+noTmp.getHostName()+"- Erro ao enviar a mensagem! ");
		                         mensagem.append("As mensagens n&atilde;o foram enviadas para os perfis: "+perfilString+" !\n");
		                     }	
		                     else
		                     {
		                         mensagem.append("Erro ao enviar a mensagem!");
		                     }
		                 }
		             }
		             
		             perfisDoNo = new Vector();
	             
	             }
	             
	             
	             //////////////////Usuarios///////////////
	             if(Usuarios != null)
	             {
	             	 usuarioString = noTmp.getConexaoServUtil().trataListaUsuario(Usuarios);
	             	
		             if (usuarioString.indexOf(";") != -1)
		             {
		                 usuariosDoNo = VetorUtil.String2Vetor(usuarioString,';');
		             }
		             else if (!usuarioString.equals(""))
		             {
		             	 usuariosDoNo.add(usuarioString);
		             }
		             
		             if(usuariosDoNo.size() > 0 )
		             {
	             	 
		                 if (noTmp.getConexaoServUtil().enviaMensagem(Origem.getUsuario(), 
	                            Perfis, 
	                            Usuarios, 
	                            Assunto, 
	                            Mensagem, 
	                            Short.parseShort(Tipo)))
		                 {
		                 	if (NoUtil.listaDeNos.size() > 1)
		                 	{
		                 		mensagem.append("Servidor "+noTmp.getHostName()+"- Mensagem Enviada com Sucesso para os usuários: "+usuarioString+" !\n");
		                 	}
		                 	else
		                 	{
		                 		mensagem.append("Mensagem Enviada com Sucesso!");
		                 	}
		                 }
		                 else
		                 {
		                 	if (NoUtil.listaDeNos.size() > 1)
		                 	{
		                 		mensagem.append("Servidor "+noTmp.getHostName()+"- Erro ao enviar a mensagem! ");
		                 		mensagem.append("As mensagens n&atilde;o foram enviadas para os usuários: "+usuarioString+" !\n");
		                 	}	
		                 	else
		                 	{
		                 		mensagem.append("Erro ao enviar a mensagem!");
		                 	}
		                 }
		                 
		             }
		             
	             }
	             
	             usuariosDoNo = new Vector();
	             
	             
	             
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
         
         FormMensagem.iniciaOperacao(mensagem.toString());

      }
      catch (Exception Exc)
      {
         System.out.println("OpEnviaMensagem - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
