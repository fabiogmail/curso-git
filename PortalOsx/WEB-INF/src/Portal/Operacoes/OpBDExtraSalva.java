//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpBDExtraSalva.java

package Portal.Operacoes;

import java.util.Iterator;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;


/**
 */
public class OpBDExtraSalva extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D19FE860367
    */
   public OpBDExtraSalva() 
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
         String Resp = null, Datas = null;

         // Inicia a operação BDExtra
         OpBDExtra BDExtra = new OpBDExtra();
         BDExtra.setRequestResponse(getRequest(), getResponse());

         // Recupera a lista de datas
         Datas = m_Request.getParameter("novalistadatas");

         // Seta a lista de datas no ServUtil e analisa a resposta
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
             noTmp = (No) iter.next();
             Resp += noTmp.getConexaoServUtil().setBDExtra(Datas);
         }
         
         if (Resp.length() == 0)
            Resp = "Datas de DDD-X salvas!";

         // Envia a página de datas novamente
         BDExtra.iniciaOperacao(Resp);
      }
      catch (Exception Exc)
      {
         System.out.println("OpBDExtraSalva - iniciaOperacao():"+Exc);
         Exc.printStackTrace();
         return false;         
      }

      return true;
   }
}
