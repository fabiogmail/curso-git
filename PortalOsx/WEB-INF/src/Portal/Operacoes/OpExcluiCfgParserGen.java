//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpExcluiCfgParser.java

package Portal.Operacoes;

import java.util.Date;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpExcluiCfgParserGen extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E4FFF038B
    */
   public OpExcluiCfgParserGen()
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8E4FFF03D1
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExcluiCfgParser - iniciaOperacao()");
      try
      {
         setOperacao("Exclus�o de Cfg de ParserGen");

         // Recupera os par�metros
         String Configuracao = m_Request.getParameter("configuracao");
         String Servidor = m_Request.getParameter("servidor");

         OpCfgParsersGen Parsers = new OpCfgParsersGen();
         Parsers.setRequestResponse(getRequest(), getResponse());
         
         //No noTmp = NoUtil.getNoCentral();
         No noTmp = NoUtil.getNoByHostName(Servidor);
         if(noTmp == null){
         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Conversor " + Configuracao);
         }
         if (noTmp.getConexaoServUtil().excluiCfgParserGen(Configuracao))
         {
             Parsers.iniciaOperacao("Configura��o de parserGen exclu�da!");
         }
         else
         {
             Parsers.iniciaOperacao("Erro ao excluir configura��o de parserGen!");
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpExcluiCfgParser - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
