package Portal.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Portal.Operacoes.OpBilhetadores;

public class ListagemCadastroDinamico extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
	     PrintWriter out = response.getWriter();
	 
	     String[][] bilhetadoresUnicos = buscarBilhetadores();
	     
	     StringBuilder print = new StringBuilder();
	     
	     print.append(montaBilhetadores(bilhetadoresUnicos));
	     
	     out.print(print.toString());
	     
	 }
	 
	 public String[][] buscarBilhetadores(){
		 OpBilhetadores opBilhetadores = new OpBilhetadores();
		 
		 String[][] bilhetadoresETecnologias = new String[2][];
		 
		 bilhetadoresETecnologias[0] =opBilhetadores.montaFormulario("")[0].split(";");
		 bilhetadoresETecnologias[1] =opBilhetadores.montaFormulario("")[4].split(";");
		 
		 return  bilhetadoresETecnologias;
	 }
	 
	public String montaBilhetadores(String[][] bilhetadoresUnicos) {
		 StringBuilder sb = new StringBuilder();
		 
		 sb.append("Bilhetador = {");
		 for (int i =0 ; i < bilhetadoresUnicos[0].length ; i++){
			 sb.append(bilhetadoresUnicos[0][i]+" ("+bilhetadoresUnicos[1][i]+"); ");
		 }
		 sb.append("}");
		 sb.append("\n\n\n");
		 return sb.toString();
	 }

}
