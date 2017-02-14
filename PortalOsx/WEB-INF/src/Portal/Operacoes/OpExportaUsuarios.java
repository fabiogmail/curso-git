//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpExportaBase.java

package Portal.Operacoes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpExportaUsuarios extends OperacaoAbs 
{
   private FileWriter out;
   static 
   {
   }
   
   
   public OpExportaUsuarios() 
   {
   }
   
   
   public boolean iniciaOperacao(String p_Mensagem) 
   {
	   try{
		   String tipo = m_Request.getParameter("tipo");
		   montaLinhas();
		   OpListaUsuariosCfg listaUsuariosCfg = new OpListaUsuariosCfg();
		   m_Request.setAttribute("tipo",tipo);
		   listaUsuariosCfg.setRequestResponse(getRequest(), getResponse());
		   listaUsuariosCfg.iniciaOperacao("Lista atualizada");
      
	   }catch(IOException e){
		   e.printStackTrace();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
      return true;
   }
   
   public synchronized void montaLinhas() throws IOException
   {
      UsuarioDef Usuario;   
      Vector ListaUsuarios = new Vector();

      No noTmp = null;
      out = new FileWriter(new File(NoUtil.getNo().getDiretorioDefs().getS_DIR_DOWNLOAD()+"listausuarios.txt"));
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
	          noTmp = (No) iter.next();
	          ListaUsuarios.addAll(noTmp.getConexaoServUtil().getListaUsuariosCfg());
		  }
          catch(COMM_FAILURE comFail)
          {
              System.out.println(new Date()+" - SERVIDOR UTIL FORA DO AR ("+noTmp.getHostName()+").");
          }
          catch(BAD_OPERATION badOp)
          {
              System.out.println(new Date()+" - METODO NAO EXISTENTE NO SERVIDOR UTIL ("+noTmp.getHostName()+").");
              badOp.printStackTrace();
          }
      }
      out.write("Nome;");
      if(NoUtil.isAmbienteEmCluster()){
    	  out.write("Host;");
      }
      out.write("IdUsuario;");
      out.write("Perfil;");
      out.write("IdPerfil;");
      out.write("Acesso;");
      out.write("IdAcesso;");
      out.write("\r\n");
      
      for (short i = 0; i < ListaUsuarios.size(); i++)
      {
         Usuario = (UsuarioDef)ListaUsuarios.elementAt(i);
         out.write(Usuario.getUsuario()+";");
         if(NoUtil.isAmbienteEmCluster()){
         	out.write(Usuario.getHost()+";");
         }      
         
         out.write(new Integer(Usuario.getIdUsuario()).toString()+";");
         out.write(Usuario.getPerfil()+";");
         out.write(new Integer(Usuario.getIdPerfil()).toString()+";");
         out.write(Usuario.getAcesso()+";");
         out.write(new Integer(Usuario.getAcessoId()).toString()+";");
         out.write("\r\n");
        
 
      }
      out.close();
   }
}
