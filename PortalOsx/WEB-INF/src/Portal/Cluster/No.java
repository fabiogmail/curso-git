
package Portal.Cluster;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.omg.CORBA.COMM_FAILURE;

import Portal.Conexao.CnxServAlr;
import Portal.Conexao.CnxServCtrl;
import Portal.Conexao.CnxServUtil;
import Portal.Conexao.Conexao;
import Portal.Configuracoes.DefsComum;
import Portal.Configuracoes.DiretoriosDefs;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsx
 * Arquivo criado em: 20/06/2005
 * 
 * @author Marcos Urata
 * @version 1.0
 * 
 * Breve Descrição: Classe que representa cada no do cluster.
 * 
 */

public class No {
    
    private boolean noCentral = false;
    private String hostName;
    private String ip;
    private String porta;
    private String portaRMI;
    
    private DiretoriosDefs diretorioDefs;
    
    private CnxServAlr conexaoServAlarme;
    private CnxServCtrl conexaoServControle;
    private CnxServUtil conexaoServUtil;
    
    private Map usuarioLogados = Collections.synchronizedMap(new TreeMap());
    
    public No()
    {
        diretorioDefs = new DiretoriosDefs();
    }
    
    public Conexao getConexao(String nome)
    {
        if (nome.equalsIgnoreCase(DefsComum.s_ServUtil))
            return conexaoServUtil;
        else if (nome.equalsIgnoreCase(DefsComum.s_ServCtrl))
            return conexaoServControle;
        else if (nome.equalsIgnoreCase(DefsComum.s_ServAlr))
            return conexaoServAlarme;
        else 
            throw new RuntimeException("Nome de Conexao Inválida!!");
    }
    
    /**
     * Factory para criar objetos Conexao. Os tipos de objetos retornados serao
     * referencias Corba dos Servidores de Alarme, Controle e Util.
     * 
     * @param p_Nome - Nome do Servidor
     * @param p_ModoConexao - Forma de Conexao com o Servidor Corba
     * @param p_Host - IP ou Hostname do Servidor Corba
     * @param p_Objeto - Nome do objeto Corba
     * @param p_Porta - Porta em que o Servidor Corba ira rodar
     * 
     * */
    public void createConexao(String p_Nome, 
                                 short p_ModoConexao, 
                                 String p_Host, 
                                 String p_Objeto,
                                 int p_Porta) 
    {
        if (p_Nome.equalsIgnoreCase(DefsComum.s_ServUtil))
        {
            conexaoServUtil =  criaConexaoServUtil(p_ModoConexao, p_Host, p_Objeto, p_Porta);
        }
        else if (p_Nome.equalsIgnoreCase(DefsComum.s_ServCtrl))
        {
            try
            {
                conexaoServControle = new CnxServCtrl(p_ModoConexao, p_Host, p_Objeto, p_Porta);
                conexaoServControle.setNo(this);
                conexaoServControle.conectaServidor(p_ModoConexao, p_Host, p_Objeto, p_Porta);
            }
            catch (Exception Exc)
            {
               System.out.println("Erro ao conectar com ServCtrl: "+Exc);
            }
        }
        else if (p_Nome.equalsIgnoreCase(DefsComum.s_ServAlr))
        {
            try
            {
                conexaoServAlarme = new CnxServAlr(p_ModoConexao, p_Host, p_Objeto, p_Porta);
                conexaoServAlarme.setNo(this);
                conexaoServAlarme.conectaServidor(p_ModoConexao, p_Host, p_Objeto, p_Porta);
            }
            catch (Exception Exc)
            {
               System.out.println("Erro ao conectar com ServAlr: "+Exc);
            }
        }
        else 
            throw new RuntimeException("Nome de Conexao Inválida!!");
    }
    
    
    public synchronized CnxServUtil criaConexaoServUtil(short p_ModoConexao, 
                                    String p_Host, 
                                    String p_Objeto,
                                    int p_Porta)
    {
        final int TEMPO_SLEEP = 1000; // 1 segundos

        conexaoServUtil = new CnxServUtil(p_ModoConexao, p_Host, p_Objeto, p_Porta);
        
        for (int i = 1; i <= 3; i++)
        {
            try
            {
                conexaoServUtil.criaConexaoUtil(p_ModoConexao, p_Host, p_Objeto, p_Porta);
                conexaoServUtil.limpaArquivosLock();
                conexaoServUtil.setNo(this);
                System.out.println(new java.util.Date()+"\n\nCONEXAO SERVUTIL ("+this.hostName+") REALIZADA COM SUCESSO!");
                break;
            }
            catch(COMM_FAILURE comFail)
            {
                System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+this.hostName+").");
                System.out.println(new Date()+" - TENTATIVA "+i+" DE RECONECTAR COM SERVIDOR UTIL ("+this.hostName+").");
                try
                {
                    Thread.sleep(TEMPO_SLEEP);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                } 
            }
            catch(org.omg.CORBA.OBJECT_NOT_EXIST corbaNExist)
            {
                System.out.println(new Date()+" - REFERENCIA DO SERVIDOR UTIL INVALIDA! ("+this.hostName+").");
                System.out.println(new Date()+" - TENTATIVA "+i+" DE RECONECTAR COM SERVIDOR UTIL ("+this.hostName+").");
                try
                {
                    Thread.sleep(TEMPO_SLEEP);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            catch (Exception Exc)
            {
               System.out.println(new Date()+" - ERRO AO CONECTAR COM O SERVUTIL ("+this.hostName+").");
               Exc.printStackTrace();
            }  
        }
        
        return conexaoServUtil;
    }
    
    
    
    public String toString()
    {
        return hostName;
    }
    public CnxServAlr getConexaoServAlarme()
    {
        return conexaoServAlarme;
    }
    
    public void setConexaoServAlarme(CnxServAlr conexaoServAlarme)
    {
        this.conexaoServAlarme = conexaoServAlarme;
    }
    
    public CnxServCtrl getConexaoServControle()
    {
        return conexaoServControle;
    }
    
    public void setConexaoServControle(CnxServCtrl conexaoServControle)
    {
        this.conexaoServControle = conexaoServControle;
    }
    
    public CnxServUtil getConexaoServUtil()
    {
        return conexaoServUtil;
    }
    
    public void setConexaoServUtil(CnxServUtil conexaoServUtil)
    {
        this.conexaoServUtil = conexaoServUtil;
    }
    
    public String getHostName()
    {
        return hostName;
    }
    
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public boolean isNoCentral()
    {
        return noCentral;
    }
    
    public void setNoCentral(boolean noCentral)
    {
        this.noCentral = noCentral;
    }

    public DiretoriosDefs getDiretorioDefs()
    {
        return diretorioDefs;
    }
    
    public void setDiretorioDefs(DiretoriosDefs diretorioDefs)
    {
        this.diretorioDefs = diretorioDefs;
    }

    public Map getUsuarioLogados()
    {
        return usuarioLogados;
    }
    
    public void setUsuarioLogados(Map usuarioLogados)
    {
        this.usuarioLogados = usuarioLogados;
    }
    public String getPorta()
    {
        return porta;
    }
    public void setPorta(String porta)
    {
        this.porta = porta;
    }
    public String getPortaRMI() {
		return portaRMI;
	}

	public void setPortaRMI(String portaRMI) {
		this.portaRMI = portaRMI;
	}
}
