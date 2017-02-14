package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

public class OpApagaDrillExportados extends OperacaoAbs 
{
	   
	   static 
	   {
	   }
	   
	   /**
	    * @return 
	    * @exception 
	    * @roseuid 3D2AF51F03C6
	    */
	   public OpApagaDrillExportados() 
	   {
	   }
	   
	   /**
	    * @param p_Mensagem
	    * @return boolean
	    * @exception 
	    * @roseuid 3D2AF52B0192
	    */
	   public boolean iniciaOperacao(String p_Mensagem) 
	   {
	      String ListaArquivos;
	      UsuarioDef Usuario;

	      setOperacao("Apaga Relatórios Exportados");

	      OpListaExportaDrill RelExport = new OpListaExportaDrill();
	      RelExport.setRequestResponse(getRequest(), getResponse());

	      ListaArquivos = m_Request.getParameter("arquivosdel");
	      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
	      Usuario.getNo().getConexaoServUtil().apagaRelExportadosDrill(Usuario.getIdPerfil(), ListaArquivos);

	      RelExport.iniciaOperacao("Operação concluída!");
	      return true;
	   }
}
