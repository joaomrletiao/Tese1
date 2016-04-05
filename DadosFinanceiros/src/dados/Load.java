package dados;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Load {
	
	private static ArrayList<GetDados> Dados = new ArrayList<GetDados>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Open the file
				FileInputStream fstream;
				try {
					fstream = new FileInputStream("sp500.txt");
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					String strLine;
					FileOutputStream fileOut = new FileOutputStream("./Dados.ser");
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					//Read File Line By Line
					while ((strLine = br.readLine()) != null)   {
						GregorianCalendar start = new GregorianCalendar(2000,0,1);
						GregorianCalendar end = new GregorianCalendar(2015,0,1);
						GetDados novosdados = new GetDados(strLine,start,end);
						novosdados.getData(strLine);
						Dados.add(novosdados);
					}	
					out.writeObject(Dados);
					br.close();
					out.close();
					fileOut.close();
					System.out.printf("Serialized data is saved in /Dados.ser");
				
				}catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(IOException c)
			      {
			         System.out.println("GetDados class not found");
			         c.printStackTrace();
			         return;
			      }
	}
}
