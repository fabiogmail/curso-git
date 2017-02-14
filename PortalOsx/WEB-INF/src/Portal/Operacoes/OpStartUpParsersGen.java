//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpStartUpParsers.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpStartUpParsersGen extends OperacaoAbs 
{
   private String m_Resultado = null;
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3DB01DC602CD
    */
   public OpStartUpParsersGen() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3DB01DC602D7
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
	   if(DefsComum.s_CLIENTE.equalsIgnoreCase("oi")){
		   try
		      {
		         String Tipo = m_Request.getParameter("tipo");
		         String servidor = m_Request.getParameter("servidor");
		         
		         
		         //No no = NoUtil.buscaNobyNomePerfil(DefsComum.s_PRF_ADMIN);
		         
		         No noTmp = null;

		         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
		         {
		         	 noTmp = (No) iter.next();
		         	 
		         	 if(noTmp.getHostName().equalsIgnoreCase(servidor) || noTmp.getIp().equalsIgnoreCase(servidor) || NoUtil.listaDeNos.size() == 1)
		         	 {
				         if (Tipo.toLowerCase().equals("startup"))
				         {
				            setOperacao("StartUp ParsersGen");
				            m_Resultado = noTmp.getConexaoServUtil().startUpParsersGen();
				         }
				         else
				         {
				            setOperacao("ShutDown ParsersGen");
				            m_Resultado = noTmp.getConexaoServUtil().shutdownParsersGen();
				         }
		         	 }
		         	 
		         }

		         montaTabela();

		         iniciaArgs(4);
		         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
		         m_Args[1] = "errogen.htm";
		         if (Tipo.toLowerCase().equals("startup"))
		            m_Args[2] = "parsersstartup.gif";
		         else
		            m_Args[2] = "parsersshutdown.gif";
		         m_Args[3] = m_Html.m_Tabela.getTabelaString();
		         m_Html.enviaArquivo(m_Args);

		      }
		      catch (Exception Exc)
		      {
		         System.out.println("OpStartUpParsersGen - iniciaOperacao(): "+Exc);
		         Exc.printStackTrace();
		         return false;
		      }
		     // return true;
		   
	   }else{
		   try
		   {
			   String Resultado = "";
			   String Tipo = m_Request.getParameter("tipo");

			   No no = NoUtil.getNoCentral();

			   if (Tipo.toLowerCase().equals("startup"))
			   {
				   setOperacao("StartUp ParsersGen");
				   m_Resultado = no.getConexaoServUtil().startUpParsersGen();
			   }
			   else
			   {
				   setOperacao("ShutDown ParsersGen");
				   m_Resultado = no.getConexaoServUtil().shutdownParsersGen();
			   }


			   montaTabela();

			   iniciaArgs(4);
			   m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
			   m_Args[1] = "errogen.htm";
			   if (Tipo.toLowerCase().equals("startup"))
				   m_Args[2] = "parsersstartup.gif";
			   else
				   m_Args[2] = "parsersshutdown.gif";
			   m_Args[3] = m_Html.m_Tabela.getTabelaString();
			   m_Html.enviaArquivo(m_Args);
		   }

		   catch (Exception Exc)
		   {
			   System.out.println("OpStartUpParsersGen - iniciaOperacao(): "+Exc);
			   Exc.printStackTrace();
			   return false;
		   }
	}

      return true;
	
   }

   /**
    * @return Vector
    * @exception
    * @roseuid 3DC9641001DF
    */
   public Vector montaLinhas()
   {
      String Linha;
      Vector Linhas = null, Colunas = null;

      Linhas = new Vector();
      if (m_Resultado != null)
      {
          while (m_Resultado != null)
          {
             Linha = m_Resultado.substring(0, m_Resultado.indexOf('\n'));

             Colunas = new Vector();
             m_Resultado = m_Resultado.substring(m_Resultado.indexOf('\n') + 1, m_Resultado.length());
             if (m_Resultado.length() == 0)
                m_Resultado = null;
             Colunas.add(Linha);
             Colunas.trimToSize();
             Linhas.add(Colunas);
          }
      }
      else
      {
          Colunas = new Vector();
          Colunas.add("Sem informa&ccedil;&otilde;es");
          Colunas.trimToSize();
          Linhas.add(Colunas);
      }

      Linhas.trimToSize();
      return (Linhas);
   }

   /**
    * @return void
    * @exception
    * @roseuid 3DC9641001E9
    */
   public void montaTabela()
   {
      String Header[] = {"Informa&ccedil;&otilde;es"};
      String Alinhamento[] = {"left"};
      String Largura[] = {"546"};
      short Filtros[] = {0};
      Vector Linhas = null;

      Linhas = montaLinhas();
      m_Html.setTabela((short)Header.length, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=startUpParsersGen");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
