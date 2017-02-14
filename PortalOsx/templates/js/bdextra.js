function Processa(Funcao)
{
	switch (Funcao)
	{
		case 0:
			VerificaMensagem();
			MontaListaDatas();
			break;
		case 1:
			return Inclui();
		case 2:
			return Exclui();
		case 3:
			return ValidaForm();
		default:
			alert ("Função não encontrada!");
			break;
	}
}


function VerificaMensagem()
{
    szMensagem = document.form1.mensagem.value;    
 	if (szMensagem.charAt(0) != "$" && szMensagem != "null"){
		alert (document.form1.mensagem.value);		
	}
}

function MontaListaDatas()
{
	Lista = document.form1.ListaDatasServ.value;
	if (Lista.charAt(0) != "$")
	{
		var aDatas = Lista.split(";");

		while (document.form3.ListaDatas.length > 0)
			document.form3.ListaDatas.options[0] = null;

		for (i = 0; i < aDatas.length; i++)
			document.form3.ListaDatas[i] = new Option(aDatas[i], "", false, false);

		document.form3.ListaDatas.selectedIndex = -1;
	}
}


function InverteData (Data)
{
   var NovaData = "";

   NovaData = Data.substring(6, Data.length) + Data.substring(3, 5) + Data.substring(0, 2);
   return NovaData;
}


function Inclui()
{
   Data = document.form2.data.value;

   Expressao = new RegExp("[^0-9/]", "gi");
   Ret = Data.search(Expressao);

   if (document.form3.ListaDatas.length == 16)
   {
      alert("Lista de datas pode conter no máximo 16 dias!");
      return;
   }

   if (Ret != -1)
      alert("Data somente pode conter caracteres numéricos alem de '/'.");
   else
   {
      if (Data.length != 10)
      {
         alert("Data incorreta. Corrija-a!");
         return;
      }

      // Verificação detalhada
      var DataDet = new Array();
      DataDet[0] = Data.substring(0, 2);
      DataDet[1] = Data.substring(3, 5);
      DataDet[2] = Data.substring(6, 10);

      var Erro = false;
      for (i = 0; i < 3; i++)
      {
         Expressao = new RegExp("[^0-9]", "gi");
         Ret = DataDet[i].search(Expressao);

         if (Ret != -1)
            Erro = true;

         switch (i)
         {
            case 0:  // Dia
               if (Math.ceil(DataDet[0]) == 0 || Math.ceil(DataDet[0]) > 31)
                  Erro = true;
               break;
            case 1:  // Mes
               if (Math.ceil(DataDet[1]) == 0 || Math.ceil(DataDet[1]) > 12)
                  Erro = true;
               break;
            case 2:  // Ano
               if (Math.ceil(DataDet[2]) < 2000 || Math.ceil(DataDet[2]) > 2050)
                  Erro = true;
               break;
         }

         if (Erro == true)
         {
            alert("Data incorreta. Corrija-a!");
            return;
         }
      }

      Tam = document.form3.ListaDatas.length;
      for (i = 0; i < Tam; i++)
      {
         if (document.form3.ListaDatas.options[i].text == Data)
         {
            alert("Data já existe!");
            return;
         }
      }

      var bInclui = false;
      if (Tam == 1 && document.form3.ListaDatas.options[0].text == "")
         document.form3.ListaDatas[0] = new Option(Data, "", false, false);
      else
      {
         for (i = 0; i < Tam; i++)
         {
            if (InverteData (Data) <= InverteData (document.form3.ListaDatas[i].text))
            {
               // Reajusta a lista para a inserção da nova opção no lugar correto
               var ArrayDatas = new Array(Tam - i);
               for (k = 0, j = i; j < Tam; k++, j++)
               {
                  //alert ("0 - ArrayDatas = "+document.form3.ListaDatas[j].text);
                  ArrayDatas[k] = document.form3.ListaDatas[j].text;
               }

               // Insere o novo elemento na lista
               document.form3.ListaDatas[i] = new Option(Data, "", false, false);

               // Reinsere os demais elementos
               for (j = 0, i = i+1; j < ArrayDatas.length; j++, i++)
                  document.form3.ListaDatas[i] = new Option(ArrayDatas[j], "", false, false);

               bInclui = true;
               break;
            }
         }

         if (bInclui == false)
            document.form3.ListaDatas[Tam] = new Option(Data, "", false, false);
      }

      document.form2.data.value = "dd/mm/aaaa";
   }

   return false;
}

function Exclui()
{
   aSelecionados = new Array();
   Cont = 0;

   for (i = 0; i < document.form3.ListaDatas.length; i++)
   {
      if (document.form3.ListaDatas.options[i].selected == true)
      {
         aSelecionados[Cont] = document.form3.ListaDatas.options[i].text;
         Cont++;
      }
   }

   Cont = 0;
   while (Cont < aSelecionados.length)
   {
      for (i = 0; i < document.form3.ListaDatas.length; i++)
      {
         if (document.form3.ListaDatas.options[i].text == aSelecionados[Cont])
         {
            document.form3.ListaDatas.options[i] = null;
            Cont++;
            break;
         }
      }
   }

   return false;
}

function ValidaForm()
{
   Datas = "";

   if (document.form3.ListaDatas.length == 0)
      Datas = ";";  // O ";" vai ser retirado logo abaixo!
   else
   {
      for (i = 0; i < document.form3.ListaDatas.length; i++)
         Datas = Datas + document.form3.ListaDatas.options[i].text + ";"
   }

   Datas = Datas.substring(0, Datas.length-1);
   document.form4.novalistadatas.value = Datas;
   return false;
}