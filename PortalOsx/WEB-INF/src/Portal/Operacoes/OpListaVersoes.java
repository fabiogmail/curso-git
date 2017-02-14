//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpStatus.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Versoes;

public class OpListaVersoes extends OperacaoAbs 
{

   public OpListaVersoes() 
   {
   }
   
  
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
    	  montaTabela();
    	  iniciaArgs(7);
          m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
          m_Args[1] = "listagen.htm";
          m_Args[2] = ""; // javascript
          m_Args[3] = ""; // onload
          m_Args[4] = "status.gif";//versoes.gif
          m_Args[5] = m_Html.m_Tabela.getTabelaString();
          m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "status.txt", null);
          m_Html.enviaArquivo(m_Args);
          return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpStatus - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   public void montaTabela() 
   {
      Vector Linhas = null;
         
      Linhas = montaLinhas();
      if (Linhas != null)
      {
         String Header[] = {"Software", "Versão"};
         
         
         String Largura[] = {"272", "271"};
         String Alinhamento[] = {"left", "center"};

         m_Html.setTabela((short)2, false);
         m_Html.m_Tabela.setHeader(Header);
         m_Html.m_Tabela.setLarguraColunas(Largura);
         m_Html.m_Tabela.setCellPadding((short)2);
         m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
         m_Html.m_Tabela.setAlinhamento(Alinhamento);
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=status");
         m_Html.m_Tabela.setElementos(Linhas);
         m_Html.m_Tabela.enviaTabela2String();
      }
   }
   
   public Vector montaLinhas() 
   {
      Vector Linhas = null, Colunas = null;

      Linhas = new Vector();
      
      /*Versão do portal*/
      Colunas = new Vector (2);
      Colunas.addElement("Portal");
      Colunas.addElement(Versoes.versaoPortal);
      Linhas.addElement(Colunas);

      /*Versão do cliente*/
      Colunas = new Vector (2);
      Colunas.addElement("CDRView Análise");
      Colunas.addElement(Versoes.versaoClienteJava);
      Linhas.addElement(Colunas);

      /*Versão do deteção*/
      Colunas = new Vector (2);
      Colunas.addElement("Deteção");
      Colunas.addElement(Versoes.versaoClienteAlarme);
      Linhas.addElement(Colunas);

      Linhas.trimToSize();
      return Linhas;
   }
}
