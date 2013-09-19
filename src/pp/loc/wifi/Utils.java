package pp.loc.wifi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;

public class Utils {

	
	public static HashMap<MACAddress, GeoPosition> getAPPositionsFromFile(String apFilePath) {
		HashMap<MACAddress, GeoPosition> apMap = new HashMap<MACAddress, GeoPosition>();
		try {
			Scanner scanner = new Scanner(new FileInputStream(apFilePath));
		    while (scanner.hasNextLine()){
		        String[] line = scanner.nextLine().split("\\s+");
		        
		        if (!line[0].equals("#")) { // ignore the comments
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
