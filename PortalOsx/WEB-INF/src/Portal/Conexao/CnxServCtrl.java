//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Conexao/CnxServCtrl.java

package Portal.Conexao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import Interfaces.iConexao;
import Interfaces.iConexaoHelper;
import Interfaces.iListaProcControlados;
import Interfaces.iProcControlado;
import Interfaces.iTipoProc;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.MeuStrVector;
import Portal.Utils.ProcessoDef;
import Portal.Utils.UsuarioDef;

/**
 */
public class CnxServCtrl extends Conexao {
    private short m_IdConexao = 0;
    public iConexao s_iConexao;

    public CnxServCtrl(short p_ModoConexao, String p_Host, String p_Objeto, int p_Porta)
    {
        this.host = p_Host;
 	    this.porta = p_Porta;
 	    this.nomeObjetoCorba = p_Objeto;
 	    this.modoConexao = p_ModoConexao;
        
        //conectaServidor(p_ModoConexao, p_Host, p_Objeto, p_Porta);
    }

    public boolean conectaServidor(short p_ModoConexao, String p_Host, String p_Objeto, int p_Porta)
    {
        String Args[] = { "", "" };

        switch (p_ModoConexao)
        {
            case 1:
                iniciaOrb(Args, false, p_Host, p_Porta, p_Objeto);
                try
                {
                    s_iConexao = iConexaoHelper.narrow(m_Obj);
                    return true;
                }
                catch (Exception Exc)
                {
                    System.out.println("CnxServCtrl - conectaServidor():" + Exc);
                    s_iConexao = null;
                    return false;
                }
            case 2:
            	
            	String caminho = "//"+getNo().getHostName()+getNo().getDiretorioDefs().getS_DIR_ARQS_REF();
            	
                if (getIORArq(caminho, ArquivosDefs.s_ARQ_IOR_SERVCTRL))
                {
                    iniciaOrb(Args, true, p_Host, p_Porta, p_Objeto);
                    try
                    {
                        s_iConexao = iConexaoHelper.narrow(m_Obj);
                        return true;
                    }
                    catch (Exception Exc)
                    {
                        System.out.println("CnxServCtrl - conectaServidor():" + Exc);
                        s_iConexao = null;
                        return false;
                    }
                }
                break;
        }

        return false;
    }

    /**
     * @param p_Modo
     * @return @exception
     *         Construtor. O parâmetro Modo define a maneira de obter o IOR do
     *         servidor. 0 - Não Realiza conexão 1 - Diretamente pelo IP e Porta
     *         2 - Pelo IOR
     * @roseuid 3C2144A7032B
     */
    public CnxServCtrl(short p_Modo)
    {
        conectaServidor(p_Modo);
    }

    /**
     * @param p_Modo
     * @return boolean
     * @exception Tenta
     *                iniciar conexão com o ServCtrl.
     * @roseuid 3C44D5CD001B
     */
    public boolean conectaServidor(short p_Modo)
    {
        String Args[] = { "", "" };

        switch (p_Modo)
        {
            case 1:
                //System.out.println (DefsGerais.s_IP_SERV_CTRL+" -
                // "+DefsGerais.s_PORTA_SERV_CTRL+" -
                // "+DefsGerais.s_NOME_SERV_CTRL);NoUtil.getNo().getConexaoServAlarme().getPorta(), 
                iniciaOrb(Args, false, getNo().getConexaoServControle().getHost(),
                          getNo().getConexaoServControle().getPorta(),
                          getNo().getConexaoServControle().getNomeObjetoCorba());
                try
                {
                    s_iConexao = iConexaoHelper.narrow(m_Obj);
                    return true;
                }
                catch (Exception Exc)
                {
                    System.out.println("CnxServCtrl - conectaServidor():" + Exc);
                    s_iConexao = null;
                    return false;
                }
            case 2:
            	
            	String caminho = "//"+getNo().getHostName()+getNo().getDiretorioDefs().getS_DIR_ARQS_REF();
            	
                if (getIORArq(caminho, ArquivosDefs.s_ARQ_IOR_SERVCTRL))
                {
                    iniciaOrb(Args, true, getNo().getConexaoServControle().getHost(),
                              getNo().getConexaoServControle().getPorta(),
                              getNo().getConexaoServControle().getNomeObjetoCorba());
                    try
                    {
                        s_iConexao = iConexaoHelper.narrow(m_Obj);
                        return true;
                    }
                    catch (Exception Exc)
                    {
                        System.out.println("CnxServCtrl - conectaServidor():" + Exc);
                        s_iConexao = null;
                        return false;
                    }
                }
                break;
        }

        return false;
    }

    /**
     * @return boolean
     * @exception
     * @roseuid 3C8F79740329
     */
    public boolean ping()
    {
        boolean Retorno = false;

        for (int j = 0; j < 2; j++)
        {
            try
            {
                System.out.println("CnxServCtrl - ping(): Pingando no ServCtrl... Tentativa " + j);
                s_iConexao.fnPing();
                System.out.println("CnxServCtrl - ping(): ServCtrl respondeu ao ping... Tentativa " + j);
                Retorno = true;
                break;
            }
            catch (Exception Exc)
            {
                System.out.println("CnxServCtrl - ping(): Servidor de Controle fora do ar... Tentativa " + j);
                //System.out.println("CnxServCtrl - ping(): "+Exc);
                conectaServidor(getNo().getConexaoServControle().getModoConexao());
                Retorno = false;
            }
            if (Retorno) break;
        }
        return Retorno;
    }

    /**
     * @return Vector
     * @exception
     * @roseuid 3C44962802E2
     */
    public Vector getListaProcessos()
    {
        ProcessoDef Processo = null;
        iProcControlado ProcessoServ = null;
        iListaProcControlados ListaProcessosServ = null;
        MeuStrVector Perfis = null;
        Vector ListaProcessos = null;

        if (ping() == false) return null;

        for (int j = 0; j < 3; j++)
        {
            try
            {
                ListaProcessosServ = s_iConexao.fnGetListaProcControlados(DefsComum.s_USR_ADMIN, DefsComum.s_PRF_ADMIN);
                ProcessoServ = ListaProcessosServ.fnGetInicio((short) 0);

                if (ProcessoServ != null) Perfis = new MeuStrVector(false);
                else System.out.println("getListaProcessos(): Nao ha processos na lista");

                while (ProcessoServ != null)
                {
                	iTipoProc tipoProc = ProcessoServ.fnGetTipoProc();
                	
                    Processo = new ProcessoDef(ProcessoServ.fnGetPID(), ProcessoServ.fnGetNomeExec(), ProcessoServ.fnGetUsuario(), ProcessoServ.fnGetPerfil(),
                                               ProcessoServ.fnGetTipoCli(), ProcessoServ.fnGetTipoProc(), ProcessoServ.fnGetDataInicio(),
                                               ProcessoServ.fnGetDataAcesso());

                    if (ProcessoServ.fnGetHost() == null || ProcessoServ.fnGetHost().length() == 0)
                    {
                        Processo.setHost(this.getNo().getHostName());
                    }
                    
                    Processo.setDataInicioStr(ProcessoServ.fnGetDataInicioStr());
                    Processo.setUltimoAcessoStr(ProcessoServ.fnGetUltimoAcessoStr());

                    if (Processo.getNome().toLowerCase().startsWith("servagn") == true || Processo.getNome().toLowerCase().startsWith("servctrl") == true
                        || Processo.getNome().toLowerCase().startsWith("servhist") == true)
                    {
                        Processo.setUsuario("N/A");
                        Processo.setPerfil("N/A");
                    }
                    else
                    {
                        // Adiciona perfil no vetore para futura organização
                        Perfis.insereElementoOrd(Processo.getPerfil());
                    }
                    
                    //Filtrando os clientes para processamento distribuido do relatorio
                    if(tipoProc != iTipoProc.T_PROC_CLIENTE_DIST)
                	{
	                    if (ListaProcessos == null)
	                    {
	                        ListaProcessos = new Vector();
	                        ListaProcessos.addElement(Processo);
	                    }
	                    else
	                    {
	                        boolean Inseriu = false;
	                        // Insere os elementos ordenadamente
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
	
	                        if (Inseriu == false) ListaProcessos.addElement(Processo);
	                    }
                	}
                    ProcessoServ = ListaProcessosServ.fnGetProx2(ProcessoServ);
                }
                break;
            }
            catch (Exception Exc)
            {
                System.out.println("CnxServCtrl - getListaProcessos(): Servidor de Controle fora do ar");
                System.out.println("CnxServCtrl - getListaProcessos(): " + Exc);
                conectaServidor(NoUtil.getNo().getConexaoServControle().getModoConexao());
            }
        }
        //System.out.println("CnxServCtrl - Peguei lista");
        if (ListaProcessos != null)
        {
            ListaProcessos.trimToSize();
            // #######
            // # Organiza a lista de processos
            // #######
            // Primeiros os servidores "fixos"
            String Servidores[] = { "servctrl", "servagn", "servhist" };
            int Posicoes[] = { 0, 1, 2 }, ProxPosicao = 0;
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < ListaProcessos.size(); j++)
                {
                    Processo = (ProcessoDef) ListaProcessos.elementAt(j);
                    if (Processo.getNome().toLowerCase().startsWith(Servidores[i]))
                    {
                        if (j != Posicoes[i])
                        {
                            ListaProcessos.removeElementAt(j);
                            ListaProcessos.add(Posicoes[i], Processo);
                            ProxPosicao = Posicoes[i] + 1;
                            break;
                        }
                        else break;
                    }
                }
            }

            return ListaProcessos;
        }
        else return null;
    }

    /**
     * @return Vector
     * @exception
     * @roseuid 3C85532B03B4
     */
    public Vector getUsuariosCDRView()
    {
        ProcessoDef Processo;
        UsuarioDef Usuario;
        Vector Processos = null, Usuarios = null;

        Processos = getListaProcessos();
        if (Processos != null)
        {
            for (int i = 0; i < Processos.size(); i++)
            {
                Processo = (ProcessoDef) Processos.elementAt(i);
                if (Processo.getNome().toLowerCase().startsWith("servcli") == true)
                {
                    Usuario = new UsuarioDef(Processo.getUsuario(), Processo.getPerfil(), "", "", "", 0, 0, (short) 0, (short) 0, Processo.getDataInicioStr(),
                    		"","","","","","","","","");
                    Usuario.setDataAcesso(Processo.getDataInicio());
                    Usuario.setDataAcessoStr(Processo.getDataInicioStr());

                    if (Usuarios == null) Usuarios = new Vector();
                    Usuarios.add(Usuario);
                }
            }
        }

        if (Usuarios != null) Usuarios.trimToSize();
        return Usuarios;
    }

    /**
     * @param p_Id
     * @return String
     * @exception
     * @roseuid 3C87C6BC002D
     */
    public String finalizaProcesso(int p_Id)
    {
        String Retorno = "";
        iListaProcControlados ListaProcessos = null;

        ListaProcessos = s_iConexao.fnGetListaProcControlados("administrador", "admin");
        if (ListaProcessos != null)
        {
            ListaProcessos.fnMarcaProc(p_Id, (short) 0);
            Retorno = "Processo " + p_Id + " marcado para finalização.\n";
            return Retorno;
        }
        return null;
    }

    /**
     * @param p_Usuario
     * @return void
     * @exception
     * @roseuid 3C96466F03CF
     */
    public void desconectaCliente(String p_Usuario)
    {
        String Usuario = null;
        int DataAcesso = 0;

        Usuario = p_Usuario.substring(0, p_Usuario.indexOf("-"));
        DataAcesso = Integer.parseInt(p_Usuario.substring(p_Usuario.indexOf("-") + 1, p_Usuario.length()));

        InetAddress localaddr = null;
		try
		{
			localaddr = InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
        s_iConexao.fnDesconectaCliente(Usuario, DataAcesso, localaddr.getHostAddress());
    }

    /**
     * @param p_PID
     * @return Vector
     * @exception
     * @roseuid 3C9750F90031
     */
    public Vector getProcesso(int p_PID, String p_Host)
    {
        ProcessoDef Processo;
        Vector Processos = getListaProcessos();

        if (Processos != null)
        {
            for (int i = 0; i < Processos.size(); i++)
            {
                Processo = (ProcessoDef) Processos.elementAt(i);
                if (Processo.getId() == p_PID  &&
    				(Processo.getHost().equals(p_Host) == true))
                {
                    Vector Retorno = new Vector();
                    Retorno.add(Processo.getNome());
                    Retorno.add(Integer.toString(p_PID));
                    Retorno.add(Processo.getHost());
                    Retorno.add(Processo.getUsuario());
                    Retorno.add(Processo.getPerfil());
                    Retorno.add(Processo.getDataInicioStr());
                    Retorno.add(Processo.getUltimoAcessoStr());
                    Retorno.add("Sem Informação");
                    Retorno.trimToSize();
                    return Retorno;
                }
            }
        }
        return null;
    }
}