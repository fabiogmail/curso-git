//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVerificaBilParsers.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ConfiguracaoConvCfgDef;

/**
 */
public class OpVerificaBilParsers extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CE4186902D9
    */
   public OpVerificaBilParsers() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3CE4186902E3
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Verificação de Configuração de Parsers");

         String Args[] = new String[2];
         montaTabela();
         Args[0] = m_Html.m_Tabela.getTabelaString();
         Args[1] = verificaBilhetadores();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "verificacfgparsers.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "verificabilhetadores.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "verificaparsers.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpVerificaBilParsers - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @return Vector
    * @exception 
    * @roseuid 3CE41D6C005D
    */
   public Vector montaLinhas() 
   {
      Vector ListaConfig = null, Colunas = null, Linhas = null;
      ConfiguracaoConvCfgDef Config;
      String ColBil = "";
      Vector Bilhetadores = null;

      No no = NoUtil.getNoCentral();
      ListaConfig = no.getConexaoServUtil().getListaCfgParsers();
      
      Linhas = new Vector();
      if (ListaConfig != null)
      {
         for (int i = 0; i < ListaConfig.size(); i++)
         {
            Config = (ConfiguracaoConvCfgDef)ListaConfig.elementAt(i);
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
         Colunas.add("Não existe configuração de parser cadastrada.");
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
    * @roseuid 3CE41CEE00AC
    */
   public void montaTabela()
   {
      String Tipo = null;
      Vector Linhas;

      String Header[] = {"Configura&ccedil;&atilde;o", "Bilhetadores"};
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
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=verificaBilParsers");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3CEB925502DD
    */
   public String verificaBilhetadores()    {
      String Retorno = null, Ret = null;    
      
      No no = NoUtil.getNoCentral();
      Retorno = no.getConexaoServUtil().verificaBilhetadores("parser");

      if (Retorno.length() == 0)
         Ret = "Todos os bilhetadores est&atilde;o sendo tratados.";
      else
         Ret = "Os bilhetadores a seguir n&atilde;o est&atilde;o sendo tratados: "+Retorno;

      return Ret;
   }
}
