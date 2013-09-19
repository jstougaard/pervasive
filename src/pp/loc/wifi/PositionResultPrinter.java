package pp.loc.wifi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.PositioningError;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;

public class PositionResultPrinter {
	
	private TraceGenerator tg;
	
	/**
	 * Construct parsers for fingerprinter
	 * @param offlinePath
	 * @param onlinePath
	 * @param offlineSize
	 * @param onlineSize
	 */
	public PositionResultPrinter()  throws IOException, NumberFormatException {
		
		//Construct parsers
		File offlineFile = new File(BasicSetup.offlinePath);
		Parser offlineParser = new Parser(offlineFile);
		System.out.println("Offline File: " +  offlineFile.getAbsoluteFile());
		
		File onlineFile = new File(BasicSetup.onlinePath);
		Parser onlineParser = new Parser(onlineFile);
		System.out.println("Online File: " + onlineFile.getAbsoluteFile());
		
		//Generate traces from parsed files
		tg = new TraceGenerator(offlineParser, onlineParser, BasicSetup.offlineSize, BasicSetup.onlineSize);
	}
	
	/**
	 * Run fingerprinting with provided K value
	 * Save results to specified resultPath
	 * @param k
	 * @param resultPath
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void run(RadioMapGenerator mapGenerator, int k, String resultPath)  throws IOException, NumberFormatException {
		// Create file 
		System.out.println("Saving results to: " + resultPath);
		FileWriter fstream = new FileWriter(resultPath);
		BufferedWriter out = new BufferedWriter(fstream);
		String newLine = System.getProperty("line.separator");
		
		
		for (int i = 0; i<BasicSetup.calculationIterations;i++) {
			System.out.println("Process: "+ i + " / " + BasicSetup.calculationIterations);
			refreshTraces();
			
			RadioMap radiomap = mapGenerator.generateRadioMap( getOfflineSet() );
			
			ArrayList<AvgTraceEntry> onlineSet = RadioMap.geoIndexTraces( getOnlineSet() );
			
			out.write("#"+newLine);
			out.write("# " + "Position estimates with k="+ k + "; Iteration #" + (i+1) + newLine);
			out.write("#"+newLine);
			
			for (AvgTraceEntry entry : onlineSet) {
				GeoPosition estPosition = radiomap.calcFingerprintPosition(k, entry);
				PositioningError e = new PositioningError(entry.getGeoPosition(), estPosition);
				out.write( e.toString() + newLine );
	
			}

		}
		
		//Close the output stream
		out.close();
		
		System.out.println("Results saved");
	}
	
	public double calcAverageMedianDistance(RadioMapGenerator mapGenerator, int k) {
		double total = 0;
		int count = 0;
		
		for (int i = 0; i<BasicSetup.calculationIterations;i++) {
			
			refreshTraces();
			
			RadioMap radiomap = mapGenerator.generateRadioMap( getOfflineSet() );
			
			ArrayList<AvgTraceEntry> onlineSet = RadioMap.geoIndexTraces( getOnlineSet() );
			double[] distances = new double[onlineSet.size()];
			int j = 0;
			for (AvgTraceEntry entry : onlineSet) {
				GeoPosition estPosition = radiomap.calcFingerprintPosition(k, entry);
				distances[j] = entry.getGeoPosition().distance( estPosition );
				j++;
			}
			
			total += Statistics.median(distances);
			count++;
		}
		
		return total/count;
	}
	
	public void refreshTraces() {
		tg.generate();
	}
	
	public ArrayList<TraceEntry> getOfflineSet() {
		return tg.getOffline();
	}
	
	public ArrayList<TraceEntry> getOnlineSet() {
		return tg.getOnline();
	}
	
}
