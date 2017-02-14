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
         setOperacao("Inclus�o de Cfg de Parser");

         // Recupera os par�metros
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
                  Parsers.iniciaOperacao("Configura��o de parser alterada!");
               else
                  Parsers.iniciaOperacao("Configura��o de parser inclu�da!");
               break;
            case 1:
               if (Tipo.equals("altera"))
                  Parsers.iniciaOperacao("Configura��o de parser alterada! Billhetador(es) j� associados � outra configura��o!");
               else
                  Parsers.iniciaOperacao("Configura��o de parser inclu�da! Billhetador(es) j� associados � outra configura��o!");
               break;
            case 3:
               if (Tipo.equals("altera"))
                  Parsers.iniciaOperacao("Erro ao alterar configura��o de parser! Verifique os bilhetadores associados.");
               else
                  Parsers.iniciaOperacao("Erro ao incluir configura��o de parser! Verifique os bilhetadores associados.");
               break;
            case 4:
               if (Tipo.equals("inclui"))
                  Parsers.iniciaOperacao("J� existe configura��o com o nome "+Configuracao+"!");
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
