//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpExportaBase.java

package Portal.Operacoes;

import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpExportaBase extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6FF820013E
    */
   public OpExportaBase() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6FF820015C
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpExportaBase - iniciaOperacao()");
      try
      {
         short Retorno;
         UsuarioDef Usuario = (UsuarioDef)NoUtil.getUsuarioLogado(m_Request.getSession().getId());
         setOperacao("Exportação de Base");
         String Bilhetador = m_Request.getParameter("bilhetador");
         String TiposCham  = m_Request.getParameter("tiposcham");         
         String Inicio     = m_Request.getParameter("inicio");
         String Fim        = m_Request.getParameter("fim");
         String NumA       = m_Request.getParameter("numa");
         String NumB       = m_Request.getParameter("numb");
         String NumC       = m_Request.getParameter("numc");
         String RotaEnt    = m_Request.getParameter("rotaent");
         String RotaSai    = m_Request.getParameter("rotasai");
         String FDS        = m_Request.getParameter("fds");
         String QtdCDRs    = m_Request.getParameter("qtdcdrs");
         String Arquivo    = m_Request.getParameter("arquivo");

         if (TiposCham.equals("N/A"))
            TiposCham = "-1";
         if (NumA.length() == 0)
            NumA = "*";
         if (NumB.length() == 0)
            NumB = "*";
         if (NumC.length() == 0)
            NumC = "*";
         if (RotaEnt.length() == 0)
            RotaEnt = "*";
         if (RotaSai.length() == 0)
            RotaSai = "*";
         if (QtdCDRs.length() == 0)
            QtdCDRs = "-1";

         OpFormExportaBase FormExportaBase = new OpFormExportaBase();
         FormExportaBase.setRequestResponse(getRequest(), getResponse());

         String Msg;
         if (Usuario != null)
         {
            Retorno = Usuario.getNo().getConexaoServUtil().exportaBase(Usuario.getIdPerfil(),
								                                       Bilhetador,
								                                       TiposCham,
								                                       Inicio, Fim,
								                                       NumA, NumB, NumC,
								                                       RotaEnt, RotaSai,
								                                       FDS, Integer.parseInt(QtdCDRs),
								                                       Arquivo);

            switch (Retorno)
            {
               case 0:
               default:
                  Msg = "Exportação está sendo realizada!";
                  break;
               case 1:
                  Msg = "Datas estão incorretas!";
                  break;
               case 2:
                  Msg = "Só é possível exportar 1 dia de base!";
                  break;
               case 3:
                  Msg = "Não existe base para o período selecionado!";
                  break;
               case 4:
                  Msg = "Exportação não pode ser feita devido à falta de espaço em disco!";
                  break;
               case 5: // Erro na Thread 
                  Msg = "Exportação não pode ser feita devido a problemas técnicos no servidor!";
                  break;
               case 6:
                  Msg = "Já existe base exportada com o nome \""+Arquivo+"\"!";
                  break;
            }
         }
         else
            Msg = "Erro na exportação! Avise o administrador.";

         Msg += "@"+Inicio+"@"+Fim+"@"+Bilhetador+"@"+TiposCham+"@"+NumA+"@"+NumB+"@"+NumC+"@"+RotaEnt+"@"+RotaSai+"@"+FDS+"@"+QtdCDRs;
//System.out.println("Nova Msg: "+Msg);         
         FormExportaBase.iniciaOperacao(Msg);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpExportaBase - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
