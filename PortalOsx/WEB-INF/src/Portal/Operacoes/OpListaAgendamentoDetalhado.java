package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;

public class OpListaAgendamentoDetalhado extends OperacaoAbs{
	private String m_PIds = "";
	private short m_QtdProcessos = 0;
	private short m_PosIni = 0;
	private String m_Tipo;
	static 
	   {
	   }
	  
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
			m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML(),
					"listabaseCDR.txt", null);
			m_Html.enviaArquivo(m_Args);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpListaBaseCDRs - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
	}

	public Vector montaLinhas() {
		String array = "Evento1;parser1;iniciado\nEvento1;parser2;concluido\nEvento2;parser3;concluido";
		boolean bTemProcesso = false;
		int Index = 1;
		String listaBase = null, datas = "";
		Vector Linhas = new Vector(), Colunas = null;
		String Processos = "";

		//listaBase = m_ConexUtil.geraListaDiasBDCDR();
		listaBase = array;
		if (listaBase != null && (!listaBase.equals(""))) 
		{
			String[] base = listaBase.split("\n");
			for (int i = 0; i < base.length; i++) 
			{
				datas = "";
				String[] bilhetador = base[i].split(";");
				Colunas = new Vector();
				for(int k=0;k<bilhetador.length;k++){
					Colunas.add("<option value=\"" + bilhetador[k] + "\">" + bilhetador[k]+ "\n");
				}
				//Colunas.add(bilhetador[0]);
//				if (bilhetador.length > 1) {//se tiver data o tamanho do bilhetador é maior que um
//					datas = "<select size=\"1\" name=\"listadatas\" class=\"lista\">\n";
//					for (int j = 1; j < bilhetador.length; j++) 
//					{
//						datas += "<option value=\"" + bilhetador[j] + "\">" + bilhetador[j]+ "\n";
//					}
//					datas += "</select>\n";
//					Colunas.add(datas);
//				}
//				else{//senão tiver data preenche com branco
//					Colunas.add("");
//				}
				Colunas.trimToSize();
				Linhas.add(Colunas);
			}

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
	public void montaTabela() 
	   {
	      String Header[] = {"Evento","Nome", "Status"};
	      String Largura[] = {"200", "200", "200"};
	      String Alinhamento[] = {"left", "center", "left"};
	      short Filtros[] = {0, 0, 0};  
	      Vector Linhas;

	      Linhas = montaLinhas();

	      m_Html.setTabela((short)Header.length, false);      
	      m_Html.m_Tabela.setHeader(Header);
	      m_Html.m_Tabela.setLarguraColunas(Largura);
	      m_Html.m_Tabela.setCellPadding((short)2);
	      m_Html.m_Tabela.setQtdItensPagina((short)20);
	      m_Html.m_Tabela.setApresentaIndice(false);
	      m_Html.m_Tabela.setFiltros(Filtros);
	      m_Html.m_Tabela.setAlinhamento(Alinhamento);
	      m_Html.m_Tabela.setAlturaColunas((short)20);
	      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaProcessos&tipo="+m_Tipo);
	      m_Html.m_Tabela.setElementos(Linhas);
	      m_Html.trataTabela(m_Request, Linhas);
	      m_Html.m_Tabela.enviaTabela2String();
	   }

	public static void main(String[] args) {
	}
}
