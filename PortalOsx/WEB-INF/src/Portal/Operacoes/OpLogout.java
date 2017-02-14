//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpLogout.java
package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Utils.Agenda;
import Portal.Utils.UsuarioDef;


/**
 * Operação de logout do usuário.
 */
public class OpLogout extends OperacaoAbs
{
    static
    {
    }

    /**
     * @return @exception
     * @roseuid 3C06C8E0004D
     */
    public OpLogout()
    {
    }

    /**
     * @param p_Mensagem
     * @return boolean
     * @exception Inicia
     *                a operação a ser realizada.
     * @roseuid 3C06C8D60265
     */
    public boolean iniciaOperacao(String p_Mensagem)
    {
        short RetLogout;
        String Data = NoUtil.getNo().getConexaoServUtil().getDataHoraAtual();
        UsuarioDef Usuario;

        try
        {
            Usuario = (UsuarioDef) NoUtil.getNo().getConexaoServUtil().getUsuarioSessao(m_Request.getSession().getId());

            if (Usuario != null)
            {
                System.out.println(Data + " - OpLogout - Usuario: " +
                    Usuario.getUsuario() + " - " + Usuario.getIDSessao()); // .getSessaoHTTP().getId());
            }
            else
            {
                System.out.println(Data +
                    " - OpLogout - Usuario ja desconectado");

                return true;
            }

            int IdUsuario = Usuario.getIdUsuario();
            short Sequencia = Usuario.getSequencia();

            // Remove o usuário da lista e faz o invalida a sessão
            try
            {
                /** Fecha todos relatorios que por ventura nao foram liberados pelo servutil */
                Agenda.fechaRelatoriosUsuario(Usuario);
                No no = NoUtil.buscaNobyNomeUsuario(Usuario.getUsuario());
                if (Usuario.getSessaoHTTP() == null)
                {
                    System.out.println("Sessao HTTP do usuario "+Usuario.getUsuario()+" esta NULL");
                }
                else
                {
                    Usuario.getSessaoHTTP().invalidate();
                }
                no.getUsuarioLogados().remove(Usuario.getIDSessao());
                RetLogout = no.getConexaoServUtil().logout(Usuario, m_Request.getRemoteAddr(),m_Request.getRemoteHost());
            }
            catch (Exception Exc)
            {
                System.out.println(Data + " - Erro - OpLogout()");
                System.out.println(Data + " - " + Exc.getMessage());
                Exc.printStackTrace();
            }

            try
            {
                m_Response.sendRedirect("/PortalOsx/"+ArquivosDefs.s_HTML_LOGIN);
            }
            catch (Exception e)
            {
                System.out.println("Nao achou pagina de login");
            }

            /*
             * iniciaArgs(2); m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
             * m_Args[1] = "despedida.htm"; m_Html.enviaArquivo(m_Args);
             */
        }
        catch (Exception ExcLogout)
        {
            System.out.println("OpLogout - iniciaOperacao(): " + ExcLogout);
            ExcLogout.printStackTrace();
        }

        return true;
    }
}
