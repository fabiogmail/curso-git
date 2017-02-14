//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpListaRelApagar.java

package Portal.Operacoes;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.Agenda;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpListaRelApagar extends OperacaoAbs 
{
   private String m_SubOperacao;
   private String m_Perfil;
   private String m_TipoArmazenamento;
   private String m_TipoRelatorio;
   private String m_NomeRelatorio;
   
   static 
   {
   }
   
   public OpListaRelApagar() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3F0C22B403B2
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         m_SubOperacao = m_Request.getParameter("suboperacao");
//System.out.println("SubOperacao: "+m_SubOperacao);      
         m_Perfil    = m_Request.getParameter("perfil");
         m_TipoArmazenamento = m_Request.getParameter("tipoarmazenamento");
         m_TipoRelatorio   = m_Request.getParameter("tiporel");
         m_NomeRelatorio = m_Request.getParameter("nomerel");

         if (m_SubOperacao.equals("listar"))
            listaRelatorios();
         else
            apagaRelatorios();

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaRelApagar - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F0C22D500F3
    */
   public void listaRelatorios() 
   {
      String Lista = "";
      Vector LinhasAux, Linhas;

      No noTmp = NoUtil.buscaNobyNomePerfil(m_Perfil);
      LinhasAux = noTmp.getConexaoServUtil().getListaRelArmazenados2(Short.parseShort(m_TipoArmazenamento), m_Perfil, Short.parseShort(m_TipoRelatorio), (short)0, m_NomeRelatorio, "",NoUtil.getUsuarioLogado(m_Request.getSession().getId()).getPerfil());
//System.out.println("LinhasAux: "+LinhasAux);            
      if (LinhasAux != null)
      {
         String DataGeracao, DataGeracaoAux;
         Vector vAux;
         for (int i = 0; i < LinhasAux.size(); i++)
         {
            vAux = (Vector)LinhasAux.elementAt(i);
            DataGeracao = (String)vAux.elementAt(0);
            DataGeracao = DataGeracao.replace('#', '$');

            DataGeracaoAux = DataGeracao.substring(DataGeracao.indexOf('@')+1,DataGeracao.indexOf('$'));
            if (DataGeracaoAux.indexOf('!') != -1)
               DataGeracaoAux = DataGeracaoAux.substring(0, DataGeracaoAux.indexOf('!'));

            if (i % 2 == 0)
               Lista += "<tr>\n   <td align=\"left\" bgcolor=\"#CCCCCC\">\n";
            else
               Lista += "<tr>\n   <td align=\"left\">\n";            
            //Lista += "      <input type=\"checkbox\" name=\"checkbox\" value=\""+ DataGeracao +"\">&nbsp;&nbsp;<font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">"+m_Perfil + " - " + m_NomeRelatorio + " - " + DataGeracao.substring(DataGeracao.indexOf('@')+1,DataGeracao.indexOf('$')) + "</font><br>\n";
            Lista += "      <input type=\"checkbox\" name=\"checkbox\" value=\""+ DataGeracao +"\">&nbsp;&nbsp;<font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">"+m_Perfil + " - " + m_NomeRelatorio + " - " + DataGeracaoAux + "</font><br>\n";
            Lista += "</tr>\n   </td>\n";
         }
      }
      else
      {
//System.out.println("Aqui");
         Lista += "<font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\"><strong>N&atilde;o h&aacute; relat&oacute;rios a apagar.</strong></font>\n";
      }

      m_Args = new String[10];
      m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
      m_Args[1] = "listarelapagar.htm";
      m_Args[2] = "src=\"/"+DefsComum.s_ContextoWEB+"/templates/js/listarelagendados.js\"";
      m_Args[3] = "";//"onLoad=\"";
      m_Args[4] = m_Perfil;
      m_Args[5] = m_TipoArmazenamento;
      m_Args[6] = m_TipoRelatorio;
      m_Args[7] = m_NomeRelatorio;
      m_Args[8] = "excluirrelatorios.gif";      
      m_Args[9] = Lista;
      m_Html.enviaArquivo(m_Args);
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3F0C22F401DE
    */
   public void apagaRelatorios() 
   {
      String Relatorios = m_Request.getParameter("relatoriosaapagar");
      //Relatorios = Relatorios;
      
      UsuarioDef usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      List agendasUsuario = usuario.getM_ListAgendasUsuario();
	    
      for (Iterator iter = agendasUsuario.iterator(); iter.hasNext();)
      {
	        Agenda agenda = (Agenda) iter.next();
	        if (agenda.m_iRetRelatorio.fnGetNomeRel().equalsIgnoreCase(m_NomeRelatorio))
	        {
	        	System.out.println(new java.util.Date()+" - Apagando Relatorio: "+m_NomeRelatorio);
	            agenda.m_iRetRelatorio.fnFecha();
	        }
	  }
      
      usuario.getNo().getConexaoServUtil().apagaRelArmazenados(m_Perfil, Short.parseShort(m_TipoArmazenamento), Short.parseShort(m_TipoRelatorio), m_NomeRelatorio, Relatorios);
//      listaRelatorios();
   }
}
