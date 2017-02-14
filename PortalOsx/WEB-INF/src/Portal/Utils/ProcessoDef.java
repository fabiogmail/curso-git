//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/ProcessoDef.java

package Portal.Utils;

import Interfaces.iTipoCli;
import Interfaces.iTipoProc;

/**
 */
public class ProcessoDef 
{ 
   private int m_Id = 0;
   private String m_Nome = null;
   private String m_Usuario = null;
   private String m_Perfil = null;
   private iTipoCli m_TipoCli = null;
   private iTipoProc m_TipoProc = null;
   private int m_DataInicio = 0;
   private int m_UltimoAcesso = 0;
   private String m_Host = "Desconhecido";
   private String m_DataInicioStr;
   private String m_UltimoAcessoStr;
   
   //------------------ids dos processos de acordo com o servidor----------------------
   public static final int ID_PROC_CONVERSOR = 0;
   public static final int ID_PROC_UTIL = 1;
   public static final int ID_PROC_INDEF = 2;
   public static final int ID_PROC_CONTROLE = 3;
   public static final int ID_PROC_CONFIG = 4;
   public static final int ID_PROC_CLIENTE = 5;
   public static final int ID_PROC_CONSOLID = 6;
   public static final int ID_PROC_AGENDA = 7;
   public static final int ID_PROC_CLIENTE_DIST = 8; 
   public static final int ID_PROC_HISTORICO = 9;
   public static final int ID_PROC_ALARME = 10;
   public static final int ID_PROC_PARSER = 11;
   public static final int ID_PROC_EXP_BANCO = 12;//parsergen
   public static final int ID_PROC_REPRO_CDRX = 13;
   public static final int ID_PROC_CONV_BASE = 14;
   //----------------------------------------------------------------------------------
   
   /**
    * @param p_Id
    * @param p_Nome
    * @param p_Usuario
    * @param p_Perfil
    * @param p_TipoCli
    * @param p_TipoProc
    * @param p_DataInicio
    * @param p_UltimoAcesso
    * @return 
    * @exception 
    * @roseuid 3C44B0E90397
    */
   public ProcessoDef(int p_Id, String p_Nome, String p_Usuario, String p_Perfil, iTipoCli p_TipoCli, iTipoProc p_TipoProc, int p_DataInicio, int p_UltimoAcesso) 
   {
      m_Id = p_Id;
      m_Nome = p_Nome;
      m_Usuario = p_Usuario;
      m_Perfil = p_Perfil;
      m_TipoCli = p_TipoCli;
      m_TipoProc = p_TipoProc;
      m_DataInicio = p_DataInicio;
      m_UltimoAcesso = p_UltimoAcesso;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C44B12602A4
    */
   public int getId() 
   {
      return m_Id;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C44B12E03D2
    */
   public String getNome() 
   {
      return m_Nome;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C44B1340222
    */
   public String getUsuario() 
   {
      return m_Usuario;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C44B13B01A0
    */
   public String getPerfil() 
   {
      return m_Perfil;
   }
   
   /**
    * @return iTipoCli
    * @exception 
    * @roseuid 3C44B1550035
    */
   public iTipoCli getTipoCli() 
   {
      return m_TipoCli;
   }
   
   /**
    * @return iTipoProc
    * @exception 
    * @roseuid 3C44B1650178
    */
   public iTipoProc getTipoProc() 
   {
      return m_TipoProc;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C44B16E0135
    */
   public int getDataInicio() 
   {
      return m_DataInicio;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C44B1750171
    */
   public String getDataInicioStr() 
   {
      return (m_DataInicioStr);
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C44B17C02C6
    */
   public int getUltimoAcesso() 
   {
      return m_UltimoAcesso;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C44B17C02E4
    */
   public String getUltimoAcessoStr() 
   {
      return (m_UltimoAcessoStr);
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3D4FC71B0139
    */
   public String getHost() 
   {
      return m_Host;
   }
   
   /**
    * @param p_Nome
    * @return void
    * @exception 
    * @roseuid 3CBC620B01EB
    */
   public void setNome(String p_Nome) 
   {
      m_Nome = p_Nome;
   }
   
   /**
    * @param p_Usuario
    * @return void
    * @exception 
    * @roseuid 3C856A8A00DD
    */
   public void setUsuario(String p_Usuario) 
   {
      m_Usuario = p_Usuario;
   }
   
   /**
    * @param p_Perfil
    * @return void
    * @exception 
    * @roseuid 3C856A9C0331
    */
   public void setPerfil(String p_Perfil) 
   {
      m_Perfil = p_Perfil;
   }
   
   /**
    * @param p_Host
    * @return void
    * @exception 
    * @roseuid 3D4FC6F603AD
    */
   public void setHost(String p_Host) 
   {
      m_Host = p_Host;
   }
   
   /**
    * @param p_DataInicio
    * @return void
    * @exception 
    * @roseuid 3E1EC3C60289
    */
   public void setDataInicioStr(String p_DataInicio) 
   {
      m_DataInicioStr = p_DataInicio;
   }
   
   /**
    * @param p_UltimoAcesso
    * @return void
    * @exception 
    * @roseuid 3E1EC3DE0175
    */
   public void setUltimoAcessoStr(String p_UltimoAcesso) 
   {
      m_UltimoAcessoStr = p_UltimoAcesso;
   }
   
   /**
    * Metodo criado para pegar o nome da configuracao.
    * 
    * @param nome Formato Esperado: nomeProcesso.exe (nomeConfiguracao)
    * @return nomeConfiguracao
    * */
   public String formataNomeProcesso(String nome)
   {
   		String configuracao = null;
   		if((nome!=null)&&(!nome.equals("")))
   		{   		
   			try{
   				configuracao = nome.substring(nome.indexOf("(")+1,nome.indexOf(")"));
   			}catch(Exception e){
   				configuracao = null;
   			}
   		}
   		return configuracao;
   		
   }
}
