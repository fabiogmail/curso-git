package Portal.Operacoes;

import Portal.Utils.ControlXml;

public class OpSalvarXmlParser extends OperacaoAbs 
{
	
	public OpSalvarXmlParser(){
		
	}

	
	/**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C21407E018F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
    	 String URL = "/templates/xmlParser/okXmlParser.html";
    	 
         setOperacao("Editar Xml Parser");
         String[] nomes = m_Request.getParameter("escolhidos").split(";");
        
         
         ControlXml xml = new ControlXml(m_Request.getSession().getAttribute("tecnologia_gsm_nokia")+"");  
         xml.carregaXml();
         xml.escreverXml(nomes);
//         xml.apagarTodos();
//         if(nomes != null){
//        	 xml.adicionaChildrens(nomes);
//         }
//         xml.reescrevendoXmlIdentado();
         
         m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpSalvarXmlParser - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
