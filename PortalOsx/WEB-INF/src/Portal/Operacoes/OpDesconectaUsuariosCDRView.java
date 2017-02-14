//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpDesconectaUsuariosCDRView.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Mensagem;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 */
public class OpDesconectaUsuariosCDRView extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C96447202C9
    */
   public OpDesconectaUsuariosCDRView() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C964472035F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpDesconectaUsuariosCDRView - iniciaOperacao()");
      UsuarioDef Usuario;
      Vector ListaUsuarios = null;
      String UsuariosConectados = "", Usuarios = null, Sessao = null, Remetente = null;

      try
      {
         setOperacao("Desconexão de Usuários");
         Usuarios = m_Request.getParameter("usuarios");
         //System.out.println("OpDesconectaUsuariosCDRView: "+Usuarios);

         Sessao = m_Request.getSession().getId();
         Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(Sessao);
			if (Usuario == null)
	         Remetente = "N/A";
			else
				Remetente = Usuario.getUsuario();
            
         // Converte lista de usuários a desconectar em vetor
         Vector UsuariosDesc = VetorUtil.String2Vetor(Usuarios,';');
         
         OpListaUsuariosCDRView ListaUsr = new OpListaUsuariosCDRView();
         ListaUsr.setRequestResponse(getRequest(), getResponse());
         
         UsuarioDef usrDef = null;
         No noTmp = null;

         for (int i = 0; i < UsuariosDesc.size(); i++)
         {
            Usuarios = (String)UsuariosDesc.elementAt(i);
            String nomeUsuario[] = Usuarios.split("-");
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
               noTmp = (No) iter.next();
               usrDef = noTmp.getConexaoServUtil().getUsuario(nomeUsuario[0]);
               
               if (usrDef != null)
               {
                   noTmp.getConexaoServControle().desconectaCliente(Usuarios);
               }
            }

            enviaMensagem(Remetente, Usuarios);            
            //System.out.println("OpDesconectaUsuariosCDRView - iniciaOperacao(): Usuario desconectado: "+Usuarios);
         }

         // Aguarda que ServCtrl finalize os processos ServCli e ServCfg (caso último
         // cliente do perfil) para depois listar os usuários conectados
         Thread.sleep(6000);

         // Lista os usuários que ainda estão conectados         
         ListaUsr.iniciaOperacao("$ARG;");
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpDesconectaUsuariosCDRView - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Remetente
    * @param p_Usuario
    * @return void
    * @exception 
    * @roseuid 3DC92918012E
    */
   public void enviaMensagem(String p_Remetente, String p_Usuario) 
   {
       UsuarioDef usuario = null;
       No noTmp = null;

       for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
       {
          noTmp = (No) iter.next();
          String nomeUsuario[] = p_Usuario.split("-");
          usuario = noTmp.getConexaoServUtil().getUsuario(nomeUsuario[0]);
          
          if (usuario != null)
          {
              Mensagem Msg = new Mensagem(noTmp.getConexaoServUtil());
              String DataStr = noTmp.getConexaoServUtil().getDataHoraAtual();

              Msg.setRemetente(p_Remetente);
              Msg.setUsuario(p_Usuario);
              Msg.setAssunto("Desconexao do CDRView Analise");
              if (p_Remetente.equals("N/A") == true)
              {
                 if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == false)
                   Msg.setMensagem("Você foi desconectado do CDRView Analise em " + DataStr + ".");
                 else
                   Msg.setMensagem("Você foi desconectado do SGDT em " + DataStr + ".");
              }
                 //Msg.setMensagem("Você foi desconectado do CDRView Analise.");
              else
              {
                 if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == false)
                    Msg.setMensagem("Você foi desconectado do CDRView Analise pelo usu&aacute;rio " + p_Remetente + " em " + DataStr + ".");
                 else
                    Msg.setMensagem("Você foi desconectado do SGDT pelo usu&aacute;rio " + p_Remetente + " em " + DataStr + ".");         
                 //Msg.setMensagem("Você foi desconectado do CDRView Analise pelo usu&aacute;rio " + p_Remetente + ".");
              }

              Msg.envia(); 
              break;
          }
       }
   }
}
