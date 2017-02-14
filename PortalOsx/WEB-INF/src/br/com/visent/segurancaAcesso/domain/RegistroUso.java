package br.com.visent.segurancaAcesso.domain;

import java.util.Date;
import java.util.List;

public class RegistroUso {

	private int id;	
	private String usuario;
	private String perfil;
	private String modulo;
	private List filtros;
	private int resultado;
	private int tempoExec;
	private Date dtRegistro;
	private String str_resultado;
	private int tipoRelat;
	private Date dtIni;
	private Date dtFim;
	
	public RegistroUso(){}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public int getResultado() {
		return resultado;
	}
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Date getDtRegistro() {
		return dtRegistro;
	}
	public void setDtRegistro(Date dtRegistro) {
		this.dtRegistro = dtRegistro;
	}
	public int getTempoExec() {
		return tempoExec;
	}
	public void setTempoExec(int tempoExec) {
		this.tempoExec = tempoExec;
	}
	public String getStr_resultado() {
		switch(resultado){
			case 1:
				str_resultado = "Sucesso";
				break;
			case 2:
				str_resultado = "Erro";
				break;
			case 3:
				str_resultado = "Sucesso";
				break;
			default:
				str_resultado = ""+resultado;
				break;
		}
		return str_resultado;
	}
	public void setStr_resultado(String str_resultado) {
		this.str_resultado = str_resultado;
	}


	public List getFiltros() {
		return filtros;
	}


	public void setFiltros(List filtros) {
		this.filtros = filtros;
	}
	
	public int getTipoRelat()
	{
		return tipoRelat;
	}

	public void setTipoRelat(int tipoRelat)
	{
		this.tipoRelat = tipoRelat;
	}
	
	public Date getDtFim()
	{
		return dtFim;
	}

	public void setDtFim(Date dtFim)
	{
		this.dtFim = dtFim;
	}

	public Date getDtIni()
	{
		return dtIni;
	}

	public void setDtIni(Date dtIni)
	{
		this.dtIni = dtIni;
	}
	
	
/*
REL_DESEMPENHO,    // 0
REL_PESQUISA_COD,  // 1
REL_DETALHE_CHAM,  // 2
REL_DDDY_DSP,      // 3
REL_DDDY_DET,      // 4
REL_PESQ_CUR,      // 5
REL_PESQ_NCUR,     // 6
REL_ANATEL_RES,    // 7
REL_ANATEL_PVMC3,  // 8
REL_ANATEL_PVMC7,  // 9
REL_ANATEL_PVMC8,  // 10
REL_ANATEL_PVMC9,  // 11
REL_TRAFEGO,       // 12
REL_MATRAFEGO,     // 13
REL_ANATEL_SMP3,   // 14
REL_ANATEL_SMP5,   // 15
REL_ANATEL_SMP6,   // 16
REL_ANATEL_SMP7,   // 17
REL_ANATEL_LDN,    // 18
REL_ANA_COMPL,     // 19
REL_ITX_AUDIT,     // 20
REL_ITX_FORA_ROTA, // 21
REL_MINUTOS_USO,   // 22 - Relatório de Minutos de Uso (MOU) (Desempenho2)
REL_RESUMO_CHAM,   // 23 - Relatório de Chamadas (consolidadas (Desempenho2)
REL_DISTRIB_FREQ,  // 24 - Relatório de Distribuição de Freqüência
REL_PERSEVERANCA,  // 25 - Relatório de Perseverança (Comportamento de Assinante)
REL_AUDITORIA_CHAM,// 26 - Relatório de Auditoria de Chamadas (Det. Cham)
REL_MATRIZ_INTER,  // 27 - Matriz de Tráfego 2(Versão com armazenamento em BD)
REL_INTERCONEXAO,  // 28 - Relatório de Interconexão
REL_CAIXA_POSTAL,  // 29 - Relatório de Caixa Postal

//RELATÓRIOS PARA ÁREA DE FRAUDE
REL_PESQ_IMEI,     // 30 - Pesquisa de IMEI (AESN) x NTC (Num. de A)
REL_CHAM_LONGA_DUR,// 31 - Chamadas de Longa Duração (Detalhe de Chamadas)
REL_DEST_ESPEC,    // 32 - Assinantes que mais ligaram para Destinos Específicos (Pesq. de Código)
REL_DEST_COMUNS,   // 33 - Destinos mais chamados por grupode de Assinantes A (Pesq. de Código)
REL_PESQ_POR_ERB,  // 34 - Chamadas por ERB (Pesq. de Código)
REL_PREF_DE_RISCO, // 35 - Chamadas de ou para prefixos de risco (Detalhe de Chamadas) 
REL_HISTORICO,     // 38 - Relatório Histórico;
*/
	public String getTipoRelatStr()
	{
		switch (tipoRelat)
		{
			case 0: return "DESEMPENHO";
			case 1: return "PESQUISA_COD";
			case 2: return "DETALHE_CHAM";
			case 3: return "DDDY_DSP";
			case 4: return "DDDY_DET";
			case 5: return "PESQ_CUR";
			case 6: return "PESQ_NCUR";
			case 7: return "ANATEL_RES";
			case 8: return "ANATEL_PVMC3";
			case 9: return "ANATEL_PVMC7";
			case 10: return "ANATEL_PVMC8";
			case 11: return "ANATEL_PVMC9";
			case 12: return "TRAFEGO";
			case 13: return "MATRAFEGO";
			case 14: return "SMP3";
			case 15: return "SMP5";
			case 16: return "SMP6";
			case 17: return "SMP7";
			case 18: return "LDN";
			case 19: return "ANA_COMPL";
			case 20: return "ITX_AUDIT";
			case 21: return "ITX_FORA_ROTA";
			case 22: return "MINUTOS_USO";
			case 23: return "RESUMO_CHAM";
			case 24: return "DISTRIB_FREQ";
			case 25: return "PERSEVERANCA";
			case 26: return "AUDITORIA_CHAM";
			case 27: return "MATRIZ_INTER";
			case 28: return "INTERCONEXAO";
			case 29: return "CAIXA_POSTAL";
			
			case 30: return "PESQ_IMEI";
			case 31: return "CHAM_LONGA_DUR";
			case 32: return "DEST_ESPEC";
			case 33: return "DEST_COMUNS";
			case 34: return "PESQ_POR_ERB";
			case 38: return "HISTORICO";
			case 35: return "PREF_DE_RISCO";
			case 50: return "SMP3e4";
			case 51: return "SMP8e9";

			default:return "";
		}
	}
	
	
	
}
