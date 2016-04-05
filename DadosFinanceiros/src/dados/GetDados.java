package dados;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Scanner;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class GetDados implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url,simbolo;
	private URL a,b; 
	private ArrayList<GregorianCalendar> dates = new ArrayList<GregorianCalendar>();
	private ArrayList<Double> opens = new ArrayList<Double>();
	private ArrayList<Double> highs = new ArrayList<Double>();
	private ArrayList<Double> lows = new ArrayList<Double>();
	private ArrayList<Double> closes = new ArrayList<Double>();
	private ArrayList<Double> adjcloses = new ArrayList<Double>();
	private ArrayList<Integer> volume = new ArrayList<Integer>();
	
	public GetDados(String s, GregorianCalendar start, GregorianCalendar end){
		//http://real-chart.finance.yahoo.com/table.csv?s=AAPL&a=01&b=18&c=2014&d=01&e=18&f=2015&g=d&ignore=.csv
		 url = "http://real-chart.finance.yahoo.com/table.csv?s="+s+
				"&a="+start.get(Calendar.MONTH)+
				"&b="+start.get(Calendar.DAY_OF_MONTH)+
				"&c="+start.get(Calendar.YEAR)+
				"&d="+end.get(Calendar.MONTH)+
				"&e="+end.get(Calendar.DAY_OF_MONTH)+
				"&f="+end.get(Calendar.YEAR)+
				"&g=d&ignore=.csv";
		 simbolo = s;
	}
	
	public void createExcelfile(){
		try {
			a = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(a.openStream());
			//string.replace(".",",");
			FileOutputStream fos = new FileOutputStream(simbolo + ".xls");
			//fos.write("sep=,\n Date,Open,High,Low,Close,Volume".getBytes());
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.write("sep=,\n".getBytes());
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getData(String Simbolo){
		try {
			b = new URL(url);
			URLConnection data = b.openConnection();
			@SuppressWarnings("resource")
			Scanner input = new Scanner(data.getInputStream());
			if(input.hasNext())
				input.nextLine();
			
			while(input.hasNextLine())
			{
				String line = input.nextLine();
				String[] parts = line.split(",");
				String[] gregoriancal = parts[0].split("-");
				GregorianCalendar date = new GregorianCalendar(Integer.parseInt(gregoriancal[0]),Integer.parseInt(gregoriancal[1])-1,Integer.parseInt(gregoriancal[2]));
				dates.add(date);
				opens.add(Double.parseDouble(parts[1]));
				highs.add(Double.parseDouble(parts[2]));
				lows.add(Double.parseDouble(parts[3]));
				closes.add(Double.parseDouble(parts[4]));
				volume.add(Integer.parseInt(parts[5]));
				adjcloses.add(Double.parseDouble(parts[6]));
			}
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}
	
	public void getData1(String Simbolo){
		try {
			WritableWorkbook wwb = Workbook.createWorkbook(new File(simbolo + ".xls"));
			wwb.createSheet( "Api", 0 );
			WritableSheet ws = wwb.getSheet( 0 );
			b = new URL(url);
			URLConnection data = b.openConnection();
			@SuppressWarnings("resource")
			Scanner input = new Scanner(data.getInputStream());
			if(input.hasNext())
				input.nextLine();
			Label l;
			int linhaExcel = 1;
			while(input.hasNextLine())
			{
				String line = input.nextLine();
				String[] parts = line.split(",");
				String[] gregoriancal = parts[0].split("-");
				GregorianCalendar date = new GregorianCalendar(Integer.parseInt(gregoriancal[0]),Integer.parseInt(gregoriancal[1])-1,Integer.parseInt(gregoriancal[2]));
				String date1 = parts[0];
				System.out.println(date1);
				String adjcloses = parts[6].replace(".", ",");
				l = new Label( 0, linhaExcel, date1);
		        ws.addCell( l );
		         
		        l = new Label( 1, linhaExcel, adjcloses);
		        ws.addCell( l );
		        
		        linhaExcel++;
			}
			wwb.write();
			wwb.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}
	
	public void getPreçoszero(ArrayList<Double> a){
		int i;
		for (i=0;i<a.size();i++){
			//falta ver o caso em que aparece 0 na ultima posiçao
			if(a.get(i).equals(0.0)){
				a.set(i,a.get(i+1));
			}
		}	
	}
	
	public void getvolumezero(ArrayList<Integer> a){
		int i;
		for (i=0;i<a.size();i++){
			//falta ver o caso em que aparece 0 na ultima posiçao
			if(a.get(i).equals(0)){
				a.set(i,a.get(i+1));
			}
		}	
	}
	
	public ArrayList<String> getDates() {
		ArrayList<String> datas = new ArrayList<String>();
		for(GregorianCalendar gc : dates){
			int month = gc.get(Calendar.MONTH) + 1;
			datas.add(gc.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + gc.get(Calendar.YEAR));
			
		}
		Collections.reverse(datas);
		return datas;
	}

	public ArrayList<Double> getOpens() {
		Collections.reverse(opens);
		return opens;
	}

	public ArrayList<Double> getHighs() {
		Collections.reverse(highs);
		return highs;
	}

	public ArrayList<Double> getLows() {
		Collections.reverse(lows);
		return lows;
	}

	public ArrayList<Double> getCloses() {
		return closes;
	}
	
	public ArrayList<Double> getClosesReverse() {
		ArrayList<Double> newcloses = new ArrayList<Double>(closes);
		//newcloses = closes; 
		Collections.reverse(newcloses);
		return newcloses;
	}

	public ArrayList<Double> getAdjclosesReverse(){
		ArrayList<Double> newadjcloses = new ArrayList<Double>(adjcloses);
		Collections.reverse(newadjcloses);
		return newadjcloses;
	}
	
	public ArrayList<Double> getAdjcloses() {
		Collections.reverse(adjcloses);
		return adjcloses;
	}

	public ArrayList<Integer> getVolume() {
		Collections.reverse(volume);
		return volume;
	}
	
	public ArrayList<Integer> getVolumeReverse(){
		ArrayList<Integer> newvolume = new ArrayList<Integer>(volume);
		Collections.reverse(newvolume);
		return newvolume;
	}

	public URL getURL(){
		return a;
	}
	public String getSimbolo(){
		return simbolo;
	}
}
