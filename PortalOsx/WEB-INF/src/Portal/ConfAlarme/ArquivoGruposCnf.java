package Portal.ConfAlarme;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import Portal.Cluster.NoUtil;

public class ArquivoGruposCnf
{ 
   private static String m_Nome;
   private static String m_NomeRegras;
   public Vector m_Grupos;

   public ArquivoGruposCnf() throws Exception
   {
      m_Nome = NoUtil.getNo().getDiretorioDefs().getS_DIR_ARQS_ALR()+"GruposCnf.txt";
      m_NomeRegras = NoUtil.getNo().getDiretorioDefs().getS_DIR_ARQS_ALR()+"GruposRegras.txt";

      m_Grupos = new Vector();
      java.io.BufferedReader l_Arquivo;
      try
      {
         l_Arquivo = new java.io.BufferedReader(new java.io.FileReader(m_Nome));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao abrir arquivo "+m_Nome));
      }
      String l_Linha;

      int l_Estado = 0;
      GruposCnf l_GruposCnf = null;
      boolean l_Tratou = true;

      l_Linha = l_Arquivo.readLine();
      while (l_Linha != null)
      {
    	  if(l_Linha.length() > 0 && !Pattern.matches("(\\s+)?",l_Linha)){
		         switch(l_Estado)
		         {
		            case 0:
		               l_GruposCnf = new GruposCnf();
		               if (l_GruposCnf.fnLeCfn1(l_Linha))
		                  l_Estado = 1;
		               else
		                  throw (new Exception("Erro de parser 1 - Arquivo "+m_Nome+" corrompido na linha "+l_Linha));
		               m_Grupos.add(l_GruposCnf);
		               l_Tratou = true;
		               break;
		            case 1:
		               if (l_GruposCnf == null)
		                  throw (new Exception("Erro de lógica 1 lendo "+l_Linha));
		
		               if (l_GruposCnf.fnLeCfn2(l_Linha))
		                  l_Estado = 2;
		               else
		                  throw (new Exception("Erro de parser 2 - Arquivo "+m_Nome+" corrompido na linha "+l_Linha));
		               l_Tratou = true;
		               break;
		            case 2:
		               if (l_GruposCnf == null)
		                  throw (new Exception("Erro de lógica 2 lendo "+l_Linha));
		
		               if (! l_GruposCnf.fnLeParametro(l_Linha))
		               {
		                  l_Estado = 0;
		                  l_Tratou = false;
		               } else
		                  l_Tratou = true;
		               break;
		         }
    	  }
         if (l_Tratou)
            l_Linha = l_Arquivo.readLine();
      }

      l_Arquivo.close();

      try
      {
         l_Arquivo = new java.io.BufferedReader(new java.io.FileReader(m_NomeRegras));
      } catch(Exception e)
      {
         return;
      }
      l_Linha = l_Arquivo.readLine();
      while (l_Linha != null)
      {
         StringTokenizer l_Str = new StringTokenizer(l_Linha);
         
         if (l_Str.hasMoreTokens())
         {
            try
            {
               long l_Grupo = new Long(l_Str.nextToken()).longValue();

               l_GruposCnf = null;
               for (int a=0; a<m_Grupos.size(); a++)
               {
                 l_GruposCnf = (GruposCnf) m_Grupos.elementAt(a);
                 if (l_GruposCnf.m_Id == l_Grupo)
                    break;
               }

               if (l_GruposCnf.m_Id == l_Grupo)
               {
                  l_Str.nextToken(); // Le a Classe
                  
                  String l_Regra = "";
                  String temp = "";
                  while (l_Str.hasMoreTokens())
                  {
                	  /**
                	   * a variavel temp tem sempre um token na frente se for o ultimo token e a linha for a primeira
                	   * é a configuração de bilhetadores, metodo verifica se e bilhetador válido
                	   */
                	  temp = l_Str.nextToken() + " ";
                	  if(l_Str.hasMoreTokens()==false && l_GruposCnf.verificaBilhetador(temp))
                		  l_GruposCnf.bilhetadores = temp + " ";
                	  else
                		  l_Regra += temp + " ";
                  }
                  
                  l_GruposCnf.m_Regras.add(l_Regra);
               }
            } catch (Exception e)
            {
            }
         }

         l_Linha = l_Arquivo.readLine();
      }
      l_Arquivo.close();
   }

   public void fnSalva() throws Exception
   {
      java.io.BufferedWriter l_Arquivo;
      File arquivoGruposCnf = new File(m_Nome);
      File arquivoNomeRegras = new File(m_NomeRegras);
      
      try
      {
          /** Criacao de um arquivo temporario para evitar problemas de concorrencia com o servidor */
         l_Arquivo = new java.io.BufferedWriter(new java.io.FileWriter(m_Nome+".tmp"));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao abrir arquivo "+m_Nome+".tmp"));
      }

      try
      {
        for (int a=0; a<m_Grupos.size(); a++)
        {
          ((GruposCnf) m_Grupos.elementAt(a)).fnSalva(l_Arquivo);
        }

        l_Arquivo.close();
        
        if (arquivoGruposCnf.exists())
        {
            int numeroTentativas = 0;
            while (!arquivoGruposCnf.delete())
            {
                if (numeroTentativas > 3) 
                    break;
                numeroTentativas++;
                Thread.sleep(3000); /** Faz um sleep de 3 segundos */
            }
        }
        /** Renomeando o temporario para o nome original */
        new File(m_Nome+".tmp").renameTo(new File(m_Nome));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao escrever arquivo "+m_Nome+".tmp"));
      }

      try
      {
          /** Criacao de um arquivo temporario para evitar problemas de concorrencia com o servidor */
         l_Arquivo = new java.io.BufferedWriter(new java.io.FileWriter(m_NomeRegras+".tmp"));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao abrir arquivo "+m_NomeRegras+".tmp"));
      }

      try
      {
        for (int a=0; a<m_Grupos.size(); a++)
        {
            GruposCnf l_GruposCnf = (GruposCnf) m_Grupos.elementAt(a);
           
            for (int b=0; b<l_GruposCnf.m_Regras.size(); b++)
            {
               if(b==0) /*escreve primeira mascara de regra mais os bilhetadores caso existam, caso contrário coloca '*' */
            	   l_Arquivo.write(l_GruposCnf.m_Id+" "+(l_GruposCnf.m_Classe>9?"0":"0")+l_GruposCnf.m_Classe+" "+((String) l_GruposCnf.m_Regras.elementAt(0))+" "+(l_GruposCnf.bilhetadores==null?"":(l_GruposCnf.bilhetadores.equals("")?"*":l_GruposCnf.bilhetadores)));
               else/*b começa do 1 porque o indice 0 tem que escrever os bilhetadores*/
            	   l_Arquivo.write(l_GruposCnf.m_Id+" "+(l_GruposCnf.m_Classe>9?"0":"0")+l_GruposCnf.m_Classe+" "+((String) l_GruposCnf.m_Regras.elementAt(b)));
               l_Arquivo.newLine();
            }
        }

        l_Arquivo.close();
        
        if (arquivoNomeRegras.exists())
        { 
            int numeroTentativas = 0;
            while (!arquivoNomeRegras.delete())
            {
                if (numeroTentativas > 3) 
                    break;
                
                numeroTentativas++;
                Thread.sleep(3000); /** Faz um sleep de 3 segundos */ 
            }
        }
        /** Renomeando o temporario para o nome original */
        new File(m_NomeRegras+".tmp").renameTo(new File(m_NomeRegras));
      } catch(Exception e)
      {
         e.printStackTrace();
         throw (new Exception("Erro ao escrever arquivo "+m_NomeRegras));
      }
   }
}