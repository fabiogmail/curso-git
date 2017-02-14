package Portal.ConfAlarme;

import javax.servlet.http.HttpServletRequest;

import Portal.ComponentesHTML.GeraTabela;

public class TabelaPeriodos extends GeraTabela
{
  static private String m_TituloCols[] = {"Periodo", "Inicio", "Fim"};
  static private int m_TamCols[] = {40, 40, 473};
  private ArquivoPeriodosCnf m_ArquivoPeriodosCnf;
  public String m_Dica = "";

  public TabelaPeriodos(String p_NomeServlet) throws Exception
  {
    super(3, m_TituloCols, m_TamCols, p_NomeServlet);
    fnRegPorPagina(11);
    fnComSelecao(true);
    fnComSelecaoMultipla(false);
    fnComProcura(false);
    try
    {
      m_ArquivoPeriodosCnf = new ArquivoPeriodosCnf();
    } catch (Exception e)
    {
      throw e;
    }

    fnAtualiza(null);
  }

  public PeriodosCnf fnPeriodo(int p_Pos)
  {
    return (PeriodosCnf) m_ArquivoPeriodosCnf.m_Periodos.elementAt(p_Pos);
  }

  public void fnSalva() throws Exception
  {
    try
    {
      m_ArquivoPeriodosCnf.fnSalva();
    } catch (Exception e)
    {
      throw e;
    }
  }

  public int fnQtdPeriodos()
  {
    return m_ArquivoPeriodosCnf.m_Periodos.size();
  }

  public String fnTela(HttpServletRequest p_Req, String p_Form, String p_JSAdicional)
  {
    String l_Tmp = "";
    fnLeFormHTML(p_Req);
    l_Tmp += fnGeraHTML(p_Form, p_JSAdicional);
    l_Tmp += P(VAR("align","center"));
    if (fnTemSelecionados())
    {
      PeriodosCnf l_PeriodosCnf = fnPeriodo(fnPrimeiroSelecionados());
      l_Tmp += AHREFH("javascript:OperacaoAlr(2,10,"+fnPrimeiroSelecionados()+")", "Altera o Periodo selecionado", "[Altera Periodo]");
    }
    l_Tmp += _P();

    m_Dica = "Selecione o Periodo pela seta a esquerda (>) e tecle na operação para alterar o periodo."+BR()+
       ""+BR()+
       "[Altera Periodo] - Altera as paramtrizações do Periodo selecionado"+BR()+BR()+
       ""+BR();
    return l_Tmp;
  }

  public void fnAtualiza(HttpServletRequest p_Req)
  {
    fnLimpaLinhas();

    String l_Linhas[] = new String[3];
    for (int a=0; a<fnQtdPeriodos(); a++)
    {
      PeriodosCnf l_PeriodosCnf = fnPeriodo(a);

      l_Linhas[0] = l_PeriodosCnf.m_Periodo;
      l_Linhas[1] = l_PeriodosCnf.m_Inicio;
      l_Linhas[2] = l_PeriodosCnf.m_Fim;
      fnAdicionaLinha(l_Linhas);
    }
    fnLeNovoFormHTML(p_Req);
  }
}

