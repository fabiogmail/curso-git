package Portal.ConfAlarme;

import javax.servlet.http.HttpServletRequest;

import Portal.Cluster.NoUtil;
import Portal.ComponentesHTML.GeraTabela;
import Portal.ComponentesHTML.HTMLTags;

public class TabelaGruposCnfRegras extends GeraTabela
{ 
  static private String m_TituloCols[] = {"Máscara de Recursos"};
  static private int m_TamCols[] = {553};
  public String m_Dica = "";
  private String m_Regra = "";
  private int m_RegraSelecionada = -1;
  static private String m_TagRegraSelecionada;

  static
  {
    m_TagRegraSelecionada = "AlCf_SelRegra";
  }

  public TabelaGruposCnfRegras()
  {
  }

  public TabelaGruposCnfRegras(String p_NomeServlet)
  {
    super(1, m_TituloCols, m_TamCols, p_NomeServlet);
    fnLimpaParametros();
    fnRegPorPagina(0);
    fnComSelecao(true);
    fnComOrdenacao(false);
    fnComSelecaoMultipla(false);
    fnComProcura(false);
  }

  public String fnTela(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    String l_Tmp = "";

    GruposCnf l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
    fnLeRegras(p_Req, p_TabelaGrupos, p_Grupo);

    l_Tmp += P(VAR("align","center"));
    l_Tmp += TabelaGrupos.fnClasse(l_GruposCnf.m_Classe)+" - "+l_GruposCnf.m_Comentario;
    l_Tmp += _P();

    String l_Linhas[] = new String[1];
    for (int a=0; a<l_GruposCnf.m_Regras.size(); a++)
    {
      String l_Regra = (String) l_GruposCnf.m_Regras.elementAt(a);
      l_Linhas[0] = INPUT("text", 30, "REGRA_"+a, ASPAS+l_Regra+ASPAS);
      fnAdicionaLinha(l_Linhas);
    }

    fnLeNovoFormHTML(p_Req);
    
    l_Tmp += "<table><tr bgcolor=#000033 bgcolor=#000033>"+HTMLTags.NL+"<td width=553 height=19><font color=#FFFFFF><b>";
    l_Tmp += "&nbsp;&nbsp;&nbsp;&nbsp;Bilhetadores</b></font></td>"+HTMLTags.NL+"</tr>";
    l_Tmp += "<tr><td><a href=# onclick=window.open('http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/jsp/bilhetadores.jsp','GRUPOS','resizable=no,status=yes,menubar=no,scrollbars=no,width=400,height=350'); class=\"link\">";
    l_Tmp += "></a>"+HTMLTags.INPUT("text", 30, "campoBilhetadores",p_Req.getParameter("campoBilhetadores")==null?l_GruposCnf.bilhetadores:p_Req.getParameter("campoBilhetadores"))+"</td></tr></table>";
    l_Tmp += HTMLTags.NL;
    
    l_Tmp += fnGeraHTML(p_Form, p_JSAdicional);
    l_Tmp += P(VAR("align","center"));
    l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",8,"+p_Grupo+")", "Confirma a alteração", "[Confirma Alteração]");
    l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",6,"+p_Grupo+")", "Inclui uma nova regra", "[Inclui Regra]");

    if (fnTemSelecionados())
    {
      l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",7,"+p_Grupo+")", "Exclui a regra selecionada", "[Exclui Regra]");
      m_RegraSelecionada = fnPrimeiroSelecionados();
      l_Tmp += HTMLTags.INPUT("hidden", m_TagRegraSelecionada, ""+m_RegraSelecionada);
    }
    l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Cancela a operação", "[Cancela]");
    l_Tmp += _P();
    m_Dica = "preencha os valores nos campos e tecle na operação desejada."+BR()+
       ""+BR()+
       "As operações são:"+BR()+BR()+
       "[Confirma Alteração] - Confirma as alterações dos campos e volta para a tela seleção de estratos"+BR()+BR()+
       "[Inclui Regra] - Inclui uma nova regra"+BR()+BR()+
       "[Exclui Regra] - Inclui uma nova regra"+BR()+BR()+
       "[Cancela] - Descarta as alterações dos campos e volta para a tela seleção de estratos";
    return l_Tmp;
  }

  public String fnTelaAdiciona(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    fnLeRegras(p_Req, p_TabelaGrupos, p_Grupo);
    GruposCnf l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
    l_GruposCnf.fnAdicionaRegra("");
    return fnTela(p_Req, p_Form, p_JSAdicional, p_TabelaGrupos, 5, p_SubOp, p_Grupo);
  }

  public String fnTelaRemove(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    try
    {
      String l_RegraSelecionada = p_Req.getParameter(m_TagRegraSelecionada);
      m_RegraSelecionada = new Integer(l_RegraSelecionada).intValue();
    } catch (Exception e)
    {
      m_RegraSelecionada = -1;
    }

    fnLeRegras(p_Req, p_TabelaGrupos, p_Grupo);
    if (m_RegraSelecionada > -1)
    {
      GruposCnf l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
      l_GruposCnf.fnRemoveRegra(m_RegraSelecionada);
    }
    String l_Tmp = "";
    l_Tmp += "<script type=\"text/javascript\" charset=\"ISO-8859-1\" language=\"Javascript\">"+NL;
    l_Tmp += "<!--"+NL;
    l_Tmp += p_JSAdicional;
    l_Tmp += "-->"+NL;
    l_Tmp += "</script>"+NL;
    l_Tmp += P(VAR("align","center"));
    l_Tmp += _P();
    m_Dica = "";
    l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",5,"+p_Grupo+")", "Retorna a tela principal", "[Registro removido com sucesso]");
    return l_Tmp;
//    return fnTela(p_Req, p_Form, p_JSAdicional, p_TabelaGrupos, 5, p_SubOp, p_Grupo);
  }

  public String fnTelaConfirma(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    fnLeRegras(p_Req, p_TabelaGrupos, p_Grupo);
    String l_Tmp = "";
    String l_Erro = "";
    GruposCnf l_GruposCnf;
    l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
    l_Tmp += "<script type=\"text/javascript\" charset=\"ISO-8859-1\" language=\"Javascript\">"+NL;
    l_Tmp += "<!--"+NL;
    l_Tmp += p_JSAdicional;
    l_Tmp += "-->"+NL;
    l_Tmp += "</script>"+NL;

    l_Tmp += P(VAR("align","center"));
    if (l_Erro.compareTo("") == 0)
    {
       try
       {
         p_TabelaGrupos.fnSalva();
         l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Retorna a tela principal", "[Operação Concluída com sucesso !]");
       } catch(Exception e)
       {
         l_Tmp += e.getMessage();
         l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",5,"+p_Grupo+")", "Retorna a tela anterior", "[Volta]");
       }
    } else
    {
       l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",5,"+p_Grupo+")", "Retorna a tela anterior", "[Volta]");
    }
    l_Tmp += _P();
    m_Dica = "";
    return l_Tmp;
  }

  public void fnLeRegras(HttpServletRequest p_Req, TabelaGrupos p_TabelaGrupos, int p_Grupo)
  {
    GruposCnf l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);

    for (int a=0; a<l_GruposCnf.m_Regras.size(); a++)
    {
      String l_Regra = fnLeRegra(p_Req, l_GruposCnf, a);
    }
  }

  public String fnLeRegra(HttpServletRequest p_Req, GruposCnf p_GruposCnf, int p_Pos)
  {
    String l_Regra = null;
    try
    {
      l_Regra = p_Req.getParameter("REGRA_"+p_Pos);
    } catch(Exception e)
    {
    }

    if ((l_Regra == null) || (l_Regra.compareTo("") == 0))
      l_Regra = (String) p_GruposCnf.m_Regras.elementAt(p_Pos);
    else
      p_GruposCnf.m_Regras.setElementAt(l_Regra, p_Pos);

    return l_Regra;
  }
}

