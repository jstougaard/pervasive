package pp.loc.wifi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CalcMedianDistance {

	public static void main(String[] args) {
		
		System.out.println("Running CalcMedianDistance...");
		int maxK = 5;
		
		try {
			
			PositionResultPrinter printer = new PositionResultPrinter();
			RadioMapGenerator simpleMapGenerator = new FingerprintRadioMapGenerator();
			RadioMapGenerator modelMapGenerator = new ModelRadioMapGenerator("data/MU.AP.positions", -33.77, 3.415, 1);
			
			// Save to CSV
			String toFilename = "results/average_median_distance.csv";
			String newLine = System.getProperty("line.separator");
			String delimiter = ";";
			
		    System.out.println("Saving results to: " + toFilename);
			FileWriter fstream = new FileWriter(toFilename);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write( "\"K-Value\"" + delimiter + "\"FingerprintKNN\"" + delimiter + "ModelKNN" + newLine);
			
			for (int k=1;k<=maxK;k++) {
				double fingerprintKNN = printer.calcAverageMedianDistance(simpleMapGenerator, k);
				double modelKNN = printer.calcAverageMedianDistance(modelMapGenerator, k);
				String line = k+ delimiter + fingerprintKNN + delimiter + modelKNN;
				System.out.println(line);
				out.write(line + newLine);
			}
			
			out.close();
			
			System.out.println("Results saved");
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
