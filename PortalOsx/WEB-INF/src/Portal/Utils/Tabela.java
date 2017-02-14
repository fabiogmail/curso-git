//Source file: C:/Usr/OSx/CDRView/Servlet/Portal/Utils/Tabela.java

package Portal.Utils;

import java.io.PrintWriter;
import java.util.Vector;

/**
 */
public class Tabela 
{ 
   
   /**
    * Saída de páginas Html
    */
   private PrintWriter m_SaidaHtml = null;
   
   /**
    * Espessura da borda
    */
   private short m_Borda = 0;
   private short m_CellSpacing = 0;
   private short m_CellPadding = 0;
   
   /**
    * Largura da tabela
    */
   private short m_Largura = 553;
   
   /**
    * A tabela é paginada ou não
    */
   private boolean m_Paginado = false;
   
   /**
    * Quantidade de itens na tabela
    */
   private short m_QtdItensPagina = 10;
   
   /**
    * Cabeçalho da tabela
    */
   private Vector m_Header = null;
   
   /**
    * Quantidade de colunas
    */
   private short m_QtdColunas = 0;
   
   /**
    * Altura das colunas: 1 atributo para todas as colunas
    */
   private short m_AlturaColunas = 0;
   
   /**
    * Vetor de largura das colunas
    */
   private Vector m_LarguraColunas = null;
   
   /**
    * Elementos para composição da tabela
    */
   private Vector m_Elementos = null;
   
   /**
    * Posição inicial a partir de onde apresentar a tabela
    */
   private short m_OffSet = 0;
   private Vector m_CoresLinhas = null;
   private Vector m_AlinhamentoHeader = null;
   private Vector m_Alinhamento = null;
   private Vector m_VAlinhamento = null;
   private String m_Link;
   private String m_Operacao = null;
   private String m_Tipo = null;
   private short m_FontSizeLinks = 2;
   private boolean m_ApresentaIndice = false;
   private short[] m_Filtros = null;
   private short m_Indice = 0;
   private String m_SaidaStr = "";
   private Vector m_Form = null;
   
   /**
    * @param p_SaidaHtml
    * @param p_QtdColunas
    * @param p_Paginado
    * @return 
    * @exception 
    * @roseuid 3C4B0C2F0330
    */
   public Tabela(PrintWriter p_SaidaHtml, short p_QtdColunas, boolean p_Paginado) 
   {
      m_SaidaHtml = p_SaidaHtml;
      m_QtdColunas = p_QtdColunas;
      m_Paginado = p_Paginado;

      String [] AlinhamentoDefault = new String[m_QtdColunas];
      for (short i = 0; i < m_QtdColunas; i++)
         AlinhamentoDefault[i] = "left";

      m_Alinhamento = VetorUtil.String2Vetor(AlinhamentoDefault);

      m_CoresLinhas = new Vector();
      m_CoresLinhas.add("#000033");   // Cor de fundo do header
      m_CoresLinhas.add("#F0F0F0");   // Cor de fundo para alternação
      m_CoresLinhas.add("#FFFFFF");   // Cor de fundo para alternação
   }
   
   /**
    * @param p_Borda
    * @return void
    * @exception 
    * @roseuid 3C4B0C3F0184
    */
   public void setBorder(short p_Borda) 
   {
      m_Borda = p_Borda;
   }
   
   /**
    * @param p_Largura
    * @return void
    * @exception 
    * @roseuid 3C4B0C5401D5
    */
   public void setLargura(short p_Largura) 
   {
      m_Largura = p_Largura;
   }
   
   /**
    * @param p_Valor
    * @return void
    * @exception 
    * @roseuid 3C4B0D3902EC
    */
   public void setCellSpacing(short p_Valor) 
   {
      m_CellSpacing = p_Valor;
   }
   
   /**
    * @param p_Valor
    * @return void
    * @exception 
    * @roseuid 3C4B0D4D00BA
    */
   public void setCellPadding(short p_Valor) 
   {
      m_CellPadding = p_Valor;
   }
   
   /**
    * @param p_Header
    * @return void
    * @exception 
    * Seta as colunas de cabeçalho da tabela.
    * @roseuid 3C4B1A6E00F9
    */
   public void setHeader(Vector p_Header) 
   {
      if (p_Header != null)
         m_Header = p_Header;
      else
         m_Header = null;
   }
   
   /**
    * @param p_Header
    * @return void
    * @exception 
    * Seta as colunas de cabeçalho da tabela.
    * @roseuid 3C4C46F9004A
    */
   public void setHeader(String[] p_Header) 
   {
      if (p_Header != null)
         m_Header = VetorUtil.String2Vetor(p_Header);
      else
         m_Header = null;
   }
   
   /**
    * @param p_Paginado
    * @return void
    * @exception 
    * @roseuid 3C4B0C6B0246
    */
   public void setPaginado(boolean p_Paginado) 
   {
      m_Paginado = p_Paginado;
   }
   
   /**
    * @param p_QtdItensPagina
    * @return void
    * @exception 
    * @roseuid 3C4B0EEB00C0
    */
   public void setQtdItensPagina(short p_QtdItensPagina) 
   {
      m_QtdItensPagina = p_QtdItensPagina;
   }
   
   /**
    * @param p_QtdColunas
    * @return void
    * @exception 
    * @roseuid 3C4B0C7303D8
    */
   public void setQtdColunas(short p_QtdColunas) 
   {
      m_QtdColunas = p_QtdColunas;
   }
   
   /**
    * @param p_AlturaColunas
    * @return void
    * @exception 
    * @roseuid 3C4B0C8201A9
    */
   public void setAlturaColunas(short p_AlturaColunas) 
   {
      m_AlturaColunas = p_AlturaColunas;
   }
   
   /**
    * @param p_LarguraColunas
    * @return void
    * @exception 
    * @roseuid 3C4B0C8A039F
    */
   public void setLarguraColunas(Vector p_LarguraColunas) 
   {
      m_LarguraColunas =  p_LarguraColunas;
   }
   
   /**
    * @param p_LarguraColunas
    * @return void
    * @exception 
    * @roseuid 3C4C46DD0285
    */
   public void setLarguraColunas(String[] p_LarguraColunas) 
   {
      m_LarguraColunas = VetorUtil.String2Vetor(p_LarguraColunas);
   }
   
   /**
    * @param p_Alinhamento
    * @return void
    * @exception 
    * @roseuid 401AB96803B8
    */
   public void setAlinhamentoHeader(Vector p_Alinhamento)
   {
      m_AlinhamentoHeader = p_Alinhamento;
   }
   
   /**
    * @param p_Alinhamento
    * @return void
    * @exception 
    * @roseuid 401AB95E03D2
    */
   public void setAlinhamentoHeader(String[] p_Alinhamento) 
   {
      m_AlinhamentoHeader = VetorUtil.String2Vetor(p_Alinhamento);
   }
   
   /**
    * @param p_Alinhamento
    * @return void
    * @exception 
    * @roseuid 3C4B21620167
    */
   public void setAlinhamento(Vector p_Alinhamento) 
   {
      m_Alinhamento = p_Alinhamento;
   }
   
   /**
    * @param p_Alinhamento
    * @return void
    * @exception 
    * @roseuid 3C4C472D0208
    */
   public void setAlinhamento(String[] p_Alinhamento) 
   {
      m_Alinhamento = VetorUtil.String2Vetor(p_Alinhamento);
   }
   
   /**
    * @param p_VAlinhamento
    * @return void
    * @exception 
    * @roseuid 3C70208102FB
    */
   public void setVAlinhamento(Vector p_VAlinhamento) 
   {
      m_VAlinhamento = p_VAlinhamento;
   }
   
   /**
    * @param p_VAlinhamento
    * @return void
    * @exception 
    * @roseuid 3C7020810337
    */
   public void setVAlinhamento(String[] p_VAlinhamento) 
   {
      m_VAlinhamento = VetorUtil.String2Vetor(p_VAlinhamento);
   }
   
   /**
    * @param p_Elementos
    * @return void
    * @exception 
    * @roseuid 3C4B0FC20000
    */
   public void setElementos(Vector p_Elementos) 
   {
      m_Elementos = p_Elementos;
   }
   
   /**
    * @param p_OffSet
    * @return void
    * @exception 
    * @roseuid 3C4B12030050
    */
   public void setOffSet(short p_OffSet) 
   {
      m_OffSet = p_OffSet;
   }
   
   /**
    * @param p_Link
    * @return void
    * @exception 
    * @roseuid 3C4B31F40315
    */
   public void setLink(String p_Link) 
   {
      m_Link = p_Link;
   }
   
   /**
    * @param p_FontSizeLinks
    * @return void
    * @exception 
    * @roseuid 3C4C0AF60068
    */
   public void setFontSizeLinks(short p_FontSizeLinks) 
   {
      m_FontSizeLinks = p_FontSizeLinks;
   }
   
   /**
    * @param p_Operacao
    * @return void
    * @exception 
    * @roseuid 3C4B340300EC
    */
   public void setOperacao(String p_Operacao) 
   {
      m_Operacao = p_Operacao;
   }
   
   /**
    * @param p_Tipo
    * @return void
    * @exception 
    * @roseuid 3C4B342700BC
    */
   public void setTipo(String p_Tipo) 
   {
      m_Tipo = p_Tipo;
   }
   
   /**
    * @param p_ApresentaIndice
    * @return void
    * @exception 
    * @roseuid 3C4C0B54012B
    */
   public void setApresentaIndice(boolean p_ApresentaIndice) 
   {
      m_ApresentaIndice = p_ApresentaIndice;
   }
   
   /**
    * @param p_Filtros
    * @return void
    * @exception 
    * @roseuid 3C4C488C028F
    */
   public void setFiltros(short[] p_Filtros) 
   {
      m_Filtros = p_Filtros;
   }
   
   /**
    * @param p_Indice
    * @return void
    * @exception 
    * @roseuid 3C4CC0D002D2
    */
   public void setIndice(short p_Indice) 
   {
      m_Indice = p_Indice;
   }
   
   /**
    * @return Vector
    * @exception 
    * @roseuid 3C4CDAB30304
    */
   public Vector getElementos() 
   {
      return m_Elementos;
   }
   
   /**
    * @return String
    * @exception 
    * @roseuid 3C4CD02C039F
    */
   public String getTabelaString() 
   {
      return m_SaidaStr;
   }
   
   public void setTabelaString(String saida) 
   {
      m_SaidaStr = saida;
   }
   
   /**
    * @return short
    * @exception 
    * @roseuid 3C4CD9EA0355
    */
   public short getQtdItensPagina() 
   {
      return m_QtdItensPagina;
   }
   
   /**
    * @param p_Form
    * @return void
    * @exception 
    * @roseuid 3C4EF08F0350
    */
   public void setForm(String[] p_Form) 
   {
      if (p_Form != null)
         m_Form = VetorUtil.String2Vetor(p_Form);
   }
   
   /**
    * @param p_Form
    * @return void
    * @exception 
    * @roseuid 3C4EF0BE03C6
    */
   public void setForm(Vector p_Form) 
   {
      if (p_Form != null)
         m_Form = p_Form;
   }
   
   /**
    * @return boolean
    * @exception 
    * Envia o código HTML da tabela.
    * @roseuid 3C4B0F0E037D
    */
   public boolean enviaTabela() 
   {
      String Altura = "";
      if (m_AlturaColunas != 0)
         Altura = " height=\""+m_AlturaColunas+"\"";

      m_SaidaHtml.print("<table width=\""+m_Largura+"\" border=\""+m_Borda+"\" cellspacing=\""+m_CellSpacing+"\" cellpadding=\""+m_CellPadding+"\">\n");

      // Envia header
      m_SaidaHtml.print("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(0)+"\">\n");
      if (m_Header != null)
      {
         for (short i = 0; i < m_Header.size(); i++)
         {
            if (m_LarguraColunas != null)
               m_SaidaHtml.print("\t\t<td align=\""+ (m_AlinhamentoHeader != null ? m_AlinhamentoHeader.elementAt(i) : m_Alinhamento.elementAt(i))+"\" width=\""+m_LarguraColunas.elementAt(i)+"\""+Altura+">");
            else
               m_SaidaHtml.print("\t\t<td align=\""+(m_AlinhamentoHeader != null ? m_AlinhamentoHeader.elementAt(i) : m_Alinhamento.elementAt(i))+"\" width=\""+m_Largura/m_Header.size()+"\""+Altura+">");

            if (m_Filtros != null)
               if (m_Filtros[i] == 1 || m_Filtros[i] == 2)
                  m_SaidaHtml.print("<a href=\""+m_Link+"&atual=0&offset="+m_OffSet+"&indice="+i+"\" class=\"link\">");

            m_SaidaHtml.print("<font color=\"#FFFFFF\"><b>"+m_Header.elementAt(i)+"<font></b>");

            if (m_Filtros != null)
               if (m_Filtros[i] == 1)
                  m_SaidaHtml.print("</a>");

            m_SaidaHtml.print("</td>\n");
         }
      }
/*
      else
      {
         m_SaidaHtml.print("\t\t<td width=\""+m_Largura+"\">Tabela construída incorretamente!</td>\n");
         m_SaidaHtml.print("\t</tr>\n");
         m_SaidaHtml.print("</table>\n");
         return false;
      }
*/
      m_SaidaHtml.print("\t</tr>\n");

      // Envia tabela de acordo com a paginação
      if (m_Paginado == true)
      {
         int k, Cont, QtdPaginas = m_Elementos.size()/m_QtdItensPagina;
         if (m_Elementos.size()%m_QtdItensPagina != 0)
            QtdPaginas++;

         for (Cont = m_OffSet, k = 0; Cont < m_Elementos.size() &&  k < m_QtdItensPagina; Cont++, k++)
         {
            if (Cont % 2 == 0)
               m_SaidaHtml.print("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(1)+"\">\n");
            else
               m_SaidaHtml.print("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(2)+"\">\n");

            Vector Linha = (Vector)m_Elementos.elementAt(Cont);
            for (int j = 0; j < m_Header.size(); j++)
               m_SaidaHtml.print("\t\t<td align=\""+m_Alinhamento.elementAt(j)+"\"" +Altura+">"+Linha.elementAt(j)+"</td>\n");

            m_SaidaHtml.print("\t</tr>\n");
         }
         m_SaidaHtml.print("\t<tr>\n");
         m_SaidaHtml.print("\t\t<td colspan=\""+m_Header.size()+"\">&nbsp;</td>");
         m_SaidaHtml.print("\t</tr>\n");

         m_SaidaHtml.print("\t<tr>\n");
         m_SaidaHtml.print("\t\t<td align=\"center\" colspan=\""+m_Header.size()+"\">\n");

         m_SaidaHtml.print("\t\t\t<table width=\"554\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
         m_SaidaHtml.print("\t\t\t\t<tr>\n");
         if (m_OffSet != 0)
            m_SaidaHtml.print("\t\t\t\t\t<td width=\"102\"><b>&lt;&lt; <a href=\""+m_Link+"&atual="+m_OffSet+"&offset=ant&indice="+m_Indice+"\" class=\"link\">p&aacute;gina anterior</a></b></td>\n");
         else
            m_SaidaHtml.print("\t\t\t\t\t<td width=\"102\">&nbsp;</td>\n");
         m_SaidaHtml.print("\t\t\t\t\t<td width=\"346\">");
         m_SaidaHtml.print("<div align=\"center\">");

         int Posicao = 0;
         Posicao = (m_OffSet/m_QtdItensPagina)+1;

         if (QtdPaginas != 1)
         {
            for (int i = 0; i < QtdPaginas; i++)
            {
               if (i != QtdPaginas -1)
               {
                  if (Posicao == i+1)
                     m_SaidaHtml.print("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"\" class=\"link\"><b><i>"+(i+1)+"</i></b></a> | &nbsp;");
                  else
                     m_SaidaHtml.print("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"\" class=\"link\"><b>"+(i+1)+"</b></a> | &nbsp;");
               }
               else  if (Posicao == i+1)
                  m_SaidaHtml.print("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"\" class=\"link\"><b><i>"+(i+1)+"</i></b></a>");
               else
                  m_SaidaHtml.print("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"\" class=\"link\"><b>"+(i+1)+"</b></a>");
            }
         }

         m_SaidaHtml.print("</div>");
         m_SaidaHtml.print("</td>\n");
         if (Cont < m_Elementos.size())
            m_SaidaHtml.print("\t\t\t\t\t<td width=\"106\"><b><a href=\""+m_Link+"&atual="+m_OffSet+"&offset=prox&indice="+m_Indice+"\" class=\"link\">pr&oacute;xima p&aacute;gina</a>&gt;&gt;</b></td>\n");
         else
            m_SaidaHtml.print("\t\t\t\t\t<td width=\"106\">&nbsp;</td>\n");         
         m_SaidaHtml.print("\t\t\t\t</tr>\n");

         if (m_ApresentaIndice == true)
         {
            m_SaidaHtml.print("\t\t\t\t<tr>\n");
            m_SaidaHtml.print("\t\t\t\t\t<td align=\"center\" colspan=\""+m_Header.size()+"\">");
            m_SaidaHtml.print("<font size=\""+m_FontSizeLinks+"\">");
            Posicao = 1;
            if (m_OffSet+1 % 2 == 0)
               Posicao = m_OffSet / 2;
            else
               Posicao = (m_OffSet / 2) + 1;
               
            m_SaidaHtml.print("Página "+Posicao+" / De "+QtdPaginas);
            m_SaidaHtml.print("</font>");
            m_SaidaHtml.print("</td>\n");
            m_SaidaHtml.print("\t\t\t\t</tr>\n");
         }
         m_SaidaHtml.print("\t\t\t</table>\n");

         m_SaidaHtml.print("</td>\n");
         m_SaidaHtml.print("\t</tr>\n");
      }
      else
      {
         Vector Linha;
         int TamHeader = m_Header.size();
         int TamElementos = m_Elementos.size();
//int x ;
//System.out.println("EnviaTabela");
         for (int i = 0; i < TamElementos; i++)
         {
/*x = i % 100;
if (x == 0)
System.out.println("i: "+ i);*/
            if (i % 2 == 0)
               m_SaidaHtml.print("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(1)+"\">\n");
            else
               m_SaidaHtml.print("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(2)+"\">\n");

            Linha = (Vector)m_Elementos.elementAt(i);
            for (int j = 0; j < TamHeader; j++)
               m_SaidaHtml.print("\t\t<td align=\""+m_Alinhamento.elementAt(j)+"\"" +Altura+">"+Linha.elementAt(j)+"</td>\n");

            m_SaidaHtml.print("\t</tr>\n");
         }
      }

      m_SaidaHtml.println("</table>");
      return true;
   }
   
   /**
    * @return boolean
    * @exception 
    * Envia o código HTML da tabela para m_SaidaHtml.
    * @roseuid 3C4CCF53014E
    */
   public boolean enviaTabela2String() 
   {
      String Altura = "";
      StringBuffer stb = new StringBuffer();//stringbuffer temporario só para fazer operações
      if (m_AlturaColunas != 0)
         Altura = " height=\""+m_AlturaColunas+"\"";

      stb.append("<table width=\""+m_Largura+"\" border=\""+m_Borda+"\" cellspacing=\""+m_CellSpacing+"\" cellpadding=\""+m_CellPadding+"\">\n");

      // Envia header
      stb.append("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(0)+"\">\n");
      if (m_Header != null)
      {
         for (short i = 0; i < m_Header.size(); i++)
         {
            if (m_LarguraColunas != null)
            	stb.append("\t\t<td align=\""+(m_AlinhamentoHeader != null ? m_AlinhamentoHeader.elementAt(i) : m_Alinhamento.elementAt(i))+"\" width=\""+m_LarguraColunas.elementAt(i)+"\""+Altura+">");
            else
            	stb.append("\t\t<td align=\""+(m_AlinhamentoHeader != null ? m_AlinhamentoHeader.elementAt(i) : m_Alinhamento.elementAt(i))+"\" width=\""+m_Largura/m_Header.size()+"\""+Altura+">");

            if (m_Filtros != null)
            {
               if (m_Filtros[i] == 1  || m_Filtros[i] == 2)
            	   stb.append("<a href=\""+m_Link+"&atual=0&offset="+m_OffSet+"&indice="+i+"&ordena="+m_Filtros[i]+"\" onmouseover=\"window.status='Clique para ordenar';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">");
            }

            stb.append("<font color=\"#FFFFFF\"><b>"+m_Header.elementAt(i)+"<font></b>");

            if (m_Filtros != null)
               if (m_Filtros[i] == 1)
            	   stb.append("</a>");

            stb.append("</td>\n");
         }
      }
/*
      else
      {
         m_SaidaStr += "\t\t<td width=\""+m_Largura+"\">Tabela construída incorretamente!</td>\n";
         m_SaidaStr += "\t</tr>\n";
         m_SaidaStr += "</table>\n";
         return false;
      }
*/
      stb.append("\t</tr>\n");
      
      // Envia tabela de acordo com a paginação
      if (m_Paginado == true)
      {
         int k, Cont, QtdPaginas = m_Elementos.size()/m_QtdItensPagina;
         if (m_Elementos.size()%m_QtdItensPagina != 0)
            QtdPaginas++;

         for (Cont = m_OffSet, k = 0; Cont < m_Elementos.size() &&  k < m_QtdItensPagina; Cont++, k++)
         {
            if (Cont % 2 == 0)
            	stb.append("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(1)+"\">\n");
            else
            	stb.append("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(2)+"\">\n");

            Vector Linha = (Vector)m_Elementos.elementAt(Cont);
            for (int j = 0; j < m_Header.size(); j++)
            	stb.append("\t\t<td align=\""+m_Alinhamento.elementAt(j)+"\"" +Altura+">"+Linha.elementAt(j)+"</td>\n");

            	stb.append("\t</tr>\n");
         }
         stb.append("\t<tr>\n");
         stb.append("\t\t<td colspan=\""+m_Header.size()+"\">&nbsp;</td>\n");
         stb.append("\t</tr>\n");

         stb.append("\t<tr>\n");
         stb.append("\t\t<td align=\"center\" colspan=\""+m_Header.size()+"\">\n");

         stb.append("\t\t\t<table width=\"554\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
         stb.append("\t\t\t\t<tr>\n");
         if (m_OffSet != 0)
        	 stb.append("\t\t\t\t\t<td width=\"102\"><b>&lt;&lt; <a href=\""+m_Link+"&atual="+m_OffSet+"&offset=ant&indice="+m_Indice+"&ordena=0\" onmouseover=\"window.status='Página anterior';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">p&aacute;gina anterior</a></b></td>\n");
         else
        	 stb.append("\t\t\t\t\t<td width=\"102\">&nbsp;</td>\n");                     
         stb.append("\t\t\t\t\t<td width=\"346\">");
         stb.append("<div align=\"center\">");

         int Posicao = 0;
         Posicao = (m_OffSet/m_QtdItensPagina)+1;

         
         if (QtdPaginas != 1)
         {
            for (int i = 0; i < QtdPaginas; i++)
            {
               if (i != QtdPaginas - 1)
               {
                  if (Posicao == i+1)
                	  stb.append("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"&ordena=0\" onmouseover=\"window.status='Página "+(i+1)+"';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\"><b><i>"+(i+1)+"</i></b></a> | &nbsp;");
                  else
                	  stb.append("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"&ordena=0\"  onmouseover=\"window.status='Página "+(i+1)+"';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\"><b>"+(i+1)+"</b></a> | &nbsp;");
               }
               else  if (Posicao == i+1)
            	   stb.append("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"&ordena=0\"  onmouseover=\"window.status='Página "+(i+1)+"';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\"><b><i>"+(i+1)+"</i></b></a>");
               else
            	   stb.append("<a href=\""+m_Link+"&atual=0&offset="+i+"&indice="+m_Indice+"&ordena=0\"  onmouseover=\"window.status='Página "+(i+1)+"';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\"><b>"+(i+1)+"</b></a>");
            }
         }

         stb.append("</div>");
         stb.append("</td>\n");
         if (Cont < m_Elementos.size())
        	 stb.append("\t\t\t\t\t<td width=\"106\"><b><a href=\""+m_Link+"&atual="+m_OffSet+"&offset=prox&indice="+m_Indice+"&ordena=0\" onmouseover=\"window.status='Próxima página';return true;\" onmouseout=\"window.status='';return true;\" class=\"link\">pr&oacute;xima p&aacute;gina</a> &gt;&gt;</b></td>\n");
         else
        	 stb.append("\t\t\t\t\t<td width=\"106\">&nbsp;</td>\n");
         stb.append("\t\t\t\t</tr>\n");

         if (m_ApresentaIndice == true)
         {
        	stb.append("\t\t\t\t<tr>\n");
            stb.append("\t\t\t\t\t<td align=\"center\" colspan=\""+m_Header.size()+"\">");
            stb.append("<font size=\""+m_FontSizeLinks+"\">");
            Posicao = 1;
            if (m_OffSet+1 % 2 == 0)
               Posicao = m_OffSet / 2;
            else
               Posicao = (m_OffSet / 2) + 1;

            stb.append("Página "+Posicao+" / De "+QtdPaginas);
            stb.append("</font>");
            stb.append("</td>\n");
            stb.append("\t\t\t\t</tr>\n");
         }
         stb.append("\t\t\t</table>\n");

         stb.append("</td>\n");
         stb.append("\t</tr>\n");
      }
      else
      {
         Vector Linha;
         int TamHeader = m_Header.size();
         int TamElementos = m_Elementos.size();

         /**
          * Ponto Critico em que foi criado um string buffer para melhorar o desempenho quando
          * tiver um tamanho de elementos muito grande
          * */
         for (int i = 0; i < TamElementos; i++)
         {
            if (i % 2 == 0)
            	stb.append("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(1)+"\">\n");
            else
            	stb.append("\t<tr bgcolor=\""+m_CoresLinhas.elementAt(2)+"\">\n");

            Linha = (Vector)m_Elementos.elementAt(i);
            for (int j = 0; j < TamHeader; j++)
            	stb.append("\t\t<td align=\""+m_Alinhamento.elementAt(j)+"\"" +Altura+">"+Linha.elementAt(j)+"</td>\n");
            stb.append("\t</tr>\n");
         }
      }
      m_SaidaStr += stb.toString();
      m_SaidaStr += "</table>";
      return true;
   }
}
