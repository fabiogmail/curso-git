//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpOpcoesUsuarios.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;

/**
 */
public class OpOpcoesUsuarios extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpOpcoesUsuarios - Carregando classe");
   }

   /**
    * @return
    * @exception
    * @roseuid 3C3D9EFE0102
    */
   public OpOpcoesUsuarios()
   {
      //System.out.println("OpOpcoesUsuarios - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C3D9F2103C0
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpOpcoesUsuarios - iniciaOperacao()");
      try
      {
         setOperacao("Opções de Configuração");

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/opcoesusuarios.js\"";
         m_Args[3] = "onLoad=\"Processa()\"";
         m_Args[4] = "opcoesusuarios.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "opcoesusuarios.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "opcoesusuarios.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;         
      }
      catch (Exception Exc)
      {
         System.out.println("OpOpcoesUsuarios - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C54A7160365
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Opcao, Args[] = new String[4];
      Vector Opcoes = new Vector();

      for (int i = 0; i < Args.length; i++)
         Args[i] = "";
         
      Opcao = m_Request.getParameter("opcao");

      if (Opcao.equals("salvar"))
      {
         //String Multisessao = m_Request.getParameter("multi");
    	 String Multisessao = "0";//quer dizer não
         String QtdMax = m_Request.getParameter("qtdmax");
         String NivelLog = m_Request.getParameter("nivellog");
         
         

	     NoUtil.getNo().getConexaoServUtil().setOpcoes(Short.parseShort(Multisessao), Short.parseShort(QtdMax), Short.parseShort(NivelLog));

         
         Args[0] = "Opções salvas!";
      }
      else
         Args[0] = "$ARG;";
      

      Opcoes.addAll(NoUtil.getNo().getConexaoServUtil().getOpcoes());


      for (short i = 0; i < Opcoes.size(); i++)
         Args[i+1] = (String)Opcoes.elementAt(i);

      return Args;
   }
}
