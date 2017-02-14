//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/ServidorProcCfgDef.java

package Portal.Utils;


/**
 */
public class ServidorProcCfgDef 
{
   private String m_Nome = null;
   private String m_IP = null;
   
   /**
    * @param p_Nome
    * @param p_IP
    * @return 
    * @exception 
    * @roseuid 3C471FC60038
    */
   public ServidorProcCfgDef(String p_Nome, String p_IP) 
   {
      m_Nome = p_Nome;
      m_IP = p_IP;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C471FC6004C
    */
   public String getNome() 
   {
      return m_Nome;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C471FC60060
    */
   public String getIP() 
   {
      return m_IP;
   }
}
