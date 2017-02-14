package Portal.ReproCdr;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 15/08/2005
 *
 * @author Erick e Carlos Feijão
 * @version 1.0
 *
 * Classe para armazenar a Central e seu status no reprocessamento
 */
public class CentralItemAgn implements Serializavel{
	
	/** Nome da Central*/
	private String nome;
	/** Seu status atual*/
	private int status;
	/** Quantidade de arquivos encontrados*/
	private int qtdArqEncontrados;
	/** Quantidade de cdrs encontrados*/
	private int qtdCdrEncontrados;
	/** Quantidade de cdrs processados*/
	private int qtdCdrProcessados;
	
	
	private int statusRegraArea;
	private int statusRegraFds;
	private int statusRegraOperadora;
	private int statusRegraTipoAss;
	private int statusRegraTipoCham;
	
	
	public final static int STATUS_CENTRAL_AGENDADO = 0;
	public final static int STATUS_CENTRAL_EM_EXECUCAO = 1;
	public final static int STATUS_CENTRAL_PRONTO = 2;
	
	public CentralItemAgn(){
		
	}
	
	public String serializa() 
	{
		return nome+";"+status+";"+qtdArqEncontrados+";"+qtdCdrEncontrados+";"+qtdCdrProcessados+";"+statusRegraArea+";"+statusRegraFds+";"+statusRegraOperadora+";"+statusRegraTipoAss+";"+statusRegraTipoCham;
	}
	
	public String serializaNovo(String nome) 
	{
		this.nome = nome;
		inicializar();		
		return serializa();
	}

	public boolean desserializa(StringBuffer objetoSerializado) 
	{
		String array[] = objetoSerializado.toString().split(";");
		nome = array[0];
		status = Integer.parseInt(array[1]); 
		qtdArqEncontrados = Integer.parseInt(array[2]); 
		qtdCdrEncontrados = Integer.parseInt(array[3]); 
		qtdCdrProcessados = Integer.parseInt(array[4]); 
		
		statusRegraArea = Integer.parseInt(array[5]); 
		statusRegraFds = Integer.parseInt(array[6]); 
		statusRegraOperadora = Integer.parseInt(array[7]); 
		statusRegraTipoAss = Integer.parseInt(array[8]); 
		statusRegraTipoCham = Integer.parseInt(array[9]); 
		return true;
	}
	
	public void inicializar()
	{
		status = CentralItemAgn.STATUS_CENTRAL_AGENDADO;
		qtdArqEncontrados = 0;
		qtdCdrEncontrados = 0;
		qtdCdrProcessados = 0;
		
		statusRegraArea = 0 ;
		statusRegraFds = 0;
		statusRegraOperadora = 0;
		statusRegraTipoAss = 0;
		statusRegraTipoCham = 0;
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getQtdArqEncontrados() {
		return qtdArqEncontrados;
	}
	public void setQtdArqEncontrados(int qtdArqEncontrados) {
		this.qtdArqEncontrados = qtdArqEncontrados;
	}
	public int getQtdCdrEncontrados() {
		return qtdCdrEncontrados;
	}
	public void setQtdCdrEncontrados(int qtdCdrEncontrados) {
		this.qtdCdrEncontrados = qtdCdrEncontrados;
	}
	public int getQtdCdrProcessados() {
		return qtdCdrProcessados;
	}
	public void setQtdCdrProcessados(int qtdCdrProcessados) {
		this.qtdCdrProcessados = qtdCdrProcessados;
	}
	
	public int getStatusRegraArea() {
		return statusRegraArea;
	}
	public void setStatusRegraArea(int statusRegraArea) {
		this.statusRegraArea = statusRegraArea;
	}
	public int getStatusRegraFds() {
		return statusRegraFds;
	}
	public void setStatusRegraFds(int statusRegraFds) {
		this.statusRegraFds = statusRegraFds;
	}
	public int getStatusRegraOperadora() {
		return statusRegraOperadora;
	}
	public void setStatusRegraOperadora(int statusRegraOperadora) {
		this.statusRegraOperadora = statusRegraOperadora;
	}
	public int getStatusRegraTipoAss() {
		return statusRegraTipoAss;
	}
	public void setStatusRegraTipoAss(int statusRegraTipoAss) {
		this.statusRegraTipoAss = statusRegraTipoAss;
	}
	public int getStatusRegraTipoCham() {
		return statusRegraTipoCham;
	}
	public void setStatusRegraTipoCham(int statusRegraTipoCham) {
		this.statusRegraTipoCham = statusRegraTipoCham;
	}
}
