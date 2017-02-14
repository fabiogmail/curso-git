//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApagaRelHistoricos.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpApagaRelHistoricos extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D77AAFB003C
    */
   public OpApagaRelHistoricos() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D77AAFB006E
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      String ListaRelatorios = null, RelatorioAux = null,
             Relatorio = null, Perfil = null, TipoRel = null, IDRel = null;
      UsuarioDef Usuario;

      try
      {
         setOperacao("Apaga Relatórios Históricos");
         ListaRelatorios = m_Request.getParameter("relatorios");
         No no = null;
         
         while (ListaRelatorios != null)
         {
            RelatorioAux = ListaRelatorios.substring(0, ListaRelatorios.indexOf(';'));
            //System.out.println("RelatorioAux: "+RelatorioAux);

            Perfil = RelatorioAux.substring(0, RelatorioAux.indexOf(','));
            RelatorioAux = RelatorioAux.substring(RelatorioAux.indexOf(',')+1, RelatorioAux.length());
            //System.out.println("Perfil: "+Perfil);

            TipoRel = RelatorioAux.substring(0, RelatorioAux.indexOf(','));
            RelatorioAux = RelatorioAux.substring(RelatorioAux.indexOf(',')+1, RelatorioAux.length());
            //System.out.println("TipoRel: "+TipoRel);

            IDRel = RelatorioAux.substring(0, RelatorioAux.indexOf(','));
            RelatorioAux = RelatorioAux.substring(RelatorioAux.indexOf(',')+1, RelatorioAux.length());
            //System.out.println("IDRel: "+IDRel);

            Relatorio = RelatorioAux.substring(0, RelatorioAux.length());
            //System.out.println("Relatorio: "+Relatorio);
            //System.out.println("-----------------------");

            no = NoUtil.buscaNobyNomePerfil(Perfil);
            
            if (ListaRelatorios.indexOf(';') + 1 == ListaRelatorios.length())
               ListaRelatorios = null;
            else
               ListaRelatorios = ListaRelatorios.substring(ListaRelatorios.indexOf(';')+1, ListaRelatorios.length());

            //System.out.println("ListaRelatorios: "+ListaRelatorios);
            no.getConexaoServUtil().apagaRelHistoricos(Perfil, Relatorio, IDRel, (short) 1, TipoRel);
         }

         OpListaRelHistoricos RelHistoricos = new OpListaRelHistoricos();
         RelHistoricos.setRequestResponse(getRequest(), getResponse());
         RelHistoricos.iniciaOperacao("$P"+Perfil+"-T"+TipoRel);

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpListaRelHistoricos - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
