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
public class ItemAgendaReprocCdrBruto extends ItemAgenda {
	
	/** Lista de CentralItemAgn*/
	private Vector centraisItemAgenda;
	/** data inicio do cdr que será reprocessado*/
	private Date periodoInicio;
	/** data fim do cdr que será reprocessado*/
	private Date periodoFim;
	
	
	public ItemAgendaReprocCdrBruto()
	{
		
	}
	
	public String serializa()
	{
		String serializacaoPai = super.serializa();
		StringBuffer resultado = new StringBuffer();
		resultado.append(serializacaoPai);
		resultado.append(";");
		resultado.append(DataUtil.formataData(periodoInicio,"yyyyMMddHHmmss"));
		resultado.append(";");
		resultado.append(DataUtil.formataData(periodoFim,"yyyyMMddHHmmss"));
	    resultado.append(";");
	    resultado.append(SerializaUtil.serializaVector(centraisItemAgenda,"CentralItemAgn"));
		   
	    return resultado.toString();
	}
	
	public boolean desserializa(StringBuffer objetoSerializado)
	{
		super.desserializa(objetoSerializado);
		
		periodoInicio = new Date(DataUtil.dataInMillis(objetoSerializado.substring(0,objetoSerializado.indexOf(";"))));
		objetoSerializado.delete(0,objetoSerializado.indexOf(";")+1);
			
		periodoFim = new Date(DataUtil.dataInMillis(objetoSerializado.substring(0,objetoSerializado.indexOf(";"))));
		objetoSerializado.delete(0,objetoSerializado.indexOf(";")+1);
			
		centraisItemAgenda = SerializaUtil.desserializaVector(objetoSerializado,"CentralItemAgn");   
		return true;
	}
	
	public Vector getCentrais()
	{
		return centraisItemAgenda;
	}
	public void setCentrais(Vector centraisItemAgenda)
	{
		this.centraisItemAgenda = centraisItemAgenda;
	}
	public Date getPeriodoFim()
	{
		return periodoFim;
	}
	public void setPeriodoFim(Date periodoFim)
	{
		this.periodoFim = periodoFim;
	}
	public Date getPeriodoInicio()
	{
		return periodoInicio;
	}
	public void setPeriodoInicio(Date periodoInicio)
	{
		this.periodoInicio = periodoInicio;
	}
	
	public static void main(String args[])
	{
		ItemAgendaReprocCdrBruto iabr = new ItemAgendaReprocCdrBruto();
		StringBuffer sb = new StringBuffer();
		sb.append("1;20050817171459;1;0;2;20050818171459;20050818171459;{CentralItemAgn}[obj]PSA;0[obj]POC;0{CentralItemAgn}");
		iabr.desserializa(sb);
		
		System.out.println("Posicao: "+iabr.getPosicao());
		System.out.println("Hora Execucao: "+iabr.getDataHoraExecucao());
		System.out.println("PeriodoInicio: "+iabr.getPeriodoInicio());
		System.out.println("PeriodoFim: "+iabr.getPeriodoFim());
		System.out.println("StatusItemAgenda: "+iabr.getStatusItemAgenda());
		System.out.println("TipoItemAgenda: "+iabr.getTipoItemAgenda());
		
		Vector centrais = iabr.getCentrais();
		for (int i = 0; i < centrais.size(); i++) 
		{
			CentralItemAgn cia = (CentralItemAgn)centrais.get(i);
			System.out.println(cia.getNome()+";"+cia.getStatus());			
		}
		
		System.out.println(iabr.serializa());

	}
}
