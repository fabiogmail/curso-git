package Portal.Dto;

import java.util.ArrayList;
import java.util.Date;

/** Classe que será utilizada pelo objeto AgendaDTO para armazenar todas as execucoes de uma
 * agenda, inclusive as futuras.
 * @author osx
 *
 */
public class ExecucoesAgendaDTO {
	private int idExecucao;
	private Date data;
	private Date dataExecucao;
	private boolean executou;
	
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Date getDataExecucao() {
		return dataExecucao;
	}
	public void setDataExecucao(Date dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
	public boolean isExecutou() {
		return executou;
	}
	public void setExecutou(boolean executou) {
		this.executou = executou;
	}
	public int getIdExecucao() {
		return idExecucao;
	}
	public void setIdExecucao(int idExecucao) {
		this.idExecucao = idExecucao;
	}

}
