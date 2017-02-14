package Portal.Utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DBDeteccaoProperties {
	public static ResourceBundle prop;
	private static final DBDeteccaoProperties properties = new DBDeteccaoProperties();
	
	private DBDeteccaoProperties(){
		
	}
	
	static{
		try{
			prop = ResourceBundle.getBundle("dbDeteccao");			
		}
		catch(MissingResourceException e)
		{
			e.printStackTrace();
		}
	}	
	
	public static String getProperties(String chave){
		
		return prop.getString(chave);
	}
}
