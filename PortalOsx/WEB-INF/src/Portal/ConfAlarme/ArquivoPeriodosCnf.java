package Portal.ConfAlarme;

import java.util.Vector;

import Portal.Cluster.NoUtil;

public class ArquivoPeriodosCnf
{
   private static String m_Nome;
   public Vector m_Periodos;

	static public void main(String p_Args[])
   {
//      DiretoriosDefs.s_DIR_ARQS_PERIODO = "c:/usr/osx/cdrview/exec/Embratel/cfgsis/AgnCDR/";
      
      try
      {
         ArquivoPeriodosCnf l_ArquivoPeriodosCnf = new ArquivoPeriodosCnf();

         for (int a=0 ; a<l_ArquivoPeriodosCnf.m_Periodos.size() ; a++)
         {
            PeriodosCnf l_PeriodosCnf = (PeriodosCnf) l_ArquivoPeriodosCnf.m_Periodos.elementAt(a);
            System.out.println ("P="+l_PeriodosCnf.m_Periodo+", I="+l_PeriodosCnf.m_Inicio+", F="+l_PeriodosCnf.m_Fim);
         }
      } catch (Exception e)
      {
         System.out.println(e.toString());
      }
   }

   public ArquivoPeriodosCnf() throws Exception
   {
      m_Nome = NoUtil.getNo().getDiretorioDefs().getS_DIR_ARQS_PERIODO()+"periodos.txt";

      m_Periodos = new Vector();
      java.io.BufferedReader l_Arquivo;
      try
      {
         l_Arquivo = new java.io.BufferedReader(new java.io.FileReader(m_Nome));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao abrir arquivo "+m_Nome));
      }

      PeriodosCnf l_PeriodosCnf = null;
      String l_Linha;

      l_Linha = l_Arquivo.readLine();
      while (l_Linha != null)
      {
         l_PeriodosCnf = new PeriodosCnf();
         if (! l_PeriodosCnf.fnLeCnf(l_Linha))
            throw (new Exception("Erro de parser 1 - Arquivo "+m_Nome+" corrompido na linha "+l_Linha));
         m_Periodos.add(l_PeriodosCnf);
         l_Linha = l_Arquivo.readLine();
      }

      l_Arquivo.close();
   }

   public void fnSalva() throws Exception
   {
      java.io.BufferedWriter l_Arquivo;
      try
      {
         l_Arquivo = new java.io.BufferedWriter(new java.io.FileWriter(m_Nome));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao abrir arquivo "+m_Nome));
      }

      try
      {
        for (int a=0; a<m_Periodos.size(); a++)
        {
          ((PeriodosCnf) m_Periodos.elementAt(a)).fnSalva(l_Arquivo);
        }

        l_Arquivo.close();
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao escrever arquivo "+m_Nome));
      }
   }
}