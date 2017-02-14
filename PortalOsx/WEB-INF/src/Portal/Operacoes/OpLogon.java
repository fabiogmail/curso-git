//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpLogon.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Map;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Arquivo;
import Portal.Utils.UsuarioDef;
import br.com.visent.segurancaAcesso.action.ConfigAction;
import br.com.visent.segurancaAcesso.domain.Config;
import br.com.visent.segurancaAcesso.service.EmailAjax;

/**
 * Operação de logon do usuário.
 */
public class OpLogon extends OperacaoAbs 
{

    /**
     * @return @exception
     * @roseuid 3BF55FC40390
     */
	private  boolean NomeIgualSenha = false;
    public OpLogon()
    {
    }

    /**
     * @param p_Mensagem
     * @return boolean
     * @exception Inicia
     *                a operação a ser realizada.
     * @roseuid 3BF573B7020D
     */
    public boolean iniciaOperacao(String p_Mensagem)
    {
        //System.out.println("OpLogon - iniciaOperacao()");
        String Usuario = null, Senha = null, Logar = null, motivo = null;
        UsuarioDef UsuarioD = null;
        short RetLogon = -100;
       
        
        try
        {
        	
        	if(m_Request.getParameter("motivo") == null){
        		motivo = "";
        	}else{
        		motivo = m_Request.getParameter("motivo")+"";
        	}
        	
            if (p_Mensagem.equals("$ARG;") == true)
            {
                Usuario = m_Request.getParameter("usuario")+"";
                Senha = m_Request.getParameter("senha")+"";
                
            }
            else
            {
                Usuario = p_Mensagem.substring(0, p_Mensagem.indexOf("-"));
                Senha = p_Mensagem.substring(p_Mensagem.indexOf("-") + 1, p_Mensagem.length());
            }
            
            if(Usuario.equals(Senha)){
            	NomeIgualSenha = true;
            }
//            No noTmp = null;
//
//            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
//            {
//                noTmp = (No) iter.next();
//                RetLogon = noTmp.getConexaoServUtil().logon(Usuario, Senha, m_IPRemoto, m_HostRemoto, m_Request.getSession().getId(), motivo);
//                
//                if( (RetLogon != DefsComum.s_RET_ERRO_SERV_UTIL && RetLogon != DefsComum.s_RET_USR_NAO_CADASTRADO && RetLogon != DefsComum.s_RET_SISTEMA_EM_MANUT) ){//se não for possível encontrar o usuário.
//                	break;
//                }
//             
//            }
            RetLogon = NoUtil.getNo().getConexaoServUtil().logon(Usuario, Senha, m_IPRemoto, m_HostRemoto, m_Request.getSession().getId(), motivo);
        	
            if ( RetLogon == DefsComum.s_RET_USR_ADMINISTRADOR || RetLogon == DefsComum.s_RET_USR_COMUM || RetLogon == DefsComum.s_RET_USR_COMUM2)
            {
                UsuarioD = NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(m_Request.getSession().getId());
            }
            
            	

        }
        catch (Exception ExcLogon)
        {
            System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " - OpLogon - iniciaOperacao(): " + ExcLogon);
            ExcLogon.printStackTrace();
            logonErroServUtil();
        }
        
        UsuarioDef NovoUsuario;
        
        try
        {
            // Envia a página de acordo com a resposta do servidor
            switch (RetLogon)
            {
                case DefsComum.s_RET_ERRO_SERV_UTIL:
                    logonErroServUtil();
                    break;
                case DefsComum.s_RET_LOGON_INVALIDO:
                    logonInvalido();
                    break;
                case DefsComum.s_RET_USR_ADMINISTRADOR:
                    //bUsuarioLogou = true;
                    logonUsuario(RetLogon, UsuarioD);
                    break;
                case DefsComum.s_RET_USR_COMUM:
                case DefsComum.s_RET_USR_COMUM2:
                    //bUsuarioLogou = true;
                    logonUsuario(RetLogon, UsuarioD);
                    break;
                case DefsComum.s_RET_USR_JA_LOGADO:
                    logonUsrjaLogado(Usuario);
                    break;
                case DefsComum.s_RET_USR_NAO_CADASTRADO:
                    logonUsrNaoCad();
                    break;
                case DefsComum.s_RET_SISTEMA_EM_MANUT:
                    logonSistemaManut();
                    break;
                case DefsComum.s_RET_QTD_USUARIOS_ESG:
                    logonUsuariosEsg();
                    break;
                case DefsComum.s_RET_PERFIL_BLOQ:
                    logonPerfilBloqueado();
                    break;
            }
            
            if (UsuarioD != null)
            {
                NovoUsuario = NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(m_Request.getSession().getId());
                NovoUsuario.setSessaoHTTP(m_Request.getSession());
                NovoUsuario.setIDSessao(m_Request.getSession().getId());
                NovoUsuario.setNo(NoUtil.getNo());
                
                NoUtil.getNo().getUsuarioLogados().put(m_Request.getSession().getId(), NovoUsuario);    
            }
            else System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " - OpLogon - iniciaOperacao(): UsuarioD = NULL");


            
        }
        catch (Exception Exc)
        {
            System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " - OpLogon - iniciaOperacao(): Erro ao enviar a página");
            System.out.println(NoUtil.getNo().getConexaoServUtil().getDataHoraAtual() + " - OpLogon - iniciaOperacao(): " + Exc);
            Exc.printStackTrace();
        }
        return true;
    }

    public void criaConexaoServUtil(No noTmp)
    {
        if (noTmp.getConexaoServUtil().getM_iUtil() == null)
        {
            noTmp.createConexao(DefsComum.s_ServUtil,
                                noTmp.getConexaoServUtil().getModoConexao(), 
                                noTmp.getHostName(), 
                                noTmp.getConexaoServUtil().getNomeObjetoCorba(),
                                noTmp.getConexaoServUtil().getPorta());
        }
    }
    
    /**
     * @param p_RetLogin
     * @param p_Usuario
     * @return void
     * @exception Envia
     *                a página do Portal do usuário comum.
     * @roseuid 3BFC5DF200F7
     */
    public void logonUsuario(short p_RetLogin, UsuarioDef p_Usuario)
    {
    	boolean monitorado = NoUtil.getNoCentral().getConexaoServUtil().
    						verificaUsuMonitorado(p_Usuario.getUsuario());
    	if(monitorado){
    		boolean enviar = false;
    		ConfigAction configAction = new ConfigAction();
    		Config config = configAction.buscarPorNome("HABILITAR_EMAIL");
    		if(config != null){
    			if(config.getValor().equalsIgnoreCase("1")){
    				enviar = true;
    			}
    		}    		
    		if(enviar){
    			try{        		
            		
                	Config confEmails = configAction.buscarPorNome("EMAILS_ALM");
                	if(confEmails != null){
                		if(!confEmails.getValor().equals("")){
                			String[] emails = confEmails.getValor().split(";");
                			EmailAjax email = new EmailAjax();
                			email.iniciar();
                			String assunto = "Login: "+p_Usuario.getUsuario();
                			String msg = "Usuário "+p_Usuario.getUsuario()+" logou no Portal CDRView ("+new java.util.Date()+")";
                			email.enviar(emails,assunto,msg);    			
                		}
                	}else{
                		System.out.println("Usuário nao foi monitorado(cadastre as informações de email)");
                	}
            	}catch (Exception e) {
            		System.out.println("Problema ao alertar Login do usuario "+p_Usuario.getUsuario()+" por email");
            	}
    		}    		
    	}    	
    	
        Arquivo ArqInfo = new Arquivo();
        String Linha = null, Info = "";
        
        try
        {
        	if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro")||DefsComum.s_CLIENTE.equalsIgnoreCase("gvt")||DefsComum.s_CLIENTE.equalsIgnoreCase("nextel")
        			||DefsComum.s_CLIENTE.equalsIgnoreCase("sercomtel") || DefsComum.s_CLIENTE.equalsIgnoreCase("BrasilTelecom")
        			||DefsComum.s_CLIENTE.equalsIgnoreCase("Telemig") || DefsComum.s_CLIENTE.equalsIgnoreCase("TimSul")
        			|| DefsComum.s_CLIENTE.equalsIgnoreCase("Amazonia_Celular") || DefsComum.s_CLIENTE.equalsIgnoreCase("Embratel")
        			|| DefsComum.s_CLIENTE.equalsIgnoreCase("Nextel") || DefsComum.s_CLIENTE.equalsIgnoreCase("CTBC") || DefsComum.s_CLIENTE.equalsIgnoreCase("ACME")
        			|| DefsComum.s_CLIENTE.equalsIgnoreCase("Tim") || DefsComum.s_CLIENTE.equalsIgnoreCase("Oi") || DefsComum.s_CLIENTE.equalsIgnoreCase("Anatel"))//clientes que teram alteracao de senha no primeiro acesso
        	{
        		/*if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro") && 
        				!p_Usuario.getPerfil().equals(DefsComum.s_PRF_ADMIN)){
        			
        			No noTmp = null;
        			UsuarioDef usuario = null;
        			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();) {
        				noTmp = (No) iter.next();
        				usuario = noTmp.getConexaoServUtil().getUsuario(p_Usuario.getUsuario());
        				if(usuario != null){
        					break;
        				}
        			}        			
        			
            		if(usuario.getNome().equals("") || 
            			usuario.getEmail().equals("") || 
            			usuario.getRamal().equals("") ||
            			usuario.getTelefone().equals("") ||
            			usuario.getRegional().equals("") ||
            			usuario.getArea().equals("") ||
            			usuario.getResponsavel().equals("") ||
            			NomeIgualSenha)
            		{    
            			m_Request.getSession().setAttribute("usuario_web",p_Usuario.getUsuario());
            			m_Request.getSession().setAttribute("senha_web",p_Usuario.getSenha());
            			m_Request.getSession().setAttribute("perfil_web",p_Usuario.getPerfil());
            			if(NomeIgualSenha){
            				m_Request.getSession().setAttribute("ver_senha","1");
            			}else{
            				m_Request.getSession().setAttribute("ver_senha","0");
            			}
            			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/jsp/atualizaCadastro.jsp");
            		}else{
    	        		m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/paginas/index.jsp");
    	        	}    
            		
            	}else{
            		if(NomeIgualSenha){
    	        		m_Request.setAttribute("senha",p_Usuario.getSenha());
    	        		m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/jsp/senhaNova.jsp");
    	        	}else{
    	        		m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/paginas/index.jsp");
    	        	}            		
            	}*/
        		if(NomeIgualSenha){
	        		m_Request.setAttribute("senha",p_Usuario.getSenha());
	        		m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/jsp/senhaNova.jsp");
	        	}else{
	        		m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/paginas/index.jsp");
	        	}         		
	        	
        	}
        	else
        	{
        		m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/paginas/index.jsp");
        	}
        }
        catch (Exception e)
        {
            System.out.println("Nao achou pagina de login");
        }
        /*
         * 
         * iniciaArgs(3); m_Args[0] =
         * NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB(); m_Args[1] =
         * ArquivosDefs.s_HTML_LOGIN; m_Args[2] = "@";
         * m_Html.enviaArquivo(m_Args);
         */
    }

    /**
     * @return void
     * @exception Envia
     *                mensagem de logon inválido.
     * @roseuid 3BFC5E0F0207
     */
    public void logonInvalido()
    {
        try
        {
        	m_Request.getSession().setAttribute("mensagem","Login incorreto!");
			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    /**
     * @return void
     * @exception Envia
     *                mensagem de logon de usuário não cadastrado. Por motivos
     *                de segurança, deve ser a mesma mensagem do logon inválido.
     * @roseuid 3BFC5E2F02F4
     */
    public void logonUsrNaoCad()
    {
        logonInvalido();
    }

    /**
     * @param p_Usuario
     * @return void
     * @exception Envia
     *                mensagem de que o usuário já está logado.
     * @roseuid 3BFC5E8A03A9
     */
    public void logonUsrjaLogado(String p_Usuario)
    {
        boolean Achou = false;
        Iterator ItUsuarios = null;
        UsuarioDef Usuario = null;
        String Mensagem = "!";

        Map usrLogados = NoUtil.atualizaMapUsuariosLogados();
        
        synchronized (usrLogados)
        {
            ItUsuarios = usrLogados.values().iterator();

            while (ItUsuarios.hasNext())
            {
                Usuario = (UsuarioDef) ItUsuarios.next();
                if (Usuario.getUsuario().equals(p_Usuario))
                {
                    Achou = true;
                    break;
                }
            }
        }

        if (Achou) Mensagem += p_Usuario + "+" + Usuario.getHost() + "+" + Usuario.getDataAcessoStr();

//        iniciaArgs(3);
//        m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//        m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//        m_Args[2] = Mensagem;
//        m_Html.enviaArquivo(m_Args);
        try
        {
        	m_Request.getSession().setAttribute("mensagem",Mensagem);
			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    /**
     * @return void
     * @exception Envia
     *                mensagem de houve erro no Servidor Util.
     * @roseuid 3BFC5EB403DB
     */
    public void logonErroServUtil()
    {
//        iniciaArgs(3);
//        m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//        m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//        m_Args[2] = "Erro ao conectar com o Servidor! Contacte o administrador.";
//        m_Html.enviaArquivo(m_Args);
     
        try
        {
        	m_Request.getSession().setAttribute("mensagem","Erro ao conectar com o Servidor! Contacte o administrador.");
			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
    
    /**
     * @return void
     * @exception Envia
     *                mensagem de que o sistema está em manutenção (QtdMaxUsr =
     *                0).
     * @roseuid 3C0665D8011F
     */
    public void logonSistemaManut()
    {
//        iniciaArgs(3);
//        m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//        m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//        m_Args[2] = "O sistema está em manutenção!";
//        m_Html.enviaArquivo(m_Args);
    	try
        {
        	m_Request.getSession().setAttribute("mensagem","O sistema está em manutenção!");
			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    /**
     * @return void
     * @exception Envia
     *                mensagem de que a quantidade máxima de usuários já foi
     *                atingida.
     * @roseuid 3C0666160377
     */
    public void logonUsuariosEsg()
    {
//        iniciaArgs(3);
//        m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//        m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//        m_Args[2] = "Limite de usuários conectados já foi atingido!";
//        m_Html.enviaArquivo(m_Args);
        try
        {
        	m_Request.getSession().setAttribute("mensagem","Limite de usuários conectados já foi atingido!");
			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    /**
     * @return void
     * @exception Envia
     *                mensagem de que o perfil do usuário está bloqueado.
     * @roseuid 3C42D6620167
     */
    public void logonPerfilBloqueado()
    {
//        iniciaArgs(3);
//        m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//        m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//        m_Args[2] = "O seu perfil encontra-se bloqueado! Contacte o administrador.";
//        m_Html.enviaArquivo(m_Args);
        try
        {
        	m_Request.getSession().setAttribute("mensagem","O seu perfil encontra-se bloqueado! Contacte o administrador.");
			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
}
