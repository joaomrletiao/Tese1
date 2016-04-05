package dados;
import java.util.ArrayList;


public class OBV {
	
	private ArrayList<Integer> obv=new ArrayList<Integer>();
	
	public ArrayList<Integer> getOBV(ArrayList<Integer> volume,ArrayList<Double> prices){
		int currentobv = 0;
		for(int i=0;i<prices.size();i++){
			obv.add(0);
		}
		
		for(int i=0;i<prices.size()-1;i++){
			if(prices.get(i+1) > prices.get(i)){
				currentobv += volume.get(i+1);
				obv.set(i+1,currentobv);
			}else if(prices.get(i+1) < prices.get(i)){
				currentobv -= volume.get(i+1);
				obv.set(i+1,currentobv);
			}else{
				obv.set(i+1,currentobv);
			}
				
		}
		return obv;
	}
}
