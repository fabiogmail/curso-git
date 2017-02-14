package br.com.visent.segurancaAcesso;

import java.util.ArrayList;
import java.util.Collections;

import Portal.Utils.Ordena;
import br.com.visent.segurancaAcesso.domain.Config;


public class Teste {
	
	public static void main(String[] args) {		 
		/*RelatorioAcessoAction act = new RelatorioAcessoAction();
		ArrayList teste = new ArrayList();
		teste.addAll(act.listar());
		for (int i = 0; i < teste.size(); i++) {
			RegistroAcesso rel = (RegistroAcesso)teste.get(i);
			System.out.println(rel.getUsuario());
		}		
		teste = new ArrayList();
		RelatorioUsoAction action = new RelatorioUsoAction();
		teste.addAll(action.buscarListaDeFiltros(1));
		for (int i = 0; i < teste.size(); i++) {
			Filtro filtro = (Filtro)teste.get(i);
			System.out.println(filtro.getNomeFiltro());
		}*/	
		
		/*ConfigAction action = new ConfigAction();
		ArrayList lista = action.listar();
		for (int i = 0; i < lista.size(); i++) {
			Config config = (Config)lista.get(i);
			System.out.print(config.getId()+": ");
			System.out.print(config.getNome()+"=");
			System.out.println(config.getValor());
		}
		
		Config config = action.buscarPorNome("POP3");
		System.out.print(config.getId()+": ");
		System.out.print(config.getNome()+"=");
		System.out.println(config.getValor());*/
		
		/*Email email = new Email("mail.visent.com.br","feijao@visent.com.br");
		
		ArrayList lista = new ArrayList();
		lista.add("carlosfeijao@gmail.com");
		lista.add("carlosfeijao@terra.com.br");
		lista.add("c.feijao@terra.com.br");
		lista.add("feijao@visent.com.br");
		lista.add("cowsoa@gmail.com");
		lista.add("feijao51@hotmail.com");	
		
		lista.add("feijao@gmail.com.br");
		lista.add("feijao@email.com.br");
		
		
		try {
			email.enviar(lista,"Testando spam","é isso ai");
		} catch (MessagingException e) {
			e.printStackTrace();
		}*/
		Config config1 = new Config();
		config1.setId(10);
		config1.setNome("nome1");
		config1.setValor("lalala1");
		
		Config config2 = new Config();
		config2.setId(2);
		config2.setNome("nome2");
		config2.setValor("lalala2");
		
		Config config3 = new Config();
		config3.setId(3);
		config3.setNome("nome3");
		config3.setValor("lalala3");
		
		ArrayList lista = new ArrayList();
		lista.add(config1);
		lista.add(config3);
		lista.add(config2);
		
		
		Ordena ordena = new Ordena(
				"br.com.visent.segurancaAcesso.domain.Config",
				"id");
		
		Collections.sort(lista,ordena);
		
		for (int i = 0; i < lista.size(); i++) {
			Config config = (Config)lista.get(i);
			System.out.println(config.getId()+":"+config.getNome()+":"+config.getValor());
		}
				
		
		
	}
}
