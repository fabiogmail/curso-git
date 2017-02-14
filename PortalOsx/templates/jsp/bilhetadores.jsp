<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="org.omg.CORBA.BAD_OPERATION"%>
<%@page import="org.omg.CORBA.COMM_FAILURE"%>
<%@page import="Portal.Utils.BilhetadorCfgDef"%>
<%@page import="Portal.Cluster.*"%>
<%

	 List bilhetadoresEscolhidos = new Vector();
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
	 
%>
<html>
<head>
<title>Sele&ccedil;&atilde;o de Bilhetadores</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="/PortalOsx/css/style.css" type="text/css">
<script type="text/javascript">
function init(){
	var bilhets = window.opener.document.form1.campoBilhetadores.value;
    if(bilhets!=null)
    {
    	if(bilhets.length > 0)
    	{
    		var arrayBilhets = bilhets.split(";");
    		for(var i = 0; i< arrayBilhets.length; i++){
    			document.form1.bilhet2.options[i] = new Option(arrayBilhets[i],arrayBilhets[i]);
    		}
    	}
    }
	
}
function Adiciona()
{
   var ListaBilhetadoresAAdicionar = new Array();
   var ListaBilhetadoresApresentados = new Array();

   // Acrescenta os indicadores ja apresentados num array a parte
   for (i = 0; i < document.form1.bilhet2.length; i++)
      ListaBilhetadoresApresentados.push(document.form1.bilhet2.options[i].text);

   // Monta um so array
   for (i = 0; i < ListaBilhetadoresApresentados.length; i++)
      ListaBilhetadoresAAdicionar.push(ListaBilhetadoresApresentados[i]);

   // Monta array de indicadores a adicionar
   var Adiciona = true;
   for (i = 0; i < document.form1.bilhet1.length; i++)
   {
      if (document.form1.bilhet1.options[i].selected == true)
      {
         Adiciona = true;
         for (j = 0; j < ListaBilhetadoresApresentados.length; j++)
         {
            if (ListaBilhetadoresApresentados[j] == document.form1.bilhet1.options[i].value)
            {
               Adiciona = false;
               break;
            }
         }
         if (Adiciona == true)
            ListaBilhetadoresAAdicionar.push(document.form1.bilhet1.options[i].value);
      }
   }

   if (ListaBilhetadoresAAdicionar.length == 0)
      return;

   if (ListaBilhetadoresAAdicionar.length == 0)
      return;

   while (document.form1.bilhet2.length != 0)
      document.form1.bilhet2.options[0] = null;

   for (i = 0; i < ListaBilhetadoresAAdicionar.length; i++)
      document.form1.bilhet2[i] = new Option(ListaBilhetadoresAAdicionar[i], ListaBilhetadoresAAdicionar[i], false, false);
}

function Remove()
{
   var ListaBilhetadoresARemover = new Array();

   for (i = 0; i < document.form1.bilhet2.length; i++)
   {
      if (document.form1.bilhet2.options[i].selected == true)
         ListaBilhetadoresARemover.push(document.form1.bilhet2.options[i].value);
   }

   for (i = 0; i < ListaBilhetadoresARemover.length; i++)
   {
      for (j = 0; j < document.form1.bilhet2.length; j++)
      {
         if (document.form1.bilhet2.options[j].value == ListaBilhetadoresARemover[i])
         {
            document.form1.bilhet2.options[j] = null;
            break;
         }
      }
   }
}

function SelecionaBilhetadores()
{
   
   var Bilhetadores = "";

   for (i = 0; i < document.form1.bilhet2.length; i++)
      Bilhetadores += document.form1.bilhet2.options[i].value + ";";

   if (Bilhetadores.length != 0)
      Bilhetadores = Bilhetadores.substr(0, Bilhetadores.length-1);
   else
   {
      alert ("Selecione pelo menos um indicador!");
      //window.close();
      return false;
   }



   //
   // Recupera o frame onde o gráfico está apresentado
   // Antes verifica se a janela do gráfico está aberta
   //
   if (window.opener.closed == true)
   {
      alert ("Janela do gráfico foi fechada! Abra-a novamente!");
      return false;
   }
   else
   {
   	  //salva na tela os valores que o usuário escolher
   	  window.opener.document.form1.campoBilhetadores.value = Bilhetadores;
   }	
  
   window.close();

   return false;
}

</script>
</head>
<body onLoad="init();">
<form name="form1" method="post" action="/PortalOsx/servlet/Portal.cPortal">
<input type="hidden" name="bilhetadoresDisponiveis" value="">
<input type="hidden" name="bilhetadoresApresentados" value="">


  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td><img src="/PortalOsx/imagens/indicadores.gif"></td>
    </tr>
    <tr>
      <td><font size="1" face="Verdana, Arial, Helvetica, sans-serif">
	         Selecione os bilhetadores:
		  </font>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
  </table>

    <tr>
      <td><table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr>
            <td width="49%" align="left"><strong><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Dispon&iacute;veis:</font></strong></td>
            <td>&nbsp;</td>
            <td width="51%" align="left"><strong><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Apresentados:</font></strong></td>
          </tr>
          <tr>
             <!--<td> -->
                <!--<table width="100%" border="1" cellspacing="0" cellpadding="0">
                   <tr> -->
                     <td align="center">
                     <select name="bilhet1" class="lista" size="15" multiple>
                        <%for(int i=0; i<Bilhetadores.size(); i++){%>
							<%
							BilhetadorCfgDef central = (BilhetadorCfgDef)Bilhetadores.get(i);																														
							%>
							<option value="<%=central.getBilhetador()%>"><%=central.getBilhetador()%></option>																																
						<%}%>														
                       </select>
                     </td>

                     <td align="center">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                           <tr>
                             <td align="center">
                                &nbsp;&nbsp;<input type="button" name="button" value=" >> " class="button" onClick="return Adiciona()">
                             </td>
                           </tr>
                           <tr>
                             <td align="center">
                                &nbsp;
                             </td>
                           </tr>
                           <tr>
                             <td align="center">
                                &nbsp;
                             </td>
                           </tr>
                           <tr>
                             <td align="center">
                                &nbsp;&nbsp;<input type="button" name="button" value=" << " class="button" onClick="return Remove()">
                             </td>
                           </tr>
                        </table>
                     </td>

                     <td align="center">
                       <select name="bilhet2" class="lista" size="15" multiple>		
                       </select>
                     </td>
                   <!--</tr>
                </table>
             </td>-->
          </tr>
        </table></td>
    </tr>
    <p>
      <input type="submit" name="Submit" value="Salvar" class="button" onClick="return SelecionaBilhetadores()">
      <input type="button" value="Cancelar" class="button" onClick="window.close()">
    </p>
</form>
</body>
</html>
