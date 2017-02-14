//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpGerenciaConversores.java

package Portal.Operacoes;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.BilhetadorCfgDef;
import Portal.Utils.ConfiguracaoConvCfgDef;
import Portal.Utils.ConversorCfgDef;
import Portal.Utils.ServidorProcCfgDef;

/**
 */
public class OpGerenciaConversores extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E0EE702AD
    */
   public OpGerenciaConversores() 
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
      //System.out.println("OpGerenciaConversores - iniciaOperacao()");
      try
      {
         setOperacao("Gerenciamento de Conversores");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/gerenciaconversores.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "conversoresgerenciamento.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "gerenciaconversores.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "gerenciaconversores.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpGerenciaConversores - iniciaOperacao(): "+Exc);
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
             Conversores = new Vector();
      List   Bilhetadores = new Vector();
      for (int i = 0; i < Args.length; i++)
         Args[i] = "";

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          noTmp = (No) iter.next();
          Vector configuracoes = noTmp.getConexaoServUtil().getListaCfgConversores();
          Vector servidores = noTmp.getConexaoServUtil().getListaServProcessos();
          Vector conversores = noTmp.getConexaoServUtil().getListaConversores();
          List bilhetadores = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
          
          if(configuracoes != null)
          	Configuracoes.addAll(configuracoes);
          if(servidores != null)
          	Servidores.addAll(servidores);
          if(conversores != null)
          	Conversores.addAll(conversores);
          if(bilhetadores != null)
          	Bilhetadores.addAll(bilhetadores);
      }
      
      Collections.sort(Bilhetadores);
      
      Configuracoes.trimToSize();
      Servidores.trimToSize();
      Conversores.trimToSize();
      //Bilhetadores.trimToSize();

      if (Configuracoes.size() > 0)
      {
         QtdElem = Configuracoes.size();
         // Monta lista com nomes das configuracoes
         ConfiguracaoConvCfgDef Configuracao = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Configuracao = (ConfiguracaoConvCfgDef) Configuracoes.elementAt(i);
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

      if (Conversores.size() > 0)
      {
         QtdElem = Conversores.size();
         // Monta lista com nomes dos conversores
         ConversorCfgDef Conversor = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Conversor = (ConversorCfgDef) Conversores.elementAt(i);
            if (i == QtdElem - 1)
               Args[2] += Conversor.getNome();
            else
               Args[2] += Conversor.getNome() + ";";
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
