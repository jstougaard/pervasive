package pp.loc.wifi;

import java.util.ArrayList;

import org.pi4.locutil.trace.TraceEntry;

public interface RadioMapGenerator {
	
	public RadioMap generateRadioMap(ArrayList<TraceEntry> offlineSet);
	
	public String getOfflinePath();
	
	public int getOfflineSize();
	
	public String getOnlinePath();
	
	public int getOnlineSize();
}
