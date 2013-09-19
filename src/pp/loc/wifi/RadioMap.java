package pp.loc.wifi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;
import org.pi4.locutil.*;

public class RadioMap {
	/**
	 *  If a access point is not available in either the offline or online trace, this value will be used
	 */
	private int unhearableStrength = -100;
	
	/**
	 * If both offline and online strength is below this value, the AP will be ignored in the calculation
	 */
	private int unhearableLimit = -90;
	
	private ArrayList<AvgTraceEntry> entrySet;
	private ArrayList<MACAddress> macSet;
	
	public RadioMap(ArrayList<AvgTraceEntry> entrySet, int unhearableStrength, int unhearableLimit) {
		this.entrySet = entrySet;
		this.unhearableStrength = unhearableStrength;
		this.unhearableLimit = unhearableLimit;
		macSet = getUniqueMACAddresses(entrySet);
	}
	
	public RadioMap(ArrayList<AvgTraceEntry> entrySet, ArrayList<MACAddress> macSet, int unhearableStrength, int unhearableLimit) {
		this.entrySet = entrySet;
		this.unhearableStrength = unhearableStrength;
		this.unhearableLimit = unhearableLimit;
		this.macSet = macSet;
	}
	
	/**
	 * Use radiomap to calculate position of trace measurement
	 * @param k Use k nearest neighbors (k = 1 for simple NN)
	 * @param entry The measurement to find position of
	 * @return
	 */
	public GeoPosition calcFingerprintPosition(int k, AvgTraceEntry entry) {
		
		HashMap<GeoPosition,Double> map = new HashMap<GeoPosition,Double>();
		
		for (AvgTraceEntry knownEntry : entrySet) {
			map.put(knownEntry.getGeoPosition(), calcSignalDistance(entry, knownEntry));
		}
		
		// Sort by distance
		SignalDistanceComparator bvc =  new SignalDistanceComparator(map);
        TreeMap<GeoPosition,Double> sorted_map = new TreeMap<GeoPosition,Double>(bvc);
        sorted_map.putAll(map);
		
        if (k==1) {
        	return sorted_map.firstKey();
        } else {
        	ArrayList<GeoPosition> positions = new ArrayList<GeoPosition>();
        	for (int i=0;i<k;i++) {
        		positions.add( sorted_map.pollFirstEntry().getKey() );
        	}
        	return Statistics.avgPosition(positions);
        }
        
		
	}
	
	/**
	 * Calculate signal strength distance between two sample sets
	 * Uses the average signal strength for each AP
	 * Makes use of UNHEARBLE_STRENGTH and UNHEARABLE_LIMIT to account for AP reachability variations
	 * @param myPos The sample of where to find position of
	 * @param knownPos A known sample from radiomap
	 * @return signal strength distance
	 */
	private double calcSignalDistance(AvgTraceEntry myPos, AvgTraceEntry knownPos) {
		double sum = 0;
		
		for (MACAddress mac : macSet) {
			if (!myPos.containsKey(mac) && !knownPos.containsKey(mac)) continue;
			
			double knownVal = knownPos.containsKey(mac) ? knownPos.getAverageSignalStrength(mac) : unhearableStrength;
			double myVal = myPos.containsKey(mac) ? myPos.getAverageSignalStrength(mac) : unhearableStrength;
			
			if (knownVal < unhearableLimit && myVal < unhearableLimit) continue;
			
			sum += Math.pow( myVal - knownVal, 2);
		}
		
		return Math.sqrt(sum);
	}
	
	/**
	 * Get unique MACAddress in radiomap
	 * @param entries
	 * @return
	 */
	private ArrayList<MACAddress> getUniqueMACAddresses(ArrayList<AvgTraceEntry> entries) {
		ArrayList<MACAddress> macs = new ArrayList<MACAddress>();
		
		for (AvgTraceEntry entry : entries) {
			for (MACAddress mac : entry.getMacAddress()) {
				if (!macs.contains(mac))
					macs.add(mac);
			}
		}
		
		return macs;
	}
	
	
	/**
	 * This will return a reduce entrylist with only unique GeoPositions and all measurements hereunder
	 * @param entries List of TraceEntries to reduce
	 * @return ArrayList<TraceEntry> reduced list
	 */
	public static ArrayList<AvgTraceEntry> geoIndexTraces(List<TraceEntry> entries) {
		Map<GeoPosition, AvgTraceEntry> indexedEntries = new HashMap<GeoPosition, AvgTraceEntry>();
		
		for (TraceEntry entry : entries) {	
			if (indexedEntries.containsKey(entry.getGeoPosition())) {
				indexedEntries.get(entry.getGeoPosition())
					.add(entry.getSignalStrengthSamples());
			} else {
				indexedEntries.put(entry.getGeoPosition(), new AvgTraceEntry(entry));
			}	
		}
				
		return new ArrayList<AvgTraceEntry>(indexedEntries.values());
	}

	
	/**
	 * Comparator used to sort positions by signal strength distance
	 * @author jeppestougaard
	 *
	 */
	class SignalDistanceComparator implements Comparator<GeoPosition> {

	    Map<GeoPosition, Double> base;
	    public SignalDistanceComparator(Map<GeoPosition, Double> base) {
	        this.base = base;
	    }

	    @Override  
	    public int compare(GeoPosition a, GeoPosition b) {
	        if (base.get(a) >= base.get(b)) {
	            return 1;
	        } else {
	            return -1;
	        } // returning 0 would merge keys
	    }
	}
}
