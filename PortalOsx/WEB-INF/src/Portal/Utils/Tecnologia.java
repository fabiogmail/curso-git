package Portal.Utils;

import Portal.Cluster.No;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: PortalOsxCluster
 * Arquivo criado em: 24/11/2005
 * 
 * @author Erick Rodrigo
 * @version 1.0
 * 
 * Breve Descrição: classe que é um objeto do tipo tecnologia que vem com id e nome
 * 
 */

public class Tecnologia {

	private String id;
	private String nome;
	private String tecnologias;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeById(int id){
		 No noTmp = null;
		 try{
			 tecnologias += noTmp.getConexaoServUtil().getTecnologias();
		 }catch (Exception e) {
			
		}
		 return null;
	}
}
