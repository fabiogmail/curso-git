
//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpCfgParsers.java

package Portal.Operacoes;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.*;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iPerfil;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.BilhetadorCfgDef;
import Portal.Utils.ConfiguracaoConvCfgDef;
import Portal.Utils.ConversorCfgDef;
import Portal.Utils.ServidorProcCfgDef;


public class OpCfgParsersGen extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8E4C8B0271
    */
   public OpCfgParsersGen()
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8E4C8B0285
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpCfgParsers - iniciaOperacao()");
      try
      {
         setOperacao("Configuração de ParsersGen");

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/cfgparsersGen.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "cfgparsers.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "cfgparsersGen.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "cfgparsers.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpCfgParsersGen - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C8E4C8B0299
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      int QtdElem = 0;
      String Temp = "", Args[] = new String[6];
      Vector Configuracoes = new Vector(), Servidores = new Vector(),
             ParsersGen = new Vector();
      List Bilhetadores = new Vector();

      for (int i = 0; i < Args.length; i++)
         Args[i] = "";
      
      No noTmp = null;
      iPerfil perfilAdmin = null;
      
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          if(DefsComum.s_CLIENTE.equalsIgnoreCase("Oi"))
	          {
	        	  Vector configuracoes = noTmp.getConexaoServUtil().getListaCfgParsersGen();
	          	  Vector parsersGen = noTmp.getConexaoServUtil().getListaParsersGen();
	          	  Vector servidores = noTmp.getConexaoServUtil().getListaServProcessos();
	          	  if(configuracoes != null)
	          	  	Configuracoes.addAll(configuracoes);
	          	  if(parsersGen != null)
	          	  	ParsersGen.addAll(parsersGen);
	          	  if(servidores != null)
	          	  	Servidores.addAll(servidores);
	          }
	          else
	          {
	          if (noTmp.isNoCentral())
	          {
	          	  Vector configuracoes = noTmp.getConexaoServUtil().getListaCfgParsersGen();
	          	  Vector parsersGen = noTmp.getConexaoServUtil().getListaParsersGen();
	          	  Vector servidores = noTmp.getConexaoServUtil().getListaServProcessos();
	          	  if(configuracoes != null)
	          	  	Configuracoes.addAll(configuracoes);
	          	  if(parsersGen != null)
	          	  	ParsersGen.addAll(parsersGen);
	          	  if(servidores != null)
	          	  	Servidores.addAll(servidores);
	          }
	          }
	          List bilhetadores = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
	          if(bilhetadores != null)
          	  	Bilhetadores.addAll(bilhetadores);
	          
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
      
      if (Configuracoes != null)
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

      if (ParsersGen != null)
      {
         QtdElem = ParsersGen.size();
         // Monta lista com nomes dos parsers
         ConversorCfgDef Conversor = null;
         for (short i = 0; i < QtdElem; i++)
         {
            Conversor = (ConversorCfgDef) ParsersGen.elementAt(i);
            if (i == QtdElem - 1)
               Args[2] += Conversor.getNome();
            else
               Args[2] += Conversor.getNome() + ";";
         }
      }
      else
         Args[2] = "$ARG;";

//      if (Bilhetadores != null)
//      {
//         QtdElem = Bilhetadores.size();
//         // Monta lista com nomes dos bilhetadores
//         BilhetadorCfgDef Bilhetador = null;
//         for (short i = 0; i < QtdElem; i++)
//         {
//            Bilhetador = (BilhetadorCfgDef) Bilhetadores.get(i);
//            if (i == QtdElem - 1)
//               Args[3] += Bilhetador.getBilhetador();
//            else
//               Args[3] += Bilhetador.getBilhetador() + ";";
//         }
//      }
//      else
//         Args[3] = "$ARG;";
      /**gato por conta do parsegen que deve receber uma string que não é um bilhetador mas funciona como um*/
      if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro")){
    	  Args[3]="RelatCdr;MOU";
      }
      if(DefsComum.s_CLIENTE.equalsIgnoreCase("Telemig")){
    	  Args[3]="SMP3;SMP5;SMP6;SMP7;LDN;Interconexao;CaixaPostal;OUVM";
      }
      if(DefsComum.s_CLIENTE.equalsIgnoreCase("GVT") || DefsComum.s_CLIENTE.equalsIgnoreCase("GVT_MKT") 
    		  || DefsComum.s_CLIENTE.equalsIgnoreCase("GVT_ADSL")){
    	  Args[3]="ENTRANTES;SAINTES;PERDIDAS";
      }
      if(DefsComum.s_CLIENTE.equalsIgnoreCase("BrasilTelecom")|| DefsComum.s_CLIENTE.equalsIgnoreCase("Oi")){
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
      }
      Args[5] = p_Mensagem;

      return Args;   
   }
}
