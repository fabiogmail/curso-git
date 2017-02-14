package Portal.Utils;

import java.util.Date;

/**
 * 
 * Visent Inform�tica.
 * Projeto: PortalOsx
 * 
 * @author Carlos Feij�o
 * @version 1.0 
 * @created 04/12/2007 15:10:01
 */
public class ErroUtil extends Exception {
	
	public ErroUtil(String msg, Date data) {
		super(DataUtil.formataData(data,"dd/MM/yyyy hh:mm:ss")+" > "+msg);
	}
	public ErroUtil(String msg) {
		super(msg);
	}

}
