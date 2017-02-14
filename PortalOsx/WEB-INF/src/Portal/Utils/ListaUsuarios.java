//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/ListaUsuarios.java

package Portal.Utils;

import java.util.Vector;

/**
 */
public class ListaUsuarios 
{
   
   /**
    * Lista de usuários conectados
    */
   private Vector m_Usuarios = null;
   
   static 
   {
      //System.out.println("Static - ListaUsuarios");
   }
   
   /**
    * @param p_Usuarios
    * @return void
    * @exception 
    * @roseuid 3C3B563802E5
    */
   public void setUsuarios(Vector p_Usuarios) 
   {
      m_Usuarios = p_Usuarios;
   }
   
   /**
    * @return Vector
    * @exception 
    * Retorna a lista de usuários.
    * @roseuid 3C3AF43B02BC
    */
   public Vector getUsuarios() 
   {
      return m_Usuarios;
   }
   
   /**
    * @param p_Usuario
    * @return boolean
    * @exception 
    * Adiciona um usuário à lista.
    * @roseuid 3C3AF44401E3
    */
   public boolean adicionaUsuario(UsuarioDef p_Usuario) 
   {
      boolean bInseriu = false;
      UsuarioDef Usuario;

      //System.out.println("ListaUsuarios - adicionaUsuario() "+m_Usuarios);
      for (short i = 0; i < m_Usuarios.size(); i++)
      {
         Usuario = (UsuarioDef)m_Usuarios.elementAt(i);
         //System.out.println("Usuario: "+Usuario.getUsuario()+ "- "+p_Usuario.getUsuario());
         if (Usuario.getUsuario().compareTo(p_Usuario.getUsuario()) <= 0)
         {
            //System.out.println("Usuario: "+Usuario.getUsuario()+ " - "+p_Usuario.getUsuario()+" - "+i);
            m_Usuarios.add(i, p_Usuario);
            //bInseriu = true;
            return true;
         }
      }

      m_Usuarios.addElement(p_Usuario);
      return true;
   }
   
   /**
    * @param p_Usuario
    * @return boolean
    * @exception 
    * Remove um usuário da lista.
    * @roseuid 3C3AF45501BF
    */
   public boolean removeUsuario(UsuarioDef p_Usuario) 
   {
      boolean bInseriu = false;
      UsuarioDef Usuario;

      //System.out.println("ListaUsuarios - removeUsuario("+p_Usuario.getUsuario()+")");
      try
      {
         if (m_Usuarios.remove(p_Usuario) == false)
            System.out.println("ListaUsuarios - removeUsuario(): Nao removeu o usuario: "+p_Usuario.getUsuario());
      /* else
         System.out.println("Usuario removido"); */
      }
      catch (Exception Exc)
      {
         System.out.println("ListaUsuarios - removeUsuario(): "+Exc);
      }
      return false;
   }
   
   /**
    * @param p_IDSessao
    * @return boolean
    * @exception 
    * Remove um usuário da lista. Faz busca pelo ID da sessão.
    * @roseuid 3CB1F28A0137
    */
   public boolean removeUsuarioIDSessao(UsuarioDef p_IDSessao) 
   {
      UsuarioDef Usuario = null;

      for (int i = 0; i < m_Usuarios.size(); i++)
      {
         Usuario = (UsuarioDef)m_Usuarios.elementAt(i);
         if (Usuario.getIDSessao().equals(p_IDSessao) == true)
         {
            m_Usuarios.remove (Usuario);
            return true;
         }
      }
      return false;
   }
   
   /**
    * @param p_NomeUsuario
    * @param p_SessaoId
    * @param p_Sequencia
    * @return UsuarioDef
    * @exception 
    * Procura um usuário na lista e retorna o objeto, caso encontrado. Caso contrário, retorna null.
    * @roseuid 3C3AF45C0089
    */
   public UsuarioDef buscaUsuario(String p_NomeUsuario, String p_SessaoId, short p_Sequencia) 
   {
      UsuarioDef Usuario;
      
      for (short i = 0; i < m_Usuarios.size(); i++)
      {
         Usuario = (UsuarioDef)m_Usuarios.elementAt(i);
         if (p_NomeUsuario != null && (Usuario.getUsuario().equals(p_NomeUsuario) == true && Usuario.getSequencia() == p_Sequencia))
            return Usuario;
         else if (p_NomeUsuario == null && Usuario.getIDSessao().equals(p_SessaoId) == true)
            return Usuario;
      }

      return null;
   }
}
