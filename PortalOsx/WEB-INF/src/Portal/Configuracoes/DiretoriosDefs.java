//Source file: C:/Usr/OSx/CDRView/Servlet/Configuracoes/DiretoriosDefs.java

package Portal.Configuracoes;

import Portal.Cluster.No;


/**
 * Classe para armazenamento de definições de diretórios que serão utilizadas
 * pelo Servlet para montagem dos caminhos das páginas/scripts.
 */
public class DiretoriosDefs {
    /**
     * Diretório dos executáveis/base do CDRView.
     */
    private String s_DIR_CDRVIEW;
    
    /**
     * Diretório dos executáveis/base do CDRView.
     */
    private String s_DIR_EXEC;

    /**
     * Diretório Web onde estão localizadas as páginas e JavaScripts.
     */
    private String s_DIR_WEB;

    /**
     * Diretório de download dos relatorios agendados.
     */
    private String s_DIR_DOWNLOAD;

    /**
     * Diretório de templates (páginas, js)
     */
    private String s_DIR_TEMPLATES;

    /**
     * Subdiretório de templates de páginas Html
     */
    private String s_DIR_TMPL_HTML;

    /**
     * Subdiretório de templates de JavaScripts
     */
    private String s_DIR_TMPL_JS;

    /**
     * Subdiretório de templates de JavaScripts
     */
    private String s_DIR_TMPL_TXT;

    /**
     * Subdiretório de templates de JavaScripts
     */
    private String s_DIR_TMPL_FORMS;

    /**
     * Subdiretório de templates de páginas Html
     */
    private String s_DIR_TMPL_PAGINAS;

    /**
     * Diretório onde estão os arquivos de IOR dos servidores
     */
    private String s_DIR_ARQS_REF;

    /**
     * Diretório que contém arquivos de configuração de alarmes
     */
    private String s_DIR_ARQS_ALR;

    /**
     * Diretório que contém arquivos de configuração de períodos (PMMs/HFIX)
     */
    private String s_DIR_ARQS_PERIODO;

    /**
     * Subdiretório de templates de menus de usuários
     */
    private String s_DIR_TMPL_MENUS;

    /**
     * Diretório que contém arquivos de informações a serem apresentados aos
     * usuários
     */
    public static final String s_DIR_ARQS_INFO = "Info/";

    /**
     * Diretório que contém arquivos de configuração
     */
    public static final String s_DIR_ARQS_CFG = "Cfg/";

    /**
     * Diretório dos arquivos de configuração do cdrview
     */
    public static final String s_DIR_CFG = "cfgsis/";

    /**
     * Diretório dos arquivos de configuração do Portal CDRView
     */
    public static final String s_DIR_CFG_WEB = "cfgsis/Web/";
    
    private No no;

    public DiretoriosDefs() { }

    /**
     * @return void
     * @exception Seta
     *                os diretórios após a leitura do arquivo de configuração
     *                básico.
     * @roseuid 3BF5A14E027D
     */
    public final void setDiretorios()
    {
    	s_DIR_EXEC = s_DIR_CDRVIEW;
        s_DIR_CDRVIEW += DefsComum.s_CLIENTE + "/";
        s_DIR_DOWNLOAD = s_DIR_WEB + "download/";
        s_DIR_TEMPLATES = s_DIR_WEB + "templates/";
        s_DIR_ARQS_REF = s_DIR_CDRVIEW + "ref/";
        s_DIR_TMPL_HTML = s_DIR_TEMPLATES + "html/";
        s_DIR_TMPL_JS = s_DIR_TEMPLATES + "js/";
        s_DIR_TMPL_TXT = s_DIR_TEMPLATES + "textos/";
        s_DIR_TMPL_FORMS = s_DIR_TEMPLATES + "formularios/";
        s_DIR_TMPL_PAGINAS = s_DIR_TEMPLATES + "paginas/";
        s_DIR_TMPL_MENUS = s_DIR_TEMPLATES + "menus/";

        System.out.println("\n--------------------------------------------------------------------");
        System.out.println("***** NO: "+this.getNo().getHostName()+"  ******\n");
        System.out.println("DefsComum.s_CLIENTE:          " + DefsComum.s_CLIENTE);
        System.out.println("DefsComum.s_ContextoWEB:      " + DefsComum.s_ContextoWEB);
        System.out.println("DefsComum.s_MAIL_ADM:         " + DefsComum.s_MAIL_ADM);
        System.out.println("DefsComum.s_QTD_ITENS_TABELA: " + DefsComum.s_QTD_ITENS_TABELA);
        System.out.println("s_DIR_EXEC:                   " + s_DIR_EXEC);
        System.out.println("s_DIR_CDRVIEW:                " + s_DIR_CDRVIEW);
        System.out.println("s_DIR_DOWNLOAD:               " + s_DIR_DOWNLOAD);
        System.out.println("s_DIR_TEMPLATES:              " + s_DIR_TEMPLATES);
        System.out.println("s_DIR_ARQS_REF:               " + s_DIR_ARQS_REF);
        System.out.println("s_DIR_TMPL_HTML:              " + s_DIR_TMPL_HTML);
        System.out.println("s_DIR_TMPL_JS:                " + s_DIR_TMPL_JS);
        System.out.println("s_DIR_TMPL_TXT:               " + s_DIR_TMPL_TXT);
        System.out.println("s_DIR_TMPL_FORMS:             " + s_DIR_TMPL_FORMS);
        System.out.println("s_DIR_TMPL_PAGINAS:           " + s_DIR_TMPL_PAGINAS);
        System.out.println("s_DIR_TMPL_MENUS:             " + s_DIR_TMPL_MENUS);
        System.out.println("----------------------------------------------------------------------\n");
    }
    
    public String getS_DIR_ARQS_ALR()
    {
        return s_DIR_ARQS_ALR;
    }

    public void setS_DIR_ARQS_ALR(String s_dir_arqs_alr)
    {
        s_DIR_ARQS_ALR = s_dir_arqs_alr;
    }

    public String getS_DIR_ARQS_PERIODO()
    {
        return s_DIR_ARQS_PERIODO;
    }

    public void setS_DIR_ARQS_PERIODO(String s_dir_arqs_periodo)
    {
        s_DIR_ARQS_PERIODO = s_dir_arqs_periodo;
    }

    public String getS_DIR_ARQS_REF()
    {
        return s_DIR_ARQS_REF;
    }

    public void setS_DIR_ARQS_REF(String s_dir_arqs_ref)
    {
        s_DIR_ARQS_REF = s_dir_arqs_ref;
    }

    public String getS_DIR_CDRVIEW()
    {
        return s_DIR_CDRVIEW;
    }

    public void setS_DIR_CDRVIEW(String s_dir_cdrview)
    {
        s_DIR_CDRVIEW = s_dir_cdrview;
    }

	public String getS_DIR_EXEC() 
	{
		return s_DIR_EXEC;
	}
	public void setS_DIR_EXEC(String s_dir_exec) 
	{
		s_DIR_EXEC = s_dir_exec;
	}
    public String getS_DIR_DOWNLOAD()
    {
        return s_DIR_DOWNLOAD;
    }

    public void setS_DIR_DOWNLOAD(String s_dir_download)
    {
        s_DIR_DOWNLOAD = s_dir_download;
    }

    public String getS_DIR_TEMPLATES()
    {
        return s_DIR_TEMPLATES;
    }

    public void setS_DIR_TEMPLATES(String s_dir_templates)
    {
        s_DIR_TEMPLATES = s_dir_templates;
    }

    public String getS_DIR_TMPL_FORMS()
    {
        return s_DIR_TMPL_FORMS;
    }

    public void setS_DIR_TMPL_FORMS(String s_dir_tmpl_forms)
    {
        s_DIR_TMPL_FORMS = s_dir_tmpl_forms;
    }

    public String getS_DIR_TMPL_HTML()
    {
        return s_DIR_TMPL_HTML;
    }

    public void setS_DIR_TMPL_HTML(String s_dir_tmpl_html)
    {
        s_DIR_TMPL_HTML = s_dir_tmpl_html;
    }

    public String getS_DIR_TMPL_JS()
    {
        return s_DIR_TMPL_JS;
    }

    public void setS_DIR_TMPL_JS(String s_dir_tmpl_js)
    {
        s_DIR_TMPL_JS = s_dir_tmpl_js;
    }

    public String getS_DIR_TMPL_MENUS()
    {
        return s_DIR_TMPL_MENUS;
    }

    public void setS_DIR_TMPL_MENUS(String s_dir_tmpl_menus)
    {
        s_DIR_TMPL_MENUS = s_dir_tmpl_menus;
    }

    public String getS_DIR_TMPL_PAGINAS()
    {
        return s_DIR_TMPL_PAGINAS;
    }

    public void setS_DIR_TMPL_PAGINAS(String s_dir_tmpl_paginas)
    {
        s_DIR_TMPL_PAGINAS = s_dir_tmpl_paginas;
    }

    public String getS_DIR_TMPL_TXT()
    {
        return s_DIR_TMPL_TXT;
    }

    public void setS_DIR_TMPL_TXT(String s_dir_tmpl_txt)
    {
        s_DIR_TMPL_TXT = s_dir_tmpl_txt;
    }

    public String getS_DIR_WEB()
    {
        return s_DIR_WEB;
    }

    public void setS_DIR_WEB(String s_dir_web)
    {
        s_DIR_WEB = s_dir_web;
    }
    public No getNo()
    {
        return no;
    }
    public void setNo(No no)
    {
        this.no = no;
    }
}