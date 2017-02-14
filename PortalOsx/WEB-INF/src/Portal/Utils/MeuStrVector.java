//Source file: C:/usr/osx/CDRView/Servlet/Portal/Utils/MeuStrVector.java

package Portal.Utils;

import java.util.Vector;

/**
 */
public class MeuStrVector extends Vector 
{
   private boolean m_RepeteElemento = false;
   
   /**
    * @param p_Repete
    * @return 
    * @exception 
    * @roseuid 3C85813F03C0
    */
   public MeuStrVector(boolean p_Repete) 
   {
      m_RepeteElemento = p_Repete;
   }
   
   /**
    * @param p_Objeto
    * @return boolean
    * @exception 
    * @roseuid 3C8580AE02DB
    */
   public boolean insereElementoOrd(Object p_Objeto) 
   {
      if (m_RepeteElemento == false)
      {
         if (buscaElemento(p_Objeto) == false)
         {
            String Aux = null;
            for (int i = 0; i < this.size(); i++)
            {
               Aux = (String)this.elementAt(i);
               if (Aux.compareTo((String)p_Objeto) < 0)
               {
                  this.add(i, p_Objeto);
                  return true;
               }
            }
            this.addElement(p_Objeto);
         }
         else
            return false;
      }
      else
         addElement(p_Objeto);

      return true;
   }
   
   /**
    * @param p_Objeto
    * @return boolean
    * @exception 
    * @roseuid 3C8580F0013C
    */
   public boolean buscaElemento(Object p_Objeto) 
   {
      String Aux = null;
      for (int i = 0; i < this.size(); i++)
      {
         Aux = (String)this.elementAt(i);
         if (Aux.equals((String)p_Objeto) == true)
            return true;
      }
      return false;
   }
}
