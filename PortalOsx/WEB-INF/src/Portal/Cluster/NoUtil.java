package Portal.Cluster;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsxTest Arquivo criado em:
 * 22/06/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: Classe utilitaria responsavel em retornar informacoes dos Nos
 *  
 */

public class NoUtil {

    private static No no;
    public static boolean noLocalSetado = false;

    private NoUtil()
    {
    }

    public static List listaDeNos = new ArrayList();

    public static No getNo()
    {
        String hostName = null, ipAddress = null, porta = null;

        if (no == null)
        {
            try
            {
                InetAddress inetAdd = InetAddress.getByName("127.0.0.1");
                hostName = inetAdd.getLocalHost().getHostName();
                ipAddress = inetAdd.getLocalHost().getHostAddress();
                
                for (Iterator iter = listaDeNos.iterator(); iter.hasNext();)
                {
                    No noTmp = (No) iter.next();

                    if (noTmp.getHostName().equalsIgnoreCase(hostName) || noTmp.getIp().equals(ipAddress))
                    {
                        no = noTmp;
                        return no;
                    }
                }
            }
            catch (java.net.UnknownHostException uhe)
            {
                System.out.println("Nao foi possivel pegar o hostname da maquina: " + uhe);
            }

            throw new RuntimeException(new java.util.Date() + " - No com IP " + ipAddress + " e nome " + hostName
                                       + " nao encontrado. Verifique as configuracoes dos Nos" + " no arquivo DefsWeb.xml!");
        }

        return no;
    }
    
    public static void setNoLocal(String host, String porta)
	{
    	for(Iterator iter = listaDeNos.iterator(); iter.hasNext();)
    	{
    		No noTmp = (No) iter.next();
    		if(noTmp.getHostName().equals(host) || noTmp.getIp().equals(host))
    		{
				if(noTmp.getPorta().equals(porta))
				{
					no = noTmp;
					noLocalSetado = true;
				}
    		}
    	}
    	//Em alguns casos o ip retornado pelo servidor nao é o mesmo usado para configuração
    	//Nesses casos( na Claro por exemplo ) eu faço outra varredura, somente verificando 
    	//pela porta
    	if(!noLocalSetado)
    	{
    		for(Iterator iter = listaDeNos.iterator(); iter.hasNext();)
        	{
        		No noTmp = (No) iter.next();
				if(noTmp.getPorta().equals(porta))
				{
					no = noTmp;
					noLocalSetado = true;
				}
        	}
    	}
    	
    	//Se mesmo assim não encontrar, é porque não existe configuração no DefsWeb
    	if(!noLocalSetado)
    	{
    		throw new RuntimeException(new java.util.Date() + " - No com IP " + host + " e nome " + porta
                    + " nao encontrado. Verifique as configuracoes dos Nos" + " no arquivo DefsWeb.xml!");
    	}
	}
    
    public static No getNoCentral()
    {
        for (Iterator iter = listaDeNos.iterator(); iter.hasNext();)
        {
            No noTmp = (No) iter.next();

            if (noTmp.isNoCentral())
            {
                return noTmp;
            }
        }
        throw new RuntimeException(new java.util.Date() + " - Nao Existe nenhum no configurado como 'No Central'. Verifique as configuracoes dos Nos"
                                   + " no arquivo DefsWeb.xml!");
    }

    /**
     * Retorna o No do cluster com o hostname informado.
     * 
     * @param hostname
     * @return No com o hostname informado.
     */
    public static No getNoByHostName(String hostName)
    {
        String ipAddress = null;
        No no = null;

        for (Iterator iter = listaDeNos.iterator(); iter.hasNext();)
        {
            No noTmp = (No) iter.next();

            if (noTmp.getHostName().equalsIgnoreCase(hostName))
            {
                no = noTmp;
            }
        }
        
        if(no != null)
        {
        	return no;
        }
        else
        {
        	System.out.println("getNoByHostName() - No com HOSTNAME "+hostName+" não encontrado retornando o No Central");
        	return NoUtil.getNoCentral();
        }
        
        //throw new RuntimeException(new java.util.Date() + " - No com HOSTNAME " + hostName + " nao encontrado. Verifique as configuracoes dos Nos"
        //                           + " no arquivo DefsWeb.xml!");
    }

    public static boolean pingListaServUtil()
    {
        boolean servidorOnline = false;

        for (Iterator iter = listaDeNos.iterator(); iter.hasNext();)
        {
            No noTmp = (No) iter.next();

            //            servidorOnline = noTmp.getConexaoServUtil().ping();
            if (servidorOnline == false) return false;
        }
        return true;
    }

    /**
     * Metodo responsavel em retornar um usuario. Primeiro, verifica-se se o
     * usuario com o ID da HttpSession passado existe na lista do No do
     * servidor. Caso nao exista, as listas de todos os Nos sao novamente
     * preenchidas.
     * 
     * @param httpSessionId
     * @return UsuarioDef
     *  
     */
    public static UsuarioDef getUsuarioLogado(String httpSessionId)
    {
        Map mapUsuariosLogados = atualizaMapUsuariosLogados();
        UsuarioDef usuarioLogado = (UsuarioDef) mapUsuariosLogados.get(httpSessionId);

        return usuarioLogado;
    }

    /**
     * Metodo responsavel em atualizar a lista de usuarios de todos os Nos do
     * cluster.
     * 
     * @return Retorna um Map atualizado com todos os usuarios logados.
     */
    public static Map atualizaMapUsuariosLogados()
    {
        Map mapUsuarios = Collections.synchronizedMap(new TreeMap());

        UsuarioDef usuario = null, usrTmp = null;
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            noTmp = (No) iter.next();

            synchronized (noTmp.getUsuarioLogados())
            {
                try
                {
	                if (noTmp.getConexaoServUtil().getListaUsuarios() != null)
	                {
	                    for (Iterator iter2 = noTmp.getConexaoServUtil().getListaUsuarios().iterator(); iter2.hasNext();)
	                    {
	                        usuario = (UsuarioDef) iter2.next();
	                        usrTmp = (UsuarioDef) noTmp.getUsuarioLogados().get(usuario.getIDSessao());
	                        
	                        /**
	                         * Obs. Importante!! O usuario que vem do servUtil nao possui o objeto HTTPSession.
	                         * Por esse motivo, apenas quando o usuario retornado nao existir na lista  de
	                         * usuarios do No eh que sera inserido no Map o usuario vindo do ServUtil. 
	                         * */
	                        if (usrTmp == null)
	                        {
	                            noTmp.getUsuarioLogados().put(usuario.getIDSessao(), usuario);
	                            mapUsuarios.put(usuario.getIDSessao(), usuario);
	                        }
	                        else
	                        {
	                            mapUsuarios.put(usuario.getIDSessao(), usrTmp);
	                        }
	                    }
	                }
                }
                catch(NullPointerException npe)
                {
                    System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
                    noTmp.createConexao(DefsComum.s_ServUtil,
                                        noTmp.getConexaoServUtil().getModoConexao(),
                                        noTmp.getHostName(),
                                        noTmp.getConexaoServUtil().getNomeObjetoCorba(),
                                        noTmp.getConexaoServUtil().getPorta());
                }
                catch(COMM_FAILURE comFail)
                {
                    System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
                    noTmp.createConexao(DefsComum.s_ServUtil,
                                        noTmp.getConexaoServUtil().getModoConexao(),
                                        noTmp.getHostName(),
                                        noTmp.getConexaoServUtil().getNomeObjetoCorba(),
                                        noTmp.getConexaoServUtil().getPorta());
                }
                catch(BAD_OPERATION badOp)
                {
                    System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
                    badOp.printStackTrace();
                }
                catch(org.omg.CORBA.OBJECT_NOT_EXIST corbaNExist)
                {
                    noTmp.createConexao(DefsComum.s_ServUtil,
                                        noTmp.getConexaoServUtil().getModoConexao(),
                                        noTmp.getHostName(),
                                        noTmp.getConexaoServUtil().getNomeObjetoCorba(),
                                        noTmp.getConexaoServUtil().getPorta());
                }
            }
        }

        return mapUsuarios;
    }
    
    public static boolean isAmbienteEmCluster()
    {
        return (listaDeNos.size() > 1) ? true : false;
    }
    
    public static No buscaNobyIDSessaoUsuario(String idSessaoUsuario)
    {
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	            if (noTmp.getConexaoServUtil().existeUsuariobySessao(idSessaoUsuario)) return noTmp;
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

        throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo usuario com ID Sessao " + idSessaoUsuario);
    }

    public static No buscaNobyNomeUsuario(String nomeUsuario)
    {
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	            if (noTmp.getConexaoServUtil().existeUsuariobyNome(nomeUsuario)) return noTmp;
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

        throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo usuario " + nomeUsuario);
    }

    public static No buscaNobyNomePerfil(String nomePerfil)
    {
    	if(nomePerfil.equals(DefsComum.s_PRF_ADMIN))
    		return getNoCentral();
    	
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	            if (noTmp.getConexaoServUtil().existePerfilbyNome(nomePerfil)) return noTmp;
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

        throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo perfil " + nomePerfil);
    }

    public static No buscaNobyIDPerfil(int idPerfil)
    {
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	            if (noTmp.getConexaoServUtil().existePerfilbyID(idPerfil)) return noTmp;
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

        throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo perfil com id " + idPerfil);
    }

    public static No buscaNobyCfgConversor(String configuracaoConversor)
    {
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	
	            if (noTmp.getConexaoServUtil().trataConversor(configuracaoConversor))
	            {
	                return noTmp;
	            }
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
        return noTmp;
    }
    
    public static No buscaNobyCfgReprocessador(String configuracaoReprocessador)
    {
    	//TODO Trocar interfaces para as de Reprocessadores
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	
	            if (noTmp.getConexaoServUtil().trataReprocessador(configuracaoReprocessador))
	            {
	                return noTmp;
	            }
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
        return noTmp;
    }

    public static No buscaNobyCfgParserGen(String configuracaoParserGen)
    {
    	//TODO Trocar interfaces para as de Reprocessadores
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	
	            if (noTmp.getConexaoServUtil().trataParserGen(configuracaoParserGen))
	            {
	                return noTmp;
	            }
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
        return noTmp;
    }
    
    
    public static No buscaNobyBilhetador(String bilhetador)
    {
        No noTmp = null;

        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            try
            {
	            noTmp = (No) iter.next();
	
	            if (noTmp.getConexaoServUtil().trataBilhetador(bilhetador))
	            {
	                return noTmp;
	            }
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

        throw new RuntimeException(new Date() + " - Nao existe nenhum Servidor de Util responsavel pelo Bilhetador " + bilhetador);
    }

}