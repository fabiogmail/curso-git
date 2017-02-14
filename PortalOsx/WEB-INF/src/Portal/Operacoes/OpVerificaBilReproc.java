//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVerificaBilConversores.java

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
public class OpVerificaBilReproc extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CE418310365
    */
   public OpVerificaBilReproc() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3CE418310379
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Verificação de Configuração de Reprocessadores");

         String Args[] = new String[2];
         montaTabela();
         Args[0] = m_Html.m_Tabela.getTabelaString();
         Args[1] = verificaBilhetadores();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "verificacfgreproc.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "verificabilhetadores.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "verificareproc.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpVerificaBilReproc - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3CE41D8300A7
    */
   public Vector montaLinhas() 
   {
      Vector ListaConfig = new Vector(20), Colunas = null, Linhas = null;
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
            Colunas = new Vector(2);
            Colunas.add(Config.getNome());
            Bilhetadores = Config.getBilhetadores();
            if (Bilhetadores != null)
            {
               ColBil = "";
               for (int j = 0; j < Bilhetadores.size(); j++)
                  ColBil += (String)Bilhetadores.elementAt(j) + ", ";
               if (ColBil.charAt(ColBil.length()-1) == ' ')
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
         Colunas.add("Não existe configuração de conversor cadastrada.");
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
    * @roseuid 3CE41D0D011F
    */
   public void montaTabela()
   {
      String Tipo = null;
      Vector Linhas;

      String Header[] = {"Configuração", "Bilhetadores"};
      String Largura[] = {"150", "402"};
      String Alinhamento[] = {"left", "left"};
      short Filtros[] = {0, 0};

      Linhas = montaLinhas();
      m_Html.setTabela((short)Header.length, false);

      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=verificaBilReproc");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3CEB94650097
    */
   public String verificaBilhetadores() 
   {
      String Retorno = "", Ret = null;
      String tmp = null;

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          tmp = noTmp.getConexaoServUtil().verificaBilhetadores("reprocessador");
	          
	          if ((tmp != null) && (tmp.indexOf("," , tmp.length()-1) == -1))
	          {
	              Retorno = Retorno + "("+noTmp.getHostName()+") "+tmp + ", ";
	          }
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
      
      if (Retorno.length() == 0)
         Ret = "Todos os bilhetadores estão sendo tratados.";
      else
         Ret = "Os bilhetadores a seguir não estão sendo tratados: "+Retorno;

      return Ret;   
   }
}
