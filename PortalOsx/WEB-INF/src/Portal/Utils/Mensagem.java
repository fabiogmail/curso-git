//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/Mensagem.java

package Portal.Utils;

import Portal.Conexao.CnxServUtil;

/**
 * Classe para envio de mensagens genéricas para um usuário ou para um perfil.
 */
public class Mensagem 
{
   private String m_Remetente = "administrador";
   private String m_Usuario = null;
   private String m_Perfil = null;
   private String m_Assunto = null;
   private String m_Mensagem = null;
   private short m_Tipo = 1;
   private CnxServUtil m_ConexUtil;
   
   static 
   {
   }
   
   public Mensagem() 
   {
   }
   
   /**
    * @param p_ConexUtil
    * @return 
    * @exception 
    * @roseuid 3DC915E6031D
    */
   public Mensagem(CnxServUtil p_ConexUtil) 
   {
      m_ConexUtil = p_ConexUtil;
   }
   
   /**
    * @param p_Remetente
    * @return void
    * @exception 
    * @roseuid 3DC921F001B6
    */
   public void setRemetente(String p_Remetente) 
   {
   }
   
   /**
    * @param p_Usuario
    * @return void
    * @exception 
    * @roseuid 3DC915ED033B
    */
   public void setUsuario(String p_Usuario) 
   {
      m_Usuario = p_Usuario;
   }
   
   /**
    * @param p_Perfil
    * @return void
    * @exception 
    * @roseuid 3DC915FD033E
    */
   public void setPerfil(String p_Perfil) 
   {
      m_Perfil = p_Perfil;
   }
   
   /**
    * @param p_Assunto
    * @return void
    * @exception 
    * @roseuid 3DC915FF0029
    */
   public void setAssunto(String p_Assunto) 
   {
      m_Assunto = p_Assunto;
   }
   
   /**
    * @param p_Mensagem
    * @return void
    * @exception 
    * @roseuid 3DC915FF03D7
    */
   public void setMensagem(String p_Mensagem) 
   {
      m_Mensagem = p_Mensagem;
   }
   
   /**
    * @param p_Tipo
    * @return void
    * @exception 
    * @roseuid 3DC91659015F
    */
   public void setTipo(short p_Tipo) 
   {
      m_Tipo = p_Tipo;
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3DC9166D010E
    */
   public void envia() 
   {
      m_ConexUtil.enviaMensagem(m_Remetente, m_Perfil, m_Usuario, m_Assunto, m_Mensagem, m_Tipo);
   }
}
