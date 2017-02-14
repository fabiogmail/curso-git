package Portal.ReproCdr;

import java.util.Date;

import Portal.Utils.DataUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 15/08/2005
 *
 * @author Erick e Carlos Feijão
 * @version 1.0
 *
 * Item da agenda usado no reprocessamento
 */
public class ItemAgenda implements Serializavel{
	
	/** Posicao do item cdr dentre a qnt permitida, funciona como ID*/
	private int posicao;
	/** Hora de execução deste item */
	private Date dataHoraExecucao;
	/** Tipo da agenda, seguindo Dominio TIPO */
	private int tipoItemAgenda;
	/** Status da agenda, seguinda Dominio STATUS*/
	private int statusItemAgenda;

	
	/** Constantes do STATUS da agenda */
	public final static int STATUS_ITEM_INATIVO = 0;
	public final static int STATUS_ITEM_AGENDADO = 1;
	public final static int STATUS_ITEM_EM_EXECUCAO = 2;
	public final static int STATUS_ITEM_PRONTO = 3;
	public final static int STATUS_ITEM_INCOMPLETO = 4;
	
	/** Constantes do TIPO da agenda */
	public final static int TIPO_ITEM_AGN = 0;
	public final static int TIPO_ITEM_AGN_REPROC_CDR_BRUTO = 1;
	public final static int TIPO_ITEM_AGN_REPROC_CDR_X = 2;
	public final static int TIPO_ITEM_CDR_BRUTO = 3;
	
	
	public ItemAgenda(){
		
	}
	
	public String serializa()
	{	
		return posicao + ";" + DataUtil.formataData(dataHoraExecucao,"yyyyMMddHHmmss") + ";" + tipoItemAgenda + ";" + statusItemAgenda ;
	}


	public boolean desserializa(StringBuffer objetoSerializado)
	{
		String[] atributos = objetoSerializado.toString().split(";");
	  
		posicao = Integer.parseInt(atributos[0]);
		objetoSerializado.delete(0,atributos[0].length()+1);
	   
		dataHoraExecucao = new Date(DataUtil.dataInMillis(atributos[1]));
		objetoSerializado.delete(0,atributos[1].length()+1);

		tipoItemAgenda = Integer.parseInt(atributos[2]);
		objetoSerializado.delete(0,atributos[2].length()+1);
	   
		statusItemAgenda = Integer.parseInt(atributos[3]);
		objetoSerializado.delete(0,atributos[3].length()+1);
	   
		return true;
	}
	
	public int getPosicao() {
		return posicao;
	}
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	public Date getDataHoraExecucao() {
		return dataHoraExecucao;
	}
	public void setDataHoraExecucao(Date dataHoraExecucao) {
		this.dataHoraExecucao = dataHoraExecucao;
	}
	public int getStatusItemAgenda() {
		return statusItemAgenda;
	}
	public void setStatusItemAgenda(int statusItemAgenda) {
		this.statusItemAgenda = statusItemAgenda;
	}
	public int getTipoItemAgenda() {
		return tipoItemAgenda;
	}
	public void setTipoItemAgenda(int tipoItemAgenda) {
		this.tipoItemAgenda = tipoItemAgenda;
	}

}
