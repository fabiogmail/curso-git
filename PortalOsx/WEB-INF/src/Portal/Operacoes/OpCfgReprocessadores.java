//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpCfgConversores.java

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
import Portal.Utils.BilhetadorCfgDef;
import Portal.Utils.ConfiguracaoReprocCfgDef;
import Portal.Utils.ReprocessadorCfgDef;
import Portal.Utils.ServidorProcCfgDef;

/**
 */
public class OpCfgReprocessadores extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpCfgConversores - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C46CCE8032D
    */
   public OpCfgReprocessadores() 
   {
      //System.out.println("OpCfgConversores - construtor");
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C46CCEF028D
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpCfgConversores - iniciaOperacao()");
      try
      {
         setOperacao("Configuração de Reprocessadores");

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/cfgreproc.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "cfgreproc.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "cfgreproc.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "cfgreproc.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpCfgReproCDRX - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @param p_Mensagem
    * @return String[]
    * @exception
    * @roseuid 3C54A27E026A
    */
   public String[] montaFormulario(String p_Mensagem)
   {
      int QtdElem = 0;
      String Temp = "", Args[] = new String[6];
      Vector Configuracoes = new Vector(), 
	  Servidores = new Vector(),
      Reprocessadores = new Vector(); 
	  List Bilhetadores = new Vector();

      for (int i = 0; i < Args.length; i++)
         Args[i] = "";

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          Vector configuracoes = noTmp.getConexaoServUtil().getListaCfgReprocessadores();
	          Vector servidores = noTmp.getConexaoServUtil().getListaServProcessos();
	          List bilhetadores = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
	          
	          if(configuracoes != null)
	          	Configuracoes.addAll(configuracoes);
	          if(servidores != null)
	          	Servidores.addAll(servidores);
	          if(bilhetadores != null)
	          	Bilhetadores.addAll(bilhetadores);
	          
	          //buscando reprocessadores do no central apenas!, pois está replicado nas 3 maquinas
	          if(noTmp.isNoCentral())
	          {
	          	Vector reprocessadores = noTmp.getConexaoServUtil().getListaReprocessadores();
	          	if(reprocessadores != null)
		          	Reprocessadores.addAll(reprocessadores);
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

      if (Servidores != null)
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

      if (Reprocessadores != null)
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

      if (Bilhetadores != null)
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
