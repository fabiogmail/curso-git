
package Portal.Xml;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsxCluster
 * Arquivo criado em: 20/06/2005
 * 
 * @author Marcos Urata
 * @version 1.0
 * 
 * Breve Descrição: Factory para objetos Document
 * 
 */

public class XmlDomFactory 
{
    
    public static Node loadXmlFile(String arquivo)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Node doc;
        URL resource;
        try
        {
            builder = factory.newDocumentBuilder();
            resource = XmlDomReader.class.getClassLoader().getResource(arquivo);
            if(resource != null){
            	doc = builder.parse(resource.toString());
            }else{
            	File f = new File(arquivo);
 
            	FileReader fr = new FileReader(f);
            	BufferedReader br = new BufferedReader(fr); 
            	while(!br.ready());
            	
            	doc = builder.parse("file:///" + f.getAbsolutePath());            	
            	br.close();
            	fr.close();
            }
            return doc;
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXParseException se)
        {
        	System.out.println("Line Number:  " + se.getLineNumber());
            System.out.println("Column Number:  " + se.getColumnNumber());
        	se.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }       
        return null;
    }

}
