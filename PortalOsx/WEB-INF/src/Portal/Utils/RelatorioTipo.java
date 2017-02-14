package Portal.Utils;

public class RelatorioTipo {
	
	public static final int DESEMPENHO = 0;
	public static final int PESQUISA_DE_CODIGO = 1;
	public static final int DETALHE_DE_CHAMADAS = 2;
	public static final int ANATEL_SMP3 = 14;
	public static final int ANATEL_SMP5 = 15;
	public static final int ANATEL_SMP6 = 16;
	public static final int ANATEL_SMP7 = 17;
	public static final int ANATEL_LDN = 18;
	public static final int MATRAF = 13;
	public static final int ANALISE_DE_COMPLEMENTO = 19;
	public static final int MINUTO_DE_USO = 22;
	public static final int RESUMO_DE_CHAMADAS = 23;
	public static final int DISTRIBUICAO_DE_FREQUENCIA = 24;
	public static final int PERSEVERANCA = 25;
	public static final int AUDITORIA_DE_CHAMADAS = 26;
	public static final int PESQUISA_DE_IMEIxNTX = 30;
	public static final int CHAMADA_LONGA_DURACAO = 31;
	public static final int DESTINOS_ESPECIFICOS = 32;
	public static final int DESTINOS_COMUNS = 33;
	public static final int PESQUISA_POR_ERB = 34;
	public static final int PREFIXO_DE_RISCO = 35;
	public static final int SERIE_HISTORICA = 38;
	
	public static String tipo(int tipo){
		switch (tipo) {
		case 0:
			return "DESENPENHO";
			
		case 1:
			return "PESQUISA DE CODIGO";
			
		case 2:
			return "DETALHE DE CHAMADAS";
		
		case 14:
			return "ANATEL SMP3";
			
		case 15:
			return "ANATEL SMP5";
			
		case 16:
			return "ANATEL SMP6";
			
		case 17:
			return "ANATEL SMP7";
			
		case 18:
			return "ANATEL LDN";
			
		case 13:
			return "MATRAF";
			
		case 19:
			return "ANALISE DE COMPLEMENTO";
			
		case 22:
			return "MINUTO DE USO";
			
		case 23:
			return "RESUMO DE CHAMADAS";
			
		case 24:
			return "DISTRIBUICAO DE FREQUENCIA";
			
		case 25:
			return "PERSEVERANCA";
			
		case 26:
			return "AUDITORIA DE CHAMADAS";
			
		case 30:
			return "PESQUISA DE IMEI x NTX";
			
		case 31:
			return "CHAMADA LONGA DURACAO";
			
		case 32:
			return "DESTINOS ESPECIFICOS";
			
		case 33:			
			return "DESTINOS COMUNS";
		
		case 34:	
			return "PESQUISA POR ERB";
			
		case 35:
			return "PREFIXO DE RISCO";
			
		case 38:
			return "SERIE HISTORICA";

		default:
			return "";
		}
	}

}
