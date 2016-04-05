package GA;


import org.jgap.GeneticOperator;
import org.jgap.Population;



import java.util.*;

import org.jgap.*;

/**
* The crossover operator randomly selects two Chromosomes from the
* population and "mates" them by randomly picking a gene and then
* swapping that gene and all subsequent genes between the two
* Chromosomes. The two modified Chromosomes are then added to the
* list of candidate Chromosomes.
*
* If you work with CompositeGenes, this operator expects them to contain
* genes of the same type (e.g. IntegerGene). If you have mixed types, please
* provide your own crossover operator.
*
* This CrossoverOperator supports both fixed and dynamic crossover rates.
* A fixed rate is one specified at construction time by the user. This
* operation is performed 1/m_crossoverRate as many times as there are
* Chromosomes in the population. Another possibility is giving the crossover
* rate as a percentage. A dynamic rate is determined by this class on the fly
* if no fixed rate is provided.
*
* @author Neil Rotstan
* @author Klaus Meffert
* @author Chris Knowles
* @since 1.0
*/
public class Cooperator
   extends BaseGeneticOperator implements Comparable {
 /** String containing the CVS revision. Read out via reflection!*/
 private final static String CVS_REVISION = "$Revision: 1.48 $";

 /**
  * The current crossover rate used by this crossover operator (mutual
  * exclusive to m_crossoverRatePercent and m_crossoverRateCalc).
  */
 private int m_crossoverRate;

 /**
  * Crossover rate in percentage of population size (mutual exclusive to
  * m_crossoverRate and m_crossoverRateCalc).
  */
 private double m_crossoverRatePercent;

 /**
  * Calculator for dynamically determining the crossover rate (mutual exclusive
  * to m_crossoverRate and m_crossoverRatePercent)
  */
 private IUniversalRateCalculator m_crossoverRateCalc;

 /**
  * true: x-over before and after a randomly chosen x-over point
  * false: only x-over after the chosen point.
  */
 private boolean m_allowFullCrossOver;

 /**
  * true: also x-over chromosomes with age of zero (newly created chromosomes)
  */
 private boolean m_xoverNewAge;

 /**
  * Constructs a new instance of this CrossoverOperator without a specified
  * crossover rate, this results in dynamic crossover rate being turned off.
  * This means that the crossover rate will be fixed at populationsize/2.<p>
  * Attention: The configuration used is the one set with the static method
  * Genotype.setConfiguration.
  *
  * @throws InvalidConfigurationException
  *
  * @author Chris Knowles
  * @since 2.0
  */
 public Cooperator()
     throws InvalidConfigurationException {
   super(Genotype.getStaticConfiguration());
   init();
 }

 /**
  * Constructs a new instance of this CrossoverOperator without a specified
  * crossover rate, this results in dynamic crossover rate being turned off.
  * This means that the crossover rate will be fixed at populationsize/2.
  *
  * @param a_configuration the configuration to use
  * @throws InvalidConfigurationException
  *
  * @author Klaus Meffert
  * @since 3.0
  */
 public Cooperator(final Configuration a_configuration)
     throws InvalidConfigurationException {
   super(a_configuration);
   init();
 }

 /**
  * Initializes certain parameters.
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 protected void init() {
   // Set the default crossoverRate.
   // ------------------------------
   m_crossoverRate = 6;
   m_crossoverRatePercent = -1;
   setCrossoverRateCalc(null);
   setAllowFullCrossOver(true);
   setXoverNewAge(true);
 }



 /**
  * Does the crossing over.
  *
  * @param a_population the population of chromosomes from the current
  * evolution prior to exposure to crossing over
  * @param a_candidateChromosomes the pool of chromosomes that have been
  * selected for the next evolved population
  *
  * @author Neil Rotstan
  * @author Klaus Meffert
  * @since 2.0
  */
 public void operate(final Population a_population,
                     final List a_candidateChromosomes) {
   // Work out the number of crossovers that should be performed.
   // -----------------------------------------------------------
   int size = Math.min(getConfiguration().getPopulationSize(),
                       a_population.size());
   int numCrossovers = 0;
   if (m_crossoverRate >= 0) {
     numCrossovers = size / m_crossoverRate;
   }
   else if (m_crossoverRateCalc != null) {
     numCrossovers = size / m_crossoverRateCalc.calculateCurrentRate();
   }
   else {
     numCrossovers = (int) (size * m_crossoverRatePercent);
   }
   RandomGenerator generator = getConfiguration().getRandomGenerator();
   IGeneticOperatorConstraint constraint = getConfiguration().
       getJGAPFactory().getGeneticOperatorConstraint();
   // For each crossover, grab two random chromosomes, pick a random
   // locus (gene location), and then swap that gene and all genes
   // to the "right" (those with greater loci) of that gene between
   // the two chromosomes.
   // --------------------------------------------------------------
   int index1, index2;
   IChromosome chrom1;
   IChromosome chrom2;
   //
   for (int i = 0; i < numCrossovers; i++) {
     index1 = generator.nextInt(size);
     index2 = generator.nextInt(size);
     chrom1 = a_population.getChromosome(index1);
     chrom2 = a_population.getChromosome(index2);
     // Verify that crossover is allowed.
     // ---------------------------------
     if (!isXoverNewAge() && chrom1.getAge() < 1 && chrom2.getAge() < 1) {
       // Crossing over two newly created chromosomes is not seen as helpful
       // here.
       // ------------------------------------------------------------------
       continue;
     }
     if (constraint != null) {
       List v = new Vector();
       v.add(chrom1);
       v.add(chrom2);
       if (!constraint.isValid(a_population, v, this)) {
         // Constraint forbids crossing over.
         // ---------------------------------
         continue;
       }
     }
     // Clone the chromosomes.
     // ----------------------
     IChromosome firstMate = (IChromosome) chrom1.clone();
     IChromosome secondMate = (IChromosome) chrom2.clone();
   
     // In case monitoring is active, support it.
     // -----------------------------------------
     if (m_monitorActive) {
       firstMate.setUniqueIDTemplate(chrom1.getUniqueID(), 1);
       firstMate.setUniqueIDTemplate(chrom2.getUniqueID(), 2);
       secondMate.setUniqueIDTemplate(chrom1.getUniqueID(), 1);
       secondMate.setUniqueIDTemplate(chrom2.getUniqueID(), 2);
     }
     // Cross over the chromosomes.
     // ---------------------------
     doCrossover(firstMate, secondMate, a_candidateChromosomes, generator);
   }
}
 

 @SuppressWarnings("null")
protected void doCrossover(IChromosome firstMate, IChromosome secondMate,
                          List a_candidateChromosomes,
                          RandomGenerator generator) {
   Gene[] firstGenes = firstMate.getGenes();
   Gene[] secondGenes = secondMate.getGenes();
   int limitetroca = 0;
   if(firstMate.size() != secondMate.size()){
   int locus = generator.nextInt(4);
  // System.out.println(locus);
   // Swap the genes.
   // ---------------
   Gene gene1;
   Gene gene2;
   Object firstAllele;
   for (int j = locus; j < 4; j++) {
	 // Make a distinction for ICompositeGene for the first gene.
     // ---------------------------------------------------------
     int index = 0;
     if (firstGenes[j] instanceof ICompositeGene) {
       // Randomly determine gene to be considered.
       // -----------------------------------------
       
       index = generator.nextInt(firstGenes[j].size());
       gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index);
     }
     else {
       gene1 = firstGenes[j];
      // System.out.println(gene1.getAllele());
     }
     // Make a distinction for the second gene if CompositeGene.
     // --------------------------------------------------------
     if (secondGenes[j] instanceof ICompositeGene) {
       gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index);
     }
     else {
       gene2 = secondGenes[j];
     }
     if (m_monitorActive) {
       gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
       gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
     }
     firstAllele = gene1.getAllele();
     gene1.setAllele(gene2.getAllele());
     gene2.setAllele(firstAllele);
   	}
   }else if(firstMate.size() == secondMate.size() && ((Integer)firstMate.getGene(7).getAllele() == 1) && ((Integer)secondMate.getGene(7).getAllele() == 1)){
	   int locus = generator.nextInt(firstGenes.length);
	   //System.out.println(firstMate);
	   //System.out.println(secondMate);
	   // Swap the genes.
	   // ---------------
	   //obriga a que os PPis, as regras, a distSAX e a janela sejam sempre trocadas ao mesmo tempo
	   if(locus == 5 || locus == 6 || locus == 7)
		   locus = 4;
	   Gene gene1;
	   Gene gene2;
	   Object firstAllele;
	   if(locus < 5){
		   limitetroca = 8 + (int)(Math.random() * ((firstGenes.length) - 8) + 1);
		   for (int j = locus; j < limitetroca; j++) {
			   	// Make a distinction for ICompositeGene for the first gene.
			     // ---------------------------------------------------------
			     int index = 0;
			     if (firstGenes[j] instanceof ICompositeGene) {
			       // Randomly determine gene to be considered.
			       // -----------------------------------------
			       
			       index = generator.nextInt(firstGenes[j].size());
			       gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index);
			     }
			     else {
			       gene1 = firstGenes[j];
			      // System.out.println(gene1.getAllele());
			     }
			     // Make a distinction for the second gene if CompositeGene.
			     // --------------------------------------------------------
			     if (secondGenes[j] instanceof ICompositeGene) {
			       gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index);
			     }
			     else {
			       gene2 = secondGenes[j];
			     }
			     if (m_monitorActive) {
			       gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
			       gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
			     }
			     firstAllele = gene1.getAllele();
			     gene1.setAllele(gene2.getAllele());
			     gene2.setAllele(firstAllele);
			   	}
	   }else{
		   // troca os genes conjuntos no minimo ou os genes conjuntos mais um bocado ou todo o padrao
		   limitetroca = locus + (int)(Math.random() * ((firstGenes.length) - locus) + 1);
		   for (int j = locus; j < limitetroca; j++) {
	     // Make a distinction for ICompositeGene for the first gene.
	     // ---------------------------------------------------------
	     int index = 0;
	     if (firstGenes[j] instanceof ICompositeGene) {
	       // Randomly determine gene to be considered.
	       // -----------------------------------------
	       
	       index = generator.nextInt(firstGenes[j].size());
	       gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index);
	     }
	     else {
	       gene1 = firstGenes[j];
	     }
	     // Make a distinction for the second gene if CompositeGene.
	     // --------------------------------------------------------
	     if (secondGenes[j] instanceof ICompositeGene) {
	       gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index);
	     }
	     else {
	       gene2 = secondGenes[j];
	     }
	     if (m_monitorActive) {
	       gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
	       gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
	     }
	     firstAllele = gene1.getAllele();
	     gene1.setAllele(gene2.getAllele());
	     gene2.setAllele(firstAllele);
	   	}
	   }
   }else if(firstMate.size() == secondMate.size() && ((Integer)firstMate.getGene(7).getAllele() != 1) || ((Integer)secondMate.getGene(7).getAllele() != 1)){
	   int locus = generator.nextInt(5);
	   //System.out.println(firstMate);
	   //System.out.println(secondMate);
	   // Swap the genes.
	   Gene gene1;
	   Gene gene2;
	   Object firstAllele;
	   int troca[] = new int[2];
	   troca[0] = 4;
	   troca[1] = firstGenes.length;
	   Random r = new Random();
	   int Result = r.nextInt(1);
	   if(locus < 4){
		   for (int j = locus; j < troca[Result] ; j++) {
			   	// Make a distinction for ICompositeGene for the first gene.
			     // ---------------------------------------------------------
			     int index = 0;
			     if (firstGenes[j] instanceof ICompositeGene) {
			       // Randomly determine gene to be considered.
			       // -----------------------------------------
			       
			       index = generator.nextInt(firstGenes[j].size());
			       gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index);
			     }
			     else {
			       gene1 = firstGenes[j];
			      // System.out.println(gene1.getAllele());
			     }
			     // Make a distinction for the second gene if CompositeGene.
			     // --------------------------------------------------------
			     if (secondGenes[j] instanceof ICompositeGene) {
			       gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index);
			     }
			     else {
			       gene2 = secondGenes[j];
			     }
			     if (m_monitorActive) {
			       gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
			       gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
			     }
			     firstAllele = gene1.getAllele();
			     gene1.setAllele(gene2.getAllele());
			     gene2.setAllele(firstAllele);
			   	}
	   }else{
		   // troca os genes conjuntos mais todo o padrao para que as letras estejam de acordo com os ppis e regras
		   for (int j = locus; j < firstGenes.length; j++) {
	     // Make a distinction for ICompositeGene for the first gene.
	     // ---------------------------------------------------------
	     int index = 0;
	     if (firstGenes[j] instanceof ICompositeGene) {
	       // Randomly determine gene to be considered.
	       // -----------------------------------------
	       
	       index = generator.nextInt(firstGenes[j].size());
	       gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index);
	     }
	     else {
	       gene1 = firstGenes[j];
	     }
	     // Make a distinction for the second gene if CompositeGene.
	     // --------------------------------------------------------
	     if (secondGenes[j] instanceof ICompositeGene) {
	       gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index);
	     }
	     else {
	       gene2 = secondGenes[j];
	     }
	     if (m_monitorActive) {
	       gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
	       gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
	     }
	     firstAllele = gene1.getAllele();
	     gene1.setAllele(gene2.getAllele());
	     gene2.setAllele(firstAllele);
	   	}
	   }
   
   }
   // Add the modified chromosomes to the candidate pool so that
   // they'll be considered for natural selection during the next
   // phase of evolution.
   // -----------------------------------------------------------
   a_candidateChromosomes.add(firstMate);
   a_candidateChromosomes.add(secondMate);
 }

 /**
  * Sets the crossover rate calculator.
  *
  * @param a_crossoverRateCalculator the new calculator
  *
  * @author Chris Knowles
  * @since 2.0
  */
 private void setCrossoverRateCalc(final IUniversalRateCalculator
                                   a_crossoverRateCalculator) {
   m_crossoverRateCalc = a_crossoverRateCalculator;
   if (a_crossoverRateCalculator != null) {
     m_crossoverRate = -1;
     m_crossoverRatePercent = -1d;
   }
 }

 /**
  * Compares the given object to this one.
  *
  * @param a_other the instance against which to compare this instance
  * @return a negative number if this instance is "less than" the given
  * instance, zero if they are equal to each other, and a positive number if
  * this is "greater than" the given instance
  *
  * @author Klaus Meffert
  * @since 2.6
  */
 public int compareTo(final Object a_other) {
   /**@todo consider Configuration*/
   if (a_other == null) {
     return 1;
   }
   Cooperator op = (Cooperator) a_other;
   if (m_crossoverRateCalc == null) {
     if (op.m_crossoverRateCalc != null) {
       return -1;
     }
   }
   else {
     if (op.m_crossoverRateCalc == null) {
       return 1;
     }
   }
   if (m_crossoverRate != op.m_crossoverRate) {
     if (m_crossoverRate > op.m_crossoverRate) {
       return 1;
     }
     else {
       return -1;
     }
   }
   if (m_allowFullCrossOver != op.m_allowFullCrossOver) {
     if (m_allowFullCrossOver) {
       return 1;
     }
     else {
       return -1;
     }
   }
   if (m_xoverNewAge != op.m_xoverNewAge) {
     if (m_xoverNewAge) {
       return 1;
     }
     else {
       return -1;
     }
   }
   // Everything is equal. Return zero.
   // ---------------------------------
   return 0;
 }

 /**
  * @param a_allowFullXOver x-over before and after a randomly chosen point
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 public void setAllowFullCrossOver(boolean a_allowFullXOver) {
   m_allowFullCrossOver = a_allowFullXOver;
 }

 /**
  * @return x-over before and after a randomly chosen x-over point
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 public boolean isAllowFullCrossOver() {
   return m_allowFullCrossOver;
 }

 /**
  * @return the crossover rate set
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 public int getCrossOverRate() {
   return m_crossoverRate;
 }

 /**
  * @return the crossover rate set
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 public double getCrossOverRatePercent() {
   return m_crossoverRatePercent;
 }

 /**
  * @param a_xoverNewAge true: also x-over chromosomes with age of zero (newly
  * created chromosomes)
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 public void setXoverNewAge(boolean a_xoverNewAge) {
   m_xoverNewAge = a_xoverNewAge;
 }

 /**
  * @return a_xoverNewAge true: also x-over chromosomes with age of zero (newly
  * created chromosomes)
  *
  * @author Klaus Meffert
  * @since 3.3.2
  */
 public boolean isXoverNewAge() {
   return m_xoverNewAge;
 }
}

