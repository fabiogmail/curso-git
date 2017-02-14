//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/AcessoCfgDef.java

package Portal.Utils;


/**
 */
public class AcessoCfgDef 
{
   private String m_Acesso;
   private short m_Id;
   
   /**
    * @param p_Acesso
    * @param p_Id
    * @return 
    * @exception 
    * @roseuid 3C30E8A603D9
    */
   public AcessoCfgDef(String p_Acesso, short p_Id) 
   {
      m_Acesso = p_Acesso;
      m_Id = p_Id;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C30E8C10098
    */
   public String getAcesso() 
   {
      return m_Acesso;
   }

   /**
    * @return short
    * @exception
    * @roseuid 3C30E8C80175
    */
   public short getId()
   {
      return m_Id;
   }
}
