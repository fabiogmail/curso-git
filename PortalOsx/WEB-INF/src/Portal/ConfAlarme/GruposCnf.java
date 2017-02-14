package Portal.ConfAlarme;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.COMM_FAILURE;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Utils.BilhetadorCfgDef;

public class GruposCnf
{ 
   public long m_Id;
   public char m_Tipo;
   public short m_Classe;
   public String m_Comentario;
   public double m_K;
   public double m_PM;
   public long m_Cham;
   public long m_TMC;
   public String m_Comentario2;
   public Vector m_Parametros;
   public Vector m_Regras;
   public String bilhetadores;

   public GruposCnf()
   {
      m_Id = 0l;
      m_Tipo = ' ';
      m_Classe = (short) 0;
      m_Comentario = null;
      m_K = 0d;
      m_PM = 0d;
      m_Cham = 0l;
      m_TMC = 0l;
      m_Comentario2 = null;
      m_Parametros = new Vector();
      m_Regras = new Vector();
      bilhetadores = null;
   }

   public GruposCnf fnCopia()
   {
      GruposCnf l_Copia = new GruposCnf();
      l_Copia.m_Id = m_Id;
      l_Copia.m_Tipo = 'N';
      l_Copia.m_Classe = m_Classe;
      l_Copia.m_Comentario = m_Comentario;
      l_Copia.m_K = m_K;
      l_Copia.m_PM = m_PM;
      l_Copia.m_Cham = m_Cham;
      l_Copia.m_TMC = m_TMC;
      l_Copia.m_Comentario2 = m_Comentario2;
      for (int a=0; a<m_Parametros.size(); a++)
      {
        GruposCnfParm l_GruposCnfParm = (GruposCnfParm) m_Parametros.elementAt(a);
        l_Copia.m_Parametros.add(l_GruposCnfParm.fnCopia());
      }

      return l_Copia;
   }

   public boolean fnLeCfn1(String p_Linha)
   {
      StringTokenizer l_Str = new StringTokenizer(p_Linha);
      try
      {
//      m_Id;
         if (l_Str.hasMoreTokens())
            m_Id =  new Long(l_Str.nextToken()).longValue();
         else
            return false;

//      m_Tipo;
         if (l_Str.hasMoreTokens())
            m_Tipo =  l_Str.nextToken().charAt(0);
         else
            return false;

//      m_Classe;
         if (l_Str.hasMoreTokens())
            m_Classe =  new Short(l_Str.nextToken()).shortValue();
         else
            return false;

//      m_Comentario;
         m_Comentario = "";
         if (l_Str.hasMoreTokens())
         {
            l_Str.nextToken();
            while (l_Str.hasMoreTokens())
               m_Comentario += l_Str.nextToken() + " ";
         } else
            return false;
      } catch(Exception e)
      {
         return false;
      }
      return true;
   }

   public boolean fnLeCfn2(String p_Linha)
   {
      StringTokenizer l_Str = new StringTokenizer(p_Linha);
      try
      {
//      m_K;
         if (l_Str.hasMoreTokens())
            m_K =  new Double(l_Str.nextToken()).doubleValue();
         else
            return false;

//      m_PM;
         if (l_Str.hasMoreTokens())
            m_PM =  new Double(l_Str.nextToken()).doubleValue();
         else
            return false;

//      m_Cham;
         if (l_Str.hasMoreTokens())
            m_Cham =  new Long(l_Str.nextToken()).longValue();
         else
            return false;

//      m_TMC;
         if (l_Str.hasMoreTokens())
            m_TMC =  new Long(l_Str.nextToken()).longValue();
         else
            return false;

//      m_Comentario2;
         m_Comentario2 = "";
         if (l_Str.hasMoreTokens())    
         {
            while (l_Str.hasMoreTokens())
               m_Comentario2 += l_Str.nextToken() + " ";
         } else
            return false;
      } catch(Exception e)
      {
         return false;
      }
      return true;
   }

   public boolean fnLeParametro(String p_Linha)
   {
      if (GruposCnfParm.fnTesta(p_Linha))
      {
         GruposCnfParm l_GruposCnfParm = new GruposCnfParm();
         l_GruposCnfParm.fnLeCfn(p_Linha);
         m_Parametros.add(l_GruposCnfParm);
         return true;
      } else
         return false;
   }

   public void fnSalva(java.io.BufferedWriter p_Arquivo) throws Exception
   {
      try
      {
        p_Arquivo.write(""+m_Id+" "+m_Tipo+" "+(m_Classe > 9?"":"0")+m_Classe+"\t\t* "+m_Comentario);
        p_Arquivo.newLine();
        p_Arquivo.write(""+m_K+" "+m_PM+" "+m_Cham+" "+m_TMC+"\t\t"+m_Comentario2);
        p_Arquivo.newLine();

        for (int a=0; a<m_Parametros.size(); a++)
        {
           ((GruposCnfParm) m_Parametros.elementAt(a)).fnSalva(p_Arquivo);
        }
      } catch(Exception e)
      {
         throw (e);
      }
   }

   public boolean fnAdicionaRegra(String p_Regra)
   {
      m_Regras.add(p_Regra);
      return true;
   }

   public boolean fnRemoveRegra(int p_Regra)
   {
      m_Regras.removeElementAt(p_Regra);
      return true;
   }
   
   public boolean verificaBilhetador(String temp)
   {
	     String stringBilhet = "";
	     
		 List Bilhetadores = new Vector();
		 No noTmp = null;
		 
		 for (Iterator iter = NoUtil.listaDeNos.iterator(); iter.hasNext();)
	     {
	          try
	          {
		          noTmp = (No) iter.next();
				  List bilhetadores = noTmp.getConexaoServUtil().getListaBilhetadoresCfg();
				  if(bilhetadores != null)
					  Bilhetadores.addAll(bilhetadores);
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
		 
		 String arrayBilhetadores[] = temp.split(";"); 
		 
		 
		 for (int i = 0; i < Bilhetadores.size(); i++) 
		 {
			 if(((BilhetadorCfgDef)Bilhetadores.get(i)).getBilhetador().trim().equalsIgnoreCase(arrayBilhetadores[0].trim()))//se o primeiro ja for bilhetadora esta correto
			 {
				 return true;
			 }

		 }
		 
		 return false;
		 
   }
   
   
}