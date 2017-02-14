//Source file: C:/usr/osx/CDRView/Servlet/Portal/Operacoes/OpFormExportaBase.java

package Portal.Operacoes;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.BilhetadorCfgDef;

/**
 */
public class OpFormExportaBase extends OperacaoAbs 
{
   
   /**
    * @return 
    * @exception 
    * @roseuid 3C6D480B011E
    */
   public OpFormExportaBase() 
   {
   }
   
   /**
    * @param p_Mensagem
    * @return boolean
    * @exception 
    * @roseuid 3C6D480B0128
    */
   public boolean iniciaOperacao(String p_Mensagem) 
   {
      //System.out.println("OpFormExportaBase - iniciaOperacao()");
      try
      {
         setOperacao("Exportação de Base");
         iniciaArgs(7);
         m_Args[0] = NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_HTML();
         m_Args[1] = "formgen.htm";
         m_Args[2] = "src=\"/PortalOsx/templates/js/exportabase.js\"";
         m_Args[3] = "onLoad=\"Processa(0)\"";
         m_Args[4] = "exportabase.gif";
         m_Args[5] = m_Html.criaFormulario(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_FORMS(), "exportabase.form", montaFormulario(p_Mensagem));
         m_Args[6] = m_Html.leArquivo(NoUtil.getNo().getDiretorioDefs().getS_DIR_TMPL_TXT(), "exportabase.txt");
         m_Html.enviaArquivo(m_Args);
         return true;
      }
      catch (Exception Exc)
      {
         System.out.println("OpFormExportaBase - iniciaOperacao(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @param p_Mensagem
    * @return String[]
    * @exception 
    * @roseuid 3C6D480B013C
    */
   public String[] montaFormulario(String p_Mensagem) 
   {
      boolean bMudou = false;
      BilhetadorCfgDef Bilhetador;
      int QtdElem;
      List Bilhetadores = new Vector(20);
      final int QTD_ARGS = 25;
      String Args[] = new String[QTD_ARGS];
      String DiaInicio, MesInicio, AnoInicio, HoraInicio, MinutoInicio, SegundoInicio,
             DiaFim, MesFim, AnoFim, HoraFim, MinutoFim, SegundoFim;
      String DataAtual = NoUtil.getNo().getConexaoServUtil().getDataHoraAtual(), Aux = null, Inicio = null, Fim = null;
      short QtdDiasDoMes[] = {31,28,31,30,31,30,31,31,30,31,30,31};

      Args[0] = "";
      
      No noTmp = null;

      for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
      {
          try
          {
              noTmp = (No) iter.next();
              Bilhetadores.addAll(noTmp.getConexaoServUtil().getListaBilhetadoresCfg());
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
      
      Collections.sort(Bilhetadores);
      
      //Bilhetadores.trimToSize();
      
      if (Bilhetadores != null)
      {
         // Monta lista de relacionamentos
         for (short i = 0; i < Bilhetadores.size(); i++)
         {
            Bilhetador = (BilhetadorCfgDef) Bilhetadores.get(i);
            if (i == Bilhetadores.size() - 1)
               Args[0] += Bilhetador.getBilhetador();
            else
               Args[0] += Bilhetador.getBilhetador() + ";";
         }
      }

      Args[1] = "";
      // Como todos os Nos vao ter a mesma lista de FDS, pode-se usar qqer referencia de No
      Args[1] = NoUtil.getNo().getConexaoServUtil().getListaFDS(); 
      if (Args[1] == null)
         Args[1] = "$ARG;";

      Args[2] = "";
      // Como todos os Nos vao ter a mesma lista de Tipos de Chamada, pode-se usar qqer referencia de No
      Args[2] = NoUtil.getNo().getConexaoServUtil().getTiposCham();
      if (Args[2] == null)
         Args[2] = "$ARG;";

      if (p_Mensagem.indexOf("@") == -1)
      {
         Args[3] = p_Mensagem;
         for (short i = 16; i < QTD_ARGS; i++)
            Args[i] = "";
      }
      else
      {
        Args[3] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));   // Mensagem
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());

        Inicio = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Fim = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[16] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Bilhetador
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[17] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Tipos de Chamada
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[18] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Origem
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[19] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Destino
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[20] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Encaminhado
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[21] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Rota de Entrada
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[22] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Rota de Saída
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[23] = p_Mensagem.substring(0, p_Mensagem.indexOf("@"));  // Finais de Seleção
        p_Mensagem = p_Mensagem.substring(p_Mensagem.indexOf("@")+1, p_Mensagem.length());
        Args[24] = p_Mensagem;  // Qtd de CDRs
      }

      if (Fim == null) Aux = new String(DataAtual);
      else Aux = new String(Fim);

      DiaFim = Aux.substring(0, Aux.indexOf("/"));
      Aux = Aux.substring(Aux.indexOf("/")+1, Aux.length());
      MesFim = Aux.substring(0, Aux.indexOf("/"));
      Aux = Aux.substring(Aux.indexOf("/")+1, Aux.length());
      AnoFim = Aux.substring(0, Aux.indexOf(" "));
      Aux = Aux.substring(Aux.indexOf(" ")+1, Aux.length());
      HoraFim = Aux.substring(0, Aux.indexOf(":"));
      Aux = Aux.substring(Aux.indexOf(":")+1, Aux.length());      
      MinutoFim = Aux.substring(0, Aux.indexOf(":"));
      Aux = Aux.substring(Aux.indexOf(":")+1, Aux.length());            
      SegundoFim = Aux;

      int iDia, iMes, iAno;
      iDia = Integer.parseInt(DiaFim);
      iMes = Integer.parseInt(MesFim);
      iAno = Integer.parseInt(AnoFim);

      if (iDia == 1)
      {
         iDia = QtdDiasDoMes[iMes-1];
         bMudou = true;
      }
      else iDia -= 1;

      if (bMudou)
      {
        if (iMes == 1) iMes = 12;
        else iMes -= 1;
        if (iMes == 12) iAno -= 1;
      }
      
      if (Inicio == null)
      {
         DiaInicio = ""+iDia;
         MesInicio = ""+iMes;
         AnoInicio = ""+iAno;
         HoraInicio = new String (HoraFim);
         MinutoInicio = new String (MinutoFim);
         SegundoInicio = new String (SegundoFim);
      }
      else
      {
         DiaInicio = Inicio.substring(0, Inicio.indexOf("/"));
         Inicio = Inicio.substring(Inicio.indexOf("/")+1, Inicio.length());
         MesInicio = Inicio.substring(0, Inicio.indexOf("/"));
         Inicio = Inicio.substring(Inicio.indexOf("/")+1, Inicio.length());
         AnoInicio = Inicio.substring(0, Inicio.indexOf(" "));
         Inicio = Inicio.substring(Inicio.indexOf(" ")+1, Inicio.length());
         HoraInicio = Inicio.substring(0, Inicio.indexOf(":"));
         Inicio = Inicio.substring(Inicio.indexOf(":")+1, Inicio.length());      
         MinutoInicio = Inicio.substring(0, Inicio.indexOf(":"));
         Inicio = Inicio.substring(Inicio.indexOf(":")+1, Inicio.length());            
         SegundoInicio = Inicio;
      }
      
      Args[4] = DiaInicio;
      Args[5] = MesInicio;
      Args[6] = AnoInicio;
      Args[7] = HoraInicio;
      Args[8] = MinutoInicio;
      Args[9] = SegundoInicio;
      Args[10] = DiaFim;
      Args[11] = MesFim;
      Args[12] = AnoFim;
      Args[13] = HoraFim;
      Args[14] = MinutoFim;
      Args[15] = SegundoFim;
     
      return Args;
   }

}
