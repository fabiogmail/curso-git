package Portal.ReproCdr;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 15/08/2005
 *
 * @author Erick e Carlos Feijão
 * @version 1.1
 *
 * Interface de Serialização
 */
public interface Serializavel {

	public String serializa();
	public boolean desserializa(StringBuffer objetoSerializado);

}
