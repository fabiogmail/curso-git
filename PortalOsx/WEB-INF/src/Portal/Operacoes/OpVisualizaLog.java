//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVisualizaLog.java

package Portal.Operacoes;

import java.util.Date;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iLogView;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;

public class OpVisualizaLog extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D5160D5018C
    */
   public OpVisualizaLog() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D5161070329
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         boolean bNaoSai = true, bRetorno = false;
         int Cont = 0;
         String Linha = null, Cfg = null,
                DiasDaSemana[] = {"Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"};

         setOperacao("Visualização de Log");

         // Recupera os parâmetros
         String IDProcesso = m_Request.getParameter("idprocesso");
         String Processo = m_Request.getParameter("processo");
         short Dia = Short.parseShort(m_Request.getParameter("dia"));
         String HoraInicial = m_Request.getParameter("HoraIni");
         String HoraFinal   = m_Request.getParameter("HoraFim");
         String servidor    = m_Request.getParameter("servidor");
         
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = ArquivosDefs.s_HTML_LOG1;
         m_Args[2] = getProcesso(Short.parseShort(IDProcesso))+".gif";
         m_Args[3] = getNomeProc(Short.parseShort(IDProcesso), Processo);
         m_Args[4] = DiasDaSemana[Dia];
         m_Args[5] = HoraInicial;
         m_Args[6] = HoraFinal;
         m_Html.enviaArquivo(m_Args);

         if (Processo.indexOf(" Exec") != -1)
            Processo = "exec";
         
         
         iLogView log = null;
         
         No no = null;
         if(NoUtil.isAmbienteEmCluster() && servidor != null)
         {
         	no = NoUtil.getNoByHostName(servidor);
         }
         else if(NoUtil.isAmbienteEmCluster() && servidor == null)
         {
        	 //if(IDProcesso.equals(21)){
        	 	no = NoUtil.buscaNobyCfgConversor(Processo);
         }
        	// else{
        		// no = NoUtil.getNoCentral();
        //	 }
        // }
         else
         {
         	no = NoUtil.getNo();
         }  

         try
		 {
			 log = no.getConexaoServUtil().getInfoLog(Short.parseShort(IDProcesso), Processo, Dia, HoraInicial, HoraFinal);
			     
		     if (log != null)
		     {
		     	bRetorno = true; 
		     }
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

         if (bRetorno)
         {
            do
            {
               Linha = no.getConexaoServUtil().getLinhaLog();
               if (Linha.length() == 0)
                  bNaoSai = false;
               else
               {
                  //if (Linha.endsWith("Processo ServUtil\n") == false)
                  {
                     if (Cont%2 == 0)
                        m_Html.getSaidaHtml().print("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
                     else
                        m_Html.getSaidaHtml().print("<tr bgcolor=\"#FFFFFF\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
                     Cont++;
                  }
               }
            } while (bNaoSai);

            if (Cont == 0)
               m_Html.getSaidaHtml().print("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">Não há log para o período solicitado</td>\n</tr>\n");

            // Destroi o objeto Log no Servidor
            no.getConexaoServUtil().destroiLog();
         }
         else
            m_Html.getSaidaHtml().print("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">Não foi possível recuperar a log.</td>\n</tr>\n");

         iniciaArgs(3);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = ArquivosDefs.s_HTML_LOG2;
         m_Args[2] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), getProcesso(Short.parseShort(IDProcesso))+".txt", null);
         m_Html.enviaArquivo(m_Args);
      }
      catch (Exception Exc)
      {
         System.out.println("OpVisualizaLog - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
		 return false;
      }

      return true;
   }
   
   /**
    * @param p_IDProcesso
    * @param p_Processo
    * @return String
    * @exception 
    * @roseuid 3D5C02C8024C
    */
   public String getNomeProc(short p_IDProcesso, String p_Processo) 
   {
      /*
       * Códigos dos servidores/processos: Códigos retirados do Constantes.h
       */
/*
      short
      COD_SRV_CONTROLE   =  0,
      COD_SRV_CONFIG     =  1,
      COD_SRV_CLIENTE    =  2,
      COD_SRV_CONSOLID   =  3,
      COD_SRV_AGENDA     =  4,
      COD_SRV_HISTORICO  =  5,
      COD_SRV_UTILITARIO =  6,
      COD_SRV_DISTRIB    =  7,
      COD_SRV_PROCESSOS  =  8,
      COD_SRV_ALARMES    =  9,
      COD_SRV_AGNCDR     = 10,
      COD_CONVERSOR      = 20,
      COD_PARSER         = 21,
      COD_GETCDR         = 22;
      COD_REPROCESSADOR  = 23;
      COD_PARSERGEN      = 24;
*/
      switch(p_IDProcesso)
      {
         case 0:
            return "[ServCtrl]";
         case 1:
            break;
         case 2:
            break;
         case 3:
            break;
         case 4:
            return "[ServAgn]";
         case 5:
            return "[ServHist]";
         case 6:
            return "[ServUtil]";
         case 7:
            break;
         case 8:
            break;
         case 9:
            return "[ServAlr]";
         case 10:
            return "[AgnCDR]";
         case 20:
            return "Conversor: ["+p_Processo+"]";
         case 21:
            return "Parser: ["+p_Processo+"]";
         case 22:
            break;
         case 23:
             return "Reprocessador: ["+p_Processo+"]";
         case 24:
        	 return "ParserGen: ["+p_Processo+"]";
      }

      return "";
   }
   
   /**
    * @param p_IDProcesso
    * @return String
    * @exception 
    * @roseuid 3D5C0B8E0025
    */
   public String getProcesso(short p_IDProcesso)
   {
      switch(p_IDProcesso)
      {
         case 0:
            return "logservctrl";
         case 1:
            break;
         case 2:
            break;
         case 3:
            break;
         case 4:
            return "logservagn";
         case 5:
            return "logservhist";
         case 6:
            return "logservutil";
         case 7:
            break;
         case 8:
            break;
         case 9:
            return "logservalr";
         case 10:
            return "logagncdr";
         case 20:
            return "logconversores";
         case 21:
            return "logparsers";
         case 22:
            break;
         case 23:
        	return "logreprocessadores";
         case 24:
         	return "logreparsersgen";
         case 25:
        	 return "logservagn";
      }

      return "";
   }
}
