//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Conexao/Conexao.java

package Portal.Conexao;

import java.util.Properties;

import org.omg.CORBA.ORB;

import Portal.Cluster.No;
import Portal.Utils.Arquivo;


public class Conexao
{
   protected String m_IOR;
   protected org.omg.CORBA.ORB m_Orb;
   protected org.omg.CORBA.Object m_Obj;
   protected Properties m_Propriedades;
   protected String host;
   protected short modoConexao;
   protected int porta;
   protected String nomeObjetoCorba;
   protected No no;
   
   public Conexao() 
   {
   }
   
   /**
    * @param p_Diretorio
    * @param p_Arquivo
    * @return boolean
    * @exception 
    * Obtém o IOR do servidor através da leitura direta do arquivo gerado pelo mesmo.
    * @roseuid 3BF9B06802B4
    */
   protected boolean getIORArq(String p_Diretorio, String p_Arquivo) 
   {
      Arquivo ArqIOR = new Arquivo();

      ArqIOR.setDiretorio(p_Diretorio);
      ArqIOR.setNome(p_Arquivo);

      try
      {
         System.out.println("Conexao - getIORArq(): Abrindo arquivo "+p_Diretorio+p_Arquivo);
         if (ArqIOR.abre('r'))
         {
            m_IOR = ArqIOR.leLinha();
            return true;
         }
         else
            return false;
      }
      catch (Exception Exc)
      {
         System.out.println("Conexao - getIORArq(): Arquivo de IOR do servidor nao encontrado");
         System.out.println("Conexao - getIORArq(): "+p_Diretorio+p_Arquivo);
         System.out.println("Conexao - getIORArq(): "+Exc);
         return false;
      }
   }
   
   /**
    * @param p_Args
    * @param p_Modo
    * @param p_Servidor
    * @param p_Porta
    * @param p_Objeto
    * @return void
    * @exception 
    * @roseuid 3BF9B9E7002E
    */
   protected void iniciaOrb(String[] p_Args, boolean p_Modo, String p_Servidor, int p_Porta, String p_Objeto) 
   {
	   
	  System.setProperty("com.sun.CORBA.transport.ORBTCPReadTimeouts", "1:60000:300:1");
      m_Propriedades = System.getProperties();
      m_Orb = ORB.init(p_Args, m_Propriedades);      
      if (p_Modo)
      {
         m_Obj = m_Orb.string_to_object(m_IOR);
      }
      else
      {
         m_IOR = "corbaloc:iiop:1.0@"+p_Servidor+":"+p_Porta+"/"+p_Objeto;
         System.out.println("m_IOR: "+m_IOR);         
         m_Obj = m_Orb.string_to_object(m_IOR);
      }
   }
   
	public String getHost()
	{
	    return host;
	}
	public void setHost(String host)
	{
	    this.host = host;
	}
	public String getNomeObjetoCorba()
	{
	    return nomeObjetoCorba;
	}
	public void setNomeObjetoCorba(String nomeObjetoCorba)
	{
	    this.nomeObjetoCorba = nomeObjetoCorba;
	}
	
	public short getModoConexao()
	{
	    return modoConexao;
	}
	public void setModoConexao(short modoConexao)
	{
	    this.modoConexao = modoConexao;
	}
	public int getPorta()
	{
	    return porta;
	}
	public void setPorta(int porta)
	{
	    this.porta = porta;
	}
	
	public String getM_IOR()
	{
	    return m_IOR;
	}
	
	public void setM_IOR(String m_ior)
	{
	    m_IOR = m_ior;
	}
	
	public No getNo()
	{
		return no;
	}
	
	public void setNo(No no)
	{
		this.no = no;
	}
	
}
