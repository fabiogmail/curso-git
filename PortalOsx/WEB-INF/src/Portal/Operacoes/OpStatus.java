//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpStatus.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpStatus extends OperacaoAbs 
{
   private int qtdUsuariosLogadosPortal;
   private int qtdUsuariosLogadosCDRView;
   private No no = null;
    
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3CB598930355
    */
   public OpStatus() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3CB5989D01D3
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpStatus - iniciaOperacao()");
      try
      {
         setOperacao("Status do Sistema");
         StringBuffer tabelaBuffer = new StringBuffer(200);
         
         if (NoUtil.listaDeNos.size() > 1)
         {
             m_Html.setTabela((short)2, false);
	         tabelaBuffer.append("<TABLE>");
	         
	         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	         {
	         	 try
				 {
		             no = (No) iter.next();
		             m_Html.m_Tabela.setTabelaString("");
		             montaTabela(no, true);
		             tabelaBuffer.append("<TR> <TD></TD> </TR>");
		             tabelaBuffer.append("<TR> <TD>"+m_Html.m_Tabela.getTabelaString()+"</TD> </TR>");
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
	         montaLinhasTotalizacao();
	         m_Html.m_Tabela.setTabelaString("");
	         tabelaBuffer.append("<TR> <TD></TD> </TR>");
	         tabelaBuffer.append("<TR> <TD>"+m_Html.m_Tabela.getTabelaString()+"</TD> </TR>");
	         
	         tabelaBuffer.append("</TABLE>  ");
         }
         else
         {
             montaTabela(NoUtil.getNo(), false);
             tabelaBuffer.append(m_Html.m_Tabela.getTabelaString()); 
         }
         
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = ""; // javascript
         m_Args[3] = ""; // onload
         m_Args[4] = "status.gif";
         m_Args[5] = tabelaBuffer.toString();
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "status.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpStatus - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   public void montaTabela(No no, boolean rodandoEmCluster) 
   {
      Vector Linhas = null;
         
      Linhas = montaLinhas(no);
      if (Linhas != null)
      {
         String Header[] = null;
         
         /** Se estiver rodando em cluster, mostrar o nome de cada Servidor */
         if (rodandoEmCluster)
         {
             Header = new String[] {"Informa&ccedil;&atilde;o Servidor "+no.getHostName(), "Valor"}; 
         }
         else
         {
             Header = new String[] {"Informação", "Valor"};
         }
         
         String Largura[] = {"272", "271"};
         String Alinhamento[] = {"left", "center"};
         short Filtros[] = {1, 0};

         m_Html.setTabela((short)2, false);
         m_Html.m_Tabela.setHeader(Header);
         m_Html.m_Tabela.setLarguraColunas(Largura);
         m_Html.m_Tabela.setCellPadding((short)2);
         m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
         m_Html.m_Tabela.setFiltros(Filtros);
         m_Html.m_Tabela.setAlinhamento(Alinhamento);
         m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=status");
         m_Html.m_Tabela.setElementos(Linhas);
         m_Html.m_Tabela.enviaTabela2String();
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3CB59B2900F4
    */
   public Vector montaLinhas(No no) 
   {
      Vector ListaUsuarios = new Vector(), Linhas = null, Colunas = null;

      Linhas = new Vector();

      // Qtd de Usuarios conectados ao portal
      Colunas = new Vector (2);
      Colunas.addElement("Usuários conectados ao portal:");   

      if (no.getConexaoServControle().getUsuariosCDRView() != null)
  	  {
          ListaUsuarios.addAll(no.getConexaoServControle().getUsuariosCDRView());
  	  }
  	  
  	  qtdUsuariosLogadosPortal += no.getUsuarioLogados().size();
  	  qtdUsuariosLogadosCDRView += ListaUsuarios.size();

      if (ListaUsuarios.size() != 0)
      {
         Colunas.addElement(Integer.toString(no.getUsuarioLogados().size()));
      }
      else
         Colunas.addElement("0");
      Linhas.addElement(Colunas);

      // Qtd de Usuarios utilizando o CDRView
      Colunas = new Vector (2);
      Colunas.addElement("Usuários utilizando CDRView Cliente:");
      if (ListaUsuarios.size() != 0)
      {
         Colunas.addElement(Integer.toString(ListaUsuarios.size()));
      }
      else
         Colunas.addElement("0");
      Linhas.addElement(Colunas);

/*
      // Qtd de Usuarios utilizando o CDRView Alarmes
      Colunas = new Vector (2);
      Colunas.addElement("Usuários utilizando CDRView Alarmes:");
      ListaUsuarios = m_ConexCtrl.getUsuariosCDRView();
      if (ListaUsuarios != null)
         Colunas.addElement(Integer.toString(ListaUsuarios.size()));
      else
         Colunas.addElement("0");
      Linhas.addElement(Colunas);
*/

      // Qtd de arquivos de CDRs a serem colocados na base de dados
      Colunas = new Vector (2);
      Colunas.addElement("Arquivos de CDRs a serem processados:");
      Colunas.addElement(no.getConexaoServUtil().getQtdArqsIn());
      Linhas.addElement(Colunas);

/*
      // Qtd de arquivos de alarmes a serem processados
      Colunas = new Vector (2);
      Colunas.addElement("Arquivos de alarmes a serem processados:");
      Colunas.addElement(m_ConexUtil.getQtdArqsIn());
      Linhas.addElement(Colunas);
*/

       // Qtd de mensagens de alarmes
      Colunas = new Vector (2);
      Colunas.addElement("Alarmes de plataforma a serem verificados:");
      Colunas.addElement(Short.toString(no.getConexaoServUtil().getQtdAlarmes()));
      Linhas.addElement(Colunas);

      // Espaço total do disco
      Colunas = new Vector (2);

      Colunas.addElement("Espaço total do disco:");
      Colunas.addElement(no.getConexaoServUtil().getHDTotal());
      Linhas.addElement(Colunas);

      // Espaço total livre do disco
      Colunas = new Vector(2);
      Colunas.addElement("Espaço livre do disco:");
      Colunas.addElement(no.getConexaoServUtil().getHDLivre());
      Linhas.addElement(Colunas);

      // Espaço total ocupado do disco
      Colunas = new Vector (2);
      Colunas.addElement("Espaço ocupado do disco:");
      Colunas.addElement(no.getConexaoServUtil().getHDOcupado());
      Linhas.addElement(Colunas);

      Linhas.trimToSize();
      return Linhas;
   }
   
   public void montaLinhasTotalizacao()
   {
       Vector linhas = new Vector(), colunas = new Vector();

       // Qtd de Usuarios conectados ao portal
       colunas = new Vector (2);
       colunas.addElement("Total de Usuários conectados no portal:");   
       colunas.addElement(""+qtdUsuariosLogadosPortal);
       linhas.addElement(colunas);
       
       colunas = new Vector (2);
       colunas.addElement("Total de Usuários utilizando CDRView Cliente:");   
       colunas.addElement(""+qtdUsuariosLogadosCDRView);
       linhas.addElement(colunas);
       
       String Header[] = {"Total", "Valor"};
       String Largura[] = {"272", "271"};
       String Alinhamento[] = {"left", "center"};
       short Filtros[] = {1, 0};

       m_Html.setTabela((short)2, false);
       m_Html.m_Tabela.setHeader(Header);
       m_Html.m_Tabela.setLarguraColunas(Largura);
       m_Html.m_Tabela.setCellPadding((short)2);
       m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
       m_Html.m_Tabela.setFiltros(Filtros);
       m_Html.m_Tabela.setAlinhamento(Alinhamento);
       m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=status");
       m_Html.m_Tabela.setElementos(linhas);
       m_Html.m_Tabela.enviaTabela2String();
   }
}
