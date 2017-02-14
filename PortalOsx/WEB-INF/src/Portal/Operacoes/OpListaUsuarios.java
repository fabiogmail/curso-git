//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaUsuarios.java

package Portal.Operacoes;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpListaUsuarios extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpListaUsuarios - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C0A35D30267
    */
   public OpListaUsuarios() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * Inicia a operação a ser realizada.
    * @roseuid 3C0A35D10065
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpListaUsuarios - iniciaOperacao()");
      String Args[], TipoConexao;

      try
      {
         setOperacao("Lista Usu&aacute;rios Portal");
         int QtdUsuarios = 0;
         UsuarioDef Usuario;
         String Usuarios = "";

         Map usuariosLogados = NoUtil.atualizaMapUsuariosLogados();
         
         synchronized(usuariosLogados)
		 {
	         Iterator ItUsuarios = usuariosLogados.values().iterator();
	         while (ItUsuarios.hasNext())
	         {
	            Usuario = (UsuarioDef)ItUsuarios.next();
	            if (Usuario.getUsuario().equals(DefsComum.s_USR_ADMIN) == false)
	            {
	               Usuarios += Usuario.getIDSessao() + ";";
	               QtdUsuarios++;
	            }
	         }
		 }
         //System.out.println("Usuarios: "+Usuarios);
         if (Usuarios.length() > 0)
            Usuarios = Usuarios.substring(0, Usuarios.length()-1);

         montaTabela();
         Args = new String[5];
         Args[0] = "desconexaoPortal";
         Args[1] = Integer.toString(QtdUsuarios);
         Args[2] = Usuarios;
         Args[3] = p_Mensagem;
         Args[4] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listausuariosconect.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/deselec.gif','/PortalOsx/imagens/selec.gif'); IniciaDesconexao()\""; // onload
         m_Args[4] = "listausuariosportal.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listausuariosconect.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listausuariosportal.txt", null);
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception ExcListaUsr)
      {
         System.out.println("OpListaUsuarios - iniciaOperacao(): "+ExcListaUsr);
         ExcListaUsr.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C599547024D
    */
   public Vector montaLinhas() 
   {
      String TipoConexao, IdSessao;
      UsuarioDef Usuario;
      Vector Colunas = null, Linhas = new Vector();
      int i=0;
      
      Map usuariosLogados = NoUtil.atualizaMapUsuariosLogados();

      if (usuariosLogados.size() > 0)
      {  
         synchronized(usuariosLogados)
 		 {
	         Iterator ItUsuarios = usuariosLogados.values().iterator();
	         while (ItUsuarios.hasNext()) 
	         {
	            Usuario = (UsuarioDef)ItUsuarios.next();
	            IdSessao = Usuario.getIDSessao();            
	            Colunas = new Vector();
	            //Colunas.add(Usuario.getUsuario() + " ("+Usuario.getIP()+")");
	            Colunas.add("<a href=\"javascript:AbreJanela('detalheUsuario', '"+IdSessao+"')\" onmouseover=\"window.status='Clique para ver detalhes do usuário';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">"+Usuario.getUsuario()+"</a>");
//	            Colunas.add(Usuario.getSenha());
	            
	            if(NoUtil.isAmbienteEmCluster())
	            	Colunas.add(Usuario.getHost());
	            
				Colunas.add(Usuario.getPerfil());
	            Colunas.add(Usuario.getDataAcessoStr());
	            if (Usuario.getOperacao() != null)
	               Colunas.add(Usuario.getOperacao());
	            else
	               Colunas.add("N/A");
	
	            if (Usuario.getUsuario().equals(DefsComum.s_USR_ADMIN) == false)
	            {
	               Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+i+"','','/PortalOsx/imagens/selec.gif',1,'"+i+"','"+IdSessao+"')\" onmouseover=\"window.status='Marca/desmarca usuário para desconexão';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/deselec.gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
	               i++;
	            }
	            else
	               Colunas.add("N/A");
	
	            Colunas.trimToSize();
	            Linhas.add(Colunas);
	            
	         }
		}
      }
      else
      {
         // !! Nunca deve passar aqui! Pelo menos Adm conectado !!
         Colunas = new Vector();
         Colunas.add("N&atilde;o h&aacute; usu&aacute;rios conectados (Erro)");
         Colunas.add("&nbsp;");
         Colunas.add("&nbsp;");
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
    * @roseuid 3C59954800D2
    */
   public void montaTabela() 
   {
      String Header[];
      String Largura[];
      String Alinhamento[];
      short Filtros[];
      
      if(NoUtil.isAmbienteEmCluster())
      {
         Header = new String[] {"Usu&aacute;rio", "Servidor", "Perfil", "Logon", "P&aacute;gina", "Desconecta"};
         Largura = new String[] {"145", "90", "73", "150", "135", "50"};
         Alinhamento = new String[] {"left", "center", "center", "center", "left", "center"};
         Filtros = new short[] {1, 0, 1, 1, 1, 0};
      }
      else
      {
         Header = new String[] {"Usu&aacute;rio", "Perfil", "Logon", "P&aacute;gina", "Desconecta"};
         Largura = new String[] {"145", "73", "150", "135", "50"};
         Alinhamento = new String[] {"left", "center", "center", "left", "center"};
         Filtros = new short[] {1, 1, 1, 1, 0};
      }
      
      
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
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaUsuariosPortal");
      m_Html.m_Tabela.setElementos(Linhas);

      m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
