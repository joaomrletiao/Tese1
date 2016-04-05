package dados;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class AlgoritmoTrading{
	
	private String simbolo;
	private ArrayList<Point2D.Double> finalpips;
	
	public AlgoritmoTrading(String s){
		simbolo = s;
	}
	public AlgoritmoTrading(){
	}
	
	public ArrayList<Point2D.Double> getPIPs(ArrayList<Double> prices,int pipsnumber){
		double maxdist = 0.0;
		Point2D.Double pip = new Point2D.Double();
		ArrayList<Point2D.Double> pipspoints = new ArrayList<Point2D.Double>();
		ArrayList<Point2D.Double> allpoints = new ArrayList<Point2D.Double>();
		ArrayList<Point2D.Double> pipspointstest = new ArrayList<Point2D.Double>();
		ArrayList<Double> pips = new ArrayList<Double>(pipsnumber);
		ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();
		pips.add(0,prices.get(0));
		pips.add(1,prices.get(prices.size()-1));
		for(int i=0;i<prices.size();i++){
			pipspoints.add(new Point2D.Double(i,prices.get(i)));
			allpoints.add(new Point2D.Double(i,prices.get(i)));
			}
		pipspointstest.add(pipspoints.get(0));
		pipspointstest.add(pipspoints.get(pipspoints.size()-1));
		Line2D.Double line = new Line2D.Double(pipspoints.get(0), pipspoints.get(prices.size()-1));
		lines.add(line);
		for(int a=1;a<pipsnumber-1;a++){
			for(int j=1;j<pipspoints.size()-1;j++){
				for(Line2D.Double l : lines){
					if(pipspoints.get(j).getX() > l.getX1() && pipspoints.get(j).getX() < l.getX2()){
						if(l.ptLineDist(pipspoints.get(j)) > maxdist){
							pip = pipspoints.get(j);
							maxdist = l.ptLineDist(pipspoints.get(j));
						}
					}
				}
			}
			pipspointstest.add(a,pip);
			ArrayList<Point2D.Double> ordered = orderppis(pipspointstest,allpoints);
			pips.add(a,prices.get((int)pip.getX()));
			lines.removeAll(lines);
			for(int x=0;x<pipspointstest.size()-1;x++){
				lines.add(new Line2D.Double(ordered.get(x),ordered.get(x+1)));
			}	
			pipspoints.remove(pip);
			maxdist = 0.0;	
		}
		finalpips = orderppis(pipspointstest,allpoints);
		return orderppis(pipspointstest,allpoints);
		//return pipspointstest;
	}	

	public ArrayList<Point2D.Double> orderppis(ArrayList<Point2D.Double> p, ArrayList<Point2D.Double> allpoints){
		ArrayList<Double> ycord = new ArrayList<Double>();
		ArrayList<Point2D.Double> order = new ArrayList<Point2D.Double>();
		for(Point2D.Double poi : p ){
			ycord.add(poi.getX());
		}
		Collections.sort(ycord);
		for(Double d : ycord){
			order.add(allpoints.get(d.intValue()));	
		}
		return order;
	}

	public ArrayList<String> createRules(ArrayList<Point2D.Double> pips,int regras,int liminf,int limsup){
		ArrayList<String> rules = new ArrayList<String>();
		if(regras == pips.size()){
			return null;
		}
		else if(regras == 0){
			return null;
		}
		for(int i=0;i<pips.size()-1;i++){
			double diff=0;
			diff = (pips.get(i+1).getY() / pips.get(i).getY()) - 1.0;
			if((diff*100) > liminf){
				if((diff*100) < limsup)
					rules.add("H");
				else rules.add("C");
					if(regras > 1 && pips.size()-i > regras){
						for(int x=2;x<regras+1;x++){
							diff = (pips.get(i+x).getY() / pips.get(i).getY()) - 1.0;
							if((diff*100) > liminf){
								if((diff*100) < limsup)
									rules.add("H");
								else rules.add("C");
							}
							else if((diff*100) < -liminf){
								if((diff*100) > -limsup)
									rules.add("R");
								else rules.add("W");
							}else
							rules.add("M");
							}
				}else if(regras > 1 && pips.size()-i <= regras && pips.size()-i > 2){
					for(int y=2;y<pips.size()-i;y++){
						diff = (pips.get(i+y).getY() / pips.get(i).getY()) - 1.0;
						if((diff*100) > liminf){
							if((diff*100) < limsup)
								rules.add("H");
							else rules.add("C");
					}
					else if((diff*100) < -liminf){
							if((diff*100) > -limsup)
								rules.add("R");
							else rules.add("W");
						}else 
						rules.add("M");
						}
				}
			}else if((diff*100) < -liminf){
				if((diff*100) > -limsup)
					rules.add("R");
				else rules.add("W");
				if(regras > 1 && pips.size()-i > regras){
					for(int y=2;y<regras+1;y++){
						diff = (pips.get(i+y).getY() / pips.get(i).getY()) - 1.0;
						if((diff*100) > liminf){
							if((diff*100) < limsup)
								rules.add("H");
							else rules.add("C");
							}
							else if((diff*100) < -liminf){
								if((diff*100) > -limsup)
									rules.add("R");
								else rules.add("W");
							}else
							rules.add("M");
						}
					}else if(regras > 1 && pips.size()-i <= regras && pips.size()-i > 2){
						for(int y=2;y<pips.size()-i;y++){
							diff = (pips.get(i+y).getY() / pips.get(i).getY()) - 1.0;
							if((diff*100) > liminf){
								if((diff*100) < limsup)
									rules.add("H");
								else rules.add("C");
								}
								else if((diff*100) < -liminf){
									if((diff*100) > -limsup)
										rules.add("R");
									else rules.add("W");
							}else 
							rules.add("M");
							}
					}
				}else if((diff*100) < liminf || (diff*100) > -liminf){
					rules.add("M");
					if(regras > 1 && pips.size()-i > regras){
					for(int y=2;y<regras+1;y++){
						diff = (pips.get(i+y).getY() / pips.get(i).getY()) - 1.0;
						if((diff*100) > liminf){
							if((diff*100) < limsup)
								rules.add("H");
							else rules.add("C");
							}
							else if((diff*100) < -liminf){
								if((diff*100) > -limsup)
									rules.add("R");
								else rules.add("W");
							}else
							rules.add("M");
						}
						}else if(regras > 1 && pips.size()-i <= regras && pips.size()-i > 2){
							for(int y=2;y<pips.size()-i;y++){
							diff = (pips.get(i+y).getY() / pips.get(i).getY()) - 1.0;
							if((diff*100) > liminf){
								if((diff*100) < limsup)
									rules.add("H");
								else rules.add("C");
								}
								else if((diff*100) < -liminf){
									if((diff*100) > -limsup)
										rules.add("R");
									else rules.add("W");
								}else 
								rules.add("M");
							}
						}
					}
				}
		return rules;
	}
	
	public int getNumberRules(int pips,int regras){
		int diff= 0;
		for(int i=0;i<pips-1;i++){
			diff += 1;
					if(regras > 1 && pips-i > regras){
						for(int x=2;x<regras+1;x++){
							diff += 1;
							}
					}else if(regras > 1 && (pips-i) <= regras && (pips-i) > 2){
					for(int y=2;y<pips-i;y++){
							diff += 1;
						}
				}
		}
	return diff;
	}
	
	
	public int LevenshteinDistance(ArrayList<String> lhs, ArrayList<String> rhs) {                          
	    int len0 = lhs.size() + 1;                                                     
	    int len1 = rhs.size() + 1;                                                     
	                                                                                    
	    // the array of distances                                                       
	    int[] cost = new int[len0];                                                     
	    int[] newcost = new int[len0];                                                  
	                                                                                    
	    // initial cost of skipping prefix in String s0                                 
	    for (int i = 0; i < len0; i++) cost[i] = i;                                     
	                                                                                    
	    // dynamically computing the array of distances                                  
	                                                                                    
	    // transformation cost for each letter in s1                                    
	    for (int j = 1; j < len1; j++) {                                                
	        // initial cost of skipping prefix in String s1                             
	        newcost[0] = j;                                                             
	                                                                                    
	        // transformation cost for each letter in s0                                
	        for(int i = 1; i < len0; i++) {                                             
	            // matching current letters in both strings                             
	            int match = (lhs.get(i - 1) == rhs.get(j - 1)) ? 0 : 1;             
	                                                                                    
	            // computing cost for each transformation                               
	            int cost_replace = cost[i - 1] + match;                                 
	            int cost_insert  = cost[i] + 1;                                         
	            int cost_delete  = newcost[i - 1] + 1;                                  
	                                                                                    
	            // keep minimum cost                                                    
	            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
	        }                                                                           
	                                                                                    
	        // swap cost/newcost arrays                                                 
	        int[] swap = cost; cost = newcost; newcost = swap;                          
	    }                                                                               
	                                                                                    
	    // the distance is the cost for transforming all letters in both strings        
	    return cost[len0 - 1];                                                          
	}
	
	public ArrayList<String> bigrams(ArrayList<String> s){
		ArrayList<String> test,bigrams;
		bigrams = new ArrayList<String>();
		test = s;
		test.add(0,"#");
		test.add(s.size(),"#");
		for(int i=0;i<s.size()-1;i++){
			bigrams.add(i,test.get(i).concat(test.get(i+1)));
		}
		
		return bigrams;
	}
	
	public double jaccard(ArrayList<String> padrao,ArrayList<String> seq){
		ArrayList<String> bi1,bi2,intersection;
		double result;
		intersection = new ArrayList<String>();
		bi1 = bigrams(padrao);
		bi2 = bigrams(seq);
		for(int i=0;i<bi1.size();i++){
			for(int j=0;j<bi2.size();j++){
				if(bi1.get(i).equals(bi2.get(j))){
					intersection.add(bi1.get(i));
				}
			}	
		}
		bi1.addAll(bi2);
		result = (double) intersection.size()/(double) bi1.size();
		return result;
	}
	
	public double distanceSAX(ArrayList<String> padrao,ArrayList<String> seq){
		double result,somatorio = 0;
		for(int i=0;i<padrao.size();i++){
			char c = padrao.get(i).charAt(0);
			char d = seq.get(i).charAt(0);
			somatorio += Math.pow(c - d,2);
			
		}
		result = Math.sqrt(somatorio);
		somatorio = 0;
		return result;
	}
	
	public double getAmplitude(ArrayList<Double> preços){
		double max = 0.0;
		double min = Double.MAX_VALUE;
		double amplitude = 0;
		for(Double d: preços){
			if(d > max)
				max = d;
			else if(d < min)
				min = d;
			
		}
		amplitude = max - min;
		return amplitude;
	}
	
	public ArrayList<String> takeProfitpercentagem(GetDados g,int compra,double percentagem){
		ArrayList<String> datas = new ArrayList<String>(g.getDates().subList(compra,g.getDates().size()));
		ArrayList<Double> prices = new ArrayList<Double>(g.getAdjclosesReverse().subList(compra,g.getAdjclosesReverse().size()));
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		double preçocompra = g.getAdjclosesReverse().get(compra);
		double preçovenda = 0;
		for(int b=1;b<prices.size();b++){
			if((((prices.get(b) / preçocompra) - 1.0)*100) >= percentagem){
				diavenda = datas.get(b);
				preçovenda = prices.get(b);
				break;
			}
		}
		double diff = (preçovenda / preçocompra) - 1.0;
		result.add(diavenda);
		String diff1 = String.valueOf(diff);
		result.add(diff1);
		return result;
	}
	
	public ArrayList<String> stopLosspercentagem(GetDados g,int compra,double percentagem){
		ArrayList<String> datas = new ArrayList<String>(g.getDates().subList(compra,g.getDates().size()));
		ArrayList<Double> prices = new ArrayList<Double>(g.getAdjclosesReverse().subList(compra,g.getAdjclosesReverse().size()));
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		double preçocompra = g.getAdjclosesReverse().get(compra);
		double preçovenda = 0;
		for(int b=1;b<prices.size();b++){
			if((((prices.get(b) / preçocompra) - 1.0) * 100) <= percentagem){
				diavenda = datas.get(b);
				preçovenda = prices.get(b);
				break;
			}
		}
		double diff = (preçovenda / preçocompra) - 1.0;
		result.add(diavenda);
		String diff1 = String.valueOf(diff);
		result.add(diff1);
		return result;
	}

	public ArrayList<String> takeProfit(GetDados g,int janela,int i,int compra,double variavel){
		ArrayList<String> datas = new ArrayList<String>(g.getDates().subList(i*janela,g.getDates().size()));
		ArrayList<Double> prices = new ArrayList<Double>(g.getAdjclosesReverse().subList(i*janela,g.getAdjclosesReverse().size()));
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		double preçovenda = 0;
		double preçocompra = g.getAdjclosesReverse().get(compra);
		double a = getAmplitude(new ArrayList<Double>(prices.subList(0,janela)));
		double constante = prices.get(janela) + (a*variavel);
		for(int b=janela;b<prices.size();b++){
			if(prices.get(b) >= constante){
				diavenda = datas.get(b);
				preçovenda = prices.get(b);
				break;
			}
		}
		double diff = (preçovenda / preçocompra) - 1.0;
		result.add(diavenda);
		String diff1 = String.valueOf(diff);
		result.add(diff1);
		return result;
	}
	
	public ArrayList<String> stopLoss(GetDados g,int janela,int i,int compra,double variavel){
		ArrayList<String> datas = new ArrayList<String>(g.getDates().subList(i*janela,g.getDates().size()));
		ArrayList<Double> prices = new ArrayList<Double>(g.getAdjclosesReverse().subList(i*janela,g.getAdjclosesReverse().size()));
		ArrayList<String> result = new ArrayList<String>();
		String diavenda = null;
		double preçovenda = 0;
		double preçocompra = g.getAdjclosesReverse().get(compra);
		double a = getAmplitude(new ArrayList<Double>(prices.subList(0,janela)));
		double constante = prices.get(janela) - (a*variavel); 
		for(int b=janela;b<prices.size();b++){
			if(prices.get(b) <= constante){
				diavenda = datas.get(b);
				preçovenda = prices.get(b);
				break;
			}
		}
		double diff = (preçovenda / preçocompra) - 1.0;
		result.add(diavenda);
		String diff1 = String.valueOf(diff);
		result.add(diff1);
		return result;
	}
	
	public String janelaVenda(GetDados g,int diacompra, int ndias){
		String diavenda = g.getDates().get(diacompra+ndias);
		double preçovenda = 0;
		preçovenda = g.getAdjclosesReverse().get(diacompra+ndias);
		return "Vender activo " + this.getSimbolo() + " no dia " + diavenda + " ao preço " + preçovenda +" que foi comprado ao preço " + g.getAdjclosesReverse().get(diacompra);
	}
	
	public ArrayList<String> janelaVendadiff(GetDados g,int diacompra, int ndias){
		ArrayList<String> resultado = new ArrayList<String>();
		String diavenda = null;
		double preçovenda = 0;
		preçovenda = g.getAdjclosesReverse().get(diacompra+ndias);
		diavenda = g.getDates().get(diacompra+ndias);
		double diff = ((preçovenda / g.getAdjclosesReverse().get(diacompra)) - 1.0);
		resultado.add(diavenda);
		String diff1 = String.valueOf(diff);
		resultado.add(diff1);
		return resultado;
	}
	
	public ArrayList<String> getPatternExit(GetDados g,int diadecompra,int janela,double dist,int ppis,int regras,int tpadrao,int liminf,int limsup){
		ArrayList<String> resultado = new ArrayList<String>();
		ArrayList<String> padrao = new ArrayList<String>();
		ArrayList<String> ascendente = new ArrayList<String>();
		ArrayList<Double> serie = new ArrayList<Double>(g.getAdjclosesReverse().subList(diadecompra+1,g.getAdjclosesReverse().size()));
		ArrayList<String> se1rie = new ArrayList<String>(g.getDates().subList(diadecompra+1,g.getDates().size()));
		String diavenda = null;
		double precocompra = g.getAdjclosesReverse().get(diadecompra);
		double preçovenda = 0;
		for(int j=0; j < tpadrao;j++){
			ascendente.add(j,"C");
		}
		for(int i=0; i < serie.size()/janela;i++){
			ArrayList<Double> a = new ArrayList<Double>(serie.subList(i*janela,(i+1)*janela));
			ArrayList<Point2D.Double> teste = this.getPIPs(a,ppis);
			padrao = this.createRules(teste,regras,liminf,limsup);
//			System.out.println(padrao);
//			System.out.println(this.distanceSAX1(padrao,ascendente));
			if(this.distanceSAX1(padrao,ascendente) > dist){
				if((i+1)*janela == serie.size()){
					preçovenda = serie.get(((i+1)*janela)-1);
					diavenda = se1rie.get(((i+1)*janela)-1);
					break;
				}else 
				preçovenda = serie.get((i+1)*janela);
				diavenda = se1rie.get((i+1)*janela);
				break;
				}
		}	
		double diff = (preçovenda / precocompra) - 1.0;
		resultado.add(diavenda);
		String diff1 = String.valueOf(diff);
		resultado.add(diff1);
		return resultado;	
	}
	
	public ArrayList<String> Padraodesaida(GetDados g,int diadecompra,int janela,double dist,int ppis,int regras,int tpadrao,int liminf,int limsup){
		ArrayList<String> resultado = new ArrayList<String>();
		ArrayList<String> padrao = new ArrayList<String>();
		ArrayList<String> ascendente = new ArrayList<String>();
		ArrayList<Double> serie = new ArrayList<Double>(g.getAdjclosesReverse().subList(diadecompra+1,g.getAdjclosesReverse().size()));
		ArrayList<String> se1rie = new ArrayList<String>(g.getDates().subList(diadecompra+1,g.getDates().size()));
		String diavenda = null;
		double precocompra = g.getAdjclosesReverse().get(diadecompra);
		double preçovenda = 0;
		for(int j=0; j < tpadrao;j++){
			ascendente.add(j,"R");
		}
		for(int i=0; i < serie.size()/janela;i++){
			ArrayList<Double> a = new ArrayList<Double>(serie.subList(i*janela,(i+1)*janela));
			ArrayList<Point2D.Double> teste = this.getPIPs(a,ppis);
			padrao = this.createRules(teste,regras,liminf,limsup);
			if(this.distanceSAX1(padrao,ascendente) <= dist){
				preçovenda = serie.get((i+1)*janela);
				diavenda = se1rie.get((i+1)*janela);
				break;
				}
		}	
		double diff = (preçovenda / precocompra) - 1.0;
		resultado.add(diavenda);
		String diff1 = String.valueOf(diff);
		resultado.add(diff1);
		return resultado;	
	}
	
	
	
	public double distanceSAX1(ArrayList<String> padrao,ArrayList<String> seq){
		double result,somatorio = 0;
		for(int i=0;i<padrao.size();i++){
			char c = padrao.get(i).charAt(0);
			char d = seq.get(i).charAt(0);
			if((c == ("H").charAt(0) && d == ("C").charAt(0))){
				somatorio += Math.pow(0,2);
			}
			
			else {
				somatorio += Math.pow(c-d, 2);
			}
		}
		result = Math.sqrt(somatorio);
		somatorio = 0;
		return result;
	}
	
	public String getSimbolo() {
		return simbolo;
	}
	
	public ArrayList<Point2D.Double> getFinalpips() {
		return finalpips;
	}
	
}
