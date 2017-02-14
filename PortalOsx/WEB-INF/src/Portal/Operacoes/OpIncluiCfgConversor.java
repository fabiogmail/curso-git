//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpIncluiCfgConversor.java

package Portal.Operacoes;

import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpIncluiCfgConversor extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpIncluiCfgConversor - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C46CD0C0176
    */
   public OpIncluiCfgConversor() 
   {
      //System.out.println("OpIncluiCfgConversor - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C46CD0C0180
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpIncCfgConversor - iniciaOperacao()");
      try
      {
         setOperacao("Inclus�o de Cfg de Conversor");

         // Recupera os par�metros
         String Tipo = m_Request.getParameter("tipo");         
         String Configuracao = m_Request.getParameter("configuracao");
         String Servidor = m_Request.getParameter("servidor");
         String Conversor = m_Request.getParameter("conversor");
         String Bilhetadores = m_Request.getParameter("bilhetadores");                  

         OpCfgConversores Conversores = new OpCfgConversores();
         Conversores.setRequestResponse(getRequest(), getResponse());

         No noTmp = NoUtil.buscaNobyCfgConversor(Configuracao);
             
         if (Tipo.equals("altera"))
         {
         	 if(noTmp == null){
             	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Conversor " + Configuracao);
             }
             noTmp.getConexaoServUtil().excluiCfgConversor(Configuracao);
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

         switch (noTmp.getConexaoServUtil().incluiCfgConversor(Configuracao, Servidor, Conversor, Bilhetadores))
         {
            case 0:
               if (Tipo.equals("altera"))
                  Conversores.iniciaOperacao("Configura��o de conversor alterada!");
               else
                  Conversores.iniciaOperacao("Configura��o de conversor inclu�da!");
               break;
            case 1:
               if (Tipo.equals("altera"))
                  Conversores.iniciaOperacao("A configura��o de conversor N�O foi alterada! O(s) Billhetador(es) j� est�(�o) associados � outra configura��o!");
               else
                  Conversores.iniciaOperacao("Configura��o de conversor inclu�da! Billhetador(es) j� associados � outra configura��o!");
               break;
            case 3:
               if (Tipo.equals("altera"))
                  Conversores.iniciaOperacao("Erro ao alterar configura��o de conversor! Verifique os bilhetadores associados.");
               else
                  Conversores.iniciaOperacao("Erro ao incluir configura��o de conversor! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
                  Conversores.iniciaOperacao("J� existe configura��o com o nome "+Configuracao+"!");
               break;
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncCfgConversor - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
