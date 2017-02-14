//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpResumoDiario.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.VetorUtil;

/**
 */
public class OpResumoDiario extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6BF617014D
    */
   public OpResumoDiario() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6BF6170161
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpResumoDiario - iniciaOperacao()");
      try
      {
         setOperacao("Resumo Diário");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/resumodiario.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "resumodiario.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "resumodiario.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "resumodiario.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpResumoDiario - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C6BF617019D
    */
   public String[] montaFormulario(String p_Mensagem)
   {
      String listaDatas = "";
      Vector datas = new Vector();
      String tmp = null;
      String Args[] = new String[3];;
      
   	  No noTmp = null;
   	  for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
   	  {
   		  try
   		  {
   			  noTmp = (No) iter.next();
   			  tmp = noTmp.getConexaoServUtil().getListaDatas();
   			
   			  listaDatas += (tmp == null) ? "" : tmp;
   			  if (listaDatas.indexOf(";", listaDatas.length()-1) == -1)
   			  {
   				  listaDatas += ";";
   			  }
   			  Vector datasAux = VetorUtil.String2Vetor(listaDatas,';');
   			  for (int i = 0; i < datasAux.size(); i++) 
   			  {
   				  String data = (String)datasAux.get(i);
   				  if(!datas.contains(data))
   				  {
   					  datas.add(data);
   				  }
   			  }
   			  
   			  
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
   	  
   	  String lista = "";
   	  
   	  for (int i = 0; i < datas.size(); i++) 
   	  {
   		  lista +=	datas.get(i) + ";";
   	  }
   	  

      Args[0] = lista.substring(0, lista.length()-1);
      Args[1] = p_Mensagem;

      
      return Args;
   }
}
