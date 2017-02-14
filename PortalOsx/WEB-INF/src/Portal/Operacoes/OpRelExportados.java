//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpRelAgendados.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.UsuarioDef;

/**
 * 
 * Visent 
 * Projeto PortalOsx 
 * @author Erick Rodrigo
 * @versao 1.0
 * data:16/08/2007 11:51:56
 */
public class OpRelExportados extends OperacaoAbs 
{ 
   private String perfil;
   private UsuarioDef usuario=null;
   private Vector Perfis = new Vector();
   static 
   {
   }
   

   public OpRelExportados() 
   {
   }
   

   public boolean iniciaOperacao(String p_Mensagem) 
   {

      try
      {
         setOperacao("Relatórios Agendados");
         usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         perfil = usuario.getPerfil();         
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/relexportados.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "listarelexportados.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "relexportados.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "relagendados.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpFormListaRelAgendados - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C71A8510287
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[] = new String[4];
      String IdPerfil = new String();
      UsuarioDef Usuario   = null;
      Vector Permissoes    = null;
      PerfilCfgDef Perfil  = null;
      Vector PerfisPermitidos = new Vector();
      Vector ListaTipoRel = null;

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          Perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisOtimizado());
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

      int QtdElem = Perfis.size();

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());     
      Args[0] = new String();
      Args[1] = new String();
      if (Usuario.getUsuario().equals(DefsComum.s_USR_ADMIN))
      {
         // Monta lista com nomes de todos perfis
         for (int i = 0; i < QtdElem; i++)
         {
            Perfil = (PerfilCfgDef) Perfis.elementAt(i);
            Args[0] += Perfil.getPerfil() + ";";
            Args[1] += "" + Perfil.getId() + ";";
         }
      }
      else
      {
         Args[0] += Usuario.getPerfil() + ";";
         Args[1] += ""+ Usuario.getIdPerfil() + ";"; 
         Args[2] = new String();

         // Monta a lista com nomes dos perfis permitidos para o usuario
         Permissoes = Usuario.getNo().getConexaoServUtil().getPermissoesBasesExportadas();
         QtdElem = Permissoes.size();         

         for (int i=0; i<QtdElem; i++)
         {
            StringTokenizer st = new StringTokenizer((String)Permissoes.elementAt(i), ":");
            if ( ((String)st.nextElement()).startsWith(""+Usuario.getIdPerfil()))
            {
               StringTokenizer st2 = new StringTokenizer((String)st.nextElement(), ";" );
               while (st2.hasMoreTokens())
               {
                  // procurando o nome do perfil
                  IdPerfil = (String)st2.nextElement();
                  Perfil = getPerfilbyId(IdPerfil);
                  if (Perfil == null)
                  {
                     System.out.println("------------------------------------------------\n"
                                      + "Perfil " + IdPerfil + " nao encontrado.         \n"
                                      + "Arquivo PermissoesRel.txt pode estar corrompido.\n"
                                      + "------------------------------------------------\n");
                     continue;
                  }
                  if (PerfisPermitidos.contains(IdPerfil))                  
                     continue;
                  else
                     PerfisPermitidos.addElement(IdPerfil);

                  // montando os argumentos
                  Args[0] += Perfil.getPerfil() + ";";
                  Args[1] += ""+ Perfil.getId() + ";"; 
               }              
               Args[2] += (String) Permissoes.elementAt(i);
               Args[2] += "@";
            }
         }
         
         // Removendo o ultimo ";"
         if (Args[2].length() > 0)
            Args[2] = Args[2].substring(0, Args[2].length() - 1);         

      }

      // Removendo o ultimo ";"
      Args[0] = Args[0].substring(0, Args[0].length() - 1);
      Args[1] = Args[1].substring(0, Args[1].length() - 1);
      Args[3] = p_Mensagem;
      
      return Args;
   }
   
   /**
    * @param Id
    * @return PerfilCfgDef
    * @exception 
    * @roseuid 3FC5F02501D6
    */
   public PerfilCfgDef getPerfilbyId(String Id) 
   {
      PerfilCfgDef Perfil = null;
      for (int j=0; j<Perfis.size(); j++)
      {
         Perfil = (PerfilCfgDef)Perfis.elementAt(j);
         if (Id.equals(""+Perfil.getId()))
            return Perfil;         
      }

      return null;
   }
}
