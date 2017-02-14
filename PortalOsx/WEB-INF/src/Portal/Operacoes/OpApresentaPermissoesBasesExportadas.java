//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpApresentaPermissoesRel.java

package Portal.Operacoes;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;

/**
 */
public class OpApresentaPermissoesBasesExportadas extends OperacaoAbs 
{

	private Vector Perfis;
	private Vector PermissoesAux;
    private Vector ListaTipoRel;
    static
    {}

    /**
     * @return @exception
     * @roseuid 3FB8CC300118
     */
    public OpApresentaPermissoesBasesExportadas()
    {
    }

    /**
     * @param p_Mensagem
     * @return boolean
     * @exception
     * @roseuid 3FB8CC30012C
     */
    public boolean iniciaOperacao(String p_Mensagem)
    {

        try
        {
            setOperacao("Apresenta Permiss&oatilde;es das Bases");
            boolean TipoListagem = false;

            iniciaArgs(7);
            m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();

            if ((m_Request.getParameter("tipo") != null) && (m_Request.getParameter("tipo").equalsIgnoreCase("listagem")))
            {
                TipoListagem = true;
            }

            inicializa();
            
            if (TipoListagem)
            {
                montaTabela();
                m_Args[1] = "listagen.htm";
                m_Args[2] = "";
                m_Args[3] = "";
                m_Args[4] = "listapermissaodeacesso.gif";
                m_Args[5] = m_Html.m_Tabela.getTabelaString();
                m_Args[6] = "";
                m_Html.enviaArquivo(m_Args);
            }
            else
            {
                m_Args[1] = "formgen.htm";
                m_Args[2] = "src=\"/PortalOsx/templates/js/permissoesbase.js\"";
                m_Args[3] = "onLoad=\"MontaListaPerfis()\"";
                m_Args[4] = "permissaodeacesso.gif";
                m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "permissoesbase.form", montaFormulario(p_Mensagem));
                m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "permissoesrel.txt", null);
                m_Html.enviaArquivo(m_Args);
            }

            return true;
        }
        catch (Exception Exc)
        {
            System.out.println("OpApresentaPermissoesBasesExportadas - iniciaOperacao(): " + Exc);
            Exc.printStackTrace();
            return false;
        }
    }

    /**
     * @param p_Mensagem
     * @return String[]
     * @exception
     * @roseuid 3FB8FB090240
     */
    public String[] montaFormulario(String p_Mensagem)
    {
        String Args[] = new String[4];
        PerfilCfgDef Perfil = null;
        int QtdElem = 0, i = 0;

        QtdElem = Perfis.size();
        Args[0] = new String();
        Args[1] = new String();
        for (i = 0; i < QtdElem; i++)
        {
            Perfil = (PerfilCfgDef) Perfis.elementAt(i);
            // Montando a lista de perfis e de id's de perfil
            if (Perfil.getPerfil().equals("admin"))
            {
                continue;
            }
            Args[0] += "" + Perfil.getPerfil();
            Args[0] += ";";
            Args[1] += "" + Perfil.getId();
            Args[1] += ";";
            
            System.out.println(Perfil.getPerfil()+"="+Perfil.getId());
        }

        // Montando a lista de permissoes
        QtdElem = PermissoesAux.size();
        Args[2] = new String();
        for (i = 0; i < QtdElem; i++)
        {
            Args[2] += (String) PermissoesAux.elementAt(i);
            Args[2] += "@";
            System.out.println(PermissoesAux.elementAt(i));
        }

        // Removendo o ultimo ";"
        if (Args[0].length() > 0) Args[0] = Args[0].substring(0, Args[0].length() - 1);

        if (Args[1].length() > 0) Args[1] = Args[1].substring(0, Args[1].length() - 1);

        if (Args[2].length() > 0) Args[2] = Args[2].substring(0, Args[2].length() - 1);

        // Tipos de Relatorio
//        Args[3] = new String();
//        for (i = 0; i < ListaTipoRel.size(); i++)
//        {
//            Args[3] += (String) ListaTipoRel.elementAt(i);
//            Args[3] += ";";
//        }
//
//        Args[3] = Args[3].substring(0, Args[3].length() - 1);

        // Mensagem
        Args[3] = p_Mensagem;

        return Args;
    }

    /**
     * @return Vector
     * @exception
     * @roseuid 3FC502800352
     */
    public Vector montaLinhas()
    {

        Vector Linhas = null;
        Vector Colunas = null;
        HashMap Permissoes = new HashMap();
        PerfilCfgDef Perfil = null;
        int QtdElem = 0, i = 0, j = 0;
        String Chave = "";
        String Valor = "";
        String PerfilId = "";
        String TipoRel = "";
        String TipoRelId = "";
        
        // Populando o HashMap de permissoes
        QtdElem = PermissoesAux.size();
        for (i = 0; i < QtdElem; i++)
        {
            StringTokenizer st = new StringTokenizer((String) PermissoesAux.elementAt(i), ":");
            Chave = (String) st.nextElement();
            Valor = new String();

            StringTokenizer st2 = new StringTokenizer((String) st.nextElement(), ";");
            while (st2.hasMoreTokens())
            {
                PerfilId = (String) st2.nextElement();
                Perfil = (PerfilCfgDef) getPerfilbyId(PerfilId);
                if (Perfil == null)
                {
//                    System.out.println("------------------------------------------------\n" + "Perfil " + PerfilId + " nao encontrado.         \n"
//                                       + "Arquivo PermissoesRel.txt pode estar corrompido.\n" + "------------------------------------------------\n");
                    continue;
                }
                Valor += Perfil.getPerfil();
                Valor += ", ";
            }
            // Retirando o ultimo ","
            if (Valor.length() > 0) Valor = Valor.substring(0, Valor.length() - 2);

            Permissoes.put(Chave, Valor);
        }
        
        // Montando a linha para cada Perfil
        QtdElem = Perfis.size();
        Linhas = new Vector();
        for (i = 0; i < QtdElem; i++)
        {
            Perfil = (PerfilCfgDef) Perfis.elementAt(i);

            if (Perfil.getPerfil().equalsIgnoreCase("admin")) continue;

            Colunas = new Vector(2);
            Colunas.add("<b>" + Perfil.getPerfil() + "</b>");
            Chave = "" + Perfil.getId() ;//+ "-" + TipoRelId;
            Valor = (Permissoes.get(Chave) != null) ? (String) Permissoes.get(Chave) : "-";
            Colunas.add(Valor);
            Linhas.add(Colunas);
        }
        return Linhas;
    }

    /**
     * @return void
     * @exception
     * @roseuid 3FC502800370
     */
    public void montaTabela()
    {
        String Header[] = { "Perfil", "Perfis Acess&iacute;veis" };
        String Largura[] = { "150", "250" };
        String AlinhamentoHeader[] = { "left", "center" };
        String Alinhamento[] = { "left", "left" };
        short Filtros[] = { 1, 0 };
        Vector Linhas = null;

        Linhas = montaLinhas();
        m_Html.setTabela((short) 2, false);
        m_Html.m_Tabela.setHeader(Header);
        m_Html.m_Tabela.setLarguraColunas(Largura);
        m_Html.m_Tabela.setCellPadding((short) 2);
        m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
        m_Html.m_Tabela.setApresentaIndice(false);
        m_Html.m_Tabela.setFiltros(Filtros);
        m_Html.m_Tabela.setAlinhamento(Alinhamento);
        m_Html.m_Tabela.setAlinhamentoHeader(AlinhamentoHeader);
        m_Html.m_Tabela.setAlturaColunas((short) 19);
        m_Html.m_Tabela.setElementos(Linhas);

        m_Html.trataTabela(m_Request, Linhas);
        m_Html.m_Tabela.enviaTabela2String();
    }

    /**
     * @param Id
     * @return PerfilCfgDef
     * @exception
     * @roseuid 3FC5EF7D00E4
     */
    public PerfilCfgDef getPerfilbyId(String Id)
    {
        PerfilCfgDef Perfil = null;

        for (int j = 0; j < Perfis.size(); j++)
        {
            Perfil = (PerfilCfgDef) Perfis.elementAt(j);
            if (Id.equals("" + Perfil.getId())) return Perfil;
        }

        return null;
    }
    
    private void inicializa()
    {
    	 No noTmp = null;
    	 Perfis = new Vector();
         PermissoesAux = new Vector();

    	 for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             try
             {
                noTmp = (No) iter.next();
                Perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisOtimizado());
                PermissoesAux.addAll(noTmp.getConexaoServUtil().getPermissoesBasesExportadas());

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
    	 Perfis.trimToSize();
         PermissoesAux.trimToSize();
    }
}