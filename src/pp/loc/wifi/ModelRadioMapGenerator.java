package pp.loc.wifi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.TraceEntry;

public class ModelRadioMapGenerator implements RadioMapGenerator {
	
	private HashMap<MACAddress, GeoPosition> apPositions;
	private ArrayList<MACAddress> macSet;
	
	private double pd0;
	private double n;
	private double d0;
	
	public ModelRadioMapGenerator(double pd0, double n, double d0) {
		this.pd0 = pd0;
		this.n = n;
		this.d0 = d0;
		
		apPositions = getAPPositions("data/MU.AP.positions");
		macSet = new ArrayList<MACAddress>( apPositions.keySet() );
		
	}

	@Override
	public RadioMap generateRadioMap(ArrayList<TraceEntry> offlineSet) {

		Map<GeoPosition, TraceEntry> modelEntries = new HashMap<GeoPosition, TraceEntry>();
		
		for (TraceEntry entry : offlineSet) {
			
			// Do not recalculate previous positions
			if (modelEntries.containsKey(entry.getGeoPosition())) continue;
			
			TraceEntry newEntry = new TraceEntry();
			newEntry.setGeoPosition( entry.getGeoPosition() );
			
			for (MACAddress mac : macSet) {
				double distance = entry.getGeoPosition().distance(apPositions.get(mac));
				
				double strength = pd0  - 10 * n * Math.log(distance / d0);
				newEntry.getSignalStrengthSamples().put(mac, strength);

			}
			
			modelEntries.put(entry.getGeoPosition(), newEntry);
		}
		
		return new RadioMap(new ArrayList<TraceEntry>(modelEntries.values()), macSet);
	}

	@Override
	public String getOfflinePath() {
		return "data/MU.1.5meters.offline.trace";
	}

	@Override
	public int getOfflineSize() {
		return 25;
	}

	@Override
	public String getOnlinePath() {
		return "data/MU.1.5meters.online.trace";
	}

	@Override
	public int getOnlineSize() {
		return 5;
	}
	
	private HashMap<MACAddress, GeoPosition> getAPPositions(String apFilePath) {
		HashMap<MACAddress, GeoPosition> apMap = new HashMap<MACAddress, GeoPosition>();
		try {
			Scanner scanner = new Scanner(new FileInputStream(apFilePath));
		    while (scanner.hasNextLine()){
		        String[] line = scanner.nextLine().split("\\s+");
		        
		        if (line[0].equals("#")) // ignore the comment about the origins of the file as valid data.
		        {}
		        else{
		            MACAddress tempMacAdd = MACAddress.parse(line[0]);
			        GeoPosition tempGeoPos = new GeoPosition(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
				       
			        apMap.put(tempMacAdd, tempGeoPos); 
		        }
		    }
		    scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return apMap;
	}
}
