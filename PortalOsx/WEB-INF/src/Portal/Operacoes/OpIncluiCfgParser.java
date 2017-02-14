//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpIncluiCfgParser.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;


/**
 */
public class OpIncluiCfgParser extends OperacaoAbs
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E4FDA00D5
    */
   public OpIncluiCfgParser() 
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8E4FDA016B
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      //System.out.println("OpIncluiCfgParser - iniciaOperacao()");
      try
      {
         setOperacao("Inclusão de Cfg de Parser");

         // Recupera os parâmetros
         String Tipo = m_Request.getParameter("tipo");         
         String Configuracao = m_Request.getParameter("configuracao");
         String Servidor = m_Request.getParameter("servidor");
         String Parser = m_Request.getParameter("parser");
         String Bilhetadores = m_Request.getParameter("bilhetadores");

         OpCfgParsers Parsers = new OpCfgParsers();
         Parsers.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.getNoCentral();
            
         if (Tipo.equals("altera"))
         {
             noTmp.getConexaoServUtil().excluiCfgParser(Configuracao);
         }

         switch (noTmp.getConexaoServUtil().incluiCfgParser(Configuracao, Servidor, Parser, Bilhetadores))
         {
            case 0:
               if (Tipo.equals("altera"))
                  Parsers.iniciaOperacao("Configuração de parser alterada!");
               else
                  Parsers.iniciaOperacao("Configuração de parser incluída!");
               break;
            case 1:
               if (Tipo.equals("altera"))
                  Parsers.iniciaOperacao("Configuração de parser alterada! Billhetador(es) já associados à outra configuração!");
               else
                  Parsers.iniciaOperacao("Configuração de parser incluída! Billhetador(es) já associados à outra configuração!");
               break;
            case 3:
               if (Tipo.equals("altera"))
                  Parsers.iniciaOperacao("Erro ao alterar configuração de parser! Verifique os bilhetadores associados.");
               else
                  Parsers.iniciaOperacao("Erro ao incluir configuração de parser! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
                  Parsers.iniciaOperacao("Já existe configuração com o nome "+Configuracao+"!");
               break;
         } // end switch
            
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiCfgParser - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
