
import java.text.DecimalFormat;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

public class Customizer extends JRDefaultScriptlet {

	public void beforeReportInit() throws JRScriptletException {
	}

	public void afterReportInit() throws JRScriptletException {
	}

	public void beforePageInit() throws JRScriptletException {
	}

	public void afterPageInit() throws JRScriptletException {
	}

	public void beforeColumnInit() throws JRScriptletException {
	}

	public void afterColumnInit() throws JRScriptletException {
	}

	public void beforeGroupInit(String groupName) throws JRScriptletException {
	}

	public void afterGroupInit(String groupName) throws JRScriptletException {
	}

	public void beforeDetailEval() throws JRScriptletException {
		
	}

	public void afterDetailEval() throws JRScriptletException {
	}

	public String teste() throws JRScriptletException{
		
		return "testando";
	}
	
	public Double calculaMediana(Double valorUm, Double valorDois, Double valorTres, Double valorQuatro){
		Double []valor = new Double[4];
		
		valor[0] = new Double(valorUm);
		valor[1] = new Double(valorDois);
		valor[2] = new Double(valorTres);
		valor[3] = new Double(valorQuatro);
		
		Double auxiliar = new Double(0);
		
		boolean isOrdenado = false;
		
		while(!isOrdenado) {
			for (int i = 0; i < 3; i++) {
				if(valor[i] > valor[i+1]){
					auxiliar = valor[i];
					valor[i] = valor[i+1];
					valor[i+1] = auxiliar;
				}
			}
			
			if(valor[0] <= valor[1] && valor[1] <= valor[2] && valor[2] <= valor[3])
				isOrdenado = true;					
		}
		
		
		Double aux = new Double( (valor[1] + valor[2]) / 2.0);
		
		return formatar(aux);
		
	}
	
	public Double calculaVariacao(Double valorAtual, Double mediana){
		
		if(mediana == 0)
			return formatar(new Double(0));
		
		Double aux = new Double(valorAtual) / new Double(mediana) * 100;
		
		return formatar(aux);
	}
	
	public int getRestoDiv(Double dividendo, Double divisor){
		
		return (int) (dividendo.doubleValue() % divisor.doubleValue());
	}
	
	public static void main(String[] args) {
		new Customizer().formatar(new Double("0.0"));
	}
	
	public Double formatar(Double valor){
	
		DecimalFormat format = new DecimalFormat("#0.00");
		//System.out.println(valor);
		return new Double(format.format(valor).replace(",",".")); 
	}
	
	public Integer geraLink(Integer valorUm, Integer valorDois){
		
		if(valorUm == null & valorDois == null)
			return 0;
		else if((valorUm != null && valorUm > 0) || (valorDois != null && valorDois > 0))
			return 1;
		
		return 0;
		
	}
	
	public String concatenaBilhetador(String listaBilhetador, String Bilhetador){
		
		if(listaBilhetador == null || listaBilhetador.length() == 0){
			return Bilhetador;
		}
		else if(listaBilhetador.contains(Bilhetador)){
			return listaBilhetador;
		}else{
			String recebe = listaBilhetador +";"+Bilhetador;
			return recebe;
		}
		
		
	}

}
