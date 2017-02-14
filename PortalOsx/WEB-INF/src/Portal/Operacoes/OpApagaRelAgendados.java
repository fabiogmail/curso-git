//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpApagaRelAgendados.java

package Portal.Operacoes;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.UsuarioDef;

/**
 */
public class OpApagaRelAgendados extends OperacaoAbs 
{
   
   static 
   {
   }
   
   /**
    * @return 
    * @exception 
    * @roseuid 3D779B7B03CF
    */
   public OpApagaRelAgendados() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3D779B7B03D9
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      String ListaRelatorios = null, RelatorioAux = null,
             Relatorio = null, Perfil = null, TipoRel = null, IDRel = null;
      UsuarioDef Usuario;

      try
      {
         setOperacao("Apaga Relatórios Agendados");
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

            IDRel = RelatorioAux.substring(0, RelatorioAux.indexOf(','));
            RelatorioAux = RelatorioAux.substring(RelatorioAux.indexOf(',')+1, RelatorioAux.length());

            Relatorio = RelatorioAux.substring(0, RelatorioAux.length());
            
            no = NoUtil.buscaNobyNomePerfil(Perfil);

            if (ListaRelatorios.indexOf(';') + 1 == ListaRelatorios.length())
               ListaRelatorios = null;
            else
               ListaRelatorios = ListaRelatorios.substring(ListaRelatorios.indexOf(';')+1, ListaRelatorios.length());

            //System.out.println("ListaRelatorios: "+ListaRelatorios);
            no.getConexaoServUtil().apagaRelAgendados(Perfil, Relatorio, IDRel, (short) 0, TipoRel);
         }

         OpListaRelAgendados RelAgendados = new OpListaRelAgendados();
         RelAgendados.setRequestResponse(getRequest(), getResponse());
         RelAgendados.iniciaOperacao("$P"+Perfil+"-T"+TipoRel);

         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpApagaRelAgendados - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
}
