//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpLog.java

package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpLog extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D3DBC3E02CB
    */
   public OpLog() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D3DBC570335
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Logs de Processos");
         
         String Args[] = getProcessos();
         Args[1] = "";
         
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             noTmp = (No) iter.next();
             Args[1] += "<option>"+noTmp.getHostName()+"</option>\n"; 
         }
         
         String Tipo = m_Request.getParameter("tipo");
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/log.js\"";
         m_Args[3] = "";  // OnLoad
         if (Tipo.compareToIgnoreCase("analise") == 0)
            m_Args[4] = "analiselogsprocessos.gif";
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
            m_Args[4] = "deteccaologsprocessos.gif";
         else if (Tipo.compareToIgnoreCase("web") == 0)
            m_Args[4] = "weblogsprocessos.gif";
         
         if(NoUtil.isAmbienteEmCluster())
         {
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "logcluster.form", Args);
         }
         else
         {
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "log.form", Args);
         }
         
         
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "log.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception ExcLogAcesso)
      {
         System.out.println("OpLog - iniciaOperacao(): "+ExcLogAcesso);
         ExcLogAcesso.printStackTrace();
         return false;
      }
   }

   /**
    * @return String[]
    * @exception 
    * @roseuid 3D3DC5AE0301
    */
   public String[] getProcessos() 
   {
      /*
       * Códigos dos servidores/processos: Códigos retirados do Constantes.h
       */
      String
      COD_SRV_CONTROLE   =  "0",
      COD_SRV_CONFIG     =  "1",
      COD_SRV_CLIENTE    =  "2",
      COD_SRV_CONSOLID   =  "3",
      COD_SRV_AGENDA     =  "25",
      //COD_SRV_HISTORICO  =  "5",
      COD_SRV_UTILITARIO =  "6",
      COD_SRV_DISTRIB    =  "7",
      COD_SRV_PROCESSOS  =  "8",
      COD_SRV_ALARMES    =  "9",
      COD_SRV_AGNCDR     = "10",
      COD_CONVERSOR      = "20",
      COD_PARSER         = "21",
      COD_GETCDR         = "22";

      String Processos[], Tipo;
      Processos = new String[2];
      Processos[0] = "";

      Tipo = m_Request.getParameter("tipo");

      if (DefsComum.s_CLIENTE.compareToIgnoreCase("amazonia_celular") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("americel") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }         
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("oi") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("claro") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("anatel") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("ctbc") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("embratel") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("telemig") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";*/
            //Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            //Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("telecelular_sul") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("sercomtel") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("gvt") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("acme") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
//            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("tim") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
           // Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
           // Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      else if (DefsComum.s_CLIENTE.compareToIgnoreCase("GVT_MKT") == 0)
      {
         if (Tipo.compareToIgnoreCase("analise") == 0)
         {
            /*Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";*/
            Processos[0] += "<option value=\""+COD_SRV_CONTROLE+"\">Servidor de Controle</option>";
         }
         else if (Tipo.compareToIgnoreCase("deteccao") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_AGNCDR+"\">AgnCDR</option>";
            Processos[0] += "<option value=\""+COD_SRV_ALARMES+"\">Servidor de Alarmes</option>";
         }
         else if (Tipo.compareToIgnoreCase("web") == 0)
         {
            Processos[0] += "<option value=\""+COD_SRV_UTILITARIO+"\">Servidor Util</option>";
            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Servidor de Agenda</option>\n";
//            Processos[0] += "<option value=\""+COD_SRV_AGENDA+"\">Agenda Exec</option>\n";
            //Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Servidor de Historico</option>";
            //Processos[0] += "<option value=\""+COD_SRV_HISTORICO+"\">Historico Exec</option>";
         }
      }
      return Processos;
   }
}
