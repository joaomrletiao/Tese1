package dados;
import java.util.ArrayList;
import java.util.List;

// Relative Strength Index
public class RSI {

        ArrayList<Double> rsi = new ArrayList<Double>();

        // Get Relative Strength Index
        public ArrayList<Double> getRSI(ArrayList<Double> prices, int period) {

        		ArrayList<Double> change = new ArrayList<Double>();
                ArrayList<Double> gain = new ArrayList<Double>();
                ArrayList<Double> loss = new ArrayList<Double>();
                ArrayList<Double> averageGain = new ArrayList<Double>();
                ArrayList<Double> averageLoss = new ArrayList<Double>();
                double gainSum = 0;
                double lossSum = 0;

                // Initialize
                for (int i = 0; i < prices.size(); i++) {
                        change.add(0.0);
                        gain.add(0.0);
                        loss.add(0.0);
                        averageGain.add(0.0);
                        averageLoss.add(0.0);
                        rsi.add(0.0);
                }

                // Calculate "change", "gain" and "loss"
                for (int i = 0; i < prices.size()-1; i++) {
                        change.set(i,prices.get(i+1) - prices.get(i));
                        if (change.get(i) > 0) {
                                gain.set(i, change.get(i));
                                loss.set(i, 0.0);
                        } else if (change.get(i) < 0) {
                                loss.set(i, change.get(i) * (-1.0));
                                gain.set(i, 0.0);
                        }
                }

                // Calculate first "average gain" and "average loss"
                for (int i = 0; i < period; i++) {
                        gainSum += gain.get(i);
                }
                for (int i = 0; i < loss.size(); i++) {
                        lossSum += loss.get(i);
                }
                averageGain.set(period, gainSum / ((double) period));
                averageLoss.set(period, lossSum / ((double) period));

                // Calculate subsequent "average gain" and "average loss" values
                for (int i = period+1; i < prices.size(); i++) {
                        averageGain.set(i, (averageGain.get(i - 1)
                                        * ((double) (period - 1)) + gain.get(i))
                                        / ((double) period));
                        averageLoss.set(i, (averageLoss.get(i - 1)
                                        * ((double) (period - 1)) + loss.get(i))
                                        / ((double) period));
                }

                // Calculate RSI
                double RS = 0; // Relative strength
                double RSIndex = 0; // Relative strength index
                for (int i = period; i < prices.size(); i++) {
                        RS = averageGain.get(i) / averageLoss.get(i);
                        RSIndex = 100 - 100 / (1 + RS);
                        rsi.set(i, RSIndex);
                }

          return rsi;
        }

}
