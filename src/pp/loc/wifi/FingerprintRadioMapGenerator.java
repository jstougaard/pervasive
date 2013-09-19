package pp.loc.wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.trace.TraceEntry;

public class FingerprintRadioMapGenerator implements RadioMapGenerator {

	@Override
	public RadioMap generateRadioMap(ArrayList<TraceEntry> offlineSet) {
		return new RadioMap( RadioMap.geoIndexTraces( offlineSet ) );
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
	
	
}
