//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpInfoProcessos.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpInfoProcessos extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpInfoProcessos - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C50CD2B02FD
    */
   public OpInfoProcessos() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C50CD2B0312
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpInfoProcessos - iniciaOperacao()");
      try
      {
         setOperacao("Versões dos Aplicativos");
         montaTabela();
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "listaversoes.gif";
         m_Args[5] = m_Html.m_Tabela.getTabelaString();
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listaversoes.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpInfoProcessos - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C5AE92202D6
    */
   public Vector montaLinhas() 
   {
      Vector Linhas = new Vector(20);
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          noTmp = (No) iter.next();
          Linhas.addAll(noTmp.getConexaoServUtil().getSisInfo());
      }
      Linhas.trimToSize();
      
      return Linhas;
   }

   /**
    * @return void
    * @exception 
    * @roseuid 3C5AE92202FE
    */
   public void montaTabela() 
   {
      String Header[] = {"Aplicativo/Base de Dados", "Versão"};
      String Largura[] = {"272", "271"};
      String Alinhamento[] = {"left", "left"};
      short Filtros[] = {1, 1};
      Vector Linhas;

      Linhas = montaLinhas();
      m_Html.setTabela((short)2, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=infoProcessos");
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
