//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpBDExtra.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
/**
 * @author osx
 * Classe de reprocessamento para o cliente CLARO
 *
 */
public class OpDataReprocessamento extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D1719700367
    */
   public OpDataReprocessamento() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D171970037B
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
	   try {
			String URL = "/templates/jsp/dataReprocessamento.jsp";
			setOperacao("Inserir data do Reprocessamento");
			m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
			return true;
		} catch (Exception Exc) {
			System.out.println("OpDataReprocessamento - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
			return false;
		}
      
   }  

}
