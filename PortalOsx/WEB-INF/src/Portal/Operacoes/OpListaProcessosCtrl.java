//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaProcessosCtrl.java

package Portal.Operacoes;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import agenda.facade.IManutencaoAgenda;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.AgendaRMI;
import Portal.Utils.ProcessoDef;

/**
 */
public class OpListaProcessosCtrl extends OperacaoAbs 
{
	private String m_PIds = "";
	private String m_Usuarios = "";
	private short m_QtdProcessos = 0;
	private short m_PosIni = 0;

	static 
	{
	}

	/**
	 * @return 
	 * @exception 
	 * @roseuid 3C856E260244
	 */
	public OpListaProcessosCtrl() 
	{
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception 
	 * @roseuid 3C856E260258
	 */
	public boolean iniciaOperacao(String p_Mensagem) 
	{
		try
		{
			setOperacao("Lista Processos/ServCtrl");
			montaTabela();

			String Args[] = new String[8];
			m_PosIni = 0;
			Args[0] = Args[1] = "ctrl";
			Args[2] = Short.toString(m_PosIni);
			Args[3] = Integer.toString(m_QtdProcessos);
			Args[4] = m_PIds;
			Args[5] = m_Usuarios;
			Args[6] = p_Mensagem;
			Args[7] = m_Html.m_Tabela.getTabelaString();

			iniciaArgs(7);
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
			m_Args[1] = "listagen.htm";
			m_Args[2] = "src=\"/PortalOsx/templates/js/listaprocessos.js\"";
			m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/selec.gif','/PortalOsx/imagens/deselec.gif'); IniciaFinalizacao(); VerificaMensagem()\""; // onload
			m_Args[4] = "analisemanutencao.gif";
			m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listaprocessos.form", Args);
			m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listaprocessosctrl.txt", null);
			m_Html.enviaArquivo(m_Args);
			return true;
		}
		catch (Exception Exc)
		{
			System.out.println("OpListaProcessosCtrl - iniciaOperacao(): "+Exc);
			Exc.printStackTrace();
			return false;
		}
	}

	/**
	 * @return Vector
	 * @exception 
	 * @roseuid 3C856E260276
	 */
	public Vector montaLinhas() 
	{
		int Index = 1;
		ProcessoDef Processo;
		Vector ListaProcessos = new Vector(), Linhas = new Vector(), Colunas = null;
		Vector vectorTmp = null;
		Vector rmi = new Vector<String>();
		String Tipo[] = null, Processos = "";

		No noTmp = null;
		for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
		{
			try
			{
				noTmp = (No) iter.next();
				vectorTmp = noTmp.getConexaoServControle().getListaProcessos();

				if (vectorTmp != null)
				{
					ListaProcessos.addAll(vectorTmp);
				}
				IManutencaoAgenda agenda = AgendaRMI.retornaManutencaoAgenda(noTmp.getIp(), noTmp.getPortaRMI(), noTmp.getHostName());
				if(agenda != null){
					if(agenda.ping()){
						Vector tmp = new Vector();
						tmp.add("ServidorAgenda");

						if(NoUtil.isAmbienteEmCluster())
							tmp.add(noTmp.getHostName());

						tmp.add("N/A");
						tmp.add("N/A");
						tmp.add(getDataHoraInicio(agenda.getHorarioDeInicio()));
						tmp.add("<a href=\"javascript:AbreJanela('detalhesProcesso', 'servagenda', '"+agenda.getPID()+" - "+noTmp.getHostName()+"')\" onmouseover=\"window.status='Ver detalhes';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+agenda.getPID()+"</a>");
						tmp.add("N/A");
						rmi.add(tmp);
//						rmi.add("teste");
						agenda = null;
					}	  
				}
				//{"Nome", "Servidor", "Usu&aacute;rio", "Perfil", "In&iacute;cio", "Id", "Finaliza"};

			}
			catch(COMM_FAILURE comFail)
			{
				System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
			}
			catch(BAD_OPERATION badOp)
			{
				System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
				badOp.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				System.out.println(new Date()+" - SERVIDOR RMI FORA DO AR ("+noTmp.getHostName()+").");
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		if (ListaProcessos != null && (ListaProcessos.size() > 0 || rmi.size() > 0))
		{
			if(ListaProcessos != null && ListaProcessos.size() > 0)
				for (short i = 0; i < ListaProcessos.size(); i++)
				{
					Processo = (ProcessoDef)ListaProcessos.elementAt(i);

					Colunas = new Vector();
					Colunas.add(Processo.getNome());

					if (NoUtil.isAmbienteEmCluster())
					{
						Colunas.add(Processo.getHost());
					}

					Colunas.add(Processo.getUsuario());
					Colunas.add(Processo.getPerfil());
					Colunas.add(Processo.getDataInicioStr());
					Colunas.add("<a href=\"javascript:AbreJanela('detalhesProcesso', 'ctrl', '"+Processo.getId()+" - "+Processo.getHost()+"')\" onmouseover=\"window.status='Ver detalhes';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+Processo.getId()+"</a>");

					if (Processo.getNome().toLowerCase().startsWith("servagn")  == false &&
							Processo.getNome().toLowerCase().startsWith("servctrl") == false &&
							Processo.getNome().toLowerCase().startsWith("servhist") == false &&
							Processo.getNome().toLowerCase().startsWith("servcfg") == false)
					{
						Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+Index+"','','/PortalOsx/imagens/selec.gif',1,'"+Index+"','"+Processo.getId()+"-"+ Processo.getHost()+"','"+Processo.getUsuario()+"')\" onmouseover=\"window.status='Marca/desmarca o processo para finalizar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/deselec.gif\" border=\"0\" name=\"Image"+Index+"\"></a>\n");
						m_QtdProcessos++;
						m_Usuarios += Processo.getUsuario() + ";";
						m_PIds += Processo.getId() + ";";
						Index++;
					}
					else
						Colunas.add("N/A");

					Colunas.trimToSize();
					Linhas.add(Colunas);
				}

			Colunas = new Vector();
			if(rmi.size() > 0)
				for (Iterator iterator = rmi.iterator(); iterator.hasNext();) {
					Vector vector = (Vector) iterator.next();
					Colunas = new Vector();
					for (Iterator iterator2 = vector.iterator(); iterator2.hasNext();) {
						String string = (String) iterator2.next();
						Colunas.add(string);				
					}
					if(Colunas.size() > 0){
						Colunas.trimToSize();
						Linhas.add(Colunas);
					}
					m_QtdProcessos++;

				}
//			Colunas.trimToSize();
//			Linhas.add(Colunas);
			if (m_PIds.length() != 0)
				m_PIds = m_PIds.substring(0, m_PIds.length()-1);
			if (m_Usuarios.length() != 0)
				m_Usuarios = m_Usuarios.substring(0, m_Usuarios.length()-1);            
		}
		else
		{
			Colunas = new Vector();
			Colunas.add("Todos os processos est&atilde;o fora do ar");
			Colunas.add("&nbsp;");

			if (NoUtil.isAmbienteEmCluster())
			{
				Colunas.add("&nbsp;");
			}

			Colunas.add("&nbsp;");
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
	 * @roseuid 3C856E260294
	 */
	public void montaTabela() 
	{
		String header[];
		String largura[];
		String alinhamento[];
		short filtros[];

		if (NoUtil.isAmbienteEmCluster())
		{
			header      = new String[] {"Nome", "Servidor", "Usu&aacute;rio", "Perfil", "In&iacute;cio", "Id", "Finaliza"};
			largura     = new String[] {"80", "80", "70", "70", "120", "87", "76"};
			alinhamento = new String[] {"left", "center", "center", "center", "center", "center", "center"};
			filtros     = new short[]  {0, 1, 0, 0, 0, 0, 0};
		}
		else
		{
			header      = new String[] {"Nome", "Usu&aacute;rio", "Perfil", "In&iacute;cio", "Id", "Finaliza"};
			largura     = new String[] {"100", "80", "80", "130", "87", "76"};
			alinhamento = new String[] {"left", "center", "center", "center", "center", "center"};
			filtros     = new short[]  {0, 0, 0, 0, 0, 0};
		}


		Vector Linhas;

		Linhas = montaLinhas();

		m_Html.setTabela((short)header.length, false);      
		m_Html.m_Tabela.setHeader(header);
		m_Html.m_Tabela.setLarguraColunas(largura);
		m_Html.m_Tabela.setCellPadding((short)2);
		m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
		m_Html.m_Tabela.setApresentaIndice(false);
		m_Html.m_Tabela.setFiltros(filtros);
		m_Html.m_Tabela.setAlinhamento(alinhamento);
		m_Html.m_Tabela.setAlturaColunas((short)20);
		m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaProcessosCtrl");
		m_Html.m_Tabela.setElementos(Linhas);

		m_Html.trataTabela(m_Request, Linhas);
		m_Html.m_Tabela.enviaTabela2String();
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
