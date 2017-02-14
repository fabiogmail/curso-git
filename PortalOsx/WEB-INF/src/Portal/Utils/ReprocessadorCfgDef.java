//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/ConversorCfgDef.java

package Portal.Utils;


/**
 */
public class ReprocessadorCfgDef 
{
   private String m_Nome = null;
   private String m_Tecnologia = null;
   
   /**
    * @param p_Nome
    * @param p_Tecnologia
    * @return 
    * @exception 
    * @roseuid 3C471F610083
    */
   public ReprocessadorCfgDef(String p_Nome, String p_Tecnologia)
   {
      m_Nome = p_Nome;
      m_Tecnologia = p_Tecnologia;
   }

   /**
    * @return String
    * @exception
    * @roseuid 3C471F8E00B9
    */
   public String getNome()
   {
      return m_Nome;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C471F95002D
    */
   public String getTecnologia() 
   {
      return m_Tecnologia;   
   }
}

