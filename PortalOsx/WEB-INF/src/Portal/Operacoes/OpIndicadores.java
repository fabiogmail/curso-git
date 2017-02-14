//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpIndicadores.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;

/**
 */
public class OpIndicadores extends OperacaoAbs 
{
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C794DF100F3
    */
   public OpIndicadores() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C7E401B01D1
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpIndicadores - iniciaOperacao()");   
      try
      {
         //setOperacao("Indicadores de Relatório");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "indicadores.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/indicadores2.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "selecaoindicadores.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "indicadores.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "perfis.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpIndicadores - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C7E427601CE
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[] = new String[7];

      String Operacao = m_Request.getParameter("operacaoportal");
System.out.println(Operacao);
      String Indicadores = m_Request.getParameter("indicadores");
      String Perfil  = m_Request.getParameter("perfil");
      String TipoRel = m_Request.getParameter("tiporel");
      String IdRel   = m_Request.getParameter("idrel");
      String Arquivo = m_Request.getParameter("arquivo");

      // Aqui temos a lista de todos os possiveis indicadores para este cliente
      if (TipoRel.equals("0") == true)
         Args[0] = m_IndicadoresDesempenho;
      else if (TipoRel.equals("2") == true)
         Args[0] = m_IndicadoresDetalhe;
      else if (TipoRel.equals("1") == true)
         Args[0] = m_IndicadoresPesquisa;

      if (Args[0].indexOf("Frequência;Aglutinado;") != -1)
         Args[0] = Args[0].substring("Frequência;Aglutinado;".length(), Args[0].length());

      Args[1] = Indicadores;
      Args[2] = Operacao;
      Args[3] = Perfil;
      Args[4] = TipoRel;
      Args[5] = IdRel;
      Args[6] = Arquivo;
      return Args;
   }
}

