//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpRelHistoricos.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpRelHistoricos extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C71BD8702C1
    */
   public OpRelHistoricos()
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception
    * @roseuid 3C71BD8702D5
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      //System.out.println("OpRelHistoricos - iniciaOperacao()");
      try
      {
         setOperacao("Relatórios Históricos");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/relhistoricos.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "relhistoricos.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "relhistoricos.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "relhistoricos.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpRelHistoricos - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C71BD8702E9
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[] = new String[2];
      UsuarioDef Usuario;

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      if (Usuario.getUsuario().equals(DefsComum.s_USR_ADMIN))
      {
         Vector Perfis = new Vector(20);
         
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
         	 try
			 {
	             noTmp = (No) iter.next();
	             Perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisCfg());
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
         
         PerfilCfgDef Perfil;
         int QtdElem = Perfis.size();
         // Monta lista com nomes dos perfis
         Args[0] = "";
         for (int i = 0; i < QtdElem; i++)
         {
            Perfil = (PerfilCfgDef) Perfis.elementAt(i);
            if (i == QtdElem - 1)
               Args[0] += Perfil.getPerfil();
            else
               Args[0] += Perfil.getPerfil() + ";";
         }
      }
      else
         Args[0] = Usuario.getPerfil();
         
      Args[1] = p_Mensagem;
      return Args;   
   }
}
