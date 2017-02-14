//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpGerenciaReprocessadores.java

package Portal.Operacoes;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.BilhetadorCfgDef;
import Portal.Utils.ConfiguracaoReprocCfgDef;
import Portal.Utils.ReprocessadorCfgDef;
import Portal.Utils.ServidorProcCfgDef;

/**
 */
public class OpGerenciaReproc extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E0EE702AD
    */
   public OpGerenciaReproc() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8E0EE70375
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpGerenciaReprocessadores - iniciaOperacao()");
      try
      {
         setOperacao("Gerenciamento de Reprocessadores");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/gerenciareproc.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "reprocgerenciamento.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "gerenciareproc.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "gerenciareproc.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpGerenciaReprocessadores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C8E0EE703E3
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      int QtdElem = 0;
      String Temp = "", Args[] = new String[6];
      Vector Configuracoes = new Vector(), Servidores = new Vector(),
             Reprocessadores = new Vector();
      List   Bilhetadores = new Vector();
      for (int i = 0; i < Args.length; i++)
         Args[i] = "";

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          noTmp = (No) iter.next();
          Vector configuracoes = noTmp.getConexaoServUtil().getListaCfgReprocessadores();
          Vector servidores = noTmp.getConexaoServUtil().getListaServProcessos();
          Vector conversores = noTmp.getConexaoServUtil().getListaReprocessadores();
          List   bilhetadores = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
          
          if(configuracoes != null)
          	Configuracoes.addAll(configuracoes);
          if(servidores != null)
          	Servidores.addAll(servidores);
          if(conversores != null)
          	Reprocessadores.addAll(conversores);
          if(bilhetadores != null)
          	Bilhetadores.addAll(bilhetadores);
      }
      
      Collections.sort(Bilhetadores);
      
      Configuracoes.trimToSize();
      Servidores.trimToSize();
      Reprocessadores.trimToSize();
      //Bilhetadores.trimToSize();

      if (Configuracoes.size() > 0)
      {
         QtdElem = Configuracoes.size();
         // Monta lista com nomes das configuracoes
         ConfiguracaoReprocCfgDef Configuracao = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Configuracao = (ConfiguracaoReprocCfgDef) Configuracoes.elementAt(i);
            Temp =  Configuracao.getRelacionamento();
            Temp = Temp.replace(';', '@');

            if (i == QtdElem - 1)
            {
               Args[0] += Configuracao.getNome();
               Args[4] += Temp;
            }
            else
            {
               Args[0] += Configuracao.getNome() + ";";
               Args[4] += Temp + ";";
            }
         }
      }
      else
      {
         Args[0] = "$ARG;";
         Args[4] = "$ARG;";
      }

      if (Servidores.size() > 0)
      {
         QtdElem = Servidores.size();
         // Monta lista com nomes dos servidores de processos
         ServidorProcCfgDef Servidor = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Servidor = (ServidorProcCfgDef) Servidores.elementAt(i);
            if (i == QtdElem - 1)
               Args[1] += Servidor.getNome();
            else
               Args[1] += Servidor.getNome() + ";";
         }
      }
      else
         Args[1] = "$ARG;";

      if (Reprocessadores.size() > 0)
      {
         QtdElem = Reprocessadores.size();
         // Monta lista com nomes dos conversores
         ReprocessadorCfgDef Reprocessador = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Reprocessador = (ReprocessadorCfgDef) Reprocessadores.elementAt(i);
            if (i == QtdElem - 1)
               Args[2] += Reprocessador.getNome();
            else
               Args[2] += Reprocessador.getNome() + ";";
         }
      }
      else
         Args[2] = "$ARG;";

      if (Bilhetadores.size() > 0)
      {
         QtdElem = Bilhetadores.size();
         // Monta lista com nomes dos conversores
         BilhetadorCfgDef Bilhetador = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Bilhetador = (BilhetadorCfgDef) Bilhetadores.get(i);
            if (i == QtdElem - 1)
               Args[3] += Bilhetador.getBilhetador();
            else
               Args[3] += Bilhetador.getBilhetador() + ";";
         }
      }
      else
         Args[3] = "$ARG;";

      Args[5] = p_Mensagem;

      return Args;
   }
}
