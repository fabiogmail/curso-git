package Portal.ReproCdr;

/**
 * Visent Telecomunica��es LTDA. Projeto: PortalOsx Arquivo criado em:
 * 15/08/2005
 *
 * @author Erick e Carlos Feij�o
 * @version 1.1
 *
 * Interface de Serializa��o
 */
public interface Serializavel {

	public String serializa();
	public boolean desserializa(StringBuffer objetoSerializado);

}
