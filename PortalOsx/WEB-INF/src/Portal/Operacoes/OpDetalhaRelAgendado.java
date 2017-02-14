//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpDetalhaRelAgendado.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iPerfil;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;

/**
 */
public class OpDetalhaRelAgendado extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C7304E300BD
    */
   public OpDetalhaRelAgendado() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C7304E300D1
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpDetalhaRelAgendado - iniciaOperacao()");
      try
      {
         //setOperacao("Detalha Relatório Agendado");
         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "detalheformgen.htm";
         m_Args[2] = "Relatório Agendado";
         m_Args[3] = "";
         m_Args[4] = "";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "detalherelarmazenado.form", montaFormulario());
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpDetalhaRelAgendado - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return String[]
    * @exception 
    * @roseuid 3C73F65D0367
    */
   public String[] montaFormulario() 
   {
      String Args[];   
      Vector Linhas = null;

      String Perfil  = m_Request.getParameter("perfil");
      String TipoRel = m_Request.getParameter("tiporel");
      String IdRel   = m_Request.getParameter("idrel");
      String Arquivo = m_Request.getParameter("arquivo");
      String DataGer = m_Request.getParameter("datageracao");
      String NomeRel = m_Request.getParameter("nomerel");
      
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          
	          iPerfil perfilServ = noTmp.getConexaoServUtil().getM_iListaPerfis().fnPerfilExiste(Perfil);
	          
	          if (perfilServ != null)
	          {
	              Linhas = noTmp.getConexaoServUtil().getRelatorioHeader2((short)0, // Agendado
	                                                       Perfil,
	                                                       Integer.parseInt(TipoRel),
	                                                       Integer.parseInt(IdRel),
	                                                       Arquivo,
	                                                       NomeRel,
	                                                       DataGer);
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

      


      if (Linhas != null)
      {
         Args = new String[Linhas.size()];
         for (int i = 0; i < Linhas.size(); i++)
         {
            switch (i)
            {
               case 3:
               case 4:
               case 5:
                  String Aux = (String)Linhas.elementAt(i);
                  Args[i] = Aux.substring(6,8) +"/"+ Aux.substring(4,6) +"/"+  Aux.substring(0,4)+" "+
                            Aux.substring(8,10) +":"+ Aux.substring(10,12) +":"+ Aux.substring(12,Aux.length());
                  break;
               default:
                  Args[i] = (String)Linhas.elementAt(i);
                  if (Args[i].length() == 0)
                     Args[i] = "Sem Filtro";
                  break;
            }
         }
         return Args;
      }
      else
      {
         return null;      
      }
   }
}

