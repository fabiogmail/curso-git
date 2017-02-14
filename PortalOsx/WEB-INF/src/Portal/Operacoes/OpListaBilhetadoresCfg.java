//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaBilhetadoresCfg.java

package Portal.Operacoes;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.BilhetadorCfgDef;


public class OpListaBilhetadoresCfg extends OperacaoAbs
{

   static
   {
      //System.out.println("OpListaBilhetadoresCfg - Carregando classe");
   }

   /**
    * @return
    * @exception
    * @roseuid 3C43A0A10053
    */
   public OpListaBilhetadoresCfg()
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception
    * @roseuid 3C43A05C00AE
    */
   public boolean iniciaOperacao(String p_Mensagem)
   {
      try
      {
         setOperacao("Lista Bilhetadores");
         montaTabela();
         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "listabilhetadores.gif";
         m_Args[5] = m_Html.m_Tabela.getTabelaString();
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listabilhetadorescfg.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaBilhetadoresCfg - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @return Vector
    * @exception
    * @roseuid 3C51C26601CC
    */
   public Vector montaLinhas()
   {
      BilhetadorCfgDef Bilhetador;
      String Status = "";
      Vector Colunas = null, Linhas = null;
      List ListaBilhetadores = new Vector();
      No noTmp = null;
      
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          List listatmp = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
	          if(listatmp != null)
	          {
	          	ListaBilhetadores.addAll(listatmp);
	          	for (int i = 0; i < listatmp.size(); i++) 
	          	{
	          		BilhetadorCfgDef bil = (BilhetadorCfgDef)listatmp.get(i);
	          		bil.setHostname(noTmp.getHostName());				
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
      
      Collections.sort(ListaBilhetadores);
      
      Linhas = new Vector();
      if (ListaBilhetadores != null)
      {
         for (short i = 0; i < ListaBilhetadores.size(); i++)
         {
            Bilhetador = (BilhetadorCfgDef)ListaBilhetadores.get(i);
            Colunas = new Vector();
            Colunas.add(Bilhetador.getBilhetador());
            Colunas.add(Bilhetador.getApelido());
            Colunas.add(Bilhetador.getTecnologia().getTecnologia());

            if (DefsComum.s_CLIENTE.equalsIgnoreCase("claro") 
            		|| DefsComum.s_CLIENTE.equalsIgnoreCase("tim")
            		|| DefsComum.s_CLIENTE.equalsIgnoreCase("oi") 
            		|| DefsComum.s_CLIENTE.equalsIgnoreCase("anatel") )
               Colunas.add(Bilhetador.getOperadora().getOperadora());
           
            if (Bilhetador.getFase() == 0)
               Status = "Produção";
            else
               Status = "Teste";

            Colunas.add(Status);
            Colunas.add(Bilhetador.getHostname());
            Colunas.trimToSize();
            Linhas.add(Colunas);
         }
      }
      else
      {
         Colunas = new Vector();
         Colunas.add("N&atilde;o h&atilde; bilhetadores configurados!");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");                  
         if (DefsComum.s_CLIENTE.equalsIgnoreCase("claro") 
        		 || DefsComum.s_CLIENTE.equalsIgnoreCase("tim")
        		 || DefsComum.s_CLIENTE.equalsIgnoreCase("oi")
            		|| DefsComum.s_CLIENTE.equalsIgnoreCase("anatel") )
            Colunas.add("&nbsp;");
         
         Colunas.trimToSize();
         Linhas.add(Colunas);
      }

      Linhas.trimToSize();
      return Linhas;
   }

   /**
    * @return void
    * @exception
    * @roseuid 3C59E99A015F
    */
   public void montaTabela()
   {
      String Tipo = null;
      Vector Linhas;
      String Header[] = null;
      String Largura[] = null; //;{"180", "180", "97", "96"};
      String Alinhamento[] = null; //{"left", "center", "center", "center"};
      short Filtros[] = null; // {1, 1, 1, 1};

      if (DefsComum.s_CLIENTE.toLowerCase().equals("claro") == true 		  
    		  || DefsComum.s_CLIENTE.toLowerCase().equals("oi") == true
    		  || DefsComum.s_CLIENTE.toLowerCase().equals("tim") == true
    		  || DefsComum.s_CLIENTE.toLowerCase().equals("anatel") == true)
      {
         Header = new String[6];
         Header[0] = "Bilhetador";
         Header[1] = "Apelido";
         Header[2] = "Tecnologia";
         Header[3] = "Regional";
         Header[4] = "Status";
         Header[5] = "Servidor";

         Largura = new String[6];
         Largura[0] = "150";
         Largura[1] = "150";
         Largura[2] = "97";
         Largura[3] = "60";
         Largura[4] = "96";
         Largura[5] = "60";

         Alinhamento = new String[6];
         Alinhamento[0] = "left";
         Alinhamento[1] = "center";
         Alinhamento[2] = "center";
         Alinhamento[3] = "center";
         Alinhamento[4] = "center";
         Alinhamento[5] = "center";

         Filtros = new short[6];
         Filtros[0] = 1;
         Filtros[1] = 1;
         Filtros[2] = 1;
         Filtros[3] = 1;
         Filtros[4] = 1;
         Filtros[5] = 1;
      }
      else if(DefsComum.s_CLIENTE.toLowerCase().equals("tim") == true)
      {
         Header = new String[5];
         Header[0] = "Bilhetador";
         Header[1] = "Apelido";
         Header[2] = "Tecnologia";
         Header[3] = "Status";
         Header[4] = "Servidor";
         if (DefsComum.s_CLIENTE.toLowerCase().equals("americel") == true)
            Header[1] = "C&oacute;ds.&Aacute;rea/Pr&eacute;-Pago";   

         Largura = new String[5];
         Largura[0] = "180";
         Largura[1] = "180";
         Largura[2] = "97";
         Largura[3] = "96";
         Largura[4] = "60";

         Alinhamento = new String[5];
         Alinhamento[0] = "left";
         Alinhamento[1] = "center";
         Alinhamento[2] = "center";
         Alinhamento[3] = "center";
         Alinhamento[4] = "center";

         Filtros = new short[5];
         Filtros[0] = 1;
         Filtros[1] = 1;
         Filtros[2] = 1;
         Filtros[3] = 1;   
         Filtros[4] = 1;
      }
      else
      {
         Header = new String[4];
         Header[0] = "Bilhetador";
         Header[1] = "Apelido";
         Header[2] = "Tecnologia";
         Header[3] = "Status";
         if (DefsComum.s_CLIENTE.toLowerCase().equals("americel") == true)
            Header[1] = "C&oacute;ds.&Aacute;rea/Pr&eacute;-Pago";   

         Largura = new String[4];
         Largura[0] = "180";
         Largura[1] = "180";
         Largura[2] = "97";
         Largura[3] = "96";

         Alinhamento = new String[4];
         Alinhamento[0] = "left";
         Alinhamento[1] = "center";
         Alinhamento[2] = "center";
         Alinhamento[3] = "center";

         Filtros = new short[4];
         Filtros[0] = 1;
         Filtros[1] = 1;
         Filtros[2] = 1;
         Filtros[3] = 1;               
      }

      Linhas = montaLinhas();
      Tipo = m_Request.getParameter("tipo");
      if (Tipo.equals("parcial") == true)
         m_Html.setTabela((short)Header.length, true);
      else
         m_Html.setTabela((short)Header.length, false);

      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaBilhetadores&tipo="+Tipo);
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
