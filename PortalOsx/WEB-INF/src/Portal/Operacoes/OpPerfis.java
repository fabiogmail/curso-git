//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpPerfis.java

package Portal.Operacoes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import CDRView2.Cliente;
import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Conexao.CnxServUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.AcessoCfgDef;
import Portal.Utils.PerfilCfgDef;
import Portal.Utils.Relatorio;
import Portal.Utils.Tecnologia;


public class OpPerfis extends OperacaoAbs 
{
   
   static 
   {
      //System.out.println("OpPerfis - Carregando classe");
   }
   
   public OpPerfis() 
   {
      //System.out.println("OpPerfis - construtor");   
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C21407E018F
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      try
      {
    	 String URL;

    	 if(DefsComum.s_CLIENTE.equalsIgnoreCase("BrasilTelecom"))
    	 {
    		 URL = "/templates/jsp/perfisBRT.jsp";
    	 }
    	 else
    	 {
    		 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Sercomtel"))
    		 {
        		 URL = "/templates/jsp/perfisSercomtel.jsp";
    		 }
    		 else
    		 {
    			 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro"))
    			 {
    				 URL = "/templates/jsp/perfisClaro.jsp";
    			 }
    			 else
    			 {
    				 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Telemig"))
    				 {
    					 URL = "/templates/jsp/perfisTelemig.jsp";
    				 }
    				 else
        			 {
    					 if(DefsComum.s_CLIENTE.equalsIgnoreCase("TimSul"))
    					 {
    						 URL = "/templates/jsp/perfisTimSul.jsp";
    					 }
    					 else
    					 {
    						 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Oi"))
    						 {
    							 URL = "/templates/jsp/perfisOi.jsp";
    						 }
    						 else
        					 {
        						 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Amazonia_celular"))
        						 {
        							 URL = "/templates/jsp/perfisAmazonia_Celular.jsp";
        						 }
    						 else
        					 {
    							 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Embratel"))
        						 {
        							 URL = "/templates/jsp/perfisEBT.jsp";
        						 }
        						 else
            					 {
        							 if(DefsComum.s_CLIENTE.equalsIgnoreCase("Nextel")){
        								 URL = "/templates/jsp/perfisNextel.jsp";
        								 System.out.println("Cliente: "+DefsComum.s_CLIENTE);
        							 }
        							 else{
        							 URL = "/templates/jsp/perfis.jsp";
        							 }
            					 }        						
        					 }
    					 }
    					
        			 } 
    			 }   			 
    		 }
    	   }
    	 }
    		 
    	 
         setOperacao("Configuração de Perfis");
         montaFormulario();
         m_Request.setAttribute("mensagem",p_Mensagem);
         m_Request.getRequestDispatcher(URL).forward(m_Request, m_Response);	
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpPerfis - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C51B2710055
    */
   public void montaFormulario() 
   {
      ArrayList listaTecnologias = new ArrayList();
      ArrayList listaRelatorios = new ArrayList();
      HashMap hashTecnologias = new HashMap();
      HashMap hashRelatorios = new HashMap();
      
      AcessoCfgDef acesso;
      PerfilCfgDef perfil;
      Vector perfis = new Vector(20), acessos = new Vector(20);

      No noTmp = null;
      ArrayList listaTmpNos = new ArrayList();
      StringBuffer tecnologias = new StringBuffer();
      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
      	  try
		  {
      		  noTmp = (No) iter.next();
      		  CnxServUtil cnxUtil = noTmp.getConexaoServUtil();
      		  if(cnxUtil.getM_iUtil()!=null)
      		  {
      			  listaTmpNos.add(noTmp);
      			  perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisCfg());
      			  tecnologias.append(noTmp.getConexaoServUtil().getTecnologias());
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
      acessos.addAll(NoUtil.getNo().getConexaoServUtil().getListaAcessoCfg());
      perfis.trimToSize();
      acessos.trimToSize();
      
      if (tecnologias != null && tecnologias.length()!=0)
      {
    	 String[] idsTecnologias = tecnologias.toString().split("\n");
         // Monta lista com nomes das tecnologias
         for (int i = 0; i < idsTecnologias.length; i++)
         {
        	String[] tecnologiasTmp = idsTecnologias[i].split(";");
        	if(tecnologiasTmp!= null){
        		Tecnologia tecnologia = new Tecnologia();
        		tecnologia.setId(tecnologiasTmp[0]);
        		tecnologia.setNome(tecnologiasTmp[1]);
        		listaTecnologias.add(tecnologia);
        		hashTecnologias.put(tecnologiasTmp[0], tecnologiasTmp[1]);
        	}           
         }
      }
      
      //monta lista de relatorios
      Vector ListaTipoRel = NoUtil.getNo().getConexaoServUtil().getListaTipoRelatorios();
      Relatorio rel;
	  for (int i=0; i < ListaTipoRel.size(); i++)// formato: 0-0:Desempenho
      {
		 rel = new Relatorio();
		 String[] tmp1 = ((String)ListaTipoRel.elementAt(i)).split(":");//0-0,desempenho	 

		 rel.setNome(tmp1[1]);
		 rel.setId(tmp1[0]);
		 listaRelatorios.add(rel);  
		 hashRelatorios.put(rel.getId(),rel.getNome());
      }     
      
      StringBuffer listaNomesTecnologias = new StringBuffer();
      StringBuffer listaIdsTecnologias = new StringBuffer();
      StringBuffer listaNomesRelatorios = new StringBuffer();
      StringBuffer listaIdsRelatorios = new StringBuffer();
      StringBuffer nomesPerfis = new StringBuffer();
      StringBuffer idsPerfis = new StringBuffer(); 
      StringBuffer listaNomeAcessos = new StringBuffer();
      StringBuffer listaRelacionamentos = new StringBuffer();
      StringBuffer listaSigiloTelefonico = new StringBuffer();
      
      
      for (short i = 0; i < perfis.size(); i++)
      {
         perfil = (PerfilCfgDef) perfis.elementAt(i);
         nomesPerfis.append(perfil.getPerfil()+";");
         idsPerfis.append(perfil.getId()+";");
         listaSigiloTelefonico.append(perfil.isSigiloTelefonico()+";");
         boolean temTodosRel = false;
         
         //-------------------------tecnologias----------------------------------------
         
         if(perfil.getIdTecnologias()!=null)
         {
        	 String[] idsTec = perfil.getIdTecnologias().split(";");
        	 for(int j=0; j<idsTec.length; j++)
        	 {
        		 if(idsTec[j].equalsIgnoreCase("todas"))
        		 {
        			 listaNomesTecnologias.append("todas");
        			 listaIdsTecnologias.append("todas");
        		 }
        		 else
        		 {       			     			
    				 if(hashTecnologias.get(idsTec[j])!=null)
    				 {
    					 listaIdsTecnologias.append(idsTec[j]+";");
    					 listaNomesTecnologias.append(hashTecnologias.get(idsTec[j])+";");
    				 }
        		 }
        	 }        	
         }
         
         //retirando o ultimo ";" da lista de tecnologias (ids e nomes)
         if(listaIdsTecnologias.charAt(listaIdsTecnologias.length()-1)==';')
         {
        	 listaIdsTecnologias.deleteCharAt(listaIdsTecnologias.length()-1);
        	 listaNomesTecnologias.deleteCharAt(listaNomesTecnologias.length()-1);
         }
         
         //finalizando a linha com '@'
         if(perfil.getIdTecnologias()==null)
         {
        	 listaNomesTecnologias.append("null"+"@");
        	 listaIdsTecnologias.append("null"+"@");
         }
         else
         {
        	 listaNomesTecnologias.append("@");
        	 listaIdsTecnologias.append("@");
         }
         
         //-----------------------------------fim tecnologias------------------------------
         //-------------------------------------relatorios---------------------------------
         
         //condicional que atende somente os relatorios agendados
         if(perfil.getIdRelatorios()!=null & !perfil.getIdRelatorios().trim().equals(""))
         {
        	 String[] idsRel = perfil.getIdRelatorios().split(";");
        	 for(int j=0; j<idsRel.length; j++)
        	 {
        		 if(idsRel[j].equalsIgnoreCase("todas"))
        		 {
        			 listaNomesRelatorios.append("todas");
        			 listaIdsRelatorios.append("todas");
        			 temTodosRel = true;
        		 }
        		 else
        		 {        			 
        			 if(hashRelatorios.get("0-"+idsRel[j])!= null)
        			 {
        				 listaIdsRelatorios.append("0-"+idsRel[j]+";");
             			 listaNomesRelatorios.append(hashRelatorios.get("0-"+idsRel[j])+";");
             		 }
        		 }
        	 }
	     
         }
         //condicional que atende somente os relatorios não agendados
         if(!temTodosRel)//gato para os que devolvem todos relatorios e id da serie historica
         {	         
	         if(perfil.getIdRelatoriosHistoricos()!=null & !perfil.getIdRelatoriosHistoricos().trim().equals(""))
	         {
	        	 String[] idsRelHist = perfil.getIdRelatoriosHistoricos().split(";");
	        	 for(int j=0; j<idsRelHist.length; j++)
	        	 {
	        		 if(!idsRelHist[j].equalsIgnoreCase("todas"))
	        		 {//se for todas ja foi colocado nos agendados a cima
	        			if(hashRelatorios.get("1-"+idsRelHist[j])!= null)
	        			{
	        				listaIdsRelatorios.append("1-"+idsRelHist[j]+";");
	            			listaNomesRelatorios.append(hashRelatorios.get("1-"+idsRelHist[j])+";");
	        			}        			
	        		 }
	        	 }        	
	         }
         }
         //retirando o ultimo ";" da lista de relatorios (ids e nomes)
         if(listaIdsRelatorios.charAt(listaIdsRelatorios.length()-1)==';')
         {
        	 listaIdsRelatorios.deleteCharAt(listaIdsRelatorios.length()-1);
        	 listaNomesRelatorios.deleteCharAt(listaNomesRelatorios.length()-1);
         }
         
         if(((perfil.getIdRelatoriosHistoricos() == null)|(perfil.getIdRelatoriosHistoricos().equals(""))) &&((perfil.getIdRelatorios() == null)|(perfil.getIdRelatorios().equals(""))))
         {
        	 listaNomesRelatorios.append("null"+"@");
        	 listaIdsRelatorios.append("null"+"@");
         }else{
        	 listaNomesRelatorios.append("@");
        	 listaIdsRelatorios.append("@");
         }
      }
      //----------------------------------fim relatorios ---------------------------------------------
      /**
       * as tecnologias são passadas assim abc;bcd@jfe@todas
       */
      // Monta lista com nomes dos tipos de acesso
      for (short i = 0; i < acessos.size(); i++)
      {
         acesso = (AcessoCfgDef) acessos.elementAt(i);
         listaNomeAcessos.append(acesso.getAcesso()+";");
      }

      // Monta lista de relacionamentos
      for (short i = 0; i < perfis.size(); i++)
      {
         perfil = (PerfilCfgDef) perfis.elementAt(i);
         for (short j = 0; j < acessos.size(); j++)
         {
            acesso = (AcessoCfgDef) acessos.elementAt(j);
            if (perfil.getAcesso() == acesso.getId())
            {
            	listaRelacionamentos.append(acesso.getAcesso()+";");
            	break;
            }
         }
      }
      
     //caso da claro em que ela ja tem que receber os nomes do nós para o usuário poder escolher
     //em qual nó deverá ser cadastrado o perfil
//      ArrayList listaNos = null;
//      if(DefsComum.s_CLIENTE.equalsIgnoreCase("Claro"))
//      {
//    	  listaNos = listaTmpNos;
//      }
      //mudança pra passa sempre a lista de nós para os jsp's que forem usar
      ArrayList listaNos =  listaTmpNos;
      
      m_Request.setAttribute("listaNomesTecnologias",listaNomesTecnologias.substring(0,listaNomesTecnologias.length()-1));
      m_Request.setAttribute("listaNomesRelatorios",listaNomesRelatorios.substring(0,listaNomesRelatorios.length()-1));
      m_Request.setAttribute("listaSigiloTelefonico",listaSigiloTelefonico.substring(0,listaSigiloTelefonico.length()-1));
      m_Request.setAttribute("tecnologias", listaTecnologias);
      m_Request.setAttribute("relatorios",listaRelatorios);
      m_Request.setAttribute("listaNomesPerfis", nomesPerfis.substring(0,nomesPerfis.length()-1));//tirando o ultimo ;
      m_Request.setAttribute("listaIdsPerfis", idsPerfis.substring(0,idsPerfis.length()-1));//tirando o ultimo ;
      m_Request.setAttribute("listaNomeAcessos",listaNomeAcessos.substring(0,listaNomeAcessos.length()-1));
      m_Request.setAttribute("listaRelacionamentos",listaRelacionamentos.substring(0,listaRelacionamentos.length()-1));
      m_Request.setAttribute("listaIdsTecnologias",listaIdsTecnologias.substring(0,listaIdsTecnologias.length()-1));
      m_Request.setAttribute("listaIdsRelatorios",listaIdsRelatorios.substring(0,listaIdsRelatorios.length()-1));
      m_Request.setAttribute("listaNos",listaNos);
   }
}
