//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpCDRViewCliente.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpCDRViewCliente3 extends OperacaoAbs 
{
   public static short qtdAberturas = 0;
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C8F5081032F
    */
   public OpCDRViewCliente3() 
   {
   }

   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C8F508803BB
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
         if (DefsComum.s_CLIENTE.toLowerCase().equals("embratel") == true)
            setOperacao("Cliente  SGDT");
         else
            setOperacao("Cliente  CDRView An&aacute;lise");

         qtdAberturas +=1;
         NoUtil.getNo().getConexaoServControle().ping();
            
         montaFormulario();
//         m_Response.sendRedirect("http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/templates/jsp/appletCDRView.jsp");

         m_Request.getRequestDispatcher("/templates/jsp/appletCDRView.jsp").forward(m_Request, m_Response);
        
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpCDRViewCliente - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C8F56C5024F
    */
   public void montaFormulario() 
   {       
       UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
       m_Request.setAttribute("codebase","http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/PortalOsx/lib/");
       m_Request.setAttribute("name","CDRView");
       m_Request.setAttribute("type","application/x-java-applet");
       m_Request.setAttribute("usuario",Usuario.getUsuario());
       m_Request.setAttribute("perfil",Usuario.getPerfil());
       m_Request.setAttribute("ior",NoUtil.getNo().getConexaoServControle().getM_IOR());
       m_Request.setAttribute("host", NoUtil.getNo().getIp());//Ip para conexão com servidor de Agenda via RMi.

       System.out.println("Quantidade de aberturas: " + qtdAberturas);
       
       if(qtdAberturas > 1)
    	   m_Request.setAttribute("podeAbrir","false");
       else
    	   m_Request.setAttribute("podeAbrir","true");
     
       m_Request.setAttribute("portaRmi", NoUtil.getNo().getPortaRMI());//Porta para conexão com servidor de Agenda via RMi.
       m_Request.setAttribute("hostName", NoUtil.getNo().getHostName());//Nome do host para conexão RMI

       if(DefsComum.sSUB_CLIENTE != null){
    	   if(DefsComum.sSUB_CLIENTE.length() > 0 ){
    		   m_Request.setAttribute("subCliente",DefsComum.sSUB_CLIENTE);
    	 	}
       }else{
    	   m_Request.setAttribute("subCliente",DefsComum.s_CLIENTE);
       }
     
   }
}
