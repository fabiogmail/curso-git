function tabela(idTabela){
	this.idTabela = idTabela;
	this.ordenar = ordenar;
	this.indices = new Array();
}

function ordenar(indice){
	$(this.idTabela).innerHTML = "Criando a tabela...";
	
	if(this.indices[indice] == undefined){
		this.indices[indice] = true;
	}
	
	if(this.indices[indice]){
		this.indices[indice] = false;
	}else{
		this.indices[indice] = true;
	}
	
	Pesquisa.getRelatorio(resultado,1,[indice],this.indices[indice]);
}