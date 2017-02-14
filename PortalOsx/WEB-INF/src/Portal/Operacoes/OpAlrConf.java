package Portal.Operacoes;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.Request;

import Portal.Cluster.NoUtil;
import Portal.ComponentesHTML.HTMLTags;
import Portal.ConfAlarme.TabelaGrupos;
import Portal.ConfAlarme.TabelaGruposCnf;
import Portal.ConfAlarme.TabelaGruposCnfParm;
import Portal.ConfAlarme.TabelaGruposCnfRegras;
import Portal.ConfAlarme.TabelaPeriodos;
import Portal.ConfAlarme.TabelaPeriodosCnf;

public class OpAlrConf extends OperacaoAbs
{ 

	/**
	 * 
	 * @uml.property name="m_TabelaGrupos"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	static private TabelaGrupos m_TabelaGrupos;

	/**
	 * 
	 * @uml.property name="m_TabelaPeriodos"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	static private TabelaPeriodos m_TabelaPeriodos;

  private String m_NomeServlet;
  private int m_Op = 0;
  private int m_SubOp = 0;
  private int m_Objeto = 0;
  static private String m_TagOp;
  static private String m_TagSubOp;
  static private String m_TagObjeto;
  static private String m_Dica = "";

  static
  {
    m_TagOp = "AlCf_OP";
    m_TagSubOp = "AlCf_SOP";
    m_TagObjeto = "AlCf_OBJ";
    m_TabelaGrupos = null;
    m_TabelaPeriodos = null;
  }

  public OpAlrConf() throws Exception
  {
    try
    {
      fnInicializa("/PortalOsx/servlet/Portal.cPortal");
    } catch (Exception e)
    {
      throw e;
    }
  }

  public OpAlrConf(String p_NomeServlet) throws Exception
  {
    try
    {
      fnInicializa(p_NomeServlet);
    } catch (Exception e)
    {
      throw e;
    }
  }

  public void fnInicializa(String p_NomeServlet) throws Exception
  {
    m_NomeServlet = p_NomeServlet;
    try
    {
      if (m_TabelaGrupos == null)
        m_TabelaGrupos = new TabelaGrupos(p_NomeServlet);
      if (m_TabelaPeriodos == null)
        m_TabelaPeriodos = new TabelaPeriodos(p_NomeServlet);
    } catch (Exception e)
    {
      throw e;
    }
  }

  public boolean iniciaOperacao(String p_Mensagem)
  {
    m_Args = new String[4];
    
    //System.out.println("Diretorio: "+Portal.Configuracoes.NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML());
    //System.out.println("Arquivo  : "+Portal.Configuracoes.ArquivosDefs.s_HTML_CONF_ALR);

    m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
    m_Args[1] = Portal.Configuracoes.ArquivosDefs.s_HTML_CONF_ALR;
    try
    {
      m_Args[2] = fnTela(m_Request, m_Response, "form1");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      m_Args[2] = e.getMessage();
    }
    m_Args[3] = m_Dica;

    m_Html.enviaArquivo(m_Args);
    return true;
  }

  public void fnPost(HttpServletRequest p_Req, HttpServletResponse p_Resp) throws ServletException, IOException
  {
    PrintWriter out = new PrintWriter (p_Resp.getOutputStream());
    String l_NomeForm = "Form1";
    String l_Titulo = "Parametrização de Alarmes - Estratos";

    out.println(HTMLTags.HTML());
    out.println(HTMLTags.HEAD());
    out.println(HTMLTags.TITLE());
    out.println(l_Titulo);
    out.println(HTMLTags._TITLE());
    out.println(HTMLTags._HEAD());
    out.println(HTMLTags.BODY());
    out.println(HTMLTags.FORM(l_NomeForm, "post", m_NomeServlet));
    try
    {
      out.println(fnTela(p_Req, p_Resp, l_NomeForm));
    } catch (Exception e)
    {
      out.println("Erro");
      e.printStackTrace();	 
    }
    out.println(HTMLTags._FORM());
    out.println(HTMLTags._BODY());
    out.println(HTMLTags._HTML());
    out.close();
  }

  public String fnTela(HttpServletRequest p_Req, HttpServletResponse p_Resp, String p_NomeForm) throws ServletException, IOException, Exception
  {
    p_Resp.setContentType("text/html");
    String l_Tmp = "";

    String l_Form = p_NomeForm;
    if (p_NomeForm == null)
      l_Form = "FormGeraTabela";

    if (p_NomeForm == null)
      l_Tmp += HTMLTags.FORM(l_Form, "post", m_NomeServlet);

    fnLeParametros(p_Req);
    l_Tmp += HTMLTags.INPUT("hidden", m_TagOp, ""+m_Op);
    if ((m_SubOp == 6) || (m_SubOp == 7))
      l_Tmp += HTMLTags.INPUT("hidden", m_TagSubOp, "5");
    else
      l_Tmp += HTMLTags.INPUT("hidden", m_TagSubOp, ""+m_SubOp);
    l_Tmp += HTMLTags.INPUT("hidden", m_TagObjeto, ""+m_Objeto);
    l_Tmp += HTMLTags.NL;

    String l_JSAdicional = "";
    l_JSAdicional += "function OperacaoAlr(p_Operacao, p_SubOperacao, p_Objeto)"+HTMLTags.NL;
    l_JSAdicional += "{"+HTMLTags.NL;
    l_JSAdicional += "  document."+l_Form+"."+m_TagOp+".value = p_Operacao;"+HTMLTags.NL;
    l_JSAdicional += "  document."+l_Form+"."+m_TagSubOp+".value = p_SubOperacao;"+HTMLTags.NL;
    l_JSAdicional += "  document."+l_Form+"."+m_TagObjeto+".value = p_Objeto;"+HTMLTags.NL;
    l_JSAdicional += "  document."+l_Form+".submit();"+HTMLTags.NL;
    l_JSAdicional += "}"+HTMLTags.NL;
    l_JSAdicional += HTMLTags.NL;

    
    if (m_Op == 0)
    {
      l_Tmp += m_TabelaGrupos.fnTela(p_Req, l_Form, l_JSAdicional);
      m_Dica = m_TabelaGrupos.m_Dica;
    } else
    {
      switch(m_SubOp)
      {
        case 0:
        case 1:
        {
          TabelaGruposCnf l_TabelaGruposCnf = new TabelaGruposCnf(m_NomeServlet);
          l_Tmp += l_TabelaGruposCnf.fnTela(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnf.m_Dica;
          break;
        }
        case 2:
        {
          TabelaGruposCnf l_TabelaGruposCnf = new TabelaGruposCnf();
          l_Tmp += l_TabelaGruposCnf.fnTelaConfirma(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnf.m_Dica;
          break;
        }
        case 3:
        {
          TabelaGruposCnfParm l_TabelaGruposCnfParm = new TabelaGruposCnfParm(m_NomeServlet);
          l_Tmp += l_TabelaGruposCnfParm.fnTela(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnfParm.m_Dica;
          break;
        }
        case 4:
        {
          TabelaGruposCnfParm l_TabelaGruposCnfParm = new TabelaGruposCnfParm();
          l_Tmp += l_TabelaGruposCnfParm.fnTelaConfirma(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnfParm.m_Dica;
          break;
        }
        case 5:
        {          
          TabelaGruposCnfRegras l_TabelaGruposCnfRegras = new TabelaGruposCnfRegras(m_NomeServlet);
          l_Tmp += l_TabelaGruposCnfRegras.fnTela(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnfRegras.m_Dica;
          break;
        }
        case 6:
        {
          TabelaGruposCnfRegras l_TabelaGruposCnfRegras = new TabelaGruposCnfRegras(m_NomeServlet);
          l_Tmp += l_TabelaGruposCnfRegras.fnTelaAdiciona(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnfRegras.m_Dica;
          break;
        }
        case 7:
        {
          TabelaGruposCnfRegras l_TabelaGruposCnfRegras = new TabelaGruposCnfRegras(m_NomeServlet);
          l_Tmp += l_TabelaGruposCnfRegras.fnTelaRemove(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnfRegras.m_Dica;
          break;
        }
        case 8:
        {
          TabelaGruposCnfRegras l_TabelaGruposCnfRegras = new TabelaGruposCnfRegras();
          //adiciona os bilhetadores que estão na tela
          m_TabelaGrupos.fnGrupo(m_Objeto).bilhetadores = p_Req.getParameter("campoBilhetadores");
          l_Tmp += l_TabelaGruposCnfRegras.fnTelaConfirma(p_Req, p_NomeForm, l_JSAdicional, m_TabelaGrupos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaGruposCnfRegras.m_Dica;
          break;
        }
        case 9:
        {
          l_Tmp += m_TabelaPeriodos.fnTela(p_Req, p_NomeForm, l_JSAdicional);
          m_Dica = m_TabelaPeriodos.m_Dica;
          break;
        }
        case 10:
        {
          TabelaPeriodosCnf l_TabelaPeriodosCnf = new TabelaPeriodosCnf(m_NomeServlet);
          l_Tmp += l_TabelaPeriodosCnf.fnTela(p_Req, p_NomeForm, l_JSAdicional, m_TabelaPeriodos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaPeriodosCnf.m_Dica;
          break;
        }
        case 11:
        {
          TabelaPeriodosCnf l_TabelaPeriodosCnf = new TabelaPeriodosCnf();
          l_Tmp += l_TabelaPeriodosCnf.fnTelaConfirma(p_Req, p_NomeForm, l_JSAdicional, m_TabelaPeriodos, m_Op, m_SubOp, m_Objeto);
          m_Dica = l_TabelaPeriodosCnf.m_Dica;
          break;
        }
      }
    }

    if (p_NomeForm == null)
      l_Tmp += HTMLTags._FORM();
      
    return l_Tmp;
  }

  public void fnLeParametros(HttpServletRequest p_Req)
  {
    try
    {
      String l_Operacao = p_Req.getParameter(m_TagOp);
      m_Op = new Integer(l_Operacao).intValue();
      String l_SubOperacao = p_Req.getParameter(m_TagSubOp);
      m_SubOp = new Integer(l_SubOperacao).intValue();
      String l_Grupo = p_Req.getParameter(m_TagObjeto);
      m_Objeto = new Integer(l_Grupo).intValue();
    } catch (Exception e)
    {
      m_Op = 0;
      m_SubOp = 0;
      m_Objeto = 0;
    }
  }
}
