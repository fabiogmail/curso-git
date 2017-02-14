package br.com.visent.segurancaAcesso.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataUtil {

	/**
	 * Metodo que recebe um objeto Date e retorna uma String no formato desejado
	 * @param data objeto do tipo Date
	 * @param formato string de formato seguindo o modelo do SimpleDateFormat
	 * @return data formatada
	 */
	public static String getDataFormatada(Date data, String formato){
		if(data != null)
		{
			SimpleDateFormat formatoData = new SimpleDateFormat(formato);
			String dt = formatoData.format(data);
			return dt;
		}
		else
		{
			return "";
		}

	}
	
	/**
	 * @param data - Data a ser convertida.
	 *  Formato esperado: AAAAMMDDHHMMSS. Ex: 20050101100000 (01/01/2005 10:00hs)
	 * */
	public static long dataInMillis(String data)
	{
		if (data == null)
			throw new IllegalArgumentException("Parametro data esta null!");
		else if (data.length() != 14 && data.length() != 8)
			throw new IllegalArgumentException("Data com formato Invalido." +
											   " Formato esperado: AAAAMMDDHHMMSS. " +
											   "Ex: 20050101100000 (01/01/2005 10:00hs)");
		
		int ano = Integer.parseInt(data.substring(0,4));
		int mes = Integer.parseInt(data.substring(4,6));
		int dia = Integer.parseInt(data.substring(6,8));
		int hora = 0;
		int minuto = 0;
		int segundo = 0;
		if(data.length() > 8){
			hora = Integer.parseInt(data.substring(8,10));
			minuto = Integer.parseInt(data.substring(10,12));
			segundo = Integer.parseInt(data.substring(12,14));
		}
		
		
		//subtraindo um do mês, pois no java os meses são de 0 - 11 e não de 1 - 12
		Calendar cal = new GregorianCalendar(ano, mes-1, dia, hora, minuto, segundo);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * Metodo que recebe uma data(string) no formato "dd/mm/yyyy hh:mm:ss"
	 * e converte para o formato "yyyyMMddHHmmss"
	 * @param data data para conversão
	 * @return data convertida em yyyyMMddHHmmss
	 */
	public static String dataToLong(String valor)
	{
		String valorArray[] = valor.split(" ");
		
		String data = "00000000";
		String hora = "00:00:00";
		
		if(valorArray.length > 1){
			data = valorArray[0];
			hora = valorArray[1];
		}else{
			data = valorArray[0];
		}
		
		String dataArray[] = data.split("/");
		String dia = dataArray[0];
		String mes = dataArray[1];
		String ano = dataArray[2];
		
		String horaArray[] = hora.split(":");
		String h = horaArray[0];
		String m = horaArray[1];
		String s = horaArray[2];
		
		return ano+mes+dia+h+m+s;
	}

}
