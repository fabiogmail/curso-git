//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaCfgConversores.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ConfiguracaoReprocCfgDef;

/**
 */
public class OpListaCfgReproc extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C9B544E0038
    */
   public OpListaCfgReproc() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C9B544E004C
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Lista Configuração de Reprocessadores");
         montaTabela();
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "listacfgreproc.gif";
         m_Args[5] = m_Html.m_Tabela.getTabelaString();
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listareproccfg.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaCfgReproc - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C9B544E006A
    */
   public Vector montaLinhas() 
   {
      Vector ListaConfig = new Vector(), Colunas = null, Linhas = null;
      ConfiguracaoReprocCfgDef Config;
      String ColBil = "";
      Vector Bilhetadores = null;
      
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          Vector listaconfig = noTmp.getConexaoServUtil().getListaCfgReprocessadores();
	          if(listaconfig != null)
	          	ListaConfig.addAll(listaconfig);
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

      Linhas = new Vector();
      if (ListaConfig.size() > 0)
      {
         for (int i = 0; i < ListaConfig.size(); i++)
         {
            Config = (ConfiguracaoReprocCfgDef)ListaConfig.elementAt(i);
            Colunas = new Vector();
            Colunas.add(Config.getNome());
            Colunas.add(Config.getServidor());
            Colunas.add(Config.getConversor());
            Bilhetadores = Config.getBilhetadores();
            if (Bilhetadores != null)
            {
               ColBil = "";
               for (int j = 0; j < Bilhetadores.size(); j++)
                  ColBil += (String)Bilhetadores.elementAt(j) + ", ";

               if (ColBil.charAt(ColBil.length()-2) == ',')
                  ColBil = ColBil.substring(0, ColBil.length()-2);
            }
            Colunas.add(ColBil);
            Colunas.trimToSize();
            Linhas.add(Colunas);
         }
      }
      else
      {
         Colunas = new Vector();
         Colunas.add("N&atilde;o existe configura&ccedil;&atilde;o");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");                  
         Colunas.trimToSize();
         Linhas.add(Colunas);
      }
      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C9B544E007E
    */
   public void montaTabela() 
   {
      String Tipo = null;
      Vector Linhas;

      String Header[] = {"Configura&ccedil;&atilde;o", "Servidor", "Reprocessador", "Bilhetadores"};
      String Largura[] = {"125", "125", "126", "176"};
      String Alinhamento[] = {"left", "center", "center", "left"};
      short Filtros[] = {1, 1, 1, 0};

      Linhas = montaLinhas();
      Tipo = m_Request.getParameter("tipo");
      if (Tipo.equals("parcial") == true)
         m_Html.setTabela((short)Header.length, true);
      else
         m_Html.setTabela((short)Header.length, false);

      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaCfgReproc&tipo="+Tipo);
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
