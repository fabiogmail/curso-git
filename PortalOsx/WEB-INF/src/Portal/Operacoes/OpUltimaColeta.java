//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpUltimaColeta.java

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
public class OpUltimaColeta extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6BF5E303AB
    */
   public OpUltimaColeta()
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6BF5E303BF
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      //System.out.println("OpUltimaColeta - iniciaOperacao()");
      try
      {
         setOperacao("Resumo - Última Coleta");
         montaTabela();
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "ultimacoleta.gif";
         m_Args[5] = m_Html.m_Tabela.getTabelaString();
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "ultimacoleta.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpUltimaColeta - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C6BF5E40013
    */
   public Vector montaLinhas() 
   {
	   
	   Vector Linhas = new Vector(), Coluna;
	   
	   No noTmp = null;

       for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
       {
       	  try
       	  {
       		  noTmp = (No) iter.next();
       		  Vector linhas = noTmp.getConexaoServUtil().getUltimaColeta();
       		  if(linhas != null)
       			  Linhas.addAll(linhas);
       		  
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
  	  
       if (Linhas.size() == 0)
       {
         Linhas = new Vector();
         Coluna = new Vector();
         Coluna.add("Não há informações ou bilhetadores cadastrados!");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.trimToSize();

         Linhas.add(Coluna);
         Linhas.trimToSize();
      
       }
       else
       {
    	   m_Request.setAttribute("atual","0");
    	   m_Request.setAttribute("offset","0");
    	   m_Request.setAttribute("indice","0");
    	   m_Request.setAttribute("ordena","1");
       }
       return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C6BF5E40027
    */
   public void montaTabela() 
   {
      String Header[] = {"Bilhetador", "&Uacute;ltimo Arquivo", "&Uacute;ltimo CDR", "Quantidade CDRs"};
      String Alinhamento[] = {"left", "center", "center", "center"};
      String Largura[] = {"137", "136", "136", "136"};
      String Tipo;
      short Filtros[] = {1, 1, 1, 2};
      Vector Linhas = null;

      Linhas = montaLinhas();
      m_Html.setTabela((short)4, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=ultimaColeta");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();   
   }
}
