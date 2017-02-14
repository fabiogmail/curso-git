//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaUsuariosCDRView.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpListaUsuariosCDRView extends OperacaoAbs 
{
   private Vector m_ListaUsuarios;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C850E1F0287
    */
   public OpListaUsuariosCDRView() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * Inicia a operação a ser realizada.
    * @roseuid 3C850E1F0382
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpListaUsuariosCDRView - iniciaOperacao()");
      String Args[];

      try
      {
         setOperacao("Lista Usu&aacute;rios CDRView");
         int QtdUsuarios = 0;
         String Usuarios = "";
         UsuarioDef Usuario;
         Vector listaUsuariosNo = null;
         
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
         	 try
			 {
	             noTmp = (No) iter.next();
	             listaUsuariosNo = noTmp.getConexaoServControle().getUsuariosCDRView();
	             
	             if (listaUsuariosNo != null)
	             {
	                 if (m_ListaUsuarios == null)
	                 {
	                     m_ListaUsuarios = new Vector();
	                 }
	                 
	                 m_ListaUsuarios.addAll(listaUsuariosNo);
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
         
         if (m_ListaUsuarios != null)
         {
            for (int i = 0; i < m_ListaUsuarios.size(); i++)
            {
               QtdUsuarios++;
               Usuario = (UsuarioDef)m_ListaUsuarios.elementAt(i);
               Usuarios += Usuario.getUsuario() + "-" + Usuario.getDataAcesso() + ";";
            }
         }
         if (Usuarios.length() > 0)
            Usuarios = Usuarios.substring(0, Usuarios.length()-1);

         montaTabela();
         Args = new String[5];
         Args[0] = "desconexaoCdrview";
         Args[1] = Integer.toString(QtdUsuarios);
         Args[2] = Usuarios;
         Args[3] = p_Mensagem;
         Args[4] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listausuariosconect.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/deselec.gif','/PortalOsx/imagens/selec.gif'); IniciaDesconexao()\""; // onload
         m_Args[4] = "listausuarioscdrview.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listausuariosconect.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listausuarioscdrview.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception ExcListaUsr)
      {
         System.out.println("OpListaUsuariosCDRView - iniciaOperacao(): "+ExcListaUsr);
         ExcListaUsr.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C850E200120
    */
   public Vector montaLinhas() 
   {
      String TipoConexao;
      UsuarioDef Usuario;
      Vector Colunas = null, Linhas = new Vector();

      //ListaUsuarios = m_ConexCtrl.getUsuariosCDRView();
      if (m_ListaUsuarios != null)
      {
         for (short i = 0; i < m_ListaUsuarios.size(); i++)
         {
            Usuario = (UsuarioDef)m_ListaUsuarios.elementAt(i);
            Colunas = new Vector();
            Colunas.add(Usuario.getUsuario());
            Colunas.add(Usuario.getPerfil());
            Colunas.add(Usuario.getDataAcessoStr());
            Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+i+"','','/PortalOsx/imagens/selec.gif',1,'"+i+"','"+Usuario.getUsuario()+ "-" + Usuario.getDataAcesso()+"')\" onmouseover=\"window.status='Marca/desmarca usuário para desconexão';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/deselec.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
            Colunas.trimToSize();
            Linhas.add(Colunas);
         }
      }
      else
      {
         Colunas = new Vector();
         Colunas.add("N&atilde;o h&aacute; usu&aacute;rios utilizando");
         Colunas.add("o CDRView");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");
         Linhas.add(Colunas);
      }

      Linhas.trimToSize();
      return Linhas;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3C850E2001FD
    */
   public void montaTabela() 
   {
      String TipoConexao;
      String Header[] = {"Usu&aacute;rio", "Perfil", "Logon", "Desconecta"};
      String Largura[] = {"176", "124", "203", "50"};
      String Alinhamento[] = {"left", "left", "center", "center"};
      short Filtros[] = {1, 1, 1, 0};
      Vector Linhas = null;

      Linhas = montaLinhas();
      m_Html.setTabela((short)5, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setAlturaColunas((short)19);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaUsuariosCDRView");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
