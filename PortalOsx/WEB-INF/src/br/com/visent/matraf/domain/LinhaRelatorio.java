package br.com.visent.matraf.domain;



/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe domain para as linhas da tabela LinhaRelatorio
 * 
 */
public class LinhaRelatorio {
	
	/** Lista de Campos de Recursos */
	public static final String C_RELACAO_TRAFEGO = "id_relacao_trafego";
	public static final String C_TIPO_ASSINANTE = "id_tipo_assinante";
	public static final String C_ROTAS = "id_rotas";
	public static final String C_ROTAE = "id_rotae";
	public static final String C_ORIGEM_DESTINO = "origem_destino";
	public static final String C_CELULAE = "id_celulae";
	public static final String C_CELULAS = "id_celulas";
	
	/** Lista de Campos de bilhetador */
	public static final String C_CENTRAL = "id_central";
	public static final String C_OPERADORA = "id_operadora";
	public static final String C_GRUPO_CELULA = "id_grupo_celula";
	
	public static final String[] RECURSOS = {
		C_RELACAO_TRAFEGO,
		C_TIPO_ASSINANTE,
		C_ORIGEM_DESTINO,
		C_ROTAS,
		C_ROTAE,
		C_CELULAE,
		C_CELULAS		
	};
	
	private int idLinhaRelatorio;
	private int idPeriodo;
	private int idRelatorio;
	private int idCentralRef;
	
	private int idCelulaE;
	private int idCelulaS;
	private int idCentral;
	private int idOperadora;
	private int idBsc;
	
	private int referencial;
	private int idRotaS;
	private int idRotaE;
	private int idTipoAssinante;
	private int idRelacaoTrafego;
	
	private float numChamadas;
	private float okt;
	private float ttc;
	private float tto;
	private float chamConv;
	
	public int getIdLinhaRelatorio() {
		return idLinhaRelatorio;
	}
	public void setIdLinhaRelatorio(int idLinhaRelatorio) {
		this.idLinhaRelatorio = idLinhaRelatorio;
	}
	
	public int getIdBsc() {
		return idBsc;
	}
	public void setIdBsc(int idBsc) {
		this.idBsc = idBsc;
	}
	public int getIdCelulaE() {
		return idCelulaE;
	}
	public void setIdCelulaE(int idCelulaE) {
		this.idCelulaE = idCelulaE;
	}
	public int getIdCelulaS() {
		return idCelulaS;
	}
	public void setIdCelulaS(int idCelulaS) {
		this.idCelulaS = idCelulaS;
	}
	public int getIdCentral() {
		return idCentral;
	}
	public void setIdCentral(int idCentral) {
		this.idCentral = idCentral;
	}

	public int getIdCentralRef() {
		return idCentralRef;
	}
	public void setIdCentralRef(int idCentralRef) {
		this.idCentralRef = idCentralRef;
	}

	public int getIdOperadora() {
		return idOperadora;
	}
	public void setIdOperadora(int idOperadora) {
		this.idOperadora = idOperadora;
	}

	public int getIdPeriodo() {
		return idPeriodo;
	}
	public void setIdPeriodo(int idPeriodo) {
		this.idPeriodo = idPeriodo;
	}
	
	public int getIdRelatorio() {
		return idRelatorio;
	}
	public void setIdRelatorio(int idRelatorio) {
		this.idRelatorio = idRelatorio;
	}

	public int getIdRelacaoTrafego() {
		return idRelacaoTrafego;
	}
	public void setIdRelacaoTrafego(int idRelacaoTrafego) {
		this.idRelacaoTrafego = idRelacaoTrafego;
	}
	public int getIdRotaE() {
		return idRotaE;
	}
	public void setIdRotaE(int idRotaE) {
		this.idRotaE = idRotaE;
	}
	public int getIdRotaS() {
		return idRotaS;
	}
	public void setIdRotaS(int idRotaS) {
		this.idRotaS = idRotaS;
	}
	public int getIdTipoAssinante() {
		return idTipoAssinante;
	}
	public void setIdTipoAssinante(int idTipoAssinante) {
		this.idTipoAssinante = idTipoAssinante;
	}

	public float getNumChamadas() {
		return numChamadas;
	}
	public void setNumChamadas(float numChamadas) {
		this.numChamadas = numChamadas;
	}

	public int getReferencial() {
		return referencial;
	}
	public void setReferencial(int referencial) {
		this.referencial = referencial;
	}
	
	public float getChamConv() {
		return chamConv;
	}
	public void setChamConv(float chamConv) {
		this.chamConv = chamConv;
	}
	public float getOkt() {
		return okt;
	}
	public void setOkt(float okt) {
		this.okt = okt;
	}
	public float getTtc() {
		return ttc;
	}
	public void setTtc(float ttc) {
		this.ttc = ttc;
	}
	public float getTto() {
		return tto;
	}
	public void setTto(float tto) {
		this.tto = tto;
	}
	public String toString(){
		return idPeriodo+";"+
			   idRelatorio+";"+
			   idCentralRef+";"+
			   idCelulaE+";"+
			   idCelulaS+";"+
			   idCentral+";"+
			   idOperadora+";"+
			   idBsc+";"+
			   referencial+";"+
			   idRotaS+";"+
			   idRotaE+";"+
			   idTipoAssinante+";"+
			   idRelacaoTrafego+";"+
			   numChamadas+";"+
			   okt+";"+
			   ttc+";"+
			   tto+";"+
			   chamConv+";"
			   ;
	}
	
}
