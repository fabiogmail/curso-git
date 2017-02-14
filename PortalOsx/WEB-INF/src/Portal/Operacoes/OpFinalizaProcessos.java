package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.Mensagem;
import Portal.Utils.UsuarioDef;
import Portal.Utils.VetorUtil;


/**
 */
public class OpFinalizaProcessos extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C45B9C300CC
    */
   public OpFinalizaProcessos() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C45B9C30144
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      String Sessao = null, Remetente = null;
      UsuarioDef Usuario;
		Vector ListaUsuarios = null;

      try
      {
         setOperacao("Finalização de Processos");
         
         Sessao = m_Request.getSession().getId();
			Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(Sessao);
         if (Usuario == null)
            Remetente = "N/A";
			else
				Remetente = Usuario.getUsuario();

         String Processo, Retorno = "";
         OpListaProcessos ListaProcessos;
         OpListaProcessosCtrl ListaProcessosCtrl;
         // Recupera os parâmetros
         String Processos   = m_Request.getParameter("processos");
         String Tipo        = m_Request.getParameter("tipo");
         String Usuarios    = m_Request.getParameter("usuarios");
         Vector ListaProc = VetorUtil.String2Vetor(Processos,';');
         ListaUsuarios = VetorUtil.String2Vetor(Usuarios,';');
         String hostName = null;
         String pid = null;

         for (int i = 0; i < ListaProc.size(); i++)
         {
            Processo = (String)ListaProc.elementAt(i);
            
            if (Processo.indexOf("-") != -1)
            {
            	pid = Processo.substring(0,Processo.indexOf("-"));
                hostName = Processo.substring(Processo.indexOf("-")+1, Processo.length());
            }            
            
            if (Tipo.equalsIgnoreCase("util") == true)
            {
               if (hostName.equalsIgnoreCase("localhost") || hostName.equalsIgnoreCase(NoUtil.getNo().getHostName()))
               {
                   Retorno += NoUtil.getNo().getConexaoServUtil().finalizaProcesso(Integer.parseInt(pid), "", "", "");
               }
               else
               {
                   Retorno += NoUtil.getNoByHostName(hostName).getConexaoServUtil().finalizaProcesso(Integer.parseInt(pid), "", "", "");
               }
            }
            else if (Tipo.equalsIgnoreCase("ctrl") == true)
            {
                if (hostName.equalsIgnoreCase("localhost") || hostName.equalsIgnoreCase(NoUtil.getNo().getHostName()))
                {
                    Retorno += NoUtil.getNo().getConexaoServControle().finalizaProcesso(Integer.parseInt(pid));
                }
                else
                {
                    Retorno += NoUtil.getNoByHostName(hostName).getConexaoServControle().finalizaProcesso(Integer.parseInt(pid));
                }
            }

         }

         Thread.sleep(18000);//aumento do tempo porque a thread acordava antes do processo morrer
         if (Tipo.equals("util") == true)
         {
            String Tipo2 = m_Request.getParameter("tipo2");         
            Retorno = Tipo2 + "@" + Retorno;
            ListaProcessos = new OpListaProcessos();
            ListaProcessos.setRequestResponse(getRequest(), getResponse());
            ListaProcessos.iniciaOperacao(Retorno);
         }
         else if (Tipo.equals("ctrl") == true)
         {
            ListaProcessosCtrl = new OpListaProcessosCtrl();
            ListaProcessosCtrl.setRequestResponse(getRequest(), getResponse());
            ListaProcessosCtrl.iniciaOperacao(Retorno);
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpFinalizaProcessos - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
   
   /**
    * @param p_Remetente
    * @param p_Usuario
    * @return void
    * @exception 
    * @roseuid 3DCC228200A5
    */
   public void enviaMensagem(String p_Remetente, String p_Usuario) 
   {
       UsuarioDef usuario = null;
       No noTmp = null;

       for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
       {
           try
           {
	          noTmp = (No) iter.next();
	          usuario = noTmp.getConexaoServUtil().getUsuario(p_Usuario);
	          
	          if (usuario != null)
	          {
	              Mensagem Msg = new Mensagem(noTmp.getConexaoServUtil());
	              String DataStr = noTmp.getConexaoServUtil().getDataHoraAtual();
	
	              Msg.setRemetente(p_Remetente);
	              Msg.setUsuario(p_Usuario);
	              Msg.setAssunto("Desconexao do CDRView Analise");
	              if (p_Remetente.equals("N/A") == true)
	              {                  
	                  Msg.setMensagem("Você foi desconectado do CDRView Analise em " + DataStr + ".");
	              }
	              else
	              {
	                  Msg.setMensagem("Você foi desconectado do CDRView Analise pelo usu&aacute;rio " + p_Remetente + " em " + DataStr + ".");
	              }
	              
	              Msg.envia();
	              break;
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
   }
}
