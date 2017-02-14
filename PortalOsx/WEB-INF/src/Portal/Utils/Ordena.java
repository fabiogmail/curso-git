package Portal.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

public class Ordena implements Comparator {

	public static final int DATA = 2;
	public static final int STR = 1;
	public static final int INT = 0;

	private Class tipo;
	private Method metodo;

	/**
	 * Construtor
	 * @param classeStr ex: "br.com.visent.Classe"
	 * @param coluna atributo do objeto que será ordenado
	 * @param tipo do atributo
	 */
	public Ordena(String classeStr, String coluna) {
		Class parametros[] = null, classe = null;
		Object argumentos[] = null;

		try {
			classe = Class.forName(classeStr);
		} catch (ClassNotFoundException e) {
			System.out.println("Ordena: ClassNotFoundException");
		}
		
		String metodoStr = coluna;
		String primeiraLetra = metodoStr.substring(0,1).toUpperCase();
		metodoStr = "get"+primeiraLetra+metodoStr.substring(1,metodoStr.length());
		
		try {
			this.metodo = classe.getMethod(metodoStr,parametros);
			this.tipo = this.metodo.getReturnType();
			if(tipo.getName().equalsIgnoreCase("int")) this.tipo = Integer.class;
			if(tipo.getName().equalsIgnoreCase("float")) this.tipo = Float.class;
			if(tipo.getName().equalsIgnoreCase("double")) this.tipo = Double.class;
		} catch (SecurityException e) {
			System.out.println("Ordena: SecurityException");
		} catch (NoSuchMethodException e) {
			System.out.println("Ordena: NoSuchMethodException");
		} catch (IllegalArgumentException e) {
			System.out.println("Ordena: IllegalArgumentException");
		} 
	}
	
	public int compare(Object ob1, Object ob2) {
		try {
			Class parametros[] = {tipo};
			Object args[] = {metodo.invoke(ob2,null)};
			Method method = tipo.getMethod("compareTo",parametros);
			return Integer.parseInt(method.invoke(metodo.invoke(ob1,null),args)+"");		
		} catch (IllegalArgumentException e) {
			System.out.println("Ordena: IllegalArgumentException");
		} catch (IllegalAccessException e) {
			System.out.println("Ordena: IllegalAccessException");
		} catch (InvocationTargetException e) {
			System.out.println("Ordena: InvocationTargetException");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
