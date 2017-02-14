//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlarmeConversor.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.VetorUtil;

/**
 */
public class OpAlarmeConversor extends OperacaoAbs 
{
   private Vector m_Linhas = null;
   private String m_Bilhetadores = null;
   private String m_Periodicidade = null;
   private String m_CfgAlr[] = null;
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C5EDFFC01EE
    */
   public OpAlarmeConversor() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C5EDFFC0234
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpAlarmeConversor - iniciaOperacao()");
      try
      {
         setOperacao("Alarme de Espaço Disco");

         montaTabela();
         String Args[];

         // Argumentos do formulário         
         Args = new String[6];
         Args[0] = p_Mensagem;
         Args[1] = m_Bilhetadores;
         Args[2] = m_CfgAlr[4];
         Args[3] = Integer.toString(m_Linhas.size());
         Args[4] = m_Html.m_Tabela.getTabelaString();
         Args[5] = m_CfgAlr[5];

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         
         if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro"))
         	m_Args[2] = "src=\"/PortalOsx/templates/js/alarmeconversor_claro.js\"";
         else
         	m_Args[2] = "src=\"/PortalOsx/templates/js/alarmeconversor.js\"";
         
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "alarmeconversor.gif";
         
         if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro"))
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeconversor_claro.form", Args);
         else
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeconversor.form", Args);
         
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "alarmeconversor.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlarmeConversor - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3DAB0B69010D
    */
   public Vector montaLinhas() 
   {
      String Args[] = null, Habilita;
      Vector Bilhetadores = new Vector(), 
      Ociosidade = new Vector(), 
      Colunas = null, 
      Habilitacao = new Vector();

      m_Linhas = new Vector();

      No noTmp = null;
      m_Bilhetadores = "";
      m_Periodicidade = "";
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
	      	try
			{
	      		noTmp = (No) iter.next();
	      		m_CfgAlr = noTmp.getConexaoServUtil().getCfgAlarme((short)3);
	            
	      		
	            Bilhetadores.addAll(VetorUtil.String2Vetor(m_CfgAlr[0],';'));
	            Ociosidade.addAll(VetorUtil.String2Vetor(m_CfgAlr[1],';'));
	            Habilitacao.addAll(VetorUtil.String2Vetor(m_CfgAlr[2],';'));
	            if(m_Bilhetadores.equals(""))
	            {
	            	m_Bilhetadores = m_CfgAlr[0];
	            }
	            else
	            {
	            	m_Bilhetadores = m_Bilhetadores+";"+m_CfgAlr[0];
	            }
	            if(m_Periodicidade.equals(""))
	            {
	            	m_Periodicidade = m_CfgAlr[3];
	            }
	            else
	            {
	            	m_Periodicidade = m_Periodicidade +";"+ m_CfgAlr[3];
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
      

      for (int i = 0; i < Bilhetadores.size(); i++)
      {
         Colunas = new Vector();
         Colunas.add("<b>"+(String)Bilhetadores.elementAt(i)+"</b>");
         Colunas.add("<input type=\"text\" size=\"10\" name=\"ociosidade\" value=\""+(String)Ociosidade.elementAt(i)+"\" class=\"inputtext\">");

         Habilita = (String)Habilitacao.elementAt(i);
         if (Habilita.equals("1") == true)
            Colunas.add("<input type=\"checkbox\" name=\"habilita\" checked=\"true\">");
         else
            Colunas.add("<input type=\"checkbox\" name=\"habilita\">");

         Colunas.trimToSize();
         m_Linhas.add(Colunas);
      }

      m_Linhas.trimToSize();
      return m_Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3DAB0B69015E
    */
   public void montaTabela() 
   {
      String Header[] = {"Bilhetador", "Ociosidade (s)", "Habilita"};
      String Largura[] = {"264",  "223", "66"};
      String Alinhamento[] = {"left", "center", "center"};
      short Filtros[] = {0, 0, 0};

      montaLinhas();
      m_Html.setTabela((short)Header.length, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=alarmeConversor");
      m_Html.m_Tabela.setElementos(m_Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}

