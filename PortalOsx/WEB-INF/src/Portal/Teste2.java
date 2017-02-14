/*
 * OSx Telecom
 * Criado em 18/01/2005
 * 
 */
package Portal;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import CDRView2.Arquivo;

/**
 * @author Marcos Urata
 *  
 */
public class Teste2 {

	/**
	 *  
	 */
	public Teste2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		String separador = System.getProperty("line.separator");
		Arquivo arquivo = new Arquivo();
		
		String s = "0;BOT;2193165787;312140043991;COP4AMI;1TRHCIO;OK1TCT;20040226090000;91;--------------------;Originada;1403744276;----------;A;VC1;Pós Pago;21;CLARORJ;EMBRATEL;01;00;000024;000000;1TRHCIO;0;003;002;0;4;00;-----;-;-;--;00;0312140043991;0;003;002;-----;2193165787;0312140043991;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;N/A;20010911100000;N/A;N/A;\n";
		StringBuffer sb = new StringBuffer(200);

		BufferedWriter arq = null;
		BufferedReader reader = null;
		final int TAMANHO_BUFFER = 2000;
		char[] c = new char[TAMANHO_BUFFER]; 
		

		try 
		{
			reader = new BufferedReader(new FileReader("/usr/entrada.txt"));
			arq = new BufferedWriter(new FileWriter("/usr/saida.txt"));
			
			long inicio = System.currentTimeMillis();
			int i = 0;
			int length;
			
			while ((length = reader.read(c, 0, TAMANHO_BUFFER)) > 1)
			{
				arq.write(c, 0, length);
			}
			arq.flush();
			reader.close();
			
			zipaArquivo("/usr/entrada.txt");
			System.out.println("Tempo Gasto: "+((System.currentTimeMillis() - inicio) / 1000)+"  segundos");
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public static void zipaArquivo(String p_Arquivo)
	{
		File fil;

		// Must use try because file io is occurring.
		try
		{
			fil = new File(p_Arquivo + ".zip");

			if (fil.exists() == true)
			{
				fil.delete();
			}

			// Create the file output streams for both the file and the zip.
			FileOutputStream fos = new FileOutputStream(p_Arquivo + ".zip");
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
	private static void zipFunc(String filePath,
								ZipOutputStream zos)
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
			ZipEntry fileEntry = new ZipEntry(fil.getName()); //filePath);
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
//				fil.delete();
			}
			catch (Exception e)
			{
				System.out.println("Arquivo::zipFunc(): Erro ao fechar os arquivos");
				e.printStackTrace();
			}
		}
	}

}