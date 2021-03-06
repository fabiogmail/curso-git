//Source file: C:/usr/OSx/CDRView/Servlet/Portal/Operacoes/OpMenuGerente.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpMenuGerente extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D8A05EE020B
    */
   public OpMenuGerente() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D8A05EE0215
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Menu Gerente Usuario");
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());

         iniciaArgs(3);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS()+DefsComum.s_CLIENTE.toLowerCase()+"/";
         m_Args[1] = "topo_"+DefsComum.s_CLIENTE.toLowerCase()+"_Gerente"+".htm";
         m_Args[2] = Usuario.getUsuario();
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception ExcLogout)
      {
         System.out.println("OpMenuGerente - iniciaOperacao(): "+ExcLogout);
         ExcLogout.printStackTrace();
      }
      return true;
   }
}
