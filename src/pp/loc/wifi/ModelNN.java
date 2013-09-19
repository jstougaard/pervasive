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

public class ModelNN {
	
	/**
	 * Execute ModelNN 
	 * @param args
	 */

	
	public static void main(String[] args) {
		System.out.println("Running ModelNN...");
		
		String resultsPath = "results/model_NN";
		int kSize = 1;
		
		try {
			
			PositionResultPrinter printer = new PositionResultPrinter( );
			RadioMapGenerator mapGenerator = new ModelRadioMapGenerator("data/MU.AP.positions", -33.77, 3.415, 1);
			printer.run(mapGenerator, kSize, resultsPath);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
}
