//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpBDInconsistencia.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.PerfilCfgDef;

/**
 */
public class OpBDInconsistencia extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpBDInconsistencia - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C4707EB03C6
    */
   public OpBDInconsistencia() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C4707EB03DA
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Inconsistência de Base de Usuários");

         PerfilCfgDef Perfil;
         String Opcao[],  PerfisVer = null;

         Opcao = m_Request.getParameterValues("opcao");
         if (Opcao != null)
         {
/*            montaTabela();
            m_Args = new String[7];
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "listagen.htm";
            m_Args[2] = ""; // javascript
            m_Args[3] = ""; // onload
            m_Args[4] = "inconsistencia.gif";
            m_Args[5] = m_Html.m_Tabela.getTabelaString();
            m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "inconsistenciacfgusrresul.txt", null);
*/
            iniciaArgs(2);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "fix1.htm";
            m_Html.enviaArquivo(m_Args);

            verifica ();

            iniciaArgs(3);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "fix2.htm";
            m_Args[2] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "inconsistenciacfgusrresul.txt");
            m_Html.enviaArquivo(m_Args);
         }
         else
         {
            iniciaArgs(7);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
            m_Args[1] = "formgen.htm";
            m_Args[2] = "src=\"/PortalOsx/templates/js/inconsistenciacfgusr.js\"";
            m_Args[3] = "onLoad=\"Processa(0)\"";
            m_Args[4] = "inconsistencia.gif";
            m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "inconsistenciacfgusr.form", montaFormulario(p_Mensagem));
            m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "inconsistenciacfgusr.txt", null);
            m_Html.enviaArquivo(m_Args);
         }

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpBDInconsistencia - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C5AD2F502E7
    */
   public void verifica() 
   {
      PerfilCfgDef Perfil;
      String Opcao[],  PerfisVer = null;
      Vector Perfis = null;

      Opcao = m_Request.getParameterValues("opcao");
      if (Opcao != null)
      {
         PerfisVer = m_Request.getParameter("perfis");
         No noTmp = null;
         boolean haInconsistencia = false;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             try
             {
             noTmp = (No) iter.next();
             
             /**
              * O ServUtil so ira tratar os perfis os quais eh responsavel.
              * */
             if (Opcao[0].equals("verificacao"))
                 haInconsistencia = noTmp.getConexaoServUtil().verificaInconsistencia(PerfisVer);
             else if (Opcao[0].equals("correcao"))
                 haInconsistencia = noTmp.getConexaoServUtil().corrigeInconsistencia(PerfisVer);
             
             if (haInconsistencia && noTmp.getConexaoServUtil().existePerfilbyNome(PerfisVer))
             {
                 noTmp.getConexaoServUtil().setInfoArq("inconsistencia", Opcao[0]);
                 
                 boolean bNaoSai = true;
                 String Linha;

                 int Cont = 0;
                 do
                 {
                    Linha = noTmp.getConexaoServUtil().getLinhaArq();
                    if (Linha.length() == 0)
                       bNaoSai = false;
                    else
                    {
                       if (Linha.endsWith("Processo ServUtil\n") == false)
                       {
                          if (Cont%2 == 0)
                             m_Html.getSaidaHtml().print("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
                          else
                             m_Html.getSaidaHtml().print("<tr bgcolor=\"#FFFFFF\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
                          Cont++;
                       }
                    }
                 } while (bNaoSai);
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
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3CFE6A7C00D5
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[];
      Vector Perfis = new Vector();

      Args = new String[2];
      Args[0] = "";
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
	          noTmp = (No) iter.next();
	          Perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisCfg());
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
      
      if (Perfis != null)
      {
         int QtdElem = Perfis.size();
         PerfilCfgDef Perfil;

         // Monta lista com nomes dos perfis
         for (short i = 0; i < QtdElem; i++)
         {
            Perfil = (PerfilCfgDef) Perfis.elementAt(i);
            if (i == QtdElem - 1)
               Args[0] += Perfil.getPerfil();
            else
               Args[0] += Perfil.getPerfil() + ";";
         }
      }
      else
         Args[0] = "Erro! Lista de perfis não pode ser vazia!";

      Args[1] = p_Mensagem;

      return Args;
   }
}
