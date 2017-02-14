package br.com.visent.matraf.service.html;

import java.util.ArrayList;


public class TabHTML {
	
	private ArrayList relatorio;
	private StringBuffer colunas;
	private StringBuffer linhas;
	private StringBuffer titulo;
	
	public TabHTML(ArrayList relatorio) {
		this.relatorio = relatorio;	
		this.colunas = new StringBuffer();
		this.linhas = new StringBuffer();
		this.titulo = new StringBuffer();
	}
	
	public String criarTabela(){
		
		ArrayList recursos = (ArrayList)relatorio.get(0);
		ArrayList linhas = (ArrayList)relatorio.get(1);

		for(int i = 0; i < recursos.size(); i++)
		{
			String[] recurso = ((String)recursos.get(i)).split(";");
			addColuna(i,recurso[0],recurso[1],"0","#FEF0B3",true);
		}
		int numRec = recursos.size();
		int numCol = numRec+6;
		
		
		addColuna(numRec  ,"Cham","50","0","#",true);
		addColuna(numRec+1,"Tráfego","50","0","#",true);
		addColuna(numRec+2,"TMC","50","0","#",true);
		addColuna(numRec+3,"TMO","50","0","#",true);
		addColuna(numRec+4,"OK","50","0","#",true);
		addColuna(numRec+5,"OK%","50","0","#",true);
		
		String[] align = new String[numCol];
		String[] cores = new String[numCol];
		
		for(int i = 0; i < numCol; i++){
			if(i < numRec){
				align[i] = "left";
			}else{
				align[i] = "right";
			}
			cores[i] = "#";
		}
		
		
		for (int i = 0; i < linhas.size(); i++) {
			String strLinha = (String)linhas.get(i);
			String[] valores = strLinha.split(";");
			addLinha(i,valores,cores,align);
		}
		
		
		return montarHTML();
	}
	/**
	 * 
	 * @param isCentral
	 * @return
	 */
	public String criarTabelaResumida(boolean isCentral){
		
		ArrayList recursos = (ArrayList)relatorio.get(0);
		ArrayList linhas = (ArrayList)relatorio.get(1);
		if(isCentral)
			addTituloCentral((String)relatorio.get(2),(String)relatorio.get(3));
		else
			addTitulo((String)relatorio.get(2),(String)relatorio.get(3));
		

		String[] recurso = ((String)recursos.get(1)).split(";");
		addColuna(0,recurso[0],"30","0","#FEF0B3",false);
		addColuna(1,"Cham","30","0","#",false);
		addColuna(2,"Tráfego","30","0","#",false);
		addColuna(3,"OK%","30","0","#",false);
		int numRec = 1;
		int numCol = 4;
		
		String[] align = new String[numCol];
		String[] cores = new String[numCol];
		
		for(int i = 0; i < numCol; i++){
			if(i < numRec){
				align[i] = "left";
			}else{
				align[i] = "right";
			}
			cores[i] = "#";
		}
		
		
		for (int i = 0; i < linhas.size(); i++) {
			String strLinha = (String)linhas.get(i);
			String[] linha = strLinha.split(";");
			String[] valores = {linha[1],linha[2],linha[3],linha[7]};
			addLinha(i,valores,cores,align);
		}
		
		
		return montarHTML();
	}
	
	
	
	
	/**
	 * Método que adiciona uma Coluna
	 * @param indice , posição da coluna
	 * @param nome , nome para interface
	 * @param tamanho , tamanho da coluna
	 * @param colspan , formatação span
	 * @param bgcolor , cor do background
	 * @param ordenacao , têm ou não ordenação
	 */
	private void addColuna(int indice, String nome, String tamanho, String colspan,
							String bgcolor, boolean ordenacao){
		String color = "";
		if(!bgcolor.equals("#")){
			color = "bgcolor="+bgcolor;
		}
		this.colunas.append("<th width="+tamanho+" "+color+" >");
		if(ordenacao){
			this.colunas.append("<a href=# onClick=tabela.ordenar("+indice+")>"+nome+"</a>");
		}else{
			this.colunas.append(nome);
		}
		this.colunas.append("</th>");

	}
	
	/**
	 * Método para adicionar uma linha
	 * @param indice , numero da linha
	 * @param valores , valores das colunas
	 * @param cores , background das colunas
	 * @param align , alinhamentos
	 */
	private void addLinha(int indice, String[] valores, String[] cores, String[] align){
		this.linhas.append("<tr>");
		for (int j = 0; j < valores.length; j++) {
			this.linhas.append("<td "+cores[j]+" align="+align[j]+" >"+valores[j]+"</td>");
		}
		this.linhas.append("</tr>");
	}
	
	/**
	 * Método para adicionar um título a tabela
	 * @param titulo , titulo da tabela
	 */
	private void addTitulo(String titulo, String id){
		this.titulo.append("<table><tr><td class=\"tabela\">");
		this.titulo.append("<a href=\"#\" " +
						   "onClick=\"processarRes('"+id+"'," +
											       "'"+(String)relatorio.get(4)+"'," +
											       "'"+(String)relatorio.get(2)+"'" +
											       ");\">");

		this.titulo.append("<b>"+titulo+"</b>");
		this.titulo.append("</a></td></tr></table>");
	}
	
	/**
	 * Método para adicionar um título a tabela
	 * @param titulo , titulo da tabela
	 */
	private void addTituloCentral(String titulo, String id){
		this.titulo.append("<table><tr><td class=\"tabela\">");
		this.titulo.append("<a href=\"#\" " +
						   "onClick=\"processarRelatorio('"+id+"'," +
											       "'"+(String)relatorio.get(4)+"'," +
											       "'"+(String)relatorio.get(2)+"'" +
											       ");\">");

		this.titulo.append("<b>"+titulo+"</b>");
		this.titulo.append("</a></td></tr></table>");
	}
	
	/**
	 * Classe para retornar a tabela construída
	 * @return tabela
	 */
	private String montarHTML(){
		if(linhas.equals("")){
			return "Dados não encontrados";
		}
		StringBuffer table = new StringBuffer();
		table.append(this.titulo.toString());
		table.append("<table border=\"1\" class=\"classlinhas\" >");
		table.append("<thead id=\"colunas\">");
		table.append(this.colunas);
		table.append("</thead>");
		table.append("<tbody class=\"tabela\" id=\"linhas\" >");
		table.append(this.linhas.toString());
		table.append("</tbody>");
		table.append("</table>");
		table.append("<table><tr><td>&nbsp;</td></tr></table>");
		return table.toString();
	}

}
