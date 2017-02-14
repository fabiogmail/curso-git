package Portal.ComponentesHTML;

/**
   Encapsula Tags HTML
   A Classe HTML.Tag é meio enrrolada
*/
public class HTMLTags
{
   public int m_Nivel = 0;
   public static String ASPAS="\"";
   public static String NL="\n";
   public static String SP="&nbsp;";

   public static String VAR(String p_Nome, String p_Valor)
   {
//      return p_Nome+"="+ASPAS+p_Valor+ASPAS+" ";
      if ((p_Valor == null) || (p_Nome.compareTo("")==0))
         return p_Nome+"="+ASPAS+ASPAS;
      else
         return p_Nome+"="+p_Valor;
   }

   public static String VAR(String p_Nome, int p_Valor)
   {
      return VAR(p_Nome, ""+p_Valor);
   }

   public static String COR(String p_Cor)
   {
      return "#"+p_Cor;
   }

   public static String Tag(String p_Tag)
   {
      return "<"+p_Tag+">"+NL;
   }

   public static String Tag(String p_Tag, String p_Parms)
   {
      return "<"+p_Tag+" "+p_Parms+">"+NL;
   }

   public static String FORM(String p_Name, String p_Method, String p_Action)
   {
      return Tag("form", VAR("name",p_Name)+" "+VAR("method",p_Method)+" "+VAR("action",p_Action));
   }

   public static String _FORM()
   {
      return Tag("/form");
   }

   public static String BR()
   {
      return Tag("br");
   }

   public static String B()
   {
      return Tag("b");
   }

   public static String _B()
   {
      return Tag("/b");
   }

   public static String DIV()
   {
      return Tag("div");
   }

   public static String DIV(String p_Parms)
   {
      return Tag("div", p_Parms);
   }

   public static String _DIV()
   {
      return Tag("/div");
   }

   public static String TD()
   {
      return Tag("td");
   }

   public static String TD(String p_Parms)
   {
      return Tag("td", p_Parms);
   }

   public static String _TD()
   {
      return Tag("/td");
   }

   public static String P()
   {
      return Tag("p");
   }

   public static String P(String p_Parms)
   {
      return Tag("p", p_Parms);
   }

   public static String _P()
   {
      return Tag("/p");
   }

   public static String TR()
   {
      return Tag("tr");
   }

   public static String TR(String p_Parms)
   {
      return Tag("tr", p_Parms);
   }

   public static String _TR()
   {
      return Tag("/tr");
   }

   public static String INPUT(String p_Type, String p_Name, String p_Value)
   {
      return Tag("input",VAR("type",p_Type)+" "+VAR("name",p_Name)+" "+VAR("value",p_Value));
   }

   public static String INPUT(String p_Type, String p_Name, String p_Value, String p_Adicional)
   {
      return Tag("input",VAR("type",p_Type)+" "+VAR("name",p_Name)+" "+VAR("value",p_Value)+" "+p_Adicional);
   }

   public static String INPUT(String p_Type, int p_Tam, String p_Name, String p_Value)
   {
      return Tag("input",VAR("type",p_Type)+" "+VAR("size",p_Tam)+" "+VAR("name",p_Name)+" "+VAR("value",p_Value));
   }

   public static String AHREF(String p_Servlet, String p_Text)
   {
      return Tag("a",VAR("href",p_Servlet))+p_Text+Tag("/a");
   }

   public static String AHREFH(String p_Servlet, String p_Help, String p_Text)
   {
      return Tag("a",VAR("href",p_Servlet+" class=\"link\" onmouseover=\"window.status='"+p_Help+"';return true;\" onmouseout=\"window.status='';return true;\" "))+p_Text+Tag("/a");
/*          " class=\"link\" onmouseover=\"window.status='Deseleciona todos';return true;\" onmouseout=\"window.status='';return true;\" "*/
   }

   public static String AHREF(String p_Servlet, String p_Cor, String p_Text)
   {
      return Tag("a",VAR("href",p_Servlet))+FONT(VAR("color",p_Cor))+B()+p_Text+_FONT()+_B()+Tag("/a");
   }

   public static String AHREFH(String p_Servlet, String p_Help, String p_Cor, String p_Text)
   {
      return Tag("a",VAR("href",p_Servlet+" class=\"link\" onmouseover=\"window.status='"+p_Help+"';return true;\" onmouseout=\"window.status='';return true;\" "))+FONT(VAR("color",p_Cor))+B()+p_Text+_FONT()+_B()+Tag("/a");
/*      return Tag("a",VAR("href",p_Servlet))+
        " class=\"link\" onmouseover=\"window.status='"+p_Help+"';return true;\" onmouseout=\"window.status='';return true;\" "+FONT(VAR("color",p_Cor))+B()+p_Text+_FONT()+_B()+Tag("/a");*/
   }

   public static String FONT(String p_Parms)
   {
      return Tag("font", p_Parms);
   }

   public static String _FONT()
   {
      return Tag("/font");
   }

   public static String TABLE(String p_Parms)
   {
      return Tag("table", p_Parms);
   }

   public static String _TABLE()
   {
      return Tag("/table");
   }

   public static String HTML()
   {
      return Tag("html");
   }

   public static String _HTML()
   {
      return Tag("/html");
   }

   public static String HEAD()
   {
      return Tag("head");
   }

   public static String _HEAD()
   {
      return Tag("/head");
   }

   public static String TITLE()
   {
      return Tag("title");
   }

   public static String _TITLE()
   {
      return Tag("/title");
   }

   public static String BODY()
   {
      return Tag("body");
   }

   public static String BODY(String p_Parms)
   {
      return Tag("body", p_Parms);
   }

   public static String _BODY()
   {
      return Tag("/body");
   }
}

