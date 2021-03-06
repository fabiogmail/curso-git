//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpMenuAdministrador.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.GeradorDeMenu;
import Portal.Utils.UsuarioDef;

/**
 * Classe respons�vel por enviar a p�gina de navaga��o do usu�rio administrador.
 */
public class OpMenuAdministrador extends OperacaoAbs 
{ 
   
   static 
   {
      //System.out.println("OpMenuAdministrador - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C2F038903A3
    */
   public OpMenuAdministrador() 
   {
      //System.out.println("OpMenuAdministrador - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C2F038903B7
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         GeradorDeMenu GeraMenu=null;
         String NomeMenu = "oCMenu", ArqMenu = "menu_a";

         setOperacao("Menu Administrador");

         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());

         ArqMenu += Usuario.getAcessoId() + "_1.txt";
         if(DefsComum.sSUB_CLIENTE != null){
        	 if(DefsComum.sSUB_CLIENTE.length() > 0 ){
        	 GeraMenu = new GeradorDeMenu(NomeMenu, NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_MENUS() + DefsComum.sSUB_CLIENTE.toLowerCase()+"/", ArqMenu);
        	 }
         }
        	 else{
         GeraMenu = new GeradorDeMenu(NomeMenu, NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_MENUS() + DefsComum.s_CLIENTE.toLowerCase()+"/", ArqMenu);
        	 }
         GeraMenu.geraMenu();

         iniciaArgs(11);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "topo_tpl_1.htm";
         m_Args[2] = "especial";
         m_Args[3] = Usuario.getUsuario();
         m_Args[4] = Usuario.getUsuario();
         m_Args[5] = Usuario.getPerfil();
         m_Args[6] = Usuario.getUsuario();
         m_Args[7] = "menuUsuario";
         m_Args[8] = Usuario.getUsuario();
         
         m_Args[9] = DefsComum.s_CLIENTE.toLowerCase();
         m_Args[10] = "Admin";
         m_Html.enviaArquivo(m_Args);         

         iniciaArgs(91);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "topo_tpl_menu.htm";
         for (int i = 2; i <= 90; i++)
            m_Args[i] = NomeMenu;
         m_Html.enviaArquivo(m_Args);

         // Recupera menu gerado
         m_Html.getSaidaHtml().print(GeraMenu.getMenuGerado());

         iniciaArgs(5);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
         m_Args[1] = "topo_tpl_2.htm";
         m_Args[2] = m_Args[3] = NomeMenu;
         m_Args[4] += "          <td width=\"74\">\n";
         m_Args[4] += "            <table width=\"67\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n";
         m_Args[4] += "              <tr bgcolor=\"FFDB48\">\n";
         m_Args[4] += "                <td height=\"20\">\n";
         m_Args[4] += "                  <div align=\"center\"><b>&nbsp;<a href=\"javascript:SetaFlag()\" onmouseover=\"window.status='Alterna para menu de usu�rio';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Usu&aacute;rio</a>&gt;&gt;&nbsp;&nbsp;</b></div>\n";
         m_Args[4] += "                </td>\n";
         m_Args[4] += "              </tr>\n";
         m_Args[4] += "            </table>";          
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception ExcLogout)
      {
         System.out.println("OpMenuAdministrador - iniciaOperacao(): "+ExcLogout);
         ExcLogout.printStackTrace();
      }

      return true;
   }
}
