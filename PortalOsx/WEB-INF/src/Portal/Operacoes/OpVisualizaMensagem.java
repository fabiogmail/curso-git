//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVisualizaMensagem.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpVisualizaMensagem extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6D054600C6
    */
   public OpVisualizaMensagem() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6BF17303BB
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpVisualizaMensagem - iniciaOperacao()");
      try
      {
         setOperacao("Leitura de Mensagem");
         String Args[] = new String[2];
         montaTabela();
         Args[0] = m_Request.getParameter("arquivo");
         Args[1] = m_Html.m_Tabela.getTabelaString();
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/mensagem.js\""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "mensagem.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "mensagem.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "mensagem.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpVisualizaMensagens - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C6C258C01C6
    */
   public Vector montaLinhas() 
   {
      String Arquivo;
      Vector Linhas = new Vector();
      No noTmp = null;
      Arquivo = m_Request.getParameter("arquivo");
      
      

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	try
		{
      	  UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());	  
          noTmp = (No) iter.next();
          Linhas = noTmp.getConexaoServUtil().getMensagemOutras(Arquivo + "#" + Usuario.getUsuario());
          
          if (Linhas != null  && Linhas.size()> 0)
              break;
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
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C6C258C01DA
    */
   public void montaTabela() 
   {
      String Header[] = new String[1];
      String Alinhamento[] = {"left"};
      String Largura[] = {"546"};
      String Arquivo, Origem, Assunto, Data;
      Vector Linhas = null;

      Arquivo = m_Request.getParameter("arquivo");
      // Monta o cabeçalho da mensagem
      Origem = Arquivo.substring(0, Arquivo.indexOf("_")); //Origem
      Assunto = Arquivo.substring(Arquivo.indexOf("=")+1, Arquivo.indexOf("$")); //Header
      Data = Arquivo.substring(Arquivo.indexOf("$")+1, Arquivo.length() - 4);
      Data = Data.replace('@', '/');
      Data = Data.replace('_', ' ');
      Data = Data.replace('.', ':');
      if (Assunto.length() > 30)
         Assunto = Assunto.substring(0, 30) + "...";
      Header[0] = "De: ["+Origem+"]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Assunto: ["+Assunto+"]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data: ["+Data+"]";

      Linhas = montaLinhas();
      if (Linhas == null)
      {
         Vector Colunas = new Vector();
         Colunas.add("Houve erro na recuperação da mensagem de alarme!");
         Colunas.trimToSize();
         Linhas = new Vector();
         Linhas.add(Colunas);
         Linhas.trimToSize();
      }

      m_Html.setTabela((short)1, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
