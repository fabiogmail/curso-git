//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpBDExtraSalva.java

package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpBDDataReprocessamentoSalva extends OperacaoAbs 
{

	static 
	{
	}

	/**
	 * @return 
	 * @exception 
	 * @roseuid 3D19FE860367
	 */
	public OpBDDataReprocessamentoSalva() 
	{
	}

	/**
	 * @param p_Mensagem
	 * @return boolean
	 * @exception 
	 * @roseuid 3D19FE86037B
	 */
	public boolean iniciaOperacao(String p_Mensagem)
	{
		try
		{
			String Resp = "", Datas = null;

			OpBDDataReprocessamento bDDataReprocessamento = new OpBDDataReprocessamento();
			bDDataReprocessamento.setRequestResponse(getRequest(), getResponse());
			bDDataReprocessamento.setOperacao("bdDataReprocessamentoSalva");
			// Recupera a lista de datas
			Datas = m_Request.getParameter("novalistadatas");

			// Seta a lista de datas no ServUtil e analisa a resposta
			No noTmp = null;

			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
			{
				noTmp = (No) iter.next();
//				Resp += noTmp.getConexaoServUtil().setBDExtra(Datas);
				Resp += noTmp.getConexaoServUtil().setBDDataReprocessamento(mudaFormatoData(Datas));
				
			}

			if (Resp.length() == 0)
				Resp = "Datas de DDD-X salvas!";

			// Envia a página de datas novamente
			bDDataReprocessamento.iniciaOperacao(Resp);
		}
		catch (Exception Exc)
		{
			System.out.println("OpBDDataReprocessamentoSalva - iniciaOperacao():"+Exc);
			Exc.printStackTrace();
			return false;         
		}

		return true;
	}


		
	/**
	 * @param data
	 * @return lista de datas com o novo formato
	 * Método responsável por alterar o formato da data de dd/mm/aaaa para aaaammdd
	 */
	private String mudaFormatoData(String dataOriginal){
		
		if(dataOriginal.length() < 10)
			return dataOriginal;
		
		return mudar(dataOriginal.split(";"));
	}

	/**
	 * @param datas
	 * @return data no novo Formato aaaammdd
	 * Método que auxilia o método mudaFormatoData
	 */
	private String mudar(String []datas){

		String novaData = "";

		String dia = "";
		String mes = "";
		String ano = "";

		for (int i = 0; i < datas.length; i++) {
			if(datas[i] != null && datas[i].length() > 7){//se a string contém uma data inteira (dd/mm/aaaa)
				dia = obterDia(datas[i]);	
				mes = obterMes(datas[i]);				
				ano = obterAno(datas[i]);
				

				System.out.println("DATA QUEBRADA: "+dia+" "+mes+" "+ano);

				novaData += ano+mes+dia;

				if(i<datas.length - 1)
					novaData += ";";

			}
		}


		return novaData;
	}

	/**
	 * @param dataDia
	 * @return dia da data no formato dd/mm/aaaa
	 */
	private String obterDia(String dataDia){
		System.out.println("DATA DIA: "+dataDia);
		return dataDia.substring(0, 2);
	}

	/**
	 * @param dataMes
	 * @return mes da data no formato dd/mm/aaaa
	 */
	private String obterMes(String dataMes){
		return dataMes.substring(3, 5);
	}

	/**
	 * @param dataAno
	 * @return ano da data no formato dd/mm/aaaa
	 */
	private String obterAno(String dataAno){
		return dataAno.substring(6,10);
	}
}
