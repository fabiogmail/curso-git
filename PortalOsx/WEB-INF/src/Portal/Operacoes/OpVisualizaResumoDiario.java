//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpVisualizaResumoDiario.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

public class OpVisualizaResumoDiario extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6C181203E0
    */
   public OpVisualizaResumoDiario() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6C181301BB
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpVisualizaResumoDiario - iniciaOperacao()");
      try
      {
         setOperacao("Resumo - Resumo Diário");
         String Args[];
         Args = new String[2];
         montaTabela();
         Args[0] = m_Request.getParameter("data");
         Args[1] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "resumodiario.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "visualizaresumodiario.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "visualizaresumodiario.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
        System.out.println("OpVisualizaResumoDiario - iniciaOperacao(): "+Exc);
        Exc.printStackTrace();
        return false;
      }
   }

   /**
    * @return Vector
    * @exception
    * @roseuid 3C6C18130297
    */
   public Vector montaLinhas()
   {
      Vector Linhas = new Vector();
      Vector linhasTmp = new Vector();
      int[] TotalParcial = new int[24];
      int Total = 0;
      for (int i = 0; i < 24; i++)
			TotalParcial[i] = 0;

      String DataResumo = m_Request.getParameter("data");

   	 
   	  No noTmp = null;
  	  for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
  	  {
  		  try
  		  {
  			  noTmp = (No) iter.next();
  			  Vector linhasAux = noTmp.getConexaoServUtil().getResumoDiario(DataResumo);
  			  if(linhasAux != null)
  			  {
  				  Total += Integer.parseInt((String)linhasAux.get(linhasAux.size()-2));
  				  int[] TotalParcialTmp = (int[]) linhasAux.get(linhasAux.size()-1);
  				  for (int i = 0; i < 24; i++)
  				  {
  					  TotalParcial[i] += TotalParcialTmp[i];
  				  }
  				  
  				  linhasAux.removeElementAt(linhasAux.size()-1);
				  linhasAux.removeElementAt(linhasAux.size()-1);
				  if(NoUtil.isAmbienteEmCluster())
			  	  {
					  linhasAux.removeElementAt(linhasAux.size()-1);
			  	  }
  				  linhasAux.removeElementAt(0);
				  linhasTmp.addAll(linhasAux);
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
  	  
  	
  	  if(NoUtil.isAmbienteEmCluster() && linhasTmp.size()>0)
	  {
  		  String Linha = "", Tabela = "";
  		  Vector Coluna = new Vector();  		  
  		  Coluna.add("<b>&nbsp;TOTAL&nbsp;</b>");
  		  Coluna.add("-");
  		  Coluna.add("<b>" + new Integer(Total).toString() + "</b>");
  		  Tabela = "<table border=\"0\" width=\"100%\">\n";
  		  Tabela += "<tr>\n";
  		  
  		  short i = 0;
  		  short Max = 8;
  		  for (; i < Max; i++)
  		  {
  			  if (i != (Max - 8))
  			  {
  				  Linha += ("<td align=\"center\">" + TotalParcial[i] +
  				  "</td>");
  			  }
  			  else
  			  {
  				  //Linha += "<tr bgcolor=\"#000099\">\n";
  				  Linha += "<tr>\n";
  				  for (short j = i; j < Max; j++)
  					  Linha += ("<td align=\"center\"><b>" + j + "</b></td>");
					
  				  Linha += "\n";
  				  Linha += "</tr>\n<tr>\n";
  				  Linha += ("<td align=\"center\">" + TotalParcial[i] + "</td>");
  			  }
  			  if (i == (Max - 1))
  			  {
  				  Linha += "\n</tr>\n";
  				  if (Max != 24)
  				  {
  					  Max += 8;
  				  }
  				  else
  				  {
  					  Tabela += Linha;
  					  Tabela += "\n</tr>\n</table>\n";
  				  }
  			  }
  		  }
  		  Coluna.add(Tabela);
  		  Coluna.trimToSize();
  		  linhasTmp.add(Coluna);
	  }
  	  
  	  linhasTmp.trimToSize();
	      
	  if (linhasTmp.size() > 0)
	  {
		  // Remove a data
		  Linhas.addAll(linhasTmp);
		  m_Request.setAttribute("atual","0");
		  m_Request.setAttribute("offset","0");
		  m_Request.setAttribute("indice","0");
		  m_Request.setAttribute("ordena","1");
	  }
	  else
	  {
		  Vector Coluna = new Vector();
		  Coluna.add("Não há informações diárias!");
		  Coluna.add("&nbsp;");
		  Coluna.add("&nbsp;");
		  Coluna.add("&nbsp;");                  
		  Coluna.trimToSize();
		  Linhas = new Vector();
		  Linhas.add(Coluna);
		  Linhas.trimToSize();
	  }
        
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C6C18140040
    */
   public void montaTabela() 
   {
      String Header[] = {"Bilhetador", "CDRs","Qtd.Arquivos", "Per&iacute;odos/Quantidade de CDRs"};
      String Alinhamento[] = {"left", "center" , "center", "center"};
      String Largura[] = {"80", "80", "80", "386"};
      String Tipo;
      short Filtros[] = {1, 2, 0, 0};
      Vector Linhas = null;

      Linhas = montaLinhas();
      m_Html.setTabela((short)4, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=visualizaResumoDiario&data="+m_Request.getParameter("data"));
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();   
   }
}
