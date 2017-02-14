package br.com.visent.matraf.action;

import br.com.visent.matraf.service.ajax.Pesquisa;

public class TesteAction {
	

    public static void main(String[] args)
    { 
    	/*String[] a = {"a","b","c"};
    	   	
    	Pesquisa pesquisa = new Pesquisa();
    	pesquisa.novo(5,"origem","celula",1,1,a);
    	ArrayList linhas = (ArrayList)pesquisa.getTabela().getLinhas();
    	for (int i = 0; i < linhas.size(); i++) {
    		ArrayList linha = (ArrayList)linhas.get(i);
    		for (int j = 0; j < linha.size(); j++) {
				System.out.print(linha.get(j)+";");
			}
			System.out.println();
		}
    	System.out.println("----------------");
    	
    	int[] indices = {0};
    	linhas = (ArrayList)pesquisa.getTabela().ordenar(indices); 
    	for (int i = 0; i < linhas.size(); i++) {
    		ArrayList linha = (ArrayList)linhas.get(i);
    		for (int j = 0; j < linha.size(); j++) {
				System.out.print(linha.get(j)+";");
			}
			System.out.println();
		}*/
    	
    	String[] a = {"relacao_trafego","rota","tipo_assinante","origem_destino"};
    	String[] b = {"relacao_trafego","origem_destino"};
    	String[] d = {"relacao_trafego","rota","origem_destino"};
    	String[] e = {};
    	String[] f = {"relacao_trafego","celula","rotas","rotae","tipo_assinante","origem_destino"};
    	int[] indices = {5};
    	
    	Pesquisa pesquisa = new Pesquisa();
    	//pesquisa.executar(2,"origem","grupocelula",17,0,f);

    	//String tabela = pesquisa.getRelatorio(0,indices,false);
    	String tabela = pesquisa.getRelatorioOrdenado(0);
    	//System.out.println(tabela);
    	/*ArrayList recursos = (ArrayList)tabela.get(0);
    	ArrayList indicadores = (ArrayList)tabela.get(1);

    	System.out.println("Numero de Paginas: "+pesquisa.getNumPaginas());
    	System.out.println("Numero de registros: "+indicadores.size());
    	System.out.println("Central Ref: "+pesquisa.getCentralRef());
    	System.out.println("Bilhetador: "+pesquisa.getNomeBilhetador());
    	
    	for (int i = 0; i < indicadores.size(); i++) {
			System.out.println(indicadores.get(i));
		}*/
    	
    //////////////////////////////////////////////
    	
    	
    	/*String[] a = {"relacao_trafego","origem_destino"};
    	LinhaRelatorioAction action = new LinhaRelatorioAction();
    	Pesquisa pesquisa = new Pesquisa();
    	pesquisa.executarResumo(2,"origem","central",11,0,a);
    	
    	int[] indices = {1};
    	ArrayList tabelas = pesquisa.getResumo(indices,false);
    	
    	for (int i = 0; i < tabelas.size(); i++) {
    		ArrayList tabela = (ArrayList)tabelas.get(i);
    		TabHTML tab = new TabHTML(tabela);
    		String html = tab.criarTabelaResumida();
    		
    		ArrayList resultado = (ArrayList)tabela.get(1);
    		System.out.println(tabela.get(2));
    		for (int j = 0; j < resultado.size(); j++) {
    			System.out.println(resultado.get(j));
			}
    		System.out.println("");
		}
    	System.out.println();*/
    	
    	
    }
}