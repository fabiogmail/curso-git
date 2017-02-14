package Portal.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Portal.Xml.XmlDomFactory;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ControlXml 
{
	
	private Document principal;
	private String arquivoXml;
	public static String XML_ENTRADA = "Campos_CDRBruto";
	public static String XML_SAIDA = "Formato_CDRX";
	private ArrayList<Node> todasTags = new ArrayList<Node>();
	ArrayList<String> nomesCampos = new ArrayList<String>();
	ArrayList<String> nomesCamposSaida = new ArrayList<String>();
	private String pathIn;
	private String pathOut;
	private static int estado = 0;//0 - livre, 1 - ocupado
	private static int falhaXML = 0;//0 - ok, 1 - falha
	private String tecnologia;
		
	public ControlXml(String tecnologia)
	{		
		this.tecnologia = tecnologia;
	}
	
	public synchronized void carregaXml(){
		while(estado==1);
		estado = 1;
		try{
			if(tecnologia.equalsIgnoreCase("gsm")){
				Node node = XmlDomFactory.loadXmlFile("PathConfig.xml");
				getPathsConfigXml((Document)node);
			}
			if(tecnologia.equalsIgnoreCase("nokia")){
				Node node = XmlDomFactory.loadXmlFile("PathConfigNokia.xml");
				getPathsConfigXml((Document)node);
			}
			else{
				Node node = XmlDomFactory.loadXmlFile("PathConfig.xml");
				getPathsConfigXml((Document)node);
				
			}			
				//lendo o arquivo do parser 
			
			Node nodeParser = XmlDomFactory.loadXmlFile(pathIn);
			arquivoXml = pathIn;
			principal = (Document)nodeParser;	
			encotraNomeCampos();
		
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		estado = 0;
	}
	
	public String getPathOut(){
		return pathOut;
	}
	
	public ArrayList<String> getNomesCampos(){
		return nomesCampos;
	}
	
	public ArrayList<String> getNomesCamposSaida(){
		return nomesCamposSaida;
	}
	
	private void getPathsConfigXml(Document node){
		NodeList node1 = node.getElementsByTagName("pathArquivoConfigParser");
		for(int i=0;i<node1.getLength();i++){
			Node n = node1.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				pathIn = n.getFirstChild().getTextContent();
				System.out.println("ControlXml.getPathsConfigXml() - pathIn: "+pathIn);
			}
		}
		
		NodeList node2 = node.getElementsByTagName("pathArquivoSaidaConfigParser");
		for(int i=0;i<node2.getLength();i++){
			Node n = node2.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				pathOut = n.getFirstChild().getTextContent();
			}
		}
	}
	
	
	private Node getCampoSaida(){
		//pega todos os Campos que tem o nome definido no XML_SAIDA
		NodeList nos = principal.getElementsByTagName(XML_SAIDA);
		Node saida = null;
		for (int i = 0; i < nos.getLength(); i++) {
			//deve ter apenas um campo com esse nome.
			saida = nos.item(i);
		}
		return saida;
	}
	
	private Node getCampoEntrada(){
		//pega todos os Campos que tem o nome definido no XML_SAIDA
		Node saida = null;
		NodeList nos = principal.getElementsByTagName(XML_ENTRADA);

		for (int i = 0; i < nos.getLength(); i++) {
			//deve ter apenas um campo com esse nome.
			saida = nos.item(i);
		}

		return saida;
	}
	
	private void encotraNomeCampos(){
		Node entrada = getCampoEntrada();
		NodeList nos = entrada.getChildNodes();
		
		
		for (int i = 0; i < nos.getLength(); i++) {
			Node no = nos.item(i);
			if(no.getNodeType() == Node.ELEMENT_NODE){
				todasTags.add(no);
				NamedNodeMap atributos =  no.getAttributes();
				
				Node atr = atributos.getNamedItem("CAMPO");
				
				String tagXml = atr.getNodeValue();
				//esse if é so para nao acrescentar o campo DATA_HORA 2 vezes.
				if(!nomesCampos.contains(tagXml)){
					nomesCampos.add(tagXml);
				}
			}
		}
		
		Node saida = getCampoSaida();
		NodeList nosSaida = saida.getChildNodes();
		
		
		for (int i = 0; i < nosSaida.getLength(); i++) {
			Node no = nosSaida.item(i);
			if(no.getNodeType() == Node.ELEMENT_NODE){				
				NamedNodeMap atributos =  no.getAttributes();
				
				Node atr = atributos.getNamedItem("CAMPO");
				
				String tagXml = atr.getNodeValue();
				//esse if é so para nao acrescentar o campo DATA_HORA 2 vezes.
				if(!nomesCamposSaida.contains(tagXml)){
					nomesCamposSaida.add(tagXml);
				}
			}
		}
		
		nomesCampos.removeAll(nomesCamposSaida);
	}
	
	private Node getPeloNome(String nome){
		for (Node no : todasTags) {
			Node atr = no.getAttributes().getNamedItem("CAMPO");			
			if(atr.getNodeValue().equals(nome)){
				Element novoNo = principal.createElement("Campo");
				Attr atributoCampo = principal.createAttribute("CAMPO");
				atributoCampo.setNodeValue(atr.getNodeValue());
				//Attr atributoTamanho = principal.createAttribute("TAMANHO");
				//atributoTamanho.setNodeValue(no.getAttributes().getNamedItem("TAMANHO").getNodeValue());
				
				novoNo.setAttributeNode(atributoCampo);
				//novoNo.setAttributeNode(atributoTamanho);
				
				return novoNo;
			}
		}
		return null;
	}
	
	private ArrayList<Node> getCampoDataHora(){
		ArrayList<Node> hrs = new ArrayList<Node>();
		for (Node no : todasTags) {
			Node atr = no.getAttributes().getNamedItem("CAMPO");			
			if(atr.getNodeValue().equals("DataHora")){
				Element novoNo = principal.createElement("Campo");
				Attr atributoCampo = principal.createAttribute("CAMPO");
				atributoCampo.setNodeValue(atr.getNodeValue());
				novoNo.setAttributeNode(atributoCampo);
				hrs.add(novoNo);
			}
		}
		return hrs;
	}
	
	public void addCampoXml(Node referencia,String nomeTag,HashMap<String, String> atributos){
		Node no = getCampoSaida();		
		
		Element elem = principal.createElement(nomeTag);
		Set<String> keys = atributos.keySet();
		for (String key : keys) {
			String value = atributos.get(key);
			elem.setAttribute(key,value);
		}
		
		if(referencia == null){
			no.insertBefore(elem, referencia);
		}else{
			no.appendChild(elem);
		}
	}
	
	public void apagarCampoXml(String nomeTag){
		Node no = getCampoSaida();
		NodeList chil = no.getChildNodes();
		Node nd = null;
		for (int i = 0; i < chil.getLength(); i++) {
			nd = chil.item(i);
			if(nd.getNodeName().equalsIgnoreCase(nomeTag)){
				break;
			}
		}
		no.removeChild(nd);
		
	}
	
	public void adicionaChildrens(String[] nomes){
		Node no = getCampoSaida();
		for (int i = 0; i < nomes.length; i++) {						
			Node children;
			ArrayList<Node> hrs;
			if(!nomes[i].equals("DataHora")){
				children = getPeloNome(nomes[i]);
				if(children != null){
					no.appendChild(children);
				}
			}else{
				hrs = getCampoDataHora();
				for (Node node : hrs) {
					no.appendChild(node);
				}
			}
			
		}		
	}
	
	public void apagarTodos(){
		Node no = getCampoSaida();
		NodeList chil = no.getChildNodes();
		Node nd = null;
		for (int i = 0; i < chil.getLength(); i++) {
			nd = chil.item(i);
			if(nd.getNodeType() == Node.ELEMENT_NODE){			
				nd.getParentNode().removeChild(nd);
			}
		}
	}
	
	public void reescrevendoXmlIdentado(){
		reescrevendoXmlIdentado(pathOut);
	}
	
	/**
	 * Medoto que reescreve o xml que foi editado.
	 * Esse metodo identa o xml corretamente. 
	 * @param pathArquivo -  caminho com o nome do novo arquivo a ser criado.
	 * Caso seja passado 'null' é editado o arquivo atual.
	 */
	public void reescrevendoXmlIdentado(String pathArquivo){
		try{
			//nova exportação
			File fileNew = null;
			if(pathArquivo != null){
				fileNew = new File(pathArquivo);
			}else{
				fileNew = new File(arquivoXml);
			}
			FileOutputStream fout = new FileOutputStream(fileNew);			
			XMLSerializer serializer = new XMLSerializer(fout, new OutputFormat(principal, "UTF-8", true));
			serializer.serialize(principal);
			fout.close();
		}catch(IOException io){
			io.printStackTrace();
		}
	}
	
	
	public void reescrevendoXml(){
		reescrevendoXml(pathOut);
	}
	
	/**
	 * Medoto que reescreve o xml que foi editado.
	 * Esse metodo tem um problema com a identação que não é muito boa.
	 * @param pathArquivo - caminho com o nome do novo arquivo a ser criado.
	 * Caso seja passado 'null' é editado o arquivo atual.
	 */
	public void reescrevendoXml(String pathArquivo) {
		try {
			// Prepare the DOM document for writing
			Source source = new DOMSource(principal);
	
			// Prepare the output file
			File file = null;
			if(pathArquivo != null){
				file = new File(pathArquivo);
			}else{
				file = new File(arquivoXml);
			}
			
			Result result = new StreamResult(file);
	
			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty("indent", "yes"); 
			xformer.transform(source, result);
			System.out.println("ok - alterado com sucesso");
			
		} catch (TransformerConfigurationException tce) {
			System.out.println ("\n** Transformer Factory error");
			System.out.println(" " + tce.getMessage() );
	
			// Use the contained exception, if any
			Throwable x = tce;
			if (tce.getException() != null)
			x = tce.getException();
			x.printStackTrace();
		} catch (TransformerException te) {
			// Error generated by the parser
			System.out.println ("\n** Transformation error");
			System.out.println(" " + te.getMessage() );
	
			// Use the contained exception, if any
			Throwable x = te;
			if (te.getException() != null)
			x = te.getException();
			x.printStackTrace();
		}
	}
	
	public synchronized void escreverXml(String[] nomes){
		while(estado==1);
		estado = 1;
		 apagarTodos();
         if(nomes != null){
        	 adicionaChildrens(nomes);
         }
         reescrevendoXmlIdentado();
         estado = 0;
	}
	
	public boolean isTudoOk(){
		if(principal == null)
			return false;
		else 
			return true;
	}

	public static int getEstado() {
		return estado;
	}

	public static void setEstado(int estado) {
		ControlXml.estado = estado;
	}

	public static int getFalhaXML() {
		return falhaXML;
	}

	public static void setFalhaXML(int falhaXML) {
		ControlXml.falhaXML = falhaXML;
	}
	
}
