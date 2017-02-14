//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/PerfilCfgDef.java

package Portal.Utils;

import java.util.Vector;

/**
 * Classe para representação dos perfis configurados.
 */
public class PerfilCfgDef 
{ 
   
   /**
    * Nome do perfil
    */
   private String m_Perfil;
   
   /**
    * Id do Perfil
    */
   private int m_Id;
   
   /**
    * Id do tipo de acesso do perfil
    */
   private short m_AcessoId;
   
   /**
    * Lista de usuários do perfil
    */
   private Vector m_ListaUsuarios = null;
   
   /**
    * Nome do tipo de acesso
    */
   private String m_AcessoNome;
   
   /**
    * Perfil está bloqueado
    */
   private boolean m_Bloqueado;
   
   /**
    * Host do perfil, usado qnd execução em cluster
    */
   private String m_Host;
   
   /**
    * se tem ou nao sigilo telefonico
    */
   private boolean sigiloTelefonico;
   
   /**
    * variavel que diz se são todas as tecnologias
    */
   private boolean selecionouTodas;
   
   /**
    * variavel que diz se são todaos os relatorios Agendados
    */
   private boolean selecionouTodosRel;
   
   /**
    * variavel que diz se são todaos os relatorios Historicos
    */
   private boolean selecionouTodosRelHist;
   /**
    * contem os ids das tecnologias escolhidas
    */
   private String idTecnologias;
   
   /**
    * contem os ids dos relatorios Agendados escolhidos
    */
   private String idRelatorios;
   
   /**
    * contem os ids dos relatorios Historicos escolhidos
    */
   private String idRelatoriosHist;
   
   static 
   {
   }
   
   /**
    * @param p_Perfil
    * @param p_Id
    * @param p_Acesso
    * @param p_ListaUsuarios
    * @param p_Bloqueado
    * @return 
    * @exception 
    * @roseuid 3C23D5F0032E
    */
   public PerfilCfgDef(String p_Perfil, int p_Id, short p_Acesso, Vector p_ListaUsuarios, boolean p_Bloqueado) 
   {
      m_Perfil = p_Perfil;
      m_Id = p_Id;
      m_AcessoId = p_Acesso;
      m_ListaUsuarios = p_ListaUsuarios;
      m_Bloqueado = p_Bloqueado;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C23D606016D
    */
   public String getPerfil() 
   {
      return m_Perfil;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C23D60E00CE
    */
   public int getId() 
   {
      return m_Id;
   }
   
   /**
    * @return short
    * @exception 
    * @roseuid 3C23D6140267
    */
   public short getAcesso() 
   {
      return m_AcessoId;
   }
   
   /**
    * @return Vector
    * @exception 
    * Retorna a lista de usuários do perfil.
    * @roseuid 3C23D6330121
    */
   public Vector getUsuarios() 
   {
      return m_ListaUsuarios;
   }
   
   /**
    * @param p_AcessoNome
    * @return void
    * @exception 
    * @roseuid 3C389FAE03C2
    */
   public void setAcessoNome(String p_AcessoNome) 
   {
      m_AcessoNome = p_AcessoNome;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C389FB60002
    */
   public String getAcessoNome() 
   {
      return m_AcessoNome;
   }
   
   /**
    * @return boolean
    * @exception 
    * @roseuid 3C3CEAF7002C
    */
   public boolean getBloqueio() 
   {
      return m_Bloqueado;
   }

   public String getHost() {
	  return m_Host;
   }
   
   public void setHost(String host) {
	  m_Host = host;
   }

public String getIdTecnologias() {
	return idTecnologias;
}

public void setIdTecnologias(String idTecnologias) {
	this.idTecnologias = idTecnologias;
}

public boolean isSelecionouTodas() {
	return selecionouTodas;
}

public void setSelecionouTodas(boolean selecionouTodas) {
	this.selecionouTodas = selecionouTodas;
}

public boolean isSelecionouTodosRel() {
	return selecionouTodosRel;
}

public void setSelecionouTodosRel(boolean selecionouTodosRel) {
	this.selecionouTodosRel = selecionouTodosRel;
}

public boolean isSelecionouTodosRelHistorico() {
	return selecionouTodosRelHist;
}

public void setSelecionouTodosRelHistorico(boolean selecionouTodosRelHist) {
	this.selecionouTodosRelHist = selecionouTodosRelHist;
}

public boolean isSigiloTelefonico() {
	return sigiloTelefonico;
}

public void setSigiloTelefonico(boolean sigiloTelefonico) {
	this.sigiloTelefonico = sigiloTelefonico;
}

public String getIdRelatorios() {
	return idRelatorios;
}

public void setIdrelatorios(String idRelatorios) {
	this.idRelatorios = idRelatorios;
}

public String getIdRelatoriosHistoricos() {
	return idRelatoriosHist;
}

public void setIdrelatoriosHistoricos(String idRelatoriosHist){
	this.idRelatoriosHist = idRelatoriosHist;
}
}