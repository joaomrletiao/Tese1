
 /* This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package GA;

import dados.AlgoritmoTrading;
import dados.GetDados;
import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import org.jgap.*;
import org.jgap.audit.*;
import org.jgap.data.*;
import org.jgap.impl.*;
import org.jgap.xml.*;
import org.w3c.dom.*;
import java.*;

 public class MaximizeProfit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.27 $";
  private static ArrayList<GetDados> Dados = new ArrayList<GetDados>();
  /**
   * The total number of times we'll let the population evolve.
   */
  //mudar isto para 100 a 150
  private static final int MAX_ALLOWED_EVOLUTIONS = 30;
  private static final int saida = 0;
  private static final int acc = 1;
  private static final int liminf = 2;
  private static final int limsup = 3;
  private static final int janelades = 4;
  private static final int dist = 5;
  private static final int ppis = 6;
  private static final int regras = 7;
  public static EvolutionMonitor m_monitor;
  
  private static IChromosome bestSolution;

  public static IChromosome getBestSolution() {
	return bestSolution;
}

public static void makeProfit(ArrayList<GetDados> accao,int pop, int generations)
      throws Exception {
   
    Configuration conf = new DefaultConfiguration();
    conf.setPreservFittestIndividual(true);
    conf.setKeepPopulationSizeConstant(false);
    conf.addGeneticOperator(new Cooperator(conf));
    conf.addGeneticOperator(new MutateOperator(conf));
    conf.getGeneticOperators().remove(0);
    conf.getGeneticOperators().remove(0);
    System.out.println(conf.getGeneticOperators());
   
    FitnessFunction myFunc =
        new MaximizeProfitFitnessFunction(accao);
    conf.setFitnessFunction(myFunc);
    
    
    //Caso de estudo de produzir padroes 
    Gene[] sampleGenes = new Gene[4];
    sampleGenes[0] = new IntegerGene(conf, 20, 100); // Tamanho janela
    sampleGenes[1] = new IntegerGene(conf,5,20); //Nºaccoes diferentes
    sampleGenes[2] = new IntegerGene(conf,2,10); //Limite inferior padrao
    sampleGenes[3] = new IntegerGene(conf,11,30); //Limite superior padrao 
    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sampleChromosome);
    
    //Populacao tem de ser 128
    conf.setPopulationSize(pop);
    
    Genotype population;

    AlgoritmoTrading a = new AlgoritmoTrading();
    population = Genotype.randomInitialGenotype(conf);
    for(int i=0;i < population.getPopulation().size(); i++){
    	Gene[] newGenes = new Gene[sampleGenes.length + 4];
    	Integer janela = (Integer) population.getPopulation().getChromosome(i).getGene(0).getAllele();
    	//Tipo de medida para sair
    	newGenes[saida] = new IntegerGene(conf,0,6);
    	//newGenes[saida].setAllele(0 + (int)(Math.random() * ((6 - 0) + 1)));
    	newGenes[saida].setAllele(2);
    	//Nºde accoes 
    	newGenes[acc] = population.getPopulation().getChromosome(i).getGene(1);
    	newGenes[acc].setAllele(7);
    	//Limite inferior 
    	newGenes[liminf] = population.getPopulation().getChromosome(i).getGene(2);
    	newGenes[liminf].setAllele(8);
    	//Limite superior
    	newGenes[limsup] = population.getPopulation().getChromosome(i).getGene(3);
    	newGenes[limsup].setAllele(16);
    	//Janela
    	newGenes[janelades] = population.getPopulation().getChromosome(i).getGene(0);
    	newGenes[janelades].setAllele(46);
    	//Nºde PPis
    	newGenes[ppis] = new IntegerGene(conf,5,Math.min(janela/4,12));//janela/4 maximo e 12, maximo igual ao minimo??
    	newGenes[ppis].setAllele(8);
    	//newGenes[ppis].setAllele(5 + (int)(Math.random() * ((Math.min(janela/4,12) - 5) + 1)));
    	//Nº de regras
    	newGenes[regras] = new IntegerGene(conf,1,Math.min(((Integer) newGenes[ppis].getAllele()-1),5));
    	newGenes[regras].setAllele(2);
    	//newGenes[regras].setAllele(1 + (int)(Math.random() * ((Math.min(((Integer) newGenes[ppis].getAllele()-1),5) - 1) + 1)));
    	//Padrao a descobrir
    	int tamanhopadrao = a.getNumberRules((Integer)newGenes[ppis].getAllele(), (Integer)newGenes[regras].getAllele());
    	Gene[] finalGenes = new Gene[newGenes.length + tamanhopadrao];
    	//DistanciaSAX - limite superior esta de acordo com o tamanho do padrao de 5 em 5 (4 letras max e 5, 5 letras max e 10, etc)
    	int distlimite = ((tamanhopadrao+1)-4)*5;
    	//PIORES RESULTADOS com este limite a cada letra a mais no padrao a margem de erro e de uma distancia de 5, e por isso e a raiz de 25 que somamos 
    	//double distlimite = Math.sqrt(((tamanhopadrao+3)-4)*25);
    	newGenes[dist] = new DoubleGene(conf,0,distlimite);
    	Random s = new Random();
    	double randomValue = 47.2082; 
    	//double randomValue = 0 + (distlimite - 0) * s.nextDouble();
    	newGenes[dist].setAllele(randomValue);
    	
    	for(int b=0;b < newGenes.length;b++){
    		 finalGenes[b] = newGenes[b];
    	}
    	if((Integer)newGenes[regras].getAllele() == 1){
    		for(int j=regras+1; j < tamanhopadrao+8;j++){
    			finalGenes[j] = new StringGene(conf,1,1);
    			final String alphabet = "HCRWM";
    			final int N = alphabet.length();
    			Random r = new Random();
    			finalGenes[j].setAllele(Character.toString(alphabet.charAt(r.nextInt(N))));
    		}		
    	}else{ 
    		for(int r=regras+1; r < (Integer)newGenes[regras].getAllele()+8; r++){
    			finalGenes[r] = new StringGene(conf,1,1);
    			final String alphabet = "HCRWM";
    			final int N = alphabet.length();
    			Random random = new Random();
    			finalGenes[r].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    		}
    		int letrasrestantes = tamanhopadrao - (Integer)newGenes[regras].getAllele();
    		int nregras = (Integer)newGenes[regras].getAllele();
    		int blocos = nregras-1;
    		int count=0,count1=0;
    		int ppiactual = 2;
    		int ppi = 1;
    		int possregras=0,possiveisregras=0, start= 8+nregras;
    		possiveisregras =  (Integer)newGenes[ppis].getAllele() - ppi;
			int reg = Math.min(possiveisregras, nregras);
    		for(int l=nregras+8; l < letrasrestantes+nregras+8; l++){
    			possregras =  (Integer)newGenes[ppis].getAllele() - ppiactual;
    			int re = Math.min(possregras, nregras);
    			finalGenes[l] = new StringGene(conf,1,1);
    			if(count1 == reg || ((reg != re) && (count1 == re))){
    				ppi++;
    				ppiactual++;
    				possiveisregras =  (Integer)newGenes[ppis].getAllele() - ppi;
        			reg = Math.min(possiveisregras, nregras);
    				start = start + reg; 
    				count1=0;
    			}
    			if((count == blocos) && (count < re)){
    				final String alphabet = "HCRWM";
        			final int N = alphabet.length();
        			Random random = new Random();
        			finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    				count=0;
    				count1++;
    				//ppiactual++;

    			}else {
    			if( finalGenes[start - reg].getAllele().equals("H") && ((finalGenes[l-(reg-1)].getAllele().equals("M")) || (finalGenes[l-(reg-1)].getAllele().equals("R")))){
    				final String alphabet = "RW";
        			final int N = alphabet.length();
        			Random random = new Random();
        			finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    			} else if((finalGenes[start - reg].getAllele().equals("H")) && (finalGenes[l-(reg-1)].getAllele().equals("W"))){
    				finalGenes[l].setAllele("W");
    			} else if((finalGenes[start - reg].getAllele().equals("C")) && (finalGenes[l-(reg-1)].getAllele().equals("M"))){
    				finalGenes[l].setAllele("R");
    			} else if((finalGenes[start - reg].getAllele().equals("C")) && ((finalGenes[l-(reg-1)].getAllele().equals("R")) || (finalGenes[l-(reg-1)].getAllele().equals("W")))){
    				finalGenes[l].setAllele("W");
    			} else if((finalGenes[start - reg].getAllele().equals("R")) && (finalGenes[l-(reg-1)].getAllele().equals("M"))){
        			finalGenes[l].setAllele("H");
    			}else if((finalGenes[start - reg].getAllele().equals("W")) && (finalGenes[l-(reg-1)].getAllele().equals("M"))){
        			finalGenes[l].setAllele("C");
    			}else if(((finalGenes[start - reg].getAllele().equals("R")) || (finalGenes[start - reg].getAllele().equals("W"))) && ((finalGenes[l-(reg-1)].getAllele().equals("C")) || (finalGenes[l-(reg-1)].getAllele().equals("H")))){
            		finalGenes[l].setAllele("C");
    			} else if((finalGenes[start - reg].getAllele().equals("M")) && ((finalGenes[l-(reg-1)].getAllele().equals("C")) || (finalGenes[l-(reg-1)].getAllele().equals("H")))){
    				final String alphabet = "CH";
        			final int N = alphabet.length();
        			Random random = new Random();
        			finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
        		} else if(((finalGenes[start - reg].getAllele().equals("M")) && (finalGenes[l-(reg-1)].getAllele().equals("R"))) || ((finalGenes[start - reg].getAllele().equals("M")) && (finalGenes[l-(reg-1)].getAllele().equals("W")))){
        			final String alphabet = "RW";
        			final int N = alphabet.length();
        			Random random = new Random();
        			finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
        		} else if((finalGenes[start - reg].getAllele().equals("M")) && (finalGenes[l-(reg-1)].getAllele().equals("M"))){
        			finalGenes[l].setAllele("M");
        		}else if((finalGenes[start - reg].getAllele().equals("C")) && (finalGenes[l-(reg-1)].getAllele().equals("H"))){
        			//acrescentar W?
        			final String alphabet = "MR";
        			final int N = alphabet.length();
        			Random random = new Random();
        			finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    			}else if((finalGenes[start - reg].getAllele().equals("H")) && (finalGenes[l-(reg-1)].getAllele().equals("C"))){
    				final String alphabet = "MHC";
    	        	final int N = alphabet.length();
    	        	Random random = new Random();
    	        	finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    			}else if((finalGenes[start - reg].getAllele().equals("W")) && (finalGenes[l-(reg-1)].getAllele().equals("R"))){
    				//acrescentar C?
    				final String alphabet = "HC";
    	        	final int N = alphabet.length();
    	        	Random random = new Random();
    	        	finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    			}else if((finalGenes[start - reg].getAllele().equals("R")) && (finalGenes[l-(reg-1)].getAllele().equals("W"))){
    				final String alphabet = "MRW";
    	        	final int N = alphabet.length();
    	        	Random random = new Random();
    	        	finalGenes[l].setAllele(Character.toString(alphabet.charAt(random.nextInt(N))));
    			}else if(finalGenes[start - reg].getAllele().equals(finalGenes[l-(reg-1)].getAllele())){
    				//Quando as duas letras sao iguais e pq estao mais ou menos na mesma posição 
        			finalGenes[l].setAllele("M");
        		}
    			count++;
    			count1++;
    			}
    		}
    	}
    	//Caso de estudo encontrar padrões conhecidos
    	/*Gene[] newGenes = new Gene[sampleGenes.length + 3];
    	Integer janela = (Integer) population.getPopulation().getChromosome(i).getGene(0).getAllele();
    	//Janela
    	newGenes[0] = population.getPopulation().getChromosome(i).getGene(0);
    	//Tipo de medida para sair
    	newGenes[1] = new IntegerGene(conf,0,6);
    	newGenes[1].setAllele(0 + (int)(Math.random() * ((6 - 0) + 1)));
    	//DistanciaSAX
    	newGenes[2] = population.getPopulation().getChromosome(i).getGene(1);	
    	 */
   
    	population.getPopulation().getChromosome(i).setGenes(finalGenes);
    	   
    }

   System.out.println(population);
    
    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < generations; i++) {
    	//System.out.println(i);
      if (!uniqueChromosomes(population.getPopulation())) {
        throw new RuntimeException("Invalid state in generation "+i);
      }
      if(m_monitor != null) {
        population.evolve(m_monitor);
      }
      else {
    	 System.out.println(i);
        population.evolve();
      }
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Total evolution time: " + ( endTime - startTime)
                       + " ms");
    // Save progress to file. A new run of this example will then be able to
    // resume where it stopped before! --> this is completely optional.
    // ---------------------------------------------------------------------

    // Represent Genotype as tree with elements Chromomes and Genes.
    // -------------------------------------------------------------
    DataTreeBuilder builder = DataTreeBuilder.getInstance();
    IDataCreators doc2 = builder.representGenotypeAsDocument(population);
    // create XML document from generated tree
    XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
    Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
    XMLManager.writeFile(xmlDoc, new File("JGAPExample26.xml"));
    // Display the best solution we found.
    // -----------------------------------
    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    double v1 = bestSolutionSoFar.getFitnessValue();
    ArrayList<String> padrao = new ArrayList<String>();
    bestSolution = bestSolutionSoFar;
    System.out.println("The best solution has a fitness value of " +
                       bestSolutionSoFar.getFitnessValue());
    //bestSolutionSoFar.setFitnessValueDirectly(-1);
    System.out.println("It contains the following: ");
    switch (MaximizeProfitFitnessFunction.getIntParameter(bestSolutionSoFar, saida)){
//    case 0: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias() +" Days to Sell.");
//    break;
//    case 1: System.out.println("\t" + MaximizeProfitFitnessFunction.getPtk()  +" TakeProfit e " + MaximizeProfitFitnessFunction.getPsl() +" StopLoss" );
//    break;
//    case 2: System.out.println("\t" + MaximizeProfitFitnessFunction.getJpadrao()  +" Uptrend Pattern Days.");
//    break;
//    case 3: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias()  +" Days to Sell with " + MaximizeProfitFitnessFunction.getPtk()  +" TakeProfit and " + MaximizeProfitFitnessFunction.getPsl() +" StopLoss" );
//    break;
//    case 4: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias()  +" Days to Sell with " + MaximizeProfitFitnessFunction.getJpadrao() +" Uptrend Pattern Days.");
//    break;
//    case 5: System.out.println("\t" + MaximizeProfitFitnessFunction.getPtk()  +" TakeProfit and " + MaximizeProfitFitnessFunction.getPsl() +" StopLoss with "  + MaximizeProfitFitnessFunction.getJpadrao() +" Uptrend Pattern Days.");
//    break;
//    case 6: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias()  +" Days to Sell with " + MaximizeProfitFitnessFunction.getPtk()  +" TakeProfit e " + MaximizeProfitFitnessFunction.getPsl() +" StopLoss with "  + MaximizeProfitFitnessFunction.getJpadrao() +" Uptrend Pattern Days.");
//    break;
    case 0: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias() +" Dias para vender.");
    break;
    case 1: System.out.println("\t" + MaximizeProfitFitnessFunction.getPtk()  +"% TakeProfit e " + MaximizeProfitFitnessFunction.getPsl() +"% StopLoss" );
    break;
    case 2: System.out.println("\t" + MaximizeProfitFitnessFunction.getJpadrao()  +" Dias do Padrão Ascendente.");
    break;
    case 3: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias()  +" Dias para vender com " + MaximizeProfitFitnessFunction.getPtk()  +"% TakeProfit e " + MaximizeProfitFitnessFunction.getPsl() +"% StopLoss" );
    break;
    case 4: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias()  +" Dias para vender com " + MaximizeProfitFitnessFunction.getJpadrao() +" Dias do Padrão Ascendente.");
    break;
    case 5: System.out.println("\t" + MaximizeProfitFitnessFunction.getPtk()  +"% TakeProfit e " + MaximizeProfitFitnessFunction.getPsl() +"% StopLoss com "  + MaximizeProfitFitnessFunction.getJpadrao() +" Dias do Padrão Ascendente.");
    break;
    case 6: System.out.println("\t" + MaximizeProfitFitnessFunction.getDias()  +" Dias para vender " + MaximizeProfitFitnessFunction.getPtk()  +"% TakeProfit e " + MaximizeProfitFitnessFunction.getPsl() +"% StopLoss com "  + MaximizeProfitFitnessFunction.getJpadrao() +" Dias do Padrão Ascendente.");
    break;
    }
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getIntParameter(
        bestSolutionSoFar, acc) + " Acções.");
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getIntParameter(
        bestSolutionSoFar, liminf) + " Percentagem Inferior.");
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getIntParameter(
        bestSolutionSoFar, limsup) + " Percentagem Superior.");
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getIntParameter(
        bestSolutionSoFar, janelades) + " Janela Deslizante.");
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getDoubleParameter(
        bestSolutionSoFar, dist) + " Distância SAX.");
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getIntParameter(
        bestSolutionSoFar, ppis) + " PPIs.");
    System.out.println("\t" +
            MaximizeProfitFitnessFunction.
            getIntParameter(
         bestSolutionSoFar, regras) + " Relações.");
//    }
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getIntParameter(
//        bestSolutionSoFar, acc) + " Stocks.");
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getIntParameter(
//        bestSolutionSoFar, liminf) + " Lower Percentage.");
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getIntParameter(
//        bestSolutionSoFar, limsup) + " Higher Percentage.");
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getIntParameter(
//        bestSolutionSoFar, janelades) + " Window size.");
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getDoubleParameter(
//        bestSolutionSoFar, dist) + " Distance to buy.");
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getIntParameter(
//        bestSolutionSoFar, ppis) + " PIPs.");
//    System.out.println("\t" +
//            MaximizeProfitFitnessFunction.
//            getIntParameter(
//         bestSolutionSoFar, regras) + " Relations.");
    for(int i=regras+1; i < bestSolutionSoFar.size(); i++){
    	padrao.add(MaximizeProfitFitnessFunction.getStringParameter(bestSolutionSoFar, i));
    	}
    	System.out.println("\t" + padrao + " Pattern.");
   
    	//bestSolution = bestSolutionSoFar;
  }

  /**
   * Main method. A single command-line argument is expected, which is the
   * amount of change to create (in other words, 75 would be equal to 75
   * cents).
   *
   * @param args amount of change in cents to create
   * @throws Exception
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public static void main(String[] args)
      throws Exception {
	  
	    //FileInputStream fstream = new FileInputStream("txt/20.txt");
        FileInputStream fstream = new FileInputStream("txt/" + args[2]);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		//Read File Line By Line
		String[] parts = args[0].split("/");
		String anoinicio = parts[0]; 
		String mesinicio = parts[1]; 
		String diainicio = parts[2];
		String[] parts1 = args[1].split("/");
		String anofim = parts1[0]; 
		String mesfim = parts1[1]; 
		String diafim = parts1[2];
		while ((strLine = br.readLine()) != null)   {
//			GregorianCalendar start = new GregorianCalendar(2013,0,1);
//			GregorianCalendar end = new GregorianCalendar(2014,0,1);
			GregorianCalendar start = new GregorianCalendar(Integer.parseInt(anoinicio),Integer.parseInt(mesinicio),Integer.parseInt(diainicio));
			GregorianCalendar end = new GregorianCalendar(Integer.parseInt(anofim),Integer.parseInt(mesfim),Integer.parseInt(diafim));
			GetDados novosdados = new GetDados(strLine,start,end);
			novosdados.getData(strLine);
			Dados.add(novosdados);
		}	
		br.close();
		makeProfit(Dados, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
		//makeProfit(Dados,128,50);
		/*
		FileInputStream fileIn = new FileInputStream("./Dados.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        ArrayList<GetDados> g1 = null;
		g1 = (ArrayList<GetDados>) in.readObject();
        in.close();
        fileIn.close();
        
        makeProfit(g1);
        
        */
		
	
  }

  /**
   * @param a_pop the population to verify
   * @return true if all chromosomes in the populationa are unique
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
  public static boolean uniqueChromosomes(Population a_pop) {
    // Check that all chromosomes are unique
    for(int i=0;i<a_pop.size()-1;i++) {
      IChromosome c = a_pop.getChromosome(i);
      for(int j=i+1;j<a_pop.size();j++) {
        IChromosome c2 =a_pop.getChromosome(j);
        if (c == c2) {
          return false;
        }
      }
    }
    return true;
	}
}
