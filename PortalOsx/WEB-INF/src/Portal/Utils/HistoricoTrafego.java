package Portal.Utils;

import java.util.StringTokenizer;
import java.util.Vector;

import libjava.tipos.TipoData;
import CDRView2.Cliente;
import Portal.Cluster.NoUtil;
import Portal.Conexao.CnxServUtil;
import Portal.Configuracoes.ArquivosDefs;
import Portal.Configuracoes.DefsComum;

public class HistoricoTrafego
{
   public static final String COR_HEADER="#000066";         // Azul escuro
   public static final String COR_FONTEHEADER="#FFFFFF";    // Branco
   public static final String COR_LINHASIM="#FFFFFF";       // Branco
   public static final String COR_LINHANAO="#F0F0F";        // Cinza claro
   public static final String COR_NORMAL="#CCFFFF";         // Cian
   public static final String COR_CONGESTIONADO="#FFCC33";  // Laranja // FF9900
   public static final String COR_OCIOSO="#FFFF99";         // Amarelo
   public static final String COR_ANOMALA="#FF6666";        // Rosa
   public static final String COR_ALERTA="#99FF99";         // Verde claro

//   public static final String FONTE="\"verdana\" size=\"1\"";
   public static final String FONTE="\"arial\" size=\"1\"";
   
   /***************************************************************************************/
/*
// Calcula o fatorial de um numero
   static private long fnFatorial(long p_Valor)
   {
      if (p_Valor == 0)
         return 1;
      long l_Fatorial=1;
      for (int a=2; a<=p_Valor; a++)
         l_Fatorial *= a;

      return l_Fatorial;
   }

// Erlang B com Fatorial
   static private double fnPerdaOLD(long p_Circuitos, double p_Trafego)
   {
      double l_Num = Math.pow(p_Trafego, (double) p_Circuitos) / ((double) fnFatorial(p_Circuitos));
      double l_Den = 0;
      for (long i=0; i<=p_Circuitos; i++)
         l_Den += (Math.pow(p_Trafego, i) / ((double) fnFatorial(i)));
      return l_Num / l_Den;
   }

// Calcula o trafego oferecido
   static private double fnTrafegoOferecido(double p_Trafego, double p_Perda)
   {
      return p_Trafego/(1-p_Perda);
   }

// Determina o Tráfego Limite
   static private double fnTrafegoMaximo(double p_Perda, long p_CircuitosLimite)
   {
      return p_CircuitosLimite*(1-p_Perda);
   }
*/

// Qtd de circuitos de 2Mb
   static private long fnCircuitos2MNecessarios(long p_Circuitos)
   {
      long l_Resto = (p_Circuitos-1) % 30;
      long l_Ret=(((p_Circuitos-l_Resto)/30)+1)*30;
      return l_Ret;
   }

// Erlang B - recursiva (Bem mais rápida)
   static private double fnPerda(long p_Circuitos, double p_Trafego)
   {
      double l_Perda = 1.0;
      for (long i=0; i<p_Circuitos; i++)
         l_Perda = (p_Trafego * l_Perda) / (i + 1 + p_Trafego * l_Perda);
      return l_Perda;
   }

// Determina a quantidade de circuitos para atingir a perda X   
   static private long fnCircuitosNecessarios(double p_Trafego, double p_GrauDeServico)
   {
      double l_Perda = 1.0;
      for (long c=1;; c++)
      {
         l_Perda = (p_Trafego * l_Perda) / (c + p_Trafego * l_Perda);
         if (l_Perda <= p_GrauDeServico)
            return c;
      }
   }

// Determina o grau de servico em função do tipo de rota
   static private double fnGrauServico(String p_RotaEmbratel, String p_Rede)
   {
      String l_TipoRota = p_RotaEmbratel.substring(p_RotaEmbratel.length()-2, p_RotaEmbratel.length()-1);
      if (p_Rede.equals("RTBI") && l_TipoRota.equals("F"))
         return 0.001;
      else if (l_TipoRota.equals("A"))
         return 0.1;
      else
         return 0.01;
  }

// Determina a quantidade de circuitos limite   
   static private double fnTrafegoOferecido(double p_GrauDeServico, double p_Trafego, long p_CircuitosAtivos)
   {
      if (p_CircuitosAtivos > 0)
      {
         if (p_Trafego >= p_CircuitosAtivos || p_CircuitosAtivos <= 0)
            return 0.0;
         boolean l_I1 = true;
         boolean l_I2 = true;
         double p_Delta=10;
         if (p_CircuitosAtivos <= 10)
            p_Delta = 1;
         double l_A = p_Trafego/0.9;
         double l_Ae = 0;
         double l_Perda = 1.0;
         
         for (int j=1; j<=10000; j++)
         {
            l_Perda = 1.0;
            for (long k=1; k<=p_CircuitosAtivos; k++)
               l_Perda = (l_A * l_Perda) / (((double) k) + l_A * l_Perda);

            l_Ae = l_A * (1-l_Perda);
            if (Math.abs(l_Ae-p_Trafego) <= 0.001)
               break;
            if (l_Ae > p_Trafego)
            {
               if (! l_I1)
                  p_Delta /= 10;
               l_A-=p_Delta;
               l_I1 = true;
               l_I2 = false;
            } else
            {
               if (! l_I2)
                  p_Delta /= 10;
               l_A+=p_Delta;
               l_I2 = true;
               l_I1 = false;
            }

            // Isto não tem na fórmula passada pela EBT
            if (l_A < 0)
               break;
         }
         if (l_Perda >= 0.3)
            return (l_Ae * (1 - l_Perda * 0.9)) / (1 - l_Perda);
         // Isto não tem na fórmula passada pela EBT
         else if (l_A < 0)
            return p_Trafego; 
         else
            return l_A;
      } else
         return 0.0;
   }

   /***************************************************************************************/
   private static final short NORMAL=0;
   private static final short CONGESTIONADO=1;
   private static final short OCIOSO=2;
   private static final short ANOMALA=3;
   private static final short ALERTA=4;
   
   class Calculado
   {
      public double m_TrafegoMax;
      public double m_GrauDeServico;
      public double m_Perda;
      public double m_TrafegoOferecido;
      public long m_CircuitosNecessarios;
      public long m_Circuitos2MNecessarios;
      public short m_Posicao; // Congestionada; Normal; Ociosa; Alerta; Anomala

      public Calculado(double p_TrafegoLimite, double p_GrauDeServico, long p_CircuitosAtivos, double p_TrafegoDiario [], int p_Selecionados [])
      {
         m_TrafegoMax = fnMaior(p_TrafegoDiario, p_Selecionados);
         m_GrauDeServico = p_GrauDeServico;
         m_TrafegoOferecido = fnTrafegoOferecido(m_GrauDeServico, m_TrafegoMax, p_CircuitosAtivos);
         m_Perda = fnPerda(p_CircuitosAtivos, m_TrafegoOferecido);
         m_CircuitosNecessarios = fnCircuitosNecessarios(m_TrafegoOferecido, m_GrauDeServico);

         m_Circuitos2MNecessarios = fnCircuitos2MNecessarios(m_CircuitosNecessarios);
         m_Posicao = fnPosicao(m_TrafegoMax, p_TrafegoLimite, p_CircuitosAtivos);
      }
   }

   public short fnPosicao(double p_TrafEscoado, double p_TrafLimite, long p_CircuitosAtivos)
   {
      if (p_TrafEscoado >= p_CircuitosAtivos) 
      {
//         System.out.println("Anomala - p_TrafEscoado="+p_TrafEscoado+", p_CircuitosAtivos="+p_CircuitosAtivos);
         return ANOMALA;
      }
      if (p_TrafEscoado >= p_TrafLimite) return CONGESTIONADO;
      if (p_TrafEscoado < (p_TrafLimite*0.8)) return OCIOSO;
      if (p_TrafEscoado >= (p_TrafLimite*0.95)) return ALERTA;
      return NORMAL;
   }
   
   public String fnCorPosicao(short p_Posicao)
   {
      switch(p_Posicao)
      {
         case NORMAL:
            return COR_NORMAL;
         case CONGESTIONADO:
            return COR_CONGESTIONADO;
         case OCIOSO:
            return COR_OCIOSO;
         case ANOMALA:
            return COR_ANOMALA;
         case ALERTA:
            return COR_ALERTA;
      }
      return null;
   }

   /***************************************************************************************/
   class Linha
   {
      public String m_Central;
      public String m_Rota;
      public String m_Embratel;
      public String m_Rede;
      public String m_Gerencia;
      public String m_Holding;
      public String m_Prestadora;
      public long m_CircuitosAtivos;
      public double m_TrafegoLimite;
      public double m_TrafegoDiario [];
      
      public Linha(String p_Linha [])
      {
// 0;AJUB;ROTA1 ;ROTA1 EMB              ;RTDC;Tele Norte  ;TELERGIPE;50;10.10;3.80;22.20;
// 0;AJUB;EIMDXF;AJU CPT IMD AJU TR B FN;RTDC;SERVICO 0800;-        ;7 ;2.4;4.1;3.8;3.7;3.8;3.6;2.6;0.0

         m_Central = p_Linha[1];
         m_Rota = p_Linha[2];
         m_Embratel = p_Linha[3];
         m_Rede = p_Linha[4];
         m_Gerencia = p_Linha[5];
         m_Holding = p_Linha[6];
         m_Prestadora = p_Linha[7];
         m_CircuitosAtivos = Long.parseLong(p_Linha[8]);
         m_TrafegoLimite = Double.parseDouble(p_Linha[9]);
         int l_QtdDias = p_Linha.length - 10;
         if (l_QtdDias > 0)
         {
            m_TrafegoDiario = new double [l_QtdDias];
            for (int a=0; a<l_QtdDias; a++)
               m_TrafegoDiario [a] = Double.parseDouble(p_Linha[10+a]);
         } else
            m_TrafegoDiario = null;
      }
   }

   class PtLinha
   {
      public Linha m_Linha;
      public PtLinha(Linha p_Linha)
      {
         m_Linha = p_Linha;
      }
   }
   /***************************************************************************************/
   public TipoData m_DataIni=null;
   public TipoData m_DataFim=null;
   public TipoData m_Datas [];
   private int m_DiasSemana [];
   private Vector m_Linhas = new Vector();
   public Vector m_LinhasUltimaPesquisa = new Vector();
   private Vector m_LinhasCabecalho = new Vector();
   private CnxServUtil m_ConexUtil;
   // Qtd de linhas de filtros no relatorio
   private static final int QTD_FILTROS=12;
   // Marca a linha inicial dos contadores
   private static final int INICIO_LINHAS=16;
   private String m_Filtros [] = new String[QTD_FILTROS];
   public int m_Dias [] = null;
   
   public Vector m_Centrais = new Vector();
   public Vector m_Redes = new Vector();
   public Vector m_Gerencias = new Vector();
   public Vector m_Holdings = new Vector();
   public Vector m_Prestadoras = new Vector();

   /***************************************************************************************/
   public int m_Pag = 0;
   
   public Vector fnListaRecursosVec(int p_Pos)
   {
      switch (p_Pos)
      {
         case 0:
            return m_Centrais;
         case 1:
            return m_Redes;
         case 2:
            return m_Gerencias;
         case 3:
            return m_Holdings;
         case 4:
            return m_Prestadoras;
      }
      return null;
   }
   
   public Vector fnLinhasCabecalho()
   {
      return m_LinhasCabecalho;
   }
   
   public boolean m_Colunas [] = new boolean [15];

   public HistoricoTrafego(CnxServUtil p_ConexUtil)
   {
      m_ConexUtil = p_ConexUtil;
      for (int a=0; a<m_Colunas.length; a++)
         m_Colunas[a] = true;
      m_Colunas[14] = false;
   }

   private void fnTrataData(String p_Linha [])
   {
      int l_Qtd = p_Linha.length - 1; 
      m_Datas = new TipoData [l_Qtd];
      int l_DiasSemana [] = new int [l_Qtd];
      int l_QtdDiasSemana = 0;
      for (int a=0; a<l_Qtd; a++)
      {
         m_Datas[a] = new TipoData(p_Linha[a+1]);
         int l_DiaSemana = m_Datas[a].fnDiaSem();
         if (l_DiaSemana > 1 && l_DiaSemana < 7)
            l_DiasSemana [l_QtdDiasSemana++] = a;
      }

      if (l_QtdDiasSemana > 0)
      {
         m_DiasSemana = new int [l_QtdDiasSemana];
         for (int a=0; a<l_QtdDiasSemana; a++)
            m_DiasSemana[a] = l_DiasSemana[a];
      } else
         m_DiasSemana = null;
   }

   private String m_Perfil;
   private String m_TipoRel;
   private String m_IdRel;
   private String m_Arquivo;
   private String m_NomeRelatorio;
   private String m_DataGeracao;
   
   public boolean fnLeRelatorio(String p_Perfil, String p_TipoRel, String p_IdRel, String p_Arquivo, String p_NomeRelatorio, String p_DataGeracao)
   {
      m_Perfil = p_Perfil;
      m_TipoRel = p_TipoRel;
      m_IdRel = p_IdRel;
      m_Arquivo = p_Arquivo;
      m_NomeRelatorio = p_NomeRelatorio;
      m_DataGeracao = p_DataGeracao;
      
      Vector l_Linhas = m_ConexUtil.getRelatorio2((short) 1,
         p_Perfil,
         Integer.parseInt(p_TipoRel),
         Integer.parseInt(p_IdRel),
         p_DataGeracao,
         p_Arquivo,
         p_NomeRelatorio);
         
      if (l_Linhas != null)
      {
         // Le os filtros
         for (int i = 5; i < QTD_FILTROS; i++)
         {
            m_Filtros[i] = (String)l_Linhas.elementAt(i);
            if (m_Filtros[i].length() == 0)
               m_Filtros[i] = "Sem Filtro";
            break;
         }

         // Le a lista de colunas do relatorio
//         fnSetColunas((String) l_Linhas.elementAt(12), (String) l_Linhas.elementAt(13), (String) l_Linhas.elementAt(14));
         m_LinhasCabecalho.removeAllElements();
         m_Linhas.removeAllElements();

         m_DataIni = new TipoData(l_Linhas.elementAt(3).toString());
         m_DataFim = new TipoData(l_Linhas.elementAt(4).toString());
/*
         for (int i = 0; i<INICIO_LINHAS; i++)
            System.out.println("Linha "+i+" "+l_Linhas.elementAt(i));
*/

         for (int i = INICIO_LINHAS; i < l_Linhas.size(); i++)
         {
//            System.out.println("Lendo linha "+(i-INICIO_LINHAS));
            String l_Linha = (String) l_Linhas.elementAt(i);

            if ((l_Linha.indexOf("0;") == 0) || (l_Linha.indexOf("6;") == 0))
            {
               String l_LinhaToken [] = fnFromCSV(l_Linha);
               for (int a=0; a<l_LinhaToken.length; a++)
               {
                  if (l_LinhaToken[a].equals("-"))
                     l_LinhaToken[a] = "";
               }
               Linha l_Contadores = new Linha(l_LinhaToken);
               m_Linhas.addElement(l_Contadores);

               if ((!l_Contadores.m_Central.equals("")) && fnProcura(m_Centrais, l_Contadores.m_Central) == null)
                  m_Centrais.addElement(l_Contadores.m_Central);
               if ((!l_Contadores.m_Rede.equals("")) && fnProcura(m_Redes, l_Contadores.m_Rede) == null)
                  m_Redes.addElement(l_Contadores.m_Rede);
               if ((!l_Contadores.m_Gerencia.equals("")) && fnProcura(m_Gerencias, l_Contadores.m_Gerencia) == null)
                  m_Gerencias.addElement(l_Contadores.m_Gerencia);
               if ((!l_Contadores.m_Holding.equals("")) && fnProcura(m_Holdings, l_Contadores.m_Holding) == null)
                  m_Holdings.addElement(l_Contadores.m_Holding);
               if ((!l_Contadores.m_Prestadora.equals("")) && fnProcura(m_Prestadoras, l_Contadores.m_Prestadora) == null)
                  m_Prestadoras.addElement(l_Contadores.m_Prestadora);
                  
            } else if (l_Linha.indexOf("5;") == 0)
            {
               String l_LinhaToken [] = fnFromCSV(l_Linha);
               m_LinhasCabecalho.add(l_LinhaToken[1]+(l_LinhaToken.length > 2?" = "+l_LinhaToken[2]:""));
            } else if (l_Linha.indexOf("7;") == 0)
            {
               String l_LinhaToken [] = fnFromCSV(l_Linha);
               fnTrataData(l_LinhaToken);
            }
            
            m_Linhas.trimToSize();
         }

         return true;
      }
      else
      {
         System.out.println("Linha == null");
         return false;
      }
   }

   public String fnLinha(int p_Cont, int p_Pos)
   {
      return fnLinhasHTML(p_Cont,((PtLinha) m_LinhasUltimaPesquisa.elementAt(p_Pos)).m_Linha);
   }
   
   public void fnOrdena(int p_Coluna)
   {
      Linha l_Linhas [] = new Linha [m_LinhasUltimaPesquisa.size()];
      for (int a=0; a<m_LinhasUltimaPesquisa.size(); a++)
         l_Linhas[a] = ((PtLinha) m_LinhasUltimaPesquisa.elementAt(a)).m_Linha;
      fnSort((Linha[])l_Linhas.clone(), l_Linhas, 0, l_Linhas.length, p_Coluna, true);
      m_LinhasUltimaPesquisa.removeAllElements();
      for (int a=0; a<l_Linhas.length; a++)
      {
         m_LinhasUltimaPesquisa.addElement(new PtLinha(l_Linhas[a]));
      }
   }

   public String fnLinhasHTML(int p_Pos, Linha p_Linha)
   {
      String l_Cor = " bgcolor=\""+COR_LINHASIM+"\"";
      if (p_Pos%2 != 0)
         l_Cor = " bgcolor=\""+COR_LINHANAO+"\"";
         
      String l_String = "   <TR>\n";
      if (m_Colunas[0])
         l_String += "      <td "+l_Cor+"><font face="+FONTE+" nowrap>"+p_Linha.m_Central+"</font></td>";
      if (m_Colunas[1])
         l_String += "<td align=\"left\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_Rota+"</font></td>";
      if (m_Colunas[2])
         l_String += "<td align=\"left\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_Embratel+"</font></td>";
      if (m_Colunas[3])
         l_String += "<td align=\"left\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_Rede+"</font></td>";
      if (m_Colunas[4])
         l_String += "<td align=\"left\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_Gerencia+"</font></td>";
      if (m_Colunas[5])
         l_String += "<td align=\"left\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_Holding+"</font></td>";
      if (m_Colunas[6])
         l_String += "<td align=\"left\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_Prestadora+"</font></td>";
      if (m_Colunas[7])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+p_Linha.m_CircuitosAtivos+"</font></td>";
      if (m_Colunas[8])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+new java.text.DecimalFormat("0.00").format(p_Linha.m_TrafegoLimite)+"</font></td>";      
      Calculado l_Dias = new Calculado(p_Linha.m_TrafegoLimite, fnGrauServico(p_Linha.m_Embratel, p_Linha.m_Rede), p_Linha.m_CircuitosAtivos, p_Linha.m_TrafegoDiario, m_Dias);
      if (m_Dias == null)
      {
         for (int b=0; b<m_Datas.length; b++)
         {
            l_String += "<td align=\"center\" nowrap bgcolor=\""+fnCorPosicao(fnPosicao(p_Linha.m_TrafegoDiario[b], p_Linha.m_TrafegoLimite, p_Linha.m_CircuitosAtivos))+"\"><font face="+FONTE+">"+new java.text.DecimalFormat("0.00").format(p_Linha.m_TrafegoDiario[b])+"</font></td>";
         }
      } else
      {
         for (int b=0; b<m_Dias.length; b++)
            l_String += "<td align=\"center\" nowrap  bgcolor=\""+fnCorPosicao(fnPosicao(p_Linha.m_TrafegoDiario[b], p_Linha.m_TrafegoLimite, p_Linha.m_CircuitosAtivos))+"\"><font face="+FONTE+">"+new java.text.DecimalFormat("0.00").format(p_Linha.m_TrafegoDiario[m_Dias[b]])+"</font></td>";
      }
      if (m_Colunas[9])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+new java.text.DecimalFormat("0.00").format(l_Dias.m_TrafegoMax)+"</font></td>";
      if (m_Colunas[10])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+new java.text.DecimalFormat("0.00").format(l_Dias.m_Perda*100)+"</font></td>";
      if (m_Colunas[11])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+new java.text.DecimalFormat("0.00").format(l_Dias.m_TrafegoOferecido)+"</font></td>";
      if (m_Colunas[12])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+l_Dias.m_CircuitosNecessarios+"</font></td>";
      if (m_Colunas[13])
         l_String += "<td align=\"right\" nowrap "+l_Cor+"><font face="+FONTE+">"+l_Dias.m_Circuitos2MNecessarios+"</font></td>";
      if (m_Colunas[14])
      {
         switch(l_Dias.m_Posicao)
         {
            case NORMAL:
               l_String += "<td align=\"right\" nowrap bgcolor=\""+fnCorPosicao(l_Dias.m_Posicao)+"\"><font face="+FONTE+">N</font></td>\n";
               break;
            case CONGESTIONADO:
               l_String += "<td align=\"right\" nowrap bgcolor=\""+fnCorPosicao(l_Dias.m_Posicao)+"\"><font face="+FONTE+">C</font></td>\n";
               break;
            case OCIOSO:
               l_String += "<td align=\"right\" nowrap bgcolor=\""+fnCorPosicao(l_Dias.m_Posicao)+"\"><font face="+FONTE+">O</font></td>\n";
               break;
            case ANOMALA:
               l_String += "<td align=\"right\" nowrap bgcolor=\""+fnCorPosicao(l_Dias.m_Posicao)+"\"><font face="+FONTE+">X</font></td>\n";
               break;
            case ALERTA:
               l_String += "<td align=\"right\" nowrap bgcolor=\""+fnCorPosicao(l_Dias.m_Posicao)+"\"><font face="+FONTE+">A</font></td>\n";
               break;
         }
      }

      l_String += "   </TR>\n";
      return l_String;
   }
   
   public void fnLinhas(String p_Central, String p_Rede, String p_Gerencia, String p_Holding, String p_Prestadora)
   {
      String l_Central [] = fnFromCSV(p_Central);
      String l_Rede [] = fnFromCSV(p_Rede);
      String l_Gerencia [] = fnFromCSV(p_Gerencia);
      String l_Holding [] = fnFromCSV(p_Holding);
      String l_Prestadora [] = fnFromCSV(p_Prestadora);
      int c = 0;
      
      m_LinhasUltimaPesquisa.removeAllElements();
      for (int a=0; a<m_Linhas.size(); a++)
      {
         Linha l_Linha = (Linha) m_Linhas.elementAt(a);
         boolean l_EhValido = true;

         if (l_Central != null)
         {
            boolean l_Encontrou = false;
            for (int b=0; b<l_Central.length; b++)
               if (l_Linha.m_Central.equals(l_Central[b]))
               {
                  l_Encontrou = true;
                  break;
               }
            if (! l_Encontrou)
               l_EhValido = false;
         }

         if (l_EhValido && l_Rede != null)
         {
            boolean l_Encontrou = false;
            for (int b=0; b<l_Rede.length; b++)
               if (l_Linha.m_Rede.equals(l_Rede[b]))
               {
                  l_Encontrou = true;
                  break;
               }
            if (! l_Encontrou)
               l_EhValido = false;
         }

         if (l_EhValido && l_Gerencia != null)
         {
            boolean l_Encontrou = false;
            for (int b=0; b<l_Gerencia.length; b++)
               if (l_Linha.m_Gerencia.equals(l_Gerencia[b]))
               {
                  l_Encontrou = true;
                  break;
               }
            if (! l_Encontrou)
               l_EhValido = false;
         }

         if (l_EhValido && l_Holding != null)
         {
            boolean l_Encontrou = false;
            for (int b=0; b<l_Holding.length; b++)
               if (l_Linha.m_Holding.equals(l_Holding[b]))
               {
                  l_Encontrou = true;
                  break;
               }
            if (! l_Encontrou)
               l_EhValido = false;
         }

         if (l_EhValido && l_Prestadora != null)
         {
            boolean l_Encontrou = false;
            for (int b=0; b<l_Prestadora.length; b++)
               if (l_Linha.m_Prestadora.equals(l_Prestadora[b]))
               {
                  l_Encontrou = true;
                  break;
               }
            if (! l_Encontrou)
               l_EhValido = false;
         }

         if (l_EhValido)
         {
            m_LinhasUltimaPesquisa.addElement(new PtLinha(l_Linha));
         }
      }
   }

   private String fnColunaHeader(int p_Coluna, String p_Tag, int rowspan, boolean p_NoWrap, String p_Alinhamento, boolean p_ComOrdena)
   {
      if (p_ComOrdena)
         return fnColunaHeader(
/*                "<a href=\"/PortalOsx/servlet/Portal.cPortal?operacao=apresentaRelHistoricoTrafego&suboperacao=ordenacao&"+
                "perfil="+m_Perfil+
                "&tiporel="+m_TipoRel+
                "&idrel="+m_IdRel+
                "&arquivo="+m_Arquivo+
                "&nomerel="+m_NomeRelatorio+
                "&datageracao="+m_DataGeracao+
                "&tipo=ascendente"+
                "&coluna="+p_Coluna+"\" "+
                "class=\"link2\" "+
                "alt=\"Ordenar por "+p_Tag+"\">"+p_Tag+"</a>", rowspan, p_NoWrap, p_Alinhamento);
*/

                "<a href=\"javascript:Ordena('"+
                m_Perfil+"','"+
                m_TipoRel+"','"+
                m_IdRel+"','"+
                m_Arquivo+"','"+
                m_NomeRelatorio+"','"+
                m_DataGeracao+"','"+
                "ascendente"+"','"+
                p_Coluna+"')\""+
                " class=\"link2\" " +
                "onmouseover=\"window.status='Ordenar por "+p_Tag+"';return true;\" onmouseout=\"window.status='';return true;\" "+
                //"alt=\"Ordenar por "+p_Tag+"\">"+p_Tag+"</a>", rowspan, p_NoWrap, p_Alinhamento);
                ">"+p_Tag+"</a>", rowspan, p_NoWrap, p_Alinhamento);
      else
         return fnColunaHeader(p_Tag, rowspan, p_NoWrap, p_Alinhamento);
   }

   private String fnColunaHeader(String p_Tag, int rowspan, boolean p_NoWrap, String p_Alinhamento)
   {
      return "<td "+(rowspan > 0? "rowspan=\""+rowspan+"\"": "")+" align=\""+p_Alinhamento+"\" "+(p_NoWrap ? "nowrap": "")+"><font face="+FONTE+" color=\""+COR_FONTEHEADER+"\"><b>"+p_Tag+"</b></font></td>";
   }
   
   public String fnHeader(boolean p_ComOrdena)
   {
      String l_String = "";
  
      l_String += "   <tr bgcolor=\""+COR_HEADER+"\">\n";
      if (m_Colunas[0])
         l_String += fnColunaHeader(0, "Central", 2, true, "center", p_ComOrdena);
      if (m_Colunas[1])
         l_String += fnColunaHeader(1, "Rota Fab", 2, true, "center", p_ComOrdena);
      if (m_Colunas[2])
         l_String += fnColunaHeader(2, "Rota EBT", 2, true, "center", p_ComOrdena);
      if (m_Colunas[3])
         l_String += fnColunaHeader(3, "Rede", 2, true, "center", p_ComOrdena);
      if (m_Colunas[4])
         l_String += fnColunaHeader(4, "Gerência", 2, true, "center", p_ComOrdena);
      if (m_Colunas[5])
         l_String += fnColunaHeader(5, "Holding", 2, true, "center", p_ComOrdena);
      if (m_Colunas[6])
         l_String += fnColunaHeader(6, "Prest", 2, true, "center", p_ComOrdena);
      /*
      if (m_Colunas[7])
         l_String += fnColunaHeader(7, "Circ Ativ", 2, false, "center", p_ComOrdena);
      if (m_Colunas[8])
         l_String += fnColunaHeader(8, "Tr&aacute;f Lim", 2, false, "center", p_ComOrdena);
      */
      int l_Qtd1 = 0;
      if (m_Colunas[7])
         l_Qtd1++;
      if (m_Colunas[8])
         l_Qtd1++;
      if (l_Qtd1 > 0)
         l_String += "<td nowrap align=\"center\" colspan=\""+l_Qtd1+"\"><font face="+FONTE+" color=\""+COR_FONTEHEADER+"\"><b>"+m_Datas[m_Datas.length-1].fnConverte("dd/MM")+"</b></font></td>\n";

      l_String += "<td nowrap align=\"center\" colspan=\""+(m_Dias==null?m_Datas.length:m_Dias.length)+"\"><font face="+FONTE+" color=\""+COR_FONTEHEADER+"\"><b>Tr&aacute;f Esc ["+m_DataIni.fnConverte("HH:mm")+"-"+m_DataFim.fnConverte("HH:mm")+"]</b></font></td>\n";

      if (m_Colunas[9])
         l_String += fnColunaHeader("Tr&aacute;f M&aacute;x", 2, false, "center");
      if (m_Colunas[10])
         l_String += fnColunaHeader("Perda (%)", 2, false, "center");
      if (m_Colunas[11])
         l_String += fnColunaHeader("Tr&aacute;f Ofer", 2, false, "center");
      if (m_Colunas[12])
         l_String += fnColunaHeader("Circ Nec", 2, false, "center");
      if (m_Colunas[13])
         l_String += fnColunaHeader("Circ x2M", 2, false, "center");
      if (m_Colunas[14])
         l_String += fnColunaHeader("Fx", 2, true, "center");
      l_String += "   </TR>\n";

      l_String += "   <tr bgcolor=\""+COR_HEADER+"\">\n";

      if (m_Colunas[7])
         l_String += fnColunaHeader(7, "C Ativ", 0, true, "center", p_ComOrdena);
      if (m_Colunas[8])
         l_String += fnColunaHeader(8, "T Lim", 0, true, "center", p_ComOrdena);
      if (m_Dias == null)
      {
         for (int b=0; b<m_Datas.length; b++)        
            l_String += fnColunaHeader(9+b, m_Datas[b].fnConverte("dd/MM"), 0, true, "center", p_ComOrdena);
      }
      else
      {
         for (int b=0; b<m_Dias.length; b++)
            l_String += fnColunaHeader(9+b, m_Datas[m_Dias[b]].fnConverte("dd/MM"), 0, true, "center", p_ComOrdena);
      }
      l_String += "   </tr>\n";
      return l_String;
   }

   /***************************************************************************************/
   static public Object fnProcura(Vector p_Vetor, String p_Chave)
   {
      for (int a=0; a<p_Vetor.size(); a++)
      {
         if (p_Vetor.elementAt(a).toString().equals(p_Chave))
            return p_Vetor.elementAt(a);
      }

      return null;
   }

   static private String [] fnFromCSV(String p_CSV)
   {
      if (p_CSV == null)
         return null;
      StringTokenizer l_Token = new StringTokenizer(p_CSV, ";", false);
      int l_QtdTokens = l_Token.countTokens();
      if (l_QtdTokens <= 0)
         return null;
      String l_Ret [] = new String[l_QtdTokens];
      for (int a=0; a<l_QtdTokens; a++)
         l_Ret[a] = l_Token.nextToken();
      return l_Ret;
   }

   static private double fnMaior(double p_Valores[], int p_Selecionados [])
   {
      double l_Maior = 0;
      if (p_Selecionados != null)
      {
         for (int a=0; a<p_Selecionados.length; a++)
         {
            if (p_Valores[p_Selecionados[a]] > l_Maior)
               l_Maior = p_Valores[p_Selecionados[a]];
         }
      } else
      {
         for (int a=0; a<p_Valores.length; a++)
         {
            if (p_Valores[a] > l_Maior)
               l_Maior = p_Valores[a];
         }
      }
      return l_Maior;
   }

   private static int fnCompara(Linha p_1, Linha p_2, int p_Coluna, boolean p_Ordem)
   {
      int l_Ret = fnCompara(p_1, p_2, p_Coluna);
      if (! p_Ordem)
         return l_Ret;
      else
         return l_Ret * -1;
   }
   
   private static int fnCompara(Linha p_1, Linha p_2, int p_Coluna)
   {
      switch (p_Coluna)
      {
         case 0:
            return p_2.m_Central.compareTo(p_1.m_Central);
         case 1:
            return p_2.m_Rota.compareTo(p_1.m_Rota);
         case 2:
            return p_2.m_Embratel.compareTo(p_1.m_Embratel);
         case 3:
            return p_2.m_Rede.compareTo(p_1.m_Rede);
         case 4:
            return p_2.m_Gerencia.compareTo(p_1.m_Gerencia);
         case 5:
            return p_2.m_Holding.compareTo(p_1.m_Holding);
         case 6:
            return p_2.m_Prestadora.compareTo(p_1.m_Prestadora);
         case 7:
            return (int) (p_2.m_CircuitosAtivos - p_1.m_CircuitosAtivos);
         case 8:
            if (p_2.m_TrafegoLimite - p_1.m_TrafegoLimite < 0) return -1;
            if (p_2.m_TrafegoLimite - p_1.m_TrafegoLimite > 0) return 1;
            return 0;
         default:
            if (p_2.m_TrafegoDiario[p_Coluna-9] - p_1.m_TrafegoDiario[p_Coluna-9] < 0) return -1;
            if (p_2.m_TrafegoDiario[p_Coluna-9] - p_1.m_TrafegoDiario[p_Coluna-9] > 0) return 1;
            return 0;
      }
   }
/*   
   private static void fnSort(Linha from[], Linha to[], int low, int high, int p_Coluna, boolean p_Ordem) 
   {
      if (high - low < 2)
         return;

      int middle = (low + high)/2;
      fnSort(to, from, low, middle, p_Coluna, p_Ordem);
      fnSort(to, from, middle, high, p_Coluna, p_Ordem);

      int p = low;
      int q = middle;

      if (high - low >= 4 && fnCompara(from[middle-1], from[middle], p_Coluna, p_Ordem) <= 0) 
      {
         for (int i = low; i < high; i++)
            to[i] = from[i];
         return;
      }

      for (int i = low; i < high; i++) 
      {
         if (q >= high || (p < middle && fnCompara(from[middle-1], from[middle], p_Coluna, p_Ordem) <= 0))
            to[i] = from[p++];
         else 
            to[i] = from[q++];
      }
   }

   public void fnShuttleSort(int p_Origem[], int p_Destino[], int p_Inferior, int p_Superior)
*/
   private static void fnSort(Linha p_Origem[], Linha p_Destino[], int p_Inferior, int p_Superior, int p_Coluna, boolean p_Ordem) 
   {
      try
      {
         if (p_Superior - p_Inferior < 2)
            return;

         int p_Meio = (p_Inferior + p_Superior)/2;
         fnSort(p_Destino, p_Origem, p_Inferior, p_Meio, p_Coluna, p_Ordem);
         fnSort(p_Destino, p_Origem, p_Meio, p_Superior, p_Coluna, p_Ordem);

         int p = p_Inferior;
         int q = p_Meio;

         if (p_Superior - p_Inferior >= 4 && fnCompara(p_Origem[p_Meio-1], p_Origem[p_Meio], p_Coluna, p_Ordem) <= 0)
         {
            for (int i = p_Inferior; i < p_Superior; i++)
               p_Destino[i] = p_Origem[i];

            return;
         }

         for (int i = p_Inferior; i < p_Superior; i++)
         {
            if (q >= p_Superior || (p < p_Meio && fnCompara(p_Origem[p], p_Origem[q], p_Coluna, p_Ordem) <= 0))
               p_Destino[i] = p_Origem[p++];
            else
               p_Destino[i] = p_Origem[q++];
         }
      } catch (Exception e)
      {
      } catch (java.lang.OutOfMemoryError e)
      {
         System.out.println("Estourou memória");
         return;
      }
   }

   public void teste()
   {
      double x [] = new double [1];
      x [0] = 4.1;
      new Calculado(2.4, fnGrauServico("AJU  RIT IT  AJU  TR  B BP", "RIT"), 7, x, null);
   }
   
   /***************************************************************************************/
   public static void main (String p_Args[])
   {
//      HistoricoTrafego l_HistoricoTrafego = new HistoricoTrafego(null);
//      l_HistoricoTrafego.teste();
      /* */
      ArquivosDefs l_ArquivosDefs = new ArquivosDefs();
      CnxServUtil l_ConexUtil = null;
      try
      {
         l_ConexUtil = new CnxServUtil(NoUtil.getNo().getConexaoServUtil().getModoConexao());
      }
      catch (Exception Exc)
      {
         System.out.println("Erro ao conectar com ServUtil: "+Exc);
         System.out.println("Saindo... Inicie o ServUtil!");
         System.exit(0);
      }
      Cliente.fnCliente(DefsComum.s_CLIENTE);
      HistoricoTrafego l_HistoricoTrafego = new HistoricoTrafego(l_ConexUtil);
      l_HistoricoTrafego.fnLeRelatorio("osx", "12", "7", "87.txt", "Des 5", "24/07/2003 09:16:45");
//      l_HistoricoTrafego.fnLeRelatorio("osx", "12", "7", "1059048000-7.txt", "Des 5", "24/07/2003 18:18:00");
      
      System.out.println(l_HistoricoTrafego.fnHeader(true));
      l_HistoricoTrafego.fnLinhas(null, null, null, null, null);
      Vector l_Linhas = l_HistoricoTrafego.m_LinhasUltimaPesquisa;
      l_HistoricoTrafego.fnOrdena(7);

      for (int a=0; a<l_Linhas.size(); a++)
         System.out.println(l_Linhas.elementAt(a).toString());


      System.out.println("************ Centrais ************");
      for (int a=0; a<l_HistoricoTrafego.m_Centrais.size(); a++)
         System.out.println(l_HistoricoTrafego.m_Centrais.elementAt(a).toString());

      System.out.println("************ Gerencias ************");
      for (int a=0; a<l_HistoricoTrafego.m_Gerencias.size(); a++)
         System.out.println(l_HistoricoTrafego.m_Gerencias.elementAt(a).toString());

      System.out.println("************ Holdings ************");
      for (int a=0; a<l_HistoricoTrafego.m_Holdings.size(); a++)
         System.out.println(l_HistoricoTrafego.m_Holdings.elementAt(a).toString());

      System.out.println("************ Prestadoras ************");
      for (int a=0; a<l_HistoricoTrafego.m_Prestadoras.size(); a++)
         System.out.println(l_HistoricoTrafego.m_Prestadoras.elementAt(a).toString());
      /* */
   }
}


