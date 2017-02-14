//Source file: C:/Usr/OSx/CDRView/Servlet/Configuracoes/DiretoriosDefs.java

package Portal.Configuracoes;

import Portal.Cluster.No;


/**
 * Classe para armazenamento de defini��es de diret�rios que ser�o utilizadas
 * pelo Servlet para montagem dos caminhos das p�ginas/scripts.
 */
public class DiretoriosDefs {
    /**
     * Diret�rio dos execut�veis/base do CDRView.
     */
    private String s_DIR_CDRVIEW;
    
    /**
     * Diret�rio dos execut�veis/base do CDRView.
     */
    private String s_DIR_EXEC;

    /**
     * Diret�rio Web onde est�o localizadas as p�ginas e JavaScripts.
     */
    private String s_DIR_WEB;

    /**
     * Diret�rio de download dos relatorios agendados.
     */
    private String s_DIR_DOWNLOAD;

    /**
     * Diret�rio de templates (p�ginas, js)
     */
    private String s_DIR_TEMPLATES;

    /**
     * Subdiret�rio de templates de p�ginas Html
     */
    private String s_DIR_TMPL_HTML;

    /**
     * Subdiret�rio de templates de JavaScripts
     */
    private String s_DIR_TMPL_JS;

    /**
     * Subdiret�rio de templates de JavaScripts
     */
    private String s_DIR_TMPL_TXT;

    /**
     * Subdiret�rio de templates de JavaScripts
     */
    private String s_DIR_TMPL_FORMS;

    /**
     * Subdiret�rio de templates de p�ginas Html
     */
    private String s_DIR_TMPL_PAGINAS;

    /**
     * Diret�rio onde est�o os arquivos de IOR dos servidores
     */
    private String s_DIR_ARQS_REF;

    /**
     * Diret�rio que cont�m arquivos de configura��o de alarmes
     */
    private String s_DIR_ARQS_ALR;

    /**
     * Diret�rio que cont�m arquivos de configura��o de per�odos (PMMs/HFIX)
     */
    private String s_DIR_ARQS_PERIODO;

    /**
     * Subdiret�rio de templates de menus de usu�rios
     */
    private String s_DIR_TMPL_MENUS;

    /**
     * Diret�rio que cont�m arquivos de informa��es a serem apresentados aos
     * usu�rios
     */
    public static final String s_DIR_ARQS_INFO = "Info/";

    /**
     * Diret�rio que cont�m arquivos de configura��o
     */
    public static final String s_DIR_ARQS_CFG = "Cfg/";

    /**
     * Diret�rio dos arquivos de configura��o do cdrview
     */
    public static final String s_DIR_CFG = "cfgsis/";

    /**
     * Diret�rio dos arquivos de configura��o do Portal CDRView
     */
    public static final String s_DIR_CFG_WEB = "cfgsis/Web/";
    
    private No no;

    public DiretoriosDefs() { }

    /**
     * @return void
     * @exception Seta
     *                os diret�rios ap�s a leitura do arquivo de configura��o
     *                b�sico.
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