//Source file: C:/usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaMensagens.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpListaMensagens extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6C28610268
    */
   public OpListaMensagens() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6C285E0386
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      try
      {
         setOperacao("Lista Mensagens");
         int QtdMensagens = 0;
         String Args[], Arquivos = "", Arquivo;
         UsuarioDef Usuario;
         Vector ListaMensagens = null;

         Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         ListaMensagens = Usuario.getNo().getConexaoServUtil().getListaMensagensOutras(Usuario.getUsuario());
         
         if (ListaMensagens != null)
         {
            for (int i = 0; i < ListaMensagens.size(); i++)
            {
               Arquivo = (String)ListaMensagens.elementAt(i);
               Arquivos += Arquivo + ";";
            }
            Arquivos = Arquivos.substring(0, Arquivos.length()-1);
            QtdMensagens = ListaMensagens.size();
         }

         Args = new String[4];
         montaTabela();
         Args[0] = Integer.toString(QtdMensagens);
         Args[1] = Arquivos;
         Args[2] = p_Mensagem;
         Args[3] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listamensagens.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/lixo0.gif','/PortalOsx/imagens/lixo1.gif'); IniciaDelecao()\"";
         m_Args[4] = "listamensagens.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listamensagens.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listamensagens.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaMensagens - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C6C285E039A
    */
   public Vector montaLinhas() 
   {
      Vector ListaMensagens, Linhas, Colunas;
      char TipoMsg;
      UsuarioDef Usuario;

      String Arquivo, Aux = "", Data, Origem = "", Header = "";
      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      ListaMensagens = Usuario.getNo().getConexaoServUtil().getListaMensagensOutras(Usuario.getUsuario());
      Linhas = new Vector();
      if (ListaMensagens != null)
      {
         String ArquivoAux = "";
         for (int i = 0; i < ListaMensagens.size(); i++)
         {
            Colunas = new Vector();
            Arquivo = (String)ListaMensagens.elementAt(i);
            TipoMsg = Arquivo.charAt(Arquivo.indexOf("-")+1);
            ArquivoAux = Arquivo.replace(' ', '+');

            Origem = Arquivo.substring(0, Arquivo.indexOf("_")); //Origem
            Header = Arquivo.substring(Arquivo.indexOf("=")+1, Arquivo.indexOf("$")); //Header
            Colunas.add(Origem);
            Colunas.add("<a href=\"javascript:LeMensagem('"+Arquivo+"')\" onmouseover=\"window.status='Lê mensagem';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+Header+"</a>");
            // Recupera da data
            Data = Arquivo.substring(Arquivo.indexOf("$")+1, Arquivo.length() - 4);
            Data = Data.replace('@', '/');
            Data = Data.replace('_', ' ');
            Data = Data.replace('.', ':');
            Colunas.add(Data);
            Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+i+"','','/PortalOsx/imagens/lixo1.gif',1,'"+i+"','"+Arquivo+"')\" onmouseover=\"window.status='Marca/desmarca a mensagem para apagar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
            Colunas.trimToSize();
            Linhas.add(Colunas);
         }
      }
      else
      {
         Colunas = new Vector();
         Colunas.add("N&atilde;o h&aacute; mensagens");
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
    * @roseuid 3C6C285E03AE
    */
   public void montaTabela() 
   {
      String Header[] = {"De", "Assunto", "Data/Hora", "Apagar"};
      String Alinhamento[] = {"left", "left", "center", "center"};
      String Largura[] = {"136", "194", "136", "80"};
      short Filtros[] = {1, 1, 1, 0};
      Vector Linhas = null;

      Linhas = montaLinhas();
      m_Html.setTabela((short)4, true);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaMensagens");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
