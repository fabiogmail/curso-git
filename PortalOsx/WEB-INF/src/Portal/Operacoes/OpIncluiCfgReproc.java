//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpIncluiCfgReprocessador.java

package Portal.Operacoes;

import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpIncluiCfgReproc extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpIncluiCfgReprocessador - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C46CD0C0176
    */
   public OpIncluiCfgReproc() 
   {
      //System.out.println("OpIncluiCfgReprocessador - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C46CD0C0180
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpIncCfgReprocessador - iniciaOperacao()");
      try
      {
         setOperacao("Inclus�o de Cfg de Reprocessador");

         // Recupera os par�metros
         String Tipo = m_Request.getParameter("tipo");         
         String Configuracao = m_Request.getParameter("configuracao");
         String Servidor = m_Request.getParameter("servidor");
         String Reprocessador = m_Request.getParameter("reprocessador");
         String Bilhetadores = m_Request.getParameter("bilhetadores");                  

         OpCfgReprocessadores Reprocessadores = new OpCfgReprocessadores();
         Reprocessadores.setRequestResponse(getRequest(), getResponse());

         No noTmp = NoUtil.buscaNobyCfgReprocessador(Configuracao);
             
         if (Tipo.equals("altera"))
         {
         	 if(noTmp == null){
             	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Reprocessador " + Configuracao);
             }
             noTmp.getConexaoServUtil().excluiCfgReprocessador(Configuracao);
         }
         else
         {
         	if(Servidor != null && !Servidor.equals(""))
         	{
         		noTmp = NoUtil.getNoByHostName(Servidor);
         	}
         	else
         	{
         		noTmp = NoUtil.getNoCentral();
         	}
         }

         switch (noTmp.getConexaoServUtil().incluiCfgReprocessador(Configuracao, Servidor, Reprocessador, Bilhetadores))
         {
            case 0:
               if (Tipo.equals("altera"))
               	  Reprocessadores.iniciaOperacao("Configura��o de reprocessador alterada!");
               else
               	  Reprocessadores.iniciaOperacao("Configura��o de reprocessador inclu�da!");
               break;
            case 1:
               if (Tipo.equals("altera"))
               	  Reprocessadores.iniciaOperacao("A configura��o de reprocessador N�O foi alterada! O(s) Billhetador(es) j� est�(�o) associados � outra configura��o!");
               else
               	  Reprocessadores.iniciaOperacao("Configura��o de reprocessador inclu�da! Billhetador(es) j� associados � outra configura��o!");
               break;
            case 3:
               if (Tipo.equals("altera"))
               	  Reprocessadores.iniciaOperacao("Erro ao alterar configura��o de reprocessador! Verifique os bilhetadores associados.");
               else
               	  Reprocessadores.iniciaOperacao("Erro ao incluir configura��o de reprocessador! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
               	  Reprocessadores.iniciaOperacao("J� existe configura��o com o nome "+Configuracao+"!");
               break;
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiCfgReproc - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
