package dados;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jgap.impl.IntegerGene;

public class Testar {

	private static ArrayList<GetDados> Dados = new ArrayList<GetDados>();
	private static int saida = 4;
	private static int acc = 6;
	private static int liminf = 6;
	private static int limsup = 20;
	private static int janela = 40;
	private static double dist = 59.5417;
	private static int ppis = 7;
	private static int regras = 3;
	private static double saldofinal;

	public static double getSaldofinal() {
		return saldofinal;
	}

	public static void setSaida(int saida) {
		Testar.saida = saida;
	}

	public static void setAcc(int acc) {
		Testar.acc = acc;
	}

	public static void setLiminf(int liminf) {
		Testar.liminf = liminf;
	}

	public static void setLimsup(int limsup) {
		Testar.limsup = limsup;
	}

	public static void setJanela(int janela) {
		Testar.janela = janela;
	}

	public static void setDist(double dist) {
		Testar.dist = dist;
	}

	public static void setPpis(int ppis) {
		Testar.ppis = ppis;
	}

	public static void setRegras(int regras) {
		Testar.regras = regras;
	}

	static int preço=1;
	static int padrao=1;
	static int dias=1;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	public static void main(String[] args, ArrayList<String> pattern,int dias,double tk, double sl, int diaspadrao) throws IOException, ParseException, RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		try {
			//FileInputStream fstream = new FileInputStream("txt/All.txt");
			FileInputStream fstream = new FileInputStream("txt/" + args[0]);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				//System.out.println(strLine);
				String[] parts = args[1].split("/");
				String anoinicio = parts[0]; 
				String mesinicio = parts[1]; 
				String diainicio = parts[2];
				String[] parts1 = args[2].split("/");
				String anofim = parts1[0]; 
				String mesfim = parts1[1]; 
				String diafim = parts1[2];
				GregorianCalendar start = new GregorianCalendar(Integer.parseInt(anoinicio),Integer.parseInt(mesinicio),Integer.parseInt(diainicio));
				GregorianCalendar end = new GregorianCalendar(Integer.parseInt(anofim),Integer.parseInt(mesfim),Integer.parseInt(diafim));
//				GregorianCalendar start = new GregorianCalendar(2012,0,1);
//				GregorianCalendar end = new GregorianCalendar(2015,0,1);
				GetDados novosdados = new GetDados(strLine,start,end);
				novosdados.getData(strLine);
				Dados.add(novosdados);
			}	
			br.close();
			
			double saldo = 100000;
			int sizecromossoma = pattern.size() + 8;
			int ndias = dias;
			double percentagemtk = tk;
			double percentagemsl = sl;
			int janelapadrao = diaspadrao;
			double rentabilidade = 0;
			int noperacoes = 0;
			int oppositivas = 0 , opnegativas = 0;
			int mediadias = 0;
			ArrayList<String> padrao = new ArrayList<String>();
			for (int i=0;i<pattern.size();i++){
				padrao.add(i,pattern.get(i));
			}
	System.out.println(saida);
			LinkedHashMap<String, String> rent = new LinkedHashMap<String,String>();
			HashMap<GetDados, HashMap<Integer, String>> vendas = new HashMap<GetDados,HashMap<Integer,String>>();
			HashMap<String, String> portfolio = new HashMap<String,String>();
			Map<String, List<Double>> venda = new HashMap<String,List<Double>>();
			HashMap<GetDados,Double> rank = new HashMap<GetDados,Double>();
			String dataactual=null;
			HashMap<String, Integer> ticker = new HashMap<String,Integer>();
			HashMap<String, Integer> ticker1 = new HashMap<String,Integer>();
			System.out.println(saldo);
			System.out.println(portfolio);
			System.out.println(venda);
			for(int i=0;i<Dados.get(0).getDates().size()/janela;i++){	
				HashMap<String, Double> compra = new HashMap<String,Double>();
				HashMap<String, String> datacompra = new HashMap<String,String>();
				rent = getRentdiaria(rent,Dados.get(0).getDates().subList(i*janela,(i+1)*janela),vendas,saldo);
				for(GetDados active : Dados){
					AlgoritmoTrading a = new AlgoritmoTrading(active.getSimbolo());
					ArrayList<String> serie = new ArrayList<String>();
					ArrayList<Double> b = new ArrayList<Double>(active.getAdjclosesReverse().subList(i*janela,(i+1)*janela));
					ArrayList<Point2D.Double> test = a.getPIPs(b,ppis);
					serie = a.createRules(test,regras,liminf,limsup);
					//comprar a accao
					if(a.distanceSAX(serie,padrao) <= dist){
						// caso a janela deslizante acabe no ultimo dia do periodo, entao não é possivel comprar no dia a seguir pois nao existe(mudar para o teste)
						if(b.get(b.size()-1) == active.getAdjclosesReverse().get(active.getAdjclosesReverse().size()-1))
							break;
						else dataactual = active.getDates().get((i+1)*janela);
						saldo = updateSaldo(venda,dataactual,saldo);
						portfolio = updateportfolio(portfolio,dataactual);
						if(portfolio.size() == acc){
							break;
						}else if((portfolio.size() < acc) && (!portfolio.containsKey(active.getSimbolo())) && (active.getAdjclosesReverse().get((i+1)*janela) <= saldo)){ 
							rank.put(active, a.distanceSAX(serie,padrao));
						}
					}
				}
				rank = sortHashMapByValuesD(rank);
				int naccoesparacompra = acc - portfolio.size();
				int compradas = 0;
				double saldoporaccao = saldo / (double) naccoesparacompra;
				for(Iterator<Map.Entry<GetDados,Double>>it= rank.entrySet().iterator();it.hasNext();){
				    Map.Entry<GetDados, Double> entry = it.next();
				    if(compradas == naccoesparacompra){
				    	break;
				    }else if(entry.getKey().getAdjclosesReverse().get((i+1)*janela) <= saldoporaccao){
				    	compradas++;
				    	ArrayList<String> resultado;
				    	AlgoritmoTrading a = new AlgoritmoTrading(entry.getKey().getSimbolo());
				    	noperacoes++;
					    switch (saida) {
					    //dias
						case 0: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							HashMap<Integer,String> aux = new HashMap<Integer,String>();
							System.out.println(qtaccoescompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = DiasParaSair(entry.getKey(),a,(i+1)*janela,ndias);
							rentabilidade = Double.parseDouble(resultado.get(1));
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							aux.put(qtaccoescompra, resultado.get(0));
							vendas.put(entry.getKey(),aux);
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(),resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
							}
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;

							}
						break;
						//take profit e stop loss
						case 1: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							System.out.println(qtaccoescompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = TakeProfitStopLoss(entry.getKey(),a,(i+1)*janela,percentagemtk,percentagemsl);
							rentabilidade = Double.parseDouble(resultado.get(1));
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							HashMap<Integer,String> aux = new HashMap<Integer,String>();
							aux.put(qtaccoescompra, resultado.get(0));
							vendas.put(entry.getKey(),aux);
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(),resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
								}
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;
							}
							break;
						//ver padrao a seguir
						case 2: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							System.out.println(qtaccoescompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = PatternExit(entry.getKey(),a,(i+1)*janela,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
							rentabilidade = Double.parseDouble(resultado.get(1));
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							HashMap<Integer,String> aux = new HashMap<Integer,String>();
							aux.put(qtaccoescompra, resultado.get(0));
							vendas.put(entry.getKey(),aux);
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(),resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
							}
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;
						}
						break;
						// 0 e 1
						case 3: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							System.out.println(qtaccoescompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = DiascomTkSl(entry.getKey(),a,(i+1)*janela,ndias,janela,i,percentagemtk,percentagemsl);
							rentabilidade = Double.parseDouble(resultado.get(1));		
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							HashMap<Integer,String> aux = new HashMap<Integer,String>();
							aux.put(qtaccoescompra, resultado.get(0));
							vendas.put(entry.getKey(),aux);
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(),resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
							}
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;
						}
						break;
						// 0 e 2
						case 4: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							System.out.println(qtaccoescompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = DiascomPattern(entry.getKey(),a,(i+1)*janela,ndias,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
							rentabilidade = Double.parseDouble(resultado.get(1));
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							HashMap<Integer,String> aux = new HashMap<Integer,String>();
							aux.put(qtaccoescompra, resultado.get(0));
							vendas.put(entry.getKey(),aux);
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(),resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
							}
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;
						}
						break;
						// 1 e 2
						case 5: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							System.out.println(qtaccoescompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = SlTkcomPattern(entry.getKey(),a,(i+1)*janela,percentagemtk,percentagemsl,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
							rentabilidade = Double.parseDouble(resultado.get(1));
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							HashMap<Integer,String> aux = new HashMap<Integer,String>();
							aux.put(qtaccoescompra, resultado.get(0));
							vendas.put(entry.getKey(),aux);
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(),resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
							}
							System.out.println("Distância para o padrão:" + rank.get(entry.getKey()));
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;
						}
						break;
						//todos
						case 6: {
							double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*janela);
							int qtaccoescompra = (int) (saldoporaccao / precocompra);
							datacompra.put(entry.getKey().getSimbolo(), dataactual);
							compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
							resultado = TodosdeSaida(entry.getKey(),a,(i+1)*janela,ndias,percentagemtk,percentagemsl,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
							rentabilidade = Double.parseDouble(resultado.get(1));
							// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
							if(resultado.get(0) == null){
								resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
								double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
								rentabilidade = (precovendaespecial / precocompra) - 1.0;
							}
							int diasmercado = entry.getKey().getDates().indexOf(resultado.get(0)) - entry.getKey().getDates().indexOf(dataactual);
							mediadias += diasmercado;
							portfolio.put(entry.getKey().getSimbolo(), resultado.get(0));
							saldo -= precocompra*qtaccoescompra;
							double precovenda = precocompra + (precocompra * rentabilidade);
							if(venda.containsKey(resultado.get(0)))
								venda.get(resultado.get(0)).add(precovenda*qtaccoescompra);
							else {
								List<Double> preço = new ArrayList<Double>();
								preço.add(precovenda*qtaccoescompra);
								venda.put(resultado.get(0),preço);
								}
							System.out.println(datacompra);
							System.out.println(compra);
							System.out.println(portfolio);
							System.out.println(venda);
							System.out.println(rentabilidade*100);
							if(rentabilidade*100 > 0){
								oppositivas++;
							}else opnegativas++;
							}
							break;
							}
				    	}
					}
				vendas = updateVendas(vendas,dataactual);
				rank.clear();
				}
			
			vendas = updateVendas(vendas,dataactual);
			//rent = getRentdiaria(rent,Dados.get(0).getDates().subList(Dados.get(0).getDates().indexOf("8/10/2013"),Dados.get(0).getDates().size()),vendas,saldo);
			//getExcel(rent);
			saldo = updateSaldo(venda,"01/01/2016",saldo);
			System.out.println("Media de dias dentro do mercado:" + mediadias/noperacoes);
			System.out.println("Nº operações:" + noperacoes);
			System.out.println("Nº operações positivas:" + oppositivas);
			System.out.println("Nº operações negativas:" + opnegativas);
			//System.out.println(saldo);
			System.out.println(((saldo / 100000) - 1.0) * 100);
			saldofinal = ((saldo / 100000) - 1.0) * 100;
					
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
	
	public static HashMap<GetDados,HashMap<Integer,String>> getRentlastyear(HashMap<String,Integer> ticker,HashMap<GetDados,HashMap<Integer,String>> vendas,int dia, int mes,int ano){
		for(Iterator<Entry<String, Integer>>it= ticker.entrySet().iterator();it.hasNext();){
			Entry<String, Integer> entry = it.next();
			GregorianCalendar start = new GregorianCalendar(ano,mes,dia);
			GregorianCalendar end = new GregorianCalendar(2016,0,1);
			GetDados novosdados = new GetDados(entry.getKey(),start,end);
			novosdados.getData(entry.getKey());
			HashMap<Integer,String> aux = new HashMap<Integer,String>();
			if(entry.getKey().equals("AN")){
				aux.put(entry.getValue(),"11/1/2013");
				vendas.put(novosdados,aux);
			}else
				aux.put(entry.getValue(),"8/1/2013");
				vendas.put(novosdados,aux);
			//else if(entry.getKey().equals("ADI")){
//				aux.put(entry.getValue(),"1/2/2013");
//				vendas.put(novosdados,aux);
//			}else if(entry.getKey().equals("IRM")){
//				aux.put(entry.getValue(),"22/1/2013");
//				vendas.put(novosdados,aux);
//			}else if(entry.getKey().equals("MCO")){
//				aux.put(entry.getValue(),"4/1/2013");
//				vendas.put(novosdados,aux);
//			}
		}
		
		return vendas;
		
	}
	
	public static void getExcel(LinkedHashMap<String,String> rent){
		try {
			WritableWorkbook wwb = Workbook.createWorkbook(new File("2013-2.xls"));
			wwb.createSheet( "Api", 0 );
			WritableSheet ws = wwb.getSheet( 0 );
			Label l;
			int linhaExcel = 1;
			for(Iterator<Entry<String, String>>it= rent.entrySet().iterator();it.hasNext();){
				Entry<String, String> entry = it.next();
				String a = entry.getValue().replace(".", ",");
				l = new Label( 0, linhaExcel, entry.getKey());
		        ws.addCell( l );
		         
		        l = new Label( 1, linhaExcel, a);
		        ws.addCell( l );
		        
		        linhaExcel++;
			}
		    wwb.write();
			wwb.close();
		
		}catch(Exception e){
			System.err.println(e);
		}
	}
	
	public static LinkedHashMap<String, String> getRentdiaria(LinkedHashMap<String,String> rent,List<String> dates,HashMap<GetDados,HashMap<Integer,String>> vendas,double saldo) throws ParseException{
		double saldo1 = 0;
		for(String d: dates){
			if(vendas.isEmpty()){
				String saldototal = Double.toString(saldo);
				rent.put(d,saldototal);
				}else {
					saldo1 = getVendas(vendas,d);
					String saldototal = Double.toString(saldo+saldo1);
					rent.put(d,saldototal);
					}
			}
		return rent;
	}
	
	public static double getVendas(HashMap<GetDados,HashMap<Integer,String>> vendas, String date){
		double venda=0;
		int index = 0;
		for(Iterator<Entry<GetDados, HashMap<Integer, String>>>it= vendas.entrySet().iterator();it.hasNext();){
			Entry<GetDados, HashMap<Integer, String>> entry = it.next();
			index = entry.getKey().getDates().indexOf(date);
			for(Iterator<Entry<Integer, String>>it1= entry.getValue().entrySet().iterator();it1.hasNext();){
				Entry<Integer, String> entry1 = it1.next();
					venda += entry.getKey().getAdjclosesReverse().get(index) * entry1.getKey();/* multiplicar pela qt de cada acção*/;
			}
		}
		return venda;
	}
	
public static double getActivestocks(double saldo,Map<String, List<Double>> venda, HashMap<String,String> portfolio, HashMap<String,Integer> accao, int dia, int mes, int ano) throws ParseException{
	int janela=21;
	int ppis = 7;
	int regras = 5;
	double dist = 32.9751;
	int liminf = 10;
	int limsup = 29;
	int ndias = 44;
	int janelapadrao = 53;
	double percentagemtk = 8.0498;
	double percentagemsl = -16.6075;
	int sizecromossoma = 28;
	double rentabilidade = 0;
	for(Iterator<Entry<String, Integer>>it= accao.entrySet().iterator();it.hasNext();){
		Entry<String, Integer> entry = it.next();
	GetDados d;
	GregorianCalendar start = new GregorianCalendar(ano,mes,dia);
	GregorianCalendar end = new GregorianCalendar(2016,0,1);
	GetDados novosdados = new GetDados(entry.getKey(),start,end);
	novosdados.getData(entry.getKey());
	d = novosdados;
	ArrayList<String> resultado;
	AlgoritmoTrading a = new AlgoritmoTrading();
		//resultado = DiasParaSair(d,a,0,ndias);
		//resultado = TakeProfitStopLoss(d,a,0,percentagemtk,percentagemsl);
		resultado = PatternExit(d,a,0,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
		//resultado = DiascomTkSl(d,a,0,ndias,janela,0,percentagemtk,percentagemsl);
		//resultado = DiascomPattern(d,a,0,ndias,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
		//resultado = SlTkcomPattern(d,a,0,percentagemtk,percentagemsl,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
		//resultado = TodosdeSaida(d,a,0,ndias,percentagemtk,percentagemsl,janelapadrao,dist,ppis,regras,sizecromossoma-8,liminf,limsup);
		rentabilidade = Double.parseDouble(resultado.get(1));
		double precocompra = d.getAdjclosesReverse().get(0);
		// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
		if(resultado.get(0) == null){
			resultado.add(0,d.getDates().get(d.getDates().size()-1));
			double precovendaespecial = d.getAdjclosesReverse().get(d.getAdjclosesReverse().size()-1);
			rentabilidade = (precovendaespecial / precocompra) - 1.0;
			}
		portfolio.put(d.getSimbolo(),resultado.get(0));
		saldo -= precocompra*entry.getValue();
		double precovenda = precocompra + (precocompra * rentabilidade);
		if(venda.containsKey(resultado.get(0)))
			venda.get(resultado.get(0)).add(precovenda*entry.getValue());
		else {
			List<Double> preço = new ArrayList<Double>();
			preço.add(precovenda*entry.getValue());
			venda.put(resultado.get(0),preço);
		}
	}
		return saldo;
}

public static LinkedHashMap<GetDados, Double> sortHashMapByValuesD(HashMap<GetDados, Double> passedMap) {
	   List<GetDados> mapKeys = new ArrayList<GetDados>(passedMap.keySet());
	   List<Double> mapValues = new ArrayList<Double>(passedMap.values());
	   Collections.sort(mapValues);

	   LinkedHashMap<GetDados, Double> sortedMap = new LinkedHashMap<GetDados, Double>();

	   Iterator<Double> valueIt = mapValues.iterator();
	   while (valueIt.hasNext()) {
	       Object val = valueIt.next();
	       Iterator<GetDados> keyIt = mapKeys.iterator();

	       while (keyIt.hasNext()) {
	           Object key = keyIt.next();
	           String comp1 = passedMap.get(key).toString();
	           String comp2 = val.toString();

	           if (comp1.equals(comp2)){
	               passedMap.remove(key);
	               mapKeys.remove(key);
	               sortedMap.put((GetDados)key, (Double)val);
	               break;
	           }

	       }

	   }
	   return sortedMap;
	}

public static HashMap<GetDados, HashMap<Integer, String>> updateVendas(HashMap<GetDados,HashMap<Integer,String>> v,String data) throws ParseException{
	for(Iterator<Entry<GetDados, HashMap<Integer, String>>>it=v.entrySet().iterator();it.hasNext();){
	     Entry<GetDados, HashMap<Integer, String>> entry = it.next();
		if(entry.getValue() == null)
			it.remove();
		else{ 
			for(Iterator<Entry<Integer, String>>it1= entry.getValue().entrySet().iterator();it1.hasNext();){
				Entry<Integer, String> entry1 = it1.next();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date start = sdf.parse(data);
				Date end = sdf.parse(entry1.getValue());
				// se a data onde me encontro for depois da que vendi a accao entao posso voltar a comprar
				if(start.after(end)){
					it.remove();
					}
				}
			}
		}
	return v;
}

public static HashMap<String, String> updateportfolio(HashMap<String, String> p,String data) throws ParseException{
	for(Iterator<Map.Entry<String,String>>it=p.entrySet().iterator();it.hasNext();){
	     Map.Entry<String, String> entry = it.next();
		if(entry.getValue() == null)
			it.remove();
		else{ 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date start = sdf.parse(data);
		Date end = sdf.parse(entry.getValue());
		// se a data onde me encontro for depois da que vendi a accao entao posso voltar a comprar
		if(start.after(end)){
			it.remove();
			}
		}
	}
	return p;
}

public static double updateSaldo(Map<String, List<Double>> venda,String data,double saldo) throws ParseException{
	for(Iterator<java.util.Map.Entry<String, List<Double>>>it=venda.entrySet().iterator();it.hasNext();){
	     java.util.Map.Entry<String, List<Double>> entry = it.next();
		if(entry.getKey() == null){
			for(int i=0;i< entry.getValue().size();i++){
				saldo += entry.getValue().get(i);
			}
			it.remove();
		}
		else{ 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date start = sdf.parse(data);
		Date end = sdf.parse(entry.getKey());
		// se a data onde me encontro for depois da que vendi a accao entao posso voltar a comprar
		if(start.after(end)){
			for(int i=0;i< entry.getValue().size();i++){
				saldo += entry.getValue().get(i);
			}
			it.remove();
			}
		}
	}
	return saldo;
}
public static ArrayList<String> DiasParaSair(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias){
	ArrayList<String> resultado = new ArrayList<String>();
	if(diacompra + ndias  < accao.getAdjclosesReverse().size()){
		resultado = a.janelaVendadiff(accao, diacompra,ndias); 
	} else{
		resultado.add(null);
		resultado.add(String.valueOf(0));
	}
return resultado;
}

public static ArrayList<String> getEarlyDate(ArrayList<String> a,ArrayList<String> b) throws ParseException{
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	double diff = 0; 
	String diavenda = null;
	ArrayList<String> result = new ArrayList<String>();
	Date start = sdf.parse(a.get(0));
	Date end = sdf.parse(b.get(0));
	if(start.before(end)){
		diff = Double.parseDouble(a.get(1));
		diavenda = a.get(0);
	}else {
		diff = Double.parseDouble(b.get(1));
		diavenda = b.get(0);
	}
	
	result.add(diavenda);
	String saldo1 = String.valueOf(diff);
	result.add(saldo1);
	return result;
}


public static ArrayList<String> TakeProfitStopLoss(GetDados accao,AlgoritmoTrading a,int compra,double percentagemtk,double percentagemsl) throws ParseException{
	double saldo = 0;
	String diavenda = null;
	ArrayList<String> tk,sl;
	ArrayList<String> result = new ArrayList<String>();
	tk = a.takeProfitpercentagem(accao,compra, percentagemtk);
	sl = a.stopLosspercentagem(accao,compra,percentagemsl);
	if(tk.get(0) != null && sl.get(0) != null){
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date start = sdf.parse(tk.get(0));
	Date end = sdf.parse(sl.get(0));
		if(start.before(end)){
			saldo = Double.parseDouble(tk.get(1));
			diavenda = tk.get(0);
		}else {
			saldo = Double.parseDouble(sl.get(1));
			diavenda = sl.get(0);
		}
	}else if(tk.get(0) == null && sl.get(0) != null){
			saldo = Double.parseDouble(sl.get(1));
			diavenda = sl.get(0);
	}else if(tk.get(0) != null && sl.get(0) == null){
		saldo = Double.parseDouble(tk.get(1));
		diavenda = tk.get(0);
	}
	result.add(diavenda);
	String saldo1 = String.valueOf(saldo);
	result.add(saldo1);
	return result;
}

public static ArrayList<String> PatternExit(GetDados accao,AlgoritmoTrading a,int diadecompra,int janela,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup){
	ArrayList<String> result;
	result = a.getPatternExit(accao,diadecompra,janela,dist,ppis,regras,tpadrao,limiteinf,limitesup);
	return result;
}

public static ArrayList<String> DiascomTkSl(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias,int janela,int i,double percentagemtk,double percentagemsl) throws ParseException{
	ArrayList<String> resultdias;
	ArrayList<String> resulttksl;
	double saldo = 0;
	ArrayList<String> result = new ArrayList<String>();
	String diavenda = null;
	resultdias = Testar.DiasParaSair(accao,a,diacompra,ndias);
	resulttksl = TakeProfitStopLoss(accao,a,diacompra,percentagemtk,percentagemsl);
	if((resulttksl.get(0)==null) && (resultdias.get(0)==null)){
		System.out.println("Saída último dia");
	}else if(resulttksl.get(0)==null){
		saldo = Double.parseDouble(resultdias.get(1));
		diavenda = resultdias.get(0);
		System.out.println("Dias" + dias++);
	} else if(resultdias.get(0)==null){
			saldo = Double.parseDouble(resulttksl.get(1));
			diavenda = resulttksl.get(0);
			System.out.println("Preço" + preço++);
	}else{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date start = sdf.parse(resultdias.get(0));
		Date end = sdf.parse(resulttksl.get(0));
		if(start.before(end)){
			saldo = Double.parseDouble(resultdias.get(1));
			diavenda = resultdias.get(0);
			System.out.println("Dias" + dias++);
		}else {
			saldo = Double.parseDouble(resulttksl.get(1));
			diavenda = resulttksl.get(0);
			System.out.println("Preço" + preço++);
		}
	}
	result.add(diavenda);
	String saldo1 = String.valueOf(saldo);
	result.add(saldo1);
	return result;
}

public static ArrayList<String> DiascomPattern(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias,int janela,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup) throws ParseException{
	ArrayList<String> resultdias;
	ArrayList<String> resultpattern;
	double saldo = 0;
	ArrayList<String> result = new ArrayList<String>();
	String diavenda = null;
	resultdias = Testar.DiasParaSair(accao,a,diacompra,ndias);
	//System.out.println(resultdias.get(0));
	resultpattern = PatternExit(accao,a,diacompra,janela,dist,ppis,regras,tpadrao,limiteinf,limitesup);
	if((resultdias.get(0)==null) && (resultpattern.get(0)==null)){
		System.out.println("Saída último dia");
	}else if(resultpattern.get(0)==null){
		saldo = Double.parseDouble(resultdias.get(1));
		diavenda = resultdias.get(0);
		System.out.println("Dias" + dias++);
	} else if(resultdias.get(0)==null){
		saldo = Double.parseDouble(resultpattern.get(1));
		diavenda = resultpattern.get(0);
		System.out.println("Pattern" + padrao++);
	}else {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date start = sdf.parse(resultdias.get(0));
		Date end = sdf.parse(resultpattern.get(0));
		if(start.before(end)){
			saldo = Double.parseDouble(resultdias.get(1));
			diavenda = resultdias.get(0);
			System.out.println("Dias" + dias++);
		}else {
			saldo = Double.parseDouble(resultpattern.get(1));
			diavenda = resultpattern.get(0);
			System.out.println("Pattern" + padrao++);
		}
	}
	result.add(diavenda);
	String saldo1 = String.valueOf(saldo);
	result.add(saldo1);
	return result;
}

public static ArrayList<String> SlTkcomPattern(GetDados accao, AlgoritmoTrading a,int diacompra,double percentagemtk,double percentagemsl,int janelapattern,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup) throws ParseException{
	ArrayList<String> resultsltk;
	ArrayList<String> resultpattern;
	double saldo = 0;
	ArrayList<String> result = new ArrayList<String>();
	String diavenda = null;
	resultsltk = TakeProfitStopLoss(accao,a,diacompra,percentagemtk,percentagemsl);
	resultpattern = PatternExit(accao,a,diacompra,janelapattern,dist,ppis,regras,tpadrao,limiteinf,limitesup);
	if(resultpattern.get(0)==null && resultsltk.get(0)==null){
		System.out.println("Saída ultimo dia");
	}else if(resultpattern.get(0)==null){
		saldo = Double.parseDouble(resultsltk.get(1));
		diavenda = resultsltk.get(0);
		System.out.println("Preço" + preço++);
	} else if(resultsltk.get(0)==null){
		saldo = Double.parseDouble(resultpattern.get(1));
		diavenda = resultpattern.get(0);
		System.out.println("Padrão" + padrao++);
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date start = sdf.parse(resultsltk.get(0));
			Date end = sdf.parse(resultpattern.get(0));
			if(start.before(end)){
				saldo = Double.parseDouble(resultsltk.get(1));
				diavenda = resultsltk.get(0);
				System.out.println("Preço" + preço++);
			}else {
				saldo = Double.parseDouble(resultpattern.get(1));
				diavenda = resultpattern.get(0);
				System.out.println("Padrão" + padrao++);
			}
		}
	result.add(diavenda);
	String saldo1 = String.valueOf(saldo);
	result.add(saldo1);
	return result;
}

public static ArrayList<String> TodosdeSaida(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias,double percentagemtk,double percentagemsl,int janelapattern,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup) throws ParseException{
	ArrayList<String> resultsltk;
	ArrayList<String> resultpattern;
	ArrayList<String> resultdias;
	double saldo = 0;
	ArrayList<String> result = new ArrayList<String>();
	String diavenda = null;
	resultdias = Testar.DiasParaSair(accao,a, diacompra, ndias);
	resultsltk = TakeProfitStopLoss(accao,a,diacompra,percentagemtk,percentagemsl);
	resultpattern = PatternExit(accao,a,diacompra,janelapattern,dist,ppis,regras,tpadrao,limiteinf,limitesup);
	if((resultdias.get(0) == null) && (resultsltk.get(0) != null) && (resultpattern.get(0) != null)){
			saldo = Double.parseDouble(getEarlyDate(resultsltk, resultpattern).get(1));
			diavenda = getEarlyDate(resultsltk, resultpattern).get(0);
		}else if((resultdias.get(0) != null) && (resultsltk.get(0) == null) && (resultpattern.get(0) != null)){
			saldo = Double.parseDouble(getEarlyDate(resultdias, resultpattern).get(1));
			diavenda = getEarlyDate(resultdias, resultpattern).get(0);
		}else if((resultdias.get(0) != null) && (resultsltk.get(0) != null) && (resultpattern.get(0) == null)){
			saldo = Double.parseDouble(getEarlyDate(resultdias, resultsltk).get(1));
			diavenda = getEarlyDate(resultdias, resultsltk).get(0);
		}else if((resultdias.get(0)==null) && (resultsltk.get(0)==null)){
			saldo = Double.parseDouble(resultpattern.get(1));
			diavenda = resultpattern.get(0);
		} else if((resultdias.get(0)==null) && (resultpattern.get(0)==null)){
			saldo = Double.parseDouble(resultsltk.get(1));
			diavenda = resultsltk.get(0);
		} else if((resultsltk.get(0)==null) && (resultpattern.get(0)==null)){
			saldo = Double.parseDouble(resultdias.get(1));
			diavenda = resultdias.get(0);
		}else if((resultdias.get(0) != null) && (resultsltk.get(0) != null) && (resultpattern.get(0) != null)){
			double diff = Double.parseDouble(getEarlyDate(resultdias, resultsltk).get(1));
			if(diff == Double.parseDouble(resultdias.get(1))){
				saldo = Double.parseDouble(getEarlyDate(resultdias, resultpattern).get(1));
				diavenda = getEarlyDate(resultdias, resultpattern).get(0);
			}else if(diff == Double.parseDouble(resultsltk.get(1))){
				saldo = Double.parseDouble(getEarlyDate(resultsltk, resultpattern).get(1));
				diavenda = getEarlyDate(resultsltk, resultpattern).get(0);
			}
		}
	result.add(diavenda);
	String saldo1 = String.valueOf(saldo);
	result.add(saldo1);
	return result;
}

}
