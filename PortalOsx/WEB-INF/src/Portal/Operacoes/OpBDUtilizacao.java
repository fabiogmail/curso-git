//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpBDUtilizacao.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpBDUtilizacao extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpBDUtilizacao - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C4707780276
    */
   public OpBDUtilizacao() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C47078001F5
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpBDUtilizacao - iniciaOperacao()");
      try
      {
         setOperacao("Utilização do BD");
         No noTmp = null;
         StringBuffer htmlBuffer = new StringBuffer(100);
         
         if (NoUtil.listaDeNos.size() > 1)
         {
	         htmlBuffer.append("<TABLE>");
	
	         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	         {
	             try
	             {
		             noTmp = (No) iter.next();
		             montaTabela(noTmp);
		             htmlBuffer.append("<TR> <TD> Servidor "+noTmp.getHostName()+": </TD> </TR>");
		             htmlBuffer.append("<TR> <TD>");
		             htmlBuffer.append(m_Html.m_Tabela.getTabelaString());
		             htmlBuffer.append("</TD> </TR>");
		             htmlBuffer.append("<TR><TD> </TD></TR>");
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
	         
	         htmlBuffer.append("</TABLE>");
         }
         else
         {
             montaTabela(NoUtil.getNo());
             htmlBuffer.append(m_Html.m_Tabela.getTabelaString());
         }
         
         iniciaArgs(7);         
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "bdutilizacao.gif";
         m_Args[5] = htmlBuffer.toString();
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "bdutilizacao.txt", null);
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println("OpBDUtilizacao - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }

   /**
    * @return void
    * @exception
    * @roseuid 3C5E87DA014B
    */
   public void montaTabela(No no)
   {
      Vector Linhas = null;
         
      Linhas = no.getConexaoServUtil().getBDInfo();
      if (Linhas != null)
      {
         String Header[] = {"Disco/Diretório", "Espaço Utilizado"};
         String Largura[] = {"272", "271"};
         String Alinhamento[] = {"left", "left"};
         short Filtros[] = {1, 1};

         m_Html.setTabela((short)2, false);
         m_Html.m_Tabela.setHeader(Header);
         m_Html.m_Tabela.setLarguraColunas(Largura);
         m_Html.m_Tabela.setCellPadding((short)2);
         m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
         m_Html.m_Tabela.setFiltros(Filtros);
         m_Html.m_Tabela.setAlinhamento(Alinhamento);
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=infoBDUtilizacao");
         m_Html.m_Tabela.setElementos(Linhas);
         m_Html.m_Tabela.enviaTabela2String();
      }
   }
}
