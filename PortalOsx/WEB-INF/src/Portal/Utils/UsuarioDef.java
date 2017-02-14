//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/UsuarioDef.java

package Portal.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import Portal.Cluster.No;

/**
 * Classe para representação dos usuários conectados.
 */
public class UsuarioDef 
{ 
   private String usuario = null;
   private String perfil = null;
   private String acesso = null;
   private String senha = null;
   private String dicaDeSenha = null;
   private int idPerfil = 0;
   private int idUsuario = 0;
   private short idAcesso = 0;
   private short sequencia = 0;
   private int dataAcesso = 0;
   private String dataAcessoStr = null;
   
   private String nome;
   private String email;
   private String telefone;
   private String ramal;
   private String area;
   private String regional;
   private String motivo;
   private String responsavel;
   private String ativacao;
   private String ordem;
   
   private HttpSession sessao = null;
   private List listAgendasUsuario = new ArrayList();
   
   /**
    * Última operação invocada pelo usuário
    */
   private String operacao = "N/A";
   private String IP = null;
   private String host = null;
   private String IDSessao = null;
   private No no;
   
   /**
    * Armazena a configuração web de visualização de relatórios históricos:
    * 0 - excel/html
    * 1 - cabeçalho
    * 2 - relatório
    * 3 - benchmarking
    * 4 - valores médios
    * 5 - gráfico
    * 6 - botão imprimir
    */
   private Vector m_ConfWebHistorico = null;
   
   /**
    * Armazena a configuração web de visualização de relatórios históricos:
    * 0 - excel/html
    * 1 - cabeçalho
    * 2 - relatório
    * 3 - benchmarking
    * 4 - valores médios
    * 5 - gráfico
    * 6 - botão imprimir
    */
   private Vector m_ConfWebAgenda = null;
   
   /**
    * Armazena a configuração web de visualização de relatórios históricos:
    * 0 - excel/html
    * 1 - cabeçalho
    * 2 - relatório
    * 3 - benchmarking
    * 4 - valores médios
    * 5 - gráfico
    * 6 - botão imprimir
    */
   private Vector m_ConfWebNormal = null;
   
   static 
   {
   }
   
   /**
    * @param p_Usuario
    * @param p_Perfil
    * @param p_Acesso
    * @param p_Senha
    * @param p_DicaSenha
    * @param p_IdPerfil
    * @param p_IdUsuario
    * @param p_IdAcesso
    * @param p_Sequencia
    * @param p_DataAcesso
    * @return 
    * @exception 
    * @roseuid 3C85619B0387
    */
   public UsuarioDef(String usuario, String perfil, String acesso, String senha, String dicaSenha, int idPerfil, int idUsuario, short idAcesso, short sequencia, int dataAcesso,
		   String nome, String email, String telefone, String ramal, String area, String regional, String motivo, String responsavel, String ativacao) 
   {
      this.usuario = usuario;
      this.perfil = perfil;
      this.senha = senha;
      this.dicaDeSenha = dicaSenha;
      this.idPerfil = idPerfil;
      this.idUsuario = idUsuario;
      this.idAcesso = idAcesso;
      this.acesso = acesso;
      this.sequencia = sequencia;      
      this.dataAcesso = dataAcesso;
      m_ConfWebHistorico = new Vector();
      
      this.nome = nome;
      this.email = email;
      this.telefone = telefone;
      this.ramal = ramal;
      this.area = area;
      this.regional = regional;
      this.motivo = motivo;
      this.responsavel = responsavel;
      this.ativacao = ativacao;
     
      
      setConfWebHistorico("0;1;1;1;1;1;1");
      setConfWebAgenda   ("0000");
      setConfWebNormal   ("0;1;1;1;1;1;1");      
   }
   
   /**
 * @param usuario
 * @param perfil
 * @param acesso
 * @param senha
 * @param dicaSenha
 * @param idPerfil
 * @param idUsuario
 * @param idAcesso
 * @param sequencia
 * @param dataAcesso
 * @param nome
 * @param email
 * @param telefone
 * @param ramal
 * @param area
 * @param regional
 * @param motivo
 * @param responsavel
 * @param ativacao
 * @param ordem
 */
public UsuarioDef(String usuario, String perfil, String acesso, String senha, String dicaSenha, int idPerfil, int idUsuario, short idAcesso, short sequencia, int dataAcesso,
		   String nome, String email, String telefone, String ramal, String area, String regional, String motivo, String responsavel, String ativacao, String ordem) 
   {
      this.usuario = usuario;
      this.perfil = perfil;
      this.senha = senha;
      this.dicaDeSenha = dicaSenha;
      this.idPerfil = idPerfil;
      this.idUsuario = idUsuario;
      this.idAcesso = idAcesso;
      this.acesso = acesso;
      this.sequencia = sequencia;      
      this.dataAcesso = dataAcesso;
      m_ConfWebHistorico = new Vector();
      
      this.nome = nome;
      this.email = email;
      this.telefone = telefone;
      this.ramal = ramal;
      this.area = area;
      this.regional = regional;
      this.motivo = motivo;
      this.responsavel = responsavel;
      this.ativacao = ativacao;
      this.ordem = ordem;
     
      
      setConfWebHistorico("0;1;1;1;1;1;1");
      setConfWebAgenda   ("0000");
      setConfWebNormal   ("0;1;1;1;1;1;1");      
   }
   
   /**
    * @param p_Usuario
    * @param p_Perfil
    * @param p_Acesso
    * @param p_Senha
    * @param p_DicaSenha
    * @param p_IdPerfil
    * @param p_IdUsuario
    * @param p_IdAcesso
    * @param p_Sequencia
    * @param p_DataAcesso
    * @return 
    * @exception 
    * @roseuid 3C064F6E0236
    */
   public UsuarioDef(String usuario, String perfil, String acesso, String senha, String dicaSenha, int idPerfil, int idUsuario, short idAcesso, short sequencia, String dataAcesso,
		   String nome, String email, String telefone, String ramal, String area, String regional, String motivo, String responsavel, String ativacao) 
   {
   
      this.usuario = usuario;
      this.perfil = perfil;
      this.senha = senha;
      this.dicaDeSenha = dicaSenha;
      this.idPerfil = idPerfil;
      this.idUsuario = idUsuario;
      this.idAcesso = idAcesso;
      this.acesso = acesso;
      this.sequencia = sequencia;      
      this.dataAcessoStr = dataAcesso;
      
      m_ConfWebHistorico = new Vector();
      
      this.nome = nome;
      this.email = email;
      this.telefone = telefone;
      this.ramal = ramal;
      this.area = area;
      this.regional = regional;
      this.motivo = motivo;
      this.responsavel = responsavel;
      this.ativacao = ativacao;
      
      setConfWebHistorico("0;1;1;1;1;1;1");
      setConfWebAgenda   ("0000");
      setConfWebNormal   ("0;1;1;1;1;1;1");
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C064F2D00F2
    */
   public String getUsuario() 
   {
      return usuario;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C064F3A00C9
    */
   public String getPerfil() 
   {
      return perfil;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C38BDA20255
    */
   public String getAcesso() 
   {
      return acesso;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C064F4201E3
    */
   public String getSenha() 
   {
      return senha;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C064F470316
    */
   public String getDicaSenha() 
   {
      return dicaDeSenha;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C064F5300A7
    */
   public int getIdPerfil() 
   {
      return idPerfil;
   }
   
   /**
    * @return int
    * @exception 
    * @roseuid 3C064F630136
    */
   public int getIdUsuario() 
   {
      return idUsuario;
   }
   
   /**
    * @return short
    * @exception 
    * @roseuid 3C0651AF01BE
    */
   public short getAcessoId() 
   {
      return idAcesso;
   }
   
   /**
    * @return short
    * @exception 
    * Recupera o número de seqüência do usuário. Diferente de 0 em caso de multisessão.
    * @roseuid 3C0653B50186
    */
   public short getSequencia() 
   {
      return sequencia;
   }
   
   /**
    * @return int
    * @exception 
    * Recupera a data/hora em que o usuário se logou.
    * @roseuid 3C06535502B5
    */
   public int getDataAcesso() 
   {
      return dataAcesso;
   }
   
   /**
    * @return String
    * @exception 
    * Recupera a data/hora em que o usuário se logou.
    * @roseuid 3C59968C038B
    */
   public String getDataAcessoStr() 
   {
      return dataAcessoStr;
   }
   
   /**
    * @return String
    * @exception 
    * Recupera a data/hora em que o usuário se logou.
    * @roseuid 3C8561F702AD
    */
   public String getDataAcessoStr2() 
   {
      return dataAcessoStr;
   }
   
   /**
    * @param p_Sessao
    * @return void
    * @exception 
    * @roseuid 3C3A4F5000D5
    */
   public void setSessaoHTTP(HttpSession sessao) 
   {
      this.sessao = sessao;
   }
   
   /**
    * @return HttpSession
    * @exception 
    * @roseuid 3C3A4F8200C3
    */
   public HttpSession getSessaoHTTP() 
   {
      return sessao;
   }
   
   /**
    * @param p_IDSessao
    * @return void
    * @exception 
    * @roseuid 3CB1E86F030E
    */
   public void setIDSessao(String IDSessao) 
   {
      this.IDSessao = IDSessao;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3CB1E88B0051
    */
   public String getIDSessao() 
   {
      return IDSessao;
   }
   
   /**
    * @return String
    * @exception 
    * Retorna a última operação invocada pelo usuário.
    * @roseuid 3C3A54EE01D7
    */
   public String getOperacao() 
   {
      return operacao;
   }
   
   /**
    * @param p_Operacao
    * @return void
    * @exception 
    * Seta a última operação invocada pelo usuário.
    * @roseuid 3C3A54F602D3
    */
   public void setOperacao(String operacao) 
   {
      this.operacao = operacao;
   }
   
   /**
    * @param p_IdPerfil
    * @return void
    * @exception 
    * @roseuid 3C37297F0320
    */
   public void setIdPerfil(int idPerfil) 
   {
      this.idPerfil = idPerfil;
   }
   
   /**
    * @param p_IdAcesso
    * @return void
    * @exception 
    * @roseuid 3C37299A02D8
    */
   public void setAcessoId(short idAcesso) 
   {
      this.idAcesso = idAcesso;
   }
   
   /**
    * @param p_Senha
    * @return void
    * @exception 
    * @roseuid 3C8799E60029
    */
   public void setSenha(String senha) 
   {
      this.senha = senha;
   }
   
   /**
    * @param p_Dica
    * @return void
    * @exception 
    * @roseuid 3C8799FC03B0
    */
   public void setDicaSenha(String dica) 
   {
      dicaDeSenha = dica;
   }
   
   /**
    * @param p_DataAcesso
    * @return void
    * @exception 
    * @roseuid 3C9684B2014D
    */
   public void setDataAcesso(int dataAcesso) 
   {
      this.dataAcesso = dataAcesso;
   }
   
   /**
    * @param p_DataAcessoStr
    * @return void
    * @exception 
    * @roseuid 3E1ED19E00A5
    */
   public void setDataAcessoStr(String dataAcessoStr) 
   {
      this.dataAcessoStr = dataAcessoStr;
   }
   
   /**
    * @param p_IP
    * @return void
    * @exception 
    * @roseuid 3CA1CB69037F
    */
   public void setIP(String IP) 
   {
      this.IP = IP;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3CA1CB6F0247
    */
   public String getIP() 
   {
      return IP;
   }
   
   /**
    * @param p_Host
    * @return void
    * @exception 
    * @roseuid 3CA1CB840324
    */
   public void setHost(String host) 
   {
      this.host = host;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3CA1CB840326
    */
   public String getHost() 
   {
      return host;
   }
   
   /**
    * @param p_CfgWebHistorico
    * @return void
    * @exception 
    * @roseuid 3EEA346702AC
    */
   public void setConfWebHistorico(String p_CfgWebHistorico) 
   {
      if (p_CfgWebHistorico != null && p_CfgWebHistorico.length() != 0)
         m_ConfWebHistorico = VetorUtil.String2Vetor(p_CfgWebHistorico, ';') ;
      else
         m_ConfWebHistorico = VetorUtil.String2Vetor("html;1;1;1;1;1;1", ';') ;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3EEA348403E5
    */
   public Vector getConfWebHistorico() 
   {
      return m_ConfWebHistorico;
   }
   
   /**
    * @param p_CfgWebAgenda
    * @return void
    * @exception 
    * @roseuid 3EEDD5BB00C0
    */
   public void setConfWebAgenda(String p_CfgWebAgenda) 
   {
      m_ConfWebAgenda = new Vector();
      if (p_CfgWebAgenda == null || p_CfgWebAgenda.length() == 0 || p_CfgWebAgenda.indexOf(';') != -1)
         for (int i = 0; i < 4; i++) m_ConfWebAgenda.add("0");
      else
      {
         for (int i = 0; i < p_CfgWebAgenda.length(); i++)
            m_ConfWebAgenda.add(p_CfgWebAgenda.charAt(i)+"");
      }
      m_ConfWebAgenda.trimToSize();
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3EEDD5BB00DE
    */
   public Vector getConfWebAgenda() 
   {
      return m_ConfWebAgenda;
   }
   
   /**
    * @param p_CfgWebNormal
    * @return void
    * @exception 
    * @roseuid 3EEDD5BC02A2
    */
   public void setConfWebNormal(String p_CfgWebNormal) 
   {
      int i;

      m_ConfWebNormal = new Vector();
      if (p_CfgWebNormal != null && p_CfgWebNormal.length() != 0)
      {
         if (p_CfgWebNormal.indexOf(';') != -1)
         {
            for (i = 0; i < 13; i++)
            {
               if (i == 0 || i == 1 || i == 12)
                  m_ConfWebNormal.addElement("0");
               else
                  m_ConfWebNormal.addElement("1");
            }
         }
         else
         {
            for (i = 0; i < p_CfgWebNormal.length(); i++)
            {
               switch (p_CfgWebNormal.charAt(i))
               {
                  case 0:
                     m_ConfWebNormal.addElement("0");
                     break;
                  case 1:
                     m_ConfWebNormal.addElement("1");                  
                     break;
                  case 2:
                     m_ConfWebNormal.addElement("2");                  
                     break;
                  case 3:
                     m_ConfWebNormal.addElement("3");                  
                     break;
                  case 4:
                     m_ConfWebNormal.addElement("5");                  
                     break;
                  case 5:
                     m_ConfWebNormal.addElement("6");                  
                     break;
                  case 6:
                     m_ConfWebNormal.addElement("7");                  
                     break;
                  case 7:
                     m_ConfWebNormal.addElement("8");                  
                     break;                  
               }
               //m_ConfWebNormal.addElement(Character.toString(p_CfgWebNormal.charAt(i)));
            }
         }
      }
      else
      {
         for (i = 0; i < 13; i++)
         {
            if (i == 0 || i == 1 || i == 12)
               m_ConfWebNormal.addElement("0");
            else
               m_ConfWebNormal.addElement("1");
         }
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3EEDD5BC02AC
    */
   public Vector getConfWebNormal() 
   {
      return m_ConfWebNormal;
   }
	public List getM_ListAgendasUsuario()
	{
	    return listAgendasUsuario;
	}
	public void setM_ListAgendasUsuario(List listAgendasUsuario)
	{
	    this.listAgendasUsuario = listAgendasUsuario;
	}
	public No getNo()
	{
	    return no;
	}
	public void setNo(No no)
	{
	    this.no = no;
	}
	
	
	/* 
	 * Métodos para o funcionamento do objeto no AJAX
	 */
	

	public String getArea() {
		return area;
	}

	public String getAtivacao() {
		return ativacao;
	}
	
	public Date getAtivacaoDate() {
		long millis = DataUtil.dataInMillis(DataUtil.dataToLong(ativacao));
		Date data = new Date(millis);
		return data;
	}

	public String getDicaDeSenha() {
		return dicaDeSenha;
	}

	public String getEmail() {
		return email;
	}

	public short getIdAcesso() {
		return idAcesso;
	}

	public String getMotivo() {
		return motivo;
	}

	public String getNome() {
		return nome;
	}

	public String getRamal() {
		return ramal;
	}

	public String getRegional() {
		return regional;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public HttpSession getSessao() {
		return sessao;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}


	
	
	
}
