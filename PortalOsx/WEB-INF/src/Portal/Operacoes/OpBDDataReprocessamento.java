//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpBDExtra.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;

/**
 */
public class OpBDDataReprocessamento extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D1719700367
    */
   public OpBDDataReprocessamento() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D171970037B
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         setOperacao("Salva Data Reprocessamento");
         
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/bdextra.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "bdextra_"+DefsComum.s_CLIENTE.toLowerCase()+".gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "bdDataReprocessamento.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "bdDataReprocessamento.txt", null);
         m_Html.enviaArquivo(m_Args);
 
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpBDExtra - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }

   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3D1719700385
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      String Args[];

      Args = new String[2];
      
      No noTmp = null;
      StringBuffer datasBaseDados = new StringBuffer();

//      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
//      {
          try
          {
	          //noTmp = (No) iter.next();
        	  noTmp = (No) NoUtil.getNoCentral();
        	  
        	  
        	  //System.out.println("DATA NOVA: "+retornaFormatoData(noTmp.getConexaoServUtil().getBDDataReprocessamento()));
        	  
	          datasBaseDados.append(retornaFormatoData(noTmp.getConexaoServUtil().getBDDataReprocessamento()));
	          datasBaseDados.append(";");
	      }
	      catch(COMM_FAILURE comFail)
	      {
	          System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
	      }
	      catch(BAD_OPERATION badOp)
	      {
	          System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
	          badOp.printStackTrace();
	      }
//      }
      String datas = datasBaseDados.toString();
      
      // retirando o ultimo ';'
      Args[0] = datas.substring(0, datas.length()-1);
      Args[1] = p_Mensagem;

      return Args;
   }
   
   /**
	 * @param dataAlterada
	 * @return lista de datas no formato dd/mm/aaaa
	 * Método responsável por alterar a data do formato aaaammdd para dd/mm/aaaa
	 */
	private String retornaFormatoData(String dataAlterada){
		if(dataAlterada.length() < 8)
			return dataAlterada;
		
		return alterar(dataAlterada.split(";"));
	}
	
	/**
	 * @param datas
	 * @return data no formato dd/mm/aaaa
	 */
	private String alterar(String []datas){
		
		String dia = "";
		String mes = "";
		String ano = "";
		
		String novaData = "";
		for (int i = 0; i < datas.length; i++) {
			
			dia = pegaDia(datas[i]);
			mes = pegaMes(datas[i]);
			ano = pegaAno(datas[i]);
			
			novaData += dia+"/"+mes+"/"+ano;
			
			if(i<datas.length - 1)
				novaData += ";";
		}
		
		return novaData;		
		
	}
	
	/**
	 * @param data
	 * @return dia da data no formato aaaammdd
	 */
	private String pegaDia(String data){		
		return data.substring(6, data.length());
		
	}
	
	/**
	 * @param data
	 * @return mes da data no formato aaaammdd
	 */
	private String pegaMes(String data){
		return data.substring(4, 6);
	}
	
	/**
	 * @param data
	 * @return ano da data no formato aaaammdd
	 */
	private String pegaAno(String data){
		return data.substring(0, 4);
	}

}
