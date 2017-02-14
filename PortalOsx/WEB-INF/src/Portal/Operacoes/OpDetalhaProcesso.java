//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpDetalhaProcesso.java

package Portal.Operacoes;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import agenda.facade.IManutencaoAgenda;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.AgendaRMI;

/**
 */
public class OpDetalhaProcesso extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C50C2ED02D5
    */
   public OpDetalhaProcesso() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C50C2ED02E9
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpDetalhaProcesso - iniciaOperacao()");
      try
      {
         //setOperacao("Detalha Relatório Agendado");
         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "detalheprocgen.htm";
         m_Args[2] = "Detalhamento de Processo";
         m_Args[3] = "";
         m_Args[4] = "";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "detalheprocgen.form", montaFormulario());
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpDetalhaProcesso - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return String[]
    * @exception 
    * @roseuid 3C974FAF018B
    */
   public String[] montaFormulario() 
   {
      String Args[] = new String[8], Host = null;
      Vector Processo = null;
      Vector rmi = new Vector();

      String Tipo  = m_Request.getParameter("tipo");
      String PID = m_Request.getParameter("pid");

      if (PID.indexOf("-") != -1)
      {
         Host = PID.substring (PID.indexOf("-")+2, PID.length());
         PID = PID.substring (0, PID.indexOf("-")-1);
      }

      if (Tipo.toLowerCase().equals("util") == true)
      {
         if (Host.equalsIgnoreCase("localhost") || Host.equalsIgnoreCase(NoUtil.getNo().getHostName()))
             Processo = NoUtil.getNo().getConexaoServUtil().getProcesso(Integer.parseInt(PID), Host);
         else
             Processo = NoUtil.getNoByHostName(Host).getConexaoServUtil().getProcesso(Integer.parseInt(PID), Host);
         
      }else if(Tipo.equalsIgnoreCase("servagenda")){
    	  No noTmp = null;
    	  for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
          {
    		  noTmp = (No) iter.next();
    		  if(noTmp.getHostName().equalsIgnoreCase(Host)){
    			
    			  try {
    			  IManutencaoAgenda agenda = AgendaRMI.retornaManutencaoAgenda(noTmp.getIp(), noTmp.getPortaRMI(), noTmp.getHostName());
    			  
    			  rmi.add("ServidorAgenda");
					rmi.add(Integer.toString(agenda.getPID()));
				
    			  rmi.add(noTmp.getHostName());
    			  rmi.add("N/A");
    			  rmi.add("N/A");
    			  rmi.add(getDataHoraInicio(agenda.getHorarioDeInicio()));
    			  rmi.add("N/A");
    			  rmi.add(getMemoriaUso(agenda.getMemoriaEmUso()));
    			  } catch (RemoteException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				  } catch (Exception e) {
						// TODO: handle exception
  						e.printStackTrace();
					}
    			  
    		  }
          }
      }
      else
      {
          if (Host.equalsIgnoreCase("localhost") || Host.equalsIgnoreCase(NoUtil.getNo().getHostName()))
              Processo = NoUtil.getNo().getConexaoServControle().getProcesso(Integer.parseInt(PID), Host);
          else
              Processo = NoUtil.getNoByHostName(Host).getConexaoServUtil().getProcesso(Integer.parseInt(PID), Host);
      }

      if (Processo != null)
      {
         for (int i = 0; i < Processo.size(); i++)
            Args[i] = (String)Processo.elementAt(i);
      }else if(rmi.size() > 0){
    	  int indice = 0;
    	  for (Iterator iter = rmi.iterator(); iter.hasNext();)
    	  {
    		  String str = (String) iter.next();

    		  Args[indice] = str;
    		  indice++;
//    		  Args[0] = "ServidorAgenda";
//    		  Args[1] = "PID";
//    		  Args[2] = noTmp.getHostName();
//    		  Args[3] = "N/A";
//    		  Args[4] = "N/A";
//    		  Args[5] = "Inicio";
//    		  Args[6] = "Ultimo Acesso";
//    		  Args[7] = "Sem Informação";


    	  }
      }
      else
      {
         for (int i = 0; i < Args.length; i++)
            Args[i] = "Sem Informação";
      }
      return Args;
   }

   private String getMemoriaUso(long memoriaEmUso) {

	   long resp = memoriaEmUso/1024/1024;
	   
	   return (Long.toString(resp)+" M");
	   
}

private String getDataHoraInicio(Date horarioDeInicio) {
	
	   GregorianCalendar gc = new GregorianCalendar();
	   gc.setTime(horarioDeInicio);
	   
	   int dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
	   int mes = gc.get(GregorianCalendar.MONTH);
	   int ano = gc.get(GregorianCalendar.YEAR);
	   int hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
	   int minuto = gc.get(GregorianCalendar.MINUTE);
	   int segundo = gc.get(GregorianCalendar.SECOND);
	   
	   String resposta = "";
	   
	   if(dia < 10)
		   resposta += "0"+dia+"/";
	   else
		   resposta += dia+"/"; 
	   
	   if(mes < 10)
		   resposta += "0"+mes+"/";
	   else
		   resposta += mes+"/";
	   
	   resposta += ano+" ";
	   
	   if(hora < 10)
		   resposta += "0"+hora+":";  
	   else
		   resposta += hora+":";  
	   
	   if(minuto < 10)
		   resposta += "0"+minuto+":";  
	   else
		   resposta += minuto+":";  
	   
	   if(segundo < 10)
		   resposta += "0"+segundo;
	   else
		   resposta += segundo;  
	   
	   return resposta;
   }
}
