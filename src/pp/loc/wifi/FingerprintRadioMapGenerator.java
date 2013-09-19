package pp.loc.wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class FingerprintRadioMapGenerator implements RadioMapGenerator {

	public static final int UNHEARABLE_STRENGTH = -100;
	public static final int UNHEARABLE_LIMIT = -80;
	
	@Override
	public RadioMap generateRadioMap(ArrayList<TraceEntry> offlineSet) {
		return new RadioMap( RadioMap.geoIndexTraces( offlineSet ), UNHEARABLE_STRENGTH, UNHEARABLE_LIMIT );
	}
	
}
