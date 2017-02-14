package Portal.Dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Portal.Xml.XMLDomAgenda;


public class ConexaoDAO {
	
	private Connection conexao;

	
	public ConexaoDAO(){
		iniciaConexao();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ConexaoDAO dao = new ConexaoDAO();

	}
	
	
	/**
	 * Responsável por criar a conexao com o banco
	 */
	private void iniciaConexao(){
		try {
			XMLDomAgenda xml = new XMLDomAgenda();
//			Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName( "com.mysql.jdbc.Driver" );  
			//DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			//jdbc:oracle:thin:@192.168.200.50:1521:rolhmg
			String url = xml.getUrlBanco();
			String usuario = xml.getUsuarioBanco();
			String senha = xml.getSenhaBanco();
			conexao = DriverManager.getConnection(url,usuario,senha);
			
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConexao() {
		return conexao;
	}

	public void setConexao(Connection conexao) {
		this.conexao = conexao;
	}
	
	
	

}
