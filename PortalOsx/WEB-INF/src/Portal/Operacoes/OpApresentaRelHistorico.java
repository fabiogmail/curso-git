//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApresentaRelHistorico.java
package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.HTMLUtil;
import Portal.Utils.Historico;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

// import Componentes.*;

public class OpApresentaRelHistorico extends OperacaoAbs
{
	static
	{
		
	}
	//essas variaveis estao sendo criadas para controlar o refresh quando é escolhido 
	// novos periodos e grafico esta parado
	private static String guardaVarAtualizaGrafico = "";
	private static String guardaVarAtipoOperacao = "";
	private static boolean passouPeloPeriodo = false;
	private static boolean passouPeloIndicadores = false;
	private static boolean finalHistorico = false;
	//--------------------------
	//essas variaveis sao usadas para vazer os recursos ficarem certo quando o undozoom
	// é chamado
	private static String valorDoRecurso1VoltaZoom = ""; 
	private static String valorDoRecurso2VoltaZoom = "";
	private static boolean passouPeloVoltaZoom = false;
	//-----------------------------------------
	//essa variavel é para receber a variavel 'linhaDuplicadoArquivo' do Historico, e será usada
	//para desidir quando vai ser colocada a mensagem de erro.
	private boolean linhaDuplicadaNoArquivo = false;
	//-----------------------------------------	
	private String m_SubOperacao;
	private String m_NomeRelatorio;
	private String m_DataGeracao;
	private String m_Perfil;
	private String m_TipoRelatorio;
	private String m_IdRelatorio;
	private String m_NomeArquivoRelatorio;                                                                                                                                                                                                                                                    
	private String m_KeyCache;

	public OpApresentaRelHistorico()
	{
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3EDB7C3E019E
	 */
	public boolean iniciaOperacao(String p_Mensagem)
	{
		setOperacao("Apresenta&ccedil;&atilde;o de Relat&oacute;rio Hist&oacute;rico");

		// Recupera os argumentos passados pela página
		m_SubOperacao = m_Request.getParameter("suboperacao");
		m_NomeRelatorio = m_Request.getParameter("nomerel");
		m_DataGeracao = m_Request.getParameter("datageracao");
		m_Perfil = m_Request.getParameter("perfil");
		m_TipoRelatorio = m_Request.getParameter("tiporel");
		m_IdRelatorio = m_Request.getParameter("idrel");
		m_NomeArquivoRelatorio = m_Request.getParameter("arquivo");

		m_NomeRelatorio = m_NomeRelatorio.replace('@', ' ');

		String teste = m_Request.getParameter("atualizaGrafico");
		String teste2= m_Request.getParameter("tipo");
		
		System.out.println(teste);
		
		if(teste != null){
			guardaVarAtualizaGrafico = teste;
		}
		if(teste2 != null){
			guardaVarAtipoOperacao = teste2;
		}
		// Monta a chave para inserção e busca na cache de históricos
		m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" + m_NomeRelatorio +
					 "-" + m_IdRelatorio + "-" + m_NomeArquivoRelatorio + "-" +
					 m_Request.getSession().getId();

		if (m_SubOperacao.toLowerCase().equals("paginicial"))
		{
			apresentaPagInicial();
		}
		else if (m_SubOperacao.toLowerCase().equals("paginicial2"))
		{
			apresentaPagInicial2();
		}
		else if (m_SubOperacao.toLowerCase().equals("relatorio"))
		{
			apresentaRelatorio();
		}
		else if (m_SubOperacao.toLowerCase().equals("meiorelatorio"))
		{
			apresentaMeioRelatorio();
		}
		else if (m_SubOperacao.toLowerCase().equals("relatorioesquerda"))
		{
			apresentaRelatorioEsquerda();
		}
		else if (m_SubOperacao.toLowerCase().equals("relatoriodireita"))
		{
			apresentaRelatorioDireita();
		}
		else if (m_SubOperacao.toLowerCase().equals("toporelatorioesquerda"))
		{
			apresentaTopoRelatorioEsquerda();
		}
		else if (m_SubOperacao.toLowerCase().equals("meiorelatorioesquerda"))
		{
			apresentaMeioRelatorioEsquerda();
		}
		else if (m_SubOperacao.toLowerCase().equals("baserelatoriodireita"))
		{
			apresentaBaseRelatorioDireita();
		}
		else if (m_SubOperacao.toLowerCase().equals("benchmarking"))
		{
			apresentaBenchmarking();
		}
		else if (m_SubOperacao.toLowerCase().equals("valoresmedios"))
		{
			apresentaValoresMedios();
		}
		else if (m_SubOperacao.toLowerCase().equals("pagindicadores"))
		{
			apresentaIndicadores();
		}
		else if (m_SubOperacao.toLowerCase().equals("pagperiodos"))
		{
			apresentaPeriodos();
		}
		else if (m_SubOperacao.toLowerCase().equals("pagconfiguracao"))
		{
			apresentaConfiguracao();
		}
		else if (m_SubOperacao.toLowerCase().equals("pagrecursos"))
		{
			apresentaRecursos();
		}
		else if (m_SubOperacao.toLowerCase().equals("pagrelatorio"))
		{
			apresentaRelatorioFinal();
		}
		else if (m_SubOperacao.toLowerCase().equals("alterarecursos"))
		{
			alteraRecursos();
		}
		else if (m_SubOperacao.toLowerCase().equals("alteraindicadores"))
		{
			alteraIndicadores();
		}
		else if (m_SubOperacao.toLowerCase().equals("alteraperiodos"))
		{
			alteraPeriodos();
		}
		else if (m_SubOperacao.toLowerCase().equals("alteraconfiguracao"))
		{
			alteraConfiguracao();
		}
		else if (m_SubOperacao.toLowerCase().equals("home"))
		{
			home();
		}
		else if (m_SubOperacao.toLowerCase().equals("zoom"))
		{
			zoom();
		}
		else if (m_SubOperacao.toLowerCase().equals("undozoom"))
		{
			undoZoom();
		}
		else if (m_SubOperacao.toLowerCase().equals("navegacao"))
		{
			navega();
		}
		else if (m_SubOperacao.toLowerCase().equals("removehistorico"))
		{
			removeHistorico();
		}
		else
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:iniciaOperacao() - Suboperacao nao encontrada: " +
							   m_SubOperacao);
		}

		//teste pra ver se a performance melhora
		System.gc();
		
		return true;
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página index.htm
	 * @roseuid 3EDB7F86012E
	 */
	public void apresentaPagInicial()
	{
		try
		{
			iniciaArgs(9);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "index.htm";
			m_Args[2] = m_NomeRelatorio + " - " +
						((m_DataGeracao.indexOf('!') != -1)
						 ? m_DataGeracao.substring(0,
												   m_DataGeracao.indexOf('!'))
						 : m_DataGeracao);
			m_Args[3] = m_Perfil;
			m_Args[4] = m_TipoRelatorio;
			m_Args[5] = m_IdRelatorio;
			m_Args[6] = m_NomeArquivoRelatorio;
			m_Args[7] = m_NomeRelatorio;
			m_Args[8] = m_DataGeracao;
			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaPagInicial(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página index2.htm
	 * @roseuid 3EDB92A5006E
	 */
	public void apresentaPagInicial2()
	{
		try
		{
			iniciaArgs(9);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "index2.htm";
			m_Args[2] = m_NomeRelatorio + " - " +
						((m_DataGeracao.indexOf('!') != -1)
						 ? m_DataGeracao.substring(0,
												   m_DataGeracao.indexOf('!'))
						 : m_DataGeracao);
			m_Args[3] = m_Perfil;
			m_Args[4] = m_TipoRelatorio;
			m_Args[5] = m_IdRelatorio;
			m_Args[6] = m_NomeArquivoRelatorio;
			m_Args[7] = m_NomeRelatorio;
			m_Args[8] = m_DataGeracao;
			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaPagInicial2(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página relatorio.htm
	 * @roseuid 3EDCA55C02F5
	 */
	public void apresentaRelatorio()
	{
		try
		{
			iniciaArgs(14);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "relatorio.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			m_Args[8] = m_Perfil;
			m_Args[9] = m_TipoRelatorio;
			m_Args[10] = m_IdRelatorio;
			m_Args[11] = m_NomeArquivoRelatorio;
			m_Args[12] = m_NomeRelatorio;
			m_Args[13] = m_DataGeracao;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaRelatorio(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página relatorioesquerda.htm
	 * @roseuid 3EDB95F8031B
	 */
	public void apresentaRelatorioEsquerda()
	{
		try
		{
			iniciaArgs(20);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "relatorioesquerda.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			m_Args[8] = m_Perfil;
			m_Args[9] = m_TipoRelatorio;
			m_Args[10] = m_IdRelatorio;
			m_Args[11] = m_NomeArquivoRelatorio;
			m_Args[12] = m_NomeRelatorio;
			m_Args[13] = m_DataGeracao;

			m_Args[14] = m_Perfil;
			m_Args[15] = m_TipoRelatorio;
			m_Args[16] = m_IdRelatorio;
			m_Args[17] = m_NomeArquivoRelatorio;
			m_Args[18] = m_NomeRelatorio;
			m_Args[19] = m_DataGeracao;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaRelatorioEsquerda(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página relatoriodireita.htm
	 * @roseuid 3EDB95F9033B
	 */
	public void apresentaRelatorioDireita()
	{
		try
		{
			iniciaArgs(20);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "relatoriodireita.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			m_Args[8] = m_Perfil;
			m_Args[9] = m_TipoRelatorio;
			m_Args[10] = m_IdRelatorio;
			m_Args[11] = m_NomeArquivoRelatorio;
			m_Args[12] = m_NomeRelatorio;
			m_Args[13] = m_DataGeracao;

			m_Args[14] = m_Perfil;
			m_Args[15] = m_TipoRelatorio;
			m_Args[16] = m_IdRelatorio;
			m_Args[17] = m_NomeArquivoRelatorio;
			m_Args[18] = m_NomeRelatorio;
			m_Args[19] = m_DataGeracao;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaRelatorioDireita(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página toporelatorioesquerda.htm
	 * @roseuid 3EDB9AB40057
	 */
	public void apresentaTopoRelatorioEsquerda()
	{
		try
		{
			Historico Hist = null;
			int QtdRec1 = 0;
			int QtdRec2 = 0;
			String TipoRecurso1;
			String TipoRecurso2;
			String Recursos1 = null;
			String Recursos2 = null;
			String QtdRec = "1";
			String SelectRecurso1 = "";
			String SelectRecurso2 = "";
			String TipoRecurso = null;
			String Recursos = null;
			String RecursosSelecionados1 = null;
			String RecursosSelecionados2 = null;
			Vector ListaRecurso1 = null;
			Vector ListaRecurso2 = null;

			Hist = buscaHistorico();
			System.out.println("Hist: " + Hist);

			if (Hist == null)
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
				m_Html.enviaArquivo(m_Args);

				return;
			}

			TipoRecurso1 = Hist.fnTituloRecurso1();
			ListaRecurso1 = Hist.fnListaRecursosVec1();

			Vector list = Hist.getListaContadores();
			QtdRec1 = ListaRecurso1.size();

			TipoRecurso = m_Request.getParameter("tiporecurso");
			Recursos1 = m_Request.getParameter("recursos1");
			Recursos2 = m_Request.getParameter("recursos2");

			if ((TipoRecurso != null) && (TipoRecurso.equals("1") == true))
			{
				RecursosSelecionados1 = m_Request.getParameter("recursos");
			}
			else
			{
				RecursosSelecionados2 = m_Request.getParameter("recursos");
			}

			if (RecursosSelecionados1 == null)
			{
				RecursosSelecionados1 = Recursos1;
			}
			//so vai passar por aki quando a operacao feita for o volta zoom
			if(passouPeloVoltaZoom){
				RecursosSelecionados1 = valorDoRecurso1VoltaZoom;
			}

			if ((RecursosSelecionados1 == null) ||
				(RecursosSelecionados1.length() == 0))
			{
				RecursosSelecionados1 = ListaRecurso1.elementAt(0).toString();
			}

			SelectRecurso1 = "<b>" + TipoRecurso1 + ":</b>&nbsp;<a href=\"";
			SelectRecurso1 += ("javascript:AbreJanela('1','" +
			RecursosSelecionados1 + "')");

			if (RecursosSelecionados1.length() > 15)
			{
				Recursos = RecursosSelecionados1.substring(0, 15) + "...";
			}
			else
			{
				Recursos = RecursosSelecionados1;
			}

			SelectRecurso1 += ("\" class=\"link\">" + Recursos + "</a>");

			TipoRecurso2 = Hist.fnTituloRecurso2();

			if (TipoRecurso2 != null)
			{
				QtdRec = "2";
				ListaRecurso2 = Hist.fnListaRecursosVec2();
				QtdRec2 = ListaRecurso2.size();

				if (RecursosSelecionados2 == null)
				{
					RecursosSelecionados2 = Recursos2;
				}

				if ((RecursosSelecionados2 == null) ||
					(RecursosSelecionados2.length() == 0))
				{
					RecursosSelecionados2 = ListaRecurso2.elementAt(0).toString();
				}
				else
				{
					Recursos = ListaRecurso2.elementAt(0).toString();
				}

				//so vai passar por aki quando a operacao feita for o volta zoom
				if(passouPeloVoltaZoom){
					RecursosSelecionados2 = valorDoRecurso2VoltaZoom;
					
				}
				
				SelectRecurso2 = "&nbsp;&nbsp;&nbsp;<b>" + TipoRecurso2 +
								 ":</b>&nbsp;<a href=\"";
				SelectRecurso2 += ("javascript:AbreJanela('2','" +
				RecursosSelecionados2 + "')");

				if (RecursosSelecionados2.length() > 15)
				{
					Recursos = RecursosSelecionados2.substring(0, 15) + "...";
				}
				else
				{
					Recursos = RecursosSelecionados2;
				}

				SelectRecurso2 += ("\" class=\"link\">" + Recursos + "</a>");
			}

			iniciaArgs(18);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "toporelatorioesquerda.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;
			m_Args[8] = QtdRec;

			if (RecursosSelecionados1 != null)
			{
				m_Args[9] = RecursosSelecionados1;
			}
			else
			{
				m_Args[9] = "";
			}

			if (RecursosSelecionados2 != null)
			{
				m_Args[10] = RecursosSelecionados2;
			}
			else
			{
				m_Args[10] = "";
			}

			m_Args[11] = ListaRecurso1.elementAt(0).toString();

			if (ListaRecurso2 != null)
			{
				m_Args[12] = ListaRecurso2.elementAt(0).toString();
			}
			else
			{
				m_Args[12] = "-";
			}

			m_Args[13] = QtdRec1 + "";
			m_Args[14] = QtdRec2 + "";

			m_Args[15] = m_NomeRelatorio +
						 "&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp; &Uacute;ltima Execu&ccedil;&atilde;o:&nbsp;" +
						 ((m_DataGeracao.indexOf('!') != -1)
						  ? m_DataGeracao.substring(0,
													m_DataGeracao.indexOf('!'))
						  : m_DataGeracao);

			if (m_NomeArquivoRelatorio.indexOf('-') == -1)
			{
				m_Args[15] += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>(Processando...)</b>";
			}
			passouPeloVoltaZoom = false;
			m_Args[16] = SelectRecurso1;
			m_Args[17] = SelectRecurso2;
			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaTopoRelatorioEsquerda(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página relatorioesquerda.htm
	 * @roseuid 3EDCACD101EB
	 */
	public void apresentaMeioRelatorio()
	{
		try
		{
			iniciaArgs(14);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "meiorelatorio.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			m_Args[8] = m_Perfil;
			m_Args[9] = m_TipoRelatorio;
			m_Args[10] = m_IdRelatorio;
			m_Args[11] = m_NomeArquivoRelatorio;
			m_Args[12] = m_NomeRelatorio;
			m_Args[13] = m_DataGeracao;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaRelatorioDireita(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página meiorelatorioesquerda.htm
	 * @roseuid 3EDB9ABF002B
	 */
	public void apresentaMeioRelatorioEsquerda()
	{
		try
		{
			String Grafico;
			String botoesRefresh="";
			Grafico = geraGrafico();
			
			String TemRefresh = m_Request.getParameter("atualizaGrafico");
			String tipoOperacao = m_Request.getParameter("tipo");
			
			//so vai entrar aqui se pasar pela funcao alteraperiodo
			//e a atualizacao do grafico tiver parada. 
			if(passouPeloPeriodo)// && !fimExecucao)
			{
				//if(TemRefresh == null){
					TemRefresh = "false";//guardaVarAtualizaGrafico;					
				//}
				//if(tipoOperacao == null){
					tipoOperacao = "PararAtualizacao";//guardaVarAtipoOperacao;
				//}
				passouPeloPeriodo = false;
			}
			//so vai entrar aqui se pasar pela funcao alteraindicadores
			//e a atualizacao do grafico tiver parada. 
			if(passouPeloIndicadores){
				if(TemRefresh == null){
					TemRefresh = guardaVarAtualizaGrafico;
				}
				if(tipoOperacao == null){
					tipoOperacao = guardaVarAtipoOperacao;
				}
				passouPeloIndicadores = false;
			}
			if (Grafico != null)
			{
				iniciaArgs(14);
				System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
								   " OpApresentaRelHistorico:apresentaMeioRelatorioEsquerda(): Nome do historico mudado... Alterando...");

				// Apaga o ".png" do historico do diretorio "imagens"
//				new java.io.File(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB() +
//								 "imagens/historico/" + m_KeyCache + ".png").delete();
//
//				 // Remove historico da memoria
//                removeHistorico();
//                
//				// Altera chave de busca do historico
//				m_KeyCache = m_Perfil + "-" + m_TipoRelatorio + "-" +
//							 m_NomeRelatorio + "-" + m_IdRelatorio + "-" +
//							 m_NomeArquivoRelatorio + "-" +
//							 m_Request.getSession().getId();
//				
//				Historico Hist = buscaHistorico();
//                Grafico = geraGrafico();

				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
				m_Args[1] = "meiorelatorioesquerda.htm";
				
		        if (!NoUtil.buscaNobyNomePerfil(m_Perfil).getConexaoServUtil().verificaFimHistorico((short) 1,
																	 Integer.parseInt(m_TipoRelatorio),
																	 m_Perfil,
																	 m_DataGeracao,
																	 m_NomeRelatorio,
																	 Integer.parseInt(m_IdRelatorio)))
				{
		        	//aqui vamos pegar a progressao do relatorio.
		        	float progressao = NoUtil.buscaNobyNomePerfil(m_Perfil).getConexaoServUtil().getProgressaoHistorico((short) 1,
																		 Integer.parseInt(m_TipoRelatorio),
																		 m_Perfil,
																		 m_DataGeracao,
																		 m_NomeRelatorio,
																		 Integer.parseInt(m_IdRelatorio));
		        	
					if( TemRefresh != null )
					{
						if(TemRefresh.equals("false") && tipoOperacao.equals("PararAtualizacao"))
						{
							m_Args[2] = "";
							m_Args[3] = "Hist&oacute;rico em execu&ccedil;&atilde;o...(Atualiza&ccedil;&atilde;o parada)";
							
						}else
						{//para forçar a atualizacao do grafico pelo botao atualizaGrafico.
							if(tipoOperacao.equals("AtualizaGrafico"))
							{
								m_Args[2] = "<meta http-equiv=\"refresh\" content=\"5\">\n";
								m_Args[3] = "Hist&oacute;rico em execu&ccedil;&atilde;o...(Atualizando)"+"("+((int)progressao)+"%)";
							}
							if(tipoOperacao.startsWith("X")||tipoOperacao.startsWith("Y")){
								m_Args[2] = "";
								m_Args[3] = "Hist&oacute;rico em execu&ccedil;&atilde;o...(Atualiza&ccedil;&atilde;o parada)";
							}
						}
					}else
					{
						//quando retorna esse "_", a operacao realizada foi a "undozoom" e não é pra atualizar o grafico
						if(tipoOperacao != null){
							if(tipoOperacao.equals("_")){
								m_Args[2] = "";
								m_Args[3] = "Hist&oacute;rico em execu&ccedil;&atilde;o...(Atualiza&ccedil;&atilde;o parada)";
							}
						}else{
							m_Args[2] = "<meta http-equiv=\"refresh\" content=\"5\">\n";
							m_Args[3] = "Hist&oacute;rico em execu&ccedil;&atilde;o...(Atualizando)"+"("+((int)progressao)+"%)";
						}
					}
					//se tiver refresh deve ter a os botoes para para o refresh
					botoesRefresh="<tr align=\"center\">"+
					"<td>"+
						"<input type=\"button\" value=\"Atualiza Gr&aacute;fico\" onclick=\"AtualizaGrafico()\" >"+
					"</td>"+
					"<td>"+
						"<input type=\"button\" value=\"Parar Atualiza&ccedil;&atilde;o\" onclick=\"ParaAtualizacao()\" >"+			
					"</td>"+
					"</tr>";
				}
				else
				{
					m_Args[2] ="";
					m_Args[3] ="";
					//aqui é para o caso do historico ter finalizado e a tela não ter sido atualizada ainda.
					//tipo poderia esta fazendo zoom no grafico e ai nesse periodo o historico finaliza,
					//ai é colocado o botao de atualizar para recuperar o grafico final.
					if(TemRefresh != null){
						if(!finalHistorico){
							m_Args[3] = "Hist&oacute;rico Finalizado...";							
							finalHistorico = true;
						}
					}
				}
		        if(linhaDuplicadaNoArquivo){
		        	m_Args[4] = "Erro ao processar linhas do relatorio-Linhas duplicadas";
		        }else{
		        	m_Args[4] = "";
		        }
				m_Args[5] = m_Perfil;
				m_Args[6] = m_TipoRelatorio;
				m_Args[7] = m_IdRelatorio;
				m_Args[8] = m_NomeArquivoRelatorio;
				m_Args[9] = m_NomeRelatorio;
				m_Args[10] = m_DataGeracao;
				m_Args[11] = m_NomeRelatorio;
				m_Args[12] = botoesRefresh;
				m_Args[13] = Grafico+"?"+System.nanoTime();
			}
			else
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
			}

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaMeioRelatorioEsquerda(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página baserelatorioesquerda.htm
	 * @roseuid 3EDB9AC5021E
	 */
	public void apresentaBaseRelatorioDireita()
	{
		try
		{
			iniciaArgs(8);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "baserelatoriodireita.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaBaseRelatorioEsquerda(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página valoresmedios.htm
	 * @roseuid 3EDBBA8F037E
	 */
	public void apresentaValoresMedios()
	{
		try
		{
			Historico Hist;
			int i;
			int iTam;
			String Valores = "";
			Vector ValoresMedios;

			//
			// Faz um sleep para dar o tempo necessário do histórico já ter alterado 
			// 
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception Exc)
			{
			}

			System.out.println("m_NomeArquivoRelatorio: " +
							   m_NomeArquivoRelatorio);
			System.out.println("m_NomeRelatorio: " + m_NomeRelatorio);

			Hist = buscaHistorico();

			if (Hist == null)
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
				m_Html.enviaArquivo(m_Args);

				return;
			}

			ValoresMedios = Hist.fnValoresMedios();
			iTam = ValoresMedios.size();

			for (i = 1; i < iTam; i++)
				Valores += ((String) ValoresMedios.elementAt(i) + "<br>");

			iniciaArgs(4);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "valoresmedios.htm";

			m_Args[2] = (String) ValoresMedios.elementAt(0);
			m_Args[3] = Valores;
			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaValoresMedios(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta a página benchmarking.htm
	 * @roseuid 3EDBBA8F0392
	 */
	public void apresentaBenchmarking()
	{
		try
		{
			Historico Hist;
			int i;
			int iTam;
			String Valores = "";
			Vector Benchmark;

			//
			// Faz um sleep para dar o tempo necessário do histórico já ter alterado 
			// 
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception Exc)
			{
			}

			Hist = buscaHistorico();

			if (Hist == null)
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
				m_Html.enviaArquivo(m_Args);

				return;
			}

			Benchmark = Hist.fnBenchMarking();
			iTam = Benchmark.size();

			for (i = 1; i < iTam; i++)
				Valores += ((String) Benchmark.elementAt(i) + "<br>");
			System.out.println(Valores);

			iniciaArgs(4);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "benchmarking.htm";

			m_Args[2] = (String) Benchmark.elementAt(0);
			m_Args[3] = Valores;
			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaBenchmarking(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EDE474202F7
	 */
	public void apresentaIndicadores()
	{
		try
		{
			Historico Hist;
			int i = 0;
			String Indicador = null;
			String Indicadores1 = "";
			String Indicadores2 = "";
			String TabelaIndicadores = "";
			Vector Indicadores;

			iniciaArgs(10);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "indicadores.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			Hist = buscaHistorico();

			if (Hist == null)
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
				m_Html.enviaArquivo(m_Args);

				return;
			}

			Indicadores = VetorUtil.String2Vetor(m_IndicadoresDesempenho, ';');

			if (Indicadores != null)
			{
				int QtdIndicadores = Indicadores.size();

				for (i = 0; i < QtdIndicadores; i++)
				{
					Indicador = (String) Indicadores.elementAt(i);
					Indicadores1 += ("                   <option value=\"" +
					Indicador + "\">" + Indicador + "</option>\n");

					if (Hist.fnIndicadorSetado(Indicador) == true)
					{
						Indicadores2 += ("                   <option value=\"" +
						Indicador + "\">" + Indicador + "</option>\n"); //TabelaIndicadores += Indicador + ";";
					}
				}
			}

			m_Args[8] = Indicadores1;
			m_Args[9] = Indicadores2;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaIndicadores(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EE881980167
	 */
	public void apresentaPeriodos()
	{
		try
		{
			Historico Hist;
			int i;
			int iQtdPeriodosIniciais;
			int iQtdPeriodosFinais;
			String Periodos1 = "";
			String Periodos2 = "";
			Vector PeriodosIniciais;
			Vector PeriodosFinais;

			iniciaArgs(10);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "periodo.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			Hist = buscaHistorico();

			if (Hist == null)
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
				m_Html.enviaArquivo(m_Args);

				return;
			}

			PeriodosIniciais = Hist.fnGetPeriodosIniciais();
			PeriodosFinais = Hist.fnGetPeriodosFinais();
//			if (Hist.m_Periodo1 == null)
//			{
				iQtdPeriodosIniciais = PeriodosIniciais.size();
				iQtdPeriodosFinais = PeriodosFinais.size();
				Periodos1 += "                   <option selected>Selecione Per&iacute;odo Inicial</option>\n";
				Periodos2 += "                   <option selected>Selecione Per&iacute;odo Final  </option>\n";

				for (i = 0; i < (iQtdPeriodosIniciais - 1); i++)
					Periodos1 += ("                   <option value=\"" +
					PeriodosIniciais.elementAt(i) + "ddd"+"\">" + PeriodosIniciais.elementAt(i) +
					"</option>\n");

				for (i = 0; i < iQtdPeriodosFinais; i++)//antes comecava de i=1
					Periodos2 += ("                   <option value=\"" +
					PeriodosFinais.elementAt(i) +"cfcfc" +"\">" + PeriodosFinais.elementAt(i) +
					"</option>\n");

				Hist.m_Periodo1 = Periodos1;
				Hist.m_Periodo2 = Periodos2;
//			}

			m_Args[8] = Hist.m_Periodo1;
			m_Args[9] = Hist.m_Periodo2;

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaPeriodos(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EEA21200206
	 */
	public void apresentaConfiguracao()
	{
		try
		{
			UsuarioDef Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
			UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
			Vector Configuracao = UsuarioAux.getConfWebHistorico();

			iniciaArgs(20);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "configuracao.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;

			if (Configuracao.elementAt(0).equals("html") ||
				Configuracao.elementAt(0).equals("0"))
			{
				Configuracao.setElementAt("0", 0);
			}
			else if (Configuracao.elementAt(0).equals("excel") ||
					 Configuracao.elementAt(0).equals("1"))
			{
				Configuracao.setElementAt("1", 0);
			}

			for (int i = 0; i < Configuracao.size(); i++)
				m_Args[i + 8] = (String) Configuracao.elementAt(i);

			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaConfiguracao(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Apresenta o relatório numa nova janela no formato especificado pelo p_Formato
	 * @roseuid 3EE8C78902D6
	 */
	public void apresentaRelatorioFinal()
	{
		int i;
		int iTam;
		StringBuffer Cabecalho = new StringBuffer();
		StringBuffer BenchValores = new StringBuffer();
		String Aux1;
		String Aux2;
		String Recurso1;
		String Recurso2;
		StringBuffer Relatorio = new StringBuffer();
		String Grafico = "";
		Historico Hist;
		UsuarioDef Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());
		UsuarioDef UsuarioAux = Usuario.getNo().getConexaoServUtil().getUsuario(Usuario.getUsuario());
		Vector vCabecalho = null;
		Vector vBenchmark;
		Vector vValoresMedios;
		Vector CfgHist;

		CfgHist = UsuarioAux.getConfWebHistorico();

		if (CfgHist.elementAt(0).equals("1") == true) // Tipo de visualização
		{
			m_Response.setContentType("application/vnd.ms-excel");
		}

		Hist = buscaHistorico();

		Vector list = Hist.getListaContadores();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}

		// Recupera os tipos de recursos
		Recurso1 = Hist.fnTituloRecurso1();
		Recurso2 = Hist.fnTituloRecurso2();

		if (CfgHist.elementAt(1).equals("1") == true) // Cabeçalho
		{
			// Recupera o cabeçalho
			vCabecalho = new Vector(Hist.fnLinhasCabecalho());
			vCabecalho.remove(0);

			// Insere outras 
			vCabecalho.insertElementAt("Relat&oacute;rio: &nbsp;" +
									   m_NomeRelatorio, 0);
			vCabecalho.insertElementAt("&Uacute;ltima Execu&ccedil;&atilde;o: &nbsp;" +
									   m_DataGeracao, 1);
			vCabecalho.insertElementAt(Recurso1 + ": &nbsp; " +
									   Hist.m_UltimoFiltroRec1, 2);

			if (Recurso2 != null)
			{
				vCabecalho.insertElementAt(Recurso2 + ": &nbsp; " +
										   Hist.m_UltimoFiltroRec2, 3);
			}

			Cabecalho.append("<table border=\"0\">\n");
			iTam = vCabecalho.size();

			for (i = 0; i < iTam; i++)
			{
				Aux1 = (String) vCabecalho.elementAt(i);
				Aux2 = Aux1.substring(Aux1.indexOf(":") + 2,
									  Aux1.length());
				Aux1 = Aux1.substring(0, Aux1.indexOf(":") + 1);

				Cabecalho.append("   <tr>\n");
				Cabecalho.append( ("      <td><b>" + Aux1 + "</b> &nbsp;" + Aux2 +
				"</td>\n"));
				Cabecalho.append("   </tr>\n");
			}

			Cabecalho.append("   <tr>\n");
			Cabecalho.append("      <td>&nbsp;</td>\n");
			Cabecalho.append("   </tr>\n");
			Cabecalho.append("</table>\n");
		}
		else
		{
			Cabecalho.append("");
		}

		if (CfgHist.elementAt(2).equals("1") == true) // Relatório
		{
			Relatorio.append(Hist.fnAreaRelatorioTabela());
		}

		if ((CfgHist.elementAt(3).equals("1") == true) &&
			(CfgHist.elementAt(4).equals("1") == true)) // Benchmarking e Valores Médios
		{
			vBenchmark = Hist.fnBenchMarking();
			vValoresMedios = Hist.fnValoresMedios();

			iTam = vBenchmark.size();

			BenchValores.append("      <tr>\n");
			BenchValores.append("         <td align=\"center\" bgcolor=\"#33CCFF\"><b>Benchmarking</b></td>");
			BenchValores.append("         <td align=\"center\" bgcolor=\"#33CCFF\"><b>Valores M&eacute;dios</b></td>\n");
			BenchValores.append("      </tr>\n");

			for (i = 0; i < iTam; i++)
			{
				BenchValores.append("   <tr>\n");
				BenchValores.append("      <td align=\"center\">" +
				(String) vBenchmark.elementAt(i) + "</td>");
				BenchValores.append("      <td align=\"center\">" +
				(String) vValoresMedios.elementAt(i) + "</td>");
				BenchValores.append("   </tr>\n");
			}
		}
		else if ((CfgHist.elementAt(3).equals("1") == true) &&
				 (CfgHist.elementAt(4).equals("0") == true)) // Benchmarking e Valores Médios
		{
			vBenchmark = Hist.fnBenchMarking();

			iTam = vBenchmark.size();

			BenchValores.append("      <tr>\n");
			BenchValores.append("         <td align=\"center\" bgcolor=\"#33CCFF\" colspan=\"2\"><b>Benchmarking</b></td>");
			BenchValores.append("      </tr>\n");

			for (i = 0; i < iTam; i++)
			{
				BenchValores.append("   <tr>\n");
				BenchValores.append("      <td  align=\"center\" colspan=\"2\">" +
				(String) vBenchmark.elementAt(i) + "</td>");
				BenchValores.append("   </tr>\n");
			}
		}
		else if ((CfgHist.elementAt(3).equals("0") == true) &&
				 (CfgHist.elementAt(4).equals("1") == true)) // Benchmarking e Valores Médios
		{
			vValoresMedios = Hist.fnValoresMedios();
			iTam = vValoresMedios.size();

			BenchValores.append("      <tr>\n");
			BenchValores.append("         <td align=\"center\" bgcolor=\"#33CCFF\" colspan=\"2\"><b>Valores M&eacute;dios</b></td>");
			BenchValores.append("      </tr>\n");

			for (i = 0; i < iTam; i++)
			{
				BenchValores.append("   <tr>\n");
				BenchValores.append("      <td  align=\"center\" colspan=\"2\">" +
				(String) vValoresMedios.elementAt(i) + "</td>");
				BenchValores.append("   </tr>\n");
			}
		}

		if (CfgHist.elementAt(5).equals("1") == true) // Gráfico
		{
			Grafico = "   <tr>\n";
			Grafico += "      <td colspan=\"2\" bgcolor=\"#33CCFF\" align=\"center\"><b>Gr&aacute;fico Associado</b></td>\n";
			Grafico += "   </tr>\n";
			Grafico += "   <tr>\n";
			Grafico += "<!-- Gráfico -->\n";
			Grafico += ("      <td colspan=\"2\" align=\"center\"><img src=\"http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/"+DefsComum.s_ContextoWEB+"/imagens/historico/" +
			m_KeyCache + ".png\"></td>\n");
			Grafico += "   </tr>\n";
		}
		else
		{
			Grafico = "";
		}

		m_Html.enviaInicio("CDRView", m_NomeRelatorio + " - " + m_DataGeracao,
						   null);

		/*
		 *   m_Html.envia("<p>\n<b>OSx Telecom - CDRView <sup>&reg;</sup></b><br>\n<b><small><small>Copyright 1999 - 2003<small></small></b>\n</p>");
		*/
		/* Monta tabela com os dados do Cabecalho do Relatorio */
		m_Html.envia(HTMLUtil.getCabecalho());

		m_Html.envia(Cabecalho.toString());
		m_Html.envia(Relatorio.toString());
		m_Html.envia("<br>\n<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\n");
		m_Html.envia(BenchValores.toString());
		m_Html.envia("   <tr>\n");
		m_Html.envia("      <td colspan=\"2\">&nbsp;</td>\n");
		m_Html.envia("   </tr>\n");
		m_Html.envia(Grafico);
		m_Html.envia("</table>\n");
		m_Html.enviafinal(null, null, true);
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EEE106203B0
	 */
	public void apresentaRecursos()
	{
		try
		{
			Historico Hist;
			int i;
			int iTam;
			String TipoRecurso = m_Request.getParameter("tiporecurso");
			String Recurso;
			String SelectRecursos = "";
			String RecursosSelecionados = "";
			Vector ListaRecursos;

			Hist = buscaHistorico();

			if (Hist == null)
			{
				iniciaArgs(2);
				m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
				m_Args[1] = "relatorioinexistente.htm";
				m_Html.enviaArquivo(m_Args);

				return;
			}

			if (TipoRecurso.equals("1") == true)
			{
				Recurso = Hist.fnTituloRecurso1();
				ListaRecursos = Hist.fnListaRecursosVec1();
			}
			else
			{
				Recurso = Hist.fnTituloRecurso2();
				ListaRecursos = Hist.fnListaRecursosVec2();
			}

			RecursosSelecionados = m_Request.getParameter("recursosselecionados");

			//System.out.println("ListaRecursos: "+ListaRecursos.size());         
			iTam = ListaRecursos.size();

			for (i = 0; i < iTam; i++)
			{
				//System.out.println("ListaRecursos.elementAt(i): "+ListaRecursos.elementAt(i));        
				SelectRecursos += ("            <option value=\"" +
				ListaRecursos.elementAt(i) + "\">" +
				ListaRecursos.elementAt(i) + "</option>\n");
			}

			iniciaArgs(12);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS() + "historico/";
			m_Args[1] = "recursos.htm";

			m_Args[2] = m_Perfil;
			m_Args[3] = m_TipoRelatorio;
			m_Args[4] = m_IdRelatorio;
			m_Args[5] = m_NomeArquivoRelatorio;
			m_Args[6] = m_NomeRelatorio;
			m_Args[7] = m_DataGeracao;
			m_Args[8] = TipoRecurso;
			m_Args[9] = RecursosSelecionados;

			m_Args[10] = Recurso;
			m_Args[11] = SelectRecursos;
			m_Html.enviaArquivo(m_Args);
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:apresentaTopoRelatorioEsquerda(): " +
							   Exc);
			Exc.printStackTrace();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Altera os recursos do histórico
	 * @roseuid 3EDDF6A00395
	 */
	public void alteraRecursos()
	{
		int iIndice;
		Historico Hist = null;
		String Recursos = null;
		String Recursos1 = null;
		String Recursos2 = null;

		Recursos = m_Request.getParameter("recursos");

		//System.out.println("Rec: " + Recursos);
		iIndice = Recursos.indexOf("@@");

		if (iIndice == -1)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:alteraRecursos(): 'Recursos' passados esta incorreto: " +
							   Recursos);

			return;
		}

		Recursos1 = Recursos.substring(0, iIndice);

		if (Recursos.substring(iIndice + 2,
							   Recursos.length()).length() != 0)
		{
			Recursos2 = Recursos.substring(iIndice + 2,
										   Recursos.length());
		}

		//System.out.println("OpApresentaRelHistorico::alteraRecursos: Rec1: " + Recursos1 + " - Rec2: " + Recursos2);
		// Busca historico no cache
		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}

		Hist.fnZera();
		Hist.fnReCarrega(Recursos1, Recursos2, null, null);
		apresentaMeioRelatorioEsquerda();
	}

	/**
	 * @return void
	 * @exception
	 * Altera os indicadores do histórico
	 * @roseuid 3EDF5CC90393
	 */
	public void alteraIndicadores()
	{
		int iIndice;
		Historico Hist = null;
		String Indicadores = null;

		// Recupera os novos indicadores
		Indicadores = m_Request.getParameter("indicadores");

		// Substitui o caracter "@" por "%"
		Indicadores = Indicadores.replace('@', '%');

		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}
		passouPeloIndicadores = true;
		// Se os indicadores forem os mesmos, não faz nada
		if (Indicadores.equals(Hist.fnGetTabelaIndicadores()) == false)
		{
			Hist.fnSetTabelaIndicadores(Indicadores);
			apresentaMeioRelatorioEsquerda();
		}else{//forçando a atualizacao da pagina do grafico
			apresentaMeioRelatorioEsquerda();
		}
	}

	/**
	 * @return void
	 * @exception
	 * Altera os períodos do histórico
	 * @roseuid 3EE88F530021
	 */
	public void alteraPeriodos()
	{
		int iIndice;
		Historico Hist = null;
		String Periodos = null;
		String Periodo1;
		String Periodo2;

		// Recupera os periodos
		Periodos = m_Request.getParameter("periodos");

		// Recupera os períodos inicial e final
		Periodo1 = Periodos.substring(0,
									  Periodos.indexOf("@"));
		Periodo2 = Periodos.substring(Periodos.indexOf("@") + 1,
									  Periodos.length());

		System.out.println("OpApresentaRelHistorico::alteraPeriodos(): Periodos: " +
						   Periodo1 + " - " + Periodo2);

		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}
		passouPeloPeriodo = true;
		/*a variavel 'guardaVarAtualizaGrafico', tem que ser setada com falso aqui
		 * para que, quando o grafico tiver em execução e for selecionado um periodo para
		 * ser visualizado ele, pare a execução e de o 'foco' no periodo desejado. 
		 */
		guardaVarAtualizaGrafico = "false";
		Hist.fnSetPeriodos(Periodo1, Periodo2);
		apresentaMeioRelatorioEsquerda();
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EEA26C603D2
	 */
	public void alteraConfiguracao()
	{
		UsuarioDef Usuario = null;

		//System.out.println("alteraConfiguracao.size()");
		Usuario = (UsuarioDef) NoUtil.getUsuarioLogado(m_Request.getSession().getId());

		if (Usuario != null)
		{
			String Configuracao = m_Request.getParameter("configuracao");
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:alteraConfiguracao(): " +
							   Configuracao);
			Usuario.setConfWebHistorico(Configuracao);
			Usuario.getNo().getConexaoServUtil().alteraUsuario(Usuario);
		}
		else
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:alteraConfiguracao(): Usuario nao foi encontrado");
		}

		apresentaConfiguracao();
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3F1D792D02CF
	 */
	public void home()
	{
		Historico Hist;

		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}

		Hist.fnZera();

		//Hist.fnReCarrega(null, null, null, null);
		apresentaMeioRelatorioEsquerda();
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EE60CDC0335
	 */
	public void zoom()
	{
		Historico Hist;
		String Aux;
		String EixoZoom;
		String TipoZoom;
		String CoordX;
		String CoordY;

		// Recupera as informações de zoom que vem no seguinte formato:
		// Formato: EixoDoZoom+"-"+CoordX+"-"+CoordY+"-"+TipoDeZoom;
		// EixoDoZoom = X, Y, XY ou D (para desfazer)
		// TipoDeZoom = '-' ou '_' (que significa '+')
		Aux = m_Request.getParameter("tipo");

		//System.out.println("ZOOM: "+Aux);
		// Recupera o eixo do zoom: horizontal, vertical, ambos
		EixoZoom = Aux.substring(0,
								 Aux.indexOf("-"));
		Aux = Aux.substring(Aux.indexOf("-") + 1,
							Aux.length());

		// Recupera a Coordenada X
		CoordX = Aux.substring(0,
							   Aux.indexOf("-"));
		Aux = Aux.substring(Aux.indexOf("-") + 1,
							Aux.length());

		// Recupera a Coordenada Y
		CoordY = Aux.substring(0,
							   Aux.indexOf("-"));
		Aux = Aux.substring(Aux.indexOf("-") + 1,
							Aux.length());

		// Recupera o tipo de zoom
		TipoZoom = Aux;

		if (TipoZoom.equals("_"))
		{
			TipoZoom = "+";
		}

		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}
		//a variavel 'guardaVarAtualizaGrafico' é setada como false para quando o zoom for dado,
		// o grafico não ficar atualizando.
		guardaVarAtualizaGrafico = "false";
		// Faz o zoom
		Hist.fnZoom(Integer.parseInt(CoordX),
					Integer.parseInt(CoordY),
					TipoZoom.charAt(0),
					EixoZoom);
		apresentaMeioRelatorioEsquerda();
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EEF6D3D0372
	 */
	public void undoZoom()
	{
		Historico Hist;

		System.out.println("undoZoom");
		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}
		passouPeloVoltaZoom = true;
		Hist.fnUndo();
		valorDoRecurso1VoltaZoom = Hist.m_Periodo1;
		valorDoRecurso2VoltaZoom = Hist.m_Periodo2;
		apresentaMeioRelatorioEsquerda();
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3EE60D000029
	 */
	public void navega()
	{
		Historico Hist;
		String TipoNavegacao;

		// Recupera as informações de zoom que vem no seguinte formato:
		TipoNavegacao = m_Request.getParameter("tipo");

		//System.out.println("TipoNavegacao: "+TipoNavegacao);
		Hist = buscaHistorico();

		if (Hist == null)
		{
			iniciaArgs(2);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_PAGINAS();
			m_Args[1] = "relatorioinexistente.htm";
			m_Html.enviaArquivo(m_Args);

			return;
		}

		// Seta as informações de navegação do histórico
		if (TipoNavegacao.equals(">") == true)
		{
			Hist.fnDireita();
		}
		else
		{
			Hist.fnEsquerda();
		}

		apresentaMeioRelatorioEsquerda();
	}

	/**
	 * DOCUMENT ME!
	 */
	public void removeHistorico()
	{
		removeHistorico(this);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_Operacao DOCUMENT ME!
	 */
	public static synchronized void removeHistorico(OpApresentaRelHistorico p_Operacao)
	{
		//System.out.println("RemoveHistorico");
		try
		{
			s_MapHistoricos.remove(p_Operacao.m_KeyCache);
			new java.io.File(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB() + "imagens/historico/" +
							 p_Operacao.m_KeyCache + ".png").delete();

			//System.out.println("Objeto historico removido!");
		}
		catch (Exception Exc)
		{
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:removeHistorico(): Nao foi possivel remvoer o historico com chave: " +
							   p_Operacao.m_KeyCache);
			System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() +
							   " OpApresentaRelHistorico:removeHistorico(): " +
							   Exc);
		}
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3EE4A0F000B3
	 */
	public String geraGrafico()
	{
		boolean Retorno;
		Historico Hist;
		String NomeGrafico = "";

		Hist = buscaHistorico();

		if (Hist == null)
		{
			return null;
		}

        // Se o historico nao tiver terminado...
        if (!NoUtil.buscaNobyNomePerfil(m_Perfil).getConexaoServUtil().verificaFimHistorico((short) 1,
															 Integer.parseInt(m_TipoRelatorio),
															 m_Perfil,
															 m_DataGeracao,
															 m_NomeRelatorio,
															 Integer.parseInt(m_IdRelatorio)))
		{
			Retorno = true;
			//Rafael - coloquei esse if aqui para nao deixar a atualizacao do grafico
			//quando o relatorio esta parado(Para Atualizacao), mas em Execucao...para que se
			//consiga ver o periodo selecionado quando o relatorio ainda esta em execucao.
			// se não entrar no if o grafico não é atualizado.
			System.out.println("\n***********>>>guardaVarAtualizaGrafico => "+guardaVarAtualizaGrafico);
			if(!guardaVarAtualizaGrafico.equals("false")){				
				Retorno = Hist.fnLeRelatorio(m_Perfil, m_TipoRelatorio,
											 m_IdRelatorio, m_NomeArquivoRelatorio,
											 m_NomeRelatorio, m_DataGeracao);
			}

			if (Retorno == false)
			{
				return null;
			}
		}else{
			/*Aqui nesse 'else' vamos garantir que quando o historico tiver terminado sua execução, 
			 * e o grafico que estiver mostrando não corresponder ao grafico 'total final', quando o usuario
			 * clicar no botao atualizar, ele deve passar aqui, e executar o 'fnLeRelatorio' para carregar
			 * o grafico com todos os seus periodos. 
			 */
			if(m_Request.getParameter("tipo") != null){
				if(m_Request.getParameter("tipo").equals("AtualizaGrafico")){
					Retorno = Hist.fnLeRelatorio(m_Perfil, m_TipoRelatorio,
							 m_IdRelatorio, m_NomeArquivoRelatorio,
							 m_NomeRelatorio, m_DataGeracao);
				}
			}
		}
		
		//mudan o tamanho do grafico na coordenada y de 469 para 429 para acrecentar os botoes de para e atualizar o grafico
		Hist.fnGeraRelatorioGrafico(580, 429,
									NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB() +
									"imagens/historico/" + m_KeyCache + ".png");
		NomeGrafico = m_KeyCache + ".png";
		
		linhaDuplicadaNoArquivo = Hist.getErroLinhaDuplicadaArquivo();
		
		return NomeGrafico;
	}

	/**
	 * @return Historico
	 * @exception
	 * Busca o objeto historico no cache e o retorna caso ele já se encontre na lista. Caso contrário, retorna null.
	 * @roseuid 3EEF6E7B0171
	 */
	public Historico buscaHistorico()
	{
		return buscaHistorico(this);
	}

	/**
	 * @param p_Operacao
	 * @return Historico
	 * @exception
	 * Busca o objeto historico no cache e o retorna caso ele já se encontre na lista. Caso contrário, retorna null.
	 * @roseuid 3EDE20BD0167
	 */
	public static synchronized Historico buscaHistorico(OpApresentaRelHistorico p_Operacao)
	{
		Object ObjHist = null;
		Historico Hist = null;

		if (s_MapHistoricos.size() == 0)
		{
			Hist = p_Operacao.criaHistorico();

			return Hist;
		}

		ObjHist = s_MapHistoricos.get(p_Operacao.m_KeyCache);

		if (ObjHist != null)
		{
			Hist = (Historico) ObjHist;
		}
		else
		{
			Hist = p_Operacao.criaHistorico();
		}

		return Hist;
	}

	/**
	 * @param p_Historico
	 * @return void
	 * @exception
	 * Insere um objeto Historico no cache de históricos.
	 * @roseuid 3EDE220203BD
	 */
	public void insereHistorico(Historico p_Historico)
	{
		//System.out.println("m_KeyCache de insercao: "+ m_KeyCache);   
		synchronized (s_MapHistoricos)
		{
			s_MapHistoricos.put(m_KeyCache, p_Historico);

			Historico hist = (Historico) s_MapHistoricos.get(m_KeyCache);
			System.out.println("Hist Depois de Inserir: " + hist);

			Vector list = hist.getListaContadores();
		}

		//System.out.println("OpApresentaRelHistorico::insereHistorico(): Qtd no cache: " +s_MapHistoricos.size());
	}

	/**
	 * @return Historico
	 * @exception
	 * Cria um objeto historico, realiza a leitura inicial do arquivo e o insere no cache de históricos.
	 * @roseuid 3EDE2DB1033B
	 */
	public Historico criaHistorico()
	{
		boolean Retorno;
		Historico Hist;
		Vector param = new Vector();
		param.addElement(m_Perfil);
		param.addElement(m_TipoRelatorio);
		param.addElement(m_IdRelatorio);
		param.addElement(m_NomeArquivoRelatorio);
		param.addElement(m_NomeRelatorio);
		param.addElement(m_DataGeracao);
		//Acresentando parametros ao construtor do Historico, para  fazer a verificação
		//do relatorio do Historico, se os contadores do relatorio estao de acordo com os do xml.
		//*Pensar numa solução melhor*
		Hist = new Historico(NoUtil.buscaNobyNomePerfil(m_Perfil).getConexaoServUtil(),
							 getRecursosDesempenho(),
							 getIndicadoresDesempenho(),param);
		Retorno = Hist.fnLeRelatorio(m_Perfil, m_TipoRelatorio, m_IdRelatorio,
									 m_NomeArquivoRelatorio, m_NomeRelatorio,
									 m_DataGeracao);

		if (Retorno == false)
		{
			return null;
		}

		// Insere histórico no cache
		insereHistorico(Hist);

		return Hist;
	}
		
}
