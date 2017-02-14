//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpPerfis.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.AcessoCfgDef;
import Portal.Utils.PerfilCfgDef;

/**
 */
public class OpPerfisAntigo extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpPerfis - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C2140790048
    */
   public OpPerfisAntigo() 
   {
      //System.out.println("OpPerfis - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C21407E018F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpPerfis - iniciaOperacao()");
      try
      {
         setOperacao("Configuração de Perfis");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/perfisAntigo.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "perfis.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "perfis.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "perfis.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpPerfis - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C51B2710055
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      int QtdElem = 0;
      AcessoCfgDef Acesso;
      PerfilCfgDef Perfil;
      String Args[] = new String[5];
      Vector perfis = new Vector(20), acessos = new Vector(20);
      for (short i = 0; i < Args.length; i++)
         Args[i] = "";
      
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisCfg());
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
      acessos.addAll(NoUtil.getNo().getConexaoServUtil().getListaAcessoCfg());
      
      perfis.trimToSize();
      acessos.trimToSize();

      QtdElem = perfis.size();
      // Monta lista com nomes dos perfis
      for (short i = 0; i < QtdElem; i++)
      {
         Perfil = (PerfilCfgDef) perfis.elementAt(i);
         if (i == QtdElem - 1)
         {
            Args[0] += Perfil.getPerfil();
            Args[1] += Perfil.getId();
         }
         else
         {
            Args[0] += Perfil.getPerfil() + ";";
            Args[1] += Perfil.getId() + ";";
         }
      }

      QtdElem = acessos.size();
      // Monta lista com nomes dos tipos de acesso
      for (short i = 0; i < QtdElem; i++)
      {
         Acesso = (AcessoCfgDef) acessos.elementAt(i);
         if (i == QtdElem - 1)
            Args[2] += Acesso.getAcesso();
         else
            Args[2] += Acesso.getAcesso() + ";";
      }

      // Monta lista de relacionamentos
      for (short i = 0; i < perfis.size(); i++)
      {
         Perfil = (PerfilCfgDef) perfis.elementAt(i);
         for (short j = 0; j < acessos.size(); j++)
         {
            Acesso = (AcessoCfgDef) acessos.elementAt(j);
            if (Perfil.getAcesso() == Acesso.getId())
            {
               if (i == perfis.size() - 1)
                  Args[3] += Acesso.getAcesso();
               else
                  Args[3] += Acesso.getAcesso() + ";";
               break;
            }
         }
      }

      Args[4] = p_Mensagem;

      return Args;
   }
}
