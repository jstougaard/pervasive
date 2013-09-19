package pp.loc.wifi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;

public class CalcSignalStrenghDistanceRelation {

	public static void main(String[] args) {
		
		try {
			
			//Generate traces from parsed files
			PositionResultPrinter printer = new PositionResultPrinter( );
			printer.refreshTraces();
			ArrayList<AvgTraceEntry> entries = RadioMap.geoIndexTraces( printer.getOfflineSet() );


			
			// Get Access Points
			HashMap<MACAddress, GeoPosition> apPositions = Utils.getAPPositionsFromFile("data/MU.AP.positions");
			
			// Save to new file
			String toFilename = "results/distance_signalstrength_plot.csv";
			String newLine = System.getProperty("line.separator");
			String delimiter = ";";
			
		    System.out.println("Saving results to: " + toFilename);
			FileWriter fstream = new FileWriter(toFilename);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write( "\"Distance\"" + delimiter + "\"Signal Strength\"" + newLine);
			
			for (AvgTraceEntry entry : entries) {
				for (MACAddress mac : apPositions.keySet()) {
					if (!entry.containsKey(mac)) continue;
					
					double distance = entry.getGeoPosition().distance( apPositions.get(mac) );
					out.write( distance + delimiter + entry.getAverageSignalStrength(mac) + newLine );
				}
				
			}
			
			//Close the output stream
			out.close();
			
			System.out.println("Results saved");
			
			
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
