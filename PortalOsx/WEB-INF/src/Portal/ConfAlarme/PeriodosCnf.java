package Portal.ConfAlarme;


public class PeriodosCnf
{
   public String m_Periodo;
   public String m_Inicio;
   public String m_Fim;

   public PeriodosCnf()
   {
      m_Periodo = "";
      m_Inicio = "";
      m_Fim = "";
   }

   public boolean fnLeCnf(String p_Linha)
   {
      boolean l_JaLeuPeriodo = false;
      boolean l_JaLeuInicio = false;

      for (int a=0; a<p_Linha.length(); a++)
      {
         char l_Car = p_Linha.charAt(a);

         if (l_Car != ' ')
         {
            if (! l_JaLeuPeriodo)
            {
               if (l_Car != ':')
                  m_Periodo += l_Car;
               else
                  l_JaLeuPeriodo = true;
            } else
            {
               if (! l_JaLeuInicio)
               {
                  if (l_Car != '-')
                     m_Inicio += l_Car;
                  else
                     l_JaLeuInicio = true;
               } else
               {
                  m_Fim += l_Car;
               }
            }
         }
      }
      return true;
   }

   public void fnSalva(java.io.BufferedWriter p_Arquivo) throws Exception
   {
      try
      {
        p_Arquivo.write(m_Periodo+" : "+m_Inicio+"-"+m_Fim);
        p_Arquivo.newLine();
      } catch(Exception e)
      {
         throw (e);
      }
   }
}
