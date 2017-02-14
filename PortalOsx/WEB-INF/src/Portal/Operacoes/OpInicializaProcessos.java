//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpFinalizaProcessos.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.Mensagem;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;

/**
 */
public class OpInicializaProcessos extends OperacaoAbs {

	static {
	}

	/**
	 * @return @exception
	 * @roseuid 3C45B9C300CC
	 */
	public OpInicializaProcessos() {
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3C45B9C30144
	 */
	public boolean iniciaOperacao(String p_Mensagem) {
		String todosParsers = null, Remetente = null;
		UsuarioDef Usuario;
		Vector ListaUsuarios = null;
		String iniciaOperacao = "";
		String Resultado = "";
		try {
			setOperacao("Finalização de Processos");
			String Operacao = m_Request.getParameter("tipoProcesso");
			String Processos = m_Request.getParameter("processos");
			String[] arrayProcessos = null;
			if (Processos.indexOf(";") != -1) {
				arrayProcessos = Processos.split(";");
			} else//se não tem ; é porque é só um elemento
			{
				arrayProcessos = new String[1];
				arrayProcessos[0] = Processos;
			}
			
			if (Operacao.equalsIgnoreCase("inicializaparser"))//inicializa parsers
			{
				Operacao = "Inicia";
				for (int i = 0; i < arrayProcessos.length; i++) {
					try {
						setOperacao("Inicialização de Parsers");

						// Recupera os parâmetros
						Resultado += NoUtil.getNoCentral().getConexaoServUtil().controlaParser(Operacao,
								arrayProcessos[i]);
						iniciaOperacao = "parser";
					} catch (Exception Exc) {
						System.out
								.println("OpControlaConversores - iniciaOperacao(): "
										+ Exc);
						Exc.printStackTrace();
						return false;
					}
				}
			}
			else if (Operacao.equalsIgnoreCase("inicializaparsergen"))//inicializa parsers
			{
				Operacao = "Inicia";
				for (int i = 0; i < arrayProcessos.length; i++) {
					try {
						setOperacao("Inicialização de ParsersGen");

						// Recupera os parâmetros
						No no = NoUtil.buscaNobyCfgParserGen(arrayProcessos[i]);
						if(no == null){
				         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Reprocessador " + arrayProcessos[i]);
				        }
						Resultado += no.getConexaoServUtil().controlaParserGen(Operacao,
								arrayProcessos[i]);
						iniciaOperacao = "parsergen";
					} catch (Exception Exc) {
						System.out
								.println("OpControlaConversores - iniciaOperacao(): "
										+ Exc);
						Exc.printStackTrace();
						return false;
					}
				}
			}
			else if (Operacao.equalsIgnoreCase("inicializareprocessador"))
			{
				Operacao = "Inicia";
				for (int i = 0; i < arrayProcessos.length; i++) {
					try {
						setOperacao("Inicialização de Reprocessadores");

						// Recupera os parâmetros
						No no = NoUtil.buscaNobyCfgReprocessador(arrayProcessos[i]);
						if(no == null){
				         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Reprocessador " + arrayProcessos[i]);
				        }
						Resultado += no.getConexaoServUtil().controlaReprocessador(Operacao, arrayProcessos[i]);
						iniciaOperacao = "reprocessador";
					} catch (Exception Exc) {
						System.out
								.println("OpControlaReproc - iniciaOperacao(): "
										+ Exc);
						Exc.printStackTrace();
						return false;
					}
				}
			}
			else
			{
				Operacao = "Inicia";
				for (int i = 0; i < arrayProcessos.length; i++) {
					try {
						setOperacao("Inicialização de Conversores");

						// Recupera os parâmetros
						No no = NoUtil.buscaNobyCfgConversor(arrayProcessos[i]);
						if(no == null){
				         	throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Configuracao de Conversor " + arrayProcessos[i]);
				        }
						Resultado += no.getConexaoServUtil().controlaConversor(Operacao, arrayProcessos[i]);
						iniciaOperacao = "conversor";
					} catch (Exception Exc) {
						System.out
								.println("OpControlaConversores - iniciaOperacao(): "
										+ Exc);
						Exc.printStackTrace();
						return false;
					}
				}

			}
			String Processo, Retorno = "processo(s) iniciado(s) com sucesso";
			OpListaProcessosParados ListaProcessos;
			OpListaProcessosCtrl ListaProcessosCtrl;
			// Recupera os parâmetros
			String Tipo = m_Request.getParameter("tipo");
			String Usuarios = m_Request.getParameter("usuarios");
			Vector ListaProc = VetorUtil.String2Vetor(Processos, ';');
			ListaUsuarios = VetorUtil.String2Vetor(Usuarios, ';');
			Thread.sleep(5000);
			if (Tipo.equals("util") == true) {
				String Tipo2 = m_Request.getParameter("tipo2");
				ListaProcessos = new OpListaProcessosParados();
				ListaProcessos.setRequestResponse(getRequest(), getResponse());
				ListaProcessos.iniciaOperacao(iniciaOperacao+";"+Resultado);
			} else if (Tipo.equals("ctrl") == true) {
				ListaProcessosCtrl = new OpListaProcessosCtrl();
				ListaProcessosCtrl.setRequestResponse(getRequest(), getResponse());
				ListaProcessosCtrl.iniciaOperacao(Resultado);
			}
		} catch (Exception Exc) {
			System.out
					.println("OpFinalizaProcessos - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param p_Remetente
	 * @param p_Usuario
	 * @return void
	 * @exception
	 * @roseuid 3DCC228200A5
	 */
	public void enviaMensagem(String p_Remetente, String p_Usuario) 
	{
	   No noTmp = NoUtil.buscaNobyNomeUsuario(p_Usuario);

       Mensagem Msg = new Mensagem(noTmp.getConexaoServUtil());
       String DataStr = noTmp.getConexaoServUtil().getDataHoraAtual();

       Msg.setRemetente(p_Remetente);
       Msg.setUsuario(p_Usuario);
       Msg.setAssunto("Desconexao do CDRView Analise");
       
       if (p_Remetente.equals("N/A") == true)
       {   
           Msg.setMensagem("Você foi desconectado do CDRView Analise em "+ DataStr + ".");
       }
       else
       {
           Msg.setMensagem("Você foi desconectado do CDRView Analise pelo usu&aacute;rio "+ p_Remetente + " em " + DataStr + ".");
       }
  
       Msg.envia();
	}
}