/*
 * OSx Telecom
 * Criado em 15/09/2005
 * 
 */
package br.com.visent.matraf.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Marcos Urata 
 * 
 */
public class Test {

    public static void main(String[] args)
    {
    	Calendar cal = Calendar.getInstance();
    	String dataIstr = "20041104004521";
    	String dataFstr = "20041104004530";
    	int intervalo = 35;
    	
    	
    	long dataImilles = DataUtil.dataInMillis("20041102000000");
    	long dataFmilles = DataUtil.dataInMillis("20041103023015");
    	Date dataI = new Date(dataImilles);
    	Date dataF = new Date(dataFmilles);
    	
    	
    	
    	cal.setTime(dataI);
    	cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)-0);
    	Date dataIres = cal.getTime();
    	
    	cal.setTime(dataF);
    	cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+0);
    	Date dataFres = cal.getTime();
    	
    	Date dataR = new Date(dataFres.getTime()-dataIres.getTime());
    	
    	float totalSegundos = dataR.getTime()/1000;
    	float totalMinutos = totalSegundos/60;
    	float totalHoras = totalMinutos/60;
    	float totalDias = totalHoras/24;
    	
    	int dias = (int)totalDias;
    	int horas = (int)totalHoras%24;
    	int minutos = (int)totalMinutos%60;
    	int segundos = (int)totalSegundos%60;
   	
    	
    	System.out.println("mT: "+totalSegundos);
    	System.out.println("sT: "+totalMinutos);
    	System.out.println("hT: "+totalHoras);
    	System.out.println("dT: "+totalDias);
    	
    	System.out.println("d: "+dias);
    	System.out.println("h: "+horas);
    	System.out.println("m: "+minutos);
    	System.out.println("m: "+segundos);
    	
    	
    	
    	System.out.println(dataIres);
    	System.out.println(dataFres);
    	System.out.println(DataUtil.getDataFormatada(dataIres,"yyyy"));
    	
    	
    	
    }
}
