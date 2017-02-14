package br.com.visent.matraf.service.util;

import java.util.Comparator;

public class ChaveComparator implements Comparator{
	public int compare(Object arg0, Object arg1) {
		String elem1[] = arg0.toString().split(";");
		String elem2[] = arg1.toString().split(";");
		
		int num1 = Integer.parseInt(elem1[0]);
		int num2 = Integer.parseInt(elem2[0]);
		
		return num1-num2;
	}

}
