//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/ConfiguracaoConvCfgDef.java

package Portal.Utils;

import java.util.Vector;

/**
 */
public class ConfiguracaoReprocCfgDef 
{
   private String m_Nome = null;
   private String m_Servidor = null;
   private String m_Reprocessador = null;
   private Vector m_Bilhetadores = null;
   private String m_Relacionamento = null;
   
   /**
    * @param p_Nome
    * @param p_Servidor
    * @param p_Conversor
    * @param p_Bilhetadores
    * @param p_Relacionamento
    * @return 
    * @exception 
    * @roseuid 3C4720C10007
    */
   public ConfiguracaoReprocCfgDef(String p_Nome, String p_Servidor, String p_Reprocessador, Vector p_Bilhetadores, String p_Relacionamento) 
   {
      m_Nome = p_Nome;
      m_Servidor = p_Servidor;
      m_Reprocessador = p_Reprocessador;
      m_Bilhetadores = p_Bilhetadores;
      m_Relacionamento = p_Relacionamento;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C4720C10025
    */
   public String getNome() 
   {
      return m_Nome;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C4720C10039
    */
   public String getServidor() 
   {
      return m_Servidor;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C4720D900D3
    */
   public String getConversor() 
   {
      return m_Reprocessador;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C4720F2010C
    */
   public Vector getBilhetadores() 
   {
      return m_Bilhetadores;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C488A98037F
    */
   public String getRelacionamento() 
   {
      return m_Relacionamento;
   }
}
