//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaProcessos.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ProcessoDef;

/**
 */
public class OpListaBaseCDR extends OperacaoAbs {
	private String m_PIds = "";

	private short m_QtdProcessos = 0;

	private short m_PosIni = 0;

	private String m_Tipo;

	static {
	}

	/**
	 * @return @exception
	 * @roseuid 3C44BBF90321
	 */
	public OpListaBaseCDR() {
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception
	 * @roseuid 3C44BC04018C
	 */
	public boolean iniciaOperacao(String p_Mensagem) {
		//System.out.println("OpUltimaColeta - iniciaOperacao()");
		try {
			setOperacao("Lista Base CDRs");
			montaTabela();
			m_Args = new String[7];
			m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
			m_Args[1] = "listagen.htm";
			m_Args[2] = ""; // javascript
			m_Args[3] = ""; // onload
			m_Args[4] = "listabilhetadores.gif";//ultimacoleta.gif
			m_Args[5] = m_Html.m_Tabela.getTabelaString();
			m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(),"listabaseCDR.txt", null);
			m_Html.enviaArquivo(m_Args);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpListaBaseCDRs - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
	}

	/**
	 * @return Vector
	 * @exception
	 * @roseuid 3C59F74302A1
	 */
	public Vector montaLinhas() {
		
		ProcessoDef Processo, Processos_Agn_Hist;
		boolean bTemProcesso = false;
		int Index = 1;
		String listaBase = "", datas = "";
		Vector Linhas = new Vector(), Colunas = null;
		String Processos = "";
		
	   	No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
        	try
        	{
        		noTmp = (No) iter.next();
        		listaBase += noTmp.getConexaoServUtil().geraListaDiasBDCDR();
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

		if (listaBase != null && !listaBase.equals("")) 
		{
			String[] base = listaBase.split("\n");
			for (int i = 0; i < base.length; i++) 
			{
				datas = "";
				String[] bilhetador = base[i].split(";");
				Colunas = new Vector();
				Colunas.add(bilhetador[0]);
				if (bilhetador.length > 1) {//se tiver data o tamanho do bilhetador é maior que um
					datas = "<select size=\"1\" name=\"listadatas\" class=\"lista\">\n";
					for (int j = 1; j < bilhetador.length; j++) 
					{
						datas += "<option value=\"" + bilhetador[j] + "\">" + bilhetador[j]+ "\n";
					}
					datas += "</select>\n";
					Colunas.add(datas);
				}
				else{//senão tiver data preenche com branco
					Colunas.add("");
				}
				Colunas.trimToSize();
				Linhas.add(Colunas);
			}
			m_Request.setAttribute("atual","0");
			m_Request.setAttribute("offset","0");
			m_Request.setAttribute("indice","0");
			m_Request.setAttribute("ordena","1");

		} else {
			Colunas = new Vector();
			Colunas.add("Todos as bases est&atilde;o fora do ar");
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
	 * @roseuid 3C59F74302B5
	 */
	public void montaTabela() {
		String Header[] = { "Bilhetador", "Datas Dispon&iacute;veis" };
		String Largura[] = { "100", "100" };
		String Alinhamento[] = { "left", "left" };
		short Filtros[] = { 0, 0 };
		Vector Linhas;

		Linhas = montaLinhas();

		m_Html.setTabela((short) Header.length, false);
		m_Html.m_Tabela.setHeader(Header);
		m_Html.m_Tabela.setLargura((short)450);
		m_Html.m_Tabela.setLarguraColunas(Largura);
		m_Html.m_Tabela.setCellPadding((short) 2);
		m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
		m_Html.m_Tabela.setApresentaIndice(false);
		m_Html.m_Tabela.setFiltros(Filtros);
		m_Html.m_Tabela.setAlinhamento(Alinhamento);
		m_Html.m_Tabela.setAlturaColunas((short) 20);
		//m_Html.m_Tabela
				//.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaProcessos&tipo="
						//+ m_Tipo);
		m_Html.m_Tabela.setElementos(Linhas);
		m_Html.trataTabela(m_Request, Linhas);
		m_Html.m_Tabela.enviaTabela2String();
	}
}