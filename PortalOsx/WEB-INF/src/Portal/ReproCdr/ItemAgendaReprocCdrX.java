package Portal.ReproCdr;

import java.util.Date;
import java.util.Vector;

import Portal.Utils.DataUtil;

/**
 * Visent Telecomunicações LTDA. Projeto: PortalOsx Arquivo criado em:
 * 15/08/2005
 *
 * @author Erick e Carlos Feijão
 * @version 1.0
 *
 * Item Agenda para reprocessamento do CdrX
 */
public class ItemAgendaReprocCdrX extends ItemAgenda {
	
	/** Lista de CentralItemAgn*/
	private Vector centraisItemAgenda;
	/** Data inicio do cdr que será reprocessado*/
	private Date periodoInicio;
	/** Data fim do cdr que será reprocessado*/
	private Date periodoFim;
	/** Lista de Regras*/
	private Vector regras;

	
	public ItemAgendaReprocCdrX(){
		
	}
	
	public String serializa(){
		   String serializacaoPai = super.serializa();
		   StringBuffer resultado = new StringBuffer();
		   resultado.append(serializacaoPai);
		   resultado.append(";");
		   resultado.append(DataUtil.formataData(periodoInicio,"yyyyMMddHHmmss"));
		   resultado.append(";");
		   resultado.append(DataUtil.formataData(periodoFim,"yyyyMMddHHmmss"));
		   resultado.append(";");
		   resultado.append(SerializaUtil.serializaVector(centraisItemAgenda,"CentralItemAgn"));
		   resultado.append(";");
		   resultado.append(SerializaUtil.serializaVector(regras,"RegraItemAgn"));
		   
		   return resultado.toString();
	}
	
	public boolean desserializa(StringBuffer objetoSerializado){
		super.desserializa(objetoSerializado);
		
		periodoInicio = new Date(DataUtil.dataInMillis(objetoSerializado.substring(0,objetoSerializado.indexOf(";"))));
		objetoSerializado.delete(0,objetoSerializado.indexOf(";")+1);	
		
		periodoFim = new Date(DataUtil.dataInMillis(objetoSerializado.substring(0,objetoSerializado.indexOf(";"))));			
		objetoSerializado.delete(0,objetoSerializado.indexOf(";")+1);
		
		centraisItemAgenda = SerializaUtil.desserializaVector(objetoSerializado,"CentralItemAgn");
		objetoSerializado.delete(0,1);
		regras = SerializaUtil.desserializaVector(objetoSerializado,"RegraItemAgn");
		
		return true;
	}
	
	public Vector getCentrais(){
		return centraisItemAgenda;
	}
	public void setCentrais(Vector centraisItemAgenda){
		this.centraisItemAgenda = centraisItemAgenda;		
	}
	public Date getPeriodoFim() {
		return periodoFim;
	}
	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}
	public Date getPeriodoInicio() {
		return periodoInicio;
	}
	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}
	public Vector getRegras() {
		return regras;
	}
	public void setRegras(Vector regras) {
		this.regras = regras;
	}
	
	public static void main(String args[])
	{
		ItemAgendaReprocCdrX iax = new ItemAgendaReprocCdrX();
		StringBuffer sb = new StringBuffer();
		sb.append("1;20051206000000;1;1;20020101000000;20020101000000;{CentralItemAgn}[obj]CCC1MO;0;0;0;0;1;1;1;1;1[obj]CCCPA2;0;0;0;0;1;1;1;1;1[obj]CCCPA3;0;0;0;0;1;1;1;1;1{CentralItemAgn};{RegraItemAgn}[obj]Regra1[obj]Regra2{RegraItemAgn}");
		iax.desserializa(sb);
		
		System.out.println("Posicao: "+iax.getPosicao());
		System.out.println("Hora Execucao: "+iax.getDataHoraExecucao());
		System.out.println("PeriodoInicio: "+iax.getPeriodoInicio());
		System.out.println("PeriodoFim: "+iax.getPeriodoFim());
		System.out.println("StatusItemAgenda: "+iax.getStatusItemAgenda());
		System.out.println("TipoItemAgenda: "+iax.getTipoItemAgenda());
		
		Vector centrais = iax.getCentrais();
		for (int i = 0; i < centrais.size(); i++) 
		{
			CentralItemAgn cia = (CentralItemAgn)centrais.get(i);
			System.out.println(cia.getNome()+";"+cia.getStatus());			
		}
		
		Vector regras = iax.getRegras();
		for (int i = 0; i < regras.size(); i++) 
		{
			RegraItemAgn ria = (RegraItemAgn)regras.get(i);
			System.out.println(ria.getNome());			
		}
		
		System.out.println(iax.serializa());

	}
}
