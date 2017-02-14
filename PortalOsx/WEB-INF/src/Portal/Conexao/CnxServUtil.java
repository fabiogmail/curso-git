package Portal.Conexao;

import java.net.InetAddress;
import java.rmi.ConnectException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.text.SimpleDateFormat;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import agenda.facade.IManutencaoAgenda;

import Interfaces.iAcesso;
import Interfaces.iAgenda;
import Interfaces.iAgendaHelper;
import Interfaces.iAlarmeSisCfg;
import Interfaces.iAlarmeView;
import Interfaces.iArqView;
import Interfaces.iBilCfg;
import Interfaces.iConversor;
import Interfaces.iConversorCfg;
import Interfaces.iIndicadores;
import Interfaces.iListaAcesso;
import Interfaces.iListaBilhetadores;
import Interfaces.iListaConversorCfgs;
import Interfaces.iListaConversores;
import Interfaces.iListaOperadoras;
import Interfaces.iListaParserCfgs;
import Interfaces.iListaParserGen;
import Interfaces.iListaParserGenCfg;
import Interfaces.iListaParsers;
import Interfaces.iListaPerfis;
import Interfaces.iListaProcControlados;
import Interfaces.iListaRelAgendaHistorico;
import Interfaces.iListaReprocCfgs;
import Interfaces.iListaReprocessadores;
import Interfaces.iListaServProcs;
import Interfaces.iListaTecnologias;
import Interfaces.iListaUsr;
import Interfaces.iListaUsuarios;
import Interfaces.iListaUsuariosWeb;
import Interfaces.iLogView;
import Interfaces.iMensagem;
import Interfaces.iOperadora;
import Interfaces.iParser;
import Interfaces.iParserCfg;
import Interfaces.iParserGen;
import Interfaces.iParserGenCfg;
import Interfaces.iPerfil;
import Interfaces.iProcControlado;
import Interfaces.iReprocessador;
import Interfaces.iReprocessadorCfg;
import Interfaces.iRetRelatorio;
import Interfaces.iServProc;
import Interfaces.iTecnologia;
import Interfaces.iTipoCli;
import Interfaces.iTipoProc;
import Interfaces.iUsuario;
import Interfaces.iUsuarioWeb;
import Interfaces.iUtil;
import Interfaces.iUtilHelper;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.AcessoCfgDef;
import Portal.Utils.AgendaRMI;
import Portal.Utils.BilhetadorCfgDef;
import Portal.Utils.ConfiguracaoConvCfgDef;
import Portal.Utils.ConfiguracaoReprocCfgDef;
import Portal.Utils.ConversorCfgDef;
import Portal.Utils.OperadoraCfgDef;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.ProcessoDef;
import Portal.Utils.ReprocessadorCfgDef;
import Portal.Utils.ServidorProcCfgDef;
import Portal.Utils.TecnologiaCfgDef;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 * Classe para conexão CORBA com o Servidor Util e utilzação do objeto distribuído Util. Todas as chamadas ao Servidor Util são feitas por essa classe.
 */
public class CnxServUtil extends Conexao
{
	private iListaAcesso m_iListaAcesso = null;
	private iListaBilhetadores m_iListaBilhetadores = null;
	private iListaPerfis m_iListaPerfis = null;
	private iListaTecnologias m_iListaTecnologias = null;
	private iListaUsuarios m_iListaUsuarios = null;
	private iListaUsuariosWeb m_iListaUsuarioWeb = null;
	private iListaOperadoras m_iListaOperadoras = null;
	private iUtil m_iUtil;
	private iAgenda m_iAgenda;
	private iListaRelAgendaHistorico m_iListaRelAgendaHistorico = null;
	private iLogView m_Log = null;
	private iArqView m_Arq = null;
	private final int COD_SRV_CONTROLE=0;
	private final int COD_SRV_AGENDA=4;
	private final int COD_SRV_HISTORICO=5;
	private final int COD_NEW_AGENDA=25;
	//variavel criada para fazer o controle do relatorio do historico, quando os 
	//contadores do relatorio estão diferente dos contadores do xml.
	private String contadoresDoRelatorio;

	public CnxServUtil(short p_ModoConexao,
	                   String p_Host,
	                   String p_Objeto,
	                   int p_Porta)
	{
	    this.host = p_Host;
	    this.porta = p_Porta;
	    this.nomeObjetoCorba = p_Objeto;
	    this.modoConexao = p_ModoConexao;
	}
	
	public String[] getVetorBillOciosidade()
	{
		String aux = m_iUtil.fnGetListaBilhetadorOciosidade();
		
		//System.out.println("Recebimento dos valores:"+aux);
		String[] listaDados = aux.split("\n");
		String bilhetador = "";
		String ociosidade = "";
		String habilitacao = "";
		
		
		for (int i = 0; i < listaDados.length; i++) {
			
			String[] auxDados = listaDados[i].split(";");
			if(auxDados.length < 3)
				continue;
			
			bilhetador+= auxDados[0] += ";";
			ociosidade+= DivideOciosidade(auxDados[1]); 
			ociosidade += ";";
			habilitacao+= auxDados[2] += ";";
		}
		
		listaDados = new String[3];
		listaDados[0] = bilhetador;
		listaDados[1] = ociosidade;
		listaDados[2] = habilitacao;
					
		return listaDados;
	}
	
	public String DivideOciosidade(String Ociosidade){	
		return Integer.toString(Integer.parseInt(Ociosidade)/60);	
	
	}
	
	public void criaConexaoUtil(short p_ModoConexao,
	     	                    String p_Host,
	    	                    String p_Objeto,
	    	                    int p_Porta)
	{
	    String[] Args = { "", "" };

		switch (p_ModoConexao)
		{
			case 1:
				if (m_iUtil == null)
				{
					iniciaOrb(Args, false, p_Host, p_Porta, p_Objeto);
					m_iUtil = iUtilHelper.narrow(m_Obj);

				}
				if (m_iAgenda == null)
				{
					iniciaOrb(Args, false, p_Host, p_Porta, "Agenda");
					m_iAgenda = iAgendaHelper.narrow(m_Obj);
				}
				break;
			case 2:
				
//				if (m_iUtil == null)
//				{
//					getIORArq(ArquivosDefs.s_DIR_ARQS_REF, ArquivosDefs.s_ARQ_IOR_SERVUTIL);
//					iniciaOrb(Args, true, p_Host, p_Porta, p_Objeto);
//					m_iUtil = iUtilHelper.narrow(m_Obj);
//				}
//				break;
		}

		iniciaListas();
	}

	/**
	 * @deprecated Construtor nao mais utilizado!!
	 * @param p_Modo
	 * @return
	 * @
	exception
	 * Construtor. O parâmetro Modo define a maneira de obter o IOR do servidor.
	 * 0 - Não Realiza conexão
	 * 1 - Diretamente pelo IP e Porta
	 * 2 - Pelo IOR
	 * @roseuid 3BF9B8120098
	 */
	public CnxServUtil(short p_Modo)
	{
		String[] Args = { "", "" };

		switch (p_Modo)
		{
			case 1:

				if (m_iUtil == null)
				{
					iniciaOrb(Args, false, NoUtil.getNo().getConexaoServUtil().getHost(),
					          NoUtil.getNo().getConexaoServUtil().getPorta(),
					          NoUtil.getNo().getConexaoServUtil().getNomeObjetoCorba());
					m_iUtil = iUtilHelper.narrow(m_Obj);
				}
				if (m_iAgenda == null)
				{
					iniciaOrb(Args, false, NoUtil.getNo().getConexaoServUtil().getHost(),
					          NoUtil.getNo().getConexaoServUtil().getPorta(),
					          "Agenda");
					m_iAgenda = iAgendaHelper.narrow(m_Obj);
				}

				break;

			case 2:

				if (m_iUtil == null)
				{
					getIORArq(NoUtil.getNo().getDiretorioDefs().getS_DIR_ARQS_REF(),
							  ArquivosDefs.s_ARQ_IOR_SERVUTIL);
					iniciaOrb(Args, true, NoUtil.getNo().getConexaoServUtil().getHost(),
					          NoUtil.getNo().getConexaoServUtil().getPorta(),
					          NoUtil.getNo().getConexaoServUtil().getNomeObjetoCorba());
					m_iUtil = iUtilHelper.narrow(m_Obj);
				}
				if (m_iAgenda == null)
				{
					getIORArq(NoUtil.getNo().getDiretorioDefs().getS_DIR_ARQS_REF(),
							  ArquivosDefs.s_ARQ_IOR_SERVUTIL);
					iniciaOrb(Args, true, NoUtil.getNo().getConexaoServUtil().getHost(),
					          NoUtil.getNo().getConexaoServUtil().getPorta(),
					          "Agenda");
					m_iAgenda = iAgendaHelper.narrow(m_Obj);
				}

				break;
		}

		iniciaListas();
		limpaArquivosLock();
	}

	/**
	 * @return void
	 * @exception
	 * @roseuid 3CB32BA902EF
	 */
	public void iniciaListas()
	{
		if (m_iUtil == null)
		{
			System.out.println("->m_iUtil eh NULL");
			System.exit(0);
		}
		else
		{
			System.out.println("->m_iUtil OK");
		}
	
		
		if (m_iListaAcesso == null)
		{
			m_iListaAcesso = m_iUtil.fnGetListaAcesso();
		}

		if (m_iListaBilhetadores == null)
		{
		    m_iListaBilhetadores = m_iUtil.fnGetListaBilhetadores();
		}

		if (m_iListaPerfis == null)
		{
			m_iListaPerfis = m_iUtil.fnGetPerfisCfg();
		}

		if (m_iListaTecnologias == null)
		{
		    m_iListaTecnologias = m_iUtil.fnGetListaTecnologias();
		}

		if (m_iListaOperadoras == null)
		{
			m_iListaOperadoras = m_iUtil.fnGetListaOperadoras();
		}

		if (m_iListaUsuarios == null)
		{
			m_iListaUsuarios = m_iUtil.fnGetUsuariosCfg();
		}

		if (m_iListaUsuarioWeb == null)
		{
			m_iListaUsuarioWeb = m_iUtil.fnGetUsuariosWeb();
		}
	}

	/**
	 * @param p_Usuario
	 * @param p_Senha
	 * @param p_IPRemoto
	 * @param p_HostRemoto
	 * @param p_Sessao
	 * @return short
	 * @exception
	 * Realiza o logon do usuário no Portal.
	 * @roseuid 3BFB086B0004
	 */
	public short logon(String p_Usuario,
					   String p_Senha,
					   String p_IPRemoto,
					   String p_HostRemoto,
					   String p_Sessao,
					   String motivo)
	{
		try
		{
			return (m_iUtil.fnLogon(p_Usuario, p_Senha, p_IPRemoto,
									p_HostRemoto, p_Sessao, motivo));
		}
		catch (Exception Exc)
		{
			System.out.println("CnxServUtil - logon(): Erro ao tentar logon");
			System.out.println("CnxServUtil - logon(): " + Exc);
			m_iUtil = null;

			return -10; // Código de erro com servidor
		}
	}

	/**
	 * @param p_Usuario
	 * @param p_IPRemoto
	 * @param p_HostRemoto
	 * @return short
	 * @exception
	 * Realiza o logout do usuário no Portal.
	 * @roseuid 3C06D32C0351
	 */
	public short logout(UsuarioDef p_Usuario,
						String p_IPRemoto,
						String p_HostRemoto)
	{
		try
		{
			return (m_iUtil.fnLogout(p_Usuario.getIdUsuario(),
									 p_Usuario.getSequencia(),
									 p_IPRemoto,
									 p_HostRemoto,
									 p_Usuario.getIDSessao()));

			//p_Usuario.getSessaoHTTP().getId()));
		}
		catch (Exception Exc)
		{
			System.out.println("CnxServUtil - logout(): Erro ao tentar logout: ");
			System.out.println("CnxServUtil - logout(): " + Exc);
			m_iUtil = null;

			return -10; // Código de erro com servidor
		}
	}

	/**
	 * @return UsuarioDef
	 * @exception
	 * Retorna o objeto UsuarioDef que espelha o usuário corrente da lista de usuários do Servidor Uitl.
	 * @roseuid 3C0645CB0225
	 */
	public UsuarioDef getUsuario()
	{
		UsuarioDef Usuario = null;
		iUsuarioWeb UsuarioWeb = null;

		UsuarioWeb = m_iListaUsuarioWeb.fnGetCorrente();

		if (UsuarioWeb != null)
		{
			Usuario = new UsuarioDef(UsuarioWeb.fnGetUsuario(),
									 UsuarioWeb.fnGetPerfil(),
									 buscaAcesso(UsuarioWeb.fnGetAcesso()),
									 UsuarioWeb.fnGetSenha(),
									 UsuarioWeb.fnGetDicaSenha(),
									 UsuarioWeb.fnGetIDPerfil(),
									 UsuarioWeb.fnGetIDUsuario(),
									 UsuarioWeb.fnGetAcesso(),
									 UsuarioWeb.fnGetSequencia(),
									 UsuarioWeb.fnGetDataAcesso(),
									 "","","","","","","","","");
			Usuario.setIP(UsuarioWeb.fnGetIP());
			Usuario.setHost(UsuarioWeb.fnGetHost());
			Usuario.setDataAcessoStr(UsuarioWeb.fnGetAcessoStr());
		}
		else
		{
			System.out.println("UsuarioDef - getUsuario(): UsuarioWeb null");
		}

		return Usuario;
	}

	/**
	 * @param p_Usuario
	 * @return UsuarioDef
	 * @exception
	 * Retorna o objeto UsuarioDef que corresponde a p_Usuario.
	 * @roseuid 3EEDF1F60311
	 */
	public UsuarioDef getUsuario(String p_Usuario)
	{
		UsuarioDef Usuario = null;
		iUsuario UsuarioServ;

		if (m_iListaUsuarios == null)
		{
			m_iListaUsuarios = m_iUtil.fnGetUsuariosCfg();
		}

		UsuarioServ = m_iListaUsuarios.fnGetInicio((short) 0);

		while (UsuarioServ != null)
		{
			if (UsuarioServ.fnGetUsuario().equals(p_Usuario) == true)
			{
				Usuario = new UsuarioDef(UsuarioServ.fnGetUsuario(),
										 UsuarioServ.fnGetPerfil(),
										 buscaAcesso(UsuarioServ.fnGetAcesso()),
										 UsuarioServ.fnGetSenha(),
										 UsuarioServ.fnGetDicaSenha(),
										 UsuarioServ.fnGetIdPerfil(),
										 UsuarioServ.fnGetId(),
										 UsuarioServ.fnGetAcesso(),									
										 (short) 0, // Sequencia
										 (int) 0,
										 UsuarioServ.fnGetNomeCompleto(),
										 UsuarioServ.fnGetEmail(),
										 UsuarioServ.fnGetTelefone(),
										 UsuarioServ.fnGetRamal(),
										 UsuarioServ.fnGetArea(),
										 UsuarioServ.fnGetRegional(),
										 UsuarioServ.fnGetMotivoAcesso(),
										 
										 UsuarioServ.fnGetResponsavel(),
										 UsuarioServ.fnGetDtAtivacaoStr()); // Hora de logon
				Usuario.setConfWebHistorico(UsuarioServ.fnGetCfgWebHist());
				Usuario.setConfWebAgenda(UsuarioServ.fnGetCfgWebAgn());
				Usuario.setConfWebNormal(UsuarioServ.fnGetCfgWebNorm());

				return Usuario;
			}

			UsuarioServ = m_iListaUsuarios.fnGetProx((short) 0);
		}

		return null;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vector onde cada elemento corresponde a uma linha do arquivo PermissoesRel.txt
	 * @roseuid 3FBA052000A3
	 */
	public Vector getPermissoesRel()
	{
		Vector Retorno = new Vector();
		String Linha = new String("inicio");
		short i = 0;

		while (!Linha.equals(""))
		{
			Linha = new String(m_iUtil.fnGetListaPermissoes(i));
			i++;

			if (!Linha.equals(""))
			{
				Retorno.addElement(Linha);
			}
		}

		return Retorno;
	}

	/**
	 * função que retorna a lista de permissões para as bases exportadas
	 * @return
	 */
	
	public Vector getPermissoesBasesExportadas()
	{
		Vector Retorno = new Vector();
		String Linha = new String("inicio");
		short i = 0;

		while (!Linha.equals(""))
		{
			Linha = new String(m_iUtil.fnGetListaPermissoesExp(i));
			i++;

			if (!Linha.equals(""))
			{
				Retorno.addElement(Linha);
			}
		}

		return Retorno;
	}

	
	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos UsuarioDef (ordenado pelo nome do usuário) que espelha a lista de usuários do Servidor Uitl.
	 * @roseuid 3C0650DC0070
	 */
	public Vector getListaUsuarios()
	{
		UsuarioDef Usuario = null;
		iUsuarioWeb UsuarioWeb = null;
		Vector ListaUsuarios = null;

		UsuarioWeb = m_iListaUsuarioWeb.fnGetInicio();

		while (UsuarioWeb != null)
		{
			Usuario = new UsuarioDef(UsuarioWeb.fnGetUsuario(),
									 UsuarioWeb.fnGetPerfil(),
									 buscaAcesso(UsuarioWeb.fnGetAcesso()),
									 UsuarioWeb.fnGetSenha(),
									 UsuarioWeb.fnGetDicaSenha(),
									 UsuarioWeb.fnGetIDPerfil(),
									 UsuarioWeb.fnGetIDUsuario(),
									 UsuarioWeb.fnGetAcesso(),
									 UsuarioWeb.fnGetSequencia(),
									 UsuarioWeb.fnGetDataAcesso(),
									 "","","","","","","","","");
			Usuario.setIDSessao(UsuarioWeb.fnGetIDSessao());
			Usuario.setIP(UsuarioWeb.fnGetIP());
			//Usuario.setHost(UsuarioWeb.fnGetHost());
			Usuario.setNo(this.getNo());
			Usuario.setHost(this.getNo().getHostName());
			Usuario.setDataAcessoStr(UsuarioWeb.fnGetAcessoStr());

			if (ListaUsuarios == null)
			{
				ListaUsuarios = new Vector();
				ListaUsuarios.addElement(Usuario);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				UsuarioDef UsuarioAux = null;

				for (short i = 0; i < ListaUsuarios.size(); i++)
				{
					UsuarioAux = (UsuarioDef) ListaUsuarios.elementAt(i);

					if (Usuario.getUsuario().compareTo(UsuarioAux.getUsuario()) < 0)
					{
						ListaUsuarios.add(i, Usuario);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaUsuarios.addElement(Usuario);
				}
			}

			UsuarioWeb = m_iListaUsuarioWeb.fnGetProx2(UsuarioWeb);
		}

		return ListaUsuarios;
	}

	/**
	 * @param p_IDSessao
	 * @return UsuarioDef
	 * @exception
	 * Retorna o usuário que se logou ao sistema cujo ID da sessão seja idêntico ao parâmetro  passado.
	 * @roseuid 3C8CE5550190
	 */
	public UsuarioDef getUsuarioSessao(String p_IDSessao)
	{
		UsuarioDef UsuarioSessao = null;
		iUsuarioWeb UsuarioWeb = null;

		UsuarioWeb = m_iListaUsuarioWeb.fnUsuarioExisteSessao(p_IDSessao);

		if (UsuarioWeb != null)
		{
			UsuarioSessao = new UsuarioDef(UsuarioWeb.fnGetUsuario(),
										   UsuarioWeb.fnGetPerfil(),
										   buscaAcesso(UsuarioWeb.fnGetAcesso()),
										   UsuarioWeb.fnGetSenha(),
										   UsuarioWeb.fnGetDicaSenha(),
										   UsuarioWeb.fnGetIDPerfil(),
										   UsuarioWeb.fnGetIDUsuario(),
										   UsuarioWeb.fnGetAcesso(),
										   UsuarioWeb.fnGetSequencia(),
										   UsuarioWeb.fnGetDataAcesso(),
										   "","","","","","","","","");
			UsuarioSessao.setIDSessao(UsuarioWeb.fnGetIDSessao());
			UsuarioSessao.setIP(UsuarioWeb.fnGetIP());
			//UsuarioSessao.setHost(UsuarioWeb.fnGetHost());
			UsuarioSessao.setHost(this.getNo().getHostName());
			UsuarioSessao.setNo(this.getNo());
			UsuarioSessao.setDataAcessoStr(UsuarioWeb.fnGetAcessoStr());
		}
		else
		{
			System.out.println("Usuario não encontrado na lista!!!");
		}

		return UsuarioSessao;
	}

	/**
	 * @return UsuarioDef
	 * @exception
	 * Retorna o último usuário que se logon no sistema (primeiro usuário da lista).
	 * @roseuid 3C2DD581036F
	 */
	public UsuarioDef getUltimoUsuario()
	{
		UsuarioDef UltimoUsuario = null;
		iUsuarioWeb UsuarioWeb = null;

		UsuarioWeb = m_iListaUsuarioWeb.fnGetFim();

		if (UsuarioWeb != null)
		{
			UltimoUsuario = new UsuarioDef(UsuarioWeb.fnGetUsuario(),
										   UsuarioWeb.fnGetPerfil(),
										   buscaAcesso(UsuarioWeb.fnGetAcesso()),
										   UsuarioWeb.fnGetSenha(),
										   UsuarioWeb.fnGetDicaSenha(),
										   UsuarioWeb.fnGetIDPerfil(),
										   UsuarioWeb.fnGetIDUsuario(),
										   UsuarioWeb.fnGetAcesso(),
										   UsuarioWeb.fnGetSequencia(),
										   UsuarioWeb.fnGetDataAcesso(),
										   "","","","","","","","","");
			UltimoUsuario.setIP(UsuarioWeb.fnGetIP());
			UltimoUsuario.setHost(UsuarioWeb.fnGetHost());
		}

		return UltimoUsuario;
	}

	public boolean existeUsuariobySessao(String idSessao)
	{
	    UsuarioDef usuario = getUsuarioSessao(idSessao);

	    return (usuario != null) ? true : false;
	}

	public boolean existeUsuariobyNome(String nome)
	{
	    iUsuario usuarioServ = m_iListaUsuarios.fnUsuarioExiste(nome);

	    return (usuarioServ != null) ? true : false;
	}

	/**
	 * Metodo que verifica se o Servutil trata determinado Perfil.
	 * */
	public boolean existePerfilbyNome(String nome)
	{
	    Vector listaPerfis = getListaPerfisOtimizado();

		for (Iterator iter = listaPerfis.iterator(); iter.hasNext();)
        {
		    PerfilCfgDef perfil = (PerfilCfgDef) iter.next();

            if (perfil.getPerfil().equalsIgnoreCase(nome))
                return true;
        }
		return false;
	}

	/**
	 * Metodo que verifica se o Servutil trata determinado Perfil.
	 * */
	public boolean existePerfilbyID(int idPerfil)
	{
	    Vector listaPerfis = getListaPerfisOtimizado();
		for (Iterator iter = listaPerfis.iterator(); iter.hasNext();)
        {
		    PerfilCfgDef perfil = (PerfilCfgDef) iter.next();

            if (perfil.getId() == idPerfil)
                return true;
        }
		
		
		return false;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos PerfisCfg (perfis configurados no Servidor).
	 * @roseuid 3C23D4DC010A
	 */
	public Vector getListaPerfisCfg()
	{
		AcessoCfgDef Acesso;
		PerfilCfgDef Perfil = null;
		UsuarioDef Usuario = null;
		iPerfil PerfilServ = null;
		iListaUsr ListaUsrServ = null;
		iUsuario UsuarioServ;
		Vector ListaPerfis = null;
		Vector ListaUsuarios = null;
		Vector ListaAcessos = null;

		// Prepara lista de tipos de acessos
		ListaAcessos = getListaAcessoCfg();

		// Lê lista de perfis
		PerfilServ = m_iListaPerfis.fnGetInicio((short) 0);

		while (PerfilServ != null)
		{
			// Lê lista de usuários do perfil
			ListaUsrServ = PerfilServ.fnGetUsuarios();
			UsuarioServ = ListaUsrServ.fnGetInicio((short) 0);
			ListaUsuarios = null;

			while (UsuarioServ != null)
			{
				Usuario = new UsuarioDef(UsuarioServ.fnGetUsuario(),
										 PerfilServ.fnGetPerfil(),
										 buscaAcesso(UsuarioServ.fnGetAcesso()),
										 UsuarioServ.fnGetSenha(),
										 UsuarioServ.fnGetDicaSenha(),
										 PerfilServ.fnGetId(),
										 UsuarioServ.fnGetId(),
										 PerfilServ.fnGetAcesso(),
										 (short) 0, // Sequencia
										 (int) 0,
										 UsuarioServ.fnGetNomeCompleto(),
										 UsuarioServ.fnGetEmail(),
										 UsuarioServ.fnGetTelefone(),
										 UsuarioServ.fnGetRamal(),
										 UsuarioServ.fnGetArea(),
										 UsuarioServ.fnGetRegional(),
										 UsuarioServ.fnGetMotivoAcesso(),
										 UsuarioServ.fnGetResponsavel(),
										 UsuarioServ.fnGetDtAtivacaoStr()); // Hora de logon

				if (ListaUsuarios == null)
				{
					ListaUsuarios = new Vector();
					ListaUsuarios.addElement(Usuario);
				}
				else
				{
					// Insere os elementos ordenadamente
					boolean Inseriu = false;
					UsuarioDef UsuarioAux = null;

					for (short i = 0; i < ListaUsuarios.size(); i++)
					{
						UsuarioAux = (UsuarioDef) ListaUsuarios.elementAt(i);

						if (Usuario.getUsuario().compareTo(UsuarioAux.getUsuario()) < 0)
						{
							ListaUsuarios.add(i, Usuario);
							Inseriu = true;

							break;
						}
					}

					if (Inseriu == false)
					{
						ListaUsuarios.addElement(Usuario);
					}
				}

				UsuarioServ = ListaUsrServ.fnGetProx2(UsuarioServ);
			}

			Perfil = new PerfilCfgDef(PerfilServ.fnGetPerfil(),
									  PerfilServ.fnGetId(),
									  PerfilServ.fnGetAcesso(),
									  ListaUsuarios,
									  PerfilServ.fnGetBloqueio());
			Perfil.setHost(this.getNo().getHostName());
			
			Perfil.setSigiloTelefonico(PerfilServ.fnGetSigiloTelefonico());
			if(PerfilServ.fnGetIdsTecCSV().equalsIgnoreCase("todas")){
				Perfil.setSelecionouTodas(true);
				Perfil.setIdTecnologias("todas");//porque sao todas
			}
			else{
				Perfil.setSelecionouTodas(false);
				Perfil.setIdTecnologias(PerfilServ.fnGetIdsTecCSV());
			}
			
			if(PerfilServ.fnGetIdsRelCSV().equalsIgnoreCase("todas")){
				Perfil.setSelecionouTodosRel(true);
				Perfil.setIdrelatorios("todas");//porque sao todas
			}
			else{
				Perfil.setSelecionouTodosRel(false);
				/*gato que serve pra tirar sempre o campo da string que o servidor manda
				 * exemplo : 1;2;199 e o certo e: 1;2 */
				String idsRelServ[] = PerfilServ.fnGetIdsRelCSV().split(";");
				StringBuffer idsRel = new StringBuffer();
				for(int i=0; i<idsRelServ.length-1; i++)
				{
					idsRel.append(idsRelServ[i]);
					if(i<idsRelServ.length-2)
					{
						idsRel.append(";");
					}
				}
				Perfil.setIdrelatorios(idsRel.toString());
			}
			
			//para relatorios historicos copia do codigo acima so mundando o 
			//id para pegar os relatorios não agendados
			if(PerfilServ.fnGetIdsRelHistoricoCSV().equalsIgnoreCase("todas")){
				Perfil.setSelecionouTodosRelHistorico(true);
				Perfil.setIdrelatoriosHistoricos("todas");//porque sao todas
			}
			else{
				Perfil.setSelecionouTodosRelHistorico(false);
				/*gato que serve pra tirar sempre o campo da string que o servidor manda
				 * exemplo : 1;2;199 e o certo e: 1;2 */
				String idsRelHistServ[] = PerfilServ.fnGetIdsRelHistoricoCSV().split(";");
				StringBuffer idsRelHist = new StringBuffer();
				for(int i=0; i<idsRelHistServ.length-1; i++)
				{
					idsRelHist.append(idsRelHistServ[i]);
					if(i<idsRelHistServ.length-2)
					{
						idsRelHist.append(";");
					}
				}
				Perfil.setIdrelatoriosHistoricos(idsRelHist.toString());
			}
			
			
			for (short i = 0; i < ListaAcessos.size(); i++)
			{
				Acesso = (AcessoCfgDef) ListaAcessos.elementAt(i);

				if (Acesso.getId() == Perfil.getAcesso())
				{
					Perfil.setAcessoNome(Acesso.getAcesso());
				}
			}

			if (ListaPerfis == null)
			{
				ListaPerfis = new Vector();
				ListaPerfis.addElement(Perfil);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				PerfilCfgDef PerfilAux = null;

				for (short i = 0; i < ListaPerfis.size(); i++)
				{
					PerfilAux = (PerfilCfgDef) ListaPerfis.elementAt(i);

					if (Perfil.getPerfil().compareTo(PerfilAux.getPerfil()) < 0)
					{
						ListaPerfis.add(i, Perfil);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaPerfis.addElement(Perfil);
				}
			}

			PerfilServ = m_iListaPerfis.fnGetProx2(PerfilServ);
		}
		return ListaPerfis;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos PerfisCfg so com os atributos id e nome é simplicação
	 * do metodo acima para otimização de classes que só precisam de nome e id do perfil. 
	 * Ele só é usado na operação: OpApresentaPermissoesRel
	 */
	public Vector getListaPerfisOtimizado()
	{
		iPerfil PerfilServ = null;
		PerfilCfgDef perfil = null;
		PerfilServ = m_iListaPerfis.fnGetInicio((short) 0);
		Vector listaPerfis = null;
	    Vector listaUsuarios = new Vector();//so pra enganar o servidor que não pode receber null
		while(PerfilServ != null)
	    {
			perfil = new PerfilCfgDef(PerfilServ.fnGetPerfil(),
					  PerfilServ.fnGetId(),
					  PerfilServ.fnGetAcesso(),
					  listaUsuarios,
					  PerfilServ.fnGetBloqueio());
			if (listaPerfis == null)
			{
				listaPerfis = new Vector();
				listaPerfis.addElement(perfil);
			}
			else
			{
				//Insere os elementos ordenadamente
				boolean Inseriu = false;
				PerfilCfgDef PerfilAux = null;

				for (short i = 0; i < listaPerfis.size(); i++)
				{
					PerfilAux = (PerfilCfgDef) listaPerfis.elementAt(i);

					if (perfil.getPerfil().compareTo(PerfilAux.getPerfil()) < 0)
					{
						listaPerfis.add(i, perfil);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					listaPerfis.addElement(perfil);
				}
			}
			
			PerfilServ = m_iListaPerfis.fnGetProx2(PerfilServ);
		}
		return listaPerfis;
	}
	
	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos UsuariosCfg (usuarios configurados no Servidor).
	 * @roseuid 3C23D536022C
	 */
	public Vector getListaUsuariosCfg()
	{
		UsuarioDef Usuario;
		iUsuario UsuarioServ;
		Vector ListaUsuarios = null;
		ListaUsuarios = new Vector();

		UsuarioServ = m_iListaUsuarios.fnGetInicio((short) 0);

		
		while (UsuarioServ != null)
		{
			if(DefsComum.s_CLIENTE.equalsIgnoreCase("CLARO")){
				
			
			Usuario = new UsuarioDef(UsuarioServ.fnGetUsuario(),
									 UsuarioServ.fnGetPerfil(),
									 buscaAcesso(UsuarioServ.fnGetAcesso()),
									 UsuarioServ.fnGetSenha(),
									 UsuarioServ.fnGetDicaSenha(),
									 UsuarioServ.fnGetIdPerfil(),
									 UsuarioServ.fnGetId(),
									 UsuarioServ.fnGetAcesso(),
									 (short) 0, // Sequencia
									 (int) 0,
									 UsuarioServ.fnGetNomeCompleto(),
									 UsuarioServ.fnGetEmail(),
									 UsuarioServ.fnGetTelefone(),
									 UsuarioServ.fnGetRamal(),
									 UsuarioServ.fnGetArea(),
									 UsuarioServ.fnGetRegional(),
									 UsuarioServ.fnGetMotivoAcesso(),
									 UsuarioServ.fnGetResponsavel(),
									 UsuarioServ.fnGetDtAtivacaoStr(), // Hora de logon
									 UsuarioServ.fnGetOrdemServico());
			}				
			
			else{
				Usuario = new UsuarioDef(UsuarioServ.fnGetUsuario(),
						 UsuarioServ.fnGetPerfil(),
						 buscaAcesso(UsuarioServ.fnGetAcesso()),
						 UsuarioServ.fnGetSenha(),
						 UsuarioServ.fnGetDicaSenha(),
						 UsuarioServ.fnGetIdPerfil(),
						 UsuarioServ.fnGetId(),
						 UsuarioServ.fnGetAcesso(),
						 (short) 0, // Sequencia
						 (int) 0,
						 UsuarioServ.fnGetNomeCompleto(),
						 UsuarioServ.fnGetEmail(),
						 UsuarioServ.fnGetTelefone(),
						 UsuarioServ.fnGetRamal(),
						 UsuarioServ.fnGetArea(),
						 UsuarioServ.fnGetRegional(),
						 UsuarioServ.fnGetMotivoAcesso(),
						 UsuarioServ.fnGetResponsavel(),
						 UsuarioServ.fnGetDtAtivacaoStr()); // Hora de logon
			}
			Usuario.setHost(this.getNo().getHostName());
			Usuario.setConfWebHistorico(UsuarioServ.fnGetCfgWebHist());
			Usuario.setConfWebAgenda(UsuarioServ.fnGetCfgWebAgn());
			Usuario.setConfWebNormal(UsuarioServ.fnGetCfgWebNorm());

			if (ListaUsuarios == null)
			{
				ListaUsuarios = new Vector();
				ListaUsuarios.addElement(Usuario);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				UsuarioDef UsuarioAux = null;

				for (short i = 0; i < ListaUsuarios.size(); i++)
				{
					UsuarioAux = (UsuarioDef) ListaUsuarios.elementAt(i);

					if (Usuario.getUsuario().compareTo(UsuarioAux.getUsuario()) < 0)
					{
						ListaUsuarios.add(i, Usuario);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaUsuarios.addElement(Usuario);
				}
			}

			UsuarioServ = m_iListaUsuarios.fnGetProx2(UsuarioServ);
		}

		return ListaUsuarios;
	}
	
	/**
	 * Retorna a lista de usuarios de acordo com os filtros
	 * @param filtros
	 * @return lista de usuarios
	 */
	public ArrayList getListaUsuariosFiltros(String filtros) throws COMM_FAILURE{
		ArrayList usuarios = new ArrayList();
		String listaStr = m_iListaUsuarios.fnBuscaUsuarios(filtros);
		if(listaStr.equalsIgnoreCase("")){
			return usuarios;
		}
		
		String[] linhas = listaStr.split("\n");
		for (int i = 0; i < linhas.length; i++) {
			String linha = linhas[i]+" ;";
			String[] lista = linha.split(";");
			UsuarioDef usuario;
			/*
			00 - idUsuario
			01 - idPerfil
			02 - login;
			03 - perfil;
			04 - dicasenha;
			05 - email;
			06 - telefone;
			07 - regional;
			08 - area;
			09 - responsavel;
			10 - motivo;
			11 - dtAtivacao, 00/01/1900 00:00:00;
			12 - nome;
			13 - ramal;
			14 - ordem de serviço
			*/
			try{
				if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro")){
					usuario = new UsuarioDef(
							lista[2], //login
							lista[3], //perfil
							"", //acesso
							"", //senha
							lista[4], //dicasenha
							Integer.parseInt(lista[1]), //idPerfil
							Integer.parseInt(lista[0]), //idUsuario
							(short) 0, //acesso
							(short) 0, //sequencia
							(int) 0, 
							lista[12], // nome
							lista[5], //email
							lista[6], //telefone
							lista[13], //ramal
							lista[8], //area
							lista[7], //regional
							lista[10], //motivo
							lista[9], //responsavel
							lista[11],//ativacao
							lista[14]); //ordem de serviço
				}
				else{
					usuario = new UsuarioDef(
							lista[2], //login
							lista[3], //perfil
							"", //acesso
							"", //senha
							lista[4], //dicasenha
							Integer.parseInt(lista[1]), //idPerfil
							Integer.parseInt(lista[0]), //idUsuario
							(short) 0, //acesso
							(short) 0, //sequencia
							(int) 0, 
							lista[12], // nome
							lista[5], //email
							lista[6], //telefone
							lista[13], //ramal
							lista[8], //area
							lista[7], //regional
							lista[10], //motivo
							lista[9], //responsavel
							lista[11]); //ativacao
				}
				usuario.setHost(this.getNo().getHostName());
				usuarios.add(usuario);
			}catch (Exception e) {
				System.out.println("Problema ao buscar usuarios com filtro: "+e);
			}
		}
		return usuarios;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos AcessoCfg (tipos de acesso possíveis para os perfis do sistema).
	 * @roseuid 3C30E7F00084
	 */
	public Vector getListaAcessoCfg()
	{
		AcessoCfgDef Acesso = null;
		iAcesso AcessoServ = null;
		Vector ListaAcessos = null;

		AcessoServ = m_iListaAcesso.fnGetInicio();

		while (AcessoServ != null)
		{
			Acesso = new AcessoCfgDef(AcessoServ.fnGetAcessoNome(),
									  AcessoServ.fnGetAcessoId());

			if (ListaAcessos == null)
			{
				ListaAcessos = new Vector();
				ListaAcessos.addElement(Acesso);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				AcessoCfgDef AcessoAux = null;

				for (short i = 0; i < ListaAcessos.size(); i++)
				{
					AcessoAux = (AcessoCfgDef) ListaAcessos.elementAt(i);

					if (Acesso.getAcesso().compareTo(AcessoAux.getAcesso()) < 0)
					{
						ListaAcessos.add(i, Acesso);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaAcessos.addElement(Acesso);
				}
			}

			AcessoServ = m_iListaAcesso.fnGetProx2(AcessoServ);
		}

		return ListaAcessos;
	}

	/**
	 * @param p_Perfil
	 * @param p_Acesso
	 * @return boolean
	 * @exception
	 * Inclui um perfil na configuração do sistema.
	 * @roseuid 3C335E920060
	 */
	public boolean incluiPerfil(String p_Perfil,String p_Acesso, boolean sigiloTelefonico, String ids, boolean todasTecnologias, String idsRel, boolean todosRelatorios, String idsRelHistorico, boolean todosRelHistorico)
	{
		iPerfil PerfilServ = null;
		iAcesso AcessoServ;
		short IdAcesso = -1;

		//System.out.println(p_Perfil+" - "+p_Acesso);
		if (m_iListaAcesso != null)
		{
			AcessoServ = m_iListaAcesso.fnGetInicio();

			while (AcessoServ != null)
			{
				if (AcessoServ.fnGetAcessoNome().equals(p_Acesso))
				{
					IdAcesso = AcessoServ.fnGetAcessoId();

					break;
				}

				AcessoServ = m_iListaAcesso.fnGetProx();
			}
		}

		if (IdAcesso == -1)
		{
			return false;
		}

		if (m_iListaPerfis != null)
		{
			PerfilServ = m_iListaPerfis.fnAdicionaPerfil(p_Perfil, IdAcesso, sigiloTelefonico, ids, todasTecnologias, idsRel, todosRelatorios, idsRelHistorico, todosRelHistorico);//sigilo, ids, todas ou nao

			if (PerfilServ == null)
			{
				return false;
			}
		}

		return true;
	}

	public boolean incluiPerfilPorId(String p_Perfil, int maiorId, String p_Acesso, boolean sigiloTelefonico, String ids, boolean todasTecnologias, String idsRel, boolean todosRelatorios, String idsRelHistorico, boolean todosRelHistorico)
	{
		iPerfil PerfilServ = null;
		iAcesso AcessoServ;
		short IdAcesso = -1;

		//pega o maior id encontrado em todos os nós e soma mais um para garantir que nenhum nó terá um id repetido
		maiorId++;

		//System.out.println(p_Perfil+" - "+p_Acesso);
		if (m_iListaAcesso != null)
		{
			AcessoServ = m_iListaAcesso.fnGetInicio();

			while (AcessoServ != null)
			{
				if (AcessoServ.fnGetAcessoNome().equals(p_Acesso))
				{
					IdAcesso = AcessoServ.fnGetAcessoId();

					break;
				}

				AcessoServ = m_iListaAcesso.fnGetProx();
			}
		}

		if (IdAcesso == -1)
		{
			return false;
		}

		if (m_iListaPerfis != null)
		{
			PerfilServ = m_iListaPerfis.fnAdicionaPerfil(p_Perfil, IdAcesso, sigiloTelefonico, ids, todasTecnologias, idsRel, todosRelatorios, idsRelHistorico, todosRelHistorico);//sigilo, ids, todas ou nao

			if (PerfilServ == null)
			{
				return false;
			}
		}

		return true;
	}


	/**
	 * @param p_Perfil
	 * @param p_Id
	 * @return boolean
	 * @exception
	 * Exclui um perfil da configuração do sistema.
	 * @roseuid 3C335EC901B4
	 */
	public boolean excluiPerfil(String p_Perfil,
								String p_Id)
	{
		iPerfil PerfilServ = null;

		if (m_iListaPerfis != null)
		{
			PerfilServ = m_iListaPerfis.fnPerfilExiste(p_Perfil);

			if (PerfilServ == null)
			{
				return false;
			}
			else
			{
				if (m_iListaPerfis.fnRemovePerfil(PerfilServ) == false)
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param p_Perfil
	 * @param p_Operacao
	 * @return short
	 * @exception
	 * Bloqueia o acesso ao portal CDRView de um perfil da configuração do sistema.
	 * 0 - Erro
	 * 1 - Perfil bloqueado
	 * 2 - Perfil desbloqueado
	 * @roseuid 3C3CE87E02C0
	 */
	public short bloqueiaPerfil(String p_Perfil,
								String p_Operacao)
	{
		iPerfil PerfilServ = null;

		if (m_iListaPerfis != null)
		{
			PerfilServ = m_iListaPerfis.fnPerfilExiste(p_Perfil);

			if (PerfilServ == null)
			{
				return 0;
			}
			else
			{
				switch (p_Operacao.charAt(0))
				{
					case 'b':
						PerfilServ.fnSetBloqueio(true);
						PerfilServ.fnFlush();

						return 1;

					case 'd':
						PerfilServ.fnSetBloqueio(false);
						PerfilServ.fnFlush();

						return 2;
				}
			}
		}

		return 1;
	}

	/**
	 * Método para inserir usuario
	 * @param usuario
	 * @param senha
	 * @param perfil
	 * @param dicaSenha
	 * @param email
	 * @param ramal
	 * @param telefone
	 * @param regional
	 * @param area
	 * @param responsavel
	 * @param motivo
	 * @param ativacao
	 * @param sessao
	 * @return true se der sucesso, false se der erro
	 */
	public int incluiUsuario(
			String usuario,
			String senha,
			String perfil,
			String dicaSenha,
			String nome,
			String email,
			String ramal,
			String telefone,
			String regional,
			String area,
			String responsavel,
			String motivo,
			String ativacao,
			String sessao,
			String ordemServico)
	{
		iPerfil PerfilServ = null;
		iUsuario UsuarioServ = null;
		UsuarioDef Usuario;
		Vector ListaUsuarios = null;
		No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
            	noTmp = (No) iter.next();
        		ListaUsuarios = noTmp.getConexaoServUtil().getListaUsuariosCfg();

        		for (short i = 0; i < ListaUsuarios.size(); i++)
        		{
        			Usuario = (UsuarioDef) ListaUsuarios.elementAt(i);

        			if (Usuario.getUsuario().equals(usuario))
        			{
        				return 2;
        			}
        		}

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

		UsuarioServ = m_iListaUsuarios.fnAdicionaUsuario(
				usuario, 
				senha,
				perfil, 
				usuario,
				email,
				telefone,
				regional,
				area,
				responsavel,
				motivo,
				ativacao,
				nome,
				ramal,
				ordemServico);

		if (UsuarioServ != null)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	/**
	 * Editar usuário
	 * @param usuario
	 * @param senha
	 * @param perfil
	 * @param dicaSenha
	 * @param email
	 * @param ramal
	 * @param telefone
	 * @param regional
	 * @param area
	 * @param responsavel
	 * @param motivo
	 * @param ativacao
	 * @param sessao
	 * @return
	 */
	public boolean editaUsuario(
			String usuario,
			String senha,
			String perfil,
			String dicaSenha,
			String nome,
			String email,
			String ramal,
			String telefone,
			String regional,
			String area,
			String responsavel,
			String motivo,
			String ativacao,
			String sessao,
			String ordemServico)
	{
		iUsuario UsuarioServ = null;

        UsuarioServ = m_iListaUsuarios.fnEditaUsuario(
				usuario, 
				senha,
				perfil, 
				usuario,
				email,
				telefone,
				regional,
				area,
				responsavel,
				motivo,
				ativacao,
				nome,
				ramal,
				ordemServico);

		if (UsuarioServ != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	

	/**
	 * @param p_Usuario
	 * @return boolean
	 * @exception
	 * Exclui um usuário da configuração do sistema.
	 * @roseuid 3C35E8910396
	 */
	public boolean excluiUsuario(String p_Usuario)
	{
		iUsuario UsuarioServ = null;

		if (m_iListaUsuarios != null)
		{
			UsuarioServ = m_iListaUsuarios.fnUsuarioExiste(p_Usuario);

			if (UsuarioServ == null)
			{
				return false;
			}
			else
			{
				if (m_iListaUsuarios.fnRemoveUsuario(UsuarioServ) == false)
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param p_Usuario
	 * @param p_Senha
	 * @return boolean
	 * @exception
	 * @roseuid 3C87995A017C
	 */
	public boolean alteraSenha(String p_Usuario,
							   String p_Senha)
	{
		iUsuario UsuarioServ = null;

		if (m_iListaUsuarios != null)
		{
			UsuarioServ = m_iListaUsuarios.fnUsuarioExiste(p_Usuario);

			if (UsuarioServ != null)
			{
				UsuarioServ.fnSetSenha(p_Senha);
				UsuarioServ.fnFlush();

				return true;
			}
			else
			{
				return false;
			}
		}

		return false;
	}

	/**
	 * @param p_IdAceso
	 * @return String
	 * @exception
	 * Retorna o nome do tipo de acesso para o p_IdAceso do mesmo.
	 * @roseuid 3C38BFED024F
	 */
	public String buscaAcesso(short p_IdAceso)
	{
		AcessoCfgDef Acesso;
		Vector ListaAcessos = getListaAcessoCfg();

		for (short i = 0; i < ListaAcessos.size(); i++)
		{
			Acesso = (AcessoCfgDef) ListaAcessos.elementAt(i);

			if (Acesso.getId() == p_IdAceso)
			{
				return Acesso.getAcesso();
			}
		}

		return null;
	}

	/**
	 * @param p_Multisessao
	 * @param p_Quantidade
	 * @param p_NivelLog
	 * @return void
	 * @exception
	 * Seta as opções correntes setadas para configuração de usuários.
	 * @roseuid 3C3DA1FC01EA
	 */
	public void setOpcoes(short p_Multisessao,
						  short p_Quantidade,
						  short p_NivelLog)
	{
		m_iUtil.fnSetCfgUsuario(p_Multisessao, p_Quantidade, p_NivelLog);
	}

	/**
	 * @return Vector
	 * @exception
	 * Recupera as opções correntes setadas para configuração de usuários. Retorna um vetor de strings contendo:
	 * [0] : Multisessão
	 * [1] : Quantidade máxima de usuários
	 * [2] : Nível de log
	 * @roseuid 3C3DA0F700D6
	 */
	public Vector getOpcoes()
	{
		Vector Opcoes = new Vector(3);

		Opcoes.addElement(new Short(m_iUtil.fnGetMultiSessao()).toString());
		Opcoes.addElement(new Short(m_iUtil.fnGetNivelLog()).toString());
		Opcoes.addElement(new Short(m_iUtil.fnGetQuantidade()).toString());

		return Opcoes;
	}

	/**
	 * @param p_sIDTipoProc
	 * @param p_Processo
	 * @param p_DiaDaSemana
	 * @param p_HoraInicial
	 * @param p_HoraFinal
	 * @return boolean
	 * @exception
	 * Seta as informações para visualização do log.
	 * @roseuid 3C3D0AC60189
	 */
	public boolean setInfoLog(short p_sIDTipoProc,
							  String p_Processo,
							  short p_DiaDaSemana,
							  String p_HoraInicial,
							  String p_HoraFinal)
	{
		m_Log = m_iUtil.fnGetLogProcessos(p_sIDTipoProc, p_Processo,
										  p_DiaDaSemana, p_HoraInicial,
										  p_HoraFinal);

		if (m_Log == null)
		{
			return false;
		}

		return true;
	}

	public iLogView getInfoLog(short p_sIDTipoProc,
							  String p_Processo,
							  short p_DiaDaSemana,
							  String p_HoraInicial,
							  String p_HoraFinal)
	{

	    m_Log = m_iUtil.fnGetLogProcessos(p_sIDTipoProc, p_Processo,
										  p_DiaDaSemana, p_HoraInicial,
										  p_HoraFinal);

		return m_Log;
	}


	/**
	 * @return String
	 * @exception
	 * Recupera as linhas do log.
	 * @roseuid 3C3D0B820125
	 */
	public String getLinhaLog()
	{
		return m_Log.fnGetLinha();
	}

	/**
	 * @return void
	 * @exception
	 * Destroi o objeto de log.
	 * @roseuid 3C3E016101C8
	 */
	public void destroiLog()
	{
		m_Log.fnDestroy();
	}

	/**
	 * @param p_Operacao
	 * @param p_Valor
	 * @return void
	 * @exception
	 * Seta as informações para visualização do arquivo.
	 * @roseuid 3C50AD2B02F4
	 */
	public void setInfoArq(String p_Operacao,
						   String p_Valor)
	{
		m_Arq = m_iUtil.fnCriaInfoArq(p_Operacao, p_Valor);
	}

	/**
	 * @return String
	 * @exception
	 * Recupera as linhas do arquivo.
	 * @roseuid 3C50AD2B0330
	 */
	public String getLinhaArq()
	{
		return m_Arq.fnGetLinha();
	}

	/**
	 * @return void
	 * @exception
	 * Destroi o objeto de arquivo.
	 * @roseuid 3C50AE6B0272
	 */
	public void destroiArq()
	{
		try
		{
			if (m_Arq != null)
			{
				m_Arq.fnDestroy();
			}
		}
		catch (Exception Exc)
		{
			System.out.println("**************************");
			System.out.println(getDataHoraAtual());
			Exc.printStackTrace();
			System.out.println("**************************");
		}
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos TecnologiaCfgDef (ordenado pelo nome da tecnologia).
	 * @roseuid 3C43A3340367
	 */
	public Vector getListaTecnologiasCfg()
	{
		iTecnologia TecnologiaServ = null;
		TecnologiaCfgDef Tecnologia;
		Vector ListaTecnologias = null;

		TecnologiaServ = m_iListaTecnologias.fnGetInicio();

		while (TecnologiaServ != null)
		{
			Tecnologia = new TecnologiaCfgDef(TecnologiaServ.fnGetTecnologia());

			if (ListaTecnologias == null)
			{
				ListaTecnologias = new Vector();
				ListaTecnologias.addElement(Tecnologia);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				TecnologiaCfgDef TecnologiaAux = null;

				for (short i = 0; i < ListaTecnologias.size(); i++)
				{
					TecnologiaAux = (TecnologiaCfgDef) ListaTecnologias.elementAt(i);

					if (Tecnologia.getTecnologia().compareTo(TecnologiaAux.getTecnologia()) < 0)
					{
						ListaTecnologias.add(i, Tecnologia);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaTecnologias.addElement(Tecnologia);
				}
			}

			TecnologiaServ = m_iListaTecnologias.fnGetProx2(TecnologiaServ);
		}

		return ListaTecnologias;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos BilhetadorCfgDef (ordenado pelo nome do bilhetador).
	 * @roseuid 3C43A3680087
	 */
	public Vector getListaBilhetadoresCfg()
	{
		iBilCfg BilhetadorServ = null;
		BilhetadorCfgDef Bilhetador;
		TecnologiaCfgDef Tecnologia;
		OperadoraCfgDef Operadora;
		Vector ListaBilhetadores = null;

		if(m_iListaBilhetadores != null)
			BilhetadorServ = m_iListaBilhetadores.fnGetInicio();

		while (BilhetadorServ != null)
		{
			Tecnologia = new TecnologiaCfgDef(BilhetadorServ.fnGetTecnologia());
			Operadora = new OperadoraCfgDef(BilhetadorServ.fnGetOperadora());
			Bilhetador = new BilhetadorCfgDef(BilhetadorServ.fnGetNome(),
											  Tecnologia,
											  Operadora);
			Bilhetador.setApelido(BilhetadorServ.fnGetApelido());
			Bilhetador.setOciosidade(BilhetadorServ.fnGetOciosidade());
			Bilhetador.setFase(BilhetadorServ.fnGetEmTest());
			Bilhetador.setHabilita(BilhetadorServ.fnGetHabilita());

			if (ListaBilhetadores == null)
			{
				ListaBilhetadores = new Vector();
				ListaBilhetadores.addElement(Bilhetador);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				BilhetadorCfgDef BilhetadorAux = null;

				for (short i = 0; i < ListaBilhetadores.size(); i++)
				{
					BilhetadorAux = (BilhetadorCfgDef) ListaBilhetadores.elementAt(i);

					if (Bilhetador.getBilhetador().compareTo(BilhetadorAux.getBilhetador()) < 0)
					{
						ListaBilhetadores.add(i, Bilhetador);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaBilhetadores.addElement(Bilhetador);
				}
			}

			BilhetadorServ = m_iListaBilhetadores.fnGetProx2(BilhetadorServ);
		}

		return ListaBilhetadores;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos ConfiguracaoConvCfg (ordenado pelo nome da configuração).
	 * @roseuid 3C486F6C03CD
	 */
	public Vector getListaCfgConversores()
	{
		ConfiguracaoConvCfgDef Configuracao = null;
		iConversorCfg ConfiguracaoServ = null;
		iListaConversorCfgs ListaConfiguracoesServ = null;
		Vector ListaConfiguracoes = null;
		Vector InfoCfg = null;
		Vector Bilhetadores = null;
		String ConfigNome = null;
		String ServidorNome = null;
		String Relacioamento = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaConfiguracao();
		ConfiguracaoServ = ListaConfiguracoesServ.fnGetInicio();

		while (ConfiguracaoServ != null)
		{
			InfoCfg = new Vector();
			Relacioamento = ConfiguracaoServ.fnGetLinha();

			StringTokenizer Config = new StringTokenizer(Relacioamento);

			while (Config.hasMoreTokens())
				InfoCfg.add(Config.nextToken(";").toString());

			Bilhetadores = new Vector();
			Config = new StringTokenizer((String) InfoCfg.elementAt(3));

			while (Config.hasMoreTokens())
				Bilhetadores.add(Config.nextToken(",").toString());

			Configuracao = new ConfiguracaoConvCfgDef((String) InfoCfg.elementAt(0),
													  (String) InfoCfg.elementAt(1),
													  (String) InfoCfg.elementAt(2),
													  Bilhetadores,
													  Relacioamento);

			if (ListaConfiguracoes == null)
			{
				ListaConfiguracoes = new Vector();
				ListaConfiguracoes.addElement(Configuracao);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConfiguracaoConvCfgDef ConfiguracaoAux = null;

				for (short i = 0; i < ListaConfiguracoes.size(); i++)
				{
					ConfiguracaoAux = (ConfiguracaoConvCfgDef) ListaConfiguracoes.elementAt(i);

					if (Configuracao.getNome().compareTo(ConfiguracaoAux.getNome()) < 0)
					{
						ListaConfiguracoes.add(i, Configuracao);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaConfiguracoes.addElement(Configuracao);
				}
			}

			ConfiguracaoServ = ListaConfiguracoesServ.fnGetProx2(ConfiguracaoServ);
		}

		if (ListaConfiguracoes != null)
		{
			ListaConfiguracoes.trimToSize();
		}

		return ListaConfiguracoes;
	}
	
	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos ConfiguracaoConvCfg (ordenado pelo nome da configuração).
	 * @roseuid 3C486F6C03CD
	 */
	public Vector getListaCfgReprocessadores()
	{
		//TODO Trocar interfaces para as de Reprocessadores
		ConfiguracaoReprocCfgDef Configuracao = null;
		iReprocessadorCfg ConfiguracaoServ = null;
		iListaReprocCfgs ListaConfiguracoesServ = null;
		Vector ListaConfiguracoes = null;
		Vector InfoCfg = null;
		Vector Bilhetadores = null;
		String ConfigNome = null;
		String ServidorNome = null;
		String Relacioamento = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaReprocCfgs();
		ConfiguracaoServ = ListaConfiguracoesServ.fnGetInicio();

		while (ConfiguracaoServ != null)
		{
			InfoCfg = new Vector();
			Relacioamento = ConfiguracaoServ.fnGetLinha();

			StringTokenizer Config = new StringTokenizer(Relacioamento);

			while (Config.hasMoreTokens())
				InfoCfg.add(Config.nextToken(";").toString());

			Bilhetadores = new Vector();
			Config = new StringTokenizer((String) InfoCfg.elementAt(3));

			while (Config.hasMoreTokens())
				Bilhetadores.add(Config.nextToken(",").toString());

			Configuracao = new ConfiguracaoReprocCfgDef((String) InfoCfg.elementAt(0),
													  (String) InfoCfg.elementAt(1),
													  (String) InfoCfg.elementAt(2),
													  Bilhetadores,
													  Relacioamento);

			if (ListaConfiguracoes == null)
			{
				ListaConfiguracoes = new Vector();
				ListaConfiguracoes.addElement(Configuracao);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConfiguracaoReprocCfgDef ConfiguracaoAux = null;

				for (short i = 0; i < ListaConfiguracoes.size(); i++)
				{
					ConfiguracaoAux = (ConfiguracaoReprocCfgDef) ListaConfiguracoes.elementAt(i);

					if (Configuracao.getNome().compareTo(ConfiguracaoAux.getNome()) < 0)
					{
						ListaConfiguracoes.add(i, Configuracao);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaConfiguracoes.addElement(Configuracao);
				}
			}

			ConfiguracaoServ = ListaConfiguracoesServ.fnGetProx2(ConfiguracaoServ);
		}

		if (ListaConfiguracoes != null)
		{
			ListaConfiguracoes.trimToSize();
		}

		return ListaConfiguracoes;
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos ConfiguracaoConvCfg (ordenado pelo nome da configuração) contendo as configurações dos parsers.
	 * @roseuid 3C8E6CDA01B6
	 */
	public Vector getListaCfgParsers()
	{
		ConfiguracaoConvCfgDef Configuracao = null;
		iParserCfg ParserServ = null;
		iListaParserCfgs ListaParsersServ = null;
		Vector ListaConfiguracoesParsers = null;
		Vector InfoCfg = null;
		Vector Bilhetadores = null;
		String ConfigNome = null;
		String ServidorNome = null;
		String Relacioamento = null;

		ListaParsersServ = m_iUtil.fnGetListaParserCfgs();
		ParserServ = ListaParsersServ.fnGetInicio();

		while (ParserServ != null)
		{
			InfoCfg = new Vector();
			Relacioamento = ParserServ.fnGetLinha();

			StringTokenizer Config = new StringTokenizer(Relacioamento);

			while (Config.hasMoreTokens())
				InfoCfg.add(Config.nextToken(";").toString());

			Bilhetadores = new Vector();
			Config = new StringTokenizer((String) InfoCfg.elementAt(3));

			while (Config.hasMoreTokens())
				Bilhetadores.add(Config.nextToken(",").toString());

			Configuracao = new ConfiguracaoConvCfgDef((String) InfoCfg.elementAt(0),
													  (String) InfoCfg.elementAt(1),
													  (String) InfoCfg.elementAt(2),
													  Bilhetadores,
													  Relacioamento);

			if (ListaConfiguracoesParsers == null)
			{
				ListaConfiguracoesParsers = new Vector();
				ListaConfiguracoesParsers.addElement(Configuracao);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConfiguracaoConvCfgDef ConfiguracaoAux = null;

				for (short i = 0; i < ListaConfiguracoesParsers.size(); i++)
				{
					ConfiguracaoAux = (ConfiguracaoConvCfgDef) ListaConfiguracoesParsers.elementAt(i);

					if (Configuracao.getNome().compareTo(ConfiguracaoAux.getNome()) < 0)
					{
						ListaConfiguracoesParsers.add(i, Configuracao);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaConfiguracoesParsers.addElement(Configuracao);
				}
			}

			ParserServ = ListaParsersServ.fnGetProx2(ParserServ);
		}

		if (ListaConfiguracoesParsers != null)
		{
			ListaConfiguracoesParsers.trimToSize();
		}

		return ListaConfiguracoesParsers;
	}
	
	/**
	 * método que retorna as configurações do parsergen
	 * 19/04/2006
	 * */

	public Vector getListaCfgParsersGen()
	{
		ConfiguracaoConvCfgDef Configuracao = null;
		iParserGenCfg ParserGenServ = null;
		iListaParserGenCfg ListaParsersGenServ = null;
		Vector ListaConfiguracoesParsersGen = null;
		Vector InfoCfg = null;
		Vector Bilhetadores = null;
		String ConfigNome = null;
		String ServidorNome = null;
		String Relacionamento = null;

		ListaParsersGenServ = m_iUtil.fnGetListaParserGenCfg();
		ParserGenServ = ListaParsersGenServ.fnGetInicio();

		while (ParserGenServ != null)
		{
			InfoCfg = new Vector();
			Relacionamento = ParserGenServ.fnGetLinha();

			StringTokenizer Config = new StringTokenizer(Relacionamento);

			while (Config.hasMoreTokens())
				InfoCfg.add(Config.nextToken(";").toString());

			Bilhetadores = new Vector();
			Config = new StringTokenizer((String) InfoCfg.elementAt(3));

			while (Config.hasMoreTokens())
				Bilhetadores.add(Config.nextToken(",").toString());

			Configuracao = new ConfiguracaoConvCfgDef((String) InfoCfg.elementAt(0),
													  (String) InfoCfg.elementAt(1),
													  (String) InfoCfg.elementAt(2),
													  Bilhetadores,
													  Relacionamento);

			if (ListaConfiguracoesParsersGen == null)
			{
				ListaConfiguracoesParsersGen = new Vector();
				ListaConfiguracoesParsersGen.addElement(Configuracao);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConfiguracaoConvCfgDef ConfiguracaoAux = null;

				for (short i = 0; i < ListaConfiguracoesParsersGen.size(); i++)
				{
					ConfiguracaoAux = (ConfiguracaoConvCfgDef) ListaConfiguracoesParsersGen.elementAt(i);

					if (Configuracao.getNome().compareTo(ConfiguracaoAux.getNome()) < 0)
					{
						ListaConfiguracoesParsersGen.add(i, Configuracao);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaConfiguracoesParsersGen.addElement(Configuracao);
				}
			}

			ParserGenServ = ListaParsersGenServ.fnGetProx2(ParserGenServ);
		}

		if (ListaConfiguracoesParsersGen != null)
		{
			ListaConfiguracoesParsersGen.trimToSize();
		}

		return ListaConfiguracoesParsersGen;
	}
	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de objetos ServidorProCfg (ordenado pelo nome do servidor).
	 * @roseuid 3C486FB600EE
	 */
	public Vector getListaServProcessos()
	{
		ServidorProcCfgDef Servidor = null;
		iServProc ServidorServ = null;
		iListaServProcs ListaServidoresServ = null;
		Vector ListaServidores = null;

		ListaServidoresServ = m_iUtil.fnGetListaServProcs();
		ServidorServ = ListaServidoresServ.fnGetInicio();

		while (ServidorServ != null)
		{
			Servidor = new ServidorProcCfgDef(ServidorServ.fnGetNomeProc(),
											  ServidorServ.fnGetHost());

			if (ListaServidores == null)
			{
				ListaServidores = new Vector();
				ListaServidores.addElement(Servidor);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ServidorProcCfgDef ServidorAux = null;

				for (short i = 0; i < ListaServidores.size(); i++)
				{
					ServidorAux = (ServidorProcCfgDef) ListaServidores.elementAt(i);

					if (Servidor.getNome().compareTo(ServidorAux.getNome()) < 0)
					{
						ListaServidores.add(i, Servidor);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaServidores.addElement(Servidor);
				}
			}

			ServidorServ = ListaServidoresServ.fnGetProx2(ServidorServ);
		}

		return ListaServidores;
	}

	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3C486FC2038A
	 */
	public Vector getListaConversores()
	{
		ConversorCfgDef Conversor = null;
		iConversor ConversorServ = null;
		iListaConversores ListaConversoresServ = null;
		Vector ListaConversores = null;

		ListaConversoresServ = m_iUtil.fnGetListaConversores();
		ConversorServ = ListaConversoresServ.fnGetInicio();

		while (ConversorServ != null)
		{
			Conversor = new ConversorCfgDef(ConversorServ.fnGetNomeConversor(),
											ConversorServ.fnGetNomeTecnologia());

			if (ListaConversores == null)
			{
				ListaConversores = new Vector();
				ListaConversores.addElement(Conversor);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConversorCfgDef ConversorAux = null;

				for (short i = 0; i < ListaConversores.size(); i++)
				{
					ConversorAux = (ConversorCfgDef) ListaConversores.elementAt(i);

					if (Conversor.getNome().compareTo(ConversorAux.getNome()) < 0)
					{
						ListaConversores.add(i, Conversor);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaConversores.addElement(Conversor);
				}
			}

			ConversorServ = ListaConversoresServ.fnGetProx2(ConversorServ);
		}

		return ListaConversores;
	}
	
	public Vector getListaReprocessadores()
	{ 
		//TODO Trocar interfaces para as de Reprocessadores
		ReprocessadorCfgDef Reprocessador = null;
		iReprocessador ReprocessadorServ = null;
		iListaReprocessadores ListaReprocessadoresServ = null;
		Vector ListaReprocessadores = null;

		ListaReprocessadoresServ = m_iUtil.fnGetListaReprocessadores();
		ReprocessadorServ = ListaReprocessadoresServ.fnGetInicio();

		while (ReprocessadorServ != null)
		{
			Reprocessador = new ReprocessadorCfgDef(ReprocessadorServ.fnGetNome(),
												ReprocessadorServ.fnGetNomeTecnologia());

			if (ListaReprocessadores == null)
			{
				ListaReprocessadores = new Vector();
				ListaReprocessadores.addElement(Reprocessador);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ReprocessadorCfgDef ReprocessadorAux = null;

				for (short i = 0; i < ListaReprocessadores.size(); i++)
				{
					ReprocessadorAux = (ReprocessadorCfgDef) ListaReprocessadores.elementAt(i);

					if (Reprocessador.getNome().compareTo(ReprocessadorAux.getNome()) < 0)
					{
						ListaReprocessadores.add(i, Reprocessador);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaReprocessadores.addElement(Reprocessador);
				}
			}

			ReprocessadorServ = ListaReprocessadoresServ.fnGetProx2(ReprocessadorServ);
		}

		return ListaReprocessadores;
	}

	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3C8E6CDD0106
	 */
	public Vector getListaParsers()
	{
		ConversorCfgDef Parser = null;
		iParser ParserServ = null;
		iListaParsers ListaParsersServ = null;
		Vector ListaParsers = null;

		ListaParsersServ = m_iUtil.fnGetListaParsers();
		ParserServ = ListaParsersServ.fnGetInicio();

		while (ParserServ != null)
		{
			Parser = new ConversorCfgDef(ParserServ.fnGetNomeParser(),
										 ParserServ.fnGetNomeTecnologia());

			if (ListaParsers == null)
			{
				ListaParsers = new Vector();
				ListaParsers.addElement(Parser);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConversorCfgDef ParserAux = null;

				for (short i = 0; i < ListaParsers.size(); i++)
				{
					ParserAux = (ConversorCfgDef) ListaParsers.elementAt(i);

					if (Parser.getNome().compareTo(ParserAux.getNome()) < 0)
					{
						ListaParsers.add(i, Parser);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaParsers.addElement(Parser);
				}
			}

			ParserServ = ListaParsersServ.fnGetProx2(ParserServ);
		}

		return ListaParsers;
	}
	
	/**
	 * Retorna lista de parsersGen
	 * 19/04/2006
	 * */
	public Vector getListaParsersGen()
	{
		ConversorCfgDef ParserGen = null;
		iParserGen ParserGenServ = null;
		iListaParserGen ListaParsersGenServ = null;
		Vector ListaParsersGen = null;

		ListaParsersGenServ = m_iUtil.fnGetListaParserGen();
		ParserGenServ = ListaParsersGenServ.fnGetInicio();

		while (ParserGenServ != null)
		{
			ParserGen = new ConversorCfgDef(ParserGenServ.fnGetNome(),
										 ParserGenServ.fnGetNomeTecnologia());

			if (ListaParsersGen == null)
			{
				ListaParsersGen = new Vector();
				ListaParsersGen.addElement(ParserGen);
			}
			else
			{
				// Insere os elementos ordenadamente
				boolean Inseriu = false;
				ConversorCfgDef ParserAux = null;

				for (short i = 0; i < ListaParsersGen.size(); i++)
				{
					ParserAux = (ConversorCfgDef) ListaParsersGen.elementAt(i);

					if (ParserGen.getNome().compareTo(ParserAux.getNome()) < 0)
					{
						ListaParsersGen.add(i, ParserGen);
						Inseriu = true;

						break;
					}
				}

				if (Inseriu == false)
				{
					ListaParsersGen.addElement(ParserGen);
				}
			}

			ParserGenServ = ListaParsersGenServ.fnGetProx2(ParserGenServ);
		}

		return ListaParsersGen;
	}

	/**
	 * @param p_Bilhetador
	 * @param p_Tecnologia
	 * @param p_Operadora
	 * @param p_Apelido
	 * @param p_Fase
	 * @return boolean
	 * @exception
	 * Inclui um bilhetador na configuração do sistema.
	 * @roseuid 3C43A3920345
	 */
	public boolean incluiBilhetador(String p_Bilhetador,
									String p_Tecnologia,
									String p_Operadora,
									String p_Apelido,
									String p_Fase)
	{
		iBilCfg BilhetadorServ = null;
		BilhetadorServ = m_iListaBilhetadores.fnAdicionaBilhetador(p_Bilhetador,
																  p_Tecnologia,
																  p_Operadora,
																  p_Apelido,
																  Short.parseShort(p_Fase));

		if (BilhetadorServ != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param p_Bilhetador
	 * @return boolean
	 * @exception
	 * Exclui um bilhetador da configuração do sistema.
	 * @roseuid 3C43A39E01E3
	 */
	public boolean excluiBilhetador(String p_Bilhetador)
	{
		if (m_iListaBilhetadores.fnRemoveBilhetador(p_Bilhetador) == true)
		{
			iListaParserCfgs p_ListaParserCfgs = null;
			iListaReprocCfgs p_ListaReprocCfgs = null;
			iListaConversorCfgs p_ListaConversorCfgs = null;
			iListaParserGenCfg p_ListaParserGenCfgs = null;
			
			iParserCfg p_ParserCfg = null;
			iReprocessadorCfg p_ReprocCfg = null;
			iConversorCfg p_ConversorCfg = null;
			iParserGenCfg p_ParserGenCfg = null;

			p_ListaParserCfgs = m_iUtil.fnGetListaParserCfgs();
			p_ParserCfg = p_ListaParserCfgs.fnGetInicio();

			while (p_ParserCfg != null)
			{
				p_ParserCfg.fnRemoveBilhetador(p_Bilhetador);
				p_ParserCfg = p_ListaParserCfgs.fnGetProx2(p_ParserCfg);
			}

			p_ListaParserCfgs.fnGrava();

			p_ListaConversorCfgs = m_iUtil.fnGetListaConfiguracao();
			p_ConversorCfg = p_ListaConversorCfgs.fnGetInicio();

			while (p_ConversorCfg != null)
			{
				p_ConversorCfg.fnRemoveBilhetador(p_Bilhetador);
				p_ConversorCfg = p_ListaConversorCfgs.fnGetProx2(p_ConversorCfg);
			}

			p_ListaConversorCfgs.fnGrava();
			
			p_ListaReprocCfgs = m_iUtil.fnGetListaReprocCfgs();
			p_ReprocCfg = p_ListaReprocCfgs.fnGetInicio();

			while (p_ReprocCfg != null)
			{
				p_ReprocCfg.fnRemoveBilhetador(p_Bilhetador);
				p_ReprocCfg = p_ListaReprocCfgs.fnGetProx2(p_ReprocCfg);
			}

			p_ListaReprocCfgs.fnGrava();
			
			p_ListaParserGenCfgs = m_iUtil.fnGetListaParserGenCfg();
			p_ParserGenCfg = p_ListaParserGenCfgs.fnGetInicio();

			while (p_ParserGenCfg != null)
			{
				p_ParserGenCfg.fnRemoveBilhetador(p_Bilhetador);
				p_ParserGenCfg = p_ListaParserGenCfgs.fnGetProx2(p_ParserGenCfg);
			}

			p_ListaParserGenCfgs.fnGrava();

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param p_CfgConversor
	 * @param p_Servidor
	 * @param p_Conversor
	 * @param p_Bilhetadores
	 * @return short
	 * @exception
	 * @roseuid 3C4AE8F000E7
	 */
	public short incluiCfgConversor(String p_CfgConversor,
									String p_Servidor,
									String p_Conversor,
									String p_Bilhetadores)
	{
		iConversorCfg ConfiguracaoServ = null;
		iListaConversorCfgs ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaConfiguracao();
		ConfiguracaoServ = ListaConfiguracoesServ.fnAdicionaConversorCfg(p_CfgConversor,
																		 p_Servidor,
																		 p_Conversor,
																		 p_Bilhetadores);

		if (ConfiguracaoServ != null)
		{
			return ConfiguracaoServ.fnGetErro();
		}
		else
		{
			return (short) 4;
		}
	}
	
	/**
	 * @param p_CfgConversor
	 * @param p_Servidor
	 * @param p_Conversor
	 * @param p_Bilhetadores
	 * @return short
	 * @exception
	 * @roseuid 3C4AE8F000E7
	 */
	public short incluiCfgReprocessador(String p_CfgReprocessador,
										String p_Servidor,
										String p_Reprocessador,
										String p_Bilhetadores)
	{
		//TODO Trocar interfaces para as de Reprocessadores
		iReprocessadorCfg ConfiguracaoServ = null;
		iListaReprocCfgs ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaReprocCfgs();
		ConfiguracaoServ = ListaConfiguracoesServ.fnAdicionaReprocCfg(p_CfgReprocessador,
																		 p_Servidor,
																		 p_Reprocessador,
																		 p_Bilhetadores);

		if (ConfiguracaoServ != null)
		{
			return ConfiguracaoServ.fnGetErro();
		}
		else
		{
			return (short) 4;
		}
	}

	/**
	 * @param p_CfgConversor
	 * @return boolean
	 * @exception
	 * @roseuid 3C4AE8FB025F
	 */
	public boolean excluiCfgConversor(String p_CfgConversor)
	{
		iListaConversorCfgs ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaConfiguracao();

		if (ListaConfiguracoesServ.fnRemoveConversorCfg(p_CfgConversor) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @param p_CfgConversor
	 * @return boolean
	 * @exception
	 * @roseuid 3C4AE8FB025F
	 */
	public boolean excluiCfgReprocessador(String p_CfgReprocessador)
	{
		//TODO Trocar interfaces para as de Reprocessadores
		iListaReprocCfgs ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaReprocCfgs();

		if (ListaConfiguracoesServ.fnRemoveReprocCfg(p_CfgReprocessador) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param p_CfgParser
	 * @param p_Servidor
	 * @param p_Conversor
	 * @param p_Bilhetadores
	 * @return short
	 * @exception
	 * @roseuid 3C8E52EA0258
	 */
	public short incluiCfgParser(String p_CfgParser,
								 String p_Servidor,
								 String p_Conversor,
								 String p_Bilhetadores)
	{
		iParserCfg ConfiguracaoServ = null;
		iListaParserCfgs ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaParserCfgs();
		ConfiguracaoServ = ListaConfiguracoesServ.fnAdicionaParserCfg(p_CfgParser,
																	  p_Servidor,
																	  p_Conversor,
																	  p_Bilhetadores);

		if (ConfiguracaoServ != null)
		{
			return ConfiguracaoServ.fnGetErro();
		}
		else
		{
			return (short) 4;
		}
	}
	
	/**
	 * método que insere novo parsergen
	 * 19/04/2006
	 * */
	
	public short incluiCfgParserGen(String p_CfgParserGen,
			 String p_Servidor,
			 String p_Conversor,
			 String p_Bilhetadores)
	{
		iParserGenCfg ConfiguracaoServ = null;
		iListaParserGenCfg ListaConfiguracoesServ = null;
		
		ListaConfiguracoesServ = m_iUtil.fnGetListaParserGenCfg();
		ConfiguracaoServ = ListaConfiguracoesServ.fnAdicionaParserGenCfg(p_CfgParserGen,
														  p_Servidor,
														  p_Conversor,
														  p_Bilhetadores);
		
		if (ConfiguracaoServ != null)
		{
			return ConfiguracaoServ.fnGetErro();
		}
		else
		{
			return (short) 4;
		}
	}

	/**
	 * @param p_CfgParser
	 * @return boolean
	 * @exception
	 * @roseuid 3C8E52EA02E5
	 */
	public boolean excluiCfgParser(String p_CfgParser)
	{
		iListaParserCfgs ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaParserCfgs();

		if (ListaConfiguracoesServ.fnRemoveParserCfg(p_CfgParser) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * método que remove o tipo parserGen
	 * 19/04/2006
	 * */
	
	public boolean excluiCfgParserGen(String p_CfgParserGen)
	{
		iListaParserGenCfg ListaConfiguracoesServ = null;

		ListaConfiguracoesServ = m_iUtil.fnGetListaParserGenCfg();

		if (ListaConfiguracoesServ.fnRemoveParserGenCfg(p_CfgParserGen) == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param p_Id
	 * @param p_Nome
	 * @param p_IPRemoto
	 * @param p_HostRemoto
	 * @return String
	 * @exception
	 * Finaliza um processo pelo seu Id.
	 * @roseuid 3C45BD5500F8
	 */
	public String finalizaProcesso(int p_Id,
								   String p_Nome,
								   String p_IPRemoto,
								   String p_HostRemoto)
	{
		String Retorno = "";

		Retorno = m_iUtil.fnTermina(p_Id);

		return Retorno;
	}

	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3C45CF2E033D
	 */
	public Vector getListaProcessos()
	{
		ProcessoDef Processo = null;
		iProcControlado ProcessoServ = null;
		iListaProcControlados ListaProcessosServ = null;
		String Parametros = "";
		Vector ListaProcessos = null;

		try
		{
			ListaProcessosServ = m_iUtil.fnGetListaProcessos();
			ProcessoServ = ListaProcessosServ.fnGetInicio((short) 0);

			while (ProcessoServ != null)
			{
				Processo = new ProcessoDef(ProcessoServ.fnGetPID(),
										   ProcessoServ.fnGetNomeExec(),
										   //ProcessoServ.fnGetNomeProc(),
										   ProcessoServ.fnGetUsuario(),
										   ProcessoServ.fnGetPerfil(),
										   ProcessoServ.fnGetTipoCli(),
										   ProcessoServ.fnGetTipoProc(),
										   ProcessoServ.fnGetDataInicio(),
										   ProcessoServ.fnGetDataAcesso());

				Processo.setHost(ProcessoServ.fnGetHost());
				Processo.setDataInicioStr(ProcessoServ.fnGetDataInicioStr());
				Processo.setUltimoAcessoStr(ProcessoServ.fnGetUltimoAcessoStr());

				if ((Processo.getNome().toLowerCase().startsWith("conv") == true) ||
					(Processo.getNome().toLowerCase().startsWith("parser") == true) ||
					(Processo.getNome().toLowerCase().startsWith("reproc") == true))
				{
					Parametros = ProcessoServ.fnGetParametros();

					if (Parametros.indexOf(" ") != -1)
					{
						Parametros = Parametros.substring(0, Parametros.indexOf(" "));
					}

					Processo.setNome(Processo.getNome() + " (" + Parametros + ")");
				}

				if (ListaProcessos == null)
				{
					ListaProcessos = new Vector();
					ListaProcessos.addElement(Processo);
				}
				else
				{
					// Insere os elementos ordenadamente
					boolean Inseriu = false;
					ProcessoDef ProcessoAux = null;

					for (short i = 0; i < ListaProcessos.size(); i++)
					{
						ProcessoAux = (ProcessoDef) ListaProcessos.elementAt(i);

						if (Processo.getNome().compareTo(ProcessoAux.getNome()) < 0)
						{
							ListaProcessos.add(i, Processo);
							Inseriu = true;

							break;
						}
					}

					if (Inseriu == false)
					{
						ListaProcessos.addElement(Processo);
					}
				}

				ProcessoServ = ListaProcessosServ.fnGetProx2(ProcessoServ);
			}
			try{
			IManutencaoAgenda agenda = AgendaRMI.retornaManutencaoAgenda(NoUtil.getNo().getIp(), NoUtil.getNo().getPortaRMI(), NoUtil.getNo().getHostName());
			Processo = new ProcessoDef(agenda.getPID(),
					   "ServidorAgenda.exe",
					   //ProcessoServ.fnGetNomeProc(),
					   "",
					   "",
					   iTipoCli.from_int(iTipoCli._T_CLI_HISTORICO),//tipo do cliente
					   iTipoProc.from_int(iTipoProc._T_PROC_AGENDA),//tipo de processo
					   100000,//data inicio
					   0);//data do último acesso
			
			Processo.setHost("");
			Processo.setDataInicioStr(getDataHoraInicio(agenda.getHorarioDeInicio()));
			Processo.setUltimoAcessoStr("0");
			
			ListaProcessos.add(Processo);
			}catch (NullPointerException e) {
				System.out.println("Sem agenda...");
			}
		}
		catch (ConnectException e){
			System.out.println("ServAgenda fora do ar.");
		}
		catch (Exception Exc)
		{
			System.out.println("CnxServUtil - getListaProcessos(): " + Exc);

			return null;
		}

		return ListaProcessos;
	}

	/**
	 * @return Vector
	 * @exception
	 * Recupera as informações de utilização de disco pelo BD.
	 * @roseuid 3C47300D0383
	 */
	public Vector getBDInfo()
	{
		Vector Info = new Vector();
		Vector Celula = null;
		String[] Titulos =
						   {
							   "Total Disco:", "Total Utilizado:",
							   "Total Livre:", "Total Utilizado por CfgUsr:",
							   "Total Utilizado por BD:"
						   };
		String[] SubTitulos =
							  {
								  "Total Utilizado por BD/armazenados:",
								  "Total Utilizado por BD/cdr:",
								  "Total Utilizado por BD/dddy:",
								  "Total Utilizado por BD/estat:",
								  "Total Utilizado por BD/export:",
								  "Total Utilizado por BD/historico:",
								  "Total Utilizado por BD/rotas:",
								  "Total Utilizado por BD/tco:"
							  };
		String[] SubDiretorios =
								 {
									 "armazenados", "cdr", "dddy", "estat",
									 "export", "historico", "rotas", "tco"
								 };

		for (int i = 0; i < Titulos.length; i++)
		{
			Celula = new Vector();
			Celula.addElement(Titulos[i]);

			switch (i)
			{
				case 0:
					Celula.addElement(m_iUtil.fnGetTotalDisco());

					break;

				case 1:
					Celula.addElement(m_iUtil.fnGetTotalUsado());

					break;

				case 2:
					Celula.addElement(m_iUtil.fnGetTotalLivre());

					break;

				case 3:
					Celula.addElement(m_iUtil.fnGetTotalCfgusr());

					break;

				case 4:
					Celula.addElement(m_iUtil.fnGetTotalBD());

					break;
			}

			Celula.trimToSize();
			Info.addElement(Celula);
		}

		for (int i = 0; i < SubTitulos.length; i++)
		{
			Celula = new Vector();
			Celula.addElement(SubTitulos[i]);
			Celula.addElement(m_iUtil.fnGetBD2Nivel(SubDiretorios[i]));
			Celula.trimToSize();
			Info.addElement(Celula);
		}

		Info.trimToSize();

		return Info;
	}

	/**
	 * @return Vector
	 * @exception
	 * Recupera as informações de versão dos processos/bases de dados.
	 * @roseuid 3C515CC30268
	 */
	public Vector getSisInfo()
	{
		Vector Info = new Vector();
		Vector Celula = null;
		String[] Titulos =
						   {
							   "BD", "CfgUsr", "Parser", "Conversor", "ServAgn",
							   "ServCfg", "ServCli", "ServCtrl", "ServCons",
							   "ServHist", "ServUtil"
						   };

		for (int i = 0; i < Titulos.length; i++)
		{
			Celula = new Vector();
			Celula.addElement(Titulos[i]);
			Celula.addElement(m_iUtil.fnGetVersao(Titulos[i].toLowerCase()));
			Celula.trimToSize();
			Info.addElement(Celula);
		}

		Info.trimToSize();

		return Info;
	}

	/**
	 * @param p_Perfis
	 * @return boolean
	 * @exception
	 * @roseuid 3C4F0C0B011C
	 */
	public boolean verificaInconsistencia(String p_Perfis)
	{
		m_iUtil.fnVerificaCfg(p_Perfis);

		return true;
	}

	/**
	 * @param p_Perfis
	 * @return boolean
	 * @exception
	 * @roseuid 3C4F0C2B01B9
	 */
	public boolean corrigeInconsistencia(String p_Perfis)
	{
		m_iUtil.fnCorrigeCfg(p_Perfis);

		return true;
	}

	/**
	 * @param p_TipoAlr
	 * @return String[]
	 * @exception
	 * @roseuid 3C5ECAB400DB
	 */
	public String[] getCfgAlarme(short p_TipoAlr)
	{
		String[] Args = null;
		String Habilitado = "false";
		iAlarmeSisCfg AlarmeCfg = null;

		AlarmeCfg = m_iUtil.fnGetAlarmeCfg();

		if (AlarmeCfg != null)
		{
			switch (p_TipoAlr)
			{
				case 0:
					Args = new String[2];

					// Envia SMS
					Args[0] = Short.toString(AlarmeCfg.fnGetSMS((short) 0));

					// Telefones para SMS
					Args[1] = AlarmeCfg.fnGetTelefonesSMS((short) 0);

					break;

				case 1:

					if (AlarmeCfg.fnGetAlarme((short) 1) == true)
					{
						Habilitado = "true";
					}

					Args = new String[5];

					// Habilitação do alarme
					Args[0] = Habilitado;
					Args[1] = Short.toString(AlarmeCfg.fnGetQtsMinDisc());
					Args[2] = Integer.toString(AlarmeCfg.fnGetPeriocidade((short) 1));

					// Envia SMS
					Args[3] = Short.toString(AlarmeCfg.fnGetSMS((short) 1));

					// Telefones para SMS
					Args[4] = AlarmeCfg.fnGetTelefonesSMS((short) 1);

					break;

				case 2: // Alarme de espaço máximo nos diretórios

					String[] Diretorios = null;

					if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
					{
						Diretorios = new String[3];
						Diretorios[0] = "armazenados";
						Diretorios[1] = "export";
						Diretorios[2] = "historico";
					}
					else
					{
						Diretorios = new String[4];
						Diretorios[0] = "cdr";
						Diretorios[1] = "armazenados/agenda";
						Diretorios[2] = "armazenados/historico";
						Diretorios[3] = "export";
					}

					Args = new String[6];

					for (int i = 0; i < Args.length; i++)
						Args[i] = "";

					// Nomes dos diretórios
					for (int i = 0; i < Diretorios.length; i++)
					{
						// Nomes dos diretórios
						if (i == (Diretorios.length - 1))
						{
							Args[0] += ("bdados/" + Diretorios[i]);
						}
						else
						{
							Args[0] += ("bdados/" + Diretorios[i] + ";");
						}

						// Espaço máximo de cada diretório
						if (i == (Diretorios.length - 1))
						{
							Args[1] += Integer.toString(AlarmeCfg.fnGetEspacoDir("bdados/" +
																				 Diretorios[i]));
						}
						else
						{
							Args[1] += (Integer.toString(AlarmeCfg.fnGetEspacoDir("bdados/" +
																				  Diretorios[i])) +
							";");
						}

						// Periodicidade
						if (i == (Diretorios.length - 1))
						{
							Args[2] += Integer.toString(AlarmeCfg.fnGetPeriodicidadeDir("bdados/" +
																						Diretorios[i]));
						}
						else
						{
							Args[2] += (Integer.toString(AlarmeCfg.fnGetPeriodicidadeDir("bdados/" +
																						 Diretorios[i])) +
							";");
						}

						// Habilitação do alarme
						if (i == (Diretorios.length - 1))
						{
							if (AlarmeCfg.fnGetHabilitaDir("bdados/" +
														   Diretorios[i]) == true)
							{
								Args[3] += "1";
							}
							else
							{
								Args[3] += "0";
							}
						}
						else
						{
							if (AlarmeCfg.fnGetHabilitaDir("bdados/" +
														   Diretorios[i]) == true)
							{
								Args[3] += "1;";
							}
							else
							{
								Args[3] += "0;";
							}
						}

						// Envia SMS
						Args[4] = Short.toString(AlarmeCfg.fnGetSMS((short) 2));

						// Telefones para SMS
						Args[5] = AlarmeCfg.fnGetTelefonesSMS((short) 2);
					}

					break;

				case 3:
					Args = new String[6];

					for (int i = 0; i < Args.length; i++)
						Args[i] = "";

					// Monta lista de bilhetadores e ociosidades
					Vector Bilhetadores = getListaBilhetadoresCfg();
					BilhetadorCfgDef Bilhetador;

					for (int i = 0; i < Bilhetadores.size(); i++)
					{
						Bilhetador = (BilhetadorCfgDef) Bilhetadores.elementAt(i);

						if (i == (Bilhetadores.size() - 1))
						{
							Args[0] += Bilhetador.getBilhetador();
							Args[1] += new Integer(Bilhetador.getOciosidade()).toString();
							Args[2] += new Short(Bilhetador.getHabilita()).toString();
						}
						else
						{
							Args[0] += (Bilhetador.getBilhetador() + ";");
							Args[1] += (new Integer(Bilhetador.getOciosidade()).toString() +
							";");
							Args[2] += (new Short(Bilhetador.getHabilita()).toString() +
							";");
						}
					}

					// Periodicidade
					Args[3] = Integer.toString(AlarmeCfg.fnGetPeriocidade((short) 2));

					// Envia SMS
					Args[4] = Short.toString(AlarmeCfg.fnGetSMS((short) 3));

					// Telefones para SMS
					Args[5] = AlarmeCfg.fnGetTelefonesSMS((short) 3);

					break;

				case 4:

					if (AlarmeCfg.fnGetAlarme((short) 3) == true)
					{
						Habilitado = "true";
					}

					Args = new String[5];

					for (int i = 0; i < Args.length; i++)
						Args[i] = "";

					// Habilitação do alarme
					Args[0] = Habilitado;

					// Recupera quantidade máxima de arquivos no Dir In antes de alarmar
					Args[1] = Integer.toString(AlarmeCfg.fnGetQtdMaxArqIn());

					// Periodicidade
					Args[2] = Integer.toString(AlarmeCfg.fnGetPeriocidade((short) 3));

					// Envia SMS
					Args[3] = Short.toString(AlarmeCfg.fnGetSMS((short) 4));

					// Telefones para SMS
					Args[4] = AlarmeCfg.fnGetTelefonesSMS((short) 4);

					break;
			}
		}

		return Args;
	}

	/**
	 * @param p_TipoAlr
	 * @param p_Args
	 * @return void
	 * @exception
	 * @roseuid 3C6015CA0045
	 */
	public void setCfgAlarme(short p_TipoAlr,
							 String[] p_Args)
	{
		iAlarmeSisCfg AlarmeCfg = null;
		short TemSMS = 0;
		String strHabilita;
		Vector vHabilitacao;

		AlarmeCfg = m_iUtil.fnGetAlarmeCfg();

		if (AlarmeCfg != null)
		{
			switch (p_TipoAlr)
			{
				case 0:
					AlarmeCfg.fnSetSMS((short) 0,
									   Short.parseShort(p_Args[0]));
					AlarmeCfg.fnSetTelefonesSMS((short) 0, p_Args[1]);

					break;

				case 1: // Alarme para espaço mínimo no disco
					AlarmeCfg.fnSetQtsMinDisc(Short.parseShort(p_Args[2]));
					AlarmeCfg.fnSetPeriocidade((short) 1,
											   Integer.parseInt(p_Args[1]));
					AlarmeCfg.fnSetAlarme((short) 1,
										  Short.parseShort(p_Args[0]));

					try
					{
						TemSMS = Short.parseShort(p_Args[3]);
					}
					catch (Exception Exc)
					{
						TemSMS = 0;
					}

					AlarmeCfg.fnSetSMS((short) 1, TemSMS);
					AlarmeCfg.fnSetTelefonesSMS((short) 1, p_Args[4]);

					break;

				case 2: // Alarme de espaço máximo para diretórios

					boolean Habilita = false;
					Vector vDiretorios;
					Vector vLimites;
					Vector vPeriodicidades;
					String Diretorio = null;
					int Limite;
					int Periodicidade;

					vDiretorios = VetorUtil.String2Vetor(p_Args[0], ';');
					vLimites = VetorUtil.String2Vetor(p_Args[1], ';');
					vPeriodicidades = VetorUtil.String2Vetor(p_Args[2], ';');
					vHabilitacao = VetorUtil.String2Vetor(p_Args[3], ';');

					for (int i = 0; i < vDiretorios.size(); i++)
					{
						Diretorio = (String) vDiretorios.elementAt(i);
						Limite = Integer.parseInt((String) vLimites.elementAt(i));
						Periodicidade = Integer.parseInt((String) vPeriodicidades.elementAt(i));
						strHabilita = (String) vHabilitacao.elementAt(i);

						if (strHabilita.equals("1"))
						{
							Habilita = true;
						}
						else
						{
							Habilita = false;
						}

						AlarmeCfg.fnSetEspacoDir(Diretorio, Limite,
												 Periodicidade, Habilita);
					}

					try
					{
						TemSMS = Short.parseShort(p_Args[4]);
					}
					catch (Exception Exc)
					{
						TemSMS = 0;
					}

					AlarmeCfg.fnSetSMS((short) 2, TemSMS);
					AlarmeCfg.fnSetTelefonesSMS((short) 2, p_Args[5]);

					break;

				case 3:

					// Seta os tempos associados aos bilhetadores
					AlarmeCfg.fnSetOciosidade(p_Args[0], p_Args[1], p_Args[3]);
					AlarmeCfg.fnSetPeriocidade((short) 2,
											   Integer.parseInt(p_Args[2]));

					try
					{
						TemSMS = Short.parseShort(p_Args[4]);
					}
					catch (Exception Exc)
					{
						TemSMS = 0;
					}

					AlarmeCfg.fnSetSMS((short) 3, TemSMS);
					AlarmeCfg.fnSetTelefonesSMS((short) 3, p_Args[5]);

					break;

				case 4:
					AlarmeCfg.fnSetQtdMaxArqIn(p_Args[2]);
					AlarmeCfg.fnSetPeriocidade((short) 3,
											   Integer.parseInt(p_Args[1]));
					AlarmeCfg.fnSetAlarme((short) 3,
										  Short.parseShort(p_Args[0]));

					try
					{
						TemSMS = Short.parseShort(p_Args[3]);
					}
					catch (Exception Exc)
					{
						TemSMS = 0;
					}

					AlarmeCfg.fnSetSMS((short) 4, TemSMS);
					AlarmeCfg.fnSetTelefonesSMS((short) 4, p_Args[4]);

					break;
			}

			AlarmeCfg.fnGrava();
		}
	}

	/**
	 * @param p_TipoAlr
	 * @param p_Args
	 * @return void
	 * @exception
	 * @roseuid 3C6015CA0045
	 */
	public void setExportaDrill(String bilhetadores,
							 String ociosidade,String habilitacao)
	{
			String arrayBilhetadores[] = bilhetadores.split(";");
			String arrayOciosidade[] = ociosidade.split(";");
			String arrayHabilitacao[] = habilitacao.split(";");
			StringBuffer dados = new StringBuffer();
		try{
			for (int i = 0; i < arrayBilhetadores.length; i++) {
				dados.append(arrayBilhetadores[i]);
				dados.append(";");
				dados.append(MultiplicaOciosidade(arrayOciosidade[i]));
				dados.append(";");
				dados.append(arrayHabilitacao[i]);
				dados.append("\n");
				
				}
			m_iUtil.fnSalvaListaBilhetadorOciosidade(dados.toString());

		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Quantidade de Bilhetadores inconsistentes com quantidade de ociosidade e habilitacao.");
			e.printStackTrace();
		}
			//AlarmeCfg.fn;
		
	}
	
	public String MultiplicaOciosidade(String Ociosidade){	
			return Integer.toString(Integer.parseInt(Ociosidade)*60);	
		
	}

	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3C6AABCB0352
	 */
	public Vector getListaMensagensAlarmes()
	{
		iAlarmeView Alarme;
		String Aux = "";
		String StrAlarme;
		short QtdAlarmes = 0;
		Vector ListaAlarmes = new Vector();

		Alarme = m_iUtil.fnCriaViewAlr();

		if (Alarme != null)
		{
			for (short i = 0; i < 8; i++)
			{
				QtdAlarmes += Alarme.fnVerifica((short) (i), "admin");
				Aux = Alarme.fnGetMsg();

				if (Aux.length() != 0)
				{
					while (Aux != null)
					{
						StrAlarme = Aux.substring(0,
												  Aux.indexOf(";"));

						if ((Aux.indexOf(";") != -1) &&
							(Aux.indexOf(";") != (Aux.length() - 1)))
						{
							Aux = Aux.substring(Aux.indexOf(";") + 1,
												Aux.length());
						}
						else
						{
							Aux = null;
						}

						ListaAlarmes.add(StrAlarme);
					}
				}
			}

			ListaAlarmes.trimToSize();

			//         Alarme.fnDestroy();
		}

		if (QtdAlarmes == 0)
		{
			return null;
		}
		else
		{
			return ListaAlarmes;
		}
	}

	/**
	 * @param p_Usuario
	 * @return Vector
	 * @exception
	 * @roseuid 3C6AAC3201C9
	 */
	public Vector getListaMensagensOutras(String p_Usuario)
	{
		iMensagem Mensagem;
		String MensagensAux = "";
		String StrMensagem;
		short QtdMensagens = 0;
		Vector ListaMensagens = new Vector();

		Mensagem = m_iUtil.fnCriaMsg();
		QtdMensagens = Mensagem.fnVerifica(p_Usuario, (short) 1);

		if (QtdMensagens != 0)
		{
			MensagensAux = Mensagem.fnGetHeader();

			if (MensagensAux.length() != 0)
			{
				while (MensagensAux != null)
				{
					StrMensagem = MensagensAux.substring(0,
														 MensagensAux.indexOf(";"));

					if ((MensagensAux.indexOf(";") != -1) &&
						(MensagensAux.indexOf(";") != (MensagensAux.length() -
						1)))
					{
						MensagensAux = MensagensAux.substring(MensagensAux.indexOf(";") +
															  1,
															  MensagensAux.length());
					}
					else
					{
						MensagensAux = null;
					}

					ListaMensagens.add(StrMensagem);
				}
			}
		}

		Mensagem.fnDestroy();

		if (QtdMensagens > 0)
		{
			return ListaMensagens;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param p_Arquivo
	 * @return Vector
	 * @exception
	 * @roseuid 3C6BAD590268
	 */
	public Vector getMensagemAlarme(String p_Arquivo)
	{
		iArqView Arquivo;
		iAlarmeView Alarme;
		String Linha = "";
		Vector Coluna;
		Vector Linhas;
		String Aux = "";
		String StrAlarme;

		Alarme = m_iUtil.fnCriaViewAlr();

		if (Alarme != null)
		{
			Arquivo = Alarme.fnGetArq(p_Arquivo);

			if (Arquivo != null)
			{
				Linhas = new Vector();

				do
				{
					Linha = Arquivo.fnGetLinha();

					if (Linha.length() != 0)
					{
						Coluna = new Vector();
						Coluna.add(Linha);
						Coluna.trimToSize();
						Linhas.add(Coluna);
					}
				}
				while (Linha.length() != 0);

				Linhas.trimToSize();

				Arquivo.fnDestroy();
				Alarme.fnDestroy();

				return Linhas;
			}
			else
			{
				Alarme.fnDestroy();

				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param p_Arquivo
	 * @return Vector
	 * @exception
	 * @roseuid 3C6D724C0231
	 */
	public Vector getMensagemOutras(String p_Arquivo)
	{
		iArqView Arquivo;
		iMensagem Mensagem;
		String Linha = "";
		Vector Coluna;
		Vector Linhas;
		String Aux = "";
		String StrMensagem;

		Mensagem = m_iUtil.fnCriaMsg();

		if (Mensagem != null)
		{
			Arquivo = Mensagem.fnGetArq(p_Arquivo);

			if (Arquivo != null)
			{
				Linhas = new Vector();

				do
				{
					Linha = Arquivo.fnGetLinha();

					if (Linha.length() != 0)
					{
						Coluna = new Vector();
						Coluna.add(Linha);
						Coluna.trimToSize();
						Linhas.add(Coluna);
					}
				}
				while (Linha.length() != 0);

				Linhas.trimToSize();

				Arquivo.fnDestroy();
				Mensagem.fnDestroy();

				return Linhas;
			}
			else
			{
				Mensagem.fnDestroy();

				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param p_Perfil
	 * @param p_Arquivos
	 * @return void
	 * @exception
	 * @roseuid 3D2AF69D02F3
	 */
	public void apagaRelExportados(int p_Perfil,
								   String p_Arquivos)
	{
		m_iUtil.fnDelBaseExportada(p_Perfil, p_Arquivos);
	}

	/**
	 * @param p_Perfil
	 * @param p_Arquivos
	 * @return void
	 * @exception
	 * @roseuid 3D2AF69D02F3
	 */
	public void apagaRelExportadosDrill(int p_Perfil,
								   String p_Arquivos)
	{
		m_iUtil.fnDelBaseExportadaDrill(p_Perfil, p_Arquivos);
	}

	/**
	 * @param p_ListaArquivos
	 * @return boolean
	 * @exception
	 * @roseuid 3C6BC873017E
	 */
	public boolean apagaMensagemAlarme(String p_ListaArquivos)
	{
		iAlarmeView Alarme = null;
		String Arquivo;

		Alarme = m_iUtil.fnCriaViewAlr();

		if (Alarme == null)
		{
			return false;
		}

		p_ListaArquivos = p_ListaArquivos + ";";

		while (p_ListaArquivos != null)
		{
			Arquivo = p_ListaArquivos.substring(0,
												p_ListaArquivos.indexOf(";"));
			Alarme.Apaga(Arquivo);

			if (p_ListaArquivos.indexOf(";") != (p_ListaArquivos.length() - 1))
			{
				p_ListaArquivos = p_ListaArquivos.substring(p_ListaArquivos.indexOf(";") +
															1,
															p_ListaArquivos.length());
			}
			else
			{
				p_ListaArquivos = null;
			}
		}

		Alarme.fnDestroy();

		return true;
	}

	/**
	 * @param p_ListaArquivos
	 * @return boolean
	 * @exception
	 * @roseuid 3C6D72B30120
	 */
	public boolean apagaMensagemOutras(String p_ListaArquivos)
	{
		iMensagem Mensagem = null;
		String Arquivo;

		Mensagem = m_iUtil.fnCriaMsg();

		if (Mensagem == null)
		{
			return false;
		}

		p_ListaArquivos = p_ListaArquivos + ";";

		while (p_ListaArquivos != null)
		{
			Arquivo = p_ListaArquivos.substring(0,
												p_ListaArquivos.indexOf(";"));
			Mensagem.Apaga(Arquivo);

			if (p_ListaArquivos.indexOf(";") != (p_ListaArquivos.length() - 1))
			{
				p_ListaArquivos = p_ListaArquivos.substring(p_ListaArquivos.indexOf(";") +
															1,
															p_ListaArquivos.length());
			}
			else
			{
				p_ListaArquivos = null;
			}
		}

		Mensagem.fnDestroy();

		return true;
	}

	/**
	 * @param p_Usuario
	 * @return short
	 * @exception
	 * @roseuid 3C90990D0110
	 */
	public short getQtdMensagens(String p_Usuario)
	{
		iMensagem Mensagem;
		short QtdMensagens = 0;

		Mensagem = m_iUtil.fnCriaMsg();

		try
		{
			QtdMensagens = Mensagem.fnVerifica(p_Usuario, (short) 1);
			Mensagem.fnDestroy();

			return QtdMensagens;
		}
		catch (Exception Exc)
		{
			System.out.println("CnxServUtil - getQtdMensagens()");
			System.out.println("CnxServUtil - getQtdMensagens() " + Exc);

			return (short) 0;
		}
	}

	/**
	 * @return short
	 * @exception
	 * @roseuid 3C90991B028D
	 */
	public short getQtdAlarmes()
	{
		iAlarmeView Alarme = null;
		short QtdAlarmes = 0;

		Alarme = m_iUtil.fnCriaViewAlr();

		try
		{
			if (Alarme != null)
			{
				for (short i = 0; i < 8; i++)

					//QtdAlarmes += Alarme.fnVerifica((short)(i+1), "admin");
					QtdAlarmes += Alarme.fnVerifica((short) (i), "admin");

				Alarme.fnDestroy();
			}

			return QtdAlarmes;
		}
		catch (Exception Exc)
		{
			System.out.println("CnxServUtil - getQtdAlarmes()");
			System.out.println("CnxServUtil - getQtdAlarmes() " + Exc);

			return (short) 0;
		}
	}

	/**
	 * @param p_Usuario
	 * @return String
	 * @exception
	 * @roseuid 3C909926008A
	 */
	public String getMensagensAdmin(String p_Usuario)
	{
		return "";
	}

	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3C6BF76401D8
	 */
	public Vector getUltimaColeta()
	{
 		String UltColeta = null;
		String ListaBilhetadores = "";
		BilhetadorCfgDef Bilhetador;
		Vector Bilhetadores = null;

		Bilhetadores = getListaBilhetadoresCfg();

		if ((Bilhetadores == null) || (Bilhetadores.size() == 0))
		{
			return null;
		}
		
		for (int i = 0; i < Bilhetadores.size(); i++)
		{
			Bilhetador = (BilhetadorCfgDef) Bilhetadores.elementAt(i);

			if (i == (Bilhetadores.size() - 1))
			{
				ListaBilhetadores += Bilhetador.getBilhetador();
			}
			else
			{
				ListaBilhetadores += (Bilhetador.getBilhetador() + ";");
			}
		}

		UltColeta = m_iUtil.fnGetUltColeta(ListaBilhetadores,
										   (short) Bilhetadores.size());

		if (UltColeta != null && !UltColeta.equals(""))
		{
			String Bil = null;
			String DataArq = null;
			String DataCdr = null;
			String QtdCdrs = null;
			Vector Linhas;
			Vector Coluna;

			Linhas = new Vector();

			while (UltColeta != null)
			{
				Coluna = new Vector();

				// Recupera Bilhetador e últimas coletas associadas
				Bil = UltColeta.substring(0,
										  UltColeta.indexOf(";"));
				QtdCdrs = Bil.substring(Bil.lastIndexOf(",") + 1,
										Bil.length());
				Bil = Bil.substring(0,
									Bil.lastIndexOf(","));
				DataCdr = Bil.substring(Bil.indexOf(",") + 1,
										Bil.lastIndexOf(","));
				DataArq = Bil.substring(Bil.lastIndexOf(",") + 1,
										Bil.length());
				Bil = Bil.substring(0,
									Bil.indexOf(","));

				if ((DataArq.equals("-") == true) &&
					(QtdCdrs.equals("0") == true))
				{
					QtdCdrs = "-";
				}

				Coluna.add(Bil);
				Coluna.add(DataArq);
				Coluna.add(DataCdr);
				Coluna.add(QtdCdrs);
				Coluna.trimToSize();
				Linhas.add(Coluna);

				if (UltColeta.indexOf(";") != (UltColeta.length() - 1))
				{
					UltColeta = UltColeta.substring(UltColeta.indexOf(";") + 1,
													UltColeta.length());
				}
				else
				{
					UltColeta = null;
				}
			}

			Linhas.trimToSize();

			return Linhas;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param p_Data
	 * @return Vector
	 * @exception
	 * @roseuid 3C6BF76F0165
	 */
	public Vector getResumoDiario(String p_Data)
	{
		String ResumoDiario = null;
		String ListaBilhetadores = "";
		BilhetadorCfgDef Bilhetador;
		Vector Bilhetadores = null;

		Bilhetadores = getListaBilhetadoresCfg();

		for (int i = 0; i < Bilhetadores.size(); i++)
		{
			Bilhetador = (BilhetadorCfgDef) Bilhetadores.elementAt(i);

			if (i == (Bilhetadores.size() - 1))
			{
				ListaBilhetadores += Bilhetador.getBilhetador();
			}
			else
			{
				ListaBilhetadores += (Bilhetador.getBilhetador() + ";");
			}
		}

		if ((Bilhetadores == null) || (Bilhetadores.size() == 0))
		{
			return null;
		}

		ResumoDiario = m_iUtil.fnGetResDiario(ListaBilhetadores, p_Data,
											  (short) Bilhetadores.size());
		
		if (ResumoDiario != null && !ResumoDiario.equals(""))
		{
			int Total = 0;
			int[] TotalParcial;
			String Aux = null;
			String Bil = null;
			String DataRes = null;
			String QtdCdrs = null;
			String QtdCdrsHora = null;
			String Tabela = "";
			String Linha = "";
			Vector Linhas;
			Vector Coluna;
			short i;

			TotalParcial = new int[24];

			for (i = 0; i < 24; i++)
				TotalParcial[i] = 0;

			Linhas = new Vector();

			// Salva data do resumo
			DataRes = ResumoDiario.substring(0,ResumoDiario.indexOf(";"));
			
			Coluna = new Vector();
			Coluna.add(DataRes);
			Coluna.trimToSize();
			Linhas.add(Coluna);

			while (ResumoDiario != null)
			{
				Coluna = new Vector();

				// Recupera Bilhetador e informações associadas
				Aux = ResumoDiario.substring(0,
											 ResumoDiario.indexOf(";"));
				Aux += ",";

				Bil = Aux.substring(0,
									Aux.indexOf(","));
				Aux = Aux.substring(Aux.indexOf(",") + 1,
									Aux.length());

				QtdCdrs = Aux.substring(0,
										Aux.indexOf(","));
				Aux = Aux.substring(Aux.indexOf(",") + 1,
									Aux.length());

				if (QtdCdrs.equals("0") == true)
				{
					QtdCdrs = "-";
				}
				else
				{
					Total += Integer.parseInt(QtdCdrs);
				}

				Coluna.add(Bil);
				Coluna.add(QtdCdrs);
				String dat[] = p_Data.split("/");
				String dt = dat[2];
				dt += dat[1];
				dt += dat[0];
				short valor = m_iUtil.fnGetQtdArquivos15min(Bil,dt);
				Coluna.add(""+valor);
				
				Tabela = "<table border=\"0\" width=\"100%\">\n";
				Tabela += "<tr>\n";

				Linha = "";
				i = 0;

				short Max = 8;

				for (; i < Max; i++)
				{
					QtdCdrsHora = Aux.substring(0,
												Aux.indexOf(","));

					if (QtdCdrs.equals("-") == true)
					{
						QtdCdrsHora = "-";
					}
					else
					{
						TotalParcial[i] += Integer.parseInt(QtdCdrsHora);
					}

					if (i != (Max - 8))
					{
						Linha += ("<td align=\"center\">" + QtdCdrsHora +
						"</td>");
					}
					else
					{
						//Linha += "<tr bgcolor=\"#000099\">\n";
						Linha += "<tr>\n";

						for (short j = i; j < Max; j++)

							//Linha += "<td align=\"center\"><font color=\"#FFFFFF\"><b>"+(j+1)+"</b></font></td>";
							Linha += ("<td align=\"center\"><b>" + j +
							"</b></td>");

						Linha += "\n";
						Linha += "</tr>\n<tr>\n";
						Linha += ("<td align=\"center\">" + QtdCdrsHora +
						"</td>");
					}

					if (i == (Max - 1))
					{
						Linha += "\n</tr>\n";

						if (Max != 24)
						{
							Max += 8;
						}
					}

					if (Aux.indexOf(",") != (Aux.length() - 1))
					{
						Aux = Aux.substring(Aux.indexOf(",") + 1,
											Aux.length());
					}
					else
					{
						Aux = null;
						Tabela += Linha;
						Tabela += "\n</tr>\n</table>\n";
						Coluna.add(Tabela);
					}
				}

				Coluna.trimToSize();
				Linhas.add(Coluna);

				if (ResumoDiario.indexOf(";") != (ResumoDiario.length() - 1))
				{
					ResumoDiario = ResumoDiario.substring(ResumoDiario.indexOf(";") +
														  1,
														  ResumoDiario.length());
				}
				else
				{
					ResumoDiario = null;
				}
			}

			Coluna = new Vector();
			Coluna.add("<b>&nbsp;TOTAL&nbsp;</b>");
			Coluna.add("<b>" + new Integer(Total).toString() + "</b>");
			Coluna.add("");
			Linha = "";
			Tabela = "<table border=\"0\" width=\"100%\">\n";
			Tabela += "<tr>\n";

			i = 0;

			short Max = 8;

			for (; i < Max; i++)
			{
				if (i != (Max - 8))
				{
					Linha += ("<td align=\"center\">" + TotalParcial[i] +
					"</td>");
				}
				else
				{
					//Linha += "<tr bgcolor=\"#000099\">\n";
					Linha += "<tr>\n";

					for (short j = i; j < Max; j++)

						//Linha += "<td align=\"center\"><font color=\"#FFFFFF\"><b>"+(j+1)+"</b></font></td>";
						Linha += ("<td align=\"center\"><b>" + j + "</b></td>");

					Linha += "\n";
					Linha += "</tr>\n<tr>\n";
					Linha += ("<td align=\"center\">" + TotalParcial[i] +
					"</td>");
				}

				if (i == (Max - 1))
				{
					Linha += "\n</tr>\n";

					if (Max != 24)
					{
						Max += 8;
					}
					else
					{
						Tabela += Linha;
						Tabela += "\n</tr>\n</table>\n";
					}
				}
			}

			Coluna.add(Tabela);
			Coluna.trimToSize();
			Linhas.add(Coluna);
			Linhas.add(Total+"");
			Linhas.add(TotalParcial);
			

			Linhas.trimToSize();

			return Linhas;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3C6C19460375
	 */
	public String getListaDatas()
	{
		String ListaDatasServ = null;

		ListaDatasServ = m_iUtil.fnGetListaDatasResumo();

		return ListaDatasServ;
	}

	/**
	 * @param p_Origem
	 * @param p_DestinoPerfil
	 * @param p_DestinoUsuario
	 * @param p_Assunto
	 * @param p_Mensagem
	 * @param p_Tipo
	 * @return boolean
	 * @exception
	 * @roseuid 3C6FB2BF019D
	 */
	public boolean enviaMensagem(String p_Origem,
								 String p_DestinoPerfil,
								 String p_DestinoUsuario,
								 String p_Assunto,
								 String p_Mensagem,
								 short p_Tipo)
	{
		iMensagem Mensagem = null;

		Mensagem = m_iUtil.fnCriaMsg();

		if (Mensagem != null)
		{
			Mensagem.fnSetOrigem(p_Origem);

			if (p_DestinoPerfil != null)
			{
				Mensagem.fnSetDestino(p_DestinoPerfil);
				Mensagem.fnSetTipoDestino((short) 1);
			}
			else
			{
				Mensagem.fnSetDestino(p_DestinoUsuario);
				Mensagem.fnSetTipoDestino((short) 0);
			}

			Mensagem.fnSetHeader(p_Assunto);
			Mensagem.fnSetMsg(p_Mensagem);
			Mensagem.fnSetTipoMsg(p_Tipo);
			Mensagem.fnEnvia();
			Mensagem.fnDestroy();

			return true;
		}

		return false;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3C705B3D0229
	 */
	public String getListaFDS()
	{
		String ListaFDS = null;

		ListaFDS = m_iUtil.fnGetListaFDS();

		if ((ListaFDS != null) &&
			(ListaFDS.charAt(ListaFDS.length() - 1) == ';'))
		{
			ListaFDS = ListaFDS.substring(0, ListaFDS.length() - 1);
		}

		return ListaFDS;
	}

	/**
	 * @param p_IdPerfil
	 * @param p_Bilhetador
	 * @param p_TiposCham
	 * @param p_Inicio
	 * @param p_Fim
	 * @param p_NumA
	 * @param p_NumB
	 * @param p_NumC
	 * @param p_RotaEnt
	 * @param p_RotaSai
	 * @param p_FDS
	 * @param p_QtdCdrs
	 * @param p_Arquivo
	 * @return short
	 * @exception
	 * @roseuid 3C7101C401A2
	 */
	public short exportaBase(int p_IdPerfil,
							 String p_Bilhetador,
							 String p_TiposCham,
							 String p_Inicio,
							 String p_Fim,
							 String p_NumA,
							 String p_NumB,
							 String p_NumC,
							 String p_RotaEnt,
							 String p_RotaSai,
							 String p_FDS,
							 int p_QtdCdrs,
							 String p_Arquivo)
	{
		short Retorno = m_iUtil.fnExportaBase(p_Bilhetador,
											  p_TiposCham,
											  p_Inicio,
											  p_Fim,
											  Integer.toString(p_IdPerfil),
											  p_Arquivo,
											  p_QtdCdrs,
											  p_NumA,
											  p_NumB,
											  p_NumC,
											  p_RotaEnt,
											  p_RotaSai,
											  p_FDS);

		return (short) Retorno;
	}

	/**
	 * @param p_UsuarioDef
	 * @return void
	 * @exception
	 * @roseuid 3EEDC4470166
	 */
	public void alteraUsuario(UsuarioDef p_UsuarioDef)
	{
		int i;
		int iTam;
		String CfgHistorico = "";
		String CfgAgenda = "";
		String CfgNormal = "";
		Vector Aux;

		if (m_iListaUsuarios == null)
		{
			m_iListaUsuarios = m_iUtil.fnGetUsuariosCfg();
		}

		Aux = p_UsuarioDef.getConfWebHistorico();
		iTam = Aux.size();

		for (i = 0; i < iTam; i++)
			CfgHistorico += ((String) Aux.elementAt(i) + ";");

		if (CfgHistorico.length() > 0)
		{
			CfgHistorico = CfgHistorico.substring(0, CfgHistorico.length() - 1);
		}

		Aux = p_UsuarioDef.getConfWebAgenda();
		iTam = Aux.size();

		for (i = 0; i < iTam; i++)
			CfgAgenda += (String) Aux.elementAt(i);

		Aux = p_UsuarioDef.getConfWebNormal();
		iTam = Aux.size();

		for (i = 0; i < iTam; i++)
			CfgNormal += (String) Aux.elementAt(i);

		m_iListaUsuarios.fnAlteraUsuario(p_UsuarioDef.getUsuario(),
										p_UsuarioDef.getSenha(),
										p_UsuarioDef.getPerfil(),
										p_UsuarioDef.getDicaSenha(),
										CfgHistorico,
										CfgAgenda,
										CfgNormal);
	}
	
	/**
	 * método que reinicia a senha do usuário, a senha volta a ser o nome
	 * @param usuario
	 * @return true caso volte um objeto usuario ou false caso volte null
	 */
	public boolean reiniciaSenha(String usuario){
		
		if (m_iListaUsuarios == null)
		{
			m_iListaUsuarios = m_iUtil.fnGetUsuariosCfg();
		}
		
		if(m_iListaUsuarios.fnReiniciaSenhaUsuario(usuario)!= null)
			return true;
		
		return false;		
	}

	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRelArmazenado
	 * @return Vector
	 * @exception
	 * @roseuid 3C71A07703DC
	 */
	public Vector getListaRelArmazenados(short p_TipoArmazenado,
										 String p_Perfil,
										 short p_TipoRelArmazenado)
	{
		iListaRelAgendaHistorico ListaRelatorios = null;
		String ListaRel = null;
		Vector Relatorios = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			ListaRel = ListaRelatorios.fnListaRel(p_Perfil, p_TipoArmazenado,
												  p_TipoRelArmazenado);

			if ((ListaRel != null) && (ListaRel.length() != 0))
			{
				String Aux;
				Vector Coluna;
				Relatorios = new Vector();

				while (ListaRel != null)
				{
					Coluna = new Vector();

					// Relatorio
					Aux = ListaRel.substring(0,
											 ListaRel.indexOf(";"));

					// Nome do arquivo
					Coluna.add(Aux.substring(0,
											 Aux.indexOf(",")));
					Aux = Aux.substring(Aux.indexOf(",") + 1,
										Aux.length());

					// Nome do relatorio
					Coluna.add(Aux.substring(0,
											 Aux.indexOf(",")));
					Aux = Aux.substring(Aux.indexOf(",") + 1,
										Aux.length());

					// ID do Relatorio
					Coluna.add(Aux.substring(0,
											 Aux.indexOf(",")));
					Aux = Aux.substring(Aux.indexOf(",") + 1,
										Aux.length());

					// Data do relatorio
					Coluna.add(Aux);

					Coluna.trimToSize();
					Relatorios.add(Coluna);

					if (ListaRel.indexOf(";") != (ListaRel.length() - 1))
					{
						ListaRel = ListaRel.substring(ListaRel.indexOf(";") +
													  1,
													  ListaRel.length());
					}
					else
					{
						ListaRel = null;
					}
				}

				Relatorios.trimToSize();
			}

			ListaRelatorios.fnDestroy();

			return Relatorios;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param p_TipoArmazenamento
	 * @param p_Perfil
	 * @param p_TipoRelArmazenado
	 * @param p_Tipo
	 * @param p_NomesRels
	 * @param p_DatasRels
	 * @return Vector
	 * @exception
	 * @roseuid 3F0093FF0255
	 */
	public Vector getListaRelArmazenados2(short p_TipoArmazenamento,
										  String p_Perfil,
										  short p_TipoRelArmazenado,
										  short p_Tipo,
										  String p_NomesRels,
										  String p_DatasRels,
										  String perfilAtual)
	{
		iListaRelAgendaHistorico ListaRelatorios = null;
		String ListaRel = null;
		String Perfil;
		String QtdRelatorios;
		String ArqRelatorio;
		String NomeRelatorio;
		String Relatorio;
		String Data;
		String DataGeracao;
		String Aux;
		Vector vListaRelatorios = null;
		Vector Datas = null;
		Vector DatasQtd;
		Vector Coluna = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			//
			// Exemplo de retorno:
			// perfil#Nome Rel#1056975677-7.txt@20030630-1,20030701-1;perfil#Nomre Rel#1056975677-7.txt@20030630-1
			//
			ListaRel = ListaRelatorios.fnListaRelatorios(p_Perfil,perfilAtual,
														 p_TipoArmazenamento,
														 p_TipoRelArmazenado,
														 p_Tipo, p_NomesRels,
														 p_DatasRels);

			//System.out.println("Linhas: "+ListaRel);
			//System.out.println("Linhas size: "+ListaRel.length());
			if ((ListaRel != null) && (ListaRel.length() != 0))
			{
				vListaRelatorios = new Vector();
			}
			String []listaAux = ListaRel.split(";");
			System.out.println("/-----------------LISTA DE RELATORIOS DO SERVIDOR ("+listaAux.length+" relatórios)---------------------------------------------");
			for(int i = 0; i< listaAux.length; i++){
				System.out.println("|\t"+listaAux[i]);
			}
			System.out.println("\\---------------------------------------------------------------------------------------------");
			// Separa relatórios
			while ((ListaRel != null) && (ListaRel.length() != 0))
			{
				if (p_NomesRels.length() == 0)
				{
					if (ListaRel.indexOf(";") != -1)
					{
						Relatorio = ListaRel.substring(0,
													   ListaRel.indexOf(";"));
						ListaRel = ListaRel.substring(ListaRel.indexOf(";") +
													  1,
													  ListaRel.length());
					}
					else
					{
						Relatorio = new String(ListaRel);
						ListaRel = null;
					}
					/**
					 * Esta validação esta sendo feita para evitar que dê erro na sequência abaixo, pois
					 * de vez em quando o servidor manda errado a lista de relatorios.
					 * Quando um diretorio onde fica os relatorios está vazio, normalmente o servidor apaga esse
					 * diretorio ou simplemente ignora ele, pois não a oque ler nele. Mas por algum motivo
					 * de fez em quando ele lê os diretorios vazios e na variavel 'ListaRel' um dos tokens
					 * que é o diretorio vazio vem uma string sem nada "". Esse if é para evitar esse erro.
					 */
					if(Relatorio.length() > 0 && !Pattern.matches("\\s+",Relatorio))
					{
						try{//para evitar dar tela branca, pro usuario
							System.out.println("RelatorioString: " + Relatorio);
							System.out.println("p_DataRels: " + p_DatasRels);
							Perfil = Relatorio.substring(0,
														 Relatorio.indexOf('#'));
							System.out.println("--- Perfil: " + Perfil);
							Relatorio = Relatorio.substring(Relatorio.indexOf('#') + 1,
															Relatorio.length());
							NomeRelatorio = Relatorio.substring(0,
																Relatorio.indexOf('#'));
							System.out.println("--- NomeRelatorio: " + NomeRelatorio);
							Relatorio = Relatorio.substring(Relatorio.indexOf('#') + 1,
															Relatorio.length());
							ArqRelatorio = Relatorio.substring(0,
															   Relatorio.indexOf('#'));
							System.out.println("--- ArqRelatorio: " + ArqRelatorio);
							Relatorio = Relatorio.substring(Relatorio.indexOf('#') + 1,
															Relatorio.length());
							DataGeracao = Relatorio.substring(0,
															  Relatorio.indexOf('@'));
		
							System.out.println("--- DataGeracao: " + DataGeracao);
		
							// Separa datas
							Aux = Relatorio.substring(Relatorio.indexOf('@') + 1,
													  Relatorio.length());
		
							//System.out.println("--- Datas: "+Aux);
							Datas = new Vector();
		
							while (Aux != null)
							{
								if (Aux.indexOf(",") != -1)
								{
									Data = Aux.substring(0,
														 Aux.indexOf(","));
									Aux = Aux.substring(Aux.indexOf(",") + 1,
														Aux.length());
								}
								else
								{
									Data = new String(Aux);
									Aux = null;
								}
		
								QtdRelatorios = Data.substring(Data.indexOf('-') + 1,
															   Data.length());
								Data = Data.substring(0,
													  Data.indexOf('-'));
		
								//System.out.println("--- Data: "+Data);
								//System.out.println("--- QtdRelatorios: "+QtdRelatorios);
								DatasQtd = new Vector(2);
								DatasQtd.add(Data);
								DatasQtd.add(QtdRelatorios);
		
								Datas.add(DatasQtd);
							}
		
							//System.out.println("--- Saiu ---");
							Datas.trimToSize();
		
							//Relatorio = Relatorio.substring(0, Relatorio.indexOf('@'));
							//System.out.println("--- Relatorio: "+Relatorio + " --- "+Datas.size());
							Coluna = new Vector();
							Coluna.add(Perfil);
							Coluna.add(NomeRelatorio);
							Coluna.add(ArqRelatorio);
							Coluna.add(DataGeracao);
							Coluna.add(Datas);
							Coluna.trimToSize();
		
							vListaRelatorios.add(Coluna);
						}catch(StringIndexOutOfBoundsException exc){
							System.out.println("------Lista de Relatorios com erro------");
							exc.printStackTrace();
						}
					}
				}
				else if (p_DatasRels.length() != 0)
				{
					if (ListaRel.indexOf(";") != -1)
					{
						Relatorio = ListaRel.substring(0,
													   ListaRel.indexOf(";"));
						ListaRel = ListaRel.substring(ListaRel.indexOf(";") +
													  1,
													  ListaRel.length());
					}
					else
					{
						Relatorio = new String(ListaRel);
						ListaRel = null;
					}

					Perfil = Relatorio.substring(0,
												 Relatorio.indexOf('#'));

					//   System.out.println("--- Perfil: "+Perfil);
					Relatorio = Relatorio.substring(Relatorio.indexOf('#') + 1,
													Relatorio.length());
					NomeRelatorio = Relatorio.substring(0,
														Relatorio.indexOf('#'));

					//   System.out.println("--- NomeRelatorio: "+NomeRelatorio);
					Relatorio = Relatorio.substring(Relatorio.indexOf('#') + 1,
													Relatorio.length());

					//               ArqRelatorio = Relatorio.substring(0, Relatorio.indexOf('#'));
					//   System.out.println("--- ArqRelatorio: "+ArqRelatorio);
					//               Relatorio = Relatorio.substring(Relatorio.indexOf('#')+1, Relatorio.length());
					DataGeracao = Relatorio.substring(0,
													  Relatorio.length());

					//   System.out.println("--- DataGeracao: "+DataGeracao);
					Datas = new Vector();

					if (DataGeracao.indexOf(',') != -1)
					{
						while (DataGeracao != null)
						{
							if (DataGeracao.indexOf(",") != -1)
							{
								Data = DataGeracao.substring(0,
															 DataGeracao.indexOf(","));
								DataGeracao = DataGeracao.substring(DataGeracao.indexOf(",") +
																	1,
																	DataGeracao.length());
							}
							else
							{
								Data = new String(DataGeracao);
								DataGeracao = null;
							}

							//   System.out.println("--- Data: "+Data);
							//   System.out.println("--- DataGeracao: "+DataGeracao);
							Datas.add(Data);
						}
					}
					else
					{
						Datas.add(DataGeracao);
					}

					Datas.trimToSize();

					Coluna = new Vector();
					Coluna.add(Perfil);
					Coluna.add(NomeRelatorio);

					//Coluna.add(ArqRelatorio);
					Coluna.add(Datas);
					Coluna.trimToSize();

					vListaRelatorios.add(Coluna);
				}
				else
				{
					if (ListaRel.indexOf(";") != -1)
					{
						ArqRelatorio = ListaRel.substring(0,
														  ListaRel.indexOf(";"));
						ListaRel = ListaRel.substring(ListaRel.indexOf(";") +
													  1,
													  ListaRel.length());
					}
					else
					{
						ArqRelatorio = new String(ListaRel);
						ListaRel = null;
					}

					Coluna = new Vector();
					Coluna.add(ArqRelatorio);
					Coluna.trimToSize();

					vListaRelatorios.add(Coluna);
				}
			}
		}

		if (vListaRelatorios != null)
		{
			vListaRelatorios.trimToSize();
		}

		return vListaRelatorios;
	}

	/**
	 * @param p_Perfil
	 * @param p_TipoArmazenamento
	 * @param p_TipoRelatorio
	 * @param p_NomeRelatorio
	 * @param p_Relatorios
	 * @return void
	 * @exception
	 * @roseuid 3F1306620111
	 */
	public void apagaRelArmazenados(String p_Perfil,
									short p_TipoArmazenamento,
									short p_TipoRelatorio,
									String p_NomeRelatorio,
									String p_Relatorios)
	{
		iListaRelAgendaHistorico ListaRelatorios = null;
		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			ListaRelatorios.fnApagaRelatorios(p_Perfil, p_TipoArmazenamento,
											  p_TipoRelatorio, p_NomeRelatorio,
											  p_Relatorios);
		}
	}

	/**
	 * @param p_Perfil
	 * @return Vector
	 * @exception
	 * @roseuid 3C71B0950095
	 */
	public Vector getListaRelExportados(String p_Perfil)
	{
		String ListaRel = null;
		Vector Relatorios = null;

		ListaRel = m_iUtil.fnGetListaBaseExportada2(p_Perfil);

		if ((ListaRel != null) && (ListaRel.length() != 0))
		{
			String Aux;
			String Aux2;
			Vector Coluna;
			Relatorios = new Vector();
			int cont=0;
			List<String> nomeDosArquivos = new ArrayList<String>();

			while (ListaRel != null)
			{
				cont++;
				Aux2 = ListaRel.substring(0,
										  ListaRel.indexOf(";"));
				Coluna = new Vector();

				// Relatorio
				Aux = Aux2.substring(0,
									 Aux2.indexOf(","));
				Coluna.add(Aux);
				nomeDosArquivos.add(Aux);

				// Cabeçalho do relatório
				Aux2 = Aux2.substring(Aux2.indexOf(",") + 1,
									  Aux2.length());
				Aux = Aux2.substring(0,
									 Aux2.indexOf(","));
				Coluna.add(Aux);

				// Data do arquivo
				Aux2 = Aux2.substring(Aux2.indexOf(",") + 1,
									  Aux2.length());
				Aux = Aux2.substring(0,
									 Aux2.indexOf(","));
				Coluna.add(Aux);

				// Tamanho do arquivo
				Aux2 = Aux2.substring(Aux2.indexOf(",") + 1,
									  Aux2.length());
				Coluna.add(Aux2);
				Coluna.trimToSize();
				Relatorios.add(Coluna);

				if (ListaRel.indexOf(";") != (ListaRel.length() - 1))
				{
					ListaRel = ListaRel.substring(ListaRel.indexOf(";") + 1,
												  ListaRel.length());
				}
				else
				{
					ListaRel = null;
				}
			}

			Relatorios.trimToSize();
			/////////////////////////////////
			Locale locale = new Locale("pt","BR"); 
			GregorianCalendar calendar = new GregorianCalendar(); 
			SimpleDateFormat formatador = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' 'HH':'mm':'ss",locale); 
			/////////////////////////////////
			System.out.println("\n       \\--------------------------------------------------------------------------------------------------------------");
			System.out.println("       |");
			System.out.println("       |    "+formatador.format(calendar.getTime())+" - Listagem dos "+cont+
					" Arquivos recuperados do bdados e adicionados na Tabela.");
			for (String s : nomeDosArquivos) {
				System.out.println("       |    "+formatador.format(calendar.getTime())+" -    "+s);
			}
			System.out.println("       |");
			System.out.println("       /--------------------------------------------------------------------------------------------------------------");
			System.out.println();
			//////////////////////////////////
		}
		return Relatorios;
	}

	/**
	 * @param p_Perfil
	 * @return Vector
	 * @exception
	 * @roseuid 3C71B0950095
	 */
	public Vector getListaRelExportadosXLSDrill(String p_Perfil)
	{
		String ListaRel = null;
		Vector Relatorios = null;

		ListaRel = m_iUtil.fnGetListaBaseExportadaXLS(p_Perfil);

		if ((ListaRel != null) && (ListaRel.length() != 0))
		{
			String Aux;
			String Aux2;
			Vector Coluna;
			Relatorios = new Vector();

			while (ListaRel != null)
			{
				Aux2 = ListaRel.substring(0,
										  ListaRel.indexOf(";"));
				Coluna = new Vector();

				// Relatorio
				Aux = Aux2.substring(0,
									 Aux2.indexOf(","));
				Coluna.add(Aux);

				// Cabeçalho do relatório
				Aux2 = Aux2.substring(Aux2.indexOf(",") + 1,
									  Aux2.length());
				Aux = Aux2.substring(0,
									 Aux2.indexOf(","));
				Coluna.add(Aux);

				// Data do arquivo
				Aux2 = Aux2.substring(Aux2.indexOf(",") + 1,
									  Aux2.length());
				Aux = Aux2.substring(0,
									 Aux2.indexOf(","));
				Coluna.add(Aux);

				// Tamanho do arquivo
				Aux2 = Aux2.substring(Aux2.indexOf(",") + 1,
									  Aux2.length());
				Coluna.add(Aux2);
				Coluna.trimToSize();
				Relatorios.add(Coluna);

				if (ListaRel.indexOf(";") != (ListaRel.length() - 1))
				{
					ListaRel = ListaRel.substring(ListaRel.indexOf(";") + 1,
												  ListaRel.length());
				}
				else
				{
					ListaRel = null;
				}
			}

			Relatorios.trimToSize();
		}

		return Relatorios;
	}

	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_IdRel
	 * @param p_Arquivo
	 * @return Vector
	 * @exception
	 * @roseuid 3C7305F1012A
	 */
	public Vector getRelatorioHeader(short p_TipoArmazenado,
									 String p_Perfil,
									 int p_TipoRel,
									 int p_IdRel,
									 String p_Arquivo)
	{
		iListaRelAgendaHistorico ListaRelatorios = null;
		iRetRelatorio Relatorio;
		Vector Header = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			Relatorio = ListaRelatorios.fnGetRelatorio(p_TipoArmazenado,
													   p_TipoRel, p_Perfil,
													   p_IdRel, p_Arquivo);

			if (Relatorio != null)
			{
			    try
			    {
					Relatorio.fnAbre();

					Header = new Vector();
					Header.add(Relatorio.fnGetNomeRel());
					Header.add(Integer.toString(Relatorio.fnGetQtdLinhas()));
					Header.add(Relatorio.fnGetBilhet());
					Header.add(Relatorio.fnGetDataRel());
					Header.add(Relatorio.fnGetDataIni());
					Header.add(Relatorio.fnGetDataFim());
					Header.add(Relatorio.fnGetOrigem());
					Header.add(Relatorio.fnGetDestino());
					Header.add(Relatorio.fnGetEncaminhado());
					Header.add(Relatorio.fnGetRotaSai());
					Header.add(Relatorio.fnGetRotaEnt());
					Header.add(Relatorio.fnGetTroncoEnt());
					Header.add(Relatorio.fnGetTroncoSai());
			    }
			    catch (Exception exc)
			    {
			        /** Necessario para garantir que o Servutil feche o arquivo
			         * caso ocorra alguma excecao. */
			        Relatorio.fnFecha();
			    }
				Relatorio.fnFecha();
			}

			ListaRelatorios.fnDestroy();
		}

		return Header;
	}

	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_IdRel
	 * @param p_Arquivo
	 * @param p_NomeRel
	 * @param p_DataGeracao
	 * @return Vector
	 * @exception
	 * @roseuid 3F09B56200D5
	 */
	public Vector getRelatorioHeader2(short p_TipoArmazenado,
									  String p_Perfil,
									  int p_TipoRel,
									  int p_IdRel,
									  String p_Arquivo,
									  String p_NomeRel,
									  String p_DataGeracao)
	{
		iListaRelAgendaHistorico ListaRelatorios = null;
		iRetRelatorio Relatorio;
		Vector Header = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			Relatorio = ListaRelatorios.fnGetRelatorio2(p_TipoArmazenado,
														p_TipoRel, p_Perfil,
														p_IdRel, p_DataGeracao,
														p_Arquivo, p_NomeRel);

			if (Relatorio != null)
			{
			    try
			    {
					Relatorio.fnAbre();

					Header = new Vector();
					Header.add(Relatorio.fnGetNomeRel());
					Header.add(Integer.toString(Relatorio.fnGetQtdLinhas()));
					Header.add(Relatorio.fnGetBilhet());
					Header.add(Relatorio.fnGetDataRel());
					Header.add(Relatorio.fnGetDataIni());
					Header.add(Relatorio.fnGetDataFim());
					Header.add(Relatorio.fnGetOrigem());
					Header.add(Relatorio.fnGetDestino());
					Header.add(Relatorio.fnGetEncaminhado());
					Header.add(Relatorio.fnGetRotaSai());
					Header.add(Relatorio.fnGetRotaEnt());
					Header.add(Relatorio.fnGetTroncoEnt());
					Header.add(Relatorio.fnGetTroncoSai());
			    }
			    catch(Exception exc)
			    {
			        Relatorio.fnFecha();
			    }
				Relatorio.fnFecha();
			}

			ListaRelatorios.fnDestroy();
		}

		return Header;
	}

	/**
	 * @param p_Perfil
	 * @param p_Arquivo
	 * @return Vector
	 * @exception
	 * @roseuid 3C765C350161
	 */
	public Vector getRelatorioExpHeaderDrill(String p_Perfil,
										String p_Arquivo)
	{
		//      iRelatorioExportado Relatorio = null;
		String Relatorio = null;

		//      iRelatorioExportado Relatorio = null;
		String Aux;
		Vector Header = null;

		Relatorio = m_iUtil.fnGetRelatorioExportadoDrill(p_Perfil, p_Arquivo);

		if (Relatorio != null)
		{
			Header = new Vector();

			// Nome
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// QtdMaxCDRs
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Bilhetadores
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// DataArq
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Periodo Inicial
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Periodo Final
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// NumA
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// NumB
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// NumC
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Rota Entrada
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Rota Saida
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// FDS
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Header.add(Relatorio);
		}

		/*      Relatorio = m_iUtil.fnGetRelatorioExportado(p_Perfil, p_Arquivo);
		      if (Relatorio != null)
		      {
		         if (Relatorio.fnAbre() == true)
		         {
		            Header = new Vector();
		            Header.add(Relatorio.fnGetNome());
		            Header.add(Integer.toString(Relatorio.fnGetQtdMaxCdr()));
		            Header.add(Relatorio.fnGetFltBilhetador());
		            Header.add(Relatorio.fnGetFltDataArq());
		            Header.add(Relatorio.fnGetFltPeriodoInicial());
		            Header.add(Relatorio.fnGetFltPeriodoFinal());
		            Header.add(Relatorio.fnGetFltNumA());
		            Header.add(Relatorio.fnGetFltNumB());
		            Header.add(Relatorio.fnGetFltNumC());
		            Header.add(Relatorio.fnGetFltRotaE());
		            Header.add(Relatorio.fnGetFltRotaS());
		            Header.add(Relatorio.fnGetFltFDS());
		            Relatorio.fnFecha();
		            Relatorio.fnDestroy();
		         }
		         else
		         {
		            Relatorio.fnDestroy();
		            return null;
		         }
		      }
		*/
		return Header;
	}
	
	/**
	 * @param p_Perfil
	 * @param p_Arquivo
	 * @return Vector
	 * @exception
	 * @roseuid 3C765C350161
	 */
	public Vector getRelatorioExpHeader(String p_Perfil,
										String p_Arquivo)
	{
		//      iRelatorioExportado Relatorio = null;
		String Relatorio = null;

		//      iRelatorioExportado Relatorio = null;
		String Aux;
		Vector Header = null;

		Relatorio = m_iUtil.fnGetRelatorioExportado(p_Perfil, p_Arquivo);

		if (Relatorio != null)
		{
			Header = new Vector();

			// Nome
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// QtdMaxCDRs
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Bilhetadores
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// DataArq
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Periodo Inicial
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Periodo Final
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// NumA
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// NumB
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// NumC
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Rota Entrada
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// Rota Saida
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Aux = Relatorio.substring(0,
									  Relatorio.indexOf("@"));
			Header.add(Aux);

			// FDS
			Relatorio = Relatorio.substring(Relatorio.indexOf("@") + 1,
											Relatorio.length());
			Header.add(Relatorio);
		}

		/*      Relatorio = m_iUtil.fnGetRelatorioExportado(p_Perfil, p_Arquivo);
		      if (Relatorio != null)
		      {
		         if (Relatorio.fnAbre() == true)
		         {
		            Header = new Vector();
		            Header.add(Relatorio.fnGetNome());
		            Header.add(Integer.toString(Relatorio.fnGetQtdMaxCdr()));
		            Header.add(Relatorio.fnGetFltBilhetador());
		            Header.add(Relatorio.fnGetFltDataArq());
		            Header.add(Relatorio.fnGetFltPeriodoInicial());
		            Header.add(Relatorio.fnGetFltPeriodoFinal());
		            Header.add(Relatorio.fnGetFltNumA());
		            Header.add(Relatorio.fnGetFltNumB());
		            Header.add(Relatorio.fnGetFltNumC());
		            Header.add(Relatorio.fnGetFltRotaE());
		            Header.add(Relatorio.fnGetFltRotaS());
		            Header.add(Relatorio.fnGetFltFDS());
		            Relatorio.fnFecha();
		            Relatorio.fnDestroy();
		         }
		         else
		         {
		            Relatorio.fnDestroy();
		            return null;
		         }
		      }
		*/
		return Header;
	}

	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_IdRel
	 * @param p_Arquivo
	 * @return Vector
	 * @exception
	 * @roseuid 3C7306050273
	 */
	public Vector getRelatorio(short p_TipoArmazenado,
							   String p_Perfil,
							   int p_TipoRel,
							   int p_IdRel,
							   String p_Arquivo)
	{
		iRetRelatorio Relatorio = null;
		iListaRelAgendaHistorico ListaRelatorios = null;
		Vector Header = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			Relatorio = ListaRelatorios.fnGetRelatorio(p_TipoArmazenado,
													   p_TipoRel, p_Perfil,
													   p_IdRel, p_Arquivo);

			if (Relatorio != null)
			{
			    try
			    {
					Relatorio.fnAbre();

					Header = new Vector();
					Header.add(Relatorio.fnGetNomeRel());
					Header.add(Relatorio.fnGetBilhet());
					Header.add(Relatorio.fnGetDataRel());
					Header.add(Relatorio.fnGetDataIni());
					Header.add(Relatorio.fnGetDataFim());
					Header.add(Relatorio.fnGetOrigem());
					Header.add(Relatorio.fnGetDestino());
					Header.add(Relatorio.fnGetEncaminhado());
					Header.add(Relatorio.fnGetRotaSai());
					Header.add(Relatorio.fnGetRotaEnt());
					Header.add(Relatorio.fnGetTroncoEnt());
					Header.add(Relatorio.fnGetTroncoSai());
					Header.add(Relatorio.fnGetNomeColuna((short) 1));
					Header.add(Relatorio.fnGetNomeColuna((short) 2));
					Header.add(Relatorio.fnGetIndicadores());
					Header.add(Relatorio.fnGetIndicadoresCores());

					boolean bSai = false;
					String LinhaRel = null;

					do
					{
						LinhaRel = Relatorio.fnGetLinha();

						if ((LinhaRel != null) && (LinhaRel.length() != 0))
						{
							//if (LinhaRel.charAt(0) != '1')
							Header.add(LinhaRel);
						}
						else
						{
							bSai = true;
						}
					}
					while (!bSai);
				}
				catch(Exception exc)
				{
				    Relatorio.fnFecha();
				}
				Relatorio.fnFecha();

				/*
				            else
				            {
				               ListaRelatorios.fnDestroy();
				               return null;
				            }
				*/
			}

			ListaRelatorios.fnDestroy();
		}

		if (Header != null)
		{
			Header.trimToSize();
		}

		return Header;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p_TipoArmazenado DOCUMENT ME!
	 * @param p_Perfil DOCUMENT ME!
	 * @param p_TipoRel DOCUMENT ME!
	 * @param p_IdRel DOCUMENT ME!
	 * @param p_DataGeracao DOCUMENT ME!
	 * @param p_Arquivo DOCUMENT ME!
	 * @param p_Relatorio DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean verificaFimHistorico(short p_TipoArmazenado,
									    int p_TipoRel,
										String p_Perfil,
										String p_DataGeracao,
										String p_Relatorio,
										int p_IdRel)
	{
		iListaRelAgendaHistorico ListaRelatorios = m_iUtil.fnCriaListaRelatorios();
		boolean relConcluido = ListaRelatorios.fnRelConcluido(p_TipoArmazenado,
															  p_TipoRel,
															  p_Perfil,
															  p_DataGeracao,
															  p_Relatorio,
															  p_IdRel);

		return relConcluido;
	}
	
	public String getContadoresDoRelatorio(){
		return contadoresDoRelatorio;
	}
	
	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_IdRel
	 * @param p_DataGeracao
	 * @param p_Arquivo
	 * @param p_Relatorio
	 * @return Vector
	 * @exception
	 * @roseuid 3F01E7730120
	 */
	public Vector getRelatorio2(short p_TipoArmazenado,
								String p_Perfil,
								int p_TipoRel,
								int p_IdRel,
								String p_DataGeracao,
								String p_Arquivo,
								String p_Relatorio)
	{
		iRetRelatorio Relatorio = null;
		iListaRelAgendaHistorico ListaRelatorios = null;
		Vector Header = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			Relatorio = ListaRelatorios.fnGetRelatorio2(p_TipoArmazenado,
														p_TipoRel, p_Perfil,
														p_IdRel, p_DataGeracao,
														p_Arquivo, p_Relatorio);

			if (Relatorio != null)
			{
			    try
			    {
					Relatorio.fnAbre();

					Header = new Vector();
					Header.add(Relatorio.fnGetNomeRel()); // 0
					Header.add(Relatorio.fnGetBilhet());
					Header.add(Relatorio.fnGetDataRel());
					Header.add(Relatorio.fnGetDataIni());
					Header.add(Relatorio.fnGetDataFim());
					Header.add(Relatorio.fnGetOrigem()); // 5
					Header.add(Relatorio.fnGetDestino());
					Header.add(Relatorio.fnGetEncaminhado());
					Header.add(Relatorio.fnGetRotaSai());
					Header.add(Relatorio.fnGetRotaEnt());
					Header.add(Relatorio.fnGetTroncoEnt()); // 10
					Header.add(Relatorio.fnGetTroncoSai());
					Header.add(Relatorio.fnGetNomeColuna((short) 1));
					Header.add(Relatorio.fnGetNomeColuna((short) 2));
					Header.add(Relatorio.fnGetIndicadores());
					Header.add(Relatorio.fnGetIndicadoresCores()); // 15

					//Este 'GATO' que esta sendo feito aqui, é para poder tratar o relatorio do historico quando
					//o relatorio tem contadores que estão incompativeis com o arquivo xml.
					// e "Relatorio.fnGetContadores()" não esta sendo inserido no 'Header', porque
					//esse vetor é muito usado no Historico e poderia gerar erros caso sua ordem fosse mudada. 
					contadoresDoRelatorio = Relatorio.fnGetContadores();
					//-------------------------------------
					boolean bSai = false;
					String LinhaRel = null;

					do
					{
						LinhaRel = Relatorio.fnGetLinha();
						if ((LinhaRel != null) && (LinhaRel.length() != 0))
						{
							//if (LinhaRel.charAt(0) != '1')
							//System.out.println("LINHA do RELATORIO: "+LinhaRel);
							Header.add(LinhaRel);
						}
						else
						{
							bSai = true;
						}
					}
					while (!bSai);
			    }
			    catch(Exception exc)
			    {
			        Relatorio.fnFecha();
			    }
				Relatorio.fnFecha();
			}

			else
			{
				ListaRelatorios.fnDestroy();

				return null;
			}
		}

		ListaRelatorios.fnDestroy();

		if (Header != null)
		{
			Header.trimToSize();
		}

		return Header;
	}
	
	
	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_IdRel
	 * @param p_DataGeracao
	 * @param p_Arquivo
	 * @param p_Relatorio
	 * @return 
	 * 
	 * Esse método foi criado na tentativa de melhorar o desempenho pois havia uma chamada ao método 
	 * getRelatorio2, no contrutor do Historico, que servia apenas para a criação dessa lista, sendo que
	 * nesse método existe uma séria de outras chamadas ao servidor que se tornam desnecessárias nesse 
	 * momento. 
	 * Sua finalidade é inicializar a lista de contadores do relatorio.
	 * 
	 */
	public boolean iniciaContadores(short p_TipoArmazenado,
			String p_Perfil,
			int p_TipoRel,
			int p_IdRel,
			String p_DataGeracao,
			String p_Arquivo,
			String p_Relatorio)
	{

		iRetRelatorio Relatorio = null;

		iListaRelAgendaHistorico ListaRelatorios = null;

		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		Relatorio = ListaRelatorios.fnGetRelatorio2(p_TipoArmazenado,
				p_TipoRel, p_Perfil,
				p_IdRel, p_DataGeracao,
				p_Arquivo, p_Relatorio);

		if (Relatorio != null)
		{
			Relatorio.fnAbre();

			contadoresDoRelatorio = Relatorio.fnGetContadores();
			
			ListaRelatorios.fnDestroy();
			
	//		Relatorio.fnFecha();

			return true;

		}
		else{
			ListaRelatorios.fnDestroy();
	//		Relatorio.fnFecha();
			return false;
		}
			

	}

	/**
	 * @param p_Operacao
	 * @param p_CfgConversor
	 * @return String
	 * @exception
	 * @roseuid 3C8E1E6402C2
	 */
	public String controlaConversor(String p_Operacao,
									String p_CfgConversor)
	{
		String Retorno = null;
		Retorno = m_iUtil.fnControlaConversor(p_Operacao, p_CfgConversor);

		return Retorno;
	}
	
	/**
	 * @param p_Operacao
	 * @param p_CfgConversor
	 * @return String
	 * @exception
	 * @roseuid 3C8E1E6402C2
	 */
	public String controlaReprocessador(String p_Operacao,
										String p_CfgReprocessador)
	{
		String Retorno = null;
		Retorno = m_iUtil.fnControlaReprocessador(p_Operacao, p_CfgReprocessador);

		return Retorno;
	}

	/**
	 * @param p_Operacao
	 * @param p_CfgParser
	 * @return String
	 * @exception
	 * @roseuid 3C8E588E02E1
	 */
	public String controlaParser(String p_Operacao,
								 String p_CfgParser)
	{
		String Retorno = null;
		Retorno = m_iUtil.fnControlaParser(p_Operacao, p_CfgParser);

		return Retorno;
	}
	
	
	/**
	 * Método que controla parseGen
	 * 19/04/2006
	 * */
	public String controlaParserGen(String p_Operacao,
			 String p_CfgParserGen)
	{
		String Retorno = null;
		Retorno = m_iUtil.fnControlaParserGen(p_Operacao, p_CfgParserGen);
		
		return Retorno;
	}

	/**
	 * @param p_Operacao
	 * @return String
	 * @exception
	 * @roseuid 3C8E589002C6
	 */
	public String controlaServidores(String p_Operacao, int idServidor, No noCluster)
	{
		// Definição do código do ServCtrl: COD_SRV_CONTROLE       0
		StringBuffer msgRetornoBuffer = new StringBuffer(100);
		String msgRetorno = null;
		String servidor = "\n";

		if (noCluster != null)
		    servidor = "  Servidor: " + noCluster.getHostName() + "\n";

		if (idServidor == COD_SRV_CONTROLE)
		{
		    msgRetornoBuffer.append(m_iUtil.fnControlaProcGen((short) COD_SRV_CONTROLE, p_Operacao));

			if (msgRetornoBuffer!=null)
			{
//			    msgRetornoBuffer.append(m_iUtil.fnControlaProcGen((short) COD_SRV_AGENDA, p_Operacao));
//			    msgRetornoBuffer.append(m_iUtil.fnControlaProcGen((short) COD_SRV_HISTORICO, p_Operacao));
				String aux = m_iUtil.fnControlaProcGen((short) COD_NEW_AGENDA, p_Operacao);
				if(aux.contains(".bat"))
					aux = aux.replace(".bat", ".exe");
				msgRetornoBuffer.append(aux);
			}
		}
		else
		{
			String aux = m_iUtil.fnControlaProcGen((short) idServidor, p_Operacao);
			if(aux.contains(".bat"))
				aux = aux.replace(".bat", ".exe");
		    msgRetornoBuffer.append(aux);
		}

		msgRetorno = msgRetornoBuffer.toString();

		return msgRetorno.replaceAll("\n", servidor);
	}

	/**
	 * @param p_Operacao
	 * @return String
	 * @exception
	 * @roseuid 3CE3EB770209
	 */
	
	public String controlaServAlr(String p_Operacao, String Cliente, String Opcao)
	{
		// Definição do código do ServCtrl: COD_SRV_ALARMES       12
		
		String Retorno = null;
		if(Cliente.equalsIgnoreCase("claro") && Opcao.equalsIgnoreCase("servidor"))
			Retorno = m_iUtil.fnControlaProcGen((short) 11, p_Operacao);
		else if(Cliente.equalsIgnoreCase("claro") && Opcao.equalsIgnoreCase("gera"))
			Retorno = m_iUtil.fnControlaProcGen((short) 12, p_Operacao);
		else if(Cliente.equalsIgnoreCase("oi") && Opcao.equalsIgnoreCase("servidor"))
			Retorno = m_iUtil.fnControlaProcGen((short) 11, p_Operacao);
		else if(Cliente.equalsIgnoreCase("oi") && Opcao.equalsIgnoreCase("gera"))
			Retorno = m_iUtil.fnControlaProcGen((short) 12, p_Operacao);
		else
			Retorno = m_iUtil.fnControlaProcGen((short) 9, p_Operacao);
		return Retorno;
	}

	/**
	 * @param p_Operacao
	 * @return String
	 * @exception
	 * @roseuid 3CE3EB7D01E9
	 */
	public String controlaAgnCDR(String p_Operacao)
	{
		// Definição do código do ServCtrl: COD_SRV_AGNCDR        10
		String Retorno = null;
		Retorno = m_iUtil.fnControlaProcGen((short) 10, p_Operacao);

		return Retorno;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3C923A21026B
	 */
	public String StartUp()
	{
		String Retorno = null;
		Retorno = m_iUtil.fnStartUp();

		return Retorno;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3C923A3C006B
	 */
	public String ShutDown()
	{
		String Retorno = null;
		Retorno = m_iUtil.fnShutDown();

		return Retorno;
	}

	/**
	 * @param p_PID
	 * @param p_Host
	 * @return Vector
	 * @exception
	 * @roseuid 3C97507A027E
	 */
	public Vector getProcesso(int p_PID,
							  String p_Host)
	{
		ProcessoDef Processo;
		Vector Processos = getListaProcessos();

		if (Processos != null)
		{
			for (int i = 0; i < Processos.size(); i++)
			{
				Processo = (ProcessoDef) Processos.elementAt(i);

				if ((Processo.getId() == p_PID) &&
					(Processo.getHost().equals(p_Host) == true))
				{
					Vector Retorno = new Vector();
					Retorno.add(Processo.getNome());
					Retorno.add(Integer.toString(p_PID));
					Retorno.add(Processo.getHost());
					Retorno.add("N/A");
					Retorno.add("N/A");
					Retorno.add(Processo.getDataInicioStr());
					Retorno.add(Processo.getUltimoAcessoStr());

					//Retorno.add("N/A");
					Retorno.add("Sem Informação");
					Retorno.trimToSize();

					return Retorno;
				}
			}
		}

		return null;
	}


	/**
	 * @param p_IP
	 * @return UsuarioDef
	 * @exception
	 * Verifica se o usuario está logado e retorna o  mesmo em UsuarioDef ou null, caso ele não esteja logado. A verificação é feita pelo IP da máquina do usuário.
	 * @roseuid 3CB200C10266
	 */
	public UsuarioDef usuarioLogadoIP(String p_IP)
	{
		UsuarioDef Usuario = null;
		iUsuarioWeb UsuarioWeb = null;

		UsuarioWeb = m_iListaUsuarioWeb.fnUsuarioExisteIP(p_IP);

		if (UsuarioWeb != null)
		{
			Usuario = new UsuarioDef(UsuarioWeb.fnGetUsuario(),
									 UsuarioWeb.fnGetPerfil(),
									 buscaAcesso(UsuarioWeb.fnGetAcesso()),
									 UsuarioWeb.fnGetSenha(),
									 UsuarioWeb.fnGetDicaSenha(),
									 UsuarioWeb.fnGetIDPerfil(),
									 UsuarioWeb.fnGetIDUsuario(),
									 UsuarioWeb.fnGetAcesso(),
									 UsuarioWeb.fnGetSequencia(),
									 UsuarioWeb.fnGetDataAcesso(),
									 "","","","","","","","","");
			Usuario.setNo(this.getNo());
			Usuario.setIDSessao(UsuarioWeb.fnGetIDSessao());
			Usuario.setIP(UsuarioWeb.fnGetIP());
			Usuario.setHost(UsuarioWeb.fnGetHost());
		}

		return Usuario;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3CB2E6980101
	 */
	public String liberaUsuarios()
	{
		String Ret = null;

		Ret = m_iUtil.fnLiberaUsuarios();

		if (Ret.length() == 0)
		{
			Ret = null;
		}

		return Ret;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3CB59C6703B8
	 */
	public String getQtdArqsIn()
	{
		String QtdArqsInStr = null;

		QtdArqsInStr = m_iUtil.fnGetQtdArqsIn();

		return QtdArqsInStr;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3CB5A638013B
	 */
	public String getHDTotal()
	{
		String HDStr = null;

		HDStr = m_iUtil.fnGetTotalDisco();

		return HDStr;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3CB5A65F00B5
	 */
	public String getHDLivre()
	{
		String HDStr = null;

		HDStr = m_iUtil.fnGetTotalLivre();

		return HDStr;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3CB5A66D0083
	 */
	public String getHDOcupado()
	{
		String HDStr = null;

		HDStr = m_iUtil.fnGetTotalUsado();

		return HDStr;
	}

	/**
	 * @param Tipo
	 * @return String
	 * @exception
	 * @roseuid 3CE4199300BB
	 */
	public String verificaBilhetadores(String Tipo)
	{
		//TODO
		String Retorno = "";

		if (Tipo.toLowerCase().equals("parser"))
		{
			Retorno = m_iUtil.fnVerificaParser();
		}
		else if (Tipo.toLowerCase().equals("conversor"))
		{
			Retorno = m_iUtil.fnVerificaConversor();
		}
		else if (Tipo.toLowerCase().equals("reprocessador"))
		{
			Retorno = m_iUtil.fnVerificaReprocessador();
		}
		else if (Tipo.toLowerCase().equals("parserGen"))
		{
			Retorno = m_iUtil.fnVerificaParserGen();
		}

		return Retorno;

	}

	/**
	 * @return String
	 * @exception
	 * Recupera a lista de datas de base de dados extra do servidor.
	 * @roseuid 3D19FFAE03A9
	 */
	public String getBDExtra()
	{
		return m_iUtil.fnGetBDExtra();
	}

	/**
	 * @param p_Datas
	 * @return String
	 * @exception
	 * Seta a lista de datas de base de dados extra no servidor.
	 * @roseuid 3D19FFE60074
	 */
	public String setBDExtra(String p_Datas)
	{
		String Resp = "";

		Resp = m_iUtil.fnSetBDExtra(p_Datas);

		return Resp;
	}
	
	/**
	 * @return
	 * Recupera uma lista de datas de reprocessamento do servidor
	 */
	public String getBDDataReprocessamento(){
		
		String data = new String();
		data = m_iUtil.fnGetBDDataReprocessamento();
		
		if(data == null){
			System.out.println("DATA VAZIA");
			return "";
		}
		else 
			System.out.println(data.length());
		
		System.out.println("DATA DO SERVIDOR ALTERADA: "+data);
		
		return data;
	}

	/**
	 * @param p_Datas
	 * @return resposta do servidor
	 * Retorna uma lista de datas de reprocessamentos armazenadas no servidor.
	 */
	public String setBDDataReprocessamento(String p_Datas)
	{

		if(m_iUtil.fnSetBDDataReprocessamento(p_Datas)){
			return "Data(s) salvas com sucesso!";
		}

		else
			return "Não foi possível salvar a(s) data(s)";
	}
	
	public String setDataReprocessamento(String data){
		
//		String resp = "";
//		
//		resp = "pegar datas";
		
		
		
		
		return m_iUtil.fnReprocessaID(data);
		
	}
	
	/** Método responsável por retornar os bilhetadores cadastrados para o cliente.
	 * @return String separando os bolhetadores por ";"
	 */
	public String getBilhetadores(){
		Vector lista = getListaBilhetadoresCfg();
		BilhetadorCfgDef bilhetador = null; 
		
		StringBuffer listaBilhetadores = new StringBuffer();
		
		
		
		for (int i = 0; i < lista.size(); i++) {
			bilhetador = (BilhetadorCfgDef)lista.get(i);
			listaBilhetadores.append(bilhetador.getBilhetador()+";");
		}
		
		listaBilhetadores.deleteCharAt(listaBilhetadores.lastIndexOf(";"));
		

		return listaBilhetadores.toString();
		
	}
	
	/**
	 * @param p_Tipo
	 * @return iArqView
	 * @exception
	 * p_Tipo
	 * 0 - Agenda
	 * 1 - Historico
	 * @roseuid 3D597AC203E3
	 */
	public iArqView getStatusAgenda(short p_Tipo)
	{
		switch (p_Tipo)
		{
			case 0:

				/*m_Arq = */ return m_iUtil.fnGetEstadoAgenda();

			//break;
			case 1:

				/*m_Arq = */ return m_iUtil.fnGetEstadoHistorico();

			//break;
			default:
				return null;
		}

		/*
		      if (m_Arq != null)
		         return true;
		      else
		         return false;
		*/
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3D6954B90215
	 */
	public String getTiposCham()
	{
		String TipoCham = "";
		String TiposCham = "";
		boolean bRet = true;
		short i = 1;

		do
		{
			TipoCham = m_iUtil.fnGetTipoChamada(i);

			if (TipoCham.length() == 0)
			{
				bRet = false;
			}
			else
			{
				TiposCham += (TipoCham + ";");
			}

			i++;
		}
		while (bRet);

		if (TiposCham.length() == 0)
		{
			TiposCham = "N/A";
		}
		else
		{
			TiposCham = TiposCham.substring(0, TiposCham.length() - 1);
		}

		return TiposCham;
	}

	/**
	 * @param p_Perfil
	 * @param p_Relatorio
	 * @param p_IDRelatorio
	 * @param p_TipoAgnHis
	 * @param p_TipoRelatorio
	 * @return void
	 * @exception
	 * @roseuid 3D78F6960003
	 */
	public void apagaRelAgendados(String p_Perfil,
								  String p_Relatorio,
								  String p_IDRelatorio,
								  short p_TipoAgnHis,
								  String p_TipoRelatorio)
	{
		m_iUtil.fnDelRelArmazenado(p_Perfil,
								   p_Relatorio,
								   p_IDRelatorio,
								   p_TipoAgnHis,
								   Short.parseShort(p_TipoRelatorio));
	}

	/**
	 * @param p_Perfil
	 * @param p_Relatorio
	 * @param p_IDRelatorio
	 * @param p_TipoAgnHis
	 * @param p_TipoRelatorio
	 * @return void
	 * @exception
	 * @roseuid 3D7E46BF00D4
	 */
	public void apagaRelHistoricos(String p_Perfil,
								   String p_Relatorio,
								   String p_IDRelatorio,
								   short p_TipoAgnHis,
								   String p_TipoRelatorio)
	{
		m_iUtil.fnDelRelArmazenado(p_Perfil,
								   p_Relatorio,
								   p_IDRelatorio,
								   p_TipoAgnHis,
								   Short.parseShort(p_TipoRelatorio));
	}

	/**
	 * @param p_Bilhetador
	 * @param p_BilhetadorNovo
	 * @param p_Tecnologia
	 * @param p_Operadora
	 * @param p_Apelido
	 * @param p_Fase
	 * @return boolean
	 * @exception
	 * Altera um bilhetador na configuração do sistema passando Operadora como parametro.
	 * @roseuid 3D80D698004D
	 */
	public boolean alteraBilhetador(String p_Bilhetador,
									String p_BilhetadorNovo,
									String p_Tecnologia,
									String p_Operadora,
									String p_Apelido,
									String p_Fase)
	{
		iListaParserCfgs p_ListaParserCfgs = null;
		iListaConversorCfgs p_ListaConversorCfgs = null;
		iBilCfg p_BilCfg = null;
		iParserCfg p_ParserCfg = null;
		iConversorCfg p_ConversorCfg = null;

		p_ListaParserCfgs = m_iUtil.fnGetListaParserCfgs();
		p_ListaConversorCfgs = m_iUtil.fnGetListaConfiguracao();

		if (p_ListaParserCfgs == null)
		{
			return false;
		}

		if (p_ListaParserCfgs == null)
		{
			return false;
		}

		p_BilCfg = m_iUtil.fnGetListaBilhetadores().fnAlteraBilhetador(p_Bilhetador,
																	   p_BilhetadorNovo,
																	   p_Tecnologia,
																	   p_Operadora,
																	   p_Apelido,
																	   Short.parseShort(p_Fase));

		if (p_BilCfg != null)
		{
			p_ParserCfg = p_ListaParserCfgs.fnGetInicio();

			while (p_ParserCfg != null)
			{
				if (p_ParserCfg.fnRemoveBilhetador(p_Bilhetador))
				{
					p_ParserCfg.fnAdicionaBilhetador(p_BilhetadorNovo);
				}

				p_ParserCfg = p_ListaParserCfgs.fnGetProx2(p_ParserCfg);
			}

			p_ListaParserCfgs.fnGrava();

			p_ListaConversorCfgs = m_iUtil.fnGetListaConfiguracao();
			p_ConversorCfg = p_ListaConversorCfgs.fnGetInicio();

			while (p_ConversorCfg != null)
			{
				if (p_ConversorCfg.fnRemoveBilhetador(p_Bilhetador))
				{
					p_ConversorCfg.fnAdicionaBilhetador(p_BilhetadorNovo);
				}

				p_ConversorCfg = p_ListaConversorCfgs.fnGetProx2(p_ConversorCfg);
			}

			p_ListaConversorCfgs.fnGrava();

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param p_Tipo
	 * @return String
	 * @exception
	 * @roseuid 3DB02087023A
	 */
	public String startUpParsers(short p_Tipo)
	{
		String Resultado;

		Resultado = m_iUtil.fnStartUpParsers(p_Tipo);

		return Resultado;
	}
	
	/**
	 * Método que starta o parseGen
	 * 19/04/2006
	 * */
	public String startUpParsersGen()
	{
		String Resultado;

		Resultado = m_iUtil.fnStartUpParserGen();

		return Resultado;
	}

	/**
	 * @param p_Tipo
	 * @return String
	 * @exception
	 * @roseuid 3DB0234C02A8
	 */
	public String shutdownParsers(short p_Tipo)
	{
		String Resultado;

		Resultado = m_iUtil.fnShutDownParsers(p_Tipo);

		return Resultado;
	}
	
	/**
	 * Método que mata o parserGen
	 * 19/04/2006
	 * */
	public String shutdownParsersGen()
	{
		String Resultado;

		Resultado = m_iUtil.fnShutDownParserGen();

		return Resultado;
	}
	
	/**
	 * @param p_Tipo
	 * @return String
	 * @exception
	 * @roseuid 3DB02087023A
	 */
	public String startUpReproc()
	{
		String Resultado;

		Resultado = m_iUtil.fnStartUpReprocessadores();

		return Resultado;
	}

	/**
	 * @param p_Tipo
	 * @return String
	 * @exception
	 * @roseuid 3DB0234C02A8
	 */
	public String shutDownReproc()
	{
		String Resultado;

		Resultado = m_iUtil.fnShutDownReprocessadores();

		return Resultado;
	}

	/**
	 * @return String
	 * @exception
	 * @roseuid 3DD9484001F4
	 */
	public String getDataHoraAtual()
	{
		//String DataHora = m_iUtil.fnGetDataHoraAtual();
		//(DataHora);
		return m_iUtil.fnGetDataHoraAtual();

		//return "11/09/2001 09:00:00";
	}

	/**
	 * @return iIndicadores
	 * @exception
	 * @roseuid 3DE3704B01A3
	 */
	public iIndicadores getIndicadores()
	{
		return m_iUtil.fnGetIndicadores();
	}

	/**
	 * @return Vector
	 * @exception
	 * Retorna um vetor de Strings com os nomes das Operadoras
	 * @roseuid 3F81AA6E017B
	 */
	public Vector getListaOperadoras()
	{
		iOperadora operadoraServ = null;
		OperadoraCfgDef operadora = null;
		Vector listaOperadoras = null;

		operadoraServ = m_iListaOperadoras.fnGetInicio();

		while (operadoraServ != null)
		{
			operadora = new OperadoraCfgDef(operadoraServ.fnGetOperadora());

			if (listaOperadoras == null)
			{
				listaOperadoras = new Vector();
				listaOperadoras.addElement(operadora);
			}
			else
			{
				listaOperadoras.add(operadora);
			}

			operadoraServ = m_iListaOperadoras.fnGetProx(operadoraServ);
		}

		return listaOperadoras;
	}

	/**
	 * @param p_strChave
	 * @param p_strPermissoes
	 * @return boolean
	 * @exception
	 * @roseuid 3FBE3D0A0216
	 */
	public boolean atualizaPermissao(String p_strChave,
									 String p_strPermissoes)
	{
		String LinhaPermissao = new String();

		LinhaPermissao += p_strChave;
		LinhaPermissao += ":";

		StringTokenizer st = new StringTokenizer(p_strPermissoes, ",");

		while (st.hasMoreTokens())
		{
			LinhaPermissao += (String) st.nextElement();
			LinhaPermissao += ";";
		}

		// retirando o ultimo ';'
		LinhaPermissao = LinhaPermissao.substring(0, LinhaPermissao.length() -
												  1);

		m_iUtil.fnAtualizaListaPermissoes(LinhaPermissao);

		return true;
	}

	
	public boolean atualizaPermissaoBaseExportada(String p_strChave,
			 String p_strPermissoes)
	{
		String LinhaPermissao = new String();
		
		LinhaPermissao += p_strChave;
		LinhaPermissao += ":";
		
		StringTokenizer st = new StringTokenizer(p_strPermissoes, ",");
		
		while (st.hasMoreTokens())
		{
		LinhaPermissao += (String) st.nextElement();
		LinhaPermissao += ";";
		}
		
		// retirando o ultimo ';'
		LinhaPermissao = LinhaPermissao.substring(0, LinhaPermissao.length() -
								  1);
		
		m_iUtil.fnAtualizaListaPermissoesExp(LinhaPermissao);
		
		return true;
	}
	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3FC36D43026D
	 */
	public Vector getListaTipoRelatorios()
	{
		Vector ListaTipoRel = new Vector();
		String Lista = null;

		Lista = m_iUtil.fnGetListaTipoRel();

		StringTokenizer st = new StringTokenizer(Lista, "@");

		while (st.hasMoreTokens())
		{
			ListaTipoRel.addElement((String) st.nextElement());
		}

		return ListaTipoRel;
	}
	
	/**
	 * @return Vector
	 *
	 */
	public Vector getListaTipoRelatoriosPerfil(String Perfil)
	{
		Vector ListaTipoRel = new Vector();
		String Lista = null;

		Lista = m_iUtil.fnGetListaTipoRelPorPerfil(Perfil);

		StringTokenizer st = new StringTokenizer(Lista, "@");

		while (st.hasMoreTokens())
		{
			ListaTipoRel.addElement((String) st.nextElement());
		}

		return ListaTipoRel;
	}
	
	/**
	 * @param p_IdPerfilLogado
	 * @param p_TiposRel
	 * @param p_Perfis
	 * @param p_NomeRel
	 * @param p_DataGeracao
	 * @param p_DataColeta
	 * @param p_PeriodoColeta
	 * @param p_Bilhetadores
	 * @param p_Origens
	 * @param p_Destinos
	 * @param p_RotasEnt
	 * @param p_RotasSai
	 * @return Vector
	 * @exception
	 * Faz a pesquisa avançada nos relatorios armazenados.
	 * Retorna um vector de relatorios
	 * @roseuid 3FE6ED4C0275
	 */
	public Vector pesquisaRelAvancada(String p_IdPerfilLogado,
									  String p_TiposRel,
									  String p_Perfis,
									  String p_NomeRel,
									  String p_DataGeracao,
									  String p_DataColeta,
									  String p_PeriodoColeta,
									  String p_Bilhetadores,
									  String p_Origens,
									  String p_Destinos,
									  String p_RotasEnt,
									  String p_RotasSai)
	{
		String Relatorios = null;
		Vector Linhas = null;
		Vector Coluna = null;
		StringTokenizer stLinha = null;
		StringTokenizer stColuna = null;

		/************ Formato das Colunas ************
		      pos(0): (String)NomeRel
		      pos(1): (String)TipoRel
		      pos(2): (String)IdRel
		      pos(3): (String)Perfil
		      pos(4): (String)ArqRelatorio
		      pos(5): (String)DataGeracao
		      pos(6): (String)TipoArmazenamento
		***********************************************/
		Relatorios = m_iUtil.fnPesquisaRelAvancada(p_IdPerfilLogado,
												   p_TiposRel, p_Perfis,
												   p_NomeRel, p_DataGeracao,
												   p_DataColeta,
												   p_PeriodoColeta,
												   p_Bilhetadores, p_Origens,
												   p_Destinos, p_RotasEnt,
												   p_RotasSai);

		Linhas = new Vector();

		stLinha = new StringTokenizer(Relatorios, ";");

		while (stLinha.hasMoreTokens())
		{
			Coluna = new Vector();
			stColuna = new StringTokenizer((String) stLinha.nextElement(), "@");

			while (stColuna.hasMoreTokens())
			{
				Coluna.add(stColuna.nextElement());
			}

			Linhas.add(Coluna);
		}

		return Linhas;
	}

	/**
	 * @param p_IdPerfilLogado
	 * @param p_TiposRel
	 * @param p_Perfis
	 * @param p_NomeRel
	 * @return Vector
	 * @exception
	 * Faz a pesquisa simples nos relatorios armazenados.
	 * Retorna um vector de relatorios
	 * @roseuid 3FE6ED600165
	 */
	public Vector pesquisaRel(String p_IdPerfilLogado,
							  String p_TiposRel,
							  String p_Perfis,
							  String p_NomeRel)
	{
		String Relatorios = null;
		Vector Linhas = null;
		Vector Coluna = null;
		StringTokenizer stLinha = null;
		StringTokenizer stColuna = null;

		/************ Formato das Colunas ************
		      pos(0): (String)NomeRel
		      pos(1): (String)TipoRel
		      pos(2): (String)IdRel
		      pos(3): (String)Perfil
		      pos(4): (String)ArqRelatorio
		      pos(5): (String)DataGeracao
		      pos(6): (String)TipoArmazenamento
		***********************************************/
		// Falta fazer a parte do servutil
		Relatorios = m_iUtil.fnPesquisaRel(p_IdPerfilLogado, p_TiposRel,
										   p_Perfis, p_NomeRel);

		// Teste
		/*
		Relatorios = "Desempenho@0@0@osx@1071236308-0.txt@12/12/2003 10:38:07@0;" +
		             "Novo@2@1@osx@1071236481-1.txt@12/12/2003 10:41:09@0;" +
		             "Des 1 Claro OK@0@1@osx@1068203420-1.txt@07/11/2003 08:07:41@1" ;
		*/
		Linhas = new Vector();

		stLinha = new StringTokenizer(Relatorios, ";");

		while (stLinha.hasMoreTokens())
		{
			Coluna = new Vector();
			stColuna = new StringTokenizer((String) stLinha.nextElement(), "@");

			while (stColuna.hasMoreTokens())
			{
				Coluna.add(stColuna.nextElement());
			}

			Linhas.add(Coluna);
		}

		return Linhas;
	}

	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_DataGeracao
	 * @param p_Arquivo
	 * @param p_Relatorio
	 * @return String
	 * @exception
	 * Verifica se o nome do arquivo que está sendo atualizado constantemente no Portal foi alterado. Retorno o novo nome do mesmo ou null.
	 * @roseuid 403519BA03CC
	 */
	public String verificaAlteracaoDeNomeCorba(short p_TipoArmazenado,
											   String p_Perfil,
											   int p_TipoRel,
											   String p_DataGeracao,
											   String p_Arquivo,
											   String p_Relatorio)
	{
		String NomeDoArquivo = "";

		iListaRelAgendaHistorico ListaRelatorios = null;
		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			try
			{
				NomeDoArquivo = ListaRelatorios.fnVerificaAlteracaoDeNomeCorba((short) p_TipoArmazenado,
																			   (int) p_TipoRel,
																			   p_Perfil,
																			   p_DataGeracao,
																			   p_Arquivo,
																			   p_Relatorio);
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		ListaRelatorios.fnDestroy();

		return NomeDoArquivo;
	}

	/**
	 * @param p_TipoArmazenado
	 * @param p_Perfil
	 * @param p_TipoRel
	 * @param p_DataGeracao
	 * @param p_Relatorio
	 * @return String
	 * @exception
	 * Verifica se o diretorio de armazenamento do arquivo que está sendo atualizado constantemente no Portal foi alterado. Retorno o novo nome do mesmo ou null.
	 * @roseuid 403CF798019B
	 */
	public String verificaAlteracaoDeDiretorioCorba(short p_TipoArmazenado,
													String p_Perfil,
													int p_TipoRel,
													String p_DataGeracao,
													String p_Relatorio)
	{
		String NomeDoDiretorio = "";

		iListaRelAgendaHistorico ListaRelatorios = null;
		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		if (ListaRelatorios != null)
		{
			try
			{
				NomeDoDiretorio = ListaRelatorios.fnVerificaAlteracaoDeDiretorioCorba((short) p_TipoArmazenado,
																					  (int) p_TipoRel,
																					  p_Perfil,
																					  p_DataGeracao,
																					  p_Relatorio);
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		ListaRelatorios.fnDestroy();

		return NomeDoDiretorio;
	}

	/**
	 * @return iRetRelatorio
	 * @exception
	 * Metodo que retorna um objeto iRetRelatorio para posterior manipulação pela Agenda.
	 * @roseuid 4124E52D0287
	 */
	public iListaRelAgendaHistorico getRelatorio3(short p_TipoArmazenado,
												  String p_Perfil,
												  int p_TipoRel,
												  int p_IdRel,
												  String p_DataGeracao,
												  String p_Arquivo,
												  String p_Relatorio)
	{
		iListaRelAgendaHistorico ListaRelatorios = null;
		ListaRelatorios = m_iUtil.fnCriaListaRelatorios();

		return ListaRelatorios;
	}

	public void limpaArquivosLock()
	{
	    m_iUtil.fnApagaArquivosLock();
	}

	/**
	 * Metodo que verifica se o servutil trata a conf de conversor passado(s)
	 * como parametro.
	 * @param configuracaoConversor - String que contem o nome da configuracao do conversor
	 *
	 * */
	public boolean trataConversor(String configuracaoConversor)
	{
	    iConversorCfg conversor = m_iUtil.fnGetListaConfiguracao().fnConversorCfgExiste(configuracaoConversor);

	    return (conversor != null) ? true : false;
	}
	
	/**
	 * Metodo que verifica se o servutil trata a conf de reprocessador passado(s)
	 * como parametro.
	 * @param configuracaoConversor - String que contem o nome da configuracao do conversor
	 *
	 * */
	public boolean trataReprocessador(String configuracaoReprocessador)
	{
		//TODO
	    iReprocessadorCfg reprocessador = m_iUtil.fnGetListaReprocCfgs().fnReprocCfgExiste(configuracaoReprocessador);

	    return (reprocessador != null) ? true : false;
	}

	/**
	 * Metodo que verifica se o servutil trata a conf de parsergen passado(s)
	 * como parametro.
	 * @param configuracaoConversor - String que contem o nome da configuracao do conversor
	 *
	 * */
	public boolean trataParserGen(String configuracaoParserGen)
	{
		//TODO
	    iParserGenCfg parsergen = m_iUtil.fnGetListaParserGenCfg().fnParserGenCfgExiste(configuracaoParserGen);

	    return (parsergen != null) ? true : false;
	}

	/**
	 * Metodo que verifica se o servutil trata o bilhetador passado
	 * como parametro.
	 * @param bilhetador - String que contem o nome do bilhetador
	 *
	 * */
	public boolean trataBilhetador(String bilhetador)
	{
	    iBilCfg bilCfg = m_iListaBilhetadores.fnBilhetadorExiste(bilhetador);

	    return (bilCfg != null) ? true : false;
	}

	/**
	 * Metodo que verifica se o servutil trata o(s) perfil(is) passado(s)
	 * como parametro.
	 * @param perfil - String que pode contem varios IDs de perfis separados por ';'
	 *
	 * */
	public String trataListaPerfil(String p_Perfil)
	{
	    return m_iUtil.fnTrataPerfis(p_Perfil);
	}
	
	/**
	 * Metodo que verifica se o servutil trata o(s) usuario(s) passado(s)
	 * como parametro.
	 * @param perfil - String que pode contem varios IDs de usuarios separados por ';'
	 *
	 * */
	public String trataListaUsuario(String p_Usuario)
	{
	    return m_iUtil.fnTrataUsuarios(p_Usuario);
	}

	/**
	 * 
	 * @return
	 */
	public String getDocumentos(){
		return m_iUtil.fnGetDocumentos();
	}
	/**
	 * 
	 * @param tecnologia
	 * @return
	 */
	public String getBilhetadorTec(String tecnologia){
		return m_iUtil.fnGetBilhetadoresTec(tecnologia);
	}
	/**
	 * 
	 * @param documento
	 * @param central
	 * @return
	 */
	public String getCaminhoDocumento(String documento,String central){
		return m_iUtil.fnGetCaminhoDocumentos(documento,central);
	}
	
	public String getLinkDocumento(String caminhoDocumento){
		return m_iUtil.fnGetLinkDoc(caminhoDocumento);
	}
	
	public float getProgressaoHistorico(short p_TipoArmazenado,
									    int p_TipoRel,
										String p_Perfil,
										String p_DataGeracao,
										String p_Relatorio,
										int p_IdRel)
	{
	iListaRelAgendaHistorico ListaRelatorios = m_iUtil.fnCriaListaRelatorios();
	float relConcluido = ListaRelatorios.fnProgressaoRel(p_TipoArmazenado,
										  p_TipoRel,
										  p_Perfil,
										  p_DataGeracao,
										  p_Relatorio,
										  p_IdRel);
	
	return relConcluido;

	}
	
	public boolean trataPerfil(String p_Perfil)
	{
        iPerfil perfil = m_iListaPerfis.fnPerfilExiste(p_Perfil);

	    return (perfil != null) ? true : false;
	}

	public int getMaiorIdPerfil()
	{
		iPerfil perfil = m_iListaPerfis.fnGetFim((short)0);

		if(perfil != null)
			return perfil.fnGetId();

		return -1;
	}

    public iArqView getM_Arq()
    {
        return m_Arq;
    }
    public void setM_Arq(iArqView arq)
    {
        m_Arq = arq;
    }
    public iListaAcesso getM_iListaAcesso()
    {
        return m_iListaAcesso;
    }
    public void setM_iListaAcesso(iListaAcesso listaAcesso)
    {
        m_iListaAcesso = listaAcesso;
    }
    public iListaBilhetadores getM_iListaBilhetadores()
    {
        return m_iListaBilhetadores;
    }
    public void setM_iListaBilhetadores(iListaBilhetadores listaBilhetadores)
    {
        m_iListaBilhetadores = listaBilhetadores;
    }
    public iListaOperadoras getM_iListaOperadoras()
    {
        return m_iListaOperadoras;
    }
    public void setM_iListaOperadoras(iListaOperadoras listaOperadoras)
    {
        m_iListaOperadoras = listaOperadoras;
    }
    public iListaPerfis getM_iListaPerfis()
    {
        return m_iListaPerfis;
    }
    public void setM_iListaPerfis(iListaPerfis listaPerfis)
    {
        m_iListaPerfis = listaPerfis;
    }
    public iListaRelAgendaHistorico getM_iListaRelAgendaHistorico()
    {
        return m_iListaRelAgendaHistorico;
    }
    public void setM_iListaRelAgendaHistorico(iListaRelAgendaHistorico listaRelAgendaHistorico)
    {
        m_iListaRelAgendaHistorico = listaRelAgendaHistorico;
    }
    public iListaTecnologias getM_iListaTecnologias()
    {
        return m_iListaTecnologias;
    }
    public void setM_iListaTecnologias(iListaTecnologias listaTecnologias)
    {
        m_iListaTecnologias = listaTecnologias;
    }
    public iListaUsuarios getM_iListaUsuarios()
    {
        return m_iListaUsuarios;
    }
    public void setM_iListaUsuarios(iListaUsuarios listaUsuarios)
    {
        m_iListaUsuarios = listaUsuarios;
    }
    public iListaUsuariosWeb getM_iListaUsuarioWeb()
    {
        return m_iListaUsuarioWeb;
    }
    public void setM_iListaUsuarioWeb(iListaUsuariosWeb listaUsuarioWeb)
    {
        m_iListaUsuarioWeb = listaUsuarioWeb;
    }
    public iUtil getM_iUtil()
    {
        return m_iUtil;
    }
    public void setM_iUtil(iUtil util)
    {
        m_iUtil = util;
    }
    public iAgenda getM_iAgenda()
    {
        return m_iAgenda;
    }
    public void setM_iAgenda(iAgenda agenda)
    {
        m_iAgenda = agenda;
    }
    public iLogView getM_Log()
    {
        return m_Log;
    }
    public void setM_Log(iLogView log)
    {
        m_Log = log;
    }
    
	public String geraListaDiasBDCDR()
	{
		String Retorno = null;
		Retorno = m_iUtil.fnGeraListaDiasBDCDR();

		return Retorno;
	}
	
	public String geraListaDiasBDCDRBil(String nomeBilhetador)
	{
		String Retorno = null;
		Retorno = m_iUtil.fnGeraListaDiasBDCDRBil(nomeBilhetador);

		return Retorno;
	}
	
	public String getTecnologias(){
		String retorno = null;
		retorno = m_iUtil.fnGetTecnologias();
		return retorno;
	}
	
	/**
	 * Novo método que altera o perfil
	 * */
	public boolean alteraPerfil(String p_Perfil,String p_Acesso, boolean sigiloTelefonico, String ids, boolean todasTecnologias, String idsRel, boolean todosRelatorios, String idsRelHistorico, boolean todosRelHistorico)
	{
		iPerfil PerfilServ = null;
		iAcesso AcessoServ;
		short IdAcesso = -1;

		//System.out.println(p_Perfil+" - "+p_Acesso);
		if (m_iListaAcesso != null)
		{
			AcessoServ = m_iListaAcesso.fnGetInicio();

			while (AcessoServ != null)
			{
				if (AcessoServ.fnGetAcessoNome().equals(p_Acesso))
				{
					IdAcesso = AcessoServ.fnGetAcessoId();

					break;
				}

				AcessoServ = m_iListaAcesso.fnGetProx();
			}
		}

		if (IdAcesso == -1)
		{
			return false;
		}

		if (m_iListaPerfis != null)
		{
			PerfilServ = m_iListaPerfis.fnAlteraPerfil(p_Perfil, IdAcesso, sigiloTelefonico, ids, todasTecnologias, idsRel, todosRelatorios, idsRelHistorico, todosRelHistorico);//sigilo, ids, todas ou nao
			
			if (PerfilServ == null)
			{
				return false;
			}
		}

		return true;
	}
	
	public void escreveUsuarioInvalido(String nomeUsr, String ip)
	{
		m_iUtil.fnEscreveUsuarioInvalido(nomeUsr, ip);
	}
	
	/**
	 * Método que envia mensagem sms
	 * @param telefones, lista de telefones separado por ";"
	 * @param mensagem, mensagem para ser enviada
	 * @return true se enviar ocm sucesso, false se der erro
	 */
	public boolean enviaMensagemSMS(String telefones, String mensagem){
		try{
			m_iUtil.fnEnviaMensagem(telefones,mensagem);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	/**
	 * Método para verificar se o usuário vai ser monitorado
	 * @param usuario
	 * @return true, se ele for monitorado e false se não
	 */
	public boolean verificaUsuMonitorado(String usuario){
		boolean monitorado = false;
		try{
			monitorado = m_iUtil.fnVerificaUsuarioMonitorado(usuario);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return monitorado;
	}
	
	/**
	 * Verifica Login mais nao loga o usuario
	 * @param usuario
	 * @param senha
	 * @return true se o usuario e senha forem validos
	 */
	public boolean verificaLogon(String usuario, String senha){
		boolean sucesso = false;
		try{
			InetAddress localaddr = InetAddress.getLocalHost();
			sucesso = m_iUtil.fnVerificaLogin(usuario,senha,localaddr.getHostAddress());
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return sucesso;
	}
	
	private String getDataHoraInicio(Date horarioDeInicio) {
		
		   GregorianCalendar gc = new GregorianCalendar();
		   gc.setTime(horarioDeInicio);
		   
		   int dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
		   int mes = gc.get(GregorianCalendar.MONTH);
		   int ano = gc.get(GregorianCalendar.YEAR);
		   int hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
		   int minuto = gc.get(GregorianCalendar.MINUTE);
		   int segundo = gc.get(GregorianCalendar.SECOND);
		   
		   String resposta = "";
		   
		   if(dia < 10)
			   resposta += "0"+dia+"/";
		   else
			   resposta += dia+"/"; 
		   
		   if(mes < 10)
			   resposta += "0"+mes+"/";
		   else
			   resposta += mes+"/";
		   
		   resposta += ano+" ";
		   
		   if(hora < 10)
			   resposta += "0"+hora+":";  
		   else
			   resposta += hora+":";  
		   
		   if(minuto < 10)
			   resposta += "0"+minuto+":";  
		   else
			   resposta += minuto+":";  
		   
		   if(segundo < 10)
			   resposta += "0"+segundo;
		   else
			   resposta += segundo;  
		   
		   
		   return resposta;
	   }
}
