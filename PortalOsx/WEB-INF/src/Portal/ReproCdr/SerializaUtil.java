package Portal.ReproCdr;

import java.util.Vector;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 15/08/2005
 *
 * @author Erick e Carlos Feijão
 * @version 1.0
 * 
 * Classe Util para metodos de serialização e desserialização
 */
public class SerializaUtil {
	
	public static String serializaVector(Vector objetosSerializaveis , String nomeClasse){
		String marcador = "{"+nomeClasse+"}";
		StringBuffer resultado = new StringBuffer(marcador);
		for(int i=0; i<objetosSerializaveis.size(); i++){
			Serializavel objeto = (Serializavel)objetosSerializaveis.get(i);
			resultado.append("[obj]");
			resultado.append(objeto.serializa());
		}
		resultado.append(marcador);
		return resultado.toString();
	}
	
	public static Vector desserializaVector(StringBuffer objetosSerializados, String nomeClasse){
		Class classe;
		Vector vetor = new Vector();
		try {
			classe = Class.forName("Portal.ReproCdr."+nomeClasse);
			
			String marcador = "{"+nomeClasse+"}";
			objetosSerializados.delete(0,marcador.length());
			
			//obtendo o valor dos objetos
			String objValor = objetosSerializados.substring(0,objetosSerializados.indexOf(marcador));
			//fazendo split em [obj], usando a \\[ pois o [ é usado para expressão regular
			String arrayObjetos[] = objValor.split("\\[obj\\]");
			objetosSerializados.delete(0,objValor.length());
			
			//desprezando o primeiro valor do array, pois é vazio
			for(int i=1; i<arrayObjetos.length; i++){
				Serializavel obj = (Serializavel)classe.newInstance();
				obj.desserializa(new StringBuffer(arrayObjetos[i]));//testar aqui
				vetor.add(obj);
			}
			
			objetosSerializados.delete(0,marcador.length());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return vetor;
	}

}
