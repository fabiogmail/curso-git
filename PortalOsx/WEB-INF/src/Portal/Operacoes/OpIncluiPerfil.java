//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpIncluiPerfil.java

package Portal.Operacoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;


public class OpIncluiPerfil extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpIncluiPerfil - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * Construtor.
    * @roseuid 3C335E28002C
    */
   public OpIncluiPerfil() 
   {
      //System.out.println("OpIncluiPerfil - construtor");
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C335E210130
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpIncluiPerfil - iniciaOperacao()");
      try
      {
         setOperacao("Inclusão de Perfil");
         boolean sigilo = false;
         boolean todasTecnologias = false;
         boolean todosRelatorios = false;
         boolean todosRelsHist = false;
         // Recupera os parâmetros 
    		
         String idTecnologias = m_Request.getParameter("tecnologias");
         String idRelatorios = m_Request.getParameter("relatorios");
         String idRelsHist = "";
         
         String Perfil = m_Request.getParameter("perfil");
         String Acesso = m_Request.getParameter("acesso");
         String habilitaSigilo = m_Request.getParameter("habilitaSigilo");
         
         String selecionaTodasTecnologias = m_Request.getParameter("selecionaTodasTec");
         String selecionaTodosRelatorios = m_Request.getParameter("selecionaTodosRel");
         
         
         if(habilitaSigilo.equalsIgnoreCase("true")){
        	 sigilo = true;
         }
         if(selecionaTodasTecnologias.equalsIgnoreCase("true")){
        	 todasTecnologias = true;
         }
         if(selecionaTodosRelatorios.equalsIgnoreCase("true")){
        	 todosRelatorios = true;
        	 todosRelsHist = true;
         }         
         if(idTecnologias != null && idTecnologias.length()>0){
        	 //tirando o ; do fim da string
        	 idTecnologias = idTecnologias.substring(0,idTecnologias.length()-1);
         }
         if(idRelatorios != null && idRelatorios.length()>0){
        	 //tirando o ; do fim da string
        	 idRelatorios = idRelatorios.substring(0,idRelatorios.length()-1);
         
	         /**Quebrando o vetor de relatorios em dois os agendados que começam com 0 e 
	          * os não agendados que começam com 1
	          **/
	         String idsRel[]= idRelatorios.split(";");
	         ArrayList idsRelAgendados = new ArrayList();
	         ArrayList idsRelHistoricos = new ArrayList();
	         for(int i=0; i<idsRel.length; i++)
	         {
	        	 if(idsRel[i].indexOf("0")==0)//é agendado
	        	 {
	        		 idsRelAgendados.add(idsRel[i].substring(2));
	        	 }
	        	 else
	        	 {	        		 
	        		 //GATO - em que o relatorio de historico é colocado como agendado
	        		 //historico é um relatorio agora com desempenho2
	        		 if(idsRel[i].substring(2).equals("38")){
	        			 idsRelAgendados.add(idsRel[i].substring(2));
	        		 }
	        		 
	        		 idsRelHistoricos.add(idsRel[i].substring(2));
	        	 }	        	 
	         }
	         
	         //monta os ids dos relatorios agendados
	         idRelatorios = "";
	         for(int i=0; i<idsRelAgendados.size(); i++)
	         {
	        	 if(i<(idsRelAgendados.size()-1))
	        		 idRelatorios+=idsRelAgendados.get(i)+";";
	        	 else
	        		 idRelatorios+=idsRelAgendados.get(i);
	         }
	         //monta os ids dos relatorios agendados
	         for(int i=0; i<idsRelHistoricos.size(); i++)
	         {
	        	 if(i<(idsRelHistoricos.size()-1))
	        		 idRelsHist+=idsRelHistoricos.get(i)+";";
	        	 else
	        		 idRelsHist+=idsRelHistoricos.get(i);
	         }
         }
         OpPerfis Perfis = new OpPerfis();
         Perfis.setRequestResponse(getRequest(), getResponse());
         
         No noTmp = null;
         No no = null; // No que contem a menor qtd de usuarios.
         int qtdUsuarios = Integer.MAX_VALUE, qtdUsuariosTmp = -1;
         boolean perfilExistente = false;
         boolean existeAdminFraude = false;
         
         String automatico = "";
         String nomeNoEscolhido = "";
         int maxId = -1;
         
         if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro") || DefsComum.s_CLIENTE.equalsIgnoreCase("Tim") || DefsComum.s_CLIENTE.equalsIgnoreCase("Oi"))
         {        	 
        	 automatico = m_Request.getParameter("automatico");
        	 nomeNoEscolhido = m_Request.getParameter("noEspecifico");  
        	 if(automatico.equalsIgnoreCase("true"))
        	 {//modo normal. Réplica do código do else abaixo
        		 for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
    	         {
    	             try
    	             {
    		             noTmp = (No) iter.next();
		             	 
    		             if (noTmp.getConexaoServUtil().trataPerfil(Perfil))
    		             {
    		                 perfilExistente = true;
    		                 break;
    		             }
    		             
    		             int idAtual = noTmp.getConexaoServUtil().getMaiorIdPerfil();
    		             
    		             if(idAtual > maxId)
    		            	 maxId = idAtual;
    		             
    		             qtdUsuariosTmp = noTmp.getConexaoServUtil().getListaUsuariosCfg().size();
    		             
    		             if (qtdUsuariosTmp < qtdUsuarios)
    		             {
    		                 qtdUsuarios = qtdUsuariosTmp;
    		                 no = noTmp;
    		             }
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
        	 }
        	 else
        	 {//caso em que o cliente escolheu o nome do no
        		 for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
    	         {
    	             try
    	             {
    		             noTmp = (No) iter.next();
    		             
    		             if(noTmp.toString().equalsIgnoreCase(nomeNoEscolhido))
    		             {
    		            	 if (noTmp.getConexaoServUtil().trataPerfil(Perfil))
        		             {
        		                 perfilExistente = true;        		             
    		                 break;
    		             }
    		             
    		             if(noTmp.toString().equalsIgnoreCase(nomeNoEscolhido))
        		             {
        		                 no = noTmp;   		            	 
        		             }
    		             
    		             int idAtual = noTmp.getConexaoServUtil().getMaiorIdPerfil();
    		             
    		             if(idAtual > maxId)
    		            	 maxId = idAtual;    		             
    		             }
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
        	 }
        	 /**
              * esse trecho de codigo abaixo verifica se ja existe algum perfil com acesso
              * adminfraude se ja existir não poderá existir outro igual
              */
        	 for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	         {
        		 try
	             {
		             noTmp = (No) iter.next();
		             Vector todosPerfis = noTmp.getConexaoServUtil().getListaPerfisCfg();
		        	 for(int i=0; i<todosPerfis.size(); i++)
		        	 {
		        		 if(((PerfilCfgDef)todosPerfis.get(i)).getAcessoNome().equalsIgnoreCase("adminFraude"))
		        		 {
		        			 existeAdminFraude = true;
		        		 }
		        	 }
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
         }
         else
         {
         /**
          * Se o sistema estiver rodando em cluster, a criacao de um novo
          * PERFIL se dara na maquina que possuir a menor quantidade de 
          * usuarios. Eh importante lembrar que o perfil so sera criado 
          * desde que o mesmo nao exista (em TODOS os Nos do cluster).
          * */
	         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	         {
	             try
	             {
		             noTmp = (No) iter.next();
		             
		             if (noTmp.getConexaoServUtil().trataPerfil(Perfil))
		             {
		                 perfilExistente = true;
		             }
		             
		             int idAtual = noTmp.getConexaoServUtil().getMaiorIdPerfil();
		             
		             if(idAtual > maxId)
		            	 maxId = idAtual;
		             
		             qtdUsuariosTmp = noTmp.getConexaoServUtil().getListaUsuariosCfg().size();
		             
		             if (qtdUsuariosTmp < qtdUsuarios)
		             {
		                 qtdUsuarios = qtdUsuariosTmp;
		                 no = noTmp;
		             }	             
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
         
         }
         if (!perfilExistente)
         {
        	 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro"))
        	 {
        		 if(existeAdminFraude && Acesso.equalsIgnoreCase("adminFraude"))
        		 {
        			 Perfis.iniciaOperacao("Erro ao incluir perfil! Já existe um perfil com acesso AdminFraude");
        		 }
        		 else
        		 {
        			 if(maxId>0)
        			 {
        				 if (no.getConexaoServUtil().incluiPerfilPorId(Perfil, maxId, Acesso, sigilo, idTecnologias , todasTecnologias, idRelatorios, todosRelatorios, idRelsHist, todosRelsHist))
	    	             {
	    	                 Perfis.iniciaOperacao("Perfil incluído!");
	    	             }
	    	             else
	    	             {
	    	                 Perfis.iniciaOperacao("Erro ao incluir perfil!");
	    	             }
        			 }        				 
        			 else
        			 {
        			 if (no.getConexaoServUtil().incluiPerfil(Perfil, Acesso, sigilo,idTecnologias , todasTecnologias, idRelatorios, todosRelatorios, idRelsHist, todosRelsHist))
    	             {
    	                 Perfis.iniciaOperacao("Perfil incluído!");
    	             }
    	             else
    	             {
    	                 Perfis.iniciaOperacao("Erro ao incluir perfil!");
    	             }
        		 }
        	 }
        	 }
        	 else
        	 {
        		 if(maxId>0)
    			 {
        			 if (no.getConexaoServUtil().incluiPerfilPorId(Perfil, maxId, Acesso, sigilo,idTecnologias , todasTecnologias, idRelatorios, todosRelatorios, idRelsHist, todosRelsHist))
		             {
		                 Perfis.iniciaOperacao("Perfil incluído!");
		             }
		             else
		             {
		                 Perfis.iniciaOperacao("Erro ao incluir perfil!");
		             }
    			 }
        	 else
        	 {
	             if (no.getConexaoServUtil().incluiPerfil(Perfil, Acesso, sigilo,idTecnologias , todasTecnologias, idRelatorios, todosRelatorios, idRelsHist, todosRelsHist))
	             {
	                 Perfis.iniciaOperacao("Perfil incluído!");
	             }
	             else
	             {
	                 Perfis.iniciaOperacao("Erro ao incluir perfil!");
	             }
        	 }
         }
         }
         else
         {
        	 Perfis.iniciaOperacao("Erro ao incluir perfil! Perfil já existe!");
         }
      }
      catch (Exception Exc)
      {
         System.out.println("OpIncluiPerfil - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }

      return true;
   }
}
