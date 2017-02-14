package Portal.ConfAlarme;

import javax.servlet.http.HttpServletRequest;

import Portal.ComponentesHTML.GeraTabela;

public class TabelaGruposCnfParm extends GeraTabela
{
  static private String m_TituloCols[] = {"Algoritmo", "", "", ""};
  static private int m_TamCols[] = {433, 20, 50, 50};
  public String m_Dica = "";

  public TabelaGruposCnfParm()
  {
  }

  public TabelaGruposCnfParm(String p_NomeServlet)
  {
    super(4, m_TituloCols, m_TamCols, p_NomeServlet);
    fnLimpaParametros();
    fnRegPorPagina(0);
    fnComSelecao(false);
    fnComOrdenacao(false);
    fnComSelecaoMultipla(false);
    fnComProcura(false);
  }

  public String fnTela(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    String l_Tmp = "";
    String l_Linhas[] = new String[4];

    GruposCnf l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);

    l_Tmp += P(VAR("align","center"));
    l_Tmp += TabelaGrupos.fnClasse(l_GruposCnf.m_Classe)+" - "+l_GruposCnf.m_Comentario;
    l_Tmp += _P();

    for (int a=0; a<l_GruposCnf.m_Parametros.size(); a++)
    {
       GruposCnfParm l_GruposCnfParm = (GruposCnfParm) l_GruposCnf.m_Parametros.elementAt(a);
       l_Linhas[0] = l_GruposCnfParm.m_Comentario;
       l_Linhas[1] = INPUT("checkbox", "H_"+a, "1", " "+(l_GruposCnfParm.m_Hab ? "checked":""));
       l_Linhas[2] = INPUT("text", 10, "P1_"+a, ASPAS+l_GruposCnfParm.m_P1+ASPAS);
       if (l_GruposCnfParm.m_qtdP == 2)
          l_Linhas[3] = INPUT("text", 10, "P2_"+a, ASPAS+l_GruposCnfParm.m_P2+ASPAS);
       else
          l_Linhas[3] = "";
       fnAdicionaLinha(l_Linhas);
    }
    fnLeNovoFormHTML(p_Req);
    l_Tmp += fnGeraHTML(p_Form, p_JSAdicional);
    l_Tmp += P(VAR("align","center"));
    l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",4,"+p_Grupo+")", "Confirma as alterações", "[Confirma Alteração]");
    l_Tmp += AHREFH("javascript:OperacaoAlr(0,0,0)", "Retorna a tela anterior", "[Cancela]");
    l_Tmp += _P();
    m_Dica = "preencha os valores nos campos e tecle na operação desejada."+BR()+
       ""+BR()+
       "As operações são:"+BR()+BR()+
       "[Confirma Alteração] - Confirma as alterações dos campos e volta para a tela seleção de estratos"+BR()+BR()+
       "[Cancela] - Descarta as alterações dos campos e volta para a tela seleção de estratos";

    return l_Tmp;
  }

  public String fnTelaConfirma(HttpServletRequest p_Req, String p_Form, String p_JSAdicional, TabelaGrupos p_TabelaGrupos, int p_Op, int p_SubOp, int p_Grupo)
  {
    String l_Tmp = "";
    String l_Erro = "";

    GruposCnf l_GruposCnf;
    l_GruposCnf = p_TabelaGrupos.fnGrupo(p_Grupo);
    for (int a=0; a<l_GruposCnf.m_Parametros.size(); a++)
    {
       GruposCnfParm l_GruposCnfParm = (GruposCnfParm) l_GruposCnf.m_Parametros.elementAt(a);
       String l_Hab = p_Req.getParameter("H_"+a);
       String l_P1 = p_Req.getParameter("P1_"+a);
       String l_P2 = p_Req.getParameter("P2_"+a);
       if ((l_Hab != null) && (l_Hab.compareTo("1") == 0))
          l_GruposCnfParm.m_Hab = true;
       else
          l_GruposCnfParm.m_Hab = false;

       try
       {
          l_GruposCnfParm.m_P1 = new Integer(l_P1).intValue();
       } catch(Exception e)
       {
         l_Erro += "Erro no campo P1 de "+l_GruposCnfParm.m_Comentario+" "+BR();
         l_Erro += e.getMessage()+BR();
       }
       if (l_GruposCnfParm.m_qtdP == 2)
       {
          try
          {
             l_GruposCnfParm.m_P2 = new Integer(l_P2).intValue();
          } catch(Exception e)
          {
            l_Erro += "Erro no campo P2 de "+l_GruposCnfParm.m_Comentario+" "+BR();
            l_Erro += e.getMessage()+BR();
          }
       } else
          l_GruposCnfParm.m_P2 = 0;

       if ((l_GruposCnfParm.m_Hab) && (l_GruposCnfParm.m_P1 == 0) && (l_GruposCnfParm.m_P2 == 0))
       {
          l_Erro += "Erro no campo P1 e P2 de "+l_GruposCnfParm.m_Comentario+" "+BR();
          l_Erro += "Valor 0 com campo habilitado"+BR();
       }
    }

    l_Tmp += l_Erro;
      
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
         l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",3,"+p_Grupo+")", "Retorna a tela anterior", "[Volta]");
       }
    } else
    {
       l_Tmp += AHREFH("javascript:OperacaoAlr("+p_Op+",3,"+p_Grupo+")", "Retorna a tela anterior", "[Volta]");
    }
    l_Tmp += _P();
    m_Dica = "";
    return l_Tmp;
  }
}

