//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpBilhetadores.java

package Portal.Operacoes;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.BilhetadorCfgDef;
import Portal.Utils.OperadoraCfgDef;
import Portal.Utils.TecnologiaCfgDef;


public class OpBilhetadores extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpBilhetadores - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C43A06A00D6
    */
   public OpBilhetadores() 
   {
      //System.out.println("OpBilhetadores - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C43A02A0387
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpBilhetadores - iniciaOperacao()");
      try
      {
         setOperacao("Configuração de Bilhetadores");
         String Chave = "";         

         /*if (DefsComum.s_CLIENTE.toLowerCase().equals("americel") == true)
            Chave += "_americel";
         else if (DefsComum.s_CLIENTE.toLowerCase().equals("telemig") == true)
            Chave += "_americel";*/
         if (DefsComum.s_CLIENTE.toLowerCase().equals("claro") == true){
        	 Chave += "_claro";  
         }else if(DefsComum.s_CLIENTE.toLowerCase().equals("tim") == true){
        	 Chave += "_tim";
         }else if(DefsComum.s_CLIENTE.toLowerCase().equals("oi") == true){
        	 Chave += "_oi";
         }

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/bilhetadores"+Chave+".js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "bilhetadores.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "bilhetadores"+Chave+".form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "bilhetadores.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpBilhetadores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C51C70403A2
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      TecnologiaCfgDef Tecnologia;
      BilhetadorCfgDef Bilhetador;
      OperadoraCfgDef  Operadora;
      
      int QtdElem;
      String Args[] = new String[10];
      Vector Tecnologias = new Vector(), Operadoras = null;
      List Bilhetadores = new Vector();
      
      for (int i = 0; i < Args.length; i++)
         Args[i] = "";

      No noTmp = null;
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          List bilhetadores = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
			  Vector tecnologias = noTmp.getConexaoServUtil().getListaTecnologiasCfg();
			  
			  if(bilhetadores != null)
			  {
			  	 Bilhetadores.addAll(bilhetadores);
			  	 for(int i = 0; i < bilhetadores.size();i++)
		         {
			  	 	Args[9] += noTmp.getHostName() + ";";
		         }
			  }
			  	
			  if(tecnologias != null)
			  	Tecnologias.addAll(tecnologias);
	          
	          Args[8] += noTmp.getHostName()+";";
	          
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
      
      Collections.sort(Bilhetadores);
      
      if(Args[8].length() > 0){
      	Args[8] = Args[8].substring(0,Args[8].length()-1);
      }
      if(Args[9].length() > 0){
      	Args[9] = Args[9].substring(0,Args[9].length()-1);
      }
      
      /**
       * A lista de Operadoras sera sempre a mesma, por esse motivo basta pegar a lista de
       * qualquer ServUtil.
       * */
      //if (DefsComum.s_CLIENTE.compareToIgnoreCase("asdsa") == 0)
      

      if (!DefsComum.s_CLIENTE.toUpperCase().equals("CTBC") && !DefsComum.s_CLIENTE.toUpperCase().equals("GVT")
    		  && !DefsComum.s_CLIENTE.toUpperCase().equals("NEXTEL") && !DefsComum.s_CLIENTE.toUpperCase().equals("TELEMIG") && !DefsComum.s_CLIENTE.toUpperCase().equals("TIM"))
      {
    	  Operadoras   = NoUtil.getNo().getConexaoServUtil().getListaOperadoras();
      }
      if(Operadoras != null)
      {
    	  Operadoras.trimToSize();
      }
    	  
      
         
      
      Tecnologias.trimToSize();
      //Bilhetadores.trimToSize();
      

      if (Bilhetadores != null)
      {
         QtdElem = Bilhetadores.size();
         // Monta lista com nomes dos bilhetadores
         for (int i = 0; i < QtdElem; i++)
         {
            Bilhetador = (BilhetadorCfgDef) Bilhetadores.get(i);
            if (i == QtdElem - 1)
            {
               Args[0] += Bilhetador.getBilhetador();
               Args[1] += Bilhetador.getApelido();
               Args[2] += Short.toString(Bilhetador.getFase());
               Args[4] += Bilhetador.getTecnologia().getTecnologia();
               Args[7] += Bilhetador.getOperadora().getOperadora();
            }
            else
            {
               Args[0] += Bilhetador.getBilhetador() + ";";
               Args[1] += Bilhetador.getApelido() + ";";
               Args[2] += Short.toString(Bilhetador.getFase()) + ";";
               Args[4] += Bilhetador.getTecnologia().getTecnologia() + ";";               
               Args[7] += Bilhetador.getOperadora().getOperadora() +";";
            }
         }
      }

      if (Tecnologias != null)
      {
         QtdElem = Tecnologias.size();
         // Monta lista com nomes das tecnologias
         for (int i = 0; i < QtdElem; i++)
         {
            Tecnologia = (TecnologiaCfgDef) Tecnologias.elementAt(i);
            if (i == QtdElem - 1)
               Args[3] += Tecnologia.getTecnologia();
            else
               Args[3] += Tecnologia.getTecnologia() + ";";
         }
      }

	  if (Operadoras != null)
	  {
	     QtdElem = Operadoras.size();
	     // Monta lista com nomes das tecnologias
	     for (int i = 0; i < QtdElem; i++)
	     {
	        Operadora = (OperadoraCfgDef) Operadoras.elementAt(i);
	        if (i == QtdElem - 1)
	           Args[6] += Operadora.getOperadora();
	        else
	           Args[6] += Operadora.getOperadora() + ";";
	     }
	  }
		  
      Args[5] = p_Mensagem;
      
      
      
      return Args;
   }
}
