package Portal.ConfAlarme;

import java.util.StringTokenizer;

public class GruposCnfParm
{
   public int m_qtdP;
   public int m_P1;
   public int m_P2;
   public boolean m_Hab;
   public String m_Comentario;

   public GruposCnfParm()
   {
      m_qtdP = 0;
      m_P1 = 0;
      m_P2 = 0;
      m_Hab = false;
      m_Comentario = "";
   }

   public GruposCnfParm fnCopia()
   {
      GruposCnfParm l_Copia = new GruposCnfParm();
      l_Copia.m_qtdP = m_qtdP;
      l_Copia.m_P1 = m_P1;
      l_Copia.m_P2 = m_P2;
      l_Copia.m_Hab = m_Hab;
      l_Copia.m_Comentario = m_Comentario;

      return l_Copia;
   }

   public static boolean fnTesta(String p_Linha)
   {
      StringTokenizer l_Str = new StringTokenizer(p_Linha);
      int l_P1;
      int l_P2;
      boolean l_Hab = false;
      String l_Comentario;
      int l_qtdP = 0;

      try
      {
//System.out.println("F1 "+p_Linha);
         if (l_Str.hasMoreTokens())
         {
            l_P1 =  new Integer(l_Str.nextToken()).intValue();
         } else
            return false;
         l_qtdP++;

//System.out.println("F2 "+l_P1);
         if (l_Str.hasMoreTokens())
         {
            String l_Tk = l_Str.nextToken();
//System.out.println("F3 "+l_Tk+" "+l_Tk.charAt(0));
            if ((l_Tk.charAt(0) == 'H') || (l_Tk.charAt(0) == 'D'))
            {
               if (l_Tk.charAt(0) == 'H')
               {
                  l_Hab = true;
               } else
               {
                  l_Hab = true;
               }
//System.out.println("F4 "+(l_Hab?"TRUE":"FALSE"));
            } else
            {
               l_P2 =  new Integer(l_Tk).intValue();
               l_qtdP++;
//System.out.println("F5 "+l_P2);
            }
         } else
            return false;

//System.out.println("F6 "+l_qtdP);
         if (l_qtdP == 2)
         {
            if (l_Str.hasMoreTokens())
            {
               String l_Tk = l_Str.nextToken();
//System.out.println("F7 "+l_Tk+" "+l_Tk.charAt(0));
               if (l_Tk.charAt(0) == 'H')
               {
                  l_Hab = true;
               } else if (l_Tk.charAt(0) == 'D')
               {
                  l_Hab = false;
               } else
                  return false;
            } else
               return false;
         }

//System.out.println("F8 "+(l_Hab?"TRUE":"FALSE"));
         l_Comentario = "";
         if (l_Str.hasMoreTokens())
         {
            l_Str.nextToken();
            while (l_Str.hasMoreTokens())
               l_Comentario += l_Str.nextToken() + " ";
         } else
            return false;
//System.out.println("F9 "+l_Comentario);
      } catch(Exception e)
      {
//         e.printStackTrace();
         return false;
      }
      return true;
   }

   public boolean fnLeCfn(String p_Linha)
   {
      StringTokenizer l_Str = new StringTokenizer(p_Linha);
      try
      {
         if (l_Str.hasMoreTokens())
            m_P1 =  new Integer(l_Str.nextToken()).intValue();
         else
            return false;
         m_qtdP++;

         if (l_Str.hasMoreTokens())
         {
            String l_Tk = l_Str.nextToken();
// System.out.print("Lendo 1"+l_Tk);
            if ((l_Tk.charAt(0) == 'H') || (l_Tk.charAt(0) == 'D'))
            {
               if (l_Tk.charAt(0) == 'H')
                  m_Hab = true;
               else
                  m_Hab = false;
            } else
            {
               m_P2 =  new Integer(l_Tk).intValue();
               m_qtdP++;
            }
         } else
            return false;

         if (m_qtdP == 2)
         {
            if (l_Str.hasMoreTokens())
            {
               String l_Tk = l_Str.nextToken();
// System.out.print(" Lendo 2"+l_Tk);
               if (l_Tk.charAt(0) == 'H')
                  m_Hab = true;
               else if (l_Tk.charAt(0) == 'D')
                  m_Hab = false;
               else
                  return false;
            } else
               return false;
         }

         m_Comentario = "";
         if (l_Str.hasMoreTokens())
         {
           l_Str.nextToken();
           while (l_Str.hasMoreTokens())
               m_Comentario += l_Str.nextToken() + " ";
         } else
            return false;
//         System.out.println(" Lendo "+m_Comentario+", P1="+m_P1+" "+(m_Hab?"HAB":"DES"));
      } catch(Exception e)
      {
         return false;
      }
      return true;
   }

   public void fnSalva(java.io.BufferedWriter p_Arquivo) throws Exception
   {
      try
      {
        if (m_qtdP == 1)
           p_Arquivo.write(""+m_P1+" "+(m_Hab?"H":"D")+"\t\t\t# "+m_Comentario);
        else
           p_Arquivo.write(""+m_P1+" "+m_P2+" "+(m_Hab?"H":"D")+"\t\t\t# "+m_Comentario);
        p_Arquivo.newLine();
      } catch(Exception e)
      {
         throw (e);
      }
   }
}