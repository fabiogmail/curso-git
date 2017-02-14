//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVisualizaAlarme.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;

/**
 */
public class OpVisualizaAlarme extends OperacaoAbs 
{
    private No no;
    private String Arquivo;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6ACCC20108
    */
   public OpVisualizaAlarme() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6ACCBF013F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpVisualizaAlarme - iniciaOperacao()");
      try
      {
         setOperacao("Visualização de Alarme");
         montaTabela();
         String Args[] = new String[2];
         Args[0] = Arquivo;
         Args[1] = m_Html.m_Tabela.getTabelaString();
         m_Args = new String[7];         
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/alarme.js\""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "alarme.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarme.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "alarme.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpVisualizaAlarme - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();         
		 return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C6BB85D02D1
    */
   public Vector montaLinhas() 
   {
      Vector Linhas = new Vector();

      Arquivo = m_Request.getParameter("arquivo");
      
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
	      	try
			{
	      		no = (No) iter.next();
	      		Linhas = no.getConexaoServUtil().getMensagemAlarme(Arquivo);	      		
	      		if (Linhas.size() > 0 ){
	      			Arquivo += "-"+no.getHostName();
	      			break;
	      		}
			}
	        catch(COMM_FAILURE comFail)
	        {
	            System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+no.getHostName()+").");
	        }
	        catch(BAD_OPERATION badOp)
	        {
	            System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+no.getHostName()+").");
	            badOp.printStackTrace();
	        }
      }
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C6BB85D0317
    */
   public void montaTabela() 
   {
      String Header[] = null; 
      String Alinhamento[] = {"left"};
      String Largura[] = {"546"};
      Vector Linhas = null;
      Linhas = montaLinhas();
      
      if (NoUtil.listaDeNos.size() > 1) 
      {
          Header = new String[] {"Alarme - Servidor "+no.getHostName()};
      }
      else
      {
          Header = new String[] {"Alarme"};
      }
      
      if (Linhas == null)
      {
         Vector Colunas = new Vector();
         Colunas.add("Houve erro na recuperação da mensagem de alarme!");
         Linhas = new Vector();
         Linhas.add(Colunas);
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
