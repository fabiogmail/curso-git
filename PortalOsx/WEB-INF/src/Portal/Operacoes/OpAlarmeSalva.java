//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlarmeSalva.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpAlarmeSalva extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C5FF285002E
    */
   public OpAlarmeSalva() 
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

      try
      {
         setOperacao("Alarme de Espaço Em Disco");
         // Recupera os parâmetros
         String Tipo = m_Request.getParameter("tipoalr");

         if (Tipo.equals("0"))
         {
            String Args[] = new String[2];
            Args[0] = m_Request.getParameter("sms");
            Args[1] = m_Request.getParameter("telefones");
            
            No noTmp = null;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
                try
                {
                	noTmp = (No) iter.next();
                	noTmp.getConexaoServUtil().setCfgAlarme((short)0, Args);
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
            	
            
            
            Mensagem = "Configurações para envio de SMS salvas!";
            OpAlarmeProcessoParado Alarme = new OpAlarmeProcessoParado();
            Alarme.setRequestResponse(getRequest(), getResponse());
            Alarme.iniciaOperacao(Mensagem);
         }
         else if (Tipo.equals("1"))
         {
            String Args[] = new String[5];
            Args[0] = m_Request.getParameter("habilita");
            if (Args[0].equals("true") == false)
               Args[0] = "0";
            else
               Args[0] = "1";

            Args[1] = m_Request.getParameter("periodicidade");
            Args[2] = m_Request.getParameter("espaco");
            Args[3] = m_Request.getParameter("sms");
            Args[4] = m_Request.getParameter("telefones");
            
            No noTmp = null;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
                try
                {
                	noTmp = (No) iter.next();
                	noTmp.getConexaoServUtil().setCfgAlarme((short)1, Args);
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
            
            Mensagem = "Alarme de espaço em disco configurado!";
            OpAlarmeEspacoDisco Alarme = new OpAlarmeEspacoDisco();
            Alarme.setRequestResponse(getRequest(), getResponse());
            Alarme.iniciaOperacao(Mensagem);
         }
         else if (Tipo.equals("2"))
         {
            String Args[] = new String[6];

            Args[0] = m_Request.getParameter("diretorios");
            Args[1] = m_Request.getParameter("limites");
            Args[2] = m_Request.getParameter("periodicidades");
            Args[3] = m_Request.getParameter("habilitacao");
            Args[4] = m_Request.getParameter("sms");
            Args[5] = m_Request.getParameter("telefones");            

            No noTmp = null;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
                try
                {
                	noTmp = (No) iter.next();
                	noTmp.getConexaoServUtil().setCfgAlarme((short)2, Args);
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
            
            Mensagem = "Alarme de espaço máximo configurado!";
            OpAlarmeEspacoMaximo Alarme = new OpAlarmeEspacoMaximo();
            Alarme.setRequestResponse(getRequest(), getResponse());
            Alarme.iniciaOperacao(Mensagem);
         }
         else if (Tipo.equals("3"))
         {
            String Args[] = new String[6];

            Args[0] = m_Request.getParameter("bilhetadores");
            Args[1] = m_Request.getParameter("ociosidades");
            Args[2] = "0";
            Args[3] = m_Request.getParameter("habilitacao");
            Args[4] = m_Request.getParameter("sms");
            Args[5] = m_Request.getParameter("telefones");
            
            No noTmp = null;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
                try
                {
                	noTmp = (No) iter.next();
                	noTmp.getConexaoServUtil().setCfgAlarme((short)3, Args);
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

            if (Args[3].indexOf('1') != -1)
               Mensagem = "Alarme de ociosidade habilitado!";
            else
               Mensagem = "Configuração salva!";

            OpAlarmeConversor Alarme = new OpAlarmeConversor();
            Alarme.setRequestResponse(getRequest(), getResponse());
            Alarme.iniciaOperacao(Mensagem);
         }
         else if (Tipo.equals("4"))
         {
            String Args[] = new String[5];
            Args[0] = m_Request.getParameter("habilita");
            if (Args[0].equals("true") == false)
               Args[0] = "0";
            else
               Args[0] = "1";
            Args[1] = m_Request.getParameter("periodicidade");
            Args[2] = m_Request.getParameter("quantidade");
            Args[3] = m_Request.getParameter("sms");
            Args[4] = m_Request.getParameter("telefones");

            No noTmp = null;
            for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
            {
                try
                {
                	noTmp = (No) iter.next();
                	noTmp.getConexaoServUtil().setCfgAlarme((short)4, Args);
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

            Mensagem = "Alarme de quantidade de arquivos configurado!";
            OpAlarmeDirIn Alarme = new OpAlarmeDirIn();
            Alarme.setRequestResponse(getRequest(), getResponse());
            Alarme.iniciaOperacao(Mensagem);
         }


         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlarmeSalva - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
