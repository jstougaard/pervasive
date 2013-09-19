package pp.loc.wifi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;
import org.pi4.locutil.trace.macfilter.MacFilter;
import org.pi4.locutil.trace.macfilter.MacFilterExplizit;

public class ModelKNN {
	
	/**
	 * Execute ModelKNN 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running ModelKNN...");
		
		if (args.length < 1) {
			System.out.println("USAGE: Please provide a K-size as parameter to this application");
			return;
		}
		
		int kSize = Integer.parseInt(args[0]);
		String resultsPath = "results/model_KNN";
		
		try {
			
			PositionResultPrinter printer = new PositionResultPrinter( new ModelRadioMapGenerator("data/MU.AP.positions", -33.77, 3.415, 1) );
			printer.run(kSize, resultsPath);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
		
		
		
	}
	
	
}
