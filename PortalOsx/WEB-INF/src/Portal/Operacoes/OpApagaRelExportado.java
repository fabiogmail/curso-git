//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApagaRelExportado.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpApagaRelExportado extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D2AF51F03C6
    */
   public OpApagaRelExportado() 
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

      OpListaRelExportados RelExport = new OpListaRelExportados();
      RelExport.setRequestResponse(getRequest(), getResponse());

      ListaArquivos = m_Request.getParameter("arquivosdel");
      Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
      Usuario.getNo().getConexaoServUtil().apagaRelExportados(Usuario.getIdPerfil(), ListaArquivos);

      RelExport.iniciaOperacao("Operação concluída!");
      return true;
   }
}
