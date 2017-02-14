//Source file: C:/usr/OSx/CDRView/Servlet/Portal/Operacoes/OpStatusAgn.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iArqView;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpStatusAgn extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D59643E039F
    */
   public OpStatusAgn() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D5964390080
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      try
      {
         iArqView ArqStatus = null;
         boolean bNaoSai = true,  bApresenta = false;
         int Cont = 0, Pos1, Pos2;
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         String Linha = null, Perfil, PerfilArq,
                Controle = "<b><font color=\"#0044FF\">Perfil:</font></b>&nbsp;";
         String Tipo = m_Request.getParameter("tipo");

         setOperacao("Status da Agenda");

         Perfil = Usuario.getPerfil();
         if (Perfil.equals("admin"))
            bApresenta = true;

         m_Html.enviaInicio("", "", null);
         m_Html.envia("<body bgcolor=\"#FFFFFF\" style=\"margin:0px\">\n");
         m_Html.envia("<table width=\"780\" height=\"383\" cellpadding=\"0\" cellspacing=\"0\" background=\"/PortalOsx/imagens/fundo.gif\">\n");
         
         m_Html.envia("<tr>\n");
         m_Html.envia("<td width=\"13\">&nbsp;</td>\n");
         m_Html.envia("<td width=\"579\" valign=\"top\">\n");
         m_Html.envia("<table width=\"546\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
         m_Html.envia("<tr bgcolor=\"#000033\">\n");
         m_Html.envia("<td align=\"left\" height=\"19\"><font color=\"#FFFFFF\"><b>&nbsp;Informa&ccedil;&otilde;es</b></font></a></td>\n");
         m_Html.envia("</tr>\n");
         
         
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             noTmp = (No) iter.next();
             
             //inicializando, para verificar em cada loop sem que o antigo nó afete no proxim
             bNaoSai = true;
             
             //m_Html.envia("<b><font color=\"#0044FF\">"+noTmp.getHostName()+":</font></b><br>\n");
             
	         if (Tipo.equals("Agenda"))
	             /* bRetorno = */ ArqStatus = noTmp.getConexaoServUtil().getStatusAgenda((short)0);
	         else if (Tipo.equals("Historico"))
	             /* bRetorno = */ ArqStatus = noTmp.getConexaoServUtil().getStatusAgenda((short)1);
	         
	         if (ArqStatus != null)
	         {
	            do
	            {
	               //Linha = m_ConexUtil.getLinhaArq();
	               Linha = ArqStatus.fnGetLinha();
	               if (Linha.length() == 0)
	               {
	                  bNaoSai = false;
	               }
	               else
	               {
	                  if (Perfil.equals("admin") == false)
	                  {
	                     //
	                     // Formato da linha que contém o perfil:
	                     // <b><font color="#0044FF">Perfil:</font></b>&nbsp;P E R F I L<br>
	                     //
	                     Pos1 = Linha.indexOf(Controle);
	                     if (Pos1 != -1)
	                     {
	                        Pos2 = Linha.indexOf("<br>");
	                        PerfilArq = Linha.substring(Pos1+Controle.length(), Pos2);
	
	                        if (Perfil.equals(PerfilArq) == true)
	                           bApresenta = true;
	                        else
	                           bApresenta = false;
	                     }
	                  }
	
	                  if (bApresenta)
	                  {
	                     if (Cont%2 == 0)
	                        m_Html.envia("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
	                     else
	                        m_Html.envia("<tr bgcolor=\"#FFFFFF\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
	                     Cont++;
	                  }
	               }
	            } while (bNaoSai);
	           /*m_ConexUtil.destroiArq();*/
	            
	            ArqStatus.fnDestroy();
	
	            if (Cont == 0)
	               m_Html.envia("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">Não há entrada(s) para "+Tipo+" para o perfil <b>"+Perfil+"</b></td>\n</tr>\n");
	         }
	         else
	            m_Html.envia("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">"+Tipo+": N&atilde;o foi poss&iacute;vel recuperar as informa&ccedil;&otilde;es.</td>\n</tr>\n");
         }
         m_Html.envia("<tr>\n");
         m_Html.envia("<td></td>\n");
         m_Html.envia("</tr>\n");
         m_Html.envia("</table>\n");
         m_Html.envia("</td>\n");
         if (Tipo.equals("Agenda")){// mensagem de ajuda	         
	         m_Html.envia("<td width=\"142\" valign=\"top\">\n");
	         m_Html.envia("<table width=\"100\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
	         m_Html.envia("<tr>\n");
	         m_Html.envia("<td><img src=\"/PortalOsx/imagens/dicasDeUtilizacao.gif\" width=\"141\" height=\"19\"></td>\n");
	         m_Html.envia("</tr>\n");
	         m_Html.envia("<tr>\n");
	         m_Html.envia("<td>&nbsp;</td>\n");
	         m_Html.envia("</tr>\n");
	         m_Html.envia("<tr>\n");
	         m_Html.envia("<td>\n");
	         m_Html.envia("<p>Apresenta todas as agendas configuradas no CDRView an&aacute;lise, com status em execu&ccedil;&atilde;o ou aguardando data e hora programada para iniciar a execu&ccedil;&atilde;o. Esta tela disponibiliza para o usu&aacute;rio as informa&ccedil;&otilde;es da configura&ccedil;&atilde;o da agenda, bem como dia e hor&aacute;rio programado para sua execu&ccedil;&atilde;o. As agendas em execu&ccedil;&atilde;o apresentam para o usu&aacute;rio a mensagem \"Em execu&ccedil;&atilde;o\".</p>\n");
	         m_Html.envia("<p>ATEN&Ccedil;&Atilde;O: Agendas executadas não permanecem na lista.</p>\n");                  
	         m_Html.envia("</td>\n");
	         m_Html.envia("</tr>\n");
	         m_Html.envia("</table>\n");
	         m_Html.envia("</td>\n");         
         }else{
        	 if (Tipo.equals("Historico")){        		 
    	         m_Html.envia("<td width=\"142\" valign=\"top\">\n");
    	         m_Html.envia("<table width=\"100\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
    	         m_Html.envia("<tr>\n");
    	         m_Html.envia("<td><img src=\"/PortalOsx/imagens/dicasDeUtilizacao.gif\" width=\"141\" height=\"19\"></td>\n");
    	         m_Html.envia("</tr>\n");
    	         m_Html.envia("<tr>\n");
    	         m_Html.envia("<td>&nbsp;</td>\n");
    	         m_Html.envia("</tr>\n");
    	         m_Html.envia("<tr>\n");
    	         m_Html.envia("<td>\n");
    	         m_Html.envia("<p>Apresenta todas as S&eacute;ries Hist&oacute;ricas configuradas no CDRView an&aacute;lise, cujo execu&ccedil;&atilde;o ainda n&atilde;o finalizou. Esta tela disponibiliza para o usu&aacute;rio as informa&ccedil;&otilde;es da S&eacute;rie, bem como dia e hor&aacute;rio da execu&ccedil;&atilde;o e a mensagem \"Em execu&ccedil;&atilde;o\".</p>\n");    	                           
    	         m_Html.envia("</td>\n");
    	         m_Html.envia("</tr>\n");
    	         m_Html.envia("</table>\n");
    	         m_Html.envia("</td>\n");    
        	 }
         }
         m_Html.envia("</tr>\n");
         m_Html.envia("</table>\n");
         m_Html.enviafinal(null, null, true);
         return true;
      }
      catch (Exception Exc)
      {
          No noTmp = null;
          
          for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
          {
          	  try
			  {
	              noTmp = (No) iter.next();
	              noTmp.getConexaoServUtil().destroiArq();
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
         
         System.out.println("OpStatusAgn - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
