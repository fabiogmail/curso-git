//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpFormEnviaMensagem.java

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

/**
 */
public class OpFormEnviaMensagem extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6FB0B8033C
    */
   public OpFormEnviaMensagem() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6FB0B8035A
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpFormEnviaMensagem - iniciaOperacao()");
      try
      {
         setOperacao("Envia Mensagem");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/enviamensagem.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "enviamensagem.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "enviamensagem.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "enviamensagem.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpFormEnviaMensagem - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C6FB0B8036E
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      int QtdElem = 0;
      PerfilCfgDef Perfil;
      UsuarioDef Usuario;
      String Args[] = new String[3];
      Vector Perfis = new Vector(), Usuarios = new Vector();
      
      No noTmp = null;

      Vector listaTemp;
      
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          
	          listaTemp = noTmp.getConexaoServUtil().getListaPerfisCfg();
	          if(listaTemp != null)
	          {
	          	Perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisCfg());
	          }
	          
	          listaTemp = noTmp.getConexaoServUtil().getListaUsuariosCfg();
	          if(listaTemp != null)
	          {
	          	Usuarios.addAll(noTmp.getConexaoServUtil().getListaUsuariosCfg());	          	
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

      Perfis.trimToSize();
      Usuarios.trimToSize();

      Args[0] = "";
      Args[1] = "";
      QtdElem = Perfis.size();
      // Monta lista com nomes dos perfis
      for (short i = 0; i < QtdElem; i++)
      {
         Perfil = (PerfilCfgDef) Perfis.elementAt(i);
         if (i == QtdElem - 1)
            Args[0] += Perfil.getPerfil();
         else
            Args[0] += Perfil.getPerfil() + ";";
      }

      QtdElem = Usuarios.size();
      // Monta lista com nomes dos perfis
      for (short i = 0; i < QtdElem; i++)
      {
         Usuario = (UsuarioDef) Usuarios.elementAt(i);
         if (i == QtdElem - 1)
            Args[1] += Usuario.getUsuario();
         else
            Args[1] += Usuario.getUsuario() + ";";
      }
      
      Args[2] = p_Mensagem;
      return Args;
   }
}
