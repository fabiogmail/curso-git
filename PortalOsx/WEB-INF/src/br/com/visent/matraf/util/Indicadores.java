package br.com.visent.matraf.util;

import java.text.DecimalFormat;

import br.com.visent.matraf.domain.LinhaRelatorio;
import br.com.visent.matraf.domain.Periodo;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe para cálculo dos indicadores
 * 
 */
public class Indicadores {
	
	/** Periodo da pesquisa desses indicadores */
	private Periodo periodo;
	/** Instancia da LinhaRelatorio com os Indicadores e Recursos */
	private LinhaRelatorio linhaRelatorio; 
	/** formatar os números para interface*/
	public static final DecimalFormat  nFormat = new DecimalFormat("0.##");
	
	public Indicadores(Periodo periodo, LinhaRelatorio linhaRelatorio){
		this.periodo = periodo;
		this.linhaRelatorio = linhaRelatorio;
	}
	
	public Float getOkPorcentagem(){
		float resultado = 0;
		if(linhaRelatorio.getNumChamadas() > 0){
			resultado = (float)linhaRelatorio.getOkt() / (float)linhaRelatorio.getNumChamadas();
			resultado = resultado * 100;
		}
		return new Float(resultado);
	}
	
	public Float getTMO(){
		float resultado = 0;
		if(linhaRelatorio.getNumChamadas() > 0){
			resultado = (float)linhaRelatorio.getTto() / (float)linhaRelatorio.getNumChamadas();
		}
		return new Float(resultado);
	}
	
	public Float getTMC(){
		float resultado = 0;
		if(linhaRelatorio.getChamConv() > 0){
			resultado = (float)linhaRelatorio.getTtc() / (float)linhaRelatorio.getChamConv();
		}
		return new Float(resultado);
	}
	
	public Float getTrafego(){
		float resultado = 0;
    	long dataImilles = DataUtil.dataInMillis(DataUtil.getDataFormatada(periodo.getHoraInicial(),"yyyyMMddHHmmSS"));
    	long dataFmilles = DataUtil.dataInMillis(DataUtil.getDataFormatada(periodo.getHoraFinal(),"yyyyMMddHHmmSS"));
    	
    	float valor = (float)(dataFmilles - dataImilles) / (float)1000;
    	
    	if (valor > 0) {
    		resultado = (float)linhaRelatorio.getTto() / valor;
		}
    	
    	return new Float(resultado);
	}
	
	
}
