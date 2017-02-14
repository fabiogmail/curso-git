package Portal.ReproCdr;

import java.util.Vector;


public class ItemAgendaCdrBruto extends ItemAgenda {
	
	/** Lista de CentralItemAgn*/
	private Vector centraisItemAgenda;
	
	public String serializa()
	{
		String serializacaoPai = super.serializa();
		StringBuffer resultado = new StringBuffer();
		resultado.append(serializacaoPai);
		resultado.append(";");
	    resultado.append(SerializaUtil.serializaVector(centraisItemAgenda,"CentralItemAgn"));
	    return resultado.toString();
	}
	
	public boolean desserializa(StringBuffer objetoSerializado)
	{
		super.desserializa(objetoSerializado);
			
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
	
	public static void main(String args[])
	{
		ItemAgendaCdrBruto iabr = new ItemAgendaCdrBruto();
		StringBuffer sb = new StringBuffer();
		sb.append("1;20050817171459;1;0;{CentralItemAgn}[obj]PSA;0;0;0;0;0;0;0;0;0[obj]BOT;0;0;0;0;0;0;0;0;0[obj]TESTE;0;0;0;0;0;0;0;0;0[obj]TESTE2;0;0;0;0;0;0;0;0;0{CentralItemAgn}");
		iabr.desserializa(sb);
		
		System.out.println("Posicao: "+iabr.getPosicao());
		System.out.println("Hora Execucao: "+iabr.getDataHoraExecucao());
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
