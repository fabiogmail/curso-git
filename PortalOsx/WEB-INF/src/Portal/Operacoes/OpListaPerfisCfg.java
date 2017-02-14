//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Operacoes/OpListaPerfisCfg.java

package Portal.Operacoes;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.PerfilCfgDef;


public class OpListaPerfisCfg extends OperacaoAbs 
{
   private Vector Perfis = new Vector();
   
   static 
   {
      //System.out.println("OpListaPerfisCfg - Carregando classe");
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C386D1E0317
    */
   public OpListaPerfisCfg() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C386D1E032B
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpListaPerfisCfg - iniciaOperacao(): "+m_Mensagem);

      try
      {
 
         setOperacao("Lista Perfis Configurados");
         String Args[], Tipo[], PerfisExistentes = "", Bloqueio = "";
                  
         No noTmp = null;

         for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
         {
         	 try
			 {
	             noTmp = (No) iter.next();
	             Perfis.addAll(noTmp.getConexaoServUtil().getListaPerfisCfg());
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

         for (int i = 0; i < Perfis.size(); i++)
         {
            PerfilCfgDef Perfil = (PerfilCfgDef)Perfis.elementAt(i);
            PerfisExistentes += Perfil.getPerfil() + ";";
            if (Perfil.getBloqueio() == true)
               Bloqueio += "1;";
            else
               Bloqueio += "0;";
         }
         PerfisExistentes = PerfisExistentes.substring(0, PerfisExistentes.length()-1);
         Bloqueio = Bloqueio.substring(0, Bloqueio.length()-1);

         Tipo = m_Request.getParameterValues("tipo");
         Args = new String[6];
         montaTabela();
         Args[0] = Tipo[0];
         Args[1] = Integer.toString(Perfis.size());
         Args[2] = PerfisExistentes;
         Args[3] = Bloqueio;
         Args[4] = p_Mensagem;
         Args[5] = m_Html.m_Tabela.getTabelaString();

         m_Args = new String[7];
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "listagen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/listaperfiscfg.js\"";
         m_Args[3] = "onLoad=\"PreloadImages('/PortalOsx/imagens/aberto.gif','/PortalOsx/imagens/fechado.gif'); IniciaDesconexao(); VerificaMensagem()\""; // onload
         m_Args[4] = "listaperfis.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "listaperfiscfg.form", Args);
         m_Args[6] = m_Html.leFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "listaperfiscfg.txt", null);
         m_Html.enviaArquivo(m_Args);
         
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaPerfisCfg - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();		
         return false;
      }
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C51B5B902CA
    */
   public Vector montaLinhas() 
   {
      int PosUltimoItem = -1;
      PerfilCfgDef Perfil;
      String Check, NaoCheck;
      Vector Linhas = new Vector(), Colunas = null;
      
      No noTmp = null;

      for (int i = 0; i < Perfis.size(); i++)
      {
         Perfil = (PerfilCfgDef)Perfis.elementAt(i);
         if (Perfil.getPerfil().equals(m_Mensagem) == true)
            PosUltimoItem = i;

         Colunas = new Vector();
         Colunas.add(Perfil.getPerfil());
         
         if(NoUtil.isAmbienteEmCluster())
         	Colunas.add(Perfil.getHost());
         
         Colunas.add(new Integer(Perfil.getId()).toString());
         Colunas.add(Perfil.getAcessoNome());
         Colunas.add(new Integer(Perfil.getAcesso()).toString());

         if (Perfil.getPerfil().equals(DefsComum.s_PRF_ADMIN) == false)
         {
            if (Perfil.getBloqueio() == false)
            {
               Check = "fechado";
               NaoCheck = "aberto";
            }
            else
            {
               Check = "aberto";
               NaoCheck = "fechado";
            }

            //Colunas.add("<input type=\"checkbox\" name=\"bloqueia\" value=\""+Perfil.getPerfil()+"\""+Check+">\n");
            //Colunas.add("<input type=\"image\" src=\"/PortalOsx/imagens/deselec.gif\" name=\"bloqueia\" value=\""+Perfil.getPerfil()+"\""+Check+" onClick=\"MM_swapImage('Image1','','/PortalOsx/imagens/selec.gif',1)\" border=\"0\">\n");
            Colunas.add("<a href=\"javascript:;\" onClick=\"SwapImage('Image"+i+"','','/PortalOsx/imagens/"+Check+".gif',1,'"+i+"','"+Perfil.getPerfil()+"')\" onmouseover=\"window.status='Marca o perfil para bloqueio/desbloqueio';return true;\" onmouseout=\"window.status='';return true;\"><img src=\"/PortalOsx/imagens/"+NaoCheck+".gif\" border=\"0\" name=\"Image"+i+"\"></a>\n");
         }
         else
            Colunas.add("N/A");

         Colunas.trimToSize();
         Linhas.add(Colunas);
      }
      Linhas.add(new Integer(PosUltimoItem));
      Linhas.trimToSize();
      return Linhas;
   }

   /**
    * @return void
    * @exception
    * @roseuid 3C5848F40368
    */
   public void montaTabela()
   {
      int PosUltimoItem = -1;
      String Tipo[];
      Vector Linhas;
      
      Linhas = montaLinhas();
      Integer Aux = (Integer)Linhas.elementAt(Linhas.size()-1);
      PosUltimoItem = Aux.intValue();
      Linhas.removeElementAt((Linhas.size()-1));

      Tipo = m_Request.getParameterValues("tipo");
      if (Tipo != null)
      {
         if (Tipo[0].equals("parcial") == true)
            m_Html.setTabela((short)5, true);
         else
            m_Html.setTabela((short)5, false);
      }
      else
      {
         Tipo = new String[1];
         Tipo[0] = "parcial";
         m_Html.setTabela((short)5, true);
      }
      
      String Header[];
      String Largura[];
      String Alinhamento[];
      short Filtros[];      
      
      if(NoUtil.isAmbienteEmCluster())
      {
	      Header = new String[] {"Perfil", "Servidor", "Id Perfil", "Acesso", "Id Acesso", "Bloqueia"};
	      Largura = new String[] {"150", "90", "64", "130", "93", "116"};
	      Alinhamento = new String[] {"left", "center", "center", "center", "center", "center"};
	      Filtros = new short[] {1, 0, 0, 1, 0, 0};
      }
      else
      {
	      Header = new String[] {"Perfil", "Id Perfil", "Acesso", "Id Acesso", "Bloqueia"};
	      Largura = new String[] {"150", "64", "130", "93", "116"};
	      Alinhamento = new String[] {"left", "center", "center", "center", "center"};
	      Filtros = new short[] {1, 0, 1, 0, 0};
      }
      
      m_Html.m_Tabela.setHeader(Header);
      m_Html.m_Tabela.setLarguraColunas(Largura);
      m_Html.m_Tabela.setCellPadding((short)2);
      m_Html.m_Tabela.setQtdItensPagina(DefsComum.s_QTD_ITENS_TABELA);
      m_Html.m_Tabela.setApresentaIndice(false);
      m_Html.m_Tabela.setFiltros(Filtros);
      m_Html.m_Tabela.setAlinhamento(Alinhamento);
      m_Html.m_Tabela.setAlturaColunas((short)20);
      m_Html.m_Tabela.setLink("/PortalOsx/servlet/Portal.cPortal?operacao=listaPerfis&tipo="+Tipo[0]);
      m_Html.m_Tabela.setElementos(Linhas);

      if (PosUltimoItem != -1)
         m_Html.m_Tabela.setOffSet((short)((((PosUltimoItem/DefsComum.s_QTD_ITENS_TABELA)+1)-1)*DefsComum.s_QTD_ITENS_TABELA));
      else
         m_Html.trataTabela(m_Request, Linhas);
      m_Html.m_Tabela.enviaTabela2String();
   }
}
