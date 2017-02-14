package Portal.Operacoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import uk.ltd.getahead.dwr.WebContextFactory;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ErroUtil;
import Portal.Utils.Ordena;
import Portal.Utils.UsuarioDef;
import br.com.visent.segurancaAcesso.jasper.ListaUsuariosJasper;
import br.com.visent.segurancaAcesso.util.DataUtil;

public class OpUsuariosAjax
{

	private ArrayList listaUsuarios;
	private ArrayList listaPerfis;
	private ArrayList listaRegionais;
	private ListaUsuariosJasper jasper;
	private ArrayList listaUsuariosFiltros;

	/**
	 * Carregar as listas de usuarios e perfis
	 * @throws ErroUtil
	 */
	public void iniciar() throws ErroUtil {
		buscarUsuarios();
		buscarPerfis();
		buscaRegionais();
	}

	/**
	 * Carregar a lista de usuarios
	 * @throws ErroUtil
	 */
	public void iniciarUsuarios() throws ErroUtil {
		buscarUsuarios();
	}

	/**
	 * Carregar a lista de perfis
	 * @throws ErroUtil
	 */
	public void iniciarPerfis() throws ErroUtil {
		buscarPerfis();
	}
	
	/**
	 * Carregar a lista de regionais
	 * @throws ErroUtil
	 */
	public void iniciarRegionais() throws ErroUtil {
		buscaRegionais();
	}

	/**
	 * Metodo que busca no servutil a lista de usuarios
	 * @return lista
	 * @throws ErroUtil
	 */
	private ArrayList buscarUsuarios() throws ErroUtil {
		listaUsuarios = new ArrayList();
		No noTmp = null;
		Vector vectorTmp = null;
		for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();) {
			noTmp = (No) iter.next();
			if (!verificaServUtil(noTmp)) {
				throw new ErroUtil("Problema no ServUtil da maquina "
						+ noTmp.getHostName());
			}
			try {
				vectorTmp = noTmp.getConexaoServUtil().getListaUsuariosCfg();
				if (vectorTmp != null)
					listaUsuarios.addAll(vectorTmp);
			} catch (COMM_FAILURE comFail) {
				throw new ErroUtil("Problema no ServUtil da maquina "
						+ noTmp.getHostName());
			} catch (BAD_OPERATION badOp) {
				System.out.println(new Date()
						+ " - METODO NAO EXISTENTE NO SERVIDOR UTIL ("
						+ noTmp.getHostName() + ").");
				badOp.printStackTrace();
			}
		}
		return listaUsuarios;
	}

	/**
	 * Metodo que busca lista de perfis no servutil
	 * @return lista
	 * @throws ErroUtil
	 */
	private ArrayList buscarPerfis() throws ErroUtil {
		listaPerfis = new ArrayList();
		No noTmp = null;
		Vector vectorTmp = null;
		for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();) {
			noTmp = (No) iter.next();
			if (!verificaServUtil(noTmp)) {
				throw new ErroUtil("Problema no ServUtil da maquina "+ noTmp.getHostName());
			}
			try {
				vectorTmp = noTmp.getConexaoServUtil().getListaPerfisCfg();
				if (vectorTmp != null)
					listaPerfis.addAll(vectorTmp);
			} catch (COMM_FAILURE comFail) {
				throw new ErroUtil("Problema no ServUtil da maquina "
						+ noTmp.getHostName());
			} catch (BAD_OPERATION badOp) {
				System.out.println(new Date()
						+ " - METODO NAO EXISTENTE NO SERVIDOR UTIL ("
						+ noTmp.getHostName() + ").");
				badOp.printStackTrace();
			}
		}
		return listaPerfis;
	}
	
	/**
	 * Busca lista de regionais no servutil
	 * @return lista de regionais
	 * @throws ErroUtil
	 */
	private ArrayList buscaRegionais() throws ErroUtil{
		listaRegionais = new ArrayList();
		No no = NoUtil.getNoCentral();
		Vector vectorTmp = null;
		if (!verificaServUtil(no)) {
			throw new ErroUtil("Problema no ServUtil da maquina "+ no.getHostName());
		}
		try {
			vectorTmp = no.getConexaoServUtil().getListaOperadoras();
			if (vectorTmp != null)
				listaRegionais.addAll(vectorTmp);
		} catch (COMM_FAILURE comFail) {
			throw new ErroUtil("Problema no ServUtil da maquina "
					+ no.getHostName());
		} catch (BAD_OPERATION badOp) {
			System.out.println(new Date()
					+ " - METODO NAO EXISTENTE NO SERVIDOR UTIL ("
					+ no.getHostName() + ").");
			badOp.printStackTrace();
		}
		return listaRegionais;
	}

	/**
	 * Salvar usuario
	 * @param usuario
	 * @param senha
	 * @param perfil
	 * @param nome
	 * @param email
	 * @param ramal
	 * @param telefone
	 * @param regional
	 * @param area
	 * @param responsavel
	 * @param motivo
	 * @param ativacao
	 * @param novo
	 * @return 0 - sucesso, 1 - problema ao salvar
	 * @throws ErroUtil
	 */
	public int salvar(String usuario, String senha, String perfil, String nome,
			String email, String ramal, String telefone, String regional,
			String area, String responsavel, String motivo, String ativacao, String ordemServico,
			boolean novo) throws ErroUtil {

		No no = NoUtil.buscaNobyNomePerfil(perfil);
		if (!verificaServUtil(no)) {
			throw new ErroUtil("Problema no ServUtil da maquina "+ no.getHostName());
		}

		try {
			if (novo) {
				
				if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro")){
					Date dataAtual = new Date();
					String dataStr = DataUtil.getDataFormatada(dataAtual,"dd/MM/yyyy HH:mm:ss");

					return no.getConexaoServUtil().incluiUsuario(usuario, senha,
							perfil, usuario, nome, email, ramal, telefone,
							regional, area, responsavel, motivo, dataStr, "", ordemServico);
				}else{
					Date dataAtual = new Date();
					String dataStr = DataUtil.getDataFormatada(dataAtual,"dd/MM/yyyy HH:mm:ss");

					return no.getConexaoServUtil().incluiUsuario(usuario, senha,
							perfil, usuario, nome, email, ramal, telefone,
							regional, area, responsavel, motivo, dataStr, "", "");
				}
			}else{
				if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro")){
					no.getConexaoServUtil().editaUsuario(usuario, senha, perfil,
							usuario, nome, email, ramal, telefone, regional, area,
							responsavel, motivo, ativacao, "", ordemServico);
					return 0;
				}else{
					no.getConexaoServUtil().editaUsuario(usuario, senha, perfil,
							usuario, nome, email, ramal, telefone, regional, area,
							responsavel, motivo, ativacao, "", "");
					return 0;
				}	
			}	
			
		} catch (Exception e) {
			System.out.println("OpUsuariosAjax - salvar(): " + e);
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * reinicia senha do usuário
	 * @param nomeUsuario
	 * @return true caso senha reiniciada
	 * @throws ErroUtil
	 */
	public boolean reiniciaSenha(String nomeUsuario) throws ErroUtil {
		
		No no = NoUtil.buscaNobyNomeUsuario(nomeUsuario);
		
		if (!verificaServUtil(no)) {
			throw new ErroUtil("Problema no ServUtil da maquina "+ no.getHostName());
		}
		try {
			return no.getConexaoServUtil().reiniciaSenha(nomeUsuario);
			
		}catch (COMM_FAILURE comFail) {
			throw new ErroUtil("Problema no ServUtil da maquina "
					+ no.getHostName());
		} catch (BAD_OPERATION badOp) {
			System.out.println(new Date()
					+ " - METODO NAO EXISTENTE NO SERVIDOR UTIL ("
					+ no.getHostName() + ").");
				badOp.printStackTrace();
		}
		return false;
	}

	/**
	 * Excluis usuario
	 * @param nomeUsuario
	 * @return true para sucesso
	 */
	public boolean excluir(String nomeUsuario) {
		try {
			No noTmp = null;
			UsuarioDef usuario = null;

			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();) {
				try {
					noTmp = (No) iter.next();
					if (!verificaServUtil(noTmp)) {
						throw new ErroUtil("Problema no ServUtil da maquina "
								+ noTmp.getHostName());
					}
					usuario = noTmp.getConexaoServUtil()
							.getUsuario(nomeUsuario);

					if (usuario != null) {
						if (noTmp.getConexaoServUtil().excluiUsuario(
								nomeUsuario))
							return true;
						else
							return false;
					}
				} catch (COMM_FAILURE comFail) {
					throw new ErroUtil("Problema no ServUtil da maquina "
							+ noTmp.getHostName());
				} catch (BAD_OPERATION badOp) {
					System.out.println(new Date()
							+ " - METODO NAO EXISTENTE NO SERVIDOR UTIL ("
							+ noTmp.getHostName() + ").");
					badOp.printStackTrace();
				}
			}
		} catch (Exception Exc) {
			System.out.println("OpUsuariosAjax - excluir(): " + Exc);
			Exc.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Lista perfis
	 * @return perfis
	 */
	public ArrayList getListaPerfis() {
		return listaPerfis;
	}

	/**
	 * Lista usuarios
	 * @return usuarios
	 */
	public ArrayList getListaUsuarios() {
		return listaUsuarios;
	}
	
	/**
	 * Lista regionais
	 * @return regionais
	 */
	public ArrayList getListaRegionais() {
		return listaRegionais;
	}

	/**
	 * Metodo para listar os usuarios com filtros e gerar o relatorio jasper
	 * @param filtros
	 * @return true para sucesso
	 * @throws ErroUtil
	 */
	public boolean executarListaUsuarios(String filtros) throws ErroUtil {
		No noTmp = null;
		ArrayList listaTmp = null;
		this.listaUsuariosFiltros = new ArrayList();
		for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();) {
			noTmp = (No) iter.next();
			if (!verificaServUtil(noTmp)) {
				throw new ErroUtil("Problema no ServUtil da maquina "
						+ noTmp.getHostName());
			}
			try {
				listaTmp = noTmp.getConexaoServUtil().getListaUsuariosFiltros(
						filtros);
				if (listaTmp != null)
					this.listaUsuariosFiltros.addAll(listaTmp);
			} catch (COMM_FAILURE comFail) {
				throw new ErroUtil("Problema no ServUtil da maquina "
						+ noTmp.getHostName());
			} catch (BAD_OPERATION badOp) {
				System.out.println(new Date()
						+ " - METODO NAO EXISTENTE NO SERVIDOR UTIL ("
						+ noTmp.getHostName() + ").");
				badOp.printStackTrace();
			}
		}
		jasper = new ListaUsuariosJasper(this.listaUsuariosFiltros);
		if(this.listaUsuariosFiltros.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Metodo para ordenação
	 * @param coluna atributo da classe para ordenar
	 * @param tipo tipo do atributo
	 * @param reverse ordenação reversa
	 * @return
	 */
	public boolean ordenar(String coluna, boolean reverse){
		Ordena ordena = new Ordena("Portal.Utils.UsuarioDef",coluna);
		Collections.sort(listaUsuariosFiltros,ordena);
		if(reverse){
			Collections.reverse(listaUsuariosFiltros);
		}		
		jasper = new ListaUsuariosJasper(listaUsuariosFiltros);
		return true;
	}

	/**
	 * Retorna numero de paginas do relatorio de usuarios com filtros
	 * @return
	 */
	public int getNumPaginasRelUsu() {
		return jasper.getNumeroPaginas();
	}

	/**
	 * Retorna o html da lista de usuarios filtrada
	 * @param pg
	 * @return
	 */
	public String getRelatorioUsuarios(int pg) {
		String html = jasper.relatorioHTML(pg);
		if(html == null){
			html = "";
		}
		return html;
	}

	/**
	 * Exportar
	 * @param exportacao
	 */
	public void exportar(int exportacao) {
		HttpSession sess = WebContextFactory.get().getSession();
		sess.setAttribute("jasperPrint", jasper.getJasperPrint());
		sess.setAttribute("exportacao", exportacao + "");
	}

	/**
	 * Verifica servutil
	 * @return true, se estiver ok, false se der problema
	 */
	private boolean verificaServUtil(No no) {
		if (no.getConexaoServUtil().getM_iUtil() == null) {
			no.createConexao(DefsComum.s_ServUtil, no.getConexaoServUtil()
					.getModoConexao(), no.getHostName(), no
					.getConexaoServUtil().getNomeObjetoCorba(), no
					.getConexaoServUtil().getPorta());
			if (no.getConexaoServUtil().getM_iUtil() == null) {
				return false;
			} else {
				no.setConexaoServUtil(no.getConexaoServUtil());
				return true;
			}
		} else {
			return true;
		}
	}

}
