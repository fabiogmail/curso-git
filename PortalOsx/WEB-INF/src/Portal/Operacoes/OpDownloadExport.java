//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApagaRelExportado.java

package Portal.Operacoes;



/**
 */
public class OpDownloadExport extends OperacaoAbs  {
	
	static{}
	
	public OpDownloadExport(){
		
	}
	
	public boolean iniciaOperacao(String mensagem)
    {

	      try
	      {
	    	 String URL = "/templates/jsp/downloadExport.jsp";	 
	         setOperacao("Download Export");
	         
	         String usuario = m_Request.getParameter("usuario");
	         String arquivo = m_Request.getParameter("arquivo");
	         String diretorio = m_Request.getParameter("diretorio");
	         
	         m_Request.setAttribute("usuario", usuario);
	         m_Request.setAttribute("arquivo", arquivo);
	         m_Request.setAttribute("diretorio", diretorio);
	         
	         m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
	         return true;
	      }
	      catch (Exception Exc)
	      {
	         System.out.println("OpDownloadExport - iniciaOperacao(): "+Exc);
	         Exc.printStackTrace();
	         return false;
	      }
	   }
		
    



}