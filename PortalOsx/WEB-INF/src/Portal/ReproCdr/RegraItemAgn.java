package Portal.ReproCdr;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 08/09/2005
 *
 * @author Carlos Feijão
 * @version 1.0
 *
 * Classe para as Regras de Reprocessamento do CDR-X
 */
public class RegraItemAgn implements Serializavel {
	
	/** Nome da Regra */
	private String nome;
	
	public final static int INVALIDO = 0;
	public final static int ARQ_CONFIG_INEXISTENTE = 1;
	public final static int ARQ_CONFIG_INVALIDO = 2;
	public final static int NAO_EXISTE_TEC = 3;
	public final static int CAMPO_INVALIDO = 4;
	public final static int OK = 5;
	
	public final static String REGRA_FDS = "Regra_FDS";
	public final static String REGRA_TIPOCHAM = "Regra_TipoCham";
	public final static String REGRA_TIPOASS = "Regra_TipoAss";
	public final static String REGRA_OP = "Regra_Op";
	public final static String REGRA_AREA = "Regra_Area";
	
	public String serializa() {
		return nome;
	}

	public boolean desserializa(StringBuffer objetoSerializado) {
		String array[] = objetoSerializado.toString().split(";");
		nome = array[0];
		return true;
	}
	

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
