/*
 * OSx Telecom
 * Criado em 13/07/2005
 * 
 */
package Portal;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Xml.XmlDomReader;

/**
 * @author Marcos Urata
 *  
 */
public class PortalStartup extends HttpServlet {

    public void init() throws ServletException
    {
        System.out.println(new java.util.Date() + " - Inicializando PortalOsx...");
        new ArquivosDefs();
        XmlDomReader.readXml("DefsWeb.xml");
        
        if(NoUtil.listaDeNos.size() > 1)
        {
        
	        int qtdNoCentral = 0;
	        No noTmp = null;
	        
	        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	        {
	            noTmp = (No) iter.next();
	            if(noTmp.isNoCentral())
	            {
	            	qtdNoCentral++;
	            }
	            
	        }
	        
	        if(qtdNoCentral == 0)
	        {
	        	getServletContext().setAttribute("ErroConfig","O Nó central não foi indicado. Verifique as configurações dos Nós");
	        }
	        else if(qtdNoCentral > 1)
	        {
	        	getServletContext().setAttribute("ErroConfig","Existe mais de um Nó central. Verifique as configurações dos Nós");
	        }
	        
        }
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }
}