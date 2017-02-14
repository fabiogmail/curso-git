/*
 * Created on 13/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpControlaServCtrl.java
package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;

/**
 * @author osx
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OpControlaServidores extends OperacaoAbs 
{
	static 
	   {
	   }
	   
	   /**
	    * @return 
	    * @exception 
	    * @roseuid 3C9235DB02B8
	    */
	   public OpControlaServidores() 
	   {
	   }
	   
	   /**
	    * @param p_Mensagem
	    * @return boolean
	    * @exception 
	    * @roseuid 3C9235E901BE
	    */
	   public boolean iniciaOperacao(String p_Mensagem) 
	   {
	      try
	      {
	         setOperacao("Gerenciamento do ServCtrl");
	         // Recupera os parâmetros
	         String Tipo  = m_Request.getParameter("tipo");
	         String servidor = m_Request.getParameter("servidor");
	         int idServ   = Integer.parseInt(m_Request.getParameter("idServ"));
	         
	         No noTmp = null;
	         
	         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	         {
	             noTmp = (No) iter.next();
	             /** 
	              * Caso so exista 1 no, nao sera mostrado o Servidor em que estao rodando cada um
	              * dos processos.
	              * */
	             if (NoUtil.listaDeNos.size() == 1)
	             {
	                 m_Resultado = noTmp.getConexaoServUtil().controlaServidores(Tipo, idServ, null);
	             }
	             else if(noTmp.getHostName().equalsIgnoreCase(servidor) || noTmp.getIp().equalsIgnoreCase(servidor))
	             {
	                 m_Resultado = noTmp.getConexaoServUtil().controlaServidores(Tipo, idServ, noTmp);
	                 break;
	             }
	         }

	         // Inicia tabela de resposta
	         montaTabelaResultado("/PortalOsx/servlet/Portal.cPortal?operacao=controlaServCtrl&tipo=Inicia");

	         // Envia resultado
	         iniciaArgs(4);
	         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
	         m_Args[1] = "errogen.htm";
	         if (Tipo.toLowerCase().equals("inicia"))
	            m_Args[2] = "analisestartup.gif";
	         else
	            m_Args[2] = "analiseshutdown.gif";
	         m_Args[3] = m_Html.m_Tabela.getTabelaString();
	         m_Html.enviaArquivo(m_Args);
	      }
	      catch (Exception Exc)
	      {
	         System.out.println("OpControlaServCtrl - iniciaOperacao(): "+Exc);
	         Exc.printStackTrace();
	         return false;
	      }

	      return true;
	   }

}
