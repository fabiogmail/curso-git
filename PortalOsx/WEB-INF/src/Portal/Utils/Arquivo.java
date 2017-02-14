//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/Arquivo.java

package Portal.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Classe responsável por acesso a arquivos e diretórios.
 */
public final class Arquivo 
{
   private char m_Modo;
   private String m_Diretorio = null;
   private String m_Nome = null;
   private BufferedWriter m_BufferedWriter = null;
   private BufferedReader m_BufferedReader = null;
   private File m_File;
   
   /**
    * @return 
    * @exception 
    * @roseuid 3BEF197800EB
    */
   public Arquivo() 
   {
   }
   
   /**
    * @param p_Nome
    * @return void
    * @exception 
    * @roseuid 3BF433390004
    */
   public void setNome(String p_Nome) 
   {
      m_Nome = p_Nome;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3BF4330E0251
    */
   public String getNome() 
   {
      return m_Nome;
   }
   
   /**
    * @param p_Diretorio
    * @return void
    * @exception 
    * @roseuid 3BF4331F00D9
    */
   public void setDiretorio(String p_Diretorio) 
   {
      m_Diretorio = p_Diretorio;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3BF43307016B
    */
   public String getDiretorio() 
   {
      return m_Diretorio;
   }
   
   /**
    * @param p_Modo
    * @return boolean
    * @exception 
    * Abre um arquivo de acordo com o modo desejado: R(leitura) ou W(escrita).
    * @roseuid 3BEF19340011
    */
   public boolean abre(char p_Modo) 
   {
      //System.out.println(m_Diretorio+m_Nome);
      if (m_Diretorio == null || m_Nome == null)
         return false;

      try
      {
         m_Modo = p_Modo;
         switch (p_Modo)
         {
         case 'r':
         case 'R':
            m_BufferedReader = new BufferedReader(new FileReader(m_Diretorio+m_Nome));
            return true;
         case 'w':
         case 'W':
            m_BufferedWriter = new BufferedWriter(new FileWriter(m_Diretorio+m_Nome));
            return true;
         default :
            return false;
         }
      } catch (Exception Exc)
      {
         System.out.println("Arquivo ("+m_Diretorio+m_Nome+") - abre(): "+Exc);
         Exc.printStackTrace();
         return false;
      }
   }
   
   /**
    * @return void
    * @exception 
    * Fecha o arquivo.
    * @roseuid 3BEF1AEC037B
    */
   public void fecha() 
   {
		try
		{
         switch (m_Modo)
         {
         case 'r':
         case 'R':
            m_BufferedReader.close();
            break;
         case 'w':
         case 'W':
            m_BufferedWriter.close();
            break;
         }         
		} catch (Exception Exc)
		{
         System.out.println("Arquivo ("+m_Diretorio+m_Nome+") - fecha(): "+Exc);
		}
   }
   
   /**
    * @return void
    * @exception 
    * Escreve New Line no arquivo.
    * @roseuid 3BEF19F202EF
    */
   public void enter() 
   {
		try
		{
			m_BufferedWriter.newLine();
		} catch (Exception Exc)
		{
         System.out.println("Arquivo ("+m_Diretorio+m_Nome+") - enter(): "+Exc);
		}
   }
   
   /**
    * @param p_Texto
    * @return void
    * @exception 
    * Escreve p_Texto seguido de New Line.
    * @roseuid 3BEF1A24012E
    */
   public void escreveLN(String p_Texto) 
   {
		escreve (p_Texto);
		enter();
   }
   
   /**
    * @param p_Texto
    * @return void
    * @exception 
    * Escreve p_Texto no arquivo sem New Line.
    * @roseuid 3BEF1A690228
    */
   public void escreve(String p_Texto) 
   {
		try
		{
			m_BufferedWriter.write (p_Texto, 0, p_Texto.length());
			m_BufferedWriter.flush();
		} catch (Exception Exc)
		{
         System.out.println("Arquivo ("+m_Diretorio+m_Nome+") - escreve(): "+Exc);
		}
   }
   
   /**
    * @return int
    * @exception 
    * Lê um caracter do arquivo. Em caso de erro, retorna -1.
    * @roseuid 3BEF1A94032E
    */
   public int leChar() 
   {
		try
		{
			return m_BufferedReader.read ();
		} catch (Exception Exc)
		{
         System.out.println("Arquivo ("+m_Diretorio+m_Nome+") - leChar(): "+Exc);
         return -1;
		}
   }
   
   /**
    * @return String
    * @exception 
    * Lê uma linha do arquivo. Em caso de erro, retorn null.
    * @roseuid 3BEF1AD0021C
    */
   public String leLinha() 
   {
		try
		{
			return m_BufferedReader.readLine ();
		} catch (Exception Exc)
		{
         System.out.println("Arquivo ("+m_Diretorio+m_Nome+") - leLinha(): "+Exc);
         return null;
		}
   }
   
   /**
    * @return BufferedReader
    * @exception 
    * Recupera o stream de leitura para acesso direto.
    * @roseuid 3BEF1BB003D7
    */
   public BufferedReader getBufferedRead() 
   {
      return m_BufferedReader;
   }
   
   /**
    * @return BufferedWriter
    * @exception 
    * Recupera o stream de escrita para acesso direto.
    * @roseuid 3BEF1BCA029E
    */
   public BufferedWriter getBufferedWrit() 
   {
      return m_BufferedWriter;
   }
   
   /**
    * @param p_Filtro
    * @return boolean
    * @exception 
    * Procura um aquivo de acordo com o parametro passado que é usado como filtro.
    * @roseuid 3D3845AF033B
    */
   public boolean procuraArquivo(String p_Filtro) 
   {
      short Tipo = 0;
      String Arquivo = null, Prefixo = null, Sufixo = null;
      Vector Arquivos = listaDir((short)0);

      if (Arquivos == null)
         return false;

      if (p_Filtro.equals("*"))   // Qualquer arquivo
         Tipo = 0;
      else if (p_Filtro.startsWith("*") == true && p_Filtro.endsWith("*") == false)
      {
         Sufixo = p_Filtro.substring(p_Filtro.indexOf("*")+1, p_Filtro.length());
System.out.println("Arquivo: Sufixo: "+Sufixo);
         Tipo = 1;  // Qualquer prefixo e sufixo deve ser igual
      }
      else if (p_Filtro.startsWith("*") == false && p_Filtro.endsWith("*") == true)
      {
         Prefixo = p_Filtro.substring(0, p_Filtro.indexOf("*"));
System.out.println("Arquivo: Pref: "+Prefixo);
         Tipo = 2;  // Qualquer sufixo e prefixo deve ser igual
      }
      else if (p_Filtro.indexOf("*") != -1 && p_Filtro.startsWith("*") == false && p_Filtro.endsWith("*") == false)
      {
         Prefixo = p_Filtro.substring(0, p_Filtro.indexOf("*"));
         Sufixo = p_Filtro.substring(p_Filtro.indexOf("*")+1, p_Filtro.length());
System.out.println("Arquivo: Pref: "+Prefixo + " - Sufixo: "+Sufixo);
         Tipo = 3;  // Sufixo e prefixo devem ser iguais
      }
      else if (p_Filtro.indexOf("*") == -1 && p_Filtro.startsWith("*") == false && p_Filtro.endsWith("*") == false)
         Tipo = 4;  // Arquivo com nome exato

      for (int i = 0; i < Arquivos.size(); i++)
      {
         Arquivo = (String)Arquivos.elementAt(i);
         switch (Tipo)
         {
            case 0:   // Qualquer arquivo
               m_Nome = Arquivo;
               return true;
            case 1:  // Qualquer prefixo e sufixo deve ser igual
               if (Arquivo.endsWith(Sufixo) == true)
               {
                  m_Nome = Arquivo;
                  return true;
               }
            case 2:  // Qualquer sufixo e prefixo deve ser igual
               if (Arquivo.startsWith(Prefixo) == true)
               {
                  m_Nome = Arquivo;
                  return true;
               }
            case 3:  // Sufixo e prefixo devem ser iguais
               if (Arquivo.startsWith(Prefixo) == true && Arquivo.endsWith(Sufixo) == true)
               {
                  m_Nome = Arquivo;
                  return true;
               }
            case 4:  // Arquivo com nome exato
               if (Arquivo.equals(p_Filtro) == true)
               {
                  m_Nome = Arquivo;
                  return true;
               }
            default:
               return false;
         }
      }

      return false;
   }
   
   /**
    * @param p_Filtro
    * @return Vector
    * @exception 
    * Retorna uma lista contendo o conteúdo do diretório de acordo com o filtro passado:
    * 0 - somente arquivos
    * 1 - somente diretórios
    * 2 - arquivos e diretórios
    * @roseuid 3D38461303B7
    */
   public Vector listaDir(short p_Filtro) 
   {
      File Temp;
      Vector ArqRet;

      m_File = new File(m_Diretorio);
      String Arquivos[] = m_File.list();

      if (Arquivos.length == 0)  // Não há arquivos
         return null;

      ArqRet = new Vector();
      for (int i = 0; i < Arquivos.length; i++)
      {
         switch (p_Filtro)
         {
            case 0:    // Procura arquivos
               Temp = new File (m_Diretorio+"/"+Arquivos[i]);
               if (Temp.isFile ())  // É arquivo
                  ArqRet.addElement(Arquivos[i]);
               break;
            case 1:    // Procura diretórios
               Temp = new File (m_Diretorio+"/"+Arquivos[i]);
               if (!Temp.isFile ())  // É diretório
                  ArqRet.addElement(Arquivos[i]);
               break;
            case 2:   // Procura arquivo ou diretório
               ArqRet.addElement(Arquivos[i]);
               break;
         }
      }

      ArqRet.trimToSize();
      return ArqRet;
   }
   
   /**
    * @return boolean
    * @exception 
    * @roseuid 3D593BD301C7
    */
   public boolean criaDir() 
   {
      m_File = new File(m_Diretorio);   
      try
      {
         return m_File.mkdir();
      }
      catch (Exception Exc)
      {
         return false;
      }
   }

   /**
    * @return boolean
    * @exception
    * @roseuid 3D593BE6014C
    */
   public boolean criaDirs()
   {
      m_File = new File(m_Diretorio);
      try
      {
         return m_File.mkdirs();
      }
      catch (Exception Exc)
      {
         return false;
      }
   }

   public boolean apaga()
   {
      try
      {
         m_File.delete();
         return true;
      }
      catch (Exception Exc)
      {
         return false;
      }
   }
   
   public void zipaArquivo(String p_Arquivo)
   {
      File fil;

      // Must use try because file io is occurring.
      try
      {
         fil = new File(p_Arquivo+".zip");
         if (fil.exists() == true)
            fil.delete();

         // Create the file output streams for both the file and the zip.
         FileOutputStream fos = new FileOutputStream(p_Arquivo+".zip");
         ZipOutputStream zos = new ZipOutputStream(fos);

      	zipFunc(p_Arquivo, zos);

         // Close the file output streams for both the file and the zip.
         zos.flush();
         zos.close();
         fos.close();
      }
      catch (IOException e)
      {
         System.out.println("Arquivo::zipaArquivo(): Erro!");
         e.printStackTrace();         
      }
   }

	// New zipFunc method.
	private static void zipFunc (String filePath, ZipOutputStream zos)
   {
//System.out.println("Arquivo::zipFunc(): 1");   
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      File fil = null;
      
   	try
      {
			// Create a file input stream and a buffered input stream.
			fis = new FileInputStream(filePath);         
			bis = new BufferedInputStream(fis);
         fil = new File(filePath);

			// Create a Zip Entry and put it into the archive (no data yet).
			ZipEntry fileEntry = new ZipEntry(fil.getName());//filePath);
			zos.putNextEntry(fileEntry);

			// Create a byte array object named data and declare byte count variable.
			byte[] data = new byte[1024];
			int byteCount;
			// Create a loop that reads from the buffered input stream and writes
			// to the zip output stream until the bis has been entirely read.
			while ((byteCount = bis.read(data, 0, 1024)) > -1)
         {
				zos.write(data, 0, byteCount);
			}
		}
      catch (IOException e)
      {
         System.out.println("Arquivo::zipFunc(): Erro");
         e.printStackTrace();
		}
      finally
      {
         try
         {
            bis.close();      
            fis.close();
            fil.delete();
         }
         catch (Exception e)
         {
            System.out.println("Arquivo::zipFunc(): Erro ao fechar os arquivos");
            e.printStackTrace();
         }
      }
	}
}
