/*
 * Created on 14/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ConfiguracaoConvCfgDef;
import Portal.Utils.ConfiguracaoReprocCfgDef;
import Portal.Utils.ProcessoDef;

/**
 * @author osx
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpListaProcessosParados extends OperacaoAbs {
    private String m_PIds = "";
    private short m_QtdProcessos = 0;
    private short m_PosIni = 0;
    private String m_Tipo;
    private String listagem;
    private Vector processosParados = null;

    static
    {}

    /**
     * @return @exception
     * @roseuid 3C44BBF90321
     */
    public OpListaProcessosParados()
    {
    }

    /**
     * @param p_Mensagem
     * @return boolean
     * @exception
     * @roseuid 3C44BC04018C
     */
    public boolean iniciaOperacao(String p_Mensagem)
    {
        try
        {
            String Args[], TipoTmp = "";
            if (p_Mensagem.equals("$ARG;") == false && p_Mensagem.indexOf("@") != -1)
            {
                m_Tipo = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));
                p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@") + 1, p_Mensagem.length());
            }
            else
            {
                try
                {
                    m_Tipo = m_Request.getParameter("tipo");
                    listagem = m_Request.getParameter("listagem");
                    if (m_Tipo.equalsIgnoreCase("util") || m_Tipo.equalsIgnoreCase("ctrl"))
                    {
                        m_Tipo = m_Request.getParameter("tipo2");
                    }
                }
                catch (Exception e)
                {
                    if (p_Mensagem.indexOf(";") != -1)
                    {
                        String[] separa = p_Mensagem.split(";");
                        m_Tipo = separa[0];
                        p_Mensagem = separa[1];
                    }
                    else
                    {
                        m_Tipo = p_Mensagem;
                    }
                }
            }

            TipoTmp = m_Tipo.toUpperCase();
            TipoTmp = TipoTmp.substring(0, 1) + m_Tipo.substring(1, m_Tipo.length());

            setOperacao("Processos/" + TipoTmp);
            montaTabela();
            m_PosIni = 1;
            Args = new String[10];

            Args[0] = "util";
            Args[1] = m_Tipo.toLowerCase();
            Args[2] = Short.toString(m_PosIni);
            if (processosParados != null)
            {
                Args[3] = Integer.toString(processosParados.size());
            }
            else
            {
                Args[3] = "" + 0;
            }
            Args[4] = m_PIds;
            Args[5] = "N/A";
            Args[6] = p_Mensagem;
            Args[7] = "inicializa" + m_Tipo.toLowerCase();
            Args[8] = "";
            if (processosParados != null)
            {
                for (int i = 0; i < processosParados.size(); i++)
                {
                    if (i < processosParados.size() - 1) 
                    {
                    	if (m_Tipo.toUpperCase().equals("REPROCESSADOR"))
                    	{
                    		Args[8] += ((ConfiguracaoReprocCfgDef) processosParados.get(i)).getNome() + ";";
                    	}
                    	else
                    	{
                    		Args[8] += ((ConfiguracaoConvCfgDef) processosParados.get(i)).getNome() + ";";
                    	}
                    }
                    else 
                    {
                    	if (m_Tipo.toUpperCase().equals("REPROCESSADOR"))
                    	{
                    		Args[8] += ((ConfiguracaoReprocCfgDef) processosParados.get(i)).getNome();
                    	}
                    	else
                    	{
                    		Args[8] += ((ConfiguracaoConvCfgDef) processosParados.get(i)).getNome();
                    	}
                    }
                }
            }
            Args[9] = m_Html.m_Tabela.getTabelaString();
            iniciaArgs(7);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "listagen.htm";
            m_Args[2] = "src=\"/PortalOsx/templates/js/listaprocessosparados.js\"";
            m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/selec.gif','/PortalOsx/imagens/deselec.gif'); IniciaFinalizacao(); VerificaMensagem()\""; // onload

            if (m_Tipo.toLowerCase().equals("deteccao")) m_Args[4] = "deteccaomanutencao.gif";
            else if (m_Tipo.toLowerCase().equals("util")) m_Args[4] = "webmanutencao.gif";
            else if (m_Tipo.toLowerCase().equals("parser")) m_Args[4] = "parsersmanutencao.gif";
            else if (m_Tipo.toLowerCase().equals("conversor")) m_Args[4] = "conversoresmanutencao.gif";
            else if (m_Tipo.toLowerCase().equals("reprocessador")) m_Args[4] = "reprocessadoresmanutencao.gif";

            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listaprocessosparados.form", Args);
            m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listaprocessosutil.txt", null);
            m_Html.enviaArquivo(m_Args);
            return true;
        }
        catch (Exception Exc)
        {
            System.out.println("OpListaProcessos - iniciaOperacao(): " + Exc);
            Exc.printStackTrace();
            return false;
        }
    }

    /**
     * @return Vector
     * @exception
     * @roseuid 3C59F74302A1
     */
    public Vector montaLinhas()
    {
        ConfiguracaoConvCfgDef ProcessoConv = null;// 	      ProcessoDef
        ConfiguracaoReprocCfgDef ProcessoReproc = null;
        boolean bTemProcesso = false;
        int Index = 1;
        Vector ListaTodosProcessos = new Vector(), Linhas = new Vector(), Colunas = null, ListaProcessosRodando = new Vector();
        // todos os que estão rodando
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
        	try
			{
	            noTmp = (No) iter.next();
	            Vector listaProcRodando = noTmp.getConexaoServUtil().getListaProcessos();
	            if(listaProcRodando != null)
	            	ListaProcessosRodando.addAll(listaProcRodando);
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
 
        if (m_Tipo.toUpperCase().equals("PARSER"))
        {
            // todos os parsers
        	//Vector listaTodosProc = NoUtil.getNoCentral().getConexaoServUtil().getListaCfgParsers();
            //if(listaTodosProc != null)
            //	ListaTodosProcessos = listaTodosProc;
        	 for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
             {
             	try
 				{
                     noTmp = (No) iter.next();
                     Vector listaTodosProc = noTmp.getConexaoServUtil().getListaCfgParsers();
            if(listaTodosProc != null)
                     	ListaTodosProcessos.addAll(listaTodosProc);
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
        }
        else if (m_Tipo.toUpperCase().equals("PARSERGEN"))
        {
        	 // todos os parsers
        	//Vector listaTodosProc = NoUtil.getNoCentral().getConexaoServUtil().getListaCfgParsersGen();
            //if(listaTodosProc != null)
            //	ListaTodosProcessos = listaTodosProc;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
            	try
				{
                    noTmp = (No) iter.next();
                    Vector listaTodosProc = noTmp.getConexaoServUtil().getListaCfgParsersGen();
            if(listaTodosProc != null)
                    	ListaTodosProcessos.addAll(listaTodosProc);
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
        }
        else if (m_Tipo.toUpperCase().equals("CONVERSOR"))
        {
            // todos os conversores
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
            	try
				{
                    noTmp = (No) iter.next();
                    Vector listaTodosProc = noTmp.getConexaoServUtil().getListaCfgConversores();
                    if(listaTodosProc != null)
                    	ListaTodosProcessos.addAll(listaTodosProc);
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
        }
        else if (m_Tipo.toUpperCase().equals("REPROCESSADOR"))
        {
            // todos os reprocessadores
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
            	try
				{
                    noTmp = (No) iter.next();
                    Vector listaTodosProc = noTmp.getConexaoServUtil().getListaCfgReprocessadores();
                    if(listaTodosProc != null)
                    	ListaTodosProcessos.addAll(listaTodosProc);
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
        }
        if (ListaTodosProcessos != null && ListaTodosProcessos.size() > 0) //verifica se ha alguma configuração
        {
            if (ListaProcessosRodando != null)
            {
                ListaTodosProcessos.trimToSize();
                ListaProcessosRodando.trimToSize();
                ListaTodosProcessos = removeProcessosRodando(ListaTodosProcessos, ListaProcessosRodando);
            }
            processosParados = ListaTodosProcessos;
            if (ListaTodosProcessos.size() > 0)
            {
                for (short i = 0; i < ListaTodosProcessos.size();)
                {
                	Colunas = new Vector();
                	String nomeProcesso = "";
                	
                	if (m_Tipo.toUpperCase().equals("PARSER") || m_Tipo.toUpperCase().equals("CONVERSOR") || m_Tipo.toUpperCase().equals("PARSERGEN"))
                	{
	                    if (ListaTodosProcessos.elementAt(i) instanceof ConfiguracaoConvCfgDef)
	                    {
	                        ProcessoConv = (ConfiguracaoConvCfgDef) ListaTodosProcessos.elementAt(i);
	                        nomeProcesso = ProcessoConv.getNome();
	                        Colunas.add(nomeProcesso);
	                        Colunas.add(ProcessoConv.getServidor());
	                    }
	                    else
	                    {
	                        // Apagando processo do ServUtil (instancia ProcessoDef) da Lista de 
	                        // Conversores Parados
	                        ListaTodosProcessos.removeElementAt(i); 
	                        continue;
	                    }
                	}
                	if (m_Tipo.toUpperCase().equals("REPROCESSADOR"))
                	{
	                    if (ListaTodosProcessos.elementAt(i) instanceof ConfiguracaoReprocCfgDef)
	                    {
	                        ProcessoReproc = (ConfiguracaoReprocCfgDef) ListaTodosProcessos.elementAt(i);
	                        nomeProcesso = ProcessoReproc.getNome();
	                        Colunas.add(nomeProcesso);
	                        Colunas.add(ProcessoReproc.getServidor());
	                    }
	                    else
	                    {
	                        // Apagando processo do ServUtil (instancia ProcessoDef) da Lista de 
	                        // Conversores Parados
	                        ListaTodosProcessos.removeElementAt(i); 
	                        continue;
	                    }
                	}
                    
                    bTemProcesso = true;

                    if (nomeProcesso.toLowerCase().startsWith("servutil") == false && nomeProcesso.toLowerCase().startsWith("servctrl") == false)
                    {
                        Colunas
                               .add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"
                                    + Index
                                    + "','','/PortalOsx/imagens/selec.gif',1,'"
                                    + Index
                                    + "','"
                                    + nomeProcesso
                                    + "')\" onmouseover=\"window.status='Marca/desmarca o processo para finalizar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/deselec.gif\" border=\"0\" name=\"Image"
                                    + Index + "\"></a>\n");
                        m_QtdProcessos++;
                        m_PIds += nomeProcesso + ";";
                        Index++;
                    }
                    else Colunas.add("N/A");

                    Colunas.trimToSize();
                    Linhas.add(Colunas);
                    i++;
                }

                if (bTemProcesso)
                {
                    if (m_PIds.length() != 0) m_PIds = m_PIds.substring(0, m_PIds.length() - 1);
                }
                else
                {
                    Colunas = new Vector();
                    Colunas.add("Todos os processos est&atilde;o parados");
                    Colunas.add("&nbsp;");
                    Colunas.add("&nbsp;");
                    Colunas.add("&nbsp;");
                    Colunas.trimToSize();
                    Linhas.add(Colunas);
                }
            }
            else
            {
                Colunas = new Vector();
                Colunas.add("Todos os processos est&atilde;o iniciados");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.trimToSize();
                Linhas.add(Colunas);
            }
        }
        else
        {
            Colunas = new Vector();
            if (m_Tipo.toUpperCase().equals("PARSER"))
            {
                Colunas.add("Não existe nenhum parser configurado");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.trimToSize();
                Linhas.add(Colunas);
            }
            else if(m_Tipo.toUpperCase().equals("PARSERGEN"))
            {
                Colunas.add("Não existe nenhum parsegen configurado");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.trimToSize();
                Linhas.add(Colunas);
            }
            else
            {
            	Colunas.add("Não existe nenhum conversor configurado");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.add("&nbsp;");
                Colunas.trimToSize();
                Linhas.add(Colunas);
            }

        }

        Linhas.trimToSize();
        return Linhas;
    }

    /**
     * @return void
     * @exception
     * @roseuid 3C59F74302B5
     */
    public void montaTabela()
    {
        String Header[] = { "Nome", "Id - Host", "Inicializa" };
        String Largura[] = { "300", "100", "100" };//160,87,76
        String Alinhamento[] = { "left", "center", "center" };
        short Filtros[] = { 1, 1, 0 }; //{1, 1, 1, 0}
        Vector Linhas;

        Linhas = montaLinhas();

        if (listagem != null)
        {
           if (listagem.equalsIgnoreCase("parcial") == true)
              m_Html.setTabela((short)5, true);
           else
              m_Html.setTabela((short)5, false);
        }
        else
        {
           listagem = "parcial";
           m_Html.setTabela((short)5, true);
        }
        //m_Html.setTabela((short) Header.length, false);
        m_Html.m_Tabela.setHeader(Header);
        m_Html.m_Tabela.setLarguraColunas(Largura);
        m_Html.m_Tabela.setCellPadding((short) 2);
        m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
        m_Html.m_Tabela.setApresentaIndice(false);
        m_Html.m_Tabela.setFiltros(Filtros);
        m_Html.m_Tabela.setAlinhamento(Alinhamento);
        m_Html.m_Tabela.setAlturaColunas((short) 20);
        m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaProcessosParados&tipo=" + m_Tipo+"&listagem="+listagem);
        m_Html.m_Tabela.setElementos(Linhas);
        m_Html.trataTabela(m_Request, Linhas);
        m_Html.m_Tabela.enviaTabela2String();
    }

    /***************************************************************************
     * método que retorna um vector que contem somente os processos que estao
     * parados
     * 
     * @param todos
     *            os processos e somente os processos que estão rodando
     * @return somente os processos parados
     */
    public Vector removeProcessosRodando(Vector todosProcessos, Vector processosRodando)
    {
        Vector parados = new Vector();
        Vector retorno = new Vector();
        parados.addAll(todosProcessos);
        parados.trimToSize();
        for (int i = 0; i < todosProcessos.size(); i++)
        {
            for (int j = 0; j < processosRodando.size(); j++)
            {
            	String nomeConfiguracao = "";
            	String nomeProcesso = "";
            	if (m_Tipo.toUpperCase().equals("REPROCESSADOR"))
            	{
            		nomeConfiguracao = ((ConfiguracaoReprocCfgDef) todosProcessos.get(i)).getNome();
            	}
            	else
            	{
	                nomeConfiguracao = ((ConfiguracaoConvCfgDef) todosProcessos.get(i)).getNome();
            	}
            	nomeProcesso = ((ProcessoDef) processosRodando.get(j)).getNome();
                if (nomeConfiguracao.equalsIgnoreCase(((ProcessoDef) processosRodando.get(j)).formataNomeProcesso(nomeProcesso)))
                {
                    parados.setElementAt(null, i);
                }
            }
        }
        for (int i = 0; i < parados.size(); i++)
        {
            if (parados.get(i) != null)
            {
                retorno.add(parados.get(i));
            }
        }
        return retorno;
    }

}

