/*
 * Created on 27/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Portal.Operacoes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 * @author osx
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OpIndicaLogon extends OperacaoAbs 
{ 
	
	/**
     * @return @exception
     * @roseuid 3BF55FC40390
     */
    public OpIndicaLogon()
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
    	String Usuario = m_Request.getParameter("usuario");
    	String Senha = m_Request.getParameter("senha");
    	String motivo = m_Request.getParameter("motivo");
        No noLogin = null;
        short RetLogon = -100;
        List servidoresForaAr = new ArrayList();
        
        if(Usuario.equals(DefsComum.s_USR_ADMIN))
		{
			noLogin = NoUtil.getNoCentral();
		}
        else
		{
            No noTmp = null;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
            	try
					{
		    		noTmp = (No) iter.next();
		    		//Verifica se a referencia CORBA do servutil eh valida
		            if (noTmp.getConexaoServUtil().getM_iUtil() == null)
		            {
		                throw new COMM_FAILURE();
		            }
		    		String usuarioChk = "";
		    		usuarioChk = noTmp.getConexaoServUtil().trataListaUsuario(Usuario);
		    		if(!usuarioChk.equals(""))
		    		{
		    			noLogin = noTmp;
		    			//break;
		    		}
				}
        		catch(COMM_FAILURE comFail)
		        {
            		//Tenta criar uma nova conexao corba do servutil 
                    criaConexaoServUtil(noTmp); 
                    
                    // Verifica se a referencia CORBA do servutil eh valida 
    		        if (noTmp.getConexaoServUtil().getM_iUtil() == null)
                    {
                        // Se hao conseguir adiciona na lista de servidores fora do ar
                        servidoresForaAr.add(noTmp.getHostName()); 
                    }
    		        else
    		        {
    		        	String usuarioChk = "";
                		usuarioChk = noTmp.getConexaoServUtil().trataListaUsuario(Usuario);
                		if(!usuarioChk.equals(""))
                		{
                			noLogin = noTmp;
                			break;
                		}
    		        }
            		
		        }
	                
            }
		}
        
        if (servidoresForaAr.size() > 0)
        {
//        	iniciaArgs(3);
//            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//            m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//            m_Args[2] = "Servidor de Util esta fora do ar "+servidoresForaAr+"!."+
//            			"Contate o administrador do sistema e verifique se o sistema"+
//            			" esta em manuten&ccedil;&atilde;o.";
//            m_Html.enviaArquivo(m_Args);
            try
            {
            	m_Request.getSession().setAttribute("mensagem","Servidor de Util esta fora do ar "+servidoresForaAr+"!."+
            			"Contate o administrador do sistema e verifique se o sistema"+" esta em manuten&ccedil;&atilde;o.");
    			m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/login.jsp");
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
        }
        
        if(noLogin == null)
        {
        	System.out.println();
        	System.out.println("");
        	NoUtil.getNo().getConexaoServUtil().escreveUsuarioInvalido(Usuario, NoUtil.getNo().getIp());
//            iniciaArgs(3);
//            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_WEB();
//            m_Args[1] = ArquivosDefs.s_HTML_LOGIN;
//            m_Args[2] = "Login incorreto!";
//            m_Html.enviaArquivo(m_Args);
            
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
        else
        {
    		try {
				m_Response.sendRedirect("http://"+noLogin.getHostName()+":"+noLogin.getPorta()+"/"+DefsComum.s_ContextoWEB+
				        "/servlet/Portal.cPortal?operacao=logon&" +
				        "usuario="+Usuario+"&senha="+Senha+"&motivo="+motivo);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
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

}
