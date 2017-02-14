//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpPesquisaRelatorios.java

package Portal.Operacoes;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.UsuarioDef;


public class OpPesquisaRelatorios extends OperacaoAbs 
{
   private String m_TiposRel = null;
   private String m_Perfis = null;
   private String m_NomeRel = null;
   private String m_DataGeracao = null;
   private String m_DataColeta = null;
   private String m_PeriodoColeta = null;
   private String m_Bilhetadores = null;
   private String m_Origens = null;
   private String m_Destinos = null;
   private String m_RotasEnt = null;
   private String m_RotasSai = null;
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3FE09438030A
    */
   public OpPesquisaRelatorios() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3FE094B6012B
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      String SubOperacao = null;
      
      try
      {
         setOperacao("Pesquisa de Relatórios");
         SubOperacao = m_Request.getParameter("suboperacao");
         
         if (SubOperacao == null)
         {
            iniciaArgs(7);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "formgen.htm";
            m_Args[2] = "src=\"/PortalOsx/templates/js/pesquisarel.js\"";
            m_Args[3] = "onLoad=\"Processa(0)\"";
            m_Args[4] = "pesquisarel.gif";
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "pesquisarel.form", montaFormulario(p_Mensagem));
            m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "pesquisarel.txt");
            m_Html.enviaArquivo(m_Args);
         }
         else if (SubOperacao.equals("iniciaAvancada")) 
         {
            iniciaArgs(7);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "formgen.htm";
            m_Args[2] = "src=\"/PortalOsx/templates/js/pesquisarel2.js\"";
            m_Args[3] = "onLoad=\"Processa(0)\"";
            m_Args[4] = "pesquisarelavancada.gif";
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "pesquisarel2.form", montaFormulario(p_Mensagem));
            m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "pesquisarel2.txt");
            m_Html.enviaArquivo(m_Args);
         }
         else if (SubOperacao.equals("pesquisa")) 
         {
            m_TiposRel      = m_Request.getParameter("tiposrel");
            m_Perfis        = m_Request.getParameter("perfis");
            m_NomeRel       = m_Request.getParameter("nomerel");

            montaTabela(false);

         }
         else if (SubOperacao.equals("pesquisaAvancada")) 
         {
            m_TiposRel      = m_Request.getParameter("tiposrel");
            m_Perfis        = m_Request.getParameter("perfis");
            m_NomeRel       = m_Request.getParameter("nomerel");

            // Campos da pesquisa avancada
            m_DataGeracao   = m_Request.getParameter("datageracao");
            m_DataColeta    = m_Request.getParameter("datacoleta");           
            m_PeriodoColeta = m_Request.getParameter("periodocoleta");

            m_Bilhetadores  = m_Request.getParameter("bilhetadores");
            m_Origens       = m_Request.getParameter("origens");
            m_Destinos      = m_Request.getParameter("destinos");
            m_RotasEnt      = m_Request.getParameter("rotasent");
            m_RotasSai      = m_Request.getParameter("rotassai");

            montaTabela(true);
         }                      
      }                         
      catch (Exception Exc)     
      {                         
         System.out.println("OpPesquisaRelatorios - iniciaOperacao(): "+Exc);
         Exc.printStackTrace(); 
         return false;          
      }

      return true;
   }
   
   /**
    * @param p_PesquisaAvancada
    * @return Vector
    * @exception 
    * @roseuid 3FE094B60153
    */
   public Vector montaLinhas(boolean p_PesquisaAvancada) 
   {
      String NomeRel = null;
      String Perfil            = null;
      String TipoRel           = null;
      String IdRel             = null;
      String ArqRelatorio      = null;
      String DataGeracao       = null;
      String DataApresentada   = null;      
      int TipoArmazenamento = 0;

      String IdPerfil = null;
      UsuarioDef Usuario = null;
      Vector LinhasAux, Linhas = null, ColunaAux, Coluna;
      Vector ListaTipoRel = null;
      HashMap MapTipoRel = new HashMap();
      String TipoRelAux = null;
      String TipoRelIdAux = null;
      String DataGeracaoAux = null;

      // pega os tipos de relatorios e 
      // faz um map de tipo e nome do tipo de relatorio
      ListaTipoRel = NoUtil.getNo().getConexaoServUtil().getListaTipoRelatorios();
      for (int i=0; i< ListaTipoRel.size() ; i++ )
      {
         StringTokenizer st = 
            new StringTokenizer((String)ListaTipoRel.elementAt(i), ":");

         TipoRelIdAux = (String) st.nextElement();
         TipoRelAux   = (String) st.nextElement();

         MapTipoRel.put(TipoRelIdAux, TipoRelAux);
      }


      //pega o usuario e o ID do Perfil
      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      IdPerfil = "" + Usuario.getIdPerfil();

      No no = NoUtil.buscaNobyIDPerfil(Usuario.getIdPerfil());
      // Cria lista
      Linhas = new Vector();

      if (p_PesquisaAvancada)
         LinhasAux = no.getConexaoServUtil().pesquisaRelAvancada( IdPerfil,
                                                      m_TiposRel,
                                                      m_Perfis,
                                                      m_NomeRel,
                                                      m_DataGeracao,
                                                      m_DataColeta,
                                                      m_PeriodoColeta,
                                                      m_Bilhetadores,
                                                      m_Origens,
                                                      m_Destinos,
                                                      m_RotasEnt,
                                                      m_RotasSai );
      else
         LinhasAux = no.getConexaoServUtil().pesquisaRel( IdPerfil,
                                              m_TiposRel,
                                              m_Perfis,
                                              m_NomeRel );
         
      if (LinhasAux == null || LinhasAux.size() <= 0)
      {
         Coluna = new Vector();
         Coluna.add("N&atilde;o foi encontrado nenhum relat&oacute;rio.");
         Coluna.add("&nbsp;");
         Coluna.add("&nbsp;");            
         Coluna.add("&nbsp;");            
         Coluna.trimToSize();
         Linhas.add(Coluna);                 
      }
      else
      {
         for (int i = 0; i < LinhasAux.size(); i++)
         {               
            // pegando o valor das colunas de uma linha
            ColunaAux = (Vector)LinhasAux.elementAt(i);
            
            // pegando o valor de cada coluna
            NomeRel           = (String)ColunaAux.elementAt(0);
            TipoRel           = (String)ColunaAux.elementAt(1);
            IdRel             = (String)ColunaAux.elementAt(2);
            Perfil            = (String)ColunaAux.elementAt(3);
            ArqRelatorio      = (String)ColunaAux.elementAt(4);
            DataGeracao       = (String)ColunaAux.elementAt(5);
            DataApresentada   = new String(DataGeracao);
            if (DataApresentada.indexOf("!") != -1)
               DataApresentada = DataApresentada.substring(0, DataApresentada.indexOf("!"));
               
            //DataArmazenamento = DataGeracao.substring(DataGeracao.indexOf('*')+1, DataGeracao.length());
            //DataGeracao       = DataGeracao.substring(0, DataGeracao.indexOf('*'));
            TipoArmazenamento = Integer.parseInt((String)ColunaAux.elementAt(6));

            Coluna = new Vector();

            if (TipoArmazenamento == 0)
               Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+Perfil+"','"+TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"','"+TipoArmazenamento+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");
            else if (TipoArmazenamento == 1)
               Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+Perfil+"','"+TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+/*DataArmazenamento*/DataGeracao+"','"+TipoArmazenamento+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");
            
            Coluna.add(Perfil);
            
            TipoRelAux = "" + TipoArmazenamento + "-" + TipoRel;
            TipoRel = (String)MapTipoRel.get(TipoRelAux);
            if (TipoRel == null)
            {
               System.out.println("Tipo de relatorio nao encontrado:" + TipoRelAux);
               TipoRel = "";
            }
            Coluna.add(TipoRel);
            Coluna.add(DataApresentada); //DataGeracao);           
            Coluna.trimToSize();
            Linhas.add(Coluna);
         }
      }
      return Linhas;
   }
   
   /**
    * @param p_PesquisaAvancada
    * @return void
    * @exception 
    * @roseuid 3FE094B60171
    */
   public void montaTabela(boolean p_PesquisaAvancada) 
   {
      Vector Linhas        = null;
      String Alinhamento[] = null;
      String Header[]      = null;
      String Largura[]     = null;
      String Args[]        = null;
      short Filtros[]      = null;

      Header = new String[4];
      Header[0] = "Relat&oacute;rio";
      Header[1] = "Perfil";
      Header[2] = "Tipo do Relat&oacute;rio";
      Header[3] = "Data de Gera&ccedil;&atilde;o";

      Alinhamento = new String[4];
      Alinhamento[0] = "left";
      Alinhamento[1] = "center";
      Alinhamento[2] = "center";
      Alinhamento[3] = "center";
   
      Largura = new String[4];         
      Largura[0] = "173";
      Largura[1] = "100";
      Largura[2] = "145";
      Largura[3] = "145";
   
      Filtros = new short[4];
      Filtros[0] = 0;
      Filtros[1] = 0;
      Filtros[2] = 0;
      Filtros[3] = 0;

      Linhas = montaLinhas(p_PesquisaAvancada);
      
      m_Html.setTabela((short)Header.length, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros); 
      
      // procurar maneira melhor para ordenar
      // m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?" );                           

      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);

      m_Html.m_Tabela.enviaTabela2String();
     

      Args = new String[1];
      Args[0] = m_Html.m_Tabela.getTabelaString();

      m_Args = new String[7];
      m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
      m_Args[1] = "listagen.htm";
      m_Args[2] = "src=\"/PortalOsx/templates/js/listarelpesquisa.js\"";
      m_Args[3] = "";
      m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listapesquisarel.form", Args);
      m_Args[4] = "listapesquisarel.gif";
      m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listapesquisarel.txt", null);
      m_Html.enviaArquivo(m_Args);
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3FE0A3EF03C3
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[]           = new String[4];
      String IdPerfil         = new String();
      Vector ListaPerfis      = new Vector(20);
      Vector ListaTipoRel     = null;
      Vector Permissoes       = null;
      Vector PerfisPermitidos = new Vector();
      PerfilCfgDef Perfil     = null;    
      UsuarioDef Usuario      = null;
      int QtdElem = 0;
      
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          ListaPerfis.addAll(noTmp.getConexaoServUtil().getListaPerfisOtimizado());
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
      
      QtdElem = ListaPerfis.size();

      Args[0] = new String();
      Args[1] = new String();
      Args[2] = new String();
      Args[3] = new String();

      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());

      if (Usuario.getUsuario().equals(DefsComum.s_USR_ADMIN))
      {
         // Monta lista com nomes de todos perfis
         for (int i = 0; i < QtdElem; i++)
         {
            Perfil = (PerfilCfgDef) ListaPerfis.elementAt(i);
            Args[0] += Perfil.getPerfil() + ";";
            Args[1] += "" + Perfil.getId() + ";";
         }
      }else
      {
         // Adiciona o Perfil Logado
         Args[0] += Usuario.getPerfil()   + ";";
         Args[1] += Usuario.getIdPerfil() + ";";
         PerfisPermitidos.addElement(""+Usuario.getIdPerfil());

         
         // Monta a lista com nomes dos perfis permitidos para o usuario
         Permissoes = Usuario.getNo().getConexaoServUtil().getPermissoesRel();
         QtdElem = Permissoes.size();         

         for (int i=0; i<QtdElem; i++)
         {
            StringTokenizer st = new StringTokenizer((String)Permissoes.elementAt(i), ":");
            if ( ((String)st.nextElement()).startsWith(""+Usuario.getIdPerfil()))
            {
               StringTokenizer st2 = new StringTokenizer((String)st.nextElement(), ";" );
               while (st2.hasMoreTokens())
               {
                  // procurando o nome do perfil
                  IdPerfil = (String)st2.nextElement();
                  Perfil = getPerfilbyId(IdPerfil, ListaPerfis);
                  if (Perfil == null)
                  {
//                     System.out.println("------------------------------------------------\n"
//                                      + "Perfil " + IdPerfil + " nao encontrado.         \n"
//                                      + "Arquivo PermissoesRel.txt pode estar corrompido.\n"
//                                      + "------------------------------------------------\n");
                     continue;
                  }
                  if (PerfisPermitidos.contains(IdPerfil))                  
                     continue;
                  else
                     PerfisPermitidos.addElement(IdPerfil);

                  // montando os argumentos
                  Args[0] += Perfil.getPerfil() + ";";
                  Args[1] += ""+ Perfil.getId() + ";"; 
               }              
            }
         }
      }

      //Tipos de Relatorio
      ListaTipoRel = NoUtil.getNo().getConexaoServUtil().getListaTipoRelatorios();
      for (int i=0; i < ListaTipoRel.size(); i++)
      {
         Args[2] += (String)ListaTipoRel.elementAt(i);
         Args[2] += ";";         
      }

      // Removendo o ultimo ";"
      Args[0] = Args[0].substring(0, Args[0].length() - 1);
      Args[1] = Args[1].substring(0, Args[1].length() - 1);
      Args[2] = Args[2].substring(0, Args[2].length() - 1);
        
      Args[3] += p_Mensagem;
      
      return Args;
   }
   
   /**
    * @param Id
    * @return PerfilCfgDef
    * @exception 
    * @roseuid 3FE201B50253
    */
   public PerfilCfgDef getPerfilbyId(String Id, Vector Perfis) 
   {
      PerfilCfgDef Perfil = null;
           
      for (int j=0; j<Perfis.size(); j++)
      {
         Perfil = (PerfilCfgDef)Perfis.elementAt(j);
         if (Id.equals(""+Perfil.getId()))
            return Perfil;         
      }

      return null;
   }
}
