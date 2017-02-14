//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaAlarmes.java

package Portal.Operacoes;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpListaAlarmes extends OperacaoAbs 
{
   private Vector m_ListaAlarmes = new Vector(); 
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6AA2250011
    */
   public OpListaAlarmes()
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6AA2250025
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
//      System.out.println("OpListaAlarmes - iniciaOperacao()");
      String URL; 
      
	if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro")){
		 try {	 
			 	URL = "/PortalOsx/templates/paginas/visualizaAlarmesPlataforma.jsp";
			 	//setOperacao("Selecionar Dados do Relatório de Sequência");	
				m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);
				
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException Exc) {
				 System.out.println("OpListaAlarmes - iniciaOperacao(): " + Exc);
				 Exc.printStackTrace();
				 return false;
			}
			return true;
	}
	else{
	   
	   try
      {
         setOperacao("Lista de Alarmes");
         String Args[], Arquivos = "", Arquivo;
         Args = new String[4];
         int QtdAlarmes = 0;

         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
         	  try
         	  {
         		  noTmp = (No) iter.next();
         		  Vector listaAlarme = noTmp.getConexaoServUtil().getListaMensagensAlarmes();
         		  if(listaAlarme != null)
         		  {
         			 m_ListaAlarmes.addAll(listaAlarme);
         			 for(int i = 0;i < listaAlarme.size();i++)
         			 {
         				Arquivo = (String)m_ListaAlarmes.elementAt(i);
                        Arquivos += Arquivo +"-"+noTmp.getHostName()+";";
         			 }
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
         
         
         m_ListaAlarmes.trimToSize();
         QtdAlarmes = m_ListaAlarmes.size();
         if(m_ListaAlarmes.size() > 0)
         {
        	 Arquivos = Arquivos.substring(0, Arquivos.length()-1);
         }
         
            
            
         

         //Tipo = m_Request.getParameterValues("tipo");
         
         montaTabela();
         Args[0] = Integer.toString(QtdAlarmes);
         Args[1] = Arquivos;
         Args[2] = p_Mensagem;
         Args[3] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listaalarmes.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/lixo0.gif','/PortalOsx/imagens/lixo1.gif'); IniciaDelecao()\""; // onload
         m_Args[4] = "listaalarmes.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listaalarmes.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listaalarmes.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaAlarmes - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
  }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C6AA5A90372
    */
   public Vector montaLinhas() 
   {
      Vector Linhas, Colunas, listaAlarmesNo = new Vector();
      char TipoAlr;
      String Arquivo, Aux = "", Data;

      //ListaAlarmes = m_ConexUtil.getListaMensagensAlarmes();
      Linhas = new Vector();      
      No noTmp = null;
      int contAlarmes = 0;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {      	
	          noTmp = (No) iter.next();
	          listaAlarmesNo = noTmp.getConexaoServUtil().getListaMensagensAlarmes();
	          
	      
		      if (listaAlarmesNo != null)
		      {
		         for (int i = 0; i < listaAlarmesNo.size(); i++)
		         {
		            Colunas = new Vector();
		            Arquivo = (String)listaAlarmesNo.elementAt(i);
		            TipoAlr = Arquivo.charAt(Arquivo.indexOf("-")+2);
		            switch (TipoAlr)
		            {
		               case '0':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Processo Parado</a>");
		                  break;            
		               case '1':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Espa&ccedil;o de Disco</a>");
		                  break;
		               case '2':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Espa&ccedil;o Excedido</a>");
		                  break;
		               case '3':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Conversor Ocioso</a>");
		                  break;
		               case '4':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Quantidade de Arquivos no Dir In</a>");
		                  break;
		               case '5':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Parser - CDR Com Data Futura</a>");
		                  break;
		               case '6':
		                  Colunas.add("<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=visualizaAlarme&arquivo="+Arquivo+"\" onmouseover=\"window.status='Lê alarme';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">Parser - Arquivo de CDR original</a>");
		                  break;
		               default:
		                  Colunas.add("Tipo de alarme desconhecido!");
		                  break;
		            }
		
		            Data = Arquivo.substring(Arquivo.indexOf("D")+1, Arquivo.length() - 4);
		            Data = Data.replace('@', '/');
		            Data = Data.replace('_', ' ');
		            Data = Data.replace('.', ':');
		            Arquivo += "-"+noTmp.getHostName();
		            if (NoUtil.listaDeNos.size() > 1)
		            {
		                Colunas.add(noTmp.getHostName());
		            }
		            Colunas.add(Data);
		            
		            //Colunas.add("<input type=\"checkbox\" name=\"apaga\" value=\""+Arquivo+"\">");
		            Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+contAlarmes+"','','/PortalOsx/imagens/lixo1.gif',1,'"+contAlarmes+"','"+Arquivo+"')\" onmouseover=\"window.status='Marca/desmarca o alarme para apagar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/lixo0.gif\" border=\"0\" name=\"Image"+contAlarmes+"\"></a>\n");
		            Colunas.trimToSize();
		            Linhas.add(Colunas);
		            contAlarmes++;
		         }
		      
		      }
		      else
		      {
		         Colunas = new Vector();      
		         Colunas.add("N&atilde;o h&aacute; alarmes");
		         if (NoUtil.listaDeNos.size() > 1)
	             {
	                Colunas.add(noTmp.getHostName());
	             }
		         Colunas.add("&nbsp;");
		         Colunas.add("&nbsp;");
		         Colunas.trimToSize();
		         Linhas.add(Colunas);
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
      Linhas.trimToSize();
      return Linhas;
   }

   /**
    * @return void
    * @exception 
    * @roseuid 3C6AA5A90386
    */
   public void montaTabela() 
   {
      String Header[] = null;
      String Alinhamento[] = null;
      String Largura[] = null;
      short Filtros[] = null;
      
      if (NoUtil.listaDeNos.size() > 1)
      {
          Header = new String[] {"Tipo do Alarme", "Servidor", "Data/Hora de Gera&ccedil;&atilde;o", "Apaga"};
          Alinhamento = new String[] {"left", "center", "center", "center"};
          Largura = new String[] {"133","100", "233", "80"};
          Filtros = new short[] {1, 1, 1, 0};
      }
      else
      {
          Header = new String[] {"Tipo do Alarme", "Data/Hora de Gera&ccedil;&atilde;o", "Apaga"};
          Alinhamento = new String[] {"left", "center", "center"};
          Largura = new String[] {"233", "233", "80"};
          Filtros = new short[] {1, 1, 0};
      }
      Vector Linhas = null;

      Linhas = montaLinhas();
      m_Html.setTabela((short)3, true);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaAlarmes");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
