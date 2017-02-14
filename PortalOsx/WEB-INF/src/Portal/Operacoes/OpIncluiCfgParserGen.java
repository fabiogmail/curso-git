//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpIncluiCfgParser.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;


/**
 */
public class OpIncluiCfgParserGen extends OperacaoAbs
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E4FDA00D5
    */
   public OpIncluiCfgParserGen() 
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
         setOperacao("Inclusão de Cfg de ParserGen");

         // Recupera os parâmetros
         String Tipo = m_Request.getParameter("tipo");         
         String Configuracao = m_Request.getParameter("configuracao");
         String Servidor = m_Request.getParameter("servidor");
         String Parser = m_Request.getParameter("parser");
         String Bilhetadores = m_Request.getParameter("bilhetadores");

         OpCfgParsersGen ParsersGen = new OpCfgParsersGen();
         ParsersGen.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = NoUtil.getNoByHostName(Servidor);
            
         if (Tipo.equals("altera"))
         {
             noTmp.getConexaoServUtil().excluiCfgParserGen(Configuracao);
         }

         switch (noTmp.getConexaoServUtil().incluiCfgParserGen(Configuracao, Servidor, Parser, Bilhetadores))
         {
            case 0:
               if (Tipo.equals("altera"))
                  ParsersGen.iniciaOperacao("Configuração de parser alterada!");
               else
                  ParsersGen.iniciaOperacao("Configuração de parser incluída!");
               break;
            case 1:
               if (Tipo.equals("altera"))
                  ParsersGen.iniciaOperacao("Configuração de parser alterada! Billhetador(es) já associados à outra configuração!");
               else
                  ParsersGen.iniciaOperacao("Configuração de parser incluída! Billhetador(es) já associados à outra configuração!");
               break;
            case 3:
               if (Tipo.equals("altera"))
                  ParsersGen.iniciaOperacao("Erro ao alterar configuração de parser! Verifique os bilhetadores associados.");
               else
                  ParsersGen.iniciaOperacao("Erro ao incluir configuração de parser! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
                  ParsersGen.iniciaOperacao("Já existe configuração com o nome "+Configuracao+"!");
               break;
         } // end switch
            
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiCfgParserGen - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
