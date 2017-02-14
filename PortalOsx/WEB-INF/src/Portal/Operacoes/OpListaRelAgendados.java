//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaRelAgendados.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.UsuarioDef;


public class OpListaRelAgendados extends OperacaoAbs 
{
   private Vector m_Arquivos = null;
   private String m_Perfil = null;
   private String m_TipoRel = null;
   private boolean m_PodeApagar = false;
   private String m_NomeRels = null;
   private String m_DataRels = null;
   private UsuarioDef usuario = null;
   private boolean isFraude = false;
   private Vector todosPerfis = null;
   private int menosUm = 0;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C7006870031
    */
   public OpListaRelAgendados() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C700687004F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         String Args[] = new String[6];
         String SubOperacao = m_Request.getParameter("suboperacao");
         
         
         
         setOperacao("Lista Relatórios Agendados");
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

         usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         if((usuario.getAcesso()!= null)&&(usuario.getAcesso().equalsIgnoreCase("fraude") || usuario.getAcesso().equalsIgnoreCase("adminFraude")))
         {
        	 isFraude = true;
        	 menosUm = -1;
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
         m_Args[2] = "src=\"/PortalOsx/templates/js/listarelagendados.js\""; // javascript
         if (SubOperacao == null)
         {
            m_Args[3] = "onLoad=\"DesmarcaDatas()\"";
            if(Integer.parseInt(m_TipoRel)==30)
            {
            	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelagendadosIMEI.form", Args);
            }
            else
            {
            	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelagendados3.form", Args);
            }
            
         }
         else
         {
            m_Args[3] = "";//"onLoad=\"PreloadImages('/PortalOsx/imagens/lixo0.gif','/PortalOsx/imagens/lixo1.gif'); IniciaDelecao()\"";
            if (m_PodeApagar)
               m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelagendados.form", Args);
            else
               m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listarelagendados2.form", Args);

         }

         switch (Integer.parseInt(m_TipoRel))
         {
            case 0:
               m_Args[4] = "relagendadodesempenho.gif\"";
               break;
            case 1:
               m_Args[4] = "relagendadopesquisadecodigo.gif\"";
               break;
            case 2:
               m_Args[4] = "relagendadodetalhedechamada.gif\"";
               break;
            case 8:
                m_Args[4] = "relagendadoanatelsmp3.gif\"";
                break;
             case 9:
                m_Args[4] = "relagendadoanatelsmp5.gif\"";
                break;
             case 10:
                m_Args[4] = "relagendadoanatelsmp6.gif\"";
                break;
             case 11:
                m_Args[4] = "relagendadoanatelsmp7.gif\"";
                break;
            case 13:
               m_Args[4] = "relagendadomatraf.gif\"";
               break;
            case 14:
               m_Args[4] = "relagendadoanatelsmp3.gif\"";
               break;
            case 15:
               m_Args[4] = "relagendadoanatelsmp5.gif\"";
               break;
            case 16:
               m_Args[4] = "relagendadoanatelsmp6.gif\"";
               break;
            case 17:
               m_Args[4] = "relagendadoanatelsmp7.gif\"";
               break;
            case 18:
               m_Args[4] = "relagendadoanatelldn.gif\"";
               break;               
            case 19:
               m_Args[4] = "relagendadoanalisecompletamento.gif\"";
               break;
            case 20:
               m_Args[4] = "relagendadointerconexaoaudit.gif\"";
               break;
            case 21:
               m_Args[4] = "relagendadointerconexaoforarota.gif\"";
               break;
            case 22:
               m_Args[4] = "relagendadominutosdeuso.gif\"";
               break;
            case 23:
                m_Args[4] = "relaagendadoresumochamadas.gif\"";
                break;
            case 24:
                m_Args[4] = "relaagendadodistribuicaofrequencia.gif\"";
                break;
            case 25:
                m_Args[4] = "relaagendadoperseveranca.gif\"";
                break;                
            case 30:
            	m_Args[4] = "relagendadoimei.gif\"";
            	break;
            case 31:
            	m_Args[4] = "relagendadolongaduracao.gif\"";
                break;
            case 32:
            	m_Args[4] = "relagendadestespec.gif\"";
                break;
            case 33:
            	m_Args[4] = "relagendadosdestporgrupos.gif\"";
                break;
            case 34:
            	m_Args[4] = "relagendadoschamcelula.gif\"";
                break;
            case 35:
            	m_Args[4] = "relagendadosprefixosriscos.gif\"";
            	break;
            case 36:
            	m_Args[4] = "relagendadodesempenhoderede.gif\"";
            	break;
            case 37:
            	m_Args[4] = "relagendadoqos.gif\"";
            	break;
            case 43:
            	m_Args[4] = "relagendadodwgprs.gif\"";
            	break;
            case 44:
            	m_Args[4] = "relagendadodwgeral.gif\"";
            	break;
            case 45:
            	m_Args[4] = "relagendadotrend.gif\"";
            	break;
            case 46:
            	m_Args[4] = "relagendadogre.gif\"";
            	break;
            case 47:
            	m_Args[4] = "relagendadoreceita.gif\"";
            	break;
            case 48:
            	m_Args[4] = "relagendadodespesa.gif\"";
            	break;
            case 49:
            	m_Args[4] = "relagendadoagrupado.gif\"";
            	break;
            case 50:
            	m_Args[4] = "relagendadoanatelsmp3e4.gif\"";
            	break;
            case 51:
            	m_Args[4] = "relagendadoanatelsmp8e9.gif\"";
            	break;
         }

         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listarelagendados.txt", null);
         m_Html.enviaArquivo(m_Args);

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaRelAgendados - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();         
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C71ADFC01DA
    */
   public Vector montaLinhas() 
   {
      String NomeRel, SubOperacao = null;
      Vector LinhasAux = null, Linhas = null, ColunaAux, Coluna;

      // Recupera parametros
      SubOperacao = m_Request.getParameter("suboperacao");

      if (SubOperacao == null)
      {
         // Cria lista
         Linhas = new Vector();
         No noTmp = NoUtil.buscaNobyNomePerfil(m_Perfil);
         /**
          * Caso seja o usuario fraude ele pode ver todos os relatorios do admin fraude
          * supondo que existe somente um usuario administrador fraude
          * */
         if((usuario.getAcesso()!= null)&&(usuario.getAcesso().equalsIgnoreCase("fraude")))
         {
        	 todosPerfis = noTmp.getConexaoServUtil().getListaPerfisCfg();
        	 for(int i=0; i<todosPerfis.size(); i++)
        	 {
        		 if(((PerfilCfgDef)todosPerfis.get(i)).getAcessoNome().equalsIgnoreCase("adminFraude"))
        		 {
        			 m_Perfil = ((PerfilCfgDef)todosPerfis.get(i)).getPerfil();
        			 LinhasAux = noTmp.getConexaoServUtil().getListaRelArmazenados2((short) 0, ((PerfilCfgDef)todosPerfis.get(i)).getPerfil(), Short.parseShort(m_TipoRel), (short)0, "", "",NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil());
        		 }
        	 }
         }
         else
         {
        	 LinhasAux = noTmp.getConexaoServUtil().getListaRelArmazenados2((short) 0, m_Perfil, Short.parseShort(m_TipoRel), (short)0, "", "",NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil());
         }

         if (LinhasAux != null)
         {
            Character Chave;
            Object Obj = null;
            String Datas = "", QtdArquivos, Data, Aux, NomeRelAux;
            String ArqRelatorio, IdRel, DataGeracao;
            Vector vDatasQtd = null, vDatas;
            int QtdDatas = 0;
            for (int i = 0; i < LinhasAux.size(); i++)
            {
               //Formato do elemento do vetor: Perfil - NomeRelatorio - ArqRelatorio - Datas (Data - QtdArq) (vetor)
               Datas = "";
              
               ColunaAux = (Vector)LinhasAux.elementAt(i);
               NomeRel = (String)ColunaAux.elementAt(1);
               ArqRelatorio = (String)ColunaAux.elementAt(2);
               DataGeracao = (String)ColunaAux.elementAt(3);
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

               NomeRelAux = "";
               NomeRelAux = new String(NomeRel);

               for (int y = 0; y < NomeRelAux.length(); y++)
               {
                  Chave = new Character(NomeRelAux.charAt(y));
                  Obj = DefsComum.s_Map_CaracEspec_CaracCorresp.get(Chave);
                  if (Obj != null)
                  {
                     NomeRelAux = NomeRelAux.substring(0, y) + (String)Obj + NomeRelAux.substring(y+1, NomeRelAux.length());
                     y += (((String)Obj).length()) - 1;
                  }
               }

               if (m_TipoRel.equals("12"))
                  //Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");
                  Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRelAux+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRelAux+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRelAux+"</a>");
               else
//            	   Coluna.add("<a href=\"javascript:AbreJanela('"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRelAux+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRelAux+"</a>");
            	 
//                  Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRel+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRel+"</a>");
                  Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRelAux+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório "+NomeRelAux+" mais recente';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+NomeRelAux+"</a>");
               
               if(!isFraude)
               {
            	   Coluna.add(m_Perfil);
            	  
               }
               
               Coluna.add(Datas);

					if (m_PodeApagar)
						//Coluna.add("<a href=\"javascript:AbreJanela('listaapagar','"+m_Perfil+"','"+m_TipoRel+"','','','"+NomeRel+"');\" onmouseover=\"window.status='Selecione relatórios para excluir';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
						Coluna.add("<a href=\"javascript:AbreJanela('listaapagar','"+m_Perfil+"','"+m_TipoRel+"','','','"+NomeRelAux+"');\" onmouseover=\"window.status='Selecione relatórios para excluir';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");                  

               Coluna.trimToSize();
               Linhas.add(Coluna);
            }
         }
         else
         {
            Coluna = new Vector();
            Coluna.add("N&atilde;o h&aacute; relat&oacute;rios agendados.");
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
         String ArqRelatorio, Relatorio, Data, DataGeracao, IdRel;
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
         No noTmp = NoUtil.buscaNobyNomePerfil(m_Perfil);
         LinhasAux = noTmp.getConexaoServUtil().getListaRelArmazenados2((short) 0, m_Perfil, Short.parseShort(m_TipoRel), (short)0, Relatorio, Data,NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil());
         
         if (LinhasAux != null)
         {
            m_Arquivos = new Vector();
            for (int i = 0; i < LinhasAux.size(); i++)
            {
               ColunaAux = (Vector)LinhasAux.elementAt(i);
               NomeRel = (String)ColunaAux.elementAt(1);
               vDatas = (Vector)ColunaAux.elementAt(2);

               m_Arquivos.add(ColunaAux.elementAt(1)+","+ColunaAux.elementAt(2)+","+ColunaAux.elementAt(0));

               for (int j = 0; j < vDatas.size(); j++)
               {
                  DataGeracao = (String)vDatas.elementAt(j);

                  ArqRelatorio = DataGeracao.substring(0, DataGeracao.indexOf('='));

                  DataGeracao = DataGeracao.substring(DataGeracao.indexOf('=')+1, DataGeracao.length());

                  if (ArqRelatorio.indexOf('-') != -1)
                     IdRel = ArqRelatorio.substring(ArqRelatorio.indexOf('-')+1, ArqRelatorio.indexOf('.'));
                  else
                     IdRel = ArqRelatorio.substring(0, ArqRelatorio.indexOf('.'));

                  Coluna = new Vector();
                  Coluna.add(NomeRel);
                  if(!isFraude)
                  {
                	  Coluna.add(m_Perfil);
                  }
                  
                  Coluna.add(DataGeracao);
                  Coluna.add("<a href=\"javascript:AbreJanela('detalhar', '"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para ver detalhes do relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/reldetalha.gif\" border=\"0\"></a>");
                  /*if (DefsComum.s_CLIENTE.equals("Telemig"))
                     Coluna.add("<a href=\"javascript:AbreJanela('visualizar','"+m_Perfil+"','"+m_TipoRel+"','"+ColunaAux.elementAt(2)+"','"+ColunaAux.elementAt(0)+"')\" onmouseover=\"window.status='Clique para visualizar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                  else
                  {*/
                  if (m_TipoRel.equals("12"))
                     Coluna.add("<a href=\"javascript:AbreJanela('apresentartrafego','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                  else
                     Coluna.add("<a href=\"javascript:AbreJanela('apresentar','"+m_Perfil+"','"+m_TipoRel+"','"+IdRel+"','"+ArqRelatorio+"','"+NomeRel+"','"+DataGeracao+"')\" onmouseover=\"window.status='Clique para apresentar o relatório';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/relvisualiza.gif\" border=\"0\"></a>");
                  //}

                  if (m_PodeApagar)						
                	  Coluna.add("<a href=\"javascript:AbreJanela('listaapagar','"+m_Perfil+"','"+m_TipoRel+"','','','"+NomeRel+"');\" onmouseover=\"window.status='Selecione relatórios para excluir';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");

                  Coluna.trimToSize();
                  Linhas.add(Coluna);
               }
            }
         }
         else
         {        
            Coluna = new Vector();
            Coluna.add("N&atilde;o h&aacute; relat&oacute;rios agendados.");
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
      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C71ADFC01EE
    */
   public void montaTabela() 
   {
      Vector Linhas = null;
      String Alinhamento[] = null, Header[] = null, Largura[] = null;
      short Filtros[] = null;
      String SubOperacao = m_Request.getParameter("suboperacao");   
      int i = 0; //contador
      
      if (SubOperacao == null)
      {
			if (m_PodeApagar)
			{
				Header = new String[4+menosUm];
				if(isFraude)
				{
					Header[i++] = "Relat&oacute;rio";
				}
				else
				{
					Header[i++] = "Nome de Pesquisa";
				}
				
				if(!isFraude)
			    {
					Header[i++] = "Perfil";
			    }
				
				Header[i++] = "Datas de Gera&ccedil;&atilde;o";
				Header[i++] = "Apagar";
         
				i = 0;
				
				Alinhamento = new String[4+menosUm];
				Alinhamento[i++] = "left";
				if(!isFraude)
			    {
					Alinhamento[i++] = "center";
			    }
				
				Alinhamento[i++] = "center";
				Alinhamento[i++] = "center";
         
				i = 0;
				
				Largura = new String[4+menosUm];         
				Largura[i++] = "173";
				if(!isFraude)
			    {
					Largura[i++] = "100";
			    }
				
				Largura[i++] = "190";
				Largura[i++] = "80";
         
				i = 0;
				
				Filtros = new short[4+menosUm];
				Filtros[i++] = 1;
				if(!isFraude)
			    {
					Filtros[i++] = 1;
			    }
				
				Filtros[i++] = 0;
				Filtros[i++] = 0;
			}
			else
			{
				Header = new String[3+menosUm];
				i = 0;
				if(isFraude)
				{
					Header[i++] = "Relat&oacute;rio";
				}
				else
				{
					Header[i++] = "Nome de Pesquisa";
				}
				
				if(!isFraude)
			    {
					Header[i++] = "Perfil";
			    }
				
				Header[i++] = "Datas de Gera&ccedil;&atilde;o";
         
				i = 0;
				
				Alinhamento = new String[3+menosUm];
				Alinhamento[i++] = "left";
				if(!isFraude)
			    {
					Alinhamento[i++] = "center";
			    }
				
				Alinhamento[i++] = "center";
         
				i = 0;
				
				Largura = new String[3+menosUm]; 				
				Largura[i++] = "173";
				if(!isFraude)
			    {
					Largura[i++] = "140";
			    }
				
				Largura[i++] = "230";
         
				i = 0;
								
				Filtros = new short[3+menosUm];
				Filtros[i++] = 1;
				if(!isFraude)
			    {
					Filtros[i++] = 1;
			    }
				
				Filtros[i++] = 0;			
			}
      }
      else if (SubOperacao.equals("listar"))
      {
			if (m_PodeApagar)
			{
				i = 0;
				Header = new String[6+menosUm];
				if(isFraude)
				{
					Header[i++] = "Relat&oacute;rio";
				}
				else
				{
					Header[i++] = "Nome";
				}
				
				if(!isFraude)
				{
					Header[i++] = "Perfil";
				}
				
				Header[i++] = "Data de Gera&ccedil;&atilde;o";
				Header[i++] = "Detalhar";
				if (DefsComum.s_CLIENTE.equals("Telemig") || DefsComum.s_CLIENTE.equals("Telecelular_Sul"))
					Header[i++] = "Visualizar";
				else
					Header[i++] = "Apresentar";
				Header[i++] = "Apagar";
        
				i = 0;
				Alinhamento = new String[6+menosUm];
				Alinhamento[i++] = "left";
				if(!isFraude)
				{
					Alinhamento[i++] = "center";
				}
				
				Alinhamento[i++] = "center";
				Alinhamento[i++] = "center";
				Alinhamento[i++] = "center";
				Alinhamento[i++] = "center";     
				
				i = 0;

				Largura = new String[6+menosUm];
				Largura[i++] = "163";
				if(!isFraude)
				{
					Largura[i++] = "50"; 
				}
				
				Largura[i++] = "140";
				Largura[i++] = "65";
				Largura[i++] = "65";
				Largura[i++] = "60";

				i = 0;
				
				Filtros = new short[6+menosUm];
				Filtros[i++] = 1;
				if(!isFraude)
				{
					Filtros[i++] = 1;
				}
				
				Filtros[i++] = 1;
				Filtros[i++] = 0;
				Filtros[i++] = 0;
				Filtros[i++] = 0;
			}else
			{
				i = 0;
				
				Header = new String[5+menosUm];
				if(isFraude)
				{
					Header[i++] = "Relat&oacute;rio";
				}
				else
				{
					Header[i++] = "Nome";
				}
				if(!isFraude)
				{
					Header[i++] = "Perfil";
				}
				
				Header[i++] = "Data de Gera&ccedil;&atilde;o";
				Header[i++] = "Detalhar";
				if (DefsComum.s_CLIENTE.equals("Telemig") || DefsComum.s_CLIENTE.equals("Telecelular_Sul"))
					Header[i++] = "Visualizar";
				else
					Header[i++] = "Apresentar";
				
				i = 0;
        
				Alinhamento = new String[5+menosUm];
				Alinhamento[i++] = "left";
				if(!isFraude)
				{
					Alinhamento[i++] = "center";
				}
				
				Alinhamento[i++] = "center";
				Alinhamento[i++] = "center";
				Alinhamento[i++] = "center";

				i = 0;
				
				Largura = new String[5+menosUm];
				Largura[i++] = "163";
				if(!isFraude)
				{
					Largura[i++] = "50";
				}
				
				Largura[i++] = "140";
				Largura[i++] = "95";
				Largura[i++] = "95";

				i = 0;
				
				Filtros = new short[5+menosUm];
				Filtros[i++] = 1;
				if(!isFraude)
				{
					Filtros[i++] = 1;
				}
				
				Filtros[i++] = 1;
				Filtros[i++] = 0;
				Filtros[i++] = 0;

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
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaRelAgendados&perfil="+m_Perfil+"&tiporel="+m_TipoRel);
      else
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaRelAgendados&perfil="+m_Perfil+"&suboperacao="+SubOperacao+"&tiporel="+m_TipoRel+"&nomesrels="+m_NomeRels+"&datasrels="+m_DataRels);
         
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
