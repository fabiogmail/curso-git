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
         setOperacao("Inclus�o de Cfg de ParserGen");

         // Recupera os par�metros
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
                  ParsersGen.iniciaOperacao("Configura��o de parser alterada!");
               else
                  ParsersGen.iniciaOperacao("Configura��o de parser inclu�da!");
               break;
            case 1:
               if (Tipo.equals("altera"))
                  ParsersGen.iniciaOperacao("Configura��o de parser alterada! Billhetador(es) j� associados � outra configura��o!");
               else
                  ParsersGen.iniciaOperacao("Configura��o de parser inclu�da! Billhetador(es) j� associados � outra configura��o!");
               break;
            case 3:
               if (Tipo.equals("altera"))
                  ParsersGen.iniciaOperacao("Erro ao alterar configura��o de parser! Verifique os bilhetadores associados.");
               else
                  ParsersGen.iniciaOperacao("Erro ao incluir configura��o de parser! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
                  ParsersGen.iniciaOperacao("J� existe configura��o com o nome "+Configuracao+"!");
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
