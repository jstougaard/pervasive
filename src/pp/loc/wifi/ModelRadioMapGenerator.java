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
	
	public static final int UNHEARABLE_STRENGTH = -200;
	public static final int UNHEARABLE_LIMIT = -200;
	
	private HashMap<MACAddress, GeoPosition> apPositions;
	private ArrayList<MACAddress> macSet;
	
	private double pd0;
	private double n;
	private double d0;
	
	public ModelRadioMapGenerator(String apPositionPath, double pd0, double n, double d0) {
		this.pd0 = pd0;
		this.n = n;
		this.d0 = d0;
		
		apPositions = Utils.getAPPositionsFromFile(apPositionPath);
		macSet = new ArrayList<MACAddress>( apPositions.keySet() );
		
	}

	@Override
	public RadioMap generateRadioMap(ArrayList<TraceEntry> offlineSet) {

		Map<GeoPosition, AvgTraceEntry> modelEntries = new HashMap<GeoPosition, AvgTraceEntry>();
		
		for (TraceEntry entry : offlineSet) {
			
			// Do not recalculate previous positions
			if (modelEntries.containsKey(entry.getGeoPosition())) continue;
			
			AvgTraceEntry newEntry = new AvgTraceEntry(entry.getGeoPosition());
			
			for (MACAddress mac : macSet) {
				double distance = entry.getGeoPosition().distance(apPositions.get(mac));
				
				double strength = pd0  - 10 * n * Math.log(distance / d0);
				newEntry.put(mac, strength);

			}
			
			modelEntries.put(entry.getGeoPosition(), newEntry);
		}
		
		return new RadioMap(new ArrayList<AvgTraceEntry>(modelEntries.values()), macSet, UNHEARABLE_STRENGTH, UNHEARABLE_LIMIT );
	}
	
	
}
