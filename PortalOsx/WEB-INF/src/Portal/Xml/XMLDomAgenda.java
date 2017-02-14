package Portal.Xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

public class XMLDomAgenda {
	
	public String usuarioBanco;
	public String senhaBanco;
	public String urlBanco;
	
	public XMLDomAgenda() {
		//abreArquivo();

		Document doc = (Document)CarregarXML();    

		lerEstruturaXML(doc);
		
	}

	public static XMLDomAgenda getLeitorXML(){

		return XMLReaderHolder.instance;
	}

	private static class XMLReaderHolder {
		private final static XMLDomAgenda instance = new XMLDomAgenda();
	}


	private void lerEstruturaXML(Document doc) {
		if(doc != null){

			Element noPai = doc.getDocumentElement();

			NodeList filhos = noPai.getChildNodes();

			for (int i = 0; i < filhos.getLength(); i++) {

				Node no = filhos.item(i);

				if(no.hasChildNodes() ){

					//Verifica se o nó encontrado indica existência de arquivos (no xml, tipo='arquivo')
					if(no.getNodeName().equalsIgnoreCase("Banco")){
						
						NodeList neto = (NodeList) no.getChildNodes();
						
						for (int j = 0; j < neto.getLength(); j++) {
							Node tmp = neto.item(j);
							if(tmp.getFirstChild() != null){
								String bd = tmp.getFirstChild().getNodeValue();
								if(tmp.getNodeName().equalsIgnoreCase(XmlTags.BANCO_AGENDA_USUARIO))
									setUsuarioBanco(bd);
								else if(tmp.getNodeName().equalsIgnoreCase(XmlTags.BANCO_AGENDA_SENHA)){
									setSenhaBanco(bd);
								}else if(tmp.getNodeName().equalsIgnoreCase(XmlTags.BANCO_AGENDA_URL)){
									setUrlBanco(bd);
								}
							}
						}

					}
				}
			}
		}


	}

	private Document CarregarXML(){
		Document doc = null;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
//			doc = docBuilder.parse(new File("C:/usr/osx/cdrview/exec-Oi/Oi/cfgsis/xml/ConfigAgenda.xml"));
			System.out.println(NoUtil.getNo().getDiretorioDefs().getS_DIR_CDRVIEW()+"cfgsis/xml/ConfigAgenda.xml");
			doc = docBuilder.parse(new File(NoUtil.getNo().getDiretorioDefs().getS_DIR_CDRVIEW()+"/"+"cfgsis/xml/ConfigAgenda.xml"));
		}catch(IOException io){
			System.out.println("ERRO - Não encontrou o o arquivo configuracao.xml");
			io.printStackTrace();
		}catch (SAXException se) {
			System.out.println("ERRO - arquivo configuracao.xml nao esta no formato correto");
			se.printStackTrace();
		}catch (Exception e){
			System.out.println("ERRO");
			e.printStackTrace();
		}
		return doc;
	}

	

	public void getDiretorio(){
		//Oi/cfgsis/xml/ConfigAgenda.xml

		NoUtil.getNo().getDiretorioDefs().getS_DIR_CDRVIEW();
	}


	public static void main(String[] args) {
		XMLDomAgenda.getLeitorXML();
	}

	public String getUsuarioBanco() {
		return usuarioBanco;
	}

	public void setUsuarioBanco(String usuarioBanco) {
		this.usuarioBanco = usuarioBanco;
	}

	public String getSenhaBanco() {
		return senhaBanco;
	}

	public void setSenhaBanco(String senhaBanco) {
		this.senhaBanco = senhaBanco;
	}

	public String getUrlBanco() {
		return urlBanco;
	}

	public void setUrlBanco(String urlBanco) {
		this.urlBanco = urlBanco;
	}
}
