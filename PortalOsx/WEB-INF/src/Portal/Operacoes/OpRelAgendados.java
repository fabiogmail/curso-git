//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpRelAgendados.java

package Portal.Operacoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;
import com.sun.org.apache.bcel.internal.generic.LSTORE;
import CDRView2.Cliente;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.UsuarioDef;

public class OpRelAgendados extends OperacaoAbs 
{
   private String perfil;
   private UsuarioDef usuario=null;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C71A8510255
    */
   public OpRelAgendados() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C71A8510269
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpFormListaRelAgendados - iniciaOperacao()");
      try
      {
         setOperacao("Relatórios Agendados");      
         boolean isFraude = false;
         usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         perfil = usuario.getPerfil();
         if((usuario.getAcesso() != null)&&(usuario.getAcesso().equalsIgnoreCase("fraude") || usuario.getAcesso().equalsIgnoreCase("adminFraude")))
         {
        	 isFraude = true;
         }         
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         if(isFraude)
         {
        	 m_Args[2] = "src=\"/PortalOsx/templates/js/relagendadosFraude.js\"";
         }else
         {
        	 m_Args[2] = "src=\"/PortalOsx/templates/js/relagendados.js\"";
         }
         m_Args[3] = "onLoad=\"Processa(0)\"";
         
         if(isFraude)
         {
        	 m_Args[4] = "pesquisas_fraude.gif";
         }
         else
         {
        	 m_Args[4] = "relagendados.gif";
         }
         
         if(isFraude)
         {
        	 if((usuario.getAcesso()!= null)&&(usuario.getAcesso().equalsIgnoreCase("fraude")))
        		 m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "relagendadosFraude.form", montaFormulario(p_Mensagem));
        	 else//adminfraude
        		 m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "relagendadosAdminFraude.form", montaFormulario(p_Mensagem));
         }
         else
         {
        	 m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "relagendados.form", montaFormulario(p_Mensagem));
         }
         
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
      String Args[] = new String[7];
      String IdPerfil = new String();
      UsuarioDef Usuario   = null;
      Vector Perfis        = new Vector(20);
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
         Permissoes = Usuario.getNo().getConexaoServUtil().getPermissoesRel();
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
                  Perfil = getPerfilbyId(IdPerfil, Perfis);
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

      //Tipos de Relatorio
//      ListaTipoRel = NoUtil.getNo().getConexaoServUtil().getListaTipoRelatorios();
      ListaTipoRel = NoUtil.getNo().getConexaoServUtil().getListaTipoRelatoriosPerfil(Usuario.getPerfil());
      if(Cliente.fnCliente() == Cliente.TIM_BRASIL){
    			  ListaTipoRel.remove("0-43:DW GPRS");	
    			  ListaTipoRel.remove("0-44:DW Geral");			
    			  //ListaTipoRel.remove("0-45:Trend Analysis");	
      }    	  

      Args[3] = new String();
      for (int i=0; i < ListaTipoRel.size(); i++)
      {
         Args[3] += (String)ListaTipoRel.elementAt(i);
         Args[3] += ";";         
      }

      Args[3] = Args[3].substring(0, Args[3].length() - 1);
        
      Args[4] = p_Mensagem;
      Args[5] = perfil;//sera usado para os perfis fraudes
      Args[6] = "";//gato pra mostrar o perfil adminfraude
      
      if((usuario.getAcesso()!= null)&&(usuario.getAcesso().equalsIgnoreCase("fraude")))
      {
     	 Vector todosPerfis = noTmp.getConexaoServUtil().getListaPerfisCfg();
     	 for(int i=0; i<todosPerfis.size(); i++)
     	 {
     		 if(((PerfilCfgDef)todosPerfis.get(i)).getAcessoNome().equalsIgnoreCase("adminFraude"))
     		 {
     			Args[6] = ((PerfilCfgDef)todosPerfis.get(i)).getPerfil();
     		 }
     	 }
      }
      return Args;
   }
   
   /**
    * @param Id
    * @return PerfilCfgDef
    * @exception 
    * @roseuid 3FC5F02501D6
    */
   public PerfilCfgDef getPerfilbyId(String Id, Vector Perfis) 
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
