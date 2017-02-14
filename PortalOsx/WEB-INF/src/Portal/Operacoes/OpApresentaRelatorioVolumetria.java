package Portal.Operacoes;

public class OpApresentaRelatorioVolumetria extends OperacaoAbs {
	
	public OpApresentaRelatorioVolumetria(){
		
	}
	
	 public boolean iniciaOperacao(String p_Mensagem) 
	   {
		 String subOperacao = m_Request.getParameter("subOperacao");
		 
		 if(subOperacao == null)
			 subOperacao = "";
		 
		 
		 try {
			 String URL;
			 
			 if(subOperacao.equalsIgnoreCase("relVolumetria")){
				 URL = "/templates/jsp/relatorioVolumetria.jsp";
			 }
			 else if(subOperacao.equalsIgnoreCase("relVolumetriaAlarme"))
				 URL = "/templates/jsp/relatorioVolumetriaAlarme.jsp";
			 else
				 URL = "/templates/jsp/dadosRelatorioVolumetria.jsp";
			 
			 setOperacao("Selecionar Dados do Relatório de Volumetria");
			 m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			 return true;
		 } catch (Exception Exc) {
			 System.out.println("OpApresentaRelatorioSequencia - iniciaOperacao(): " + Exc);
			 Exc.printStackTrace();
			 return false;
		 }
	      
	   }  
	

}
