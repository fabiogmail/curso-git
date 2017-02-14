//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApagaAlarme.java

package Portal.Operacoes;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.VetorUtil;


/**
 */
public class OpApagaAlarme extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6BC72901AC
    */
   public OpApagaAlarme() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6BC725000B
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      String ListaArquivos,Arquivos = "";
      Vector ListaAux = new Vector();
      
      setOperacao("Apaga Alarmes");

      OpListaAlarmes Alarmes = new OpListaAlarmes();
      Alarmes.setRequestResponse(getRequest(), getResponse());

      ListaArquivos = m_Request.getParameter("alarmes");
      
      HashMap map = new HashMap();
      
      
      ListaAux = VetorUtil.String2Vetor(ListaArquivos,';');
      for(int i = 0;i < ListaAux.size();i++)
      {
    	  String arq = (String)ListaAux.get(i);
    	  String servidor = arq.substring(arq.lastIndexOf('-')+1,arq.length());
    	  arq = arq.substring(0,arq.lastIndexOf('-'));
    	  
    	  if(map.containsKey(servidor))
    	  {
    		  Vector valores = (Vector)map.get(servidor);
    		  valores.add(arq);
    		  map.put(servidor,valores);
    	  }
    	  else
    	  {
    		  Vector valores = new Vector();
    		  valores.add(arq);
    		  map.put(servidor, valores);
    	  }
      }
      
      
      for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {

    	  String key = (String) iter.next();
    	  Vector valores = (Vector) map.get(key);
    	  for (int i = 0; i < valores.size(); i++) {
    		  Arquivos += valores.get(i)+";";
    	  }
    	  
    	  No no = NoUtil.getNoByHostName(key);
    	  if(no != null)
    	  {
    		  try
         	  {
    			  no.getConexaoServUtil().apagaMensagemAlarme(Arquivos);
         	  }
    		  catch(COMM_FAILURE comFail)
         	  {
         		  System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+no.getHostName()+").");
         	  }
         	  catch(BAD_OPERATION badOp)
         	  {
         		  System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+no.getHostName()+").");
         		  badOp.printStackTrace();
         	  }
    		  
    	  }
    	  
    	  Arquivos = "";
	
      }
      
      //NoUtil.getNo().getConexaoServUtil().apagaMensagemAlarme(ListaArquivos);

      Alarmes.iniciaOperacao("Operação concluída!");
      return true;
   }
}
