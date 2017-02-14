//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/VetorUtil.java

package Portal.Utils;

import java.util.Vector;

/**
 */
public class VetorUtil 
{
   
   /**
    * @param p_String
    * @param p_CharDelimit
    * @return Vector
    * @exception 
    * @roseuid 3C822DB6020A
    */
   public static Vector String2Vetor(String p_String, char p_CharDelimit) 
   {
     if (p_String != null)
      {
         if (p_String.charAt(p_String.length()-1) != p_CharDelimit)
            p_String = p_String + p_CharDelimit;
            
         Vector Vetor = new Vector();
         while  (p_String != null)
         {
            Vetor.add(p_String.substring(0, p_String.indexOf(p_CharDelimit)));
            if (p_String.indexOf(p_CharDelimit) != p_String.length()-1)
               p_String = p_String.substring(p_String.indexOf(p_CharDelimit)+1, p_String.length());
            else
               p_String = null;
         }
         Vetor.trimToSize();
         return Vetor;
      }
      return null;
   }
   
   /**
    * Transforma de String para sem repetir elementos 
    * @param p_String
    * @param p_CharDelimit
    * @return
    */
   public static Vector String2VetorUnique(String p_String, char p_CharDelimit) 
   {
     if (p_String != null)
      {
         if (p_String.charAt(p_String.length()-1) != p_CharDelimit)
            p_String = p_String + p_CharDelimit;
            
         Vector Vetor = new Vector();
         while  (p_String != null)
         {
        	String indicador = p_String.substring(0, p_String.indexOf(p_CharDelimit));
        	if(!Vetor.contains(indicador))
        		Vetor.add(indicador);
            if (p_String.indexOf(p_CharDelimit) != p_String.length()-1)
               p_String = p_String.substring(p_String.indexOf(p_CharDelimit)+1, p_String.length());
            else
               p_String = null;
         }
         Vetor.trimToSize();
         return Vetor;
      }
      return null;
   }
   
   /**
    * @param p_String
    * @return Vector
    * @exception 
    * @roseuid 3C4B181D0101
    */
   public static Vector String2Vetor(String[] p_String) 
   {
      if (p_String != null)
      {
         Vector Vetor = new Vector();
         for (short i = 0; i < p_String.length; i++)
            Vetor.add(p_String[i]);

         Vetor.trimToSize();            
         return Vetor;
      }
      return null;
   }
   
   /**
    * @param p_Vetor
    * @return String[]
    * @exception 
    * @roseuid 3C7C306E0134
    */
   public static String[] Vetor2String(Vector p_Vetor) 
   {
      if (p_Vetor != null && p_Vetor.size() != 0)
      {
         String Retorno[] = new String[p_Vetor.size()];
         for (int i = 0; i < p_Vetor.size(); i++)
            Retorno[i] = (String)p_Vetor.elementAt(i);
            
         return Retorno;
      }
      else
         return null;
   }
   
   /**
    * @param p_Vetor
    * @param p_Coluna
    * @param p_Tipo
    * @return Vector
    * @exception 
    * @roseuid 3C4C527B01D7
    */
   public static Vector ClassificaVetor(Vector p_Vetor, short p_Coluna, char p_Tipo) 
   {
      boolean bTotal = false;
      int i, iTam = 0;
      String Coluna = null;
      Vector Classificado = null, Linha = null, LinhasValorNulo = null;

      Linha = (Vector)p_Vetor.elementAt(p_Vetor.size()-1);
      Coluna = (String)Linha.elementAt(0);
      // Retira linha de total da lista
      if (Coluna.toLowerCase().equals("<b>&nbsp;total&nbsp;</b>") == true)
      {
         p_Vetor.remove(Linha);
         bTotal = true;
      }

      if (p_Tipo == 'N') // Se ordenação for do tipo numérica, retira os valores nulos
      {
         LinhasValorNulo = new Vector();
         iTam = p_Vetor.size();
         Vector Aux;
         String Elemento;
         for (i = 0; i < iTam; i++)
         {
            Aux = (Vector)p_Vetor.elementAt(i);
            Elemento = (String)Aux.elementAt(p_Coluna);
            if (Elemento.equals("-") == true)
            {
               p_Vetor.remove(i);
               LinhasValorNulo.add(Aux);
               i = -1;
               iTam = p_Vetor.size();               
               //System.out.println("Removeu: '"+ Elemento+"'");
            }
         }
         if (LinhasValorNulo != null) LinhasValorNulo.trimToSize();
      }

      if (p_Tipo == 'A')
         Classificado = quickSortAsc(p_Vetor, p_Vetor.size(), 0, p_Vetor.size()-1, (int)p_Coluna);
      else if (p_Tipo == 'D')
         Classificado = quickSortDesc(p_Vetor, p_Vetor.size(), 0, p_Vetor.size()-1, (int)p_Coluna);
      else if (p_Tipo == 'N')
      {
         boolean bTipoInsercao = false;
         //System.out.println("Qtd Linhas Nulas: "+LinhasValorNulo.size());
         // Só ordena se o vetor nao ficou vazio
         if (p_Vetor.size() != 0)
            Classificado = quickSortAscNum(p_Vetor, p_Vetor.size(), 0, p_Vetor.size()-1, (int)p_Coluna);
         else
         {
            iTam = LinhasValorNulo.size();         
            Classificado = new Vector(iTam + 1);         
            bTipoInsercao = true;
         }

         // Recoloca as linhas com valores nulos caso existam
         if (LinhasValorNulo != null)
         {
            iTam = LinhasValorNulo.size();
            for (i = 0; i < iTam; i++)
            {
               if (bTipoInsercao) Classificado.add(Classificado.size(), LinhasValorNulo.elementAt(i));               
               else Classificado.add(0, LinhasValorNulo.elementAt(i));            
            }
         }
      }

      if (bTotal)
         Classificado.add(Linha);
      Classificado.trimToSize();
      return Classificado;
   }
   
   /**
    * @param p_Vetor
    * @param p_High
    * @param p_Lo
    * @param p_Hi
    * @param p_Chave
    * @return Vector
    * @exception 
    * @roseuid 3C4EAFDE00FE
    */
   private static Vector quickSortAsc(Vector p_Vetor, int p_High, int p_Lo, int p_Hi, int p_Chave) 
   {
      int Lo, Hi;
      String Mid, StrAux;
      Vector Aux, Elemento, VAux;


      Lo = p_Lo;
      Hi = p_Hi;
      Aux = (Vector)p_Vetor.elementAt((Lo+Hi)/2);
      Mid = (String)Aux.elementAt(p_Chave);

      do
      {
         VAux = (Vector)p_Vetor.elementAt(Lo);
         StrAux = (String)VAux.elementAt(p_Chave);
         while (StrAux.compareToIgnoreCase(Mid) < 0)
         {
            Lo++;
            VAux = (Vector)p_Vetor.elementAt(Lo);
            StrAux =(String) VAux.elementAt(p_Chave);
         }

         VAux = (Vector)p_Vetor.elementAt(Hi);
         StrAux = (String)VAux.elementAt(p_Chave);
         while (StrAux.compareToIgnoreCase(Mid) > 0)
         {
            Hi--;         
            VAux = (Vector)p_Vetor.elementAt(Hi);
            StrAux = (String)VAux.elementAt(p_Chave);
         }

         if (Lo <= Hi)
         {
            if (Lo != Hi)   // Para explicitar
            {
               Elemento = (Vector)p_Vetor.elementAt(Lo);
               p_Vetor.removeElementAt(Lo);
               if (Lo < Hi)
                  p_Vetor.add(Lo, p_Vetor.elementAt(Hi-1));
               else
                  p_Vetor.add(Lo, p_Vetor.elementAt(Hi));
               p_Vetor.removeElementAt(Hi);
               p_Vetor.add(Hi, Elemento);
            }
            Lo++;
            Hi--;
         }
      }
      while (Lo <= Hi);

      if (Hi > p_Lo)
         p_Vetor = quickSortAsc(p_Vetor, p_High, p_Lo, Hi, p_Chave);
      if (Lo < p_Hi)
         p_Vetor = quickSortAsc(p_Vetor, p_High, Lo, p_Hi, p_Chave);

      return p_Vetor;
   }
   
   /**
    * @param p_Vetor
    * @param p_High
    * @param p_Lo
    * @param p_Hi
    * @param p_Chave
    * @return Vector
    * @exception 
    * @roseuid 3C4ED00601EB
    */
   private static Vector quickSortDesc(Vector p_Vetor, int p_High, int p_Lo, int p_Hi, int p_Chave) 
   {
      int Lo, Hi;
      String Mid, StrAux;
      Vector Aux, Elemento, VAux;


      Lo = p_Lo;
      Hi = p_Hi;
      Aux = (Vector)p_Vetor.elementAt((Lo+Hi)/2);
      Mid = (String)Aux.elementAt(p_Chave);

      do
      {
         VAux = (Vector)p_Vetor.elementAt(Lo);
         StrAux = (String)VAux.elementAt(p_Chave);
         while (StrAux.compareToIgnoreCase(Mid) < 0)
         {
            Lo++;
            VAux = (Vector)p_Vetor.elementAt(Lo);
            StrAux =(String) VAux.elementAt(p_Chave);
         }

         VAux = (Vector)p_Vetor.elementAt(Hi);
         StrAux = (String)VAux.elementAt(p_Chave);
         while (StrAux.compareToIgnoreCase(Mid) > 0)
         {
            Hi--;         
            VAux = (Vector)p_Vetor.elementAt(Hi);
            StrAux = (String)VAux.elementAt(p_Chave);
         }

         if (Lo >= Hi)
         {
            Elemento = (Vector)p_Vetor.elementAt(Lo);
            p_Vetor.removeElementAt(Lo);
            if (Lo < Hi)
               p_Vetor.add(Lo, p_Vetor.elementAt(Hi-1));
            else
               p_Vetor.add(Lo, p_Vetor.elementAt(Hi));            
            p_Vetor.removeElementAt(Hi);
            p_Vetor.add(Hi, Elemento);
            Lo++;
            Hi--;
         }
      }
      while (Lo >= Hi);

      if (Hi > p_Lo)
         p_Vetor = quickSortAsc(p_Vetor, p_High, p_Lo, Hi, p_Chave);
      if (Lo < p_Hi)
         p_Vetor = quickSortAsc(p_Vetor, p_High, Lo, p_Hi, p_Chave);

      return p_Vetor;
   }
   
   /**
    * @param p_Vetor
    * @param p_High
    * @param p_Lo
    * @param p_Hi
    * @param p_Chave
    * @return Vector
    * @exception 
    * @roseuid 3EF0ACC502DB
    */
   private static Vector quickSortAscNum(Vector p_Vetor, int p_High, int p_Lo, int p_Hi, int p_Chave) 
   {
      int Lo, Hi;
      int iMid, iAux;
      Vector Aux, Elemento, VAux;

      Lo = p_Lo;
      Hi = p_Hi;
      Aux = (Vector)p_Vetor.elementAt((Lo+Hi)/2);
      iMid = Integer.parseInt((String)Aux.elementAt(p_Chave));

      do
      {
         VAux = (Vector)p_Vetor.elementAt(Lo);
         iAux = Integer.parseInt((String)VAux.elementAt(p_Chave));
         while (iAux < iMid)
         {
            Lo++;
            VAux = (Vector)p_Vetor.elementAt(Lo);
            iAux = Integer.parseInt((String)VAux.elementAt(p_Chave));
         }

         VAux = (Vector)p_Vetor.elementAt(Hi);
         iAux = Integer.parseInt((String)VAux.elementAt(p_Chave));
         while (iAux > iMid)
         {
            Hi--;
            VAux = (Vector)p_Vetor.elementAt(Hi);
            iAux = Integer.parseInt((String)VAux.elementAt(p_Chave));          
         }

         if (Lo <= Hi)
         {
            if (Lo != Hi)   // Para explicitar
            {
               Elemento = (Vector)p_Vetor.elementAt(Lo);
               p_Vetor.removeElementAt(Lo);
               if (Lo < Hi)
                  p_Vetor.add(Lo, p_Vetor.elementAt(Hi-1));
               else
                  p_Vetor.add(Lo, p_Vetor.elementAt(Hi));
               p_Vetor.removeElementAt(Hi);
               p_Vetor.add(Hi, Elemento);
            }
            Lo++;
            Hi--;
         }
      }
      while (Lo <= Hi);

      if (Hi > p_Lo)
         p_Vetor = quickSortAscNum(p_Vetor, p_High, p_Lo, Hi, p_Chave);
      if (Lo < p_Hi)
         p_Vetor = quickSortAscNum(p_Vetor, p_High, Lo, p_Hi, p_Chave);

      return p_Vetor;
   }

   private static Vector quickSortAscFloat(Vector p_Vetor, int p_High, int p_Lo, int p_Hi, int p_Chave) 
   {
      int Lo, Hi;
      float fMid, fAux;
      Vector Aux, Elemento, VAux;

      Lo = p_Lo;
      Hi = p_Hi;
      Aux = (Vector)p_Vetor.elementAt((Lo+Hi)/2);
      fMid = Float.parseFloat((String)Aux.elementAt(p_Chave));

      do
      {
         VAux = (Vector)p_Vetor.elementAt(Lo);
         fAux = Float.parseFloat((String)VAux.elementAt(p_Chave));
         while (fAux < fMid)
         {
            Lo++;
            VAux = (Vector)p_Vetor.elementAt(Lo);
            fAux = Float.parseFloat((String)VAux.elementAt(p_Chave));
         }

         VAux = (Vector)p_Vetor.elementAt(Hi);
         fAux = Float.parseFloat((String)VAux.elementAt(p_Chave));
         while (fAux > fMid)
         {
            Hi--;
            VAux = (Vector)p_Vetor.elementAt(Hi);
            fAux = Float.parseFloat((String)VAux.elementAt(p_Chave));          
         }

         if (Lo <= Hi)
         {
            if (Lo != Hi)   // Para explicitar
            {
               Elemento = (Vector)p_Vetor.elementAt(Lo);
               p_Vetor.removeElementAt(Lo);
               if (Lo < Hi)
                  p_Vetor.add(Lo, p_Vetor.elementAt(Hi-1));
               else
                  p_Vetor.add(Lo, p_Vetor.elementAt(Hi));
               p_Vetor.removeElementAt(Hi);
               p_Vetor.add(Hi, Elemento);
            }
            Lo++;
            Hi--;
         }
      }
      while (Lo <= Hi);

      if (Hi > p_Lo)
         p_Vetor = quickSortAscNum(p_Vetor, p_High, p_Lo, Hi, p_Chave);
      if (Lo < p_Hi)
         p_Vetor = quickSortAscNum(p_Vetor, p_High, Lo, p_Hi, p_Chave);

      return p_Vetor;
   }   
}

