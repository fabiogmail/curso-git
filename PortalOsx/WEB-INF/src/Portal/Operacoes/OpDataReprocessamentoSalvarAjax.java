//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpDataReprocessamentoSalvarAjax.java

package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.ErroUtil;



/**
 * @author Bruno
 * 
 * Salva data inserida no formulário dataProcessamento.jsp no servidor
 *
 */
public class OpDataReprocessamentoSalvarAjax extends OperacaoAbs 
{
	
	private String data = "";
	
	static 
	{
	}

	/**
	 * @return 
	 * @exception 
	 * @roseuid 3D19FE860367
	 */
	public OpDataReprocessamentoSalvarAjax() 
	{
	}
	
	No noTmp = null;
	
	public String salvar(String data) throws ErroUtil {
		String resp = "";
		
		try {
			for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
			{
				noTmp = (No) iter.next();
				
				resp = noTmp.getConexaoServUtil().setDataReprocessamento(mudaFormatoData(data)) ;
			}
			
			return resp;			
						
			
		} catch (Exception e) {
			System.out.println("OpDataReprocessamento - salvar(): " + e);
			e.printStackTrace();
			return "Não foi possivel reprocessar a data informada ";
		}
	}
	
	private boolean verificaServUtil(No no) {
		if (no.getConexaoServUtil().getM_iUtil() == null) {
			no.createConexao(DefsComum.s_ServUtil, no.getConexaoServUtil()
					.getModoConexao(), no.getHostName(), no
					.getConexaoServUtil().getNomeObjetoCorba(), no
					.getConexaoServUtil().getPorta());
			if (no.getConexaoServUtil().getM_iUtil() == null) {
				return false;
			} else {
				no.setConexaoServUtil(no.getConexaoServUtil());
				return true;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * @param data
	 * @return lista de datas com o novo formato: aaaammdd
	 * Método responsável por alterar o formato da data de dd/mm/aaaa para aaaammdd
	 */
	private String mudaFormatoData(String dataOriginal){
		

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
			//System.out.println("TAMANHO DATA: "+datas[i].length());
			if(datas[i] != null && datas[i].length() > 0){
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
