package Portal.Operacoes;

import java.util.ArrayList;

import Portal.Utils.ControlXml;

public class OpEditaXmlParser extends OperacaoAbs 
{

	public OpEditaXmlParser(){
		
	}
	
	/**
	    * @param p_Mensagem
	    * @return boolean
	    * @exception 
	    * @roseuid 3C21407E018F
	    */
	   public boolean iniciaOperacao(String p_Mensagem) 
	   {
		   String Mensagem = p_Mensagem;
	      try
	      {
	    	 String URL = "/templates/xmlParser/editarXmlParser.jsp"; 
	    	 String inicio = m_Request.getParameter("frame");
	    	 String subOperacao = (String)m_Request.getAttribute("suboperacao");
	    	 String tecparam = m_Request.getParameter("tecnologia");
	    	 

	    	 if(tecparam != null){
	    		 m_Request.getSession().setAttribute("tecnologia_gsm_nokia", tecparam);
	    	 }
	    	 
	    	 if(subOperacao != null){
	    		 if(subOperacao.equals("erro")){
	    			 URL = "/templates/xmlParser/erroXmlParser.jsp?mensagem="+Mensagem; 
	    			 m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
			         return true;
	    			 
	    		 }
	    	 }
	    	
	    	 //o parametro 'frame' é passado, no link do menu, por que na primeira chamada
	    	 //vem uma pagina html com um 'iframe' e ele que chama a pagina que irá mostrar
	    	 //a configuração do xml do parser.
	    	 
	    	 if(inicio != null){
	    		 URL = "/templates/xmlParser/iframePagina.html";
	    		 m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
		         return true;
	    	 }	    	 
	    	 
	         setOperacao("Editar Xml Parser");
	         
	         ControlXml xml = new ControlXml(m_Request.getSession().getAttribute("tecnologia_gsm_nokia")+"");
	        	 xml.carregaXml();
	        	 if(xml.isTudoOk()){
	        		 m_Request.setAttribute("tags",xml.getNomesCampos());
	        		 m_Request.setAttribute("tagsSaida",xml.getNomesCamposSaida());
	        		 m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
	        		 return true;
	        	 }else{
	        		 Mensagem = "O Caminho para o arquivo esta errado ou arquivo inexistente!";
	        		 OpEditaXmlParser parseXML = new OpEditaXmlParser();
	        		 m_Request.setAttribute("suboperacao","erro");
	        		 parseXML.setRequestResponse(getRequest(), getResponse());
	        		 parseXML.iniciaOperacao(Mensagem);	        		 
	        		 return true;
	        	 }
	         }
	      
	      catch (Exception Exc)
	      {
	         System.out.println("OpEditaXmlParser - iniciaOperacao(): "+Exc);
	         Exc.printStackTrace();
	         return false;
	      }
	   }
}
