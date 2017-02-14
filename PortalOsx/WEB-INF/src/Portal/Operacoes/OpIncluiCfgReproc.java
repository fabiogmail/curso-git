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
         setOperacao("Inclusão de Cfg de Reprocessador");

         // Recupera os parâmetros
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
               	  Reprocessadores.iniciaOperacao("Configuração de reprocessador alterada!");
               else
               	  Reprocessadores.iniciaOperacao("Configuração de reprocessador incluída!");
               break;
            case 1:
               if (Tipo.equals("altera"))
               	  Reprocessadores.iniciaOperacao("A configuração de reprocessador NÃO foi alterada! O(s) Billhetador(es) já está(ão) associados à outra configuração!");
               else
               	  Reprocessadores.iniciaOperacao("Configuração de reprocessador incluída! Billhetador(es) já associados à outra configuração!");
               break;
            case 3:
               if (Tipo.equals("altera"))
               	  Reprocessadores.iniciaOperacao("Erro ao alterar configuração de reprocessador! Verifique os bilhetadores associados.");
               else
               	  Reprocessadores.iniciaOperacao("Erro ao incluir configuração de reprocessador! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
               	  Reprocessadores.iniciaOperacao("Já existe configuração com o nome "+Configuracao+"!");
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
