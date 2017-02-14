package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Conexao.CnxServUtil;
import Portal.Configuracoes.DefsComum;

public class OpLogonAjax 
{
	
	private No no = null;
	private boolean usuarioExiste = false;
	private boolean problemaServUtil = false;
	
	/**
	 * Verifica login do usuario
	 * 
	 * @param usuario
	 * @param senha
	 * @return informações para interface
	 */
	public Object[] verificaLogon(String usuario, String senha) {
		Object[] object = new Object[2];		
		if(problemaServUtil){
			object[0] = new Boolean(false);
			object[1] = new Integer(4); // problema no servutil
			return object;
		}else{
			if(usuarioExiste){
				CnxServUtil util = no.getConexaoServUtil();
				if (util.verificaLogon(usuario, senha)) {
					object[0] = new Boolean(true);
					object[1] = new Integer(1); // senha valido
				} else {
					object[0] = new Boolean(false);
					object[1] = new Integer(2); // senha invalido
				}
				return object;
			}else{
				//primeiro login do administrador
				if(usuario.equalsIgnoreCase("administrador"))
				{
					no = NoUtil.getNoCentral();
					no.getConexaoServUtil().verificaLogon(usuario, senha);
				
					object[0] = new Boolean(true);
					object[1] = new Integer(1); // senha valida
				}
				else
				{
					//Faço o verificaLogon somente para armazenar 
					//o registro de tentativa de acesso
					CnxServUtil util = NoUtil.getNoCentral().getConexaoServUtil();
					util.verificaLogon(usuario, senha);
				
					object[0] = new Boolean(false);
					object[1] = new Integer(3); // login nao existe
				}
				return object;
			}
		}		
	}

	/**
	 * Verifica se o perfil é monitorado
	 * 
	 * @param usuario
	 * @return informações para interface
	 */
	public Object[] verificaMonitorado(String usuario) {
		Object[] object = new Object[2];
		CnxServUtil util = no.getConexaoServUtil();
		if (util.verificaUsuMonitorado(usuario)) {
			object[0] = new Boolean(true);
			object[1] = new Integer(1); // usuario deve ser monitorado
		} else {
			object[0] = new Boolean(false);
			object[1] = new Integer(2); // usuario nao deve ser monitorado
		}
		return object;

	}
	
	/**
	 * verifica o servidor que o usuario irá se conectar
	 * @param usuario
	 * @return true para sucesso, false para erro
	 */
	public void check(String usuario){
		No noTmp = null;
		boolean chkUsuario = false;
		boolean chkProblemaServUtil = false;
		for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();) {
			noTmp = (No) iter.next();
			if(verificaServUtil(noTmp)){
				String usuarioChk = noTmp.getConexaoServUtil().trataListaUsuario(usuario);
		    	if(!usuarioChk.equals("")){
		    		chkUsuario = true;
		    		this.no = noTmp;
		    		break;
		    	}
			}else{
				chkProblemaServUtil = true;
				break;
			}			
		}
		this.usuarioExiste = chkUsuario;
		this.problemaServUtil = chkProblemaServUtil;
	}

	/**
	 * Verifica servutil
	 * 
	 * @return
	 */
	private boolean verificaServUtil(No no) {
		if (no.getConexaoServUtil().getM_iUtil() == null) {
			no.createConexao(DefsComum.s_ServUtil, no.getConexaoServUtil()
					.getModoConexao(), no.getHostName(), no
					.getConexaoServUtil().getNomeObjetoCorba(), no
					.getConexaoServUtil().getPorta());
			if (no.getConexaoServUtil().getM_iUtil() == null) {
				return false;
			} else {
				no.setConexaoServUtil(no.getConexaoServUtil());
				return true;
			}
		} else {
			try {
				no.getConexaoServUtil().getM_iUtil().fnPing();
			} catch (org.omg.CORBA.COMM_FAILURE e) {
				return false;
			}
			return true;
		}
	}
}
