package Portal.ConfAlarme;

import javax.servlet.http.HttpServletRequest;

import Portal.ComponentesHTML.GeraTabela;

public class TabelaPeriodosCnf extends GeraTabela
{
  static private String m_TituloCols[] = {"Nome", "Valor"};
  static private int m_TamCols[] = {100, 453};
  public String m_Dica = "";

  private String m_Periodo = "";
  private String m_Inicio = "";
  private String m_Fim = "";

  static private String m_TagPeriodo;
  static private String m_TagInicio;
  static private String m_TagFim;

  static
  {
    m_TagPeriodo = "PeCf_PR";
    m_TagInicio = "PeCf_IN";
    m_TagFim = "PeCf_FN";
  }

  public TabelaPeriodosCnf()
  {
  }

  public TabelaPeriodosCnf(String p_NomeServlet)
  {
    super(2, m_TituloCols, m_TamCols, p_NomeServlet);
    fnLimpaParametros();
    fnRegPorPagina(0);
    fnComOrdenacao(false);
    fnComSelecao(false);
    fnComProcura(false);
  }

  public String fnTela(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaPeriodos p_TabelaPeriodos, int p_Op, int p_SubOp, int p_PosPeriodo)
  {
    String l_Tmp = "";
    String l_Linhas[] = new String[2];

    if (p_PosPeriodo > -1)
    {
      PeriodosCnf l_PeriodosCnf = p_TabelaPeriodos.fnPeriodo(p_PosPeriodo);
      m_Periodo = ""+l_PeriodosCnf.m_Periodo;
      m_Inicio = ""+l_PeriodosCnf.m_Inicio;
      m_Fim = l_PeriodosCnf.m_Fim;
    } else
    {
      m_Periodo = "";
      m_Inicio = "";
      m_Fim = "";
    }

    l_Tmp += INPUT("hidden", m_TagPeriodo, ASPAS+m_Periodo+ASPAS);

    l_Linhas[0] = "Periodo:";
    l_Linhas[1] = m_Periodo;
    fnAdicionaLinha(l_Linhas);

    l_Linhas[0] = "Inicio:";
    l_Linhas[1] = INPUT("text", 50, m_TagInicio, ASPAS+m_Inicio+ASPAS);
    fnAdicionaLinha(l_Linhas);

    l_Linhas[0] = "Fim:";
    l_Linhas[1] = INPUT("text", 50, m_TagFim, ASPAS+m_Fim+ASPAS);
    fnAdicionaLinha(l_Linhas);

    if (p_SubOp == 0)
    {
      fnLeNovoFormHTML(p_Req);
    } else
      fnLeFormHTML(p_Req);

    l_Tmp += fnGeraHTML(p_Form, p_JSAdicional);
    l_Tmp += P(VAR("align","center"));
    m_Dica = "preencha os valores nos campos e tecle na operação desejada."+BR()+
       ""+BR()+
       "As operações são:"+BR()+BR();
    m_Dica += "[Confirma Alteração] - Confirma as alterações dos campos e volta para a tela seleção de períodos"+BR()+BR();
    l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",11,"+p_PosPeriodo+")", "Confirma a alteração", "[Confirma Alteração]");
    l_Tmp += " ";
    l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Retorna a tela anterior", "[Cancela]");
    l_Tmp += _P();
    m_Dica += "[Cancela] - Descarta a operação e volta para a tela seleção de estratos";
    return l_Tmp;
  }

  public String fnTelaConfirma(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaPeriodos p_TabelaPeriodos, int p_Op, int p_SubOp, int p_PosPeriodo)
  {
    String l_Tmp = "";
    String l_Erro = "";

    try
    {
      m_Periodo = p_Req.getParameter(m_TagPeriodo);
      m_Inicio = p_Req.getParameter(m_TagInicio);
      m_Fim = p_Req.getParameter(m_TagFim);
    } catch (Exception e)
    {
      return "Erro ao ler variaveis";
    }

    PeriodosCnf l_PeriodosCnf;
    l_PeriodosCnf = p_TabelaPeriodos.fnPeriodo(p_PosPeriodo);
    l_PeriodosCnf.m_Periodo = m_Periodo;
    l_PeriodosCnf.m_Inicio = m_Inicio;
    l_PeriodosCnf.m_Fim = m_Fim;

    l_Tmp += l_Erro;
    l_Tmp += "<script type=\"text/javascript\" charset=\"ISO-8859-1\" language=\"Javascript\">"+NL;
    l_Tmp += "<!--"+NL;
    l_Tmp += p_JSAdicional;
    l_Tmp += "-->"+NL;
    l_Tmp += "</script>"+NL;

    l_Tmp += P(VAR("align","center"));
    if (l_Erro.compareTo("") == 0)
    {
       p_TabelaPeriodos.fnAtualiza(p_Req);
       try
       {
         p_TabelaPeriodos.fnSalva();
         l_Tmp += "Para as informações começarem a valer, é necessário que reinicialize o AGNCDR"+BR()+BR();
         l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Retorna a tela principal", "[Operação Concluída com sucesso !]");
       } catch(Exception e)
       {
         l_Tmp += e.getMessage();
         l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",0,"+p_PosPeriodo+")", "Retorna a tela anterior", "[Volta]");
       }
    } else
    {
       l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",0,"+p_PosPeriodo+")", "Retorna a tela anterior", "[Volta]");
    }
    l_Tmp += _P();
    m_Dica = "";
    return l_Tmp;
  }
}

