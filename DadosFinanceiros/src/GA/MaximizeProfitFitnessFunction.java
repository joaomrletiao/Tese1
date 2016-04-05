package GA;

import org.jgap.*;
import org.jgap.impl.IntegerGene;

import dados.*;

import java.awt.geom.Point2D;
import java.security.KeyStore.Entry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class MaximizeProfitFitnessFunction extends FitnessFunction{

	private final static String CVS_REVISION = "$Revision: 1.18 $";
	
	private final ArrayList<GetDados> accao;
	private static double fitness = 0;
	private static int dias;
	private static double ptk;
	private static double psl;
	private static int jpadrao;
	private static final int saida = 0;
	private static final int acc = 1;
	private static final int liminf = 2;
	private static final int limsup = 3;
	private static final int janela = 4;
	private static final int dist = 5;
	private static final int ppis = 6;
	private static final int regras = 7;

	public MaximizeProfitFitnessFunction(ArrayList<GetDados> g) {
		super();
		this.accao = g;
	}
	
	public double evaluate(IChromosome a_subject) {
		// Take care of the fitness evaluator. It could either be weighting higher
	    // fitness values higher (e.g.DefaultFitnessEvaluator). Or it could weight
	    // lower fitness values higher, because the fitness value is seen as a
	    // defect rate (e.g. DeltaFitnessEvaluator)
	    boolean defaultComparation = a_subject.getConfiguration().
	        getFitnessEvaluator().isFitter(2, 1);
	   // System.out.println(a_subject);
	    double fitness=0;
	    	try {
				fitness = ((getPattern(a_subject) / 100000) - 1.0);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    //double fitness = getPattern(a_subject);
	    
		 
	    
	    
		return Math.max(1.0d,fitness * 100);
	}

	public static int getIntParameter(IChromosome a_potentialSolution,int a_position){
		Integer parameter =
		        (Integer) a_potentialSolution.getGene(a_position).getAllele();
		    return parameter.intValue();
	}
	
	public static double getDoubleParameter(IChromosome a_potentialSolution,int a_position){
		Double parameter =
		        (Double) a_potentialSolution.getGene(a_position).getAllele();
		    return parameter.doubleValue();
	}
	
	public static String getStringParameter(IChromosome a_potentialSolution,int a_position){
		String parameter =
		        (String) a_potentialSolution.getGene(a_position).getAllele();
		    return parameter.toString();
	}
	
	public ArrayList<String> DiasParaSair(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias){
		ArrayList<String> resultado = new ArrayList<String>();
		if(diacompra + ndias  < accao.getAdjclosesReverse().size()){
			resultado = a.janelaVendadiff(accao, diacompra,ndias); 
		} else{
			resultado.add(null);
			resultado.add(String.valueOf(0));
		}
	return resultado;
	}
	
	public ArrayList<String> getEarlyDate(ArrayList<String> a,ArrayList<String> b) throws ParseException{
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
	
	
	public ArrayList<String> TakeProfitStopLoss(GetDados accao,AlgoritmoTrading a,int compra,double percentagemtk,double percentagemsl) throws ParseException{
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
	
	public ArrayList<String> PatternExit(GetDados accao,AlgoritmoTrading a,int diadecompra,int janela,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup){
		ArrayList<String> result;
		result = a.getPatternExit(accao,diadecompra,janela,dist,ppis,regras,tpadrao,limiteinf,limitesup);
		return result;
	}
	
	public ArrayList<String> DiascomTkSl(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias,int janela,int i,double percentagemtk,double percentagemsl) throws ParseException{
		ArrayList<String> resultdias;
		ArrayList<String> resulttksl;
		double saldo = 0;
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		resultdias = this.DiasParaSair(accao,a,diacompra,ndias);
		resulttksl = this.TakeProfitStopLoss(accao,a,diacompra,percentagemtk,percentagemsl);
		if(resulttksl.get(0)==null){
			saldo = Double.parseDouble(resultdias.get(1));
			diavenda = resultdias.get(0);
		} else if(resultdias.get(0)==null){
				saldo = Double.parseDouble(resulttksl.get(1));
				diavenda = resulttksl.get(0);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date start = sdf.parse(resultdias.get(0));
			Date end = sdf.parse(resulttksl.get(0));
			if(start.before(end)){
				saldo = Double.parseDouble(resultdias.get(1));
				diavenda = resultdias.get(0);
			}else {
				saldo = Double.parseDouble(resulttksl.get(1));
				diavenda = resulttksl.get(0);
			}
		}
		result.add(diavenda);
		String saldo1 = String.valueOf(saldo);
		result.add(saldo1);
		return result;
	}
	
	public ArrayList<String> DiascomPattern(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias,int janela,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup) throws ParseException{
		ArrayList<String> resultdias;
		ArrayList<String> resultpattern;
		double saldo = 0;
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		resultdias = this.DiasParaSair(accao,a,diacompra,ndias);
		resultpattern = this.PatternExit(accao,a,diacompra,janela,dist,ppis,regras,tpadrao,limiteinf,limitesup);
		if(resultpattern.get(0)==null){
			saldo = Double.parseDouble(resultdias.get(1));
			diavenda = resultdias.get(0);
		} else if(resultdias.get(0)==null){
			saldo = Double.parseDouble(resultpattern.get(1));
			diavenda = resultpattern.get(0);
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date start = sdf.parse(resultdias.get(0));
			Date end = sdf.parse(resultpattern.get(0));
			if(start.before(end)){
				saldo = Double.parseDouble(resultdias.get(1));
				diavenda = resultdias.get(0);
			}else {
				saldo = Double.parseDouble(resultpattern.get(1));
				diavenda = resultpattern.get(0);
			}
		}
		result.add(diavenda);
		String saldo1 = String.valueOf(saldo);
		result.add(saldo1);
		return result;
	}
	
	public ArrayList<String> SlTkcomPattern(GetDados accao, AlgoritmoTrading a,int diacompra,double percentagemtk,double percentagemsl,int janelapattern,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup) throws ParseException{
		ArrayList<String> resultsltk;
		ArrayList<String> resultpattern;
		double saldo = 0;
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		resultsltk = this.TakeProfitStopLoss(accao,a,diacompra,percentagemtk,percentagemsl);
		resultpattern = this.PatternExit(accao,a,diacompra,janelapattern,dist,ppis,regras,tpadrao,limiteinf,limitesup);
		if(resultpattern.get(0)==null){
			saldo = Double.parseDouble(resultsltk.get(1));
			diavenda = resultsltk.get(0);
		} else if(resultsltk.get(0)==null){
			saldo = Double.parseDouble(resultpattern.get(1));
			diavenda = resultpattern.get(0);
			}else {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date start = sdf.parse(resultsltk.get(0));
				Date end = sdf.parse(resultpattern.get(0));
				if(start.before(end)){
					saldo = Double.parseDouble(resultsltk.get(1));
					diavenda = resultsltk.get(0);
				}else {
					saldo = Double.parseDouble(resultpattern.get(1));
					diavenda = resultpattern.get(0);
				}
			}
		result.add(diavenda);
		String saldo1 = String.valueOf(saldo);
		result.add(saldo1);
		return result;
	}
	
	public ArrayList<String> TodosdeSaida(GetDados accao,AlgoritmoTrading a,int diacompra,int ndias,double percentagemtk,double percentagemsl,int janelapattern,double dist,int ppis,int regras,int tpadrao,int limiteinf,int limitesup) throws ParseException{
		ArrayList<String> resultsltk;
		ArrayList<String> resultpattern;
		ArrayList<String> resultdias;
		double saldo = 0;
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		resultdias = this.DiasParaSair(accao,a, diacompra, ndias);
		resultsltk = this.TakeProfitStopLoss(accao,a,diacompra,percentagemtk,percentagemsl);
		resultpattern = this.PatternExit(accao,a,diacompra,janelapattern,dist,ppis,regras,tpadrao,limiteinf,limitesup);
		if((resultdias.get(0) == null) && (resultsltk.get(0) != null) && (resultpattern.get(0) != null)){
				saldo = Double.parseDouble(this.getEarlyDate(resultsltk, resultpattern).get(1));
				diavenda = this.getEarlyDate(resultsltk, resultpattern).get(0);
			}else if((resultdias.get(0) != null) && (resultsltk.get(0) == null) && (resultpattern.get(0) != null)){
				saldo = Double.parseDouble(this.getEarlyDate(resultdias, resultpattern).get(1));
				diavenda = this.getEarlyDate(resultdias, resultpattern).get(0);
			}else if((resultdias.get(0) != null) && (resultsltk.get(0) != null) && (resultpattern.get(0) == null)){
				saldo = Double.parseDouble(this.getEarlyDate(resultdias, resultsltk).get(1));
				diavenda = this.getEarlyDate(resultdias, resultsltk).get(0);
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
				double diff = Double.parseDouble(this.getEarlyDate(resultdias, resultsltk).get(1));
				if(diff == Double.parseDouble(resultdias.get(1))){
					saldo = Double.parseDouble(this.getEarlyDate(resultdias, resultpattern).get(1));
					diavenda = this.getEarlyDate(resultdias, resultpattern).get(0);
				}else if(diff == Double.parseDouble(resultsltk.get(1))){
					saldo = Double.parseDouble(this.getEarlyDate(resultsltk, resultpattern).get(1));
					diavenda = this.getEarlyDate(resultsltk, resultpattern).get(0);
				}
			}
		result.add(diavenda);
		String saldo1 = String.valueOf(saldo);
		result.add(saldo1);
		return result;
	}
	
	public HashMap<String, String> updateportfolio(HashMap<String, String> p,String data) throws ParseException{
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
	
	public double updateSaldo(Map<String, List<Double>> venda,String data,double saldo) throws ParseException{
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
	
	public double getPattern(IChromosome a_subject) throws ParseException{
		System.out.println(a_subject);
		double saldo = 100000;
		int noperacoes = 0;
		int operacoessucesso, operacoesmas = 0;
		//int ndias = 68;
		int ndias = ((IntegerGene) a_subject.getGene(janela)).intValue() + (int)(Math.random() * (((((IntegerGene) a_subject.getGene(janela)).intValue()*2) - ((IntegerGene) a_subject.getGene(janela)).intValue()) + 1));
		//int janelapadrao = (((IntegerGene) a_subject.getGene(janela)).intValue()) + (int)(Math.random() * (((((IntegerGene) a_subject.getGene(janela)).intValue()*2) - (((IntegerGene) a_subject.getGene(janela)).intValue())) + 1));
		int janelapadrao = 63;
		Random r = new Random();
		//double percentagemtk = 48.29;
		//double percentagemsl = -9.74;
		double percentagemtk = 5 + (50 - 5) * r.nextDouble();
		double percentagemsl = -20 + (-5 - (-20)) * r.nextDouble();
		double rentabilidade=0;
		ArrayList<String> padrao = new ArrayList<String>();
		for(int j=regras+1; j< a_subject.size(); j++){
			padrao.add((String)a_subject.getGene(j).getAllele());
		}
		HashMap<String, String> portfolio = new HashMap<String,String>();
		Map<String, List<Double>> venda = new HashMap<String,List<Double>>();
		HashMap<GetDados,Double> rank = new HashMap<GetDados,Double>();
		String dataactual=null;
		for(int i=0;i<accao.get(0).getDates().size()/((IntegerGene) a_subject.getGene(janela)).intValue();i++){	
			HashMap<String, Double> compra = new HashMap<String,Double>();
			HashMap<String, String> datacompra = new HashMap<String,String>();
			for(GetDados active : accao){
				AlgoritmoTrading a = new AlgoritmoTrading(active.getSimbolo());
				ArrayList<String> serie = new ArrayList<String>();
				ArrayList<Double> b = new ArrayList<Double>(active.getAdjclosesReverse().subList(i*((IntegerGene) a_subject.getGene(janela)).intValue(), (i+1)*((IntegerGene) a_subject.getGene(janela)).intValue()));
				ArrayList<Point2D.Double> test = a.getPIPs(b,((IntegerGene) a_subject.getGene(ppis)).intValue());
				serie = a.createRules(test,((IntegerGene) a_subject.getGene(regras)).intValue(),((IntegerGene) a_subject.getGene(liminf)).intValue(),((IntegerGene) a_subject.getGene(limsup)).intValue());
				//comprar a accao
				if(a.distanceSAX(serie,padrao) <= (Double)a_subject.getGene(dist).getAllele()){
					// caso a janela deslizante acabe no ultimo dia do periodo, entao não é possivel comprar no dia a seguir pois nao existe(mudar para o teste)
					if(b.get(b.size()-1) == active.getAdjclosesReverse().get(active.getAdjclosesReverse().size()-1))
						break;
					else dataactual = active.getDates().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
					saldo = updateSaldo(venda,dataactual,saldo);
					portfolio = updateportfolio(portfolio,dataactual);
					if(portfolio.size() == (Integer)a_subject.getGene(acc).getAllele()){
						break;
					}else if((portfolio.size() < (Integer)a_subject.getGene(acc).getAllele()) && (!portfolio.containsKey(active.getSimbolo())) && (active.getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue()) <= saldo)){ 
						rank.put(active, a.distanceSAX(serie,padrao));
					}
				}
			}
			rank = sortHashMapByValuesD(rank);
			int naccoesparacompra = (Integer)a_subject.getGene(acc).getAllele() - portfolio.size();
			int compradas = 0;
			double saldoporaccao = saldo / (double) naccoesparacompra;
			for(Iterator<Map.Entry<GetDados,Double>>it= rank.entrySet().iterator();it.hasNext();){
			    Map.Entry<GetDados, Double> entry = it.next();
			    if(compradas == naccoesparacompra){
			    	break;
			    }else if(entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue()) <= saldoporaccao){
			    	compradas++;
			    	noperacoes++;
			    	ArrayList<String> resultado;
			    	AlgoritmoTrading a = new AlgoritmoTrading(entry.getKey().getSimbolo());
				    switch ((Integer)a_subject.getGene(saida).getAllele()) {
				    //dias
					case 0: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = DiasParaSair(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),ndias);
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
						//System.out.println(datacompra);
						//System.out.println(compra);
						//System.out.println(portfolio);
						//System.out.println(venda);
						//System.out.println(saldo);
						}
					break;
					//take profit e stop loss
					case 1: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = TakeProfitStopLoss(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),percentagemtk,percentagemsl);
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
//						System.out.println(datacompra);
//						System.out.println(compra);
//						System.out.println(portfolio);
//						System.out.println(venda);
						}
						break;
					//ver padrao a seguir
					case 2: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = PatternExit(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),janelapadrao,(Double)a_subject.getGene(dist).getAllele(),((IntegerGene) a_subject.getGene(ppis)).intValue(),((IntegerGene) a_subject.getGene(regras)).intValue(),a_subject.size()-(regras+1),((IntegerGene) a_subject.getGene(liminf)).intValue(),((IntegerGene) a_subject.getGene(limsup)).intValue());
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
//						System.out.println(datacompra);
//						System.out.println(compra);
//						System.out.println(portfolio);
//						System.out.println(venda);
					}
					break;
					// 0 e 1
					case 3: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = DiascomTkSl(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),ndias,((IntegerGene) a_subject.getGene(janela)).intValue(),i,percentagemtk,percentagemsl);
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
//						System.out.println(datacompra);
//						System.out.println(compra);
//						System.out.println(portfolio);
//						System.out.println(venda);
					}
					break;
					// 0 e 2
					case 4: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = DiascomPattern(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),ndias,janelapadrao,(Double)a_subject.getGene(dist).getAllele(),((IntegerGene) a_subject.getGene(ppis)).intValue(),((IntegerGene) a_subject.getGene(regras)).intValue(),a_subject.size()-(regras+1),((IntegerGene) a_subject.getGene(liminf)).intValue(),((IntegerGene) a_subject.getGene(limsup)).intValue());
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
//						System.out.println(datacompra);
//						System.out.println(compra);
//						System.out.println(portfolio);
//						System.out.println(venda);
					}
					break;
					// 1 e 2
					case 5: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = SlTkcomPattern(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),percentagemtk,percentagemsl,janelapadrao,(Double)a_subject.getGene(dist).getAllele(),((IntegerGene) a_subject.getGene(ppis)).intValue(),((IntegerGene) a_subject.getGene(regras)).intValue(),a_subject.size()-(regras+1),((IntegerGene) a_subject.getGene(liminf)).intValue(),((IntegerGene) a_subject.getGene(limsup)).intValue());
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
//						System.out.println(datacompra);
//						System.out.println(compra);
//						System.out.println(portfolio);
//						System.out.println(venda);
					}
					break;
					//todos
					case 6: {
						double precocompra = entry.getKey().getAdjclosesReverse().get((i+1)*((IntegerGene) a_subject.getGene(janela)).intValue());
						int qtaccoescompra = (int) (saldoporaccao / precocompra);
						datacompra.put(entry.getKey().getSimbolo(), dataactual);
						compra.put(entry.getKey().getSimbolo(),precocompra*qtaccoescompra);
						resultado = TodosdeSaida(entry.getKey(),a,(i+1)*((IntegerGene) a_subject.getGene(janela)).intValue(),ndias,percentagemtk,percentagemsl,janelapadrao,(Double)a_subject.getGene(dist).getAllele(),((IntegerGene) a_subject.getGene(ppis)).intValue(),((IntegerGene) a_subject.getGene(regras)).intValue(),a_subject.size()-(regras+1),((IntegerGene) a_subject.getGene(liminf)).intValue(),((IntegerGene) a_subject.getGene(limsup)).intValue());
						rentabilidade = Double.parseDouble(resultado.get(1));
						// Caso a venda ocorra depois do periodo de treino, a venda e feita com o preço do ultimo dia 31/12/2014
						if(resultado.get(0) == null){
							resultado.add(0,entry.getKey().getDates().get(entry.getKey().getDates().size()-1));
							double precovendaespecial = entry.getKey().getAdjclosesReverse().get(entry.getKey().getAdjclosesReverse().size()-1);
							rentabilidade = (precovendaespecial / precocompra) - 1.0;
						}
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
//						System.out.println(datacompra);
//						System.out.println(compra);
//						System.out.println(portfolio);
//						System.out.println(venda);
						}
						break;
						}
			    	}
//			    System.out.println(datacompra);
//			    System.out.println(compra);
//				System.out.println(portfolio);
//				System.out.println(venda);
				}
			//System.out.println(saldo);
			rank.clear();
			}
		saldo = updateSaldo(venda,"01/01/2016",saldo);
		if(saldo > fitness){
			fitness = saldo;
			dias = ndias;
			jpadrao = janelapadrao;
			ptk = percentagemtk;
			psl = percentagemsl;
		}
		return saldo;
		}


	public static int getDias() {
		return dias;
	}

	public static double getPtk() {
		return ptk;
	}

	public static double getPsl() {
		return psl;
	}
	
	public static int getJpadrao() {
		return jpadrao;
	}
	
	public LinkedHashMap<GetDados, Double> sortHashMapByValuesD(HashMap<GetDados, Double> passedMap) {
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
}

