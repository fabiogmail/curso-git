//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpControlaGeral.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpControlaGeral extends OperacaoAbs 
{
   private String m_Resultado = null;
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C92388B00F5
    */
   public OpControlaGeral() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C92389101A8
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         String Operacao = m_Request.getParameter("tipo");
         String servidor = m_Request.getParameter("servidor");
         String Texto = "";

         iniciaArgs(4);
         
         No noTmp = null;
         
         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
         	 noTmp = (No) iter.next();
         
         	 if(noTmp.getHostName().equalsIgnoreCase(servidor) || noTmp.getIp().equalsIgnoreCase(servidor) || NoUtil.listaDeNos.size() == 1)
         	 {	
		         if (Operacao.toLowerCase().equals("inicia"))
		         {
		            Texto = "StartUp";
		            m_Args[2] = "manutencaostartup.gif";
		            //m_Resultado = NoUtil.getNo().getConexaoServUtil().StartUp();
		            m_Resultado = noTmp.getConexaoServUtil().StartUp();
		         }
		         else
		         {
		            Texto = "ShutDown";
		            m_Args[2] = "manutencaoshutdown.gif";
		            //m_Resultado = NoUtil.getNo().getConexaoServUtil().ShutDown();
		            m_Resultado = noTmp.getConexaoServUtil().ShutDown();
		         }
         	 }
         
         }

         montaTabela();

         setOperacao("Gerenciamento do Sistema: "+Texto);

         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "errogen.htm";

         m_Args[3] = m_Html.m_Tabela.getTabelaString();

         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println("OpControlaServCtrl - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3DC95741020C
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
          	 if(m_Resultado.indexOf('\n')!=-1)
          	 {
	      		Linha = m_Resultado.substring(0, m_Resultado.indexOf('\n'));
	      		m_Resultado = m_Resultado.substring(m_Resultado.indexOf('\n') + 1, m_Resultado.length());
          	 }
          	 else
          	 {
	      		Linha = m_Resultado;
                m_Resultado = null;
          	 }

          	 Colunas = new Vector();
             
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
    * @roseuid 3DC957410216
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
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=controlaGeral");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
