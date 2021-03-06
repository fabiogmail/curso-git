//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaRelHistoricos.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Conexao.CnxServUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpListaRelHistoricos extends OperacaoAbs 
{
   private Vector m_Arquivos = null;
   private boolean m_bDetalha = false;
   private String m_Perfil = null;
   private String m_TipoRel = null;
   private boolean m_PodeApagar = false;
   private String m_NomeRels = null;
   private String m_DataRels = null;
   
   static
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C71BDD501FB
    */
   public OpListaRelHistoricos() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C71BDD50219
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpListaRelHistoricos - iniciaOperacao()");
      try
      {
         String Args[] = new String[6];
         String SubOperacao = m_Request.getParameter("suboperacao");            

         setOperacao("Lista Relatórios Históricos");
         if (p_Mensagem.charAt(1) != 'P')
         {
            m_Perfil  = m_Request.getParameter("perfil");
            m_TipoRel = m_Request.getParameter("tiporel");
         }
         else
         {
            // Formato de p_Mensagem: $P[perfil]-T[tiporel]
            m_Perfil  = p_Mensagem.substring(2, p_Mensagem.indexOf('-'));
            m_TipoRel = p_Mensagem.substring(p_Mensagem.indexOf('-')+2, p_Mensagem.length());
         }

			if (NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil().equals(m_Perfil))
				m_PodeApagar = true;
			else
				m_PodeApagar = false;
			
         montaTabela();
         String Arquivos = "";
         Vector Elem = null;
         if (m_Arquivos != null)
         {
            for (int i = 0; i < m_Arquivos.size(); i++)
               Arquivos += (String)m_Arquivos.elementAt(i) + ";";

            Arquivos = Arquivos.substring(0, Arquivos.length()-1);
         }

         Args[0] = Integer.toString(m_Html.m_Tabela.getElementos().size());
         Args[1] = Arquivos;
         Args[2] = m_Perfil;
         Args[3] = m_TipoRel;
         Args[4] = p_Mensagem;
         Args[5] = m_Html.m_Tabela.getTabelaString();
         
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listarelhistoricos.js\""; // javascript
         if (SubOperacao == null)
            m_Args[3] = "onLoad=\"DesmarcaDatas();\" ";
         else
            m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/lixo0.gif','/PortalOsx/imagens/lixo1.gif'); IniciaDelecao(); \"";         

         m_Args[4] = "relhistoricos.gif";
         if (SubOperacao == null)
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelhistoricos.form", Args);
         else
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelhistoricos2.form", Args);         
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listarelagendados.txt", null);

         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaRelHistoricos - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C71BDD5022D
    */
   public Vector montaLinhas() 
   {
      String NomeRel, SubOperacao = null;
      Vector LinhasAux, Linhas = null, ColunaAux, Coluna;

      // Recupera parametros
      SubOperacao = m_Request.getParameter("suboperacao");

      if (SubOperacao == null)
      {
         // Cria lista
         Linhas = new Vector();
         CnxServUtil cnxServUtil = NoUtil.buscaNobyNomePerfil(m_Perfil).getConexaoServUtil();
         
         LinhasAux = cnxServUtil.getListaRelArmazenados2((short) 1, m_Perfil, Short.parseShort(m_TipoRel), (short)0, "", "",NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil());

         if (LinhasAux != null)
         {
            String Datas = "", QtdArquivos, Data, Aux;
            String ArqRelatorio, IdRel, DataGeracao, DataArmazenada;
            Vector vDatasQtd = null, vDatas;
            int QtdDatas = 0;
            for (int i = 0; i < LinhasAux.size(); i++)
            {
               //Formato do elemento do vetor: Perfil - NomeRelatorio - ArqRelatorio - Datas (Data - QtdArq) (vetor)
               Datas = "";
               ColunaAux = (Vector)LinhasAux.elementAt(i);
               //Perfil = (String)ColunaAux.elementAt(0);
               NomeRel = (String)ColunaAux.elementAt(1);
               ArqRelatorio = (String)ColunaAux.elementAt(2);
               DataGeracao = (String)ColunaAux.elementAt(3);
//System.out.println("---->>>> DataGeracao: "+DataGeracao);
               //DataArmazenada = DataGeracao.substring(DataGeracao.indexOf("*")+1, DataGeracao.length());
               //DataGeracao = DataGeracao.substring(0, DataGeracao.indexOf("*"));
//System.out.println("---->>>> DataGeracao 2: "+DataGeracao);
//System.out.println("---->>>> DataArmazenada: "+DataArmazenada);

               vDatasQtd = (Vector)ColunaAux.elementAt(4);
               QtdDatas = vDatasQtd.size();

               if (ArqRelatorio.indexOf('-') != 1)
                  IdRel = ArqRelatorio.substring(ArqRelatorio.indexOf('-')+1, ArqRelatorio.indexOf('.'));
               else
                  IdRel = ArqRelatorio.substring(0, ArqRelatorio.indexOf('.'));

               Coluna = new Vector();
               Datas = "<select size=\"1\" name=\"listadatas"+i+"-"+m_Perfil+"-"+NomeRel+"\" class=\"lista\">\n";
               for (int j = 0; j < QtdDatas; j++)
               {
                  vDatas = (Vector)vDatasQtd.elementAt(j);
                  Data = (String)vDatas.elementAt(0);
                  QtdArquivos = (String)vDatas.elementAt(1);
                  Data = Data.substring(6,Data.length()) + "/" + Data.substring(4,6) + "/" + Data.substring(0,4);
                  Datas += "<option value=\"" + (String)vDatas.elementAt(0)+"@"+QtdArquivos+"\">" + Data + "\n";
               }
               Datas += "</select>\n";

               if (m_TipoRel.equals("12"))
                  Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");
                  //Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataArmazenada+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");                  
               else
                  Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");               
                  //Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataArmazenada+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");
               Coluna.add(m_Perfil);
               Coluna.add(Datas);
               //Coluna.add("<a href=\"javascript:;\" ><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");               

					if (m_PodeApagar)
						Coluna.add("<a href=\"javascript:AbreJanela('listaapagar','"+m_Perfil+"','"+m_TipoRel+"','','','"+NomeRel+"');\" onmouseover=\"window.status='Selecione relatórios para excluir';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");

               Coluna.trimToSize();
               Linhas.add(Coluna);
            }
         }
         else
         {
            Coluna = new Vector();
            Coluna.add("N&atilde;o h&aacute; relat&oacute;rios hist&oacute;ricos");
            Coluna.add("&nbsp;");
            Coluna.add("&nbsp;");            

				if (m_PodeApagar)
					Coluna.add("&nbsp;");            

            Coluna.trimToSize();
            Linhas.add(Coluna);
         }
      }
      else if (SubOperacao.equals("listar"))
      {
         String ArqRelatorio, Relatorio, Data, DataGeracao, DataApresentada, IdRel;
         Vector vDatas;
         
         Relatorio = m_Request.getParameter("nomesrels");
         if (Relatorio == null)
            Relatorio = m_NomeRels;

         Relatorio = Relatorio.replace('@', ' ');

         Data = m_Request.getParameter("datasrels");
         if (Data == null)
            Data = m_DataRels;

         m_NomeRels = Relatorio;
         m_DataRels = Data;
    
         // Cria lista
         Linhas = new Vector();
         
         CnxServUtil cnxServUtil = NoUtil.buscaNobyNomePerfil(m_Perfil).getConexaoServUtil();
         
         LinhasAux = cnxServUtil.getListaRelArmazenados2((short) 1, m_Perfil, Short.parseShort(m_TipoRel), (short)0, Relatorio, Data, NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil());
         if (LinhasAux != null)
         {
            m_Arquivos = new Vector();
            for (int i = 0; i < LinhasAux.size(); i++)
            {
               ColunaAux = (Vector)LinhasAux.elementAt(i);
               //Perfil = (String)ColunaAux.elementAt(0);
               NomeRel = (String)ColunaAux.elementAt(1);
               //ArqRelatorio = (String)ColunaAux.elementAt(2);
               vDatas = (Vector)ColunaAux.elementAt(2);

               m_Arquivos.add(ColunaAux.elementAt(1)+","+ColunaAux.elementAt(2)+","+ColunaAux.elementAt(0));
//System.out.println("vDatas.size(): "+ vDatas.size());
               for (int j = 0; j < vDatas.size(); j++)
               {
                  DataGeracao = (String)vDatas.elementAt(j);
                  DataApresentada = new String (DataGeracao);
//System.out.println("=====>>>> DataGeracao: "+DataGeracao);
                  DataApresentada = DataApresentada.substring(0, DataApresentada.indexOf("!"));
                  DataApresentada = DataApresentada.substring(DataApresentada.indexOf('=')+1, DataApresentada.length());
//System.out.println("====>>>> DataGeracao 2: "+DataGeracao);
//System.out.println("====>>>> DataArmazenada: "+DataArmazenada);
                  
//System.out.println("DataGeracao Aux: "+ DataGeracao);
                  ArqRelatorio = DataGeracao.substring(0, DataGeracao.indexOf('='));
//System.out.println("ArqRelatorio: "+ ArqRelatorio);                  
                  DataGeracao = DataGeracao.substring(DataGeracao.indexOf('=')+1, DataGeracao.length());
//System.out.println("DataGeracao: "+ DataGeracao);
                  if (ArqRelatorio.indexOf('-') != -1)
                     IdRel = ArqRelatorio.substring(ArqRelatorio.indexOf('-')+1, ArqRelatorio.indexOf('.'));
                  else
                     IdRel = ArqRelatorio.substring(0, ArqRelatorio.indexOf('.'));

                  Coluna = new Vector();
                  Coluna.add(NomeRel);
                  Coluna.add(m_Perfil);
                  Coluna.add(DataApresentada) ;//DataGeracao);
                  Coluna.add("<a href=\"javascript:AbreJanela('detalhar', '"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para ver detalhes do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldetalha.gif\" border=\"0\"></a>");
                  if (DefsComum.s_CLIENTE.equals("Telemig")){
                    // Coluna.add("<a href=\"javascript:AbreJanela('visualizar','"+m_Perfil+"','"+m_TipoRel+"','"+ColunaAux.elementAt(2)+"','"+ColunaAux.elementAt(0)+"')\" onmouseover=\"window.status='Clique para visualizar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                  	Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                  }
                  else
                  {
                     if (m_TipoRel.equals("12"))
                        Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                        //Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataArmazenada+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                     else
                        Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                        //Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataArmazenada+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                  }

                  //Coluna.add("<a href=\"javascript:AbreJanela('apagar','"+m_Perfil+"','"+m_TipoRel+"','','','"+NomeRel+"');\" onmouseover=\"window.status='Selecione relatórios para apagar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
						if (m_PodeApagar)						
							Coluna.add("<a href=\"javascript:AbreJanela('listaapagar','"+m_Perfil+"','"+m_TipoRel+"','','','"+NomeRel+"');\" onmouseover=\"window.status='Selecione relatórios para excluir';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
                  //Coluna.add("<a href=\"\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"1\" name=\"Image"+i+"\"></a>\n");
                  Coluna.trimToSize();
                  Linhas.add(Coluna);
               }
            }
         }
         else
         {        
            Coluna = new Vector();
            Coluna.add("N&atilde;o h&aacute; relat&oacute;rios hist&oacute;ricos");
            Coluna.add("&nbsp;");
            Coluna.add("&nbsp;");            
            Coluna.add("&nbsp;");
            Coluna.add("&nbsp;");
				if (m_PodeApagar)
					Coluna.add("&nbsp;");
            Coluna.trimToSize();
            Linhas.add(Coluna);
         }
      }
//*/      
/*
      // Cria lista
      Linhas = new Vector();
      LinhasAux = m_ConexUtil.getListaRelArmazenados((short) 1, m_Perfil, (short)0);
      if (LinhasAux != null)
      {
         m_Arquivos = new Vector();
         for (int i = 0; i < LinhasAux.size(); i++)
         {
            ColunaAux = (Vector)LinhasAux.elementAt(i);

            m_Arquivos.add(ColunaAux.elementAt(1)+","+ColunaAux.elementAt(2)+","+ColunaAux.elementAt(0));

            Coluna = new Vector();
            Coluna.add(ColunaAux.elementAt(1));
            Coluna.add(ColunaAux.elementAt(3));
            Coluna.add("<a href=\"javascript:AbreJanela('detalhar', '"+m_Perfil+"','"+m_TipoRel+"','"+ColunaAux.elementAt(2)+"','"+ColunaAux.elementAt(0)+"')\" onmouseover=\"window.status='Clique para ver detalhes do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldetalha.gif\" border=\"0\"></a>");
            if (DefsGerais.s_CLIENTE.equals("Telemig"))            
               Coluna.add("<a href=\"javascript:AbreJanela('visualizar','"+m_Perfil+"','"+m_TipoRel+"','"+ColunaAux.elementAt(2)+"','"+ColunaAux.elementAt(0)+"')\" onmouseover=\"window.status='Clique para visualizar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
            else
               Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+ColunaAux.elementAt(2)+"','"+ColunaAux.elementAt(0)+"','"+ColunaAux.elementAt(1)+"','"+ColunaAux.elementAt(3)+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
            Coluna.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+i+"','','/PortalOsx/imagens/lixo1.gif',1,'"+i+"','"+m_Perfil+","+m_TipoRel+","+ColunaAux.elementAt(2)+","+ColunaAux.elementAt(0)+"')\" onmouseover=\"window.status='Marca/desmarca o relatório para apagar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
            Coluna.trimToSize();
            Linhas.add(Coluna);
         }
      }
      else
      {
         Coluna = new Vector();
         Coluna.add("N&atilde;o h&aacute; relat&oacute;rios hist&oacute;ricos");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");
         Coluna.trimToSize();
         Linhas.add(Coluna);
      }
*/
      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C71BDD50241
    */
   public void montaTabela() 
   {
      Vector Linhas = null;
      String Alinhamento[] = null, Header[] = null, Largura[] = null;
      short Filtros[] = null;
      String SubOperacao = m_Request.getParameter("suboperacao");   

      if (SubOperacao == null)
      {
			if (m_PodeApagar)
			{
				Header = new String[4];
				Header[0] = "Relat&oacute;rio";
				Header[1] = "Perfil";
				Header[2] = "Datas de Gera&ccedil;&atilde;o";
				Header[3] = "Apagar";
         
				Alinhamento = new String[4];
				Alinhamento[0] = "left";
				Alinhamento[1] = "center";
				Alinhamento[2] = "center";
				Alinhamento[3] = "center";
         
				Largura = new String[4];         
				Largura[0] = "173";
				Largura[1] = "100";
				Largura[2] = "190";
				Largura[3] = "80";
         
				Filtros = new short[4];
				Filtros[0] = 1;
				Filtros[1] = 1;
				Filtros[2] = 0;
				Filtros[3] = 0;
			}
			else
			{
				Header = new String[3];
				Header[0] = "Relat&oacute;rio";
				Header[1] = "Perfil";
				Header[2] = "Datas de Gera&ccedil;&atilde;o";
         
				Alinhamento = new String[3];
				Alinhamento[0] = "left";
				Alinhamento[1] = "center";
				Alinhamento[2] = "center";
         
				Largura = new String[3];         
				Largura[0] = "173";
				Largura[1] = "140";
				Largura[2] = "230";
         
				Filtros = new short[3];
				Filtros[0] = 1;
				Filtros[1] = 1;
				Filtros[2] = 0;			
			}
      }
      else if (SubOperacao.equals("listar"))
      {
			if (m_PodeApagar)
			{
				Header = new String[6];
				Header[0] = "Relat&oacute;rio";
				Header[1] = "Perfil";         
				Header[2] = "Data de Gera&ccedil;&atilde;o";
				Header[3] = "Detalhar";
				if (DefsComum.s_CLIENTE.equals("Telemig") || DefsComum.s_CLIENTE.equals("Telecelular_Sul"))
					Header[4] = "Visualizar";
				else
					Header[4] = "Apresentar";
				Header[5] = "Apagar";
        
				Alinhamento = new String[6];
				Alinhamento[0] = "left";
				Alinhamento[1] = "center";
				Alinhamento[2] = "center";
				Alinhamento[3] = "center";
				Alinhamento[4] = "center";
				Alinhamento[5] = "center";         

				Largura = new String[6];
				Largura[0] = "163";
				Largura[1] = "50";         
				Largura[2] = "140";
				Largura[3] = "65";
				Largura[4] = "65";
				Largura[5] = "60";

				Filtros = new short[6];
				Filtros[0] = 1;
				Filtros[1] = 1;         
				Filtros[2] = 1;
				Filtros[3] = 0;
				Filtros[4] = 0;
				Filtros[5] = 0;
			}else
			{
				Header = new String[5];
				Header[0] = "Relat&oacute;rio";
				Header[1] = "Perfil";         
				Header[2] = "Data de Gera&ccedil;&atilde;o";
				Header[3] = "Detalhar";
				if (DefsComum.s_CLIENTE.equals("Telemig") || DefsComum.s_CLIENTE.equals("Telecelular_Sul"))
					Header[4] = "Visualizar";
				else
					Header[4] = "Apresentar";
        
				Alinhamento = new String[5];
				Alinhamento[0] = "left";
				Alinhamento[1] = "center";
				Alinhamento[2] = "center";
				Alinhamento[3] = "center";
				Alinhamento[4] = "center";

				Largura = new String[5];
				Largura[0] = "163";
				Largura[1] = "50";         
				Largura[2] = "140";
				Largura[3] = "95";
				Largura[4] = "95";

				Filtros = new short[5];
				Filtros[0] = 1;
				Filtros[1] = 1;         
				Filtros[2] = 1;
				Filtros[3] = 0;
				Filtros[4] = 0;

			}
      }

      Linhas = montaLinhas();
      m_Html.setTabela((short)Header.length, true);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      if (SubOperacao == null)
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaRelHistoricos&perfil="+m_Perfil+"&tiporel="+m_TipoRel);
      else
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaRelHistoricos&perfil="+m_Perfil+"&suboperacao="+SubOperacao+"&tiporel="+m_TipoRel+"&nomesrels="+m_NomeRels+"&datasrels="+m_DataRels);
         
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
