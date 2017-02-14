package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;
import java.util.Date;

import br.com.visent.matraf.action.DataPeriodoAction;
import br.com.visent.matraf.domain.Periodo;
import br.com.visent.matraf.util.DataUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe para uso do AJAX
 * 
 */
public class PeriodoManager {
	
	public ArrayList getListaDatas(String central){
		DataPeriodoAction action = new DataPeriodoAction();
		ArrayList list = action.listarDatas(central);
		ArrayList resultado = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Date data = (Date)list.get(i);
			resultado.add(DataUtil.getDataFormatada(data,"dd/MM/yyyy"));
		}
		return resultado;
	}
	
	public ArrayList getListaByData(String dataStr, String central){
		Date data = new Date(DataUtil.dataInMillis(DataUtil.dataToLong(dataStr)));
		DataPeriodoAction action = new DataPeriodoAction();
		ArrayList list = action.listarPeriodos(data, central);
		ArrayList resultado = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Periodo periodo = (Periodo)list.get(i);
			PeriodoBean bean = new PeriodoBean();
			
			bean.setId(periodo.getId());
			bean.setData(data);
			bean.setHoraInicial(periodo.getHoraInicial());
			bean.setHoraFinal(periodo.getHoraFinal());
			
			bean.setDataStr(DataUtil.getDataFormatada(data,"dd/MM/yyyy"));
			bean.setHoraInicialStr(DataUtil.getDataFormatada(periodo.getHoraInicial(),"hh:mm"));
			bean.setHoraFinalStr(DataUtil.getDataFormatada(periodo.getHoraFinal(),"hh:mm"));
			bean.setIntervalo(bean.getHoraInicialStr()+" - "+bean.getHoraFinalStr());
			resultado.add(bean);
			
		}
		return resultado;
	}

}
