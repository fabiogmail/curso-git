package Portal.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import libjava.tipos.Vetor;
import Portal.Cluster.NoUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 30/03/2005
 *
 * @author Erick, Rafael e Marcos Urata
 * @version 1.1
 *
 * Breve Descrição: Nova implementacao dos metodos de download dos relatorios
 * agendados. A implementacao antiga, lia todas as linhas do arquivo, colocava
 * em memoria, processa e gerava o arquivo final. A implementacao nova, faz a
 * mesma coisa, so que a leitura, processamento e escrita no arquivo é feita em
 * blocos. Dessa forma, haverá uma melhor performance e não irá estourar a
 * memória do servidor (no caso de relatórios muitos grande).
 *
 */

public class NovoDownload implements Runnable 
{
    private static final String m_FONTE = "\"verdana\" size=\"1\"";
    private static final String m_COR_HEADER_APR = "#33CCFF";
    // Formato do arquivo a ser criado.
    private static final int HTML = 0;
    private static final int CSV = 1;
    private static NovoDownload singleton;
    private static Map poolSingleton = new HashMap();
    private float numLinhas;
    private float progressao;
    private String tempoDecorrido = "0";
    private long inicio; // Marca o inicio do tempo decorrido.
    /** Numero de linhas a serem lidas por vez */
    private static final int TAM_BUFFER = 250;
    private Arquivo Arq = new Arquivo();
    private Agenda agenda;
    private String m_NomeArq;
    private List m_vCabecalho;
    private List m_CfgWeb;
    private String separador = System.getProperty("line.separator");
    private int totalLinhasArquivo;
    private String p_TipoSalvamento;
    private Vetor contadores = new Vetor();

    private void zeraVariaveis()
    {
        numLinhas = 0; // Reinicia o status do numero de linhas e da progressao
        progressao = 0;
        inicio = 0;
        tempoDecorrido = "0";
    }

    public void run()
    {
        if (Arq.abre('w') == true)
        {
            zeraVariaveis();

            agenda.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_MONTANDO_ARQ);
            criaArquivoCabecalho(m_NomeArq, agenda.getM_Cabecalho(), agenda.getM_vIndicadores(), agenda.getM_PeriodosApresentaveis(), agenda.getM_Periodo(),
                                 agenda.getM_Datas(), m_CfgWeb);

            escreveLinhas(agenda, p_TipoSalvamento, NoUtil.getNo().getDiretorioDefs().getS_DIR_DOWNLOAD() + m_NomeArq); //diretorio
            // +
            // nome arq

            agenda.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_COMPACTANDO);
            Arq.zipaArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB() + "download/" + Arq.getNome());

            agenda.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_CONCLUIDO);
        }

        Arq.fecha();
        Arq.apaga();
    }

    /**
     * Metodo responsavel em garantir que existe apenas uma unica instancia da
     * classe NovoDownload para cada tipo de Agenda em memoria.
     *
     * @param p_Ag -
     *            Agenda
     *
     * @return Retorna uma unica instancia da classe NovoDownload para cada tipo
     *         de Agenda.
     */
    public static NovoDownload getInstance(Agenda p_Ag)
    {
        Integer chaveAgenda = new Integer(p_Ag.hashCode());
        /** Pega o singleton do tipo de relatorio */
        singleton = (NovoDownload) poolSingleton.get(chaveAgenda);

        if (singleton == null)
        {
            singleton = new NovoDownload();

            /** Adiciona o singleton no pool de singletons */
            poolSingleton.put(chaveAgenda, singleton);
        }
        p_Ag.setM_ContadoresRelatorio(p_Ag.getM_iRetRelatorio().fnGetContadores());
        return singleton;
    }

    /**
     * Metodo responsavel em setar a configuracao do arquivo de download.
     */
    public void setConfiguracaoDownload(String p_NomeArq, Agenda p_Ag, List p_CfgWeb)
    {
        this.m_NomeArq = p_NomeArq;
        this.agenda = p_Ag;
        this.m_CfgWeb = p_CfgWeb;

        if (p_CfgWeb.get(0).equals("1") == true)
        { // Tipo de visualização
            p_TipoSalvamento = "csv";
        }
        else
        {
            p_TipoSalvamento = "htm";
        }

        p_NomeArq += ("." + p_TipoSalvamento);

        Arq.setDiretorio(NoUtil.getNo().getDiretorioDefs().getS_DIR_DOWNLOAD());
        Arq.setNome(p_NomeArq);
        Arq.apaga();

        totalLinhasArquivo = agenda.getM_iRetRelatorio().fnGetQtdLinhas();
        System.out.println("Total de linhas do arquivo: " + totalLinhasArquivo);
    }

    /**
     * Metodo responsavel em montar o cabecalho do arquivo de download.
     *
     * @param p_NomeArq -
     *            Nome do arquivo a ser gerado
     * @param p_Cabecalho -
     *            List contendo todas as informacoes de cabecalho
     * @param p_Indicadores -
     *            List contendo as colunas selecionadas (utilizadas para montar
     *            o header da tabela)
     * @param p_PeriodosApresentaveis -
     *            List contendo os periodos do relatorio em formato dd/MM/AAAA
     *            HH:MM:ss
     * @param p_NomesPeriodos -
     *            List contendo os NOMES dos periodos
     * @param p_Datas -
     * @param p_CfgAg -
     *            List contendo as configuracoes do arquivo (com/sem informacoes
     *            de log, formato HTML ou CSV))
     */
    public void criaArquivoCabecalho(String p_NomeArq, List p_Cabecalho, List p_Indicadores, List p_PeriodosApresentaveis, List p_NomesPeriodos, List p_Datas,
                                     List p_CfgAg)
    {
        boolean retorno = false;
        StringBuffer linhasCabecalho = new StringBuffer(250);
        int i;
        int Tam;
        int Periodo;
        int QtdPeridos;
        int QtdIndicadores;
        int QtdRelProcessado;
        int QtdLogs;
        ArrayList RelProcessado;
        ArrayList LinhaRelProcessado;
        ArrayList Logs;
        ArrayList Log;
        String strLog; //, p_TipoSalvamento;
        String strColuna; //, p_TipoSalvamento;
        String Cor = ""; //, p_TipoSalvamento;

        if (p_TipoSalvamento.compareToIgnoreCase("csv") == 0)
        {
//        	 Escreve cabeçalho
            if (m_CfgWeb.get(2).equals("1") == true)
            {
                for (int j = 0; j < agenda.getM_Cabecalho().size(); j++)
                	linhasCabecalho.append(agenda.getM_Cabecalho().elementAt(j).toString() + separador);
            }
        }
        else
            if (p_TipoSalvamento.compareToIgnoreCase("htm") == 0)
            {
                linhasCabecalho.append("<html>\n<head>\n<title>CDRView | Visent</title>\n");
                linhasCabecalho.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
                linhasCabecalho.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">\n</head>\n");
                linhasCabecalho.append("<body text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n");

                linhasCabecalho.append("<br>\n");
                linhasCabecalho.append(HTMLUtil.getCabecalho());
                linhasCabecalho.append("<table border=\"0\" >");

                if (p_CfgAg.get(2).equals("1") == true)
                {
                    for (i = 0; i < p_Cabecalho.size(); i++)
                    {
                        linhasCabecalho.append("<tr><td><font face=" + m_FONTE + "size=\"1\"><b>"
                                               + p_Cabecalho.get(i).toString().substring(0, p_Cabecalho.get(i).toString().indexOf(":"))
                                               + "</b>"
                                               + p_Cabecalho.get(i).toString().substring(p_Cabecalho.get(i).toString().indexOf(":"),
                                                                                         p_Cabecalho.get(i).toString().length()) + "</font></td></tr>");
                    }
                }

                linhasCabecalho.append("</table>");

                //Tabela de linhas do relatório para o período desejado

            }

        Arq.escreve(linhasCabecalho.toString());
    }

    /**
     * Metodo responsavel em escrever as linhas que estao em memoria (BUFFER)
     * para o arquivo a ser gerado.
     */
    private void escreveLinhas(Agenda p_Ag, String tp, String dirNomeArq)
    {
        try
        {
            inicio = System.currentTimeMillis(); // Marca o inicio do cronometro
            // q ira medir o tempo
            // decorrido.

            List linhas = new ArrayList(NovoDownload.TAM_BUFFER);
            List indicadoresProcessados = null;
            int length;
            String linha = "";
            int tipo;

            /**
             * Posiciona o ponteiro que le o arquivo do relatorio apos o
             * cabecalho.
             */
            p_Ag.m_iRetRelatorio.fnPosicionaAposCabecalho();

            if (tp.equalsIgnoreCase("htm"))
            {
                tipo = HTML;
            }
            else
            {
                tipo = CSV;
            }

            if(Agenda.TIPO_REL_IMEI==0)//tipo Detalhe
            {
	            for (int i = 0; i < agenda.getM_Periodo().size(); i++)
	            {
	                /**
	                 * Posiciona o ponteiro para ler o dados do periodo "i", a
	                 * partir da linha 0.
	                 */
	                p_Ag.m_iRetRelatorio.fnPosiciona((short) i, 0);
	                escreveLinhasPeriodos("" + agenda.getM_Periodo().get(i), i, tipo);
	
	                /**
	                 * Busca um bloco de linhas de relatorio do servidor. Cada linha
	                 * eh separada por "\n"
	                 */
	                linha = p_Ag.m_iRetRelatorio.fnGetLinhas();
	
	                while ((linha != null) && (!linha.equals("")))
	                {
	                    linhas.addAll(processaBlocoDeLinhas(linha, "\n"));
	
	                    if (linhas.size() >= NovoDownload.TAM_BUFFER)
	                    {
	                        indicadoresProcessados = processaIndicadores(linhas, p_Ag);
	
	                        String resultado = criaArquivo(tipo, indicadoresProcessados,i);
	                        Arq.escreve(resultado);
	                        linhas.clear();
	                    }
	
	                    linha = p_Ag.m_iRetRelatorio.fnGetLinhas();
	                }
	
	                if (linhas.size() > 0)
	                {
	                    indicadoresProcessados = processaIndicadores(linhas, p_Ag);
	
	                    String resultado = criaArquivo(tipo, indicadoresProcessados,i);
	                    Arq.escreve(resultado);
	                    linhas.clear();
	                }
	                if(tipo==HTML){
	                	Arq.escreve("\n</table>");
	                	String retorno = criaArquivoLog(i);
	                	if(retorno.length()>0 && !retorno.equals("")){
	                		Arq.escreve(retorno);
	                	}
	                }else{
	                	if (m_CfgWeb.get(3).equals("1") == true)
	                    {
	                        Vector Logs = (Vector) agenda.getM_Logs().elementAt(i);
	                        int QtdLogs = Logs.size();
	                        StringBuffer linhas2 = new StringBuffer();
	                        if (QtdLogs != 0)
	                        {
	                            linhas2.append("Logs:");
	
	                            for (int j = 0; j < Logs.size(); j++)
	                            {
	                                if ((j == 0) && (Logs.elementAt(j).toString().equals("nolog") == true))
	                                {
	                                    linhas2.append("N&atilde;o h&aacute; logs para o este per&iacute;odo");
	                                }
	                                else
	                                {
	                                    linhas2.append(Logs.elementAt(j).toString().substring(0, Logs.elementAt(j).toString().length()));
	                                }
	                            }
	                        }
	                        linhas2.append("");
	                        Arq.escreve(linhas2.toString());
	                    }                	
	                }
	                
	            }
            }
            else//tipo resumo
            {
            	escreveLinhasPeriodos("" + agenda.getM_Periodo().get(0), 0, tipo);
            	p_Ag.m_iRetRelatorio.fnPosicionaDados((short)0,0,(short)12);	
            	linha = p_Ag.m_iRetRelatorio.fnGetDados((short)0,(short)12);
            	while ((linha != null) && (!linha.equals("")))
                {
                    linhas.addAll(processaBlocoDeLinhas(linha, "\n"));

                    if (linhas.size() >= NovoDownload.TAM_BUFFER)
                    {
                        indicadoresProcessados = processaIndicadoresResumo(linhas, p_Ag);

                        String resultado = criaArquivo(tipo, indicadoresProcessados,0);
                        Arq.escreve(resultado);
                        linhas.clear();
                    }

                    // Limpando referencia l_LinhaRel para o GC
         	       linha = null;
         	       linha = p_Ag.m_iRetRelatorio.fnGetDados((short)0,(short)12);	
                }

                if (linhas.size() > 0)
                {
                    indicadoresProcessados = processaIndicadoresResumo(linhas, p_Ag);

                    String resultado = criaArquivo(tipo, indicadoresProcessados,0);
                    Arq.escreve(resultado);
                    linhas.clear();
                }
                if(tipo==HTML)
                {
                	Arq.escreve("\n</table>");
                	String retorno = criaArquivoLog(0);
                	if(retorno.length()>0 && !retorno.equals(""))
                	{
                		Arq.escreve(retorno);
                	}
                }
                else
                {
                	if (m_CfgWeb.get(3).equals("1") == true)
                    {
                        Vector Logs = (Vector) agenda.getM_Logs().elementAt(0);
                        int QtdLogs = Logs.size();
                        StringBuffer linhas2 = new StringBuffer();
                        if (QtdLogs != 0)
                        {
                            linhas2.append("Logs:");

                            for (int j = 0; j < Logs.size(); j++)
                            {
                                if ((j == 0) && (Logs.elementAt(j).toString().equals("nolog") == true))
                                {
                                    linhas2.append("N&atilde;o h&aacute; logs para o este per&iacute;odo");
                                }
                                else
                                {
                                    linhas2.append(Logs.elementAt(j).toString().substring(0, Logs.elementAt(j).toString().length()));
                                }
                            }
                        }
                        linhas2.append("");
                        Arq.escreve(linhas2.toString());
                    }                	
                }
            }

            if (tipo == HTML)
            {
                Arq.escreve("\n</body>\n</html>");
            }

            System.out.println("Tempo Gasto: " + ((System.currentTimeMillis() - inicio) / 1000) + "  segundos");
            p_Ag.setStatusArqDownload(Agenda.STATUS_DOWNLOAD_CONCLUIDO);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    /**
     * Metodo que adiciona os indicadores na lista de indicadores
     *
     * @param agenda -
     *            Objeto Agenda
     * @return - Retorna uma lista com os indicadores
     *
     */
    public List adicionaIndicadores(Agenda agenda)
    {
        short tipoRelatorio = new Short(agenda.getM_TipoRel()).shortValue();
        List indicadores = new ArrayList();

        /**
         * Variavel que marca em que posicao da String comeca a coluna dos
         * contadores
         */
        int inicioContadores = 0;

        /**
         * Verificacao para saber em que posicao ira comecar as colunas dos
         * indicadores
         */
        if (agenda.getM_Rec() != null)
        {
            inicioContadores = agenda.getM_Rec().length;
        }

        for (int j = 0; j < (agenda.m_vIndicadores).size(); j++)
        {
            if (agenda.getM_SelecaoRecursos() == null)
            {
                indicadores.add(agenda.getM_SelecaoIndicador().fnGetIndicador(agenda.getM_vIndicadores().elementAt(j).toString(), tipoRelatorio,
                                                                              agenda.getM_ContadoresRelatorio()));
            }
            else
            {
            	if(agenda.getM_Rec() != null)
            	{
            		indicadores.add(agenda.getM_SelecaoRecursos().fnGetIndicador(agenda.getM_vIndicadores().elementAt(j).toString(), agenda.getM_Rec(),
                            tipoRelatorio, agenda.getM_ContadoresRelatorio()));
            	}
            	else
            	{
            		if((agenda.getM_SelecaoRecursos().fnGetIndicador(agenda.getM_vIndicadores().elementAt(j).toString(), agenda.getM_Recv(),
                            tipoRelatorio, agenda.getM_ContadoresRelatorio())) != null){
            		indicadores.add(agenda.getM_SelecaoRecursos().fnGetIndicador(agenda.getM_vIndicadores().elementAt(j).toString(), agenda.getM_Recv(),
                            tipoRelatorio, agenda.getM_ContadoresRelatorio()));
            		}else{
            			/**
            			 * tratamento feito para o relatorio de Distribuição de Frequencia.
            			 */
            			indicadores.add(agenda.m_vIndicadores.get(j));
            		}
            	}
                
            }

        }
        return indicadores;
    }

    /**
     * Metodo responsavel em realizar o processamento dos indicadores
     * selecionados pelo usuarios.
     *
     * @param linhasRel -
     *            List que contem as linhas do relatorio
     * @param p_Ag -
     *            Objeto do tipo Agenda. (Pode ser qualquer subclasse de Agenda)
     *
     * @return Retorna um List contendo as os indicadores processados de cada
     *         linha do List linhasRel
     */
    public List processaIndicadores(List linhasRel, Agenda p_Ag)
    {
        /** O ArrayList indicadores contem a lista de objetos selecionados */
        List indicadores = adicionaIndicadores(p_Ag);
        Vector linhaRelProcessado;
        Vector relProcessado = new Vector();
        String linhaRelatorio;
        String StrIndicador;
        String[] linhaTotalizacao = ("" + linhasRel.get(0)).split(";");

        for (int i = 0; i < linhasRel.size(); i++)
        {
            linhaRelatorio = (String) linhasRel.get(i);

            /**
             * Caso a linha contenha a flag de controle "0;" a mesma eh
             * removida, para evitar problemas de deslocamento dos contadores.
             */
            linhaRelatorio = linhaRelatorio.startsWith("0;") ? linhaRelatorio.substring(2) : linhaRelatorio;
            linhaRelProcessado = new Vector();

            /** Processa os indicadores que devem ser apresentados */
            for (int j = 0; j < indicadores.size(); j++)
            {
            	try{
	                libjava.indicadores.IndicadorValor NoCampoInd = (libjava.indicadores.IndicadorValor) indicadores.get(j);
	
	                if (NoCampoInd != null)
	                {
	                    StrIndicador = NoCampoInd.fnProcessa(linhaRelatorio.split(";"), linhaTotalizacao).toString();
	                    linhaRelProcessado.add(StrIndicador);
	                }
            	}catch(Exception e){
            		/**
            		 * Esse 'GATO'  que esta sendo feito aki, é pelo fato de que os indicadores do relatorio 
            		 * Distribuição de Frequencia não são tratados pela libjava.o fato é q pra esse relatorio 
            		 * o tratamento pode ser feito assim pq todas as linhas do relatorio seram apresentadas
            		 * não precisa ficar fazendo tratamento de qual dado vai ser apresentado.
            		 */
            		linhaRelProcessado.add((linhaRelatorio.split(";"))[j]);
            	}
            }

            linhaRelProcessado.trimToSize();
            relProcessado.add(linhaRelProcessado);
        }

        return relProcessado;
    }

    /**
     * Método que simula o método só que feito na mão
     * */
    public List processaIndicadoresResumo(List linhasRel, Agenda p_Ag)
    {
        /** O ArrayList indicadores contem a lista de objetos selecionados */
        List indicadores = p_Ag.m_vIndicadores;
        Vector linhaRelProcessado;
        Vector relProcessado = new Vector();
        String linhaRelatorio;
        String StrIndicador;
        String[] linhaTotalizacao = ("" + linhasRel.get(0)).split(";");

        for (int i = 0; i < linhasRel.size(); i++)
        {
            linhaRelatorio = (String) linhasRel.get(i);

            /**
             * Caso a linha contenha a flag de controle ";" a mesma eh
             * removida, para evitar problemas de deslocamento dos contadores.
             */
            linhaRelatorio = linhaRelatorio.startsWith(";") ? linhaRelatorio.substring(1) : linhaRelatorio;
            linhaRelProcessado = new Vector();

            /** Processa os indicadores que devem ser apresentados */
            for (int j = 0; j < indicadores.size(); j++)
            {
            	linhaRelProcessado.add((linhaRelatorio.split(";"))[j]);
            }

            linhaRelProcessado.trimToSize();
            relProcessado.add(linhaRelProcessado);
        }

        return relProcessado;
    }
    
    
    public String criaArquivo(int formato, List RelProcessado, int per)//per é o periodo pra incluir a log do periodo certo
    {
        StringBuffer linhas = new StringBuffer(TAM_BUFFER);
        List LinhaRelProcessado;

        String m_COR_LINHANAO = "#F0F0F";
        String Cor = "";
        String m_COR_LINHASIM = "#FFFFFF";

        if (formato == HTML)
        {

            for (int j = 0; j < RelProcessado.size(); j++)
            {
                if ((j % 2) != 0)
                {
                    Cor = " bgcolor=\"" + m_COR_LINHANAO + "\"";
                }
                else
                {
                    Cor = " bgcolor=\"" + m_COR_LINHASIM + "\"";
                }

                LinhaRelProcessado = (List) RelProcessado.get(j);
                linhas.append("   <tr" + Cor + ">\n   ");

                for (int k = 0; k < LinhaRelProcessado.size(); k++)
                {
                    if (k == 0)
                    {
                        linhas.append("      ");
                    }

                    linhas.append("<td align=\"center\" nowrap><font face=" + m_FONTE + ">" + LinhaRelProcessado.get(k).toString() + "</font></td>");
                }

                LinhaRelProcessado.clear();
                linhas.append("\n   </tr>\n");
            }

        }
        else{
            if (formato == CSV)
            {
                int m_GeraArqDownload = 0;
                int QtdIndicadores = 0;
                int QtdPeridos = 0;
                int QtdRelProcessado = 0;
                int QtdLogs = 0;
                Vector[] rProcessados;
                Vector Logs;
                String strColuna;
               
                QtdIndicadores = agenda.getM_vIndicadores().size();
                QtdRelProcessado = RelProcessado.size();

                for (int j = 0; j < QtdRelProcessado; j++)
                {
                    m_GeraArqDownload = ((QtdPeridos) * 100000) + j + 1;
                    LinhaRelProcessado = (Vector) RelProcessado.get(j);

                    for (int k = 0; k < QtdIndicadores; k++)
                    {
                        strColuna = LinhaRelProcessado.get(k).toString();
                        strColuna = strColuna.replace(',', '.');
                        linhas.append(strColuna + ",");
                    }

                    linhas.append(separador);
                }

                linhas.append("");
            }
        }
        numLinhas += TAM_BUFFER;



        return linhas.toString();
    }

    /**
     * Método responsável em escrever o nome dos periodos no arquivo.
     *
     * @param periodo -
     *            Formato do Parametro:
     *            "20040820090000;20040820110000;NomePeriodo;QtdLinhas"
     * @param numPeriodo -
     *            numero do periodo
     * @param formatoArquivo -
     *            Define o formato a ser escrito o arquivo
     */
    public void escreveLinhasPeriodos(String periodo, int numPeriodo, int formatoArquivo)
    {
        StringBuffer linhasCabecalho = new StringBuffer(300);
        List data = (List) agenda.getM_Datas();
        List indicadores = (List) agenda.getM_vIndicadores();

        if (formatoArquivo == HTML)
        {
            linhasCabecalho.append("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
            linhasCabecalho.append("   <tr>\n   ");
            linhasCabecalho.append("<td align=\"left\" colspan=\"" + agenda.getM_vIndicadores().size() + "\"> </td>");
            linhasCabecalho.append("\n   </tr>\n");
            if (periodo != null)
            {
                String[] nome = periodo.split(";");
                linhasCabecalho.append("<tr><td align=\"left\" colspan=\"9\"><font face=" + m_FONTE + "size=\"1\"><b>Per&iacute;odo: </b>" + nome[2]
                                       + "&nbsp;[" + data.get(numPeriodo) + "]</font></td></tr><BR>");
            }
            else
            {
                linhasCabecalho.append("<font face=" + m_FONTE + "size=\"1\">" + data.get(numPeriodo).toString() + "</font>");
            }

            linhasCabecalho.append("   <tr bgcolor=\"" + m_COR_HEADER_APR + "\">\n   ");

            for (int k = 0; k < indicadores.size(); k++)
            {
                linhasCabecalho.append("<td align=\"center\" nowrap><font face=" + m_FONTE + " color=\"#000000\"><b>" + indicadores.get(k).toString()
                                       + "</font></b></td>");
            }

            Arq.escreve(linhasCabecalho.toString());
        }
        else
        {
            if (formatoArquivo == CSV)
            {
                //não precisa pois todo o processamento do CSV ta sendo feito
                // em CRIAARQUIVO
            	int QtdIndicadores = agenda.getM_vIndicadores().size();
                if(periodo != null){
	                String[] periodos = periodo.split(";");
	
	                if ((agenda.getM_Periodo() != null) && (agenda.getM_Periodo().size() != 0))
	                {
	                	Arq.escreve("Periodo: " + periodos[2] + ": " + agenda.getM_Datas().elementAt(numPeriodo));
	                }
	                else
	                {
	                	Arq.escreve("Periodo: " + agenda.getM_Datas().elementAt(numPeriodo));
	                }
	
	                Arq.escreve(separador);
	                StringBuffer linhas = new StringBuffer();
	                for (int j = 0; j < QtdIndicadores; j++){
                        linhas.append(agenda.getM_vIndicadores().elementAt(j) + ",");
	                }
                    linhas.append(separador);
                    Arq.escreve(linhas.toString());
                }
            }
        }
    }

    /**
     * Metodo responsavel em processar o bloco de linhas
     *
     * @param linhas -
     *            String que contem o bloco de linhas separadas pelo separador
     *            "\n".
     * @param separador -
     *            Separador usado pelo servidor para quebrar as linhas.
     */
    public List processaBlocoDeLinhas(String linhas, String separador)
    {
        List todasLinhas = new ArrayList();

        if ((linhas == null) || (separador == null))
        {
            throw new IllegalArgumentException("Linhas ou sepador estao null");
        }
        else
        {
            String[] blocoLinhas = linhas.split(separador);

            for (int i = 0; i < blocoLinhas.length; i++)
            {
                todasLinhas.add(blocoLinhas[i] + separador);
            }
        }

        return todasLinhas;
    }

    /**
     * @return Returns the progressao.
     */
    public float getProgressao()
    {
        if (numLinhas != 0 && totalLinhasArquivo != 0)
        {
            progressao = (numLinhas / totalLinhasArquivo) * 100;
        }
        else
        {
        	progressao = 0;
        }

        DecimalFormat formatacao = new DecimalFormat();
        formatacao.applyPattern("##.##");

        String formatacaoStr = formatacao.format(progressao).replaceAll(",", ".");

        return new Float(formatacaoStr).floatValue();
    }

    /**
     * @param progressao
     *            The progressao to set.
     */
    public void setProgressao(float progressao)
    {
        this.progressao = progressao;
    }

    /**
     *
     * @return
     */
    public String getTempoDecorrido()
    {
        int tpDecorridoSeg = 0;

        if (numLinhas != 0)
        {
            tpDecorridoSeg = (int) ((System.currentTimeMillis() - inicio) / 1000);

            int segundos = tpDecorridoSeg % 60;

            if ((tpDecorridoSeg / 60) != 0)
            {
                tempoDecorrido = (tpDecorridoSeg / 60) + " min e " + segundos + " s";
            }
            else
            {
                if (segundos != 0)
                {
                    tempoDecorrido = segundos + " s";
                }
            }
        }

        return tempoDecorrido;
    }



    private String criaArquivoLog(int periodo){
    	List log = new ArrayList();
    	StringBuffer linhas = new StringBuffer("");
    	if (m_CfgWeb.get(3).equals("1") == true) // Log
        {
    		linhas = new StringBuffer(TAM_BUFFER);
            log = (List) agenda.getM_Logs().elementAt(periodo);
            if (log.get(0).toString().equals("nolog") == false)
            {
                linhas.append("<table>\n");
                linhas.append("   <tr>\n      <td align=\"left\"><font face=" + m_FONTE + "><b>Logs:</b></font></td>\n   </tr>\n");
                int TamLog = log.size();
                for (int j = 0; j < TamLog; j++)
                    linhas.append("   <tr>\n      <td><font face=" + m_FONTE + ">"
                                 + log.get(j).toString().substring(0, log.get(j).toString().length()) + "</font></td>\n   </tr>\n");
                linhas.append("</table>\n");
                linhas.append("<br>");
            }
        }
    	return linhas.toString();
    }

    /**
     *
     * @param tempo_decorrido
     */
    public void setTempoDecorrido(String tempoDecorrido)
    {
        this.tempoDecorrido = tempoDecorrido;
    }

    public List getM_CfgWeb()
    {
        return m_CfgWeb;
    }

    public void setM_CfgWeb(List cfgWeb)
    {
        m_CfgWeb = cfgWeb;
    }

    public String getM_NomeArq()
    {
        return m_NomeArq;
    }

    public void setM_NomeArq(String nomeArq)
    {
        m_NomeArq = nomeArq;
    }

    public List getM_vCabecalho()
    {
        return m_vCabecalho;
    }

    public void setM_vCabecalho(List cabecalho)
    {
        m_vCabecalho = cabecalho;
    }
}