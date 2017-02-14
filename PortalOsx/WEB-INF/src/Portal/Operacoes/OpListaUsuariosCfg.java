//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaUsuariosCfg.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import CDRView2.Cliente;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpListaUsuariosCfg extends OperacaoAbs 
{ 
   
   static 
   {
      //System.out.println("OpListaUsuariosCfg - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C386DBA0069
    */
   public OpListaUsuariosCfg() 
   {
      //System.out.println("OpListaUsuariosCfg - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C386DBA007D
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpListaUsuariosCfg - iniciaOperacao()");
      try
      {
         setOperacao("Lista Usu&aacute;rios Configurados");
         String Args[] = new String[4];         
         montaTabela();	
         Args[0] = m_Request.getParameter("tipo");
         Args[1] = p_Mensagem;
         Args[2] = "";
         Args[3] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];         
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listausuariocfg.js\"";//javascript
         m_Args[3] = "";//"onLoad=\"Abrir('Portal.cPortal?operacao=downloadListaUsuarios',400,200)\"";// onload
         m_Args[4] = "listausuarios.gif";  
         //m_Args[5] = m_Html.m_Tabela.getTabelaString();
         if(p_Mensagem.equalsIgnoreCase("Lista atualizada"))
         {
        	 Args[2] = "<a href ="+"\""+"/PortalOsx/download/"+"listausuarios.txt"+"\""+"target='blank'>Download</a>";
        	 m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listausuariosdownload.form", Args);         
         }
         else
         {
        	 if(Cliente.fnCliente() == Cliente.CTBC){
        		 m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listausuarioscfgCTBC.form", Args);
        	 }else{
        		 m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listausuarioscfg.form", Args); 
        	 }
         }
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listausuarioscfg.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaUsuariosCfg - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C5822C20354
    */
   public Vector montaLinhas() 
   {
      UsuarioDef Usuario;   
      Vector ListaUsuarios = new Vector(), Linhas = new Vector(), Colunas;

      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          ListaUsuarios.addAll(noTmp.getConexaoServUtil().getListaUsuariosCfg());
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
      
      for (short i = 0; i < ListaUsuarios.size(); i++)
      {
         Usuario = (UsuarioDef)ListaUsuarios.elementAt(i);
         Colunas = new Vector();
         Colunas.add(Usuario.getUsuario());
         
         if(NoUtil.isAmbienteEmCluster())
         	Colunas.add(Usuario.getHost());
         
         Colunas.add(new Integer(Usuario.getIdUsuario()).toString());
         Colunas.add(Usuario.getPerfil());
         Colunas.add(new Integer(Usuario.getIdPerfil()).toString());
         Colunas.add(Usuario.getAcesso());
         Colunas.add(new Integer(Usuario.getAcessoId()).toString());
         Colunas.trimToSize();
         Linhas.add(Colunas);
      }
      Linhas.trimToSize();

      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C5822D1026F
    */
   public void montaTabela() 
   {
   	
   	  String Header[] ;
   	  String Alinhamento[] ;
   	  String Largura[];
   	  short Filtros[];
   	  
   	  if (NoUtil.isAmbienteEmCluster())
   	  {
   		 Header = new String[] {"Usu&aacute;rio", "Servidor", "Id Usu&aacute;rio", "Perfil", "Id Perfil", "Acesso", "Id Acesso"};
   		 Alinhamento = new String[] {"left", "center", "center", "center", "center", "center", "center"};
   		 Largura = new String[] {"90", "90", "75", "90", "74", "90", "74"};
   		 Filtros = new short[] {1, 0, 0, 1, 0, 1, 0};
   	  }
   	  else
   	  {
  		 Header = new String[] {"Usu&aacute;rio", "Id Usu&aacute;rio", "Perfil", "Id Perfil", "Acesso", "Id Acesso"};
   		 Alinhamento = new String[] {"left", "center", "center", "center", "center", "center"};
   		 Largura = new String[] {"90", "75", "90", "74", "90", "74"};
   		 Filtros = new short[] {1, 0, 1, 0, 1, 0};   	  	
   	  }
   	
      
      String Tipo;
      Vector Linhas = null;
      
      Linhas = montaLinhas();
      Tipo = m_Request.getParameter("tipo");

      if (Tipo.equals("parcial") == true)
         m_Html.setTabela((short)Header.length, true);
      else
         m_Html.setTabela((short)Header.length, false);

      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaUsuarios&tipo="+Tipo);
      m_Html.m_Tabela.setElementos(Linhas);
      
      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
   
   
}
