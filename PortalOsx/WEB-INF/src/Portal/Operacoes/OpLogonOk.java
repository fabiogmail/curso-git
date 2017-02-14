//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpLogonOk.java

package Portal.Operacoes;

import CDRView2.Cliente;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.GeradorDeMenu;
import Portal.Utils.UsuarioDef;

/**
 * Envia a página de navegação contendo as informações do último usuário que se logou.
 */
public class OpLogonOk extends OperacaoAbs 
{ 
   
   static 
   {
      //System.out.println("OpLogonOk - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C2DD4560243
    */
   public OpLogonOk() 
   {
      //System.out.println("OpLogonOk - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C2DD45E0077
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      UsuarioDef UltimoUsuario = null;
      try
      {
         setOperacao("Menu Principal");

         UltimoUsuario = (UsuarioDef)NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(m_Request.getSession().getId());
         if (UltimoUsuario != null)
         {
        	 //Esta variavel conta a quantidade de abertura do portal sendo sendo zerada a cada abertura
        	 OpCDRViewCliente2.qtdAberturas = 0;
			 
			 //Esta variavel conta a quantidade de abertura do portal sendo sendo zerada a cada abertura para o jeito novo que funciona no chrome
        	 OpCDRViewCliente3.qtdAberturas = 0;

            // Envia a página de acordo com o tipo do usuário: administrador/comum
            GeradorDeMenu GeraMenu = null;
            String NomeMenu = "ocMenu", ArqMenu = "menu_a";

            iniciaArgs(11);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
            m_Args[1] = "topo_tpl_1.htm";

            if (UltimoUsuario.getUsuario().equals(DefsComum.s_USR_ADMIN))
            {
               ArqMenu += UltimoUsuario.getAcessoId() + "_1.txt"; 
               if(DefsComum.sSUB_CLIENTE != null){          	   
            	   if(DefsComum.sSUB_CLIENTE.length() > 0){
            		   GeraMenu = new GeradorDeMenu(NomeMenu, NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_MENUS() + DefsComum.sSUB_CLIENTE.toLowerCase()+"/", ArqMenu);
            	   }
            	  
               }
               else{
               GeraMenu = new GeradorDeMenu(NomeMenu, NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_MENUS() + DefsComum.s_CLIENTE.toLowerCase()+"/", ArqMenu);
               }
               GeraMenu.geraMenu();

               m_Args[2] = "especial";
               
               m_Args[3] = UltimoUsuario.getUsuario();
               m_Args[4] = UltimoUsuario.getUsuario();
               m_Args[5] = UltimoUsuario.getPerfil();
               m_Args[6] = UltimoUsuario.getUsuario();
               m_Args[7] = "menuUsuario";
               m_Args[8] = UltimoUsuario.getUsuario();
          
               if(DefsComum.sSUB_CLIENTE != null){          	   
            	   if(DefsComum.sSUB_CLIENTE.length() > 0){
            		   m_Args[9] = DefsComum.sSUB_CLIENTE.toLowerCase();
            	   }
               }
               else            	  
            	   m_Args[9] = DefsComum.s_CLIENTE.toLowerCase();
               m_Args[10] = "Admin";
            }
            else
            {
               ArqMenu += UltimoUsuario.getAcessoId() + ".txt";            
               if(DefsComum.sSUB_CLIENTE != null){
            	   if(DefsComum.sSUB_CLIENTE.length() > 0){
            		   GeraMenu = new GeradorDeMenu(NomeMenu, NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_MENUS() + DefsComum.sSUB_CLIENTE.toLowerCase()+"/", ArqMenu);
            	   }
               }	   
               else{
               GeraMenu = new GeradorDeMenu(NomeMenu, NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_MENUS() + DefsComum.s_CLIENTE.toLowerCase()+"/", ArqMenu);
               }
               GeraMenu.geraMenu();

               m_Args[2] = "comum";
               m_Args[3] = UltimoUsuario.getUsuario();
               m_Args[4] = UltimoUsuario.getUsuario();
               m_Args[5] = UltimoUsuario.getPerfil();
               m_Args[6] = UltimoUsuario.getUsuario();
               m_Args[7] = "";
               m_Args[8] = UltimoUsuario.getUsuario();
               if(DefsComum.sSUB_CLIENTE != null){
            	   if(DefsComum.sSUB_CLIENTE.length() > 0){
            		   m_Args[9] = DefsComum.sSUB_CLIENTE.toLowerCase();
            	   }
               }
               else            	  
            	   m_Args[9] = DefsComum.s_CLIENTE.toLowerCase();
               if (UltimoUsuario.getPerfil().toLowerCase().equals("gerplataforma") ||
                   UltimoUsuario.getPerfil().toLowerCase().equals("gerusuario"))
                  m_Args[10] = "Admin";
               else
                  m_Args[10] = "Usu&aacute;rio";
            }
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

            if (UltimoUsuario.getUsuario().equals(DefsComum.s_USR_ADMIN))
            {
               m_Args[4] += "          <td width=\"74\">\n";
               m_Args[4] += "            <table width=\"67\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n";
               m_Args[4] += "              <tr bgcolor=\"FFDB48\">\n";
               m_Args[4] += "                <td height=\"20\">\n";
               m_Args[4] += "                  <div align=\"center\"><b>&nbsp;<a href=\"javascript:SetaFlag()\" onmouseover=\"window.status='Alterna para menu de usuário';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Usu&aacute;rio</a>&gt;&gt;&nbsp;&nbsp;</b></div>\n";
               m_Args[4] += "                </td>\n";
               m_Args[4] += "              </tr>\n";
               m_Args[4] += "            </table>";
            }
            else
            {
               m_Args[4] = "";
            }
            m_Html.enviaArquivo(m_Args);            
         }
         else
         {
            // #######
            // # Erro !!! Não deve passar por aqui!!
            // #######
            System.out.println("OpLogonOk - iniciaOperacao(): ERRO!!!");            
         }
      }
      catch (Exception ExcLogout)
      {
         System.out.println("OpLogonOk - iniciaOperacao(): "+ExcLogout);
         ExcLogout.printStackTrace();
      }

      return true;
   }
}
