package Portal.Xml;

import java.util.Stack;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsxCluster
 * Arquivo criado em: 20/06/2005
 *
 * @author Marcos Urata
 * @version 1.0
 *
 * Breve Descrição:
 *
 */

public class XmlDomReader {

    private Node node;
    private static Stack pilhaDeNos = new Stack();

    public static void readXml(String arquivo)
    {
        new XmlDomReader(XmlDomFactory.loadXmlFile(arquivo));
        System.out.println("------------------------------------------------------");
        System.out.println("QUANTIDADE DE NOS: "+pilhaDeNos.size());
        System.out.println("------------------------------------------------------");
//        pilhaDeNos.trimToSize();
//        NoUtil.listaDeNos = new ArrayList(pilhaDeNos);
    }

    private XmlDomReader(Node node)
    {
        this.node = node;

         parseData();
         leFilhos();
    }

    public void parseData()
    {
        String nodeName = node.getNodeName();
        NamedNodeMap attribs = null;
        No no = null;

        if (nodeName != null)
        {
            nodeName = nodeName.trim();
        }

        if (node.getNodeType() == Node.ELEMENT_NODE && nodeName.equalsIgnoreCase(XmlTags.CLIENTE))
        {
            DefsComum.s_CLIENTE = node.getAttributes().item(0).getNodeValue();
        }
        else  if (node.getNodeType() == Node.ELEMENT_NODE && nodeName.equalsIgnoreCase(XmlTags.SUB_CLIENTE))
        {
            DefsComum.sSUB_CLIENTE = node.getAttributes().item(0).getNodeValue();
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getParentNode().getNodeName().equalsIgnoreCase(XmlTags.COMUM))
        {
            montaNoOutros(nodeName);
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && nodeName.equalsIgnoreCase(XmlTags.NO))
        {
            no = new No();
            pilhaDeNos.add(no);
            NoUtil.listaDeNos.add(no);

            montaNoCluster(no);
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getParentNode().getNodeName().equalsIgnoreCase(XmlTags.DIRETORIO))
        {
            montaNoDiretorios(nodeName);
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && nodeName.equalsIgnoreCase(XmlTags.SERVIDOR))
        {
            montaNoServidores(node);
        }
    }

    private void montaNoCluster(No noCluster)
    {
        NamedNodeMap attribs = node.getAttributes();
        Node nodeAtributo = null;

        for (int i = 0; i < attribs.getLength(); i++)
        {
            nodeAtributo = attribs.item(i);

            if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.CENTRAL))
            {
                boolean noCentral = new Boolean(attribs.item(i).getNodeValue()).booleanValue();
                noCluster.setNoCentral(noCentral);
            }
            else if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.HOSTNAME))
            {
                noCluster.setHostName(nodeAtributo.getNodeValue());
            }
            else if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.IP))
            {
                noCluster.setIp(nodeAtributo.getNodeValue());
            }
            else if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.PORTA))
            {
                noCluster.setPorta(nodeAtributo.getNodeValue());
            }
            else if(nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.PORTA_RMI)){
            	noCluster.setPortaRMI(nodeAtributo.getNodeValue());
            }
        }
    }

    private void montaNoOutros(String noDom)
    {
        if (noDom.equalsIgnoreCase(XmlTags.EMAIL_ADMIN))
        {
            DefsComum.s_MAIL_ADM = node.getAttributes().item(0).getNodeValue();
        }
        else if (noDom.equalsIgnoreCase(XmlTags.ELEMENTOS_PAGINA))
        {
            DefsComum.s_QTD_ITENS_TABELA = Short.parseShort(node.getAttributes().item(0).getNodeValue());
        }
        else if (noDom.equalsIgnoreCase(XmlTags.SESSION_TIMEOUT))
        {
            DefsComum.s_TIMEOUT_INATIVIDADE = Integer.parseInt(node.getAttributes().item(0).getNodeValue());
        }
        else if (noDom.equalsIgnoreCase(XmlTags.ELEMENTOS_PAGINA_REAGENDAMENTO))
        {
			DefsComum.s_ELEMENTOS_PAGINA_REAGENDAMENTO = Short.parseShort(node.getAttributes().item(0).getNodeValue());
		}
    }

    private void montaNoDiretorios(String noDom)
    {
        No noCluster = (No)pilhaDeNos.peek();

        if (noDom.equalsIgnoreCase(XmlTags.TOMCAT_HOME))
        {
            String diretorioWeb = node.getAttributes().item(0).getNodeValue().trim();

            if (!diretorioWeb.endsWith("/"))
            {
                diretorioWeb += "/";
            }
            noCluster.getDiretorioDefs().setS_DIR_WEB(diretorioWeb);
        }
        else if (noDom.equalsIgnoreCase(XmlTags.BASE_CDRVIEW))
        {
            String diretorioCDRView = node.getAttributes().item(0).getNodeValue().trim();

            if (!diretorioCDRView.endsWith("/"))
            {
                diretorioCDRView += "/";
            }
            noCluster.getDiretorioDefs().setS_DIR_CDRVIEW(diretorioCDRView);
        }
        else if (noDom.equalsIgnoreCase(XmlTags.GRUPOS_CONF_REGRAS))
        {
            String diretorioGruposConf = node.getAttributes().item(0).getNodeValue().trim();

            if (!diretorioGruposConf.endsWith("/"))
            {
                diretorioGruposConf += "/";
            }
            	noCluster.getDiretorioDefs().setS_DIR_ARQS_ALR(diretorioGruposConf);
        }
        else if (noDom.equalsIgnoreCase(XmlTags.PERIODOS_ALARMES))
        {
            String diretorioArqsPeriodo = node.getAttributes().item(0).getNodeValue().trim();

            if (!diretorioArqsPeriodo.endsWith("/"))
            {
                diretorioArqsPeriodo += "/";
            }
            noCluster.getDiretorioDefs().setS_DIR_ARQS_PERIODO(diretorioArqsPeriodo);
            noCluster.getDiretorioDefs().setNo(noCluster);
            noCluster.getDiretorioDefs().setDiretorios();
        }
    }

    private void montaNoServidores(Node noDom)
    {
        NamedNodeMap attribs = noDom.getAttributes();
        Node nodeAtributo = null;
        String nomeServidor = null, host = null, objeto = null;
        short modoConexao=-1;
        int porta=-1;

        for (int i = 0; i < attribs.getLength(); i++)
        {
            nodeAtributo = attribs.item(i);

            if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.NOME))
            {
                nomeServidor = nodeAtributo.getNodeValue().trim();
            }
            if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.HOST))
            {
                host = nodeAtributo.getNodeValue().trim();
            }
            else if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.CONEXAO))
            {
                modoConexao = Short.parseShort(nodeAtributo.getNodeValue());
            }
            else if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.PORTA))
            {
                porta = Integer.parseInt(nodeAtributo.getNodeValue());
            }
            else if (nodeAtributo.getNodeName().equalsIgnoreCase(XmlTags.OBJETO))
            {
                objeto = nodeAtributo.getNodeValue();
            }

            if (nomeServidor != null && host != null && modoConexao != -1 && porta != -1 && objeto != null)
                ((No)pilhaDeNos.peek()).createConexao(nomeServidor, modoConexao, host, objeto, porta);
        }
    }

    public void leFilhos()
    {
        Node child = node.getFirstChild();

        while(child != null)
        {
            new XmlDomReader(child);
            child = child.getNextSibling();
        }
    }

    public static void main(String[] args)
    {
        readXml("DefsWeb.xml");
    }

}
