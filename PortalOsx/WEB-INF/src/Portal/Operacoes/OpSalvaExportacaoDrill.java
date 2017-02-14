package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;

public class OpSalvaExportacaoDrill extends OperacaoAbs
{
	
	   
	   static 
	   {
	   }
	   
	   /**
	    * @return 
	    * @exception 
	    * @roseuid 3C5FF285002E
	    */
	   public OpSalvaExportacaoDrill() 
	   {
	   }
	   
	   /**
	    * @param p_Mensagem
	    * @return boolean
	    * @exception 
	    * @roseuid 3C5FF2850042
	    */
	   public boolean iniciaOperacao(String p_Mensagem) 
	   {
		   		String Mensagem = "$ARG;";

	            String bilhetador = m_Request.getParameter("bilhetadores");
	            String ociosidade = m_Request.getParameter("ociosidades");
	            String habilitacao = m_Request.getParameter("habilitacao");
	            
	            No noTmp = null;
	            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	            {
	                try
	                {
	                	noTmp = (No) iter.next();
	                	noTmp.getConexaoServUtil().setExportaDrill(bilhetador, ociosidade, habilitacao);
	                } 
	                catch(COMM_FAILURE comFail)
					{
	                	System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
					}
	                catch(BAD_OPERATION badOp)
					{
	                	System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
	                	badOp.printStackTrace();
					}
	            }

	               Mensagem = "Configuração salva!";

	            OpExportacaoDrill expoDrill = new OpExportacaoDrill();
	            expoDrill.setRequestResponse(getRequest(), getResponse());
	            expoDrill.iniciaOperacao(Mensagem);
	            return true;
	         }
			
	  	}

