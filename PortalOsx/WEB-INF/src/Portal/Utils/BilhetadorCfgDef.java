//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/BilhetadorCfgDef.java

package Portal.Utils;



public class BilhetadorCfgDef implements Comparable
{
   private String m_Bilhetador;
   private String m_Apelido;
   private int m_OciosidadeAlr;
   private short m_Fase;
   private short m_HabilitaAlr = 0;
   private String hostname;
   public TecnologiaCfgDef m_Tecnologia;
   public OperadoraCfgDef m_Operadora;
   
   /**
    * @param p_Bilhetador
    * @param p_Tecnologia
    * @param p_Operadora
    * @return 
    * @exception 
    * @roseuid 3C43A1680059
    */
   public BilhetadorCfgDef(String p_Bilhetador, TecnologiaCfgDef p_Tecnologia, OperadoraCfgDef p_Operadora) 
   {
      m_Bilhetador = p_Bilhetador;
      m_Tecnologia = p_Tecnologia;
      m_Operadora  = p_Operadora;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C43A1C702E0
    */
   public String getBilhetador() 
   {
      return m_Bilhetador;
   }
   
   /**
    * @return TecnologiaCfgDef
    * @exception 
    * @roseuid 3C43A1CF0007
    */
   public TecnologiaCfgDef getTecnologia() 
   {
      return m_Tecnologia;
   }
   
   /**
    * @param p_OciosidadeAlr
    * @return void
    * @exception 
    * @roseuid 3C63DAED029B
    */
   public void setOciosidade(int p_OciosidadeAlr) 
   {
      m_OciosidadeAlr = p_OciosidadeAlr;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C63DAFA01D1
    */
   public int getOciosidade() 
   {
      return m_OciosidadeAlr;
   }
   
   /**
    * @param p_Apelido
    * @return void
    * @exception 
    * @roseuid 3C9205C602C3
    */
   public void setApelido(String p_Apelido) 
   {
      m_Apelido = p_Apelido;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C9205BB037B
    */
   public String getApelido() 
   {
      return m_Apelido;
   }
   
   /**
    * @param p_Fase
    * @return void
    * @exception 
    * @roseuid 3C9205CD01BE
    */
   public void setFase(short p_Fase) 
   {
      m_Fase = p_Fase;
   }
   
   /**
    * @return short
    * @exception 
    * @roseuid 3C9205D20067
    */
   public short getFase() 
   {
      return m_Fase;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3DAC6E5002A7
    */
   public int getPeriodicidade() 
   {
      return 0;
   }
   
   /**
    * @param p_Periodicidade
    * @return void
    * @exception 
    * @roseuid 3DAC6E6203D0
    */
   public void setPeriodicidade(int p_Periodicidade) 
   {
   }
   
   /**
    * @return short
    * @exception 
    * @roseuid 3DAC6E9E0079
    */
   public short getHabilita() 
   {
      return m_HabilitaAlr;
   }
   
   /**
    * @param p_Habilita
    * @return void
    * @exception 
    * @roseuid 3DAC6E9E007A
    */
   public void setHabilita(short p_Habilita) 
   {
      m_HabilitaAlr = p_Habilita;
   }
   
   /**
    * @return OperadoraCfgDef
    * @exception 
    * @roseuid 3F81B8FB02E0
    */
   public OperadoraCfgDef getOperadora() 
   {
      return m_Operadora;
   }

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		return m_Bilhetador.compareTo(((BilhetadorCfgDef)arg0).getBilhetador());
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}
