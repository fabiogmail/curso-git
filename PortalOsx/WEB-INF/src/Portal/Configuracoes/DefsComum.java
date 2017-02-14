package Portal.Configuracoes;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsxCluster Arquivo criado em:
 * 21/06/2005
 *
 * @author Marcos Urata
 * @version 1.0
 *
 * Breve Descrição:
 *
 */

public class DefsComum {

    public static short s_QTD_ITENS_TABELA = 10;
    public static short s_ELEMENTOS_PAGINA_REAGENDAMENTO = 20;
    public static int s_TIMEOUT_INATIVIDADE = 240;
    public static String s_CLIENTE = null;
    public static String sSUB_CLIENTE = null;
    public static String s_MAIL_ADM;
    public static final String s_ContextoWEB = "PortalOsx";

    /**
     * Valores possíveis no retorno do Logon
     */
    public static final short s_RET_USR_ADMINISTRADOR = 1;
    public static final short s_RET_USR_COMUM = 2;
    public static final short s_RET_USR_COMUM2 = 3;
    public static final short s_RET_LOGON_INVALIDO = -1;
    public static final short s_RET_USR_NAO_CADASTRADO = -2;
    public static final short s_RET_USR_JA_LOGADO = -3;
    public static final short s_RET_ERRO_SERV_UTIL = -4;
    public static final short s_RET_SISTEMA_EM_MANUT = -5;
    public static final short s_RET_QTD_USUARIOS_ESG = -6;
    public static final short s_RET_PERFIL_BLOQ = -7;
    public static final short s_RET_SERV_UTIL_FORA_AR = -15;

    /**
     * Valores possíveis no retorno do Logout
     */
    public static final short s_RET_LOGOUT_OK = 1;
    public static final short s_RET_LOGOUT_NOK = 2;

    /**
     * Nome do usuário administrador = "administrador".
     */
    public static String s_USR_ADMIN = "administrador";

    /**
     * Nome do perfis do administrador = "admin".
     */
    public static String s_PRF_ADMIN = "admin";

    public static final String s_ServUtil = "servutil";
    public static final String s_ServCtrl = "servctrl";
    public static final String s_ServAlr = "servalr";

    public static final String s_CARACS_ESPECIAIS = "¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    public static Vector s_CARACS_ESPECIAIS_CORRESP = null;

    public static Map s_Map_CaracEspec_CaracCorresp;
    public static Map s_Map_CaracCorresp_CaracEspec;

    static
    {
        setCaracsEspeciaisCorrespond();
    }

    /**
     * @return void
     * @exception Seta
     *                o vetor de caracteres especiais
     * @roseuid 40326DF40267
     */
    public static void setCaracsEspeciaisCorrespond()
    {
        System.out.println("->setCaracsEspeciaisCorrespond()");
        s_CARACS_ESPECIAIS_CORRESP = new Vector();
        //s_CARACS_ESPECIAIS_CORRESP.add("&nbsp;");
        s_CARACS_ESPECIAIS_CORRESP.add("&iexcl;");
        s_CARACS_ESPECIAIS_CORRESP.add("&cent;");
        s_CARACS_ESPECIAIS_CORRESP.add("&pound;");
        s_CARACS_ESPECIAIS_CORRESP.add("&curren;");
        s_CARACS_ESPECIAIS_CORRESP.add("&yen;");
        s_CARACS_ESPECIAIS_CORRESP.add("&brvbar;");
        s_CARACS_ESPECIAIS_CORRESP.add("&sect;");
        s_CARACS_ESPECIAIS_CORRESP.add("&uml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&copy;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ordf;");
        s_CARACS_ESPECIAIS_CORRESP.add("&laquo;");
        s_CARACS_ESPECIAIS_CORRESP.add("&not;");
        s_CARACS_ESPECIAIS_CORRESP.add("&shy;");
        s_CARACS_ESPECIAIS_CORRESP.add("&reg;");
        s_CARACS_ESPECIAIS_CORRESP.add("&macr;");
        s_CARACS_ESPECIAIS_CORRESP.add("&deg;");
        s_CARACS_ESPECIAIS_CORRESP.add("&plusmn;");
        s_CARACS_ESPECIAIS_CORRESP.add("&sup2;");
        s_CARACS_ESPECIAIS_CORRESP.add("&sup3;");
        s_CARACS_ESPECIAIS_CORRESP.add("&acute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&micro;");
        s_CARACS_ESPECIAIS_CORRESP.add("&para;");
        s_CARACS_ESPECIAIS_CORRESP.add("&middot;");
        s_CARACS_ESPECIAIS_CORRESP.add("&cedil;");
        s_CARACS_ESPECIAIS_CORRESP.add("&sup1;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ordm;");
        s_CARACS_ESPECIAIS_CORRESP.add("&raquo;");
        s_CARACS_ESPECIAIS_CORRESP.add("&frac14;");
        s_CARACS_ESPECIAIS_CORRESP.add("&frac12;");
        s_CARACS_ESPECIAIS_CORRESP.add("&frac34;");
        s_CARACS_ESPECIAIS_CORRESP.add("&iquest;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Agrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Aacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Acirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Atilde;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Auml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Aring;");
        s_CARACS_ESPECIAIS_CORRESP.add("&AElig;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ccedil;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Egrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Eacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("ê");
        s_CARACS_ESPECIAIS_CORRESP.add("&Euml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Igrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Iacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Icirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Iuml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ETH;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ntilde;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ograve;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Oacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ocirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Otilde;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ouml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&times;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Oslash;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ugrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Uacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Ucirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Uuml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&Yacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&THORN;");
        s_CARACS_ESPECIAIS_CORRESP.add("&szlig;");
        s_CARACS_ESPECIAIS_CORRESP.add("&agrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&aacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&acirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&atilde;");
        s_CARACS_ESPECIAIS_CORRESP.add("&auml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&aring;");
        s_CARACS_ESPECIAIS_CORRESP.add("&aelig;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ccedil;");
        s_CARACS_ESPECIAIS_CORRESP.add("&egrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&eacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("ê");
        s_CARACS_ESPECIAIS_CORRESP.add("&euml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&igrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&iacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&icirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&iuml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&eth;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ntilde;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ograve;");
        s_CARACS_ESPECIAIS_CORRESP.add("&oacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ocirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&otilde;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ouml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&divide;");
        s_CARACS_ESPECIAIS_CORRESP.add("&oslash;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ugrave;");
        s_CARACS_ESPECIAIS_CORRESP.add("&uacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&ucirc;");
        s_CARACS_ESPECIAIS_CORRESP.add("&uuml;");
        s_CARACS_ESPECIAIS_CORRESP.add("&yacute;");
        s_CARACS_ESPECIAIS_CORRESP.add("&thorn;");
        s_CARACS_ESPECIAIS_CORRESP.add("&yuml;");

        s_Map_CaracEspec_CaracCorresp = Collections.synchronizedMap(new TreeMap());
        s_Map_CaracCorresp_CaracEspec = Collections.synchronizedMap(new TreeMap());
        for (int i = 0; i < s_CARACS_ESPECIAIS.length(); i++)
        {
            s_Map_CaracEspec_CaracCorresp.put(new Character(s_CARACS_ESPECIAIS.charAt(i)), s_CARACS_ESPECIAIS_CORRESP.elementAt(i));
            s_Map_CaracCorresp_CaracEspec.put(s_CARACS_ESPECIAIS_CORRESP.elementAt(i), new Character(s_CARACS_ESPECIAIS.charAt(i)));
        }
    }

}