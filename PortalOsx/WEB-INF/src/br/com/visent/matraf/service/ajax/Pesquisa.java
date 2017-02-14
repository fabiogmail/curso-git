package br.com.visent.matraf.service.ajax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import br.com.visent.matraf.action.CentralRefAction;
import br.com.visent.matraf.action.LinhaRelatorioAction;
import br.com.visent.matraf.action.PeriodoAction;
import br.com.visent.matraf.domain.Central;
import br.com.visent.matraf.domain.CentralRef;
import br.com.visent.matraf.domain.GrupoCelula;
import br.com.visent.matraf.domain.LinhaRelatorio;
import br.com.visent.matraf.domain.Operadora;
import br.com.visent.matraf.domain.Periodo;
import br.com.visent.matraf.domain.RelacaoTrafego;
import br.com.visent.matraf.service.html.TabHTML;
import br.com.visent.matraf.service.util.ChaveComparator;
import br.com.visent.matraf.service.util.Tabela;
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
public class Pesquisa {
	
	/** Paginação */
	private int paginacao = 20;
	private int numPaginas;
	private int paginaAtual = 1;
	
	/** Central de referência */
	private CentralRef centralRef;
	
	/** Local, Origem ou Destino */
	private boolean origem;
	private String referencialStr;
	private String referencial;
	
	/** Entrada ou Saída*/
	private String rotaStr;
	
	/** Tipo selecionado*/
	private String tipo;
	
	/** Periodo para execução da pesquisa */
	private Periodo periodo;
	/** Data da tabela*/
	private String dataStr;
	
	/** Lista Dinâmica de Recursos */
	private ArrayList recursos;
	/** Lista Fixa de Recursos */
	private String[] listaRecursos;
	
	/** campo para pesquisa */
	private String campoBilhetador;
	private int idBilhetador;
	private String nomeBilhetador;
	private String valorRelTraf;

	/** Tabela */
	private Tabela tabela;
	
	/** TreeMap do resumo */
	private TreeMap resumoMap;

	
	public Pesquisa() {}
	
	/**
	 * Método para criar o relatório detalhado
	 * @param idCentralRef , ID da central de referência
	 * @param referencial , 0 = Origem, 1 = Destino
	 * @param tipo , Se é central,bsc,celula,operadora e etc...
	 * @param idBilhetador , ID referente ao tipo
	 * @param idPeriodo , ID do período
	 * @param recursos , Lista de recursos selecionados
	 */
	public boolean executar(int idCentralRef, 
							String referencial, 
							String tipo, 
							int idBilhetador, 
							String dataStr,
							int idPeriodo, 
							String[] recursos){
		
		novo(idCentralRef,referencial,tipo,idBilhetador,dataStr,idPeriodo,recursos);
		if(recursos.length > 0){
			return listar();
		}else{
			this.recursos = new ArrayList();
			for (int i = 0; i < LinhaRelatorio.RECURSOS.length; i++) {
				this.recursos.add(LinhaRelatorio.RECURSOS[i]);
			}
			return listar();
		}
	}
	
	/**
	 * Método para criar o relatório detalhado
	 * @param idCentralRef , ID da central de referência
	 * @param referencial , 0 = Origem, 1 = Destino
	 * @param tipo , Se é central,bsc,celula,operadora e etc...
	 * @param idBilhetador , ID referente ao tipo
	 * @param idPeriodo , ID do período
	 * @param recursos , Lista de recursos selecionados
	 */
	public boolean executarResumo(int idCentralRef, 
								  String referencial, 
								  String tipo, 
								  int idBilhetador,
								  String dataStr,
								  int idPeriodo, 
								  String[] recursos){
		
		novo(idCentralRef,referencial,tipo,idBilhetador,dataStr,idPeriodo,recursos);
		return resumo();
	}
	
	/**
	 * @param idCentralRef , ID da central de referência
	 * @param referencial , 0 = Origem, 1 = Destino
	 * @param idBilhetador , ID referente ao tipo
	 * @param idPeriodo , ID do período
	 * @param recursos , Lista de recursos selecionados
	 * Método que cria um resumo geral para todos os tipos: central, bsc, celula, operadora
	 * 
	 * @return
	 */
	public boolean executarResumoGeral(int idCentralRef, 
			  						   String referencial, 
			  						   int idBilhetador,
			  						   String dataStr,
			  						   int idPeriodo, 
			  						   String[] recursos)
	{
		novoGeral(idCentralRef, referencial, idBilhetador, dataStr, idPeriodo, recursos);
		return resumoGeral();
	}
	
	/**
	 * 
	 * @param idCentralRef
	 * @param id
	 * @param referencial
	 * @param dataStr
	 * @param idPeriodo
	 * @return
	 */
	public boolean executarResumoCentral(int idCentralRef, 
										 String referencial,
										 String idRelTraf,										 
										 String dataStr,
			                             int idPeriodo,
			                             String[] recursos)
	{
		novoCentral(idCentralRef, referencial, idRelTraf, dataStr, idPeriodo, recursos);
		return resumoCentral();
	}
	
	/**
	 * Método que cria uma nova geral pra todos os tipos: central, bsc, celula, operadora
	 * @param idCentralRef , ID da central de referência
	 * @param referencial , 0 = Origem, 1 = Destino
	 * @param idBilhetador , ID referente ao tipo
	 * @param idPeriodo , ID do período
	 * @param recursos , Lista de recursos selecionados
	 * 
	 */
	private void novoGeral(int idCentralRef, String referencial, int idBilhetador, 
					String dataStr, int idPeriodo, String[] recursos){
		
		this.idBilhetador = idBilhetador;
		this.listaRecursos = recursos;
		//this.referencial = referencial;
		CentralRefAction centralRefAction = new CentralRefAction();
		PeriodoAction periodoAction = new PeriodoAction();
		this.centralRef = centralRefAction.buscarById(idCentralRef);
		this.periodo = periodoAction.buscarById(idPeriodo);
		this.dataStr = dataStr;
		
//		if(referencial.equalsIgnoreCase("origem")){
//			this.origem = true;
//			this.referencialStr = "Origem";
//			this.rotaStr = "Saída";
//		}else{
//			this.origem = false;
//			this.referencialStr = "Destino";
//			this.rotaStr = "Entrada";
//		}		
		
		setRecursos(recursos);
		
	}
	
	private void novoCentral(int idCentralRef,
			                    String referencial,
			                    String idRelTraf,
			                    String dataStr,
			                    int idPeriodo,
			                    String[] recursos)
	{
//		this.idBilhetador = idBilhetador;
		this.listaRecursos = recursos;
		this.referencial = referencial;
		CentralRefAction centralRefAction = new CentralRefAction();
		PeriodoAction periodoAction = new PeriodoAction();
		
		this.centralRef = centralRefAction.buscarById(idCentralRef);
		this.periodo = periodoAction.buscarById(idPeriodo);
		this.dataStr = dataStr;
		this.valorRelTraf = idRelTraf;
		
		if(referencial.equalsIgnoreCase("origem")){
			this.origem = true;
			this.referencialStr = "Origem";
			this.rotaStr = "Saída";
		}else{
			this.origem = false;
			this.referencialStr = "Destino";
			this.rotaStr = "Entrada";
		}		

		if(this.valorRelTraf.equalsIgnoreCase("A") || this.valorRelTraf.equalsIgnoreCase("1"))
		{
			this.campoBilhetador = LinhaRelatorio.C_OPERADORA;					
		}
		else if(this.valorRelTraf.equalsIgnoreCase("B") || this.valorRelTraf.equalsIgnoreCase("2"))
		{
			this.campoBilhetador = LinhaRelatorio.C_CENTRAL;	
		}
		else if(this.valorRelTraf.equalsIgnoreCase("C") || this.valorRelTraf.equalsIgnoreCase("3"))
		{
			this.campoBilhetador = LinhaRelatorio.C_GRUPO_CELULA;
		}
		
		setRecursos(recursos);
	}
	
	
	/**
	 * Método que cria uma nova pesquisa nesse objeto
	 * @param idCentralRef , ID da central de referência
	 * @param referencial , 0 = Origem, 1 = Destino
	 * @param tipo , Se é central,bsc,celula,operadora e etc...
	 * @param idBilhetador , ID referente ao tipo
	 * @param idPeriodo , ID do período
	 * @param recursos , Lista de recursos selecionados
	 */
	private void novo(int idCentralRef, String referencial, String tipo, int idBilhetador, 
					String dataStr, int idPeriodo, String[] recursos){
		
		this.idBilhetador = idBilhetador;
		this.listaRecursos = recursos;
		this.referencial = referencial;
		CentralRefAction centralRefAction = new CentralRefAction();
		PeriodoAction periodoAction = new PeriodoAction();
		this.centralRef = centralRefAction.buscarById(idCentralRef);
		this.periodo = periodoAction.buscarById(idPeriodo);
		this.tipo = tipo;
		this.dataStr = dataStr;
		
		if(referencial.equalsIgnoreCase("origem")){
			this.origem = true;
			this.referencialStr = "Origem";
			this.rotaStr = "Saída";
		}else{
			this.origem = false;
			this.referencialStr = "Destino";
			this.rotaStr = "Entrada";
		}

		this.nomeBilhetador = tipo;
		setRecursos(recursos);
		
	}
	
	/**
	 * Buscando relatório na action 
	 */
	private boolean listar(){
		LinhaRelatorioAction action = new LinhaRelatorioAction();
		ArrayList resultado = action.listar(this);
		this.numPaginas = calculaPaginas(resultado.size());
		tabela = new Tabela(resultado, recursos, null, ehOrigem());
		return (resultado.size() > 0);
	}
	
	/**
	 * Buscando relatório resumido na action 
	 */
	private boolean resumo(){
		LinhaRelatorioAction action = new LinhaRelatorioAction();
		ArrayList resultado = action.resumo(this);
		TreeMap map = new TreeMap();
		for (int i = 0; i < resultado.size(); i++) {
			Object[] objeto = (Object[])resultado.get(i);
			String nome = "";
			int id = 0;
			if(this.campoBilhetador == LinhaRelatorio.C_CENTRAL){
				Central bilhetador = (Central)objeto[5];
				nome = bilhetador.getNome();
				id = bilhetador.getId();
			}else if(this.campoBilhetador == LinhaRelatorio.C_OPERADORA){
				Operadora bilhetador = (Operadora)objeto[6];
				if(bilhetador.getDescricao() != null){
					nome = bilhetador.getDescricao();
				}else{
					nome = bilhetador.getNome();
				}
				id = bilhetador.getId();
			}else if(this.campoBilhetador == LinhaRelatorio.C_GRUPO_CELULA){
				GrupoCelula bilhetador = (GrupoCelula)objeto[7];
				nome = bilhetador.getNome();
				id = bilhetador.getId();
			}
			String chave = id+";"+nome;
			
			if(map.containsKey(chave)){
				ArrayList obLista = (ArrayList)map.get(chave);
				obLista.add(objeto);
				map.put(chave,obLista);
			}else{
				ArrayList obLista = new ArrayList();
				obLista.add(objeto);
				map.put(chave,obLista);
			}
		}
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			ArrayList obLista = (ArrayList)map.get(key);
			Tabela tabela = new Tabela(obLista, recursos, null, ehOrigem());
			map.put(key,tabela);
		}
		this.resumoMap = map;
		
		return (resultado.size() > 0);
	}
	
	/**
	 * Buscando relatório resumido na action 
	 */

	private boolean resumoGeral(){
		LinhaRelatorioAction action = new LinhaRelatorioAction();
		ArrayList resultado = action.resumoGeral(this);
		TreeMap map = new TreeMap();
		TreeMap mapOrigem = new TreeMap();//map que para cada key diz se é originada true ou false
		//boolean flag;
		for (int i = 0; i < resultado.size(); i++) {
			Object[] objeto = (Object[])resultado.get(i);
			String nome = "";
			String id = "";
			
			RelacaoTrafego relTraf = (RelacaoTrafego)objeto[9];

			//agora o banco busca tudo e aqui nós vemos o que é origem
			//
			
			LinhaRelatorio linhaRel = (LinhaRelatorio)objeto[1];
			
			if(linhaRel.getReferencial()==0)
			{
				this.origem = true;
				this.referencial = "origem";
			}
			else
			{
				this.origem = false;
				this.referencial = "destino";
			}
			
			
			if(this.origem)
			{
				id = relTraf.getNome().substring(0,1);
				nome = relTraf.getOrigem();
			}
			else
			{
				id = relTraf.getNome().substring(1,2);
				nome = relTraf.getDestino();
			}
			
			String chave = id+";"+nome;
			
			//os dois maps contem as mesmas chaves, eles são complementares
			if(map.containsKey(chave)){
				ArrayList obLista = (ArrayList)map.get(chave);
				obLista.add(objeto);
				map.put(chave,obLista);
				mapOrigem.put(chave, new Boolean(this.origem));
			}else{
				ArrayList obLista = new ArrayList();
				obLista.add(objeto);
				map.put(chave,obLista);
				mapOrigem.put(chave, new Boolean(this.origem));
			}
		}
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			ArrayList obLista = (ArrayList)map.get(key);
			Boolean isOrigem = (Boolean)mapOrigem.get(key);
			Tabela tabela = new Tabela(obLista, recursos, null, isOrigem.booleanValue());
			map.put(key,tabela);
		}
		this.resumoMap = map;
		
		return (resultado.size() > 0);
	}
	
	/**
	 * Buscando relatório resumido central na action 
	 */
	private boolean resumoCentral(){
		LinhaRelatorioAction action = new LinhaRelatorioAction();
		ArrayList resultado = action.resumoCentral(this);
		TreeMap map = new TreeMap(new ChaveComparator());
		for (int i = 0; i < resultado.size(); i++) {
			Object[] objeto = (Object[])resultado.get(i);
			String nome = "";
			int id = 0;
			if(this.campoBilhetador == LinhaRelatorio.C_CENTRAL){
				Central bilhetador = (Central)objeto[5];
				nome = bilhetador.getNome();
				id = bilhetador.getId();
			}else if(this.campoBilhetador == LinhaRelatorio.C_OPERADORA){
				Operadora bilhetador = (Operadora)objeto[6];
				if(bilhetador.getDescricao() != null){
					nome = bilhetador.getDescricao();
				}else{
					nome = bilhetador.getNome();
				}
				id = bilhetador.getId();
			}else if(this.campoBilhetador == LinhaRelatorio.C_GRUPO_CELULA){
				GrupoCelula bilhetador = (GrupoCelula)objeto[7];
				nome = bilhetador.getNome();
				id = bilhetador.getId();
			}
			String chave = id+";"+nome;
			
			if(map.containsKey(chave)){
				ArrayList obLista = (ArrayList)map.get(chave);
				obLista.add(objeto);
				map.put(chave,obLista);
			}else{
				ArrayList obLista = new ArrayList();
				obLista.add(objeto);
				map.put(chave,obLista);
			}
		}
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			ArrayList obLista = (ArrayList)map.get(key);
			Tabela tabela = new Tabela(obLista, recursos, null, ehOrigem());
			map.put(key,tabela);
		}
		this.resumoMap = map;
		
		return (resultado.size() > 0);
	}
	
	
	/**
	 * 
	 * @param pagina
	 * @param tabela
	 * @param bilhetador
	 * @param idBilhetador
	 * @return
	 */
	private ArrayList getPagina(int pagina, Tabela tabela, String bilhetador,String idBilhetador){
		this.paginaAtual = pagina;
		ArrayList linhas = tabela.getLinhas();
		ArrayList resultadoPag = new ArrayList();
		resultadoPag.add(montarRecursos());

		// Defininco o inicio e o fim da pagina
		int inicio = 0;
		int fim = linhas.size();
		if(pagina != 0){
			inicio = (pagina-1)*paginacao;
			fim = inicio+paginacao;
			if(fim > linhas.size()){
				fim = linhas.size();
			}
		}

		// montando as linhas da pagina selecionada
		ArrayList linhasPag = new ArrayList();
		for (int i = inicio; i < fim; i++) {
			linhasPag.add(linhas.get(i));	
		}
		
		// convertendo arraylist para String separada por ;
		ArrayList relatorio = new ArrayList();
		for (int i = 0; i < linhasPag.size(); i++) {
			ArrayList linha = (ArrayList)linhasPag.get(i);
			String line = "";
			for (int j = 0; j < recursos.size(); j++) {
				line += linha.get(j)+";";
			}
			for (int j = recursos.size(); j < linha.size(); j++) {
				Float number = (Float)linha.get(j);
				line += Indicadores.nFormat.format(number)+";";
			}
			relatorio.add(line);
		}
		
		resultadoPag.add(relatorio);
		resultadoPag.add(bilhetador);
		resultadoPag.add(idBilhetador);
		//resultadoPag.add(this.referencial);
		if(tabela.getOrigem())
			resultadoPag.add("origem");
		else
			resultadoPag.add("destino");
		
		return resultadoPag;
	}
	
	/**
	 * Método para calcular o número de páginas para a paginação
	 * @param qnt de linhas
	 * @return numero de paginas de acordo com a PAGINACAO
	 */ 
	private int calculaPaginas(int qnt){
		if(qnt%paginacao == 0){
			return qnt/paginacao;
		}else{
			return (qnt/paginacao)+1;
		}
	}
	
	/**
	 * Classe que monta os recursos selecionados
	 * @return lista de recursos para interface
	 */
	private ArrayList montarRecursos(){
		
		ArrayList resultado = new ArrayList();
		
		for (int i = 0; i < recursos.size(); i++) 
		{
			String recurso = (String)recursos.get(i);
			
			if(recurso.equalsIgnoreCase(LinhaRelatorio.C_RELACAO_TRAFEGO))
			{
				resultado.add("Relação de Tráfego;180");
			}
			else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_TIPO_ASSINANTE))
			{
				resultado.add("Tipo de Assinante;180");
			}
			else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_ROTAS))
			{
				resultado.add("Rota de Saída;70");
			}
			else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_ROTAE))
			{
				resultado.add("Rota de Entrada;70");
			}
			else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_ORIGEM_DESTINO))
			{
				if(ehOrigem())
				{ 
					resultado.add("Destino;70");
				}
				else
				{ 
					resultado.add("Origem;70");
				}
			}else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_CELULAE))
			{
				resultado.add("Celula de Entrada;80");
			}
			else if(recurso.equalsIgnoreCase(LinhaRelatorio.C_CELULAS))
			{
				resultado.add("Celula de Saída;80");
			}
			
		}
		
		return resultado;
	}
	
	public String getRelatorio(int pagina, int[] indices, boolean reversa){
		tabela.ordenar(indices, reversa);
		ArrayList resultado = this.getPagina(pagina,tabela,this.nomeBilhetador,this.idBilhetador+"");
		TabHTML tab = new TabHTML(resultado);
		return tab.criarTabela();
	}
	public String getRelatorioOrdenado(int pagina){
		ArrayList resultado = this.getPagina(pagina,tabela,this.nomeBilhetador,this.idBilhetador+"");
		TabHTML tab = new TabHTML(resultado);
		return tab.criarTabela();
	}
	
	public String[] getResumo(int[] indices, boolean reversa){
		StringBuffer resumoOrigem = new StringBuffer();
		StringBuffer resumoDestino = new StringBuffer();
		String[] resumo = new String[2];//posição 0 - origem, posição 1 - destino
		for (Iterator iter = resumoMap.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			Tabela tabela = (Tabela)resumoMap.get(key);
			tabela.ordenar(indices, reversa);
			String[] chave = key.split(";");
			ArrayList resultado = getPagina(0,tabela,chave[1],chave[0]);
			TabHTML tab = new TabHTML(resultado);
			if(tabela.getOrigem())
				resumoOrigem.append(tab.criarTabelaResumida(false));//false porque não é central
			else
				resumoDestino.append(tab.criarTabelaResumida(false));//false porque não é central
		}
		resumo[0] = resumoOrigem.toString();
		resumo[1] = resumoDestino.toString();
		return resumo;
	}
		
	public String getResumoCentral(int[] indices, boolean reversa){
		StringBuffer resumo = new StringBuffer();
		for (Iterator iter = resumoMap.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			Tabela tabela = (Tabela)resumoMap.get(key);
			tabela.ordenar(indices, reversa);
			String[] chave = key.split(";");
			ArrayList resultado = getPagina(0,tabela,chave[1],chave[0]);
			TabHTML tab = new TabHTML(resultado);
			resumo.append(tab.criarTabelaResumida(true));//true porque é o resumo central
		}
		return resumo.toString();
	}
	
	public void setRecursos(String[] recursos){
		this.recursos = new ArrayList();
		for (int i = 0; i < recursos.length; i++) {
			if(recursos[i].equalsIgnoreCase("relacao_trafego")){
				this.recursos.add(LinhaRelatorio.C_RELACAO_TRAFEGO);
			}else if(recursos[i].equalsIgnoreCase("tipo_assinante")){
				this.recursos.add(LinhaRelatorio.C_TIPO_ASSINANTE);
			}else if(recursos[i].equalsIgnoreCase("rotas")){
				this.recursos.add(LinhaRelatorio.C_ROTAS);
			}else if(recursos[i].equalsIgnoreCase("rotae")){
				this.recursos.add(LinhaRelatorio.C_ROTAE);
			}else if(recursos[i].equalsIgnoreCase("origem_destino")){
				this.recursos.add(LinhaRelatorio.C_ORIGEM_DESTINO);
			}else if(recursos[i].equalsIgnoreCase("celulae")){
				this.recursos.add(LinhaRelatorio.C_CELULAE);
			}else if(recursos[i].equalsIgnoreCase("celulas")){
				this.recursos.add(LinhaRelatorio.C_CELULAS);
			}
		}		
	}
	
	public String atualizar(String[] recursos,int pagina){
		setRecursos(recursos);
		this.listar();
		return getRelatorioOrdenado(pagina);
	}
	
	public String[] getListaRecursos(){
		return this.listaRecursos;
	}
	
	/**
	 * Saber se a chamada eh originada ou destinada
	 * @return true é Origem
	 */
	public boolean ehOrigem(){
		if(this.origem){
			return true;
		}else{
			return false;
		}
	}
	
	public int getNumPaginas(){
		return numPaginas;
	}
	public int getPaginacao(){
		return paginacao;
	}
	public void setPaginacao(int paginacao){
		this.paginacao = paginacao;
	}
	public String getValorRelTraf() {
		return valorRelTraf;
	}
	public Tabela getTabela(){
		return tabela;
	}
	public ArrayList getRecursos() {
		return recursos;
	}
	public int getIdBilhetador() {
		return idBilhetador;
	}
	public String getNomeBilhetador() {
		return nomeBilhetador;
	}
	public String getCampoBilhetador() {
		return campoBilhetador;
	}
	public CentralRef getCentralRef() {
		return centralRef;
	}
	public String getDataStr(){
		return this.dataStr;
	}
	public Periodo getPeriodo() {
		return periodo;
	}
	
	public String getTipo() {
		return tipo;
	}

}
