package pp.loc.wifi;

import java.util.ArrayList;

import org.pi4.locutil.trace.TraceEntry;

public interface RadioMapGenerator {
	
	public RadioMap generateRadioMap(ArrayList<TraceEntry> offlineSet);

}
