package Portal.ConfAlarme;

import javax.servlet.http.HttpServletRequest;

import Portal.ComponentesHTML.GeraTabela;

public class TabelaGruposCnf extends GeraTabela
{
  static private String m_TituloCols[] = {"Nome", "Valor"};
  static private int m_TamCols[] = {100, 453};
  public String m_Dica = "";

  private String m_Id = "";
  private String m_Classe = "";
  private String m_Descricao = "";
  private String m_K = "";
  private String m_PM = "";
  private String m_Cham = "";
  private String m_TMC = "";

  static private String m_TagId;
  static private String m_TagClasse;
  static private String m_TagDescricao;
  static private String m_TagK;
  static private String m_TagPM;
  static private String m_TagCham;
  static private String m_TagTMC;

  static
  {
    m_TagId = "AlCf_ID";
    m_TagClasse = "AlCf_CLS";
    m_TagDescricao = "AlCf_DSC";
    m_TagK = "AlCf_K";
    m_TagPM = "AlCf_PM";
    m_TagCham = "AlCf_CHAM";
    m_TagTMC = "AlCf_TMC";
  }

  public TabelaGruposCnf()
  {
  }

  public TabelaGruposCnf(String p_NomeServlet)
  {
    super(2, m_TituloCols, m_TamCols, p_NomeServlet);
    fnLimpaParametros();
    fnRegPorPagina(0);
    fnComOrdenacao(false);
    fnComSelecao(false);
    fnComProcura(false);
  }

  public String fnTela(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    String l_Tmp = "";
    String l_Linhas[] = new String[2];

    if (p_Grupo > -1)
    {
      GruposCnf l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
      if (p_Op == 1)
        m_Id = ""+p_TabelaGrupos.fnMaiorId();
      else
        m_Id = ""+l_GruposCnf.m_Id;
      m_Classe = ""+l_GruposCnf.m_Classe;
      m_Descricao = l_GruposCnf.m_Comentario;
      m_K = ""+l_GruposCnf.m_K;
      m_PM = ""+l_GruposCnf.m_PM;
      m_Cham = ""+l_GruposCnf.m_Cham;
      m_TMC = ""+l_GruposCnf.m_TMC;
    } else
    {
      m_Id = "";
      m_Classe = "";
      m_Descricao = "";
      m_K = "";
      m_PM = "";
      m_Cham = "";
      m_TMC = "";
    }

    l_Tmp += INPUT("hidden", m_TagId, ASPAS+m_Id+ASPAS);
    l_Tmp += INPUT("hidden", m_TagClasse, ASPAS+m_Classe+ASPAS);

    
    boolean l_PodeAlterarRegra = ! m_Descricao.trim().equalsIgnoreCase("Grupo default");
    
    l_Linhas[0] = "ID:";
    l_Linhas[1] = m_Id;
    fnAdicionaLinha(l_Linhas);
    l_Linhas[0] = "Classe:";
    l_Linhas[1] = TabelaGrupos.fnClasse(new Integer(m_Classe).intValue());
    fnAdicionaLinha(l_Linhas);
    l_Linhas[0] = "Nome do Estrato:";
//    if (l_PodeAlterarRegra)
    	l_Linhas[1] = INPUT("text", 50, m_TagDescricao, ASPAS+m_Descricao+ASPAS);
//    else
//    	l_Linhas[1] = m_Descricao;
    fnAdicionaLinha(l_Linhas);
    l_Linhas[0] = "K:";
    l_Linhas[1] = INPUT("text", 10, m_TagK, ASPAS+m_K+ASPAS);
    fnAdicionaLinha(l_Linhas);
    l_Linhas[0] = "Preço Médio:";
    l_Linhas[1] = INPUT("text", 10, m_TagPM, ASPAS+m_PM+ASPAS);
    fnAdicionaLinha(l_Linhas);

    /*
    l_Linhas[0] = "Cham:";
    l_Linhas[1] = INPUT("text", 10, m_TagCham, ASPAS+m_Cham+ASPAS);
    fnAdicionaLinha(l_Linhas);
    */

    l_Linhas[0] = "TMC:";
    l_Linhas[1] = INPUT("text", 10, m_TagTMC, ASPAS+m_TMC+ASPAS);
    fnAdicionaLinha(l_Linhas);
    if (p_SubOp == 0)
    {
      fnLeNovoFormHTML(p_Req);
    } else
      fnLeFormHTML(p_Req);

    l_Tmp += fnGeraHTML(p_Form, p_JSAdicional);
    l_Tmp += P(VAR("align","center"));
//      l_Tmp += TD();
    if (p_Op != 3)
    {
       m_Dica = "preencha os valores nos campos e tecle na operação desejada."+BR()+
          ""+BR()+
          "As operações são:"+BR()+BR();
    } else
    {
       m_Dica = "Tecle na operação desejada."+BR()+
          ""+BR()+
          "As operações são:"+BR()+BR();
    }

    switch (p_Op)
    {
      case 1:
        m_Dica += "[Confirma Inclusão] - Inclui o novo estrato com os valores dos campos e volta para a tela seleção de estratos"+BR()+BR();
        l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",2,"+p_Grupo+")", "Confirma a inclusão", "[Confirma Inclusão]");
        break;
      case 2:
        m_Dica += "[Confirma Alteração] - Confirma as alterações dos campos e volta para a tela seleção de estratos"+BR()+BR();
        m_Dica += "[Configuração de variáveis] - Descarta as alterações dos campos e vai para a tela de parametrização de algoritmos"+BR()+BR();
        if (l_PodeAlterarRegra)
        	m_Dica += "[Regras de associação de recursos] - Descarta as alterações dos campos e vai para a tela de regras de associação de recursos"+BR()+BR();
        l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",2,"+p_Grupo+")", "Confirma a alteração", "[Confirma Alteração]");
        l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",3,"+p_Grupo+")", "Vai para a tela de configuração de variaveis", "[Configuração de variáveis]");
        if (l_PodeAlterarRegra)
        	l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",5,"+p_Grupo+")", "Vai para a tela de associação de recursos", "[Regras de associação de recursos]");
        break;
      case 3:
        m_Dica += "[Confirma Exclusão] - Exclui definitivamente o estrato e volta para a tela seleção de estratos"+BR()+BR();
        l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",2,"+p_Grupo+")", "Confirma a exclusão", "[Confirma Exclusão]");
        break;
    }
    l_Tmp += " ";
    l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Retorna a tela anterior", "[Cancela]");
    l_Tmp += _P();
    m_Dica += "[Cancela] - Descarta a operação e volta para a tela seleção de estratos";
    return l_Tmp;
  }

  public String fnTelaConfirma(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    String l_Tmp = "";
    String l_Erro = "";

    try
    {
      m_Id = p_Req.getParameter(m_TagId);
      m_Classe = p_Req.getParameter(m_TagClasse);
      m_Descricao = p_Req.getParameter(m_TagDescricao);
      m_K = p_Req.getParameter(m_TagK);
      m_PM = p_Req.getParameter(m_TagPM);
//      m_Cham = p_Req.getParameter(m_TagCham);
      m_TMC = p_Req.getParameter(m_TagTMC);
    } catch (Exception e)
    {
      return "Erro ao ler variaveis";
    }

    GruposCnf l_GruposCnf;
    l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
    if (p_Op == 3) // Exclusão
    {
       p_TabelaGrupos.fnRemoveGrupo(l_GruposCnf);
    } else
    {
       if (p_Op == 1) // Inclusão
       {
         l_GruposCnf = l_GruposCnf.fnCopia();
         p_TabelaGrupos.fnAdicionaGrupo(l_GruposCnf);
       }

       l_GruposCnf.m_Id = new Long(m_Id).longValue();
       l_GruposCnf.m_Classe = new Short(m_Classe).shortValue();

       try
       {
         l_GruposCnf.m_Comentario = m_Descricao;
       } catch(Exception e)
       {
         l_Erro += "Erro no campo Descrição"+BR();
         l_Erro += e.getMessage()+BR();
       }
       try
       {
         l_GruposCnf.m_K = new Double(m_K).doubleValue();
       } catch(Exception e)
       {
         l_Erro += "Erro no campo K"+BR();
         l_Erro += e.getMessage()+BR();
       }
       try
       {
         l_GruposCnf.m_PM = new Double(m_PM).doubleValue();
       } catch(Exception e)
       {
         l_Erro += "Erro no campo PM"+BR();
         l_Erro += e.getMessage()+BR();
       }

       /*
       try
       {
         l_GruposCnf.m_Cham = new Long(m_Cham).longValue();
       } catch(Exception e)
       {
         l_Erro += "Erro no campo Cham"+BR();
         l_Erro += e.getMessage()+BR();
       }
       */

       try
       {
         l_GruposCnf.m_TMC = new Long(m_TMC).longValue();
       } catch(Exception e)
       {
         l_Erro += "Erro no campo TMC"+BR();
         l_Erro += e.getMessage()+BR();
       }

       l_Tmp += l_Erro;
    }

    l_Tmp += "<script type=\"text/javascript\" charset=\"ISO-8859-1\" language=\"Javascript\">"+NL;
    l_Tmp += "<!--"+NL;
    l_Tmp += p_JSAdicional;
    l_Tmp += "-->"+NL;
    l_Tmp += "</script>"+NL;

    l_Tmp += P(VAR("align","center"));
    if (l_Erro.compareTo("") == 0)
    {
       p_TabelaGrupos.fnAtualiza(p_Req);
       try
       {
         p_TabelaGrupos.fnSalva();
         l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Retorna a tela principal", "[Operação Concluída com sucesso !]");
       } catch(Exception e)
       {
         l_Tmp += e.getMessage();
         l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",0,"+p_Grupo+")", "Retorna a tela anterior", "[Volta]");
       }
    } else
    {
       l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",0,"+p_Grupo+")", "Retorna a tela anterior", "[Volta]");
    }
    l_Tmp += _P();
    m_Dica = "";
    return l_Tmp;
  }
}

