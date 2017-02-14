//Source file: C:/usr/osx/CDRView/Servlet/Portal/Conexao/CnxServAlr.java

package Portal.Conexao;

import Portal.Configuracoes.ArquivosDefs;

/**
 */
public class CnxServAlr extends Conexao {
    public static InterfacesAlr.iConexao s_iConexao;

    /**
     * @param p_Modo
     * @return @exception
     *         Construtor. O parâmetro Modo define a maneira de obter o IOR do
     *         servidor. 0 - Não Realiza conexão 1 - Diretamente pelo IP e Porta
     *         2 - Pelo IOR
     * @roseuid 3CE17249015F
     */
    public CnxServAlr(short p_Modo)
    {
        conectaServidor(p_Modo);
    }

    /**
     * @param p_Modo
     * @return boolean
     * @exception Tenta
     *                iniciar conexão com o ServAlr.
     * @roseuid 3CE172490173
     */
    public boolean conectaServidor(short p_Modo)
    {
        String Args[] = { "", "" };

        switch (p_Modo)
        {
            case 1:
                iniciaOrb(Args, false, getNo().getConexaoServAlarme().getHost(), getNo().getConexaoServAlarme().getPorta(),
                          getNo().getConexaoServAlarme().getNomeObjetoCorba());
                try
                {
                    s_iConexao = InterfacesAlr.iConexaoHelper.narrow(m_Obj);
                    return true;
                }
                catch (Exception Exc)
                {
                    System.out.println("CnxServAlr - conectaServidor():" + Exc);
                    s_iConexao = null;
                    return false;
                }
            case 2:
            	
//            	  String caminho = "//"+getNo().getHostName()+getNo().getDiretorioDefs().getS_DIR_ARQS_REF();
//            	
//                if (getIORArq(caminho, ArquivosDefs.s_ARQ_IOR_SERVCTRL))
//                {
//                    iniciaOrb(Args, true, getNo().getConexaoServAlarme().getHost(), getNo().getConexaoServAlarme().getPorta(),
//                              getNo().getConexaoServAlarme().getNomeObjetoCorba());
//                    try
//                    {
//                        s_iConexao = InterfacesAlr.iConexaoHelper.narrow(m_Obj);
//                        return true;
//                    }
//                    catch (Exception Exc)
//                    {
//                        System.out.println("CnxServAlr - conectaServidor():" + Exc);
//                        s_iConexao = null;
//                        return false;
//                    }
//                }
                break;
        }

        return false;
    }

    public CnxServAlr(short p_ModoConexao, String p_Host, String p_Objeto, int p_Porta)
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
                    s_iConexao = InterfacesAlr.iConexaoHelper.narrow(m_Obj);
                    return true;
                }
                catch (Exception Exc)
                {
                    System.out.println("CnxServAlr - conectaServidor():" + Exc);
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
                        s_iConexao = InterfacesAlr.iConexaoHelper.narrow(m_Obj);
                        return true;
                    }
                    catch (Exception Exc)
                    {
                        System.out.println("CnxServAlr - conectaServidor():" + Exc);
                        s_iConexao = null;
                        return false;
                    }
                }
                break;
        }

        return false;
    }

    /**
     * @return String
     * @exception
     * @roseuid 3CE1796101EC
     */
    public String getIOR()
    {
        String IOR = null;
        IOR = m_Orb.object_to_string(s_iConexao);
        return IOR;
    }

    /**
     * @return boolean
     * @exception
     * @roseuid 3CE172490187
     */
    public boolean ping()
    {
        boolean Retorno = false;
        InterfacesAlr.iServidorAlr ServAlr = null;

        for (int j = 0; j < 2; j++)
        {
            try
            {
                ServAlr = s_iConexao.fnGetServidorAlr();
                ServAlr.fnPing();
                Retorno = true;
                break;
            }
            catch (Exception Exc)
            {
                System.out.println("CnxServAlr - ping(): Servidor de Alarmes fora do ar");
                conectaServidor(getNo().getConexaoServAlarme().getModoConexao());
                Retorno = false;
            }
        }
        return Retorno;
    }
}