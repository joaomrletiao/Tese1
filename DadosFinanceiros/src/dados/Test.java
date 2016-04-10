package dados;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jgap.*;

public class Test {
	
	private static ArrayList<GetDados> Dados = new ArrayList<GetDados>();
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		
		try {/*
			FileInputStream fileIn = new FileInputStream("./Dados.ser");
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        ArrayList<GetDados> g1 = null;
			g1 = (ArrayList<GetDados>) in.readObject();
	        in.close();
	        fileIn.close();*/
//			GregorianCalendar start1 = new GregorianCalendar(2014,4,8);
//			GregorianCalendar end1 = new GregorianCalendar(2015,4,7);
//			GetDados novosdados1 = new GetDados("AAPL",start1,end1);
			//novosdados1.getData1("AAPL");
			//testar com pequenos exemplos
			FileInputStream fstream = new FileInputStream("txt/20.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				System.out.println(strLine);
				GregorianCalendar start = new GregorianCalendar(2014,2,11);
				GregorianCalendar end = new GregorianCalendar(2014,4,14);
				GetDados novosdados = new GetDados(strLine,start,end);
				novosdados.getData(strLine);
				Dados.add(novosdados);
//				ArrayList<String> primeiro = Dados.get(0).getDates();
//				primeiro.removeAll(novosdados.getDates());
//				System.out.println(primeiro);
			}	
			br.close();
//			//System.out.println(test);
//			number += 1;
//			padrao = a.createRules(test,2,8,20);
			
//			final File parent1 = new File("Exemplos");
//			parent1.mkdir();
//			ArrayList<Double> b = new ArrayList<Double>(Dados.get(0).getAdjclosesReverse().subList(0,Dados.get(0).getAdjclosesReverse().size()));
//			ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>();
//			for(int j=0;j<b.size();j++){
//				pontos.add(new Point2D.Double(j,b.get(j)));
//				}
//			GraphPanel b1 = new GraphPanel();
//			BufferedImage img = b1.createImage(b1.createGui(pontos));
//			final File f = new File(parent1 ,"SerieTemporal.jpg");
//			ImageIO.write(img, "JPEG", f);
			
			//testar pequenos exemplos
			for(GetDados g: Dados){
	        //for(GetDados g: g1){	
				int count=0;
				int janela = 20;
				final File parent1 = new File("Exemplos");
//				parent1.mkdir();
				AlgoritmoTrading a = new AlgoritmoTrading(g.getSimbolo());
				ArrayList<String> padrao = new ArrayList<String>();
				ArrayList<Double> c = new ArrayList<Double>(g.getAdjclosesReverse());
				ArrayList<Point2D.Double> test = a.getPIPs(c,8);
				System.out.println(test);
//				ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>();
//				for(int j=0;j<c.size();j++){
//					pontos.add(new Point2D.Double(j,c.get(j)));
//					}
				GraphPanel b1 = new GraphPanel();
				BufferedImage img = b1.createImage(b1.createGui(test));
				final File f = new File(parent1 ,"SerieTemporal.jpg");
				ImageIO.write(img, "JPEG", f);
//				number += 1;
//				padrao = a.createRules(test,3,9,23);
//				System.out.println(padrao);
//				final File parent = new File("Exemplos/7");
//				parent.mkdir();
//				final File someFile = new File(parent , g.getSimbolo() + ".txt");
//				someFile.createNewFile();
//				FileWriter fw = new FileWriter(someFile.getAbsoluteFile());
//				BufferedWriter bw = new BufferedWriter(fw);
				//bw.write("Tamanho da Janela:" + "" + janela);
				//bw.newLine();
				int number=0;
				System.out.println(g.getSimbolo());
				//descobrir padrões de x em x dias com 5 ppis e 2 regras
				for(int i=0;i<g.getDates().size()/janela;i++){
//					ArrayList<Double> b = new ArrayList<Double>(g.getAdjclosesReverse().subList(i*janela, (i+1)*janela));
					//ArrayList<String> padrao = new ArrayList<String>();
//					ArrayList<Point2D.Double> test = a.getPIPs(b,5);
//					//System.out.println(test);
//					number += 1;
//					padrao = a.createRules(test,2,8,20);
//					System.out.println(number);
//					System.out.println(padrao);
//					System.out.println(a.distanceSAX(padrao, doublebottoms));
//					GraphPanel b1 = new GraphPanel();
//					BufferedImage img = b1.createImage(b1.createGui(test));
//					final File f = new File(parent ,number + ".jpg");
//					ImageIO.write(img, "JPEG", f);
					//GraphPanel b1 = new GraphPanel();
					//System.out.println(b);
//					StandardDeviation d = new StandardDeviation(b);
//					System.out.println(d.getStandardDeviation());
//					bw.write(count + " Desvio Padrão " + d.getStandardDeviation());
//					bw.newLine();
//					ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>();
//					for(int j=0;j<b.size();j++){
//						pontos.add(new Point2D.Double(j,b.get(j)));
//						}
//					GraphPanel b1 = new GraphPanel();
//					BufferedImage img = b1.createImage(b1.createGui(pontos));
					//final File f = new File(parent ,"SerieTemporal"+ count + ".jpg");
					//ImageIO.write(img, "JPEG", f);
//					ArrayList<Point2D.Double> test = a.getPIPs(b,5);
//					padrao = a.createRules(test,2,5,20);
//					count++;
//					GraphPanel b1 = new GraphPanel();
//					//b1.createAndShowGui(test);
//					number += 1;
//					BufferedImage img = b1.createImage(b1.createGui(test));
					//final File f = new File(parent ,number + ".jpg");
					//ImageIO.write(img, "JPEG", f);
//					System.out.println(number);
//					System.out.println(a.distanceSAX(padrao,doublebottoms));
//					System.out.println(padrao);
					//encontrar padroes conhecidos ate 10
					//if(a.distanceSAX(padrao,doublebottoms) < 10){
						//OBV o = new OBV();
//						ArrayList<Integer> volume = new ArrayList<Integer>(g.getVolumeReverse().subList(i*janela,(i+1)*janela));
//						System.out.println(b);
//						System.out.println(volume);
//						System.out.println(o.getOBV(volume,b));
						//StandardDeviation d = new StandardDeviation(b);
						//System.out.println(d.getStandardDeviation());
						//System.out.println(a.takeProfit(g,janela,i,d.getStandardDeviation()));
						//System.out.println(a.stopLoss(g,janela,i,d.getStandardDeviation()));
						//System.out.println(a.takeProfitpercentagem(g,(i+1)*janela,janela,i,15.0));
						//System.out.println(a.takeProfitpercentagem(g,(i+1)*janela,janela,i,15.0).get(0));
						//System.out.println(a.stopLosspercentagem(g,janela,i,-5.0));
						//System.out.println(g.getAdjclosesReverse().size());
						//System.out.println(a.stopLosspercentagem(g,janela,i,-5.0));
						//System.out.println(a.janelaVenda(g,(i+1)*janela,40));
						//System.out.println(a.getPatternExit(g,(i+1)*janela,100));
						//bw.write(padrao.toString() + " " + g.getDates().get(i*janela) + " - " + g.getDates().get(((i+1)*janela)-1) + " " + a.distanceSAX(padrao,doublebottoms) + " " + "DoubleBottom");
						//bw.newLine();
						System.out.println(padrao);
						//System.out.println(a.jaccard(doublebottoms, padrao));
						//System.out.println(test);
//						//ArrayList<Double> serie = new ArrayList<Double>(g.getAdjclosesReverse().subList((i+1)*janela, (((i+2)*janela)+20)));
						/*
						BufferedImage img = b1.createImage(b1.createGui(pontos));
						number += 1;
						final File f = new File(parent ,janela + "SerieTemporalExtendedDB"+ number + ".jpg");
						ImageIO.write(img, "JPEG", f);*/
						//GraphPanel b2 = new GraphPanel();
						//b2.createAndShowGui(test);
						/*BufferedImage img = b2.createImage(b2.createGui(test));
						number += 1;
						final File f = new File(parent ,janela +"DoubleBottom" + number + ".jpg");
						ImageIO.write(img, "JPEG", f);*/
					//}else if(a.distanceSAX(padrao,bullflag) < 10){
						//bw.write(padrao.toString() + " " + g.getDates().get(i*janela) + " - " + g.getDates().get(((i+1)*janela)-1) + " " + a.distanceSAX(padrao,bullflag) + " " + "BullFlag");
						//bw.newLine();
						//System.out.println(padrao);
						//System.out.println(a.jaccard(padrao,padrao2));
						//System.out.println(test);
						/*ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>();
						for(int j=0;j<b.size();j++){
							pontos.add(new Point2D.Double(j,b.get(j)));
							}
						GraphPanel b1 = new GraphPanel();
						b1.createAndShowGui(pontos);*/
						//GraphPanel b2 = new GraphPanel();
						//b2.createAndShowGui(test);
						
						//criar iamgem jpg
						/*BufferedImage img = b2.createImage(b2.createGui(test));
						number += 1;
						final File f = new File(parent ,janela +"BullFlag" + number + ".jpg");
						ImageIO.write(img, "JPEG", f);*/
					//}else if(a.distanceSAX(padrao,cup) < 10){
						//bw.write(padrao.toString() + " " + g.getDates().get(i*janela) + " - " + g.getDates().get(((i+1)*janela)-1) + " " + a.distanceSAX(padrao,cup) + " " + "Cup");
						//bw.newLine();
						//System.out.println(padrao);
						//System.out.println(a.jaccard(padrao,padrao2));
						//System.out.println(test);
						/*ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>();
						for(int j=0;j<b.size();j++){
							pontos.add(new Point2D.Double(j,b.get(j)));
							}
						GraphPanel b1 = new GraphPanel();
						BufferedImage img = b1.createImage(b1.createGui(pontos));
						number += 1;
						final File f = new File(parent ,janela + "SerieTemporalCup"+ number + ".jpg");
						ImageIO.write(img, "JPEG", f);*/
						//GraphPanel b2 = new GraphPanel();
						//b2.createAndShowGui(test);
						//criar iamgem jpg
						/*number += 1;
						BufferedImage img = b2.createImage(b2.createGui(test));
						final File f = new File(parent ,janela +"Cup" + number + ".jpg");
						ImageIO.write(img, "JPEG", f);*/
					}
					//else if(a.distanceSAX(padrao,bearflag) < 10){
						//bw.write(padrao.toString() + " " + g.getDates().get(i*janela) + " - " + g.getDates().get(((i+1)*janela)-1) + " " + a.distanceSAX(padrao,bearflag) + " " + "BearFlag");
						//bw.newLine();
						//System.out.println(padrao);
						//System.out.println(a.jaccard(padrao,padrao2));
						//System.out.println(test);
						//GraphPanel b2 = new GraphPanel();
						//b2.createAndShowGui(test);
						/*BufferedImage img = b2.createImage(b2.createGui(test));
						number += 1;
						final File f = new File(parent ,janela +"BullFlag" + number + ".jpg");
						ImageIO.write(img, "JPEG", f);*/
					}
					
					/*//codigo para gerar gráfico com pontos todos
					ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>(); 
					for(int j=0;j<b.size();j++){
						pontos.add(new Point2D.Double(j,b.get(j)));
						}
					
					GraphPanel b1 = new GraphPanel();
					GraphPanel b2 = new GraphPanel();
					b2.createAndShowGui(pontos);
				    b1.createAndShowGui(test);*/
			//	bw.close();
			
				/*//if(g.getSimbolo().equals("A")){
					// ver os dias de cada acção que nao aparecem
					ArrayList<String> padrao = new ArrayList<String>();
					padrao = g.getDates();
					System.out.println(padrao.size());
					ArrayList<String> toReturn  = Dados.get(0).getDates();
					toReturn.removeAll(padrao);
					System.out.println(toReturn);
					//if(g.getDates().size() != 252)
					//System.out.println(g.getSimbolo());
					//System.out.println(g.getCloses());
				
					//testar as distancias
					System.out.println(a.jaccard(padrao,padrao1));
					System.out.println(a.LevenshteinDistance(padrao, padrao1));
					System.out.println(a.distanceSAX(padrao, padrao1));
		*/
					//}
				
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*catch(ClassNotFoundException c)
	      {
	         System.out.println("GetDados class not found");
	         c.printStackTrace();
	         return;
	      }*/
	}
	
}
