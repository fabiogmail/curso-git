//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpAtualizaPermissoesRel.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpAtualizaPermissoesRel extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3FB8CBBD0268
    */
   public OpAtualizaPermissoesRel() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3FB8CBBD0286
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {

      String Chave = m_Request.getParameter("Chave");
      String Permissoes = m_Request.getParameter("ListaPerfisPermitidos");

      OpApresentaPermissoesRel OpPermissoes = new OpApresentaPermissoesRel();
      OpPermissoes.setRequestResponse(getRequest(), getResponse());

      String idPerfil = Chave.substring(0, Chave.indexOf("-"));
      
      No no = NoUtil.buscaNobyIDPerfil(Integer.parseInt(idPerfil));
      
      if (no.getConexaoServUtil().atualizaPermissao(Chave, Permissoes))
      {
         OpPermissoes.iniciaOperacao("Permissão Atualizada!");
      }
      else
      {
         OpPermissoes.iniciaOperacao("Ocorreu algum erro durante a atualização das permissões!");
      }

      return true;
   }
}
