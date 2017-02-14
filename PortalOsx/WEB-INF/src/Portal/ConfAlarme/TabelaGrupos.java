package Portal.ConfAlarme;

import javax.servlet.http.HttpServletRequest;

import Portal.Cluster.NoUtil;
import Portal.ComponentesHTML.GeraTabela;

public class TabelaGrupos extends GeraTabela
{
  static private String m_TituloCols[] = {"Classe", "Estrato"};
  static private int m_TamCols[] = {40, 513};
  private ArquivoGruposCnf m_ArquivoGruposCnf;
  public String m_Dica = "";
  private long m_MaiorId = 0;

  public TabelaGrupos(String p_NomeServlet) throws Exception
  {
    super(2, m_TituloCols, m_TamCols, p_NomeServlet);
    fnRegPorPagina(11);
    fnComSelecao(true);
    fnComSelecaoMultipla(false);
    fnComProcura(false);
    try
    {
      m_ArquivoGruposCnf = new ArquivoGruposCnf();
    } catch (Exception e)
    {
      throw e;
    }

    fnAtualiza(null);
  }

  public GruposCnf fnGrupo(int p_Pos)
  {
    return (GruposCnf) m_ArquivoGruposCnf.m_Grupos.elementAt(p_Pos);
  }

  public void fnRemoveGrupo(GruposCnf p_Grupo)
  {
     m_ArquivoGruposCnf.m_Grupos.remove(p_Grupo);
  }

  public void fnAdicionaGrupo(GruposCnf p_Grupo)
  {
     m_ArquivoGruposCnf.m_Grupos.add(p_Grupo);
  }

  public void fnSalva() throws Exception
  {
    try
    {
      m_ArquivoGruposCnf.fnSalva();
    } catch (Exception e)
    {
      throw e;
    }
  }

  public int fnQtdGrupos()
  {
    return m_ArquivoGruposCnf.m_Grupos.size();
  }

  public String fnTela(HttpServletRequest p_Req, String p_Form, String p_JSAdicional)
  {
    String l_Tmp = "";
    fnLeFormHTML(p_Req);
    l_Tmp += fnGeraHTML(p_Form, p_JSAdicional);
    l_Tmp += P(VAR("align","center"));
    if (fnTemSelecionados())
    {
      l_Tmp += AHREFH("javascript:OperacaoAlr(1,0,"+fnPrimeiroSelecionados()+")", "Inclui um novo estrato a pertir de um existente", "[Inclui Estrato]");
      GruposCnf l_GruposCnf = fnGrupo(fnPrimeiroSelecionados());
      l_Tmp += AHREFH("javascript:OperacaoAlr(2,0,"+fnPrimeiroSelecionados()+")", "Altera o estrato selecionado", "[Altera Estrato]");
      if (l_GruposCnf.m_Tipo != 'E')
        l_Tmp += AHREFH("javascript:OperacaoAlr(3,0,"+fnPrimeiroSelecionados()+")", "Exclui o estrato selecionado", "[Exclui Estrato]");
    }
    l_Tmp += AHREFH("javascript:OperacaoAlr(1,9,0)", "Chama a tela de alteração de períodos", "[Altera Periodos]");
    l_Tmp += _P();

    m_Dica = "Selecione o estrato pela seta a esquerda (>) e tecle na operação desejada."+BR()+
       ""+BR()+
       "As operações são:"+BR()+BR()+
       "[Inclui Estrato] - Cria uma cópia do estrato com as mesmas características"+BR()+BR()+
       "[Altera Estrato] - Altera as paramtrizações do estrato selecionado"+BR()+BR()+
       "[Exclui Estrato] - Exclui o estrato selecionado"+BR()+BR()+
       "[Altera Periodos] - Vai para a tela de alteração de periodos"+BR()+BR()+
       ""+BR()+
       "ps.: as opções dependem do estrato selecionado";
    return l_Tmp;
  }

  public long fnMaiorId()
  {
    return m_MaiorId + 1;
  }

  public void fnAtualiza(HttpServletRequest p_Req)
  {
    fnLimpaLinhas();

    String l_Linhas[] = new String[2];
    for (int a=0; a<fnQtdGrupos()/*-1*/; a++)
    {
      GruposCnf l_GruposCnf = fnGrupo(a);
      if (l_GruposCnf.m_Id > m_MaiorId)
        m_MaiorId = l_GruposCnf.m_Id;

      l_Linhas[0] = fnClasse(l_GruposCnf.m_Classe);
      if (l_GruposCnf.m_Tipo == 'E')
        l_Linhas[0] = '*'+ l_Linhas[0];
      l_Linhas[1] = l_GruposCnf.m_Comentario;
      fnAdicionaLinha(l_Linhas);
    }
    fnLeNovoFormHTML(p_Req);
  }

  private static String m_ListaClasses [] = null;

	static public void main(String p_Args[])
   {
      fnIniciaListaClasses();
   }

  private static void fnIniciaListaClasses()
  {
     if (m_ListaClasses == null)
     {
        String l_ListaClasses [] = new String [255];
        int l_QtdClasses = 0;

         String l_NomeArquivo = NoUtil.getNo().getDiretorioDefs().getS_DIR_EXEC()+"ConfAlr.txt";
         java.io.BufferedReader l_Arquivo;
         boolean l_Erro = false;
         try
         {
            l_Arquivo = new java.io.BufferedReader(new java.io.FileReader(l_NomeArquivo));
            while (true)
            {
               try
               {
                  String l_Linha = l_Arquivo.readLine();
                  if (l_Linha != null)
                  {
                     if (l_Linha.substring(0, 10).compareTo("NOMECLASSE") == 0)
                     {
                        int a=10;
                        for (;a < l_Linha.length(); a++)
                           if (l_Linha.charAt(a) != ' ' && l_Linha.charAt(a) != '\t')
                              break;
                        l_ListaClasses[l_QtdClasses++] = l_Linha.substring(a, l_Linha.length());
                     }
                  } else
                     break;
               } catch(Exception e)
               {
                  e.printStackTrace();
               }
            }

            m_ListaClasses = new String [l_QtdClasses];
            for (int a=0; a<l_QtdClasses; a++)
               m_ListaClasses[a] = l_ListaClasses[a];

            try
            {
               l_Arquivo.close();
            } catch(Exception e)
            {
               e.printStackTrace();
            }
         } catch(Exception e)
         {
            System.out.println("Arquivo não encontrado: "+l_NomeArquivo);
            e.printStackTrace();
         }
     }
  }
  
  public static String fnClasse(int p_Classe)
  {
    fnIniciaListaClasses();
    String l_Ret = m_ListaClasses[p_Classe];

/*
    switch(p_Classe)
    {
      case 1:
        l_Ret = "Origem";
        break;
      case 2:
        l_Ret = "Destino";
        break;
      case 3:
        l_Ret = "PAB";
        break;
      case 4:
        l_Ret = "Orgão";
        break;
      case 5:
        l_Ret = "GrpOrg";
        break;
      case 6:
        l_Ret = "Bilhetador";
        break;
      case 7:
        l_Ret = "Serviço";
        break;
      case 8:
        l_Ret = "OPR DE ENTRADA";
        break;
      case 9:
        l_Ret = "OPR DE SAÍDA";
        break;
      case 10:
        l_Ret = "BSC";
        break;
      default:
        l_Ret = ""+p_Classe;
        break;
    }
*/
    return l_Ret;
  }
}

