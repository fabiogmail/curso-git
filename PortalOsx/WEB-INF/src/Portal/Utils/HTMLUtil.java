//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/HTMLUtil.java

package Portal.Utils;

import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import Portal.Cluster.NoUtil;
import Portal.Configuracoes.DefsComum;


public class HTMLUtil 
{
   
   /**
    * Marca de argumento no arquivo Html
    */
   private static String s_MARCA_ARG = "$ARG;";
   
   /**
    * Aviso de argumento faltante
    */
   private static String s_FALTA_ARG  = "=>Falta ARG<=";
   
   /**
    * Saída de páginas Html
    */
   private PrintWriter m_SaidaHtml;

	/**
	 * Objeto para a construção de tabelas "inteligentes".
	 * 
	 * @uml.property name="m_Tabela"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public Tabela m_Tabela = null;

   private static final int m_AlturaTab = 22;
   
   static 
   {
   }
   
   /**
    * @param p_SaidaHtml
    * @return 
    * @exception 
    * Constutor. Recebe stream para saída de páginas html.
    * @roseuid 3BF5907401BC
    */
   public HTMLUtil(PrintWriter p_SaidaHtml) 
   {
      m_SaidaHtml = p_SaidaHtml;
   }
   
   /**
    * @param p_Titulo
    * @param p_SubTitulo
    * @return void
    * @exception 
    * Envia o início de página Html.
    * @roseuid 3BF590510324
    */
   public void enviaInicio(String p_Titulo, String p_SubTitulo, String p_JavaScript) 
   {
      m_SaidaHtml.print ("<html>\n");
      m_SaidaHtml.print  ("<head>\n");
      
      if (p_SubTitulo == null)
         m_SaidaHtml.print  ("<title>"+p_Titulo+"</title>\n");
      else
        m_SaidaHtml.print  ("<title>"+p_Titulo+" - "+p_SubTitulo+"</title>\n");
      
      m_SaidaHtml.print  ("<link rev=\"made\" href=\"mailto:"+DefsComum.s_MAIL_ADM+"\">\n");
      m_SaidaHtml.print  ("<meta http-equiv=\"Pragma\" content=\"no-cache\">\n");
      m_SaidaHtml.print  ("<link rel=\"stylesheet\" href=\" /"+DefsComum.s_ContextoWEB+"/css/style.css\" type=\"text/css\">\n");

      if (p_JavaScript != null)
          m_SaidaHtml.print ("<script type=\"text/javascript\" charset=\"ISO-8859-1\" language=\"JavaScript\" src=\"/PortalOsx/templates/js/" + p_JavaScript + "\" type=\"text/JavaScript\"></script>\n");
      m_SaidaHtml.print  ("</head>\n");
   }
   
   /**
    * @param p_DirArquivoRodape
    * @param p_ArquivoRodape
    * @param p_TerminaHtml
    * @return boolean
    * @exception 
    * Envia um arquivo de finalização de página Html (rodapé).
    * @roseuid 3BF5905A029B
    */
   public boolean enviafinal(String p_DirArquivoRodape, String p_ArquivoRodape, boolean p_TerminaHtml) 
   {
      Arquivo ArqRodape;
      String Linha;

      if (p_ArquivoRodape != null)
      {
         ArqRodape = new Arquivo();
         ArqRodape.setDiretorio(p_DirArquivoRodape);
         ArqRodape.setNome(p_ArquivoRodape);
         
         // Só envia o arquivo de rodapé caso ele exista
         if (ArqRodape.abre('r') == true)
         {
            while ((Linha = ArqRodape.leLinha()) != null)
               m_SaidaHtml.print (Linha);
               
            ArqRodape.fecha();
         }
         else
            return false;
      }

      if (p_TerminaHtml == true)
      {
         m_SaidaHtml.print("</body>\n");
         m_SaidaHtml.print("</html>\n");
      }
      return true;
   }
   
   /**
    * @param p_Args
    * @return boolean
    * @exception 
    * Envia arquivo de template Html e substitui as marcas de argumento pelas strings correspondentes.
    * @roseuid 3BF59062024C
    */
   public boolean enviaArquivo(String[] p_Args) 
   {
      Arquivo ArqTemplate;
      String Linha, ParteLinha;
      short Argumento = 2;
      
      // #######
      // # Seta o nome do arquivo de template
      // #######      
      ArqTemplate = new Arquivo();
      ArqTemplate.setDiretorio(p_Args[0]);  // p_Args[0] sempre contém o dir. do arquivo
      ArqTemplate.setNome     (p_Args[1]);  // p_Args[1] sempre contém o nome do arquivo
      
      // So envia o arquivo de template caso ele exista
      if (ArqTemplate.abre('r') == true)
      {
         while ((Linha = ArqTemplate.leLinha()) != null)
         {
            // Se a linha contém marcas de argumento
            if (Linha.indexOf(s_MARCA_ARG) != -1)
            {
               while (Linha.indexOf(s_MARCA_ARG) != -1)
               {
                  // #######
                  // # Recupera linha até a marca de argumento e a envia
                  // #######
                  ParteLinha = Linha.substring(0, Linha.indexOf(s_MARCA_ARG));
                  m_SaidaHtml.print (ParteLinha);

                  // #######
                  // # Envia o argumento caso ainda exista, caso contrário envia FALTA_ARG
                  // #######
                  if (Argumento < p_Args.length)
                  {
                     m_SaidaHtml.print (p_Args[Argumento]);
                     Argumento++;
                  }
                  else
                     m_SaidaHtml.print (s_FALTA_ARG);

                  // #######
                  // # Recupera o restante da linha e a envia
                  // #######
                  Linha = Linha.substring(Linha.indexOf(s_MARCA_ARG)+s_MARCA_ARG.length(), Linha.length());
               }
            }
            // A linha não contém marcas de argumento
            m_SaidaHtml.println (Linha);
         }
         ArqTemplate.fecha();
         //m_SaidaHtml.flush();
         //m_SaidaHtml.close();
         return true;
      }
      else
      {
         m_SaidaHtml.flush();
         m_SaidaHtml.close();
         return false;
      }
   }
   
   /**
    * @param p_Texto
    * @return void
    * @exception 
    * @roseuid 3F2680E40052
    */
   public void envia(String p_Texto) 
   {
      m_SaidaHtml.print(p_Texto);   
   }
   
   /**
    * @return PrintWriter
    * @exception 
    * Retorna o stream de saída do HTML.
    * @roseuid 3C3E4E560004
    */
   public PrintWriter getSaidaHtml() 
   {
      return m_SaidaHtml;
   }
   
   /**
    * @param p_Qtdtabelas
    * @return void
    * @exception 
    * @roseuid 3C4EF1BE000C
    */
   public void setQtdTabelas(int p_Qtdtabelas) 
   {
      //m_Tabela = new Tabela[p_QtdTabelas];
   }
   
   /**
    * @param p_QtdColunas
    * @param p_Paginado
    * @return void
    * @exception 
    * Instancia o objeto tabela.
    * @roseuid 3C4B162600AD
    */
   public void setTabela(short p_QtdColunas, boolean p_Paginado) 
   {
      m_Tabela = new Tabela (m_SaidaHtml, p_QtdColunas, p_Paginado);
   }
   
   /**
    * @param p_Tabela
    * @return Tabela
    * @exception 
    * @roseuid 3C4EF3B50024
    */
   public Tabela getTabela(int p_Tabela) 
   {
      return null;
   }
   
   /**
    * @param p_Request
    * @param p_Linhas
    * @return void
    * @exception 
    * @roseuid 3C4CD84803A4
    */
   public void trataTabela(HttpServletRequest p_Request, Vector p_Linhas) 
   {
      if (m_Tabela == null)
         return;

      // Recupera índice de classificação
      String Indice = p_Request.getParameter("indice");
      if(Indice == null) Indice = (String)p_Request.getAttribute("indice");
      
      if (Indice != null)
      {
         String TipoOrdenacao = p_Request.getParameter("ordena");
         if(TipoOrdenacao == null) TipoOrdenacao = (String)p_Request.getAttribute("ordena");
         
         short sIndice = Short.parseShort(Indice);  
         if (TipoOrdenacao != null && TipoOrdenacao.equals("1") == true)
         {
            if (Indice != null)
            {
               // Ordena lista pelo índice
               m_Tabela.setElementos(VetorUtil.ClassificaVetor(m_Tabela.getElementos(), Short.parseShort(Indice), 'A'));
            }
         }
         else if (TipoOrdenacao != null && TipoOrdenacao.equals("2") == true)
         {
            m_Tabela.setElementos(VetorUtil.ClassificaVetor(m_Tabela.getElementos(), Short.parseShort(Indice), 'N'));
         }

         m_Tabela.setIndice(sIndice);
      }

      String Atual = p_Request.getParameter("atual");
      if(Atual == null) Atual = (String)p_Request.getAttribute("atual");
    	  
      if (Atual != null)
      {
         boolean bOrdena = false;
         int iOffSet = 0, iAtual = 0;

         String Ordena = p_Request.getParameter("ordena");
         if(Ordena == null) Ordena = (String)p_Request.getAttribute("ordena");
         
         if (Ordena.equals("1"))
            bOrdena = true;

         String OffSet = p_Request.getParameter("offset");
         if(OffSet == null) OffSet = (String)p_Request.getAttribute("offset");
         
         if (OffSet.equals("prox"))
         {
            iAtual = Integer.parseInt(Atual);
            if (iAtual + m_Tabela.getQtdItensPagina() < m_Tabela.getElementos().size())
               iAtual += m_Tabela.getQtdItensPagina();

            iOffSet = iAtual;
            m_Tabela.setOffSet((short)iOffSet);
         }
         else if (OffSet.equals("ant"))
         {
            iAtual = Integer.parseInt(Atual);
            if (iAtual != 0)
               iAtual -= m_Tabela.getQtdItensPagina();
            iOffSet = iAtual;
            m_Tabela.setOffSet((short)iOffSet);
         }
         else
         {
            iAtual = Integer.parseInt(OffSet);
            if (bOrdena)
               iAtual /= m_Tabela.getQtdItensPagina();
            iOffSet = iAtual*m_Tabela.getQtdItensPagina();

            m_Tabela.setOffSet((short)iOffSet);
         }
      }
      else
         m_Tabela.setOffSet((short)0);
   }
   
   /**
    * @param p_Diretorio
    * @param p_Arquivo
    * @return String
    * @exception 
    * Realiza a leitura do arquivo associado e retorna uma string contendo o seu conteúdo.
    * @roseuid 3C51AD7702A2
    */
   public String leArquivo(String p_Diretorio, String p_Arquivo) 
   {
      String Arquivo = "";
      Arquivo ArqTemplate;      

      ArqTemplate = new Arquivo();
      ArqTemplate.setDiretorio(p_Diretorio);
      ArqTemplate.setNome(p_Arquivo);
      if (ArqTemplate.abre('r'))
      {
         String Linha = null;
         while ((Linha = ArqTemplate.leLinha()) != null)
            Arquivo += Linha;
         ArqTemplate.fecha();
         return Arquivo;
      }
      else
         return "";
   }
   
   /**
    * @param p_Diretorio
    * @param p_Arquivo
    * @param p_Args
    * @return String
    * @exception 
    * Realiza a leitura de um arquivo contendo um formulário e retorna uma string contendo o seu conteúdo.
    * @roseuid 3C51AD7702DE
    */
   public String leFormulario(String p_Diretorio, String p_Arquivo, String[] p_Args) 
   {
      String Arquivo = "", Linha = "", ParteLinha = "";
      Arquivo ArqTemplate;
      short Argumento = 0;

      // #######
      // # Seta o nome do arquivo de template
      // #######
      ArqTemplate = new Arquivo();
      ArqTemplate.setDiretorio(p_Diretorio);
      ArqTemplate.setNome(p_Arquivo);

      // So le o arquivo de formulário caso ele exista
      if (ArqTemplate.abre('r') == true)
      {
         while ((Linha = ArqTemplate.leLinha()) != null)
         {
            // Se a linha contém marcas de argumento
            if (Linha.indexOf(s_MARCA_ARG) != -1)
            {
               while (Linha.indexOf(s_MARCA_ARG) != -1)
               {
                  // #######
                  // # Recupera linha até a marca de argumento e a envia
                  // #######
                  ParteLinha = Linha.substring(0, Linha.indexOf(s_MARCA_ARG));
                  Arquivo += ParteLinha;

                  // #######
                  // # Envia o argumento caso ainda exista, caso contrário envia FALTA_ARG
                  // #######
                  if (Argumento < p_Args.length)
                  {
                     Arquivo += p_Args[Argumento];
                     Argumento++;
                  }
                  else
                     Arquivo += s_FALTA_ARG;

                  // #######
                  // # Recupera o restante da linha e a envia
                  // #######
                  Linha = Linha.substring(Linha.indexOf(s_MARCA_ARG)+s_MARCA_ARG.length(), Linha.length());
               }
            }
            // A linha não contém marcas de argumento
            Arquivo += Linha+"\n";
         }
         ArqTemplate.fecha();
         return Arquivo;
      }
      else
         return "";
   }
   
   /**
    * @param p_Diretorio
    * @param p_Arquivo
    * @param p_Args
    * @return String
    * @exception 
    * @roseuid 3C55FE2C0213
    */
   public String criaFormulario(String p_Diretorio, String p_Arquivo, String[] p_Args) 
   {
      Arquivo ArqTemplate;
      short Argumento = 0;
      StringTokenizer Tokens;
      String Parametros[] = null, Arquivo = "", Linha = "", ParteLinha = "";

      // #######
      // # Seta o nome do arquivo de template
      // #######
      ArqTemplate = new Arquivo();
      ArqTemplate.setDiretorio(p_Diretorio);
      ArqTemplate.setNome(p_Arquivo);

      // So le o arquivo de formulário caso ele exista
      if (ArqTemplate.abre('r') == true)
      {
         while ((Linha = ArqTemplate.leLinha()) != null)
         {
            while (Linha.startsWith(" "))
               Linha = Linha.substring(Linha.indexOf(' ')+1, Linha.length());
            while (Linha.startsWith("\t"))
               Linha = Linha.substring(Linha.indexOf('\t')+1, Linha.length());

            if (Linha == null || Linha.length() == 0 || Linha.charAt(0) == '#' || Linha.charAt(0) == '\n')
               continue;

            if (Linha.toLowerCase().startsWith("table"))
            {
               Parametros = Tokens2String(Linha, ",");
               Arquivo += "<table width=\"" + Parametros[1] + "\" border=\"" + Parametros[2] +"\" cellspacing=\"" + Parametros[3] + "\" cellpadding=\"" + Parametros[4] + "\">\n";
               continue;
            }
            else if (Linha.toLowerCase().equals("/table"))
            {
               Arquivo += "</table>\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("form"))
            {
               Parametros = Tokens2String(Linha, ",");
               Arquivo += "<form name=\"" + Parametros[1] + "\" method=\"" + Parametros[2] + "\" action=\"" + Parametros[3] +"\">\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("/form"))
            {
               Arquivo += "</form>\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("tr"))
            {
               Arquivo += "<tr>\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("/tr"))
            {
               Arquivo += "</tr>\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("th"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<tr";
               if (Parametros[1].equals("") == false)
                  Aux += " bgcolor=\"" + Parametros[1] + "\"";

               Aux += ">\n";
               Arquivo += Aux;
               continue;
            }
            else if (Linha.toLowerCase().startsWith("/th"))
            {
               Arquivo += "</tr>\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("td"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<td";
               if (Parametros[1].equals("") == false)
                  Aux += " align=\"" + Parametros[1] + "\"";
               if (Parametros[2].equals("") == false)
                  Aux += " valign=\"" + Parametros[2] + "\"";
               if (Parametros[3].equals("") == false)
                  Aux += " colspan=\"" + Parametros[3] + "\"";
               if  (Parametros[4].equals("") == false)
                  Aux += " width=\"" + Parametros[4] + "\"";
               if  (Parametros[5].equals("") == false)
                  Aux += " height=\"" + Parametros[5] + "\"";

               Aux += ">\n";
               Arquivo += Aux;
               continue;
            }
            else if (Linha.toLowerCase().startsWith("/td"))
            {
               Arquivo += "</td>\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("hidden"))
            {
               Parametros = Tokens2String(Linha, ",");
               Arquivo += "<input type=\"hidden\" name=\"" + Parametros[1] + "\" value=\"" + Parametros[2] + "\">\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("textarea"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<textarea";
               if (Parametros[1].equals("") == false)
                  Aux += " name=\""+Parametros[1]+"\"";
               if (Parametros[2].equals("") == false)
                  Aux += " cols=\""+Parametros[2]+"\"";
               if (Parametros[3].equals("") == false)
                  Aux += " rows=\""+Parametros[3]+"\"";

               Aux += "></textarea>\n";
               Arquivo += Aux;
               continue;
            }            
            else if (Linha.toLowerCase().startsWith("text"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<input type=\"text\" name=\"" + Parametros[1] + "\"";
               if (Parametros[2].equals("") == false)
                  Aux += " value=\"" + Parametros[2] + "\"";
               if (Parametros[3].equals("") == false)
                  Aux += " maxsize=\"" + Parametros[3] + "\"";
               if  (Parametros[4].equals("") == false)
                  Aux += " size=\"" + Parametros[4] + "\"";
               if  (Parametros[5].equals("") == false)
                  Aux += " " + Parametros[5];

               Aux += ">\n";
               Arquivo += Aux;
               continue;
            }
            else if (Linha.toLowerCase().startsWith("password"))
            {
               Parametros = Tokens2String(Linha, ",");
               Arquivo += "<input type=\"password\" name=\"" + Parametros[1] + "\" value=\"" + Parametros[2] + "\" maxsize=\"" + Parametros[3] + "size=\""+ Parametros[4] + "\"" + Parametros[5] + ">\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("select"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<select name=\"" + Parametros[1] + "\"";
               if (Parametros[2].equals("") == false)
                  Aux += " size=\"" + Parametros[2] + "\"";
               if (Parametros[3].equals("") == false)
                  Aux += " " + Parametros[3];
               if  (Parametros[4].equals("") == false)
                  Aux += " " + Parametros[4];
               Aux += ">\n";

               Arquivo += Aux;
               continue;
            }
            else if (Linha.toLowerCase().startsWith("/select"))
            {
               Arquivo += "</select>\n";
            }
            else if (Linha.toLowerCase().startsWith("button") ||
                Linha.toLowerCase().startsWith("submit") ||
                Linha.toLowerCase().startsWith("reset"))
            {
               Parametros = Tokens2String(Linha, ",");
               Arquivo += "<input type=\"" + Parametros[0] + "\" name=\"" + Parametros[1] + "\" value=\"" + Parametros[2] +"\" " + Parametros[3] + ">\n";
               continue;
            }
            else if (Linha.toLowerCase().startsWith("checkbox"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<input type=\"checkbox\"";
               if (Parametros[1].equals("") == false)
                   Aux += " name=\"" + Parametros[1] + "\">";
               if (Parametros[2].equals("") == false)
                  Aux += " " + Parametros[2];

               Aux += "\n";                  
               Arquivo += Aux;
               continue;
            }
            else if (Linha.toLowerCase().startsWith("image"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<input type=\"image\"";
               if (Parametros[1].equals("") == false)
                   Aux += " src=\"" + Parametros[1] + "\"";
               if (Parametros[2].equals("") == false)
                  Aux += " " + Parametros[2];

               Aux += ">\n";                  
               Arquivo += Aux;
               continue;
            }

            else if (Linha.toLowerCase().startsWith("radio"))
            {
               continue;
            }
            else if (Linha.toLowerCase().startsWith("option"))
            {
               Parametros = Tokens2String(Linha, ",");
               String Aux = "";

               Aux = "<option";
               if (Parametros[1].equals("") == false)
                  Aux += " value=\"" + Parametros[1] + "\"";

               Aux += ">";

               if  (Parametros[2].equals("") == false)
                  Aux += Parametros[2];
               Aux += "</option>\n";

               Arquivo += Aux;
               continue;
            }
            else if (Linha.toLowerCase().startsWith("fileupload"))
            {
               continue;
            }
            else
            {
               Arquivo += Linha;
            }
         }
         ArqTemplate.fecha();
      }

      if (p_Args != null)
         Arquivo = substituiArgumentos(Arquivo, p_Args);
      return Arquivo;
   }
   
   /**
    * @param p_Tokens
    * @param p_Delimitador
    * @return String[]
    * @exception 
    * @roseuid 3C56063C01A3
    */
   public String[] Tokens2String(String p_Tokens, String p_Delimitador) 
   {
      String pTokens[];
      StringTokenizer Tokens;
      Vector vTokens;

      Tokens = new StringTokenizer(p_Tokens, p_Delimitador);
      vTokens = new Vector();

      while (Tokens.hasMoreTokens())
         vTokens.add(Tokens.nextToken());
      vTokens.trimToSize();

      pTokens = new String[vTokens.size()];
      for (int i = 0; i < vTokens.size(); i++)
      {
         pTokens[i] = (String)vTokens.elementAt(i);
         if (pTokens[i].equals(" "))
            pTokens[i] = "";
      }

      return pTokens;
   }
   
   /**
    * @param p_Arquivo
    * @param p_Args
    * @return String
    * @exception 
    * @roseuid 3C56DC5600BE
    */
   public String substituiArgumentos(String p_Arquivo, String[] p_Args) 
   {
      short Args = 0;
      String Texto = "";

      if (p_Arquivo.indexOf(s_MARCA_ARG) == -1)
         return p_Arquivo;
         
      do
      {
         if (p_Arquivo.indexOf(s_MARCA_ARG) != -1)
         {
            if (p_Arquivo.indexOf(s_MARCA_ARG) != 0)
            {
               Texto += p_Arquivo.substring(0, p_Arquivo.indexOf(s_MARCA_ARG));
               if (Args < p_Args.length)
               {
                  Texto += p_Args[Args];
                  Args++;
               }
               else
                  Texto += s_FALTA_ARG;
            }
            else
            {
               if (Args < p_Args.length)
               {
                  Texto += p_Args[Args];
                  Args++;
               }
               else
                  Texto += s_FALTA_ARG;
               Texto += p_Arquivo.substring(p_Arquivo.indexOf(s_MARCA_ARG)+1, p_Arquivo.length());
            }

            p_Arquivo = p_Arquivo.substring(p_Arquivo.indexOf(s_MARCA_ARG)+s_MARCA_ARG.length(), p_Arquivo.length());
            if (p_Arquivo.indexOf(s_MARCA_ARG) == -1)
               Texto += p_Arquivo;
         }
      } while (p_Arquivo.indexOf(s_MARCA_ARG) != -1);

      return Texto;
   }

   public void enviaPastasPeriodos(HTMLUtil p_Html, Vector p_Periodos, Vector p_Links, int p_Pos)
   {
      String CorFundo = "#BBBDBB", CorHab = "#DAD9D5", CorDes = "#737173";
      int UltimaPasta = -1;
      
      p_Html.envia("  <table bgcolor=\""+CorFundo+"\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" resizable=\"no\">\n");
      p_Html.envia("   <tr>\n");
      p_Html.envia("      <td width=\"1\">\n");
      p_Html.envia("         <table bgcolor=\""+CorFundo+"\" width=\"220\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
      p_Html.envia("            <tr>\n");

      int l_Qtd10=0;
      while (((l_Qtd10+1)*10) <= p_Pos)
         l_Qtd10++;

      for (int i = (10*l_Qtd10); i < p_Periodos.size(); i++)
         UltimaPasta = enviaPasta(p_Html, i, p_Pos, UltimaPasta, p_Links.elementAt(i).toString(), p_Periodos.elementAt(i).toString(), CorHab, CorDes);
      for (int i = 0; i < (10*l_Qtd10); i++)
         UltimaPasta = enviaPasta(p_Html, i, p_Pos, UltimaPasta, p_Links.elementAt(i).toString(), p_Periodos.elementAt(i).toString(), CorHab, CorDes);
         
      fimPasta(p_Html, UltimaPasta);

      p_Html.envia("      </td>\n</tr>\n</table>\n");
      p_Html.envia("  <table bgcolor=\""+CorHab+"\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td height=\"4\"></td></tr></table>\n");
   }

   private void inicioPasta(HTMLUtil p_Html, int p_UltimaPasta, int p_Pasta)
   {
      p_Html.envia("               <td>");
      if (p_UltimaPasta == -1)
      {
         if (p_Pasta == 1)
            p_Html.envia("<img src=\"/PortalOsx/imagens/InicioHab.jpg\" width=\"14\" height="+m_AlturaTab+"\" border=\"0\">");
         else
            p_Html.envia("<img src=\"/PortalOsx/imagens/InicioDes.jpg\" width=\"14\" height=\""+m_AlturaTab+"\" border=\"0\">");
      }
      else
      {
         if (p_UltimaPasta == 1)
            p_Html.envia("<img src=\"/PortalOsx/imagens/HabDes.jpg\" width=30 height=\""+m_AlturaTab+"\" border=\"0\">");
         else if (p_Pasta == 1)
            p_Html.envia("<img src=\"/PortalOsx/imagens/DesHab.jpg\" width=30 height=\""+m_AlturaTab+"\" border=\"0\">");
         else
            p_Html.envia("<img src=\"/PortalOsx/imagens/DesDes.jpg\" width=30 height=\""+m_AlturaTab+"\" border=\"0\">");
      }
      p_Html.envia("</td>\n");
   }

   private void fimPasta(HTMLUtil p_Html, int p_Pasta)
   {
      p_Html.envia("               <td>");

      if (p_Pasta == 1)
         p_Html.envia("<img src=\"/PortalOsx/imagens/FimHab.jpg\" width=\"30\" height=\""+m_AlturaTab+"\" border=\"0\">");
      else
         p_Html.envia("<img src=\"/PortalOsx/imagens/FimDes.jpg\" width=\"30\" height=\""+m_AlturaTab+"\" border=\"0\">");

      p_Html.envia("</td>\n            </tr>\n         </table>\n");
   }

   private int enviaPasta(HTMLUtil p_Html, int p_Pos, int p_Cur, int p_UltimaPasta, String p_Link, String p_Texto, String p_CorHab, String p_CorDes)
   {
      int Pasta, Tam;
      String CorLink, Cor, Complemento;
      
      if (p_Pos == p_Cur) Pasta = 1;
      else Pasta = 0;

      inicioPasta(p_Html, p_UltimaPasta, Pasta);

      if (Pasta == 1)
      {
         Cor = "\""+p_CorHab+"\"";
         Tam = 1;
         Complemento = "Hab";
         CorLink = "link";
      }
      else
      {
         Cor = "\""+p_CorDes+"\"";
         Tam = 1;
         Complemento = "Des";
         CorLink = "link2";
      }

      p_Html.envia("               <td bgcolor="+Cor+">\n");
      p_Html.envia("                  <table bgcolor=\""+Cor+"\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" resizable=\"no\">\n");
      p_Html.envia("                     <tr>\n");
      p_Html.envia("                          <td height=\"22\" nowrap>");
      if (Pasta != 1)
      {
         p_Html.envia("<a href=\""+p_Link +"\"");
         p_Html.envia(" class='"+CorLink+"' onmouseover=\"window.status='Apresenta per&iacute;odo "+p_Texto+"';return true;\" onmouseout=\"window.status='';return true;\"");
         p_Html.envia(">");
      }
      p_Html.envia("<font size=\""+Tam+"\" face=\"verdana\">");
      p_Html.envia(p_Texto+"</font>");
      if (Pasta != 1)      
         p_Html.envia("</a>");
      p_Html.envia("</td>\n");
      p_Html.envia("                     </tr>\n");
      p_Html.envia("                  </table>\n");
      p_Html.envia("               </td>\n");
      return Pasta;
   }   
   
   public static String getCabecalho()
   {
   		StringBuffer cabecalho = new StringBuffer(300);
   		cabecalho.append("<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">\n");
   		cabecalho.append("   <tr>\n   ");
   		cabecalho.append("			<td align=\"left\" ><p><b>Visent - CDRView <sup>&reg;</sup></b><br>\n");
   		cabecalho.append("				<small>Copyright 1999 - 2004</small></p>  ");
   		cabecalho.append("			</td> ");
   		
	   	if (DefsComum.s_CLIENTE.compareToIgnoreCase("brasiltelecom") == 0)
	    {
	   		cabecalho.append("		<td align=\"middle\"> <img src=\"http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/"+DefsComum.s_ContextoWEB+"/imagens/logo_cliente2.gif\"> </td> ");
	    }
	   	
	   	cabecalho.append("			<td align=\"right\" ><img src=\"http://"+NoUtil.getNo().getHostName()+":"+NoUtil.getNo().getPorta()+"/"+DefsComum.s_ContextoWEB+"/imagens/logo_cliente.gif\"> </td> ");
	   	cabecalho.append("   </tr>\n");
	   	cabecalho.append("</table>\n");
	   	
	   	return cabecalho.toString();
   }
   
}
