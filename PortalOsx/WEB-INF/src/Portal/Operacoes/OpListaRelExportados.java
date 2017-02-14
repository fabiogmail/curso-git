//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaRelExportados.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Vector;

import CDRView2.Cliente;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;


public class OpListaRelExportados extends OperacaoAbs 
{
   private Vector m_Linhas = null;
   private String m_Perfil = null;
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6FF8480362
    */
   public OpListaRelExportados() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6FF8480376
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      
	  if(Cliente.fnCliente() == Cliente.BrasilTelecomCelular || Cliente.fnCliente() == Cliente.Oi)
		  m_Perfil  = m_Request.getParameter("perfil");
      try
      {
         String Arquivos = "", Arquivo;
         Vector Coluna;

         montaTabela();
         for (short i = 0; i < m_Linhas.size(); i++)
         {
            Coluna = (Vector)m_Linhas.elementAt(i);
            Arquivo = (String)Coluna.elementAt(0);
            // Retira a extensão do nome do arquivo
            if (Arquivo.lastIndexOf('.') != -1)
               Arquivo = Arquivo.substring(0, Arquivo.lastIndexOf('.'));
            Arquivos += Arquivo + ";";
         }
         Arquivos = Arquivos.substring(0, Arquivos.length()-1);

         String Args[] = new String[4];
         Args[0] = Integer.toString(m_Linhas.size());
         Args[1] = Arquivos;
         Args[2] = p_Mensagem;
         Args[3] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listarelexportados.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/lixo0.gif','/PortalOsx/imagens/lixo1.gif'); IniciaDelecao()\"";
         m_Args[4] = "listarelexportados.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelexportados.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listarelexportados.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaRelExportados - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C6FF848038B
    */
   public Vector montaLinhas() 
   {
      boolean bPronto = true;
      String Perfil, TipoRel, Aux;
      Vector LinhasAux = new Vector(), ColunaAux, Coluna;
      UsuarioDef Usuario;
      No noTmp = null;

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      // Cria lista
      m_Linhas = new Vector();
      if(m_Perfil==null){
    	  for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
          {
              noTmp = (No) iter.next();
              Vector tmp = noTmp.getConexaoServUtil().getListaRelExportados(Integer.toString(Usuario.getIdPerfil()));
              if(tmp != null)
            	  LinhasAux.addAll(tmp);
 
          }
    	  
      }
      else{
    	  for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
          {
              noTmp = (No) iter.next();
              Vector tmp = noTmp.getConexaoServUtil().getListaRelExportados(m_Perfil);
              if(tmp != null)
            	  LinhasAux.addAll(tmp);
 
          }
    	  
      }
      
      if (/*LinhasAux != null ||*/ LinhasAux.size() > 0)
      {
         String Arquivo;
         for (int i = 0; i < LinhasAux.size(); i++)
         {
            ColunaAux = (Vector)LinhasAux.elementAt(i);
            Coluna = new Vector();
            Aux = (String)ColunaAux.elementAt(0);
            if (ColunaAux.elementAt(1).equals("-") == true)
            {
               if (Aux.indexOf("tmp_") != -1)
                  Aux = Aux.substring(4, Aux.length());
               bPronto = false;
            }
            Coluna.add(Aux);
            Aux = (String)ColunaAux.elementAt(2);
            if (bPronto)
            {
               Aux = Aux.substring(6,8) +"/"+ Aux.substring(4,6) +"/"+  Aux.substring(0,4)+" "+
                     Aux.substring(8,10) +":"+ Aux.substring(10,12) +":"+ Aux.substring(12,Aux.length());
            }
            Coluna.add(Aux);
            Coluna.add(ColunaAux.elementAt(3));
            if (bPronto)
            {
            	if(m_Perfil != null)
            	{
            		 Coluna.add("<a href=\"javascript:AbreJanela('detalhar', '"+Usuario.getIdPerfil()+"','"+m_Perfil+"','"+ColunaAux.elementAt(1)+"')\" onmouseover=\"window.status='Clique para ver detalhes do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldetalha.gif\" border=\"0\"></a>");
            	}
            	else
            	{
            		 Coluna.add("<a href=\"javascript:AbreJanela('detalhar', '"+Usuario.getIdPerfil()+"','"+Usuario.getPerfil()+"','"+ColunaAux.elementAt(1)+"')\" onmouseover=\"window.status='Clique para ver detalhes do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldetalha.gif\" border=\"0\"></a>");
            	}              
            }
            else
            {
               Coluna.add("-");
            }
            if (bPronto)
            {
            	if(m_Perfil != null)
            	{
            		Coluna.add("<a href=\"/PortalOsx/templates/jsp/downloadExport.jsp?operacao=downloadExport&usuario="+m_Perfil+"&arquivo="+ColunaAux.elementAt(0)+"&diretorio="+NoUtil.getNo().getDiretorioDefs().getS_DIR_CDRVIEW()+"bdados/export/"+"\" onmouseover=\"window.status='Clique para fazer o download do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldownload.gif\" border=\"0\"></a>");               	
            	}
            	else
            	{	
            		Coluna.add("<a href=\"/PortalOsx/templates/jsp/downloadExport.jsp?operacao=downloadExport&usuario="+Usuario.getIdPerfil()+"&arquivo="+ColunaAux.elementAt(0)+"&diretorio="+NoUtil.getNo().getDiretorioDefs().getS_DIR_CDRVIEW()+"bdados/export/"+"\" onmouseover=\"window.status='Clique para fazer o download do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldownload.gif\" border=\"0\"></a>");
            	}
            }
            else
            {
            	Coluna.add("-");
            }
                           
            Arquivo = (String)ColunaAux.elementAt(0);
            Arquivo = Arquivo.substring(0, Arquivo.lastIndexOf("."));

            if (bPronto)
               Coluna.add("Pronto");
            else
               Coluna.add("Exportando");

            if (bPronto)
               Coluna.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+i+"','','/PortalOsx/imagens/lixo1.gif',1,'"+i+"','"+Arquivo+"')\" onmouseover=\"window.status='Marca/desmarca o arquivo para apagar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
            else
               Coluna.add("-");

            Coluna.trimToSize();
            m_Linhas.add(Coluna);
         }
      }
      else
      {
         Coluna = new Vector();
         Coluna.add("N&atilde;o h&aacute; arquivos de bases exportadas");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.trimToSize();
         m_Linhas.add(Coluna);
      }

      m_Linhas.trimToSize();
      return m_Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C6FF84803B3
    */
   public void montaTabela() 
   {
      String Header[] = {"Arquivo", "Data", "Tamanho", "Detalhar", "Download", "Status", "Apagar"};
      String Alinhamento[] = {"left", "center", "center", "center", "center", "center", "center"};
      String Largura[] = {"138", "118", "68", "50", "60", "75", "45"};
      short Filtros[] = {1, 1, 0, 0, 0, 0, 0};
      UsuarioDef Usuario;

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      montaLinhas();
      m_Html.setTabela((short)Header.length, true);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaRelExportados&perfil="+Usuario.getIdPerfil());
      m_Html.m_Tabela.setElementos(m_Linhas);
      m_Html.trataTabela(m_Request, m_Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
