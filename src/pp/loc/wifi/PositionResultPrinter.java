package pp.loc.wifi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.PositioningError;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;

public class PositionResultPrinter {
	
	TraceGenerator tg;
	RadioMapGenerator mapGenerator;
	
	/**
	 * Construct parsers for fingerprinter
	 * @param offlinePath
	 * @param onlinePath
	 * @param offlineSize
	 * @param onlineSize
	 */
	public PositionResultPrinter(RadioMapGenerator mapGenerator)  throws IOException, NumberFormatException {
		
		//Construct parsers
		File offlineFile = new File(BasicSetup.offlinePath);
		Parser offlineParser = new Parser(offlineFile);
		System.out.println("Offline File: " +  offlineFile.getAbsoluteFile());
		
		File onlineFile = new File(BasicSetup.onlinePath);
		Parser onlineParser = new Parser(onlineFile);
		System.out.println("Online File: " + onlineFile.getAbsoluteFile());
		
		//Generate traces from parsed files
		tg = new TraceGenerator(offlineParser, onlineParser, BasicSetup.offlineSize, BasicSetup.onlineSize);
		this.mapGenerator = mapGenerator;
	}
	
	/**
	 * Run fingerprinting with provided K value
	 * Save results to specified resultPath
	 * @param k
	 * @param resultPath
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void run(int k, String resultPath)  throws IOException, NumberFormatException {
		// Create file 
		System.out.println("Saving results to: " + resultPath);
		FileWriter fstream = new FileWriter(resultPath);
		BufferedWriter out = new BufferedWriter(fstream);
		String newLine = System.getProperty("line.separator");
		
		
		refreshTraces();
		
		RadioMap radiomap = mapGenerator.generateRadioMap( getOfflineSet() );
		
		ArrayList<TraceEntry> onlineSet = RadioMap.geoIndexTraces( getOnlineSet() );
		
		for (TraceEntry entry : onlineSet) {
			GeoPosition estPosition = radiomap.calcFingerprintPosition(k, entry);
			PositioningError e = new PositioningError(entry.getGeoPosition(), estPosition);
			out.write( e.toString() + newLine );

		}
		
		//Close the output stream
		out.close();
		
		System.out.println("Results saved");
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
