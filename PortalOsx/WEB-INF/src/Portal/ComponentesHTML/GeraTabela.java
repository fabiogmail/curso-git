package Portal.ComponentesHTML;

//<a href="javascript:void(0)" onClick="javascript:open_win('/inmail/inmail.pl?acao=ler&msgnum=1&UIDL=2120021600000400.3729&folder=INBOX&max=1&sort_by=&rnd=486418833','read_msg')">Quer um novo emprego?</a></td>
//      l_Tmp += "<td colspan=\"5\"><input type=\"button\" name=\"Pg"+a+"\" value=\"Página "+(a+1)+"\" onClick=\"return ProximaPagina("+a+")\">"+NL;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
Classe para gerenciar uma tabela HTML
Gerencia:
 Quantidade de páginas,
 Linhas selecionadas,
 Pesquisas

Funciona guardando as informações de navegação em variáveis ocultas na página HTML

OSx Telecom. 19/01/2002
*/
public class GeraTabela extends HTMLTags
{
  public String m_NomeServlet;                    // Nome do Servlet chamado em Post, Get, etc.
  private int m_QtdColunas;                        // Quantidade de colunas da tabela
  private Vector m_Linhas;                         // Quantidade de linhas da tabela
  private String m_Cabecalho[];                    // Lista de títulos do cabeçalho
  private int m_TamColunas[];                      // Lista de tamanhos das colunas
  private int m_Pagina = 0;                        // Página corrente
  private int m_CampoOrdem = -1;                   // Último campo ordenado
  private int m_UltimaOrdem = 0;                   // A ordem do último campo (ascendente ou decrescente)
  private int m_Inicio;                            // Registro inicial a ser apresentado
  private int m_Fim;                               // Registro final a ser apresentado
  private String m_Procura = null;                 // Texto a ser procurado
  private String m_Selecionados = "";              // Registros selecionados - em string
  private boolean m_LinhasSelecionadas[] = null;   // Registros selecionados - em booleano
  private HttpServletRequest m_Req = null;         // Formulário que chamou (para pegar variaveis)
  private int m_Indice[] = null;                   // Tabela para ordenar na tela sem alterar a entrada
  private int m_RegPorPagina = 10;                 // Quantidade de registros por página
  private boolean m_ComOrdenacao = true;           // Indica se pode ordenar colunas
  private boolean m_ComProcura = true;             // Indica se tem caixa de procura
  private boolean m_ComSelecao = true;             // Indica se pode ou não selecionar linha
  private boolean m_ComSelecaoMultipla = true;     // Indica se pode ou não selecionar mais de um linha
  private String m_CorTitulo = "000033";           // Cor do título da tabela
  private String m_CorFgTitulo = "FFFFFF";         // Cor do texto do título da tabela
  private String m_CorPAR = "F0F0F0";              // Cor da linha PAR
  private String m_CorIMPAR = "FFFFFF";            // Cor da linha ÍMPAR
  private String m_CorSelPAR = "F0F000";           // Cor da linha PAR selecionada
  private String m_CorSelIMPAR = "FFFF00";         // Cor da linha IMPAR selecionada

  static private String m_TagProxPg;
  static private String m_TagCpOrd;
  static private String m_TagUltOrd;
  static private String m_TagSelec;

  static private String m_TagSeleciona;
  static private String m_TagDeseleciona;

  static
  {
      m_TagProxPg = "Tbl_PP";
      m_TagCpOrd = "Tbl_CO";
      m_TagUltOrd = "Tbl_UO";
      m_TagSelec = "Tbl_SL";

      m_TagSeleciona = "Tbl_SELECT";
      m_TagDeseleciona = "Tbl_DESELECT";
  }

  // Quantidade de registros por página
  public void fnRegPorPagina(int p_RegPorPagina)
  {
     m_RegPorPagina = p_RegPorPagina;
  }

  // Indica se pode ou não selecionar linha
  public void fnComSelecao(boolean p_ComSelecao)
  {
     m_ComSelecao = p_ComSelecao;
  }

  // Indica se tem caixa de procura
  public void fnComProcura(boolean p_ComProcura)
  {
     m_ComProcura = p_ComProcura;
  }

  // Indica se pode ordenar colunas
  public void fnComOrdenacao(boolean p_ComOrdenacao)
  {
     m_ComOrdenacao = p_ComOrdenacao;
  }

  // Indica se pode ou não selecionar mais de um linha
  public void fnComSelecaoMultipla(boolean p_ComSelecaoMultipla)
  {
     m_ComSelecaoMultipla = p_ComSelecaoMultipla;
  }

  // Cor do título da tabela
  public void fnCorTitulo(String p_CorTitulo)
  {
     m_CorTitulo = p_CorTitulo;
  }

  // Cor da linha PAR
  public void fnCorPAR(String p_CorPAR)
  {
     m_CorPAR = p_CorPAR;
  }

  // Cor da linha ÍMPAR
  public void fnCorIMPAR(String p_CorIMPAR)
  {
     m_CorIMPAR = p_CorIMPAR;
  }

  // Cor da linha PAR selecionada
  public void fnCorSelPAR(String p_CorSelPAR)
  {
     m_CorSelPAR = p_CorSelPAR;
  }

  // Cor da linha IMPAR selecionada
  public void fnCorSelIMPAR(String p_CorSelIMPAR)
  {
     m_CorSelIMPAR = p_CorSelIMPAR;
  }

  // Registros selecionados - em booleano
  public boolean [] fnLinhasSelecionadas()
  {
    return m_LinhasSelecionadas;
  }

  public GeraTabela()
  {
  }
  
  // Construtor
  // Params:
  //  int p_QtdColunas         - Quantidade de colunas da tabela
  //  String p_Cabecalho[]     - Lista de títulos do cabeçalho
  //  int p_TamColunas[]       - Lista de tamanhos das colunas
  //  String p_NomeServlet     - Nome do Servlet chamado em Post, Get, etc.
  public GeraTabela(int p_QtdColunas, String p_Cabecalho[], int p_TamColunas[], String p_NomeServlet)
  {
    m_Linhas = new Vector();

    m_QtdColunas = p_QtdColunas;
    m_Cabecalho = new String[m_QtdColunas];
    m_TamColunas = new int[m_QtdColunas];
    m_NomeServlet = p_NomeServlet;

    // Inicializa variaveis de tamanho e título
    for (int a=0; a<m_QtdColunas; a++)
    {
      if ((p_TamColunas != null) && (a<p_TamColunas.length))
        m_TamColunas[a] = p_TamColunas[a];
      else
        m_TamColunas[a] = 0;

      if ((p_Cabecalho != null) && (a<p_Cabecalho.length))
      {
        if (p_Cabecalho[a].compareTo("") == 0)
           m_Cabecalho[a] = SP;
        else
           m_Cabecalho[a] = p_Cabecalho[a];
      } else
        m_Cabecalho[a] = SP;
    }
  }

  // Adiciona uma linha a tabela
  public void fnAdicionaLinha(String p_Linha[])
  {
    String l_Linha[] = new String[m_QtdColunas];

    for (int a=0; a<m_QtdColunas; a++)
    {
      if (a<p_Linha.length)
      {
        if ((p_Linha[a] == null) || (p_Linha[a].compareTo("") == 0))
           l_Linha[a] = SP;
        else
           l_Linha[a] = p_Linha[a];
      } else
      {
        l_Linha[a] = SP;
      }
    }

    m_Linhas.add(l_Linha);
  }

  public void fnLimpaLinhas()
  {
    m_Linhas.removeAllElements();
  }

  public void fnLeFormHTML(HttpServletRequest p_Req)
  {
    m_Req = p_Req;

    fnLeParametros();            // Le parametros HTML
    fnIndice();                  // Atualiza pela coluna ordenada
    fnAtualizaSelecionados();    // Atualiza lista de selecionados
  }

  public void fnLeNovoFormHTML(HttpServletRequest p_Req)
  {
    m_Req = p_Req;
    m_Pagina = 0;
    m_CampoOrdem = -1;
    m_UltimaOrdem = 0;
    m_Procura = null;
    m_Selecionados = "";

    fnIndice();                  // Atualiza pela coluna ordenada
    fnAtualizaSelecionados();    // Atualiza lista de selecionados
  }

  // Cria o conteúdo HTML da tabela
  //  HttpServletRequest p_Req - Formulário que chamou (para pegar variaveis)
  public String fnGeraHTML(String p_Form, String p_JSAdicional)
  {
    String l_Tmp = "";
    String l_Form = p_Form;
    if (p_Form == null)
      l_Form = "FormGeraTabela";

    l_Tmp += "<script type=\"text/javascript\" charset=\"ISO-8859-1\" language=\"Javascript\">"+NL;
    l_Tmp += "<!--"+NL;
    if (p_JSAdicional != null)
      l_Tmp += p_JSAdicional;
    l_Tmp += "function EnviaForm(p_Pagina,p_CampoOrdem,p_UltimaOrdem,p_Selecionados)"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagProxPg+".value = p_Pagina;"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagCpOrd+".value = p_CampoOrdem;"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagUltOrd+".value = p_UltimaOrdem;"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagSelec+".value = p_Selecionados;"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += NL;
    l_Tmp += "function Ordena(p_CampoOrdem,p_UltimaOrdem)"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagProxPg+".value = 0;"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagCpOrd+".value = p_CampoOrdem;"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagUltOrd+".value = p_UltimaOrdem;"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += NL;
    l_Tmp += "function Seleciona(p_Indice)"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagSeleciona+".value = p_Indice;"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += NL;
    l_Tmp += "function Deseleciona(p_Indice)"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagDeseleciona+".value = p_Indice;"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += NL;
    l_Tmp += "function SelecionaTudo()"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagSelec+".value = "+ASPAS+fnSelecionaTudo()+ASPAS+";"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += NL;
    l_Tmp += "function DeselecionaTudo()"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagSelec+".value = 0;"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += NL;
    l_Tmp += "function Pagina(p_Pagina)"+NL;
    l_Tmp += "{"+NL;
    l_Tmp += "  document."+l_Form+"."+m_TagProxPg+".value = p_Pagina;"+NL;
    l_Tmp += "  document."+l_Form+".submit();"+NL;
    l_Tmp += "}"+NL;
    l_Tmp += "-->"+NL;
    l_Tmp += "</script>"+NL;

    // Início do FORM
    if ((p_Form == null) && (m_NomeServlet != null))
      l_Tmp += FORM(l_Form, "post", m_NomeServlet);

    // Quantidade total de páginas
    int l_QtdPags;
    if (m_RegPorPagina > 0)
      l_QtdPags = ((m_Linhas.size()-1) / m_RegPorPagina)+1;
    else
      l_QtdPags = 1;

    if (m_ComProcura)
    {
      String l_Procura = "";
      if ((m_Procura != null) && (m_Procura.compareTo("") != 0))
        l_Procura = m_Procura;

//      l_Tmp += TD();

      // Cria o botão de Find
      if ((m_RegPorPagina > 0) && ((m_Linhas.size() / m_RegPorPagina) > 0))
        l_Tmp += "Página "+(m_Pagina+1)+" de "+l_QtdPags+" - "+NL;

      l_Tmp += INPUT("text", 30, "Find", l_Procura);
      l_Tmp += INPUT("submit", "BtFind", "Procura");
    }

    // Cria os campos escondidos
    // Campo que guarda a última página
    l_Tmp += INPUT("hidden", m_TagProxPg, ""+m_Pagina);
    // Campo que guarda o último campo ordenado
    l_Tmp += INPUT("hidden", m_TagCpOrd, ""+m_CampoOrdem);
    // Campo que guarda se a ordem foi ascendente ou decrescente
    l_Tmp += INPUT("hidden", m_TagUltOrd, ""+m_UltimaOrdem);
    // Campo que guarda os ítens selecionados
    if (m_Selecionados!=null)
       l_Tmp += INPUT("hidden", m_TagSelec, ASPAS+m_Selecionados+ASPAS);
    else
       l_Tmp += INPUT("hidden", m_TagSelec, "0");

    l_Tmp += INPUT("hidden", m_TagSeleciona, "-1");
    l_Tmp += INPUT("hidden", m_TagDeseleciona, "-1");
//    l_Tmp += _TD();

    // Cria a tabela
    int l_Tam = 0;
    for (int a=0; a<m_QtdColunas; a++)
      l_Tam += m_TamColunas[a];

    l_Tmp += TABLE(VAR("width",l_Tam)+" "+VAR("border",0)+" "+VAR("cellspacing",0)+" "+VAR("cellpadding",2));
    // Campo para limpar a ordenação
    l_Tmp += TR(VAR("bgcolor", COR(m_CorTitulo))+" "+VAR("bgcolor", COR(m_CorTitulo)));
    // Se tiver seleção, crie um espaço no título para a seleção
    if (m_ComSelecao)
    {
      l_Tmp += TD(VAR("width",5)+" "+VAR("height","19"));
      if (fnTemSelecionados())
        l_Tmp += AHREFH("javascript:DeselecionaTudo()", "Deseleciona todos", COR(m_CorFgTitulo), ">");
      else if (! m_ComSelecaoMultipla)
        l_Tmp += SP;
      else
        l_Tmp += AHREFH("javascript:SelecionaTudo()", "Seleciona todos", COR(m_CorFgTitulo), ">");
      l_Tmp += _TD();
    }
    // Crie os títulos das colunas
    for (int a=0; a<m_QtdColunas; a++)
    {
      l_Tmp += TD(VAR("width",m_TamColunas[a])+" "+VAR("height","19"));
      if (m_ComOrdenacao)
      {
         // Se a coluna esta ordenada e de forma ascendente ...
         if ((a==m_CampoOrdem) && (m_UltimaOrdem == 0))
         {
           // Crie o título como sendo um link para ordenar decrescente
           l_Tmp += AHREFH("javascript:Ordena("+a+",1)", "Ordena por "+m_Cabecalho[a], COR(m_CorFgTitulo), m_Cabecalho[a]);
         // Se a coluna esta ordenada e de forma decrescente ...
         } else if ((a==m_CampoOrdem) && (m_UltimaOrdem != 0))
         {
           // Limpe a ordem
           l_Tmp += AHREFH("javascript:Ordena(-1,1)", "Restaura ordem default", COR(m_CorFgTitulo), m_Cabecalho[a]);
         // Se a coluna não esta ordenada  ...
         } else
         {
           // Crie o título como sendo um link para ordenar ascendente
           l_Tmp += AHREFH("javascript:Ordena("+a+",0)", "Ordena por "+m_Cabecalho[a], COR(m_CorFgTitulo), m_Cabecalho[a]);
         }
      } else
         l_Tmp += FONT(VAR("color",COR(m_CorFgTitulo)))+B()+m_Cabecalho[a]+_B()+_FONT()+NL;
      l_Tmp += _TD();
    }
    l_Tmp += _TR();

//    l_Tmp += TR();
    // Faça um loop nas linhas
    for (int a=0; a<m_Linhas.size(); a++)
    {
      // Veja se a linha á apresentável (se está na página corrente)
      if ((a >= m_Inicio) && (a < m_Fim))
      {
        // Pegue as colunas da linha
        String l_Linha[] = (String []) m_Linhas.elementAt(m_Indice[a]);

        // Coloque a cor correta da linha (se a linha é par ou ímpar e se está ou não selecionada)
        if (!m_LinhasSelecionadas[m_Indice[a]])
        {
           if (a%2 == 0)
             l_Tmp += TR(VAR("bgcolor", COR(m_CorPAR)));
           else
             l_Tmp += TR(VAR("bgcolor", COR(m_CorIMPAR)));
        } else
        {
           if (a%2 == 0)
             l_Tmp += TR(VAR("bgcolor", COR(m_CorSelPAR)));
           else
             l_Tmp += TR(VAR("bgcolor", COR(m_CorSelIMPAR)));
        }

        // Se for selecionável, crie um link para selecionar/deselecionar
        if (m_ComSelecao)
        {
           l_Tmp += TD(VAR("width",5)+" "+VAR("height","19"));
           if (m_LinhasSelecionadas[m_Indice[a]])
             l_Tmp += AHREFH("javascript:Deseleciona("+m_Indice[a]+")", "Deseleciona item", ">");
           else
             l_Tmp += AHREFH("javascript:Seleciona("+m_Indice[a]+")", "Seleciona item", ">");
           l_Tmp += _TD();
        }

        // Crie as colunas restantes
        for (int b=0; b<m_QtdColunas; b++)
        {
          l_Tmp += TD(VAR("width","\""+m_TamColunas[b]+"\"")+" "+VAR("align","left"));
          l_Tmp += l_Linha[b]+NL;
          l_Tmp += _TD();
        }
        l_Tmp += _TR();
      }
    }
//    l_Tmp += _TR();

    // Versão Marcelo
    l_Tmp += TR();
    l_Tmp += TD()+"&nbsp;";
    l_Tmp += _TD();
    l_Tmp += _TR();
    l_Tmp += TR();
    l_Tmp += TD(VAR("align","center")+" "+VAR("colspan","4"));
    l_Tmp += TABLE(VAR("width",l_Tam)+" "+VAR("border",0)+" "+VAR("cellspacing",0)+" "+VAR("cellpadding",0));
    l_Tmp += TR();
    l_Tmp += TD(VAR("width","102"));

    if (l_QtdPags > 1)
      l_Tmp += P(VAR("align","center"));

    // Se não está na primeira página, crie o botão de pagina anterior, se não, crie um botão desativado
//    l_Tmp += TD(VAR("colspan",5)+" "+VAR("align","center"));

    if (m_Pagina > 0)
    {
      // Versão original
      // l_Tmp += AHREF("javascript:Pagina("+(m_Pagina-1)+")", "<<");

      // Versão Marcelo
      l_Tmp += B()+AHREFH("javascript:Pagina("+(m_Pagina-1)+")", "Página anterior", "<< p&aacute;gina anterior")+_B();

    } else
    {
      if (l_QtdPags > 1)
      {
        // Versão original
        //  l_Tmp += "<<";

        // Versão Marcelo
        l_Tmp += "&nbsp;";
      }
    }

    // Versão Marcelo
    l_Tmp += _TD();
    l_Tmp += TD(VAR("width",""+((int) l_Tam-102-106)));
    l_Tmp += DIV(VAR("align", "center"));
    // Se tem mais de uma página, crie links para acessar páginas intermediárias
    if (l_QtdPags > 1)
    {
      for (int a=0; a<l_QtdPags; a++)
      {
        String l_Cor;
        if (a == m_Pagina)
          l_Cor = VAR("bgcolor", COR(m_CorSelIMPAR));
        else
          l_Cor = VAR("bgcolor", COR(m_CorIMPAR));
//        l_Tmp += TD(VAR("bgcolor", COR(m_CorSelIMPAR))+" "+VAR("colspan",5)+" "+VAR("align","center"));
        // Versão original
        // l_Tmp += AHREF("javascript:Pagina("+a+")", ""+(a+1));
        // Versão Marcelo
        l_Tmp += B()+AHREFH("javascript:Pagina("+a+")", "Página "+(a+1), ""+(a+1))+_B();
      }
    }

    // Versão Marcelo
    l_Tmp += _DIV();
    l_Tmp += _TD();
    l_Tmp += TD(VAR("width","106"))+"&nbsp;";

    // Se tem mais paginas para frente, crie o botão de próxima pagina, se não, crie um botão desativado
//    l_Tmp += TD(VAR("colspan",5)+" "+VAR("align","center"));

    if (m_Linhas.size() > m_Fim)
    {
      // Versão original
      // l_Tmp += AHREF("javascript:Pagina("+(m_Pagina+1)+")", ">>");

      // Versão Marcelo
      l_Tmp += B()+AHREFH("javascript:Pagina("+(m_Pagina+1)+")", "Próxima página", "pr&oacute;xima p&aacute;gina >>")+_B();
    } else
    {
      if (l_QtdPags > 1)
      {
        // Versão original
        // l_Tmp += ">>";

        // Versão Marcelo
        l_Tmp += "&nbsp;";
      }
    }

    // Versão Marcelo
    l_Tmp += _TD();
    l_Tmp += _TR();

    l_Tmp += _TABLE();
    l_Tmp += _TD();
    l_Tmp += _TR();
    
    l_Tmp += _TABLE();

    if (l_QtdPags > 1)
      l_Tmp += _P();

    if (p_Form == null)
      l_Tmp += _FORM();

    return l_Tmp;
  }

  public void fnLimpaParametros()
  {
    m_Pagina = 0;
    m_CampoOrdem = 0;
    m_UltimaOrdem = 0;
    m_Selecionados = "";
  }

  // Le parametros da página
  public void fnLeParametros()
  {
    // Página
    try
    {
      String l_ProximaPagina = m_Req.getParameter(m_TagProxPg);
      m_Pagina = new Integer(l_ProximaPagina).intValue();
    } catch (Exception e)
    {
    }

    // CampoOrdem
    try
    {
      String l_CampoOrdem = m_Req.getParameter(m_TagCpOrd);
      m_CampoOrdem = new Integer(l_CampoOrdem).intValue();
    } catch (Exception e)
    {
    }

    // UltimaOrdem
    try
    {
      String l_UltimaOrdem = m_Req.getParameter(m_TagUltOrd);
      m_UltimaOrdem = new Integer(l_UltimaOrdem).intValue();
    } catch (Exception e)
    {
    }

    // Selecionados
    try
    {
      m_Selecionados = m_Req.getParameter(m_TagSelec);
    } catch (Exception e)
    {
    }
  }

  // Atualiza os índices
  private void fnIndice()
  {
    m_Indice = new int [m_Linhas.size()];

    for (int a=0; a<m_Linhas.size(); a++)
      m_Indice[a] = a;

    if (m_CampoOrdem > -1)
      fnSort((int[])m_Indice.clone(), m_Indice, 0, m_Indice.length);
  }

  // Verifica se tem algum ítem selecionado
  public boolean fnTemSelecionados()
  {
    for (int a=0; a<m_Linhas.size();a++)
      if (m_LinhasSelecionadas[a]) return true;
    return false;
  }

  // Verifica qual o primeiro ítem selecionado, -1 se nenhum
  public int fnPrimeiroSelecionados()
  {
    for (int a=0; a<m_Linhas.size();a++)
      if (m_LinhasSelecionadas[a]) return a;
    return -1;
  }

  // Gera a string para selecionar todos
  private String fnSelecionaTudo()
  {
    String l_Tmp = "";

    for (int a=0; a<m_Linhas.size();a++)
      l_Tmp += "1";

    return l_Tmp;
  }

  // Atualiza a lista de booleanos pela string de selecionados e trata parametros de seleciona e deseleciona
  private void fnAtualizaSelecionados()
  {
    m_LinhasSelecionadas = new boolean [m_Linhas.size()];
    for (int a=0; a<m_Linhas.size();a++)
    {
      if ((m_Selecionados == null) || (a >= m_Selecionados.length()) || (m_Selecionados.charAt(a) == '0'))
         m_LinhasSelecionadas[a] = false;
      else
         m_LinhasSelecionadas[a] = true;
    }

    if (m_Req != null)
    {
      fnTrataProcura();            // Verifica se é para procurar algo

      // Seleciona
      try
      {
        String l_Seleciona = m_Req.getParameter(m_TagSeleciona);
        int m_Linha = new Integer(l_Seleciona).intValue();
        if (m_Linha > -1)
        {
          if (! m_ComSelecaoMultipla)
            for (int a=0; a<m_Linhas.size();a++)
              m_LinhasSelecionadas[a] = false;
          m_LinhasSelecionadas[m_Linha] = true;
        }
      } catch (Exception e)
      {
      }

      // Deseleciona
      try
      {
        String l_Deseleciona = m_Req.getParameter(m_TagDeseleciona);
        int m_Linha = new Integer(l_Deseleciona).intValue();
        if (m_Linha > -1)
          m_LinhasSelecionadas[m_Linha] = false;
      } catch (Exception e)
      {
      }
    }

    fnAtualizaStrSelecionados();
  }

  public void fnSeleciona(int l_Pos)
  {
    fnAtualizaStrSelecionados();
    if ((m_LinhasSelecionadas == null) || (l_Pos >= m_Selecionados.length()))
      return;

    m_LinhasSelecionadas[l_Pos] = true;
    fnAtualizaStrSelecionados();
  }

  public void fnDeseleciona(int l_Pos)
  {
    fnAtualizaStrSelecionados();
    if ((m_LinhasSelecionadas == null) || (l_Pos >= m_Selecionados.length()))
      return;

    m_LinhasSelecionadas[l_Pos] = false;
    fnAtualizaStrSelecionados();
  }

  private void fnAtualizaStrSelecionados()
  {
    m_Selecionados = "";
    if (m_LinhasSelecionadas != null)
    {
       // Atualiza a string de selecionados
       for (int a=0; a<m_LinhasSelecionadas.length; a++)
       {
         if (m_LinhasSelecionadas[a])
            m_Selecionados += "1";
         else
            m_Selecionados += "0";
       }
    } else
    {
      m_LinhasSelecionadas = new boolean [m_Linhas.size()];
      for (int a=0; a<m_Linhas.size();a++)
      {
        m_Selecionados += "0";
        m_LinhasSelecionadas[a] = false;
      }
    }
  }

  // Verifica se é para procurar algo
  private void fnTrataProcura()
  {
    // Procura
    try
    {
       m_Procura = m_Req.getParameter("Find");
       if ((m_Procura != null) && (m_Procura.compareTo("") != 0))
       {
          int l_Found = fnProcura(0, m_Procura);
          if (l_Found > -1)
          {
            if (m_ComSelecao)
            {
               if (! m_ComSelecaoMultipla)
                 for (int a=0; a<m_Linhas.size();a++)
                   m_LinhasSelecionadas[a] = false;
               m_LinhasSelecionadas[l_Found] = true;
            }
            if (m_RegPorPagina > 0)
               m_Pagina = (l_Found / m_RegPorPagina);
            else
               m_Pagina = 1;
          }
       }
    } catch (Exception e)
    {
    }

    // Atualiza o indicador de registro inicial e final visível
    if (m_RegPorPagina > 0)
    {
       m_Inicio = m_Pagina * m_RegPorPagina;
       m_Fim = m_Inicio+m_RegPorPagina;
    } else
    {
      m_Inicio = 0;
      m_Fim = m_Linhas.size(); 
    }

//    System.out.println("De "+m_Inicio+ " até "+m_Fim+ " "+m_RegPorPagina+"/Pag");
  }

  // Procura uma string, a partir de um registro, em qualquer coluna
  private int fnProcura(int p_Inicio, String p_Texto)
  {
    for (int a=p_Inicio; a<m_Linhas.size(); a++)
    {
      String l_Linha[] = (String []) m_Linhas.elementAt(m_Indice[a]);
      for (int b=0; b<m_QtdColunas; b++)
      {
         if(p_Texto.length() <= l_Linha[b].length())
         {
           if (l_Linha[b].substring(0, p_Texto.length()).compareToIgnoreCase(p_Texto) == 0)
             return a;
         }
      }
    }
    return -1;
  }

  // Compara duas linhas na coluna ordenada
  private int fnCompara(int p_Linha1, int p_Linha2)
  {
    String l_Linha1[] = (String []) m_Linhas.elementAt(p_Linha1);
    String l_Linha2[] = (String []) m_Linhas.elementAt(p_Linha2);
    String l_Str1 = l_Linha1[m_CampoOrdem];
    String l_Str2 = l_Linha2[m_CampoOrdem];

    // Se e ascendente ou decrescente
    if (m_UltimaOrdem == 0)
      return l_Str1.compareTo(l_Str2);
    else
      return l_Str2.compareTo(l_Str1);
  }

  // Ordena com o método de bolhas
  private void fnSort(int p_Origem[], int p_Destino[], int p_Menor, int p_Maior)
  {
    try
    {
      if (p_Maior - p_Menor < 2)
      {
        return;
      }
      int p_Meio = (p_Menor + p_Maior)/2;
      fnSort(p_Destino, p_Origem, p_Menor, p_Meio);
      fnSort(p_Destino, p_Origem, p_Meio, p_Maior);

      int p = p_Menor;
      int q = p_Meio;

      if (p_Maior - p_Menor >= 4 && fnCompara(p_Origem[p_Meio-1], p_Origem[p_Meio]) <= 0)
      {
        for (int i = p_Menor; i < p_Maior; i++)
        {
          p_Destino[i] = p_Origem[i];
        }
        return;
      }

      for (int i = p_Menor; i < p_Maior; i++)
      {
        if (q >= p_Maior || (p < p_Meio && fnCompara(p_Origem[p], p_Origem[q]) <= 0))
        {
          p_Destino[i] = p_Origem[p++];
        }
        else
        {
          p_Destino[i] = p_Origem[q++];
        }
      }
    } catch (Exception e)
    {
    } catch (java.lang.OutOfMemoryError e)
    {
      System.out.println("Estourou memória");
      return;
    }
  }
}

