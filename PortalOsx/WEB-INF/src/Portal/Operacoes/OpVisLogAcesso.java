//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpVisLogAcesso.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpVisLogAcesso extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpVisLogAcesso - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C3D099301B0
    */
   public OpVisLogAcesso() 
   {
      //System.out.println("OpVisLogAcesso - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C3D09990295
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpVisLogAcesso - iniciaOperacao()");
      try
      {
         boolean bNaoSai = true;
         int Cont = 0;
         String COD_SRV_UTILITARIO =  "6";

         String Linha = null, DiasDaSemana[] = {"Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"};

         String Operacao = m_Request.getParameter("operacao");
         if (Operacao.toLowerCase().equals("vislogacesso") == true)
            setOperacao("Visualização de Log de Acesso");
         else if (Operacao.toLowerCase().equals("vislogparser") == true)
            setOperacao("Visualização de Log de Parser");
         
         

         // Recupera os parâmetros
         short Dia = Short.parseShort(m_Request.getParameter("dia"));
         String HoraInicial = m_Request.getParameter("HoraIni");
         String HoraFinal   = m_Request.getParameter("HoraFim");
         String servidor = m_Request.getParameter("servidor");
         
         No no = null;
         if(NoUtil.isAmbienteEmCluster() && servidor != null)
         {
         	no = NoUtil.getNoByHostName(servidor);
         }
         else
         {
         	no = NoUtil.buscaNobyNomePerfil(DefsComum.s_PRF_ADMIN);
         }
         

         no.getConexaoServUtil().setInfoLog(Short.parseShort(COD_SRV_UTILITARIO), "ativ", Dia, HoraInicial, HoraFinal);
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = ArquivosDefs.s_HTML_LOG1;
         m_Args[2] = "logacesso.gif";
         m_Args[3] = "Log de Acesso";
         m_Args[4] = DiasDaSemana[Dia];
         m_Args[5] = HoraInicial;
         m_Args[6] = HoraFinal;
         m_Html.enviaArquivo(m_Args);

         do
         {
            Linha = no.getConexaoServUtil().getLinhaLog();
   			if (Linha.length() == 0)
               bNaoSai = false;
            else
            {
               if (Linha.endsWith("Processo ServUtil\n") == false)
               {
                  if (Cont%2 == 0)
                     m_Html.getSaidaHtml().print("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
                  else
                     m_Html.getSaidaHtml().print("<tr bgcolor=\"#FFFFFF\">\n<td height=\"19\">"+Linha+"</td>\n</tr>\n");
                  Cont++;
               }
            }
         } while (bNaoSai);

         if (Cont == 0)
            m_Html.getSaidaHtml().print("<tr bgcolor=\"#F0F0F0\">\n<td height=\"19\">Não há log para o período solicitado</td>\n</tr>\n");

         iniciaArgs(3);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = ArquivosDefs.s_HTML_LOG2;
         m_Args[2] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "logacesso2.txt", null);         
         m_Html.enviaArquivo(m_Args);

         // Destroi o objeto Log no Servidor
         no.getConexaoServUtil().destroiLog();
      }
      catch (Exception Exc)
      {
         System.out.println("OpVisLogAcesso - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
