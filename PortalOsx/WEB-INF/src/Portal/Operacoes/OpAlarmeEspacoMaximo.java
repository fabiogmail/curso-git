//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpAlarmeEspacoMaximo.java

package Portal.Operacoes;

import java.util.Vector;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.VetorUtil;

/**
 */
public class OpAlarmeEspacoMaximo extends OperacaoAbs 
{
   private Vector m_Linhas = null;
   private String m_Diretorios = null;
   private String m_CfgAlr[] = null;
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C5EDFDF028C
    */
   public OpAlarmeEspacoMaximo() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C5EDFDF02A0
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpAlarmeEspacoMaximo - iniciaOperacao()");
      try
      {
         setOperacao("Alarme de Espaço Máximo");

         montaTabela();
         String Args[];

         // Argumentos do formulário
         Args = new String[6];
         Args[0] = p_Mensagem;
         Args[1] = m_CfgAlr[4];
         Args[2] = m_Diretorios;
         Args[3] = Integer.toString(m_Linhas.size());
         Args[4] = m_Html.m_Tabela.getTabelaString();
         Args[5] = m_CfgAlr[5];

         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/alarmeespacomaximo.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "alarmeespacomaximo.gif";
         
         if(DefsComum.s_CLIENTE.equalsIgnoreCase("claro"))
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeespacomaximo_claro.form", Args);
         else
         	m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "alarmeespacomaximo.form", Args);
         
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "alarmeespacomaximo.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpAlarmeEspacoMaximo - iniciaOperacao(): "+Exc);
		 Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3DAAF3B001E7
    */
   public Vector montaLinhas() 
   {
      String Args[] = null, Habilita = "";
      Vector Diretorios = null, Limites = null, Colunas = null, Periodicidade = null, Habilitacao = null;

      m_Linhas = new Vector();

      m_CfgAlr = NoUtil.getNo().getConexaoServUtil().getCfgAlarme((short)2);

      Diretorios = VetorUtil.String2Vetor(m_CfgAlr[0],';');
      m_Diretorios = m_CfgAlr[0];
      Limites = VetorUtil.String2Vetor(m_CfgAlr[1],';');
      Periodicidade = VetorUtil.String2Vetor(m_CfgAlr[2],';');
      Habilitacao = VetorUtil.String2Vetor(m_CfgAlr[3],';');

      for (int i = 0; i < Diretorios.size(); i++)
      {
         Colunas = new Vector();
         Colunas.add("<b>"+(String)Diretorios.elementAt(i)+"</b>");
         Colunas.add("<input type=\"text\" size=\"10\" name=\"espaco\" value=\""+(String)Limites.elementAt(i)+"\" class=\"inputtext\">");
         Colunas.add("<input type=\"text\" size=\"10\" name=\"periodicidade\" value=\"" + (String)Periodicidade.elementAt(i) + "\" class=\"inputtext\">");

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
    * @roseuid 3DAAF3A700C2
    */
   public void montaTabela() 
   {
      String Header[] = {"Diret&oacute;rio", "Espa&ccedil;o M&aacute;ximo (Mb)", "Periodicidade (s)", "Habilitado"};
      String Largura[] = {"190",  "149", "148", "66"};
      String Alinhamento[] = {"left", "center", "center", "center"};
      short Filtros[] = {0, 0, 0, 0};

      montaLinhas();
      m_Html.setTabela((short)Header.length, false);
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=alarmeEspacoMaximo");
      m_Html.m_Tabela.setElementos(m_Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
