//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpDesconectaUsuarios.java

package Portal.Operacoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.Mensagem;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;


/**
 */
public class OpDesconectaUsuarios extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpDesconectaUsuarios - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C3F280A0037
    */
   public OpDesconectaUsuarios() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C3F27EF02EB
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpDesconectaUsuarios - iniciaOperacao()");
      UsuarioDef Usuario = null;
      String Usuarios = null, IdSessao = null;
      String Sessao = null, Remetente = null;
      Map.Entry Ent;
      Iterator It;
      Object Obj;
      Map mapSessaoNo = new HashMap();

      try
      {
         setOperacao("Desconexão de Usuários");
         Usuarios = m_Request.getParameter("usuarios");

         // Converte lista de usuários a desconectar em vetor
         Vector listaIDSessaoUsrSelecionados = VetorUtil.String2Vetor(Usuarios,';');
         
         Sessao = m_Request.getSession().getId();
         Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(Sessao);
         if (Usuario != null)
            Remetente = Usuario.getUsuario();
         else
            Remetente = "N/A";

         for (int i = 0; i < listaIDSessaoUsrSelecionados.size(); i++)
         {
            IdSessao = (String)listaIDSessaoUsrSelecionados.elementAt(i);
            Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(IdSessao);
            
            if (Usuario == null)  // Não encontrou o usuario pois o parametro de busca
                                  // não é IDSessao, e sim, o nome do usuário
            {
                Map usuariosLogados = NoUtil.atualizaMapUsuariosLogados();
                
            	synchronized(usuariosLogados)
				{
	               It = usuariosLogados.entrySet().iterator();
	               while (It.hasNext())
	               {
	                  Ent = (Map.Entry)It.next();
	                  Obj = Ent.getValue();
	                  Usuario = (UsuarioDef)Obj;
	                  if (Usuario.getUsuario().compareToIgnoreCase(IdSessao.startsWith("USR:") == true ? IdSessao.substring(4, IdSessao.length()) : IdSessao) == 0)
	                  {
	                     IdSessao = Usuario.getIDSessao();
	                     break;
	                  }
	                  else Usuario = null;
	               }
				}
            }

            if (Usuario != null)
            {
               try
               {
                  No noUsuario = NoUtil.buscaNobyNomeUsuario(Usuario.getUsuario());
                  
                  if (NoUtil.getNo().getHostName().equals(noUsuario.getHostName()) && NoUtil.getNo().getPorta().equals(noUsuario.getPorta()))
                  {
                      Usuario.getSessaoHTTP().invalidate();
                  }
//                  else
//                  {
//                      separaUsuariosPorNo(Usuario);
//                  }
                  
                  Usuario.getNo().getConexaoServUtil().logout(Usuario, noUsuario.getIp(), noUsuario.getHostName());
                  Usuario.getNo().getUsuarioLogados().remove(IdSessao);
                  
               }
               catch (Exception Exc)
               {
                  Exc.printStackTrace();                  
				      System.out.println("OpDesconectaUsuarios - Usuario ja invalidado(): "+Exc);
               }

               
               enviaMensagem(Remetente, Usuario.getUsuario());               
               System.out.println("OpDesconectaUsuarios - iniciaOperacao(): Usuario desconectado: "+Usuario.getUsuario());
            }
            else
            {
               System.out.println("OpDesconectaUsuarios - iniciaOperacao(): Usuario a desconectar nao encontrado");
            }
         }
         
//         if (NoUtil.isAmbienteEmCluster())
//         {
//             m_Request.setAttribute("mapSessaoNo", mapSessaoNo);
//             
//         }

         if (Usuarios.startsWith("USR:") == false)
         {
            OpListaUsuarios ListaUsr = new OpListaUsuarios();
            ListaUsr.setRequestResponse(getRequest(), getResponse());
            ListaUsr.iniciaOperacao("$ARG;");
         }
         else
         {
            OpLogon Logon = new OpLogon();
            Logon.setRequestResponse(getRequest(), getResponse());
            Logon.iniciaOperacao(Usuario.getUsuario() + "-" +Usuario.getSenha());
         }

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpDesconectaUsuarios - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   
   /**
    * Metodo utilizado para separar os usuarios por No. Utilizado somente em ambientes 
    * que forem rodar em cluster.
    * 
    * @param listaIDSessaoUsrSelecionados
    * @return Map que contem como chave o HostName do No e valor uma Lista com os 
    * Usuarios de cada No.
    */
   private void separaUsuariosPorNo(UsuarioDef usuario)
   {
       
       Map mapSessaoNo = new HashMap();
       No noTmp = null;
       String idSessaoUsuario = null;
       String key = null;
       List listaUsuarios = null;
       
       // Percorre cada No da lista para fazer a separacao de Usuarios por No.
       for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
       {
           try
           {
	          noTmp = (No) iter.next();
	          
	          idSessaoUsuario = usuario.getIDSessao();
	          
	          usuario = noTmp.getConexaoServUtil().getUsuarioSessao(idSessaoUsuario);
	          
	          if (usuario != null)
	          {
	              key = (String) mapSessaoNo.get(noTmp.getHostName());
	              
	              if (key == null)
	              {
	                  mapSessaoNo.put(noTmp.getHostName(), new ArrayList());
	                  listaUsuarios = (List) mapSessaoNo.get(noTmp.getHostName());
	                  listaUsuarios.add(usuario);
	              }
	              else
	              {
	                  listaUsuarios = (List) mapSessaoNo.get(noTmp.getHostName());
	                  listaUsuarios.add(usuario);
	              }
	              
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
   
   /**
    * @param p_Remetente
    * @param p_Usuario
    * @return void
    * @exception 
    * @roseuid 3DC914C8014F
    */
   public void enviaMensagem(String p_Remetente, String p_Usuario)
   {
       
      UsuarioDef usuario = null;
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	         noTmp = (No) iter.next();
	         usuario = noTmp.getConexaoServUtil().getUsuario(p_Usuario);
	         
	         if (usuario != null)
	         {
	             Mensagem Msg = new Mensagem(noTmp.getConexaoServUtil());
	             String DataStr = noTmp.getConexaoServUtil().getDataHoraAtual();
	
	             Msg.setRemetente(p_Remetente);
	             Msg.setUsuario(p_Usuario);
	             Msg.setAssunto("Desconexao do Portal");
	             if (p_Remetente.equals("N/A") == true)
	                Msg.setMensagem("Você foi desconectado do CDRView Web em " + DataStr + ".");
	                //Msg.setMensagem("Você foi desconectado do Portal.");
	             else
	                Msg.setMensagem("Você foi desconectado do CDRView Web pelo usu&aacute;rio " + p_Remetente + " em " + DataStr + ".");
	                //Msg.setMensagem("Você foi desconectado do Portal pelo usu&aacute;rio " + p_Remetente + ".");
	             Msg.envia();
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
}
