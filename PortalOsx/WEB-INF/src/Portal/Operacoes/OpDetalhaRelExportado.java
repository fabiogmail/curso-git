//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpDetalhaRelExportado.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Interfaces.iPerfil;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.PerfilCfgDef;

/**
 */
public class OpDetalhaRelExportado extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C7466D1012F
    */
   public OpDetalhaRelExportado()
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C7466D10157
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpDetalhaRelExportado - iniciaOperacao()");
      try
      {
         //setOperacao("Detalha Relatório Agendado");
         iniciaArgs(6);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "detalheformgen.htm";
         m_Args[2] = "Relatório Exportado";
         m_Args[3] = "";
         m_Args[4] = "";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "detalherelexportado.form", montaFormulario());
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpDetalhaRelExportado - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @return String[]
    * @exception 
    * @roseuid 3C7466D1016B
    */
   public String[] montaFormulario() 
   {
      String Args[], QtdCDRs = "";
      Vector Linhas = null;
      
      String Perfil  = m_Request.getParameter("perfil");
      String nomePerfil = m_Request.getParameter("nomePerfil");
      String Arquivo = m_Request.getParameter("arquivo");
      
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          PerfilCfgDef perf;
	          
	          iPerfil perfilServ = noTmp.getConexaoServUtil().getM_iListaPerfis().fnPerfilExisteByID(Integer.parseInt(nomePerfil));
	          
	          if (perfilServ != null)
	          {
	              Linhas = noTmp.getConexaoServUtil().getRelatorioExpHeader(Perfil,Arquivo);
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
               case 1:
                  QtdCDRs = (String)Linhas.elementAt(i);
                  if (QtdCDRs.equals("0"))
                     Args[1] = "Sem Filtro";
                  else
                     Args[1] = QtdCDRs;
                  break;
               case 2:
                  String Aux = null, Bilhetadores = "";
                  Aux = (String)Linhas.elementAt(i);
                  while (Aux != null)
                  {
                    if (Aux.indexOf(';') != -1)
                    {
                       Bilhetadores += Aux.substring(0, Aux.indexOf(';')) + ", ";
                       Aux = Aux.substring(Aux.indexOf(';')+1, Aux.length());
                    }
                    else
                    {
                      Bilhetadores += Aux;
                      Aux = null;
                    }
                  }
                  Args[i] = Bilhetadores;
                  break;
               case 3:
               case 4:
               case 5:
                  Aux = (String)Linhas.elementAt(i);
                  Args[i] = Aux.substring(6,8) +"/"+ Aux.substring(4,6) +"/"+  Aux.substring(0,4)+" "+
                            Aux.substring(8,10) +":"+ Aux.substring(10,12) +":"+ Aux.substring(12,Aux.length());
                  break;
               default:
                  Args[i] = (String)Linhas.elementAt(i);
                  if (i == 0 && Args[i].indexOf(".cabe") != -1)
                  {
                     Args[i] = Args[i].substring(0, Args[i].indexOf(".cabe"));
                  }
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
