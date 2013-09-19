package pp.loc.wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

public class AvgTraceEntry {
	
	private GeoPosition position;
	private HashMap<MACAddress, SignalStrengthAvg> signalAvg;
	
	public AvgTraceEntry(GeoPosition position) {
		this.position = position;
		signalAvg = new HashMap<MACAddress, SignalStrengthAvg>();
	}
	
	public AvgTraceEntry (TraceEntry entry) {
		this(entry.getGeoPosition());
		add(entry.getSignalStrengthSamples());
	}
	
	public void add(SignalStrengthSamples samples) {
		Iterator<MACAddress> it = samples.keySet().iterator();
		while (it.hasNext()) {
			MACAddress mac = it.next();
			
			if (!signalAvg.containsKey(mac)) {
				signalAvg.put(mac, new SignalStrengthAvg());
			}
			
			Iterator<Double> it2 = samples.iterator(mac);

			while (it2.hasNext()) {
				signalAvg.get(mac).add(it2.next());
			}
			
		}
	}
	
	public GeoPosition getGeoPosition() {
		return position;
	}
	
	public ArrayList<MACAddress> getMacAddress() {
		return new ArrayList<MACAddress>(signalAvg.keySet());
	}
	
	public boolean containsKey(MACAddress mac) {
		return signalAvg.containsKey(mac);
	}
	
	public void put(MACAddress mac, double signalStrength) {	
		if (!signalAvg.containsKey(mac)) {
			SignalStrengthAvg avg = new SignalStrengthAvg();
			avg.add(signalStrength);
			signalAvg.put(mac, avg);
		} else {
			signalAvg.get(mac).add(signalStrength);
		}
	}
	
	public double getAverageSignalStrength(MACAddress mac) {
		return signalAvg.containsKey(mac) ? signalAvg.get(mac).getAvg() : 0;
	}
	
	class SignalStrengthAvg {
		
		private double total = 0;
		private int count = 0;
		private double avgValue = 0;
		
		
		public void add(double addValue) {
			count++;
			total += addValue;
			avgValue = total / count;
		}
		
		public double getAvg() {
			return avgValue;
		}
	}

}
