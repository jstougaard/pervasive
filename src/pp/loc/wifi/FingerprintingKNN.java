package pp.loc.wifi;

import java.io.IOException;

public class FingerprintingKNN {
	
	/**
	 * Execute K Nearest Neighbor fingerprinting 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running FingerprintingKNN...");
		
		if (args.length < 1) {
			System.out.println("USAGE: Please provide a K-size as parameter to this application");
			return;
		}
		
		int kSize = Integer.parseInt(args[0]);
		String resultPath = "results/fingerprint_KNN";
		
		
		
		try {
			FingerPrinter fp = new FingerPrinter("data/MU.1.5meters.offline.trace", "data/MU.1.5meters.online.trace", 25, 5);
			fp.run(kSize, resultPath);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
