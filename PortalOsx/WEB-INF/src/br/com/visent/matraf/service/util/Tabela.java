package br.com.visent.matraf.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.visent.matraf.domain.Celula;
import br.com.visent.matraf.domain.LinhaRelatorio;
import br.com.visent.matraf.domain.Periodo;
import br.com.visent.matraf.domain.RelacaoTrafego;
import br.com.visent.matraf.domain.Rota;
import br.com.visent.matraf.domain.TipoAssinante;
import br.com.visent.matraf.util.Indicadores;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe domain para os efetuar a pesquisa
 * 
 */
public class Tabela {
	
	/** Lista de Object[] (linhas) */
	private ArrayList pesquisa;
	/** Linhas */
	private ArrayList linhas;
	/** Indice atual da ordenação */
	private int indice;
	/** Variavel pra saber se é pesquisa por origem*/
	private boolean origem;
	/** Lista de Recursos*/
	private ArrayList recursos;
	/** Lista de Contadores */
	private ArrayList contadores;
	
	
	/**
	 * construtor
	 * @param pesquisa lista com o resultado
	 * @param origem se a pesquisa é por origem
	 */
	public Tabela(ArrayList pesquisa, ArrayList recursos, ArrayList contadores, boolean origem){
		this.pesquisa = pesquisa;
		this.origem = origem;
		this.indice = 0;
		this.recursos = recursos;
		this.contadores = contadores;
		this.linhas = montarLinhas();
	}
	
	/**
	 * Método que pega a lista de objetos do DAO e monta uma tabela de Strings
	 * para ser usada na interface
	 * @return Arraylist da tabela
	 */
	public ArrayList montarLinhas(){
		
		ArrayList linhas = new ArrayList();
		
		for (int i = 0; i < pesquisa.size(); i++) {
			
			//Array de Objetos é definido pelo LinhaRelatorioDAO
			Object[] objeto = (Object[])pesquisa.get(i);
			
			ArrayList linha = new ArrayList();
			
			//Definindo Recursos
			for (int j = 0; j < recursos.size(); j++) {
				
				String recurso = (String)recursos.get(j);
				
				if(recurso.equalsIgnoreCase(LinhaRelatorio.C_RELACAO_TRAFEGO))
				{
					linha.add("("+ ((RelacaoTrafego)objeto[9]).getId()      +")"+
						       ((RelacaoTrafego)objeto[9]).getOrigem()  +"-"+
						       ((RelacaoTrafego)objeto[9]).getDestino()  );
				}
				else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_TIPO_ASSINANTE))
				{
					linha.add(((TipoAssinante)objeto[8]).getNome());
				}
				else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_ROTAS))
				{
					if(objeto[10] == null){
						linha.add("--");
					}else{
						linha.add(((Rota)objeto[10]).getNome());
					}
				}
				else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_ROTAE))
				{
					if(objeto[11] == null){
						linha.add("--");
					}else{
						linha.add(((Rota)objeto[11]).getNome());
					}
				}
				else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_CELULAE))
				{
					if(objeto[3] == null){
						linha.add("--");
					}else{
						linha.add(((Celula)objeto[3]).getNome());
					}
					
				}
				else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_CELULAS))
				{
					if(objeto[4] == null){
						linha.add("--");
					}else{
						linha.add(((Celula)objeto[4]).getNome());
					}
				}
				else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_ORIGEM_DESTINO))
				{
					String ref = "";
					if(origem){
						ref = ((RelacaoTrafego)objeto[9]).getDestino();
					}else{
						ref = ((RelacaoTrafego)objeto[9]).getOrigem();
					}
					
					linha.add(ref);
				}
			}
			
			
			// Adicionando Indicadores
			Indicadores indicadores = new Indicadores(((Periodo)objeto[0]),((LinhaRelatorio)objeto[1]));
			
			linha.add(new Float(((LinhaRelatorio)objeto[1]).getNumChamadas()));
			linha.add(indicadores.getTrafego());
			linha.add(indicadores.getTMC());
			linha.add(indicadores.getTMO());
			linha.add(new Float(((LinhaRelatorio)objeto[1]).getOkt()));
			linha.add(indicadores.getOkPorcentagem());
			

			// Adicinando linha
			linhas.add(linha);
		}
		return linhas;
	}
	
	
	/**
	 * Método que ordena os valores da tabela, de acordo com a ordem das colunas
	 * @param indices indice das colunas, ordena pela ordem passada
	 * @return lista ordenada
	 */
	public ArrayList ordenar(int[] indices, boolean reversa){
		ArrayList resultado = new ArrayList();
		List lista = new ArrayList();
		
		boolean ehNumber = true;
		if(indices[0] < recursos.size()){
			ehNumber = false;
		}
		
		for (int i = 0; i < linhas.size(); i++) {
			ArrayList linha = (ArrayList)linhas.get(i);
			LinhaOrdena linhaOrdena = new LinhaOrdena(linha,linha.get(indices[0]),ehNumber);
			lista.add(linhaOrdena);
			for (int j = 0; j < recursos.size(); j++) {
				String valor = (String)linha.get(j)+"";
			}
			for (int j = recursos.size(); j < linha.size(); j++) {
				Float valor = (Float)linha.get(j);
			}
		}
		
		//ordenando
		Collections.sort(lista);
		//caso seja no sentido inverso
		if(reversa){
			Collections.reverse(lista);
		}
		
		for (int i = 0; i < lista.size(); i++) {
			LinhaOrdena linharOrdena = (LinhaOrdena)lista.get(i);
			resultado.add(linharOrdena.getLinha());
		}
		this.linhas = resultado;
		
		return resultado;
	}
	

	public ArrayList getLinhas(){
		return linhas;
	}

	public boolean getOrigem()
	{
		return this.origem;
	}
	
	public ArrayList getPesquisa()
	{
		return this.pesquisa;
	}
}
