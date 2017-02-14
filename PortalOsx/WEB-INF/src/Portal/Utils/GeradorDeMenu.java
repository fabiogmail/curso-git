//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/GeradorDeMenu.java

package Portal.Utils;

import java.util.Properties;
import java.util.Vector;

/**
 */
public class GeradorDeMenu 
{
   private String m_NomeMenu;
   private String m_MenuGerado = "";
   public Arquivo m_ArqMenu;
   
   static 
   {
   }
   
   /**
    * @param p_NomeMenu
    * @param p_Diretorio
    * @param p_Arquivo
    * @return 
    * @exception 
    * @roseuid 3E19BCB601A8
    */
   public GeradorDeMenu(String p_NomeMenu, String p_Diretorio, String p_Arquivo) 
   {
      m_NomeMenu = p_NomeMenu;
      
      m_ArqMenu = new Arquivo();
      m_ArqMenu.setDiretorio(p_Diretorio);
      m_ArqMenu.setNome(p_Arquivo);
   }
   
   /**
    * @return void
    * @exception 
    * @roseuid 3E19BD2702B9
    */
   public void geraMenu() 
   {
      String Linha = null, Coluna = null, Opcao = null;
      Vector Colunas = null;
      Properties Prop = null;

      if (m_ArqMenu.abre('r'))
      {
         Prop = System.getProperties();
         Prop.getProperty("line.separator");
//         System.out.println(" -"+ Prop.getProperty("line.separator")+ "- ");

         while ((Linha = m_ArqMenu.leLinha()) != null)
         {
            while (Linha.startsWith(" "))
               Linha = Linha.substring(1, Linha.length());

            if (Linha.startsWith("#") || Linha.length() <= 3)
               continue;
               
//System.out.println(Linha);
            Colunas = VetorUtil.String2Vetor(Linha, ',');
            if (Colunas.size() < 3)
            {
               System.out.println("ERRO !!: Linha (" + Linha + ") errada no : "+m_ArqMenu.getDiretorio() + m_ArqMenu.getNome());
               continue;
            }
//Ex:
//MP,top1,Relat&oacute;rios
//oCMenu.makeMenu('top0','','&nbsp;&#149;&nbsp;CDRView','','frmMain2')
            Coluna = (String)Colunas.elementAt(0);
            if (Coluna.equals("MP") || Coluna.equals("mp"))  // Menu Principal
               Opcao = m_NomeMenu + ".makeMenu('"+(String)Colunas.elementAt(1)+ "','','&nbsp;&#149;&nbsp;"+(String)Colunas.elementAt(2)+"','','')";
//Ex:
//SM,sub12,top1,Exporta&ccedil;&atilde;o de Base
//oCMenu.makeMenu('sub12','top1','&nbsp;&#149;&nbsp;Exportação de Base','')
            else if (Coluna.equals("SM") || Coluna.equals("SM"))  // Submenu
               Opcao = m_NomeMenu + ".makeMenu('"+(String)Colunas.elementAt(1)+ "','"+(String)Colunas.elementAt(2)+
                       "','&nbsp;&#149;&nbsp;"+(String)Colunas.elementAt(3)+"','')";
//Ex:
//SMF,sub00,top0,CDRView Detec&ccedil;&atilde;o,javascript:{AbreJanela("clienteAlr");}
//oCMenu.makeMenu('sub00','top0','&nbsp;&#149;&nbsp;CDRView An&aacute;lise','','','','','','','','','','','javascript:{AbreJanela("cliente");}','','')
            else if (Coluna.equals("SMF") || Coluna.equals("SBF"))  // Submenu com função
               Opcao = m_NomeMenu + ".makeMenu('"+(String)Colunas.elementAt(1)+ "','"+(String)Colunas.elementAt(2)+
                       "','&nbsp;&#149;&nbsp;"+(String)Colunas.elementAt(3)+"','','','','','','','','','','','"+
                       (String)Colunas.elementAt(4)+"','','')";
//Ex:
//SML,sub10,top1,Agendados,/PortalOsx/servlet/Portal.cPortal?operacao=RelAgendados
//oCMenu.makeMenu('sub02','top0','&nbsp;&#149;&nbsp;Java Plug-in','/paginas/plugin.htm')
            else if (Coluna.equals("SML") || Coluna.equals("sml")) // Submenu com link
               Opcao = m_NomeMenu + ".makeMenu('"+(String)Colunas.elementAt(1)+ "','"+(String)Colunas.elementAt(2)+
                       "','&nbsp;&#149;&nbsp;"+(String)Colunas.elementAt(3)+"','"+(String)Colunas.elementAt(4) +
                       "','','','','','','','','','','','','')";
            else
               System.out.println("ERRO !!: OPCAO de menu nao existe: "+ Coluna);
            m_MenuGerado += "\n"+Opcao;
         }
         m_ArqMenu.fecha();
         m_MenuGerado += "\n";         
      }
      else
      {
         // Arquivo não foi aberto
         System.out.println("ERRO !!: Arquivo de menus nao existe: "+m_ArqMenu.getDiretorio() + "/" + m_ArqMenu.getNome());
      }
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3E19CEA40180
    */
   public String getMenuGerado() 
   {
      return m_MenuGerado;
   }
}
