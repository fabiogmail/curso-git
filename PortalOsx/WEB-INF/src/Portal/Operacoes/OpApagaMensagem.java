//Source file: C:/usr/OSx/CDRView/Servlet/Portal/Operacoes/OpApagaMensagem.java

package Portal.Operacoes;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpApagaMensagem extends OperacaoAbs 
{
   /**
    * @return 
    * @exception 
    * @roseuid 3C6D4E6703D2
    */
   public OpApagaMensagem() { }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6D4E340053
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      String ListaArquivos;
      String verificaMarcacao;
      setOperacao("Apaga Mensagens");

      OpListaMensagens Mensagens = new OpListaMensagens();
      Mensagens.setRequestResponse(getRequest(), getResponse());

      ListaArquivos = m_Request.getParameter("mensagens");
      verificaMarcacao = m_Request.getParameter("marcaTodosPag");
      String[] mensagens = ListaArquivos.split(";");
      
      /**
       * Se o numero de mensagens selecionadas para exclusao for igual a 10 (o q significa, excluir todas
       * da tela), todas as demais mensagens (q estiverem nas outras paginas) serao EXCLUIDAS!!
       * 
       * A flag verificaMarcacao representa se vai ser apagado apenas os arquivos da pagina.
       * */
      if(mensagens.length < 10 || verificaMarcacao.equals("true"))
      {
          No noTmp = null;
          
          for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
          {
              try
              {
	              noTmp = (No) iter.next();
	              noTmp.getConexaoServUtil().apagaMensagemOutras(ListaArquivos);
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
          }
      }
      else
      {
      	StringBuffer Arquivos = new StringBuffer();
      	String Arquivo;
        UsuarioDef Usuario;
        Vector ListaMensagens = new Vector();
        Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
        No noTmp = null;
        
        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            noTmp = (No) iter.next();
            ListaMensagens.addAll(noTmp.getConexaoServUtil().getListaMensagensOutras(Usuario.getUsuario()));
        }
        ListaMensagens.trimToSize();
        
        if (ListaMensagens != null)
        {
           for (int i = 0; i < ListaMensagens.size(); i++)
           {
              Arquivo = (String)ListaMensagens.elementAt(i);
              Arquivos.append(Arquivo);
              Arquivos.append(";");
           }
        }
        
        for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
        {
            noTmp = (No) iter.next();
            noTmp.getConexaoServUtil().apagaMensagemOutras(Arquivos.toString().substring(0,Arquivos.length()-1));
        }
      }
      Mensagens.iniciaOperacao("Operação concluída!");
      return true;
   }
}

