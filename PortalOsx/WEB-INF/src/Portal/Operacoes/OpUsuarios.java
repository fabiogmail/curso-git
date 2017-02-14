//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpUsuarios.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.UsuarioDef;

public class OpUsuarios extends OperacaoAbs 
{ 
   
   static 
   {
      //System.out.println("OpUsuarios - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C35E57C0265
    */
   public OpUsuarios() 
   {
      //System.out.println("construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C35E58702CF
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpUsuarios - iniciaOperacao()");

      try
      {
    	 setOperacao("Configuração de Usuários");
    	 iniciaArgs(7);
    	 m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/usuarios.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "usuarios.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "usuarios.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "usuarios.txt", null);
         m_Html.enviaArquivo(m_Args); 
         return true;
      }
      catch (Exception ExcLogAcesso)
      {
         System.out.println("OpUsuarios - iniciaOperacao(): "+ExcLogAcesso);
         ExcLogAcesso.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception
    * @roseuid 3C540F370162
    */
   public String[] montaFormulario(String p_Mensagem)
   {
      int QtdElem = 0;
      String Args[] = new String[5];;
      UsuarioDef Usuario;
      PerfilCfgDef Perfil;
      Vector Usuarios = new Vector(20), Perfis = new Vector(20);

      for (short i = 0; i < Args.length; i++)
         Args[i] = "";

      No noTmp = null;
      Vector vectorTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          noTmp = (No) iter.next();
          try
          {
              vectorTmp = noTmp.getConexaoServUtil().getListaPerfisCfg();
              if (vectorTmp != null) 
                  Perfis.addAll(vectorTmp);
              
              vectorTmp = noTmp.getConexaoServUtil().getListaUsuariosCfg();
              if (vectorTmp != null)
                  Usuarios.addAll(vectorTmp);    
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
      
      Perfis.trimToSize();
      Usuarios.trimToSize();

      if (Usuarios != null)
      {
         QtdElem = Usuarios.size();
         // Monta lista com nomes dos usuários
         for (short i = 0; i < QtdElem; i++)
         {
            Usuario = (UsuarioDef) Usuarios.elementAt(i);
            if (i == QtdElem - 1)
            {
               Args[0] += Usuario.getUsuario();
               Args[1] += Usuario.getIdUsuario();
            }
            else
            {
               Args[0] += Usuario.getUsuario() + ";";
               Args[1] += Usuario.getIdUsuario() + ";";
            }
         }
      }

      if (Perfis != null)
      {
         QtdElem = Perfis.size();
         // Monta lista com nomes dos tipos de acesso
         for (short i = 0; i < QtdElem; i++)
         {
            Perfil = (PerfilCfgDef) Perfis.elementAt(i);
            if (i == QtdElem - 1)
               Args[2] += Perfil.getPerfil();
            else
               Args[2] += Perfil.getPerfil() + ";";
         }
      }

      if (Usuarios != null)
      {
         // Monta lista de relacionamentos
         for (short i = 0; i < Usuarios.size(); i++)
         {
            Usuario = (UsuarioDef) Usuarios.elementAt(i);
            if (i == Usuarios.size() - 1)
               Args[3] += Usuario.getPerfil();
            else
               Args[3] += Usuario.getPerfil() + ";";
         }
      }

      Args[4] = p_Mensagem;

      return Args;
   }
}
