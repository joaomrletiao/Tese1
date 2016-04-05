package dados;
import java.util.ArrayList;


public class StandardDeviation {
	
	private ArrayList<Double> preços = new ArrayList<Double>();
		 
	public StandardDeviation(ArrayList<Double> preços) {
		super();
		this.preços = preços;
	}

	public double getStandardDeviation() {
		  double sum = 0;
		  // Taking the average to numbers
		  for(int i =0; i< preços.size(); i++) {
		   sum = sum + preços.get(i);
		  }
		  
		 
		  double mean = sum/preços.size();
		  ArrayList<Double> deviations = new ArrayList<Double>();
		 
		  // Taking the deviation of mean from each numbers
		  for(int i = 0; i < preços.size(); i++) {
		   deviations.add(i,preços.get(i) - mean); 
		   
		  }
		  
		  ArrayList<Double> squares = new ArrayList<Double>();
		 
		  // getting the squares of deviations
		  for(int i =0; i< preços.size(); i++) {
		   squares.add(i,deviations.get(i) * deviations.get(i));
		  }
		  sum = 0;
		  // adding all the squares
		  for(int i =0; i< squares.size(); i++) {
		   sum = sum + squares.get(i);
		  }
		  // dividing the numbers by one less than total numbers
		  double result = sum / (preços.size() - 1);
		  double standardDeviation = Math.sqrt(result);
		  
		  return standardDeviation;
		 }

}
