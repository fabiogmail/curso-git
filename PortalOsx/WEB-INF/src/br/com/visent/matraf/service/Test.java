/*
 * OSx Telecom
 * Criado em 15/09/2005
 * 
 */
package br.com.visent.matraf.service;


/**
 * @author Marcos Urata 
 * 
 */
public class Test {

    public static void main(String[] args)
    {
    	/*int[] indices = {0,1,2};
		List lista = new ArrayList();
		lista.add(new Float(12.2));
		lista.add(new Float(9.1));
		lista.add(new Float(0.3));

		//ordenando
		Collections.sort(lista);
		
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i));
		}*/
		
		
    	/*Valor valor1 = new Valor("c",(float)12.2);
    	Valor valor2 = new Valor("b",(float)9.1);
    	Valor valor3 = new Valor("a",(float)0.3);
    	List lista = new ArrayList();	
    	lista.add(valor1);
    	lista.add(valor3);
    	lista.add(valor2);
    	Collections.sort(lista);
    	Collections.reverse(lista);
    	for (int i = 0; i < lista.size(); i++) {
    		Valor v = (Valor)lista.get(i);
			System.out.println(v.getNome()+" - "+v.getNumero());
		}*/
    	
    	int n = 0;
    	int p = 50;
    	int r = 0;
    	if((n%p) == 0){
    		r = (n/p);
    	}else{
    		r = (n/p)+1;
    	}
    	System.out.println(r);
    }
}

class Valor implements Comparable{

	private String nome;
	private float numero;
	
	public Valor(String nome, float numero){
		this.nome = nome;
		this.numero = numero;
	}
	
	public int compareTo(Object o) {
		Valor ob = (Valor)o;
		if(this.numero > ob.getNumero()){
			return 1;
		}else if(this.numero < ob.getNumero()){
			return -1;
		}else{
			return 0;
		}
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public float getNumero() {
		return numero;
	}
	public void setNumero(float numero) {
		this.numero = numero;
	}
	
}
