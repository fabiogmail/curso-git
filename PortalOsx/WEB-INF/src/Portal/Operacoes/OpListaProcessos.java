//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaProcessos.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ProcessoDef;


public class OpListaProcessos extends OperacaoAbs 
{
   private String m_PIds = "";
   private short m_QtdProcessos = 0;
   private short m_PosIni = 0;
   private String m_Tipo;
   private String listagem;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C44BBF90321
    */
   public OpListaProcessos() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C44BC04018C
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         String Args[], TipoTmp = "";
         if (p_Mensagem.equals("$ARG;") == false && p_Mensagem.indexOf("@") != -1)
         {
            m_Tipo = p_Mensagem.substring(0,p_Mensagem.indexOf("@"));
            p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
         }
         else
            m_Tipo = m_Request.getParameter("tipo");

         listagem = m_Request.getParameter("listagem");
         TipoTmp = m_Tipo.toUpperCase();
         TipoTmp = TipoTmp.substring(0, 1) + m_Tipo.substring(1,m_Tipo.length());

         setOperacao("Processos/"+TipoTmp);
         montaTabela();
         m_PosIni = 1;
         Args = new String[8];

         Args[0] = "util";
         Args[1] = m_Tipo.toLowerCase();
         Args[2] = Short.toString(m_PosIni);
         Args[3] = Integer.toString(m_QtdProcessos);
         Args[4] = m_PIds;
         Args[5] = "N/A";
         Args[6] = p_Mensagem;
         Args[7] = m_Html.m_Tabela.getTabelaString();

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listaprocessos.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/selec.gif','/PortalOsx/imagens/deselec.gif'); IniciaFinalizacao(); VerificaMensagem()\""; // onload

         if (m_Tipo.toLowerCase().equals("deteccao"))
            m_Args[4] = "deteccaomanutencao.gif";
         else if (m_Tipo.toLowerCase().equals("util"))
            m_Args[4] = "webmanutencao.gif";
         else if (m_Tipo.toLowerCase().equals("parser"))
            m_Args[4] = "parsersmanutencao.gif";
         else if (m_Tipo.toLowerCase().equals("parsergen"))
             m_Args[4] = "parsersmanutencao.gif";//mudar o gif
         else if (m_Tipo.toLowerCase().equals("conversor"))
            m_Args[4] = "conversoresmanutencao.gif";
         else if (m_Tipo.toLowerCase().equals("reprocessador"))
         	m_Args[4] = "reprocessadoresmanutencao.gif";

         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listaprocessos.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listaprocessosutil.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaProcessos - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C59F74302A1
    */
   public Vector montaLinhas() 
   {
      ProcessoDef Processo,Processos_Agn_Hist;
      boolean bTemProcesso = false;
      int Index = 1;
      Vector ListaProcessos = new Vector(), Linhas = new Vector(), Colunas = null, ListaProcessosCtrl=null;
      String Processos = "";

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          Vector listaproc = noTmp.getConexaoServUtil().getListaProcessos();
	          if(listaproc != null)
	          	ListaProcessos.addAll(listaproc);
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
      
      if (ListaProcessos.size() > 0)
      {
         for (short i = 0; i < ListaProcessos.size(); i++)
         {
            Processo = (ProcessoDef)ListaProcessos.elementAt(i);
            if (m_Tipo.equals("deteccao"))
            {
               if (Processo.getNome().toLowerCase().startsWith("agncdr") == false &&
            	   Processo.getNome().toLowerCase().startsWith("servalarmes") == false &&
            	   Processo.getNome().toLowerCase().startsWith("geraalarmes") == false &&
                   Processo.getNome().toLowerCase().startsWith("servalr") == false)
                  continue;
               else
                  bTemProcesso = true;
            }
            if (m_Tipo.equals("util"))
            {
               if (Processo.getNome().toLowerCase().startsWith("servutil") == false &&
               	   Processo.getNome().toLowerCase().startsWith("servhist") == false &&
				   Processo.getNome().toLowerCase().startsWith("servagn") == false)
                  continue;
               else
                  bTemProcesso = true;
            }
            else if (m_Tipo.toLowerCase().equals("deteccao"))
            {
               if (Processo.getNome().toLowerCase().startsWith("agncdr") == false &&
            	   Processo.getNome().toLowerCase().startsWith("servalarmes") == false &&
            	   Processo.getNome().toLowerCase().startsWith("geraalarmes") == false &&
                   Processo.getNome().toLowerCase().startsWith("servalr") == false)
                  continue;
               else
                  bTemProcesso = true;
            }
            //comparação para o parsergen por enquanto com o id dele que é 12)
            else if (m_Tipo.toLowerCase().equals("parsergen"))
            {
            	if (DefsComum.s_CLIENTE.toUpperCase().equals("CTBC") && (Processo.getNome().startsWith("parser"))){
            		bTemProcesso = true;
            	}else{
            		if(Processo.getTipoProc().value() == ProcessoDef.ID_PROC_EXP_BANCO)
            			bTemProcesso = true;
            		else
            			continue;
            	}
            }
            else if (m_Tipo.toLowerCase().equals("parser"))
            {//id 11 para o parser
            	if (DefsComum.s_CLIENTE.toUpperCase().equals("CTBC") && (Processo.getNome().startsWith("parser"))){
            		bTemProcesso = true;
            	}else{
            		if(Processo.getTipoProc().value() == ProcessoDef.ID_PROC_PARSER)
            			bTemProcesso = true;
            		else
            			continue;
            	}
            }
            else if (Processo.getNome().toLowerCase().startsWith(m_Tipo.toLowerCase()) == false)
               continue;
            else
               bTemProcesso = true;
            
            
            
            

            Colunas = new Vector();
            Colunas.add(Processo.getNome());
            Colunas.add(Processo.getDataInicioStr());
            Colunas.add("<a href=\"javascript:AbreJanela('detalhesProcesso', 'util', '"+Processo.getId()+" - "+ Processo.getHost() +"')\" onmouseover=\"window.status='Ver detalhes';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+ Processo.getId()+ " - " + Processo.getHost()+"</a>");

            if (Processo.getNome().toLowerCase().startsWith("servutil") == false &&
                Processo.getNome().toLowerCase().startsWith("servctrl") == false)
            {
                  Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+Index+"','','/PortalOsx/imagens/selec.gif',1,'"+Index+"','"+Processo.getId()+"-"+Processo.getHost()+"')\" onmouseover=\"window.status='Marca/desmarca o processo para finalizar';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/deselec.gif\" border=\"0\" name=\"Image"+Index+"\"></a>\n");
                  m_QtdProcessos++;
                  m_PIds += Processo.getId()+"-"+Processo.getHost()+";";
                  Index++;
            }
            else
               Colunas.add("N/A");

            Colunas.trimToSize();
            Linhas.add(Colunas);
         }

         if (bTemProcesso)
         {
            if (m_PIds.length() != 0)
               m_PIds = m_PIds.substring(0, m_PIds.length()-1);
         }
         else
         {
            Colunas = new Vector();
            Colunas.add("Todos os processos est&atilde;o fora do ar");
            Colunas.add("&nbsp;");
            Colunas.add("&nbsp;");
            Colunas.add("&nbsp;");
            Colunas.trimToSize();
            Linhas.add(Colunas);
         }
      }
      else
      {
         Colunas = new Vector();
         Colunas.add("Todos os processos est&atilde;o fora do ar");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");
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
    * @roseuid 3C59F74302B5
    */
   public void montaTabela() 
   {
      String Header[] = {"Nome", "In&iacute;cio", "Id - Host", "Finaliza"};
      String Largura[] = {"160",  "230", "87", "76"};
      String Alinhamento[] = {"left", "center", "center", "center"};
      short Filtros[] = {1, 1, 1, 0};  
      Vector Linhas;

      Linhas = montaLinhas();

      if (listagem != null)
      {
         if (listagem.equalsIgnoreCase("parcial") == true)
            m_Html.setTabela((short)5, true);
         else
            m_Html.setTabela((short)5, false);
      }
      else
      {
         listagem = "parcial";
         m_Html.setTabela((short)5, true);
      }
      
      //m_Html.setTabela((short)Header.length, false);      
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setAlturaColunas((short)20);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaProcessos&tipo="+m_Tipo+"&listagem="+listagem);
      m_Html.m_Tabela.setElementos(Linhas);
      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
